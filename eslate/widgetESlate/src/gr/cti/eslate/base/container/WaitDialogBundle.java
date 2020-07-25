package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class WaitDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"LoadTitle",       "Loading microworld. Please wait..."},
        {"SaveTitle",       "Saving microworld. Please wait..."},
        {"Loading",         "Loading "},
        {"Saving",          "Saving "},
////nikosM new
        {"DownLoadTitle",    "Downloading"},
        {"ClosingConnection","Testing the transferred file. Please wait..."},
        {"DownLoadMsg1",    " of "},
        {"DownLoadMsg2",    "bytes sent"},
////nikosM new end
    };
}