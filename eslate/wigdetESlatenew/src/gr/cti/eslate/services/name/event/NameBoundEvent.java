package gr.cti.eslate.services.name.event;

import gr.cti.eslate.services.name.*;

/**
 * Event triggered when a name is associated successfully with an object.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameBoundEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The name that was associated with an object.
   */
  private String name;
  /**
   * The object.
   */
  private Object object;

  /**
   * Constructs a name bound event.
   * @param     source  The name service where the event occurred.
   * @param     name    The name that was associated with an object.
   * @param     object  The object.
   */
  public NameBoundEvent(NameServiceContext source, String name, Object object)
  {
    super(source);
    this.name = name;
    this.object = object;
  }

  /**
   * Returns the name that was associated with an object.
   * @return    The requested name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the object with which a name was associated.
   * @return    The requested object.
   */
  public Object getObject()
  {
    return object;
  }

}
