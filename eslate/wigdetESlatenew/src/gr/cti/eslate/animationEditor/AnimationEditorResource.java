package gr.cti.eslate.animationEditor;

import java.util.*;

/**
 * English language localized strings for the animation  viewer component.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 12-May-2002
 * @see		gr.cti.eslate.animationEditor.AnimationEditor
 */
public class AnimationEditorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Component info
    //
    {"componentName", "Animation Editor component"},
    {"name", "Animation Editor"},
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
    //
    // Other text
    //
    {"zoom", "Zoom"},
    {"start", "Start animation player"},
    {"stop", "Stop animation player"},
    {"reset", "Reset animation player"},
    {"loopLabel", "Loop"},
    {"loop", "Looped playback"},
    {"fps", "Frames per second"},
    {"editMilestone", "Edit milestone of "},
    {"editVariables", "Edit variables of "},
    {"variables", "Variables of actor"},
    {"animatedVariables", "Process of variables"},
    {"insertFrames", "Insert frames in "},
    {"deleteFrames", "Delete frames from "},
    {"framesNumber", "Number of frames"},
    {"addFrameLabel", "Add label at frame "},
    {"removeFrameLabel", "Remove label from frame "},
    {"editFrameLabel", "Edit label of frame "},
    {"frameLabel", "Frame label"},
    {"okButton", "OK"},
    {"cancelButton", "Cancel"},
    {"addMilestone", "Add Milestone"},
    {"removeMilestone", "Remove Milestone"},
    {"removeSegment", "Remove Segment"},
    {"removeActor", "Remove Actor"},
    {"shiftRight", "Insert frames"},
    {"shiftLeft", "Remove frames"},
    {"soundActor", "Sound"},
    {"soundFiles", "Sound files"},
    {"addSoundFile", "Add sound"},
    {"editSoundFile", "Edit sound"},
    {"removeSoundFile", "Remove sound"},
    //
    // BeanInfo resources
    //
    //
    // Performance manager resources
    //
  };
}
