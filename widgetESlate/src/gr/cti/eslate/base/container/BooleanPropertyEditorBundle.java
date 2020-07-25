package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class BooleanPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"True",         "True"},
        {"False",        "False"},
    };
}