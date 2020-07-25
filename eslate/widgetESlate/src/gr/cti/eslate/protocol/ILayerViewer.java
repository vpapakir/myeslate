package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.LayerListener;

/**
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 23-Aug-1999
 * @see		gr.cti.eslate.protocol.EnchancedMapListener
 */
public interface ILayerViewer {
	/**
	 * This method gets the LayerListener object.
	 */
	public abstract LayerListener getLayerListener();
}
