package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class MetadataBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
/*        {"Geography",             "Geography"},
        {"Geometry",              "Geometry"},
        {"History",               "History"},
        {"Mathematics",           "Mathematics"},
        {"Physics",               "Physics"},
        {"CrossSubject",          "Cross - Subject"},*/
        {"Title",                 "Title"},
        {"Subject",               "Subject"},
        {"Company",               "Company"},
        {"Category",              "Category"},
        {"Keyword",               "Keyword"},
        {"Comment",               "Comment"},
        {"Author",                "Authors"},
        {"AddAuthor",             "Add"},
        {"RemoveAuthor",          "Remove"},
        {"Lock",                  "Lock"},
        {"LockLabel",             "Protection"},
        {"Unlock",                "Unlock"},
        {"MicoworldInfo",         "Microworld information"},
        {"TitleTip",              "Microworld title"},
        {"SubjectTip",            "Subject of the microworld"},
        {"CompanyTip",            "Company"},
        {"CategoryTip",           "Microworld category"},
        {"KeywordTip",            "Useful keywords related to the microworld"},
        {"CommentTip",            "Author comments for the microworld"},
        {"AuthorTip",             "Microworld authors"},
        {"LockTip",               "Password protect the settings of the microworld"},
        {"UnlockTip",             "Unlock the microworld settings to edit"},
        {"AddAuthorTip",          "Add new author"},
        {"RemoveAuthorTip",       "Remove selected author"},
        {"InvalidConfirmation",   "The passwords don't match"},
        {"InvalidPassword",       "Wrong password!"},
    };
}