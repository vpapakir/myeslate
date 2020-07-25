package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ActiveRecordChangedEvent
 */
public class ActiveRecordChangedEvent extends EventObject {
//t    int tableIndex;
    int previousActiveRecord;
    int activeRecord;

//t    public ActiveRecordChangedEvent(Object source, int tableIndex, int previousActiveRecord, int activeRecord) {
    public ActiveRecordChangedEvent(Object source, int previousActiveRecord, int activeRecord) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.previousActiveRecord = previousActiveRecord;
        this.activeRecord = activeRecord;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public int getActiveRecord() {
        return activeRecord;
    }

    public int getPreviousActiveRecord() {
        return previousActiveRecord;
    }
}
