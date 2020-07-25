/*
 * Created on 6 Ιουν 2006
 *
 */
package gr.cti.eslate.scene3d;

import java.util.ListResourceBundle;

public class BundleMessages extends ListResourceBundle {

    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
        {"HorizontalAngle", "Horizontal angle"},
        {"VerticalAngle",   "Vertical angle"},
        {"XPos", "Position X"},
        {"YPos", "Position Y"},
        {"ZPos", "Position Z"},
        
        {"XDim", "Dimension X"},
        {"YDim", "Dimension Y"},
        {"ZDim", "Dimension Z"},
        { "XScale", "Scale Χ" },
        { "YScale", "Scale Υ" },
        { "ZScale", "Scale Ζ" },
        {"Viewer3D", "Viewer3D"},
        {"PlugsUsed"," PlugsUsed"},
        { "PitchAngle", "Pitch angle" },
        { "YawAngle", "Yaw angle" },
        { "RollAngle", "Roll angle" },
        
        {"Title", "Title"},
        {"NumberValue", "Numeric value"},
        {"Pressed", "Boolean"},
        // component info
        {"componentName", "Text field component"},
        {"name", "Viewer3D"},
        {"part", "Part of the E-Slate environment (http://E-Slate.cti.gr/)"},
        {"development", "Development : Th. Mantes"},
        //{"funding", "© Computer Technology Institute"},
        {"copyright", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
        {"version", "version"},
        //Bean Info
        {"ProviderCase", "This plug has provider/dependent plugs connected to it. Do you wish to proceed ?"},
        {"DependentCase", "This plug has dependent plugs connected to it. Do you wish to proceed ?"},
        {"PlugDisconnection", "Plug Disconnection"},
        {"CartesianHelperVisible", "Cartesian Helper visible"},
        {"CartesianHelperVisibleTip", "Shows/hides the reference cartesian helper renderings"},
        {"CameraPosition", "Camera position"},
        {"CameraOrientation", "Camera orientation"},
        {"CameraPositionTip", "Sets the camera's position"},
        {"CameraOrientationTip", "Sets the camera's orientation"},
        {"BoundingBoxDimensions", "BoundingBoxDimensions"},
        {"BoundingBoxDimensionsTip", "Sets the bounding box dimensions"},
        {"PositionChanged", "Camera position changed"},
        {"OrientationChanged", "Camera orientation changed"},
        
        };

}

