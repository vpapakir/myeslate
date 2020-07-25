package gr.cti.eslate.mapViewer;

/**
 * A typical adapter class.
 * @see         gr.cti.eslate.mapViewer.MapViewerListener
 * @author      Giorgos Vasiliou
 * @version     1.0
 */

public class MapViewerAdapter implements MapViewerListener {
	/**
	 * Invoked when the user presses the mouse button on the map area (only).
	 */
	public void mousePressedOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the user drags the mouse on the map area (only).
	 */
	public void mouseDraggedOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the user releases the mouse button on the map area (only).
	 */
	public void mouseReleasedOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the user clicks the mouse on the map area (only).
	 */
	public void mouseClickedOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the user moves the mouse on the map area (only).
	 */
	public void mouseMovedOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the user moves the mouse outside the map area (only).
	 */
	public void mouseExitedOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the user moves the mouse inside the map area (only).
	 */
	public void mouseEnteredOnMapArea(MapViewerMouseEvent e) {}
	/**
	 * Invoked when the scale changes.
	 */
	public void mapViewerScaleChanged(MapViewerEvent e) {}

	/**
	 * Invoked when the busy state of the viewer changes.
	 */
	public void mapViewerBusyStatusChanged(MapViewerEvent e) {}
}