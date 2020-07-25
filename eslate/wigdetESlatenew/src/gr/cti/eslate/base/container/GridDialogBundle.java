package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class GridDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Grid"},
        {"GridSettings",        "Grid settings"},
        {"ShowGrid",            "Grid visible"},
        {"SnapToGrid",          "Snap to grid"},
        {"GridStep",            "Grid step"},
        {"GridColor",           "Grid color"},
        {"OK",                  "OK"},
        {"Cancel",              "Cancel"},
        {"Apply",               "Apply"},
        {"BadNumber",           "Please provide a valid grid step"},
        {"Error",               "Error"},
    };
}
