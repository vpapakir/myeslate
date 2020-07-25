package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class IconPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Error",         "Error"},
        {"EditIcon",      "Edit image"},
        {"ClearIcon",     "Clear image"},
    };
}

