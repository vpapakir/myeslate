package gr.cti.eslate.database.engine;

import gr.cti.eslate.tableModel.event.*;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.database.engine.plugs.TablePlug;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.tableModel.event.*;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.beans.PropertyVetoException;
import java.io.*;

/**
 * <p>Title: TableView</p>
 * <p>Description: A TableView is a view of a Table. A view of a baseTable is defined
 * in the following way:
 * <ul>
 * <li> The records of the TableView are a subset of the records of the Table.
 * <li> The fields of the TableView are a subset of the fields of the Table.
 * <li> TableViews cannot have keys. So a TableView which is created on a keyed
 * Table does not have a key, nor a key can be set for it.
 * <li> Removing a record from a TableView does not affect the underlying Table.
 *      The TableView simply does not contain the record any more.
 * <li> A record can be added to a TableView in two ways. If the record already belongs
 *      to the underlying Table, it is simply included in the TableView too (see
 *      <code>addRecordToView()</code>. If is is a new record, then it is inserted
 *      in the underlying Table and included in the TableView too (see <code>addRecord(),
 *      TableViewRecordEntryStructure</code>).
 * <li> A field can be added to a TableView in two ways. If the field already exists in the
 *      underlying Table, it is simply included in the list of fields of the TableView (see
 *      <code>addFieldToView()</code>). If a new field is created using the standard
 *      Table API (TableView is a subclass of Table), then the new field is first created
 *      in the underlying Table and, if that succeeds, it is also added to the TableView
 *      (see various forms of <code>addField()</code>).
 * <li> When a field is removed from the TableView, it is not removed from the underlying Table.
 *      However when a field is removed from the underlying Table, it is removed from all the TableViews
 *      whice ar based on the Table and contain it. This happens even if removing fields is not allowed
 *      in the TableView.
 * <li> Similarly, when a record is removed from the underlying Table, it is removed from all the TableViews
 *      which contain it, even if removing records is not enabled for some of the TableViews.
 * <li> The records of the TableView have constant order. That means that every time
 * <code>getRecord(recIndex)</code> or <code>getRow(recIndex)</code> is called
 * with the same <code>recIndex</i> the same record values will be returned.
 * TableViews also support view record order through the methods
 * <code>rowForRecord()</code> and <code>recordForRow</code>.
 * </ul>
 * <p>
 * TableViews can be constructed in two ways. The most common is to use the method <code>createTableView()</code> of the
 * Table. In this case the Table constructs the TableView and is responsible to save and restore it. The second way is
 * to directly use the constructor of the TableView. In this case the TableView cannot be stored.
 * </p>
 * <p>
 * TableView storage deserves particular intention. The TableView does not persist the Table it is based on. It persists
 * the rest of its state, but not the Table. So when a TableView is not created using the <code>createTableView()</code>
 * method of a Table (in which case the Table is responsible to restore its views), but directly using the TableView
 * constructor, it can't be stored.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 * @see Table#createTableView(IntBaseArray, IntBaseArray)
 */

public class TableView extends Table implements Externalizable {
    public static final int FORMAT_VERSION = 1;
	static final long serialVersionUID = 12;
	/**
	 * The Table to which this TableView is attached. The TableView provides
	 * a view of this Table.
	 */
	private Table baseTable = null;
	/** Stores the indices of the records of the source baseTable, which are
     *  included in this TableView. The rule is that record at index <code>i</code>
	 *  of the TableView is mapped to record at index <code>records[i]</code> of
	 *  the source baseTable.
	 */
	IntBaseArray records = new IntBaseArray();
	/** A listener which informs the TableView of changes in the structure of
	 * the source Table.
	 */
	ViewTableListener baseTableListener = new ViewTableListener();
    /** This flag is used to prevent the process of events from the underlying Table, when the TableView decides
     *  to do so. Sometimes, like when the data type of a field view changes, the TableView propagates this change
     *  to the underlying Table. The TableView will be notified by the Table that the data type of the specified field
     *  was changed, but must do nothing, cause the TableView has already acted. However, if the data type of a
     *  field was changed in the underlying Table directly, then all the TableViews of the Table must process the event.
     */
    private boolean ignoreEventFromBaseTable = false;
    /** Determines if the active record of the TableView will be syncronized with the active record of the base Table.*/
    private boolean synchronizedActiveRecord = false;

    /** The only reason of existence of this constructor is to be used by the Externalization mechanism */
    public TableView() {
    }

    /** Constructs a TableView on <code>table</code>.
     * @param table The underlying Table.
     * @param fieldIndices The indices of the fields in <code>table</code>, which will be included in the TableView.
     * @param recordIndices The indices of the records in <code>table</code>, which will be included in the TableView.
     */
    public TableView(Table table, IntBaseArray fieldIndices, IntBaseArray recordIndices)
            throws InvalidRecordIndexException, InvalidFieldIndexException {
		this.baseTable = table;
		initTableView(table);
        baseTable.addTableModelListener(baseTableListener);
        try{
    		setViewFields(fieldIndices);
		    setViewRecords(recordIndices);
        }catch (TableNotExpandableException exc) {
            System.out.println("TableNotExpandableException while creating view. This should never occur.");
            throw new RuntimeException("TableView intenal error...");
        }catch (CalculatedFieldExistsException exc) {
            System.out.println("CalculatedFieldExistsException while creating view. This should never occur.");
            throw new RuntimeException("TableView intenal error...");
        }catch (FieldNotRemovableException exc) {
            System.out.println("FieldNotRemovableException while creating view. This should never occur.");
            throw new RuntimeException("TableView intenal error...");
        }catch (AttributeLockedException exc) {
            System.out.println("FieldNotRemovableException while creating view. This should never occur.");
            throw new RuntimeException("TableView intenal error...");
        }
		resetModified();
    }

	/**
	 * Sets the records of this TableView. The records must be a subset of the
	 * records of the Table this TableView is mapped to.
	 * @param recordIndices The array with the record indices.
	 */
	public void setViewRecords(IntBaseArray recordIndices) throws InvalidRecordIndexException, TableNotExpandableException {
		if (recordIndices == null) {
			throw new IllegalArgumentException("The record array of a TableView cannot be null");
		}

        // Remove 1 by 1 all the existing records from the TableView. This gives the listeners the chance
        // to respond to the change of records.
        for (int i=records.size()-1; i>0; i--) {
            removeRecordInternal(i, true, null);
        }
//System.out.println("setViewRecords() records.size(): " + records.size() + ", view: " + this);
        if (records.size() > 0)
            removeRecordInternal(0, false, null);

        // Clean-up, just to be sure...
		selectedSubset.clear();
		unselectedSubset.clear();
        recordSelection.clear();
        recordIndex.clear();

        // Add the new set of records to the TableView. Add them 1 by 1 so that the listeners are notified for every
        // record that is added.
        if (tableFields.size() > 0) {
            for (int i=0; i<recordIndices.size()-1; i++) {
                addRecordToViewInternal(recordIndices.get(i), true);
            }
            if (recordIndices.size() > 0)
                addRecordToViewInternal(recordIndices.get(recordIndices.size()-1), false);
        }else
            records = (IntBaseArray) recordIndices.clone();

        // No active record at start
        activeRecord = -1;
        clearFieldSortFlags();
		setModified();
	}

    /** This is used only when a TableView is restored in order to assign back its underlying Table. */
    void setViewTable(Table table) {
        this.baseTable = table;
        baseTable.addTableModelListener(baseTableListener);
//System.out.println("setViewTable() table: " + table.getTitle() + "this: " + this.getTitle() + ", tableFields.size(): " + tableFields.size());
        for (int i=0; i<tableFields.size(); i++) {
//System.out.println("setViewTable() tableFields.get(i): " + tableFields.get(i));
            tableFields.get(i).setTable(this);
        }
    }

