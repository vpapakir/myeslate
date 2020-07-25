package gr.cti.eslate.utils;

/**
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 */

public class KeyDoesntExistException extends Exception
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public KeyDoesntExistException(String message)
  {
    super(message);
  }
}

