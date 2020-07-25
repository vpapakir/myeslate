package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.RecordAddedEvent.
 */
public class RecordAddedEvent extends EventObject {
//t    private int tableIndex;
    private int recordIndex;

//t    public RecordAddedEvent(Object source, int tableIndex, int recordIndex) {
    public RecordAddedEvent(Object source, int recordIndex) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.recordIndex = recordIndex;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public int getRecordIndex() {
        return recordIndex;
    }
}
