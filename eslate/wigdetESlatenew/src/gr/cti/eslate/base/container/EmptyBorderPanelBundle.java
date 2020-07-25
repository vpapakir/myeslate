package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class EmptyBorderPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Top",             "Top"},
        {"Bottom",          "Bottom"},
        {"Left",            "Left"},
        {"Right",           "Right"},
    };
}