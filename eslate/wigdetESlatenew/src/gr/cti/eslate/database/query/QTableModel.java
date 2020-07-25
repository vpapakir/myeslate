package gr.cti.eslate.database.query;

import gr.cti.eslate.database.engine.*;
import com.objectspace.jgl.Array;
import javax.swing.table.AbstractTableModel;

class QTableModel extends AbstractTableModel {
    Array data;
    Table ctable;
    int rowCount;
    int fieldCount;
    Array fieldIndices;
    QueryComponent qComponent;


    public QTableModel(Table ctable, QueryComponent queryComponent) {
        this.ctable = ctable;
        this.qComponent = queryComponent;
        data = new Array();
        rowCount = 1;
        fieldCount = 0;
        fieldIndices = new Array();

        /* Exclude the fields of Image data type.
         */
        AbstractTableField f;
        TableFieldBaseArray tableFields = ctable.getFields();
        for (int i=0; i<ctable.getFieldCount(); i++) {
            f = (AbstractTableField) tableFields.get(i);
//            if (f.getFieldType().getName().equals("gr.cti.eslate.database.engine.CImageIcon")) {
//                continue;
//            }
            fieldIndices.add(new Integer(i));
            fieldCount++;
        }
        for (int i=0; i<fieldCount; i++)
            data.add(new Array());
        for (int i=0; i<fieldCount; i++)
            ((Array) data.at(i)).add(null);
    }


    public int getColumnCount() {
        return fieldCount;
    }


    public Class getColumnClass(int columnIndex) {
//        System.out.println("In getColumnClass(): " + columnIndex);
        try{
            columnIndex = ((Integer) fieldIndices.at(columnIndex)).intValue();
            return ctable.getTableField(columnIndex).getDataType(); //tci ((Integer) table.tableColumnsIndex.at(columnIndex)).intValue()).getFieldType();
        }catch (InvalidFieldIndexException e) {return null;}
    }


    public String getColumnName(int columnIndex) {
//        System.out.println("In getColumnIdentifier(): " + columnIndex);
        try{
            columnIndex = ((Integer) fieldIndices.at(columnIndex)).intValue();
            return ctable.getTableField(columnIndex).getName(); //tci ((Integer) table.tableColumnsIndex.at(columnIndex)).intValue()).getName();
        }catch (InvalidFieldIndexException e) {return null;}
    }


    public int getColumnIndex(Object identifier) {
//        System.out.println("In getColumnIndex(): " + identifier);
        try{
            int fieldIndex =  ctable.getFieldIndex((String) identifier);
            for (int i=0; i<fieldIndices.size(); i++) {
                if (((Integer) fieldIndices.at(i)).intValue() == fieldIndex)
                    return i;
            }
            throw new NullPointerException("Cannot find the column " + identifier);
        }catch (InvalidFieldNameException e) {return 0;}
    }


    public int getRowCount() {
        return rowCount;
    }


    public Object getValueAt(int recIndex, int columnIndex) {
//        System.out.println("In getValueAt(): " + recIndex + ", " + columnIndex);
        try{
            return ((Array) data.at(columnIndex)).at(recIndex);  //tci ((Integer) table.tableColumnsIndex.at(columnIndex)).intValue(), table.recordIndex[recIndex]);
        }catch (Exception e) {e.printStackTrace(); return null;}
    }


    public boolean isCellEditable(int recIndex, int columnIndex) {
//        if (((JTable) qComponent.QTables.at(qComponent.activeQTableIndex)).isEditing())
//            qComponent.enableRemoveRowButton();
        return true;
    }


    public void setValueAt(Object value, int rowIndex, int columnIndex) {
//        System.out.println("Setting value: " + value + "for cell at: " + rowIndex + ", " + columnIndex);
//        System.out.println(ctable.riskyGetCell(columnIndex, rowIndex));
        try{
            ((Array) data.at(columnIndex)).put(rowIndex, value);
        }catch (Exception e) {e.printStackTrace();}
        qComponent.checkToolStatus();
    }


    public void addNewColumn(int columnIndex) {
        Array a = new Array();
        a.add(null);
        data.add(a);
        fieldIndices.add(new Integer(columnIndex));
        fieldCount++;
    }

    public void removeColumn(int columnIndex) {
        data.remove(columnIndex);
        fieldIndices.remove(columnIndex);
        for (int i=0; i<fieldIndices.size(); i++) {
            int k = ((Integer) fieldIndices.at(i)).intValue();
            if (k > columnIndex)
                fieldIndices.put(i, new Integer(k-1));
        }
        fieldCount--;
        qComponent.checkToolStatus();
    }

    public boolean isEmpty() {
        String cellValue;
        for (int i=0; i<rowCount; i++) {
            for (int k=0; k<fieldCount; k++) {
                cellValue = (String) ((Array) data.at(k)).at(i);
                if (cellValue == null || cellValue.length() == 0)
                    continue;
                return false;
            }
        }
        return true;
    }

    public void empty() {
        for (int i=0; i<rowCount; i++) {
            for (int k=0; k<fieldCount; k++) {
                ((Array) data.at(k)).put(i, null);
            }
        }
    }

    public void addRow() {
        for (int i=0; i<fieldCount; i++)
            ((Array) data.at(i)).add(null);
        rowCount++;
    }

    public void removeRow(int rowIndex) {
        for (int i=0; i<fieldCount; i++)
            ((Array) data.at(i)).remove(rowIndex);

        rowCount--;
    }

    public boolean isRowEmpty(int rowIndex) {
        for (int i=0; i<fieldCount; i++) {
            String cell = (String) ((Array) data.at(i)).at(rowIndex);
            if (cell != null && cell.length() != 0)
                return false;
        }
        return true;
    }
}

