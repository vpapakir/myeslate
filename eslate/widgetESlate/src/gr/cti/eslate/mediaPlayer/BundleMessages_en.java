package gr.cti.eslate.mediaPlayer;

import java.util.ListResourceBundle;

/**
 * @author augril
 */
public class BundleMessages_en extends ListResourceBundle {
	static final Object[][] contents = {
			{ "componentName", "Media player component" },
			{ "name", "Media player" },
			{ "credits1",
					"Part of the E-Slate environment (http://E-Slate.cti.gr)" },
			{ "credits2", "Development: Augustine Grillakis" },
			{ "credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα." },
			{ "version", "version" },

			{ "file", "File" }, { "open", "Open..." }, { "replay", "Replay" },
			{ "close", "Close" },

			{ "Video files", "Video files" }, { "Audio files", "Audio files" },
			{ "Image files", "Image files" }, { "Flash files", "Flash files" },

			{ "fileProperty", "File to play" },
			{ "filePropertyTip", "File to play" },
			{ "repeatProperty", "Toggle repeat" },
			{ "repeatPropertyTip", "Toggle repeat" },

			{ "playerStartedEvent", "Start play" },
			{ "playerPausedEvent", "Pause play" },
			{ "playerStoppedEvent", "Stop play" },
			{ "playerFinishedEvent", "Finish play" },

			{ "filePlug", "File" }, };

	protected Object[][] getContents() {
		return contents;
	}
}
