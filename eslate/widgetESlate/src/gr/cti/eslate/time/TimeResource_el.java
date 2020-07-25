package gr.cti.eslate.time;

import java.util.*;

/**
 * Greek language localized strings for the travel-for-a-given-time
 * control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.time.Time
 */
public class TimeResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Χειριστήριο Ταξιδιού για Δεδομένο Χρόνο"},
    {"name", "Χειριστήριο Ταξιδιού για Δεδομένο Χρόνο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/time_el.html"},
    // plug names
    {"time", "Χρόνος"},
    //
    // Other text
    //
    {"go", "Εκκίνηση"},
    {"hours", "ώρες"},
    {"minutes", "λεπτά"},
    {"seconds", "δευτερόλεπτα"},
    {"stop", "Στάση στα ορόσημα"},
    {"badUnit", "Η μονάδα αυτή δεν υποστηρίζεται"},
    //
    // BeanInfo resources
    //
    {"setTime", "Χρόνος ταξιδιού"},
    {"setTimeTip", "Δώστε το χρόνο τον οποίον θα διαρκέσει το ταξίδι"},
    {"setStopAtLandmarks", "Στάση στα ορόσημα"},
    {"setStopAtLandmarksTip", "Ορίστε αν το ταξίδι θα σταματά στα ορόσημα"},
    {"setUnit", "Μονάδα μετρήσεως"},
    {"setUnitTip", "Μονάδα με την οποία μετράται η απόσταση"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή χειριστηρίου ταξιδιού για δεδομένο χρόνο"},
    {"LoadTimer",             "Ανάκτηση χειριστηρίου ταξιδιού για δεδομένο χρόνο"},
    {"SaveTimer",             "Αποθήκευση χειριστηρίου ταξιδιού για δεδομένο χρόνο"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς χειριστηρίου ταξιδιού για δεδομένο χρόνο"},
  };
}
