package gr.cti.eslate.mapModel;

import java.awt.AWTEvent;

/**
 * This is the general region event. This can be used for simply
 * informing (use the first constructor) as well as providing
 * the old and new values (use the second constructor) of a
 * property which has been changed.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 28-Feb-2000
 * @see		gr.cti.eslate.mapModel.RegionListener
 * @see		gr.cti.eslate.mapModel.RegionAdapter
 */
public class RegionEvent extends AWTEvent {
	public static final int REGION_FIRST=AWTEvent.RESERVED_ID_MAX+1;
	public static final int REGION_RENAMED=REGION_FIRST;
	public static final int REGION_DATABASE_CHANGED=REGION_FIRST+1;
	public static final int REGION_BOUNDING_RECT_CHANGED=REGION_FIRST+2;
	public static final int REGION_LAYER_ADDED=REGION_FIRST+3;
	public static final int REGION_LAYER_REMOVED=REGION_FIRST+4;
	public static final int REGION_LAYERS_SWAPPED=REGION_FIRST+5;
	public static final int REGION_SCALE_CHANGED=REGION_FIRST+6;
	public static final int REGION_ORIENTATION_CHANGED=REGION_FIRST+7;
	public static final int REGION_BACKGROUND_IMAGE_ADDED=REGION_FIRST+8;
	public static final int REGION_DEFAULT_BACKGROUND_IMAGE_CHANGED=REGION_FIRST+9;
	public static final int REGION_COORDINATE_SYSTEM_CHANGED=REGION_FIRST+10;
	public static final int REGION_LAYERS_REORDERED=REGION_FIRST+11;
	public static final int REGION_ACTIVE_LAYER_CHANGED=REGION_FIRST+12;
	public static final int REGION_BACKGROUND_IMAGE_REMOVED=REGION_FIRST+13;
	public static final int REGION_LINK_CHANGED=REGION_FIRST+14;
	public static final int REGION_LAST=REGION_FIRST+14;

	private Object oldValue,newValue;
	private int objID;

	public RegionEvent(Object source,int id) {
		super(source,id);
	}

	public RegionEvent(Object source,int id,Object oldValue,Object newValue) {
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
