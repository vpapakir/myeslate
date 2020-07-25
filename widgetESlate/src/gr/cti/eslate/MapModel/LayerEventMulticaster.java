package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.LayerVisibilityEvent;

import java.util.HashSet;
import java.util.Iterator;

public class LayerEventMulticaster extends HashSet implements LayerListener {

	public LayerEventMulticaster() {
		super();
	}

	public void layerVisibilityChanged(LayerVisibilityEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerVisibilityChanged(e);
	}

	public void layerSelectionChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerSelectionChanged(e);
	}

	public void layerActiveGeographicObjectChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerActiveGeographicObjectChanged(e);
	}

	public void layerGeographicObjectAdded(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerGeographicObjectAdded(e);
	}

	public void layerGeographicObjectRemoved(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerGeographicObjectRemoved(e);
	}

	public void layerGeographicObjectRepositioned(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerGeographicObjectRepositioned(e);
	}

	public void layerColoringChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerColoringChanged(e);
	}

	public void layerTipBaseChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerTipBaseChanged(e);
	}

	public void layerLabelBaseChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerLabelBaseChanged(e);
	}

	public void layerDateFromBaseChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerDateFromBaseChanged(e);
	}

	public void layerDateToBaseChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerDateToBaseChanged(e);
	}

	public void layerDefaultVisibilityChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerDefaultVisibilityChanged(e);
	}

	public void layerCanBeHiddenChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerCanBeHiddenChanged(e);
	}

	public void layerObjectsCanBeSelectedChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerObjectsCanBeSelectedChanged(e);
	}

	public void layerNotSelectedObjectsShownDefaultChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerNotSelectedObjectsShownDefaultChanged(e);
	}

	public void layerNotSelectedObjectsShownCanChangeChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerNotSelectedObjectsShownCanChangeChanged(e);
	}

	public void layerObjectsWithNoDataShownChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerObjectsWithNoDataShownChanged(e);
	}

	public void layerPaintPropertiesChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerPaintPropertiesChanged(e);
	}

	/**
	 * Thrown when a property which is not covered by the other events is changed.
	 */
	public void layerUnknownPropertyChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerUnknownPropertyChanged(e);
	}

	public void layerGeographicObjectPropertiesChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerGeographicObjectPropertiesChanged(e);
	}

	public void layerRenamed(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerRenamed(e);
	}

	public void layerDatabaseTableChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerDatabaseTableChanged(e);
	}

	public void layerObjectVisibilityChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerObjectVisibilityChanged(e);
	}

	public void layerRasterTransparencyLevelChanged(LayerEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((LayerListener) it.next()).layerRasterTransparencyLevelChanged(e);
	}
}
