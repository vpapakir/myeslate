package gr.cti.eslate.tableInspector;

/**
 * An adapter class for the TableInspectorListener interface.
 */
public class TableInspectorAdapter implements TableInspectorListener {

    public TableInspectorAdapter() {
    }

    public void activeTabChanged(TableInspectorEvent e) {}
    public void activeRecordBrowserRecordChanged(TableInspectorEvent e){}
}