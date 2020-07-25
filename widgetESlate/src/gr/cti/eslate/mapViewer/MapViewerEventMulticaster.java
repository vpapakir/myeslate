package gr.cti.eslate.mapViewer;

import java.util.ArrayList;

public class MapViewerEventMulticaster extends ArrayList implements MapViewerListener {

	public MapViewerEventMulticaster() {
		super();
	}

	public void mousePressedOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mousePressedOnMapArea(e);
	}
	public void mouseDraggedOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mouseDraggedOnMapArea(e);
	}
	public void mouseReleasedOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mouseReleasedOnMapArea(e);
	}
	public void mouseClickedOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mouseClickedOnMapArea(e);
	}
	public void mouseMovedOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mouseMovedOnMapArea(e);
	}
	public void mouseExitedOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mouseExitedOnMapArea(e);
	}
	public void mouseEnteredOnMapArea(MapViewerMouseEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mouseEnteredOnMapArea(e);
	}
	/**
	 * Invoked when the scale changes.
	 */
	public void mapViewerScaleChanged(MapViewerEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mapViewerScaleChanged(e);
	}

	/**
	 * Invoked when the busy state of the viewer changes.
	 */
	public void mapViewerBusyStatusChanged(MapViewerEvent e) {
		for (int i=0;i<size();i++)
			((MapViewerListener) get(i)).mapViewerBusyStatusChanged(e);
	}
}