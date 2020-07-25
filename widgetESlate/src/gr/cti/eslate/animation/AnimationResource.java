package gr.cti.eslate.animation;

import java.util.*;

/**
 * English language localized strings for the animation component.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 12-May-2002
 * @see		gr.cti.eslate.animation.Animation
 */
public class AnimationResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Component info
    //
    {"componentName", "Animation component"},
    {"name", "Animation"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: Augustine Grillakis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    //
    // Help file
    //
    //
    // Plug names
    //
    {"animationPlug", "Animation"},
    {"actorPlug", "Actor"},
    //
    // Other text
    //
    //
    // BeanInfo resources
    //
    {"setDelay", "Enter delay:"},
    {"setDelayTip", "Enter delay"},

    {"segmentEntered", "Enter segment"},
    {"segmentExited", "Exit segment"},
    {"milestoneFound", "Find milestone"},
    {"timeChanged", "Cursor time change"},
    //
    // Performance manager resources
    //
  };
}
