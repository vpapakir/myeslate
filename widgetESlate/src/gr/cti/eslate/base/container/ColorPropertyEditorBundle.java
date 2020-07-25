package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ColorPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"SelectColor",         "Choose color"},
    };
}
