package gr.cti.eslate.database.engine;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;
//import java.util.HashMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import gr.cti.eslate.tableModel.event.TableModelListener;
import gr.cti.eslate.database.view.*;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;
//import com.objectspace.Array;

public interface ITable {
    public void addCalculatedField(String fieldName, String formula, boolean isKey, boolean isRemovable, boolean isEditingFormula)
    throws IllegalCalculatedFieldException, InvalidFormulaException, InvalidFieldNameException,
    InvalidKeyFieldException, AttributeLockedException;

    public void addCustomField(String fieldName, String type) throws InvalidFieldNameException,
    InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException;

    public boolean addEmptyRecord() throws UnableToAddNewRecordException, DuplicateKeyException,
    NullTableKeyException, AttributeLockedException;

    public void addField(String fieldName, String typeName, boolean isEditable, boolean isKey, boolean isRemovable, boolean isHidden)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
    AttributeLockedException;

    public TableField addField(TableField field) throws IllegalCalculatedFieldException, InvalidFormulaException,
    InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException;

    public void addImageField(String fieldName, boolean isEditable, boolean isRemovable, boolean isHidden, boolean containsLinksToExternalData)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
    AttributeLockedException;

    public void addKeyField(String fieldName, String typeName, boolean isEditable) throws InvalidFieldNameException,
    InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException;

    public void addPropertyChangeListener(PropertyChangeListener pcl);

    public boolean addRecord(ArrayList recEntryForm) throws InvalidDataFormatException,
    NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException;

    public void addTableModelListener(TableModelListener dtl);

    public void addToKey(String fieldName) throws FieldAlreadyInKeyException,
    FieldContainsEmptyCellsException, InvalidFieldNameException, TableNotExpandableException,
    InvalidKeyFieldException, AttributeLockedException;

    public void addToSelectedSubset(ArrayList recIndices);

    public void addToSelectedSubset(int[] recIndices);

    public void addToSelectedSubset(int recIndex);

    public void addVetoableChangeListener(VetoableChangeListener vcl);

    public boolean changeCalcFieldFormula(String fieldName, String formula)
    throws InvalidFieldNameException, InvalidFormulaException, AttributeLockedException;

    public void changeFieldType(String fieldName, String type, boolean permitDataLoss) throws InvalidFieldNameException,
    InvalidFieldTypeException, FieldIsKeyException, InvalidTypeConversionException,
    FieldNotEditableException, DataLossException, DependingCalcFieldsException, AttributeLockedException;

    public boolean containsField(String fieldName);

    public ArrayList createEmptyRecordEntryForm();

    public int[] findFirst(ArrayList fieldNames, int[] recordIndices, String findWhat, boolean caseSensitive) throws InvalidFieldNameException;

    public int[] findNext(ArrayList fieldNames, int[] recordIndices, String findWhat, boolean caseSensitive, int startFromFieldIndex, int startFromRecordIndex, boolean downwards) throws InvalidFieldNameException;

    public int getActiveRecord();

    public Object getCell(int fieldIndex, int recIndex) throws InvalidCellAddressException;

    public Object getCell(String fieldName, int recIndex) throws InvalidCellAddressException;

    public Object getCell(TableField f, Record r) throws InvalidCellAddressException;

    public Object getCellValue(int row, int col);

    public Hashtable getChangedCalcFieldCells();

    public ArrayList getColumn(int colIndex);

    public ArrayList getColumn(String colName);

    public int getColumnCount();

    public int getColumnIndex(String colName);

    public String getColumnName(int colIndex);

    public Class getColumnType(int colIndex);

    public SimpleDateFormat getDateFormat();

    public ArrayList getDateFormats();

    public char getDecimalSeparator();

    public ArrayList getField(int fieldIndex) throws InvalidFieldIndexException;

    public ArrayList getField(String fieldName) throws InvalidFieldNameException;

    public int getFieldCount();

    public int getFieldIndex(String fieldName) throws InvalidFieldNameException;

    public int getFieldIndex(TableField f) throws InvalidFieldException;

    public String getFieldName(int fieldIndex) throws InvalidFieldIndexException;

    public StringBaseArray getFieldNames();

    public TableFieldBaseArray getFields();

    public int getImageFieldCount();

    public String getMetadata();

    public DoubleNumberFormat getNumberFormat();

    public ArrayList getRecord(int recIndex) throws InvalidRecordIndexException;

//    public Array getRecord(Record r) throws InvalidRecordIndexException;

    public int getRecordCount();

    public int getRowCount();

    public String[] getSampleDates();

    public int getSelectedRecordCount();

    public IntBaseArrayDesc getSelectedSubset();

    public TableField getTableField(int fieldIndex) throws InvalidFieldIndexException;

    public TableField getTableField(String fieldName) throws InvalidFieldNameException;

    public SharedObjectPlug getTablePlug();

    public TableView getTableView();

    public char getThousandSeparator();

    public SimpleDateFormat getTimeFormat();

    public ArrayList getTimeFormats();

    public String getTitle();

    public byte getTypeForClass(Class cls);

    public Object[] getUniqueFieldValues(String fieldName, boolean selectedRecordsOnly) throws InvalidFieldNameException;

    public ArrayList getUniqueFieldValuesArray(String fieldName, boolean selectedRecordsOnly) throws InvalidFieldNameException;

    public IntBaseArrayDesc getUnSelectedSubset();

    public boolean hasKey();

    public boolean haveCalculatedFieldsChanged();

    public boolean identicalRecords(ArrayList record1, ArrayList record2);

    public void invertSelection();

    public boolean isDataChangeAllowed();

