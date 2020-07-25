package gr.cti.eslate.services.name.event;

import java.util.*;

/**
 * The listener interface for receiving events related to the E-Slate name
 * service.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface NameServiceListener extends EventListener
{
  /**
   * Invoked when a name is successfully associated with an object.
   */
  public void nameBound(NameBoundEvent e);
  /**
   * Invoked when a name is successfully disassociated from an object.
   */
  public void nameUnbound(NameUnboundEvent e);
  /**
   * Invoked when a name is successfully associated with an object with which
   * another name had been associated previously.
   */
  public void nameRebound(NameReboundEvent e);
  /**
   * Invoked when a name associated with an object is changed successfully.
   */
  public void nameChanged(NameChangedEvent e);
}
