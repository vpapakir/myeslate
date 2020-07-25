package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.event.ActiveTableChangedEvent
 */
public class ActiveTableChangedEvent extends EventObject {
    protected int fromIndex;
    protected int toIndex;

    public ActiveTableChangedEvent(Object source, int fromIndex, int toIndex) {
        super(source);
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public int getFromIndex() {
        return fromIndex;
    }

}