	/**
	 * Sets the fields of this TableView. The fields must be a subset of the
	 * fields of the Table this TableView is based on.
	 * @param fieldIndices The indices of the fields in the Table the view is based on, to be included in the TableView.
	 */
	public void setViewFields(IntBaseArray fieldIndices) throws TableNotExpandableException, CalculatedFieldExistsException,
            FieldNotRemovableException, AttributeLockedException, InvalidFieldIndexException {
		if (fieldIndices == null) {
			throw new IllegalArgumentException("The field indices array of a TableView cannot be null");
		}

        IntBaseArray recordsBck = (IntBaseArray) records.clone();
        // First remove 1 by 1 all the existing fields of the TableView. This give the listeners the chance to
        // respond to field removal.
        for (int i=tableFields.size()-1; i>0; i--) {
            removeFieldView((TableFieldView) tableFields.get(i), true);
        }
        if (tableFields.size() > 0)
            removeFieldView((TableFieldView) tableFields.get(0), false);

        // Add the nw fields 1 by 1 to the TableView
        for (int i=0; i<fieldIndices.size()-1; i++) {
            addFieldToView(fieldIndices.get(i), true);
        }
        if (fieldIndices.size() > 0) {
            addFieldToView(fieldIndices.get(fieldIndices.size()-1), false);
        }

        records.clear();
        if (tableFields.size() > 0) {
            try{
                setViewRecords(recordsBck);
            }catch (InvalidRecordIndexException exc) {
                exc.printStackTrace();
                System.out.println("InvalidRecordIndexException in setViewFields(). Should never occur...");
            }
        }

        clearFieldSortFlags();
		setModified();
	}

/*	private void checkMinMaxIndices(IntBaseArray checkedArray, int maxIndex) {
		int size = checkedArray.size();
		if (size != 0) {
			boolean negativeIndexFound = false;
			int maxRecordIndex = checkedArray.get(0);
			int recIndex = -1;
			for (int i=1; i<size; i++) {
				recIndex = checkedArray.get(i);
				if (maxRecordIndex < recIndex) maxRecordIndex = recIndex;
				if (recIndex < 0) {
					negativeIndexFound = true;
					break;
				}
			}
			if (negativeIndexFound || maxRecordIndex > maxIndex) {
				throw new IllegalArgumentException("The indices are invalid. Either a negative index or a index greater than the Table's record/field count was encountered.");
			}
		}
	}
*/
	/**
	 * Adds the record at <code>recIndex</code> in the source Table to this TableView.
	 * If record at <code>recIndex</code> is already part of this TableView, or
	 * it is invalid (there is no such index in Table), addition fails.
	 * @param recIndex The index of the record in the Table.
	 * @return True, if addition succeeds. False otherwise.
	 * @exception InvalidRecordIndexException If the recordIndex is less than 0 or
	 * greater that the number of records in the source Table.
	 */
	public boolean addRecordToView(int recIndex) throws InvalidRecordIndexException {
        return addRecordToView(recIndex, false);
    }

    /**
     * Adds the record at <code>recIndex</code> in the source Table to this TableView.
     * If record at <code>recIndex</code> is already part of this TableView, or
     * it is invalid (there is no such index in Table), addition fails.
     * @param recIndex The index of the record in the Table.
     * @param moreToBeAdded Flag which signals that more record additions will follow. This flag is carried by the
     *        RecordAddedEvent.
     * @return True, if addition succeeds. False otherwise.
     * @exception InvalidRecordIndexException If the recordIndex is less than 0 or
     * greater that the number of records in the source Table.
     */
    public boolean addRecordToView(int recIndex, boolean moreToBeAdded) throws InvalidRecordIndexException {
		if (records.indexOf(recIndex) != -1)
			return false;

        addRecordToViewInternal(recIndex, moreToBeAdded);
        return true;
	}

    /**
     * Adds the record at <code>recIndex</code> in the source Table to this TableView.
     * If <code>recIndex</code> is invalid (there is no such index in the base Table), addition fails.
     * This method does not perform the 'heavy' check if the TableView already contains the specified record of
     * the base Table, so use with care.
     * @param recIndex The index of the record in the Table.
     * @param moreToBeAdded Flag which signals that more record additions will follow. This flag is carried by the
     *        RecordAddedEvent.
     * @exception InvalidRecordIndexException If the recordIndex is less than 0 or
     * greater that the number of records in the source Table.
     */
    private void addRecordToViewInternal(int recIndex, boolean moreToBeAdded) throws InvalidRecordIndexException {
        if (recIndex < 0 && recIndex >= baseTable.recordCount)
            throw new InvalidRecordIndexException("Invalid record index: " + recIndex);

//System.out.println("============================== addRecordToViewInternal() ===========================");
//Thread.currentThread().dumpStack();
        records.add(recIndex);
        unselectedSubset.add(recordCount);
        recordIndex.add(recordCount);
        recordSelection.add(false);
        recordCount++;

        setModified();
        fireRecordAdded(moreToBeAdded);
    }

	/**
	 * Adds a set of records to this TableView.
	 * @param recordIndices The indices of the records in Table to be added
	 * @return True, if all the records were added successfully. False, if at
	 * least one record addition falied.
	 * @see TableView#addRecordToView(int)
	 * @exception InvalidRecordIndexException If any recordIndex is less than 0 or
	 * greater that the number of records in the source Table.
	 */
	public boolean addRecordToView(int[] recordIndices) throws InvalidRecordIndexException {
		boolean anyFailure = false;
        // Find the last valid record in 'recordIndices[]' to be added to the TableView.
        int lastValidIndex = -1;
        for (int i=recordIndices.length-1; i>=0; i--) {
            int recIndex = recordIndices[i];
            if (records.indexOf(recIndex) == -1) {
                if (recIndex >= 0 && recIndex < baseTable.recordCount) {
                    lastValidIndex = i;
                    break;
                }
            }
        }
		for (int i=0; i<lastValidIndex; i++) {
			if (!addRecordToView(recordIndices[i], true)) {
				anyFailure = true;
			}
		}
        addRecordToView(recordIndices[lastValidIndex], false);
		return anyFailure;
	}

	/**
	 * Adds a set of records to this TableView.
	 * @param recordIndices The indices of the records in Table to be added.
	 * @return True, if all the records were added successfully. False, if at
	 * least one record addition falied.
	 * @see TableView#addRecordToView(int)
	 * @exception InvalidRecordIndexException If any recordIndex is less than 0 or
	 * greater that the number of records in the source Table.
	 */
	public boolean addRecordToView(IntBaseArray recordIndices) throws InvalidRecordIndexException {
		int[] recordIndicesArray = recordIndices.toArray();
		return addRecordToView(recordIndicesArray);
	}

