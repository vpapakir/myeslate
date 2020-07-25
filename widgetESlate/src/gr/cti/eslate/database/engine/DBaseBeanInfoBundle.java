package gr.cti.eslate.database.engine;

import java.util.ListResourceBundle;


public class DBaseBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Title",               "Title"},
        {"TitleTip",            "Database title"},
        {"ActiveTableChanged",  "Active table changed"},
        {"DatabaseRenamed",     "Database title changed"},
        {"TableAdded",          "Table added to database"},
        {"TableRemoved",        "Table removed from database"},
        {"TableReplaced",       "Table replaced in database"},
    };
}
