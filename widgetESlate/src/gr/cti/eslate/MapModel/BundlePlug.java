package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

public class BundlePlug extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"map",         "Map (View "},
        {"database",    "Database"},
        {"record",      "Current record"},
        {"of",          "of"},
        {"timemachine", "Time machine"},
    };
}
