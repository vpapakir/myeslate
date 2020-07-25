package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class AuthorDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Title",             "Microworld author"},
        {"Name",              "Name"},
        {"Surname",           "Surname"},
        {"E-Mail",            "E-mail"},
        {"NameTip",           "Author first name"},
        {"SurnameTip",        "Author surname"},
        {"E-MailTip",         "Author's e-mail"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
        {"AuthorInfo",        "Author information"},
    };
}
