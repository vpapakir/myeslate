package gr.cti.eslate.protocol;


public interface IPolyLineLayerView extends IVectorLayerView {
	/**
	 * Gets the line width.
	 */
	public abstract int getLineWidth();
	/**
	 * Drawing error tolerance. Small value better quality, big value greater speed.
	 */
	public abstract float getErrorTolerance();

	static final int PAINT_AS_STRAIGHT_LINE=0;
	static final int PAINT_AS_DASHED_LINE=1;
	static final int PAINT_AS_DOTTED_LINE=2;
}
