/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 24 Οκτ 2002
 * Time: 2:06:39 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.tableModel.event;

public class FloatCellValueChangedEvent extends CellValueChangedEvent {
    private float oldFloatValue;

    public FloatCellValueChangedEvent(Object source, String columnName, int recordIndex, float oldValue, boolean affectsOtherCells) {
        super(source, columnName, recordIndex, null, affectsOtherCells);
        this.oldFloatValue = oldValue;
    }

    /** The previous value of the cell.
     */
    public Object getOldValue() {
        if (super.getOldValue() == null)
            oldValue = new Float(oldFloatValue);
        return oldValue;
    }

    /** Returns the previous value of the cell as a float. */
    public float getOldFloatValue() {
        return oldFloatValue;
    }
}
