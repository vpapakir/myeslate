package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events about the addition of a
 * primitive group to an E-Slate handle.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface PrimitiveGroupAddedListener extends EventListener
{
  /**
   * Invoked when a primitive group is added to an E-Slate handle.
   */
  public void primitiveGroupAdded(PrimitiveGroupAddedEvent e);
}
