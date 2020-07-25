package gr.cti.eslate.base;

/**
 * This exception is thrown when an error occurs while trying to open or read
 * from a saved microworld file.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.14, 3-Nov-2006
 */
public class ReadingException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;

  /**
   * Create a <code>ReadingException</code> instance.
   * @param     text    A text describing what went wrong.
   */
  public ReadingException(String text)
  {
    super(text);
  }

  /**
   * Create a <code>ReadingException</code> instance.
   * @param     text    A text describing what went wrong.
   * @param     cause   The <code>Throwable</code> that caused this exception.
   */
  public ReadingException(String text, Throwable cause)
  {
    super(text, cause);
  }
}
