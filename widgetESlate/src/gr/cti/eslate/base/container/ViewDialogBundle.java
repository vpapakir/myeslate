package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ViewDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Title",             "View Editor"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
        {"New",               "New"},
        {"NewTip",            "Define a new view"},
        {"Replace",           "Update"},
        {"ReplaceTip",        "Update the selected view"},
        {"Rename",            "Rename"},
        {"RenameTip",         "Rename the selected view"},
        {"Activate",          "Activate"},
        {"ActivateTip",       "Activate the selected view"},
        {"Remove",            "Remove"},
        {"ActivateTip",       "Delete the selected view from the list of microworld views"},
        {"View",              "View"},
        {"Msg1",              "Enter the new name of the view"},
        {"Msg2",              "View Title"},
        {"Msg3",              "The view can not be renamed, because there already exists a view with named "},
        {"Error",             "Error"},
    };
}

