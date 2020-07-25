package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

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
