package gr.cti.eslate.base;

import java.util.*;

/**
 * Listener for changes in the activated componnet for a given component.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public interface ActiveHandleListener extends EventListener
{
  /**
   * Invoked when the active handle of a component changes.
   * @param     e       The event signaling the change.
   */
  public void activeHandleChanged(ActiveHandleEvent e);
}
