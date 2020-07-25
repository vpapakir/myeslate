package gr.cti.eslate.base.container;

/**
 * Wrapper for integers allowing access to the wrapped number.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class IntWrapper
{
  /**
   * The wrapped integer.
   */
  int n;

  /**
   * Create a wrapper.
   * @param     n       The integer to wrap.
   */
  IntWrapper(int n)
  {
    super();
    this.n = n;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * @param     obj     The reference object with which to compare.
   * @return    <code>true</code> if this object is the same as the obj
   *            argument; <code>false</code> otherwise.
   */
  public boolean equals(Object obj)
  {
    return (n == ((IntWrapper)obj).n);
  }

  /**
   * Returns a hash code value for the object.
   * @return    A hash code value for this object.
   */
  public int hashCode()
  {
    return n;
  }
}
