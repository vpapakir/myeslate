package gr.cti.eslate.eslateToolBar;

import java.util.*;

/**
 * English language localized strings for the visual group editor of the
 * E-Slate toolbar.
 *
 * @author      Kriton kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.eslateToolBar.ButtonGroupEditor
 */
public class ButtonGroupEditorResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"bgDialogTitle", "Button group layout"},
    {"root", "Button groups"},
    {"error", "Error"},
    {"addGroup", "Add button group"},
    {"remTool", "Remove tool from group"},
    {"remove", "Remove button group"},
    {"ok", "OK"},
    {"cancel", "Cancel"},
    {"bgEditText", "Edit"},
    {"groupExists", "A group having this name already exists"},
    {"toolExists", "A tool having this name already exists"},
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
    {"bGroupPropDialogTitle", "Button group properties"},
    {"groupVisible", "Visible"},
  };
}
