package gr.cti.eslate.tableModel.event;

import java.util.EventObject;
//timport gr.cti.eslate.database.engine.CTableField;


public class  ColumnReplacedEvent extends EventObject {
//t    protected int tableIndex;
//t    protected CTableField field;
    private int columnIndex = 0;

//t    public FieldAddedEvent(Object source, int tableIndex, CTableField field) {
    public ColumnReplacedEvent(Object source, int columnIndex) { //CTableField field) {
        super(source);
//t        this.tableIndex = tableIndex;
//t        this.field = field;
        this.columnIndex = columnIndex;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }

    public CTableField getField() {
        return field;
    }
t*/
    public int getColumnIndex() {
        return columnIndex;
    }
}
