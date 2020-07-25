package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ColorChoosePanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Color",                 "Color"},
        {"ChooseColor",           "Choose color"},
    };
}