	/**
	 * Adds the field at <code>fieldIndex</code> in the Table to this TableView.
	 * If the field is already part of this TableView, or
	 * it is invalid (there is no such field index in Table), addition fails.
	 * @param fieldIndex The index of the field in the Table.
	 * @return The new field view or <code>null</code> if addition fails.
	 * @exception InvalidFieldIndexException If the source Table does not have any
	 * field at <code>fieldIndex</code>.
	 */
	public TableFieldView addFieldToView(int fieldIndex, boolean moreToAdd) throws InvalidFieldIndexException, AttributeLockedException {
        if (!fieldAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg88"));
        AbstractTableField field = baseTable.getTableField(fieldIndex);
        if (getFieldView(field) != null) return null;
//System.out.println("Adding field view for field: " + field.getName());

        AbstractTableField fieldView = (AbstractTableField) AbstractTableField.createNewFieldView(field);
//System.out.println("field: " + field + ", fieldView: " + fieldView);
//System.out.println("addFieldToView() field: " + field.getName() + ", fieldView: " + fieldView.getName());
        fieldView.indexInTable = tableFields.size();
        fieldView.setTable(this);
	    tableFields.add(fieldView);

        // Copy the TableFields of the baseTable to the 'tableFields' of the TableView.
///        if (newField.isCalculated) numOfCalculatedFields++;

        setModified();
        fireColumnAdded(fieldView, moreToAdd);
        return (TableFieldView) fieldView;
	}

	/**
	 * Adds a set of fields to this TableView.
	 * @param fieldIndices The indices of the fields in Table to be added.
	 * @return True, if all the fields were added successfully. False, if at
	 * least one field addition falied.
	 * @see TableView#addRecordToView(int)
	 * @exception InvalidFieldIndexException If the source Table does not have any
	 * field at <code>fieldIndex</code>.
	 */
	public boolean addFieldToView(int[] fieldIndices) throws InvalidFieldIndexException, AttributeLockedException {
		boolean anyFailure = false;
		for (int i=0; i<fieldIndices.length-1; i++) {
			if (addFieldToView(fieldIndices[i], true) == null) {
				anyFailure = true;
			}
		}
        if (fieldIndices.length > 0)
            addFieldToView(fieldIndices[fieldIndices.length-1], false);
		return anyFailure;
	}

	/**
	 * Adds a set of fields to this TableView.
	 * @param fieldIndices The indices of the fields in Table to be added.
	 * @return True, if all the fields were added successfully. False, if at
	 * least one field addition falied.
	 * @see TableView#addFieldToView(int, boolean)
	 * @exception InvalidFieldIndexException If the source Table does not have any
	 * field at <code>fieldIndex</code>.
	 */
	public boolean addFieldToView(IntBaseArray fieldIndices) throws InvalidFieldIndexException, AttributeLockedException {
		int[] fieldIndicesArray = fieldIndices.toArray();
		return addFieldToView(fieldIndicesArray);
	}

	/**
	 * Adds the field <code>fieldName</code> in the Table to this TableView.
	 * If the field is already part of this TableView, or
	 * it is invalid (there is no such field field in Table), addition fails.
	 * @param fieldName The name of the field in the Table.
     * @param moreToBeAdded If more fields will be added subsequently.
	 * @return The new field view, or null if the addition fails.
	 * @exception InvalidFieldNameException If there is no field with this name in the
	 * source Table.
	 */
	public TableFieldView addFieldToView(String fieldName, boolean moreToBeAdded) throws InvalidFieldNameException, AttributeLockedException {
		int fieldIndex = baseTable.getFieldIndex(fieldName);
		try{
			return addFieldToView(fieldIndex, moreToBeAdded);
		}catch (InvalidFieldIndexException exc) {
			System.out.println("Serious inconsistency error in TableView addFieldToView(String fieldName)");
			return null;
		}
	}

	/**
	 * Adds a set of fields to this TableView.
	 * @param fieldNames The names of the fields in Table to be added.
	 * @return True, if all the fields were added successfully. False, if at
	 * least one field addition failed.
	 * @see TableView#addFieldToView(String, boolean)
	 * @exception InvalidFieldNameException If at least one field name does not
	 * corrspond to a field in the source Table.
	 */
	public boolean addFieldToView(String[] fieldNames) throws InvalidFieldNameException, AttributeLockedException {
		boolean anyFailure = false;
		for (int i=0; i<fieldNames.length; i++) {
			if (addFieldToView(fieldNames[i], false) == null) {
				anyFailure = true;
			}
		}
		return anyFailure;
	}

	/**
	 * Adds a set of fields to this TableView.
	 * @param fieldNames The names of the fields in Table to be added.
	 * @return True, if all the fields were added successfully. False, if at
	 * least one field addition failed.
	 * @see TableView#addFieldToView(String, boolean)
	 * @exception InvalidFieldNameException If at least one field name does not
	 * correspond to a field in the source Table.
	 */
	public boolean addFieldToView(StringBaseArray fieldNames) throws InvalidFieldNameException, AttributeLockedException {
		String[] fieldNamesArray = fieldNames.toArray();
		return addFieldToView(fieldNamesArray);
	}

	/**
	 * Removes a record from the TableView.
	 * @param recordIndex The index in the source Table of the record to be removed from this TableView.
     * @param isChanging Signals if more record removals will follow.
	 * @exception InvalidRecordIndexException If this TableView does contain record at
	 * <code>recordIndex</code> of the source Table.
	 * @exception TableNotExpandableException If this TableView does allow record removal.
	 */
	public void removeRecordFromView(int recordIndex, boolean isChanging) throws InvalidRecordIndexException, TableNotExpandableException {
        removeRecordFromView(recordIndex, isChanging, null);
    }

    /**
     * Removes a record from the TableView.
     * @param recordIndex The index in the source Table of the record to be removed from this TableView.
     * @param isChanging Signals if more record removals will follow.
     * @param removedRecord The data of the record to be removed.
     * @exception InvalidRecordIndexException If this TableView does contain record at
     * <code>recordIndex</code> of the source Table.
     * @exception TableNotExpandableException If this TableView does allow record removal.
     */
    private void removeRecordFromView(int recordIndex, boolean isChanging, ArrayList removedRecord) throws InvalidRecordIndexException, TableNotExpandableException {
		int recIndex = records.indexOf(recordIndex);
		if (recIndex == -1) {
			throw new InvalidRecordIndexException("Invalid record index: " + recordIndex);
		}
        removeRecordInternal(recIndex, isChanging, removedRecord);
		setModified();
		return;
	}

    /** Removes the record at <code>recIndex</code> of the TableView.
     * @param recIndex The index of the record in the TableView.
     * @param isChanging Flag used in the <code>RecordRemovedEvent</code>, which signals if more record removal will folllow.
     * @param removedRecord The Arraylist with the data of the removed record.
     * @throws InvalidRecordIndexException
     * @throws TableNotExpandableException
     */
    private void removeRecordInternal(int recIndex, boolean isChanging, ArrayList removedRecord)
            throws InvalidRecordIndexException, TableNotExpandableException {
        int rowIndex = rowForRecord(recIndex);
//        recordSelection.add(false);
        super.removeRecord(recIndex, rowIndex, isChanging, false, removedRecord);

        records.remove(recIndex);
        if (!isChanging) {
            // Make sure the views active record is synchronized with the underlying table's, if it is requested to be
            // so. This has to be explicitly done after record(s) are removed, cause though setActiveRecord() is called
            // by 'super.removeRecord()' if the new active record index is the same as the previous active record index,
            // then the check for the new active record being the same as from the previous one will succeed and the method
            // will return, i.e.  the code that follows which synchronizes the active records won't be executed.
            if (synchronizedActiveRecord) {
                int baseTableActiveRecord = (activeRecord == -1)? -1:records.get(activeRecord);
                if (baseTableActiveRecord != baseTable.getActiveRecord())
                    baseTable.setActiveRecord(baseTableActiveRecord);
            }

        }
    }

    String getRecordIndexStr() {
        StringBuffer buff = new StringBuffer("RecordIndex: ");
        for (int i=0; i<recordIndex.size(); i++)
            buff.append(recordIndex.get(i) + ", ");
        return buff.toString();
    }

    String getRecordsStr() {
        StringBuffer buff = new StringBuffer("Records: ");
        for (int i=0; i<records.size(); i++)
            buff.append(records.get(i) + ", ");
        return buff.toString();
    }
	/**
	 * Removes a sets of records from the TableView.
	 * @param records The indices in the source Table of the records to be
	 * removed from this TableView.
	 * @see TableView#removeRecordFromView
	 * @exception InvalidRecordIndexException If this TableView does contain any of the
	 * specified <code>records</code> of the source Table.
	 * @exception TableNotExpandableException If this TableView does allow record removal.
	 */
	public void removeRecordFromView(int[] records) throws InvalidRecordIndexException, TableNotExpandableException {
		for (int i=0; i<records.length-1; i++) {
			removeRecordFromView(records[i], true);
		}
		removeRecordFromView(records[records.length-1], false);
	}

	/**
	 * Removes a sets of records from the TableView.
	 * @param records The indices in the source Table of the records to be
	 * removed from this TableView.
	 * @see TableView#removeRecordFromView
	 * @exception InvalidRecordIndexException If this TableView does contain any of the
	 * specified <code>records</code> of the source Table.
	 * @exception TableNotExpandableException If this TableView does allow record removal.
	 */
	public void removeRecordFromView(IntBaseArray records) throws InvalidRecordIndexException, TableNotExpandableException {
		int[] recordsArray = records.toArray();
		removeRecordFromView(recordsArray);
	}

	/**
	 * Removes a field view from the TableView.
	 * @param fieldIndex The index in the source Table of the field to be removed
	 * from this TableView.
     * @param moreToBeRemoved <code>true</code>, if more fields removals will follow.
	 * @exception InvalidFieldIndexException If field at <code>fieldIndex</code> of
	 * the source Table is not included in this TableView.
	 * @see Table#removeField(String, boolean)
	 */
	public void removeFieldFromView(int fieldIndex, boolean moreToBeRemoved) throws InvalidFieldIndexException,
	TableNotExpandableException, CalculatedFieldExistsException, FieldNotRemovableException,
	AttributeLockedException {
        AbstractTableField field = baseTable.getTableField(fieldIndex);
//System.out.println("removeFieldFromView() field: " + field.getName());
        TableFieldView fieldView = getFieldView(field);
		if (fieldView == null) {
			throw new InvalidFieldIndexException("The TableView does not contain field index " + fieldIndex + " of the source Table");
		}
        removeFieldView(fieldView, moreToBeRemoved);
//		fireColumnRemoved(removedField);
	}

    /** Removes the specified TableFieldView from the TableView.
     * @param fieldView The TableFieldView to be removed from the TableView.
     * @param moreToBeRemoved Whether more field removals will follow. This info is carried by the <code>FieldRemovedEvent</code>.
     * @throws TableNotExpandableException
     * @throws CalculatedFieldExistsException
     * @throws FieldNotRemovableException
     * @throws AttributeLockedException
     * @see Table#removeField(String, boolean)
     */
    private void removeFieldView(TableFieldView fieldView, boolean moreToBeRemoved) throws TableNotExpandableException, CalculatedFieldExistsException,
            FieldNotRemovableException, AttributeLockedException {
//System.out.println("removeFieldView() fieldView: " + ((AbstractTableField) fieldView).getName());
        AbstractTableField removedField = (AbstractTableField) fieldView;
//System.out.println("removeFieldFromView() removedField.indexInTable: " + removedField.indexInTable);
		_removeField(removedField, moreToBeRemoved);
		setModified();
    }

	/**
	 * Removes a sets of fields from the TableView.
	 * @param fieldIndices The indices in the source Table of the fields to be
	 * removed from this TableView.
	 * @see TableView#removeFieldFromView
	 * @exception InvalidFieldIndexException If any of the specified fields of
	 * the source Table is not included in this TableView.
	 * @see Table#removeField(String, boolean)
	 */
	public void removeFieldFromView(int[] fieldIndices) throws InvalidFieldIndexException,
	TableNotExpandableException, CalculatedFieldExistsException, FieldNotRemovableException,
	AttributeLockedException {
        if (fieldIndices.length == 0) return;
		for (int i=fieldIndices.length-1; i>0; i--) {
			removeFieldFromView(fieldIndices[i], true);
		}
        removeFieldFromView(fieldIndices[0], false);
	}

	/**
	 * Removes a sets of fields from the TableView.
	 * @param fieldIndices The indices in the source Table of the fields to be
	 * removed from this TableView.
	 * @see TableView#removeFieldFromView
	 * @exception InvalidFieldIndexException If any of the specified fields of
	 * the source Table is not included in this TableView.
	 * @see Table#removeField(String, boolean)
	 */
	public void removeFieldFromView(IntBaseArray fieldIndices) throws InvalidFieldIndexException,
	TableNotExpandableException, CalculatedFieldExistsException, FieldNotRemovableException,
	AttributeLockedException {
		int[] fieldArray = fieldIndices.toArray();
		removeFieldFromView(fieldArray);
	}

	/**
	 * @see Table#addCalculatedField(String, String, boolean, boolean)
	 */
	public AbstractTableField addCalculatedField(String fieldName, String formula, boolean isRemovable, boolean isEditingFormula)
	throws IllegalCalculatedFieldException, InvalidFormulaException, InvalidFieldNameException,
	InvalidKeyFieldException, AttributeLockedException {
		AbstractTableField fld = baseTable.addCalculatedField(fieldName, formula, isRemovable, isEditingFormula);
		AbstractTableField field = (AbstractTableField) addFieldToView(fld.getName(), false);
		return field;
	}

	/**
	 * @see Table#addField(String, Class)
	 */
	public AbstractTableField addField(String fieldName, Class type) throws InvalidFieldNameException,
	InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
		AbstractTableField fld = baseTable.addField(fieldName, type);
		AbstractTableField field = (AbstractTableField) addFieldToView(fld.getName(), false);
		return field;
	}

