package gr.cti.eslate.base;

/**
 * This exception is thrown when trying to rename a component when this has
 * been forbidden.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class RenamingForbiddenException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a RenamingForbiddenException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public RenamingForbiddenException(String message)
  {
    super(message);
  }
}
