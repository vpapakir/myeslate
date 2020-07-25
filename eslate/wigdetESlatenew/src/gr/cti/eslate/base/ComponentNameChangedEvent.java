package gr.cti.eslate.base;

/**
 * Event triggered when a component is renamed successfully. The event's
 * <code>getSource()</code> method will return the renamed component,
 * while the <code>getHandle()</code> will return the renamed component's
 * E-Slate handle.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ComponentNameChangedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The old name of the component.
   */
  private String oldName;
  /**
   * The new name of the component.
   */
  private String newName;
  /**
   * The E-Slate handle of the renamed component.
   */
  private ESlateHandle component;

  /**
   * Constructs a component name changed event.
   * @param     component       The component whose state has changed.
   * @param     oldName         The old name of the component.
   * @param     newName         The new name of the component.
   */
  public ComponentNameChangedEvent(ESlateHandle component, String oldName,
                                   String newName)
  {
    super(
      (component.getComponent() != null) ? component.getComponent() : component
    );
    this.component = component;
    this.oldName = oldName;
    this.newName = newName;
  }

  /**
   * Returns the old name of the renamed component.
   * @return    The requested name.
   */
  public String getOldName()
  {
    return oldName;
  }

  /**
   * Returns the new name of the renamed component.
   * @return    The requested name.
   */
  public String getNewName()
  {
    return newName;
  }

  /**
   * Returns the new name of the renamed component.
   * @return    The requested name.
   * @deprecated        As of E-Slate version 1.5.12,
   *                    replaced by {@link #getNewName()}
   */
  public String getName()
  {
    return getNewName();
  }

  /**
   * Returns the E-Slate handle of the renamed component.
   * @return    The requested handle.
   */
  public ESlateHandle getHandle()
  {
    return component;
  }

}
