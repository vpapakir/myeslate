package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class FontDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Choose font"},
        {"Font",                "Font"},
        {"Size",                "Size"},
        {"Demo",                "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqSsTtUuVvWwXxYyZz"},
        {"Close",               "Close"}
    };
}

