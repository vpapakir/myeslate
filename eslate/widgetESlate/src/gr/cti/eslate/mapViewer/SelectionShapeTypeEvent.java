package gr.cti.eslate.mapViewer;


public class SelectionShapeTypeEvent extends java.util.EventObject {
    public static final int SHAPE_TYPE_CHANGED = SelectionShapeTypeEvent.SHAPE_TYPE_CHANGED;
    private int type;
    private int eventID;

    public SelectionShapeTypeEvent(Object source, int id,int type) {
        super(source);
        if (id != SHAPE_TYPE_CHANGED)
            throw new RuntimeException("Invalid SelectionShapeTypeEvent id");
        this.eventID = id;
        this.type=type;
    }

    public int getID() {
        return eventID;
    }

    public int getShapeType() {
        return type;
    }

}