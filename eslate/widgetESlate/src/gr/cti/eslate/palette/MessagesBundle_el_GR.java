package gr.cti.eslate.palette;

import java.util.ListResourceBundle;

/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class MessagesBundle_el_GR extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }
    
    static final String[] info={"Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)",
                                "Ανάπτυξη: Γ. Μπιρμπίλης, Κ. Κυρίμης",
                                "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.",
                                "Ανάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.",
                                "Το Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.",
                                " ",
                                "Επιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων",
                                "βασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται",
                                "και να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."
                                };

    static final Object[][] contents={
     {"info", info},
     //
     {"name", "Χρώματα"}, //29Jun2000: fixed the "key" - it must have had some strange character encoding
     {"title", "Ψηφίδα Παλέττα, έκδοση "},
     {"pin",   "Χρώματα"},
     //
     {"color",     "Χρώμα: "},
     {"fgrColor",  "σχεδίαση"},
     {"fillColor", "γέμιση"},
     {"bkgrColor", "σβήσιμο"}
 	};
}
