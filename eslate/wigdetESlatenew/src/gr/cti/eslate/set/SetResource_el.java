package gr.cti.eslate.set;

import java.util.*;

/**
 * Greek language localized strings for the set component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.set.Set
 */
public class SetResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Σύνολο"},
    {"name", "Σύνολο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Σχεδιαστική ιδέα: προϊόν Tabletop (Broderbund)"},
    {"credits3", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits4", "Συμβολή: Γ. Τσιρώνης, Χ. Κυνηγός, Μ. Κουτλής"},
    {"credits5", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/set_el.html"},
    // plug names
    {"database", "Βάση δεδομένων"},
    // Other text
    //
    {"count", "Μέτρησε"},
    {"percentTotal", "% από το σύνολο"},
    {"total", "Σύνολο"},
    {"mean", "Μέσος όρος"},
    {"median", "Μεσαίος"},
    {"smallest", "Μικρότερος"},
    {"largest", "Μεγαλύτερος"},
    {"percent", "Επί τοις εκατό"},
    {"and", "και"},
    {"or", "ή"},
    {"not", "όχι"},
    {"all", "Όλα"},
    {"queryA", "ερώτηση Α"},
    {"queryB", "ερώτηση Β"},
    {"queryC", "ερώτηση Γ"},
    {"select", "Επιλογή στοιχείων"},
    {"selectSet", "Επιλογή υποσυνόλων"},
    {"selectOval", "Επιλογή ελλείψεων"},
    {"delete", "Διαγραφή έλλειψης"},
    {"new", "Νέα έλλειψη"},
    {"copy", "Αντιγραφή"},
    {"project", "Προβολή επιλεγμένου πεδίου"},
    {"projField", "Πεδίο προβολής"},
    {"calculate", "Υπολογισμός"},
    {"calcOp", "Υπολογιστική λειτουργία"},
    {"calcKey", "Πεδίο υπολογισμού"},
    {"true", "αληθές"},
    {"false", "ψευδές"},
    {"??", ";;"},
    {"badField1", "Το πεδίο"},
    {"badField2", "δεν υπάρχει"},
    {"badOp1", "Η λειτουργία"},
    {"badOp2", "δεν υποστηρίζεται"},
    {"badKey", "Ακατάλληλο πεδίο υπολογισμού"},
    {"badTable1", "Ο πίνακας"},
    {"badTable2", "δεν υπάρχει"},
    {"badVersion1", "Η αποθηκευμένη έκδοση "},
    {"badVersion2", " είναι ασύμβατη. Απαιτείται η έκδοση "},
    {"badVersion3", "."},
    {"wrongTableNumber", "Ο αριθμός των πινάκων στη βάση είναι διαφορετικός\nαπό αυτόν στο αρχείο του μικροκόσμου."},
    {"error", "Σφάλμα"},
    {"nullQuery", "ψευδές"},
    {"none", "--κανένα--"},
    //
    // BeanInfo resources
    //
    {"setCalcKey", "Πεδίο υπολογισμού"},
    {"setCalcKeyTip", "Πεδίο πάνω στο οποίο θα βασιστούν οι υπολογισμοί"},
    {"setCalcOp", "Υπολογιστική λειτουργία"},
    {"setCalcOpTip", "Υπολογιστική λειτουργία που θα εκτελεσθεί"},
    {"setProjectionField", "Πεδίο προβολής"},
    {"setProjectionFieldTip", "Πεδίο του πίνακα που θα προβληθεί στο σύνολο"},
    {"setSelectedTable", "Επιλεγμένος πίνακας"},
    {"setSelectedTableTip", "Επιλέξτε τον πίνακα που θα απεικονισθεί"},
    {"setShowLabels", "Εμφάνιση ερωτήσεων"},
    {"setShowLabelsTip", "Εμφάνιση των ερωτήσεων που δημιούργησαν το κάθε σύνολο"},
    {"uniformProjection", "Ενιαίο πεδίο προβολής"},
    {"uniformProjectionTip", "Ορίστε αν θέλετε το ίδιο πεδίο προβολής για όλα τα υποσύνολα"},
    {"background", "Χρώμα υποβάθρου"},
    {"backgroundTip", "Ορίστε το χρώμα του υποβάθρου "},
    {"selectionColor", "Χρώμα επιλεγμένου υποσυνόλου"},
    {"selectionColorTip", "Ορίστε το χρώμα του επιλεγμένου υποσυνόλου"},
    {"toolBarVisible", "Ράβδος εργαλείων ορατή"},
    {"toolBarVisibleTip", "Ορίστε αν θα εμφανίζεται η ράβδος εργαλείων τής ψηφίδας"},
    {"backgroundImage", "Εικόνα υποβάθρου"},
    {"backgroundImageTip", "Ορίστε την εικόνα που θα ζωγραφίζεται στο υπόβαθρο"},
    {"selectionImage", "Εικόνα επιλογής"},
    {"selectionImageTip", "Οριστε την εικόνα με την οποία θα ζωγραφίζεται το επιλεγμένο υποσύνολο"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή συνόλου"},
    {"LoadTimer",             "Ανάκτηση συνόλου"},
    {"SaveTimer",             "Αποθήκευση συνόλου"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς συνόλου"},
  };
}
