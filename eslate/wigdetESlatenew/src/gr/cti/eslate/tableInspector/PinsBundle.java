package gr.cti.eslate.tableInspector;

import java.util.ListResourceBundle;

public class PinsBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"table",       "Table"},
        {"url",         "Hyperlink"},
        {"inputrecord", "Current record"},
        {"of",          "of"},
        {"current",     "ID"},
    };
}
