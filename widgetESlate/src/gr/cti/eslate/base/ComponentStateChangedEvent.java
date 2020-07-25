package gr.cti.eslate.base;

/**
 * Event triggered when the state of a component has changed. The event's
 * getSource() method will return the component whose state has changed.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ComponentStateChangedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Constructs a component state changed event.
   * @param     component       The component whose state has changed.
   */
  public ComponentStateChangedEvent(Object component)
  {
    super(component);
  }

}
