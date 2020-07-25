package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ProfileDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Title",               "Select settings profile"},
        {"Rename",              "Rename"},
        {"Remove",              "Remove"},
        {"Import",              "Import"},
        {"Export",              "Export"},
        {"RenameTip",           "Rename selected profile"},
        {"RemoveTip",           "Remove selected profile"},
        {"ImportTip",           "Import new profile from file"},
        {"ExportTip",           "Export selected profile to file"},
        {"ListTip",             "Profile list"},
        {"OK",                  "OK"},
        {"Cancel",              "Cancel"},
        {"Runtime Profile",     "Runtime profile"},
        {"Design-time Profile", "Design time profile"},
        {"Current Profile",     "Current profile"},
        {"RenameTitle",         "Rename profile"},
        {"RenameMsg",           "Enter new profile name"},
        {"SaveProfileTitle",    "Export profile"},
        {"UnableToStoreSettings", "Failed to export profile to file \""},
        {"Error",                 "Error"},
        {"NameMsg",               "Enter the name for the new profile"},
        {"MameTitle",             "Exported profile name"},
        {"PromptRename",          "Do you want to change the name of the exported profile \""},
        {"PromptRenameTitle",     "Rename exported profile"},
        {"QuestionMark",          "?"},
        {"LoadProfileTitle",      "Load profile"},
        {"UnableToLoadSettings",  "Failed to import profile from file\""},
    };
}