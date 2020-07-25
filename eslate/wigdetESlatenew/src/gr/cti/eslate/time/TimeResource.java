package gr.cti.eslate.time;

import java.util.*;

/**
 * English language localized strings for the travel-for-a-given-time
 * control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.time.Time
 */
public class TimeResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Travel-for-a-Given-Time Control component"},
    {"name", "Travel-for-a-Given-Time Control"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    // help file
    {"helpfile", "help/time.html"},
    // plug names
    {"time", "Time"},
    //
    // Other text
    //
    {"go", "Go"},
    {"hours", "hours"},
    {"minutes", "minutes"},
    {"seconds", "seconds"},
    {"stop", "Stop at landmarks"},
    {"badUnit", "Unsupported unit"},
    //
    // BeanInfo resources
    //
    {"setTime", "Time to travel"},
    {"setTimeTip", "Enter the time for which to travel"},
    {"setStopAtLandmarks", "Stop at landmarks"},
    {"setStopAtLandmarksTip", "Specify if the travel should stop at landmarks"},
    {"setUnit", "Unit of measurement"},
    {"setUnitTip", "Unit in which the distance is measured"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Travel-for-a-given-time control constructor"},
    {"LoadTimer",             "Travel-for-a-given-time control load"},
    {"SaveTimer",             "Travel-for-a-given-time control save"},
    {"InitESlateAspectTimer", "Travel-for-a-given-time control E-Slate aspect creation"},
  };
}
