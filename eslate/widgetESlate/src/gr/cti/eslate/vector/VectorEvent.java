package gr.cti.eslate.vector;

import java.util.*;

import gr.cti.eslate.base.*;

/**
 * Event sent when the value of the vector changes.
 * The event's getSource() method will return the E-Slate handle of the
 * vector.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class VectorEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The old x value of the vector.
   */
  private double oldX;
  /**
   * The old y value of the vector.
   */
  private double oldY;
  /**
   * The new x value of the vector.
   */
  private double newX;
  /**
   * The new Y value of the vector.
   */
  private double newY;

  /**
   * Constructs a vector event.
   * @param     handle  The E-Slate handle of the vector whose value has
   *                    changed.
   * @param     oldX    The old x value of the vector.
   * @param     oldY    The old y value of the vector.
   * @param     newX    The new x value of the vector.
   * @param     newY    The new y value of the vector.
   */
  VectorEvent
    (ESlateHandle handle, double oldX, double oldY, double newX, double newY)
  {
    super(handle);
    this.oldX = oldX;
    this.oldY = oldY;
    this.newX = newX;
    this.newY = newY;
  }

  /**
   * Returns the old x value of the vector.
   * @return    The old x value of the vector.
   */
  public double getOldX()
  {
    return oldX;
  }

  /**
   * Returns the old y value of the vector.
   * @return    The old y value of the vector.
   */
  public double getOldY()
  {
    return oldY;
  }

  /**
   * Returns the new x value of the vector.
   * @return    The new x value of the vector.
   */
  public double getNewX()
  {
    return newX;
  }

  /**
   * Returns the new y value of the vector.
   * @return    The new y value of the vector.
   */
  public double getNewY()
  {
    return newY;
  }

  /**
   * Returns the old length of the vector.
   * @return    The old length of the vector.
   */
  public double getOldLength()
  {
    return Math.sqrt(oldX*oldX + oldY*oldY);
  }

  /**
   * Returns the new length of the vector.
   * @return    The new length of the vector.
   */
  public double getNewLength()
  {
    return Math.sqrt(newX*newX + newY*newY);
  }

  /**
   * Returns the old angle of the vector.
   * @return    The old angle of the vector.
   */
  public double getOldAngle()
  {
    double degrees = Math.atan2(oldY, oldX) * 180.0 / Math.PI;
    if (degrees < 0.0) {
      degrees += 360.0;
    }
    return degrees;
  }

  /**
   * Returns the new angle of the vector.
   * @return    The new angle of the vector.
   */
  public double getNewAngle()
  {
    double degrees = Math.atan2(newY, newX) * 180.0 / Math.PI;
    if (degrees < 0.0) {
      degrees += 360.0;
    }
    return degrees;
  }
}
