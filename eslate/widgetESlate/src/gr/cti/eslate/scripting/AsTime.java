package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the travel-for-a-given-time
 * control component that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see gr.cti.eslate.time.Time
 */

public interface AsTime
{
  /**
   * Start moving.
   */
  public void go();

  /**
   * Set the time to travel.
   * @param     x       The time to travel.
   */
  public void setTime(double x);

  /**
   * Returns the time to travel.
   * @return    The requested time.
   */
  public double getTime();

  /**
   * Specify the unit in which the time to travel is measured.
   * @param     u       The name of the unit.
   */
  public void setUnit(String u) throws Exception;

  /**
   * Returns the unit in which the time to travel is measured.
   * @return    The requested unit.
   */
  public String getUnit();

  /**
   * Returns the supported units in which the time to travel can be
   * measured.
   * @return    An array of the names of the supported units.
   */
  public String[] getUnits();

  /**
   * Specify whether we should stop at landmarks.
   * @param     stop    Yes if true, no if false.
   */
  public void setStopAtLandmarks(boolean stop);

  /**
   * Checks whether we should stop at landmarks.
   * @return    True if yes, false if no.
   */
  public boolean getStopAtLandmarks();
}
