package gr.cti.eslate.base;

/**
 * This exception is thrown when trying to remove from a component a plug that
 * it doesn't actually have.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NoSuchPlugException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a NoSuchPlugException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public NoSuchPlugException(String message)
  {
    super(message);
  }
}
