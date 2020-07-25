package gr.cti.eslate.shapedComponent;

/**
 * This class encapsulates a shape type and the component to which it refers.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ShapeType
{
  /**
   * The encapsulated shape type.
   */
  public int type;
  /**
   * The encapsulated component.
   */
  public ShapedComponent component;
  /**
   * Rectangle (default rectanngular component).
   */
  public final static int RECTANGLE = 0;
  /**
   * Ellipse (ellipse bound by component's bounding box).
   */
  public final static int ELLIPSE = 1;
  /**
   * Arbitrary polygon.
   */
  public final static int POLYGON = 2;
  /**
   * Arbitrary shape.
   */
  public final static int FREEHAND = 3;

  /**
   * Construct a ShapeType instance.
   * @param
   */
  public ShapeType(int type, ShapedComponent component)
  {
    this.type = type;
    this.component = component;
  }
}
