package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnRemovableStateChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String columnName;
    protected boolean isRemovable;

//t    public FieldRemovableStateChangedEvent(Object source, int tableIndex, String fieldName, boolean isRemovable) {
    public ColumnRemovableStateChangedEvent(Object source, String columnName, boolean isRemovable) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.isRemovable = isRemovable;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getColumnName() {
        return columnName;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

}
