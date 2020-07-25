package org.cbook.cbookif;

import java.util.Map;

/**
 * Initial launchdata, for use in widgeteditor and/of widgetinstance
 * @author velth101
 *
 */
public interface CBookLaunchData {
	/**
	 * Return a string to display in a list/combobox.
	 * The result should react on the locale setting.
	 * @return the string
	 */
	String toString();
	
	/**
	 * Return the actual launchdata.
	 * @return a map
	 */
	Map<String,?> getLaunchData();
	
}
