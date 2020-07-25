package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IPolygonLayerView;

public class PolygonLayerView extends VectorLayerView implements IPolygonLayerView {
	PolygonLayerView() {
	}
	/**
	 * Constructs a LayerView object for layer.
	 * @param layer The Layer itself.
	 */
	public PolygonLayerView(PolygonLayer layer,RegionView parentRegionView) {
		super(layer,parentRegionView);
	}
	/**
	 * Gets the line width for the polygon outline.
	 */
	public int getLineWidth() {
		return ((PolygonLayer) layer).getLineWidth();
	}
	/**
	 * Whether the polygons will be filled.
	 */
	public boolean isPolygonFilled() {
		return ((PolygonLayer) layer).isPolygonFilled();
	}
	/**
	 * Drawing error tolerance. Small value better quality, big value greater speed.
	 */
	public float getErrorTolerance() {
		return ((PolygonLayer) layer).getErrorTolerance();
	}
}
