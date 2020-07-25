package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.MapListener;

/**
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 23-Aug-1999
 * @see		gr.cti.eslate.protocol.EnchancedMapListener
 */
public interface IMapTreeViewer {
	/**
	 * This method gets the MapListener object.
	 */
	public abstract MapListener getMapListener();
}
