package gr.cti.eslate.protocol;

/**
 * This interface declares the methods that components conncting to th
 * steering control component through its "Direction" plug must implement.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.steering.Steering
 */
public interface SteeringControlInterface
{
  /**
   * Start moving.
   */
  public void go();
}
