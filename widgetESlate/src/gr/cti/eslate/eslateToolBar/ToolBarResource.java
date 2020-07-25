package gr.cti.eslate.eslateToolBar;

import java.util.*;

/**
 * English language localized strings for the toolbar component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see         gr.cti.eslate.eslateToolBar.ESlateToolBar
 */
public class ToolBarResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // component info
    //
    {"componentName", "Toolbar component"},
    {"name", "Toolbar"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    //
    // Other text
    //
    {"group", "Group"},
    {"nullName", "Name must not be null"},
    {"nullGroup", "Group must not be null"},
    {"nullComponent", "Component must not be null"},
    {"groupExists", "A group having this name already exists"},
    {"foreignGroup", "This group does not belong to this toolbar"},
    {"foreignTool", "This tool does not belong to this toolbar"},
    {"badIndex", "Invalid component location"},
    {"removeFromParent", "Remove toolbar"},
    {"confirm", "Confirmation"},
    {"confirmRemove", "The toolbar has been modified.\nAre you sure that you want to remove it?"},
    {"notice", "Notice"},
    {"noTopLevel", "Please use E-Slate components from it"},
    {"noAddSeparator", "Do not use ESlateToolbar.addSeparator. Use visual groups, instead."},
    {"nullTool", "Tool must not be null"},
    //
    // BeanInfo resources
    //
    {"orientation", "Orientation"},
    {"orientationTip", "Specify the orientation of the toolbar"},
    {"horizontal", "Horizontal"},
    {"vertical", "Vertical"},
    {"dynBorders", "Dynamically drawn borders"},
    {"dynBordersTip", "Specify if tool borders should be drawn dynamically"},
    {"border", "Border"},
    {"borderTip", "Specify the border of the toolbar"},
    {"separation", "Separation between visual groups"},
    {"separationTip", "Specify the spacing between successive visual groups"},
    {"leadingSeparation", "Separation between the first tool and the edge of the toolbar"},
    {"leadingSeparationTip", "Specify the spacing between the first tool and the edge of the toolbar"},
    {"mouseClickedOnTool", "Mouse clicked on a tool"},
    {"mousePressedOnTool", "Mouse pressed on a tool"},
    {"mouseReleasedOnTool", "Mouse released on a tool"},
    {"mouseEnteredOnTool", "Mouse entered a tool"},
    {"mouseExitedOnTool", "Mouse exited a tool"},
    {"mouseDraggedOnTool", "Mouse dragged on a tool"},
    {"mouseMovedOnTool", "Mouse moved on a tool"},
    {"actionPerformedOnTool", "Action Performed on a tool"},
    {"vgLayout", "Visual group layout"},
    {"vgLayoutTip", "Modify the layout and composition of the toolbar"},
    {"bgLayout", "Button groups"},
    {"bgLayoutTip", "Arrange the toolbar's mutual exclusion button groups"},
    {"centered", "Centered contents"},
    {"centeredTip", "Specify if the contents of hte toolbar should be centered"},
    {"borderPainted", "Border painted"},
    {"borderPaintedTip", "Specify if the border of the toolbar should be painted"},
    {"floatable", "Floatable"},
    {"floatableTip", "Specify if the toolbar can be dragged into a different position"},
    {"preferredSize", "Preferred Size"},
    {"preferredSizeTip", "Specify the preferred size of the toolbar"},
    {"minimumSize", "Minimum Size"},
    {"minimumSizeTip", "Specify the minimum size of the toolbar"},
    {"maximumSize", "Maximum Size"},
    {"maximumSizeTip", "Specify the maximum size of the toolbar"},
    {"modified", "Modified"},
    {"modifiedTip", "Specify if the toolbar has been mofified from its stable state"},
    {"menuEnabled", "Toolbar menu enabled"},
    {"menuEnabledTip", "Specify if a menu will appear when right-clicking on unused space"},
    //
    // Performance manager resources
    //
    {"ConstructorTimer",      "Toolbar constructor"},
    {"LoadTimer",             "Toolbar load"},
    {"SaveTimer",             "Toolbar save"},
    {"InitESlateAspectTimer", "Toolbar E-Slate aspect creation"},
  };
}
