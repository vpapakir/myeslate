package gr.cti.eslate.tableModel.event;

import java.util.EventObject;
import java.util.ArrayList;
//t import com.objectspace.jgl.Array;

public class RecordRemovedEvent extends EventObject {
//t    private int tableIndex;
    private int recordIndex;
    private boolean isChanging;
    private int rowIndex;
    private ArrayList removedRecord;

//t    public RecordRemovedEvent(Object source, int tableIndex, int recordIndex, int rowIndex, Array removedRecord, boolean isChanging) {
    public RecordRemovedEvent(Object source, int recordIndex, int rowIndex, ArrayList removedRecord, boolean isChanging) {
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
    /** The internal index of the removed record.
     */
    public int getRecordIndex() {
        return recordIndex;
    }

    /** The index of the table UI row which displayed the removed record.
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /** The contents of the removed record.
     */
    public ArrayList getRemovedRecord() {
        return removedRecord;
    }

    public boolean isChanging() {
        return isChanging;
    }
}
