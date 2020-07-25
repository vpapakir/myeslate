package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.CellValueChangedEvent.
 */
public class CellValueChangedEvent extends EventObject {
//t    private int tableIndex;
    private String fieldName;
    private int recordIndex;
    private Object oldValue;
    private Object newValue;
    private boolean affectsOtherCells;

//t    public CellValueChangedEvent(Object source, int tableIndex, String fieldName, int recordIndex, Object newValue, Object oldValue, boolean affectsOtherCells) {
    public CellValueChangedEvent(Object source, String fieldName, int recordIndex, Object newValue, Object oldValue, boolean affectsOtherCells) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.recordIndex = recordIndex;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.affectsOtherCells = affectsOtherCells;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public boolean affectsOtherCells() {
        return affectsOtherCells;
    }
}
