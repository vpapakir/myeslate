package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class FontPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Bold",         "Bold"},
        {"Italic",       "Italic"},
    };
}
