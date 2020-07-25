package gr.cti.eslate.tableModel.event;

import java.util.EventObject;


public class CellRemovedEvent extends EventObject {
    private int columnIndex;
    private int rowIndex;
    private Object value;

    public CellRemovedEvent(Object source, int columnIndex, int rowIndex, Object value) {
        super(source);
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.value = value;
    }

    /** The index of the column from which the cell was removed.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /** The index of the row from which the cell was removed.
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /** The value of the removed cell.
     */
    public Object getValue() {
        return value;
    }
}
