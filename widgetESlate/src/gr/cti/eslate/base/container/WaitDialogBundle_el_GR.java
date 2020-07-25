package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class WaitDialogBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"LoadTitle",       "Ανάγνωση μικρόκοσμου. Παρακαλώ περιμένετε..."},
        {"SaveTitle",       "Αποθήκευση μικρόκοσμου. Παρακαλώ περιμένετε..."},
        {"Loading",         "Ανάγνωση "},
        {"Saving",          "Αποθήκευση "},
////nikosM new
        {"DownLoadTitle",    "Μεταφορά"},
        {"ClosingConnection","Έλεγχος σωστής αποστολής. Παρακαλώ περιμένετε..."},
        {"DownLoadMsg1",    " από "},
        {"DownLoadMsg2",    " bytes στάλθηκαν"},
////nikosM new end
    };
}
