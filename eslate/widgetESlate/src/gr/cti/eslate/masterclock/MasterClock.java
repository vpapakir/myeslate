package gr.cti.eslate.masterclock;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.scripting.*;

/**
 * This class implements a master clock component which can be used to control
 * simulations. The function of the master clock is to send "ticks" to all
 * components that are connected to it. Each tick contains information about
 * how much time in microseconds has elapsed since the previous tick.
 * Connected components should process each tick <EM>synchronously</EM>,
 * by performing whatever actions correspond to the specified time.
 * <P>
 * The component's GUI has start and stop buttons, and a "time scale"
 * slider which allows the user to input a value by which ticks are
 * multiplied, making simulation time run faster than real time.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI><B>Tick.</B> This is a multiple output plug associated with a
 * <A HREF="gr.cti.eslate.sharedObject.Tick.html">Tick</A> shared object.
 * The plug's color is Color.yellow.
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>STARTTICK</B> Start sending ticks.</LI>
 * <LI><B>STOPTICK</B> Stop sending ticks.</LI>
 * <LI><B>MASTERCLOCKMINIMUMSCALE</B> Returns the minimum value of the time
 * scale slider.</LI>
 * <LI><B>SETMASTERCLOCKMINIMUMSCALE scale</B> Sets the minimum value of the
 * time scale slider.</LI>
 * <LI><B>MASTERCLOCKMAXIMUMSCALE</B> Returns the maximum value of the time
 * scale slider.</LI>
 * <LI><B>SETMASTERCLOCKMAXIMUMSCALE scale</B> Sets the maximum value of the
 * time scale slider.</LI>
 * <LI><B>MASTERCLOCKSCALE</B> Returns the value of the time scale slider.</LI>
 * <LI><B>SETMASTERCLOCKSCALE scale</B> Sets the value of the time scale
 * slider.</LI>
 * <LI><B>MASTERCLOCKRUNNING</B> Checks whether the master clock is
 * running.</LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @author      Thanasis Mantes
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.sharedObject.Tick
 */