    /**
     * @see Table#addField(AbstractTableField)
     */
    public AbstractTableField addField(AbstractTableField field) throws IllegalCalculatedFieldException, InvalidFormulaException,
            InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
        AbstractTableField fld = baseTable.addField(field);
        AbstractTableField fieldView = (AbstractTableField) addFieldToView(fld.getName(), false);
        return fieldView;
    }

	/**
	 * @see Table#addField(String, Class, boolean, boolean, boolean)
	 */
	public AbstractTableField addField(String fieldName, Class type, boolean isEditable, boolean isRemovable, boolean isHidden)
	throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
	AttributeLockedException {
		AbstractTableField fld = baseTable.addField(fieldName, type, isEditable, isRemovable, isHidden);
		AbstractTableField field = (AbstractTableField) addFieldToView(fld.getName(), false);
		return field;
	}

	/**
	 * @see Table#addImageField(String, boolean, boolean, boolean, boolean)
	 */
	public AbstractTableField addImageField(String fieldName, boolean isEditable, boolean isRemovable, boolean isHidden, boolean containsLinksToExternalData)
	throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
	AttributeLockedException {
		AbstractTableField fld = baseTable.addImageField(fieldName, isEditable, isRemovable, isHidden, containsLinksToExternalData);
		AbstractTableField field = (AbstractTableField) addFieldToView(fld.getName(), false);
		return field;
	}

	/**
	 * @see Table#addKeyField(String, Class, boolean)
	 */
	public AbstractTableField addKeyField(String fieldName, Class type, boolean isEditable) throws InvalidFieldNameException,
	InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
		AbstractTableField fld = baseTable.addKeyField(fieldName, type, isEditable);
		AbstractTableField field = (AbstractTableField) addFieldToView(fld.getName(), false);
		return field;
	}

	/**
	 * @see Table#addRecord(ArrayList, boolean)
	 */
	public boolean addRecord(ArrayList recEntryForm) throws InvalidDataFormatException,
	NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException {
        if (!recordAdditionAllowed) {
            throw new TableNotExpandableException(bundle.getString("CTableMsg37"));
        }
        //Examining if the baseTable has fields
        if (tableFields.size() == 0)
            throw new NoFieldsInTableException(bundle.getString("CTableMsg38"));

		if (baseTable.addRecord(recEntryForm, false)) {
			try{
				addRecordToView(baseTable.recordCount-1);
			}catch (InvalidRecordIndexException exc) {
				System.out.println("Inconsistency error in TableView addRecord()");
			}
			return true;
		}
		return false;
	}

	public ESlateHandle getESlateHandle() {
//		ESlateHandle tableHandle = baseTable.getESlateHandle();
		if (handle == null) {
			PerformanceManager pm = PerformanceManager.getPerformanceManager();
			pm.eSlateAspectInitStarted(this);
			pm.init(initESlateAspectTimer);
			handle = ESlate.registerPart(this);

			try{
				handle.setUniqueComponentName(title); //bundle.getString("TableView"));
			}catch (RenamingForbiddenException exc) {}
			try {
				tablePlug = new TablePlug(this); //soClass, tableSO);
                tablePlug.setHostingPlug(false);
				handle.addPlug(tablePlug);
				handle.addPlug(tablePlug.getRecordPlug());
			}catch (Exception exc) {
				exc.printStackTrace();
				System.out.println("Something went wrong during TablePlug creation: " + exc.getMessage());
			}

			handle.addESlateListener(new ESlateAdapter() {
				public void handleDisposed(HandleDisposalEvent e) {
					PerformanceManager pm = PerformanceManager.getPerformanceManager();
					pm.removePerformanceListener(perfListener);
					perfListener = null;
                    baseTable.removeTableModelListener(baseTableListener);
                    baseTable = null;
				}
			});
			handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.TablePrimitives");

//			tableHandle.add(handle);

			pm.eSlateAspectInitEnded(this);
			pm.stop(initESlateAspectTimer);
			pm.displayTime(initESlateAspectTimer, handle, "", "ms");

		}

		return handle;
	}

	/**
	 *  Removes a field from the TableView structure. However, it does not remove the data of the field
	 *  from the Table data repository. This method should not be used together with the <i>removeField</i>
	 *  method. It is useful only in these cases, where the application which uses this API, e.g. the database
	 *  GUI, needs to delete the field's data itself. This case was raised using the <i>Java foundation classes' JTable</i> component.
	 *  @param fieldName The name of the field to be deleted.
	 *  @exception InvalidFieldNameException If no field exists with the specified <i>fieldName</i>.
	 *  @exception TableNotExpandableException If the Table is not expandable.
	 *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
	 *  @exception FieldNotRemovableException If the field is not removable.
	 *  @exception AttributeLockedException If fields cannot be removed from this baseTable.
	 */
	public void prepareRemoveField(String fieldName) throws InvalidFieldNameException,
	TableNotExpandableException, CalculatedFieldExistsException,
	FieldNotRemovableException, AttributeLockedException {
		try{
			int fieldIndex = baseTable.getFieldIndex(fieldName);
			removeFieldFromView(fieldIndex, false);
		}catch (InvalidFieldIndexException exc) {
			throw new InvalidFieldNameException("Invalid field name " + fieldName);
		}
//		super.prepareRemoveField(fieldName);
	}

	/**
	 *  Removes a field from the TableView. The field is not removed from the Table.
	 *  @param fieldName The name of the field to be deleted.
	 *  @exception InvalidFieldNameException If no field exists with the specified <i>fieldName</i>.
	 *  @exception TableNotExpandableException If the Table is not expandable.
	 *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
	 *  @exception FieldNotRemovableException If the field is not removable.
	 *  @exception AttributeLockedException If fields cannot be removed from this baseTable.
	 */
	public void removeField(String fieldName) throws InvalidFieldNameException,
	TableNotExpandableException, CalculatedFieldExistsException,
	FieldNotRemovableException, AttributeLockedException {
		try{
			int fieldIndex = baseTable.getFieldIndex(fieldName);
//System.out.println("removeField() fieldIndex: " + fieldIndex);
			removeFieldFromView(fieldIndex, false);
		}catch (InvalidFieldIndexException exc) {
			throw new InvalidFieldNameException("Invalid field name " + fieldName);
		}
	}

    /**
     *  Removes a field from the TableView. The field is not removed from the Table.
     *  @param fieldName The name of the field to be deleted.
     *  @param moreToBeRemoved <code>true</code>, if more fields are to be removed.
     *  @exception InvalidFieldNameException If no field exists with the specified <i>fieldName</i>.
     *  @exception TableNotExpandableException If the Table is not expandable.
     *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this baseTable.
     */
    public void removeField(String fieldName, boolean moreToBeRemoved) throws InvalidFieldNameException,
    TableNotExpandableException, CalculatedFieldExistsException,
    FieldNotRemovableException, AttributeLockedException {
        try{
            int fieldIndex = baseTable.getFieldIndex(fieldName);
//System.out.println("removeField() fieldIndex: " + fieldIndex);
            removeFieldFromView(fieldIndex, moreToBeRemoved);
        }catch (InvalidFieldIndexException exc) {
            throw new InvalidFieldNameException("Invalid field name " + fieldName);
        }
    }

    /**
     *  Removes all the TableView fields. The fields are not removed from the underlying Table.
     *  @exception TableNotExpandableException If the Table is not expandable.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this baseTable.
     */
    public void removeAllFields() throws TableNotExpandableException, FieldNotRemovableException, AttributeLockedException {
        if (tableFields.size() == 0) return;
        // Remove all the fields but the first one.
        for (int i=tableFields.size()-1; i>0; i--) {
            try{
                removeField(tableFields.get(i).getName(), true);
            }catch (CalculatedFieldExistsException exc) {
               System.out.println("Inconstistency error in Table.removeAllFields() 1. Operation halted...");
                exc.printStackTrace();
                return;
            }catch (InvalidFieldNameException exc) {
               System.out.println("Inconstistency error in Table.removeAllFields() 2. Operation halted...");
                exc.printStackTrace();
                return;
            }
        }
        // Now remove the first field of the TableView.
        try{
            removeField(tableFields.get(0).getName(), false);
        }catch (CalculatedFieldExistsException exc) {
           System.out.println("Inconstistency error in Table.removeAllFields() 2. Operation halted...");
            exc.printStackTrace();
            return;
        }catch (InvalidFieldNameException exc) {
           System.out.println("Inconstistency error in Table.removeAllFields() 2. Operation halted...");
            exc.printStackTrace();
            return;
        }

    }

	public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
