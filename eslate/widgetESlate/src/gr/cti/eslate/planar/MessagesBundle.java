package gr.cti.eslate.planar;

import java.util.ListResourceBundle;

/**
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Kriton Kyrimis"
 * @version     2.0.1, 23-Jan-2008
 */
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: Ch. Kynigos",
                                "Development: G. Birbilis, A. Oikonomou, K.  Kyrimis",
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
     {"title",   "2D Variation Tool component, version "},

     {"Planar", "2D Variation Tool"},
     {"Vector","Vector"},
     {"From","From"},
     {"To","To"},
     //
     {"File","File"},
     {"Edit","Edit"}, //26-11-1998
//     {"Tool","Εργαλείο"},
     {"View","View"},
//     {"Settings","Ρυθμίσεις"},
     //////
     {"New",   "New"},
     {"Open",  "Open"},
//     {"Close", "Κλείσιμο"},
//     {"Save",  "Αποθήκευση"},
     {"Print", "Print"},
     //////
     {"Cut",   "Cut"},
     {"Copy",  "Copy"},
     {"Paste", "Paste"},
     {"Clear", "Clear"},
     {"Select All", "Select All"},
     //
     {"Axis","Axis"},
     {"Grid","Grid"},
     {"Control Points","Control Points"},
     {"Mapping","Mapping"},
     {"Cartesian","Cartesian"},
     {"Polar","Polar"},
     {"Dots","Dots"},
     {"Lines","Lines"}
	};
}
