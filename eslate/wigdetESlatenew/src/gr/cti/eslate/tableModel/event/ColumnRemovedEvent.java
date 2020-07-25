package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnRemovedEvent extends EventObject {
    private String columnName;
    private int columnIndex;
    private boolean moreToBeRemoved = false;
//t    protected int tableIndex;

//t    public FieldRemovedEvent(Object source, int tableIndex, String fieldName) {
    public ColumnRemovedEvent(Object source, String columnName, int columnIndex, boolean moreToBeRemoved) {
        super(source);
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.moreToBeRemoved = moreToBeRemoved;
//t        this.tableIndex = tableIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    /** Returns the index the removed column had in the table. Beware that the column no longer
     *  exists in the table when this event is fired, so the returned index is either
     *  invalid or refers to another column in the table.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /** Informs if more fields will be removed subsequently. */
    public boolean moreToBeRemoved() {
        return moreToBeRemoved;
    }
/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
}
