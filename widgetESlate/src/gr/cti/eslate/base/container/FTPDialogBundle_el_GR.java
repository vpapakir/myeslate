package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class FTPDialogBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Ανταλλαγή αρχείων"},
        {"Close",               "Κλείσιμο"},
        {"Dir",                 "Κατάλογος"},
        {"UploadFile",          "Μεταφορά αρχείου στον απομακρυσμένο υπολογιστή"},
        {"DownloadFile",        "Μεταφορά αρχείου στον τοπικό υπολογιστή"},
        {"LocalSystem",         "Τοπικός υπολογιστής"},
        {"RemoteSystem",        "Απομακρυσμένος υπολογιστής"}
    };
}
