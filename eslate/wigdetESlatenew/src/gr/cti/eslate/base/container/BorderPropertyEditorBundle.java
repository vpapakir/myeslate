package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class BorderPropertyEditorBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"EditBorder",      "Change border"},
        {"NoBorder",        "No border"},
        {"UnknownBorder",   "Unknown border"},
    };
}

