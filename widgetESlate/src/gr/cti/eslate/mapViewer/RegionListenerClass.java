package gr.cti.eslate.mapViewer;

class RegionListenerClass extends gr.cti.eslate.mapModel.RegionAdapter {
	RegionListenerClass(MapViewer viewer) {
		this.viewer=viewer;
	}

	private MapViewer viewer;
}
