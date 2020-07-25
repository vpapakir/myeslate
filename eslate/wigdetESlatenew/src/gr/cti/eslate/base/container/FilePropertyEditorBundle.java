package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class FilePropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Title",           "Open file"},
        {"Open",            "Open"},
        {"Msg1",            "File \""},
        {"Msg2",            "does not exist"},
        {"Error",           "Error"},
    };
}
