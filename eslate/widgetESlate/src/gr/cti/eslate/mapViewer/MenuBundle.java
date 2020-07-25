package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

/**
 * Menu Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class MenuBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"file",            "File"},
        {"new",             "New map..."},
        {"save",            "Save"},
        {"manage",          "Management"},
        {"clearhistory",    "Clear map history"}
	};
}
