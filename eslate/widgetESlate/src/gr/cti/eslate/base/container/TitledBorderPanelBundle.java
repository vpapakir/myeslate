package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class TitledBorderPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Left",                "LEFT"},
        {"Center",              "CENTER"},
        {"Right",               "RIGHT"},
        {"AboveTop",            "ABOVE TOP"},
        {"Top",                 "TOP"},
        {"BelowTop",            "BELOW TOP"},
        {"AboveBottom",         "ABOVE BOTTOM"},
        {"Bottom",              "BOTTOM"},
        {"BelowBottom",         "BELOW BOTTOM"},
        {"Title",               "Title"},
        {"Justification",       "Justification"},
        {"Position",            "Position"},
        {"Font",                "Font"},
        {"ChooseFont",          "Choose font"},
    };
}
