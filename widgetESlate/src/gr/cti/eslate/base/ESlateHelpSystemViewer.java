package gr.cti.eslate.base;

import gr.cti.eslate.base.help.*;

import javax.help.*;

/**
 * A class providing a static method for creating instances of the
 * HelpSystemViewer class in a way that prevents the browser used by
 * HelpSystemViewer from installing its own security manager.
 *
 * @author      Kriton Kyrimis
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.help.HelpSystemViewer
 */
class ESlateHelpSystemViewer
{
  /**
   * Our own security manager, which we install to prevent the browser from
   * installing its own.
   */
  private static ESlateSecurityManager securityManager = null;

  /**
   * Create a new HelpSystemViewer.
   * @param     hs      The help set to display.
   * @param     x       The x coordinate of the viewer.
   * @param     y       The y coordinate of the viewer.
   * @param     width   The width of the viewer.
   * @param     height  The height of the viewer.
   * @return    The created HelpSystemViewer.
   */
  static HelpSystemViewer createViewer(HelpSet hs, int x, int y,
                                       int width, int height)
  {
    enableSecurityManager();
    HelpSystemViewer hsv = new HelpSystemViewer(hs, x, y, width, height);
    disableSecurityManager();
    return hsv;
 }

  /**
   * Enable our security manager.
   */
  private static void enableSecurityManager()
  {
    if (securityManager == null) {
      securityManager = new ESlateSecurityManager();
    }
    securityManager.enable();
    System.setSecurityManager(securityManager);
  }

  /**
   * Disable our security manager.
   */
  private static void disableSecurityManager()
  {
    if (securityManager != null) {
      securityManager.disable();
      System.setSecurityManager(null);
    }
  }

}
