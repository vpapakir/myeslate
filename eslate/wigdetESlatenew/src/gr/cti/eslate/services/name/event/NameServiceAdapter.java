package gr.cti.eslate.services.name.event;

/**
 * An adapter class implementing the listener interface for receiving events
 * related to the E-Slate name service.
 * The methods in this class are empty;  this class is provided as a
 * convenience for easily creating listeners by extending this class
 * and overriding only the methods of interest.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameServiceAdapter implements NameServiceListener
{
  /**
   * Invoked when a name is successfully associated with an object.
   */
  public void nameBound(NameBoundEvent e)
  {
  }

  /**
   * Invoked when a name is successfully disassociated from an object.
   */
  public void nameUnbound(NameUnboundEvent e)
  {
  }

  /**
   * Invoked when a name is successfully associated with an object with which
   * another name had been associated previously.
   */
  public void nameRebound(NameReboundEvent e)
  {
  }

  /**
   * Invoked when a name associated with an object is changed successfully.
   */
  public void nameChanged(NameChangedEvent e)
  {
  }

}
