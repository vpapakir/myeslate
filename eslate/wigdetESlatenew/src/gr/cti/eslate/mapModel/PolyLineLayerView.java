package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IPolyLineLayerView;

public class PolyLineLayerView extends VectorLayerView implements IPolyLineLayerView {
	PolyLineLayerView() {
	}
	/**
	 * Constructs a LayerView object for layer.
	 * @param layer The Layer itself.
	 */
	public PolyLineLayerView(PolyLineLayer layer,RegionView parentRegionView) {
		super(layer,parentRegionView);
	}
	/**
	 * Gets the line width for the polygon outline.
	 */
	public int getLineWidth() {
		return ((PolyLineLayer) layer).getLineWidth();
	}
	/**
	 * Drawing error tolerance. Small value better quality, big value greater speed.
	 */
	public float getErrorTolerance() {
		return ((PolyLineLayer) layer).getErrorTolerance();
	}
}