package gr.cti.eslate.base.container;

/**
 * This exception is thrown whenever a cycle is to be created in the
 * PerformanceTimerGroup graph of the PerformanceManager. A cycle is defined
 * as the situation when a PerformanceTimerGroup has itself as a child
 * somewhere in its hierarchy.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class CycleException extends RuntimeException
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public CycleException(String message)
  {
    super(message);
  }
}
