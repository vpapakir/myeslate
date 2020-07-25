package gr.cti.eslate.scripting;

import gr.cti.eslate.utils.TimeCount;

/**
 * This interface describes the functionality of the Chronometer component
 * that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 * @see gr.cti.eslate.chronometer.Chronometer
 */

public interface AsChronometer
{
  /**
   * Start measuring elspased time.
   */
  public void start();

  /**
   * Stop measuring elapsed time.
   */
  public void stop();

  /**
   * Reset the chronometer display.
   */
  public void reset();

  /**
   * Set the elapsed time to a given value.
   * @param     time    The new time value.
   */
  public void setTime(TimeCount time);

  /**
   * Return the elapsed time.
   * @return    The requested time.
   */
  public TimeCount getTime();

  /**
   * Set the elapsed time to a given value in milliseconds.
   * @param     time    The new time value.
   */
  public void setMilliseconds(double time);

  /**
   * Return the elapsed time in milliseconds.
   * @return    The requested time.
   */
  public double getMilliseconds();

  /**
   * Check whether the chronometer is running.
   * @return    True if yes, false if no.
   */
  public boolean isRunning();
}
