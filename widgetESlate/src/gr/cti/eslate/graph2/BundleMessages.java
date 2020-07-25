package gr.cti.eslate.graph2;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * @author augril
 */
public class BundleMessages extends ListResourceBundle {
	private static ResourceBundle instance;
	
	protected Object[][] getContents() {
		return BundleMessages_en.contents;
	}

	static String getMessage(String key) {
		if (instance == null)
			instance = ResourceBundle.getBundle(BundleMessages.class.getName());
		return instance.getString(key);
	}
	
	static ResourceBundle getResourceBundle() {
		if (instance == null)
			instance = ResourceBundle.getBundle(BundleMessages.class.getName());
		return instance;
	}
}
