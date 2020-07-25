package gr.cti.eslate.event;

import java.util.EventObject;
import com.objectspace.jgl.Array;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.RecordRemovedEvent.
 */
public class RecordRemovedEvent extends EventObject {
//t    private int tableIndex;
    private int recordIndex;
    private boolean isChanging;
    private int rowIndex;
    private Array removedRecord;

//t    public RecordRemovedEvent(Object source, int tableIndex, int recordIndex, int rowIndex, Array removedRecord, boolean isChanging) {
    public RecordRemovedEvent(Object source, int recordIndex, int rowIndex, Array removedRecord, boolean isChanging) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.recordIndex = recordIndex;
        this.isChanging = isChanging;
        this.rowIndex = rowIndex;
        this.removedRecord = removedRecord;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public int getRecordIndex() {
        return recordIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public Array getRemovedRecord() {
        return removedRecord;
    }

    public boolean isChanging() {
        return isChanging;
    }
}
