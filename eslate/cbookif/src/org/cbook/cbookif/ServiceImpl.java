package org.cbook.cbookif;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of a CBookService. The widgets are sorted by name.
 * @author Wim van Velthoven
 *
 */
public class ServiceImpl implements CBookService, Comparator<CBookWidgetIF> {

	private Set<CBookWidgetIF> list = new TreeSet<CBookWidgetIF>(this);

	public void registerWidget(CBookWidgetIF widget) {
		list.add(widget);		
	}
	
	public Collection<CBookWidgetIF> getRegisteredWidgets() {
		return list;
	}

	public void unregisterWidget(CBookWidgetIF widget) {
		list.remove(widget);
	}

	public int compare(CBookWidgetIF a, CBookWidgetIF b) {
		return a.toString().compareToIgnoreCase(b.toString());
	}

	public CBookWidgetIF widgetForName(String clazz) {
		for( CBookWidgetIF w: list) {
			if(w.getClass().getName().equals(clazz))
				return w;
		}
		return null;
	}

}
