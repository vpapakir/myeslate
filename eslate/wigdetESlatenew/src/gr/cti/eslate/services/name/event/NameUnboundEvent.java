package gr.cti.eslate.services.name.event;

import gr.cti.eslate.services.name.*;

/**
 * Event triggered when a name is disassociated successfully from an object.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameUnboundEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The name that was disassociated from an object.
   */
  private String name;
  /**
   * The object.
   */
  private Object object;

  /**
   * Constructs a name unbound event.
   * @param     source  The name service where the event occurred.
   * @param     name    The name that was disassociated from an object.
   * @param     object  The object.
   */
  public NameUnboundEvent(NameServiceContext source, String name, Object object)
  {
    super(source);
    this.name = name;
    this.object = object;
  }

  /**
   * Returns the name that was disassociated from an object.
   * @return    The requested name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the object from which a name was disassociated.
   * @return    The requested object.
   */
  public Object getObject()
  {
    return object;
  }

}
