package gr.cti.eslate.event;

import java.util.EventListener;


public interface ColumnListener extends EventListener {
    public abstract void columnMoved(ColumnMovedEvent e);
}
