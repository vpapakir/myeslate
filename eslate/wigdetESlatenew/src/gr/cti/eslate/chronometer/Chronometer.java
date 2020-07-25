package gr.cti.eslate.chronometer;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements a chronometer component. This component has a
 * HH:MM:SS display, and three buttons: start, stop, and reset.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI><B>Tick.</B> This is a single input plug associated with a
 * <A HREF="gr.cti.eslate.sharedObject.Tick.html">Tick</A> shared object.
 * The plug's color is Color.yellow.
 * </LI>
 * <LI><B>Time in hours</B> This is a single input multiple output plug
 * associated with a NumberSO shared object.
 * The plug's color is Color(135, 206, 250).
 * The number read via this plug is interpretted as hours, and is used
 * to set the chronometer's elapsed time display to that value.
 * </LI>
 * <LI><B>Time in minutes</B> This is a single input multiple output plug
 * associated with a NumberSO shared object.
 * The plug's color is Color(135, 206, 250).
 * The number read via this plug is interpretted as minutes, and is used
 * to set the chronometer's elapsed time display to that value.
 * </LI>
 * <LI><B>Time in seconds</B> This is a single input multiple output plug
 * associated with a  NumberSO shared object.
 * The plug's color is Color(135, 206, 250).
 * The number read via this plug is interpretted as seconds, and is used
 * to set the chronometer's elapsed time display to that value.
 * </LI>
 * <LI><B>Time in milliseconds</B> This is a single input multiple output plug
 * associated with a  NumberSO shared object.
 * The plug's color is Color(135, 206, 250).
 * The number read via this plug is interpretted as milliseconds, and is used
 * to set the chronometer's elapsed time display to that value.
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>STARTCHRONOMETER</B> Start measuring elspased time.</LI>
 * <LI><B>STOPCHRONOMETER</B> Stop measuring elapsed time.</LI>
 * <LI><B>RESETCHRONOMETER</B> Reset the chronometer display.</LI>
 * <LI><B>SETCHRONOMETERTIME time</B> Set the elapsed time to a given value.
 * The time can be specified as either a string of the form HH:MM:SS.mmm or
 * as a list of the form [HH MM SS mmm]. HH is mandatory, remaining fields are
 * optional.</LI>
 * <LI><B>CHRONOMETERTIME</B> Return the elapsed time. The time is returned
 * as a list of the form [HH MM SS mmm].</LI>
 * <LI><B>SETCHRONOMETERHOURS time</B> Set the elapsed time to a given value
 * in hours.</LI>
 * <LI><B>CHRONOMETERHOURS</B> Return the elapsed time measured in hours.</LI>
 * <LI><B>SETCHRONOMETERMINUTES time</B> Set the elapsed time to a given value
 * in minutes.</LI>
 * <LI><B>CHRONOMETERMINUTES</B> Return the elapsed time measured in minutes.
 * </LI>
 * <LI><B>SETCHRONOMETERSECONDS time</B> Set the elapsed time to a given value
 * in seconds.</LI>
 * <LI><B>CHRONOMETERSECONDS</B> Return the elapsed time measured in seconds.
 * </LI>
 * <LI><B>SETCHRONOMETERMILLISECONDS time</B> Set the elapsed time to a given
 * value in milliseconds.</LI>
 * <LI><B>CHRONOMETERMILLISECONDS</B> Return the elapsed time measured in
 * milliseconds.</LI>
 * <LI><B>CHRONOMETERRUNNING</B> Checks whether the chronometer is
 * running.</LI>
 * </UL>
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2007
 * @see         gr.cti.eslate.sharedObject.Tick
 */
