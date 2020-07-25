package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LogoScriptDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle1",    "Script for event \""},
        {"DialogTitle2",    " of component \""},
        {"Apply",           "Apply"},
        {"Close",           "Close"},
        {"Remove",          "Remove"},
        {"Error",           "Error"},
        {"ScriptName",      "Script name"},
        {"CompileFailureMessage", "Compilation resulted to unexpected errors. Probably the classpath is not set correctly"},
        {"CompileFailureMessage2","Unable to compile. Cannot locate the \"jikes\" compiler"},
        {"CompileFailureMessage3","Unable to define the action for the event"},
        {"CompileDenialMsg1",  "Component \""},
        {"CompileDenialMsg2",  "\" no longer exists in the microworld. Compilation is abandoned."},
        {"CompileDenialMsg3",  "\" no longer exists in the microworld."},
        {"AvailableVariables",  "Variables"},
        {"CutTip",              "Cut"},
        {"CopyTip",             "Copy"},
        {"PasteTip",            "Paste"},
        {"LoadTip",             "Load script"},
        {"SaveTip",             "Save script"},
        {"FindTip",             "Find"},
        {"FindNextTip",         "Fine next"},
        {"FindPrevTip",         "Find previous"},
        {"GoToLineTip",         "Go to line"},
        {"CompileTip",          "Compile"},
        {"ClearTip",            "Clear"},
        {"FileDialogTitle",     "Save script"},
        {"OpenFileDialogTitle", "Load script"},
        {"Error",               "Error"},
        {"UnableToSave",        "Unable to save script to file \""},
        {"UnableToLoad",        "Unable to load script from file \""},
        {"Find",                "Find"},
        {"FindWhat",            "Find in script"},
        {"UnableToFind1",       "Search string \""},
        {"UnableToFind2",       "\" not found."},
        {"UnableToFind3",       "No more occurences of \""},
        {"GoTo",                "Go to line"},
        {"GoToLine",            "Line number to go to:"},
        {"BadLineNumber1",      "Bad line number"},
        {"BadLineNumber2",      "This script contains "},
        {"BadLineNumber3",      " lines only"},
        {"LineCount",           "Line count: "},
        {"SaveChanges",         "Save changes?"},
        {"ScriptChanged",       "The script has been edited. Do you want to save changes before closing?"},
        {"Yes",                 "Yes"},
        {"No",                  "No"},
        {"Cancel",              "Cancel"},
        {"NoScript",            "No script"},
        {"Modified",            "Modified"},
    };
}

