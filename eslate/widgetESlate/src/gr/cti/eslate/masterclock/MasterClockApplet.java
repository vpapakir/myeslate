package gr.cti.eslate.masterclock;

import java.text.*;
import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class is a JApplet wrapper for the master clock component.
 *
 * The minimum and maximum values of the time scale slider can be specified
 * via the "minimum" and "maximum" parameters in the applet tag; the default
 * is 1 and 100, respectively.
 * The initial value of the time scale can be specified via the "initial"
 * parameter in the applet tag; the default is 1.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 30-May-2006
 * @see         gr.cti.eslate.masterclock.MasterClock
 */
public class MasterClockApplet extends JApplet
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private MasterClock masterClock = null;

  /**
   * Applet parameter information.
   * @return    Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name   Kind of Value  Description
      {"menubar",  "boolean",  "Specify if the menu bar will be displayed"},
      {"debug",    "boolean",  "Specify if debugging messages will be printed"},
      {"minimum",  "double",   "Minimum value of slider."},
      {"maximum",  "double",   "Maximum value of slider."},
      {"initial",  "double",   "Initial value of slider."}
    };
    return info;
  }

  /**
   * Initialize the applet.
   */
  public void init()
  {
    super.init();

    // Parse applet parameters.
    String str;
    Double minScale = null, maxScale = null, scale = null;
    NumberFormat nf =
      NumberFormat.getInstance(ESlateMicroworld.getCurrentLocale());

    // Minimum slider value.
    str = getParameter("minimum");
    if (str != null) {
      try {
        minScale = new Double(nf.parse(str).doubleValue());
      } catch (ParseException e) {
      }
    }

    // Maximum slider value.
    str = getParameter("maximum");
    if (str != null) {
      try {
        maxScale = new Double(nf.parse(str).doubleValue());
      } catch (ParseException e) {
      }
    }

    // Initial slider value.
    str = getParameter("initial");
    if (str != null) {
      try {
        scale = new Double(nf.parse(str).doubleValue());
      } catch (ParseException e) {
      }
    }

    // Check if menu bar is wanted
    boolean needMenuPanel = true;
    str = getParameter("menubar");
    if (str != null) {
      needMenuPanel = Boolean.valueOf(str).booleanValue();
    }

    // Check whether debugging should be enabled
    boolean debug = false;
    str = getParameter("debug");
    if (str != null) {
       debug = Boolean.valueOf(str).booleanValue();
    }

    // Create and add the master clock panel.
    masterClock = new MasterClock(needMenuPanel);

    masterClock.getESlateHandle().setDebugStatus(debug);
    if (minScale != null) {
      masterClock.setMinimumTimeScale(minScale.doubleValue());
    }
    if (maxScale != null) {
      masterClock.setMaximumTimeScale(maxScale.doubleValue());
    }
    if (scale != null) {
      masterClock.setTimeScale(scale.doubleValue());
    }
    getContentPane().add(masterClock);

    // Notify state changed listeners
    masterClock.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      masterClock, ESlateMicroworld.COMPONENT_INITIALIZED
    );
  }

  /**
   * Overrides JApplet.start(), to invoke any component state changed
   * listeners.
   */
  public void start()
  {
    super.start();
    // Notify state changed listeners
    masterClock.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      masterClock, ESlateMicroworld.COMPONENT_STARTED
    );
  }

  /**
   * Overrides JApplet.stop(), to invoke any component state changed
   * listeners.
   */
  public void stop()
  {
    super.stop();
    // Notify state changed listeners
    masterClock.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      masterClock, ESlateMicroworld.COMPONENT_STOPPED
    );
  }

  /**
   * Cleans up when the component is destroyed.
   */
  public void destroy()
  {
    // Notify state changed listeners
    masterClock.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      masterClock, ESlateMicroworld.COMPONENT_DESTROYED
    );

    masterClock.dispose();
    masterClock = null;
    ESlateHandle.removeAllRecursively(this);
    super.destroy();
  }

}
