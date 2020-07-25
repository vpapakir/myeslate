package gr.cti.eslate.chronometer;

import java.util.*;

/**
 * Greek language localized strings for the chronometer component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.chronometer.Chronometer
 */
public class ChronometerResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Χρονόμετρο"},
    {"name", "Χρονόμετρο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/chronometer_el.html"},
    // plug names
    {"tick", "Χρονισμός"},
    {"hours", "Ένδειξη σε ώρες"},
    {"mins", "Ένδειξη σε λεπτά"},
    {"secs", "Ένδειξη σε δευτερόλεπτα"},
    {"millis", "Ένδειξη σε χιλιοστά δευτερολέπτου"},
    //
    // Other text
    //
    {"stopString1", "Καμία ψηφίδα δεν είναι συνδεδεμένη στο χρονόμετρο."},
    {"stopString2", "Το χρονόμετρο σταμάτησε."},
    {"warning", "Προσοχή"},
    {"start", "Εκκίνηση χρονομέτρου"},
    {"stop", "Σταμάτημα χρονομέτρου"},
    {"reset", "Μηδενισμός ενδείξεως"},
    //
    // BeanInfo resources
    //
    {"setMilliseconds", "Ένδειξη σε χιλιοστά δευτερολέπτου"},
    {"setMillisecondsTip", "Δώστε την τιμή της ενδείξεως του χρονομέτρου σε χιλιοστά δευτερολέπτου"},
    {"chronometerStarted", "Εκκίνηση χρονομέτρου"},
    {"chronometerStopped", "Σταμάτημα χρονομέτρου"},
    {"valueChanged", "Αλλαγή ένδειξης χρονομέτρου"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή χρονομέτρου"},
    {"LoadTimer",             "Ανάκτηση χρονομέτρου"},
    {"SaveTimer",             "Αποθήκευση χρονομέτρου"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς χρονομέτρου"},
  };
}
