package gr.cti.eslate.masterclock;

import java.util.*;

/**
 * Greek language localized strings for the master clock component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.masterclock.MasterClock
 */
public class MasterClockResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Χρονιστής"},
    {"name", "Χρονιστής"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/masterclock_el.html"},
    // plug names
    {"tick", "Χρονισμός"},
    //
    // Other text
    //
    {"start", "Εκκίνηση χρονιστή"},
    {"stop", "Σταμάτημα χρονιστή"},
    {"set", "Ρύθμιση ώρας Γκρήνουιτς"},
    {"select", "Επιλογή κλίμακας χρόνου"},
    {"timeScale", "Κλίμακα χρόνου"},
    {"h", "Ω"},
    {"m", "Λ"},
    {"s", "Δ"},
    {"noComponents1", "Καμία ψηφίδα δεν είναι συνδεδεμένη με το χρονιστή."},
    {"noComponents2", "Ο χρονιστής σταμάτησε."},
    {"warning", "Προσοχή"},
    //
    // BeanInfo resources
    //
    {"setMinimumTimeScale", "Ελάχιστη κλίμακα χρόνου"},
    {"setMinimumTimeScaleTip", "Δώστε την ελάχιστη επιτρεπόμενη κλίμακα χρόνου"},
    {"setMaximumTimeScale", "Μέγιστη κλίμακα χρόνου"},
    {"setMaximumTimeScaleTip", "Δώστε τη μέγιστη επιτρεπόμενη κλίμακα χρόνου"},
    {"setTimeScale", "Κλίμακα χρόνου"},
    {"setTimeScaleTip", "Δώστε την τιμή της κλίμακας χρόνου"},
    {"startStop", "Αποθήκευση κατάστασης εκκίνησης/σταματήματος"},
    {"startStopTip", "Ορίστε αν θα αποθηκεύεται η κατάσταση εκκίνησης/σταματήματος"},
    {"setSleepInterval", "Χιλιοστά δευτερολέπτου μεταξύ τικ"},
    {"setSleepIntervalTip", "Ορίστε το χρόνο αναμονής μετά την αποστολή κάθε τικ, σε χιλιοστά δευτερολέπτου"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή χρονιστή"},
    {"LoadTimer",             "Ανάκτηση χρονιστή"},
    {"SaveTimer",             "Αποθήκευση χρονιστή"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς χρονιστή"},
  };
}
