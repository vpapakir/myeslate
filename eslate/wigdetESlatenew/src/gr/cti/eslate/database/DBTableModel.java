package gr.cti.eslate.database;

import gr.cti.eslate.utils.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import com.objectspace.jgl.HashMapIterator;
import com.objectspace.jgl.HashMap;
import com.objectspace.jgl.Array;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableColumn;
import java.text.NumberFormat;
import java.text.FieldPosition;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


public class DBTableModel extends AbstractTableModel {
    DBTable dbTable;
//    Table table;
    boolean messageDelivered = false;
    Object o;
    static FieldPosition intfp = new FieldPosition(NumberFormat.INTEGER_FIELD);
    static FieldPosition fracfp = new FieldPosition(NumberFormat.FRACTION_FIELD);

    public DBTableModel(DBTable dbTable) {
        this.dbTable = dbTable;
//        table = dbTable.table;
    }


/*
    protected void setTable(Table table) {
        this.table = table;
    }
*/


    public int getColumnCount() {
        if (dbTable.table == null) return 0;
//        System.out.println("In getColumnCount()" + dbTable.getFieldCount());
        return dbTable.table.getFieldCount();
    }


    public Class getColumnClass(int columnIndex) {
//        System.out.println("In getColumnClass(): " + columnIndex);
        try{
            return dbTable.table.getTableField(columnIndex).getDataType(); //tci ((Integer) jTable.tableColumnsIndex.at(columnIndex)).intValue()).getFieldType();
        }catch (InvalidFieldIndexException e) {return null;}
    }


    public String getColumnName(int columnIndex) {
//        System.out.println("In getColumnIdentifier(): " + columnIndex);
        try{
            return dbTable.table.getTableField(columnIndex).getName(); //tci ((Integer) jTable.tableColumnsIndex.at(columnIndex)).intValue()).getName();
        }catch (InvalidFieldIndexException e) {return null;}
    }


    public int getColumnIndex(Object identifier) {
//        System.out.println("In getColumnIndex(): " + identifier);
        try{
            int fieldIndex =  dbTable.table.getFieldIndex((String) identifier);
            return fieldIndex;
        }catch (InvalidFieldNameException e) {return 0;}
    }


    public AbstractTableField getField(int fieldIndex) {
        try{
            return dbTable.table.getTableField(fieldIndex); //tci ((Integer) jTable.tableColumnsIndex.at(fieldIndex)).intValue());
        }catch (InvalidFieldIndexException e) {return null;}
    }


    public int getRowCount() {
        if (dbTable == null || dbTable.table == null) return 0;
        int count = dbTable.table.getRecordCount();
        if (dbTable.table.getRecordEntryStructure().isRecordAdditionPending()) {
            count++;
        }
        return count;
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
//        System.out.println("In getValueAt(): " + rowIndex + ", " + columnIndex + ", recIndex: " + table.recordIndex.get(rowIndex));
        try{
            RecordEntryStructure res = dbTable.table.getRecordEntryStructure();
            if (rowIndex == res.getPendingRecordIndex())
                o = res.getCell/*AsString*/(columnIndex);
            else{
                o = dbTable.table.riskyGetCell/*AsString*/(columnIndex, dbTable.table.recordIndex.get(rowIndex));
            }
//if (Float.class.isInstance(o))
//System.out.println("getValueAt() o: " + o + ", floatValue: " + ((Float) o).floatValue() + ", format: " + table.numberFormat.format(((Number) o).floatValue()));
//            if (Double.class.isInstance(o) || Float.class.isInstance(o)) {
//                return table.numberFormat.format((Number) o);
//            }
            return o;
        }catch (Exception e) {e.printStackTrace(); return null;}
    }


    public boolean isCellEditable(int recIndex, int columnIndex) {
        if (!dbTable.table.isDataChangeAllowed())
            return false;
        try{
            if (getColumnClass(columnIndex).equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
//                System.out.println("isCellEditable: NO, CImageIcon");
                return false;
            }
            return dbTable.table.getTableField(columnIndex).isEditable();
        }catch (InvalidFieldIndexException e) {return false;}
    }


