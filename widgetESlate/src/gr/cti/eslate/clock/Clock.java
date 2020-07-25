package gr.cti.eslate.clock;


import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleInputMultipleOutputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputMultipleOutputPlug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.sharedObject.SharedObject;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.scripting.AsClock;
import gr.cti.eslate.sharedObject.DateSO;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.sharedObject.Tick;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;


public class Clock extends JPanel implements ESlatePart, Externalizable, AsClock {
    GregorianCalendar calendar = new GregorianCalendar();
    private Tick tickSO;
    Date date = new Date();
    private NumberSO displayHours;
    private NumberSO displayMins;
    private NumberSO displaySecs;
    //private GMT gmtSO;
    private DateSO dateSO;
    private DateSO timeSO;
    private Plug plug1,plug2,plug3,plug4,plug5,plug6;
    // public Date date;
    boolean showAnalogFace;
    private static final int  S_VERSION = 2;
    private ESlateHandle handle;
    protected ResourceBundle resources;
    public TimeCount currentGMT;
    private final static long SECPERMIN = 60L;
    private final static long SECPERHOUR = 60L * SECPERMIN;
    static final long serialVersionUID = 6235497077822565526L;
    private boolean run;
    public boolean componentClosed = false;
    private boolean plugsCreated = false;
    boolean countBackwards = false;
    String originalName;
    File backImageFile;
    Icon initialIcon = null;
    Date currentDate;
    private final String version = "3.0.5";
    private AnalogClock analogClock;
    DigitalClock digitalClock;


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
    
    private ClockThread clockThread;
    


    /**
     * Returns Copyright information.
     * @return	The Copyright information.
     */

    private ESlateInfo getInfo() {
        String[] info = {
                resources.getString("part"),
                resources.getString("development"),
                //resources.getString("funding"),
                resources.getString("copyright")
            };

        return new ESlateInfo(
                resources.getString("componentName") + " " +
                resources.getString("version") + " " + version,
                info);
    }

    /**
     * Constructs a new Clock
     *
     */

