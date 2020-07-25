package org.cbook.cbookif;

import java.util.Collection;

/**
 * Turn a CBook Widget into a factory by implementing 1 extra method.
 * @author velth101
 *
 */
public interface CBookWidgetFactoryIF extends CBookWidgetIF {
	/**
	 * Return a number of CBookLaunchData in some convenient form.
	 * The collection is read-only.
	 * @param context 
	 * @return some number of launchdata 
	 */
	Collection<CBookLaunchData> getInitialLaunchData(CBookContext context);

}