    public void setValueAt(Object value, int rowIndex, int columnIndex) {
//System.out.println("value: " + value + ", rowIndex: " + rowIndex);
//        try{
//        System.out.println("Setting value: " + value + "for cell at: " + rowIndex + ", " + columnIndex);
        /* Special care for Boolean values, which are displayed internationalized and therefore need to
        * be translated back to actual Boolean values.
        */
        if (getColumnClass(columnIndex).equals(java.lang.Boolean.class)) {
            if (value!= null) {
                if (value.equals(dbTable.dbComponent.infoBundle.getString("false")))
                    value = Boolean.FALSE;
                else
                    value = Boolean.TRUE;
            }
        }
//        System.out.println(dbTable.riskyGetCell(columnIndex, rowIndex));
        messageDelivered = false;
        RecordEntryStructure res = dbTable.table.getRecordEntryStructure();
        try{
            if (rowIndex == res.getPendingRecordIndex())
                res.setCell(dbTable.table.getTableField(columnIndex), value);
            else
                dbTable.table.setCell(columnIndex, dbTable.table.recordIndex.get(rowIndex), value); //tci ((Integer) jTable.tableColumnsIndex.at(columnIndex)).intValue(), jTable.recordIndex[rowIndex], value);
        }catch (InvalidFieldIndexException e) {
            System.out.println("DBTableModel inconsistency error 1. Should never occur.");
            return;
        }catch (InvalidCellAddressException e) {
            dbTable.tipManager.showTip(e.message, rowIndex, columnIndex, -1);
//            ESlateOptionPane.showMessageDialog(dbTable.dbComponent, e.message, dbTable.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            dbTable.jTable.requestFocus();
            return;
        }
        catch (InvalidDataFormatException e) {
            dbTable.tipManager.showTip(e.message, rowIndex, columnIndex, -1);
//            ESlateOptionPane.showMessageDialog(dbTable.dbComponent, "11. " + e.message, dbTable.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            messageDelivered = true;
            dbTable.jTable.requestFocus();
System.out.println("Focus to jTable");
            return;
        }
        catch (NullTableKeyException e) {
            dbTable.tipManager.showTip(e.message, rowIndex, columnIndex, -1);
//            ESlateOptionPane.showMessageDialog(dbTable.dbComponent, e.message, dbTable.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            dbTable.jTable.requestFocus();
            return;
        }
        catch (DuplicateKeyException e) {
            dbTable.tipManager.showTip(e.message, rowIndex, columnIndex, -1);
//            ESlateOptionPane.showMessageDialog(dbTable.dbComponent, e.message, dbTable.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            dbTable.jTable.requestFocus();
            return;
        }
        catch (AttributeLockedException e1) {
            dbTable.tipManager.showTip(e1.getMessage(), rowIndex, columnIndex, -1);
//            ESlateOptionPane.showMessageDialog(dbTable.dbComponent, e1.getMessage(), dbTable.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            dbTable.jTable.requestFocus();
            return;
        }
//        }catch (Exception e) {System.out.println(e.getClass().getName() + e.getMessage());}

        if (dbTable.table.haveCalculatedFieldsChanged()) {
//            System.out.println("haveCalculatedFieldsChanged=true");
            dbTable.table.resetCalculatedFieldsChangedFlag();

            Hashtable changedCalcCells = dbTable.table.getChangedCalcFieldCells();

//            System.out.println("changedCalcCells: " + changedCalcCells);

            Iterator iter = changedCalcCells.entrySet().iterator();
            String fieldName;
            int recIndex, fieldIndex;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                fieldName = (String) entry.getKey();
                recIndex = ((Integer) entry.getValue()).intValue();
                fieldIndex = getColumnIndex(fieldName);

                /* If the changed calculated field is part of the jTable's key, then if a record
                * is being inserted, check for the "blockingNewRecord" flag.
                */
/*                if (dbTable.dbComponent.blockingNewRecord) {
                    AbstractTableField f = getField(fieldIndex);
                    if (table.isPartOfTableKey(f)) {
                        boolean keyFieldFilled = true;
                        for (int i=0; i<getColumnCount(); i++) {
                            f = getField(i);
                            if (table.isPartOfTableKey(f) && table.riskyGetCell(i, table.recordIndex.get(rowIndex)) == null) {
                                keyFieldFilled = false;
                                break;
                            }
                        }

                        if (keyFieldFilled) {
                            dbTable.dbComponent.blockingNewRecord = false;
                            dbTable.dbComponent.unlockUI();
//                            System.out.println("2Setting pendingNewRecord to: false");
                            table.resetPendingNewRecord();
                        }
                    }
                }
*/
                String colName = getField(fieldIndex).getName();

//                System.out.println("Repainting: " + recIndex + ", " + jTable.jTable.getColumnModel().getColumnIndex(colName));
//                jTable.jTable.paintImmediately(jTable.jTable.getCellRect(recIndex, jTable.jTable.getColumnModel().getColumnIndex(colName), true));
                dbTable.jTable.repaint(dbTable.jTable.getCellRect(recIndex, dbTable.jTable.getColumnModel().getColumnIndex(colName), true));

//                iter.advance();
            }

            dbTable.table.resetChangedCalcCells();
        }
    }


    protected void addRow() throws Exception {
        try{
            dbTable.table.getRecordEntryStructure().startRecordEntry();
        }catch (TableNotExpandableException exc) {
            throw new Exception(exc.message);
        }catch (UnableToAddNewRecordException exc) {
            throw new Exception(exc.message);
        }
    }

}

