package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class JavaOutputConsoleDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Consoles"},
        {"Close",               "Close"},
        {"Clear",               "Clear"},
        {"FileDialogTitle",     "Save console"},
        {"Error",               "Error"},
        {"UnableToSave",        "Unable to save console to file \""},
        {"Find",                "Find"},
        {"FindWhat",            "Find in console"},
        {"UnableToFind1",       "Search string \""},
        {"UnableToFind2",       "\" not found."},
        {"UnableToFind3",       "No more occurences of \""},
        {"GoTo",                "Go to line"},
        {"GoToLine",            "Line number to go to:"},
        {"BadLineNumber1",      "Bad line number"},
        {"BadLineNumber2",      ""},
        {"BadLineNumber3",      " contains "},
        {"BadLineNumber4",      " lines only"},
        {"CopyTip",             "Copy"},
        {"SaveTip",             "Save"},
        {"FindTip",             "Find"},
        {"FindNextTip",         "Find Next"},
        {"FindPrevTip",         "Find Previous"},
        {"GoToLineTip",         "Go to Line"},
        {"MemoryTip",           "Memory Report"},
        {"GCTip",               "Collect Garbage"},
        {"InfoTip",             "Java VM information"},
        {"ClearTip",            "Clear Console"},
        {"Java console",        "Java console"},
        {"Javascript console",  "Javascript console"},
        {"Logo console",        "Logo console"},
        {"MatchCase",           "Match case"},
    };
}

