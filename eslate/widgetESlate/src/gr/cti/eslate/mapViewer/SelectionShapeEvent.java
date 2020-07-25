package gr.cti.eslate.mapViewer;


public class SelectionShapeEvent extends java.util.EventObject {
    public static final int SHAPE_GEOMETRY_CHANGED = SelectionShapeEvent.SHAPE_GEOMETRY_CHANGED;
    private Object value, previousValue;
    private int eventID;

    public SelectionShapeEvent(Object source, int id) {
        super(source);
        if (id != SHAPE_GEOMETRY_CHANGED)
            throw new RuntimeException("Invalid SelectionShapeEvent id");
        this.eventID = id;
        this.value=value;
        this.previousValue = previousValue;
    }

    public int getID() {
        return eventID;
    }

}