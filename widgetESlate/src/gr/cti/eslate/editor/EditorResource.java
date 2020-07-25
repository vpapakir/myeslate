package gr.cti.eslate.editor;

import java.util.*;

import org.netbeans.editor.*;

/**
 * English language localized strings for the editor panel.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class EditorResource extends ListResourceBundle
  implements LocaleSupport.Localizer
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"file", "File"},
    {"new", "New..."},
    {"open", "Open File..."},
    {"close", "Close"},
    {"save", "Save"},
    {"saveAs", "Save As..."},
    {"saveAll", "Save All"},
    {"print", "Print..."},
    {"exit", "Exit"},
    {"newTip", "New file"},
    {"openTip", "Open file"},
    {"closeTip", "Close file"},
    {"saveTip", "Save file"},
    {"saveAllTip", "Save all files"},
    {"printTip", "Print document"},
    {"copyTip", "Copy text"},
    {"pasteTip", "Paste text"},
    {"cutTip", "Cut text"},
    {"undoTip", "Undo"},
    {"redoTip", "Redo"},
    {"findTip", "Find"},
    {"findNextTip", "Find next"},
    {"replaceTip", "Replace"},
    {"fileExists1", "File "},
    {"fileExists2", " already exists. Overwrite?"},
    {"fileExists3", "File exists"},
    {"cantWrite1", "Can't write to file '"},
    {"cantWrite2", "'."},
    {"error", "Error"},
    {"cantRead1", "Can't read from file '"},
    {"cantRead2", "'."},
    {"fileModified1", "File "},
    {"fileModified2", " was modified. Save it?"},
    {"fileModified3", "File modified"},
    {"discard", "Discard"},
    {"yes", "yes"},
    {"no", "no"},
    {"cancel", "Cancel"},
    {"noTemplate", "Can't read template"},
    {"eSlateEd", "Editor"},
    {"noFile", "[no file]"},
    {"allRecognized", "All recognized files"},
    {"edit", "Edit"},
    {"undo", "Undo"},
    {"redo", "Redo"},
    {"cut", "Cut"},
    {"copy", "Copy"},
    {"paste", "Paste"},
    {"delete", "Delete"},
    {"selectAll", "Select All"},
    {"search", "Search"},
    {"find", "Find..."},
    {"replace", "Replace..."},
    {"searchAgain", "Search Again..."},
    {"gotoLine", "Go to Line..."},
    {"fontSize", "Font size"},
    {"options", "Options"},
    {"configuration", "Configuration..."},
    {"configTitle", "Editor options"},
    {"noFileEdited", "No file is being edited"},
    {"noSuchLine", "Line does not exist"},

    {"plain", "Plain text"},
    {"java", "Java code"},

    {"line-number", "Line Number"},
    {"status-bar-bold", "Search Wrapped"},
    {"inc-search", "Incremental Search"},
    {"highlight-match-brace", "Highlighted Matching Brace"},
    {"highlight-caret-row", "Highlighted Row"},
    {"bookmark", "Bookmark"},
    {"default", "Default"},
    {"status-bar", "Status Bar"},
    {"selection", "Selected Text"},
    {"guarded", "Guarded Block"},
    {"highlight-search", "Highlighted By Search"},

    {"java-layer-method", "Java Layer Method"},
    {"java-string-literal", "String Literal"},
    {"java-whitespace", "White space"},
    {"java-numeric-literals", "Numeric Literals"},
    {"java-errors", "Error in Sources"},
    {"java-identifier", "Java Identifier"},
    {"java-operators", "Java Operator"},
    {"java-block-comment", "Block Comment"},
    {"java-keywords", "Java Keyword"},
    {"java-char-literal", "Character Literal"},
    {"java-line-comment", "Single-line Comment"},

    {"font", "Font"},
    {"sample", "Sample"},
    {"abcd", "ABCDEFGHabcdefgh"},
    {"bold", "Bold"},
    {"italic", "Italic"},
    {"fgColor", "Color"},
    {"bgColor", "Background color"},
    //
    // BeanInfo resources
    //
    {"menuBarVisible", "Menu bar visible"},
    {"menuBarVisibleTip", "Specify whether the menu bar should be visible"},
    {"toolBarVisible", "Tool bar visible"},
    {"toolBarVisibleTip", "Specify whether the tool bar should be visible"},
    {"statusBarVisible", "Status bar visible"},
    {"statusBarVisibleTip", "Specify whether the status bar should be visible"},
    {"propertyChange", "Property change"},
    {"mouseEntered", "Mouse entered"},
    {"mouseExited", "Mouse exited"},
    {"mouseMoved", "Mouse moved"},
    {"vetoableChange", "Vetoable change"},
    {"componentHidden", "Component hidden"},
    {"componentShown", "Component shown"},
    {"multipleEditor", "Edit multiple files"},
    {"multipleEditorTip", "Specify whether the editor can edit may files simultaneously"},
    //
    // NetBeans resources--Defaults are OK.
    //
    // These netbeans resources do not actually exist, and are faked.
    {"find-highlight-search", "Highlight Search"},
    {"find-inc-search", "Incremental Search"},
    {"find-match-case", "Match Case"},
    {"find-smart-case", "Smart Case"},
    {"find-whole-words", "Match Whole Words Only"},
    {"find-backward-search", "Backward Search"},
    {"find-wrap search", "Wrap Search"},
  };
}
