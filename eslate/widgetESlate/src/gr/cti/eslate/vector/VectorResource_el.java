package gr.cti.eslate.vector;

import java.util.*;

/**
 * Greek language localized strings for the vector component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.2, 23-Jan-2008
 * @see         gr.cti.eslate.vector.VectorComponent
 */
public class VectorResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Ψηφίδα Διάνυσμα"},
    {"name", "Διάνυσμα"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Κ. Κυρίμης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    // help file
    {"helpfile", "help/vector_el.html"},
    // plug names
    {"vector", "Διάνυσμα"},
    //
    // Other text
    //
    {"x", "Χ"},
    {"y", "Ψ"},
    {"length", "Μήκος"},
    {"scale", "Κλίμακα"},
    {"angle", "Γωνία"},
    {"needPositive", "Η κλίμακα πρέπει να είναι ένας θετικός πραγματικός αριθμός"},
    {"error", "Σφάλμα"},
    {"nameUsed", "Το όνομα αυτό έχει ανατεθεί σε άλλη συνιστώσα τού διανύσματος"},
    //
    // BeanInfo resources
    //
    {"setEast", "Οριζόντια συνιστώσα"},
    {"setEastTip", "Οριζόντια συνιστώσα του διανύσματος"},
    {"setNorth", "Κατακόρυφη συνιστώσα"},
    {"setNorthTip", "Κατακόρυφη συνιστώσα του διανύσματος"},
    {"setLength", "Μήκος"},
    {"setLengthTip", "Μήκος του διανύσματος"},
    {"setAngle", "Γωνία"},
    {"setAngleTip", "Γωνία του διανύσματος"},
    {"setScale", "Κλίμακα"},
    {"setScaleTip", "Κλίμακα του διανύσματος"},
    {"setPrecision", "Ακρίβεια"},
    {"setPrecisionTip", "Αριθμός ψηφίων που εμφανίζονται μετά την υποδιαστολή"},
    {"setEditable", "Επιτρέπονται αλλαγές"},
    {"setEditableTip", "Ορίστε αν ο χρήστης μπορεί να αλλάξει την τιμή του διανύσματος"},
    {"graphVisible", "Γραφική αναπαράσταση"},
    {"graphVisibleTip", "Ορίστε αν θα εμφανίζεται η γραφική αναπαράσταση του διανύσματος"},
    {"componentsVisible", "Αριθμητική αναπαράσταση"},
    {"componentsVisibleTip", "Ορίστε αν θα εμφανίζεται η αριθμητική αναπαράσταση του διανύσματος"},
    {"eastName", "Όνομα οριζόντιας συνιστώσας"},
    {"eastNameTip", "Όνομα τής οριζόντιας συνιστώσας τού διανύσματος"},
    {"northName", "Όνομα κατακόρυφης συνιστώσας"},
    {"northNameTip", "Όνομα τής κατακόρυφης συνιστώσας τού διανύσματος"},
    {"angleName", "Όνομα γωνίας"},
    {"angleNameTip", "Όνομα τής γωνίας τού διανύσματος"},
    {"lengthName", "Όνομα μήκους"},
    {"lengthNameTip", "Όνομα τού μήκους τού διανύσματος"},
    {"valueChanged", "Αλλαγή τιμής διανύσματος"},
    {"maxLength", "Μέγιστο μήκος"},
    {"maxLengthTip", "Μέγιστο μήκος τού διανύσματος (0=χωρίς περιορισμό)"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Κατασκευή διανύσματος"},
    {"LoadTimer",             "Ανάκτηση διανύσματος"},
    {"SaveTimer",             "Αποθήκευση διανύσματος"},
    {"InitESlateAspectTimer", "Κατασκευή αβακιακής πλευράς διανύσματος"},
  };
}
