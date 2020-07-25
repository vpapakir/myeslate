package gr.cti.eslate.shapedComponent;

import java.awt.*;
import java.util.*;

/**
 * Event sent when the shape of an object changes.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ShapeChangedEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The type of the component's shape.
   */
  private int type;
  /**
   * The component's shape.
   */
  private Shape shape;

  /**
   * Construct a <code>ShapeChangedEvent</code> instance.
   * @param     component       The component whose shape has changed.
   * @param     type            The type of the component's shape.
   *                            One of
   *                            <code>ShapeType.RECTANGLE</code>,
   *                            <code>ShapeType.ELLIPSE</code>,
   *                            <code>ShapeType.POLYGON</code>,
   *                            <code>ShapeType.FREEHAND</code>.
   * @param     shape           The shape of the object.
   */
  public ShapeChangedEvent(Component component, int type, Shape shape)
  {
    super(component);
    this.type = type;
    this.shape = shape;
  }

  /**
   * Returns the component whose shape has changed.
   * @return    The component whose shape has changed.
   */
  public Component getComponent()
  {
    return (Component)getSource();
  }

  /**
   * Returns the type of the component's shape.
   * @return    The type of the component's shape. One of
   *                            <code>ShapeType.RECTANGLE</code>,
   *                            <code>ShapeType.ELLIPSE</code>,
   *                            <code>ShapeType.POLYGON</code>,
   *                            <code>ShapeType.FREEHAND</code>.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Returns the shape of the component.
   * @return    The shape of the component.
   */
  public Shape getShape()
  {
    return shape;
  }
}
