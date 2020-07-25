package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IRasterLayerView;

import java.awt.image.BufferedImage;

/**
 * This class wraps a raster map layer in the communication mechanism.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 29-Mar-2001
 * @see		gr.cti.eslate.mapModel.LayerView
 * @see		gr.cti.eslate.mapModel.MapView
 * @see		gr.cti.eslate.mapModel.RegionView
 */
public class RasterLayerView extends LayerView implements IRasterLayerView {
	RasterLayerView() {
		super();
	}
	/**
	 * Constructs a VectorLayerView object for a vector layer.
	 * @param layer The Layer itself.
	 */
	RasterLayerView(RasterLayer layer,RegionView parentRegionView) {
		super(layer,parentRegionView);
		this.layer=layer;
	}
	/**
	 * The raster. If it is swapped out, the method loads it as well.
	 */
	public BufferedImage getRaster() {
		return layer.getRaster();
	}
	/**
	 * Gets the GeographicObject in the given coordinates.
	 * @return  The GeographicObject.
	 */
	public GeographicObject getGeographicObjectAt(double longitude,double latitude,double tolerance) {
		return layer.getGeographicObjectAt(longitude,latitude,true);
	}
	/**
	 * The transparency level of the color areas when in normal view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	public int getNormalViewTransparencyLevel() {
		return layer.getNormalViewTransparencyLevel();
	}
	/**
	 * The transparency level of the color areas when in selected view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	public int getSelectedViewTransparencyLevel() {
		return layer.getSelectedViewTransparencyLevel();
	}
	/**
	 * The transparency level of the color areas when in highlighted view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	public int getHighlightedViewTransparencyLevel() {
		return layer.getHighlightedViewTransparencyLevel();
	}

	private RasterLayer layer;
}