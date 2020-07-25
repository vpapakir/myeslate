package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the Registry component that
 * is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 */
public interface AsRegistry
{
  /**
   * Register a variable.
   * @param     name            The name of the variable. If a variable having
   *                            this name had already been registered, it is
   *                            replaced with the new variable.
   * @param     value           The value of the variable. This value can be
   *                            <code>null</code>.
   * @param     persistent      Specifies whether this variable will be
   *                            persistent or not.
   */
  public void registerVariable(String name, Object value, boolean persistent);

  /**
   * Unregister a variable.
   * @param     name            The name of the variable.
   */
  public void unregisterVariable(String name);

  /**
   * Associates a comment with a variable in the registry.
   * @param     name    The name of the variable with which to associate the
   *                    comment. If there is no such variable in the registry,
   *                    the comment is ignored.
   * @param     comment The comment to associate with the variable.
   */
  public void setComment(String name, String comment);

  /**
   * Returns the value of a variable.
   * @param     name    The name of the variable.
   * @return    The value of the variable. If no such variable has been
   *            registered, this method returns <code>null</code>.
   */
  public Object lookupVariable(String name);
}
