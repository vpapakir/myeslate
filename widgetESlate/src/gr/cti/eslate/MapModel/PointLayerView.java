package gr.cti.eslate.mapModel;

import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.protocol.IPoint;
import gr.cti.eslate.protocol.IPointLayerView;

import javax.swing.Icon;

public class PointLayerView extends VectorLayerView implements IPointLayerView {
	PointLayerView() {
	}
	/**
	 * Constructs a LayerView object for layer.
	 * @param parentRegionView The RegionView that contains the LayerView.
	 * @param layer The Layer itself.
	 */
	public PointLayerView(PointLayer layer,RegionView parentRegionView) {
		super(layer,parentRegionView);
	}
	/**
	 * Gets the icon base field name for multi-iconed layers.
	 */
	public gr.cti.eslate.database.engine.AbstractTableField getIconBase() {
		return ((PointLayer) layer).getIconBase();
	}
	/**
	 * Gets the normal icon for the given point.
	 */
	public Icon getNormalIcon(IPoint p) {
		return ((PointLayer) layer).getNormalIcon((Point)p);
	}
	/**
	 * Gets the selected icon for the given point.
	 */
	public Icon getSelectedIcon(IPoint p) {
		return ((PointLayer) layer).getSelectedIcon((Point)p);
	}
	/**
	 * Gets the normal icon for the given point.
	 */
	public Icon getHighlightedIcon(IPoint p) {
		return ((PointLayer) layer).getHighlightedIcon((Point)p);
	}
	/**
	 * Gets the circle radius for painting the points.
	 */
	public int getCircleRadius() {
		return ((PointLayer) layer).getCircleRadius();
	}
	/**
	 * Whether the circle for painting the points will be filled.
	 */
	public boolean isCircleFilled() {
		return ((PointLayer) layer).isCircleFilled();
	}
	/**
	 * Point objects do not have a bounding rectangle as they occupy no area in space.
	 * The selection rectangle calculated by the layer may not fit all the point icons
	 * in the graphics context they will be painted. This method passes scaling values
	 * and letter sizes and enlarges the selection rectangle to fit all the area the
	 * Point objects actually occupy in a given graphics context.
	 */
	public java.awt.geom.Rectangle2D enlargeSelectionRectangle(double scalex,double scaley) {
		return ((PointLayer) layer).enlargeSelectionRectangle(scalex,scaley);
	}
}