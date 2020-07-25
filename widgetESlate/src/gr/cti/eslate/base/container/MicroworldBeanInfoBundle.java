package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class MicroworldBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"microworldLoaded",                "Microworld loaded"},
        {"microworldClosing",               "Microworld closing"},
        {"componentActivated",              "Component activated"},
        {"componentDeactivated",            "Component deactivated"},
        {"componentIconified",              "Component iconified"},
        {"componentRestored",               "Component restored"},
        {"componentMaximized",              "Component maximized"},
        {"componentClosed",                 "Component closed"},
        {"componentClosing",                "Component closing"},
        {"activeViewChanged",               "View activated"},
        {"MicroworldLAFClassName",          "Microworld L&F"},
        {"MicroworldLAFClassNameTip",       "Class name of the microworld's L&F"},
        {"StoreSkinOnAPerViewBasis",        "Store skins per view"},
        {"StoreSkinOnAPerViewBasisTip",     "Different skins for components per microworld view"},
        {"MwdLoadProgressMsg",              "Load dialog title"},
        {"MwdLoadProgressMsgTip",           "Title of the microworld load progress dialog"},
        {"MwdLoadProgressInfoDisplayed",    "Display information while loading"},
        {"MwdLoadProgressInfoDisplayedTip", "Display details about microworld loading"},
        {"MwdSaveProgressMsg",              "Save dialog title"},
        {"MwdSaveProgressMsgTip",           "Title of the microworld save progress dialog"},
        {"MwdSaveProgressInfoDisplayed",    "Display information while saving"},
        {"MwdSaveProgressInfoDisplayedTip", "Display details about microworld saving"},
        {"ProgressDialogTitleFont",         "Load/Save dialog title font"},
        {"ProgressDialogTitleFontTip",      "Title font of the microworld load/save progress dialog"},
        {"ProgressDialogTitleColor",        "Load/Save dialog title foreground color"},
        {"ProgressDialogTitleColorTip",     "Title color of the microworld load/save progress dialog"},
    };
}
