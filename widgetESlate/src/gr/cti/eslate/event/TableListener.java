package gr.cti.eslate.event;

public interface TableListener extends java.util.EventListener {
    public abstract void activeRecordChanged(ActiveRecordChangedEvent e);
}

