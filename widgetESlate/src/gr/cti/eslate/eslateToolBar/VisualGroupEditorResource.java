package gr.cti.eslate.eslateToolBar;

import java.util.*;

/**
 * English language localized strings for the visual group editor of the
 * E-Slate toolbar.
 *
 * @author      Kriton kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.eslateToolBar.VisualGroupEditor
 */
public class VisualGroupEditorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"vgDialogTitle", "Visual group layout"},
    {"root", "Visual groups"},
    {"error", "Error"},
    {"addGroup", "Add visual group"},
    {"addTool", "Add tool"},
    {"remove", "Remove tool or visual group"},
    {"default", "Reset to default state"},
    {"ok", "OK"},
    {"cancel", "Cancel"},
    {"vgEditText", "Edit"},
    {"groupExists", "A group having this name already exists"},
    {"toolExists", "A tool having this name already exists"},
    {"up", "Move up"},
    {"down", "Move down"},
    {"group", "Group"},
    {"classNotFound1", "Class "},
    {"classNotFound2", " does not exist"},
    {"classDialogTitle", "Add tool"},
    {"className", "Tool class name"},
    {"button", "Button"},
    {"checkBox", "Check box"},
    {"comboBox", "Combo box"},
    {"radioButton", "Radio button"},
    {"slider", "Slider"},
    {"spinButton", "Spin button"},
    {"toggleButton", "Toggle button"},
    {"textField", "Text field"},
    {"properties", "Properties..."},
    {"toolPropDialogTitle", "Tool properties"},
    {"toolVisible", "Visible"},
    {"text", "Associated text:"},
    {"vGroupPropDialogTitle", "Visual group properties"},
    {"groupVisible", "Visible"},
    {"confirm", "Confirmation"},
    {"confirmDefault", "Are you sure that you want to reset the toolbar to its default state?"},
  };
}
