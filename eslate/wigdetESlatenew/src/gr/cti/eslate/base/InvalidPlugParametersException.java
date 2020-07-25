package gr.cti.eslate.base;

/**
 * This exception is thrown when a plug constructor is called with invalid
 * parameters.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class InvalidPlugParametersException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create an InvalidProtocolException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public InvalidPlugParametersException(String message)
  {
    super(message);
  }
}