    public Clock() {
        resources = ResourceBundle.getBundle("gr.cti.eslate.clock.ClockBundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        setOpaque(true);
        setLayout(new BorderLayout());
        showAnalogFace = true;
        currentGMT = new TimeCount(0, 0, 0);
        analogClock = new AnalogClock();
        analogClock.logoString = resources.getString("E-Slate");
        setBorder(new NoTopOneLineBevelBorder(0));
        digitalClock = new DigitalClock();
        calendar.setTime(new Date());
        //add(digitalClock,BorderLayout.CENTER);
        //add(analogClock,BorderLayout.CENTER);
        setAppearance(showAnalogFace);
        handle = getESlateHandle();
        setTime(new TimeCount(0, 0, 0));
        run = false;
        
        if (clockThread == null) {
            clockThread = new ClockThread();
        }
        clockThread.setDaemon(true);
        clockThread.start();

        analogClock.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    double dist = 0;
                    java.awt.Point point = new Point(e.getX(), e.getY());

                    if (analogClock.hourHand.getPolygon().contains(point)) {
                        analogClock.inHourHand = true;
                        //System.out.println("HOUR HAND!");
                    } else
                        analogClock.inHourHand = false;
                    if (analogClock.minuteHand.getPolygon().contains(point)) {
                        //System.out.println("MINUTE HAND!");
                        analogClock.inMinuteHand = true;
                        analogClock.inHourHand = false;
                    } else
                        analogClock.inMinuteHand = false;
                    int X1 = analogClock.sweep.transX[1];
                    int X2 = analogClock.sweep.transX[2];
                    int Y1 = analogClock.sweep.transY[1];
                    int Y2 = analogClock.sweep.transY[2];

                    dist = analogClock.dist(new Point(e.getX() - getSize().width / 2, e.getY() - getSize().height / 2), X1 - getSize().width / 2, Y1 - getSize().height / 2, X2 - getSize().width / 2, Y2 - getSize().height / 2);
                    if (Math.abs(dist) < 2.5) {
                        analogClock.inSecondsHand = true;
                        analogClock.inMinuteHand = false;
                        analogClock.inHourHand = false;
                    } else
                        analogClock.inSecondsHand = false;
                }
            }
        );

        analogClock.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int minutesBefore = analogClock.cur_time.minutes;
                    int hourBefore = analogClock.cur_time.hours;
                    int secondsBefore = analogClock.cur_time.sec;

                    if (analogClock.inHourHand) {
                        int k = e.getX() - Clock.this.getWidth() / 2;
                        int l = e.getY() - Clock.this.getHeight() / 2;
                        //System.out.println(" k : "+ k +"l : " +l);
                        double angle = Math.abs(Math.atan2(k, l) - 3.141592653589793);

                        //System.out.println("Hour is : "+(new Double(angle/analogClock.HOUR)).intValue());
                        if (hourBefore > 9 && hourBefore < 12) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() >= 3) {
                                analogClock.cur_time.hours = (new Double(angle / AnalogClock.HOUR)).intValue();
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours =(12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        } else if (hourBefore <= 9 && hourBefore > 3) {
                            //if ((new Double(angle/analogClock.HOUR)).intValue()>=3 && (new Double(angle/analogClock.HOUR)).intValue()<9){
                            analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                            currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            //}else if {
                            //analogClock.cur_time.setHours(12+(new Double(angle/analogClock.HOUR)).intValue());
                            //currentGMT.hour = 12+(new Double(angle/analogClock.HOUR)).intValue();
                            //}
                        } else if (hourBefore <= 3) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() >= 9) {
                                analogClock.cur_time.hours = (12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        } else if (hourBefore > 3 && hourBefore < 9) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() <= 9) {
                                analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours = (12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        } else if (hourBefore > 21) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() >= 3) {
                                analogClock.cur_time.hours = (12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        } else if (hourBefore <= 21 && hourBefore > 9 && hourBefore > 15) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() >= 3) {
                                analogClock.cur_time.hours = (12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        } else if (hourBefore <= 15 && hourBefore > 3 && hourBefore > 9) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() >= 9) {
                                analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours = (12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        } else if (hourBefore > 15 && hourBefore < 21) {
                            if ((new Double(angle / AnalogClock.HOUR)).intValue() <= 9) {
                                analogClock.cur_time.hours = (12 + (new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = 12 + (new Double(angle / AnalogClock.HOUR)).intValue();
                            } else {
                                analogClock.cur_time.hours = ((new Double(angle / AnalogClock.HOUR)).intValue());
                                currentGMT.hour = (new Double(angle / AnalogClock.HOUR)).intValue();
                            }
                        }
                        analogClock.repaint();
                        digitalClock.setTime(currentGMT);
                        setTime(currentGMT);
                    } else if (analogClock.inMinuteHand) {
                        int k = e.getX() - Clock.this.getWidth() / 2;
                        int l = e.getY() - Clock.this.getHeight() / 2;
                        //System.out.println(" k : "+ k +"l : " +l);
                        double angle = Math.abs(Math.atan2(k, l) - 3.141592653589793);

                        //System.out.println("Minute is : "+(new Double(angle/analogClock.MINSEC)).intValue());

                        analogClock.cur_time.minutes = ((new Double(angle / AnalogClock.MINSEC)).intValue());
                        currentGMT.min = (new Double(angle / AnalogClock.MINSEC)).intValue();
                        if (analogClock.cur_time.minutes >= 45 && minutesBefore <= 15)
                            analogClock.cur_time.hours = (hourBefore - 1);
                        else if (analogClock.cur_time.minutes < 15 && minutesBefore >= 45)
                            analogClock.cur_time.hours = (hourBefore + 1);

                        analogClock.repaint();
                        currentGMT.hour = (new Double(analogClock.cur_time.get_hours())).intValue();
                        digitalClock.setTime(currentGMT);
                        setTime(currentGMT);
                    } else if (analogClock.inSecondsHand) {
                        int k = e.getX() - analogClock.getWidth() / 2;
                        int l = e.getY() - analogClock.getHeight() / 2;
                        //System.out.println(" k : "+ k +"l : " +l);
                        double angle = Math.abs(Math.atan2(k, l) - 3.141592653589793);

                        currentGMT.sec = (new Double(angle / AnalogClock.MINSEC)).intValue();
                        if (currentGMT.sec >= 45 && secondsBefore <= 15)
                            if (minutesBefore == 0) {
                                currentGMT.min = 59;
                                if (hourBefore == 0)
                                    currentGMT.hour = 23;
                                else
                                    currentGMT.hour = hourBefore - 1;
                            }else
                                currentGMT.min = minutesBefore - 1;
                        else if (currentGMT.sec <= 15 && secondsBefore >= 45)
                            if (minutesBefore == 59) {
                                currentGMT.min = 0;
                                if (hourBefore == 23)
                                    currentGMT.hour = 0;
                                else
                                    currentGMT.hour = hourBefore + 1;
                            } else
                                currentGMT.min = minutesBefore + 1;

                        //analogClock.repaint();
                        //currentGMT.hour = (new Double(analogClock.cur_time.get_hours())).intValue();
                        //System.out.println("CurrentGMT.hour = " + currentGMT.hour);
                        //currentGMT.min = analogClock.cur_time.getMinutes();
                        //digitalClock.setTime(currentGMT);
                        setTime(currentGMT);
                        //System.out.println("Second is : " + currentGMT.sec + " minute is : "+currentGMT.min);

                    }
                    //
                    //CODE FOR IMAGE CHECKING _ USE WHEN WANT TO VERIFY THAT WHAT YOU SEE IS WHAT YOU GET

                    //ImageIcon previewPicture = new ImageIcon(AnalogClock.this.offScrImage);
                    //MyClass1 imageDialog = new MyClass1(previewPicture);
                    //imageDialog.setSize(100+previewPicture.getIconWidth(),100+previewPicture.getIconHeight());
                    //imageDialog.show();
                }
            }
        );
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
    }

    /**
     * Returns clock's handle.
     * @return	The ESlateHandle
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);
            handle.setInfo(getInfo());

            try {
                handle.setUniqueComponentName(resources.getString("Clock"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }

            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ClockPrimitives");

            handle.addESlateListener(new ESlateAdapter() {
                    public void disposingHandle(HandleDisposalEvent e) {
                        componentClosed = true;
                    }

                    public void handleDisposed(HandleDisposalEvent e) {
                        componentClosed = true;
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();

                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );
            setPlugsUsed(true);
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    void handleHours(SharedObjectEvent soe) {
        Object so = soe.getSharedObject();
        Number num = ((NumberSO) so).value();
        double hours = num.doubleValue();
        setSeconds(Math.abs(hours) * SECPERHOUR);
    }

    void handleMins(SharedObjectEvent soe) {
        Number num;
        double mins;
        Object so = soe.getSharedObject();

        if (((NumberSO) so).value() == null) {
            mins = currentGMT.hour * 60 + (double) currentGMT.min + (double) currentGMT.sec / 60;
        } else {
            num = ((NumberSO) so).value();
            mins = num.doubleValue();
        }
        setSeconds(Math.abs(mins) * SECPERMIN);
    }

    void handleSecs(SharedObjectEvent soe) {
        Object so = soe.getSharedObject();
        Number num = ((NumberSO) so).value();
        double secs = num.doubleValue();
        setSeconds(Math.abs(secs));

    }


    /**
     * Set the elapsed time to a given value in seconds.
     * @param     secs    The new time value in seconds.
     */
    public void setSeconds(double secs) {
        synchronized (currentGMT) {
            currentGMT.hour = (int) (secs / SECPERHOUR);
            secs -= (currentGMT.hour * SECPERHOUR);
            currentGMT.min = (int) (secs / SECPERMIN);
            secs -= (currentGMT.min * SECPERMIN);
            currentGMT.sec = (int) secs;
            setTime(currentGMT.hour, currentGMT.min, currentGMT.sec);
            if (plugsCreated)
                updateSharedObjects();
            //fireChronometerListeners(VALUECHANGED);
        }
    }

    private void updateSharedObjects() {
        if (!componentClosed) {
            double seconds = currentGMT.hour * SECPERHOUR + currentGMT.min * SECPERMIN + currentGMT.sec;
            double mins = seconds / SECPERMIN;
            double hours = seconds / SECPERHOUR;
            displayHours.setValue(hours);
            displayMins.setValue(mins);
            displaySecs.setValue(seconds);
            timeSO.setDate(getTime());
            dateSO.setDate(date);
        }
    }

    /**

     * Sets clock's time
     * @param	time The time
     */


    public void setTime(TimeCount time) {
        setTime(time.hour, time.min, time.sec);
    }

    public void setTime(int hour, int min, int sec) {
        synchronized (currentGMT) {
            currentGMT.hour = hour;
            currentGMT.min = min;
            currentGMT.sec = sec;
            if (plugsCreated)
                updateSharedObjects();
            analogClock.setTime(hour, min, sec);
            digitalClock.setTime(hour, min, sec);

        }
    }

    /*
     public TimeCount getTime(){
     Calendar calendar=new Calendar();
     calendar.set(calendar.HOUR_OF_DAY, int currentGMT.hour);
     calendar.set(calendar.MINUTE, int currentGMT.min);
     calendar.set(calendar.SECOND, int currentGMT.sec);
     TimeDataModel t = calendar.getTime();
     TimeCount t;
     synchronized (currentGMT) {
     t = new TimeCount(currentGMT.hour, currentGMT.min, currentGMT.sec);
     t.usec = currentGMT.usec;
     }
     return t;
     }
     */

    /**
     * Sets clock's appearance (analog or digital)
     * @param	analogFace True for analog appearance , false for digital
     */

    public void setAppearance(boolean analogFace) {
        showAnalogFace = analogFace;
        if (analogFace == true) {
            add(analogClock, BorderLayout.CENTER);
            if (digitalClock != null)
                remove(digitalClock);
        } else {
            add(digitalClock, BorderLayout.CENTER);
            remove(analogClock);
        }
        revalidate();
        repaint();

    }

    /**
     * Returns clock's appearance (analog or digital)
     * @return True for analog appearance , false for digital
     */

    public boolean getAppearance() {
        return showAnalogFace;
    }

    /**
     * Sets clock to follow system time
     * @param	run True for showing system time, false otherwise
     */

    public void setSystemTimeCounting(boolean run) {
        this.run = run;
        setDate(new Date());
        if (run == false) {
            //setTime(new TimeCount(0,0,0));
            revalidate();
            repaint();
        }
    }

    /**
     * Returns wheteher clock follows system time or not
     * @return True if clock is showing system time, false otherwise
     */

    public boolean getSystemTimeCounting() {
        return run;
    }

    /**
     * Returns backgroung image icon
     * @return icon The icon whose image will be used for background
     */

    public Icon getBackgroundImageIcon() {
        return initialIcon;

    }

    /**
     * Sets background image icon
     * @param	icon The icon whose image will be used for background
     */

    public void setBackgroundImageIcon(Icon icon) {
        initialIcon = icon;
        analogClock.backgroundImageIcon = (NewRestorableImageIcon) toRestorableImageIcon(initialIcon);
        analogClock.repaint();
    }

    private Icon toRestorableImageIcon(Icon icon) {
        if (icon == null || icon instanceof NewRestorableImageIcon)
            return icon;
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

        icon.paintIcon(this, b.getGraphics(), 0, 0);
        return new NewRestorableImageIcon(b);
    }

    /**
     * Reads from the ESlateFieldMap to restore stored values and properties
     */

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);
        Object firstObj = in.readObject();

        /*if (!ESlateFieldMap.class.isAssignableFrom(firstObj.getClass())) {
         // Old time readExtermal()
         oldTimeReadExternal(in, firstObj);
         return;
         } */

        StorageStructure fieldMap = (StorageStructure) firstObj;

        setSystemTimeCounting(fieldMap.get("SystemTimeCounting", getSystemTimeCounting()));
        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("DigitalReedingsFont"))
            setDigitalReedingsFont((Font) fieldMap.get("DigitalReedingsFont", getDigitalReedingsFont()));
        if (fieldMap.containsKey("LogoTextFont"))
            setLogoTextFont((Font) fieldMap.get("LogoTextFont", getLogoTextFont()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));

        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));

        setDateFormat(fieldMap.get("DateFormat", getDateFormat()));

        setTime((Date) fieldMap.get("Time", getTime()));
        setDate((Date) fieldMap.get("Date", getDate()));
        setLogoText(fieldMap.get("LogoText", getLogoText()));
        //setCaretPosition(fieldMap.get("CaretPosition",getCaretPosition()));

        //setName(fieldMap.get("Name",getName()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setDateVisible(fieldMap.get("DateVisible", getDateVisible()));
        setTime((TimeCount) fieldMap.get("currentGMT"));
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        setHourHandColor(fieldMap.get("HourHandColor", getHourHandColor()));
        setMinuteHandColor(fieldMap.get("MinuteHandColor", getMinuteHandColor()));
        setSweepHandColor(fieldMap.get("SweepHandColor", getSweepHandColor()));
        setFaceColor(fieldMap.get("FaceColor", getFaceColor()));
        setHourNumberShowing(fieldMap.get("HourNumberShowing", getHourNumberShowing()));
        setMinNumberShowing(fieldMap.get("MinNumberShowing", getMinNumberShowing()));
        setCaseColor(fieldMap.get("CaseColor", getCaseColor()));
        setTextColor(fieldMap.get("TextColor", getTextColor()));
        if (fieldMap.getDataVersion().indexOf("1.") == 0)
            setAppearance(false);
        else
            setAppearance(fieldMap.get("Appearance", getAppearance()));

        if (fieldMap.containsKey("BackgroundImage")) {
            try {
                setBackgroundImageIcon((NewRestorableImageIcon) fieldMap.get("BackgroundImage", getBackgroundImageIcon()));
            } catch (Throwable thr) {}
        }

        setMaximumSize(fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize(fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize(fieldMap.get("PreferredSize", getPreferredSize()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));
        else
            setPlugsUsed(true);
        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
                //analogClock.setBorder(bd.getBorder());
                //digitalClock.setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }
        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Writes to the ESlateFieldMap to store values and properties
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(saveTimer);
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(S_VERSION);

        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getDigitalReedingsFont() instanceof UIResource))
            fieldMap.put("DigitalReedingsFont", getDigitalReedingsFont());
        if (!(getLogoTextFont() instanceof UIResource))
            fieldMap.put("LogoTextFont", getLogoTextFont());

        fieldMap.put("Enabled", isEnabled());

        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("LogoText", getLogoText());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("Time", getTime());
        fieldMap.put("Date", getDate());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());

        fieldMap.put("ToolTipText", getToolTipText());

        fieldMap.put("currentGMT", currentGMT);
        //         System.out.println("currentGMT : "+currentGMT.hour+ " , "+currentGMT.min+" , "+currentGMT.sec);
        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        fieldMap.put("DateVisible", getDateVisible());
        fieldMap.put("HourHandColor", getHourHandColor());
        fieldMap.put("MinuteHandColor", getMinuteHandColor());
        fieldMap.put("SweepHandColor", getSweepHandColor());
        fieldMap.put("FaceColor", getFaceColor());
        fieldMap.put("CaseColor", getCaseColor());
        fieldMap.put("TextColor", getTextColor());
        fieldMap.put("SystemTimeCounting", getSystemTimeCounting());
        fieldMap.put("DateFormat", getDateFormat());

        fieldMap.put("HourNumberShowing", getHourNumberShowing());
        fieldMap.put("MinNumberShowing", getMinNumberShowing());

        if (initialIcon != null) {
            fieldMap.put("BackgroundImage", toRestorableImageIcon(initialIcon));
        }

        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());

        fieldMap.put("Appearance", getAppearance());
        fieldMap.put("PlugsUsed", getPlugsUsed());

        JPanel test = new JPanel();

        if (getBorder() != null && !(getBorder() instanceof UIResource) && !getBorder().equals(test.getBorder())) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        } else if (getBorder() == null) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        }
        out.writeObject(fieldMap);
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Sets background color
     * @param	c The background color
     */

    public void setBackground(Color c) {
        super.setBackground(c);
        if (analogClock != null && digitalClock != null) {
            analogClock.bgColor = c;
            digitalClock.panel.setBackground(c);
            digitalClock.face.setBackground(c);
        }
    }

    /**
     * Returns background color
     * @return	c The background color
     */

    public Color getBackground() {
        Color c = super.getBackground();

        if (analogClock != null && digitalClock != null) {
            c = analogClock.bgColor;
        }
        return c;
    }

    /**
     * Sets hour hand color
     * @param	c The hour hand color
     */

    public void setHourHandColor(Color c) {
        if (analogClock != null)
            analogClock.hourColor = c;
        analogClock.repaint();
    }

    /**
     * Returns hour hand color
     * @return	c The hour hand color
     */

    public Color getHourHandColor() {
        Color c = null;

        if (analogClock != null)
            c = analogClock.hourColor;
        return c;
    }

    /**
     * Sets minute hand color
     * @param	c The minute hand color
     */

    public void setMinuteHandColor(Color c) {
        if (analogClock != null)
            analogClock.minuteColor = c;
        analogClock.repaint();
    }

    /**
     * Returns minute hand color
     * @return	c The minute hand color
     */

    public Color getMinuteHandColor() {
        Color c = null;

        if (analogClock != null)
            c = analogClock.minuteColor;
        return c;
    }

    /**
     * Sets sweep hand color
     * @param	c The sweep hand color
     */

    public void setSweepHandColor(Color c) {
        if (analogClock != null)
            analogClock.sweepColor = c;
        analogClock.repaint();
    }

    /**
     * Returns sweep hand color
     * @return	c The sweep hand color
     */

    public Color getSweepHandColor() {
        Color c = null;

        if (analogClock != null)
            c = analogClock.sweepColor;
        return c;
    }

    /**
     * Sets clock face color
     * @param	c The clock face color
     */

    public void setFaceColor(Color c) {
        analogClock.faceColor = c;
        analogClock.repaint();
    }

    /**
     * Returns clock face color
     * @return	c The clock face color
     */

    public Color getFaceColor() {
        Color c = null;

        c = analogClock.faceColor;
        return c;
    }

    /**
     * Sets clock case color
     * @param	c The clock case color
     */

    public void setCaseColor(Color c) {
        analogClock.caseColor = c;
        analogClock.repaint();
    }

    /**
     * Returns clock case color
     * @return	c The clock case color
     */

    public Color getCaseColor() {
        Color c = null;

        c = analogClock.caseColor;
        return c;
    }

    /**
     * Sets text reedings color
     * @param	c The text reedings color
     */

    public void setTextColor(Color c) {
        if (analogClock != null) {
            analogClock.textColor = c;
            analogClock.repaint();
            digitalClock.face.setForeground(c);
            digitalClock.repaint();
        }
    }

    /**
     * Returns text reedings color
     * @return	c The text reedings color
     */

    public Color getTextColor() {
        Color c = null;

        if (analogClock != null)
            c = analogClock.textColor;
        return c;
    }

    /**
     * Sets logo text
     * @param	s The logo text
     */

    public void setLogoText(String s) {
        if (analogClock != null)
            analogClock.logoString = s;
        analogClock.repaint();
    }

    /**
     * Returns the logo text
     * @return s The logo text
     */

    public String getLogoText() {
        String s = "";

        if (analogClock != null)
            s = analogClock.logoString;
        return s;
    }

    /**
     * Sets the digital reedings font
     * @param	f The digital reedings font
     */

    public void setDigitalReedingsFont(Font f) {
        if (analogClock != null) {
            digitalClock.face.setFont(f);
            digitalClock.revalidate();
        }
    }

    /**
     * Returns the digital reedings font
     * @return f The digital reedings font
     */

    public Font getDigitalReedingsFont() {
        Font f = null;

        if (analogClock != null)
            f = digitalClock.face.getFont();
        return f;
    }

    /**
     * Sets the logo text font
     * @param	f The logo text font
     */

    public void setLogoTextFont(Font f) {
        if (analogClock != null) {
            analogClock.font = f;
            analogClock.repaint();
        }
    }

    /**
     * Returns the logo text font
     * @return f The logo text font
     */

    public Font getLogoTextFont() {
        Font f = null;

        if (analogClock != null)
            f = analogClock.font;
        return f;
    }

    /**
     * Sets whether clock is opaque or not
     * @param opaque True if opaque
     */

    public void setOpaque(boolean opaque) {
        if (analogClock != null) {
            if (!opaque) {
                super.setBackground(new Color(0, 0, 0, 0));
                analogClock.setOpaque(opaque);
                //            analogClock.setBackground(new Color(0,0,0,0));
                //            analogClock.bgColor = new Color(0,0,0,0);
            }
            analogClock.setOpaque(opaque);
            analogClock.repaint();

            digitalClock.setOpaque(opaque);
            digitalClock.panel.setOpaque(opaque);
            digitalClock.face.setOpaque(opaque);
            digitalClock.revalidate();
            doLayout();
        }
    }

    /**
     * Returns clock opaqueness
     * @return true if clock opaque
     */

    public boolean isOpaque() {
        if (analogClock != null)
            return analogClock.isOpaque();
        return true;
    }

    /*public void setCountingBackwards(boolean b){
     countBackwards = b;
     analogClock.countBackwards = b;
     digitalClock.countBackwards = b;
     }

     public boolean getCountingBackwards(){
     return countBackwards;
     }

     public void setAIcon(NewRestorableImageIcon icon){
     }
     public NewRestorableImageIcon getAIcon(){
     return null;
     }
     */

    /**
     * Sets whether hour numbers are visible in analog appearance of the clock
     * @param show True if numbers are visible
     */

    public void setHourNumberShowing(boolean show) {
        analogClock.showNumbers = show;
        analogClock.repaint();
    }

    /**
     * Returns whether hour numbers are visible in analog appearance of the clock
     * @return true if numbers are visible
     */

    public boolean getHourNumberShowing() {
        return analogClock.showNumbers;
    }

    /**
     * Sets whether minute numbers are visible in analog appearance of the clock
     * @param show True if numbers are visible
     */

    public void setMinNumberShowing(boolean show) {
        analogClock.showMinNums = show;
        analogClock.repaint();
    }

    /**
     * Returns whether minute numbers are visible in analog appearance of the clock
     * @return true if numbers are visible
     */

    public boolean getMinNumberShowing() {
        return analogClock.showMinNums;
    }

    /**
     * Returns clock's time
     * @return The clock time
     * Notice : Only time values are valid, not date.
     * For example if d is 31/1/2000 12:43:10 then only 12:43:10 will be used
     */

    public Date getTime() {
        calendar.set(Calendar.HOUR_OF_DAY, currentGMT.hour);
        calendar.set(Calendar.MINUTE, currentGMT.min);
        calendar.set(Calendar.SECOND, currentGMT.sec);

        //      System.out.println("getTime called : "+calendar.getTime());
        return calendar.getTime();
    }

    /**
     * Sets clock's time
     * @param d The clock time
     * Notice : Only time values are used, not date.
     * For example if d is 31/1/2000 12:43:10 then only 12:43:10 will be used
     */

    public void setTime(Date d) {
        calendar.setTime(d);
        currentGMT.hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        currentGMT.min = calendar.get(GregorianCalendar.MINUTE);
        currentGMT.sec = calendar.get(GregorianCalendar.SECOND);
        this.setTime(currentGMT);
        if (plugsCreated)
            updateSharedObjects();
    }

    /**

     * Sets clock's date
     * @param d The clock date
     * Notice : Only date values are used, not time.
     * For example if d is 31/1/2000 12:43:10 then only 31/1/2000 will be used
     */


    public void setDate(Date d) {
        this.date = d;
        analogClock.setDate(d);
        analogClock.repaint();
        digitalClock.setDate(d);
        if (plugsCreated)
            dateSO.setDate(d);//updateSharedObjects(null);

    }

    /*   private void dateUpdate(GregorianCalendar calendar){

     this.year = calendar.get(calendar.YEAR);

     this.month = calendar.get(calendar.MONTH);

     this.date = calendar.get(calendar.DAY_OF_MONTH);

     System.out.println("Year : "+this.year+" month : "+this.month + " Date : "+this.date );

     }*/




    /**

     * Returns clock's date
     * @return The clock date
     * Notice : Only date values are valid, not time.
     * For example if d is 31/1/2000 12:43:10 then only 31/1/2000 will be used
     */


    public Date getDate() {

        //      System.out.println("Returning date to property editor ... : " + date);

        return date;

    }

    /**

     * Sets date format
     * @param	pattern The format pattern
     */


    public void setDateFormat(String pattern) {

        analogClock.pattern = pattern;

        analogClock.repaint();

    }

    /**

     * Returns date format
     * @return The format pattern
     */


    public String getDateFormat() {

        return analogClock.pattern;

    }

    /**

     * Sets whether date should be visible or not
     * @param visible True if date should be visible
     */


    public void setDateVisible(boolean visible) {
        analogClock.dateVisible = visible;

        analogClock.repaint();

    }

    /**

     * Returns whether date is visible or not
     * @return  True if date is visible
     */


    public boolean getDateVisible() {

        return analogClock.dateVisible;

    }

    public void updateUI() {
        boolean opaque = isOpaque();
        Border border = getBorder();

        super.updateUI();
        setOpaque(opaque);
        setBorder(border);
    }

    private void createPlugs() {
        if (handle == null)
            return;
            /////////////////////////----CLOCK PLUGS!-----\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            tickSO = new Tick(handle, 0);
            displayHours = new NumberSO(handle, 0.0);
            displayMins = new NumberSO(handle, 0.0);
            displaySecs = new NumberSO(handle, 0.0);
            dateSO = new DateSO(handle);
            timeSO = new DateSO(handle);

            try {
                SharedObjectListener sol = new SharedObjectListener() {
                        public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                            long execTime = System.currentTimeMillis();
                            SharedObject so = e.getSharedObject();

                            synchronized (currentGMT) {

                                Tick newTick = (Tick) so;
                                //***please remember to check here!
//                                System.out.println("Received tick : "+newTick.getTickLong());
                                if (run != false)
                                    Clock.this.setSystemTimeCounting(false);
                                currentGMT.advance(newTick.getTickLong());
                                setTime(currentGMT.hour, currentGMT.min, currentGMT.sec);
                                //System.out.println("seconds : "+currentGMT.sec);
                            }

                            //setTime(((Tick) e.getSharedObject()).getTick(),e.getPath());
//                            System.out.println("Time to process tick  : "+(System.currentTimeMillis()-execTime));
                        }
                    };

                plug1 = new SingleInputPlug(handle, resources, "tick", Color.yellow,
                        gr.cti.eslate.sharedObject.Tick.class,

                        /*tickSO,*/sol);

                plug1.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            // make sure connected component gets value of vector when connected
                            if (e.getType() == Plug.OUTPUT_CONNECTION) {
                                SharedObjectEvent soe = new SharedObjectEvent(tickSO);
                                ((SharedObjectPlug) e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe);
                            }
                        }
                    }
                );
                handle.addPlug(plug1);
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

            try {
                plug2 = new SingleInputMultipleOutputPlug(
                            handle, resources, "hours", new Color(135, 206, 250),
                            gr.cti.eslate.sharedObject.NumberSO.class, displayHours,
                            new SharedObjectListener() {
                                public synchronized void handleSharedObjectEvent(SharedObjectEvent soe) {
                                    //if (displayHours.doubleValue()>=0)
                                    Clock.this.handleHours(soe);
                                }
                            }
                        );
                handle.addPlug(plug2);
                plug2.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            // Make sure connected component gets elapsed time when connected.
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
                                setSeconds(so.doubleValue()*3600);
                            }
                        }
                    }
                );

                plug3 = new SingleInputMultipleOutputPlug(
                            handle, resources, "mins", new Color(135, 206, 250),
                            gr.cti.eslate.sharedObject.NumberSO.class, displayMins,
                            new SharedObjectListener() {
                                public void handleSharedObjectEvent(SharedObjectEvent soe) {
                                    //if (displayMins.doubleValue()>=0)
                                    Clock.this.handleMins(soe);
                                }
                            }
                        );
                handle.addPlug(plug3);
                plug3.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            // Make sure connected component gets elapsed time when connected.
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
                                setSeconds(so.doubleValue()*60);
                            }
                        }
                    }
                );

                plug4 = new SingleInputMultipleOutputPlug(
                            handle, resources, "secs", new Color(135, 206, 250),
                            gr.cti.eslate.sharedObject.NumberSO.class, displaySecs,
                            new SharedObjectListener() {
                                public synchronized void handleSharedObjectEvent(SharedObjectEvent soe) {
                                    //if (displaySecs.doubleValue()>=0)
                                    Clock.this.handleSecs(soe);
                                }
                            }
                        );
                handle.addPlug(plug4);
                plug4.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            // Make sure connected component gets elapsed time when connected.
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
                                setSeconds(so.doubleValue());
                            }
                        }
                    }
                );
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

            try {
                SharedObjectListener sol = new SharedObjectListener() {
                        public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                            setTime(((DateSO) e.getSharedObject()).getDate());
                        }
                    };

                plug5 = new MultipleInputMultipleOutputPlug(handle, resources, "Time", new Color(34, 139, 134),
                        gr.cti.eslate.sharedObject.DateSO.class,
                        timeSO, sol);

                plug5.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                DateSO so = (DateSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
                                setTime(so.getDate());
                            }
                        }
                    }
                );
                handle.addPlug(plug5);
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

            try {
                SharedObjectListener so2 = new SharedObjectListener() {
                        public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                            setDate(((DateSO) e.getSharedObject()).getDate());
                        }
                    };

                plug6 = new MultipleInputMultipleOutputPlug(handle, resources, "Date", new Color(34, 139, 134),
                        gr.cti.eslate.sharedObject.DateSO.class,
                        dateSO, so2);

                plug6.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                DateSO so = (DateSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
                                setDate(so.getDate());
                            }
                        }
                    }
                );
                handle.addPlug(plug6);

            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}


            updateSharedObjects();
            ///////////////////////////////-------PLUGS END-------\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    }
    
    class ClockThread extends Thread {
        public void run() {
            while (null != clockThread) {
                if (run == true) {
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(new Date());
                        setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                        GregorianCalendar calendar2 = new GregorianCalendar();
                        
                        calendar2.setTime(getDate());
                        if (calendar.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH))
                            setDate(new Date());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
        }
    }

    private void destroyPlugs() {

        try {
            if (plug1 != null)
                handle.removePlug(plug1);
            if (plug2 != null)
                handle.removePlug(plug2);
            if (plug3 != null)
                handle.removePlug(plug3);
            if (plug4 != null)
                handle.removePlug(plug4);
            if (plug5 != null)
                handle.removePlug(plug5);
            if (plug6 != null)
                handle.removePlug(plug6);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug1 = null;
        plug2 = null;
        plug3 = null;
        plug4 = null;
        plug5 = null;
        plug6 = null;

    }

    public void setPlugsUsed(boolean create) {
        if (plugsCreated==create)
            return;
        if (handle != null) {
            if (create)
                createPlugs();
            else
                destroyPlugs();
            plugsCreated = create;
        } else
            plugsCreated = false;
    }

    public boolean getPlugsUsed() {
        return plugsCreated;
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener == null) {
            perfListener = new PerformanceAdapter() {
                        public void performanceManagerStateChanged(PropertyChangeEvent e) {
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
    private void attachTimers() {
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
            constructorTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, resources.getString("ConstructorTimer"), true
                    );
            loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, resources.getString("LoadTimer"), true
                    );
            saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, resources.getString("SaveTimer"), true
                    );
            initESlateAspectTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
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

