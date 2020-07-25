/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 21, 2002
 * Time: 4:12:26 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database.engine;


public class TableViewRecordEntryStructure extends RecordEntryStructure {
    /** Holds the same value as the data member <code>table</code>, which is inherited from the superclass. It's
     *  introduced to avoid continuous casts of <code>table</code> to the TableView class.
     */
    TableView tableView = null;
    /** The index of the pending new record in the TableView's underlying Table. This usually differs from the index of
     *  the pending new record in the TableView.
     */
    int baseTablePendingRecordIndex = -1;

    TableViewRecordEntryStructure(Table table) {
        super(table);
        tableView = (TableView) table;
    }

    /** This method initiates the addition of a new record to the TableView. The method of the super class is overriden,
     *  cause the new record will not be directry added to the TableView. It will be added to the underlying Table and as
     *  a result it will be included to the TableView.
     *  @return The index of the new record in the TableView.
     *  @see RecordEntryStructure#startRecordEntry()
     */
    public int startRecordEntry() throws TableNotExpandableException, UnableToAddNewRecordException {
        if (!tableView.recordAdditionAllowed)
            throw new TableNotExpandableException(table.bundle.getString("CTableMsg37"));

        Table baseTable = tableView.getBaseTable();

        if (!baseTable.recordAdditionAllowed)
            throw new TableNotExpandableException(table.bundle.getString("CTableMsg37"));

        if (baseTable.hasKey()) {
            if (!baseTable.dataChangeAllowed)
                throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg93"));
        }

        /* If the table has a key and the insertion of the previous record has not
         * finished, then reject the addition of a new empty record.
         */
        if (pendingRecordIndex != -1)
            throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg99"));

        AbstractTableField f;
        for (int i=0; i<baseTable.tableFields.size(); i++) {
            f = baseTable.tableFields.get(i);
            if (baseTable.tableKey.isPartOfTableKey(f)/*f.isKey()*/ && !f.isEditable() && !f.isCalculated())
                throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg49") + f.getName() + "\"");
            /* If a record is inserted in a table, whose hidden fields are not displayed and at least
            * one of its key fields are hidden, then throw an exception.
            */
//            if (table.tableKey.isPartOfTableKey(f)/*f.isKey()*/ && f.isHidden() && !table.hiddenFieldsDisplayed)
//                throw new UnableToAddNewRecordException(table.bundle.getString("CTableMsg92"));

        }
        pendingRecordIndex = table.recordCount;
        baseTablePendingRecordIndex = baseTable.recordCount;
//System.out.println("1. Setting pendingRecordIndex to: " + pendingRecordIndex);
        return pendingRecordIndex;
    }

    /** After the cells of the new records have been set using the <code>setCell()</code> methods, call
     *  <code>commitNewRecord</code> to insert the record to the Table. If the new record violates
     *  the key of the Table, then it will not be accepted.
     *  @param moreToBeAdded This flag is passed to the generated <code>RecordAddedEvent</code>. It gives
     *  the listeners of the Table the chance to know if more records are to be added to the Table and
     *  act approprietely.
     *  @return The index of the new record in the TableView.
     *  @exception NoFieldsInTableException If there are no fields in the Table.
     *  @exception DuplicateKeyException If the key violates the table's key.
     */
    public int commitNewRecord(boolean moreToBeAdded) throws NoFieldsInTableException, DuplicateKeyException {
        // If 'startRecordEntry()' hasn't been called, do nothing.
        if (pendingRecordIndex == -1) return -1;

        Table baseTable = tableView.getBaseTable();

        int fieldCount = baseTable.getFieldCount();
        //Examining if the table has fields
        if (fieldCount == 0)
            throw new NoFieldsInTableException(table.bundle.getString("CTableMsg38"));

        int fieldViewCount = table.getFieldCount();
        //Examining if the table view has fields
        if (fieldViewCount == 0)
            throw new NoFieldsInTableException(table.bundle.getString("CTableMsg38"));

        AbstractTableField fld = null;
        for (int i=0; i<fieldCount; i++) {
            fld = baseTable.tableFields.get(i);
            if (fld.isCalculated()) continue;
            fld.commitNewCellValue();
        }
        for (int i=0; i<fieldCount; i++) {
            fld = baseTable.tableFields.get(i);
            if (!fld.isCalculated()) continue;
            fld.commitNewCellValue();
        }

/*        if (baseTable.numOfCalculatedFields > 0) {
            AbstractTableField calcField;
//            boolean tempKeyReset = false;
//            boolean tempKeyChangeAllowedReset = false;
            for (int i=0; i<fieldCount; i++) {
                calcField = baseTable.tableFields.get(i);
                if (calcField.isCalculated()) {
                    if (baseTable.tableKey.isPartOfTableKey(calcField)) {
                    }
                    try{
                        baseTable.evaluateAndAddCalculatedFieldCell(i, baseTablePendingRecordIndex);
                    }catch (DuplicateKeyException e) {System.out.println("Serious inconsistency error in RecordEntryStructure commitNewRecord(): (1)");}
                    catch (NullTableKeyException e) {System.out.println("Serious inconsistency error in RecordEntryStructure commitNewRecord(): (2)");}
                }
            }
        }
*/
        if (baseTable.hasKey()) {
//System.out.println("RecordEntryStructure hasCorrectKeyValues(): " + table.tableKey.hasCorrectKeyValues(pendingRecordIndex));
            try{
                baseTable.tableKey.checkRecordKey(baseTablePendingRecordIndex);
            }catch (Throwable thr) {
                for (int i=0; i<fieldCount; i++)
                    baseTable.tableFields.get(i).remove(baseTablePendingRecordIndex);
                throw new DuplicateKeyException(thr.getMessage());
            }
            baseTable.tableKey.addRecordToPrimaryKeyHash(new Integer(baseTable.tableFields.get(baseTable.tableKey.primaryKeyFieldIndex).getObjectAt(baseTablePendingRecordIndex).hashCode()), baseTablePendingRecordIndex);
        }

        baseTable.recordCount++;
        baseTable.unselectedSubset.add(baseTablePendingRecordIndex);
        baseTable.recordSelection.add(false);
        baseTable.recordIndex.add(baseTablePendingRecordIndex);
        baseTable.setModified();
//System.out.println("2. Setting pendingRecordIndex to: " + pendingRecordIndex);
        baseTablePendingRecordIndex = -1;
        pendingRecordIndex = -1;
        baseTable.fireRecordAdded(moreToBeAdded);

        for (int i=0; i<fieldCount; i++)
            baseTable.tableFields.get(i).setNewCellValue(null);

        try{
            tableView.addRecordToView(baseTable.recordCount-1);
        }catch (InvalidRecordIndexException exc) {
            System.out.println("Inconsistency error in TableView addRecord()");
        }

        return tableView.recordCount-1;
    }

    /** Cancels the process of adding a new record to the Table.
     */
    public void cancelRecordAddition() {
        Table baseTable = tableView.getBaseTable();
        pendingRecordIndex = -1;
        baseTablePendingRecordIndex = -1;
        int fieldCount = table.getFieldCount();
        for (int i=0; i<fieldCount; i++)
            baseTable.tableFields.get(i).setNewCellValue(null);
    }

}
