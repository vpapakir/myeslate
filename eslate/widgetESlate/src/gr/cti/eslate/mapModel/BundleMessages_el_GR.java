package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

/**
 * Messages Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 24-Jan-2000
 */
public class BundleMessages_el_GR extends ListResourceBundle {
	public Object [][] getContents() {
		return contents;
	}

	static final Object[][] contents={
		{"mymap",           "Ο χάρτης μου"},
		{"myregion",        "Η περιοχή μου"},
		{"mylayer",         "Νέο επίπεδο"},
		{"mapnotsaved",     "Ο χάρτης δεν έχει αποθηκευτεί.\nΘέλετε να τον αποθηκεύσετε πριν κλείσει;"},
		{"cantsave",        "Δεν είναι δυνατή η αποθήκευση του χάρτη!\nΒεβαιωθείτε ότι ο δίσκος ή το αρχείο δεν είναι προστατευμένα για εγγραφή και ότι ο δίσκος έχει αρκετό διαθέσιμο χώρο."},
		{"cantopen",        "Δεν είναι δυνατή η ανάγνωση του χάρτη!\nΊσως το αρχείο να ανήκει σε παλιά έκδοση, να μην είναι σωστό\nή κάποια άλλη εφαρμογή να το χρησιμοποιεί."},
		{"AND",             "ΚΑΙ"},
		{"OR",              "Ή"},
		{"loadingmap",      "Ανάγνωση χάρτη..."},
		{"savingmap",       "Αποθήκευση χάρτη..."},
		{"confirm",         "Επιβεβαίωση κλεισίματος"},
		{"backgroundio",    "Η προσθήκη ή η αφαίρεση εικόνας υποβάθρου απαιτεί την αποθήκευση της περιοχής στο μικρόκοσμό της ή στο αρχείο της.\nΕπιθυμείτε να συνεχίσετε;"},
	};
}
