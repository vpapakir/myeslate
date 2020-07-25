package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class WebFileDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"RenameFile",      "Rename file"},
        {"RenameFileName",  "New name:"},
        {"RenameError",     "Error renaming file"},
        {"Error",           "Error"},
        {"NewDirName",      "Name:"},
        {"NewDir",          "New Directory"},
        {"ErrorNewDirMsg",  "The directory wasn't created"},
        {"ErrorNewDir",     "Error"},
        {"FilterDescr",     "Microworld files (*.mwd)"},
    };

}