//System.out.println("------------------ TABLEVIEW READEXTERNAL() ---------------------------");
		PerformanceManager pm = PerformanceManager.getPerformanceManager();
		ESlateHandle h = getESlateHandle();
		pm.init(loadTimer);
		pm.init(fieldMapTimer);
		StorageStructure fieldMap = (StorageStructure) in.readObject();
		pm.stop(fieldMapTimer);
		pm.displayTime(fieldMapTimer, h, "", "ms");

//        records = (IntBaseArray) fieldMap.get("TableView records");
//        if (records == null) {
            ESlateFieldMap2 viewAdditionalState = (ESlateFieldMap2) fieldMap.get("TableView additional state");
            records = (IntBaseArray) viewAdditionalState.get("TableView records");
            synchronizedActiveRecord = viewAdditionalState.get("SynchronizedActiveRecord", false);
//        }

		applyState(fieldMap);
        // The following should happen only when the Table of the TableView is re-assigned using setViewTable().
        // This is because only then can the references to the Table's AbstractTableFields can be restored in
        // the setTable() of the TableFieldViews of the TableView.
//        for (int i=0; i<tableFields.size(); i++) {
//            AbstractTableField field = tableFields.get(i);
//            field.setTable(this); //Re-assign the field's table
//        }
		isModified = false;
		pm.stop(loadTimer);
