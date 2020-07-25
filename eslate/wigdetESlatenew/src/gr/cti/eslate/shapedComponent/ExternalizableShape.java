package gr.cti.eslate.shapedComponent;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import gr.cti.typeArray.*;

/**
 * This class encapsulates a shape, providing methods for reading and writing
 * the shape to a file.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ExternalizableShape implements Shape, Externalizable
{
  /**
   * The encapsulated shape.
   */
  private Shape s;

  static final long serialVersionUID = 7879536194076778220L;

  /**
   * Construct an <code>ExternalizableShape</code> instance.
   * @param     shape   The shape to encapsulate.
   */
  public ExternalizableShape(Shape shape)
  {
    s = shape;
  }

  /**
   * Empty constructor, for use in combination with <code>readExternal</code>.
   */
  public ExternalizableShape()
  {
    s = null;
  }

  /**
   * Wrapper for <code>Shape.contains</code>.
   */
  public boolean contains(double x, double y)
  {
    return s.contains(x, y);
  }

  /**
   * Wrapper for <code>Shape.contains</code>.
   */
  public boolean contains(double x, double y, double w, double h)
  {
    return s.contains(x, y, w, h);
  }

  /**
   * Wrapper for <code>Shape.contains</code>.
   */
  public boolean contains(Point2D p)
  {
    return s.contains(p);
  }

  /**
   * Wrapper for <code>Shape.contains</code>.
   */
  public boolean contains(Rectangle2D r)
  {
    return s.contains(r);
  }

  /**
   * Wrapper for <code>Shape.getBounds</code>.
   */
  public Rectangle getBounds()
  {
    return s.getBounds();
  }

  /**
   * Wrapper for <code>Shape.getBounds2D</code>.
   */
  public Rectangle2D getBounds2D()
  {
    return s.getBounds2D();
  }

  /**
   * Wrapper for <code>Shape.getPathIterator</code>.
   */
  public PathIterator getPathIterator(AffineTransform at)
  {
    return s.getPathIterator(at);
  }

  /**
   * Wrapper for <code>Shape.getPathIterator</code>.
   */
  public PathIterator getPathIterator(AffineTransform at, double flatness)
  {
    return s.getPathIterator(at, flatness);
  }

  /**
   * Wrapper for <code>Shape.intersects</code>.
   */
  public boolean intersects(double x, double y, double w, double h)
  {
    return s.intersects(x, y, w, h);
  }

  /**
   * Wrapper for <code>Shape.intersects</code>.
   */
  public boolean intersects(Rectangle2D r)
  {
    return s.intersects(r);
  }

  /**
   * Store the shape.
   * @param     out     The stream to which to write the shape.
   */
  public void writeExternal(ObjectOutput out) throws IOException
  {
    PathIterator it = s.getPathIterator(null);
    int rule = it.getWindingRule();
    out.writeInt(rule);
    GeneralPath gs = new GeneralPath(rule);
    s = gs;
    float[] p = new float[6];
    while (!it.isDone()) {
      int type = it.currentSegment(p);
      out.writeInt(type);
      switch (type) {
        case PathIterator.SEG_CLOSE:
          // Nothing else to write.
          break;
        case PathIterator.SEG_MOVETO:
          out.writeFloat(p[0]);
          out.writeFloat(p[1]);
          break;
        case PathIterator.SEG_LINETO:
          out.writeFloat(p[0]);
          out.writeFloat(p[1]);
          break;
        case PathIterator.SEG_QUADTO:
          out.writeFloat(p[0]);
          out.writeFloat(p[1]);
          out.writeFloat(p[2]);
          out.writeFloat(p[3]);
          break;
        case PathIterator.SEG_CUBICTO:
          out.writeFloat(p[0]);
          out.writeFloat(p[1]);
          out.writeFloat(p[2]);
          out.writeFloat(p[3]);
          out.writeFloat(p[4]);
          out.writeFloat(p[5]);
          break;
      }
      it.next();
    }
    out.writeInt(-1);
  }

  /**
   * Load the shape.
   * @param     in      The stream from which to read the shape.
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int rule = in.readInt();
    GeneralPath gp = new GeneralPath(rule);
    int type;
    while ((type = in.readInt()) >= 0) {
      switch (type) {
        case PathIterator.SEG_CLOSE:
          gp.closePath();
          break;
        case PathIterator.SEG_MOVETO:
          gp.moveTo(in.readFloat(), in.readFloat());
          break;
        case PathIterator.SEG_LINETO:
          gp.lineTo(in.readFloat(), in.readFloat());
          break;
        case PathIterator.SEG_QUADTO:
          gp.quadTo(
            in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat()
          );
          break;
        case PathIterator.SEG_CUBICTO:
          gp.curveTo(
            in.readFloat(), in.readFloat(), in.readFloat(),
            in.readFloat(), in.readFloat(), in.readFloat()
          );
          break;
      }
    }
    s = gp;
  }

  /**
   * Returns the encapsulated shape.
   * @return    The encapsulated shape.
   */
  public Shape getShape()
  {
    return s;
  }

  /**
   * Returns the encapsulated shape as a <code>Polygon</code> instance.
   * @return    The encapsulated shape as a <code>Polygon</code> instance.
   * @exception         NotAPolygonException    Thrown if the encapsulated
   *                            shape cannot be represented as a polygon.
   */
  public Polygon getPolygon() throws NotAPolygonException
  {
    PathIterator it = s.getPathIterator(null);
    IntBaseArray x = new IntBaseArray();
    IntBaseArray y = new IntBaseArray();
    float[] coords = new float[6];
    boolean haveMove = false;
    while (!it.isDone()) {
      int type = it.currentSegment(coords);
      switch (type) {
        case PathIterator.SEG_MOVETO:
          if (!haveMove) {
            x.add(Math.round(coords[0]));
            y.add(Math.round(coords[1]));
            haveMove = true;
          }else{
            throw new NotAPolygonException();
          }
          break;
        case PathIterator.SEG_LINETO:
          if (!haveMove) {
            throw new NotAPolygonException();
          }else{
            x.add(Math.round(coords[0]));
            y.add(Math.round(coords[1]));
          }
          break;
        default:
          throw new NotAPolygonException();
      }
    }
    return new Polygon(x.toArray(), y.toArray(), x.size());
  }

}