public class MasterClock extends JPanel
  implements ESlatePart, ActionListener, ChangeListener, Runnable,
             Externalizable, Serializable, AsMasterClock
{
  private ESlateHandle handle;
  private Tick tick;
  private Runnable send = null;
  //private Runnable suspendSend = null;
  private long tik;
  private volatile boolean threadRunning;
  private volatile Thread tt;
  private double timeScale;
  private double scale;
  private double leftScale;
  private double rightScale;
  private boolean saveState;
  private boolean needMenuBar;

  private JPanel pane = null;
  private JToggleButton startButton, stopButton;
  private JSlider scaleSlider;
  //private Dimension paneSize;
  private JTextField leftField, rightField, legendField;
  private JLabel legendLabel;
  private final static int hspace = 4;
  private final static int vspace = 4;
  private final static int prefCols = 5;
  private final static double initValue = 1.0d;
  private final static double defaultMinValue = 1.0d;
  private final static double defaultMaxValue = 100.0d;
  private double minValue;
  private double maxValue;
  private final static Dimension winSize = new Dimension(214, 114);
  private ResourceBundle resources;
  private final static String version = "2.0.3";
  private final static int saveVersion = 1;
  private NumberFormat nf;
  private FocusListener leftFocusListener = null;
  private FocusListener rightFocusListener = null;
  private FocusListener legendFocusListener = null;
  /**
   * Default sleep interval.
   */
  private final static long DEFAULT_SLEEP_INTERVAL = 10L;
  /**
   * Milliseconds to sleep after processing each tick.
   */
  private long sleepInterval = DEFAULT_SLEEP_INTERVAL;

  private boolean interrupted = true;
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

  private final static String MINVALUE = "minValue";
  private final static String MAXVALUE = "maxValue";
  private final static String TIMESCALE = "timeScale";
  private final static String SAVESTATE = "saveState";
  private final static String ISSTARTED = "isStarted";
  private final static String ISRUNNING = "isRunning";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  private final static String SLEEP_INTERVAL = "sleepInterval";
  static final long serialVersionUID = 5107186470784578689L;

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
   * Create a master clock component.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public MasterClock(boolean needMenuBar)
  {
    super();
    initialize(needMenuBar);
  }

  /**
   * Create a master clock component. The menu bar will be visible.
   */
  public MasterClock()
  {
    this(true);
  }

  /**
   * Initialize component and create component GUI.
   * @param     needMenuBar     Specifies whether to put the E-Slate menu bar
   *                            at the top of the component.
   */
  private void initialize(boolean needMenuBar)
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.masterclock.MasterClockResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    this.needMenuBar = needMenuBar;

    setBorder(BorderFactory.createRaisedBevelBorder());

    // Weird: if we do not set the preferred size here, the slider will not
    // appear!
    setPreferredSize(winSize);

    nf = NumberFormat.getInstance(Locale.getDefault());
    nf.setMaximumFractionDigits(2);

    minValue = defaultMinValue;
    maxValue = defaultMaxValue;
    timeScale = initValue;
    scale = 1.0d;
    leftScale = 1.0d;
    rightScale = 1.0d;
    saveState = true;

    ImageIcon startIcon =
      new ImageIcon(MasterClock.class.getResource("images/start.gif"));
    ImageIcon stopIcon =
      new ImageIcon(MasterClock.class.getResource("images/stop.gif"));

    pane = new JPanel(true);
    pane.setForeground(Color.black);

    setLayout(new BorderLayout());

    Box buttonRow = Box.createHorizontalBox();
    Box limitRow = Box.createHorizontalBox();
    //Box scaleSliderRow = Box.createHorizontalBox();
    Box legendRow = Box.createHorizontalBox();
    Box mainCol = Box.createVerticalBox();
    //Box scaleSliderCol = Box.createVerticalBox();

    startButton = new JToggleButton(startIcon);
    startButton.setToolTipText(resources.getString("start"));
    startButton.setSelected(false);
    startButton.setBorderPainted(true);
    startButton.setMargin(new Insets(0, 0, 0, 0));
    startButton.setHorizontalAlignment(SwingConstants.CENTER);
    startButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        interrupted = false;
        startTick();
      }
    });
    buttonRow.add(Box.createHorizontalGlue());
    buttonRow.add(startButton);
    buttonRow.add(Box.createHorizontalStrut(10 * hspace));
    stopButton = new JToggleButton(stopIcon);
    stopButton.setToolTipText(resources.getString("stop"));
    stopButton.setSelected(true);
    stopButton.setBorderPainted(true);
    stopButton.setMargin(new Insets(0, 0, 0, 0));
    stopButton.setHorizontalAlignment(SwingConstants.CENTER);
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        interrupted = true;
        //stopTick();
        stop();
      }
    });

    Dimension s1 = startButton.getPreferredSize();
    Dimension s2 = stopButton.getPreferredSize();
    int w = Math.max(s1.width, s2.width);
    int h = Math.max(s1.height, s2.height);
    startButton.setPreferredSize(new Dimension(w, h));
    stopButton.setPreferredSize(new Dimension(w, h));

    buttonRow.add(stopButton);
    buttonRow.add(Box.createHorizontalGlue());
    ButtonGroup group = new ButtonGroup();
    group.add(startButton);
    group.add(stopButton);
    mainCol.add(buttonRow);

    mainCol.add(Box.createVerticalStrut(vspace));

    leftField = new JTextField();
    leftField.setColumns(prefCols);
    leftField.setMaximumSize(leftField.getPreferredSize());
    leftField.setText(nf.format(minValue));
    leftField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateLower();
      }
    });
    leftFocusListener = new FocusListener() {
      public void focusLost(FocusEvent e) {
        updateLower();
      }
      public void focusGained(FocusEvent e) {
      }
    };
    leftField.addFocusListener(leftFocusListener);
    rightField = new JTextField();
    rightField.setColumns(prefCols);
    rightField.setMaximumSize(leftField.getPreferredSize());
    rightField.setText(nf.format(maxValue));
    rightField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateUpper();
      }
    });
    rightFocusListener = new FocusListener() {
      public void focusLost(FocusEvent e) {
        updateUpper();
      }
      public void focusGained(FocusEvent e) {
      }
    };
    rightField.addFocusListener(rightFocusListener);

    limitRow.add(Box.createHorizontalGlue());
    limitRow.add(leftField);
    //limitRow.add(Box.createHorizontalGlue());
    limitRow.add(Box.createHorizontalStrut(10 * hspace));
    limitRow.add(rightField);
    limitRow.add(Box.createHorizontalGlue());
    mainCol.add(limitRow);

    scaleSlider =
      new JSlider(JSlider.HORIZONTAL, (int)(minValue * scale),
                  (int)(maxValue * scale),
                  (int)(initValue * scale));
    scaleSlider.setToolTipText(resources.getString("select"));
    scaleSlider.addChangeListener(this);
    Dimension maxSliderSize = new Dimension (
      getPreferredSize().width - (2 * hspace),
      Integer.MAX_VALUE
    );
    scaleSlider.setMaximumSize(maxSliderSize);
    scaleSliderKludge();

    mainCol.add(Box.createVerticalStrut(vspace));
    mainCol.add(scaleSlider);
    mainCol.add(Box.createVerticalStrut(vspace));

    legendLabel = new JLabel();
    legendLabel.setText(resources.getString("timeScale") + " 1:");
    legendRow.add(legendLabel);

    legendField = new JTextField();
    legendField.setColumns(prefCols);
    legendField.setMaximumSize(leftField.getPreferredSize());
    legendField.setText(nf.format(initValue));
    legendField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateSlider();
      }
    });
    legendFocusListener = new FocusListener() {
      public void focusLost(FocusEvent e) {
        updateSlider();
      }
      public void focusGained(FocusEvent e) {
      }
    };
    legendField.addFocusListener(legendFocusListener);
    legendRow.add(legendField);

    mainCol.add(legendRow);

    pane.add(mainCol, BorderLayout.CENTER);

    add(pane, BorderLayout.CENTER);

    // Restore the default preferred size.
    setPreferredSize(null);

    setMinimumSize(getPreferredSize());

    // Start tick thread

    threadRunning = false;
    tt = new Thread(this, "Tick Thread");
    tt.start();

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * This method works around a Swing(?) bug, where if a component containing
   * a JSlider is added to E-Slate, only the first JSlider is visible, and no
   * combination of invalidate()/revalidate()/validateTree()/doLayout()
   * seems to fix the problem.
   * The workaround consists of switching the orientation of the slider to the
   * opposite of what it is and back to the original, thus forcing Swing to
   * make it visible.
   */
  private void scaleSliderKludge()
  {
    addAncestorListener(new AncestorListener(){
      public void ancestorAdded(AncestorEvent event)
      {
        SwingUtilities.invokeLater(new Runnable(){
          public void run()
          {
            if (scaleSlider != null) {
              scaleSlider.setOrientation(
                (scaleSlider.getOrientation() == JSlider.HORIZONTAL) ?
                  JSlider.VERTICAL : JSlider.HORIZONTAL
              );
              scaleSlider.setOrientation(
                (scaleSlider.getOrientation() == JSlider.HORIZONTAL) ?
                  JSlider.VERTICAL : JSlider.HORIZONTAL
              );
            }
          }
        });
      }
      public void ancestorRemoved(AncestorEvent event)
      {
      }
      public void ancestorMoved(AncestorEvent event)
      {
      }
    });
  }

  /**
   * Returns the minimum value of the time scale slider.
   * @return    The requested value.
   */
  public double getMinimumTimeScale()
  {
    return minValue;
  }

  /**
   * Sets the minimum value of the time scale slider.
   * @param     value   The minimum value of the time scale slider.
   */
  public void setMinimumTimeScale(double value)
  {
    String str = nf.format(value);
    leftField.setText(str);
    updateLower();
  }

  /**
   * Returns the maximum value of the time scale slider.
   * @return    The requested value.
   */
  public double getMaximumTimeScale()
  {
    return maxValue;
  }

  /**
   * Sets the maximum value of the time scale slider.
   * @param     value   The maximum value of the time scale slider.
   */
  public void setMaximumTimeScale(double value)
  {
    String str = nf.format(value);
    rightField.setText(str);
    updateUpper();
  }

  /**
   * Returns the value of the time scale slider.
   * @return    The requested value.
   */
  public double getTimeScale()
  {
    return timeScale;
  }

  /**
   * Sets the value of the time scale slider.
   * @param     value   The value of the time scale slider.
   */
  public void setTimeScale(double value)
  {
    String str = nf.format(value);
    legendField.setText(str);
    updateSlider();
  }

  /**
   * Specifies whether the start/stop state of the component should be saved.
   * @param     status  True if yes, false if no.
   */
  public void setSaveStartStop(boolean status)
  {
    saveState = status;
  }

  /**
   * Returns whether the start/stop state of the component will be saved.
   */
  public boolean isSaveStartStop()
  {
    return saveState;
  }

  /**
   * Update lower limit of time scale slider from the leftField text field.
   */
  private void updateLower()
  {
    double temp;
    String s;

    s = leftField.getText();
    if (s.equals("")) {
      temp = 0;
      leftField.setText("0");
    }else{
      try {
        temp = nf.parse(s).doubleValue();
      } catch (ParseException pe) {
        temp = minValue;
      }
    }
    if (temp < (1.0d / 10000.0d) || temp >= maxValue) {
      temp = minValue;
    }
    minValue = temp;
    temp = Math.abs(minValue) + (5.0d / 10000.0d);
    int fraction = (int)(1000.0 * (temp - Math.floor(temp)));
    if (fraction % 100 > 0) {
      leftScale = 1000.0d;
    }else{
      if (fraction % 10 > 0) {
        leftScale = 100.0d;
      }else{
        if (fraction > 0) {
          leftScale = 10.0d;
        }else{
          leftScale = 1.0d;
        }
      }
    }
    scale = Math.max(leftScale, rightScale);
    temp = timeScale;
    scaleSlider.setMinimum((int)(minValue * scale));
    scaleSlider.setMaximum((int)(maxValue * scale));
    timeScale = temp;
    scaleSlider.setValue((int)(scale * timeScale));
    leftField.setText(nf.format(minValue));
  }

  /**
   * Update upper limit of time scale slider from the rightField text field.
   */
  private void updateUpper()
  {
    double temp;
    String s;

    s = rightField.getText();
    if (s.equals("")) {
      temp = 0;
      rightField.setText("0");
    }else{
      try {
        temp = nf.parse(s).doubleValue();
      } catch (ParseException pe) {
        temp = maxValue;
      }
    }
    if (temp < (1.0d / 10000.0d) || temp <= minValue) {
      temp = maxValue;
    }
    maxValue = temp;
    temp = Math.abs(minValue) + (5.0d / 10000.0d);
    int fraction = (int)(1000.0 * (temp - Math.floor(temp)));
    if (fraction % 100 > 0) {
      scale = 1000.0d;
    }else{
      if (fraction % 10 > 0) {
        scale = 100.0d;
      }else{
        if (fraction > 0) {
          rightScale = 10.0d;
        }else{
          rightScale = 1.0d;
        }
      }
    }
    scale = Math.max(leftScale, rightScale);
    temp = timeScale;
    scaleSlider.setMinimum((int)(scale * minValue));
    scaleSlider.setMaximum((int)(scale * maxValue));
    timeScale = temp;
    scaleSlider.setValue((int)(scale * timeScale));
    rightField.setText(nf.format(maxValue));
  }

  /**
   * Update value of time scale slider from legendField text field.
   */
  private void updateSlider()
  {
    double temp;
    String s;

    s = legendField.getText();
    if (s.equals("")) {
      temp = 0;
      legendField.setText("0");
    }else{
      try {
        temp = nf.parse(s).doubleValue();
      } catch (ParseException pe) {
        temp = timeScale;
      }
    }
    if (temp < minValue || temp > maxValue) {
      temp = timeScale;
    }
    timeScale = temp;
    scaleSlider.setValue((int)(timeScale * scale));
    legendField.setText(nf.format(timeScale));
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

    handle.setInfo(getInfo());

    tick = new Tick(handle, 0);

    try {
      Plug plug = new MultipleOutputPlug(
                  handle, resources, "tick", Color.yellow,
                  gr.cti.eslate.sharedObject.Tick.class, tick);
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener()
      {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          if (tick.getNumberOfListeners() == 1) {
            if (startButton.isSelected()) {
              startTick();
            }
          }
        }
      });
    } catch (InvalidPlugParametersException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    } catch (PlugExistsException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.MasterClockPrimitives"
    );

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

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 6);

    map.put(MINVALUE, minValue);
    map.put(MAXVALUE, maxValue);
    map.put(TIMESCALE, timeScale);
    map.put(SAVESTATE, saveState);
    map.put(ISSTARTED, startButton.isSelected());
    synchronized(this) {
      map.put(ISRUNNING, threadRunning);
    }
    map.put(MINIMUM_SIZE, getMinimumSize());
    map.put(MAXIMUM_SIZE, getMaximumSize());
    map.put(PREFERRED_SIZE, getPreferredSize());
    map.put(SLEEP_INTERVAL, sleepInterval);

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

    //stopTick();
    stop();

    double lo;
    double hi;
    double ts;
    boolean isStarted;
    boolean isRunning;
    Object obj = (oi.readObject());
    if (obj instanceof StorageStructure) {
      StorageStructure map = (StorageStructure)obj;
      lo = map.get(MINVALUE, defaultMinValue);
      hi = map.get(MAXVALUE, defaultMaxValue);
      ts = map.get(TIMESCALE, initValue);
      saveState = map.get(SAVESTATE, false);
      isStarted = map.get(ISSTARTED, false);
      isRunning = map.get(ISRUNNING, false);
      setMinimumSize(map.get(MINIMUM_SIZE, winSize));
      setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(map.get(PREFERRED_SIZE, winSize));
      setPreferredSize(map.get(PREFERRED_SIZE, getPreferredSize()));
      sleepInterval = map.get(SLEEP_INTERVAL, DEFAULT_SLEEP_INTERVAL);
    }else{
      lo = ((Double)obj).doubleValue();
      hi = ((Double)(oi.readObject())).doubleValue();
      ts = ((Double)(oi.readObject())).doubleValue();
      // This field was introduced in version 1.26.
      try {
        saveState = oi.readBoolean();
      } catch (EOFException e) {
        saveState = true;
      }
      // This field was introduced in version 1.26.
      try {
        isStarted = oi.readBoolean();
      } catch (EOFException e) {
        isStarted = false;
      }
      // This field was introduced in version 1.26.
      try {
        isRunning = oi.readBoolean();
      } catch (EOFException e) {
        isRunning = false;
      }
      sleepInterval = DEFAULT_SLEEP_INTERVAL;
    }

    if (lo > maxValue) {
      rightField.setText(nf.format(hi));
      updateUpper();
      leftField.setText(nf.format(lo));
      updateLower();
    }else{
      leftField.setText(nf.format(lo));
      updateLower();
      rightField.setText(nf.format(hi));
      updateUpper();
    }
    legendField.setText(nf.format(ts));
    updateSlider();

    if (!saveState) {
      isStarted = false;
      isRunning = false;
    }
    startButton.setSelected(isStarted);
    stopButton.setSelected(!isStarted);
    if (isRunning) {
      interrupted = false;
      startTick();
    }

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
    MainFrame f = new MainFrame("", winSize);

    MasterClock mc = new MasterClock();

    f.add("Center", mc);
    f.pack();

    f.setTitle(mc.resources.getString("name"));

    f.setVisible(true);
  }

  /**
   * Action event handler.
   * @param     e       The event to handle.
   */
  public void actionPerformed(java.awt.event.ActionEvent e)
  {
    String cmd = e.getActionCommand();

    if (cmd.equals("start")) {
      startTick();
    }else{
      if (cmd.equals("stop")) {
        //stopTick();
        stop();
      }
    }
  }

  /**
   * Handle events generated by changing the slider's value.
   */
  public void stateChanged(ChangeEvent e)
  {
    timeScale = (double)scaleSlider.getValue() / scale;
    legendField.setText(nf.format(timeScale));
  }

  /**
   * Start sending ticks.
   */
  private void startTick()
  {
    if (interrupted) return;
    synchronized(this) {
      threadRunning = true;
      notify();
    }
  }

  /**
   * Select the "start" button, and start sending ticks.
   */
  public void start()
  {
    startButton.setSelected(true);
    interrupted = false;
    startTick();
  }

