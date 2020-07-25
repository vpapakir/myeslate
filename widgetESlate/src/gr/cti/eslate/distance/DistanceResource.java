package gr.cti.eslate.distance;

import java.util.*;

/**
 * English language localized strings for the distance control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2007
 * @see         gr.cti.eslate.distance.Distance
 */
public class DistanceResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Distance Control component"},
    {"name", "Distance Control"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    // help file
    {"helpfile", "help/distance.html"},
    // plug names
    {"distance", "Distance"},
    //
    // Other text
    //
    {"go", "Go"},
    {"m", "m"},
    {"km", "Km"},
    {"ft", "ft"},
    {"miles", "miles"},
    {"stop", "Stop at landmarks"},
    {"badUnit", "Unsupported unit"},
    //
    // BeanInfo resources
    //
    {"setDistance", "Distance to travel"},
    {"setDistanceTip", "Enter the distance to travel"},
    {"setStopAtLandmarks", "Stop at landmarks"},
    {"setStopAtLandmarksTip", "Specify if the travel should stop at landmarks"},
    {"setUnit", "Unit of measurement"},
    {"setUnitTip", "Unit in which the distance is measured"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Distance control constructor"},
    {"LoadTimer",             "Distance control load"},
    {"SaveTimer",             "Distance control save"},
    {"InitESlateAspectTimer", "Distance control E-Slate aspect creation"},
  };
}
