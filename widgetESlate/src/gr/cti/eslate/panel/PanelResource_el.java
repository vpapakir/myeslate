package gr.cti.eslate.panel;

import java.util.*;

/**
 * Greek language localized strings for the panel component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.panel.PanelComponent
 */
public class PanelResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // component info
    //
    {"componentName", "Ψηφίδα Πλαίσιο"},
    {"name", "Πλαίσιο"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    //
    // help file
    //
    {"helpfile", "help/panel_el.html"},
    //
    // Other text
    //
    {"rename", "Μετονομασία"},
    {"clone", "Δημιουργία αντιγράφου"},
    {"remove", "Αφαίρεση"},
    {"sendBack1", "Μετακίνηση "},
    {"sendBack2", " στο βάθος"},
    {"sendBack3", "Μετακίνηση στο βάθος"},
    {"cloneSelected", "Δημιουργία αντιγράφου επιλεγμένων ψηφίδων"},
    {"removeSelected", "Αφαίρεση επιλεγμένων ψηφίδων"},
    {"enterName", "Δώστε το νέο όνομα της ψηφίδας"},
    {"renamingComponent", "Μετονομασία ψηφίδας"},
    {"nameUsed", "Το όνομα αυτό χρησιμοποιείται από αλλη ψηφίδα"},
    {"renamingForbidden", "Δεν επιτρέπεται η μετονομασία ψηφίδων"},
    {"error", "Σφάλμα"},
    {"readContents...", "Ανάγνωση περιεχομένων..."},
    {"saveContents...", "Αποθήκευση περιεχομένων..."},
    {"saveContents", "Αποθήκευση περιεχομένων"},
    {"readContents", "Ανάγνωση περιεχομένων"},
    {"panelContents", "Περιεχόμενα πλαισίων"},
    {"readPanel...", "Ανάγνωση πλαισίου..."},
    {"savePanel...", "Αποθήκευση πλαισίου..."},
    {"savePanel", "Αποθήκευση πλαισίου"},
    {"readPanel", "Ανάγνωση πλαισίου"},
    {"panels", "Πλαίσια"},
    {"fileExists1", "Το αρχείο "},
    {"fileExists2", " υπάρχει ήδη. Να αντικατασταθεί;"},
    {"confirm", "Επιβεβαίωση"},
    //
    // BeanInfo resources
    //
    {"background", "Χρώμα υποβάθρου"},
    {"backgroundTip", "Χρώμα του υποβάθρου του πλαισίου"},
    {"border", "Περίγραμμα"},
    {"borderTip", "Περίγραμμα του πλαισίου"},
    {"layout", "Διάταξη"},
    {"layoutTip", "Διάταξη του πλαισίου"},
    {"designMode", "Κατάσταση σχεδίασης"},
    {"designModeTip", "Ορίστε αν η ψηφίδα θα βρίσκεται σε κατάσταση σχεδίασης"},
    {"componentAdded", "Προσθήκη ψηφίδας"},
    {"componentRemoved", "Αφαίρεση ψηφίδας"},
    {"backgroundImage", "Εικόνα υποβάθρου"},
    {"backgroundImageTip", "Ορίστε την εικόνα υποβάθρου"},
    {"transparent", "Διαφανές"},
    {"transparentTip", "Ορίστε αν η ψηφίδα είναι διαφανής"},
    {"bgStyle", "Στυλ εικόνας υποβάθρου"},
    {"bgStyleTip", "Ορίστε το στυλ με το οποίο θα ζωγραφιστεί η εικόνα υποβάθρου"},
    {"none", "Κανένα"},
    {"centered", "Κεντράρισμα"},
    {"stretched", "’πλωμα"},
    {"tiled", "Μωσαϊκό"},
    {"gridVisible", "Πλέγμα ορατό"},
    {"gridVisibleTip", "Ορίστε αν θα είναι ορατό το πλέγμα στοίχισης"},
    {"gridStep", "Βήμα πλέγματος"},
    {"gridStepTip", "Ορίστε το βήμα του πλέγματος στοίχισης"},
    {"gridColor", "Χρώμα πλέγματος"},
    {"gridColorTip", "Ορίστε το χρώμα του πλέγματος στοίχισης"},
    {"alignToGrid", "Στοίχιση βάσει πλέγματος"},
    {"alignToGridTip", "Ορίστε αν οι ψηφίδες θα στοιχίζονται βάσει ενός πλέγματος"},
    {"selectionColor", "Χρώμα επιλογής"},
    {"selectionColorTip", "Ορίστε το χρώμα του πλαισίου γύρω από την επιλεγμένη ψηφίδα"},
    {"preferredSize", "Προτιμώμενο μέγεθος"},
    {"preferredSizeTip", "Ορίστε το προτιμώμενο μέγεθος τού πλαισίου"},
    {"minimumSize", "Ελάχιστο μέγεθος"},
    {"minimumSizeTip", "Ορίστε το ελάχιστο μέγεθος τού πλαισίου"},
    {"maximumSize", "Μέγιστο μέγεθος"},
    {"maximumSizeTip", "Ορίστε το μέγιστο μέγεθος τού πλαισίου"},
    {"rectangle", "Ορθογώνιο"},
    {"ellipse", "Έλλειψη"},
    {"polygon", "Πολύγωνο"},
    {"freehand", "Ελεύθερο σχέδιο"},
    {"clipShapeType", "Σχήμα πλαισίου"},
    {"clipShapeTypeTip", "Ορίστε το σχήμα τού πλαισίου"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή πλαισίου"},
    {"LoadTimer",             "Ανάκτηση πλαισίου"},
    {"SaveTimer",             "Αποθήκευση πλαισίου"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς πλαισίου"},
  };
}
