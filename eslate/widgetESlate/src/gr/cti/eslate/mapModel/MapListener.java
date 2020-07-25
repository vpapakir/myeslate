package gr.cti.eslate.mapModel;

import java.util.EventListener;

public interface MapListener extends EventListener {
	/**
	 * Invoked when a Map component changes its contents.
	 */
	public abstract void mapChanged(MapEvent e);
	/**
	 * Invoked when a Map (a set of regions) is renamed.
	 */
	public abstract void mapRenamed(MapEvent e);
	/**
	 * Invoked when the active region is changed.
	 */
	public abstract void mapActiveRegionChanged(MapEvent e);
	/**
	 * Invoked when the database is changed.
	 */
	public abstract void mapDatabaseChanged(MapEvent e);
	/**
	 * Invoked when the bounding rectangle of the map is modified.
	 */
	public abstract void mapBoundingRectChanged(MapEvent e);
	/**
	 * Invoked when the author of the map is modified.
	 */
	public abstract void mapAuthorChanged(MapEvent e);
	/**
	 * Invoked when the creation date of the map is modified.
	 */
	public abstract void mapCreationDateChanged(MapEvent e);
	/**
	 * Invoked when the comments of the map are modified.
	 */
	public abstract void mapCommentsChanged(MapEvent e);
	/**
	 * Invoked when the entry node of the map is changed.
	 */
	public abstract void mapEntryNodeChanged(MapEvent e);
	/**
	 * Invoked when a new region is added in the structure.
	 */
	public abstract void mapRegionAdded(MapEvent e);
	/**
	 * Invoked when a region is being removed from the structure.
	 */
	public abstract void mapRegionRemoved(MapEvent e);
	/**
	 * Invoked when a map is closed. A viewer should care for the consequences.
	 */
	public abstract void mapClosed(MapEvent e);
	/**
	 * Invoked when the date interval shown by the view is changed. A viewer should care for the consequences.
	 */
	public abstract void mapDateIntervalChanged(MapEvent e);
}
