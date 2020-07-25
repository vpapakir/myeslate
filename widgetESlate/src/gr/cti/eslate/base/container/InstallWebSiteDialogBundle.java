package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class InstallWebSiteDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Register web site"},
        {"OK",                  "OK"},
        {"Cancel",              "Cancel"},
        {"Error",               "Error"},
        {"Name",                "Web site name"},
        {"Address",             "Address"},
        {"CommonFolder",        "Common folder"},
        {"DialogMsg1",          "Malformed address \""},
        {"DialogMsg2",          "The site "},
        {"DialogMsg3",          " is not available"},
        {"DialogMsg4",          " does not exist"},
        {"DialogMsg5",          "Unable to connect to host \""},
        {"DialogMsg6",          "Folder \""},
        {"DialogMsg7",          " does not exist at site \""}
    };
}
