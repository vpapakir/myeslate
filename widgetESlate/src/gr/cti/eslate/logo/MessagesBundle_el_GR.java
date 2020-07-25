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
public class MessagesBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)",
                                "Σχεδιαστική ιδέα: Μ. Κουτλής, Χ. Κυνηγός",
                                "Ανάπτυξη: Γ. Μπιρμπίλης, Γ. Τσιρώνης, Ν. Δρόσος, Κ. Κυρίμης",
                                "Βασίζεται στην TurtleTracks",
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
     {"title",    "Ψηφίδα Logo, έκδοση "},
     {"logopin",  "Εγκατάσταση εντολών"},
     {"commandpin", "Εντολές/Μεταβολέας"},
     {"logocallpin", "Ενημέρωση για εντολές"},
     //{"Ενημέρωση για εντολές", "Ενημέρωση για εντολές"},
     //Dialog Titles//
     {"Open text file","Ανοιγμα αρχείου κειμένου"},
     {"Save text file","Αποθήκευση αρχείου κειμένου"},
     {"Load machine state","Φόρτωμα κατάστασης μηχανής"},
     {"Save machine state","Αποθήκευση κατάστασης μηχανής"},
     //MenuBar//
     {"File","Αρχείο"},
     {"Edit","Επεξεργασία"},
     {"LOGO Machine","Μηχανή LOGO"},
     //File menu//
     {"New",   "Νέo"},
     {"Open Text",  "Φόρτωμα κειμένου"},
     {"Save Text",  "Αποθήκευση κειμένου"},
     {"Print", "Εκτύπωση"},
     //Edit menu//
     {"Cut",        "Αποκοπή"},
     {"Copy",       "Αντιγραφή"},
     {"Paste",      "Επικόλληση"},
     {"Clear",      "Καθαρισμός"},
     {"Select All", "Επιλογή όλου"},
     {"Read","Ανάγνωση"},
     //Search menu//
     {"Search",     "Αναζήτηση"},
     {"Find",       "Εύρεση"},
     {"Find Next",  "Εύρεση επομένου"},
     {"Replace",    "Αντικατάσταση"},
     {"Go To Line", "Εύρεση γραμμής"},
     //Machine menu//
     {"Load State", "Φόρτωμα κατάστασης"},
     {"Save State", "Αποθήκευση κατάστασης"},
     {"Reset Environment", "Επαναρχικοποίηση περιβάλλοντος"},
     {"Try to unfreeze","Προσπάθεια ξεπαγώματος"},
     {"Run","Εκτέλεση"},
     {"Pause","Παύση"},
     {"Stop","Σταμάτημα"},
     //
     {"ConsoleInputing", "ανενεργή: έτοιμη για είσοδο εντολών/δεδομένων..."},
     {"ConsoleExecuting", "απασχολημένη: εκτελεί εντολές..."},
     {"machinePin", "Μηχανή LOGO"},
     //Editor//
     {"Program", "Πρόγραμμα"},
     {"Output", "Έξοδος"},
     {"ConstructorTimer",      "Κατασκευή Logo"},
     {"LoadTimer",             "Ανάκτηση Logo"},
     {"SaveTimer",             "Αποθήκευση Logo"},
     {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς Logo"},
     //
     {"Font", "Γραμματοσειρά"},
     {"Font size", "Μέγεθος γραμματοσειράς"},
     {"Bold", "Έντονα"},
     {"Italic", "Πλάγια"},
     {"B", "Ε"},
     {"I", "Π"},

        };
}
