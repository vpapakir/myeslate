package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnRemovedEvent.
 */
public class FieldRemovedEvent extends EventObject {
    protected String fieldName;
//t    protected int tableIndex;

//t    public FieldRemovedEvent(Object source, int tableIndex, String fieldName) {
    public FieldRemovedEvent(Object source, String fieldName) {
        super(source);
        this.fieldName = fieldName;
//t        this.tableIndex = tableIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
}
