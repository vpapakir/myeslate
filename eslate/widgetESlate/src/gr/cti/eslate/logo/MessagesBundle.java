package gr.cti.eslate.logo;

import java.util.ListResourceBundle;

/**
 * Logo component.
 *
 * @version     2.0.6, 23-Jan-2008
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K. Kyrimis
 */
public class MessagesBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: M. Koutlis, Ch Kynigos",
                                "Development: G. Birbilis, G. Tsironis, N. Drossos, K. Kyrimis",
                                "Based on TurtleTracks",
                                "(http://www.ugcs.caltech.edu/~dazuma/turtle/index.html)",
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
     {"title",    "Logo component, version "},
     {"logopin", "Primitives"},
     {"commandpin", "Commands/Slider"},
     {"logocallpin", "Calls Notification"},
     //{"Ενημέρωση για εντολές", "Calls Notification"},
     //
     {"ConsoleInputing", "idle: ready for commands/data input..."},
     {"ConsoleExecuting", "busy: executing program..."},
     {"machinePin", "LOGO machine"},
     {"ConstructorTimer",      "Logo constructor"},
     {"LoadTimer",             "Logo load"},
     {"SaveTimer",             "Logo save"},
     {"InitESlateAspectTimer", "Logo E-Slate aspect creation"},

    };
}
