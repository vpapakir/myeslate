package gr.cti.eslate.services.name;

/**
 * This exception is thrown when an error occurs in the ESlate name service.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameServiceException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a  NameServiceException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public NameServiceException(String message)
  {
    super(message);
  }
}
