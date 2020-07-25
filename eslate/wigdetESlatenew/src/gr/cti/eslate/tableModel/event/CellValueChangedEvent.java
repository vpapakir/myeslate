package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class CellValueChangedEvent extends EventObject {
//t    private int tableIndex;
    private String columnName;
    private int recordIndex;
    Object oldValue;
//    private Object newValue;
    private boolean affectsOtherCells;

//t    public CellValueChangedEvent(Object source, int tableIndex, String fieldName, int recordIndex, Object newValue, Object oldValue, boolean affectsOtherCells) {
    public CellValueChangedEvent(Object source, String columnName, int recordIndex, Object oldValue, boolean affectsOtherCells) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.recordIndex = recordIndex;
//        this.newValue = newValue;
        this.oldValue = oldValue;
        this.affectsOtherCells = affectsOtherCells;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    /** The name of the column to which the cell whose value changed belongs.
     */
    public String getColumnName() {
        return columnName;
    }

    /** The index of the row to which the cell whose value changed belongs.
     */
    public int getRecordIndex() {
        return recordIndex;
    }

    /** The new value of the cell.
     */
/*    public Object getNewValue() {
        return newValue;
    }
*/
    /** The previous value of the cell.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /** Reports if the change of the value of the cell affects the values in other cells.
     */
    public boolean affectsOtherCells() {
        return affectsOtherCells;
    }
}
