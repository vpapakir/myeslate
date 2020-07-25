package fi.ltiwidget;

import java.util.Collections;
import java.util.Map;

import org.cbook.cbookif.CBookLaunchData;

public class LTILaunchData implements CBookLaunchData {

	Map<String,? > data = Collections.emptyMap();
	String name = "Empty LTI widget";
	
	@Override
	public Map<String, ?> getLaunchData() {
		return data;
	}

	public String toString() {
		return name;
	}
	
}
