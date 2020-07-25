package gr.cti.eslate.stage;

import java.util.ListResourceBundle;

/**
 * @version     2.0.12, 23-Jan-2008
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MessagesBundle_el_GR extends ListResourceBundle
{
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     {"needsJava2","Αυτή η ψηφίδα χρειάζεται εικονική μηχανή Java συμβατή με τη Java2"},
     {"info", new String[]
               {"Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)",
                "Σχεδιαστική ιδέα: Μ. Κουτλής, Γ. Μπιρμπίλης",
                "Ανάπτυξη: Γ. Μπιρμπίλης, Νίκος Δρόσος, Κρίτων Κυρίμης",
                "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.",
                "Ανάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.",
                "Το Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.",
                " ",
                "Επιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων",
                "βασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται",
                "και να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."
                }
     },
     //
     {"Stage", "Σκηνή"}, //ή Πάλκο;
     {"title",  "Ψηφίδα Σκηνή, έκδοση "},
     {"Vector","Διάνυσμα"},

// Localization of Menus //////////////////////////////////////////////////////

     {"File","Αρχείο"},
     {"New",   "Νέο"},
     {"Load...",  "Φόρτωμα..."},
//     {"Close", "Κλείσιμο"},
     {"Save...",  "Αποθήκευση..."},
     {"Print...", "Εκτύπωση..."},
     {"Photo...","Φωτογράφηση..."}, //9May2000

     ////

     {"Edit","Επεξεργασία"}, //26-11-1998
     {"New object", "Νέο αντικείμενο"}, //29Nov1999
     {"Cut",   "Αποκοπή"},
     {"Copy",  "Αντιγραφή"},
     {"Paste", "Επικόλληση"},
     {"Clear", "Καθαρισμός"},
     {"Select All", "Επιλογή όλων"},
     {"Area Selection","Επιλογή Περιοχής"}, //26Apr2000
     {"Select","Επιλογή"}, //19May2000
     {"Deselect","Αποεπιλογή"}, //19May2000
     {"Clone","Κλωνοποίηση"}, //13Apr2000

     ////

     {"View","Προβολή"},
     {"Axis","Αξονες"},
     {"Grid","Πλέγμα"},
     {"Coordinates","Συντεταγμένες"},
     {"Control Points","Σημεία ελέγχου"},
     {"Mapping","Απεικόνιση"},
     {"Cartesian","Καρτεσιανή"},
     {"Polar","Πολική"},
     {"Dots","Τελείες"},
     {"Lines","Γραμμές"},
     {"Zoom In","Μεγέθυνση"},
     {"Zoom Out", "Σμίκρυνση"},

     ////

     {"Insert","Εισαγωγή"},
     {"Scene", "Σκηνικό"}, //the document object's name (used in scripting)
     {"ControlPoint", "ΣημείοΕλέγχου"}, //used in scripting
     {"RoundBox","ΣτρογγυλοΚουτί"}, //used in scripting
     {"SquareBox","ΤετράγωνοΚουτί"}, //used in scripting
     {"pLine", "ΠολυγωνικήΓραμμή"}, //used in scripting
     {"quadCurve", "ΔευτεροβάθμιαΚαμπύλη"}, //used in scripting
     {"cubicCurve", "ΤριτοβάθμιαΚαμπύλη"}, //used in scripting

     {"Box","Κουτί"}, //used in scripting too
     {"Ball","Μπάλα"}, //used in scripting too
     {"Spring","Ελατήριο"}, //used in scripting too
     {"Slope","Κεκλιμένο"}, //used in scripting too

     {"Rope","Σχοινί"}, //to be used in scripting too...
     {"Round Box","Στρογγυλεμένο κουτί"},
     {"Square Box","Τετράγωνο κουτί"},
     {"PolyLine","Πολυγωνική γραμμή"},
     //{"Quadratic Curve","Καμπύλη με 1 σημείο ελέγχου"},
     //{"Cubic Curve","Καμπύλη με 2 σημεία ελέγχου"},
     {"Quadratic Curve","Δευτεροβάθμια καμπύλη"},
     {"Cubic Curve","Τριτοβάθμια καμπύλη"},

     //

//     {"Tool","Εργαλείο"},
//     {"Settings","Ρυθμίσεις"},

////////////////////////////////////////////////////////////////////////

     {"Error", "Σφάλμα"},
     {"BindPointPoint", "Σύνδεση Σημείο-Σημείο"},
     {"BindPointShape", "Σύνδεση Σημείο-Σχήμα"},
     {"Split", "Αποσύνδεσης"},
     {"Need2Points", "Χρειάζονται τουλάχιστον δύο επιλεγμένα σημεία για σύνδεση"},
     {"Need1ShapeNPoints", "Χρειάζεται ένα επιλεγμένο σχήμα και τουλάχιστον ένα σημείο για σύνδεση"},
     {"badConstraint", "Δεν μπόρεσε να δημιουργηθεί & ενεργοποιηθεί ο περιορισμός αυτός"},

     {"badGridSize", "Το μέγεθος τού πλέγματος πρέπει να είναι θετικό"},

// Localization of Customizers //////////////////////////////////////////////////////
     {"Customization","Ρυθμίσεις"},
     {"OK","Εντάξει"},
     {"Cancel","Ακύρωση"},

     {"Object","Αντικείμενο"},
     {"Scene Object","Σκηνικό αντικείμενο"},
     {"Name","Ονομα"},
     {"Location","Θέση"},
     {"Color","Χρώμα"},
     {"Image","Εικόνα"},

     {"Change","Αλλαγή"},

     {"Physics Object","Φυσικό αντικείμενο"},
     {"Mass","Μάζα"},
     {"Velocity","Ταχύτητα"},
     {"Acceleration","Επιτάχυνση"},
     {"Applied Force","Ασκούμενη Δύναμη"},
     {"Kinetic Energy","Κινητική Ενέργεια"},
     {"Altitude","Υψόμετρο"},

     {"Spring Constant", "Σταθερά ελατηρίου"},
     {"Length","Μήκος"},
     {"Natural Length", "Φυσικό μήκος"},
     {"Displacement", "Απομάκρυνση"},

     {"Angle","Γωνία"},

     {"Radius","Ακτίνα"}, //17May2000
     {"Diameter","Διάμετρος"}, //17May2000

     {"Width","Πλάτος"},
     {"Height","Υψος"},

     //

     {"Bring To Front", "Φέρε στο προσκήνιο"},
     {"Send To Back", "Στείλε στο υπόβαθρο"},
     {"Properties...","Ιδιότητες..."},

     {"Error!", "Σφάλμα!"}, //10May2000
     {"Failed to print", "Η εκτύπωση απέτυχε"}, //10May2000

     {"loadSceneMsg", "Φόρτωμα σκηνικού"}, //16May2000 //7Jun2000: τώρα λέει "σκηνικού" αντί "σκηνής"
     {"saveSceneMsg", "Αποθήκευση σκηνικού"}, //16May2000 //7Jun2000: τώρα λέει "σκηνικού" αντί "σκηνής"

     //

     {"LoadTimer",             "Ανάκτηση σκηνής"},
     {"SaveTimer",             "Αποθήκευση σκηνής"},

        };
}
