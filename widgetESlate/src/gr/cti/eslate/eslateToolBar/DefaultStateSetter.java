package gr.cti.eslate.eslateToolBar;

/**
 * This interface describes the method that a class must implement in order to
 * be able to reset the toolbar to its default state.
 *
 * @version     2.0.0, 22-May-2006
 * @author      Kriton Kyrimis
 */
public interface DefaultStateSetter
{
  /**
   * Reset a toolbar to its default state.
   * @param     toolbar The toolbar to be reset to its default state.
   *                    The "default state" is whatever the invoker of
   *                    this method wants it to be!
   */
  public void setDefaultState(ESlateToolBar toolbar);
}
