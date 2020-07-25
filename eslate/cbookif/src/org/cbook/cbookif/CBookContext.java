package org.cbook.cbookif;

/**
 * Access to the outerworld of a widget instance or editor.
 * @author Wim van Velthoven
 *
 */
public interface CBookContext {
	/**
	 * Get a context property.
	 * @see Constants#FONT
	 * @see Constants#BACKGROUND
	 * @see Constants#FOREGROUND
	 * @see Constants#LOGGING
	 * @see Constants#LOG_ID
	 * @param key name of the property
	 * @return various types
	 */
	Object getProperty(String key);
	
}
