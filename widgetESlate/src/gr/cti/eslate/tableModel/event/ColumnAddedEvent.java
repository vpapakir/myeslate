package gr.cti.eslate.tableModel.event;

import java.util.EventObject;
//timport gr.cti.eslate.database.engine.CTableField;

public class  ColumnAddedEvent extends EventObject {
//t    protected int tableIndex;
//t    protected CTableField field;
    private int columnIndex = 0;
    private boolean moreToBeAdded = false;

//t    public FieldAddedEvent(Object source, int tableIndex, CTableField field) {
    public ColumnAddedEvent(Object source, int columnIndex, boolean moreToBeAdded) { //CTableField field) {
        super(source);
//t        this.tableIndex = tableIndex;
//t        this.field = field;
        this.columnIndex = columnIndex;
        this.moreToBeAdded = moreToBeAdded;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }

    public CTableField getField() {
        return field;
    }
t*/

    /** Returns the index of the column which has just been added to the Table . */
    public int getColumnIndex() {
        return columnIndex;
    }

    /** Reports whether more columns will subsequently be added to the Table. */
    public boolean moreToBeAdded() {
        return moreToBeAdded;
    }

}
