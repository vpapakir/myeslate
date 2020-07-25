package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ComponentLocationDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",       "Component location"},
        {"SupplyPosition",    "Specify relative component location"},
        {"Before",            "Before"},
        {"After",             "After"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
    };
}
