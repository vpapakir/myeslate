package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class PointPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"X",         "X"},
        {"Y",         "Y"},
    };
}
