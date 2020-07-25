package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * @author augril
 */
public class Graph2PrimitivesBundle extends ListResourceBundle {
	private static ResourceBundle instance;
	
	protected Object[][] getContents() {
		return Graph2PrimitivesBundle_en.contents;
	}

	static String getMessage(String key) {
		if (instance == null)
			instance = ResourceBundle.getBundle(Graph2PrimitivesBundle.class.getName());
		return instance.getString(key);
	}
	
	static ResourceBundle getResourceBundle() {
		if (instance == null)
			instance = ResourceBundle.getBundle(Graph2PrimitivesBundle.class.getName());
		return instance;
	}
}
