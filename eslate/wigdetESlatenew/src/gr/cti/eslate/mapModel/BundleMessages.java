package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

/**
 * Messages Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 24-Jan-2000
 */
public class BundleMessages extends ListResourceBundle {
	public Object [][] getContents() {
		return contents;
	}

	static final Object[][] contents={
		{"mymap",           "My map"},
		{"myregion",        "My region"},
		{"mylayer",         "My layer"},
		{"mapnotsaved",     "This map has not been saved.\nDo you want to save it before closing?"},
		{"cantsave",        "Cannot save the map!\nMake sure the drive or the file is not write protected and that there is enough free space in the drive."},
		{"cantopen",        "Cannot load the map!\nProbably this is a previous version file, not a valid file\nor some other application is using it."},
		{"AND",             "AND"},
		{"OR",              "OR"},
		{"loadingmap",      "Loading map..."},
		{"savingmap",       "Saving map..."},
		{"confirm",         "Confirm close"},
		{"backgroundio",    "Adding or removing a background image requires saving the region in the current microworld or file.\nDo you wish to continue?"},
		{"LoadTimer",                 "Map load"},
		{"SaveTimer",                 "Map save"},
		{"ConstructorTimer",          "Map constructor"},
		{"InitESlateAspectTimer",     "Map e-Slate part creation"},
		{"PerformOpenTimer",          "PerformOpen() time"},
		{"OpenSubTreeTimer",          "OpenSubTree() time"},
		{"SetMapRootTimer",           "SetMapRoot() time"},
		{"ReadUnusedLayersTimer",     "Open unused layers time"},
		{"RefreshTimer",              "Refresh() time"},
		{"OpenRegionTimer",           "OpenRegion() time (without openLayer())"},
		{"RegionReadFieldMapTimer",   "Region FieldMap read time"},
		{"RegionRestoreAttribsTimer", "Region restoreRegionAttributes() time"},
		{"RegionChangeDirTimer",      "Region navigate struct file time"},
		{"RegionBgrReadTimer",        "Region read backgrounds time"},
		{"OpenLayerTimer",            "OpenLayer() time"},
		{"LayerChangeDirTimer",       "Layer navigate struct file time"},
		{"LayerReadFieldMapTimer",    "Layer FieldMap read time"},
		{"LayerRestoreAttribsTimer",  "Layer restoreLayerAttributes() time"},
		{"MapReadStreamTimer",        "Map readStream() time"},
		{"MapReadFieldMapTimer",      "Map read FieldMap time"},
		{"CheckNLoadTimer",           "CheckNLoad time"},
		{"CheckNLoadChangeDirTimer",  "CheckNLoad navigate struct file time"},
		{"CheckNLoadReadObjectTimer", "CheckNLoad readObject() time"},
		{"CheckNLoadLoadObjectsTimer","CheckNLoad loadObjects() time"},
	};
}
