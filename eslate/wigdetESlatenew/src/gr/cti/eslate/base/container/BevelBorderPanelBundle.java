package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class BevelBorderPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Lowered",             "Lowered"},
        {"Raised",              "Raised"},
        {"HighlightOuterColor", "Outer highlight color"},
        {"HighlightInnerColor", "Inner highlight color"},
        {"ShadowOuterColor",    "Outer shadow color"},
        {"ShadowInnerColor",    "Inner shadow color"},
    };
}