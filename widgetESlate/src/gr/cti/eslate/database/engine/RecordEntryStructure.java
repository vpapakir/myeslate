/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 30 Οκτ 2002
 * Time: 12:13:54 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

import java.net.URL;
import java.util.ArrayList;

/** This structure is used to add new records to the Table. It enables fast record addition, since primitive data
 *  values can be used without having to convert them to Objects.
 */
public class RecordEntryStructure {
    Table table = null;
    int pendingRecordIndex = -1;

    RecordEntryStructure(Table table) {
        this.table = table;
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(IntegerTableField field, int value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(DoubleTableField field, double value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(FloatTableField field, float value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(StringTableField field, String value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(BooleanTableField field, Boolean value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(URLTableField field, URL value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(DateTableField field, CDate value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(TimeTableField field, CTime value) {
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(ImageTableField field, CImageIcon value) {
        value.referenceToExternalFile = field.containsLinksToExternalData();
        field.setNewCellValue(value);
    }

    /** Sets the value of the cell of the field <code>field</code> for the new record. */
    public void setCell(AbstractTableField field, Object value) throws InvalidDataFormatException {
        value = table.convertToFieldType(value, field);
        field.setNewCellValue(value);
    }

    /** Returns the value of the cell of the new record at <code>columnIndex</code>. */
    public Object getCell(int columnIndex) {
        return table.tableFields.get(columnIndex).getNewCellValue();
    }

    /** Returns the textual representation of the value of the cell of the new record at <code>columnIndex</code>. */
    public String getCellAsString(int columnIndex) {
        return table.tableFields.get(columnIndex).getNewCellValueAsString();
    }

    /** Returns the table's field at <code>index</code>. */
    public AbstractTableField getField(int index) {
        return table.tableFields.get(index);
    }

    /** This method initiates the addition of a new record to the table. It has to be called before any attempt is made
     *  to set the values of the new record, i.e. before any <code>setCell()</code> is called. If it is not called, then
     *  <code>commitNewRecord()</code> will not do anything.
     *  @return The index of the new record in the Table.
     */
    public int startRecordEntry() throws TableNotExpandableException, UnableToAddNewRecordException {
        if (!table.recordAdditionAllowed)
            throw new TableNotExpandableException(table.bundle.getString("CTableMsg37"));

        if (table.hasKey()) {
            if (!table.dataChangeAllowed)
                throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg93"));
        }

        /* If the table has a key and the insertion of the previous record has not
         * finished, then reject the addition of a new empty record.
         */
        if (pendingRecordIndex != -1)
            throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg99"));

        AbstractTableField f;
        for (int i=0; i<table.tableFields.size(); i++) {
            f = table.tableFields.get(i);
            if (table.tableKey.isPartOfTableKey(f)/*f.isKey()*/ && !f.isEditable() && !f.isCalculated())
                throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg49") + f.getName() + "\"");
            /* If a record is inserted in a table, whose hidden fields are not displayed and at least
            * one of its key fields are hidden, then throw an exception.
            */
//            if (table.tableKey.isPartOfTableKey(f)/*f.isKey()*/ && f.isHidden() && !table.hiddenFieldsDisplayed)
//                throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg92"));

        }
        pendingRecordIndex = table.recordCount;
//System.out.println("1. Setting pendingRecordIndex to: " + pendingRecordIndex);
        return pendingRecordIndex;
    }

    /** Reports if a record addition is pending. */
    public boolean isRecordAdditionPending() {
        return (pendingRecordIndex != -1);
    }

    /** Returns the index of the new record whose addition is pending in the Table. */
    public int getPendingRecordIndex() {
        return pendingRecordIndex;
    }

    /** After the cells of the new records have been set using the <code>setCell()</code> methods, call
     *  <code>commitNewRecord</code> to insert the record to the Table. If the new record violates
     *  the key of the Table, then it will not be accepted.
     *  @param moreToBeAdded This flag is passed to the generated <code>RecordAddedEvent</code>. It gives
     *  the listeners of the Table the chance to know if more records are to be added to the Table and
     *  act approprietely.
     *  @return The index of the new record in the Table.
     *  @exception NoFieldsInTableException If there are no fields in the Table.
     *  @exception DuplicateKeyException If the key violates the table's key.
     */
    public int commitNewRecord(boolean moreToBeAdded) throws NoFieldsInTableException, DuplicateKeyException {
        // If 'startRecordEntry()' hasn't been called, do nothing.
        if (pendingRecordIndex == -1) return -1;

        int fieldCount = table.getFieldCount();
        //Examining if the table has fields
        if (fieldCount == 0)
            throw new NoFieldsInTableException(table.bundle.getString("CTableMsg38"));

        AbstractTableField fld = null;
        for (int i=0; i<fieldCount; i++) {
            fld = table.tableFields.get(i);
            if (fld.isCalculated()) continue;
            fld.commitNewCellValue();
        }
        for (int i=0; i<fieldCount; i++) {
            fld = table.tableFields.get(i);
            if (!fld.isCalculated()) continue;
            fld.commitNewCellValue();
        }

/*        if (table.numOfCalculatedFields > 0) {
            AbstractTableField calcField;
            boolean tempKeyReset = false;
            boolean tempKeyChangeAllowedReset = false;
            for (int i=0; i<fieldCount; i++) {
                calcField = table.tableFields.get(i);
                if (calcField.isCalculated()) {
                    if (table.tableKey.isPartOfTableKey(calcField)) {
                    }
                    try{
                        table.evaluateAndAddCalculatedFieldCell(i, pendingRecordIndex);
                    }catch (DuplicateKeyException e) {System.out.println("Serious inconsistency error in RecordEntryStructure commitNewRecord(): (1)");}
                    catch (NullTableKeyException e) {System.out.println("Serious inconsistency error in RecordEntryStructure commitNewRecord(): (2)");}
                }
            }
        }
*/
        if (table.hasKey()) {
//System.out.println("RecordEntryStructure hasCorrectKeyValues(): " + table.tableKey.hasCorrectKeyValues(pendingRecordIndex));
            try{
                table.tableKey.checkRecordKey(pendingRecordIndex);
            }catch (Throwable thr) {
                for (int i=0; i<fieldCount; i++)
                    table.tableFields.get(i).remove(pendingRecordIndex);
                throw new DuplicateKeyException(thr.getMessage());
            }
            table.tableKey.addRecordToPrimaryKeyHash(new Integer(table.tableFields.get(table.tableKey.primaryKeyFieldIndex).getObjectAt(pendingRecordIndex).hashCode()), pendingRecordIndex);
        }

        table.recordCount++;
        table.unselectedSubset.add(pendingRecordIndex);
        table.recordSelection.add(false);
        table.recordIndex.add(pendingRecordIndex);
        table.setModified();
//System.out.println("2. Setting pendingRecordIndex to: " + pendingRecordIndex);
        pendingRecordIndex = -1;
        table.fireRecordAdded(moreToBeAdded);

        for (int i=0; i<fieldCount; i++)
            table.tableFields.get(i).setNewCellValue(null);

        return table.recordCount-1;
    }

    /** Cancels the process of adding a new record to the Table.
     */
    public void cancelRecordAddition() {
        pendingRecordIndex = -1;
        int fieldCount = table.getFieldCount();
        for (int i=0; i<fieldCount; i++)
            table.tableFields.get(i).setNewCellValue(null);
    }
}
