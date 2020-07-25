package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnEditableStateChangedEvent.
 */
public class FieldEditableStateChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String fieldName;
    protected boolean isEditable;

//t    public FieldEditableStateChangedEvent(Object source, int tableIndex, String fieldName, boolean isEditable) {
    public FieldEditableStateChangedEvent(Object source, String fieldName, boolean isEditable) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.isEditable = isEditable;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public boolean isEditable() {
        return isEditable;
    }

}
