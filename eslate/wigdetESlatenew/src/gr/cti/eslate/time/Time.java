package gr.cti.eslate.time;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.protocol.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements a travel for a given time control component, which
 * instructs components connected to it to travel for a given time. The user
 * specifies the time in a text box, selects the unit in which this time is
 * measured from a combo box, optionally selects via a checkbox whether
 * the connected component should stop at "interesting" sites even if the
 * specified time has not elapsed, and by clicking on the "go"
 * button instructs the connected component to travel for this time.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI><B>Time.<B> This is a protocol plug associated with the
 * <A HREF="gr.cti.eslate.protocol.TimeControlInterface.html">TimeControlInterface</A>
 * interface. The plug's color is Color(250, 128, 114).
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>GOTIME</B> Start moving.</LI>
 * <LI><B>TIME</B> Returns the time to travel.</LI>
 * <LI><B>SETTIME time</B> Set the time to travel.</LI>
 * <LI><B>TIMEUNIT</B> Returns the unit in which the time to travel is
 * measured.</LI>
 * <LI><B>SETTIMEUNIT unit</B> Specify the unit in which the time to
 * travel is measured.</LI>
 * <LI><B>TIMEEUNITS</B> Returns the supported units in which the time
 * to travel can be measured.
 * <LI><B>STOPATLANDMARKS</B> Checks whether we should stop at landmarks.</LI>
 * <LI><B>SETSTOPATLANDMARKS boolean</B> Specify whether we should stop at
 * landmarks.</LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.protocol.TimeControlInterface
 */
