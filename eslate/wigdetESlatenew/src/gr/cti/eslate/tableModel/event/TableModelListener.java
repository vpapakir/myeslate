package gr.cti.eslate.tableModel.event;


import java.util.EventListener;

public interface TableModelListener extends EventListener {
    /** A <i>CellValueChangedEvent</i> is fired every time a cell's value is changed.
     */
    public abstract void cellValueChanged(CellValueChangedEvent e);
    /** When the data type of a column changes, a <i>ColumnTypeChangedEvent</i> is fired.
     */
    public abstract void columnTypeChanged(ColumnTypeChangedEvent e);
    /** Called when the name of a column in the table model changes.
     */
    public abstract void columnRenamed(ColumnRenamedEvent e);
    /** Called every time a new column is added to the table model.
     */
    public abstract void columnAdded(ColumnAddedEvent e);
    /** When a column of the table model is replaced by another column, a
     *  <i>ColumnReplacedEvent</i> ifs fired.
     */
    public abstract void columnReplaced(ColumnReplacedEvent e);
    /** Called every time a column is removed from the table model.
     */
    public abstract void columnRemoved(ColumnRemovedEvent e);
    /** Called every time the set of selected records of the table model changes.
     */
    public abstract void selectedRecordSetChanged(SelectedRecordSetChangedEvent e);
    /** Called when the order of the records in the table model changes.
     */
    public abstract void rowOrderChanged(RowOrderChangedEvent e);
    /** Called when the name of the table model changes.
     */
    public abstract void tableRenamed(TableRenamedEvent e);
    /** Called when a record is removed from the TableModel. This event should be fired
     *  only from those TableModels where record is meaningful.
     */
    public abstract void recordRemoved(RecordRemovedEvent e);
    /** Fired when the active row of the TableModel changes. The row ids which are
     *  carried by <i>ActiveRecordChangedEvent</i> event refer to the internal
     *  row sequense in the TableModel.
     */
    public abstract void activeRecordChanged(ActiveRecordChangedEvent e);
}

