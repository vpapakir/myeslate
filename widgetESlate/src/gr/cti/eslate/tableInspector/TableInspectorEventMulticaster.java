package gr.cti.eslate.tableInspector;

import java.util.HashSet;
import java.util.Iterator;

public class TableInspectorEventMulticaster extends HashSet<TableInspectorListener> implements TableInspectorListener {

    public TableInspectorEventMulticaster() {
        super();
    }

    public void activeTabChanged(TableInspectorEvent e) {
        Iterator it=iterator();
        while (it.hasNext())
            ((TableInspectorListener) it.next()).activeTabChanged(e);
    }

    public void activeRecordBrowserRecordChanged(TableInspectorEvent e) {
        Iterator it=iterator();
        while (it.hasNext())
            ((TableInspectorListener) it.next()).activeRecordBrowserRecordChanged(e);
    }
}