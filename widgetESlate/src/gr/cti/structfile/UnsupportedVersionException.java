package gr.cti.structfile;

/**
 * This exception is thrown if a structured file is opened from a different
 * structured file class from which it was created.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class UnsupportedVersionException extends RuntimeException
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create an UnsupportedVersionException instance.
   * @param     message The message associated with the exception.
   *                    It should be the version string read from the
   *                    structured file (<code>"VERSION=n"</code>), or
   *                    <code>""</code> for version 1 structured files.
   *                    (In plain words, unless version 2 format proves
   *                    to be a dud, this means that <code>message</code>
   *                    should be <code>""</code>!)
   */
  public UnsupportedVersionException(String message)
  {
    super(message);
  }
}
