package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class CustomizerDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"OK",                "OK"},
    };
}