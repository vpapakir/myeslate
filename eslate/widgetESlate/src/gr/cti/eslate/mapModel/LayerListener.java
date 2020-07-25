package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.LayerVisibilityEvent;

import java.util.EventListener;

public interface LayerListener extends EventListener {
	public abstract void layerVisibilityChanged(LayerVisibilityEvent e);
	public abstract void layerSelectionChanged(LayerEvent e);
	public abstract void layerActiveGeographicObjectChanged(LayerEvent e);
	public abstract void layerGeographicObjectAdded(LayerEvent e);
	public abstract void layerGeographicObjectRemoved(LayerEvent e);
	public abstract void layerGeographicObjectRepositioned(LayerEvent e);
	public abstract void layerColoringChanged(LayerEvent e);
	public abstract void layerTipBaseChanged(LayerEvent e);
	public abstract void layerLabelBaseChanged(LayerEvent e);
	public abstract void layerDefaultVisibilityChanged(LayerEvent e);
	public abstract void layerCanBeHiddenChanged(LayerEvent e);
	public abstract void layerObjectsCanBeSelectedChanged(LayerEvent e);
	public abstract void layerNotSelectedObjectsShownDefaultChanged(LayerEvent e);
	public abstract void layerNotSelectedObjectsShownCanChangeChanged(LayerEvent e);
	public abstract void layerObjectsWithNoDataShownChanged(LayerEvent e);
	public abstract void layerPaintPropertiesChanged(LayerEvent e);
	/**
	 * Thrown when a property which is not covered by the other events is changed.
	 */
	public abstract void layerUnknownPropertyChanged(LayerEvent e);
	public abstract void layerGeographicObjectPropertiesChanged(LayerEvent e);
	public abstract void layerRenamed(LayerEvent e);
	public abstract void layerDateFromBaseChanged(LayerEvent e);
	public abstract void layerDateToBaseChanged(LayerEvent e);
	public abstract void layerDatabaseTableChanged(LayerEvent e);
	public abstract void layerObjectVisibilityChanged(LayerEvent e);
	public abstract void layerRasterTransparencyLevelChanged(LayerEvent e);
}
