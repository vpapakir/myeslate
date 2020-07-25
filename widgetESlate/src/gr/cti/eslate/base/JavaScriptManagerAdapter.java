package gr.cti.eslate.base;

/**
 * The adapter for receiving events about the activation and
 * deactivation of the JavaScript manager.
 * The methods in this class are empty;  this class is provided as a
 * convenience for easily creating listeners by extending this class
 * and overriding only the methods of interest.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ScriptManager
 * @see         gr.cti.eslate.base.JavaScriptManagerEvent
 * @see         gr.cti.eslate.base.JavaScriptManagerListener
 */
public class JavaScriptManagerAdapter
  implements JavaScriptManagerListener
{
  /**
   * Invoked when the JavaScript manager is activated.
   */
  public void managerActivated(JavaScriptManagerEvent e)
  {
  }

  /**
   * Invoked when the JavaScript manager is terminated.
   */
  public void managerTerminated(JavaScriptManagerEvent e)
  {
  }
}
