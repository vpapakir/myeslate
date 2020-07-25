package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnKeyChangedEvent.
 */
public class FieldKeyChangedEvent extends EventObject {
//t    private int tableIndex;
    private String fieldName;
    private boolean isKey;

//t    public FieldKeyChangedEvent(Object source, int tableIndex, String fieldName, boolean isKey) {
    public FieldKeyChangedEvent(Object source, String fieldName, boolean isKey) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.isKey = isKey;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public boolean isKey() {
        return isKey;
    }

}
