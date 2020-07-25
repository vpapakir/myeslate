package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class ColumnTypeChangedEvent extends EventObject {
//t    private int tableIndex;
    private String columnName;
    private String prevType;
    private String newType;
//t    private boolean isDate;
//t    private boolean wasDate;

//t    public FieldTypeChangedEvent(Object source, int tableIndex, String fieldName, Class oldType, boolean wasDate, Class newType, boolean isDate) {
//t    public ColumnTypeChangedEvent(Object source, String columnName, Class oldType, boolean wasDate, Class newType, boolean isDate) {
    public ColumnTypeChangedEvent(Object source, String columnName, String prevType, String newType) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.prevType = prevType;
//t        this.wasDate = wasDate;
        this.newType = newType;
//t        this.isDate = isDate;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getColumnName() {
        return columnName;
    }

    /** The new data type of the column. The data type is the name of the java class
     *  of the values contained in the table column.
     */
    public String getPrevType() {
        return prevType;
    }

    /** The previous data type of the column. The data type is the name of the java class
     *  of the values contained in the table column.
     */
    public String getNewType() {
        return newType;
    }

/*t    public boolean isDate() {
        return isDate;
    }

    public boolean wasDate() {
        return wasDate;
    }
t*/
}
