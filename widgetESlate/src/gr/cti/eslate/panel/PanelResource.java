package gr.cti.eslate.panel;

import java.util.*;

/**
 * English language localized strings for the panel component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see         gr.cti.eslate.panel.PanelComponent
 */
public class PanelResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // component info
    //
    {"componentName", "Panel component"},
    {"name", "Panel"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    //
    // help file
    //
    {"helpfile", "help/panel.html"},
    //
    // Other text
    //
    {"rename", "Rename"},
    {"clone", "Clone"},
    {"remove", "Remove"},
    {"sendBack1", "Send "},
    {"sendBack2", " to back"},
    {"sendBack3", "Send to back"},
    {"cloneSelected", "Clone selected components"},
    {"removeSelected", "Remove selected components"},
    {"enterName", "Enter the new name of component"},
    {"renamingComponent", "Renaming component"},
    {"nameUsed", "There is another component with this name"},
    {"renamingForbidden", "Renaming components is not allowed"},
    {"error", "Error"},
    {"readContents", "Read contents..."},
    {"saveContents...", "Save contents..."},
    {"panelContents", "Panel contents"},
    {"saveContents", "Save panel contents"},
    {"readContents", "Read panel contents"},
    {"readPanel...", "Read panel..."},
    {"savePanel...", "Save panel..."},
    {"panels", "Panels"},
    {"savePanel", "Save panel"},
    {"readPanel", "Read panel"},
    {"fileExists1", "File "},
    {"fileExists2", " already exists. Overwrite it?"},
    {"confirm", "Confirm"},
    //
    // BeanInfo resources
    //
    {"background", "Background color"},
    {"backgroundTip", "Background color of the panel"},
    {"border", "Border"},
    {"borderTip", "Border of the panel"},
    {"layout", "Layout"},
    {"layoutTip", "Layout of the panel"},
    {"designMode", "Design mode"},
    {"designModeTip", "Specify if the component will be in design mode"},
    {"componentAdded", "Component added"},
    {"componentRemoved", "Component removed"},
    {"backgroundImage", "Background image"},
    {"backgroundImageTip", "Specify the background image"},
    {"transparent", "Transparent"},
    {"transparentTip", "Specify if the component is transparent"},
    {"bgStyle", "Background image style"},
    {"bgStyleTip", "Specify the style in which to draw the background image"},
    {"none", "None"},
    {"centered", "Centered"},
    {"stretched", "Stretched"},
    {"tiled", "Tiled"},
    {"gridVisible", "Grid visible"},
    {"gridVisibleTip", "Specify if the alignment grid should be visible"},
    {"gridStep", "Grid step"},
    {"gridStepTip", "Specify the step of the alignment grid"},
    {"gridColor", "Grid color"},
    {"gridColorTip", "Specify the color of the alignment grid"},
    {"alignToGrid", "Align to grid"},
    {"alignToGridTip", "Specify if components should be aligned to a grid"},
    {"selectionColor", "Selection color"},
    {"selectionColorTip", "Specify the color of the frame around the selected component"},
    {"preferredSize", "Preferred size"},
    {"preferredSizeTip", "Specify the preferred size of the panel"},
    {"minimumSize", "Minimum size"},
    {"minimumSizeTip", "Specify the minimum size of the panel"},
    {"maximumSize", "Maximum size"},
    {"maximumSizeTip", "Specify the maximum size of the panel"},
    {"clipShapeType", "Panel shape"},
    {"clipShapeTypeTip", "Specify the shape of the panel"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Panel constructor"},
    {"LoadTimer",             "Panel load"},
    {"SaveTimer",             "Panel save"},
    {"InitESlateAspectTimer", "Panel E-Slate aspect creation"},
  };
}
