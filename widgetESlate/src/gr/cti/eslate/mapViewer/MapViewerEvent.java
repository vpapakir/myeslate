package gr.cti.eslate.mapViewer;

/**
 * This is the general map viewer event.
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 8-Feb-2001
 */
public class MapViewerEvent extends java.util.EventObject {
	public static final int MAP_VIEWER_SCALE_CHANGED=1000;
	public static final int MAP_VIEWER_BUSY=1001;

	int id;
	Object change;

	public MapViewerEvent(Object source,int id) {
		super(source);
		this.id=id;
	}

	public MapViewerEvent(Object source,int id,Object change) {
		super(source);
		this.id=id;
		this.change=change;
	}
	/**
	 * The ID of the event. The event type.
	 */
	public int getID() {
		return id;
	}
	/**
	 * Gets the change that fired the event.
	 */
	public Object getChange() {
		return change;
	}
}
