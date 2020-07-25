package gr.cti.eslate.mediaPlayer;

import java.util.ListResourceBundle;

/**
 * @author augril
 */
public class BundleMessages_el extends ListResourceBundle {
	static final Object[][] contents = {
			{ "componentName", "Ψηφίδα Αναπαραγωγή πολυμέσων" },
			{ "name", "Αναπαραγωγή πολυμέσων" },
			{ "credits1",
					"Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)" },
			{ "credits2", "Ανάπτυξη: Αυγουστίνος Γρυλλάκης" },
			{ "credits3",
					"Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα." },
			{ "version", "έκδοση" },

			{ "file", "Αρχείο" }, { "open", "Άνοιγμα..." }, { "replay", "Επανάληψη" },
			{ "close", "Κλείσιμο" },

			{ "Video files", "Αρχεία video" },
			{ "Audio files", "Αρχεία ήχου" },
			{ "Image files", "Αρχεία εικόνας" },
			{ "Flash files", "Αρχεία flash" },

			{ "fileProperty", "Αρχείο για αναπαραγωγή" },
			{ "filePropertyTip", "Αρχείο για αναπαραγωγή" },
			{ "repeatProperty", "Επανάληψη αναπαραγωγής" },
			{ "repeatPropertyTip", "Επανάληψη αναπαραγωγής" },

			{ "playerStartedEvent", "Εκκίνηση αναπαραγωγής" },
			{ "playerPausedEvent", "Παύση αναπαραγωγής" },
			{ "playerStoppedEvent", "Σταμάτημα αναπαραγωγής" },
			{ "playerFinishedEvent", "Ολοκλήρωση αναπαραγωγής" },

			{ "filePlug", "Αρχείο" }, };

	protected Object[][] getContents() {
		return contents;
	}
}
