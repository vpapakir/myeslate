package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class RectanglePropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"X",             "X"},
        {"Y",             "Y"},
        {"Width",         "Width"},
        {"Height",        "Height"},
    };
}
