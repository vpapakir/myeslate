package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.SelectedRecordSetChangedEvent.
 */
public class SelectedRecordSetChangedEvent extends EventObject {
//t    int tableIndex;
    String queryString;

//t    public SelectedRecordSetChangedEvent(Object source, int tableIndex, String queryString) {
    public SelectedRecordSetChangedEvent(Object source, String queryString) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.queryString = queryString;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getQueryString() {
        return queryString;
    }
}
