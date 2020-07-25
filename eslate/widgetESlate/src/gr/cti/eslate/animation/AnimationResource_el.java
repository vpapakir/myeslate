package gr.cti.eslate.animation;

import java.util.*;

/**
 * Greek language localized strings for the animation component.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 12-May-2002
 * @see		gr.cti.eslate.animation.Animation
 */
public class AnimationResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Component info
    //
    {"componentName", "Ψηφίδα Κινούμενη Απεικόνιση"},
    {"name", "Κινούμενη Απεικόνιση"},
    {"credits1", "Τμήμα του περιβάλλοντος Αβάκιο (http://E-Slate.cti.gr)"},
    {"credits2", "Ανάπτυξη: Αυγουστίνος Γρυλλάκης"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "έκδοση"},
    //
    // Help file
    //
    //
    // Plug names
    //
    {"animationPlug", "Κινούμενη Απεικόνιση"},
    {"actorPlug", "Ηθοποιός"},
    //
    // Other text
    //
    //
    // BeanInfo resources
    //
    {"setDelay", "Εισήγαγε καθυστέρηση:"},
    {"setDelayTip", "Εισήγαγε καθυστέρηση"},

    {"segmentEnter", "Εισαγωγή σε τμήμα"},
    {"segmentExit", "Έξοδος από τμήμα"},
    {"milestoneFound", "Εύρεση ορόσημου"},
    {"timeChanged", "Αλλαγή χρόνου δρομέα"},
    //
    // Performance manager resources
    //
  };
}