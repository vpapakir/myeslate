package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * @version     1.0.6, 17-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BarChartPrimitivesBundle extends ListResourceBundle {
	private static ResourceBundle instance;
	
	protected Object[][] getContents() {
		return BarChartPrimitivesBundle_en.contents;
	}

	static String getMessage(String key) {
		if (instance == null)
			instance = ResourceBundle.getBundle(BarChartPrimitivesBundle.class.getName());
		return instance.getString(key);
	}
	
	static ResourceBundle getResourceBundle() {
		if (instance == null)
			instance = ResourceBundle.getBundle(BarChartPrimitivesBundle.class.getName());
		return instance;
	}
}
