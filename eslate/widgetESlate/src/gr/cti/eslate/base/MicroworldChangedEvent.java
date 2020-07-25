package gr.cti.eslate.base;

/**
 * Event triggered when a component is moved successfully to a new microworld.
 * The event's getSource() method will return the moved component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class MicroworldChangedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The previous microworld of the component.
   */
  private ESlateMicroworld oldMicroworld;
  /**
   * The new microworld of the component.
   */
  private ESlateMicroworld newMicroworld;

  /**
   * Constructs a microworld changed event.
   * @param     component       The component whose microworld has changed.
   * @param     oldMicroworld   The previous microworld of the component.
   * @param     newMicroworld   The new microworld of the component.
   */
  public MicroworldChangedEvent(Object component,
                                ESlateMicroworld oldMicroworld,
                                ESlateMicroworld newMicroworld)
  {
    super(component);
    this.oldMicroworld = oldMicroworld;
    this.newMicroworld = newMicroworld;
  }

  /**
   * Returns the old microworld of the moved component.
   * @return    The requested microworld.
   */
  public ESlateMicroworld getOldMicroworld()
  {
    return oldMicroworld;
  }

  /**
   * Returns the new microworld of the moved component.
   * @return    The requested microworld.
   */
  public ESlateMicroworld getNewMicroworld()
  {
    return newMicroworld;
  }
}
