package gr.cti.eslate.base;

/**
 * This exception is thrown if a microworld was only partially restored.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PartialReadException extends RuntimeException
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a PartialReadException
   * @param     text    A text describing what went wrong.
   */
  public PartialReadException(String text)
  {
    super(text);
  }
}
