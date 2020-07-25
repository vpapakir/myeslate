package gr.cti.eslate.protocol;

import java.awt.Color;

/**
 * Vector layer view interface.
 */
public interface IVectorLayerView extends ILayerView {
	/**
	 * Gets fill color.
	 * @return The color.
	 */
	public abstract Color getNormalFillColor();
	/**
	 * Gets outline color.
	 * @return The color.
	 */
	public abstract Color getNormalOutlineColor();
	/**
	 * Gets selected outline color.
	 * @return The color.
	 */
	public abstract Color getSelectedOutlineColor();
	/**
	 * Gets selected fill color.
	 * @return The color.
	 */
	public abstract Color getSelectedFillColor();
	/**
	 * Gets highlight outline color.
	 * @return The color.
	 */
	public abstract Color getHighlightedOutlineColor();
	/**
	 * Gets highlight fill color.
	 * @return The color.
	 */
	public abstract Color getHighlightedFillColor();
	/**
	 * Tries to add an object to the layer.
	 */
	public abstract void addObject(double tlX,double tlY,double brX,double brY,GeographicObject go) throws IncompatibleObjectTypeException,CannotAddObjectException;
	/**
	 * Tries to add an object to the layer and sets a field value in the newly created record in the table.
	 */
	public abstract void addObject(double tlX,double tlY,double brX,double brY,GeographicObject go,String fieldName,Object value) throws IncompatibleObjectTypeException,CannotAddObjectException;
	/**
	 * Repositions an object in the layer.
	 */
	public abstract void repositionObject(IVectorGeographicObject go,double xOffset,double yOffset);
}