//  private boolean stackTrace = true;
//  /**
//   * Select the "stop" button, and stop sending ticks.
//   */
//  public void stop2()
//  {
//    stackTrace = false;
//    try {
//      stop();
//    } finally {
//      stackTrace = true;
//    }
//  }

  /**
   * Stop sending ticks.
   */
  public void stopTick()
  {
//    if (stackTrace) {
//      Thread.currentThread().dumpStack();
//    }
    synchronized(this){
        threadRunning = false;
    }
  }

  /**
   * Select the "stop" button, and stop sending ticks.
   */
  public void stop()
  {
    interrupted = true;
    stopTick();
    stopButton.setSelected(true);
  }

  /**
   * Check whether the master clock is running.
   * @return    True if yes, false if no.
   */
  public boolean isRunning()
  {
    return threadRunning;
  }

  /**
   * Body of the thread that is spawned to send ticks.
   */
  public void run()
  {
    //int secs_counted = 0;
    long oldTime = 0, newTime;
    long newTick;

    // The thread that sends the ticks should run at a lower priority,
    // so that components may remain responsive. (E.g., if this is not
    // done, their GUI might freeze under some extreme conditions).
    // (Is this still needed now that we send ticks in the event dispatcher
    // thread, suspending the Tick thread while ticks are being processed?)

    Thread.currentThread().setPriority(Thread.NORM_PRIORITY-1);

    while (tt != null) {
      synchronized(this) {
        while (!threadRunning) {
          try {
            // Give a chance to non-preemptive thread schedulers to schedule
            // other threads.
            Thread.sleep(0);
            wait();
          } catch (InterruptedException e) {
            return;
          }
        }
      }
      for(;;) {
        synchronized(this) {
          if (!threadRunning) {
            break;
          }
        }
        //boolean oldThreadRunning = threadRunning;
        if (tick.getNumberOfListeners() <= 0) {
          //synchronized(this) {
          //  threadRunning = false;
            break;
          //}
        }else{
          newTime = System.currentTimeMillis();
          if (oldTime == 0) {
            oldTime = newTime;
          }
          newTick = (long)((newTime-oldTime) * 1000 * timeScale);
          if (newTick != 0) {
            //if (threadRunning/* == oldThreadRunning*/){
              //tick.setTick(newTick);
              sendTick(newTick);
              oldTime = newTime;
            //}else{
            //  threadRunning = false;
            //}
            giveOtherThreadsAChance();
          }
        }
      }
      giveOtherThreadsAChance();
      oldTime = 0;

      synchronized(this) {
        threadRunning = false;
      }
    }
  }

  /**
   * Give other threads a chance to run after processing a tick, so that we do
   * not hog the CPU.
   */
  private void giveOtherThreadsAChance()
  {
    if (sleepInterval > 0) {
      try {
        Thread.sleep(sleepInterval);
      }catch (Exception exc) {
        exc.printStackTrace();
      }
    }else{
      Thread.yield();
    }
  }

  /**
   * Send a tick. This is done by invoking tick.setTick on the event
   * dispatcher thread. As this method is invoked from the Tick thread,
   * and swing requires that all GUI updates (whichare bound to occur in the
   * components that will process the tick) to be made in the event dispatcher
   * thread, we cannot invoke tick.setTick directly.
   */
  private void sendTick(long tk)
  {
    tik = tk;

    if (send == null) {
      send = new Runnable()
      {
        public void run()
        {
          tick.setTick(tik);
        }
      };
    }
    try {
      SwingUtilities.invokeAndWait(send);
    } catch (Exception e) {
    }
  }

  /**
   * Sets the number of milliseconds to sleep after sending each tick.
   * @param     n       The number of milliseconds to sleep after sending
   *                    each tick.
   */
  public void setSleepInterval(long n)
  {
    sleepInterval = n;
  }

  /**
   * Returns the number of milliseconds to sleep after sending each tick.
   * @return    The number of milliseconds to sleep after sending each tick.
   */
  public long getSleepInterval()
  {
    return sleepInterval;
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    // Stop tick thread
    Thread t = tt;
    synchronized(this) {
      threadRunning = false;
    }
    tt = null;
    t.interrupt();
    t = null;
    if (handle != null) {
      handle.dispose();
      handle = null;
    }
    ESlateHandle.removeAllRecursively(this);
    tick = null;
    send = null;
    //suspendSend = null;
    pane = null;
    startButton = null;
    stopButton = null;
    scaleSlider = null;
    //paneSize = null;
    leftField.removeFocusListener(leftFocusListener);
    leftField = null;
    rightField.removeFocusListener(rightFocusListener);
    rightField = null;
    legendField.removeFocusListener(legendFocusListener);
    legendField = null;
    legendLabel = null;
    resources = null;
    nf = null;
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
