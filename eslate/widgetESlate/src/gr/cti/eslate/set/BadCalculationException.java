package gr.cti.eslate.set;

/**
 * This exception is thrown when a calculation on a set of elements fails.
 * (Usually because the set is empty.)
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 */
public class BadCalculationException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a BadCalculationException.
   */
  public BadCalculationException()
  {
    super();
  }
}
