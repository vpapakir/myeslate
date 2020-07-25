package gr.cti.eslate.tableInspector;

import java.util.ListResourceBundle;

/**
 * Table Browser BeanInfo bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class BundleBeanInfo_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"percentField",                "Πλάτος ονόματος πεδίων"},
        {"percentFieldtip",             "Σε ποσοστό επί του πλάτους της ψηφίδας"},
        {"toolNavigateVisible",         "Εργαλεία πλοήγησης ορατά"},
        {"toolNavigateVisibletip",      "Ελέγχει την εμφάνιση των εργαλείων πλοήγησης (βέλη)"},
        {"toolSelectViewVisible",       "Εργαλείο επιλογής προβολής ορατό"},
        {"toolSelectViewVisibletip",    "Ελέγχει την εμφάνιση του εργαλείου επιλογής προβολής"},
        {"toolShowRecordVisible",       "Εργαλείο προβολής εγγραφής ορατό"},
        {"toolShowRecordVisibletip",    "Ελέγχει την εμφάνιση του εργαλείου απόκρυψης/προβολής εγγραφής"},
        {"toolSelectViewDefaultShowAll","Αρχική τιμή εργαλείου προβολής \"όλες\""},
        {"toolSelectViewDefaultShowAlltip","Η τιμή αυτή εφαρμόζεται κατά τη σύνδεση νέου πίνακα"},
        {"toolQueryVisible",            "Εργαλεία ερώτησης ορατά"},
        {"toolQueryVisibletip",         "Ελέγχει την εμφάνιση των εργαλείων ερώτησης"},
        {"toolbarVisible",              "Μπάρα εργαλείων ορατή"},
        {"toolbarVisibletip",           "Ελέγχει την εμφάνιση της μπάρας εργαλείων"},
        {"messagebarVisible",           "Μπάρα μηνυμάτων ορατή"},
        {"messagebarVisibletip",        "Ελέγχει την εμφάνιση της μπάρας μηνυμάτων"},
        {"backgroundImage",             "Εικόνα υποβάθρου ψηφίδας"},
        {"backgroundImagetip",          "Θέτει την εικόνα του υποβάθρου"},
        {"opaque",                      "Αδιαφανής"},
        {"opaquetip",                   "Η ψηφίδα μπορεί να είναι διαφανής ή αδιαφανής"},
        {"imageAlignment",              "Θέση εικόνας υποβάθρου"},
        {"imageAlignmenttip",           "Η θέση της εικόνας υποβάθρου, αν υπάρχει"},
        {"tabPlacement",                "Θέση ετικετών"},
        {"tabPlacementtip",             "Που θα φαίνονται τα ονόματα των πινάκων"},
        {"topleft",                     "ΠΑΝΩ-ΑΡΙΣΤΕΡΑ"},
        {"centered",                    "ΚΕΝΤΡΟ"},
        {"top",                         "ΕΠΑΝΩ"},
        {"bottom",                      "ΚΑΤΩ"},
        {"left",                        "ΑΡΙΣΤΕΡΑ"},
        {"right",                       "ΔΕΞΙΑ"},
        {"widgetsOpaque",               "Επιφάνειες ψηφίδας αδιαφανείς"},
        {"widgetsOpaquetip",            "Φαίνονται σε συνδιασμό με την διαφάνεια της ψηφίδας"},
        {"background",                  "Χρώμα υποβάθρου ψηφίδας"},
        {"backgroundtip",               "Φαίνεται μόνο όταν η ψηφίδα είναι αδιαφανής"},
        {"border",                      "Περίγραμμα"},
        {"bordertip",                   "Ελέγχει το περίγραμμα της ψηφίδας"},
        {"font",                        "Γραμματοσειρά"},
        {"fonttip",                     "Η γραμματοσειρά εφαρμόζεται στις ετικέτες, σύντομες βοήθειες κτλ"},
        {"tabVisible",                  "Τίτλος πίνακα ορατός"},
        {"tabVisibletip",               "Όταν δεν φαίνεται δεν θα είναι δυνατόν να γίνει εναλλαγή πινάκων!"},
        {"queryType",                   "Μορφή ερωτήσεων"},
        {"queryTypetip",                "Πως θα γίνονται οι ερωτήσεις. Για να απενεργοποιηθούν κρύψτε το εργαλείο ερώτησης."},
        {"queryticks",                  "ΕΠΙΛΟΓΗ"},
        {"querycombo",                  "ΛΙΣΤΑ"},
        {"rowBackgroundColor",          "Χρώμα υποβάθρου πεδίων"},
        {"rowSelectedBackgroundColor",  "Χρώμα υποβάθρου πεδίων επιλεγμένων εγγραφών"},
        {"rowForegroundColor",          "Χρώμα προσκηνίου πεδίων"},
        {"rowSelectedForegroundColor",  "Χρώμα προσκηνίου πεδίων επιλεγμένων εγγραφών"},
        {"rowBorderPainted",            "Περίγραμμα στα πεδία"},
        {"rowBorderPaintedtip",         "Εμφανίζει ή αποκρύπτει το περίγραμμα που περιβάλλει τις τιμές των πεδίων"},
        {"followActiveRecord",          "Προβολή πάντα της ενεργής εγγραφής"},
        {"followActiveRecordtip",       "Ελέγχει αν η εγγραφή που προβάλεται θα είναι απαραίτητα η ενεργή εγγραφή του πίνακα στη βάση δεδομένων"},
        {"activeTabChanged",            "Αλλαγή ενεργού tab"},
        {"activeRecordBrowserRecordChanged","Αλλαγή προβαλλόμενης εγγραφής"},
    };
}
