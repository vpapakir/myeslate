package gr.cti.eslate.base;

/**
 * Event triggered when a component is removed from a microworld.
 * The event's getSource() method will return the E-Slate handle of the
 * component that was removed.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ComponentRemovedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Constructs a component removed event.
   * @param     handle  The E-Slate handle of the component that was removed.
   */
  public ComponentRemovedEvent(ESlateHandle handle)
  {
    super(handle);
  }
}
