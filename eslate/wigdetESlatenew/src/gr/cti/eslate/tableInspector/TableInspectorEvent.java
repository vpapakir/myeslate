package gr.cti.eslate.tableInspector;

import java.awt.AWTEvent;

public class TableInspectorEvent extends AWTEvent {
    public static final int RB_FIRST=AWTEvent.RESERVED_ID_MAX+1;
    public static final int RB_ACTIVE_TAB_CHANGED=RB_FIRST;
    public static final int RB_ACTIVE_RECORD_CHANGED=RB_FIRST+1;
    public static final int RB_LAST=RB_FIRST+1;

    private Object oldValue,newValue;
    private int tab;
    /**
     * This constructor should not be used. It remains only for compatibility reasons.
     */
    public TableInspectorEvent(Object source) {
        //Compatibility with the first version of the class
        super(source,RB_ACTIVE_TAB_CHANGED);
    }

    public TableInspectorEvent(Object source,int id) {
        super(source,id);
    }

    /**
     * This constructor should not be used. It remains only for compatibility reasons.
     */
    public TableInspectorEvent(Object source,Object oldValue,Object newValue) {
        //Compatibility with the first version of the class
        super(source,RB_ACTIVE_TAB_CHANGED);
        this.oldValue=oldValue;
        this.newValue=newValue;
    }

    public TableInspectorEvent(Object source,int id,Object oldValue,Object newValue) {
       super(source,id);
        this.oldValue=oldValue;
        this.newValue=newValue;
        this.tab=-1;
    }

    public TableInspectorEvent(Object source,int id,int tabNumber,Object oldValue,Object newValue) {
       super(source,id);
        this.oldValue=oldValue;
        this.newValue=newValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setNewValue(Object val) {
        newValue=val;
    }

    public void setOldValue(Object val) {
        oldValue=val;
    }

    public int getTab() {
        return tab;
    }
}
