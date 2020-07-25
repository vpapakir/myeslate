package gr.cti.eslate.steering;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.protocol.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements a steering control component. This component has
 * a composite arrow showing the eight compass points, and a large arrowhead.
 * By clicking on one of the eight compass points, the user instructs the
 * connected component to turn to that direction, and by clicking on the large
 * arrowhead, the user instructs the connected component to start moving in
 * that direction.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI><B>Direction.</B> This is a single output plug associated with a
 * <A HREF="gr.cti.eslate.sharedObject.Direction.html">Direction</A> shared
 * object (to specify the direction) and a
 * <A
 * HREF="gr.cti.eslate.protocol.SteeringControlInterface.html">SteeringControlInterface</A>
 * interface (to instruct the connected component to start moving.
 * The plug's color is Color.blue.
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>SETSTEERINGDIRECTION direction</B> Turn to the specified direction.
 * Direction can be one of N, NE, E, SE, S, SW, W, NW or their localized
 * equivalents.</LI>
 * <LI><B>STEERINGDIRECTION</B> Return the current direction.</LI>
 * <LI><B>STEERINGGO</B> Start moving.</LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.protocol.SteeringControlInterface
 * @see         gr.cti.eslate.sharedObject.Direction
 */
public class Steering extends JPanel
  implements ESlatePart, Externalizable, Serializable, AsSteering
{
  private ESlateHandle handle;
  private JPanel pane = null;
  private ArrowPanel arrowPane;
  private ArrowPicButton goButton;
  private Direction dir;
  static ImageIcon N = null, NE, E, SE, S, SW, W, NW;
  private static Integer lockPictureDownloading = new Integer(0);
  private SingleOutputProtocolPlug plug;
  private boolean needMenuBar;

  final static int edgeSize  = 40;
  final static Dimension frameSize = new Dimension(edgeSize, edgeSize);
  private final static Dimension winSize = new Dimension(96, 56);

  private ResourceBundle resources;
  private final static String version = "2.0.1";
  private final static int saveVersion = 1;

  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  PerformanceTimer saveTimer;
  /**
   * Timer which measures the time required for the construction of the
   * component.
   */
  PerformanceTimer constructorTimer;
  /**
   * Timer which measures the time required for initializing the E-Slate
   * aspect of the component.
   */
  PerformanceTimer initESlateAspectTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  private final static String DIRECTION = "direction";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  static final long serialVersionUID = 6205209853392872296L;

  /**
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  private ESlateInfo getInfo()
  {
    String[] info = {
      resources.getString("credits1"),
      resources.getString("credits2"),
      resources.getString("credits3"),
    };
    return new ESlateInfo(
      resources.getString("componentName") + ", " +
        resources.getString("version") + " " + version,
      info);
  }

  /**
   * Create a steering control component.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public Steering(boolean needMenuBar)
  {
    super();
    initialize(needMenuBar);
  }

  /**
   * Create a steering control component. The menu bar will be visible.
   */
  public Steering()
  {
    this(false);
  }

  /**
   * Initialize component and create component GUI.
   * @param     needMenuBar     Specifies whether to put the E-Slate menu bar
   *                            at the top of the component.
   */
  private void initialize(boolean needMenuBar)
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.steering.SteeringResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    synchronized (lockPictureDownloading) {
      if (N == null) {
        N  = new ImageIcon(this.getClass().getResource("n.gif"));
        NE = new ImageIcon(this.getClass().getResource("ne.gif"));
        E  = new ImageIcon(this.getClass().getResource("e.gif"));
        SE = new ImageIcon(this.getClass().getResource("se.gif"));
        S  = new ImageIcon(this.getClass().getResource("s.gif"));
        SW = new ImageIcon(this.getClass().getResource("sw.gif"));
        W  = new ImageIcon(this.getClass().getResource("w.gif"));
        NW = new ImageIcon(this.getClass().getResource("nw.gif"));
      }
    }

    pane = new JPanel(true);
    //pane.setForeground(Color.black);
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    setLayout(new BorderLayout());

    Box row = Box.createHorizontalBox();
    arrowPane = new ArrowPanel(this);
    goButton = new ArrowPicButton(this);

    row.add(arrowPane);
    row.add(Box.createHorizontalStrut(16));
    row.add(goButton);

    pane.add(row, BorderLayout.CENTER);

    add(pane, BorderLayout.CENTER);

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The requested handle. If the component's constructor has not
   *            been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      handle = ESlate.registerPart(this);

      initESlate();

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, handle, "", "ms");
    }
    return handle;
  }

  /**
   * Initializes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    //handle = ESlate.registerPart(this);

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.SteeringPrimitives"
    );

    handle.setInfo(getInfo());

    dir = new Direction(handle);

    try {
      plug = new SingleOutputProtocolPlug(
              handle, resources, "direction", Color.blue,
              gr.cti.eslate.sharedObject.Direction.class, dir,
              gr.cti.eslate.protocol.SteeringControlInterface.class);
      handle.addPlug(plug);
    } catch (NoClassDefFoundError e) {
      // SteeringControlInterface not present in CLASSPATH. Presumably this
      // means that no matching component is available, so we simply
      // ignore the error and do not add the plug. 
    } catch (InvalidPlugParametersException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    } catch (PlugExistsException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }

    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }

    if (needMenuBar) {
      add(handle.getMenuPanel(), BorderLayout.NORTH);
    }
    handle.addESlateListener( new ESlateAdapter() {
      public void handleDisposed(HandleDisposalEvent e)
      {
        dispose();
      }
    });
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 1);
    map.put(DIRECTION, dir.getDirection());
    map.put(MINIMUM_SIZE, getMinimumSize());
    map.put(MAXIMUM_SIZE, getMaximumSize());
    map.put(PREFERRED_SIZE, getPreferredSize());

    oo.writeObject(map);

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    int newDir;
    try {
      StorageStructure map = (StorageStructure)(oi.readObject());
      newDir = map.get(DIRECTION, Direction.N);
      setMinimumSize(map.get(MINIMUM_SIZE, Steering.winSize));
      setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(map.get(PREFERRED_SIZE, Steering.winSize));
    } catch (OptionalDataException ode) {
      newDir = oi.readInt();
    }
    goButton.setDir(newDir);
    dir.setDirection(newDir);

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }

  /**
   * Run the component as an application--useful for debugging.
   * @param     args    Application's arguments (not used).
   */
  public static void main(String[] args)
  {
    MainFrame f = new MainFrame("", Steering.winSize);

    Steering st = new Steering();

    f.add("Center", st);
    f.pack();

    f.setTitle(st.resources.getString("name"));

    f.setVisible(true);
  }

  /**
   * Set a new direction.
   * @param     newDir  The direction to set. One of Direction.N,
   *                    Direction.NE, Direction.E, Direction.SE, Direction.S,
   *                    Direction.SW, Direction.W, Direction.NW.
   */
  public void setDirection(int newDir)
  {
    goButton.setDir(newDir);
    dir.setDirection(newDir);
  }

  /**
   * Return the current direction.
   * @return    One of Direction.N, Direction.NE, Direction.E, Direction.SE,
   *            Direction.S, Direction.SW, Direction.W, Direction.NW.
   */
  public int getDirection()
  {
    return dir.getDirection();
  }

  /**
   * Start moving.
   */
  public void go()
  {
    try {
      IProtocolPlug p = (IProtocolPlug)(plug.getProtocolPlug());
      SteeringControlInterface sci =
        (SteeringControlInterface)(p.getProtocolImplementor());
      sci.go(); 
    } catch (NoSingleConnectedComponentException e) {
    }
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    if (handle != null) {
      handle.dispose();
      handle = null;
    }
    pane = null;
    arrowPane = null;
    goButton = null;
    dir = null;
    plug = null;
    resources = null;
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

  /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm)
  {
    if (perfListener == null) {
      perfListener = new PerformanceAdapter() {
        public void performanceManagerStateChanged(PropertyChangeEvent e)
        {
          boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
          // When the Performance Manager is enabled, try to attach the
          // timers.
          if (enabled) {
            attachTimers();
          }
        }
      };
      pm.addPerformanceListener(perfListener);
    }
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  private void attachTimers()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    boolean pmEnabled = pm.isEnabled();

    // If the performance manager is disabled, install a listener which will
    // re-invoke this method when the performance manager is enabled.
    if (!pmEnabled && (perfListener == null)) {
      createPerformanceManagerListener(pm);
    }

    // Do nothing if the PerformanceManager is disabled.
    if (!pmEnabled) {
      return;
    }

    boolean timersCreated = (loadTimer != null);
    // If the component's timers have not been constructed yet, then
    // construct them. During construction, the timers are also attached.
    if (!timersCreated) {
      // Get the performance timer group for this component.
      PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
      // Construct and attach the component's timers.
      constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("InitESlateAspectTimer"), true
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.CONSTRUCTOR, constructorTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.LOAD_STATE, loadTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.SAVE_STATE, saveTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
      );
    }
  }

}

