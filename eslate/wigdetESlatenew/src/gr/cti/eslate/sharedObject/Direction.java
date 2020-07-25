package gr.cti.eslate.sharedObject;

import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;

/**
 * This class implements a "direction" shared object, specifying one of the
 * eight compass points.
 *
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class Direction extends SharedObject
{
  private int dir;

  /**
   * North.
   */
  public static final int N  = 1;
  /**
   * Northeast.
   */
  public static final int NE = 2;
  /**
   * East.
   */
  public static final int E  = 3;
  /**
   * SouthEast.
   */
  public static final int SE = 4;
  /**
   * South.
   */
  public static final int S  = 5;
  /**
   * Southwest.
   */
  public static final int SW = 6;
  /**
   * West.
   */
  public static final int W  = 7;
  /**
   * Northwest.
   */
  public static final int NW = 8;

  /**
   * Construct a Direction shared object.
   * @param     handle  The E-Slate handle of the component to which the
   * shared object belongs.
   */
  public Direction(ESlateHandle handle)
  {
    super(handle);
    dir = N;
  }

  /**
   * Set the value of the shared object's direction.
   * @param     newDir  Value of direction.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a plug to its output
   *                    part.
   */
  @SuppressWarnings(value={"deprecation"})
  public void setDirection(int newDir, Vector path)
  {
    if (newDir != dir) {
      dir = newDir;

      // Create an event
      SharedObjectEvent soe = new SharedObjectEvent(this, path);

      fireSharedObjectChanged(soe);     // Notify the listeners
    }
  }

  /**
   * Set the value of the shared object's direction.
   * @param     newDir  Value of direction.
   */
  public void setDirection(int newDir)
  {
    if (newDir != dir) {
      dir = newDir;

      // Create an event
      SharedObjectEvent soe = new SharedObjectEvent(this);

      fireSharedObjectChanged(soe);       // Notify the listeners
    }
  }

  /**
   * Return the shared object's direction.
   * @return    The direction.
   */
  public int getDirection()
  {
    return dir;
  }

}
