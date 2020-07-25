package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LayerInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Default",       "Default"},
    };
}