//System.out.println("Table readExternal() getESlateHandle(): " + getESlateHandle() + ", time: " + (System.currentTimeMillis()-start));
		pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
	}

	/** Returns the state of the TableView in a StorageStructure.
	 */
	public StorageStructure recordState() throws IOException {
		StorageStructure fieldMap = super.recordState();
		// The 'tableData', tableKey, keyChangeAllowed should not be saved
		fieldMap.removeKey("Table data");
        fieldMap.removeKey("Table key");
        fieldMap.removeKey("Key change allowed");
        ESlateFieldMap2 tableViewAdditionalState = new ESlateFieldMap2(FORMAT_VERSION);
		tableViewAdditionalState.put("TableView records", records);
        tableViewAdditionalState.put("SynchronizedActiveRecord", synchronizedActiveRecord);
        fieldMap.put("TableView additional state", tableViewAdditionalState);
		return fieldMap;
	}

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);

        StorageStructure fieldMap = recordState();
        out.writeObject(fieldMap);
//System.out.println("Saved table: " + getTitle());
        isModified = false;
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
// throw new RuntimeException(); // for test purposes
    }

    /**
     *  Removes all the records of the TableView. The records are not removed from the underlying Table.
     *  @exception TableNotExpandableException If the base table is not expandable.
     */
    public void removeAllRecords() throws TableNotExpandableException {
        if (recordCount == 0) return;
        for (int i=records.size()-1; i>0; i--) {
            try{
                removeRecordFromView(records.get(i), true);
            }catch (InvalidRecordIndexException exc) {
                System.out.println("Inconsistency error in TableView.removeRecord() 1. Operation halted...");
                exc.printStackTrace();
                return;
            }
        }
        try{
            removeRecordFromView(records.get(0), false);
        }catch (InvalidRecordIndexException exc) {
            System.out.println("Inconsistency error in TableView.removeRecord() 1. Operation halted...");
            exc.printStackTrace();
            return;
        }
    }

	/**
	 *  Removes the record at <i>recIndex</i> from the TableView. The record is
	 *  not removed from the Table.
	 *  @param recIndex The index of the record to be deleted.
	 *  @param rowIndex The index of the record to be deleted in the GUI component. This index
	 *  is not used by <i>removeRecord</i>. It is simply passed as an argument to the generated
	 *  <i>RecordRemovedEvent</i>. <i>rowIndex</i> defaults to -1 for calls to <i>removeRecord</i>,
	 *  which are made internally by other methods of the Table API.
	 *  @param isChanging This parameter is also of no importance to <i>removeRecord</i>. It is
	 *  passed to the generated event and is used by the listeners in order to decide when
	 *  the GUI component that displays the Table is to be repainted, as a result of the record
	 *  removal. When multiple records are removed one after another, the GUI component can
	 *  watch this flag in order to get redrawn once, after the last record removal.
	 *  @exception TableNotExpandableException If the baseTable is not expandable.
	 *  @exception InvalidRecordIndexException If <i>recIndex<0</i> or <i>recIndex>#Records in the
	 *  TableView</i>.
	 */
	public void removeRecord(int recIndex, int rowIndex, boolean isChanging) throws TableNotExpandableException,
	InvalidRecordIndexException {
        if (recIndex < 0 || recIndex >= recordCount)
            throw new InvalidRecordIndexException(bundle.getString("CTableMsg48") + recIndex);
		removeRecordFromView(records.get(recIndex), isChanging);
	}

	/** Ensures that this flag is the same as that of the source baseTable.
	 * @see Table#setRecordAdditionAllowed(boolean)
	 */
	public void setRecordAdditionAllowed(boolean allowed) throws PropertyVetoException {
		super.setRecordAdditionAllowed(baseTable.isRecordAdditionAllowed());
	}

	/** Ensures that this flag is the same as that of the source baseTable.
	 * @see Table#isRecordAdditionAllowed()
	 */
	public boolean isRecordAdditionAllowed() {
		return baseTable.recordAdditionAllowed;
	}

	/** Ensures that this flag is the same as that of the source baseTable.
	 * @see Table#setRecordRemovalAllowed(boolean)
	 */
/*	public void setRecordRemovalAllowed(boolean allowed) throws PropertyVetoException {
		super.setRecordRemovalAllowed(baseTable.isRecordRemovalAllowed());
	}
*/
	/** Ensures that this flag is the same as that of the source baseTable.
	 * @see Table#isRecordRemovalAllowed()
	 */
