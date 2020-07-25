package gr.cti.eslate.set;

import java.util.*;

/**
 * English language localized strings for the set component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.set.Set
 */
public class SetResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Set component"},
    {"name", "Set"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Design idea: Tabletop (Broderbund)"},
    {"credits3", "Development: K. Kyrimis"},
    {"credits4", "Contribution: G. Tsironis, Ch. Kynigos, M. Koutlis"},
    {"credits5", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    // help file
    {"helpfile", "help/set.html"},
    // plug names
    {"database", "Database"},
    // Other text
    //
    {"count", "Count"},
    {"percentTotal", "% of total"},
    {"total", "Total"},
    {"mean", "Mean"},
    {"median", "Median"},
    {"smallest", "Smallest"},
    {"largest", "Largest"},
    {"percent", "Percent"},
    {"and", "and"},
    {"or", "or"},
    {"not", "not"},
    {"all", "All"},
    {"queryA", "query A"},
    {"queryB", "query B"},
    {"queryC", "query C"},
    {"select", "Select elements"},
    {"selectSet", "Select subsets"},
    {"selectOval", "Select ellipses"},
    {"delete", "Delete ellipse"},
    {"new", "New ellipse"},
    {"copy", "Copy"},
    {"project", "Project selected field"},
    {"projField", "Projection field"},
    {"calculate", "Calculate"},
    {"calcOp", "Calculation operation"},
    {"calcKey", "Calculation field"},
    {"true", "true"},
    {"false", "false"},
    {"??", "??"},
    {"badField1", "Field"},
    {"badField2", "does not exist"},
    {"badOp1", "Operation"},
    {"badOp2", "is not supported"},
    {"badKey", "Invalid calculation field"},
    {"badTable1", "Table"},
    {"badTable2", "does not exist"},
    {"badVersion1", "Incompatible saved version "},
    {"badVersion2", ". Version "},
    {"badVersion3", " required."},
    {"wrongTableNumber", "Number of tables in database is different\nfrom that stored in the microworld file."},
    {"error", "Error"},
    {"nullQuery", "false"},
    {"none", "--none--"},
    //
    // BeanInfo resources
    //
    {"setCalcKey", "Calculation field"},
    {"setCalcKeyTip", "Field on which calculations will be based"},
    {"setCalcOp", "Calculation operation"},
    {"setCalcOpTip", "Calculation operation to perform"},
    {"setProjectionField", "Projection field"},
    {"setProjectionFieldTip", "Table field to project on the set"},
    {"setSelectedTable", "Selected table"},
    {"setSelectedTableTip", "Select the table to display"},
    {"setShowLabels", "Display queries"},
    {"setShowLabelsTip", "Display the queries that generated each set"},
    {"uniformProjection", "Uniform projection field"},
    {"uniformProjectionTip", "Specify if you want the same projection field for all subsets"},
    {"background", "Background color"},
    {"backgroundTip", "Specify the color of the background"},
    {"selectionColor", "Selected subset color"},
    {"selectionColorTip", "Specify the color of the selected subset"},
    {"toolBarVisible", "Toolbar visible"},
    {"toolBarVisibleTip", "Specify if the component's toolbar will be shown"},
    {"backgroundImage", "Background image"},
    {"backgroundImageTip", "Specify the image to paint in the background"},
    {"selectionImage", "Selection image"},
    {"selectionImageTip", "Specify the image with which to paint selected subject"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Set constructor"},
    {"LoadTimer",             "Set load"},
    {"SaveTimer",             "Set save"},
    {"InitESlateAspectTimer", "Set E-Slate aspect creation"},
  };
}
