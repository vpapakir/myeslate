package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class FTPDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Share documents"},
        {"Close",               "Close"},
        {"Dir",                 "Directory"},
        {"UploadFile",          "Upload file to remote system"},
        {"DownloadFile",        "Download file from remote system"},
        {"LocalSystem",         "Local System"},
        {"RemoteSystem",        "Remote System"}
    };
}
