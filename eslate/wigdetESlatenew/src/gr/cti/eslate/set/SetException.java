package gr.cti.eslate.set;

/**
 * This exception is thrown when the set component wants to indicate that
 * something went wrong.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 */
public class SetException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  /**
   * Create a SetException.
   * @param     text    A text describing what went wrong.
   */
  public SetException(String text)
  {
    super(text);
  }
}
