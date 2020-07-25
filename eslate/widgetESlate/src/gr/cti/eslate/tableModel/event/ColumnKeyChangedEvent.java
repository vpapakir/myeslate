package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnKeyChangedEvent extends EventObject {
//t    private int tableIndex;
    private String columnName;
    private boolean isKey;

//t    public FieldKeyChangedEvent(Object source, int tableIndex, String fieldName, boolean isKey) {
    public ColumnKeyChangedEvent(Object source, String columnName, boolean isKey) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.isKey = isKey;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getColumnName() {
        return columnName;
    }

    public boolean isKey() {
        return isKey;
    }

}
