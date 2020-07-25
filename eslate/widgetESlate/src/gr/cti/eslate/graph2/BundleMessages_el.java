package gr.cti.eslate.graph2;

import java.util.ListResourceBundle;

/**
 * @version     1.0.6, 18-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BundleMessages_el extends ListResourceBundle {
	static final Object[][] contents = {
			{ "componentName", "Ψηφίδα Γράφημα" },
			{ "name", "Γράφημα" },
			{ "credits1",
					"Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)" },
			{ "credits2", "Ανάπτυξη: Αυγουστίνος Γρυλλάκης, Κρίτων Κυρίμης" },
			{ "credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα." },
			{ "version", "έκδοση" },
			
			{ "edit", "Επεξεργασία" },
			{ "function", "Συνάρτηση f(x)" },
			{ "domain", "Πεδίο ορισμού" },
			{ "from", "Από" },
			{ "to", "Έως" },
//			{ "step", "Βήμα ανάλυσης" },
			{ "color", "Χρώμα" },
			{ "stroke", "Πάχος γραμμής" },
			{ "plotColor", "Χρώμα διαγράμματος" },
			{ "translation", "Μετάθεση" },
			{ "rotation", "Περιστροφή" },
			{ "rotationAngle", "Γωνία περιστροφής" },
			{ "degrees", "Μοίρες" },
			{ "scale", "Κλίμακα" },
			{ "scaleFactor", "Παράγοντας κλίμακας" },
			{ "remove", "Αφαίρεση" },
			{ "menu", "Επιλογές" },
			{ "buttonUp", "Μετακίνηση πάνω" },
			{ "buttonDown", "Μετακίνηση κάτω" },
			{ "buttonLeft", "Μετακίνηση αριστερά" },
			{ "buttonRight", "Μετακίνηση δεξιά" },
			{ "buttonReset", "Αρχή αξόνων" },
			{ "buttonNormalize", "Κανονικοποίηση" },
			{ "buttonZoomIn", "Εμβάθυνση" },
			{ "buttonZoomOut", "Απομάκρυνση" },
			{ "spinnerDomainStart", "Άξονας Χ από" },
			{ "spinnerDomainEnd", "Άξονας Χ έως" },
			{ "comboBoxX", "Πεδίο άξονα Χ" },
			{ "comboBoxY", "Πεδίο άξονα Υ" },
			{ "showHide", "Εμφάνιση/Απόκρυψη" },
			{ "points", "Σημεία" },
			{ "x", "x:" },
			{ "y", "y:" },
			
			{ "expressionProperty", "Συνάρτηση f(x) =" },
			{ "expressionPropertyTip", "Συνάρτηση f(x)" },
			{ "expressionVisibleProperty", "Εισαγωγή συνάρτησης ορατή" },
			{ "expressionVisiblePropertyTip", "Προβολή ή απόκρυψη της εισαγωγής συνάρτησης" },
			
			{ "expressionPlug", "Συνάρτηση f(x)=" },
			{ "tablePlug", "Πίνακας" },

                        { "dataSetExists", "Το σύνολο δεδομένων {0} υπάρχει ήδη"},
                        { "notLocalTable", "Δεν έχετε ορίσει σύνολα δεδομένων" },
			
			// Bar chart
			{ "componentNameBarChart", "Ψηφίδα Ραβδόγραμμα" },
			{ "nameBarChart", "Ραβδόγραμμα" },
			{ "category", "Κατηγορία" },
			{ "value", "Τιμή" },
			{ "comboBoxCategory", "Πεδίο κατηγορίας" },
			{ "comboBoxValue", "Πεδίο τιμών" },

	};

	protected Object[][] getContents() {
		return contents;
	}
}
