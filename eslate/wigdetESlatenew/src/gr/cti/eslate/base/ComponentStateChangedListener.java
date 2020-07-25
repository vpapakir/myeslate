package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events about changes in component
 * state.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ComponentStateChangedListener extends EventListener
{
  /**
   * Invoked when a component is created.
   */
  public void componentCreated(ComponentStateChangedEvent e);

  /**
   * Invoked when a component running as an applet is initialized.
   */
  public void componentInitialized(ComponentStateChangedEvent e);

  /**
   * Invoked when a component running as an applet is started.
   */
  public void componentStarted(ComponentStateChangedEvent e);

  /**
   * Invoked when a component running as an applet is stopped.
   */
  public void componentStopped(ComponentStateChangedEvent e);

  /**
   * Invoked when a component running as an applet is destroyed.
   */
  public void componentDestroyed(ComponentStateChangedEvent e);

  /**
   * Invoked when a component is finalized.
   */
  public void componentFinalized(ComponentStateChangedEvent e);

}
