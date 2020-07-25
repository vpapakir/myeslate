package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

/**
 * MapBeanInfo Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 05-Apr-2000
 */
public class BundleMapBeanInfo extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"name",                "Name"},
        {"nametip",             "The name of the map"},
        {"menubarVisible",      "Menubar visible"},
        {"menubarVisibletip",   "Controls the visibility of the menubar"},
        {"border",              "Border"},
        {"bordertip",           "Controls the component border"},
        {"root",                "Root Region"},
    };
}
