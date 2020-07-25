package gr.cti.eslate.spinButton;


public class ValueChangedEvent extends java.util.EventObject {
    public static final int VALUE_CHANGED = 1;
    public static final int MAX_VALUE_CHANGED = 2;
    public static final int MIN_VALUE_CHANGED = 3;
    private Object value, previousValue;
    private int eventID;
    private boolean isLast;

    /*    public ValueChangedEvent(Object source,int id) {
     super(source,id);
     }
     */
    public ValueChangedEvent(Object source, int id, Object value, Object previousValue, boolean isLast) {
        super(source);
        if (id < VALUE_CHANGED || id > MIN_VALUE_CHANGED)
            throw new RuntimeException("Invalid ValueChangedEvent id");
        this.value = value;
        this.eventID = id;
        this.previousValue = previousValue;
        this.isLast = isLast;
    }

    public Object getValue() {
        return value;
    }

    public int getID() {
        return eventID;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public boolean isLast() {
        return isLast;
    }
}
