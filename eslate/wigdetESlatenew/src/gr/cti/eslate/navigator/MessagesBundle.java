package gr.cti.eslate.navigator;

import java.util.ListResourceBundle;

/**
 * English localized resources for Navigator component.
 *
 * @author      George Birtbilis
 * @author      Kriton Kyrimis
 * @version     3.0.5, 23-Jan-2008
 */
public class MessagesBundle extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final String[] info = {
    "Part of the E-Slate environment (http://E-Slate.cti.gr)",
    "Development: G. Birbilis, K. Kyrimis)",
    "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.",
    "Ανάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.",
    "Το Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.",
    " ",
    "Επιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων",
    "βασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται",
    "και να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."
  };

  static final Object[][] contents = {
    {"info", info},
    //
    {"title", "Navigator component"},
    {"version", "version"},
    {"homeURL", "http://E-Slate.cti.gr/"},
    //
    {"URL", "URL"},
    {"URL as string", "URL as string"},
    //
    {"warning1", "*** WARNING: Navigator "},
    {"warning2", " is not supported."},
    {"warning3", "*** Using "},
    {"warning4", " instead."},
    //
    {"LoadTimer",             "Navigator load"},
    {"SaveTimer",             "Navigator save"},
  };
}
