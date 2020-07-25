package gr.cti.eslate.protocol;


public interface IPolygonLayerView extends IVectorLayerView {
	/**
	 * Gets the line width for the polygon outline.
	 */
	public abstract int getLineWidth();
	/**
	 * Whether the polygons will be filled.
	 */
	public abstract boolean isPolygonFilled();
	/**
	 * Drawing error tolerance. Small value better quality, big value greater speed.
	 */
	public abstract float getErrorTolerance();

	static final int PAINT_AS_STRAIGHT_LINE=0;
	static final int PAINT_AS_DASHED_LINE=1;
	static final int PAINT_AS_DOTTED_LINE=2;
}
