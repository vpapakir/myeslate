package gr.cti.eslate.eslateList;


import java.awt.AWTEvent;


public class SelectionChangedEvent extends AWTEvent {

	private static final long serialVersionUID = 5287152870830005574L;
	public static final int SELECTION_CHANGED_FIRST = AWTEvent.RESERVED_ID_MAX + 1;
    public static final int SELECTION_CHANGED_LAST = SELECTION_CHANGED_FIRST;
    public Object value;

    public SelectionChangedEvent(Object source, int id) {
        super(source, id);
    }

    public SelectionChangedEvent(Object source, int id, Object value) {
        super(source, id);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
