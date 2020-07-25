package gr.cti.eslate.mapModel;

import java.awt.AWTEvent;

public class LayerEvent extends AWTEvent {
	public static final int LAYER_FIRST=AWTEvent.RESERVED_ID_MAX + 1;
	public static final int LAYER_VISIBILITY_CHANGED=LAYER_FIRST;
	public static final int LAYERS_SWAPPED=LAYER_FIRST+1;
	public static final int LAYER_SELECTION_CHANGED=LAYER_FIRST+2;
	public static final int LAYER_ACTIVE_GEOGRAPHIC_OBJECT_CHANGED=LAYER_FIRST+3;
	public static final int LAYER_GEOGRAPHIC_OBJECT_ADDED=LAYER_FIRST+4;
	public static final int LAYER_GEOGRAPHIC_OBJECT_REMOVED=LAYER_FIRST+5;
	public static final int LAYER_GEOGRAPHIC_OBJECT_REPOSITIONED=LAYER_FIRST+6;
	public static final int LAYER_COLORING_CHANGED=LAYER_FIRST+7;
	public static final int LAYER_TIP_BASE_CHANGED=LAYER_FIRST+8;
	public static final int LAYER_LABEL_BASE_CHANGED=LAYER_FIRST+9;
	public static final int LAYER_DEFAULT_VISIBILITY_CHANGED=LAYER_FIRST+10;
	public static final int LAYER_CAN_BE_HIDDEN_CHANGED=LAYER_FIRST+11;
	public static final int LAYER_OBJECTS_CAN_BE_SELECTED_CHANGED=LAYER_FIRST+12;
	//This has been removed: public static final int LAYER_OBJECTS_CAN_BE_HIGHLIGHTED_CHANGED=LAYER_FIRST+13;
	public static final int LAYER_NOT_SELECTED_OBJECTS_SHOWN_DEFAULT_CHANGED=LAYER_FIRST+14;
	public static final int LAYER_NOT_SELECTED_OBJECTS_SHOWN_CAN_CHANGE_CHANGED=LAYER_FIRST+15;
	public static final int LAYER_OBJECTS_WITH_NO_DATA_SHOWN_CHANGED=LAYER_FIRST+16;
	public static final int LAYER_PAINT_PROPERTIES_CHANGED=LAYER_FIRST+17;
	public static final int LAYER_UNKNOWN_PROPERTY_CHANGED=LAYER_FIRST+18;
	public static final int LAYER_GEOGRAPHIC_OBJECT_PROPERTIES_CHANGED=LAYER_FIRST+19;
	public static final int LAYER_RENAMED=LAYER_FIRST+20;
	public static final int LAYER_DATE_FROM_BASE_CHANGED=LAYER_FIRST+21;
	public static final int LAYER_DATE_TO_BASE_CHANGED=LAYER_FIRST+22;
	public static final int LAYER_DATABASE_TABLE_CHANGED=LAYER_FIRST+23;
	public static final int LAYER_OBJECT_VISIBILITY_CHANGED=LAYER_FIRST+24;
	public static final int LAYER_NORMAL_TRANSPARENCY_LEVEL_CHANGED=LAYER_FIRST+25;
	public static final int LAYER_SELECTED_TRANSPARENCY_LEVEL_CHANGED=LAYER_FIRST+26;
	public static final int LAYER_HIGHLIGHTED_TRANSPARENCY_LEVEL_CHANGED=LAYER_FIRST+27;
	public static final int LAYER_BOUNDING_RECT_CHANGED=LAYER_FIRST+28;
	public static final int LAYER_LAST=LAYER_FIRST+28;

	private Object obj1,obj2;
	private int objID;
	private boolean isChanging;

	public LayerEvent(Object source,int id) {
		super(source,id);
		isChanging=false;
	}

	public LayerEvent(Object source,int id,Object oldValue,Object newValue) {
		this(source,id);
		obj1=oldValue;
		obj2=newValue;
	}

	public LayerEvent(Object source,int id,Object oldValue,Object newValue,boolean isChanging) {
		super(source,id);
		obj1=oldValue;
		obj2=newValue;
		this.isChanging=isChanging;
	}

	public Object getOldValue() {
		return obj1;
	}

	public Object getNewValue() {
		return obj2;
	}

	public boolean isChanging() {
		return isChanging;
	}
}
