package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

public class BundleMenu_el_GR extends ListResourceBundle {
	public Object [][] getContents() {
		return contents;
	}

	static final Object[][] contents={
		//File
		{"file",        "Αρχείο"},
		{"new",         "Νέος..."},
		{"open",        "Άνοιγμα..."},
		{"edit",        "Επεξεργασία..."},
		{"save",        "Αποθήκευση"},
		{"saveas",      "Αποθήκευση ως..."},
		{"savetitle",   "Αποθήκευση χάρτη"},
		{"opentitle",   "Ανάγνωση χάρτη"},
		{"close",       "Κλείσιμο"},
		{"ConstructorTimer",          "Κατασκευή Χάρτη"},
		{"InitESlateAspectTimer",     "Κατασκευή αβακιακής πλευράς Χάρτη"},
		{"LoadTimer",                 "Ανάκτηση επεξεργαστή χαρτών"},
		{"SaveTimer",                 "Αποθήκευση επεξεργαστή χαρτών"},
		{"PerformOpenTimer",          "Χρόνος performOpen()"},
		{"OpenSubTreeTimer",          "Χρόνος openSubTree()"},
		{"SetMapRootTimer",           "Χρόνος setMapRoot()"},
		{"ReadUnusedLayersTimer",     "Χρόνος ανοίγματος \"unused\" επιπέδων"},
		{"RefreshTimer",              "Χρόνος refresh()"},
		{"OpenRegionTimer",           "Χρόνος openRegion() (χωρίς openLayer())"},
		{"RegionReadFieldMapTimer",   "Χρόνος ανάγνωσης region FieldMap"},
		{"RegionRestoreAttribsTimer", "Χρόνος restoreRegionAttributes()"},
		{"RegionChangeDirTimer",      "Χρόνος περιήγησης struct file(Region)"},
		{"RegionBgrReadTimer",        "Χρόνος ανάγνωσης backgrounds"},
		{"OpenLayerTimer",            "Χρόνος openLayer()"},
		{"LayerChangeDirTimer",       "Χρονος περιήγησης struct file(Layer)"},
		{"LayerReadFieldMapTimer",    "Χρόνος ανάγνωσης layer FieldMap"},
		{"LayerRestoreAttribsTimer",  "Χρόνος restoreLayerAttributes()"},
		{"MapReadStreamTimer",        "Χρόνος Map readStream()"},
		{"MapReadFieldMapTimer",      "Χρόνος ανάγνωσης Map FieldMap"},
		{"CheckNLoadTimer",           "Χρόνος checkNLoad()"},
		{"CheckNLoadChangeDirTimer",  "Χρόνος περιήγησης struct file(checkNLoad())"},
		{"CheckNLoadReadObjectTimer", "Χρόνος CheckNLoad readObject()"},
		{"CheckNLoadLoadObjectsTimer","Χρόνος CheckNLoad loadObjects()"},
	};
}

