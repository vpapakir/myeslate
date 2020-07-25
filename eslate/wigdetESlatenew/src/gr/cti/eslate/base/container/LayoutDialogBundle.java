package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LayoutDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",      "Edit layout"},
        {"HGap",              "Horizontal gap"},
        {"VGap",              "Vertical gap"},
        {"Rows",              "Rows"},
        {"Columns",           "Columns"},
        {"XAXIS",             "X Axis"},
        {"YAXIS",             "Y Axis"},
        {"CENTER",            "Center"},
        {"LEFT",              "Left"},
        {"RIGHT",             "Right"},
        {"NextCard",          "Next card"},
        {"PreviousCard",      "Previous card"},
        {"Axis",              "Axis"},
        {"Alignment",         "Alignment"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
        {"Apply",             "Apply"},
    };
}