/**
 * This class implements the panel where the composite arrow of the
 * steering control component is drawn.
 *
 * @author      Kriton Kyrimis
 * @version     1.5.13, 4-Aug-1999
 * @see         gr.cti.eslate.steering.Steering
 */
class ArrowPanel extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The steering component to which the panel belongs;
   */
  private Steering steering;

  /**
   * Create an ArrowPanel.
   * @param     steering        The steering component to which the panel
   *                            belongs.
   */
  public ArrowPanel(Steering steering)
  {
    super(true);

    setOpaque(false);
    setSize(Steering.frameSize);
    setPreferredSize(Steering.frameSize);
    setMaximumSize(Steering.frameSize);

    this.steering = steering;

    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        setDirection(e);
      }
    });

    addMouseMotionListener(new MouseMotionAdapter()
    {
      @Override
      public void mouseDragged(MouseEvent e)
      {
        setDirection(e);
      }
    });
  }

  /**
   * Process mouse events, setting the direction to the one specified by
   * the user.
   * @param   me      The event generated when the user clicks or drags the
   *                  mouse.
   */
  private void setDirection(MouseEvent me)
  {
    double x = (double)(me.getX() - Steering.edgeSize/2);
    double y = (double)(me.getY() - Steering.edgeSize/2);
    double angle = Math.atan2(x, y) * 180.0 / Math.PI;
    int newDir;

    if (angle > -22.5 && angle <= 22.5) {
      newDir = Direction.S;
    }else{
      if (angle > 22.5 && angle <= 67.5) {
        newDir = Direction.SE;
      }else{
        if (angle > 67.5 && angle <= 112.5) {
          newDir = Direction.E;
        }else{
          if (angle > 112.5 && angle <= 157.5) {
            newDir = Direction.NE;
          }else{
            if (angle > -157.5 && angle <= -112.5) {
              newDir = Direction.NW;
            }else{
              if (angle > -112.5  && angle <= -67.5) {
                newDir = Direction.W;
              }else{
                if (angle > -67.5 && angle <= -22.5) {
                  newDir = Direction.SW;
                }else{
                  newDir = Direction.N;
                }
              }
            }
          }
        }
      }
    }
    steering.setDirection(newDir);
  }

  /**
   * Paint the ArrowPanel.
   * @param     g       ArrowPanel's graphics context.
   */
  public void paint(Graphics g)
  {
    g.translate(Steering.edgeSize/2, Steering.edgeSize/2);

    int length = Steering.edgeSize/2 - 1;
    int diaglength = (int) ((double)length * Math.sin(Math.PI/4.0));
    int arrowlength = 5;
    int diagarrowlength = (int) ((double)arrowlength * Math.sin(Math.PI/4.0));

    g.setColor(Color.blue);

    //N
    g.drawLine(0, 0, 0, -length);
    g.drawLine(0, -length, diagarrowlength, -length + diagarrowlength);
    g.drawLine(0, -length, -diagarrowlength, -length + diagarrowlength);

    //NE
    g.drawLine(0, 0, diaglength, -diaglength);
    g.drawLine(diaglength, -diaglength, diaglength, -diaglength + arrowlength);
    g.drawLine(diaglength, -diaglength, diaglength - arrowlength, -diaglength);

    //E
    g.drawLine(0, 0, length, 0);
    g.drawLine(length, 0, length - diagarrowlength, diagarrowlength);
    g.drawLine(length, 0, length - diagarrowlength, -diagarrowlength);

    //SE
    g.drawLine(0, 0, diaglength, diaglength);
    g.drawLine(diaglength, diaglength, diaglength - arrowlength, diaglength);
    g.drawLine(diaglength, diaglength, diaglength, diaglength - arrowlength);

    //S
    g.drawLine(0, 0, 0, length);
    g.drawLine(0, length, diagarrowlength, length - diagarrowlength);
    g.drawLine(0, length, -diagarrowlength, length - diagarrowlength);

    //SW
    g.drawLine(0, 0, -diaglength, diaglength);
    g.drawLine(-diaglength, diaglength, -diaglength, diaglength -arrowlength);
    g.drawLine(-diaglength, diaglength, -diaglength + arrowlength, diaglength);

    //W
    g.drawLine(0, 0, -length, 0);
    g.drawLine(-length, 0, -length +diagarrowlength, diagarrowlength);
    g.drawLine(-length, 0, -length +diagarrowlength, -diagarrowlength);

    //NW
    g.drawLine(0, 0, -diaglength, -diaglength);
    g.drawLine(
      -diaglength, -diaglength, -diaglength, -diaglength + arrowlength
    );
    g.drawLine(
      -diaglength, -diaglength, -diaglength + arrowlength, -diaglength
    );
  }

}