public class Chronometer extends JPanel
  implements ESlatePart, ActionListener, Externalizable, Serializable,
             AsChronometer
{
  private ESlateHandle handle;
  private JPanel pane = null;
  private JToggleButton startButton, stopButton;
  private JButton resetButton;
  private final static int hspace = 8;
  private final static int vspace = 8;
  private boolean running = false;
  private TimeCount interval;
  private NumberSO displayHours;
  private NumberSO displayMins;
  private NumberSO displaySecs;
  private NumberSO displayMillis;
  private JLabel display;
  private boolean needMenuBar;
  private ArrayList<ChronometerListener> chronometerListeners =
    new ArrayList<ChronometerListener>();
  private ResourceBundle resources;
  private final static String version = "2.0.4";
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
  private final static int START = 0;
  private final static int STOP = 1;
  private final static int VALUECHANGED = 2;
  private final static long MSECPERSEC = 1000L;
  private final static long MSECPERMIN = 60L * MSECPERSEC;
  private final static long MSECPERHOUR = 60L * MSECPERMIN;
  private final static long USECPERMSEC = 1000L;
  private final static long SECSPERMIN = 60L;
  private final static long SECSPERHOUR = 60L * SECSPERMIN;
  //private final static long USECSPERSEC = 1000000L;
  private final static String INTERVAL = "interval";
  private final static String RUNNING = "running";
  private final static String STARTED = "started";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  static final long serialVersionUID = 1161561047696268029L;

  /*
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
   * Create a chronometer component.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public Chronometer(boolean needMenuBar)
  {
    super();
    initialize(needMenuBar);
  }

  /**
   * Create a chronometer component. The menu bar will be visible.
   */
  public Chronometer()
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
      "gr.cti.eslate.chronometer.ChronometerResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    this.needMenuBar = needMenuBar;

    setBorder(BorderFactory.createRaisedBevelBorder());

    ImageIcon startIcon =
      new ImageIcon(Chronometer.class.getResource("images/start.gif"));
    ImageIcon stopIcon =
      new ImageIcon(Chronometer.class.getResource("images/stop.gif"));
    ImageIcon resetIcon =
      new ImageIcon(Chronometer.class.getResource("images/reset.gif"));

    pane = new JPanel(true);
    pane.setForeground(Color.black);

    setLayout(new BorderLayout());

    Box buttonRow = Box.createHorizontalBox();
    Box displayRow = Box.createHorizontalBox();
    Box mainCol = Box.createVerticalBox();

    Font BoldFont = new Font("SansSerif", Font.BOLD, 20);
    display = new JLabel();
    display.setFont(BoldFont);
    display.setHorizontalAlignment(SwingConstants.CENTER);
    setDisplay(0, 0, 0);
    displayRow.add(Box.createHorizontalGlue());
    displayRow.add(display);
    displayRow.add(Box.createHorizontalGlue());

    mainCol.add(displayRow);

    mainCol.add(Box.createVerticalStrut(vspace));

    buttonRow.add(Box.createHorizontalGlue());
    startButton = new JToggleButton(startIcon);
    startButton.setToolTipText(resources.getString("start"));
    startButton.setSelected(false);
    startButton.setBorderPainted(true);
    startButton.setActionCommand("start");
    startButton.addActionListener(this);
    startButton.setMargin(new Insets(0, 0, 0, 0));
    startButton.setHorizontalAlignment(SwingConstants.CENTER);
    buttonRow.add(startButton);
    buttonRow.add(Box.createHorizontalStrut(hspace));
    stopButton = new JToggleButton(stopIcon);
    stopButton.setToolTipText(resources.getString("stop"));
    stopButton.setSelected(true);
    stopButton.setBorderPainted(true);
    stopButton.setActionCommand("stop");
    stopButton.addActionListener(this);
    stopButton.setMargin(new Insets(0, 0, 0, 0));
    stopButton.setHorizontalAlignment(SwingConstants.CENTER);
    buttonRow.add(stopButton);
    ButtonGroup group = new ButtonGroup();
    group.add(startButton);
    group.add(stopButton);
    buttonRow.add(Box.createHorizontalStrut(hspace));
    resetButton = new JButton(resetIcon);
    resetButton.setToolTipText(resources.getString("reset"));
    resetButton.setActionCommand("reset");
    resetButton.addActionListener(this);
    resetButton.setBorderPainted(true);
    resetButton.setMargin(new Insets(0, 0, 0, 0));
    resetButton.setHorizontalAlignment(SwingConstants.CENTER);
    buttonRow.add(resetButton);
    buttonRow.add(Box.createHorizontalGlue());
    mainCol.add(buttonRow);

    Dimension s1 = startButton.getPreferredSize();
    Dimension s2 = stopButton.getPreferredSize();
    Dimension s3 = resetButton.getPreferredSize();
    // Use Math.min to make small, square buttons, and Math.max to make
    // larger, rectangular buttons.
    int w = Math.min(s1.width, s2.width);
    w = Math.min(w, s3.width);
    int h = Math.min(s1.height, s2.height);
    h = Math.min(h, s3.height);
    startButton.setPreferredSize(new Dimension(w, h));
    stopButton.setPreferredSize(new Dimension(w, h));
    resetButton.setPreferredSize(new Dimension(w, h));

    pane.add(mainCol, BorderLayout.CENTER);

    add(pane, BorderLayout.CENTER);

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Run the component as an application--useful for debugging.
   * @param     args    Application's arguments (not used).
   */
  public static void main(String[] args)
  {
    Chronometer ch = new Chronometer();

    MainFrame f = new MainFrame("", ch.getPreferredSize());

    f.add("Center", ch);
    f.pack();

    f.setTitle(ch.resources.getString("name"));

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
      //if (connected) {
        startChronometer();
      //}
    }else{
      if (cmd.equals("stop")) {
        stopChronometer();
      }else{
        if (cmd.equals("reset")) {
          resetChronometer();
        }
      }
    }
  }

  /**
   * Start the chronometer.
   */
  private void startChronometer()
  {
    running = true;
    fireChronometerListeners(START);
  }

  /**
   * Stop the chronometer.
   */
  private void stopChronometer()
  {
    running = false;
    fireChronometerListeners(STOP);
  }

  /**
   * Reset the chronometer.
   */
  private void resetChronometer()
  {
    synchronized (interval) {
      interval.set(0, 0, 0);
      setDisplay(interval.hour, interval.min, interval.sec);
      updateSharedObjects();
      fireChronometerListeners(VALUECHANGED);
    }
  }

  /**
   * Select the "start" button and start the chronometer.
   */
  public void start()
  {
    startButton.setSelected(true);
    startChronometer();
  }

  /**
   * Select the "stop" button and stop the chronometer.
   */
  public void stop()
  {
    stopButton.setSelected(true);
    stopChronometer();
  }

  /**
   * Select the "reset" button and reset the chronometer.
   */
  public void reset()
  {
    resetButton.setSelected(true);
    resetChronometer();
  }

  /**
   * Check whether the chronometer is running.
   * @return    True if yes, false if no.
   */
  public boolean isRunning()
  {
    return running;
  }

  /**
   * Handle shared object event received at "tick" plug.
   * @param     soe     The event.
   */
  void handleTick(SharedObjectEvent soe)
  {
    Object so = soe.getSharedObject();
    Tick newTick = (Tick)so;
    synchronized (interval) {
      if (running) {
        interval.advance(newTick.getTick());
        setDisplay(interval.hour, interval.min, interval.sec);
        updateSharedObjects();
        fireChronometerListeners(VALUECHANGED);
      }
    }
  }

  /**
   * Handle shared object event received at "time in hours" plug.
   * @param     so      The shared object received
   */
  void handleHours(SharedObject so)
  {
    Number num = ((NumberSO)so).value();
    double hours = num.doubleValue();
    setMilliseconds(hours * (double)MSECPERHOUR);
  }

  /**
   * Handle shared object event received at "time in minutes" plug.
   * @param     so      The shared object received.
   */
  void handleMins(SharedObject so)
  {
    Number num = ((NumberSO)so).value();
    double mins = num.doubleValue();
    setMilliseconds(mins * (double)MSECPERMIN);
  }

  /**
   * Handle shared object event received at "time in seconds" plug.
   * @param     so      The shared object received.
   */
  void handleSecs(SharedObject so)
  {
    Number num = ((NumberSO)so).value();
    double secs = num.doubleValue();
    setMilliseconds(secs * (double)MSECPERSEC);
  }

  /**
   * Handle shared object event received at "time in milliseconds" plug.
   * @param     so      The shared object received.
   */
  void handleMillis(SharedObject so)
  {
    Number num = ((NumberSO)so).value();
    double msecs = num.doubleValue();
    setMilliseconds(msecs);
  }

  /**
   * Set the time on the Chronometer's display.
   * @param     h       Hours.
   * @param     m       Minutes.
   * @param     s       Seconds.
   */
  private void setDisplay(int h, int m, int s)
  {
    display.setText(twoDigits(h) + ":" + twoDigits(m) + ":" + twoDigits(s));
  }

  /**
   * Set the elapsed time to a given value.
   * @param     time    The new time value.
   */
  public void setTime(TimeCount time)
  {
    synchronized (interval) {
      interval.hour = time.hour;
      interval.min = time.min;
      interval.sec = time.sec;
      interval.usec = time.usec;
      setDisplay(interval.hour, interval.min, interval.sec);
      updateSharedObjects();
      fireChronometerListeners(VALUECHANGED);
    }
  }

  /**
   * Return the elapsed time.
   * @return    The requested time.
   */
  public TimeCount getTime()
  {
    TimeCount t;
    synchronized (interval) {
      t = new TimeCount(interval.hour, interval.min, interval.sec);
      t.usec = interval.usec;
    }
    return t;
  }

  /**
   * Set the elapsed time to a given value in milliseconds.
   * @param     msecs   The new time value in milliseconds.
   */
  public void setMilliseconds(double msecs)
  {
    synchronized (interval) {
      interval.hour = (int)(msecs / MSECPERHOUR);
      msecs -= (interval.hour * MSECPERHOUR);
      interval.min = (int)(msecs / MSECPERMIN);
      msecs -= (interval.min * MSECPERMIN);
      interval.sec = (int)(msecs / MSECPERSEC);
      msecs -= (interval.sec * MSECPERSEC);
      interval.usec = (int)(msecs * USECPERMSEC);
      setDisplay(interval.hour, interval.min, interval.sec);
      updateSharedObjects();
      fireChronometerListeners(VALUECHANGED);
    }
  }

  /**
   * Return the elapsed time in milliseconds.
   * @return    The requested time in milliseconds.
   */
  public double getMilliseconds()
  {
    double msecs;
    synchronized (interval) {
      msecs = interval.hour * (double)(SECSPERHOUR * MSECPERSEC)
            + interval.min * (double)(SECSPERMIN * MSECPERSEC)
            + interval.sec * (double)MSECPERSEC
            + interval.usec / (double)USECPERMSEC;
    }
    return msecs;
  }

  /**
   * Updates the shared objects that export the chronometer's value.
   */
  private void updateSharedObjects()
  {
    double millis =
      (double)(interval.hour * MSECPERHOUR + interval.min * MSECPERMIN +
               interval.sec * MSECPERSEC) +
      (double)interval.usec / (double)USECPERMSEC;
    double secs = millis / (double)MSECPERSEC;
    double mins = millis / (double)MSECPERMIN;
    double hours = millis / (double)MSECPERHOUR;
    displayHours.setValue(hours);
    displayMins.setValue(mins);
    displaySecs.setValue(secs);
    displayMillis.setValue(millis);
  }

  /**
   * Convert an integer to a two-digit string.
   * @param     x       The integer to convert.
   * @return    A two-digit string representing the method's parameter.
   *            The string is obtained by prepending a 0 to the integer's
   *            string representation and truncating all but the last two
   *            characters of this string. (I.e., it only works for
   *            non-negative integers.)
   */
  private static String twoDigits(int x)
  {
    if (x < 10) {
      return "0" + Integer.toString(x);
    }else{
      return Integer.toString(x % 100);
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
    interval = new TimeCount(0, 0, 0);
    displayHours = new NumberSO(handle, 0.0);
    displayMins = new NumberSO(handle, 0.0);
    displaySecs = new NumberSO(handle, 0.0);
    displayMillis = new NumberSO(handle, 0.0);
    
    try {
      Plug plug = new SingleInputPlug(
        handle, resources, "tick", Color.yellow,
        gr.cti.eslate.sharedObject.Tick.class,
        new SharedObjectListener()
        {
          public synchronized void
            handleSharedObjectEvent(SharedObjectEvent soe)
          {
            Chronometer.this.handleTick(soe);
          }
        }
      );
      handle.addPlug(plug);
      /*
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          //if (startButton.isSelected()) {
          //  running = true;
          //}
        }
      });
      plug.addDisconnectionListener(new DisconnectionListener() {
        public void handleDisconnectionEvent(DisconnectionEvent e)
        {
          //if (running) {
          //  running = false;
          //}
        }
      });
      */

      plug = new SingleInputMultipleOutputPlug(
        handle, resources, "hours", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, displayHours,
        new SharedObjectListener()
        {
          public synchronized void
            handleSharedObjectEvent(SharedObjectEvent soe)
            {
              Chronometer.this.handleHours(soe.getSharedObject());
            }
        }
      );
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get elapsed time value from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            SharedObject so =
              ((SharedObjectPlug)(e.getPlug())).getSharedObject();
            handleHours(so);
          }
        }
      });

      plug = new SingleInputMultipleOutputPlug(
        handle, resources, "mins", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, displayMins,
        new SharedObjectListener()
        {
          public synchronized void
            handleSharedObjectEvent(SharedObjectEvent soe)
            {
              Chronometer.this.handleMins(soe.getSharedObject());
            }
        }
      );
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get elapsed time value from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            SharedObject so =
              ((SharedObjectPlug)(e.getPlug())).getSharedObject();
            handleMins(so);
          }
        }
      });

      plug = new SingleInputMultipleOutputPlug(
        handle, resources, "secs", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, displaySecs,
        new SharedObjectListener()
        {
          public synchronized void
            handleSharedObjectEvent(SharedObjectEvent soe)
            {
              Chronometer.this.handleSecs(soe.getSharedObject());
            }
        }
      );
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get elapsed time value from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            SharedObject so =
              ((SharedObjectPlug)(e.getPlug())).getSharedObject();
            handleSecs(so);
          }
        }
      });

      plug = new SingleInputMultipleOutputPlug(
        handle, resources, "millis", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, displayMillis,
        new SharedObjectListener()
        {
          public synchronized void
            handleSharedObjectEvent(SharedObjectEvent soe)
            {
              Chronometer.this.handleMillis(soe.getSharedObject());
            }
        }
      );
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get elapsed time value from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            SharedObject so =
              ((SharedObjectPlug)(e.getPlug())).getSharedObject();
            handleMillis(so);
          }
        }
      });

    } catch (InvalidPlugParametersException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    } catch (PlugExistsException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    } catch (NoClassDefFoundError e) {
      // Tick.class or NumberSO.class is not available during run-time.
      // We simply do not produce the associated plug.
    }

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.ChronometerPrimitives"
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

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 3);

    map.put(INTERVAL, interval);
    map.put(RUNNING, running);
    map.put(STARTED, startButton.isSelected());
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

    running = false;
    boolean r, started;

    Object obj = (oi.readObject());
    if (obj instanceof StorageStructure) {
      StorageStructure map = (StorageStructure)obj;
      interval = (TimeCount)(map.get(INTERVAL));
      r = map.get(RUNNING, false);
      started = map.get(STARTED, false);
      setMinimumSize(map.get(MINIMUM_SIZE, getPreferredSize()));
      setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(map.get(PREFERRED_SIZE, getPreferredSize()));
    }else{
      interval = (TimeCount)obj;
      // This field was introduced in version 1.25.
      try {
        r = oi.readBoolean();
      } catch (EOFException e) {
        r = false;
      }
      // This field was introduced in version 1.25.
      try {
        started = oi.readBoolean();
      } catch (EOFException e) {
        started = false;
      }
    }

    setDisplay(interval.hour, interval.min, interval.sec);
    startButton.setSelected(started);
    stopButton.setSelected(!started);
    running = r;

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
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    ESlateHandle.removeAllRecursively(this);
    pane = null;
    startButton = null;
    stopButton = null;
    resetButton = null;
    interval = null;
    displayMillis = null;
    display = null;
    resources = null;
    chronometerListeners.clear();
    chronometerListeners = null;
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
    if (handle != null) {
      handle.dispose();
    }
  }

  /**
   * Add a listener for chronometer events.
   * @param     listener        The listener to add.
   */
  public void addChronometerListener(ChronometerListener listener)
  {
    synchronized (chronometerListeners) {
      if (!chronometerListeners.contains(listener)) {
        chronometerListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for chronometer events.
   * @param     listener        The listener to remove.
   */
  public void removeChronometerListener(ChronometerListener listener)
  {
    synchronized (chronometerListeners) {
      int ind = chronometerListeners.indexOf(listener);
      if (ind >= 0) {
        chronometerListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for chronometer events.
   * @param     what    One of START, STOP, VALUECHANGED, indicating that
   *                    the chronometer has been started, stopped, or that the
   *                    chronometer's value has changed.
   */
  @SuppressWarnings(value={"unchecked"})
  private void fireChronometerListeners(int what)
  {
    ArrayList<ChronometerListener> listeners;
    int size;
    synchronized(chronometerListeners) {
      size = chronometerListeners.size();
      if (size > 0) {
        listeners =
          (ArrayList<ChronometerListener>)(chronometerListeners.clone());
      }else{
        return; // Nothing to do
      }
    }
    ChronometerEvent e = new ChronometerEvent(handle);
    for (int i=0; i<size; i++) {
      ChronometerListener l = (ChronometerListener)(listeners.get(i));
      e.hour = interval.hour;
      e.min = interval.min;
      e.sec = interval.sec;
      e.usec = interval.usec;
      switch (what) {
        case VALUECHANGED:
          l.valueChanged(e);
          break;
        case START:
          l.chronometerStarted(e);
          break;
        case STOP:
          l.chronometerStopped(e);
          break;
      }
    }
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
