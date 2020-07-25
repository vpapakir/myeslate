package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class InsetsPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Top",           "Top"},
        {"Bottom",        "Bottom"},
        {"Right",         "Right"},
        {"Left",          "Left"},
    };
}