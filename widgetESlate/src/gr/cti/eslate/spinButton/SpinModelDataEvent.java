package gr.cti.eslate.spinButton;


public class SpinModelDataEvent extends java.util.EventObject {
	
	static final long serialVersionUID = 732321L;
	
    public static final int VALUE_CHANGED = ValueChangedEvent.VALUE_CHANGED;
    public static final int MIN_VALUE_CHANGED = ValueChangedEvent.MIN_VALUE_CHANGED;
    public static final int MAX_VALUE_CHANGED = ValueChangedEvent.MAX_VALUE_CHANGED;
    private Object value, previousValue;
    private int eventID;

    /*    public SpinModelDataEvent(Object source,int id) {
     super(source,id);
     }
     */
    public SpinModelDataEvent(Object source, int id, Object value, Object previousValue) {
        super(source);
        if (id != VALUE_CHANGED && id != MIN_VALUE_CHANGED && id != MAX_VALUE_CHANGED)
            throw new RuntimeException("Invalid SpinModelDataEvent id");
        this.eventID = id;
        this.value = value;
        this.previousValue = previousValue;
    }

    public Object getValue() {
        return value;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public int getID() {
        return eventID;
    }

}
