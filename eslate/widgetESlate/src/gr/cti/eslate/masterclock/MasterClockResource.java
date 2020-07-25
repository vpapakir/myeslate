package gr.cti.eslate.masterclock;

import java.util.*;

/**
 * English language localized strings for the master clock component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.masterclock.MasterClock
 */
public class MasterClockResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Master Clock component"},
    {"name", "Master Clock"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    // help file
    {"helpfile", "help/masterclock.html"},
    // plug names
    {"tick", "Tick"},
    //
    // Other text
    //
    {"start", "Start master clock"},
    {"stop", "Stop master clock"},
    {"set", "Set GMT"},
    {"select", "Select time scale"},
    {"timeScale", "Time scale"},
    {"h", "H"},
    {"m", "M"},
    {"s", "S"},
    {"noComponents1" , "No components connected to master clock."},
    {"noComponents2", "Master clock stopped."},
    {"warning", "Warning"},
    //
    // BeanInfo resources
    //
    {"setMinimumTimeScale", "Minimum time scale"},
    {"setMinimumTimeScaleTip", "Enter the minimum time scale"},
    {"setMaximumTimeScale", "Maximum time scale"},
    {"setMaximumTimeScaleTip", "Enter the maximum time scale"},
    {"setTimeScale", "Time scale"},
    {"setTimeScaleTip", "Enter the time scale"},
    {"startStop", "Store start/stop state"},
    {"startStopTip", "Specify if you want the start/stop state to be stored when saving"},
    {"setSleepInterval", "Milliseconds between ticks"},
    {"setSleepIntervalTip", "Specify the number of milliseconds to sleep after sending each tick"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Master clock constructor"},
    {"LoadTimer",             "Master clock load"},
    {"SaveTimer",             "Master clock save"},
    {"InitESlateAspectTimer", "Master clock E-Slate aspect creation"},
  };
}
