/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 24 Οκτ 2002
 * Time: 1:59:26 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.tableModel.event;

public class IntegerCellValueChangedEvent extends CellValueChangedEvent {
    private int oldIntValue;

    public IntegerCellValueChangedEvent(Object source, String columnName, int recordIndex, int oldValue, boolean affectsOtherCells) {
        super(source, columnName, recordIndex, null, affectsOtherCells);
        this.oldIntValue = oldValue;
    }

    /** The previous value of the cell.
     */
    public Object getOldValue() {
        if (super.getOldValue() == null)
            oldValue = new Integer(oldIntValue);
        return oldValue;
    }

    /** Returns the previous value of the cell as an int. */
    public int getOldIntegerValue() {
        return oldIntValue;
    }
}
