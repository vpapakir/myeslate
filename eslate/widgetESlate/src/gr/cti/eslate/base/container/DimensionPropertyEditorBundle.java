package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class DimensionPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Width",         "Width"},
        {"Height",        "Height"},
    };
}
