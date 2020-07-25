package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events about the addition of a
 * component to a microworld.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ComponentAddedListener extends EventListener
{
  /**
   * Invoked when a component is added to a microworld.
   */
  public void componentAdded(ComponentAddedEvent e);
}
