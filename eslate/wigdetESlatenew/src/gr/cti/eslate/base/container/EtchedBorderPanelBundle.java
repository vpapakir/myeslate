package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class EtchedBorderPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Lowered",             "Lowered"},
        {"Raised",              "Raised"},
        {"HighlightColor",      "Highlight color"},
        {"ShadowColor",         "Shadow color"},
    };
}