    public boolean isDecimalSeparatorAlwaysShown();

    public boolean isExponentialFormatUsed();

    public boolean isFieldAdditionAllowed();

    public boolean isFieldRemovalAllowed();

    public boolean isFieldReorderingAllowed();

    public boolean isHidden();

    public boolean isHiddenAttributeChangeAllowed();

    public boolean isHiddenFieldsDisplayed();

    public boolean isKeyChangeAllowed();

    public boolean isModified();

    public boolean isRecordAdditionAllowed();

    public boolean isRecordRemovalAllowed();

    public boolean isRecordSelected(int recIndex);

    public boolean isShowIntegerPartOnly();

    public boolean isThousandSeparatorUsed();

    public void prepareRemoveField(String fieldName) throws InvalidFieldNameException,
    TableNotExpandableException, CalculatedFieldExistsException,
    FieldNotRemovableException, AttributeLockedException;

    public void promoteSelectedRecords();

    public int recordForRow(int row);

    public void removeField(String fieldName) throws InvalidFieldNameException,
    TableNotExpandableException, CalculatedFieldExistsException,
    FieldNotRemovableException, AttributeLockedException;

    public void removeFromKey(String fieldName) throws FieldIsNotInKeyException,
    InvalidFieldNameException, TableNotExpandableException, AttributeLockedException;

    public void removeFromSelectedSubset(ArrayList recIndices);

    public void removeFromSelectedSubset(int[] recIndices);

    public void removeFromSelectedSubset(int recIndex);

    public void removePropertyChangeListener(PropertyChangeListener pcl);

    public void removeRecord(int recIndex, int rowIndex, boolean isChanging) throws TableNotExpandableException,
    InvalidRecordIndexException;

    public void removeRecord(Record r, int rowIndex, boolean isChanging) throws TableNotExpandableException,
    InvalidRecordIndexException;

    public void removeTableModelListener(TableModelListener dtl);

    public void removeVetoableChangeListener(VetoableChangeListener vcl);

    public void renameField(String existingName, String newName)
    throws InvalidFieldNameException, FieldNameInUseException;

    public void resetCalculatedFieldsChangedFlag();

    public void resetChangedCalcCells();

    public void resetPendingNewRecord();

    public void resetSelectedSubset();

    public Object riskyGetCell(int fieldIndex, int recIndex);

    public Object[] riskyGetRecObjects(int recIndex);

    public int rowForRecord(int recordIndex);

    public void selectAll();

    public void setActiveRecord(int activeRecord);

    public void setCalcFieldFormulaChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public void setCalcFieldResetAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public boolean setCell(int fieldIndex, int recIndex, Object value) throws InvalidCellAddressException,
    NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException;

    public boolean setCell(String fieldName, int recIndex, Object value) throws InvalidCellAddressException,
    NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException;

    public boolean setCell(TableField fld, Record r, Object value) throws InvalidCellAddressException,
    NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException;

    public void setCellValue(int row, int col, Object value, boolean insert);

    public void setColumnName(int colIndex, String colName);

    public void setDataChangeAllowed(boolean allowed) throws PropertyVetoException;

    public boolean setDateFormat(String dateForm);

    public void setDecimalSeparator(char separator);

    public void setDecimalSeparatorAlwaysShown(boolean shown);

    public void setExponentialFormatUsed(boolean exponential);

    public void setFieldAdditionAllowed(boolean allowed) throws PropertyVetoException;

    public void setFieldContainsLinksToExternalData(String fieldName, boolean containsLinks)
    throws InvalidFieldNameException;

    public void setFieldDataTypeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public void setFieldEditabilityChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public boolean setFieldEditable(String fieldName, boolean isEditable)
    throws InvalidFieldNameException, AttributeLockedException;

    public void setFieldHidden(String fieldName, boolean isHidden)
    throws InvalidFieldNameException, AttributeLockedException;

    public void setFieldHiddenAttributeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public void setFieldKeyAttributeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public void setFieldRemovabilityChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException;

    public void setFieldRemovable(String fieldName, boolean isRemovable)
    throws InvalidFieldNameException, AttributeLockedException;

    public void setFieldRemovalAllowed(boolean allowed) throws PropertyVetoException;

    public void setFieldReorderingAllowed(boolean allowed) throws PropertyVetoException;

    public void setHiddedFieldsDisplayed(boolean displayed) throws PropertyVetoException;

    public void setHidden(boolean isHidden) throws AttributeLockedException;

    public void setHiddenAttributeChangeAllowed(boolean allowed) throws PropertyVetoException;

    public void setKeyChangeAllowed(boolean allowed) throws PropertyVetoException;

    public void setMetadata(String m);

    public void setModified();

    public void setRecordAdditionAllowed(boolean allowed) throws PropertyVetoException;

    public void setRecordRemovalAllowed(boolean allowed) throws PropertyVetoException;

    public void setSelectedSubset(int[] recIndices);

    public void setSelectedSubset(IntBaseArray recIndices);

    public void setSelectedSubset(int recIndex);

    public void setShowIntegerPartOnly(boolean integerPartOnly);

    public void setTable(Table table);

    public void setTableView(TableView view);

    public void setThousandSeparator(char separator);

    public void setThousandSeparatorUsed(boolean on);

    public boolean setTimeFormat(String timeForm);

    public void setTitle(String title) throws InvalidTitleException, PropertyVetoException;

    public void sortOnField(String fieldName, boolean ascending, int fromRowIndex, int toRowIndex)
    throws InvalidFieldException;

    public boolean switchCalcFieldToNormal(String fieldName)
    throws InvalidFieldNameException, AttributeLockedException;

}