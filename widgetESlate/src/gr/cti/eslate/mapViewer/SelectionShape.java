package gr.cti.eslate.mapViewer;

import java.awt.Shape;

/**
 * Interface implemented by selection shapes.
 *
 * @author Giorgos Vasiliou
 * @version 2.0, 17 Aug 2002
 */
public interface SelectionShape extends Shape {
	/**
	 * Sets the bounding rectangle of the selection shape. Coordinates are
	 * given in (startX,startY,endX,endY) format and not in the usual
	 * (startX,startY,width,height).
	 * @param coords    An array containing the four coordinate numbers.
	 */
	public void setCoords(double[] coords);
	/**
	 * Gets the bounding rectangle of the selection shape. Coordinates are
	 * given in (startX,startY,endX,endY) format and not in the usual
	 * (startX,startY,width,height).
	 */
	public double[] getCoords();
	/**
	 * Gets the starting x-axis coordinate of the bounding rectangle of the shape.
	 * @return  The coordinate.
	 */
	public double getX();
	/**
	 * Gets the starting y-axis coordinate of the bounding rectangle of the shape.
	 * @return  The coordinate.
	 */
	public double getY();
	/**
	 * Gets the ending x-axis coordinate of the bounding rectangle of the shape.
	 * @return  The coordinate.
	 */
	public double getEndX();
	/**
	 * Gets the ending y-axis coordinate of the bounding rectangle of the shape.
	 * @return  The coordinate.
	 */
	public double getEndY();
	/**
	 * Gets the width of the bounding rectangle of the shape.
	 * @return  The width.
	 */
	public double getWidth();
	/**
	 * Gets the height of the bounding rectangle of the shape.
	 * @return  The height.
	 */
	public double getHeight();
	/**
	 * Adds a SelectionShapeListener to listen to shape changes.
	 * @param listener The listener.
	 */
	public void addSelectionShapeListener(SelectionShapeListener listener);
	/**
	 * Removes a SelectionShapeListener to listen to shape changes.
	 * @param listener The listener.
	 */
	public void removeSelectionShapeListener(SelectionShapeListener listener);
	/**
	 * Fires the event.
	 */
	public void fireShapeGeometryChanged();
	/**
	 * Rectangular selection shape type id.
	 */
	public static final int RECTANGULAR_SELECTION_SHAPE = 0;
	/**
	 * Circular selection shape type id.
	 */
	public static final int CIRCULAR_SELECTION_SHAPE = 1;
}