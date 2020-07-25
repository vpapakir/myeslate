/*
 * Created on 6 Ιουν 2006
 *
 */
package gr.cti.eslate.object3D;

import java.util.ListResourceBundle;

public class BundleMessages extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
        
            {"PitchAngle", "Pitch angle"},
            {"YawAngle",   "Yaw angle"},
            {"RollAngle",   "Roll angle"},
            {"XPos", "Position X"},
            {"YPos",   "Position Y"},
            {"ZPos", "Position Z"},
            {"ScaleX", "Scale X"},
            {"ScaleY", "Scale Y"},
            {"ScaleZ", "Scale Z"},
            {"Object3D", "Object3D"},
            {"3DModelFilePath","Model file path"},
        
            {"Title", "Title"},
            // component info
            {"componentName", "Object3D component"},
            {"name", "Object3D"},
            {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
            {"development", "Development : Th. Mantes"},
            //{"funding", "© Computer Technology Institute"},
            {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
            {"version", "version"},
            //Bean Info
            {"ProviderCase", "This plug has provider/dependent plugs connected to it. Do you wish to proceed ?"},
            {"DependentCase", "This plug has dependent plugs connected to it. Do you wish to proceed ?"},
            {"PlugDisconnection", "Plug Disconnection"},
            {"Object3D", "Object3D"},
            
            {"Visible", "Visible"},
            {"setVisibleTip", "Determines if this Object3D is visible inside a Viewer3D component"},
            
            {"Position", "Position"},
            {"Orientation", "Orientation"},
            {"Scale", "Scale"},
            {"PositionTip", "Sets the object's position"},
            {"OrientationTip", "Sets the object's orientation"},
            {"ScaleTip", "Sets the object's scale"},
            

        };

}

