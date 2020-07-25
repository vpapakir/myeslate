package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

/**
 * Menu Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class MenuBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"file",            "Αρχείο"},
        {"new",             "Νέος χάρτης..."},
        {"save",            "Αποθήκευση"},
        {"manage",          "Διαχείριση"},
        {"clearhistory",    "Καθαρισμός ιστορικού χαρτών"}
	};
}
