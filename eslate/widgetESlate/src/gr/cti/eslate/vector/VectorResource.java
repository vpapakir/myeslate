package gr.cti.eslate.vector;

import java.util.*;

/**
 * English language localized strings for the vector component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.2, 23-Jan-2008
 * @see         gr.cti.eslate.vector.VectorComponent
 */
public class VectorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // component info
    {"componentName", "Vector component"},
    {"name", "Vector"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    // help file
    {"helpfile", "help/vector.html"},
    // plug names
    {"vector", "Vector"},
    //
    // Other text
    //
    {"x", "X"},
    {"y", "Y"},
    {"length", "Length"},
    {"scale", "Scale"},
    {"angle", "Angle"},
    {"needPositive", "Scale must be a positive real number"},
    {"error", "Error"},
    {"nameUsed", "This name is assigned to another component of the vector"},
    //
    // BeanInfo resources
    //
    {"setEast", "Horizontal component"},
    {"setEastTip", "Horizontal component of the vector"},
    {"setNorth", "Vertical component"},
    {"setNorthTip", "Vertical component of the vector"},
    {"setLength", "Length"},
    {"setLengthTip", "Length of the vector"},
    {"setAngle", "Angle"},
    {"setAngleTip", "Angle of the vector"},
    {"setScale", "Scale"},
    {"setScaleTip", "Scale of the vector"},
    {"setPrecision", "Precision"},
    {"setPrecisionTip", "Number of digits to display after the decimal point"},
    {"setEditable", "Editable"},
    {"setEditableTip", "Specify if the user can modify the value of the vector"},
    {"graphVisible", "Graphical representation"},
    {"graphVisibleTip", "Specify if the vector's graphical representation should be displayed"},
    {"componentsVisible", "Numeric reprisentation"},
    {"componentsVisibleTip", "Specify if the vector's numeric representation should be displayed"},
    {"eastName", "Name of horizontal component"},
    {"eastNameTip", "Name of the horizontal component of the vector"},
    {"northName", "Name of vertical component"},
    {"northNameTip", "Name of the vertical component of the vector"},
    {"angleName", "Name of angle"},
    {"angleNameTip", "Name of the angle of the vector"},
    {"lengthName", "Name of length"},
    {"lengthNameTip", "Name of the length of the vector"},
    {"valueChanged", "Vector value change"},
    {"maxLength", "Maximum length"},
    {"maxLengthTip", "Maximum length of the vector (0=no restriction)"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Vector constructor"},
    {"LoadTimer",             "Vector load"},
    {"SaveTimer",             "Vector save"},
    {"InitESlateAspectTimer", "Vector E-Slate aspect creation"},
  };
}
