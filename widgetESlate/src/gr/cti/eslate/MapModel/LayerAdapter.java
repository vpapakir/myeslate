package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.LayerVisibilityEvent;

public abstract class LayerAdapter implements LayerListener {
	public void layerVisibilityChanged(LayerVisibilityEvent e) {}
	public void layerSelectionChanged(LayerEvent e) {}
	public void layerActiveGeographicObjectChanged(LayerEvent e) {}
	public void layerGeographicObjectAdded(LayerEvent e) {}
	public void layerGeographicObjectRemoved(LayerEvent e) {}
	public void layerGeographicObjectRepositioned(LayerEvent e) {}
	public void layerColoringChanged(LayerEvent e) {}
	public void layerTipBaseChanged(LayerEvent e) {}
	public void layerLabelBaseChanged(LayerEvent e) {}
	public void layerDefaultVisibilityChanged(LayerEvent e) {}
	public void layerCanBeHiddenChanged(LayerEvent e) {}
	public void layerObjectsCanBeSelectedChanged(LayerEvent e) {}
	public void layerNotSelectedObjectsShownDefaultChanged(LayerEvent e) {}
	public void layerNotSelectedObjectsShownCanChangeChanged(LayerEvent e) {}
	public void layerObjectsWithNoDataShownChanged(LayerEvent e) {}
	public void layerPaintPropertiesChanged(LayerEvent e) {}
	/**
	 * Thrown when a property which is not covered by the other events is changed.
	 */
	public void layerUnknownPropertyChanged(LayerEvent e) {}
	public void layerGeographicObjectPropertiesChanged(LayerEvent e) {}
	public void layerRenamed(LayerEvent e) {}
	public void layerDateFromBaseChanged(LayerEvent e) {}
	public void layerDateToBaseChanged(LayerEvent e) {}
	public void layerDatabaseTableChanged(LayerEvent e) {}
	public void layerObjectVisibilityChanged(LayerEvent e) {}
	public void layerRasterTransparencyLevelChanged(LayerEvent e) {}
}
