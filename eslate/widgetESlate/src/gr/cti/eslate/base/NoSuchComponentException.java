package gr.cti.eslate.base;

/**
 * This exception is thrown when trying to access a component that does not
 * exist in a microworld.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NoSuchComponentException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a NoSuchComponentException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public NoSuchComponentException(String message)
  {
    super(message);
  }
}
