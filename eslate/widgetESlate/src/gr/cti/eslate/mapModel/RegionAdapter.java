package gr.cti.eslate.mapModel;


public abstract class RegionAdapter implements RegionListener {
	public void regionRenamed(RegionEvent e) {}
	public void regionBoundingRectChanged(RegionEvent e) {}
	public void regionLayerAdded(RegionEvent e) {}
	public void regionLayerRemoved(RegionEvent e) {}
	public void regionLayersSwapped(RegionEvent e) {}
	public void regionLayersReordered(RegionEvent e) {}
	/**
	 * Invoked when the scale is changed.
	 */
	public void regionScaleChanged(RegionEvent e) {}
	/**
	 * Invoked when the orientation is changed.
	 */
	public void regionOrientationChanged(RegionEvent e) {}
	/**
	 * Invoked when a background image is added.
	 */
	public void regionBackgroundImageAdded(RegionEvent e) {}
	/**
	 * Invoked when a background image is removed.
	 */
	public void regionBackgroundImageRemoved(RegionEvent e) {}
	/**
	 * Invoked when the default background image is changed.
	 */
	public void regionDefaultBackgroundImageChanged(RegionEvent e) {}
	/**
	 * Coordinate system of region changed.
	 */
	public void regionCoordinateSystemChanged(RegionEvent e) {}
	/**
	 * This is a view specific event. A layer is active only in a view.
	 */
	public void regionActiveLayerViewChanged(RegionEvent e) {}
	/**
	 * Invoked when the link is changed.
	 */
	public void regionLinkChanged(RegionEvent e) {}
}
