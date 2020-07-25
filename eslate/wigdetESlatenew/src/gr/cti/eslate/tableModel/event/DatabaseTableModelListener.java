package gr.cti.eslate.tableModel.event;


public interface DatabaseTableModelListener extends TableModelListener {
    public abstract void columnKeyChanged(ColumnKeyChangedEvent e);
    public abstract void calcColumnReset(CalcColumnResetEvent e);
    public abstract void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent e);
    public abstract void columnEditableStateChanged(ColumnEditableStateChangedEvent e);
    public abstract void columnRemovableStateChanged(ColumnRemovableStateChangedEvent e);
    public abstract void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e);
    public abstract void recordAdded(RecordAddedEvent e);
    public abstract void tableHiddenStateChanged(TableHiddenStateChangedEvent e);
    /** The table notifies its listeners that a numeric field has become a currency field or vice-versa. The event
     * is also fired when the currency of a field changes.
     * @param e
     */
    public abstract void currencyFieldChanged(ColumnEvent e);
}