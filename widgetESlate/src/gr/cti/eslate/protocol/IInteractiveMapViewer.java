package gr.cti.eslate.protocol;

/**
 * Methods that a view might ask from a viewer.
 * @author  Giorgos Vasiliou
 * @version 1.0, 22-Jul-2002
 */

public interface IInteractiveMapViewer extends IMapTreeViewer {
	/**
	 * Gets the view center in map coordinates.
	 */
	public abstract java.awt.geom.Point2D getViewCenter();
}