package gr.cti.eslate.protocol;

/**
 * This interface declares the methods that components conncting to the
 * distance control component through its "Distance" plug must implement.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.distance.Distance
 */
public interface DistanceControlInterface
{
  /**
   * Travel a given instance.
   * @param     dist    The distance to travel, measured in meters.
   * @param     stopAtLandmarks Indicate whether the component should stop
   *                            at "interesting" locations even though the
   *                            specified distance has not been travelled.
   */
  public void goDistance(double dist, boolean stopAtLandmarks);
}
