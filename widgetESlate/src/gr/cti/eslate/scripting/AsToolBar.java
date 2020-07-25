package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the Toolbar component
 * that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see gr.cti.eslate.eslateToolBar.ESlateToolBar
 */

public interface AsToolBar
{
  /**
   * Specifies whether the components in a visual group will be drawn on the
   * toolbar.
   * @param     groupName       The name of the visual group.
   * @param     visible         True if yes, false otherwise.
   * @exception NullPointerException    Thrown if <code>groupName</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                                    does not belong to this toolbar.
   */
  public void setGroupVisible(String groupName, boolean visible)
    throws NullPointerException, IllegalArgumentException;

  /**
   * Checks whether the components in a visual group will be drawn on the
   * toolbar.
   * @param     groupName       The name of the visual group.
   * @return    True if yes, false otherwise.
   * @exception NullPointerException    Thrown if <code>groupName</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the visual group
   *                                    does not belong to this toolbar.
   *
   */
  public boolean isGroupVisible(String groupName)
    throws NullPointerException, IllegalArgumentException;

  /**
   * Specifies whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the value
   * specified.
   * @param     name    The name of the tool.
   * @param     visible True if the tool will be drawn. false otherwise.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public void setToolVisible(String name, boolean visible)
    throws NullPointerException, IllegalArgumentException;

  /**
   * Checks whether a particular tool will be drawn on the toolbar.
   * If the visual group to which the tool belongs is not visible, then
   * the tool will <em>not</em> be drawn, regardless of the speciied
   * value.
   * @param      name   The name of the tool.
   * @return    True if the component will be drawn, false otherwise.
   * @exception NullPointerException    Thrown if <code>name</code> is
   *                                    <code>null</code>.
   * @exception IllegalArgumentException        Thrown if the tool does not
   *                                            belong to this toolbar.
   */
  public boolean isToolVisible(String name)
    throws NullPointerException, IllegalArgumentException;

  /**
   * Repaint the component.
   */
  public void repaint();
}
