package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;

/**
 * This class implements a "vector" shared object, containing a (x, y) pair.
 *
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class VectorData extends SharedObject
{
  /**
   * The X coordinate of the vector.
   */
  private double x;
  /**
   * The Y coordinate of the vector.
   */
  private double y;

  /**
   * Construct a vectorData shared object.
   * @param     handle  The E-Slate handle of the component to which the
   * shared object belongs.
   */
  public VectorData(ESlateHandle handle)
  {
    super(handle);
    x = 0.0;
    y = 0.0;
  }

  /**
   * Set the value of the shared object's vector.
   * @param     newX    The vector's x xomponent.
   * @param     newY    he vector's y component.
   */
  public void setVectorData(double newX, double newY)
  {
    if ((x != newX) || (y != newY)) {
      x = newX;
      y = newY;

      // Create an event
      SharedObjectEvent soe = new SharedObjectEvent(this);

      fireSharedObjectChanged(soe);     // Notify the listeners
    }
  }

  /**
   * Return the value of the shared object's vector's x component.
   * @return    The x component.
   */
  public double getX()
  {
    return x;
  }

  /**
   * Return the value of the shared object's vector's y component.
   * @return    The y component.
   */
  public double getY()
  {
    return y;
  }

}
