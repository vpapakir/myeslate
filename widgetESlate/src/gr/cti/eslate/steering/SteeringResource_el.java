package gr.cti.eslate.steering;

import java.util.*;

/**
 * Greek language localized strings for the steering control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.steering.Steering
 */
public class SteeringResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Πιλοτήριο"},
    {"name", "Πιλοτήριο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/steering_el.html"},
    // plug names
    {"direction", "Κατεύθυνση"},
    //
    // Other text
    //

    //
    // BeanInfo resources
    //
    {"setDirection", "Κατεύθυνση"},
    {"setDirectionTip", "Δώστε την κατεύθυνση του χειριστηρίου"},
    {"N", "Βόρεια"},
    {"NE", "Βορειοανατολικά"},
    {"E", "Ανατολικά"},
    {"SE", "Νοτιοανατολικά"},
    {"S","Νότια"},
    {"SW", "Νοτιοδυτικά"},
    {"W", "Δυτικά"},
    {"NW", "Βορειοδυτικά"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή πιλοτηρίου"},
    {"LoadTimer",             "Ανάκτηση πιλοτηρίου"},
    {"SaveTimer",             "Αποθήκευση πιλοτηρίου"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς πιλοτηρίου"},
  };
}
