package gr.cti.eslate.timeMachine2;

import java.util.ListResourceBundle;

/**
 * @author augril
 */
public class BundleMessages_en extends ListResourceBundle {
	static final Object[][] contents = {
			{ "componentName", "Time machine component" },
			{ "name", "Time machine" },
			{ "credits1",
					"Part of the E-Slate environment (http://E-Slate.cti.gr)" },
			{ "credits2", "Development: Augustine Grillakis" },
			{ "credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα." },
			{ "version", "version" },
			
			{"buttonPan",   "Scrolling"},
			{"buttonLocate",   "Locate the cursor"},
			{"comboBoxScale",   "Scale level"},
			{"buttonZoomIn",   "Zoom In"},
			{"buttonZoomOut",   "Zoom Out"},
			{"b.c.",   "b.c."},
			{"a.d.",   "a.d."},
			
			{ "leftVisibleYearProperty", "Left visible year" },
			{ "leftVisibleYearPropertyTip", "Left visible year" },
			{ "leftSelectedYearProperty", "Left selected year" },
			{ "leftSelectedYearPropertyTip", "Left selected year" },
			{ "rightSelectedYearProperty", "Right selected year" },
			{ "rightSelectedYearPropertyTip", "Right selected year" },
			{ "scaleProperty", "Scale" },
			{ "scalePropertyTip", "Scale" },
			{ "colorPanelProperty", "Panel color" },
			{ "colorPanelPropertyTip", "Panel color" },
			{ "colorRulerProperty", "Ruler color" },
			{ "colorRulerPropertyTip", "Ruler color" },
			{ "colorLabelsProperty", "Labels color" },
			{ "colorLabelsPropertyTip", "Labels color" },
			{ "colorCursorProperty", "Cursor color" },
			{ "colorCursorPropertyTip", "Cursor color" },
			
			{ "scaleEvent", "Scale change" },
			{ "selectedPeriodEvent", "Selected period change" },
			
			{"selectedPeriodPlug",   "Selected period"},
			{"selectedPeriodStartPlug",   "Selected period start"},
			{"selectedPeriodEndPlug", "Selected period end"},
	};

	protected Object[][] getContents() {
		return contents;
	}
}
