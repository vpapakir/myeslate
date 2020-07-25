package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class AddComponentDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",       "Add component"},
        {"ComponentName",     "Component class name"},
        {"CardName",          "Card name"},
        {"Orientation",       "Orientation"},
        {"Index",             "Component position"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
    };
}
