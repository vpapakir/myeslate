package gr.cti.eslate.mapModel;

import java.util.EventListener;

public interface RegionListener extends EventListener {
	public abstract void regionRenamed(RegionEvent e);
	public abstract void regionBoundingRectChanged(RegionEvent e);
	public abstract void regionLayerAdded(RegionEvent e);
	public abstract void regionLayerRemoved(RegionEvent e);
	public abstract void regionLayersSwapped(RegionEvent e);
	public abstract void regionLayersReordered(RegionEvent e);
	/**
	 * Invoked when the scale is changed.
	 */
	public abstract void regionScaleChanged(RegionEvent e);
	/**
	 * Invoked when the orientation is changed.
	 */
	public abstract void regionOrientationChanged(RegionEvent e);
	/**
	 * Invoked when a background image is added.
	 */
	public abstract void regionBackgroundImageAdded(RegionEvent e);
	/**
	 * Invoked when a background image is removed.
	 */
	public abstract void regionBackgroundImageRemoved(RegionEvent e);
	/**
	 * Invoked when the default background image is changed.
	 */
	public abstract void regionDefaultBackgroundImageChanged(RegionEvent e);
	/**
	 * Coordinate system of region changed.
	 */
	public abstract void regionCoordinateSystemChanged(RegionEvent e);
	/**
	 * This is a view specific event. A layer is active only in a view.
	 */
	public abstract void regionActiveLayerViewChanged(RegionEvent e);
	/**
	 * Invoked when the link is changed.
	 */
	public abstract void regionLinkChanged(RegionEvent e);
}
