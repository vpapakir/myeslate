package gr.cti.eslate.protocol;

import gr.cti.eslate.database.engine.AbstractTableField;

import javax.swing.Icon;

public interface IPointLayerView extends IVectorLayerView {
	/**
	 * Gets the icon base field name for multi-iconed layers.
	 */
	public abstract AbstractTableField getIconBase();
	/**
	 * Gets the normal icon for the given Point.
	 */
	public abstract Icon getNormalIcon(IPoint p);
	/**
	 * Gets the selected icon for the given Point.
	 */
	public abstract Icon getSelectedIcon(IPoint p);
	/**
	 * Gets the normal icon for the given Point.
	 */
	public abstract Icon getHighlightedIcon(IPoint p);
	/**
	 * Gets the circle radius for painting the points.
	 */
	public abstract int getCircleRadius();
	/**
	 * Whether the circle for painting the points will be filled.
	 */
	public boolean isCircleFilled();
	/**
	 * Point objects do not have a bounding rectangle as they occupy no area in space.
	 * The selection rectangle calculated by the layer may not fit all the point icons
	 * in the graphics context they will be painted. This method passes scaling values
	 * and letter sizes and enlarges the selection rectangle to fit all the area the
	 * Point objects actually occupy in a given graphics context.
	 */
	public java.awt.geom.Rectangle2D enlargeSelectionRectangle(double scalex,double scaley);

	static final int PAINT_AS_CIRCLE=0;
	static final int PAINT_AS_SAME_ICONS=1;
	static final int PAINT_AS_MULTIPLE_ICONS=2;
}