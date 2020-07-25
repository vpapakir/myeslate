package gr.cti.eslate.base;

/**
 * Event triggered when LOGO primitive groups are added to an E-Slate handle.
 * The event's getSource() method will return the handle to which the
 * primitive group was added, and the event's getGroups() method will return
 * the class names of the primitive groups.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PrimitiveGroupAddedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The class names of the LOGO primitive groups.
   */
  private String[] names;

  /**
   * Constructs a LOGO primitive group added event referring to the addition
   * of a single primitive group.
   * @param     handle  The E-Slate handle to which the primitive group was
   *                    added.
   * @param     name    The class name of the primitive group.
   */
  public PrimitiveGroupAddedEvent(ESlateHandle handle, String name)
  {
    super(handle);
    names = new String[1];
    names[0] = name;
  }

  /**
   * Constructs a LOGO primitive group added event referring to the addition
   * of multiple primitive groups.
   * @param     handle  The E-Slate handle to which the primitive groups were
   *                    added.
   * @param     names   The class names of the primitive groups.
   */
  public PrimitiveGroupAddedEvent(ESlateHandle handle, String[] names)
  {
    super(handle);
    this.names = names;
  }

  /**
   * Returns the class names of the LOGO primitive groups that were added.
   * @return    The requested name.
   */
  public String[] getNames()
  {
    return names;
  }
}
