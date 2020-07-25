package gr.cti.eslate.shapedComponent;

import java.awt.*;

/**
 * This interface describes components that have a shape, and which can use the
 * panel component's property editors to edit it.
 *
 * @version     3.0.1, 2-Jun-2006
 * @author      Kriton Kyrimis
 */
public interface ShapedComponent
{
  /**
   * Specifies the shape type to which the component is being clipped.
   * @param     shape   The clip shape tape.
   */
  public void setClipShapeType(ShapeType shape);

  /**
   * Returns the type of shape to which the component is being clipped.
   * @return    A <code>ShapeType</code> instance.
   */
  public ShapeType getClipShapeType();

  /**
   * Specifies the shape to which the component is being clipped. This method
   * should be invoked after <code>setClipShapeType</code> has been invoked.
   * It is meant to be invoked by pressing the "edit shape" button in the
   * property editor.
   */
  public void setClipShape(Shape s);

  /**
   * Returns the shape to which the component is being clipped.
   * @return    The shape to which the panel is being clipped. If the
   *            component is a regular, rectangular component,
   *            <code>null</code> is returned.
   */
  public Shape getClipShape();

  /**
   * Returns the color used to draw the shape outline in the polygon editor.
   * @return    The color used to draw the shape outline in the polygon
   * editor.
   */
  public Color getOutlineColor();

  /**
   * Add a listener for shape changed events.
   * @param     l       The listener to add.
   */
  public void addShapeChangedListener(ShapeChangedListener l);

  /**
   * Remove a listener for shape changed events.
   * @param     l       The listener to remove.
   */
  public void removeShapeChangedListener(ShapeChangedListener l);

  /**
   * Paint the component without doing any clipping or other extra work.
   * This method will be used by the shape property editor to obtain a
   * snapshot of the component.
   * <P>
   * Suggested implementation:
   * <PRE>
   * public boolean simplePaint = false;
   *
   * public void simplePrint(Graphics g)
   * {
   *   simplePaint = true;
   *   super.print(g);
   *   simplePaint = false;
   * }
   *
   * public void paint(Graphics g)
   * {
   *   if (simplePaint) {
   *     super.paint(g);
   *   }else{
   *     // Paint with clipping and any other embellishments.
   *   }
   * }
   * </PRE>
   */
  public void simplePrint(Graphics g);
}
