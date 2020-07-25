package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.RegionListener;

/**
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 23-Aug-1999
 * @see		gr.cti.eslate.protocol.EnchancedMapListener
 */
public interface IRegionViewer {
	/**
	 * This method gets the RegionListener object.
	 */
	public abstract RegionListener getRegionListener();
}
