/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 24 Οκτ 2002
 * Time: 2:05:09 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.tableModel.event;


public class DoubleCellValueChangedEvent extends CellValueChangedEvent {
    private double oldDoubleValue;

    public DoubleCellValueChangedEvent(Object source, String columnName, int recordIndex, double oldValue, boolean affectsOtherCells) {
        super(source, columnName, recordIndex, null, affectsOtherCells);
        this.oldDoubleValue = oldValue;
    }

    /** The previous value of the cell.
     */
    public Object getOldValue() {
        if (super.getOldValue() == null)
            oldValue = new Double(oldDoubleValue);
        return oldValue;
    }

    /** Returns the previous value of the cell as a double. */
    public double getOldDoubleValue() {
        return oldDoubleValue;
    }
}
