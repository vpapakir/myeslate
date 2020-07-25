package gr.cti.eslate.mapModel;

import java.util.HashSet;
import java.util.Iterator;

public class MapEventMulticaster extends HashSet implements MapListener {

	public MapEventMulticaster() {
		super();
	}

	/**
	 * Invoked when a Map component changes its contents.
	 */
	public void mapChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapChanged(e);
	}

	public void mapActiveRegionChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapActiveRegionChanged(e);
	}

	public void mapRenamed(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapRenamed(e);
	}

	public void mapDatabaseChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapDatabaseChanged(e);
	}

	public void mapBoundingRectChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapBoundingRectChanged(e);
	}

	public void mapAuthorChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapAuthorChanged(e);
	}

	public void mapCreationDateChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapCreationDateChanged(e);
	}

	public void mapCommentsChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapCommentsChanged(e);
	}

	public void mapEntryNodeChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapEntryNodeChanged(e);
	}

	public void mapRegionAdded(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapRegionAdded(e);
	}

	public void mapRegionRemoved(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapRegionRemoved(e);
	}

	public void mapClosed(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapClosed(e);
	}

	public void mapDateIntervalChanged(MapEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((MapListener) it.next()).mapDateIntervalChanged(e);
	}
}
