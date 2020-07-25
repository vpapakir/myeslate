package gr.cti.eslate.timeMachine2;

import java.util.ListResourceBundle;

/**
 * @author augril
 */
public class BundleMessages_el extends ListResourceBundle {
	static final Object[][] contents = {
			{ "componentName", "Ψηφίδα Χρονομηχανή" },
			{ "name", "Χρονομηχανή" },
			{ "credits1",
					"Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)" },
			{ "credits2", "Ανάπτυξη: Αυγουστίνος Γρυλλάκης" },
			{ "credits3",
					"Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα." },
			{ "version", "έκδοση" },

			{ "buttonPan", "Κύλιση μεζούρας" }, 
			{"buttonLocate",   "Εύρεση δρομέα"},
			{"comboBoxScale",   "Κλίμακα"},
			{"buttonZoomIn",   "Εμβάθυνση"},
			{"buttonZoomOut",   "Απομάκρυνση"},
			{"b.c.",   "π.Χ."},
			{"a.d.",   "μ.Χ."},
			
			{ "leftVisibleYearProperty", "Αριστερό ορατό έτος" },
			{ "leftVisibleYearPropertyTip", "Αριστερό ορατό έτος" },
			{ "leftSelectedYearProperty", "Έτος αρχής επιλεγμένης περιόδου" },
			{ "leftSelectedYearPropertyTip", "Έτος αρχής επιλεγμένης περιόδου" },
			{ "rightSelectedYearProperty", "Έτος τέλους επιλεγμένης περιόδου" },
			{ "rightSelectedYearPropertyTip", "Έτος τέλους επιλεγμένης περιόδου" },
			{ "scaleProperty", "Κλίμακα" },
			{ "scalePropertyTip", "Κλίμακα" },
			{ "colorPanelProperty", "Χρώμα υποβάθρου" },
			{ "colorPanelPropertyTip", "Χρώμα υποβάθρου" },
			{ "colorRulerProperty", "Χρώμα χάρακα" },
			{ "colorRulerPropertyTip", "Χρώμα χάρακα" },
			{ "colorLabelsProperty", "Χρώμα ετικετών" },
			{ "colorLabelsPropertyTip", "Χρώμα ετικετών" },
			{ "colorCursorProperty", "Χρώμα δρομέα" },
			{ "colorCursorPropertyTip", "Χρώμα δρομέα" },
			
			{ "scaleEvent", "Αλλαγή αρίθμησης κλίμακας" },
			{ "selectedPeriodEvent", "Αλλαγή επιλεγμένης χρονικής περιόδου" },

			{"selectedPeriodPlug",   "Επιλεγμένη περιόδος"},
			{"selectedPeriodStartPlug",   "Αρχή επιλεγμένης περιόδου"},
			{"selectedPeriodEndPlug", "Τέλος επιλεγμένης περιόδου"},
	};

	protected Object[][] getContents() {
		return contents;
	}
}
