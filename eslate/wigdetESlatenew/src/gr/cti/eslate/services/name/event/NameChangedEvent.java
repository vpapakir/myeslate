package gr.cti.eslate.services.name.event;

import gr.cti.eslate.services.name.*;

/**
 * Event triggered when a name associated with an object is changed
 * successfully.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameChangedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The name that was previously associated with the object.
   */
  private String oldName;
  /**
   * The name that is currently associated with the object.
   */
  private String newName;

  /**
   * Constructs a name bound event.
   * @param     source  The name service where the event occurred.
   * @param     oldName The name that was previously associated with the
   *                    object.
   * @param     newName The name that is currently associated with the object.
   */
  public NameChangedEvent(NameServiceContext source, String oldName,
                          String newName)
  {
    super(source);
    this.oldName = oldName;
    this.newName = newName;
  }

  /**
   * Returns the name that was previously associated with the object.
   * @return    The requested name.
   */
  public String getOldName()
  {
    return oldName;
  }

  /**
   * Returns the name that is currently associated with the object.
   * @return    The requested name.
   */
  public String getNewName()
  {
    return newName;
  }

}
