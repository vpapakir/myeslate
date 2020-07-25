package gr.cti.eslate.mapModel;

import java.util.HashSet;
import java.util.Iterator;

public class RegionEventMulticaster extends HashSet implements RegionListener {

	public RegionEventMulticaster() {
		super();
	}

	public void regionRenamed(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionRenamed(e);
	}

	public void regionBoundingRectChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionBoundingRectChanged(e);
	}

	public void regionLayerAdded(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionLayerAdded(e);
	}

	public void regionLayerRemoved(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionLayerRemoved(e);
	}

	public void regionLayersSwapped(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionLayersSwapped(e);
	}

	public void regionLayersReordered(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionLayersReordered(e);
	}

	public void regionScaleChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionScaleChanged(e);
	}

	public void regionOrientationChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionOrientationChanged(e);
	}

	public void regionBackgroundImageAdded(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionBackgroundImageAdded(e);
	}

	public void regionBackgroundImageRemoved(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionBackgroundImageRemoved(e);
	}

	public void regionDefaultBackgroundImageChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionDefaultBackgroundImageChanged(e);
	}

	public void regionCoordinateSystemChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionCoordinateSystemChanged(e);
	}

	public void regionActiveLayerViewChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionActiveLayerViewChanged(e);
	}

	public void regionLinkChanged(RegionEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((RegionListener) it.next()).regionLinkChanged(e);
	}
}
