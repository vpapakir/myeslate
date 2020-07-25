package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

public class PlugBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"mapviewer",   "Browse a map"},
        {"agent",       "Host an agent"},
    };
}
