package gr.cti.eslate.protocol;

/**
 * This interface declares the methods that components conncting to the
 * time control component through its "Time" plug must implement.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.time.Time
 */

public interface TimeControlInterface
{
 /**
  * Travel for a given time.
  * @param      time    The time for which to travel, measured in seconds.
  * @param      stopAtLandmarks Indicate whether the component should stop
  *                     at "interesting" locations even though the
  *                     specified time has not elapsed.
  */
  public void goTime(double time, boolean stopAtLandmarks);
}
