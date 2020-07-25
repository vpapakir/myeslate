package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the Vector component that
 * is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see gr.cti.eslate.vector.VectorComponent
 */

public interface AsVectorComponent
{
  /**
   * Sets the vector's value by specifying its horizontal and vertical
   * components.
   * @param     x       Vector's horizontal component.
   * @param     y       Vector's vertical component.
   */
  public void setNE(double x, double y);

  /**
   * Sets the vector's vertical component.
   * @param     y       Vector's vertical component.
   */
  public void setNorth(double y);

  /**
   * Sets the vector's horizontal component.
   * @param     x       Vector's horizontal component.
   */
  public void setEast(double x);

  /**
   * Sets the vector's value by specifying its length and angle.
   * @param     length  Vector's length.
   * @param     angle   Vector's angle.
   */
  public void setLA(double length, double angle);

  /**
   * Sets the vector's length.
   * @param     length  Vector's length.
   */
  public void setLength(double length);

  /**
   * Sets the vector's angle.
   * @param     angle   Vector's angle.
   */
  public void setAngle(double angle);

  /**
   * Sets the vector's scale.
   * @param     sc      Vector's scale.
   */
  public void setScale(double sc);


  /**
   * Returns the vector's vertical component.
   * @return    The vertical component.
   */
  public double getNorth();

  /**
   * Returns the vector's horizontal component.
   * @return    The horizontal component.
   */
  public double getEast();

  /**
   * Returns the vector's angle.
   * @return    The angle.
   */
  public double getAngle();

  /**
   * Returns the vector's length.
   * @return    The length.
   */
  public double getLength();

  /**
   * Returns the vector's scale.
   * @return    The scale.
   */
  public double getScale();

  /**
   * Sets the number of digits displayed after the decimal point.
   * @param     n       The number of digits.
   */
  public void setPrecision(int n);

  /**
   * Returns the number of digits displayed after the decimal point.
   * @return    The requested number.
   */
  public int getPrecision();
}
