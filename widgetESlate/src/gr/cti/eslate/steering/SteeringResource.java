package gr.cti.eslate.steering;

import java.util.*;

/**
 * English language localized strings for the steering control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see         gr.cti.eslate.steering.Steering
 */
public class SteeringResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Steering Control component"},
    {"name", "Steering Control"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    // help file
    {"helpfile", "help/steering.html"},
    // plug names
    {"direction", "Direction"},
    //
    // Other text
    //

    //
    // BeanInfo resources
    //
    {"setDirection", "Direction"},
    {"setDirectionTip", "Enter the direction of the steering control"},
    {"N", "North"},
    {"NE", "Northeast"},
    {"E", "East"},
    {"SE", "Southeast"},
    {"S","South"},
    {"SW", "Southwest"},
    {"W", "West"},
    {"NW", "Northwest"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Steering control constructor"},
    {"LoadTimer",             "Steering control load"},
    {"SaveTimer",             "Steering control save"},
    {"InitESlateAspectTimer", "Steering control E-Slate aspect creation"},
  };
}
