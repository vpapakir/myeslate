package gr.cti.eslate.navigator;

import java.util.ListResourceBundle;

/**
 * Greek localized resources for Navigator component.
 *
 * @author      George Birtbilis
 * @author      Kriton Kyrimis
 * @version     3.0.5, 23-Jan-2008
 */
public class MessagesBundle_el_GR extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final String[] info = {
    "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)",
    "Ανάπτυξη: Γ. Μπιρμπίλης, Κ. Κυρίμης",
    "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.",
    "Ανάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.",
    "Το Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.",
    " ",
    "Επιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων",
    "βασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται",
    "και να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."
  };

  static final Object[][] contents =
  {
    {"info", info},
    //
    {"Navigator", "Πλοηγός"},
    {"title", "Ψηφίδα Πλοηγός"},
    {"version", "έκδοση"},
    {"homeURL", "http://E-Slate.cti.gr/"}, //16Jun2000
    //
    {"URL", "URL"},
    {"URL as string", "URL ως αλφαριθμητικό"}, //14Mar2000
    //
    {"Go!", "Δείξε!"},

    {"Navigation", "Πλοήγηση"},
    {"Back", "Πίσω"},
    {"Forward", "Μπροστά"},
    {"Home", "Αρχή"},

    {"View", "Προβολή"},
    {"Stop", "Στάση"},
    {"Refresh", "Ανανέωση"}, //24Jun2000
    {"Address bar", "Ράβδος διεύθυνσης"},
    {"Toolbar", "Ράβδος εργαλείων"},
    {"Status bar", "Ράβδος κατάστασης"},
    //
    {"warning1", "*** ΠΡΟΣΟΧΗ: Ο πλοηγός "},
    {"warning2", " δεν υποστηρίζεται."},
    {"warning3", "*** Αντ' αυτού θα χρησιμοποιηθή ο πλοηγός "},
    {"warning4", " ."},
    //
    {"LoadTimer",             "Ανάκτηση πλοηγού"},
    {"SaveTimer",             "Αποθήκευση πλοηγού"},
  };
}
