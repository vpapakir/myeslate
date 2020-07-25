package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class  CellAddedEvent extends EventObject {
    private int columnIndex = 0;
    private int rowIndex = 0;
    private boolean appended = false;

    public CellAddedEvent(Object source, int columnIndex, int rowIndex, boolean appended) {
        super(source);
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.appended = appended;
    }

    /** The index of the column at which the cell was added.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /** The index of the row at which the cell was added.
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /** Reports whether the cell was appended to the column or inserted in it.
     */
    public boolean isAppened() {
        return appended;
    }
}
