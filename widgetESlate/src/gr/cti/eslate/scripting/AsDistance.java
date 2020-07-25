package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the distance control
 * component that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see gr.cti.eslate.distance.Distance
 */

public interface AsDistance
{
  /**
   * Start moving.
   */
  public void go();

  /**
   * Set the distance to travel.
   * @param     x       The distance to travel.
   */
  public void setDistance(double x);

  /**
   * Returns the distance to travel.
   * @return    The requested distance.
   */
  public double getDistance();

  /**
   * Specify the unit in which the distance to travel is measured.
   * @param     u       The name of the unit.
   */
  public void setUnit(String u) throws Exception;

  /**
   * Returns the unit in which the distance to travel is measured.
   * @return    The requested unit.
   */
  public String getUnit();

  /**
   * Returns the supported units in which the distance to travel can be
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
