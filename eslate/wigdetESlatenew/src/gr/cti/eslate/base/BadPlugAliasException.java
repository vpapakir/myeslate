package gr.cti.eslate.base;

/**
 * This exception is thrown when something goes wrong during teh definition of
 * a plug alias.
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class BadPlugAliasException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a BadPlugAliasException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public BadPlugAliasException(String message)
  {
    super(message);
  }
}
