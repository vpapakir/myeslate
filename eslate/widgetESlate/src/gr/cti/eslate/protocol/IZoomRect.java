package gr.cti.eslate.protocol;

/**
 * Tags an object as a Zoom Rectangle Geographic Object.
 * @author Giorgos Vasiliou
 * @version 1.0, 30-Oct-2002
 */
public interface IZoomRect extends java.awt.Shape {
	/**
	 * Sets the X-coordinate of the zoom rectangle.
	 * @param d The X-coordinate.
	 */
	public abstract void setX(double d);
	/**
	 * Sets the Y-coordinate of the zoom rectangle.
	 * @param d The Y-coordinate.
	 */
	public abstract void setY(double d);
	/**
	 * Sets the width of the zoom rectangle.
	 * @param d The width.
	 */
	public abstract void setWidth(double d);
	/**
	 * Sets the height of the zoom rectangle.
	 * @param d The height.
	 */
	public abstract void setHeight(double d);
	/**
	 * Gets the X-coordinate of the zoom rectangle.
	 * @return  The X-coordinate.
	 */
	public abstract double getX();
	/**
	 * Gets the Y-coordinate of the zoom rectangle.
	 * @return  The Y-coordinate.
	 */
	public abstract double getY();
	/**
	 * Gets the width of the zoom rectangle.
	 * @return  The width.
	 */
	public abstract double getWidth();
	/**
	 * Gets the height of the zoom rectangle.
	 * @return  The height.
	 */
	public abstract double getHeight();
	/**
	 * Creates a new zoom rectangle object which is
	 * the transformed instance of this rectangle
	 * using the given transformation.
	 */
	public abstract IZoomRect createTransformedShape(java.awt.geom.AffineTransform at);
	/**
	 * The name of the rectangle that this rectangle
	 * is pointing to.
	 */
	public abstract String getName();
}