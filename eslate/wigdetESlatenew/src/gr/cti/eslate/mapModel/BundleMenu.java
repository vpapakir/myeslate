package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

public class BundleMenu extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        //File
        {"file",        "File"},
        {"new",         "New..."},
        {"open",        "Open..."},
        {"edit",        "Edit.."},
        {"save",        "Save"},
        {"saveas",      "Save as..."},
        {"savetitle",   "Save Map"},
        {"opentitle",   "Load Map"},
        {"close",       "Close"}
    };
}
