package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class CalcColumnResetEvent extends EventObject {
    protected String columnName;
//t    protected int tableIndex;

//t    public CalcFieldResetEvent(Object source, int tableIndex, String fieldName) {
    public CalcColumnResetEvent(Object source, String columnName) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
}
