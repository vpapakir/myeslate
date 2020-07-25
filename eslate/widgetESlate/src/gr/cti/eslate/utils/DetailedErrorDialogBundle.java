package gr.cti.eslate.utils;

import java.util.ListResourceBundle;


public class DetailedErrorDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Error",             "Error"},
        {"OK",                "OK"},
        {"ShowDetails",       "Show details"},
        {"HideDetails",       "Hide details"},
        {"CopyTip",           "Copy"},
        {"HideTip",           "Hide details"},
        {"ShowTip",           "Show details"},
    };
}
