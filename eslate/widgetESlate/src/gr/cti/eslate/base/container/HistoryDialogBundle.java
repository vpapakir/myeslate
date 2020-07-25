package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class HistoryDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Microworld history"},
        {"Close",               "Close"},
    };
}
