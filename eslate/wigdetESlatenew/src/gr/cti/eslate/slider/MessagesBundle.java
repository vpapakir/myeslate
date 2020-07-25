package gr.cti.eslate.slider;

import java.util.ListResourceBundle;

/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: Ch. Kynigos",
                                "Development: G. Birbilis, A. Oikonomou, N.  Drossos, K. Kyrimis",
                                "Contribution: M. Koutlis",
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
     {"Slider", "Variation Tool"}, //28-8-1998
     {"title",   "Variation Tool component, version "},
     {"turtlePage", "LOGO calls notification"},
     //{"Ενημέρωση για LOGO εντολές", "LOGO calls notification"},
     {"logopin", "LOGO commands"},
     //{"Εντολές LOGO", "LOGO commands"},
     {"Vector","Vector"},
     //{"Διάνυσμα","Vector"}, //29-9-1998
     //
     {"Procedure","Procedure"},
     {"Track","Track"},
     {"Grid","Grid"},
     {"From","From"},
     {"To","To"},
     //{"Από","From"},
     //{"To","Μέχρι"},
     //{"Μέχρι","To"},
     {"Step","Step"},
     {"Var","Var"},
     {"2D","2D"} //6Jul1999
     //
//     {"Yes","Yes"},
//     {"No","No"}
	};
}
