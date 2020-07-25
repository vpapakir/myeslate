package gr.cti.eslate.utils;

/**
 * This class acts as a wrapper for a boolean value, providing methods to
 * get and set this value. This wrapper can be used, e.g., to return
 * an secondary boolean result from a method that already returns another
 * result.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class BooleanWrapper
{
  /**
   * The value being wrapped.
   */
  private boolean value;

  /**
   * Constructs a boolean wrapper.
   * @param     value   The value to wrap.
   */
  public BooleanWrapper(boolean value)
  {
    super();
    this.value = value;
  }

  /**
   * Returns the wrapped value.
   * @return    The requested value.
   */
  public boolean getValue()
  {
    return value;
  }

  /**
   * Sets the value of the wrapped boolean.
   * @param     value   The value to set.
   */
  public void setValue(boolean value)
  {
    this.value = value;
  }

}
