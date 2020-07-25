package gr.cti.eslate.base;

/**
 * The event sent when the JavaScript manager is activated or terminated.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ScriptManager
 * @see         gr.cti.eslate.base.JavaScriptManagerAdapter
 * @see         gr.cti.eslate.base.JavaScriptManagerListener
 */
public class JavaScriptManagerEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a JavaScript manager event.
   * @param     mgr     The script manager owning the activated or terminated
   *                    JavaScript manager.
   */
  public JavaScriptManagerEvent(ScriptManager mgr)
  {
    super(mgr);
  }
}
