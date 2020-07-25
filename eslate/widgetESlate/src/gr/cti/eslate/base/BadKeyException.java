package gr.cti.eslate.base;

/**
 * This exception is thrown when specifying the wrong key to unlock a locked
 * operation.
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class BadKeyException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a BadKeyException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public BadKeyException(String message)
  {
    super(message);
  }
}
