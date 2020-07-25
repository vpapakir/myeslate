package gr.cti.eslate.set;

import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class is a JApplet wrapper for the set component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 19-Sep-2007
 * @see         gr.cti.eslate.set.Set
 */
public class SetApplet extends JApplet
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private Set set = null;

  /**
   * Applet parameter information.
   * @return    Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name   Kind of Value  Description
      {"toolBarVisible",  "boolean",
          "Specify if the component's toolbar will be shown"},
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

    // Parse applet parameters.

    // Check whether debugging should be enabled.
    boolean debug = false;
    String str = getParameter("debug");
    if (str != null) {
       debug = Boolean.valueOf(str).booleanValue();
    }

    // Check if the toolbar should be shown.
    boolean toolBarVisible = true;
    str = getParameter("toolBarVisible");
    if (str != null) {
      toolBarVisible = Boolean.valueOf(str).booleanValue();
    }

    // Create and add the set panel.
    set = new Set();
    set.setToolBarVisible(toolBarVisible);
    set.getESlateHandle().setDebugStatus(debug);
    getContentPane().add(set);

    // Notify state changed listeners.
    ESlateMicroworld mw = set.getESlateHandle().getESlateMicroworld();
    if (mw != null) {
      mw.fireComponentStateChangedListeners(
        set, ESlateMicroworld.COMPONENT_INITIALIZED
      );
    }
  }

  /**
   * Overrides JApplet.start(), to invoke any component state changed
   * listeners.
   */
  public void start()
  {
    super.start();
    // Notify state changed listeners
    ESlateMicroworld mw = set.getESlateHandle().getESlateMicroworld();
    if (mw != null) {
      mw.fireComponentStateChangedListeners(
        set, ESlateMicroworld.COMPONENT_STARTED
      );
    }
  }

  /**
   * Overrides JApplet.stop(), to invoke any component state changed
   * listeners.
   */
  public void stop()
  {
    super.stop();
    // Notify state changed listeners
    ESlateMicroworld mw = set.getESlateHandle().getESlateMicroworld();
    if (mw != null) {
      mw.fireComponentStateChangedListeners(
        set, ESlateMicroworld.COMPONENT_STOPPED
      );
    }
  }

  /**
   * Cleans up when the component is destroyed.
   */
  public void destroy()
  {
    // Notify state changed listeners
    ESlateMicroworld mw = set.getESlateHandle().getESlateMicroworld();
    if (mw != null) {
      mw.fireComponentStateChangedListeners(
        set, ESlateMicroworld.COMPONENT_DESTROYED
      );
    }

    set.dispose();
    set = null;
    ESlateHandle.removeAllRecursively(this);
    super.destroy();
  }

}
