package gr.cti.eslate.base;

/**
 * This exception is thrown when trying to rename a component or microworld
 * using a name that is being used by another component or microworld
 * respectively.
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class NameUsedException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a NameUsedException with a given detail message.
   *
   * @param     message The detail message associated with the exception.
   */
  public NameUsedException(String message)
  {
    super(message);
  }
}
