package gr.cti.structfile;

/**
 * This exception is thrown when the defragmentation of a structured file
 * fails.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class DefragmentException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a DefragmentException.
   * @param     text    A text describing what went wrong.
   */
  public DefragmentException(String text)
  {
    super(text);
  }
}
