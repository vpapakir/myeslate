package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class CalcColumnFormulaChangedEvent extends EventObject {
//t    protected int tableIndex;
    private String columnName;
    private String formula;
//t    private String previousDataType;
    private String previousDataType;

//t    public CalcFieldFormulaChangedEvent(Object source, int tableIndex, String fieldName, String formula, String previousDataType) {
    public CalcColumnFormulaChangedEvent(Object source, String columnName, String formula, String previousDataType) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.columnName = columnName;
        this.formula = formula;
        this.previousDataType = previousDataType;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getColumnName() {
        return columnName;
    }

    public String getFormula() {
        return formula;
    }

    /** Returns the previous data type of the calculated field. The data type is the name of the java class
     *  of the values contained in the table column.
     */
    public String getPreviousDataType() {
        return previousDataType;
    }

}
