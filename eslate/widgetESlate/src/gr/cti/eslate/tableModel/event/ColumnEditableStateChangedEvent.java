package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnEditableStateChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String columnName;
    protected boolean isEditable;

//t    public FieldEditableStateChangedEvent(Object source, int tableIndex, String fieldName, boolean isEditable) {
    public ColumnEditableStateChangedEvent(Object source, String columnName, boolean isEditable) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.isEditable = isEditable;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getColumnName() {
        return columnName;
    }

    public boolean isEditable() {
        return isEditable;
    }

}
