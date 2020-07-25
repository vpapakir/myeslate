package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class IconChoosePanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Icon",              "Icon"},
        {"OpenIcon",          "Open icon"},
        {"EditIcon",          "Edit icon"},
        {"ClearIcon",         "Clear icon"},
        {"Error",             "Error"},
    };
}