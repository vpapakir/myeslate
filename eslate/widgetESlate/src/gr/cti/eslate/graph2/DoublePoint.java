package gr.cti.eslate.graph2;

import java.awt.geom.*;
import java.io.*;

/**
 * This class extends <code>Point2D.Double</code> to make it serializable.
 * (<code>Point2D.Double</code> is serializable in Java 6, but not in Java 5.)
 * @version     1.0.6, 16-Jan-2008
 * @author      Kriton Kyrimis
 */
public class DoublePoint extends Point2D.Double implements Serializable
{
  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs and initializes a <code>DoublePoint</code> with coordinates
   * (0, 0).
   */
  public DoublePoint()
  {
    super();
  }

  /**
   * Constructs and initializes a <code>DoublePoint</code> with the specified
   * coordinates.
   * @param     x       The X coordinate of the newly constructed
   *                    <code>DoublePint</code>.
   * @param     y       The Y coordinate of the newly constructed
   *                    <code>DoublePint</code>.
   */
  public DoublePoint(double x, double y)
  {
    super(x, y);
  }
}
