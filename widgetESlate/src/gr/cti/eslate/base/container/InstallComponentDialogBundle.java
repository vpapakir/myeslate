package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class InstallComponentDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Install component"},
        {"DialogTitle2",        "Install look and feel"},
        {"OK",                  "OK"},
        {"Cancel",              "Cancel"},
        {"Error",               "Error"},
        {"Name",                "Component name"},
        {"Name2",               "Look and Feel name"},
        {"Class",               "Class"},
        {"Class2",              "Class"},
        {"DialogMsg1",          "Class "},
        {"DialogMsg2",          " can not be located"},
        {"DialogMsg3",          "Only visible components can be instantiated"},
        {"DialogMsg4",          " cannot be instantiated"},
        {"DialogMsg5",          " cannot be instantiated, because it does not have a puclic zero-argument constuctor"},
        {"DialogMsg6",          " cannot be instantiated, because it is not public"},
        {"DialogMsg7",          "Problem while registering component: "}
    };
}
