package gr.cti.eslate.mapModel;

import java.awt.AWTEvent;

/**
 * This is the general map event. This can be used for simply
 * informing (use the first constructor) as well as providing
 * the old and new values (use the second constructor) of a
 * property which has been changed.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 6-Aug-1999
 * @see		gr.cti.eslate.mapModel.MapListener
 * @see		gr.cti.eslate.mapModel.MapAdapter
 */
public class MapEvent extends AWTEvent {
	public static final int MAP_FIRST=AWTEvent.RESERVED_ID_MAX+1;
	public static final int MAP_ACTIVE_REGION_CHANGED=MAP_FIRST;
	public static final int MAP_RENAMED=MAP_FIRST+1;
	public static final int MAP_DATABASE_CHANGED=MAP_FIRST+2;
	public static final int MAP_BOUNDING_RECT_CHANGED=MAP_FIRST+3;
	public static final int MAP_CREATION_DATE_CHANGED=MAP_FIRST+4;
	public static final int MAP_AUTHOR_CHANGED=MAP_FIRST+5;
	public static final int MAP_COMMENTS_CHANGED=MAP_FIRST+6;
	public static final int MAP_ENTRY_NODE_CHANGED=MAP_FIRST+7;
	public static final int MAP_REGION_ADDED=MAP_FIRST+8;
	public static final int MAP_REGION_REMOVED=MAP_FIRST+9;
	public static final int MAP_CLOSED=MAP_FIRST+10;
	public static final int MAP_CHANGED=MAP_FIRST+11;
	public static final int MAP_DATE_INTERVAL_CHANGED=MAP_FIRST+12;
	public static final int MAP_LAST=MAP_FIRST+12;

	private Object oldValue,newValue;
	private int objID;

	public MapEvent(Object source,int id) {
		super(source,id);
	}

	public MapEvent(Object source,int id,Object oldValue,Object newValue) {
		super(source,id);
		this.oldValue=oldValue;
		this.newValue=newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}
