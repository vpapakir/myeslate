package gr.cti.eslate.base;

/**
 * Event triggered after a component's parent has changed.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ParentChangedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The component whose parent has changed.
   */
  private ESlateHandle component;
  /**
   * The old parent of the component.
   */
  private ESlateHandle oldParent;
  /**
   * The new parent of the component.
   */
  private ESlateHandle newParent;

  /**
   * Constructs a parent changed event.
   * @param     component       The handle of the component whose parent has
   *                            changed.
   * @param     oldParent       The E-Slate handle of the old parent of the
   *                            component. May be null.
   * @param     newParent       The E-Slate handle of the new parent of the
   *                            component. May be null.
   */
  public ParentChangedEvent(ESlateHandle component, ESlateHandle oldParent,
                            ESlateHandle newParent)
  {
    super(component);
    this.component = component;
    this.oldParent = oldParent;
    this.newParent = newParent;
  }

  /**
   * Returns the E-Slate handle of the component whose parent has changed.
   * @return    The requested E-Slate handle.
   */
  public ESlateHandle getComponent()
  {
    return component;
  }

  /**
   * Returns the E-Slate handle of the old parent of the component.
   * @return    The requested E-Slate handle.
   */
  public ESlateHandle getOldParent()
  {
    return oldParent;
  }

  /**
   * Returns the E-Slate handle of the new parent of the component.
   * @return    The requested E-Slate handle.
   */
  public ESlateHandle getNewParent()
  {
    return newParent;
  }
}