public class Time extends JPanel
  implements ESlatePart, ActionListener, FocusListener, Externalizable,
             Serializable, AsTime
{
  private ESlateHandle handle;
  private JPanel pane = null;
  private RightSingleConnectionProtocolPlug plug;
  private JButton goButton;
  private JTextField timeField;
  private JComboBox cbox;
  private JCheckBox check;
  private double time;
  private String unit;
  private boolean stopAtLandmarks = false;
  private boolean needMenuBar;

  private final static int hspace = 4;
  private final static int vspace = 4;
  private final static Dimension winSize = new Dimension(234, 68);
  private final static int fieldCols = 4;

  private ResourceBundle resources;
  private final static String version = "2.0.1";
  private final static int saveVersion = 1;
  private NumberFormat nf;

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

  private final static String HOURS = "hours";
  private final static String MINUTES = "minutes";
  private final static String SECONDS = "seconds";

  private static String unitNames[];

  static {
    ResourceBundle res = ResourceBundle.getBundle(
      "gr.cti.eslate.time.TimeResource", Locale.getDefault()
    );
    unitNames = new String[] {
      res.getString(HOURS),
      res.getString(MINUTES),
      // Due to a swing bug (now fixed?), combo boxes with only a few items,
      // whose popup window can fit in the containing (window? panel?) don't
      // work, so we might as well add this one...
      res.getString(SECONDS)
    };
  }

  private final static double SECONDS_PER_HOUR = 3600.0;
  private final static double SECONDS_PER_MINUTE = 60.0;
  private final static String CBOX_CMD = "cbox";
  private final static String GO_CMD = "go";
  private final static String CHECK_CMD = "check";
  private final static String TIME_CMD = "time";

  private final static String TIME = "time";
  private final static String SELECTEDINDEX = "selectedIndex";
  private final static String STOPATLANDMARKS = "stop";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  static final long serialVersionUID = -5032451928726358799L;
  
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
   * Create a travel for a given time control component.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public Time(boolean needMenuBar)
  {
    super();
    initialize(needMenuBar);
  }

  /**
   * Create a travel for a given time control component. The menu bar will be
   * visible.
   */
  public Time()
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
      "gr.cti.eslate.time.TimeResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    this.needMenuBar = needMenuBar;

    setPreferredSize(Time.winSize);
    setMinimumSize(Time.winSize);
    
    nf = NumberFormat.getInstance(Locale.getDefault());
    nf.setMaximumFractionDigits(2);

    time = 0;

    pane = new JPanel(true);
    pane.setForeground(Color.black);
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    setLayout(new BorderLayout());

    Box topRow = Box.createHorizontalBox();
    Box bottomRow = Box.createHorizontalBox();
    Box col = Box.createVerticalBox();

    goButton = new JButton(resources.getString("go"));
    goButton.setActionCommand(GO_CMD);
    goButton.addActionListener(this);
    goButton.setAlignmentY(CENTER_ALIGNMENT);
    goButton.setMargin(new Insets(2, 2, 2, 2));

    timeField = new JTextField();
    timeField.setActionCommand(TIME_CMD);
    timeField.addActionListener(this);
    timeField.addFocusListener(this);
    timeField.setColumns(fieldCols);
    timeField.setText("0");
    timeField.setAlignmentY(CENTER_ALIGNMENT);
    timeField.setMaximumSize(timeField.getPreferredSize());

    cbox = new JComboBox();
    int n = unitNames.length;
    for (int i=0; i<n; i++) {
      cbox.addItem(unitNames[i]);
    }
    cbox.setEditable(false);
    cbox.setActionCommand(CBOX_CMD);
    cbox.addActionListener(this);
    cbox.setAlignmentY(CENTER_ALIGNMENT);
    unit = (String)(cbox.getSelectedItem());
    if (handle != null) {
      cbox.setLightWeightPopupEnabled(handle.isMenuLightWeight());
    }

    // Make all three components the same height.
    Dimension s1 = goButton.getPreferredSize();
    Dimension s2 = timeField.getPreferredSize();
    Dimension s3 = cbox.getPreferredSize();
    int h = Math.max(s1.height, s2.height);
    h = Math.max(h, s3.height);
    goButton.setPreferredSize(new Dimension(s1.width, h));
    goButton.setMinimumSize(new Dimension(s1.width, h));
    goButton.setMaximumSize(new Dimension(s1.width, h));
    timeField.setPreferredSize(new Dimension(s2.width, h));
    timeField.setMinimumSize(new Dimension(s2.width, h));
    timeField.setMaximumSize(new Dimension(s2.width, h));
    cbox.setPreferredSize(new Dimension(s3.width, h));
    cbox.setMinimumSize(new Dimension(s3.width, h));
    cbox.setMaximumSize(new Dimension(s3.width, h));

    topRow.add(goButton);
    topRow.add(Box.createHorizontalStrut(hspace));
    topRow.add(Box.createHorizontalGlue());
    topRow.add(timeField);
    topRow.add(Box.createHorizontalStrut(hspace));
    topRow.add(Box.createHorizontalGlue());
    topRow.add(cbox);

    check = new JCheckBox(resources.getString("stop"));
    check.setHorizontalTextPosition(JCheckBox.LEFT);
    check.setActionCommand(CHECK_CMD);
    check.addActionListener(this);
    // Hack--checkbox doesn't get the correct font
    check.setFont(goButton.getFont());

    bottomRow.add(Box.createHorizontalGlue());
    bottomRow.add(check);
    bottomRow.add(Box.createHorizontalGlue());

    col.add(topRow);
    col.add(Box.createVerticalStrut(vspace));
    col.add(bottomRow);

    pane.add(col, BorderLayout.CENTER);

    add(pane, BorderLayout.CENTER);

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Action event handler.
   * @param     e       The event to handle.
   */
  public void actionPerformed(ActionEvent e)
  {
    String cmd = e.getActionCommand();
    Object source = e.getSource();
    String s;
    double temp;

    if (cmd.equals(CBOX_CMD)) {
      unit = (String)(cbox.getSelectedItem());
    }else{
      if (cmd.equals(GO_CMD)) {
        go();
      }else{
        if (cmd.equals(CHECK_CMD)) {
          stopAtLandmarks = ((JCheckBox)source).isSelected();
        }else{
          if (source.equals(timeField)) {
            s = timeField.getText();
            if (s.equals("")) {
              temp = 0.0;
            }else{
              try {
                temp = nf.parse(s).doubleValue();
              } catch (ParseException nfe) {
                temp = 0.0;
              }
            }
            time = temp;
            timeField.setText(nf.format(time));
          }
        }
      }
    }
  }

  /**
   * Start moving.
   */
  public void go()
  {
    try {
      IProtocolPlug p = (IProtocolPlug)(plug.getProtocolPlug());
      TimeControlInterface tci =
        (TimeControlInterface)(p.getProtocolImplementor());
      double conversionFactor;
      if (unit.equals(resources.getString(HOURS)))
        conversionFactor = SECONDS_PER_HOUR;
      else if (unit.equals(resources.getString(MINUTES)))
        conversionFactor = SECONDS_PER_MINUTE;
      else
        conversionFactor = 1.0;
      tci.goTime(time * conversionFactor, stopAtLandmarks);
    } catch (NoSingleConnectedComponentException ex) {
    }
  }

  /**
   * Set the time to travel.
   * @param     x       The time to travel.
   */
  public void setTime(double x)
  {
    time = x;
    timeField.setText(nf.format(time));
  }

  /**
   * Returns the time to travel.
   * @return    The requested time.
   */
  public double getTime()
  {
    return time;
  }

  /**
   * Specify the unit in which the time to travel is measured.
   * @param     u       The name of the unit.
   * @exception Exception       Thrown if the unit is not supported.
   */
  public void setUnit(String u) throws Exception
  {
    int nItems = cbox.getItemCount();
    for (int i=0; i<nItems; i++) {
      String s = (String)(cbox.getItemAt(i));
      if (ESlateStrings.areEqualIgnoreCase(s, u, handle.getLocale())) {
        cbox.setSelectedIndex(i);
        return;
      }
    }
    throw new Exception(resources.getString("badUnit"));
  }

  /**
   * Returns the unit in which the time to travel is measured.
   * @return    The requested unit.
   */
  public String getUnit()
  {
    return (String)(cbox.getSelectedItem());
  }

  /**
   * Returns the supported units in which the time to travel can be
   * measured.
   * @return    An array of the names of the supported units.
   */
  public static String[] getUnitNames()
  {
    int n = unitNames.length;
    String s[] = new String[n];
    System.arraycopy(unitNames, 0, s, 0, n);
    return s;
  }

  /**
   * Returns the supported units in which the time to travel can be
   * measured. Required by AsTime interface, where the method cannot be
   * static.
   * @return    An array of the names of the supported units.
   */
  public String[] getUnits()
  {
    return getUnitNames();
  }

  /**
   * Specify whether we should stop at landmarks.
   * @param     stop    Yes if true, no if false.
   */
  public void setStopAtLandmarks(boolean stop)
  {
    check.setSelected(stop);
    stopAtLandmarks = stop;
  }

  /**
   * Checks whether we should stop at landmarks.
   * @return    True if yes, false if no.
   */
  public boolean getStopAtLandmarks()
  {
    return stopAtLandmarks;
  }

  /**
   * Handle "focus gained" events.
   * @param     e       The event to handle.
   */
  public void focusGained(FocusEvent e)
  {
  }

  /**
   * Handle "focus lost" events.
   * @param     e       The event to handle.
   */
  public void focusLost(FocusEvent e)
  {
    JTextField tf = (JTextField)e.getComponent();
    double temp;
    String s;

    if (tf.equals(timeField)){
      s = timeField.getText();
      if (s.equals("")) {
        temp = 0.0;
      }else{
        try {
          temp = nf.parse(s).doubleValue();
        } catch (ParseException nfe) {
          temp = 0.0;
        }
      }
      time = temp;
      timeField.setText(nf.format(time));
    }
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

    try {
      plug = new RightSingleConnectionProtocolPlug(
              handle, resources, "time",
              new Color(250, 128, 114),
              gr.cti.eslate.protocol.TimeControlInterface.class);
      handle.addPlug(plug);
    } catch (NoClassDefFoundError e) {
      // TimeControlInterface not present in CLASSPATH. Presumably
      // this means that no matching component is available, so we simply
      // ignore the error and do not add the plug.
    } catch (InvalidPlugParametersException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    } catch (PlugExistsException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.TimePrimitives"
    );

    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }

    if (cbox != null) {
      cbox.setLightWeightPopupEnabled(handle.isMenuLightWeight());
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

    map.put(TIME, time);
    map.put(SELECTEDINDEX, cbox.getSelectedIndex());
    map.put(STOPATLANDMARKS, stopAtLandmarks);
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

    int selectedIndex;

    Object obj = (oi.readObject());
    if (obj instanceof StorageStructure) {
      StorageStructure map = (StorageStructure)obj;
      time = map.get(TIME, 0.0);
      selectedIndex = map.get(SELECTEDINDEX, 0);
      stopAtLandmarks = map.get(STOPATLANDMARKS, false);
      setMinimumSize(map.get(MINIMUM_SIZE, Time.winSize));
      setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(map.get(PREFERRED_SIZE, Time.winSize));
    }else{
      time = ((Double)obj).doubleValue();
      selectedIndex = ((Integer)(oi.readObject())).intValue();
      stopAtLandmarks = ((Boolean)(oi.readObject())).booleanValue();
    }
    timeField.setText(nf.format(time));
    cbox.setSelectedIndex(selectedIndex);
    unit = (String)(cbox.getSelectedItem());
    check.setSelected(stopAtLandmarks);

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
    MainFrame f = new MainFrame("", Time.winSize);

    Time tim = new Time();

    f.add("Center", tim);
    f.pack();

    f.setTitle(tim.resources.getString("name"));

    f.setVisible(true);
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
    plug = null;
    goButton = null;
    timeField.removeFocusListener(this);
    timeField = null;
    cbox = null;
    check = null;
    unit = null;
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
