package gr.cti.eslate.vector;

import java.util.*;

/**
 * The listener interface for receiving events about changes of the value of
 * the vector.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public interface VectorListener extends EventListener
{
  /**
   * Invoked when the vector's value has changed.
   */
  public void valueChanged(VectorEvent e);
}
