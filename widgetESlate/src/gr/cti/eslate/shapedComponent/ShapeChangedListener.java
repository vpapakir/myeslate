package gr.cti.eslate.shapedComponent;

import java.util.*;

/**
 * Listener for shape changed events.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public interface ShapeChangedListener extends EventListener
{
  /**
   * Invoked when the shape of a component has changed.
   * @param     e       The event sent when the shape of an object changes.
   */
  public void shapeChanged(ShapeChangedEvent e);
}
