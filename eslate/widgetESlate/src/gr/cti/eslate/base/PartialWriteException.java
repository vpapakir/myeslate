package gr.cti.eslate.base;

/**
 * This exception is thrown if a microworld was only partially saved.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PartialWriteException extends RuntimeException
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a PartialWriteException
   * @param     text    A text describing what went wrong.
   */
  public PartialWriteException(String text)
  {
    super(text);
  }
}
