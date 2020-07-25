package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class PageSetupPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",   "Page setup"},
        {"Top",           "Top"},
        {"Bottom",        "Bottom"},
        {"Left",          "Left"},
        {"Right",         "Right"},
        {"Margins",       "Margins"},
        {"cm",            "cm"},
        {"Center",        "Center on page"},
        {"Fit",           "Fit to page"},
        {"Scale",         "Scale"},
        {"Position",      "Position"},
        {"OK",            "OK"},
        {"Cancel",        "Cancel"},
    };
}
