package gr.cti.eslate.base;

/**
 * This exception is thrown when requesting the component
 * connected to a plug that accepts only a single protocol connection
 * and there is no component connected to the plug via the protocol mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NoSingleConnectedComponentException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a NoSingleConnectedComponentException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public NoSingleConnectedComponentException(String message)
  {
    super(message);
  }
}
