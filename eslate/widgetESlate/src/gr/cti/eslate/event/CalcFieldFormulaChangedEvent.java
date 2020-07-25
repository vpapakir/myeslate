package gr.cti.eslate.event;

import java.util.EventObject;

/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.tableModel.event.CalcFieldFormulaChangedEvent
 */
public class CalcFieldFormulaChangedEvent extends EventObject {
//t    protected int tableIndex;
    protected String fieldName;
    protected String formula;
    protected String previousDataType;

//t    public CalcFieldFormulaChangedEvent(Object source, int tableIndex, String fieldName, String formula, String previousDataType) {
    public CalcFieldFormulaChangedEvent(Object source, String fieldName, String formula, String previousDataType) {
        super(source);
//t        this.tableIndex = tableIndex;
        this.fieldName = fieldName;
        this.formula = formula;
        this.previousDataType = previousDataType;
    }

/*t    public int getTableIndex() {
        return tableIndex;
    }
t*/
    public String getFieldName() {
        return fieldName;
    }

    public String getFormula() {
        return formula;
    }

    public String getPreviousDataType() {
        return previousDataType;
    }

}
