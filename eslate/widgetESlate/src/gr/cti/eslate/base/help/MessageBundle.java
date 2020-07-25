package gr.cti.eslate.base.help;

import java.util.ListResourceBundle;

public class MessageBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
            {"waitDialogTitle", "Starting help"},
        {"waitLabel"    ,   "Please wait for the help system to load..."},
        {"helpTitle"    ,   "Microworld's Help"},
        {"TOC"          ,   "Table of Contents "},
        {"Index"        ,   "Index "},
        {"Search"       ,   "Search "},
        {"Back"         ,   "Back "},
        {"Next"         ,   "Forward "},
        {"dialogTitle"  ,   "Find a word in the document "},
        {"findTip"      ,   "Find Next Word "},
        {"cancelTip"    ,   "Cancel Finding "},
        {"find"         ,   "Find "},
        {"cancel"       ,   "Cancel "},
                {"print"                ,       "Print Current Document "},

        };
}
