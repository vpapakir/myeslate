package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events about the removal of a
 * component from a microworld.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ComponentRemovedListener extends EventListener
{
  /**
   * Invoked when a component is removed from a microworld.
   */
  public void componentRemoved(ComponentRemovedEvent e);
}
