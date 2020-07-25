package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the Master Clock component
 * that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 30-May-2006
 * @see gr.cti.eslate.masterclock.MasterClock
 */

public interface AsMasterClock
{
  /**
   * Start sending ticks.
   */
  public void start();

  /**
   * Stop sending ticks.
   */
  public void stop();

  /**
   * Sets the minimum value of the time scale slider.
   */
  public void setMinimumTimeScale(double value);

  /**
   * Returns the minimum value of the time scale slider.
   * @return    The requested value.
   */
  public double getMinimumTimeScale();

  /**
   * Sets the maximum value of the time scale slider.
   */
  public void setMaximumTimeScale(double value);

  /**
   * Returns the maximum value of the time scale slider.
   * @return    The requested value.
   */
  public double getMaximumTimeScale();

  /**
   * Sets the value of the time scale slider.
   */
  public void setTimeScale(double value);

  /**
   * Returns the value of the time scale slider.
   * @return    The requested value.
   */
  public double getTimeScale();

  /**
   * Check whether the master clock is running.
   * @return    True if yes, false if no.
   */
  public boolean isRunning();
}
