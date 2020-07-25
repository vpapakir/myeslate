package gr.cti.eslate.distance;

import java.util.*;

/**
 * Greek language localized strings for the distance control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2007
 * @see         gr.cti.eslate.distance.Distance
 */
public class DistanceResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Χειριστήριο Απόστασης"},
    {"name", "Χειριστήριο Απόστασης"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/distance_el.html"},
    // plug names
    {"distance", "Απόσταση"},
    //
    // Other text
    //
    {"go", "Εκκίνηση"},
    {"m", "μέτρα"},
    {"km", "χιλιόμετρα"},
    {"ft", "πόδια"},
    {"miles", "μίλια"},
    {"stop", "Στάση στα ορόσημα"},
    {"badUnit", "Η μονάδα αυτή δεν υποστηρίζεται"},
    //
    // BeanInfo resources
    //
    {"setDistance", "Απόσταση που θα διανυθεί"},
    {"setDistanceTip", "Δώστε την απόσταση που πρέπει να διανυθεί"},
    {"setStopAtLandmarks", "Στάση στα ορόσημα"},
    {"setStopAtLandmarksTip", "Ορίστε αν το ταξίδι θα σταματά στα ορόσημα"},
    {"setUnit", "Μονάδα μέτρησης"},
    {"setUnitTip", "Μονάδα με την οποία μετράται η απόσταση"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή χειριστηρίου απόστασης"},
    {"LoadTimer",             "Ανάκτηση χειριστηρίου απόστασης"},
    {"SaveTimer",             "Αποθήκευση χειριστηρίου απόστασης"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς χειριστηρίου απόστασης"},
  };
}
