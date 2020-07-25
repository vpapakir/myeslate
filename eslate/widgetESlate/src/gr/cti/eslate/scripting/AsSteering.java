package gr.cti.eslate.scripting;

/**
 * This interface describes the functionality of the Steering component
 * that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see gr.cti.eslate.steering.Steering
 */

public interface AsSteering
{
  /**
   * Set a new direction.
   * @param     newDir  The direction to set. One of Direction.N,
   *                    Direction.NE, Direction.E, Direction.SE, Direction.S,
   *                    Direction.SW, Direction.W, Direction.NW.
   */
  public void setDirection(int newDir);

  /**
   * Return the current direction.
   * @return    One of Direction.N, Direction.NE, Direction.E, Direction.SE,
   *            Direction.S, Direction.SW, Direction.W, Direction.NW.
   */
  public int getDirection();

  /**
   * Start moving.
   */
  public void go();
}
