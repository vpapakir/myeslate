package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LogoOutputConsoleDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",         "Logo console"},
        {"Close",               "Close"},
        {"Clear",               "Clear"},
    };
}

