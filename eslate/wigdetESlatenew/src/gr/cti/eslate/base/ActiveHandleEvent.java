package gr.cti.eslate.base;

import java.util.EventObject;

/**
 * Event sent when a component's active component changes.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class ActiveHandleEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The component's previous active E-Slate handle.
   */
  private ESlateHandle oldActive;
  /**
   * The component's new active E-Slate handle.
   */
  private ESlateHandle newActive;

  /*
   * Construct an ActiveHandleEvent.
   * @param     source          The ActivationHandleGroup whose active
   *                            component has changed.
   * @param     oldActive       The component's previous active E-Slate
   *                            handle.
   * @param     newActive       The component's new active E-Slate handle.
   */
  ActiveHandleEvent(ActivationHandleGroup source, ESlateHandle oldActive,
                    ESlateHandle newActive)
  {
    super(source);
    this.oldActive = oldActive;
    this.newActive = newActive;
  }

  /**
   * Returns the component's previous active E-Slate handle.
   * @return    The component's previous active E-Slate handle.
   */
  public ESlateHandle getOldActiveHandle()
  {
    return oldActive;
  }

  /**
   * Returns the component's previous new active E-Slate handle.
   * @return    The component's previous new active E-Slate handle.
   */
  public ESlateHandle getNewActiveHandle()
  {
    return newActive;
  }

  /**
   * Returns the ActiveHandleGroup whose active component has changed.
   * @return    The ActiveHandleGroup whose active component has changed.
   */
  public ActivationHandleGroup getActiveHandleGroup()
  {
    return (ActivationHandleGroup)getSource();
  }
}
