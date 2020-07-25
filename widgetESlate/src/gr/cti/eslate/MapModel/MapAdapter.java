package gr.cti.eslate.mapModel;


public abstract class MapAdapter implements MapListener {
	/**
	 * Invoked when a Map component changes its contents.
	 */
	public void mapChanged(MapEvent e) {}
	/**
	 * Invoked when a Map (a set of regions) is renamed.
	 */
	public void mapRenamed(MapEvent e) {}
	/**
	 * Invoked when the active region is changed.
	 */
	public void mapActiveRegionChanged(MapEvent e) {}
	/**
	 * Invoked when the database is changed.
	 */
	public void mapDatabaseChanged(MapEvent e) {}
	/**
	 * Invoked when the bounding rectangle of the map is modified.
	 */
	public void mapBoundingRectChanged(MapEvent e) {}
	/**
	 * Invoked when the author of the map is modified.
	 */
	public void mapAuthorChanged(MapEvent e) {}
	/**
	 * Invoked when the creation date of the map is modified.
	 */
	public void mapCreationDateChanged(MapEvent e) {}
	/**
	 * Invoked when the comments of the map are modified.
	 */
	public void mapCommentsChanged(MapEvent e) {}
	/**
	 * Invoked when the entry node of the map is changed.
	 */
	public void mapEntryNodeChanged(MapEvent e) {}
	/**
	 * Invoked when a new region is added in the structure.
	 */
	public void mapRegionAdded(MapEvent e) {}
	/**
	 * Invoked when a region is being removed from the structure.
	 */
	public void mapRegionRemoved(MapEvent e) {}
	/**
	 * Invoked when a map is closed. A viewer should care of the consequences.
	 */
	public void mapClosed(MapEvent e) {}
	/**
	 * Invoked when the date interval shown by the view is changed. A viewer should care for the consequences.
	 */
	public void mapDateIntervalChanged(MapEvent e) {}
}
