package org.cbook.cbookif;

import java.util.Collection;

/**
 * Interface for registration of Widgets.
 * @author Wim van Velthoven
 *
 */
public interface CBookService {

	void registerWidget(CBookWidgetIF widget);
	Collection<CBookWidgetIF> getRegisteredWidgets();
	void unregisterWidget(CBookWidgetIF widget);
	CBookWidgetIF widgetForName(String string);

}
