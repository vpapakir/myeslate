package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ErrorDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",     "Unable to attach script \""},
        {"OK",              "OK"},
    };
}

