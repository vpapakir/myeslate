package fi.ltiwidget.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.cbook.cbookif.CBookLaunchData;

public class SampleLaunchData extends HashMap<String, String> implements CBookLaunchData {

	private String name;
	
	public SampleLaunchData(String resource, Locale locale)
	{
		ResourceBundle rb;
		rb = ResourceBundle.getBundle(resource, locale);
		Enumeration<String> keys = rb.getKeys();
		String key;
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			if(! "name".equals(key))
				put(key, rb.getString(key));	
		}
		name = rb.getString("name");
	}
	

	public Map<String, ?> getLaunchData() {
		Map<String, ?> map = this;
		return Collections.unmodifiableMap(map);
	}

	
	public String toString() { 
		return name;
	}
}
