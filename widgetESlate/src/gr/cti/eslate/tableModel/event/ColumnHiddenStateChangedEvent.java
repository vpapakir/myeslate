package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnHiddenStateChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String columnName;
    protected boolean isHidden;

//t    public FieldHiddenStateChangedEvent(Object source, int tableIndex, String fieldName, boolean isHidden) {
    public ColumnHiddenStateChangedEvent(Object source, String columnName, boolean isHidden) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.isHidden = isHidden;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getColumnName() {
        return columnName;
    }

    public boolean isHidden() {
        return isHidden;
    }

}
