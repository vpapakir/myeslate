package gr.cti.eslate.protocol;

/**
 * Tags an object as a Point Geographic Object.
 * @author Giorgos Vasiliou
 * @version 1.0, 30-Oct-2002
 */
public interface IPoint extends GeographicObject,java.awt.Shape {
	/**
	 * Gets the X coordinate of the Point.
	 */
	public abstract double getX();
	/**
	 * Gets the Y coordinate of the Point.
	 */
	public abstract double getY();
}