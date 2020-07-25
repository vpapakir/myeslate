package gr.cti.eslate.shapedComponent;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * This class provides static methods that process <code>Shape</code>s.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ShapeUtilities
{
  /**
   * The constructor is private, as this class only provides static methods.
   */
  private ShapeUtilities()
  {
  }

  /**
   * Convert a shape from the coordinate space of one component to the
   * coordinate space of another component.
   * @param     from    The component in whose coordinate space the given
   *                    shape is.
   * @param     shape   The shape.
   * @param     to      The component to whose coordinate space the given
   *                    space will be converted.
   * @return    The converted shape. The type of shape (<code>Rectangle</code>,
   *            <code>Ellipse2D.Float</code>, <code>Ellipse2D.Double</code>,
   *            <code>Polygon</code>, or <code>Area</code> is preserevd.
   */
  public static Shape convertShape(Component from, Shape shape, Component to)
  {
    if (shape instanceof Rectangle) {
      Rectangle r = (Rectangle)shape;
      return convertRectangle(from, r, to);
    }

    if (shape instanceof Ellipse2D.Float) {
      Ellipse2D.Float el = (Ellipse2D.Float)shape;
      return convertEllipse2DFloat(from, el, to);
    }

    if (shape instanceof Ellipse2D.Double) {
      Ellipse2D.Double el = (Ellipse2D.Double)shape;
      return convertEllipse2DDouble(from, el, to);
    }

    if (shape instanceof Polygon) {
      Polygon pol = (Polygon)shape;
      return convertPolygon(from, pol, to);
    }

    return convertArbitraryShape(from, shape, to);
  }

  /**
   * Convert a rectangle from the coordinate space of one component to the
   * coordinate space of another component.
   * @param     from    The component in whose coordinate space the given
   *                    rectangle is.
   * @param     rect    The rectangle.
   * @param     to      The component to whose coordinate space the given
   *                    rectangle will be converted.
   * @return    The converted rectangle.
   */
  public static Rectangle convertRectangle(
    Component from, Rectangle rect, Component to)
  {
    return SwingUtilities.convertRectangle(from, rect, to);
  }

  /**
   * Convert an ellipse from the coordinate space of one component to the
   * coordinate space of another component.
   * @param     from    The component in whose coordinate space the given
   *                    ellipse is.
   * @param     el      The ellipse.
   * @param     to      The component to whose coordinate space the given
   *                    ellipse will be converted.
   * @return    The converted ellipse.
   */
  public static Ellipse2D.Float convertEllipse2DFloat(
    Component from, Ellipse2D.Float el, Component to)
  {
    Point p = SwingUtilities.convertPoint(from, 0, 0, to);
    float x = el.x + p.x;
    float y = el.y + p.y;
    return new Ellipse2D.Float(x, y, el.width, el.height);
  }

  /**
   * Convert an ellipse from the coordinate space of one component to the
   * coordinate space of another component.
   * @param     from    The component in whose coordinate space the given
   *                    ellipse is.
   * @param     el      The ellipse.
   * @param     to      The component to whose coordinate space the given
   *                    ellipse will be converted.
   * @return    The converted ellipse.
   */
  public static Ellipse2D.Double convertEllipse2DDouble(
    Component from, Ellipse2D.Double el, Component to)
  {
    Point p = SwingUtilities.convertPoint(from, 0, 0, to);
    double x = el.x + p.x;
    double y = el.y + p.y;
    return new Ellipse2D.Double(x, y, el.width, el.height);
  }

  /**
   * Convert a polygon from the coordinate space of one component to the
   * coordinate space of another component.
   * @param     from    The component in whose coordinate space the given
   *                    polygon is.
   * @param     pol     The polygon.
   * @param     to      The component to whose coordinate space the given
   *                    polygon will be converted.
   * @return    The converted polygon.
   */
  public static Polygon convertPolygon(
    Component from, Polygon pol, Component to)
  {
    Point p = SwingUtilities.convertPoint(from, 0, 0, to);
    Polygon pol2 =  new Polygon(pol.xpoints, pol.ypoints, pol.npoints);
    pol2.translate(p.x, p.y);
    return pol2;
  }

  /**
   * Convert an arbitrary shape from the coordinate space of one component to
   * the coordinate space of another component.
   * @param     from    The component in whose coordinate space the given
   *                    area is.
   * @param     shape   The shape.
   * @param     to      The component to whose coordinate space the given
   *                    shape will be converted.
   * @return    The converted shape. The result is always an instance of 
   *            <code>Area</code>.
   */
  public static Area convertArbitraryShape(
    Component from, Shape shape, Component to)
  {
    Area a = new Area(shape);
    AffineTransform at = new AffineTransform();
    Point p = SwingUtilities.convertPoint(from, 0, 0, to);
    at.translate((double)p.x, (double)p.y);
    a.transform(at);
    return a;
  }
}
