package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.CannotAddObjectException;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IncompatibleObjectTypeException;

import java.awt.Color;

/**
 * This class wraps a vector map layer in the communication mechanism.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 29-Mar-2001
 * @see		gr.cti.eslate.mapModel.LayerView
 * @see		gr.cti.eslate.mapModel.MapView
 * @see		gr.cti.eslate.mapModel.RegionView
 */
public abstract class VectorLayerView extends LayerView {
	VectorLayerView() {
		super();
	}
	/**
	 * Constructs a VectorLayerView object for a vector layer.
	 * @param layer The Layer itself.
	 */
	VectorLayerView(VectorLayer layer,RegionView parentRegionView) {
		super(layer,parentRegionView);
	}
	/**
	 * Tries to add an object to the layer.
	 */
	public void addObject(double tlX,double tlY,double brX,double brY,GeographicObject go) throws IncompatibleObjectTypeException,CannotAddObjectException {
		((VectorLayer) layer).addObject(tlX,tlY,brX,brY,go);
	}
	/**
	 * Tries to add an object to the layer and sets a field value in the newly created record in the table.
	 */
	public void addObject(double tlX,double tlY,double brX,double brY,GeographicObject go,String fieldName,Object value) throws IncompatibleObjectTypeException,CannotAddObjectException {
		((VectorLayer) layer).addObject(tlX,tlY,brX,brY,go,fieldName,value);
	}
	/**
	 * Repositions an object in the layer.
	 */
	public void repositionObject(IVectorGeographicObject go,double xOffset,double yOffset) {
		((VectorLayer) layer).repositionObject(go,xOffset,yOffset);
	}
	/**
	 * Gets the normal fill color.
	 * @return The color.
	 */
	public Color getNormalFillColor() {
		return ((VectorLayer) layer).getNormalFillColor();
	}
	/**
	 * Gets the normal outline color.
	 * @return The color.
	 */
	public Color getNormalOutlineColor() {
		return ((VectorLayer) layer).getNormalOutlineColor();
	}
	/**
	 * Gets the selected outline color.
	 * @return The color.
	 */
	public Color getSelectedOutlineColor() {
		return ((VectorLayer) layer).getSelectedOutlineColor();
	}
	/**
	 * Gets the selected fill color.
	 * @return The color.
	 */
	public Color getSelectedFillColor() {
		return ((VectorLayer) layer).getSelectedFillColor();
	}
	/**
	 * Gets the highlighted outline color.
	 * @return The color.
	 */
	public Color getHighlightedOutlineColor() {
		return ((VectorLayer) layer).getHighlightedOutlineColor();
	}
	/**
	 * Gets the highlighted fill color.
	 * @return The color.
	 */
	public Color getHighlightedFillColor() {
		return ((VectorLayer) layer).getHighlightedFillColor();
	}
}