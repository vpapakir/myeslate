package gr.cti.eslate.base;

/**
 * The adapter which receives component state changed events.
 * The methods in this class are empty;  this class is provided as a
 * convenience for easily creating listeners by extending this class
 * and overriding only the methods of interest.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public abstract class ComponentStateChangedAdapter
       implements ComponentStateChangedListener
{
  /**
   * Invoked when a component is created.
   */
  public void componentCreated(ComponentStateChangedEvent e)
  {
  }

  /**
   * Invoked when a component running as an applet is initialized.
   */
  public void componentInitialized(ComponentStateChangedEvent e)
  {
  }

  /**
   * Invoked when a component running as an applet is started.
   */
  public void componentStarted(ComponentStateChangedEvent e)
  {
  }

  /**
   * Invoked when a component running as an applet is stopped.
   */
  public void componentStopped(ComponentStateChangedEvent e)
  {
  }

  /**
   * Invoked when a component running as an applet is destroyed.
   */
  public void componentDestroyed(ComponentStateChangedEvent e)
  {
  }

  /**
   * Invoked when a component is finalized.
   */
  public void componentFinalized(ComponentStateChangedEvent e)
  {
  }

}
