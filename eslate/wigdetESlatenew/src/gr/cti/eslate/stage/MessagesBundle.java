package gr.cti.eslate.stage;

import java.util.ListResourceBundle;

/**
 * @version     2.0.12, 23-Jan-2008
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MessagesBundle extends ListResourceBundle
{
    public Object [][] getContents() {
        return contents;
    }

    static final String[] info={"Part of the E-Slate environment (http://E-Slate.cti.gr)",
                                "Design idea: M. Koutlis, G. Birbilis",
                                "Development: G. Birbilis, Nikos Drossos, Kriton Kyrimis",
                                "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.",
                                "Ανάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.",
                                "Το Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.",
                                " ",
                                "Επιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων",
                                "βασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται",
                                "και να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."
                                };

    static final Object[][] contents={
     {"needsJava2","This component needs a Java Virtual Machine compatible with Java2"},
     {"info", info},
     //
     {"title",   "Stage component, version "},
     {"BindPointPoint", "Point-Point binding"},
     {"BindPointShape", "Point-Shape binding"},

     {"Need2Points", "Need at least two selected points to bind"},
     {"Need1ShapeNPoints", "At one selected shape and at least one point to bind"},
     {"badConstraint", "Couldn't create & force this constraint"},

     {"badGridSize", "Grid size must be positive"},


     {"Stage", "Stage"}, //ή Πάλκο;
     {"Vector","Vector"},
// Localization of Menus //////////////////////////////////////////////////////

     {"File","File"},
     {"New",   "New"},
     {"Load...",  "Load..."},
//     {"Close", "Κλείσιμο"},
     {"Save...",  "Save..."},
     {"Print...", "Print..."},
     {"Photo...","Photo..."}, //9May2000

     ////

     {"Edit","Edit"}, //26-11-1998
     {"New object", "New object"}, //29Nov1999
     {"Cut",   "Cut"},
     {"Copy",  "Copy"},
     {"Paste", "Paste"},
     {"Clear", "Clear"},
     {"Select All", "Select all"},
     {"Area Selection","Area Selection"}, //26Apr2000
     {"Select","Select"}, //19May2000
     {"Deselect","Deselect"}, //19May2000
     {"Clone","Clone"}, //13Apr2000

     ////

     {"View","View"},
     {"Axis","Axis"},
     {"Grid","Grid"},
     {"Coordinates","Coordinates"},
     {"Control Points","Control Points"},
     {"Mapping","Mapping"},
     {"Cartesian","Cartesian"},
     {"Polar","Polar"},
     {"Dots","Dots"},
     {"Lines","Lines"},
     {"Zoom In","Zoom In"},
     {"Zoom Out", "Zoom Out"},

     ////

     {"Insert","Insert"},
     {"Scene", "Scene"}, //the document object's name (used in scripting)
     {"ControlPoint", "ControlPoint"}, //used in scripting
     {"RoundBox","RoundBox"}, //used in scripting
     {"SquareBox","SquareBox"}, //used in scripting
     {"pLine", "PolyLine"}, //used in scripting
     {"quadCurve", "QuadraticCurve"}, //used in scripting
     {"cubicCurve", "CubicCurve"}, //used in scripting

     {"Box","Box"}, //used in scripting too
     {"Ball","Ball"}, //used in scripting too
     {"Spring","Spring"}, //used in scripting too
     {"Slope","Slope"}, //used in scripting too

     {"Rope","Rope"}, //to be used in scripting too...
     {"Round Box","Round Box"},
     {"Square Box","Square Box"},
     {"PolyLine","PolyLine"},
     {"Quadratic Curve","Quadratic Curve"},
     {"Cubic Curve","Cubic Curve"},

     //

//     {"Tool","Εργαλείο"},
//     {"Settings","Ρυθμίσεις"},

////////////////////////////////////////////////////////////////////////

     {"Error", "Error"},
     {"Split", "Split"},

// Localization of Customizers //////////////////////////////////////////////////////
     {"Customization","Customization"},
     {"OK","OK"},
     {"Cancel","Cancel"},

     {"Object","Object"},
     {"Scene Object","Scene Object"},
     {"Name","Name"},
     {"Location","Location"},
     {"Color","Color"},
     {"Image","Image"},

     {"Change","Change"},

     {"Physics Object","Physics Object"},
     {"Mass","Mass"},
     {"Velocity","Velocity"},
     {"Acceleration","Acceleration"},
     {"Applied Force","Applied Force"},
     {"Kinetic Energy","Kinetic Energy"},
     {"Altitude","Altitude"},

     {"Spring Constant", "Spring Constant"},
     {"Length","Length"},
     {"Natural Length", "Natural Length"},
     {"Displacement", "Displacement"},

     {"Angle","Angle"},

     {"Radius","Radius"}, //17May2000
     {"Diameter","Diameter"}, //17May2000

     {"Width","Width"},
     {"Height","Height"},

     //

     {"Bring To Front", "Bring To Front"},
     {"Send To Back", "Send To Back"},
     {"Properties...","Properties..."},

     {"Error!", "Error!"}, //10May2000
     {"Failed to print", "Failed to print"}, //10May2000

     {"loadSceneMsg", "Load scene"}, //16May2000 //7Jun2000: τώρα λέει "σκηνικού" αντί "σκηνής"
     {"saveSceneMsg", "Save scene"}, //16May2000 //7Jun2000: τώρα λέει "σκηνικού" αντί "σκηνής"

     //

     {"LoadTimer",             "Stage load"},
     {"SaveTimer",             "Stage save"},

        };
}
