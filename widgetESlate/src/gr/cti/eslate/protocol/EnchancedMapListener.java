package gr.cti.eslate.protocol;

/**
 * This interface should be implemented by
 * every class that can show maps. It is a super interface of three
 * interfaces which helps its implementor that listener actions
 * will be handled automaticaly.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 23-Aug-1999
 * @see		gr.cti.eslate.protocol.IMapView
 * @see		gr.cti.eslate.protocol.IMapTreeViewer
 * @see		gr.cti.eslate.protocol.IEnchancedRegionViewer
 */
public interface EnchancedMapListener extends IMapTreeViewer, EnchancedRegionListener {
}
