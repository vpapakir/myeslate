package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.ColumnRemovableStateChangedEvent.
 */
public class FieldRemovableStateChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String fieldName;
    protected boolean isRemovable;

//t    public FieldRemovableStateChangedEvent(Object source, int tableIndex, String fieldName, boolean isRemovable) {
    public FieldRemovableStateChangedEvent(Object source, String fieldName, boolean isRemovable) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.isRemovable = isRemovable;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

}
