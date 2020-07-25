package gr.cti.eslate.base;

/**
 * This exception is thrown when trying to disconnect from a plug a plug
 * that to which it is not connected, or when trying to connect to an
 * incompatible plug.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugNotConnectedException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a PlugNotConnectedException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public PlugNotConnectedException(String message)
  {
    super(message);
  }
}
