package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events about the activation and
 * deactivation of the JavaScript manager.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ScriptManager
 * @see         gr.cti.eslate.base.JavaScriptManagerAdapter
 * @see         gr.cti.eslate.base.JavaScriptManagerEvent
 */
public interface JavaScriptManagerListener extends EventListener
{
  /**
   * Invoked when the JavaScript manager is activated.
   */
  public void managerActivated(JavaScriptManagerEvent e);

  /**
   * Invoked when the JavaScript manager is terminated.
   */
  public void managerTerminated(JavaScriptManagerEvent e);
}
