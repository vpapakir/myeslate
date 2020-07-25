package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class RecordAddedEvent extends EventObject {
    private int recordIndex;
    private boolean moreToBeAdded = false;

    public RecordAddedEvent(Object source, int recordIndex, boolean moreToBeAdded) {
        super(source);
        this.recordIndex = recordIndex;
        this.moreToBeAdded = moreToBeAdded;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public boolean moreToBeAdded() {
        return moreToBeAdded;
    }
}
