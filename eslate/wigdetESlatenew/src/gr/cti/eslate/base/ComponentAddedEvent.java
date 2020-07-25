package gr.cti.eslate.base;

/**
 * Event triggered when a component is added to a microworld.
 * The event's getSource() method will return the E-Slate handle of the
 * component that was added.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ComponentAddedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Constructs a component added event.
   * @param     handle  The E-Slate handle of the component that was added.
   */
  public ComponentAddedEvent(ESlateHandle handle)
  {
    super(handle);
  }
}
