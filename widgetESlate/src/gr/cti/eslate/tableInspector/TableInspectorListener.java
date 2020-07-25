package gr.cti.eslate.tableInspector;

public interface TableInspectorListener extends java.util.EventListener {
    public abstract void activeTabChanged(TableInspectorEvent e);
    /**
     * This event is thrown when the Record Browser changes the record shown.
     * It is similar to the database activeRecordChange event and has the same value
     * when the Record Browser has its active record synchronized with the database active record.
     * When it is not sychronized, it is not thrown simultaneously and it has a different value.
     *
     * <em>The index number of the record as well as the tab number in which the event occured
     * are given starting from 1 (not 0).
     * When 0 is returned, no record is shown by the Record Browser in the given tab.</em>
     */
    public abstract void activeRecordBrowserRecordChanged(TableInspectorEvent e);
}
