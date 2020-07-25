package gr.cti.eslate.base;

/**
 * The adapter for receiving events from E-Slate. The methods in this class
 * are empty; this class is provided as a convenience for easily
 * creating listeners by extending this class and overriding only the methods
 * of interest.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateAdapter implements ESlateListener
{
  /**
   * Invoked when a component is successfully renamed.
   */
  public void componentNameChanged(ComponentNameChangedEvent e)
  {
  }

  /**
   * Invoked immediately before an E-Slate handle is disposed.
   */
  public void disposingHandle(HandleDisposalEvent e)
  {
  }

  /**
   * Invoked immediately after an E-Slate handle is disposed.
   */
  public void handleDisposed(HandleDisposalEvent e)
  {
  }

  /**
   * Invoked when a component's parent has changed.
   */
  public void parentChanged(ParentChangedEvent e)
  {
  }
}