/**
 * This class implements the button where the large arrowhead of the
 * steering control component is drawn.
 *
 * @see         gr.cti.eslate.steering.Steering
 */
class ArrowPicButton extends JButton
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The steering component to which the button belongs.
   */
  private Steering steering;
  
  /**
   * Create an ArrowPicButtonn.
   * @param     steering        The steering component to which the button
   *                            belongs.
   */
  public ArrowPicButton(Steering steering)
  {
    super();
    //setOpaque(false);
    //setSize(Steering.frameSize);
    //setPreferredSize(Steering.frameSize);
    //setMaximumSize(Steering.frameSize);

    this.steering = steering;
    setDir(Direction.N);
    setMargin(new Insets(0, 0, 0,0));

    addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ArrowPicButton.this.steering.go();
      }
    });
  }

  /**
   * Make the arrowhead face a given direction.
   * @param     dir     The direction.
   */
  public void setDir(int dir)
  {
    switch (dir) {
      case Direction.N:
        setIcon(Steering.N);
        break;
      case Direction.NE:
        setIcon(Steering.NE);
        break;
      case Direction.E:
        setIcon(Steering.E);
        break;
      case Direction.SE:
        setIcon(Steering.SE);
        break;
      case Direction.S:
        setIcon(Steering.S);
        break;
      case Direction.SW:
        setIcon(Steering.SW);
        break;
      case Direction.W:
        setIcon(Steering.W);
        break;
      case Direction.NW:
        setIcon(Steering.NW);
        break;
    }
  }

}
