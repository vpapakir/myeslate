package gr.cti.eslate.time;

import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class is a JApplet wrapper for the travel for a given time control
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.time.Time
 */
public class TimeApplet extends JApplet
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private Time time = null;

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
      {"debug",    "boolean",  "Specify if debugging messages will be printed"}
    };
    return info;
  }

  /**
   * Initialize the applet.
   */
  public void init()
  {
    super.init();

    // Parse applet parameters

    // Check if menu bar is wanted
    boolean needMenuPanel = true;
    String str;
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

    // Create and add the time control panel.
    time = new Time(needMenuPanel);
    time.getESlateHandle().setDebugStatus(debug);
    getContentPane().add(time);

    // Notify state changed listeners
    time.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      time, ESlateMicroworld.COMPONENT_INITIALIZED
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
    time.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      time, ESlateMicroworld.COMPONENT_STARTED
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
    time.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      time, ESlateMicroworld.COMPONENT_STOPPED
    );
  }

  /**
   * Cleans up when the component is destroyed.
   */
  public void destroy()
  {
    // Notify state changed listeners
    time.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      time, ESlateMicroworld.COMPONENT_DESTROYED
    );

    time.dispose();
    time = null;
    ESlateHandle.removeAllRecursively(this);
    super.destroy();
  }

}
