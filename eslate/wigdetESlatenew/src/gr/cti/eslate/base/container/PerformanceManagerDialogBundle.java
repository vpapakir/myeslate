package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class PerformanceManagerDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Title",             "Performance monitor"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
        {"Close",             "Close"},
    };
}