/*	public boolean isRecordRemovalAllowed() {
		return baseTable.recordRemovalAllowed;
	}
*/
	/** Ensures that this flag is the same as that of the source baseTable.
	 * @see Table#setRecordRemovalAllowed(boolean)
	 */
	public void setFieldRemovalAllowed(boolean allowed) throws PropertyVetoException {
		super.setFieldRemovalAllowed(baseTable.isFieldRemovalAllowed());
	}

	/** Ensures that this flag is the same as that of the source baseTable.
	 * @see Table#isRecordRemovalAllowed()
	 */
	public boolean isFieldRemovalAllowed() {
		return baseTable.fieldRemovalAllowed;
	}

	/** Overriden so that the change of the key is never enabled in a TableView.
	 * TableViews cannot have keys.
	 */
	public void setKeyChangeAllowed(boolean allowed) throws PropertyVetoException {
		keyChangeAllowed = false;
	}

	/** Always returns false.
	 */
	public boolean isKeyChangeAllowed(boolean allowed) throws PropertyVetoException {
		return false;
	}

	/** Always returns false. A TableView cannot be keyed.
	 */
	public boolean hasKey() {
		return false;
	}

    /** Returns the structure which facilitates the addition of new records to the Table. This is the recommended
     *  mechanism to add new records to the Table. It is preferred to the use of <code>addRecord()</code>.
     *  @return The structure through which the values of the new record are set.
     *  @see RecordEntryStructure
     */
    public RecordEntryStructure getRecordEntryStructure() {
        if (res == null)
            res = new TableViewRecordEntryStructure(this);
        return res;
    }

    public AbstractTableField changeFieldType(String fieldName, Class newDataType, boolean permitDataLoss) throws InvalidFieldNameException,
            InvalidFieldTypeException, FieldIsKeyException, InvalidTypeConversionException,
            FieldNotEditableException, DataLossException, DependingCalcFieldsException, AttributeLockedException {
        if (!dataChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg89"));
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

        int fieldIndex;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {
            e.printStackTrace();
            throw new InvalidFieldNameException(bundle.getString("CTableMsg65") + fieldName + bundle.getString("CTableMsg66"));
        }

        AbstractTableField f = tableFields.get(fieldIndex);
        if (f.getDataType().equals(newDataType))
            return null;

        if (!f.isFieldDataTypeChangeAllowed())
            throw new AttributeLockedException(bundle.getString("CTableFieldMsg2"));

        if (f.isCalculated())
            throw new FieldNotEditableException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg73"));

        if (tableKey.isPartOfTableKey(f)/*f.isKey()*/)
            throw new FieldIsKeyException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg74"));

        if (!f.isEditable())
            throw new FieldNotEditableException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg75"));

        if (f.getDependentCalcFields() != null && f.getDependentCalcFields().size() != 0)
            throw new DependingCalcFieldsException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg76"));

        Class previousDataType = f.getDataType();
System.out.println("changeFieldType fieldName: " + fieldName + ", previousDataType: " + previousDataType + ", newDataType: " + newDataType);
//        boolean wasDate = f.isDate();

//        if (newDataType.isAssignableFrom(f.getDataType()))
//            throw new InvalidTypeConversionException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg78"));

//        ignoreEventFromBaseTable = true;
        baseTable.changeFieldType(((TableFieldView) f).getBaseField().getName(), newDataType, permitDataLoss);
        AbstractTableField newField = baseTable.getTableField(((TableFieldView) f).getBaseField().getName());
//        ignoreEventFromBaseTable = false;

        AbstractTableField newFieldView = (AbstractTableField) AbstractTableField.createNewFieldView(newField);
        newFieldView.setTable(this);
        newFieldView.indexInTable = fieldIndex;

        tableFields.set(fieldIndex, newFieldView);
        fireColumnTypeChanged(f, previousDataType);
        setModified();
        return newFieldView;
    }

    /** Returns the Table the view is based on.
     * @return The underlying Table.
     */
    public Table getBaseTable() {
        return baseTable;
    }


	//	public void promoteSelectedRecords() {}

	//public void removeFromKey(String fieldName) throws FieldIsNotInKeyException,
	//InvalidFieldNameException, TableNotExpandableException, AttributeLockedException {}

	//public void removeFromSelectedSubset(ArrayList recIndices) {}

	//public void removeFromSelectedSubset(int[] recIndices) {}

	//public void removeFromSelectedSubset(int recIndex) {}



	private void initTableView(Table table) {
///		tableFields = new TableFieldBaseArray(baseTable.tableFields.size());
		// Copy the TableFields of the baseTable to the 'tableFields' of the TableView.
///		for (int i=0; i<baseTable.tableFields.size(); i++) {
///			tableFields.add(baseTable.tableFields.get(i));
///		}
///		recordCount = baseTable.recordCount;
		title = bundle.getString("ViewOf") + " \""  + table.title + '\"';
		metadata = table.metadata;
//		selectedSubset = (IntBaseArrayDesc) baseTable.selectedSubset.clone();
//		unselectedSubset = (IntBaseArrayDesc) baseTable.unselectedSubset.clone();
		dateFormat = (SimpleDateFormat) table.dateFormat.clone();
		timeFormat = (SimpleDateFormat) table.timeFormat.clone();
		numberFormat = (TableNumberFormat) table.numberFormat.clone();
		numOfCalculatedFields = table.numOfCalculatedFields;
		calculatedFieldsChanged = table.calculatedFieldsChanged;
		changedCalcCells = table.changedCalcCells;
///		pendingNewRecord = baseTable.pendingNewRecord;
///		UIProperties = baseTable.UIProperties;
		isHidden = table.isHidden;
		activeRecord = 0; ///baseTable.activeRecord;
		recordAdditionAllowed = table.recordAdditionAllowed;
		recordRemovalAllowed = table.recordRemovalAllowed;
		fieldAdditionAllowed = table.fieldAdditionAllowed;
		fieldRemovalAllowed = table.fieldRemovalAllowed;
		fieldReorderingAllowed = table.fieldReorderingAllowed;
		fieldPropertyEditingAllowed = table.fieldPropertyEditingAllowed;
		keyChangeAllowed = false; // baseTable.keyChangeAllowed;
		dataChangeAllowed = table.dataChangeAllowed;
		hiddenFieldsDisplayed = table.hiddenFieldsDisplayed;
		hiddenAttributeChangeAllowed = table.hiddenAttributeChangeAllowed;
///		recordIndex = (IntBaseArray) baseTable.recordIndex.clone();
///		tableView = new gr.cti.eslate.database.view.TableView(baseTable.tableView, baseTable.tableFields.size());
		tableRenamingAllowed = table.tableRenamingAllowed;

///		baseTable.addPropertyChangeListener(viewTablePropertyChangeListener);
	}

    /**
     * Returns the indices in the <code>baseTable</code> of the AbstractTableFields for which TableFieldViews are
     * included in this TableView.
     * @return
     */
    public IntBaseArray getBaseFieldIndices() {
        IntBaseArray baseFieldIndices = new IntBaseArray();
        for (int i=0; i<tableFields.size(); i++)
            baseFieldIndices.add(((TableFieldView) tableFields.get(i)).getBaseField().getFieldIndex());
        return baseFieldIndices;
    }

    /** Returns the field view of the TableView which is based on the AbstractTableField <code>field</code>.
     * @param field The field for which a field view is searched for in this TableView.
     * @return The field view.
     */
    public TableFieldView getFieldView(AbstractTableField field) {
//System.out.println("getFieldView() for field: " + field + ", name: " + field.getName());
        for (int i=0; i<tableFields.size(); i++) {
            TableFieldView fieldView = (TableFieldView) tableFields.get(i);
//System.out.println("fieldView: " + fieldView + ", baseField: " + fieldView.getBaseField() + ", name: " + fieldView.getBaseField().getName());
            if (fieldView.getBaseField() == field)
                return fieldView;
        }
        return null;
    }

    /** Returns the field view of the TableView which is based on the AbstractTableField named <code>fieldName</code>.
     * @param fieldName The name of the base field of the field view, which is searched for.
     * @return The field view.
     */
    public TableFieldView getFieldView(String fieldName) {
        for (int i=0; i<tableFields.size(); i++) {
            TableFieldView fieldView = (TableFieldView) tableFields.get(i);
            if (fieldView.getBaseField().getName().equals(fieldName))
                return fieldView;
        }
        return null;
    }

    /** Determines if the active record of the TableView will be synchronized with the active record of the underlying
     *  Table. If they are synchronized:
     *  <ul>
     *  <li> When the TableView's active record changes, the corresponding record in the Table is activated.
     *  <li> When the Table's active record changes, the corresponding record is activated in the TableView, if it is
     *       contained in the TableView. If the Table's active record does not belong to the TableView, then the
     *       active record of the TableView is reset (becomes -1).
     *  </ul>
     * @param synch true, to synchronize.
     */
    public void setSynchronizedActiveRecord(boolean synch) {
        if (synchronizedActiveRecord == synch) return;
        synchronizedActiveRecord = synch;
        if (synchronizedActiveRecord) {
            int baseTableActiveRecord = baseTable.getActiveRecord();
            if (baseTableActiveRecord == -1)
                setActiveRecord(-1);
            else{
                int recIndex = records.indexOf(baseTableActiveRecord);
                setActiveRecord(recIndex);
            }
        }
    }

    /** Returns if the TableView's activeRecord is synchronized with the active record of the underlying Table. */
    public boolean isSynchronizedActiveRecord() {
        return synchronizedActiveRecord;
    }

    /** Overriden to provide active record synchronization with the underlying Table, when desired.
     *  @see Table#setActiveRecord(int)
     */
    public void setActiveRecord(int recIndex) {
//System.out.println("setActiveRecord(" +recIndex +") activeRecord: " + activeRecord);
//Thread.currentThread().dumpStack();
        if (activeRecord == recIndex) return;
        super.setActiveRecord(recIndex);
        if (synchronizedActiveRecord) {
            int baseTableActiveRecord = (recIndex == -1)? -1:records.get(recIndex);
            if (baseTableActiveRecord != baseTable.getActiveRecord())
                baseTable.setActiveRecord(baseTableActiveRecord);
        }
    }

    /** Returns the index in the underlying Table of the record at <code>recordIndex</code> in the TableView.
     * @param recordIndex The index of the record in the TableView.
     * @return The index of the record in the underlying Table.
     */
    public int getBaseTableRecord(int recordIndex) {
        if (recordIndex < 0 || recordIndex >= records.size())
            return -1;
        return records.get(recordIndex);
    }

    /** Returns the index in the TableView of the record at <code>baseTableRecordIndex</code> in the underlying Table.
     * @param baseTableRecordIndex The index of the record in the underlying Table.
     * @return The index of the record in the TableView. If the record is not included in the TableView, -1 is returned.
     */
    public int getTableViewRecord(int baseTableRecordIndex) {
        return records.indexOf(baseTableRecordIndex);
    }

	class ViewTableListener extends DatabaseTableModelAdapter {
		public void columnRemoved(ColumnRemovedEvent event) {
            // When a field is removed from the underlying baseTable, it also has to be removed from
            // all the TableViews it is contained in, even if the TableView forbids field removal.
			try{
                boolean tmpFieldRemovalAllowed = fieldRemovalAllowed;
                fieldRemovalAllowed = true;
//System.out.println("columnRemoved() e.getColumnIndex(): " + event.getColumnIndex() + " TableView.this: " + TableView.this.getTitle() + ", source: " + ((Table) event.getSource()).getTitle());
                TableFieldView fieldView = getFieldView(event.getColumnName());
                if (fieldView == null)// The field that was removed from the baseTable is not part of the TableView
                    return;
//System.out.println("FieldView: " + fieldView + " for field: " + event.getColumnName());
				TableView.this.removeFieldView(fieldView, event.moreToBeRemoved());
                fieldRemovalAllowed = tmpFieldRemovalAllowed;
//			}catch (InvalidFieldNameException exc) {
				// The TableView does not contain this field.
			}catch (TableNotExpandableException exc) {
				// This should never occur, since 'recordAdditionAllowed'
				// and 'recordRemovalAllowed' are synchronized between the
                // TableView and its source Table.
				System.out.println("1. Inconsistency error while removing field");
			}catch (AttributeLockedException exc) {
				// This should not occur because of 'fieldRemovalAllowed' cause
                // its synchronized between the TableView and the source Table.
				// It also should occur because of 'keyChangeAllowed' cause
				// the TableViews are not allowed to have keys and the hasKey()
				// method of the TableView always returns false.
				System.out.println("2. Inconsistency error while removing field");
			}catch (FieldNotRemovableException exc) {
				// Should never occur cause the TableView and the source Table
				// share the same AbstractTableField instances, so if the field was
				// removed from the source Table, it should also be removable from
				// the TableView.
				System.out.println("3. Inconsistency error while removing field");
			}catch (CalculatedFieldExistsException exc) {
				// Should never occur cause the TableView and the source Table
				// share the same AbstractTableField instances. If no such exception occured
				// while removing the field from the source Table, it surely must
				// not occur here too.
				System.out.println("4. Inconsistency error while removing field");
			}
		}

		public void recordRemoved(RecordRemovedEvent e) {
            // When a record is removed from the base Table, it has to be removed from all its TableViews too.
            // If record removal is not allowed in the TableView, we reset the flag and set it back again.
            boolean tmpRecordRemovalAllowed = recordRemovalAllowed;
            int removedRecordIndex = e.getRecordIndex();
			try{
                recordRemovalAllowed = true;
				TableView.this.removeRecordFromView(removedRecordIndex, e.isChanging(), e.getRemovedRecord());
			}catch (InvalidRecordIndexException exc) {
				// This record is not included in the TableView.
			}catch (TableNotExpandableException exc) {
				// This should never occur
				System.out.println("1. Inconsistency error while removing record");
			}
            recordRemovalAllowed = tmpRecordRemovalAllowed;
            // Now since the record was removed from the underlying Table, we have to decrement the record indices
            // stored in the 'records' IntBaseArray, whose value is greater than 'removedRecordIndex'.
            for (int i=0; i<records.size(); i++) {
                if (records.get(i) > removedRecordIndex)
                    records.set(i, records.get(i)-1);
            }
		}
        public void columnTypeChanged(ColumnTypeChangedEvent event) {
            try{
                AbstractTableField field = ((Table) event.getSource()).getSubstitutedField();
                TableFieldView fieldView = getFieldView(field);
System.out.println("columnTypeChanged() field: " + field.getName() + ", fieldView: " + fieldView);
                if (fieldView == null) // The field that changed data type is not part of the TableView
                    return;
                AbstractTableField f = (AbstractTableField) fieldView;
                changeFieldType(f.getName(), Class.forName(event.getNewType()), true);
            }catch (InvalidFieldNameException exc) {
                System.out.println("Exception while handling ColumnTypeChangedEvent in TableView. Should never occur...");
            }catch (Throwable thr) {
                thr.printStackTrace();
            }
        }

        // We listen to changes in the cell values of the underlying Table and forward those changes to the listeners
        // of this TableView.
        public void cellValueChanged(CellValueChangedEvent event) {
            TableFieldView fieldView = getFieldView(event.getColumnName());
            if (fieldView == null) // The field whose value changed is not part of the TableView.
                return;
            int record = event.getRecordIndex();
            int recIndex = records.indexOf(record);
            if (recIndex == -1) // The record whose value changed is not part of the TableView.
                return;
            if (event instanceof FloatCellValueChangedEvent)
                fireFloatCellValueChanged(((AbstractTableField) fieldView).getName(), recIndex, ((FloatCellValueChangedEvent) event).getOldFloatValue(), false);
            else if (event instanceof IntegerCellValueChangedEvent)
                fireIntegerCellValueChanged(((AbstractTableField) fieldView).getName(), recIndex, ((IntegerCellValueChangedEvent) event).getOldIntegerValue(), false);
            else if (event instanceof DoubleCellValueChangedEvent)
                fireDoubleCellValueChanged(((AbstractTableField) fieldView).getName(), recIndex, ((DoubleCellValueChangedEvent) event).getOldDoubleValue(), false);
            else
                fireCellValueChanged(((AbstractTableField) fieldView).getName(), recIndex, event.getOldValue(), false);
        }

        public void activeRecordChanged(gr.cti.eslate.tableModel.event.ActiveRecordChangedEvent e) {
//System.out.println("e.getActiveRecord(): " + e.getActiveRecord());
            if (synchronizedActiveRecord) {
                int baseTableActiveRecord = e.getActiveRecord();
                int recIndex = records.indexOf(baseTableActiveRecord);
//System.out.println("recIndex: " + recIndex);
                setActiveRecord(recIndex);
            }
        }
	}

/*	class ViewTablePropertyChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();
			// Synchronize the following properties between the TableView and
			// its source Table.
			if (propertyName.equals("Record addition allowed")) {
				TableView.this.recordAdditionAllowed = ((Boolean) evt.getNewValue()).booleanValue();
			}else if (propertyName.equals("Record removal allowed")) {
				TableView.this.recordRemovalAllowed = ((Boolean) evt.getNewValue()).booleanValue();
			}else if (propertyName.equals("Field removal allowed")) {
				TableView.this.fieldRemovalAllowed = ((Boolean) evt.getNewValue()).booleanValue();
			}
		}
	}
*/
}

