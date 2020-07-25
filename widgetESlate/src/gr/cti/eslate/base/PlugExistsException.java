package gr.cti.eslate.base;

/**
 * This exception is thrown when trying to add to a component a plug that has
 * the same name as another plug already attached to the component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugExistsException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a PlugExistsException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public PlugExistsException(String message)
  {
    super(message);
  }
}
