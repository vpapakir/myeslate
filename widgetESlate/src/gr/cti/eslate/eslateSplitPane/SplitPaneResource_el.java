package gr.cti.eslate.eslateSplitPane;

import java.util.*;

/**
 * Greek language localized strings for the split pane component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.eslateSplitPane.ESlateSplitPane
 */
public class SplitPaneResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    //
    {"componentName", "Ψηφίδα Πλαίσιο διαχωρισμένο"},
    {"name", "Πλαίσιο διαχωρισμένο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    //
    // plug names
    //
    //
    // Other text
    //
    //
    // BeanInfo resources
    //
    {"preferredSize", "Προτιμώμενο μέγεθος"},
    {"preferredSizeTip", "Ορίστε το προτιμώμενο μέγεθος τής επιφάνειας"},
    {"minimumSize", "Ελάχιστο μέγεθος"},
    {"minimumSizeTip", "Ορίστε το ελάχιστο μέγεθος τής επιφάνειας"},
    {"maximumSize", "Μέγιστο μέγεθος"},
    {"maximumSizeTip", "Ορίστε το μέγιστο μέγεθος τής επιφάνειας"},
    {"firstComponent", "Κλάση πρώτης ψηφίδας"},
    {"firstComponentTip", "Ορίστε το όνομα τής κλάσης τής πρώτης ψηφίδας"},
    {"secondComponent", "Κλάση δεύτερης ψηφίδας"},
    {"secondComponentTip", "Ορίστε το όνομα τής κλάσης τής δεύτερης ψηφίδας"},
    {"notCompo1", "Η κλάση "},
    {"notCompo2", " δεν είναι υποκλάση τού Component"},
    {"error", "Σφάλμα"},
    {"notFound1", "Η κλάση "},
    {"notFound2", " δεν βρέθηκε"},
    {"continuousLayout", "Συνεχής διάταξη"},
    {"continuousLayoutTip", "Ορίστε αν τα περιεχόμενα θα ζωγραφίζονται όσο μετακινείται η διαχωριστική γραμμή"},
    {"dividerLocation", "Θέση διαχωριστικής γραμμής"},
    {"dividerLocationTip", "Ορίστε τη θέση τής διαχωριστικής γραμμής (-1 για προτιμώμενη)"},
    {"dividerSize", "Μέγεθος διαχωριστικής γραμμής"},
    {"dividerSizeTip", "Ορίστε το μέγεθος τής διαχωριστικής γραμμής"},
    {"oneTouchExpandable", "Γρήγορη σύμπτυξη/ανάπτυξη"},
    {"oneTouchExpandableTip", "Ορίστε αν θα υπάρχουν εργαλεία για γρήγορη ανάπτυξη/σύμπτυξη τής διαχωριστικής γραμμής"},
    {"orientation", "Προσανατολισμός"},
    {"orientationTip", "Ορίστε τον προσανατολισμό τής επιφάνειας"},
    {"horizontal", "Οριζόντιος"},
    {"vertical", "Κατακόρυφος"},
    {"resizeWeight", "Βάρος κατά την αλλαγή μεγέθους"},
    {"resizeWeightTip", "Ορίστε το \"βάρος\" τής πρώτης ψηφίδας κατά την αλλαγή μεγέθους (0..1)"},
    //{"border", "Περίγραμμα"},
    //{"borderTip", "Περίγραμμα τού διαχωρισμένου πλαισίου"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή διαχωρισμένου πλαισίου"},
    {"LoadTimer",             "Ανάκτηση διαχωρισμένου πλαισίου"},
    {"SaveTimer",             "Αποθήκευση διαχωρισμένου πλαισίου"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς διαχωρισμένου πλαισίου"},
  };
}
