package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class PasswordDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"ProtectTitle",      "Protect microworld"},
        {"UnprotectTitle",    "Unlock microworld"},
        {"EnterPassword",     "Enter"}, // password"},
        {"ConfirmPassword",   "Confirm"}, // password"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
        {"ProtectMicroworld", "Microworld settings password"},
        {"Password",          "Password"},
    };
}