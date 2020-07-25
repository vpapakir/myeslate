package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.CalcFieldResetEvent
 */
public class CalcFieldResetEvent extends EventObject {
    protected String fieldName;
//t    protected int tableIndex;

//t    public CalcFieldResetEvent(Object source, int tableIndex, String fieldName) {
    public CalcFieldResetEvent(Object source, String fieldName) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
}
