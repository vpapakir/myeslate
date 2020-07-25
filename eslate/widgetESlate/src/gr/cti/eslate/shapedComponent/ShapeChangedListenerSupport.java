package gr.cti.eslate.shapedComponent;

import java.awt.*;

/**
 * Helper class, implementing listener registration and firing for
 * <code>ShapeChangedListener</code>s.
 *
 * @version     3.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ShapeChangedListenerSupport
{
  /**
   * The listeners being managed.
   */
  private ShapeChangedListenerBaseArray listeners =
    new ShapeChangedListenerBaseArray();
  /**
   * The component whose shape changes the listeners will track.
   */
  private Component component;

  /**
   * Create a new <code>ShapeChangedListenerSupport</code> instance.
   * @param     component       The component whose shape changes the
   *                            listeners will track.
   */
  public ShapeChangedListenerSupport(Component component)
  {
    super();
    this.component = component;
  }

  /**
   * Add a <code>ShapeChangedListener</code>.
   * @param     l       The listener to add.
   */
  public void addShapeChangedListener(ShapeChangedListener l)
  {
    synchronized(listeners) {
      if (!listeners.contains(l)) {
        listeners.add(l);
      }
    }
  }

  /**
   * Remove a <code>ShapeChangedListener</code>.
   * @param     l       The listener to remove.
   */
  public void removeShapeChangedListener(ShapeChangedListener l)
  {
    synchronized(listeners) {
      int i = listeners.indexOf(l);
      if (i >= 0) {
        listeners.remove(i);
      }
    }
  }

  /**
   * Fire all registered <code>ShapeChangedListener</code>s.
   * @param     type    The new shape type of the component. One of
   *                    <code>ShapeType.RECTANGLE</code>,
   *                    <code>ShapeType.ELLIPSE</code>,
   *                    <code>ShapeType.POLYGON</code>,
   *                    <code>ShapeType.FREEHAND</code>.
   * @param     shape   The new shape of teh component.
   */
  public void fireShapeChangedListeners(int type, Shape shape)
  {
    ShapeChangedListenerBaseArray listeners2;
    synchronized(listeners) {
      listeners2 = (ShapeChangedListenerBaseArray)(listeners.clone());
    }
    int n = listeners2.size();
    for (int i=0; i<n; i++) {
      ShapeChangedListener l = listeners.get(i);
      ShapeChangedEvent e = new ShapeChangedEvent(component, type, shape);
      l.shapeChanged(e);
    }
  }
}
