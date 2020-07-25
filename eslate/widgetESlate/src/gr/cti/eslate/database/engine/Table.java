package gr.cti.eslate.database.engine;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.ForwardIterator;
import com.objectspace.jgl.Stack;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.database.engine.plugs.TablePlug;
import gr.cti.eslate.tableModel.ITableModel;
import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.utils.BooleanWrapper;
import gr.cti.typeArray.*;

import javax.swing.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;


/**
 * @version 2.0, May 01
 */
public class Table implements Externalizable, ITableModel, ESlatePart {
    static long attachTimersTime = 0;
    static final long serialVersionUID = 12;
    gr.cti.eslate.utils.Timer tableTimer = new gr.cti.eslate.utils.Timer();
    /** The version of the storage format of the Table class
     */
    // 1.0 --> 1.1 selectedSubset and unselectedSubset turned to IntBaseArrayDescs from JGL Arrays
    // 1.1 --> 2 The Table API that involved Record objects became obsolete. The 'tableRecords'
    //           Array does not exist anymore and the primaryKeyMap stores Integers as
    //           values instead of Records.
    // 2 -->   3 'tableData' is no more an Array. It became an ArrayList!
    // 3 -->   4 'tableFields' is no more an Array. It became a TableFieldBaseArray!
    //           'keyFieldIndices' was also converted to IntBaseArray (from Array)
    //           The fields of a Table should be of one of the subclasses of the AbstractTableField class
    //           e.g. StringTableField, DoubleTableField, ... No instance of TableField should be created.
    //           'recordIndex' was transformed to IntBaseArray (for performance reasons). Used to be int[].
    //           'tableData' does not exist anymore. The data of the Table are stored in the AbstractTableField subclasses.
    //           'sdf' and 'stf' data members are no longer stored/restored.
    //           The recordCount used to take took values between -1 and #records-1. Since version 4,
    //           the recordCount takes value from 1 to #records.
    // 4 -->  5  DoubleNumberFormat classes were turned to TableNumberFormat classes.
    public static final int FORMAT_VERSION = 5; //3; //2; //"1.1";
    // Version 2.0.1 introduced storage format version 2.
    // Version 2.1.0 introduced storage format version 4.
    private static final String CODE_VERSION = "2.1.0";

    /** The DBase to which the Table belongs.
     */
//    protected transient DBase database;
    /** The Table's data container.
     */
//    protected ArrayList tableData = new ArrayList(); //"protected" in order to speed-up access from other classes
    /** The Array of fields of the Table.
     */
    TableFieldBaseArray tableFields = new TableFieldBaseArray();
    /** The Array of records in the Table.
     */
//2.0.1    private Array tableRecords = new Array();
    /** The Array of record selection status in the Table.
     */
    protected BoolBaseArray recordSelection = new BoolBaseArray();
//    private Array recordEntryForm = new Array();
    /** Specifies whether the Table's name can be changed.
     */
    boolean tableRenamingAllowed = true;
    /** Specifies whether records can be added to the table.
     */
    boolean recordAdditionAllowed; //recordAdditionAllowed
    /** Specifies whether records can be removed from the table.
     */
    boolean recordRemovalAllowed;
    /** Specifies whether fields can be added to the table.
     */
    boolean fieldAdditionAllowed;
    /** Specifies whether fields can be removed from the table.
     */
    boolean fieldRemovalAllowed;
    /** Specifies whether fields can be reordered in the table.
     */
    boolean fieldReorderingAllowed;
    /** Specifies whether fields' properties can be edited in the table.
     */
    boolean fieldPropertyEditingAllowed;
    /** Specifies whether the table's key can be changed.
     */
    boolean keyChangeAllowed;
    /** Specifies whether the table's hidden fields are displayed or not.
     */
    boolean hiddenFieldsDisplayed;
    /** Specifies whether the table's hidden fields are displayed or not.
     */
    boolean hiddenAttributeChangeAllowed;
    /** Specifies whether the table's data can be changed.
     */
    boolean dataChangeAllowed;
    /** The Table's key */
    TableKey tableKey = new TableKey(this);

    /** Contains primary key's values. Before storage format version "2" the
     *  'primaryKeyMap' stored pairs where the key was the key-value and the
     *  value was a Record object. The pair stored the primary key of the Record.
     *  However since version 2, Record is obsolete (in order to speed up the
     *  table loading time), so the 'primaryKeyMap' associates keys with Integers,
     *  which are the indices of the Records.
     */
//    Hashtable primaryKeyMap;
    /** Contains the indices of the fields which constitute the Table's key.
     */
//    IntBaseArray keyFieldIndices = new IntBaseArray();
    /** Specifies whether the Table has a key.
     */
//    boolean hasKey;
    /** The number of records in the Table.
     */
    int recordCount;
    /** The Table's title.
     */
    String title;
    /** The Table's metadata.
     */
    String metadata;
    /** The subset of selected records of the Table.
     */
    protected IntBaseArrayDesc selectedSubset = new IntBaseArrayDesc();
    /** The subset of unselected records of the Table.
     */
    protected IntBaseArrayDesc unselectedSubset = new IntBaseArrayDesc();
    /** Reports whether the Table has been modified.
     */
    protected transient boolean isModified = false;
    /** The file which stores the Table.
     */
//    public File file;


    //Date and Time formats for data entry
    /** The format in which dates are processed in the Table.
     */
//1    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH");

    /** The format used for the String representation of dates.
     *  @see #setDateFormat(String)
     *  @see #getDateFormat()
     */
    SimpleDateFormat dateFormat;

    /** The format in which time values are processed in the Table.
     */
//1    SimpleDateFormat stf = new SimpleDateFormat("H:mm");

    /** The format used for the String representation of time values.
     *  @see #setTimeFormat(String)
     *  @see #getTimeFormat()
     */
    SimpleDateFormat timeFormat;

    /** Alternative date formats.
     */
    static ArrayList dateFormats = null;

    /** Alternative time formats.
     */
    static ArrayList timeFormats = null;

    /** Date samples for each of the supported date formats.
     */
    static String[] sampleDates = null;

    /** The Table Number format.
     */
    public TableNumberFormat numberFormat;

    int numOfCalculatedFields;

    /** Used to correct input date values.
     */
    private static Calendar calendar = new GregorianCalendar();

    boolean calculatedFieldsChanged;
    Hashtable changedCalcCells;
//1    protected boolean pendingNewRecord;

    /** This Array stores table-specific attributes, which are not used by the database engine, but
     *  by the database UI. So it's up to the UI builder to store any attributes in this Array, which
     *  need to persist from session to session.
     */
//1    public Array UIProperties = new Array();

    /** This int array stores the correspondence between the record's of the Table and the rows
     *  at which they are displayed. The record order inside the Table never changes. The first
     *  record inserted will always be the first record of the Table, until it is removed from it.
     *  However the row at which the record is displayed may change. The term 'row' does not only refer
     *  to the rows of a tabular UI. It may refer to columns in a column chart, etc. The Table API
     *  provides basic support for the record's visual position. For any Table, the visual position
     *  of any of its record is the same accross all the visual representations of the Table. The
     *  <code>recordIndex</code> array stores the actual, constant record index of each row, e.g.
     *  <code>recordIndex.get(k)</code> returns the record index of the k-th row. When the row order changes
     *  a RowOrderChanged event is send to all the visual representations of the Table.
     */
    public IntBaseArray recordIndex = new IntBaseArray();

    /** Defines a table as hidden. This attribute may be used in different
     *  ways. It is not used by the API itsef.
     */
    protected boolean isHidden = false;


    /* The active record of the table
    */
    protected int activeRecord = -1;
    public static ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.database.engine.CTableResourceBundle", Locale.getDefault());
    /* The array of the TableModelListeners */
    protected transient ArrayList tableModelListeners = new ArrayList();
    /* The array of the DatabaseTableModelListeners */
    protected transient ArrayList databaseTableModelListeners = new ArrayList();
    /* The array of the Table propertyChange listeners */
    protected transient ArrayList tablePropertyChangeListeners = new ArrayList();
    protected PropertyChangeSupport tablePropertyChangeSupport = new PropertyChangeSupport(this);
    protected TableVetoableChangeSupport tableVetoableChangeSupport = new TableVetoableChangeSupport(this);

    /** Contains all the UI settings of the Table. This class gives the Table document the
     *  ability to 'carry' the settings which adjust its view in the Database component.
     */
//1    TableView tableView = null;
    ESlateHandle handle = null;
//    TableSO tableSO;
    TablePlug tablePlug = null;
    /** Timers which measure functions of the Table */
    PerformanceTimer loadTimer, saveTimer, constructorTimer, initESlateAspectTimer, fieldMapTimer;
    /* The listener that notifies about changes to the state of the PerformanceManager */
    PerformanceListener perfListener = null;
    /** The structure which facilitates the addition of new records to the Table */
    RecordEntryStructure res = null;
    /** Stores all the TableViews which have been created on this Table. */
    ArrayList tableViews = null;
    /** Holds the field which was substituted when its data type changed.
     *  @see #fireColumnTypeChanged(AbstractTableField, Class)
     *  @see #getSubstitutedField()
     */
    private AbstractTableField substitutedField = null;

    public synchronized void addTableModelListener(TableModelListener dtl) {
        boolean isDatabaseTableModelListener = DatabaseTableModelListener.class.isAssignableFrom(dtl.getClass());
        if (isDatabaseTableModelListener) {
            if (databaseTableModelListeners.indexOf(dtl) == -1)
                databaseTableModelListeners.add(dtl);
        }
        if (tableModelListeners.indexOf(dtl) == -1)
            tableModelListeners.add(dtl);
//System.out.println("Table " + this + ", tableModelListeners: " + tableModelListeners.size() + ", databaseTableModelListeners: " + databaseTableModelListeners.size());
    }


    public void removeTableModelListener(TableModelListener dtl) {
        boolean isDatabaseTableModelListener = DatabaseTableModelListener.class.isAssignableFrom(dtl.getClass());
        if (isDatabaseTableModelListener) {
            int index = databaseTableModelListeners.indexOf(dtl);
            if (index != -1)
                databaseTableModelListeners.remove(index);
        }
        int index = tableModelListeners.indexOf(dtl);
        if (index != -1)
            tableModelListeners.remove(index);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener pcl) {
        tablePropertyChangeSupport.addPropertyChangeListener(pcl);
    }


    public synchronized void removePropertyChangeListener(PropertyChangeListener pcl) {
        tablePropertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public synchronized void addVetoableChangeListener(VetoableChangeListener vcl) {
        tableVetoableChangeSupport.addVetoableChangeListener(vcl);
    }


    public synchronized void removeVetoableChangeListener(VetoableChangeListener vcl) {
        tableVetoableChangeSupport.removeVetoableChangeListener(vcl);
    }

    protected void fireTableRenamed(String oldTitle) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        numOfListeners = dl.size();
        TableRenamedEvent e = new TableRenamedEvent(this, oldTitle, title);
        for (int i=0; i<numOfListeners; i++)
            ((TableModelListener) dl.get(i)).tableRenamed(e);
    }

    protected void fireTableHiddenStateChanged() {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        numOfListeners = dl.size();
        TableHiddenStateChangedEvent e = new TableHiddenStateChangedEvent(this, isHidden);
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).tableHiddenStateChanged(e);
    }

    protected void fireColumnRenamed(AbstractTableField field, String oldName) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnRenamedEvent e = new ColumnRenamedEvent(this, oldName, field.getName());
        for (int i=0; i<numOfListeners; i++)
            ((TableModelListener) dl.get(i)).columnRenamed(e);
    }

    protected void fireColumnTypeChanged(AbstractTableField field, Class newDataType) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        ColumnTypeChangedEvent e = new ColumnTypeChangedEvent(this, field.getName(), field.getDataType().getName(), newDataType.getName());
        numOfListeners = dl.size();
        substitutedField = field;
        for (int i=0; i<numOfListeners; i++)
            ((TableModelListener) dl.get(i)).columnTypeChanged(e);
        substitutedField = null;
    }

    /** Returns the fields which has been substituted by another field in the Table. This happens when the
     *  data type of a field changes, i.e. the field is replaced by a new field of the specified data type.
     *  This method returns smth valid only while the <code>ColumnTypeChangedEvent</code> which is generated
     *  when the data type of a field changes is being processed by the <code>TableModelListeners</code>.
     *  @return The replaced field.
     */
    public AbstractTableField getSubstitutedField() {
        return substitutedField;
    }

    protected void fireColumnKeyChanged(AbstractTableField field, boolean isKey) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnKeyChangedEvent e = new ColumnKeyChangedEvent(this, field.getName(), isKey);
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).columnKeyChanged(e);
    }

    protected void fireColumnEditableStateChanged(AbstractTableField field) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnEditableStateChangedEvent e = new ColumnEditableStateChangedEvent(this, field.getName(), field.isEditable());
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).columnEditableStateChanged(e);
    }

    protected void fireColumnRemovableStateChanged(AbstractTableField field) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnRemovableStateChangedEvent e = new ColumnRemovableStateChangedEvent(this, field.getName(), field.isRemovable());
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).columnRemovableStateChanged(e);
    }

    protected void fireColumnHiddenStateChanged(AbstractTableField field) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnHiddenStateChangedEvent e = new ColumnHiddenStateChangedEvent(this, field.getName(), field.isHidden);
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).columnHiddenStateChanged(e);
    }

    protected void fireColumnRemoved(AbstractTableField field, boolean moreToBeRemoved) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnRemovedEvent e = new ColumnRemovedEvent(this, field.getName(), field.indexInTable, moreToBeRemoved);
        for (int i=0; i<numOfListeners; i++)
            ((TableModelListener) dl.get(i)).columnRemoved(e);
    }

    protected void fireColumnAdded(AbstractTableField newField, boolean moreToAdd) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        numOfListeners = dl.size();
        ColumnAddedEvent e = new ColumnAddedEvent(this, newField.indexInTable, moreToAdd);
        for (int i=0; i<numOfListeners; i++)
            ((TableModelListener) dl.get(i)).columnAdded(e);
    }

    protected void fireCalcColumnReset(String fieldName) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        CalcColumnResetEvent e = new CalcColumnResetEvent(this, fieldName);
        numOfListeners = dl.size();
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).calcColumnReset(e);
    }


    protected void fireCalcColumnFormulaChanged(String fieldName, String textFormula, String prevDataType) {
        int numOfListeners = databaseTableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        CalcColumnFormulaChangedEvent e = new CalcColumnFormulaChangedEvent(this, fieldName, textFormula, prevDataType);
        numOfListeners = dl.size();
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).calcColumnFormulaChanged(e);
    }


    protected void fireSelectedRecordSetChanged(int id, String queryString, IntBaseArray previousSelectedSubset,
                                                IntBaseArray recordsAddedToSelection, IntBaseArray recordsRemovedFromSelection) {
        int numOfListeners = tableModelListeners.size();
        if (numOfListeners == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        SelectedRecordSetChangedEvent e = new SelectedRecordSetChangedEvent(this, id, queryString, previousSelectedSubset,
                recordsAddedToSelection, recordsRemovedFromSelection);
        numOfListeners = dl.size();
        for (int i=0; i<numOfListeners; i++)
            ((TableModelListener) dl.get(i)).selectedRecordSetChanged(e);
    }


    protected void fireCellValueChanged(String fieldName, int recIndex, Object prevValue, boolean affectsOtherCells) {
        if (tableModelListeners.size() == 0) return;
        CellValueChangedEvent e = new CellValueChangedEvent(this, fieldName, recIndex, prevValue, affectsOtherCells);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).cellValueChanged(e);
    }

    protected void fireIntegerCellValueChanged(String fieldName, int recIndex, int prevValue, boolean affectsOtherCells) {
        if (tableModelListeners.size() == 0) return;
        IntegerCellValueChangedEvent e = new IntegerCellValueChangedEvent(this, fieldName, recIndex, prevValue, affectsOtherCells);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).cellValueChanged(e);
    }

    protected void fireDoubleCellValueChanged(String fieldName, int recIndex, double prevValue, boolean affectsOtherCells) {
        if (tableModelListeners.size() == 0) return;
        DoubleCellValueChangedEvent e = new DoubleCellValueChangedEvent(this, fieldName, recIndex, prevValue, affectsOtherCells);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).cellValueChanged(e);
    }

    protected void fireFloatCellValueChanged(String fieldName, int recIndex, float prevValue, boolean affectsOtherCells) {
        if (tableModelListeners.size() == 0) return;
        FloatCellValueChangedEvent e = new FloatCellValueChangedEvent(this, fieldName, recIndex, prevValue, affectsOtherCells);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).cellValueChanged(e);
    }

    protected void fireRecordAdded(boolean moreToAdd) {
        int numOfListeners = databaseTableModelListeners.size();
        if (numOfListeners == 0) return;
        RecordAddedEvent event = new RecordAddedEvent(this, recordCount-1, moreToAdd);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).recordAdded(event);
    }

    protected void fireRecordRemoved(int recIndex, int rowIndex, ArrayList removedRecord, boolean isChanging) {
        if (tableModelListeners.size() == 0) return;
        ArrayList dl;
        RecordRemovedEvent e = new RecordRemovedEvent(this, recIndex, rowIndex, removedRecord, isChanging);
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).recordRemoved(e);
    }

    protected void fireActiveRecordChanged(int previousActiveRecord) {
        if (tableModelListeners.size() == 0) return;
        ActiveRecordChangedEvent e = new ActiveRecordChangedEvent(this, previousActiveRecord, activeRecord);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).activeRecordChanged(e);

    }

    protected void fireRowOrderChanged() {
        if (tableModelListeners.size() == 0) return;
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        RowOrderChangedEvent e = new RowOrderChangedEvent(this);
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).rowOrderChanged(e);
    }

    protected void fireCurrencyFieldChanged(String fieldName) {
        int numOfListeners = databaseTableModelListeners.size();
        if (numOfListeners == 0) return;
        ColumnEvent event = new ColumnEvent(this, fieldName);
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<numOfListeners; i++)
            ((DatabaseTableModelListener) dl.get(i)).currencyFieldChanged(event);
    }

    /**
     * Adds a new non-calculated field to the Table. The field is added at the end of the Array of fields
     * of the Table. If the field is of type <code>image</code>, then it is configured so that the image data
     * are stored in the database itself.
     *
     * @param     fieldName   the name of the field.
     * @param     fieldType   the data type of the new field. The class of the new field and the type of the
     *                        data is contains depending on the value of this parameter are:
     <table border="1" width="100%">
     <tr>
     <td width="33%" align="center"><b>fieldType parameter</b></td>
     <td width="33%" align="center"><b>Field data type</b></td>
     <td width="34%" align="center"><b>Field class</b></td>
     </tr>
     <tr>
     <td width="33%" align="center">StringTableField.DATA_TYPE</td>
     <td width="33%" align="center">String</td>
     <td width="34%" align="center">StringTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">DoubleTableField.DATA_TYPE</td>
     <td width="33%" align="center">Double</td>
     <td width="34%" align="center">DoubleTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">Float.class</td>
     <td width="33%" align="center">Float</td>
     <td width="34%" align="center">FloatTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">IntegerTableField.DATA_TYPE</td>
     <td width="33%" align="center">Integer</td>
     <td width="34%" align="center">IntegerTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">BooleanTableField.DATA_TYPE</td>
     <td width="33%" align="center">Boolean</td>
     <td width="34%" align="center">BooleanTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">DateTableField.DATA_TYPE (or subclass)<td>
     <td width="33%" align="center">CDate</td>
     <td width="34%" align="center">DateTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">TimeTableField.DATA_TYPE (or subclass)</td>
     <td width="33%" align="center">CTime</td>
     <td width="34%" align="center">TimeTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">java.net.URL.class</td>
     <td width="33%" align="center">URL</td>
     <td width="34%" align="center">URLTableField</td>
     </tr>
     <tr>
     <td width="33%" align="center">ImageTableField.DATA_TYPE (or subclass)</td>
     <td width="33%" align="center">Image</td>
     <td width="34%" align="center">ImageTableField</td>
     </tr>
     </table>
     * @param     isEditable  defines if the new field can be edited or not. When a field is not editable,
     *            neither its data, nor its attributes (e.g. name) can be edited.
     * @param     isRemovable determines if this field can be deleted.
     * @param     isHidden determines if this field should be hidden.
     * @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     * @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     * @exception InvalidFieldTypeException If the type of the new field cannot be determined.
     * @exception AttributeLockedException If new fields cannot be added to the Table.
     * @see       #addCalculatedField(java.lang.String, java.lang.String, boolean, boolean)
     * @see       #addImageField(java.lang.String, boolean, boolean, boolean, boolean)
     * @see       IntegerTableField#DATA_TYPE
     * @see       FloatTableField#DATA_TYPE
     * @see       DoubleTableField#DATA_TYPE
     * @see       BooleanTableField#DATA_TYPE
     * @see       DateTableField#DATA_TYPE
     * @see       TimeTableField#DATA_TYPE
     * @see       URLTableField#DATA_TYPE
     * @see       ImageTableField#DATA_TYPE
     */
    public AbstractTableField addField(String fieldName, Class fieldType, boolean isEditable, boolean isRemovable, boolean isHidden)
            throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
            AttributeLockedException {
        return _addField(fieldName, fieldType, isEditable, isRemovable, isHidden, true);
    }

    /**
     * Adds a new Image field to the Table. The field is added at the end of the Array of fields
     * of the Table. Whether the database stores the actual data or references to the data of
     * the image field, is determined be the <code>containsLinksToExternalData</code> parameter.
     *
     * @param     fieldName   the name of the field.
     * @param     isEditable  defines if the new field can be edited or not. When a field is not editable,
     *            neither its data, nor its attributes (e.g. name) can be edited.
     * @param     isRemovable determines if this field can be deleted.
     * @param     isHidden determines if this field should be hidden.
     * @param     containsLinksToExternalData determines if this field stores actual image data or references to image files.
     * @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     * @exception AttributeLockedException If new fields cannot be added to the Table.
     */
    public AbstractTableField addImageField(String fieldName, boolean isEditable, boolean isRemovable, boolean isHidden, boolean containsLinksToExternalData)
            throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
            AttributeLockedException {
        ImageTableField fld = (ImageTableField) _addField(fieldName, CImageIcon.class, isEditable, isRemovable, isHidden, true);
        fld.setContainsLinksToExternalData(containsLinksToExternalData);
        return fld;
//            ((TableField) tableFields.at(tableFields.size()-1)).setContainsLinksToExternalData(containsLinksToExternalData);
    }

    /**
     * Adds a new field to the Table. The field is added at the end of the ArrayList of fields
     * of the Table. The new field is a clone of the given field. If this
     * method is given a AbstractTableField of the same Table, then an exception will be triggered,
     * since two fields cannot have the same name in the same Table.
     * @param     field   The field to be replicated in the current Table.
     * @return  The cloned field.
     * @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>The field has no name
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     * @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     * @exception InvalidFieldTypeException If the type of the new field cannot be determined, i.e. different from
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     * @exception IllegalCalculatedFieldException This exception is thrown, when
     *              the formula of a calculated field, which does not exist, is being edited.
     * @exception InvalidFormulaException If the supplied formula is incorrect.
     */
    public AbstractTableField addField(AbstractTableField field) throws IllegalCalculatedFieldException, InvalidFormulaException,
            InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {

        if (!fieldAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg88"));

        if (!field.isCalculated()) {
            _addField(field.getName(),
                    field.getDataType(),
//	                  TableField.getInternalDataTypeName(field),
                    field.isEditable(),
                    field.isRemovable(),
                    field.isHidden(),
                    true);
        }else{
            try{
                addCalculatedField(field.getName(),
                        field.getTextFormula(),
//                        field.isKey(),
                        field.isRemovable(),
                        field.isHidden());
            }catch (AttributeLockedException exc) {
                System.out.println("Serious inconsisency error in Table addField() : 0.5");
                return null;
            }
        }
        AbstractTableField newField;
        try{
            newField = getTableField(getFieldCount()-1);
        }catch (InvalidFieldIndexException e) {
            System.out.println("Serious inconsistency error in Table addField(AbstractTableField): (1)");
            return null;
        }
        newField.setFieldEditabilityChangeAllowed(field.isFieldEditabilityChangeAllowed());
        newField.setFieldRemovabilityChangeAllowed(field.isFieldRemovabilityChangeAllowed());
        newField.setFieldDataTypeChangeAllowed(field.isFieldDataTypeChangeAllowed());
        newField.setCalcFieldResetAllowed(field.isCalcFieldResetAllowed());
        newField.setFieldKeyAttributeChangeAllowed(field.isFieldKeyAttributeChangeAllowed());
        newField.setFieldHiddenAttributeChangeAllowed(field.isFieldHiddenAttributeChangeAllowed());
        newField.setCalcFieldFormulaChangeAllowed(field.isCalcFieldFormulaChangeAllowed());
//1        newField.UIProperties = (Array) field.UIProperties.clone();
        if (newField.getDataType().equals(ImageTableField.DATA_TYPE)) {
            newField.setContainsLinksToExternalData(field.containsLinksToExternalData());
        }
        return newField;
    }


    protected final AbstractTableField _addField(String fieldName, Class fieldType, boolean isEditable, boolean isRemovable, boolean isHidden, boolean fireEvent)
            throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
            AttributeLockedException {
        AbstractTableField newField;
        if (!fieldAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg88"));

//        System.out.println("FieldName: " + fieldName);
        //Checking for a valid field name
        if (fieldName.equals("") || fieldName == null) {
//            System.out.println("No name supplied for the new field");
            throw new InvalidFieldNameException(bundle.getString("CTableMsg1"));
        }

        //Checking if another column with the same name exists
        for (int i = 0; i<tableFields.size(); i++) {
            if (tableFields.get(i).getName().equals(fieldName)) {
//                System.out.println("Another field with the same name exists");
                throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg2"));
            }
        }

        //Checking if the table already contains records, in case a key field is added
//        if (isKey && recordCount >=0) {
//            throw new InvalidKeyFieldException(bundle.getString("CTableMsg3"));
//        }

        try {
//            newField = new TableField(fieldName, typeName, isEditable, isKey, isRemovable, isHidden);
            newField = AbstractTableField.createNewField(fieldName, fieldType, isEditable, isRemovable, isHidden);
        }catch (InvalidFieldTypeException e) {
            throw new InvalidFieldTypeException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg4") + e.message);
        }
//        if (typeName.toLowerCase().equals("image") && isKey)
//        if (newField instanceof ImageTableField && isKey)
//            throw new InvalidKeyFieldException(bundle.getString("CTableMsg67"));

        //Adding the new column to the tableFields array
        newField.indexInTable = tableFields.size();
        newField.setTable(this);
        tableFields.add(newField);

        //Initializing the primaryKey OrderedSet, if not already initialized
/*        if (primaryKeyMap == null && isKey) {
            hasKey = true;
//            primaryKeyMap = new HashMap(new EqualTo(), true, 10, (float)1.0);
            primaryKeyMap = new Hashtable(10, (float)1.0);
        }

        //Add the column to the Array of the key field indices, if it's a key
        if (isKey)
            keyFieldIndices.add(newField.index);
*/
        //Filling the record entry form HashMap
//        recordEntryForm.add("");
        //Adding a new Array for the new Field to the tableData HashMap
/*        ArrayList fld = new ArrayList();
for (int k=0; k<=recordCount; k++)
fld.add(null);
tableData.add(fld);
*/
        setModified();
        /* An event shouldn't be fired by _addField() if the new field is calculated. In this case the event
        * is fired by addCalculatedField().
        */
        if (fireEvent) {// && database != null)
            fireColumnAdded(newField, false); //database.CTables.indexOf(this), newField));
        }
        return newField;
    }


    /**
     *  Adds a key field to the Table.The table has to be empty whenever a key field is
     *  added to it. The new field is by default removable.
     *  @param fieldName   the name of the key field.
     *  @param fieldType    the data type of the new field.
     *  @param isEditable  determines whether the field's attributes and data are editable.
     *  @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     *  @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     *  @exception InvalidFieldTypeException If the type of the new field cannot be determined, i.e. different from
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     *  @exception AttributeLockedException If new fields cannot be added to the Table.
     * @see       #addField(java.lang.String, java.lang.Class, boolean, boolean, boolean)
     * @deprecated As of version 2.1.0 of Table, a field has to be part of the Table, before it becomes part of its key.
     *            Therefore this method simply adds the new field to the Table, without making it part of the Table's key.
     */
    public AbstractTableField addKeyField(String fieldName, Class fieldType, boolean isEditable) throws InvalidFieldNameException,
            InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
        if (recordCount < 0) {
            return _addField(fieldName, fieldType, isEditable, false, false, true);
        }else{
            throw new InvalidKeyFieldException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg5"));
        }
    }


    protected static boolean creatingCalculatedField = false;
    /**
     *  Adds an ordinary field to the Table. An ordinary field is editable, removable and
     *  not part of the table's key.
     *  @param fieldName  the name of the new field.
     *  @param fieldType  the data type of the column, i.e. one of
     *               <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     *  @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     *  @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     *  @exception InvalidFieldTypeException If the type of the new field cannot be determined, i.e. different from
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     *  @exception AttributeLockedException If new fields cannot be added to the Table.
     * @see       #addField(java.lang.String, java.lang.Class, boolean, boolean, boolean)
     */
    public AbstractTableField addField(String fieldName, Class fieldType) throws InvalidFieldNameException,
            InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
        return _addField(fieldName, fieldType, true, true, false, true);
    }


    /**
     * Adds a calculated field to the table. A calculated field is one whose data type and cell values are
     * determined by an expression which contains constants and/or other existing fields
     * of any of the supported data types, except from <b>image</b> and <b>sound</b>. This method
     * also changes the formula of a calculated field.
     *
     * @param fieldName  the name of the calculated field.
     * @param formula    the expression whose evaluation will produce the data type and the
     *                   data of the field.
     * @param isRemovable determines if the calculated field can be deteted from the table or not
     * @param isEditingFormula a flag, which determines if the calculated field is actually
     *                         being added to the table, or if its formula is being re-evaluated.
     * @return           The new AbstractTableField.
     * @exception IllegalCalculatedFieldException This exception is thrown, when
     *              the formula of a calculated field, which does not exist, is being edited.
     * @exception InvalidFormulaException If the supplied formula is incorrect.
     * @exception InvalidFieldNameException If no name was supplied for the new field, or another field
     *              with the same name is already defined in the table.
     * @exception InvalidKeyFieldException If the new calculated field is also introduced as part of
     *              of the key of a non-empty table.
     * @exception AttributeLockedException If a calculated field's formula is being edited, but it is locked.
     * @see   #addField(java.lang.String, java.lang.Class, boolean, boolean, boolean)
     */
    public AbstractTableField addCalculatedField(String fieldName, String formula, boolean isRemovable, boolean isEditingFormula)
            throws IllegalCalculatedFieldException, InvalidFormulaException, InvalidFieldNameException,
            InvalidKeyFieldException, AttributeLockedException {
        /* Check if the table contains any other columns. If not then abort.
        */
//        if (tableFields.isEmpty())
//            throw new IllegalCalculatedFieldException("A calculated field cannot be defined on an table without fields");
        if (isEditingFormula && !getFieldNames().contains(fieldName))
            throw new IllegalCalculatedFieldException(bundle.getString("CTableMsg6") + fieldName + bundle.getString("CTableMsg7"));

        /* Check the "formula". First we have to determine the data type of the new field.
        * So parse the "formula" string and find all the fields, which are included in
        * square brackets. The first of these fields will determine the data type of the
        * calculated field.
        */
        if (formula.length()==0)
            throw new InvalidFormulaException(bundle.getString("CTableMsg8") + fieldName + "\"");

        int firstBracket=0;
        int prevBracket = firstBracket;
        int closingBracket = 0, prevClosingBracket=0;
        int numOfQuotes;
        boolean closingBracketFound;
        String newFormula = "";
        IntBaseArray fieldIndices = new IntBaseArray();

        while ((firstBracket<formula.length()-1) && (firstBracket = formula.indexOf('[', firstBracket)) != -1) {
            /* Check whether the found '[' is contained between quotes.
            */
            numOfQuotes = 0;
            for (int i=0; i<firstBracket; i++) {
                if (formula.charAt(i) == '"')
                    numOfQuotes++;
            }

            if ((numOfQuotes%2) != 0) {
                firstBracket++;
                continue;
            }else{
                /* Look for a closing square bracket(']'), not included in quotes.
                */
                closingBracketFound = false;
                int lastFoundClosingBracket = firstBracket+1;
                while (!closingBracketFound) {
                    try{
                        closingBracket = formula.indexOf(']', lastFoundClosingBracket);
                    }catch (StringIndexOutOfBoundsException e) {break;}
                    if (closingBracket == -1)
                        break;
                    else{
                        /* Check if the closing bracket is included in quotes.
                        */
                        numOfQuotes = 0;
                        for (int i=firstBracket+1; i<closingBracket; i++) {
                            if (formula.charAt(i) == '"')
                                numOfQuotes++;
                        }

                        if ((numOfQuotes%2) != 0) { // If ']' is included in quotes
                            lastFoundClosingBracket = closingBracket+1;
                            continue;
                        }else{ //if ']' not included in quotes
                            closingBracketFound = true;
                            /* Get the name of the field within the square brackets. If this name
                            * exists the replce it in the "newFormula" String with the index of
                            * this field. Otherwise throw an exception.
                            */
                            String operandFieldName = formula.substring(firstBracket+1, closingBracket);
                            try{
                                int operandFieldIndex = getFieldIndex(operandFieldName);
                                fieldIndices.add(operandFieldIndex);
                                newFormula = newFormula + formula.substring(prevClosingBracket, firstBracket+1) + operandFieldIndex + "]";

                            }catch (InvalidFieldNameException e) {throw new InvalidFormulaException(e.message);}
                        }
                    }
                } //while

                if (!closingBracketFound)
                    throw new InvalidFormulaException(bundle.getString("CTableMsg9"));
                else{
                    prevBracket = firstBracket;
                    firstBracket = closingBracket+1;
                    prevClosingBracket = closingBracket+1;
                }
            }
        }
        if (!newFormula.equals(""))  //field names were found in "operand"
            newFormula = newFormula + formula.substring(prevClosingBracket);

//        System.out.println("Formula, newFormula: " + formula + ", " + newFormula);


        Class calcFieldType;
        boolean isDate = false;
        boolean integerFound = false;
        String firstOperand = "";
        boolean openingQuote = false;
        int openingQuoteIndex = 0;
        boolean moreThan1DateFields = false;
        int firstIndex = 0;

        if (fieldIndices.size() != 0) {
            calcFieldType = (tableFields.get(fieldIndices.get(0))).getDataType();
//            isDate = (tableFields.get(fieldIndices.get(0))).isDate();
        }else{
            /* We seek the first non-field operand in "formula", since "formula" contains
            * no fields.
            */
            int i=0;
            while ((i<formula.length()) && (formula.charAt(i) == '(' || formula.charAt(i) == ' ')) i++;
            if (i==formula.length())
                throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");

            if (formula.charAt(i) != '"') {
                firstIndex = i;
                while (i<formula.length() && formula.charAt(i) != '+' && formula.charAt(i) != '-' &&
                        formula.charAt(i) != '*' && formula.charAt(i) != '/' &&
                        formula.charAt(i) != '$' && formula.charAt(i) != '%' &&
                        formula.charAt(i) != ' ')
                    i++;

/* Because '/' is both used as the division comparator and the field
                * separator in dates, we have to chack a bit further.
                */
                if ((i<formula.length()) && (formula.charAt(i)=='/')) {
                    if (((i+2)<formula.length() && formula.charAt(i+2) == '/') ||
                            ((i+3)<formula.length() && formula.charAt(i+3) == '/')) {

                        char c[] = formula.toCharArray();
                        String firstOperand2;
                        firstOperand2 = new String(c, firstIndex, i-firstIndex);
//                            System.out.println("Looking for dates: " + firstOperand2);
                        try{
                            Integer k = new Integer(firstOperand2);
                            integerFound = true;
                            i = formula.indexOf('/', i+1);
                            i++;
                            while (i<formula.length() && formula.charAt(i) != '+' && formula.charAt(i) != '-' &&
                                    formula.charAt(i) != '*' && formula.charAt(i) != '/' &&
                                    formula.charAt(i) != '$' && formula.charAt(i) != '%' &&
                                    formula.charAt(i) != ' ')
                                i++;
                        }catch (NumberFormatException e) {}
                    }
                }

                int lastIndex;
                lastIndex = i;
//                System.out.println(firstIndex +"," + lastIndex);

                char c[] = formula.toCharArray();
                firstOperand = new String(c, firstIndex, lastIndex-firstIndex);
//                System.out.println("First operand: " + firstOperand);

                if (firstOperand.equals("true") || firstOperand.equals("false") || firstOperand.equals("not")) {
                    if (firstOperand.equals("not")) {
                        int k;
                        for (k=lastIndex+1; k<formula.length() && (formula.charAt(k) == '(' || formula.charAt(k) == ' '); k++);
                        lastIndex = k-1;
                        for (; k<formula.length() && formula.charAt(k) != ' ' && formula.charAt(k) != ')'; k++);
                        String nextToken;
                        if (k == formula.length())
                            nextToken = formula.substring(lastIndex+1);
                        else
                            nextToken = formula.substring(lastIndex+1, k);
                        if (nextToken.equals("true") || nextToken.equals("false"))
                            calcFieldType = BooleanTableField.DATA_TYPE;
                        else
                            calcFieldType = StringTableField.DATA_TYPE;
                    }else
                        calcFieldType = BooleanTableField.DATA_TYPE;
                }else{
                    try{
                        /* The following if-statement catches the case where the formula
                        * starts with a negative Integer or Double. In this case the '-'
                        * character is not an comparator but part of the operand.
                        */
                        if (firstOperand.length()==0 && formula.charAt(firstIndex) == '-') {
                            int r;
                            for (r=firstIndex+1; (r<formula.length() && formula.charAt(r) <= '9' && formula.charAt(r) >= '0'); r++);
                            if (r==formula.length()) {
                                calcFieldType = IntegerTableField.DATA_TYPE;
                                firstOperand = formula.substring(firstIndex);
                            }else{
                                String validOperators = "+/*%$-) ";
                                if (validOperators.indexOf(formula.charAt(r)) != -1) {
                                    calcFieldType = IntegerTableField.DATA_TYPE;
                                    firstOperand = formula.substring(firstIndex, r);
//                                    System.out.println("s"+firstOperand+"e");
                                }else if (formula.charAt(r) == '.') {
                                    r++;
                                    for (; (r<formula.length() && formula.charAt(r) <= '9' && formula.charAt(r) >= '0'); r++);

//                                        System.out.println("r: " + ", "+ formula.length());
                                    if (r==formula.length()) {
                                        calcFieldType = DoubleTableField.DATA_TYPE;
                                        firstOperand = formula.substring(firstIndex);
                                    }else{
                                        if (validOperators.indexOf(formula.charAt(r)) != -1) {
                                            firstOperand = formula.substring(firstIndex, r);
                                            calcFieldType = DoubleTableField.DATA_TYPE;
                                        }else
                                            calcFieldType = StringTableField.DATA_TYPE;
                                    }
                                }else
                                    calcFieldType = StringTableField.DATA_TYPE;
                            }
                        }else{
//1                            Integer k = new Integer(firstOperand);
                            calcFieldType = IntegerTableField.DATA_TYPE;
                        }
                    }catch (NumberFormatException e1) {
                        try{
//1                            Double f = new Double(firstOperand);
                            calcFieldType = DoubleTableField.DATA_TYPE;
                        }catch (NumberFormatException e2) {
//1                            try{
//1                                URL u = new URL(firstOperand);
                                calcFieldType = URLTableField.DATA_TYPE;
                                int z;
//                                System.out.println(lastIndex+1);
                                for (z=lastIndex+1; z<formula.length() && formula.charAt(z)!=' ' && formula.charAt(z)!='+'; z++);
                                firstOperand = firstOperand + new String(c, lastIndex, z-lastIndex);
/*1                            }catch (MalformedURLException e3) {
                                Date d1;
//                                System.out.println("First operand1: " + firstOperand);
                                ParsePosition pp = new ParsePosition(0);
                                try{
//                                    d1 = sdf.parse(firstOperand+":10", pp);
                                    d1 = dateFormat.parse(firstOperand, pp);
                                    calendar.setTime(d1);
                                    calendar.add(Calendar.HOUR, 10);
                                    d1 = calendar.getTime();
                                    //Don't know why this if-statement below is here!
//                                    if (pp.getIndex()!= (firstOperand.length()+3))
                                    if (pp.getIndex()!= (firstOperand.length())) // Since JDK1.1.5
                                        throw new Exception("");
                                }catch (Exception e) {
//                                    System.out.println("In exception " + e.getClass() + ", " + e.getMessage());
                                    d1=null;
                                }
//                                System.out.println("In Date: " + firstOperand+ " " + d1);
                                if (d1 != null) {
                                    calcFieldType = "gr.cti.eslate.database.engine.CDate";// "java.util.Date";
                                    isDate = true;
                                }else if (integerFound)
                                    calcFieldType = "java.lang.Integer";
                                else{
                                    Date d2;
                                    try{
                                        pp = new ParsePosition(0);
                                        d2 = timeFormat.parse(firstOperand, pp);
                                        if (pp.getIndex()!= firstOperand.length())
                                            throw new Exception("");
                                    }catch (Exception e) {d2=null;}
                                    if (d2 != null) {
                                        calcFieldType = "gr.cti.eslate.database.engine.CTime";//"java.util.Date";
                                        isDate = false;
                                    }else
                                        calcFieldType = "java.lang.String";
                                }
                            }
1*/
                        }
                    }
                }
            }else{
                openingQuote = true;
                openingQuoteIndex = i;
                calcFieldType = StringTableField.DATA_TYPE;
            }
        }

        String textFormula = formula;
        if (fieldIndices.size() != 0)
            formula = newFormula;

//        System.out.println("Field type: " + calcFieldType + "    " + isDate + " formula: " + formula);

        /* Check if this formula contains an operand expression or a single operand. In
        * the latter case, we cannot use one of the existing "OperandExpression" classes
        * to evaluate the field. We just assign the value of the single operand to all
        * the cells of the calculated field.
        * We examine the formula in order to find out if it contains any operators. If
        * it does, then it is not a single operand formula. First we check for the
        * operators "+", "-", "*", "/", "%", "$", "(" and ")".
        */
        boolean singleOperandFormula = true;
        if (fieldIndices.size() > 1)
            singleOperandFormula = false;

        if (singleOperandFormula) {
            /* Pay special attention to Date fields.
            */
            if (calcFieldType.equals(DateTableField.DATA_TYPE) ||
                    calcFieldType.equals(TimeTableField.DATA_TYPE)) { //"java.util.Date")) {
                String operatorsString = "+-*$%() ";
                for (int i=0; i<formula.length(); i++) {
                    if (formula.charAt(i) == '"') {
                        while (++i<formula.length() && formula.charAt(i) != '"');
                    }
                    if (i<formula.length() && operatorsString.indexOf(formula.charAt(i)) != -1) {
                        singleOperandFormula = false;
                        break;
                    }
                }
            }else if (calcFieldType.equals(URLTableField.DATA_TYPE)) {
                String operatorsString = "+-*$%() ";
                for (int i=0; i<formula.length(); i++) {
                    if (formula.charAt(i) == '"') {
                        while (++i<formula.length() && formula.charAt(i) != '"');
                    }
                    if (i<formula.length() && operatorsString.indexOf(formula.charAt(i)) != -1) {
                        singleOperandFormula = false;
                        break;
                    }
                }
            }else{
                String operatorsString;
                if (calcFieldType.equals(StringTableField.DATA_TYPE) || calcFieldType.equals(IntegerTableField.DATA_TYPE) || calcFieldType.equals(DoubleTableField.DATA_TYPE) || calcFieldType.equals(FloatTableField.DATA_TYPE))
                    operatorsString = "+-*/$%()";
                else
                    operatorsString = "+-*/$%() ";

                for (int i=0; i<formula.length(); i++) {
                    if (formula.charAt(i) == '"') {
                        while (++i<formula.length() && formula.charAt(i) != '"');
                    }
                    int operatorIndex;
                    if (i<formula.length() && (operatorIndex = operatorsString.indexOf(formula.charAt(i))) != -1) {
                        if (i==firstIndex && formula.charAt(i) == '-' && (calcFieldType.equals(IntegerTableField.DATA_TYPE) || calcFieldType.equals(FloatTableField.DATA_TYPE) || calcFieldType.equals(DoubleTableField.DATA_TYPE)))
                            continue;
                        singleOperandFormula = false;
                        break;
                    }
                }
//                System.out.println("singleOperandFormula: " + singleOperandFormula);
            }
        }

        /* Check for the existence of boolean operators: "and", "or" and "not".
        */
        if (singleOperandFormula) {
            String[] booleanOperators = {DBase.resources.getString("and"), DBase.resources.getString("or"), DBase.resources.getString("not")};
            String oper;
            int index;
            int counter;
            for (int i=0; i<3; i++) {
                oper = booleanOperators[i];
                if ((index = formula.indexOf(oper)) != -1) {
                    /* Check if "oper" in included in quotes.
                    */
                    counter = 0;
                    for (int k=0; k<index; k++) {
                        if (formula.charAt(k) == '"')
                            counter++;
                    }
                    if ((counter%2) == 0 && index != 0 && (formula.charAt(index-1) == ' ' || formula.charAt(index-1) == ')')) {
                        singleOperandFormula = false;
                        break;
                    }
                }
            }
        }

        /* The formula may contain a single operand (field or proper operand) with some
        * characters attached to it, i.e. without any comparator or blank separating them.
        * In these cases the operations above indicate that there exists only one operand
        * in the "formula", which is wrong.
        */
//        System.out.println(":::: " + fieldIndices.at(0).toString().length() + ", " + formula.length() + ", " + formula);
        if (singleOperandFormula && (fieldIndices.size() == 1) && (String.valueOf(fieldIndices.get(0)).length()+2)!=formula.trim().length())
            singleOperandFormula = false;
        /* firstOperand.length()!=0: we perform this test because in the case that the
        * "formula" starts with a quote, "firstOperand" contains no value.
        */
        if (singleOperandFormula && fieldIndices.size() == 0 && calcFieldType.equals(StringTableField.DATA_TYPE) && firstOperand.length()!=formula.trim().length()) {
//        if (singleOperandFormula && firstOperand.length()!=0 && firstOperand.length()!=formula.length()) {
//            System.out.println("In here: " + firstOperand);
            singleOperandFormula = false;
        }

//        System.out.println("singleOperandFormula: " + singleOperandFormula);

        /* If the formula contains a single operand then find it.
        */
        Object singleOperand = null;
        OperandExpression oe = null;
        if (singleOperandFormula) {
            if (fieldIndices.size() > 1)
                throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");

            /* If the single operand is a field contained in square brackets, then
            * assign the index of the field to the "singleOperand" object.
            */
            if (fieldIndices.size() == 1)
                singleOperand = new Integer(fieldIndices.get(0));
            else{
                if (calcFieldType.equals(IntegerTableField.DATA_TYPE)) {
                    try{
                        singleOperand = firstOperand;
                    }catch (Exception e) {throw new InvalidFormulaException(bundle.getString("CTableMsg11") + firstOperand);}
                }else if (calcFieldType.equals(DoubleTableField.DATA_TYPE)) {
                    try{
                        singleOperand = new Double(firstOperand);
                    }catch (Exception e) {throw new InvalidFormulaException(bundle.getString("CTableMsg12") + firstOperand);}
                }else if (calcFieldType.equals(FloatTableField.DATA_TYPE)) {
                    try{
                        singleOperand = new Float(firstOperand);
                    }catch (Exception e) {throw new InvalidFormulaException(bundle.getString("CTableMsg12") + firstOperand);}
                }else if (calcFieldType.equals(StringTableField.DATA_TYPE)) {
                    if (openingQuote) {
                        int closingQuoteIndex=-1;
                        for (int i=openingQuoteIndex+1; i<formula.length(); i++) {
                            if (formula.charAt(i) == '"') {
                                closingQuoteIndex = i;
                                break;
                            }
                        }
                        if (closingQuoteIndex != formula.length()-1)
                            throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");
                        else
                            singleOperand = formula.substring(openingQuoteIndex+1, closingQuoteIndex);
                    }else
                        singleOperand = firstOperand;
                }else if (calcFieldType.equals(BooleanTableField.DATA_TYPE))
                    singleOperand = new Boolean(firstOperand);
                else if (calcFieldType.equals(URLTableField.DATA_TYPE)) {
                    try{
                        singleOperand = new URL(firstOperand);
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
//t                }else if (calcFieldType.equals("java.util.Date")) {
                }else if (calcFieldType.equals(DateTableField.DATA_TYPE)) {
//t                    if (isDate)
                    singleOperand = new CDate(dateFormat.parse(firstOperand, new ParsePosition(0)));
                }else if (calcFieldType.equals(TimeTableField.DATA_TYPE)) {
                    singleOperand = new CTime(timeFormat.parse(firstOperand, new ParsePosition(0)));
                }else
                    throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");

            }
        }else{
            /* The formula is not a single operand formula, so create the proper "OperandExpression".
            * Check the data types of the fields in the formula against the speculated calculated
            * field's data type, if there exist fields in the formula. Create the proper
            * operand expression for the formula.
            */
            // Integer or Double
            if (calcFieldType.equals(IntegerTableField.DATA_TYPE) || calcFieldType.equals(DoubleTableField.DATA_TYPE) || calcFieldType.equals(FloatTableField.DATA_TYPE)) {
                Class fieldDataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    fieldDataType = tableFields.get(fieldIndices.get(i)).getDataType();
                    if (!fieldDataType.equals(IntegerTableField.DATA_TYPE) &&
                            !fieldDataType.equals(DoubleTableField.DATA_TYPE) &&
                            !fieldDataType.equals(FloatTableField.DATA_TYPE))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg13") + fieldName + "\". " +  bundle.getString("CTableMsg100") + tableFields.get(fieldIndices.get(i)).getName() + bundle.getString("CTableMsg14") + fieldDataType.getName().substring(fieldDataType.getName().lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                    else if (fieldDataType.equals(DoubleTableField.DATA_TYPE) || fieldDataType.equals(FloatTableField.DATA_TYPE))
                        calcFieldType = DoubleTableField.DATA_TYPE;
                }
                try{
                    oe = new NumericOperandExpression(formula, this);
//                    System.out.println("Creating NumericOperandExpression....");
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

                /* Check if the operand expression contains any double operand (i.e. 2.3).
                * In this case the calculated field's type will be Double.
                */
                if (((NumericOperandExpression) oe).containsDoubleOperand)
                    calcFieldType = DoubleTableField.DATA_TYPE;
                // Boolean
            }else if (calcFieldType.equals(BooleanTableField.DATA_TYPE)) {
                Class dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = tableFields.get(fieldIndices.get(i)).getDataType();
                    if (!dataType.equals(BooleanTableField.DATA_TYPE))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg16") + fieldName + "\". " + bundle.getString("CTableMsg100") + tableFields.get(fieldIndices.get(i)).getName() + bundle.getString("CTableMsg14") + dataType.getName().substring(dataType.getName().lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                }
                try{
                    oe = new BooleanOperandExpression(formula, this);
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

                // String
            }else if (calcFieldType.equals(StringTableField.DATA_TYPE)) {
                Class dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = tableFields.get(fieldIndices.get(i)).getDataType();
                    if (dataType.equals(ImageTableField.DATA_TYPE) || dataType.equals("Sound data type ???"))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg19") + fieldName + "\". " +  bundle.getString("CTableMsg100") + tableFields.get(fieldIndices.get(i)).getName() + bundle.getString("CTableMsg14") + dataType.getName().substring(dataType.getName().lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                }
                try {
                    oe = new StringOperandExpression(formula, this);
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}
                // Date or Time
//t            }else if (calcFieldType.equals("java.util.Date")) {
            }else if (calcFieldType.equals(DateTableField.DATA_TYPE) ||
                    calcFieldType.equals(TimeTableField.DATA_TYPE)) {
                Class dataType;
                int numOfDateFields = 0;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = tableFields.get(fieldIndices.get(i)).getDataType();
                    if (!(dataType.equals(IntegerTableField.DATA_TYPE) || dataType.equals(DoubleTableField.DATA_TYPE))) {
//                        System.out.println(dataType + ", " + (isDate== ((TableField) _getFields().at(((Integer) fieldIndices.at(i)).intValue())).isDate()));
                        if (!dataType.equals(DateTableField.DATA_TYPE) /*&& (!(isDate== (getFields().get(fieldIndices.get(i))).isDate()))*/) {
/*                            String dataType2 = dataType.substring(dataType.lastIndexOf('.')+1);
                            if (dataType2.equals("Date")) {
                                if (!((getFields().get(fieldIndices.get(i))).isDate()))
                                    dataType2 = "Time";
                            }
*/
//                            System.out.println("Hereee");
                            if (isDate)
                                throw new InvalidFormulaException(bundle.getString("CTableMsg17") + fieldName + "\". " + bundle.getString("CTableMsg100") + tableFields.get(fieldIndices.get(i)).getName() + bundle.getString("CTableMsg14") + AbstractTableField.localizedNameForDataType(tableFields.get(fieldIndices.get(i))) + bundle.getString("CTableMsg15"));
                            else
                                throw new InvalidFormulaException(bundle.getString("CTableMsg18") + fieldName + "\". " + bundle.getString("CTableMsg100") + tableFields.get(fieldIndices.get(i)).getName() + bundle.getString("CTableMsg14") + AbstractTableField.localizedNameForDataType(tableFields.get(fieldIndices.get(i))) + bundle.getString("CTableMsg15"));
                        }else
                            numOfDateFields++;
                    }
                }

                if (numOfDateFields > 1)
                    moreThan1DateFields = true;

                try{
//                    System.out.println("moreThan1DateFields: " + moreThan1DateFields + ", numOfDateFields: " + numOfDateFields);
                    creatingCalculatedField = true;
                    if (isDate)
                        oe = new DateOperandExpression(textFormula, formula, true, this, numOfDateFields);
                    else
                        oe = new DateOperandExpression(textFormula, formula, false, this, numOfDateFields);
                    creatingCalculatedField = false;
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                catch (IllegalDateOperandExpression e) {throw new InvalidFormulaException(e.message);}
                catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

                // For non-single-operand formulas with more than 1 Date operands
                moreThan1DateFields = ((DateOperandExpression) oe).moreThan1DateFields;
                Class calcDateFieldClass = null;
                calcDateFieldClass = ((DateOperandExpression) oe).getCalcFieldType(true);
//                System.out.println("Evaluated date calc field type to: " + calcDateFieldClass);
                if (calcDateFieldClass == null)
                    throw new InvalidFormulaException(bundle.getString("CTableMsg10") + textFormula + "\". " + bundle.getString("CTableMsg20"));
                else{
//t                    if (calcDateFieldClass.getName().equals("gr.cti.eslate.database.engine.CDate") ||
//t                    calcDateFieldClass.getName().equals("gr.cti.eslate.database.engine.CTime"))
//t                        calcFieldType = "java.util.Date";
//t                        calcFieldType = calcDateFieldClass.getName();
//t                    else
                    calcFieldType = calcDateFieldClass;
                }
                // URL
            }else if (calcFieldType.equals(URLTableField.DATA_TYPE)) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = tableFields.get(fieldIndices.get(i)).getDataType().getName();
                    if (!dataType.equals(URLTableField.DATA_TYPE) && !dataType.equals(StringTableField.DATA_TYPE))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg21") + fieldName + "\". " + bundle.getString("CTableMsg100") + tableFields.get(fieldIndices.get(i)).getName() + bundle.getString("CTableMsg14") + dataType.substring(dataType.lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                }
                try {
                    oe = new StringOperandExpression(formula, this);
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

                // any other
            }else{
                if ((calcFieldType.equals(ImageTableField.DATA_TYPE)))
                    throw new InvalidFormulaException(bundle.getString("CTableMsg22"));
                else
                    throw new InvalidFormulaException(bundle.getString("CTableMsg23") + calcFieldType.getName().substring(calcFieldType.getName().lastIndexOf('.') + 1) + bundle.getString("CTableMsg24"));
            }
        }

//        System.out.println("oe: " + oe + ", singleOperand: " + singleOperand+ " singleOperandFormula: " + singleOperandFormula);
        int calcFieldIndex;
        AbstractTableField calcField;
        String _prevDataType=null;
        if (!isEditingFormula) {
            try{
                _addField(fieldName, calcFieldType /*calcFieldType.substring(calcFieldType.lastIndexOf('.')+1)*/, false, isRemovable, false, false);
            }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(e.message);}
            catch (InvalidKeyFieldException e) {throw new InvalidKeyFieldException(e.message);}
            catch (InvalidFieldTypeException e) {System.out.println("Serious inconsistency error in CTable addCalculatedField(): (5)"); return null;}
            catch (Throwable thr) {thr.printStackTrace();}

            /* Increase the count of calculated fields in this table.
            */
            numOfCalculatedFields++;

            /* The just added calculated field is the last in the "tableFields" Array.
            */
            calcFieldIndex = tableFields.size()-1;
            calcField = tableFields.get(calcFieldIndex);
//            System.out.println("isDate: " + isDate);
//            calcField.setDate(isDate);
            try{
                calcField.setCalculated(true, formula, textFormula, fieldIndices);
                calcField.setEditable(false);
            }catch (AttributeLockedException exc) {System.out.println("Serious inconsistency error in CTable addCalculatedField(): (1.5)");}
            if (!singleOperandFormula)
                calcField.oe = oe;
            else
                calcField.oe = null;

            /* Notify any potential listeners that a new field has been added.
             */
            fireColumnAdded(calcField, false); //new ColumnAddedEvent(this, getFieldIndex(calcField))); //database.CTables.indexOf(this), calcField));
        }else{
            AbstractTableField f = null;
            int fIndex = 0;
            try{
                f = getTableField(fieldName);
                fIndex = getFieldIndex(fieldName);
            }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in Table addCalculatedField(): (2)");}
            String previousDataType = AbstractTableField.getInternalDataTypeName(f);

//            System.out.println("calcFieldType: " + calcFieldType + ", " + "currenttType: " + f.getFieldType().getName());
//            System.out.println("isDate: " + isDate + ", " + "f.isDate(): " + f.isDate());
//            if (!f.getFieldType().getName().equals(calcFieldType) && !isDate==f.isDate()) {
            if (!f.getDataType().equals(calcFieldType)) { //t || isDate!=f.isDate()) {
//                System.out.println("sd f.dependentCalcFields.isEmpty(): " + f.dependentCalcFields.isEmpty() + "," +
//                "moreThan1DateFields: " + moreThan1DateFields);

                // 24/1/1998 PENDING: possible error. I removed  "|| moreThan1DateFields)" from the following if-statement, without knowing why it is here                if (!f.dependentCalcFields.isEmpty())
                if (!(f.getDependentCalcFields().size() == 0))
                    throw new InvalidFormulaException(bundle.getString("CTableMsg25"));
                else{
                    try{
                        f = changeFieldDataType(f, calcFieldType);
//                        f.setFieldType(Class.forName(calcFieldType));
                    }catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in Table addCalculatedField() : (4)"); return null;}
                    catch (InvalidFieldTypeException e) {System.out.println("Serious inconsistency error in Table addCalculatedField() : (5)"); return null;}
///                    f.setDate(isDate);
                }
            }

            /* Remove the index of this field from the "dependentCalcFields" lists of all
            * the fields on which it depends.
            */
            IntBaseArray a = f.getDependsOnFields();
//            Integer i1 = new Integer(fIndex);
            for (int i=0; i<a.size(); i++)
                tableFields.get(a.get(i)).dependentCalcFields.removeElements(fIndex); //i1);

            f.setCalculated(true, formula, textFormula, fieldIndices);

            if (!singleOperandFormula)
                f.oe = oe;
            else
                f.oe = null;

            calcFieldIndex = fIndex;
            calcField = f;
            _prevDataType = previousDataType;
            setModified();
        }

        /* Insert the new calculated field in the "dependetCalcFields" list of each
        * of the fields that are contained in the "formula" of the calculated field.
        * This will help re-evaluate the calculated field each time one of the cells
        * of the fields, which are included in its formula, changes.
        */
        for (int i=0; i<fieldIndices.size(); i++) {
            AbstractTableField f1 = tableFields.get(fieldIndices.get(i));
            if (f1.dependentCalcFields.indexOf(calcFieldIndex) == -1)
                f1.dependentCalcFields.add(calcFieldIndex);
        }

//        System.out.println(isDate? : " + calcField.isDate());
        /* Calculate the values of the cells of the calculated field.
        */
        if (singleOperandFormula) {
            if (fieldIndices.size() == 0) {

                for (int i=0; i<recordCount; i++) {
                    try{
                        calcField.set(i, convertToFieldType(singleOperand, calcField));
                    }catch (Throwable thr) {
                        System.out.println("Unable to set value " + singleOperand + " for calculated field " + calcField.getName());
                    }
                }
//                    ((ArrayList) tableData.get(calcFieldIndex)).set(i, singleOperand);
                try{
                    calcField.calculatedValue = convertToFieldType(singleOperand, calcField);
                }catch (InvalidDataFormatException exc) {}
            }else{
                int singleFieldIndex = ((Integer) singleOperand).intValue();
                if (calcFieldType.equals(URLTableField.DATA_TYPE) && (tableFields.get(singleFieldIndex)).getDataType().equals(StringTableField.DATA_TYPE))
                    throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");

                AbstractTableField sourceField = tableFields.get(singleFieldIndex);
                for (int i=0; i<recordCount; i++) {
                    try{
                        calcField.set(i, convertToFieldType(sourceField.getObjectAt(i), calcField));
                    }catch (Throwable thr) {
                        System.out.println("Unable to set value " + sourceField.getObjectAt(i) + " for calculated field " + calcField.getName());
                    }
                }
//                    ((ArrayList) tableData.get(calcFieldIndex)).set(i, ((ArrayList) tableData.get(singleFieldIndex)).get(i));
            }
        }else{
            if (fieldIndices.size() == 0) {
                if (calcFieldType.equals(IntegerTableField.DATA_TYPE)) {
                    Object o = null;
                    boolean dateExpressionFlag = false;
                    if (oe.getClass().getName().equals("gr.cti.eslate.database.engine.DateOperandExpression"))
                        dateExpressionFlag = true;

                    if (dateExpressionFlag) {
                        for (int i=0; i<recordCount; i++) {
                            o = oe.execute(i, true);
                            if (o != null && o.getClass().equals(DoubleTableField.DATA_TYPE)) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            calcField.set(i, o);
//                            ((ArrayList) tableData.get(calcFieldIndex)).set(i, o);
                        }
                    }else{
                        for (int i=0; i<recordCount; i++) {
                            o = oe.execute(i, false);
                            if (o != null && o.getClass().equals(DoubleTableField.DATA_TYPE)) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            calcField.set(i, o);
//                            ((ArrayList) tableData.get(calcFieldIndex)).set(i, o);
                        }
                    }
                    try{
                        o = convertToFieldType(o, calcField);
                    }catch (InvalidDataFormatException exc) { o = null;}
/*                    if (o != null && o.getClass().equals(DoubleTableField.DATA_TYPE)) {
//                      o = (o==null)? null:new Integer(((Double) o).intValue());
                        o = new Integer(((Double) o).intValue());
                    }
*/
                    calcField.calculatedValue = o;
                }else if (calcFieldType.equals(URLTableField.DATA_TYPE)) {
                    try{
                        for (int i=0; i<recordCount; i++)
                            calcField.set(i, new URL((String)oe.execute(i, false)));
//                            ((ArrayList) tableData.get(calcFieldIndex)).set(i, new URL((String)oe.execute(i, false)));
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
                    try{
                        calcField.calculatedValue = new URL((String)oe.execute(0, false));
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
                }else{
                    for (int i=0; i<recordCount; i++)
                        calcField.set(i, oe.execute(i, false));
//                        ((ArrayList) tableData.get(calcFieldIndex)).set(i, oe.execute(i, false));
//                    System.out.println("calculatedValue: " + calcField.calculatedValue);
                    calcField.calculatedValue = oe.execute(0, false);
                }

/*                if (moreThan1DateFields) {
System.out.println("Evaluating date calc field type to: " + ((DateOperandExpression) oe).getCalcFieldType(true));
Integer tmp = new Integer(2);
int firstNonNull;
for (firstNonNull=0; firstNonNull<=recordCount && ((ArrayList) tableData.at(calcFieldIndex)).at(firstNonNull) == null; firstNonNull++);
System.out.print("tmp: ");
if (firstNonNull <= recordCount) {
System.out.println(((ArrayList) tableData.at(calcFieldIndex)).at(firstNonNull));
if (tmp.getClass().isInstance(((ArrayList) tableData.at(calcFieldIndex)).at(firstNonNull)))
calcField.setFieldType(tmp.getClass());
}
}
*/
            }else{
                if (calcFieldType.equals(IntegerTableField.DATA_TYPE)) {
                    Object o;
                    boolean dateExpressionFlag = false;
                    if (oe.getClass().getName().equals("gr.cti.eslate.database.engine.DateOperandExpression"))
                        dateExpressionFlag = true;
                    if (dateExpressionFlag)  {
                        for (int i=0; i<recordCount; i++) {
                            o = oe.execute(i, true);
                            if (o != null && o.getClass().equals(DoubleTableField.DATA_TYPE)) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            calcField.set(i, o);
//                            ((ArrayList) tableData.get(calcFieldIndex)).set(i, o);
                        }
                    }else{
                        for (int i=0; i<recordCount; i++) {
                            o = oe.execute(i, false);
                            if (o != null && o.getClass().equals(DoubleTableField.DATA_TYPE)) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            calcField.set(i, o);
//                            ((ArrayList) tableData.get(calcFieldIndex)).set(i, o);
                        }
                    }
                }else if (calcFieldType.equals(URLTableField.DATA_TYPE)) {
                    String u;
                    try{
                        for (int i=0; i<recordCount; i++) {
                            u = (String) oe.execute(i, false);
                            if (u!=null)
                                calcField.set(i, new URL(u));
//                                ((ArrayList) tableData.get(calcFieldIndex)).set(i, new URL(u));
                            else
                                calcField.set(i, null);
//                                ((ArrayList) tableData.get(calcFieldIndex)).set(i, null);
                        }
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
                }else{
                    for (int i=0; i<recordCount; i++)
                        calcField.set(i, oe.execute(i, false));
//                        ((ArrayList) tableData.get(calcFieldIndex)).set(i, oe.execute(i, false));
                }

                /*              if (moreThan1DateFields) {
                System.out.println("Evaluating date calc field type to: " + ((DateOperandExpression) oe).getCalcFieldType(true));
                Integer tmp = new Integer(2);
                int firstNonNull;
                for (firstNonNull=0; firstNonNull<=recordCount && ((ArrayList) tableData.at(calcFieldIndex)).at(firstNonNull) == null; firstNonNull++);

                System.out.print("tmp: ");

                if (firstNonNull <= recordCount) {
                System.out.println(((ArrayList) tableData.at(calcFieldIndex)).at(firstNonNull));
                if (tmp.getClass().isInstance(((ArrayList) tableData.at(calcFieldIndex)).at(firstNonNull)))
                calcField.setFieldType(tmp.getClass());
                }
                }
*/
            }
        }

        if (isEditingFormula) {
//t            if (this.database != null)
            fireCalcColumnFormulaChanged(calcField.getName(), textFormula, _prevDataType);
        }
        return calcField;
//        System.out.println("Type of calculated field: " + calcField.getFieldType());
    }

    /** Reports if field <code>f</code> is part of this Table's key.
     */
    public boolean isPartOfTableKey(AbstractTableField f) {
        if (f.table != this) return false;
        return tableKey.isPartOfTableKey(f);
    }

    /**
     *  Changes the formula of a calculated field.
     *  @param fieldName the name of the calculated field.
     *  @param formula the new formula.
     *  @return Returns <code>true</code>, if the formula of the calculated field is succesfully edited.
     *          Returns <code>false</code>, if the specified field is not calculated, or if it is
     *          part of the table's key.
     *  @exception InvalidFieldNameException If no field exists in the table with the supplied <code>fieldName</code>.
     *  @exception InvalidFormulaException If the new formula is not valid.
     *  @exception AttributeLockedException If the formula is locked.
     *  @see #addCalculatedField(java.lang.String, java.lang.String, boolean, boolean)
     */
    public boolean changeCalcFieldFormula(String fieldName, String formula)
            throws InvalidFieldNameException, InvalidFormulaException, AttributeLockedException {
        AbstractTableField f;
        try{
            f = getTableField(fieldName);
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(e.message);}

        if (!f.isCalculated() || tableKey.isPartOfTableKey(f)) //f.isKey())
            return false;

        try{
            addCalculatedField(fieldName, formula, false, true);
        }catch (IllegalCalculatedFieldException e) {System.out.println("Serious inconsistency error in Table changeCalcFieldFormula(): (1)");}
        catch (InvalidFormulaException e) {throw new InvalidFormulaException(e.message);}
        catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table changeCalcFieldFormula(): (2)");}

        for (int i=0; i<f.dependentCalcFields.size(); i++)
            evaluateCalculatedField(f.dependentCalcFields.get(i));

        setModified();
        return true;
    }


    /**
     *  Turns a calculated field into a normal one.
     *  @param fieldName The name of the calculated field
     *  @return <code>true</code>, if the operation is succesful. <code>false</code>, if the field is
     *          not calculated.
     *  @exception InvalidFieldNameException If no field exists in the table with the supplied <code>fieldName</code>.
     *  @exception AttributeLockedException If the field's <code>Calculated</code> attribute is locked.
     */
    public boolean switchCalcFieldToNormal(String fieldName)
            throws InvalidFieldNameException, AttributeLockedException {
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');
        AbstractTableField f;
        f = getTableField(fieldName);

        int index = tableFields.indexOf(f);

        if (!f.isCalculated())
            return false;

        f.setCalculated(false, null, null, null);
//        f.setEditable(true);
        numOfCalculatedFields--;

        /* Remove the field's index from the "dependentCalcFields" list of all
        * the fields it depends on.
        */
        AbstractTableField f1;
        for (int i=0; i<tableFields.size(); i++) {
            f1 = tableFields.get(i);
            if (!(f1.dependentCalcFields.size() == 0))
                f1.dependentCalcFields.remove(index);
        }

        setModified();
//t        if (this.database != null)
        fireCalcColumnReset(fieldName); //database.CTables.indexOf(this), fieldName));
        return true;
    }


    /**
     *  Evaluates all the cells of a calculated field.
     *  @param fieldIndex The index of the calculated field.
     */
    protected void evaluateCalculatedField(int fieldIndex) {
        AbstractTableField f = tableFields.get(fieldIndex);

//        System.out.println("Evaluating field: " + fieldIndex);
        if (f.oe == null) {
            if (f.dependsOnFields.size() == 1) {
                int secFieldIndex = f.dependsOnFields.get(0);
                AbstractTableField secField = tableFields.get(secFieldIndex);
                for (int i=0; i<recordCount; i++)
                    f.set(i, secField.getObjectAt(i));
            }
        }else{
            if (!(f.dependsOnFields.size() == 0)) {
                if (f.getDataType().equals(IntegerTableField.DATA_TYPE)) {
                    Object o;
                    for (int i=0; i<recordCount; i++) {
                        o = f.oe.execute(i, false);
                        if (o != null && o.getClass().equals(DoubleTableField.DATA_TYPE)) {
//                          o = (o==null)? null:new Integer(((Double) o).intValue());
                            o = new Integer(((Double) o).intValue());
                        }
                        f.set(i, o);
                    }
                }else if (f.getDataType().equals(URLTableField.DATA_TYPE)) {
                    String u;
                    try{
                        for (int i=0; i<recordCount; i++) {
                            u = (String) f.oe.execute(i, false);
                            if (u!=null)
                                f.set(i, new URL(u));
                            else
                                f.set(i, null);
                        }
                    }catch (MalformedURLException e) {}
                }else{
                    for (int i=0; i<recordCount; i++)
                        f.set(i, f.oe.execute(i, false));
                }

            }
        }

        for (int i=0; i<f.dependentCalcFields.size(); i++)
            evaluateCalculatedField(f.dependentCalcFields.get(i));

        setModified();

    }


    protected void updatePrimaryKeyMapForCalcField(int fieldIndex, int recIndex, Object value) {
//        System.out.println("Entering updatePrimaryKeyMapForCalcField()");
        if (getRecordEntryStructure().isRecordAdditionPending()) { //1 pendingNewRecord) {
            /* If a new record is being interactively inserted in table which contains a key, then
            * there is not entry in the "primaryKeyMap" for this record. So just insert the new
            * entry.
            */
            if (tableKey.primaryKeyFieldIndex == fieldIndex)
                tableKey.addRecordToPrimaryKeyHash(new Integer(value.hashCode()), recIndex);
//            if (keyFieldIndices.get(0) == fieldIndex)
//                primaryKeyMap.add(new Integer(value.hashCode()), new Integer(recIndex)); //2.0.1 tableRecords.at(recIndex));
        }else{
            /*If field f is part of the table's key and is also the field whose values
            *are used in the primarykeyMap HashMap, then the entry for this record in
            *the HashMap has to be deleted and a new entry with the correct new key
            *value has to be entered.
            */
            if (tableKey.primaryKeyFieldIndex == fieldIndex) {
                tableKey.removeRecordFromPrimaryKeyHash(recIndex);
                tableKey.addRecordToPrimaryKeyHash(new Integer(value.hashCode()), recIndex);
            }
/*            if (keyFieldIndices.get(0) == fieldIndex) {
                Object prevValue = riskyGetCell(fieldIndex, recIndex);
//              System.out.println(prevValue + ", " + prevValue.hashCode() + value.hashCode());
                HashMapIterator iter = primaryKeyMap.find(new Integer(prevValue.hashCode()));
//2.0.1                while (((Record) iter.value()).getIndex() != recIndex && !iter.atEnd())
                while (((Integer) iter.value()).intValue() != recIndex && !iter.atEnd())
                    iter.advance();
                primaryKeyMap.remove(iter);
//                System.out.println("Removing: " + iter.key() + ", " + ((Record) iter.value()).getIndex());
                primaryKeyMap.add(new Integer(value.hashCode()), new Integer(recIndex)); //2.0.1 tableRecords.at(recIndex));
//                System.out.println("Inserting : " + ((Record) tableRecords.at(recIndex)).getIndex() + " with key: " + value);
            }
*/
        }
//        System.out.println("Exiting updatePrimaryKeyMapForCalcField()");
    }


    /**
     *  Evaluates a particular cell of a calculated field.
     *  @param fieldIndex The index of the calculated field.
     *  @param fieldIndex The index of the record, to which the cell belongs.
     *  @exception DuplicateKeyException If the calculated field belongs to the table's key and the
     *                                   calculated value of the cell violates key uniqueness.
     *  @exception NullTableKeyException If the calculated field belongs to the table's key and the
     *                                   calculated value of the cell is <code>null</code>.
     */
    protected void evaluateCalculatedFieldCell(int fieldIndex, int rowIndex)
            throws DuplicateKeyException, NullTableKeyException {
        AbstractTableField f = tableFields.get(fieldIndex);
        Object oldValue = riskyGetCell(fieldIndex, rowIndex);

//        System.out.println("Evaluating field: " + fieldIndex);
        if (f.oe == null) {
            if (f.dependsOnFields.size() == 1) {
                int secFieldIndex = f.dependsOnFields.get(0);
                AbstractTableField secField = tableFields.get(secFieldIndex);
                if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                    Object value = riskyGetCell(secFieldIndex, rowIndex);
                    if (value == null) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                    if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, value)) {
                        updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                        f.set(rowIndex, secField.getObjectAt(rowIndex));
//                        ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, ((ArrayList) tableData.get(secFieldIndex)).get(rowIndex));
                    }else throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                }else{
//                  for (int i=0; i<recordCount; i++)
                    f.set(rowIndex, secField.getObjectAt(rowIndex));
//                    ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, ((ArrayList) tableData.get(secFieldIndex)).get(rowIndex));
                }
            }
        }else{
            if (!(f.dependsOnFields.size() == 0)) {

                if (f.getDataType().equals(IntegerTableField.DATA_TYPE)) {
                    Object o;
//                    System.out.println("rowIndex: " + rowIndex);
                    if (f.oe.getClass().getName().equals("gr.cti.eslate.database.engine.DateOperandExpression"))
                        o = f.oe.execute(rowIndex, true);
                    else
                        o = f.oe.execute(rowIndex, false);
//                    System.out.println("o: " + o);
                    if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                      o = (o==null)? null:new Integer(((Double) o).intValue());
                        o = new Integer(((Double) o).intValue());
                    }
//                    System.out.println("o= " + o + ", " + o.getClass());
                    if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                        if (o == null) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                        else{
                            if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, o)) {
                                updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, o);
                                f.set(rowIndex, o);
//                                ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, o);
                            }else throw new DuplicateKeyException(bundle.getString("CTableMsg27") + o + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                        }
                    }else
                        f.set(rowIndex, ((o==null)? null: o));
//                        ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, ((o==null)? null: o));

                }else if (f.getDataType().equals(URLTableField.DATA_TYPE)) {
                    String u;
                    try{
                        u = (String) f.oe.execute(rowIndex, false);
                        if (u!=null) {
                            if (tableKey.isPartOfTableKey(f)) {//f.isKey()) {
                                if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, u)) {
                                    updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, new URL(u));
                                    f.set(rowIndex, new URL(u));
//                                        ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, new URL(u));
                                }else throw new DuplicateKeyException(bundle.getString("CTableMsg29") + u + bundle.getString("CTableMsg27") + tableFields.get(fieldIndex).getName() + "\"");
                            }else
                                f.set(rowIndex, new URL(u));
//                                ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, new URL(u));
                        }else{
                            if (tableKey.isPartOfTableKey(f)) //f.isKey())
                                throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                            else
                                f.set(rowIndex, null);
//                                ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, null);
                        }
                    }catch (MalformedURLException e) {
                        throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");}

                }else{

                    if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                        Object value = f.oe.execute(rowIndex, false);
                        if (value == null) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                        if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, value)) {
                            updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                            f.set(rowIndex, value);
//                            ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, value);
                        }else throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                    }else
                        f.set(rowIndex, f.oe.execute(rowIndex, false));
//                        ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, f.oe.execute(rowIndex, false));
                }
            }
        }

        if (f.getDependsOnFields().size() == 0) {
//            System.out.println("Calculating cell " + rowIndex + " for field: " + f.getName());
            if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                if (f.calculatedValue == null)
                    throw new NullTableKeyException(bundle.getString("CTableMsg30") + f.getName() +"\"");
                else
                    throw new DuplicateKeyException(bundle.getString("CTableMsg31") + f.calculatedValue + bundle.getString("CTableMsg28") + f.getName() + "\"");
            }else
                f.set(rowIndex, f.calculatedValue);
//                ((ArrayList) tableData.get(fieldIndex)).set(rowIndex, f.calculatedValue);
        }

        Object newValue = riskyGetCell(fieldIndex, rowIndex);
//        System.out.println("newValue: " + newValue + ", oldValue: " + oldValue);
        if ((oldValue == null && newValue != null) || (oldValue != null && newValue == null) ||
                (oldValue != null && newValue != null && !oldValue.equals(newValue))) {
//t            if (this.database != null)
            fireCellValueChanged(f.getName(), rowIndex, oldValue, (f.dependentCalcFields.size() != 0)); //this.database.CTables.indexOf(this), f.getName(), rowIndex, newValue, oldValue, (f.dependentCalcFields.size() != 0)));

        }

        try {
            for (int i=0; i<f.dependentCalcFields.size(); i++) {
                AbstractTableField f1 = tableFields.get(f.dependentCalcFields.get(i));
                evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), rowIndex);
                changedCalcCells.put(f1.getName(), new Integer(rowIndex));
            }
        }catch (DuplicateKeyException e) {throw new DuplicateKeyException(e.message);}
        catch (NullTableKeyException e) {throw new NullTableKeyException(e.message);}

    }


    /**
     *  Evaluates a particular cell of a calculated field. Used by addRecord().
     *  @param fieldIndex The index of the calculated field.
     *  @param fieldIndex The index of the record, to which the cell belongs.
     *  @exception DuplicateKeyException If the calculated field belongs to the table's key and the
     *                                   calculated value of the cell violates key uniqueness.
     *  @exception NullTableKeyException If the calculated field belongs to the table's key and the
     *                                   calculated value of the cell is <code>null</code>.
     */
/*    protected void evaluateAndAddCalculatedFieldCell(int fieldIndex, int rowIndex)
            throws DuplicateKeyException, NullTableKeyException {
        AbstractTableField f = tableFields.get(fieldIndex);

//        System.out.println("Evaluating field: " + fieldIndex);
        if (f.oe == null) {
            if (f.dependsOnFields.size() == 1) {
                int secFieldIndex = f.dependsOnFields.get(0);
                AbstractTableField secField = tableFields.get(secFieldIndex);
                if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                    Object value = riskyGetCell(secFieldIndex, rowIndex);
                    if (value == null) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                    if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, value)) {
                        updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                        f.add(secField.getObjectAt(rowIndex));
//                        ((ArrayList) tableData.get(fieldIndex)).add(((ArrayList) tableData.get(secFieldIndex)).get(rowIndex));
                    }else throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                }else{
//                  for (int i=0; i<recordCount; i++)
                    f.add(secField.getObjectAt(rowIndex));
//                    ((ArrayList) tableData.get(fieldIndex)).add(((ArrayList) tableData.get(secFieldIndex)).get(rowIndex));
                }
            }
        }else{
            if (!(f.dependsOnFields.size() == 0)) {

                if (f.getDataType().equals(IntegerTableField.DATA_TYPE)) {
                    Object o;
//                    System.out.println("rowIndex: " + rowIndex);
                    o = f.oe.execute(rowIndex, false);
//                    System.out.println("o= " + o + ", " + o.getClass());
                    if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                        if (o == null) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                        else{
                            if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, o)) {
                                updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, o);
                                f.add(o);
//                                ((ArrayList) tableData.get(fieldIndex)).add(o);
                            }else throw new DuplicateKeyException(bundle.getString("CTableMsg27") + o + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                        }
                    }else
                        f.add(((o==null)? null: o));
//                        ((ArrayList) tableData.get(fieldIndex)).add(((o==null)? null: o));

                }else if (f.getDataType().equals(URLTableField.DATA_TYPE)) {
                    String u;
                    try{
                        u = (String) f.oe.execute(rowIndex, false);
                        if (u!=null) {
                            if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                                if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, u)) {
                                    updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, new URL(u));
                                    f.add(new URL(u));
//                                    ((ArrayList) tableData.get(fieldIndex)).add(new URL(u));
                                }else throw new DuplicateKeyException(bundle.getString("CTableMsg29") + u + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                            }else
                                f.add(new URL(u));
//                                ((ArrayList) tableData.get(fieldIndex)).add(new URL(u));
                        }else{
                            if (tableKey.isPartOfTableKey(f)) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                            else
                                f.add(null);
//                                ((ArrayList) tableData.get(fieldIndex)).add(null);
                        }
                    }catch (MalformedURLException e) {
                        throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");}

                }else{

                    if (tableKey.isPartOfTableKey(f)) { //f.isKey()) {
                        Object value = f.oe.execute(rowIndex, false);
                        if (value == null) throw new NullTableKeyException(bundle.getString("CTableMsg26") + tableFields.get(fieldIndex).getName() + "\"");
                        if (!tableKey.violatesKeyUniqueness(fieldIndex, rowIndex, value)) {
                            updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                            f.add(value);
//                            ((ArrayList) tableData.get(fieldIndex)).add(value);
                        }else throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + tableFields.get(fieldIndex).getName() + "\"");
                    }else
                        f.add(f.oe.execute(rowIndex, false));
//                        ((ArrayList) tableData.get(fieldIndex)).add(f.oe.execute(rowIndex, false));
                }
            }
        }

    }
*/

    /**
     *  Removes a field from the Table structure. However, it does not remove the data of the field
     *  from the Table data repository. This method should not be used together with the <code>removeField</code>
     *  method. It is useful only in these cases, where the application which uses this API, e.g. the database
     *  GUI, needs to delete the field's data itself. This case was raised using the <code>Java foundation classes' JTable</code> component.
     *  @param fieldName The name of the field to be deleted.
     *  @exception InvalidFieldNameException If no field exists with the specified <code>fieldName</code>.
     *  @exception TableNotExpandableException If the Table is not expandable.
     *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this table.
     */
/*    public void prepareRemoveField(String fieldName) throws InvalidFieldNameException,
            TableNotExpandableException, CalculatedFieldExistsException,
            FieldNotRemovableException, AttributeLockedException {
        _removeField(getTableField(fieldName), false);
    }
*/

    /**
     *  Removes all the fields of the Table.
     *  @exception TableNotExpandableException If the Table is not expandable.
     *  @exception FieldNotRemovableException If any field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this table.
     */
    public void removeAllFields() throws TableNotExpandableException, FieldNotRemovableException, AttributeLockedException {
        if (tableFields.size() == 0) return;

        for (int i=tableFields.size()-1; i>0; i--) {
            try{
                _removeField(tableFields.get(i), true);
            }catch (CalculatedFieldExistsException exc) {
               System.out.println("Inconstistency error in Table.removeAllFields() 1. Operation halted...");
                exc.printStackTrace();
                return;
            }
        }
        try{
            _removeField(tableFields.get(0), false);
        }catch (CalculatedFieldExistsException exc) {
           System.out.println("Inconstistency error in Table.removeAllFields() 2. Operation halted...");
            exc.printStackTrace();
            return;
        }
    }

    /**
     *  Removes a field from the Table.
     *  @param fieldName The name of the field to be deleted.
     *  @exception InvalidFieldNameException If no field exists with the specified <code>fieldName</code>.
     *  @exception TableNotExpandableException If the Table is not expandable.
     *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this table.
     */
    public void removeField(String fieldName) throws InvalidFieldNameException,
            TableNotExpandableException, CalculatedFieldExistsException,
            FieldNotRemovableException, AttributeLockedException {
        _removeField(getTableField(fieldName), false);
    }

    /**
     *  Removes a field from the Table.
     *  @param fieldName The name of the field to be deleted.
     *  @param moreToBeRemoved When multiple fields are to be removed, then this flag should be <code>true</code> for
     *  all the fields but the last one to be removed. The listeners of the <code>FieldRemovedEvent</code>s are notified
     *  about whether there are more fields to be removed and act accordingly.
     *  @exception InvalidFieldNameException If no field exists with the specified <code>fieldName</code>.
     *  @exception TableNotExpandableException If the Table is not expandable.
     *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this table.
     */
    public void removeField(String fieldName, boolean moreToBeRemoved) throws InvalidFieldNameException,
            TableNotExpandableException, CalculatedFieldExistsException,
            FieldNotRemovableException, AttributeLockedException {
        _removeField(getTableField(fieldName), moreToBeRemoved);
    }

    final void _removeField(AbstractTableField field, boolean moreToBeRemoved) throws
            TableNotExpandableException, CalculatedFieldExistsException,
            FieldNotRemovableException, AttributeLockedException {
        String fieldName = field.getName();
        //Checking for a valid field name
//        if (fieldName.equals("") || fieldName == null)
//            throw new InvalidFieldNameException(bundle.getString("CTableMsg32"));

        if (!fieldRemovalAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg87"));

        /* If this is the only field in a Table, which contains records, then the
        * records will have to be removed. However, if the table is not expandable,
        * the records cannot be removed and thus the field cannot be removed either.
        */
        if (tableFields.size() ==1 && recordCount > 0 && !recordRemovalAllowed)
            throw new TableNotExpandableException(bundle.getString("CTableMsg86"));

        //Checking if a column with the this name exists
/*        boolean fieldFound = false;
int index = 0;
for (int i = 0; i<tableFields.size(); i++) {
if (((TableField) tableFields.get(i)).getName().equals(fieldName)) {
fieldFound = true; index = i;
break;
}
}
if (!fieldFound)
throw new InvalidFieldNameException(bundle.getString("CTableMsg33") + fieldName + "\"");

int removedFieldIndex = index;
*/
        //Setting the field's name to null, so that it cannot be used
        //with the Table API anymore
//        TableField f = (TableField) tableFields.get(index);

        if (tableKey.isPartOfTableKey(field)/*field.isKey()*/ && !keyChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg90"));

        if (!field.isRemovable())
            throw new FieldNotRemovableException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg34"));

        /* Check if there exist other calculated fields in this table, which depend on
        * the field to be removed.
        */
        if (!(field.dependentCalcFields.size() == 0))
            throw new CalculatedFieldExistsException(bundle.getString("CTableMsg35"));

        //Removing the index of the field from "keyFieldIndices" Array, if the table is KEYyed
        if (tableKey.isPartOfTableKey(field)/*field.isKey()*/ && hasKey()) {
            boolean keyChangeAllowedReset = false;
            try {
                if (!field.isFieldKeyAttributeChangeAllowed()) {
                    field.setFieldKeyAttributeChangeAllowed(true);
                    keyChangeAllowedReset = true;
                }
                removeFromKey(field.getName());
            }catch (FieldIsNotInKeyException e) {System.out.println("Serious inconsistency error in CTable removeField(): (1)."); e.printStackTrace(); return;}
            catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in CTable removeField(): (2)."); e.printStackTrace(); return;}
            catch (TableNotExpandableException e) {
                if (keyChangeAllowedReset)
                    field.setFieldKeyAttributeChangeAllowed(false);
                throw new TableNotExpandableException(bundle.getString("CTableMsg36"));}
            catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable removeField(): (2.5)."); e.printStackTrace(); return;}
        }

        //Removing the field from the recordEntryForm HashMap
//        recordEntryForm.remove(index);

        //Removing the field from the tableFields array
//        if (removeData) {
//            System.out.println("Removing index: " + index + " from tableFields");
            tableFields.remove(field.indexInTable);
//        }

        //Removing the field from the tableData Array
        if (tableFields.size() != 0) {
//            if (removeData) {
//                System.out.println("Removing data");
///            tableData.remove(field.index);
//            }
            tableKey.decrementRestKeyFieldIndices(field.indexInTable);
        }else{
            //This is the last field of the table, so remove all the table's records
            try{
                for (int i=recordCount-1; i>0; i--)
                    removeRecord(i, rowForRecord(i), true, true, null);
                if (recordCount > 0)
                    removeRecord(0, 0, false, true, null);
            }catch (Throwable thr) {
                System.out.println("Inconsistency error in Table.removeField() while removing all records");
            }
///            tableData.clear();

/*            setActiveRecord(-1);
            resetSelectedSubset();
            tableFields.clear();
//2.0.1            tableRecords.clear();
            recordSelection.clear();
            selectedSubset.clear();
            unselectedSubset.clear();
            recordCount = 0;
            recordIndex.clear();
///            hasKey = false;
            tableKey.clear();
*/
//            keyFieldIndices.clear();
//            primaryKeyMap = null;
        }

        /* ATTENTION
        * Scan all the fields of the table. Find the calculated ones. Get each calculated
        * field's operand expression "oe". Get the "postfix" stack of the "oe". Reduce
        * by 1 the field index of all the "StackElements" in stack "postfix", which have
        * their "isFieldIndex" flag "true" and the field index they store is greater than
        * the index of the removed field.
        * This anomaly was inevitable since the "postfix" stack, to which a calculated
        * field's formula is transformed and which is used to evaluate the field, stores
        * indices and not names for field operands (This decision was taken in order to
        * speed up as much as possible the evaluation of a calculated field). Due to this
        * behaviour, whenever a field is removed from the table, the "postfix" Stack
        * becomes invalid because the indices of the fields change. To correct this
        * anomaly, we introduce the following corrective code.
        */
        AbstractTableField f1;
        StackElement se;
        for (int i=0; i<tableFields.size(); i++) {
            f1 = tableFields.get(i);
//            System.out.println("-------"+f1.getName()+"-------" + f1.oe);
            if (f1.isCalculated()) {
                if (f1.oe!=null) {
                    Stack postfix = null;
                    if (f1.getDataType().equals(IntegerTableField.DATA_TYPE) || f1.getDataType().equals(DoubleTableField.DATA_TYPE))
                        try{
                            postfix = ((NumericOperandExpression) f1.oe).postfix;
                        }catch (ClassCastException e) {
                            postfix = ((DateOperandExpression) f1.oe).postfix;
                        }
                    else if (f1.getDataType().equals(StringTableField.DATA_TYPE) || f1.getDataType().equals(URLTableField.DATA_TYPE))
                        postfix = ((StringOperandExpression) f1.oe).postfix;
                    else if (f1.getDataType().equals(BooleanTableField.DATA_TYPE))
                        postfix = ((BooleanOperandExpression) f1.oe).postfix;
//t                    else if (f1.fieldType.getName().equals("java.util.Date"))
                    else if (f1.getDataType().equals(DateTableField.DATA_TYPE) ||
                            f1.getDataType().equals(TimeTableField.DATA_TYPE))
                        postfix = ((DateOperandExpression) f1.oe).postfix;
//                    postfix = f1.oe.postfix;

                    ForwardIterator fi = postfix.start();
                    while (!fi.atEnd()) {
                        se = (StackElement) fi.get();
                        if (se.isFieldIndex) {
//                            System.out.println("Element: " + se.element);

                            /* Special case: "DateOperandExpression"s. In this case se.element does
                            * not contain the string representation of an "int", but smth like
                            * "[<field_index>]h", where 'h' can also be 'd', 'y', 'h', 'm' if
                            * the field is of Integer type or "[<field_index>]", if the field is of
                            * Date/Time type.
                            */
                            if (f1.oe instanceof DateOperandExpression) {
                                int i1 = field.indexInTable;
                                char dateField = ((String) se.element).charAt(((String) se.element).length()-1);
                                if (dateField == 'h' || dateField == 'm' || dateField == 'y' || dateField == 'd') {
                                    String i1Str = ((String) se.element).substring(1, ((String) se.element).length()-2);
//                                    System.out.println("i1Str: " + i1Str);
                                    i1 = new Integer(i1Str).intValue();
                                }else{
                                    String i1Str = ((String) se.element).substring(1, ((String) se.element).length()-1);
//                                    System.out.println("i1Str: " + i1Str);
                                    i1 = new Integer(i1Str).intValue();
                                }

                                if (i1 > field.indexInTable) {
                                    if (dateField != ']')
                                        se.element = '[' + new Integer(i1-1).toString() + ']' + dateField;
                                    else
                                        se.element = '[' + new Integer(i1-1).toString() + ']';
//                                    System.out.println("Tranformed se.element: " + se.element);
                                    /* Reduce each the corresponding field indices in the
                                    * "dependsOnFields" list of the calculated field.
                                    */
//                                    System.out.println("dependsOnFields BEFORE: " + f1.dependsOnFields);
                                    f1.dependsOnFields.set(f1.dependsOnFields.indexOf(i1), (i1-1));
//                                    System.out.println("dependsOnFields AFTER: " + f1.dependsOnFields);
                                }
                                /* General case.
                                */
                            }else{
                                int i1 = new Integer((String) se.element).intValue();
                                if (i1 > field.indexInTable) {
                                    se.element = new Integer(i1-1).toString();
                                    /* Reduce each the corresponding field indices in the
                                    * "dependsOnFields" list of the calculated field.
                                    */
//                                    System.out.println("dependsOnFields BEFORE: " + f1.dependsOnFields);
                                    f1.dependsOnFields.set(f1.dependsOnFields.indexOf(i1), (i1-1));
//                                    System.out.println("dependsOnFields AFTER: " + f1.dependsOnFields);
                                }
                            }
                        }

                        fi.advance();
                    }
                }else{
                    /* A calculated field without "OperandExpression".
                    */
                    if (!(f1.dependsOnFields.size() == 0)) {
//                        System.out.println(" dependsOnFields BEFORE: " + f1.dependsOnFields);
                        int e = f1.dependsOnFields.get(0);
                        if (e > field.indexInTable)
                            f1.dependsOnFields.set(0, (e-1));
//                        System.out.println(" dependsOnFields AFTER: " + f1.dependsOnFields);
                    }
                }
            }
        }


//        System.out.println("AFTER CHECK 1");
        /* If the removed field was a calculated one, then we have to remove it from
        * the "dependentCalcFields" lists of all the fields, on which it depends.
        */
        if (field.isCalculated()) {
            /* Reduce by 1 the number of calculated fields in this Table.
            */
            numOfCalculatedFields--;
/*            System.out.print("Field under removal: " + field.getName() + ", index: " + field.indexInTable + ", dependsOnFields: ");
            for (int i=0; i<field.dependsOnFields.size(); i++)
                System.out.print(field.dependsOnFields.get(i) + ", ");
            System.out.println();
*/
            for (int i=0; i<field.dependsOnFields.size(); i++) {
                AbstractTableField masterField = tableFields.get(field.dependsOnFields.get(i));
/*                System.out.println("Field: "+ masterField.getName() + " dependentCalcFields: " + masterField.dependentCalcFields);
                for (int k=0; k<masterField.dependentCalcFields.size(); k++)
                    System.out.print(masterField.dependentCalcFields.get(k) + ", ");
                System.out.println();
*/
                masterField.dependentCalcFields.removeElements(field.indexInTable);
            }
        }

//        System.out.println("AFTER CHECK 2");

        /* Reduce by 1 the indices of the calculated fields, whose indices are greater
        * than "index", in the "dependentCalcFields" of each field.
        */
        for (int i=0; i<tableFields.size(); i++) {
            f1 = tableFields.get(i);
            if (!(f1.dependentCalcFields.size() == 0)) {
//                System.out.print(f1.getName() +": ");
                IntBaseArray a = f1.dependentCalcFields;
//                System.out.print(a);
                int n;
                for (int k=0; k<a.size(); k++) {
                    n = a.get(k);
                    if (n > field.indexInTable)
                        a.set(k, (n-1));
                }
//                System.out.println("  " +a);
            }
        }

        /* Reduce by 1 the 'indexInTable' field of every TableField in this Table, whose 'indexInTable' value
         * is greater than the 'indexInTable' of the removed field.
         */
        for (int i=0; i<tableFields.size(); i++) {
            AbstractTableField fld = tableFields.get(i);
            if (fld.indexInTable > field.indexInTable)
                fld.indexInTable--;
        }
//        System.out.println("Field \"" + fieldName + "\" was removed.");
        setModified();

//t        if (database != null)
        fireColumnRemoved(field, moreToBeRemoved); //database.CTables.indexOf(this), fieldName));
//        System.out.println("After fireFieldRemoved");

        //Invalidating this field
        field.setName(null);
        field.indexInTable = -1;
        return;

    }


/*    public boolean addRecord(Array recEntryForm) throws InvalidDataFormatException,
            NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException {
        return addRecord(convertArrayToArrayList(recEntryForm));
    }
*/
    static long addRecTime1 = 0, addRecTime2 = 0, addRecTime3 = 0, addRecTime4 = 0, addRecTime5 = 0, addRecTime6 = 0, addRecTime7 = 0;
    /**
     *  Adds a record to the Table.
     *  @param recEntryForm An ArrayList which contains the data of the new record.
     *  @param moreToBeAdded A flag which is passed to the generated <code>RecordAddedEvent</code> in order to
     *  notify the listeners if there are more records to be added.
     *  @return <code>true</code>, if the record is added. <code>false</code>, if a null or empty
     *  or with different size from the standard table's records size <code>recEntryForm</code>
     *  is provided.
     *  @exception InvalidDataFormatException If a piece of the data for the new record cannot be cast to the proper data type, i.e.the data type of the field where the data will be stored.
     *  @exception NoFieldsInTableException If the table contains no fields.
     *  @exception NullTableKeyException If the table has a key and the new record's key is null.
     *  @exception DuplicateKeyException If the new record's key is the same as the key of an existing record in the table.
     *  @exception TableNotExpandableException If the table is not expandable.
     */
    public boolean addRecord(ArrayList recEntryForm, boolean moreToBeAdded) throws InvalidDataFormatException,
            NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException {
        long st1 = System.currentTimeMillis();
        if (!recordAdditionAllowed) {
            throw new TableNotExpandableException(bundle.getString("CTableMsg37"));
        }

        if (recEntryForm == null || recEntryForm.isEmpty() || recEntryForm.size() != (tableFields.size()-numOfCalculatedFields)) {
//            System.out.println(" Incorrect record entry form. Record insertion failed!");
            return false;
        }

/*        if  (timeToGarbageCollect == 5000) {
System.gc();
timeToGarbageCollect = 0;
}else
timeToGarbageCollect++;
*/
        //Examining if the table has fields
        if (tableFields.size() == 0)
            throw new NoFieldsInTableException(bundle.getString("CTableMsg38"));

//        Object[] recordData = new Object[recEntryForm.size()]; //temporary storage for the values of the new record
//1        boolean keyOK = true;
//1        Class[] cl = null; //new Class[1];
//1        Object[] val = null;//new Object[1];
//1        Constructor con;
//        Integer recPrimaryKey = null;

        Object value;
        AbstractTableField f;
        addRecTime1 = addRecTime1 + System.currentTimeMillis()-st1;

        long st2 = System.currentTimeMillis();
        RecordEntryStructure res = getRecordEntryStructure();
        try{
            res.startRecordEntry();
        }catch (UnableToAddNewRecordException exc) {throw new TableNotExpandableException(exc.getMessage());}
        for (int k=0; k<recEntryForm.size(); k++) {
            value = recEntryForm.get(k);
//            System.out.println("value: " + value);
            f = tableFields.get(k);

            /* Skip any calculated field in the way. The recordEntryForm does not contains
            * values for the calculated fields.
            */
            int l =0;
            while (f.isCalculated()) {
                f = tableFields.get(k+l);
                l++;
            }

            //Don't check the table's key fields, because they have already been checked above
//            if (recordData[k] != null)
//                continue;

            /*If the value is null, then don't perform any data type conversion. This
            *value will simply be inserted in the table.
            */
            if (value == null)
                continue;

            res.setCell(f, value);

//            System.out.println("value class: " + value.getClass() + "     field type: " + f.fieldType + ", value: " + value);
            /*If the "value" object is of the same type as the field
            *to be inserted to, then no data type conversion is required.
            */
//if (value!= null)
//System.out.println("value: " + value.getClass() + ", dataType: " + f.getDataType());
/*res            if (!f.getDataType().isInstance(value)) {
                recEntryForm.set(k, convertToFieldType(value, f));
            }else{
res*/                /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
                * attribute has to be synchronized against the field's "linkToExternalData" attribute.
                */
/*res                if (f.getDataType().equals(ImageTableField.DATA_TYPE)) {
                    if (f.containsLinksToExternalData())
                        ((CImageIcon) value).referenceToExternalFile = true;
                    else
                        ((CImageIcon) value).referenceToExternalFile = false;
                    recEntryForm.set(k, value);
                }
//addRecTime2 = addRecTime2 + System.currentTimeMillis()-st;
            }
        }//for
        addRecTime2 = addRecTime2 + System.currentTimeMillis()-st2;

        long st3 = System.currentTimeMillis();
        int newRecordIndex = recordCount; //((ArrayList) tableData.get(0)).size();
res*/
        /*All the values have been succesfully processed, i.e. cast to the
        *proper field data types. So insert them in the table's data array.
        */
/*res        int k =0;
//        System.out.println(recEntryForm.size() +", " + newRecordIndex);
        AbstractTableField fld = null;
        for (int i=0; i<recEntryForm.size(); i++) {
            fld = tableFields.get(i+k);
            while (fld.isCalculated()) k++;
            fld.add(recEntryForm.get(i));
//            ((ArrayList) tableData.get(i+k)).add(recEntryForm.get(i));
        }
        addRecTime3 = addRecTime3 + System.currentTimeMillis()-st3;

        long st4 = System.currentTimeMillis();
        if (numOfCalculatedFields > 0) {
res*/
            /* Add a new entry to the Arrays of the calculated fields. This entry will
            * be calculated properly by the following "evaluateCalculatedFieldCell".
            */
/*            for (int i=0; i<tableFields.size(); i++) {
if ((tableFields.get(i)).isCalculated())
((ArrayList) tableData.at(i)).add(null);
}
*/
/*rea            AbstractTableField calcField;
            for (int i=0; i<tableFields.size(); i++) {
                calcField = tableFields.get(i);
                if (calcField.isCalculated()) {
                    if (tableKey.isPartOfTableKey(calcField)) { //calcField.isKey()) {
res*/
/*??? Probable error                        tempKeyReset = true;
                        try{
                            if (!calcField.isFieldKeyAttributeChangeAllowed()) {
                                calcField.setFieldKeyAttributeChangeAllowed(true);
                                tempKeyChangeAllowedReset = true;
                            }
                            calcField.setKey(false);
                        }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table addRecord() : (1)");}
                        catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in Table addRecord() : (1.5)");}
*/
/*res                    }

                    try{
//                        System.out.println("FieldIndex: " + i);
                        evaluateAndAddCalculatedFieldCell(i, newRecordIndex);
                    }catch (DuplicateKeyException e) {System.out.println("Serious inconsistency error in Table addRecord(): (2)");}
                    catch (NullTableKeyException e) {System.out.println("Serious inconsistency error in Table addRecord(): (3)");}
res*/
/*???Probable error                    if (tempKeyReset) {
                        try{
                            calcField.setKey(true);
                            if (tempKeyChangeAllowedReset)
                                calcField.setFieldKeyAttributeChangeAllowed(false);
                        }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table addRecord() : (4)");}
                        catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in Table addRecord() : (5)");}
                        tempKeyReset = false;
                    }
*/
/*res                }
            }
res*/
        }
/*res
        //If the table is KEYed, check for key uniqueness
        if (hasKey()) {
            try{
                tableKey.checkRecordKey(newRecordIndex);
            }catch (Throwable thr) {
                for (int i=0; i<tableFields.size(); i++)
                    tableFields.get(i).remove(newRecordIndex);
//                    ((ArrayList) tableData.get(i)).remove(newRecordIndex);
                throw new DuplicateKeyException(thr.getMessage());
            }
        } //If (hasKey()
        addRecTime4 = addRecTime4 + System.currentTimeMillis()-st4;

        long st5 = System.currentTimeMillis();
        recordCount++;
        unselectedSubset.add(recordCount-1); //new Integer(recordCount));
//        System.out.println("unselected subset: " + unselectedSubset);
//2.0.1        Record r = new Record(this, recordCount);

        if (hasKey()) {
            tableKey.addRecordToPrimaryKeyHash(new Integer(tableFields.get(tableKey.primaryKeyFieldIndex).getObjectAt(newRecordIndex).hashCode()), newRecordIndex);
//            primaryKeyMap.add(primaryKeyValueHashCode, new Integer(recordCount)); //2.0.1  r);
        }

//2.0.1        tableRecords.add(r);
        recordSelection.add(false); //Boolean.FALSE);
        // Update the recordIndex
        recordIndex.add(recordCount-1);
res*/
//addRecTime5 = addRecTime5 + System.currentTimeMillis() - st1;
/*        int[] tmp = new int[getRecordCount()];
for (int i =0; i<tmp.length-1; i++)
tmp[i] = recordIndex[i];
tmp[tmp.length-1] = getRecordCount()-1;
recordIndex = tmp;
*/

        res.commitNewRecord(moreToBeAdded);
        recEntryForm.clear();
/*res        setModified();
//t        if (this.database != null)
//long st3 = System.currentTimeMillis();
        fireRecordAdded(moreToBeAdded); //this.database.Tables.indexOf(this), recordCount));
//addRecTime7 = addRecTime7 + System.currentTimeMillis()-st3;
        addRecTime5 = addRecTime5 + System.currentTimeMillis()-st5;
        long st6 = System.currentTimeMillis();
        addRecTime6 = addRecTime6 + System.currentTimeMillis()-st6;
res*/
        return true;
    }

    /**
     * Removes all the records of the table.
     *  @exception TableNotExpandableException If the table is not expandable.
     */
    public void removeAllRecords() throws TableNotExpandableException {
        if (recordCount == 0) return;

        // Remove all the records but the first with the 'isChanging' flag of the removeRecord() method set to true.
        for (int i=recordCount-1; i>0; i--) {
            ArrayList removedRecord = new ArrayList();
            for (int k=0; k<tableFields.size(); k++)
                removedRecord.add(tableFields.get(k).getObjectAt(i));
            try{
                removeRecord(i, i, true, true, removedRecord);
            }catch (InvalidRecordIndexException exc) {
                System.out.println("Inconsistency error in Table.removeAllRecords(). Operation halted...");
                exc.printStackTrace();
                return;
            }
        }
        // Remove the first record of the table with the 'isChanging' flag of the removeRecord() method set to false.
        ArrayList removedRecord = new ArrayList();
        for (int k=0; k<tableFields.size(); k++)
            removedRecord.add(tableFields.get(k).getObjectAt(0));
        try{
            removeRecord(0, 0, false, true, removedRecord);
        }catch (InvalidRecordIndexException exc) {
            System.out.println("Inconsistency error in Table.removeAllRecords() 2. Operation halted...");
            exc.printStackTrace();
            return;
        }
    }

    /**
     *  Removes the record at <code>recIndex</code> from the Table.
     *  @param recIndex The index of the record to be deleted.
     *  @param rowIndex The index of the record to be deleted in the GUI component. This index
     *  is not used by <code>removeRecord</code>. It is simply passed as an argument to the generated
     *  <code>RecordRemovedEvent</code>. <code>rowIndex</code> defaults to -1 for calls to <code>removeRecord</code>,
     *  which are made internally by other methods of the Table API.
     *  @param isChanging This parameter is also of no importance to <code>removeRecord</code>. It is
     *  passed to the generated event and is used by the listeners in order to decide when
     *  the GUI component that displays the Table is to be repainted, as a result of the record
     *  removal. When multiple records are removed one after another, the GUI component can
     *  watch this flag in order to get redrawn once, after the last record removal.
     *  @exception TableNotExpandableException If the table is not expandable.
     *  @exception InvalidRecordIndexException If <code>recIndex<0</code> or <code>recIndex>#Records</code>.
     */
    public void removeRecord(int recIndex, int rowIndex, boolean isChanging) throws TableNotExpandableException,
            InvalidRecordIndexException {
        removeRecord(recIndex, rowIndex, isChanging, true, null);
    }

    /**
     * This method exists becuase of TableViews. When a record is removed from a TableView
     * the data of the record in the <code>tableData</code> structure must not be changed.
     * The data do not belong to the TableView but to the underlying Table.
     * @param recIndex The index of the record in the Table.
     * @param rowIndex The index of the record where the Table is viewed.
     * @param isChanging If more records are to be removed.
     * @param removeData ??
     * @param removedRecord The data of the record to be removed. This info is send to the listeners of the Table through
     *        the RecordRemovedEvent. If this parameter is null, the data will be collected by the method, before the
     *        record is removed. However when removeRecord() is called on a TableView, the data -in some cases- have already
     *        been removed from the underlying Table and this method cannot gother them. In this case, the 'removedRecord'
     *        ArrayList is supplied filled to this method.
     */
    void removeRecord(int recIndex, int rowIndex, boolean isChanging, boolean removeData, ArrayList removedRecord) throws TableNotExpandableException,
            InvalidRecordIndexException {
        if (recIndex < recordCount && recIndex >= 0) {
            if (recordRemovalAllowed) {
                /* Removing the record's possible key from "primaryKeyMap". If the record to be removed is
                * the "pendingNewRecord", which is being interactively added to the table, then the possible
                * record's key may not have yet been filled by the user. In this case we ckeck if this key
                * is null, before removing it from "primaryKeyMap".
                */
                if (hasKey()) {
//                    Object keyValue = riskyGetCell(keyFieldIndices.get(0), recIndex);
                    Object keyValue = riskyGetCell(tableKey.primaryKeyFieldIndex, recIndex);
                    if (!(getRecordEntryStructure().isRecordAdditionPending() && keyValue == null)) {
//1                    if (!(pendingNewRecord && keyValue == null)) {
//1                        Integer recPrimaryKey = new Integer(keyValue.hashCode());
                        tableKey.removeRecordFromPrimaryKeyHash(recIndex);
/*                        if ((primaryKeyMap.count(recPrimaryKey)) > 1) {
//                            OrderedMapIterator e = primaryKeyMap.find(recPrimaryKey);
                            HashMapIterator e = primaryKeyMap.find(recPrimaryKey);
                            while (!e.atEnd()) {
//2.0.1                                 if ( ( (Record) ((Pair) e.get()).second).getIndex() == recIndex) {
                                if ( ( (Integer) ((Pair) e.get()).second).intValue() == recIndex) {
                                    primaryKeyMap.remove(e);
                                    break;
                                }
                                e.advance();
                            }
                        }else
                            primaryKeyMap.remove(recPrimaryKey);
*/
                    }

                }

                // Before removing the record, its data is stored in the 'removedRecord' array, so that
                // the RecordRemovedEvent can carry it to the listeners. In the case of TableViews, the
                // record has already been removed from the underlying Table, so there is no way to find
                // the data of the removed record. In this case the 'removeRecord()' method is passed the
                // 'removedData' ArrayList filled from the method 'removeRecordFromView()' of the TableView.
                if (removedRecord == null) {
                    removedRecord = new ArrayList();
    //                for (int i=0; i<tableData.size(); i++) {
                    for (int i=0; i<tableFields.size(); i++) {
    //                    removedRecord.add(((ArrayList) tableData.get(i)).get(recIndex));
                        removedRecord.add(tableFields.get(i).getObjectAt(recIndex));
                    }
                }
                if (removeData) {
                    for (int i=0; i<tableFields.size(); i++)
                        tableFields.get(i).remove(recIndex);
//                        ((ArrayList) tableData.get(i)).remove(recIndex);
                }

                //Set the index of the record to be removed to an invalid
                //value(-1), so as it cannot be used with the Table API anymore
//2.0.1                ((Record) tableRecords.at(recIndex)).invalidateIndex();
                //Remove the column from the tableRecords Array
//2.0.1                tableRecords.remove(recIndex);
                boolean recordSelected = isRecordSelected(recIndex);
                if (recordSelected)
                    selectedSubset.removeElements(recIndex); //(new Integer(recIndex));

                int selRecIndex;
                for (int i=0; i<selectedSubset.size(); i++) {
                    selRecIndex = selectedSubset.get(i); //((Integer) selectedSubset.at(i)).intValue();
                    if (selRecIndex > recIndex)
                        selectedSubset.set(i, selRecIndex-1); //put(i, new Integer(selRecIndex-1));
                }
                recordSelection.remove(recIndex);

//                System.out.println("Removing recordCount: " + recordCount);
                if (!recordSelected)
                    unselectedSubset.removeElements(recIndex);
//                unselectedSubset.removeElements(recordCount); //remove(new Integer(recordCount));
//                System.out.println("Removed recordCount");
                int unselRecIndex;
                for (int i=0; i<unselectedSubset.size(); i++) {
                    unselRecIndex = unselectedSubset.get(i); //((Integer) unselectedSubset.at(i)).intValue();
                    if (unselRecIndex > recIndex) //recordCount)
                        unselectedSubset.set(i, unselRecIndex-1); //put(i, new Integer(unselRecIndex-1));
                }
//                System.out.println("selected subset: " + selectedSubset);
//                System.out.println("unselected subset: " + unselectedSubset);

                recordCount--;
                //Decrement by 1 the index of the following records in the
                //tableRecords Array
//2.0.1                for (int i=recIndex; i<recordCount; i++)
//2.0.1                    ((Record) tableRecords.at(i)).decrementRecordIndex(1);

                if (!isChanging && activeRecord != -1) {
                    if (activeRecord > (recordCount-1)) {
                        setActiveRecord(recordCount-1);
                    }else{
                        if (activeRecord + 1 < recordCount) {
                            setActiveRecord(activeRecord+1);
                        }else{
                            setActiveRecord(activeRecord);
                        }
                    }
                }

                // Find the rowIndex of the supplied record index, if it's not given
//                System.out.println("rowIndex: " + rowIndex);
                if (rowIndex == -1) {
                    rowIndex = recordIndex.indexOf(recIndex);
/*                    for (int i=0; i<recordIndex.length;i++) {
if (recordIndex[i] == recIndex) {
rowIndex = i;
break;
}
}
*/
                }

                // Update the recordIndex
                recordIndex.remove(rowIndex);
                for (int i=0; i<recordIndex.size(); i++) {
                    int value = recordIndex.get(i);
                    if (value > recIndex)
                        recordIndex.set(i, value-1);
                }
/*                int tmp[] = new int[getRecordCount()];
for (int i=0; i<rowIndex; i++) {
if (recordIndex[i] > recordIndex[rowIndex])
tmp[i] = recordIndex[i]-1;
else
tmp[i] = recordIndex[i];
}
for (int i=rowIndex; i<tmp.length; i++) {
if (recordIndex[i+1] > recordIndex[rowIndex])
tmp[i] = recordIndex[i+1]-1;
else
tmp[i] = recordIndex[i+1];
}
recordIndex = tmp;
*/

//t                if (this.database != null)
//System.out.println("removeRecord() recordCount: " + recordCount + ", field size: " + tableFields.get(0).size());
//System.out.println("removed record: " + removedRecord);
//System.out.println("fireRecordRemoved() recIndex: " + recIndex + ", isChanging: " + isChanging);
                fireRecordRemoved(recIndex, rowIndex, removedRecord, isChanging); //this.database.CTables.indexOf(this), recIndex, rowIndex, removedRecord, isChanging));
                setModified();
                return;
            }else{
                throw new TableNotExpandableException(bundle.getString("CTableMsg47"));
            }
        }else{
            throw new InvalidRecordIndexException(bundle.getString("CTableMsg48") + recIndex);
        }
    }

    /** Sorts a set of records of the Table in ascending or descending order based on the contents of
     *  one of the Table's fields.
     *  @param fieldName The name of the field
     *  @param ascending <code>true</code> for ascending sorting. <code>false</code> for descending sorting
     *  @param fromRowIndex The index of the first record in the set of the records to be sorted.
     *  @param toRowIndex The index of the last record.
     *  @exception InvalidFieldException  If the specified field is of <code>image</code> data type.
     */
    public void sortOnField(String fieldName, boolean ascending, int fromRowIndex, int toRowIndex)
        throws InvalidFieldException {
        sortOnField(fieldName, ascending, fromRowIndex, toRowIndex, true);
    }

    void sortOnField(String fieldName, boolean ascending, int fromRowIndex, int toRowIndex, boolean fireEvent)
            throws InvalidFieldException {
//System.out.println("sortOnField() fromRecordIndex: " + fromRecordIndex + ", toRecordIndex: " + toRecordIndex);
        if (fromRowIndex >= toRowIndex)
            return;

        AbstractTableField f1;
        try{
            f1 = getTableField(fieldName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in Table sortOnField() : (1)"); return;}

        ObjectComparator comparator = null;
        if (ascending)
            comparator = f1.getComparatorFor("<");
        else
            comparator = f1.getComparatorFor(">");


        ObjectBaseArray data = new ObjectBaseArray(toRowIndex - fromRowIndex + 1);
        for (int i=fromRowIndex; i<=toRowIndex; i++)
            data.add(f1.getObjectAt(recordForRow(i)));
        IntBaseArray newOrder = f1.getOrder(data, 0, data.size()-1, comparator);
//        IntBaseArray newOrder = f1.getOrder(fromRowIndex, toRowIndex, comparator);
//System.out.println("newOrder: " + newOrder.size());
/*System.out.print("newOrder: ");
for (int i=0; i<newOrder.size(); i++)
    System.out.print(newOrder.get(i) + ", ");
System.out.println();
*/
        // Set the new order of the records.
        int[] tmp = new int[toRowIndex-fromRowIndex+1];
        for (int i=0; i<tmp.length; i++)
            tmp[i] = recordIndex.get(fromRowIndex+i);
        for (int i=0; i<newOrder.size(); i++) {
            recordIndex.set(fromRowIndex+i, tmp[newOrder.get(i)]);
        }
//        for (int k=fromRowIndex; k<=toRowIndex; k++) {
//System.out.println("k: " + k + ", k-fromRowIndex: " + (k-fromRecordIndex));
//            recordIndex.set(k, newOrder.get(k-fromRowIndex)); // intArray.add(new Integer(recordIndex[k]));
//        }
//            recordIndex.set(k, ((Integer) intArray.get(k-fromRowIndex)).intValue()); // intArray.add(new Integer(recordIndex[k]));

        // Clear the 'sorted' flag of all the fields of the Table.
        clearFieldSortFlags();
        // If the whole table has been sorted on this field, set the
        // 'sorted' flag for this field.
//System.out.println("fromRowIndex: " + fromRowIndex + ", toRowIndex: " + toRowIndex + ", recordCount: " + recordCount);
        if (fromRowIndex == 0 && toRowIndex == (recordCount-1)) {
            f1.sorted = true;
            f1.sortDirection = (ascending)? AbstractTableField.ASCENDING:AbstractTableField.DESCENDING;
        }

//t        if (this.database != null)
        if (fireEvent)
            rowOrderChanged(); //, database.CTables.indexOf(this)));
//        System.out.println("sortOnField. updateActiveRow: " + updateActiveRow + ", activeRow: " + activeRow);
    }

    /** Sorts all the records of the Table on the fields contained in <code>fields</code> array. The severity of each field
     * depends on its position in the array, i.e. the Table will be sorted on the first field in the array, then on the
     * second field an so on.
     * @param fields The fields to sort the Table on.
     * @param order The sorting order for each field in the <code>fields<code> array. <code>true</code> refers to ascending order,
     *        while <code>false</code> refers to descenging order.
     * @throws InvalidFieldException If any of the supplied fields does not exist in the Table.
     */
    public void sortOnFields(TableFieldBaseArray fields, BoolBaseArray order) throws InvalidFieldException {
        if (fields == null || fields.size() == 0) return;
        if (order == null || order.size() != fields.size()) return;

        AbstractTableField f1 = fields.get(0);
        // First sort the Table on the first field
        sortOnField(f1.getName(), order.get(0), 0, recordCount-1, false);
        // Now sort the table on the rest of the fields, maintaining the results of the sorting on the first record.
//        for (int i=1; i<fields.size(); i++) {
//            sortOnField(fields.get(i-1), fields.get(i), 0, order.get(i));
//        }
        sortOnField(fields, 0, 1, 0, recordCount-1, order);

        // Clear the 'sorted' flag of all the fields of the Table.
        clearFieldSortFlags();
        // Set the 'sorted' flag for the first field in the array.
        f1.sorted = true;
        f1.sortDirection = (order.get(0))? AbstractTableField.ASCENDING:AbstractTableField.DESCENDING;

        // Fire the event that row order changed.
        rowOrderChanged();
    }

    /** Sorts the Table on field <code>f</code>. The sorting affects all the table's rows which are after
     *  <code>fromRowIndex</code> (including this) that have the same value as <code>fromRowIndex</code> for
     *  the field <code>pivotField</code>.
     */
    private void sortOnField(TableFieldBaseArray fields, int pivotFieldIndex, int fieldIndex, int fromRowIndex, int toRowIndex, BoolBaseArray order)
            throws InvalidFieldException {
        AbstractTableField pivotField = fields.get(pivotFieldIndex);
        AbstractTableField f = fields.get(fieldIndex);
        if (pivotField.table != this) throw new InvalidFieldException("Field " + pivotField.getName() + " does not belong to table " + title);
//System.out.println("sortOnField() pivotField: " + pivotField.getName() + ", f: " + f.getName() + ", fromRowIndex: " + fromRowIndex + ", toRowIndex: " + toRowIndex); //f value: " + f.getObjectAt(recordForRow(fromRowIndex)));

        // The algorithm is the following. Each time this sortOnField() is called the Table is considered sorted on
        // pivotField, between rows 'fromRowIndex' and 'toRowIndex'. We search all the rows between 'fromRowIndex'
        // and 'toRowIndex' for groups of rows with the same value at 'pivotField'. We sort each such group of more
        // than 1 rows, on the field f. Recursively we do the same for the same for the rest of the fields in the
        // 'fields' array, consindering field 'f' the new 'pivotField'.
        while (fromRowIndex <= toRowIndex) {
            // Find all the view-wise adjacent records with the same value as 'pivotField' at 'fromRecIndex'
            Object value = pivotField.getObjectAt(recordForRow(fromRowIndex));
            ObjectComparator comp = pivotField.getComparatorFor("=");
            int currToRowIndex = fromRowIndex + 1;
            while (currToRowIndex <= toRowIndex  && comp.execute(value, pivotField.getObjectAt(recordForRow(currToRowIndex))))
                currToRowIndex++;
            currToRowIndex--;
    //System.out.println("currToRowIndex: " + currToRowIndex + ", value: " + value);

            // If there exist adjacent records with the same value for field 'pivotField'...
            if (currToRowIndex != fromRowIndex) {
                    // Sort the rows between 'fromRowIndex' and 'currToRowIndex' on field 'f'.
                    // Make a copy of the data of the field 'f' in the records which shared the same value for field 'pivotField'.
                    ObjectBaseArray data = new ObjectBaseArray(currToRowIndex - fromRowIndex + 1);
                    for (int i=fromRowIndex; i<=currToRowIndex; i++)
                        data.add(f.getObjectAt(recordForRow(i)));

                    // Find the proper comparator based on the sorting order.
                    ObjectComparator comparator = null;
                    if (order.get(pivotFieldIndex+1))
                        comparator = f.getComparatorFor("<");
                    else
                        comparator = f.getComparatorFor(">");
                    // Get the proper order the records should have so that field f will be sorted on the selected records.
                    IntBaseArray newRowOrder = f.getOrder(data, 0, data.size()-1, comparator);

                    // Set the new order of the records.
                    int[] tmp = new int[currToRowIndex-fromRowIndex+1];
                    for (int i=0; i<tmp.length; i++)
                        tmp[i] = recordIndex.get(fromRowIndex+i);
                    for (int i=0; i<newRowOrder.size(); i++) {
                        recordIndex.set(fromRowIndex+i, tmp[newRowOrder.get(i)]);
                    }
                for (int k=pivotFieldIndex+1; k<fields.size()-1; k++) {
                    sortOnField(fields, k, k+1, fromRowIndex, currToRowIndex, order);
    //                return;
                }
            }
            currToRowIndex++;
            fromRowIndex = currToRowIndex;
        }
    }

    /** Promotes the selected records to the beginning of the Table. The actual record index
     *  does not change. What changes is the record indexing, as it is stored in the recordIndex array.
     */
    public void promoteSelectedRecords() {
//        IntBaseArray selectedRecords = getSelectedSubset();
        IntBaseArrayDesc selectedRecords = getSelectedSubset();
        if (selectedRecords.size() == 0)
            return;

        for (int i=0; i<selectedRecords.size(); i++)
            selectedRecords.set(i, rowForRecord(selectedRecords.get(i))); //put(i, new Integer(rowForRecord(((Integer) selectedRecords.at(i)).intValue())));
//        System.out.println("BEFORE selectedRecords: " + selectedRecords);
        selectedRecords.sort(true);
//        Sorting.sort(selectedRecords, new LessNumber());
//        System.out.println("AFTER selectedRecords: " + selectedRecords);

        int[] recIndices = new int[selectedRecords.size()];
        for (int i=0; i<selectedRecords.size(); i++)
            recIndices[i] = selectedRecords.get(i); //((Integer) selectedRecords.at(i)).intValue();


/*        for (int i=0; i<recIndices.length; i++)
System.out.print(recIndices[i] + ", ");
System.out.println();
for (int i=0; i<recordIndex.length; i++)
System.out.print(recordIndex[i] + ", ");
System.out.println();
*/
        int destinationIndex = 0;
        int tmp, tmp2;
        try{
            for (int i=0; i<recIndices.length; i++) {
                if (recIndices[i] == destinationIndex) {
                    destinationIndex++;
                    continue;
                }else{
//                System.out.println("recIndices[i]: " + recIndices[i]);
                    tmp = recordIndex.get(destinationIndex);
                    recordIndex.set(destinationIndex, recordIndex.get(recIndices[i]));

//                System.out.println("Setting recordIndex["+destinationIndex+"] to : " + recIndices[i]);
                    int k = destinationIndex + 1;
                    while (k<=recIndices[i]) {
                        tmp2 = recordIndex.get(k);
                        recordIndex.set(k, tmp);
//                    System.out.println("Setting recordIndex["+k+"] to : " + tmp);
                        if (k+1 > recIndices[i])
                            break;
                        tmp = recordIndex.get(k+1);
                        recordIndex.set(k+1, tmp2);
//                    System.out.println("Setting recordIndex["+(k+1)+"] to : " + tmp2);
                        k = k + 2;
                    }
                    destinationIndex++;
                }
            }

            // Clear the 'sorted' flag of all the fields of the Table. Since the row
            // order changed, any previous sorting has been cancelled.
            clearFieldSortFlags();
            rowOrderChanged(); //, database.CTables.indexOf(this)));

        }catch (Exception e) {System.out.println(e.getClass().getName() + ", " + e.getMessage());}
    }

    /** Resets the <code>sorted</code> flag of every field in the Table.
     */
    void clearFieldSortFlags() {
//System.out.println("clearFieldSortFlags()");
        for (int i=0; i<tableFields.size(); i++) {
            AbstractTableField f = tableFields.get(i);
            f.sorted = false;
        }
    }


    /* Returns the index of the row at which the Table record at <code>recordIndex</code>
    * is displayed.
    */
    public int rowForRecord(int recordIndex) {
        /* The 'recordIndex' array is created when the first record is added in the Table. Therefore
        * 'recordIndex' is null for a new Table without any records inside it.
        */
        if (this.recordIndex == null)
            return -1;
//        System.out.println("Table: " + title + ", recordCount: " + recordCount + ", recordIndex: " + recordIndex);
//        IntArray recordIndexArray = new IntArray(this.recordIndex);
//        System.out.println("record: " + recordIndex + ", row: " + recordIndexArray.indexOf(new Integer(recordIndex)));
//        return recordIndexArray.indexOf(new Integer(recordIndex));
        return this.recordIndex.indexOf(recordIndex);
/*        int length = this.recordIndex.size();
for (int i=0; i<length; i++) {
if (this.recordIndex.get(i) == recordIndex)
return i;
}
return -1;
*/
    }

    /* Returns the index in the Table of the record which is displayed at row <code>row</code>.
    */
    public int recordForRow(int row) {
        if (row < 0 || row >= recordIndex.size())
            return -1;
        return recordIndex.get(row);
    }

    /** Switches the record indices of the two rows. The record index of the <code>firstRow</code> will become
     *  the record index of the <code>secondRow</code> and vice versa.
     * @param row The first row.
     * @param newPos The second row.
     */
    public void moveRow(int row, int newPos) {
        if (row < 0 || row >= recordIndex.size()) throw new ArrayIndexOutOfBoundsException("Invalid row " + row);
        if (newPos < 0 || newPos >= recordIndex.size()) throw new ArrayIndexOutOfBoundsException("Invalid new row position" + newPos);
        if (newPos == row) return;
//printRecordIndex();
        int recordAtRow = recordIndex.get(row);
        if (newPos > row) {
            for (int i=row; i<newPos; i++) {
                recordIndex.set(i, recordIndex.get(i+1));
            }
            recordIndex.set(newPos, recordAtRow);
        }else{
            for (int i=row; i>newPos; i--)
                recordIndex.set(i, recordIndex.get(i-1));
            recordIndex.set(newPos, recordAtRow);
        }
        clearFieldSortFlags();
        rowOrderChanged();
//printRecordIndex();
    }

    /** Prints the contents of the <code>recordIndex</code> IntBaseArray.
     */
    private void printRecordIndex() {
        System.out.print("RecordIndex: ");
        for (int i=0; i<recordIndex.size(); i++)
            System.out.print(recordIndex.get(i) + ", ");
        System.out.println("");
    }

    /** This method bypasses any integrity controls and inserts an empty record to the end of the table.
     *  It is used to initiate the interactive insertion of a new record, whch initially is empty
     *  and the user fills its fields one by one. This method should only be used for this purpose.
     *  Inproper use of this method can lead to serious table integrity errors.
     *  <code>addEmptyRecord()</code> returns true, if any other operation should block until the
     *  insertion of the new record finishes. This should happen if the table contains keys and
     *  should last until the user has filled all the key values. If the table is not keyed, this
     *  operation returns false.
     *  @return <code>true</code>, if the table has key, which means that the <code>pendingNewRecord</code> flag is
     *          set. <code>false</code>, if the table does not have a key.
     *  @exception UnableToAddNewRecordException If any of the fields that belong to the table's key is
     *             not editable.
     *  @exception DuplicateKeyException If the insertion of the empty record, violates the key uniqueness
     *             in a table with key. This happens, if the table contains a calculated field, which is
     *             also key, and this field's formula evaluates to a constant value i.e. the formula does
     *             not reference any other field in the table.
     *  @exception NullTableKeyException If the insertion of the empty record, causes a calculated, key field
     *             which does not depend on any other field of the table, to receive the <code>null</code> value.
     */
/*1    public boolean addEmptyRecord() throws UnableToAddNewRecordException, DuplicateKeyException,
            NullTableKeyException, AttributeLockedException {
//        System.out.println("IN ADDEMPTYRECORD-----");
        if (!recordAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg37"));

        if (hasKey() && !dataChangeAllowed)
            throw new UnableToAddNewRecordException(bundle.getString("CTableMsg93"));
1*/
        /* If the table has a key and the insertion of the previous record has not
        * finished, then reject the addition of a new empty record.
        */
/*1        if (hasKey() && pendingNewRecord)
            throw new UnableToAddNewRecordException(bundle.getString("CTableMsg99"));

        AbstractTableField f;
        for (int i=0; i<tableFields.size(); i++) {
            f = tableFields.get(i);
            if (tableKey.isPartOfTableKey(f) && !f.isEditable() && !f.isCalculated())
                throw new UnableToAddNewRecordException(bundle.getString("CTableMsg49") + f.getName() + "\"");
1*/            /* If a record is inserted in a table, whose hidden fields are not displayed and at least
            * one of its key fields are hidden, then throw an exception.
            */
/*1            if (tableKey.isPartOfTableKey(f) && f.isHidden() && !hiddenFieldsDisplayed)
                throw new UnableToAddNewRecordException(bundle.getString("CTableMsg92"));

        }


        for (int i=0; i<tableFields.size(); i++) {
            f = tableFields.get(i);
            tableFields.get(i).add(null);
//            ((ArrayList) tableData.get(i)).add(null);
//            System.out.println(f.getName() + ", " + f.isCalculated() + ", " + f.getDependsOnFields() + ", " + f.oe + ", calculatedValue: " + f.calculatedValue + ", " + f);
            if (f.isCalculated() && f.getDependsOnFields().size() == 0) {
                try{
                    evaluateCalculatedFieldCell(i, tableFields.get(i).size()-1);
//                    evaluateCalculatedFieldCell(i, ((ArrayList) tableData.get(i)).size()-1);
                }catch (DuplicateKeyException e) {
1*/                    /* Remove any null values that have already been inserted.
                    */
/*1                    for (int k=0; k<=i; k++)
                        tableFields.get(k).remove(recordCount+1);
//                        ((ArrayList) tableData.get(k)).remove(recordCount+1);
                    throw new DuplicateKeyException(e.message);
                }
                catch (NullTableKeyException e) {
1*/                    /* Remove any null values that have already been inserted.
                    */
/*1                    for (int k=0; k<=i; k++)
                        tableFields.get(k).remove(recordCount+1);
//                        ((ArrayList) tableData.get(k)).remove(recordCount+1);
                    throw new NullTableKeyException(e.message);
                }
            }
        }

        unselectedSubset.add(recordCount); //new Integer(recordCount));
//        System.out.println("unselected subset: " + unselectedSubset);
//2.0.1        Record r = new Record(this, recordCount);

//2.0.1        tableRecords.add(r);
        recordSelection.add(false); //Boolean.FALSE);

        //Update the recordIndex
        recordIndex.add(recordCount);
        recordCount++;
1*/
/*        int[] tmp = new int[getRecordCount()];
for (int i =0; i<tmp.length-1; i++)
tmp[i] = recordIndex[i];
tmp[tmp.length-1] = getRecordCount()-1;
recordIndex = tmp;
*/
/*1        setModified();

//t        if (this.database != null)
        fireEmptyRecordAdded(); //this.database.Tables.indexOf(this), recordCount));
        if (!hasKey())
            return false;
        else{
            pendingNewRecord = true;
            return true;
        }
    }
1*/

    /** Resets the <code>pendingNewRecord</code> flag. This flag is set when the interactive entry of a new
     *  record starts in a table which has key.
     */
/*1    public void resetPendingNewRecord() {
//        System.out.println("RESET PENDING NEW RECORD");
        pendingNewRecord = false;
    }
1*/

    /** This method accepts two IntBaseArrayDescs. It removes all the elements of
     *  the second array from the first array, if they exist there.
     */
    public static void clearArray(IntBaseArrayDesc array, IntBaseArrayDesc elemArray) {
        //array.removeElements(elemArray.get(i));
        array.sort(true);
        elemArray.sort(true);
        int k = 0;
        int elemArraySize = elemArray.size();
        for (int i=0; i<array.size() && k<elemArraySize; i++) {
            if (array.get(i) < elemArray.get(k))
                continue;
            if (array.get(i) == elemArray.get(k)) {
                array.remove(i);
                k++;
                i--;
                continue;
            }
            break;
        }
    }

    /** Resets the current set of selected records to the supplied set.
     */
    public void setSelectedSubset(IntBaseArray recIndices) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        /* Clear the selected subset.
        */
        for (int i=0; i<selectedSubset.size(); i++) {
            unselectedSubset.add(selectedSubset.get(i));
            recordSelection.set(selectedSubset.get(i), false); //((Integer) selectedSubset.at(i)).intValue(), false); //Boolean.FALSE);
        }
        selectedSubset.clear();

        /* Select the supplied records
        */
        int i; //Integer i;
        for (int k=0; k<recIndices.size(); k++) {
            try{
                i = recIndices.get(k); //i = (Integer) recIndices.at(k);
                if (i < 0 || i > recordCount)
                    continue;
            }catch (Exception e) {
                continue;
            }
            recordSelection.set(i, true); //Boolean.TRUE);
            selectedSubset.add(i); //add(i);
            unselectedSubset.removeElements(i); //remove(i);
        }
        setModified();
//t        if (this.database != null)
        fireSelectedRecordSetChanged(
                SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                null,
                prevSelectedSet,
                null,
                null); //this.database.CTables.indexOf(this), null));
    }


    /** Resets the current set of selected records to the supplied set.
     */
    public void setSelectedSubset(int[] recIndices) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        /* Clear the selected subset.
        */
        for (int i=0; i<selectedSubset.size(); i++) {
            unselectedSubset.add(selectedSubset.get(i));
            recordSelection.set(selectedSubset.get(i), false); //set(((Integer) selectedSubset.get(i)).intValue(), false); //Boolean.FALSE);
        }
        selectedSubset.clear();

        /* Select the supplied records
        */
        Integer i;
        for (int k=0; k<recIndices.length; k++) {
            if (recIndices[k] < 0 || recIndices[k] > recordCount)
                continue;
            i = new Integer(recIndices[k]);
            recordSelection.set(i.intValue(), true); //Boolean.TRUE);
            selectedSubset.add(i.intValue()); //add(i);
            unselectedSubset.removeElements(i.intValue()); //remove(i);
        }
        setModified();
        fireSelectedRecordSetChanged(
                SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                null,
                prevSelectedSet,
                null,
                null);
    }


    /** Resets the current set of selected records to the supplied set.
     */
    public void setSelectedSubset(int recIndex) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        if (recIndex < 0 || recIndex > recordCount)
            return;

        /* Clear the selected subset.
        */
        for (int i=0; i<selectedSubset.size(); i++) {
            unselectedSubset.add(selectedSubset.get(i));
            recordSelection.set(selectedSubset.get(i), false); //((Integer) selectedSubset.at(i)).intValue(), false); //Boolean.FALSE);
        }
        selectedSubset.clear();

        /* Select the supplied record
        */
        recordSelection.set(recIndex, true); //Boolean.TRUE);
        selectedSubset.add(recIndex); //(i);
        unselectedSubset.removeElements(recIndex); //remove(i);

        setModified();
//        System.out.println("Firing event for recindex: " + recIndex);
//        System.out.println(this.database);
//        System.out.println(this.database.CTables.indexOf(this));
        fireSelectedRecordSetChanged(
                SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                null,
                prevSelectedSet,
                null,
                null);
    }


    public void addToSelectedSubset(Array recIndices) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray recordsAddedToSelection = new IntBaseArray(recIndices.size());
        Integer i;
        int recIndex = -1;
        boolean selectionChanged = false;
        for (int k=0; k<recIndices.size(); k++) {
            try{
                i = (Integer) recIndices.at(k);
            }catch (Exception e) {
                continue;
            }
            recIndex = i.intValue();
            if (selectedSubset.indexOf(recIndex) == -1) {
                recordSelection.set(recIndex, true); //Boolean.TRUE);
                selectedSubset.add(recIndex); //add(i);
                recordsAddedToSelection.add(recIndex);
                unselectedSubset.removeElements(recIndex); //remove(i);
                if (!selectionChanged)
                    selectionChanged = true;
            }
        }
        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(
                    SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                    null,
                    prevSelectedSet,
                    recordsAddedToSelection,
                    null);
        }
    }


    public void addToSelectedSubset(int[] recIndices) {
        Integer i;
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray recordsAddedToSelection = new IntBaseArray(recIndices.length);
        int recIndex = -1;
        boolean selectionChanged = false;
        for (int k=0; k<recIndices.length; k++) {
            i = new Integer(recIndices[k]);
            recIndex = i.intValue();
            if (selectedSubset.indexOf(recIndex) == -1) {
                recordSelection.set(recIndex, true); //Boolean.TRUE);
                selectedSubset.add(recIndex); //(i);
                recordsAddedToSelection.add(recIndex);
                unselectedSubset.removeElements(recIndex); //remove(i);
                if (!selectionChanged)
                    selectionChanged = true;
            }
        }
        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(
                    SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                    null,
                    prevSelectedSet,
                    recordsAddedToSelection,
                    null);
        }
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    public void addToSelectedSubset(int recIndex) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();

        if (selectedSubset.indexOf(recIndex) == -1) {
            recordSelection.set(recIndex, true); //Boolean.TRUE);
            selectedSubset.add(recIndex); //add(i);
//            System.out.println("unselectedSubset: " + unselectedSubset);
            unselectedSubset.removeElements(recIndex); //remove(i);
            IntBaseArray recordsAddedToSelection = new IntBaseArray(1);
            recordsAddedToSelection.add(recIndex);
            setModified();
            fireSelectedRecordSetChanged(
                    SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                    null,
                    prevSelectedSet,
                    recordsAddedToSelection,
                    null);
        }
//        System.out.println("Firing event for recindex: " + recIndex);
//        System.out.println(this.database);
//        System.out.println(this.database.CTables.indexOf(this));
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    public void removeFromSelectedSubset(ArrayList recIndices) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray recordsRemovedFromSelection = new IntBaseArray(recIndices.size());
        Integer i;
        int recIndex = -1;
        boolean selectionChanged = false;
        for (int k=0; k<recIndices.size(); k++) {
            try{
                i = (Integer) recIndices.get(k);
            }catch (Exception e) {
                continue;
            }
            recIndex = i.intValue();
            if (selectedSubset.indexOf(recIndex) != -1) {
                recordSelection.set(recIndex, false); //Boolean.FALSE);
                selectedSubset.removeElements(recIndex); //remove(i);
                unselectedSubset.add(recIndex); //add(i);
                recordsRemovedFromSelection.add(recIndex);
                if (!selectionChanged)
                    selectionChanged = true;
            }
        }

        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(
                    SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                    null,
                    prevSelectedSet,
                    null,
                    recordsRemovedFromSelection);
        }
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    public void removeFromSelectedSubset(int[] recIndices) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray recordsRemovedFromSelection = new IntBaseArray(recIndices.length);
        boolean selectionChanged = false;
        for (int k=0; k<recIndices.length; k++) {
            if (selectedSubset.indexOf(recIndices[k]) != -1) {
                recordSelection.set(recIndices[k], false);
                selectedSubset.removeElements(recIndices[k]);
                unselectedSubset.add(recIndices[k]);
                recordsRemovedFromSelection.add(recIndices[k]);
                if (!selectionChanged)
                    selectionChanged = true;
            }
        }
//t        if (this.database != null)
        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(
                    SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                    null,
                    prevSelectedSet,
                    null,
                    recordsRemovedFromSelection);
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        }
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    public void removeFromSelectedSubset(int recIndex) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        if (selectedSubset.indexOf(recIndex) != -1) {
            recordSelection.set(recIndex, false); //Boolean.FALSE);
            selectedSubset.removeElements(recIndex); //remove(i);
            unselectedSubset.add(recIndex); //add(i);
            IntBaseArray recordsRemovedFromSelection = new IntBaseArray(1);
            recordsRemovedFromSelection.add(recIndex);

            setModified();
            fireSelectedRecordSetChanged(
                    SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                    null,
                    prevSelectedSet,
                    null,
                    recordsRemovedFromSelection);
        }
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    /** Clears the set of selected recors of the Table. */
    public void resetSelectedSubset() {
        if (selectedSubset.size() == 0) return;
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        for (int i=0; i<selectedSubset.size(); i++) {
            unselectedSubset.add(selectedSubset.get(i));
            recordSelection.set(selectedSubset.get(i), false); //(((Integer) selectedSubset.at(i)).intValue(), false); //Boolean.FALSE);
        }
        selectedSubset.clear();
        setModified();
        fireSelectedRecordSetChanged(
                SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                null,
                prevSelectedSet,
                null,
                prevSelectedSet);
    }


    public void selectAll() {
        if (selectedSubset.size() == recordCount) return;

        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray recordsAddedToSelection = (IntBaseArray) unselectedSubset.clone();
        for (int i=0; i<unselectedSubset.size(); i++) {
            selectedSubset.add(unselectedSubset.get(i));
            recordSelection.set(unselectedSubset.get(i), true); //(((Integer) unselectedSubset.at(i)).intValue(), true); //Boolean.TRUE);
        }
        unselectedSubset.clear();
        setModified();
        fireSelectedRecordSetChanged(
                SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                null,
                prevSelectedSet,
                recordsAddedToSelection,
                null);
    }


    public void invertSelection() {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
//        IntBaseArray prevUnselectedSet = (IntBaseArray) unselectedSubset.clone();
        IntBaseArrayDesc temp = selectedSubset;
        selectedSubset = unselectedSubset;
        unselectedSubset = temp;
        for (int i=0; i<recordSelection.size(); i++) {
            if (!recordSelection.get(i)) //.equals(Boolean.FALSE))
                recordSelection.set(i, true); //Boolean.TRUE);
            else
                recordSelection.set(i, false); //Boolean.FALSE);
        }
        setModified();
        fireSelectedRecordSetChanged(
                SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                null,
                prevSelectedSet,
                null,
                null);
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    /** Reports the selection status of a record in the Table. If <code>recIndex</code> is
     *  less than 0, or greater then the index of the last record, then <code>false</code> is
     *  returned.
     */
    public boolean isRecordSelected(int recIndex) {
        try{
//            System.out.println("recordSelection: " + recordSelection);
            return recordSelection.get(recIndex); //((Boolean) recordSelection.at(recIndex)).booleanValue();
        }catch (Exception e) {
            return false;
        }
    }


    /*
    *  Removes a record from the Table.
    *  @param r The record to be deleted.
    *  @exception TableNotExpandableException If the table is not expandable.
    *  @exception InvalidRecordIndexException If the provided record is not valid, e.g. is already deleted.
    */
/*2.0.1    public void removeRecord(Record r, int rowIndex, boolean isChanging) throws TableNotExpandableException,
InvalidRecordIndexException {
//        try {
removeRecord(r.getIndex(), rowIndex, isChanging);
//        }catch (TableNotExpandableException e) {throw new TableNotExpandableException(e.message);}
//         catch (InvalidRecordIndexException e) {throw new InvalidRecordIndexException(bundle.getString("CTableMsg50"));}
}
*/

    /**
     *  Enables or disables record addition. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Record addition allowed" and the event is thrown by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setRecordAdditionAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && recordAdditionAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (recordAdditionAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Record addition allowed",
                    new Boolean(recordAdditionAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            recordAdditionAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if records can be added to the Table.
     *  @return <code>true</code>, if records can be added to the table. <code>false</code>, otherwise.
     */
    public boolean isRecordAdditionAllowed() {
        return recordAdditionAllowed;
    }


    /**
     *  Enables or disables record removal. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Record removal allowed" and the event is thrown by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setRecordRemovalAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && database.isLocked()) return;
        if (recordRemovalAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Record removal allowed",
                    new Boolean(recordRemovalAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            recordRemovalAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if records can be removed from the Table.
     *  @return <code>true</code>, if records can be added to the table. <code>false</code>, otherwise.
     */
    public boolean isRecordRemovalAllowed() {
        return recordRemovalAllowed;
    }


    /**
     *  Enables or disables field addition. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Field addition allowed" and the event is thrown by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setFieldAdditionAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && fieldAdditionAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (fieldAdditionAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field addition allowed",
                    new Boolean(fieldAdditionAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            fieldAdditionAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if fields can be added to the Table.
     *  @return <code>true</code>, if fields can be added to the table. <code>false</code>, otherwise.
     */
    public boolean isFieldAdditionAllowed() {
        return fieldAdditionAllowed;
    }


    /**
     *  Enables or disables field removal. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Field removal allowed" and the event is thrown by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setFieldRemovalAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && fieldRemovalAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (fieldRemovalAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field removal allowed",
                    new Boolean(fieldRemovalAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            fieldRemovalAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if fields can be removed from the Table.
     *  @return <code>true</code>, if fields can be removed from the table. <code>false</code>, otherwise.
     */
    public boolean isFieldRemovalAllowed() {
        return fieldRemovalAllowed;
    }


    /**
     *  Enables or disables field re-ordering. Triggers a PropertyChangeEvent for the
     *  bound property <code>fieldReorderingAllowed</code>. The name of the property is
     *  "Field reordering allowed" and the event is thrown by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setFieldReorderingAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && fieldReorderingAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (fieldReorderingAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field reordering allowed",
                    new Boolean(fieldReorderingAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            fieldReorderingAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if fields can be re-ordered in the Table.
     *  @return <code>true</code>, if fields can be re-ordered. <code>false</code>, otherwise.
     */
    public final boolean isFieldReorderingAllowed() {
        return fieldReorderingAllowed;
    }


    /**
     *  Enables or disables field property editing. Triggers a PropertyChangeEvent for the
     *  bound property <code>fieldPropertyEditingAllowed</code>. The name of the property is
     *  "Field property editing allowed" and the event is thrown by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setFieldPropertyEditingAllowed(boolean allowed) throws PropertyVetoException {
        if (fieldPropertyEditingAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field property editing allowed",
                    new Boolean(fieldPropertyEditingAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            fieldPropertyEditingAllowed = allowed;
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if the fields' properties can be edited in the Table.
     *  @return <code>true</code>, if fields' properties can be edited. <code>false</code>, otherwise.
     */
    public final boolean isFieldPropertyEditingAllowed() {
        return fieldPropertyEditingAllowed;
    }

    /**
     *  Enables or disables the editing of the title of the Table. Triggers a
     *  PropertyChangeEvent for the bound property <code>tableRenamingAllowed</code>. The
     *  name of the property is "Table renaming allowed" and the event is thrown by
     *  the Table. Before the property changes it value, any registered
     *  VetoableChangeListeners have the chance to veto the change. In this case,
     *  the change will not take place.
     *  @param allowed
     */
    public void setTableRenamingAllowed(boolean allowed) throws PropertyVetoException {
        if (tableRenamingAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Table renaming allowed",
                    new Boolean(tableRenamingAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            tableRenamingAllowed = allowed;
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if the name of the Table is editable.
     *  @return <code>true</code>, if fields' properties can be edited. <code>false</code>, otherwise.
     */
    public final boolean isTableRenamingAllowed() {
        return tableRenamingAllowed;
    }

    /**
     *  Enables or disables the change of the Table's key. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Key change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setKeyChangeAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && keyChangeAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (keyChangeAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Key change allowed",
                    new Boolean(keyChangeAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            keyChangeAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if the key of the table can be changed.
     *  @return <code>true</code>, if the key can change. <code>false</code>, otherwise.
     */
    public boolean isKeyChangeAllowed() {
        return keyChangeAllowed;
    }


    /**
     *  Enables or disables hidden fields' display. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Hidden fields displayed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param displayed
     */
    public void setHiddedFieldsDisplayed(boolean displayed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && hiddenFieldsDisplayed != displayed) {
//t        if (database != null && database.isLocked()) return;
        if (hiddenFieldsDisplayed != displayed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Hidden fields displayed",
                    new Boolean(hiddenFieldsDisplayed),
                    new Boolean(displayed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            hiddenFieldsDisplayed = displayed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }


    /**
     *  Reports if the tables hidden fields are displayed
     *  @return <code>true</code>, if the key can change. <code>false</code>, otherwise.
     */
    public final boolean isHiddenFieldsDisplayed() {
        return hiddenFieldsDisplayed;
    }


    /**
     *  Enables or disables editing of the Table's data. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Data change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setDataChangeAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && dataChangeAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (dataChangeAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Data change allowed",
                    new Boolean(dataChangeAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            dataChangeAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }

    /**
     *  Reports if the table's data is editable.
     *  @return <code>true</code>, if the data is editable. <code>false</code>, otherwise.
     */
    public final boolean isDataChangeAllowed() {
        return dataChangeAllowed;
    }


    /**
     *  Enables or disables modification of the table's <code>Hidden</code> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Hidden attribute change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  @param allowed
     */
    public void setHiddenAttributeChangeAllowed(boolean allowed) throws PropertyVetoException {
//t        if (database != null && !database.isLocked() && hiddenAttributeChangeAllowed != allowed) {
//t        if (database != null && database.isLocked()) return;
        if (hiddenAttributeChangeAllowed != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Hidded attribute change allowed",
                    new Boolean(hiddenAttributeChangeAllowed),
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            hiddenAttributeChangeAllowed = allowed;
            setModified();
//t            if (database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
        }
    }

    /**
     *  Reports if the table's <code>Hidden</code> attribute can be modified.
     *  @return <code>true</code>, if the data is editable. <code>false</code>, otherwise.
     */
    public final boolean isHiddenAttributeChangeAllowed() {
        return hiddenAttributeChangeAllowed;
    }


    /**
     *  Returns an ArrayBase with the contents of a Table's field. The returned data array is a sub-class of the
     *  class ArrayBase depending on the data type of the field. The following table describes the class of the
     *  returned data array for each of the supported field data types.
        <table border="1" width="100%">
          <tr>
            <td width="50%" align="center"><b>Field data type</b></td>
            <td width="50%" align="center"><b>Array type</b></td>
          </tr>
          <tr>
            <td width="50%" align="center">String</td>
            <td width="50%" align="center">StringBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">Integer</td>
            <td width="50%" align="center">IntBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">Double</td>
            <td width="50%" align="center">DblBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">Boolean</td>
            <td width="50%" align="center">BooleanBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">URL</td>
            <td width="50%" align="center">URLBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">CDate</td>
            <td width="50%" align="center">CDateBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">CTime</td>
            <td width="50%" align="center">CTimeBaseArray</td>
          </tr>
          <tr>
            <td width="50%" align="center">CImageIcon</td>
            <td width="50%" align="center">CImageIconBaseArray</td>
          </tr>
        </table>
     *  @param fieldIndex The index of the field.
     *  @return The contents of the field.
     *  @exception InvalidFieldIndexException If <code>fieldIndex < 0</code> or <code>fieldIndex > #Fields</code>.
     */
    public ArrayBase getField(int fieldIndex) throws InvalidFieldIndexException {
        try{
//            return /*convertArrayListToArray(*/((ArrayList)tableData.get(fieldIndex))/*)*/;
            return tableFields.get(fieldIndex).getData();
        }catch (Exception e) {throw new InvalidFieldIndexException(bundle.getString("CTableMsg51"));}
    }


    /**
     *  Returns an ArrayBase with the contents of a Table's field. The type of the ArrayBase is described at method
     *  <code>getField(int fieldIndex)</code>.
     *  @param fieldName The name of the field.
     *  @return The contents of the field.
     *  @exception InvalidFieldNameException If no field <code>fieldName</code> exists in the table.
     *  @see #getField(int)
     */
    public ArrayBase getField(String fieldName) throws InvalidFieldNameException {
        if (fieldName == null || fieldName.equals(""))
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));

        boolean fieldFound = false;
        int fieldIndex;
        for (fieldIndex=0; fieldIndex<tableFields.size(); fieldIndex++) {
            if ((tableFields.get(fieldIndex)).getName().equals(fieldName)) {
                fieldFound = true;
                break;
            }
        }

        if (!fieldFound)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));
        try {
            return getField(fieldIndex);
        }catch (InvalidFieldIndexException e) {System.out.println("Inconsistency error : " + e.message);return null;}
    }


    /**
     *  Get the index of the field <code>fieldName</code> of the Table. The index of a field
     *  does not change throughout the field's lifetime in the table.
     *  @param fieldName The name of the field.
     *  @return The field's index in the table.
     *  @exception InvalidFieldNameException If the table contains no field named <code>fieldName</code>.
     */
    public int getFieldIndex(String fieldName) throws InvalidFieldNameException {
        if (fieldName == null || fieldName.equals(""))
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));

        boolean fieldFound = false;
        int fieldIndex;
        for (fieldIndex=0; fieldIndex<tableFields.size(); fieldIndex++) {
            if ((tableFields.get(fieldIndex)).getName().equals(fieldName)) {
                fieldFound = true;
                break;
            }
        }

        if (!fieldFound)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));

        return fieldIndex;
    }


    /**
     *  Returns the name of the field with index <code>fieldIndex</code>.
     *  @param fieldIndex The index of the field.
     *  @exception InvalidFieldIndexException If <code>fieldIndex<0</code> or <code>fieldIndex>#Fields</code>.
     */
    public String getFieldName(int fieldIndex) throws InvalidFieldIndexException {
        try{
            String fieldName = (tableFields.get(fieldIndex)).getName();
            return fieldName;
        }catch (Exception e) {throw new InvalidFieldIndexException(bundle.getString("CTableMsg51") + " \"" + fieldIndex + "\".");}
    }


    /**
     *  Returns an ArrayList with the contents of the record at <code>recIndex</code>.
     *  @param recIndex The index of the record.
     *  @exception InvalidRecordIndexException If <code>recIndex<0</code> or <code>recIndex>#Records</code>.
     */
    public ArrayList getRecord(int recIndex) throws InvalidRecordIndexException {
        if (recIndex <= recordCount && recIndex >= 0) {
            ArrayList record = new ArrayList();
//            for (int i=0; i<tableData.size(); i++)
//System.out.println("tableFields.size(): " + tableFields.size());
            for (int i=0; i<tableFields.size(); i++) {
//System.out.println("Getting field value of: " + tableFields.get(i).getName());
                record.add(tableFields.get(i).getObjectAt(recIndex));
            }
//                record.add(((ArrayList) tableData.get(i)).get(recIndex));
            return record;
        }else
            throw new InvalidRecordIndexException(bundle.getString("CTableMsg50") + recIndex);
    }

    /**
     *  Returns an ArrayList with the contents of the record at <code>recIndex</code>.
     *  @param recIndex The index of the record.
     *  @exception InvalidRecordIndexException If <code>recIndex<0</code> or <code>recIndex>#Records</code>.
     */
    public ArrayList getRow(int recIndex) throws InvalidRecordIndexException {
        return getRecord(recIndex);
/*		if (recIndex <= recordCount && recIndex >= 0) {
ArrayList record = new ArrayList();
for (int i=0; i<tableData.size(); i++)
record.add(((ArrayList) tableData.get(i)).get(recIndex));
return record;
}else
throw new InvalidRecordIndexException(bundle.getString("CTableMsg50") + recIndex);
*/
    }

    /*
    *  Returns an Array with the contents of record <code>r</code>.
    *  @param r The record, whose contents will be returned.
    *  @exception InvalidRecordIndexException If an invalid record is provided, e.g. a deleted one.
    */
/*2.0.1    public Array getRecord(Record r) throws InvalidRecordIndexException {
try{
return getRecord(r.getIndex());
}catch (InvalidRecordIndexException e) {throw new InvalidRecordIndexException(e.message);}
}
*/

    public static boolean identicalRecords(Array record1, Array record2) {
        if (record1 == null || record2 == null)
            return false;
        if (record1.size() != record2.size())
            return false;

        Object o1, o2;
        for (int i=0; i<record1.size(); i++) {
            o1 = record1.at(i);
            o2 = record2.at(i);
            if (o1 == null || o2 == null) {
                if (o1 != o2)
                    return false;
                continue;
            }
            if (!o1.getClass().getName().equals(o2.getClass().getName()) ||
                    !o1.equals(o2))
                return false;
        }
        return true;
    }

    /**
     *  Returns the number of records in the Table.
     */
    public int getRecordCount() {
        return recordCount;
    }


    /**
     *  Returns the number of fields in the Table.
     */
    public int getFieldCount() {
        return tableFields.size();
    }

    /**
     *  Returns the number of <code>Image</code> fields in the Table.
     */
    public int getImageFieldCount() {
        int imageFieldCount = 0;
        for (int i=0; i<tableFields.size(); i++) {
            if ((tableFields.get(i)).getDataType().equals(ImageTableField.DATA_TYPE))
                imageFieldCount++;
        }

        return imageFieldCount;
    }


    /**
     *  Returns a clone of the list of the fields of the Table.
     */
    public TableFieldBaseArray getFields() {
        return (TableFieldBaseArray) tableFields.clone();
    }

    /**
     *  Creates an empty Array whose capacity is set to the number of fields in the Table.
     *  This Array can be used to insert new records in the table.
     *  @see #addRecord(ArrayList, boolean)
     */
    public ArrayList createEmptyRecordEntryForm() {
        ArrayList recEntryForm = new ArrayList();
        recEntryForm.ensureCapacity(tableFields.size());
        return recEntryForm;
    }


    /** Returns the contents of the cell at <code>fieldIndex</code> and <code>recIndex</code>.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public Object getCell(int fieldIndex, int recIndex) throws InvalidCellAddressException {
        if (fieldIndex < tableFields.size() && fieldIndex >= 0) {// && recIndex <= recordCount && recIndex >= 0)
            return tableFields.get(fieldIndex).getCellObject(recIndex);
//            return ((Object) ((ArrayList) tableData.get(fieldIndex)).get(recIndex));
        }else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
/*        if (fieldIndex < tableFields.size() && fieldIndex >= 0 && recIndex <= recordCount && recIndex >= 0)
            return ((Object) ((ArrayList) tableData.get(fieldIndex)).get(recIndex));
        else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
*/
    }


    /**
     *  Returns the contents of the cell  at <code>fieldIndex</code> and <code>recIndex</code>. This method
     *  is a faster version of <b>getCell()</b>. It is faster because no checks are
     *  made whether the given cell address is correct. Therefore it may fail, if not
     *  used carefully.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     */
    public Object riskyGetCell(int fieldIndex, int recIndex) {
//System.out.println("Returning " + tableFields.get(fieldIndex).getObjectAt(recIndex) + " for " + tableFields.get(fieldIndex).getName() + ", recIndex: " + recIndex);
        return tableFields.get(fieldIndex).getObjectAt(recIndex);
//        return ((ArrayList) tableData.get(fieldIndex)).get(recIndex);
    }

    /**
     *  Returns the textual representation of the contents of the cell  at <code>fieldIndex</code> and <code>recIndex</code>.
     *  This method is a faster version of <b>getCell()</b>. It is faster because no checks are
     *  made whether the given cell address is correct. Therefore it may fail, if not
     *  used carefully.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     */
    public String riskyGetCellAsString(int fieldIndex, int recIndex) {
//System.out.println("Returning " + tableFields.get(fieldIndex).getObjectAt(recIndex) + " for " + tableFields.get(fieldIndex).getName() + ", recIndex: " + recIndex);
        return tableFields.get(fieldIndex).getCellAsString(recIndex);
//        return ((ArrayList) tableData.get(fieldIndex)).get(recIndex);
    }

    /** Returns the contents of the cell which belongs to field <code>fieldName</code> and
     *  record <code>recIndex</code>.
     *  @param fieldName The name of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public Object getCell(String fieldName, int recIndex) throws InvalidCellAddressException {
        try{
            int fieldIndex = getFieldIndex(fieldName);
            if (recIndex >=0 && recIndex <= recordCount)
                return riskyGetCell(fieldIndex, recIndex);
            else
                throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
        }catch (InvalidFieldNameException e) {
            e.printStackTrace();
            throw new InvalidCellAddressException(bundle.getString("CTableMsg54") + fieldName + bundle.getString("CTableMsg53") + recIndex);
        }
    }


    /* Returns the contents of the cell which belongs to field <code>f</code> and
    *  record <code>r</code>.
    *  @param f The field to which the cell belongs.
    *  @param r The record which contains the cell.
    *  @exception InvalidCellAddressException Invalid cell address.
    */
/*2.0.1    public Object getCell(TableField f, Record r) throws InvalidCellAddressException {
int recIndex = r.getIndex(), fieldIndex;
if (recIndex < 0 || recIndex > recordCount)
throw new InvalidCellAddressException(bundle.getString("CTableMsg55") + r + bundle.getString("CTableMsg56") + f + bundle.getString("CTableMsg57"));

try {
fieldIndex = getFieldIndex(f);
}catch (InvalidFieldException e) {
e.printStackTrace();
throw new InvalidCellAddressException(bundle.getString("CTableMsg55") + r + bundle.getString("CTableMsg56") + f + bundle.getString("CTableMsg57"));
}

return riskyGetCell(fieldIndex, recIndex);
}
*/

    /**
     *  Sets the specified cell to <code>value</code>.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
     *  @exception InvalidCellAddressException Invalid cell address.
     *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <code>value</code> is <code>null</code>.
     *  @exception InvalidDataFormatException If the supplied <code>value</code> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception DuplicateKeyException If the cell is part of the record's key and the new <code>value</code> violates the key uniqueness.
     */
    public boolean setCell(int fieldIndex, int recIndex, Object value) throws InvalidCellAddressException,
            NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException {
        if (fieldIndex < 0 || fieldIndex >= tableFields.size())
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
//System.out.println("Table.setCell() value: " + value + ", " + fieldIndex + ", " + recIndex);
        return tableFields.get(fieldIndex).setCell(recIndex, value);

/*        if (!dataChangeAllowed && !pendingNewRecord)
            throw new AttributeLockedException(bundle.getString("CTableMsg89"));
*/
/*        Array calculatedCellAddresses = new Array();
Array calculatedCellAffectOthers = new Array();
Array oldValues = new Array();
Array newValues = new Array();
*/
/*        if (fieldIndex < tableFields.size() && fieldIndex >= 0 && recIndex <= recordCount && recIndex >= 0) {
            Object oldValue = riskyGetCell(fieldIndex, recIndex);

            TableField f = tableFields.get(fieldIndex);
            boolean affectsOtherCells = (f.dependentCalcFields.size() != 0);

            if (!f.isEditable())
                return false;
*/
/*            if (value == null || (value.getClass().isInstance("a string") && ((String) value).equals(""))) {
if (f.isKey())
throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));
else{
Object previousValue = ((ArrayList) tableData.get(fieldIndex)).get(recIndex);
((ArrayList) tableData.get(fieldIndex)).set(recIndex, null);
try{
updateDependentCalculatedFields(f, recIndex);
}catch (DuplicateKeyException e) {
changedCalcCells.clear();
*/
            /* Undo the change. Recalculate the dependent calculated fields.
            */
/*                        ((ArrayList) tableData.get(fieldIndex)).set(recIndex, previousValue);
calculatedFieldsChanged = false;
try{
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);
}catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (1)");}
catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (1)");}
throw new DuplicateKeyException(e.message);
}
catch (NullTableKeyException e) {
changedCalcCells.clear();
*/
            /* Undo the change. Recalculate the dependent calculated fields.
            */
/*                        ((ArrayList) tableData.get(fieldIndex)).set(recIndex, previousValue);
calculatedFieldsChanged = false;
try{
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);
}catch (DuplicateKeyException e1) {
if (!pendingNewRecord)
System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (2)");
}
catch (NullTableKeyException e1) {
if (!pendingNewRecord)
System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (2)");
}

throw new NullTableKeyException(e.message);
}
//                    }
setModified();
//t                    if (this.database != null)
fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, value, oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));

return true;
}
}
*/
/*            if (value != null && value instanceof String && ((String) value).trim().length() == 0)
                value = null;

            //If the "value" object is of the same type as the field
            //to be inserted to, then don't perform any data type conversion
            if (value != null) {
                if (!f.getDataType().isInstance(value)) {
                    value = convertToFieldType(value, f);
                }else{
*/                    /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
                    * attribute has to be synchronized against the field's "linkToExternalData" attribute.
                    */
/*                    if (f.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                        if (f.containsLinksToExternalData())
                            ((CImageIcon) value).referenceToExternalFile = true;
                        else
                            ((CImageIcon) value).referenceToExternalFile = false;
                    }
                }
            }
*/
            /*If the new value of the cell is equal to existing then
            *do nothing.
            */
/*            Object currentValue = riskyGetCell(fieldIndex, recIndex);
            if (value == currentValue) return true;
            if (value != null && value.equals(currentValue))
                return true;

            if (value == null && f.isKey())
                throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));

            if (f.isKey()) {
                if (checkKeyUniquenessOnNewValue(fieldIndex, recIndex, value) == false)
                    throw new DuplicateKeyException(bundle.getString("CTableMsg60") + value + bundle.getString("CTableMsg61"));
*/
                /*If field f is part of the table's key and is also the field whose values
                *are used in the primarykeyMap HashMap, then the entry for this record in
                *the HashMap has to be deleted and a new entry with the correct new key
                *value has to be entered.
                */
/*                updatePrimaryKeyMapForCalcField(fieldIndex, recIndex, value);
            }

//            Object previousValue = ((ArrayList) tableData.get(fieldIndex)).get(recIndex);
            tableFields.get(fieldIndex).set(recIndex, value);
//            ((ArrayList) tableData.get(fieldIndex)).set(recIndex, value);
            updateDependentCalculatedFields(f, fieldIndex, recIndex, currentValue);

            setModified();
//t            if (this.database != null)
            fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, value, oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));
            return true;

        }else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
*/
    }


    /** This method makes any possible effort to convert the supplied <code>value</code> to the field type of
     * the Table field <code>f</code> and returns the result.
     * @return An object of the data type of field <code>f</code>
     * @exception InvalidDataFormatException If the conversion is not possible.
     */
    Object convertToFieldType(Object value, AbstractTableField f) throws InvalidDataFormatException {
        if (value == null) return null;
        if (f == null) return null;
        if (f.getDataType().isAssignableFrom(value.getClass()))
            return value;

        Class dataType = f.getDataType();
        if (dataType.equals(DateTableField.DATA_TYPE) ) {
            CDate d = new CDate();
            try{
                d = new CDate(dateFormat.parse(((String) value), new ParsePosition(0)));
                calendar.setTime(d);
                calendar.add(Calendar.HOUR, 10);
                d = new CDate(calendar.getTime());
                value = d;
            }catch (Exception e) {
                throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg40"));
            }

            if (d == null)
                throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg40"));

            value = d;
        }else if (dataType.equals(TimeTableField.DATA_TYPE)) {
            CTime t = new CTime();
            try{
                t = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
            }catch (Exception e) {
                throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));
            }

            if (t == null)
                throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));

            value = t;
        }else if (dataType.isAssignableFrom(DoubleTableField.DATA_TYPE) && String.class.isInstance(value)) {
            try{
                value = numberFormat.parse((String) value);
            }catch (ParseException e) {
//            }catch (DoubleNumberFormatParseException e) {
                throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));
            }
        }else if (dataType.isAssignableFrom(FloatTableField.DATA_TYPE) && String.class.isInstance(value)) {
            try{
                value = numberFormat.parse((String) value);
            }catch (ParseException e) {
//            }catch (DoubleNumberFormatParseException e) {
                throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));
            }
            /* If the "value"'s class is different from the field's class then:
            *    * get the field's and the value's Class,
            *    * get a constructor of the field's Class that takes one
            *      parameter of the Class of the "value", if one exists,
            *    * call the constructor, with the "value" object as argument and
            *      insert the returned value to the field.
            */
        }else{
            Class[] cl = new Class[1];
            Object[] val = new Object[1];
            Constructor con;
            if (!f.containsLinksToExternalData()) {
                cl[0] = value.getClass();
                val[0] = value;
            }else{
                cl = new Class[2];
                val = new Object[2];
                cl[0] = value.getClass();
                cl[1] = java.lang.Boolean.class;
                val[0] = value;
                val[1] = Boolean.TRUE;
            }
            try {
//System.out.println("f: " + f);
//System.out.println("dataType: " + f.getDataType());
                // ������� ���� ��� �� �������� Double ��� Integer ��� ������� ������ ����������. ������ ������ ��
                // ���������� Number �� ��� ���� ��� ���� ���������.
                con = f.getDataType().getConstructor(cl);
                try {
//                        System.out.println("Supplied value: " + value + ", constructor: " + cl);
                    value =  con.newInstance(val);
//                        System.out.println("value: " + value + ", " + value.getClass());
                }catch (InvocationTargetException e) {e.printStackTrace(); throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                catch (InstantiationException e) {e.printStackTrace(); throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                catch (IllegalAccessException e) {e.printStackTrace(); throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                catch (UnableToCreateImageIconException e) {e.printStackTrace(); throw new InvalidDataFormatException(e.getMessage());}
            }catch (NoSuchMethodException e) {e.printStackTrace(); throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
        }
        return value;
    }


    /** Checks if new key value <code>value</code> is unique. Flag <code>insertingNewRecord</code> is set when
     *  an interactive record insertion takes place. In this case an empty record has been inserted
     *  in the table prior to setting of the new record's values, so <code>checkKeyUniquenessOnNewValue</code>
     *  will search all the records but the one which is being added. In the common case where the value
     *  of a key field is changed, <code>insertingNewRecord</code> should be <code>false</code>.
     */
/*    boolean checkKeyUniquenessOnNewValue(int fieldIndex, int recIndex, Object value) { //, boolean insertingNewRecord) {
//        System.out.println("checkKeyUniquenessOnNewValue value: " + value + " on fieldIndex: " + fieldIndex);
//        System.out.println("recIndex: " + recIndex + " recordCount: " + recordCount);
//        System.out.println(((ArrayList) tableData.get(fieldIndex)));
        AbstractTableField f = tableFields.get(fieldIndex);
        boolean valueExists = false;
        if (recIndex == 0) {
            valueExists = (f.indexOf(1, recordCount, value) != -1);
//            System.out.println("1Checking from : 1 to : " + (recordCount));
        }else if (recIndex == recordCount) {
//            System.out.println(tableData.get(fieldIndex) + "   fieldIndex: " + fieldIndex);
//            System.out.println("Found value: " + value + "(" + value.getClass() + ")" + "? " + ((ArrayList) tableData.get(fieldIndex)).indexOf(0, recordCount-1, value));
            valueExists = (f.indexOf(0, recordCount-1, value) != -1);
//            System.out.println("2-----------Checking from : 0 to : " + (recordCount-1));
        }else{
            valueExists = ((f.indexOf(0, recIndex-1, value) != -1) ||
                    (f.indexOf(recIndex+1, recordCount, value) != -1));
//            System.out.println("3Checking from : 0  to : " + (recIndex-1) + " and from : " + (recIndex+1) + " to : " + (recordCount));
        }
//        if (((ArrayList) tableData.at(fieldIndex)).indexOf(0, recIndex-1, value) != -1 || ((ArrayList) tableData.at(fieldIndex)).indexOf(recIndex+1, recordCount, value) != -1)           contains(value)) {
        if (valueExists) {
//            System.out.println("In here");
            int index = -1, i=0;;
            if (keyFieldIndices.size() > 1) {
                boolean different = false;
//                System.out.println("In here " + fieldIndex + ", value: " + value + " , recordCount: " + recordCount);
//                System.out.println(tableData.at(fieldIndex));
                int searchUpToRecord = recordCount;
                if (pendingNewRecord)
                    searchUpToRecord = recordCount-1;
//                try{
                while ((index = f.indexOf(index+1, searchUpToRecord, value)) != -1 &&
                        index != recIndex) {
//                    System.out.println("Found " + value + " at index: " + index);
//                    System.out.println("keyFieldIndices: " + keyFieldIndices);
                    while (i<keyFieldIndices.size()) {
                        int nextKeyFldIndex = keyFieldIndices.get(i);
*/                        /* Don't compare the two records on the field that has changed. Equality
                        * on these fields is guaranteed by the first while-loop condition.
                        */
/*                        if (nextKeyFldIndex == fieldIndex)  {
                            i++;
                            continue;
                        }

//                        System.out.println("Comparing on field " + riskyGetField(i).getName() + " records " + new Integer(index).toString() + " and " + new Integer(recIndex).toString() + " : " + riskyGetCell(nextKeyFldIndex, index) + " - " + riskyGetCell(nextKeyFldIndex, recIndex));
//                        System.out.println("checkKeyUniquenessOnNewValue1");
                        if ((!riskyGetCell(nextKeyFldIndex, index).equals(riskyGetCell(nextKeyFldIndex, recIndex)))) {
                            different = true;
                            break;
                        }
                        i++;
                    }
//                    System.out.println("checkKeyUniquenessOnNewValue2     Diferrent:" + different);
                    if (!different) {
//                        System.out.println("checkKeyUniquenessOnNewValue3");
                        return false;
                    }
                    different = false;
//                    System.out.println("checkKeyUniquenessOnNewValue4");

                }
//                }catch (Exception e) {System.out.println(e.getClass() + " , " + e.getMessage()); e.printStackTrace();}
            }else
                return false;
        }
//        System.out.println("checkKeyUniquenessOnNewValue5");
        return true;
    }
*/

    /**
     *  Sets the specified cell to <code>value</code>.
     *  @param fieldName The name of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
     *  @exception InvalidCellAddressException Invalid cell address.
     *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <code>value</code> is <code>null</code>.
     *  @exception InvalidDataFormatException If the supplied <code>value</code> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception DuplicateKeyException If the cell is part of the record's key and the new <code>value</code> violates the key uniqueness.
     */
    public boolean setCell(String fieldName, int recIndex, Object value) throws InvalidCellAddressException, InvalidFieldNameException,
            NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException {
        return setCell(getFieldIndex(fieldName), recIndex, value);
/*        if (!dataChangeAllowed && !pendingNewRecord)
            throw new AttributeLockedException(bundle.getString("CTableMsg89"));

        int fieldIndex;
        try {
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {
//            e.printStackTrace();
            throw new InvalidCellAddressException(bundle.getString("CTableMsg54") + fieldName + bundle.getString("CTableMsg53") + recIndex);
        }

        if (recIndex <= recordCount && recIndex >= 0) {
            Object oldValue = riskyGetCell(fieldIndex, recIndex);
            boolean affectsOtherCells = false;
            Class[] cl = new Class[1];
            Object[] val = new Object[1];
            Constructor con;

            TableField f = tableFields.get(fieldIndex);

            if (!f.isEditable())
                return false;

//            System.out.println("value: " + value + ", value class: " + value.getClass());
            if (value == null || (value.getClass().isInstance("a string") && ((String) value).equals(""))) {
                if (f.isKey())
                    throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));
                else{
                    Object previousValue = f.getObjectAt(recIndex);
                    f.set(recIndex, null);
                    if (!(f.dependentCalcFields.size() != 0)) {
                        affectsOtherCells = true;
                        try{
                            for (int i=0; i<f.dependentCalcFields.size(); i++) {
                                evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);

                                TableField f1 = tableFields.get(f.dependentCalcFields.get(i));
                                changedCalcCells.add(f1.getName(), new Integer(recIndex));
                            }
                            calculatedFieldsChanged = true;
                        }catch (DuplicateKeyException e) {
                            changedCalcCells.clear();
*/                            /* Undo the change. Recalculate the dependent calculated fields.
                            */
/*                            f.set(recIndex, previousValue);
                            try{
                                for (int i=0; i<f.dependentCalcFields.size(); i++)
                                    evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);
                            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (1)");}
                            catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (1)");}
                            throw new DuplicateKeyException(e.message);
                        }
                        catch (NullTableKeyException e) {
                            changedCalcCells.clear();
*/                            /* Undo the change. Recalculate the dependent calculated fields.
                            */
/*                            f.set(recIndex, previousValue);
                            try{
                                for (int i=0; i<f.dependentCalcFields.size(); i++)
                                    evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);
                            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (2)");}
                            catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (2)");}

                            throw new NullTableKeyException(e.message);
                        }
                    }
                    setModified();
//t                    if (this.database != null)
                    fireCellValueChanged(new CellValueChangedEvent(this, fieldName, recIndex, value, oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), fieldName, recIndex, value, oldValue, affectsOtherCells));
                    return true;
                }
            }

//            System.out.print(value.getClass() + "     " + f.fieldType);
            //If the "value" object is of the same type as the field
            //to be inserted to, then don't perform any data type conversion
            if (!f.getDataType().isInstance(value)) {
                //if the field's type is Date ot Time
//t              if (f.fieldType.getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
                if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
                    CDate d = new CDate();
                    try{
                        d = new CDate(dateFormat.parse(((String) value), new ParsePosition(0)));
                        calendar.setTime(d);
                        calendar.add(Calendar.HOUR, 10);
                        d = new CDate(calendar.getTime());
                    }catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg40"));}

                    if (d == null)
                        throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg40"));
*/
/*                    ((ArrayList) tableData.get(fieldIndex)).put(recIndex, d);
if (!f.dependentCalcFields.isEmpty()) {
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
}
setModified();
return true;
*/
/*                    value = d;
                }else if (f.getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) {
                    CTime t = new CTime();
                    try{
                        t = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
                    }catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));}

                    if (t == null)
                        throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));
*/
/*                    ((ArrayList) tableData.get(fieldIndex)).put(recIndex, t);
if (!f.dependentCalcFields.isEmpty()) {
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
}
setModified();
return true;
*/
/*                    value = t;
                }else if (f.getDataType().getName().equals("java.lang.Double") && java.lang.String.class.isInstance(value)) {
                    try{
                        value = new Double(numberFormat.parse((String) value).doubleValue());
                    }catch (DoubleNumberFormatParseException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
*/
                    /* If the "value"'s class is different from the field's class then:
                    *    * get the field's and the value's Class,
                    *    * get a constructor of the field's Class that takes one
                    *      parameter of the Class of the "value", if one exists,
                    *    * call the constructor, with the "value" object as argument and
                    *      insert the returned value to the field.
                    */
/*                }else{
//                    System.out.println("Am here");
                    if (!f.containsLinksToExternalData()) {
                        cl[0] = value.getClass();
                        val[0] = value;
                    }else{
                        cl = new Class[2];
                        val = new Object[2];
                        cl[0] = value.getClass();
                        cl[1] = java.lang.Boolean.class;
                        val[0] = value;
                        val[1] = Boolean.TRUE;
                    }
                    try {
                        con = f.getDataType().getConstructor(cl);
                        try {
                            value =  con.newInstance(val);
                        }catch (InvocationTargetException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                        catch (InstantiationException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                        catch (IllegalAccessException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                        catch (UnableToCreateImageIconException e) {throw new InvalidDataFormatException(e.getMessage());}
                    }catch (NoSuchMethodException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                }
            }else{
*/                /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
                * attribute has to be synchronized against the field's "linkToExternalData" attribute.
                */
/*                if (f.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                    if (f.containsLinksToExternalData())
                        ((CImageIcon) value).referenceToExternalFile = true;
                    else
                        ((CImageIcon) value).referenceToExternalFile = false;
                }
            }
*/
            /*If the new value of the cell is equal to existing then
            *do nothing
            */
/*            if (value.equals(riskyGetCell(fieldIndex, recIndex)))
                return true;

            if (f.isKey()) {
                if (checkKeyUniquenessOnNewValue(fieldIndex, recIndex, value) == false)
                    throw new DuplicateKeyException(bundle.getString("CTableMsg60") + value + bundle.getString("CTableMsg61"));
*/
                /*If field f is part of the table's key and is also the field whose values
                *are used in the primarykeyMap HashMap, then the entry for this record in
                *the HashMap has to be deleted and a new entry with the correct new key
                *value has to be entered.
                */
/*                updatePrimaryKeyMapForCalcField(fieldIndex, recIndex, value);
            }

            Object previousValue = ((ArrayList) tableData.get(fieldIndex)).get(recIndex);
            ((ArrayList) tableData.get(fieldIndex)).set(recIndex, value);
            try{
                if (!(f.dependentCalcFields.size() == 0)) {
                    affectsOtherCells = true;
                    for (int i=0; i<f.dependentCalcFields.size(); i++) {
                        evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);

                        TableField f1 = tableFields.get(f.dependentCalcFields.get(i));
                        changedCalcCells.add(f1.getName(), new Integer(recIndex));
                    }
                    calculatedFieldsChanged = true;
                }
            }catch (DuplicateKeyException e) {
                changedCalcCells.clear();
*/                /* Undo the change. Recalculate the dependent calculated fields.
                */
/*                ((ArrayList) tableData.get(fieldIndex)).set(recIndex, previousValue);
                try{
                    for (int i=0; i<f.dependentCalcFields.size(); i++)
                        evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (3)");}
                catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (3)");}

                throw new DuplicateKeyException(e.message);
            }
            catch (NullTableKeyException e) {
                changedCalcCells.clear();
                ((ArrayList) tableData.get(fieldIndex)).set(recIndex, previousValue);
                try{
                    for (int i=0; i<f.dependentCalcFields.size(); i++)
                        evaluateCalculatedFieldCell(f.dependentCalcFields.get(i), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}
                catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}

                throw new NullTableKeyException(e.message);
            }
            setModified();
//t            if (this.database != null)
            fireCellValueChanged(new CellValueChangedEvent(this, fieldName, recIndex, value, oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), fieldName, recIndex, value, oldValue, affectsOtherCells));
            return true;


        }else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
*/
    }


    /**
     *  Returns the index of field <code>f</code>.
     *  @param f The field.
     *  @return -1, if field <code>f</code> does not belong to the Table.
     *  @exception InvalidFieldException If the supplied field is invalid, e.g. has been deleted.
     */
    //getFieldIndex(TableField)
    public int getFieldIndex(AbstractTableField f) throws InvalidFieldException {
        String fieldName = f.getName();
        if (fieldName != null) {
            for (int i=0; i<tableFields.size(); i++) {
                if ((tableFields.get(i)).getName().equals(fieldName))
                    return i;
            }
            System.out.println("Serious inconsistency error. The supplied field, though valid, is not included in the tableFields Array.");
            return -1;
        }else
            throw new InvalidFieldException(bundle.getString("CTableMsg62"));
    }

    /*
    *  Sets the specified cell to <code>value</code>.
    *  @param fld The field to which the cell belongs.
    *  @param r The record which contains the cell.
    *  @param value The new value of the cell.
    *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
    *  @exception InvalidCellAddressException Invalid cell address.
    *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <code>value</code> is <code>null</code>.
    *  @exception InvalidDataFormatException If the supplied <code>value</code> does not have or cannot be cast to the data type of the field, to which the cell belongs.
    *  @exception DuplicateKeyException If the cell is part of the record's key and the new <code>value</code> violates the key uniqueness.
    */
    //setCell(TableField, Record, Object)
/*2.0.1
public boolean setCell(TableField fld, Record r, Object value) throws InvalidCellAddressException,
NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException {
if (!dataChangeAllowed && !pendingNewRecord)
throw new AttributeLockedException(bundle.getString("CTableMsg89"));

int fieldIndex, recIndex;
try {
fieldIndex = getFieldIndex(fld);
}catch (InvalidFieldException e) {
e.printStackTrace();
throw new InvalidCellAddressException(bundle.getString("CTableMsg63"));
}

recIndex = r.getIndex();
if (recIndex > recordCount || recIndex < 0)
throw new InvalidCellAddressException(bundle.getString("CTableMsg64"));

Object oldValue = riskyGetCell(fieldIndex, recIndex);
boolean affectsOtherCells = false;
Class[] cl = new Class[1];
Object[] val = new Object[1];
Constructor con;

TableField f = tableFields.get(fieldIndex);

if (!f.isEditable())
return false;

if (value == null || (value.getClass().isInstance("a string") && ((String) value).equals(""))) {
if (f.isKey())
throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));
else{
Object previousValue = ((ArrayList) tableData.at(fieldIndex)).at(recIndex);
((ArrayList) tableData.at(fieldIndex)).put(recIndex, null);
if (!f.dependentCalcFields.isEmpty()) {
affectsOtherCells = true;
try{
for (int i=0; i<f.dependentCalcFields.size(); i++) {
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

TableField f1 = tableFields.get(((Integer) f.dependentCalcFields.at(i)).intValue());
changedCalcCells.add(f1.getName(), new Integer(recIndex));
}
calculatedFieldsChanged = true;
}catch (DuplicateKeyException e) {
changedCalcCells.clear();
*/                        /* Undo the change. Recalculate the dependent calculated fields.
*/
/*                        ((ArrayList) tableData.at(fieldIndex)).put(recIndex, previousValue);
try{
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
}catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (1)");}
catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (1)");}
throw new DuplicateKeyException(e.message);
}
catch (NullTableKeyException e) {
changedCalcCells.clear();
*/                        /* Undo the change. Recalculate the dependent calculated fields.
*/
/*                        ((ArrayList) tableData.at(fieldIndex)).put(recIndex, previousValue);
try{
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
}catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (2)");}
catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (2)");}

throw new NullTableKeyException(e.message);
}
}
setModified();
//t                if (this.database != null)
fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, value, oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));
return true;
}
}

//If the "value" object is of the same type as the field
//to be inserted to, then don't perform any data type conversion
if (!f.fieldType.isInstance(value)) {
//if the field's type is Date ot Time
//t            if (f.fieldType.getName().equals("java.util.Date")) {
//t                if (f.isDate()) {
if (f.fieldType.getName().equals("gr.cti.eslate.database.engine.CDate")) {
CDate d = new CDate();
try{
d = new CDate(dateFormat.parse(((String) value), new ParsePosition(0)));
calendar.setTime(d);
calendar.add(Calendar.HOUR, 10);
d = new CDate(calendar.getTime());
}catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg40"));}

if (d == null)
throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg40"));

value = d;
}else if (f.fieldType.getName().equals("gr.cti.eslate.database.engine.CTime")) {
CTime t = new CTime();
try{
t = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
}catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));}

if (t == null)
throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));
value = t;
}else if (f.getFieldType().getName().equals("java.lang.Double") && java.lang.String.class.isInstance(value)) {
try{
value = new Double(numberFormat.parse((String) value).doubleValue());
}catch (DoubleNumberFormatParseException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
}else{
*/                /* If the "value"'s class is different from the field's class then:
*    * get the field's and the value's Class,
*    * get a constructor of the field's Class that takes one
*      parameter of the Class of the "value", if one exists,
*    * call the constructor, with the "value" object as argument and
*      insert the returned value to the field.
*/

/*                    if (!f.containsLinksToExternalData()) {
cl[0] = value.getClass();
val[0] = value;
}else{
cl = new Class[2];
val = new Object[2];
cl[0] = value.getClass();
cl[1] = java.lang.Boolean.class;
val[0] = value;
val[1] = Boolean.TRUE;
}
//                cl[0] = value.getClass();
try {
con = f.fieldType.getConstructor(cl);
//                    val[0] = value;
try {
value = con.newInstance(val);
}catch (InvocationTargetException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
catch (InstantiationException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
catch (IllegalAccessException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
catch (UnableToCreateImageIconException e) {throw new InvalidDataFormatException(e.getMessage());}
}catch (NoSuchMethodException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
}
}else{
*/            /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
* attribute has to be synchronized against the field's "linkToExternalData" attribute.
*/
/*            if (f.fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
if (f.containsLinksToExternalData())
((CImageIcon) value).referenceToExternalFile = true;
else
((CImageIcon) value).referenceToExternalFile = false;
}
}

*/        /*If the new value of the cell is equal to existing then
*do nothing
*/
/*        if (value.equals(riskyGetCell(fieldIndex, recIndex)))
return true;

if (f.isKey()) {
if (checkKeyUniquenessOnNewValue(fieldIndex, recIndex, value) == false)
throw new DuplicateKeyException(bundle.getString("CTableMsg60") + value + bundle.getString("CTableMsg61"));
*/
    /* If field f is part of the table's key and is also the field whose values
    * are used in the primarykeyMap HashMap, then the entry for this record in
    * the HashMap has to be deleted and a new entry with the correct new key
    * value has to be entered.
    */
/*            updatePrimaryKeyMapForCalcField(fieldIndex, recIndex, value);
}

Object previousValue = ((ArrayList) tableData.at(fieldIndex)).at(recIndex);
((ArrayList) tableData.at(fieldIndex)).put(recIndex, value);
try{
if (!f.dependentCalcFields.isEmpty()) {
affectsOtherCells = true;
for (int i=0; i<f.dependentCalcFields.size(); i++) {
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

TableField f1 = tableFields.get(((Integer) f.dependentCalcFields.at(i)).intValue());
changedCalcCells.add(f1.getName(), new Integer(recIndex));
}
calculatedFieldsChanged = true;
}
}catch (DuplicateKeyException e) {
changedCalcCells.clear();
*/            /* Undo the change. Recalculate the dependent calculated fields.
*/
/*            ((ArrayList) tableData.at(fieldIndex)).put(recIndex, previousValue);
try{
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
}catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (3)");}
catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (3)");}

throw new DuplicateKeyException(e.message);
}
catch (NullTableKeyException e) {
changedCalcCells.clear();
((ArrayList) tableData.at(fieldIndex)).put(recIndex, previousValue);
try{
for (int i=0; i<f.dependentCalcFields.size(); i++)
evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
}catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}
catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}

throw new NullTableKeyException(e.message);
}

setModified();
//t        if (this.database != null)
fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, value, oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));
return true;
}
*/

    /**
     *  Sets the specified cell to <code>value</code>. This method is a faster version of setCell().
     *  It is faster because it performs no checks regarding the address of the cell, the
     *  validity and the uniqueness of the key or the <code>value</code>'s data type. Therefore
     *  it should be used very carefully.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
     *  @see #setCell(int, int, java.lang.Object)
     */
    //riskySetCell(int, int, Object)
    private void riskySetCell(int fieldIndex, int recIndex, Object value) {
        Object oldValue = riskyGetCell(fieldIndex, recIndex);
        tableFields.get(fieldIndex).set(recIndex, value);
        try{
            AbstractTableField f = getTableField(fieldIndex);
//t            if (this.database != null) {
            if (f.dependentCalcFields.size() == 0)
                fireCellValueChanged(f.getName(), recIndex, oldValue, false); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, false));
            else
                fireCellValueChanged(f.getName(), recIndex, oldValue, true); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, true));
//t            }
        }catch (InvalidFieldIndexException e) {}

    }


    /*
    *  Returns the record at <code>recIndex</code>. It may fail, so it should be used
    *  carefully.
    *  @param recIndex The index of the record.
    */
//2.0.1    private Record riskyGetRecord(int recIndex) {
//2.0.1        return (Record) tableRecords.at(recIndex);
//2.0.1    }


    /**
     *  Returns a record as an Object Array. It may fail if an invalid record index is supplied,
     *  so it should be used carefully.
     *  @param recIndex The index of the record.
     */
    public Object[] riskyGetRecObjects(int recIndex) {
        Object[] rec = new Object[getFieldCount()];
        for (int i=0; i<getFieldCount(); i++)
            rec[i] = tableFields.get(i).getObjectAt(recIndex);

        return rec;
    }


    /** Reports whether some value(s) in one or more calculated fields have changed as a result
     *  of editing a value in a non-calculated field. It does not report any change when all the
     *  values of a calculated field have changed as a result of editing the field's formula.
     */
    public boolean haveCalculatedFieldsChanged() {
        return calculatedFieldsChanged;
    }


    /** Returns a Hashtable with the addresses of the cells of the calculated fields of the table, whose
     *  values have been changed as a result of editing a value of a non-calculated field. The cell
     *  address is provided as an entry in the HashMap, whose key is the name of the field and its
     *  value is the record index of the cell.
     */
    public Hashtable getChangedCalcFieldCells() {
        return changedCalcCells;
    }


    /** Resets the flag which tracks whether some value(s) in one or more calculated fields of the table
     *  have changed.
     */
    public void resetCalculatedFieldsChangedFlag() {
        calculatedFieldsChanged = false;
    }


    /** Empties the HashMap which contains the addresses of the cells of the calculated fields in the
     *  table, whose values have changed.
     */
    public void resetChangedCalcCells() {
        changedCalcCells.clear();
    }


    /**
     *  Add field <code>fieldName</code> to the Table's key.
     *  @param fieldName The name of the field.
     *  @exception FieldAlreadyInKeyException If this field is already part of the table's key.
     *  @exception FieldContainsEmptyCellsException If field <code>fieldName</code> contains empty cells.
     *  @exception InvalidFieldNameException If the table contains no field named <code>fieldName</code>.
     *  @exception TableNotExpandableException Adding a new field to the table's (existing or unexisting) key may
     *             result in the removal of several records, in order to ensure key uniqueness. This is
     *             done seamlessly. However if the table is not record expandable and the addition
     *             of this field to the table's key requires the removal of some records, then
     *             this exception will be thrown.
     *  @exception InvalidKeyFieldException If field <code>fieldName</code>, is of <code>Image</code> data type.
     *  @exception AttributeLockedException If the field's <code>key</code> attribute is locked.
     *  @see #recordAdditionAllowed
     */
    //addToKey(String)
    public void addToKey(String fieldName) throws FieldAlreadyInKeyException,
            FieldContainsEmptyCellsException, InvalidFieldNameException, TableNotExpandableException,
            InvalidKeyFieldException, AttributeLockedException {
        if (!keyChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg90"));

        int fieldIndex;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {
//            e.printStackTrace();
            throw new InvalidFieldNameException(bundle.getString("CTableMsg65") + fieldName + bundle.getString("CTableMsg66"));
        }

        AbstractTableField f = tableFields.get(fieldIndex);
        tableKey.addKeyField(f);
/*        if (f.getDataType().equals(CImageIcon.class))
            throw new InvalidKeyFieldException(bundle.getString("CTableMsg67"));
        if (!f.isFieldKeyAttributeChangeAllowed())
            throw new AttributeLockedException(bundle.getString("CTableFieldMsg1"));
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

//        System.out.println("Field " + fieldName + " isKey: " + f.isKey());
        if (f.isKey())
            throw new FieldAlreadyInKeyException(bundle.getString("CTableMsg100") + f.getName() + bundle.getString("CTableMsg68"));
        else{
            //Check if this column contains any "null" values
            for (int i = 0; i<recordCount; i++) {
                if ( f.getObjectAt(i) == null)
                    throw new FieldContainsEmptyCellsException(bundle.getString("CTableMsg100") + f.getName() + bundle.getString("CTableMsg69"));
            }

            try{
                f.setKey(true);
            }catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable _addToKey() : (0.5)"); return;}

            if (this.hasKey()) {
                keyFieldIndices.add(fieldIndex);
            }else{
//                OrderedMap temp2 = new OrderedMap();
//                primaryKeyMap = new OrderedMap(true);
//                HashMap temp2 = new HashMap(new EqualTo());
//                primaryKeyMap = new HashMap(new EqualTo(), true, 10, (float) 1.0);
                Hashtable temp2 = new Hashtable();
                primaryKeyMap = new Hashtable(10, (float) 1.0);
//                System.out.println(primaryKeyMap);
//                ArrayList temp = (ArrayList) tableData.get(fieldIndex);
                //Parse all the records, but the last one
                Object res;
                int i=0;
                while (i<recordCount) {
                    res = temp2.add(f.getObjectAt(i), new Integer(i)); //2.0.1 riskyGetRecord(i));
//                    System.out.println("KeyValue: " + temp.at(i) + ", " + res + " Index: " + i);
                    if (res != null) {
//                        System.out.println("Removing record #" + i);
                        try{
                            removeRecord(i, -1, true);
                        }catch (InvalidRecordIndexException e) {
                            primaryKeyMap = null;
//                            System.out.println(e.message);
                            try{
                                f.setKey(false);
                            }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable addToKey() : (1)");}
                            catch (AttributeLockedException e1) {System.out.println("Serious inconsistency error in Table addToKey() : (1.5)");}
                        }
                        catch (TableNotExpandableException e) {
                            primaryKeyMap = null;
                            try{
                                f.setKey(false);
                            }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table addToKey() : (2)");}
                            catch (AttributeLockedException e1) {System.out.println("Serious inconsistency error in Table addToKey() : (2.5)");}
                            throw new TableNotExpandableException(bundle.getString("CTableMsg70"));
                        }
                        i--;
                    }
                    i++;
                }

                //Copy the contents of the "temp2" OrderedMap to "primaryKeyMap" using
                //the hash codes of the keys of the elements of "temp2" as keys in "primaryKeyMap"
//                OrderedMapIterator iter = temp2.begin();
                HashMapIterator iter = temp2.begin();
                while (!iter.atEnd()) {
                    primaryKeyMap.add(new Integer(iter.key().hashCode()), iter.value());
                    iter.advance();
                }

                hasKey = true;
                keyFieldIndices.add(fieldIndex);
//                System.out.println("Field " + fieldName + " isKey: " + f.isKey());
            }
*/
            /* Adjust the active record.
            */
/*            if (activeRecord != -1) {
                if (activeRecord > recordCount) {
                    setActiveRecord(recordCount);
                }else{
                    if (activeRecord + 1 <= recordCount) {
                        setActiveRecord(activeRecord+1);
                    }else{
                        setActiveRecord(activeRecord);
                    }
                }
            }
            setModified();
            fireColumnKeyChanged(f, true); //database.CTables.indexOf(this), f.getName(), true));
        }
*/
    }


    /**
     *  Removes field <code>fieldName</code> from the Table's key.
     *  @param fieldName The name of the field.
     *  @exception FieldIsNotInKeyException If field <code>fieldName</code> is not part of the
     *             table's key.
     *  @exception InvalidFieldNameException If the table does not contain any field named
     *             <code>fieldName</code>.
     *  @exception TableNotExpandableException When a field is removed from the table's key
     *             (in the case of composite keys) several records may have to be removed
     *             from the table, in order to ensure key uniqueness. This is transparent
     *             to the user. However if the table is not record expandable and the addition
     *             of this field to the table's key requires the removal of some records, then
     *             this exception will be thrown.
     *  @exception AttributeLockedException If field's <code>key</code> attribute is locked.
     *  @see #recordAdditionAllowed
     */
    //removeFromKey(String)
    public void removeFromKey(String fieldName) throws FieldIsNotInKeyException,
            InvalidFieldNameException, TableNotExpandableException, AttributeLockedException {
        if (!keyChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg90"));

        int fieldIndex;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {
            e.printStackTrace();
            throw new InvalidFieldNameException(bundle.getString("CTableMsg65") + fieldName + bundle.getString("CTableMsg66"));
        }

        AbstractTableField f = tableFields.get(fieldIndex);
        tableKey.removeKeyField(f);
/*        if (!f.isFieldKeyAttributeChangeAllowed())
            throw new AttributeLockedException(bundle.getString("CTableFieldMsg1"));

        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

        if (!f.isKey())
            throw new FieldIsNotInKeyException(bundle.getString("CTableMsg100") + f.getName() + bundle.getString("CTableMsg71"));
        else{
            if (keyFieldIndices.size() == 1) {
                keyFieldIndices.clear();
                primaryKeyMap.clear();
                primaryKeyMap = null;
                hasKey = false;
*/                /* No exception should be thrown here, because only "setKey(true)" can
                * fire an exception.
                */
/*                try{
                    f.setKey(false);
                }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table removeFromKey() : (1)");}
                setModified();
            }else{

                //Keep this back-up copy of primaryKeyMap and keyFieldIndices
                Hashtable primaryKeyMapBackUp = (Hashtable) primaryKeyMap.clone();
                IntBaseArray keyFieldIndicesBackUp = (IntBaseArray) keyFieldIndices.clone();
*/
                /* No exception should be thrown here, because only "setKey(true)" can
                * fire an exception.
                */
/*                try{
                    f.setKey(false);
                }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table removeFromKey() : (2)");}
                setModified();
                if (keyFieldIndices.get(0) == fieldIndex) {
//                    System.out.println("Removed from keyFieldIndices: " + keyFieldIndices.remove(new Integer(fieldIndex)) + " elements");
                    keyFieldIndices.remove(fieldIndex);

                    primaryKeyMap.clear();
                    Object keyValue;
                    Integer dublicateValueRecordIndex;
                    int i = 0;
                    while (i<recordCount) {
                        keyValue = riskyGetCell(keyFieldIndices.get(0), i);
*/
//*                        System.out.println("Processing record #" + i + " with keyValue: " + keyValue);
//*                        System.out.println("Count: " + primaryKeyMap.count(keyValue));
/*                        if (primaryKeyMap.count(new Integer(keyValue.hashCode())) == 0) {
                            primaryKeyMap.add(new Integer(keyValue.hashCode()), new Integer(i)); //2.0.1 riskyGetRecord(i));
                        }else{
                            int newPrimaryKeyFieldIndex = keyFieldIndices.get(0);
                            Enumeration e = primaryKeyMap.values(new Integer(keyValue.hashCode()));
//                            if (e.hasMoreElements())
//                                e.nextElement();
                            //e contains all the records with the same keyValue as the current one
                            boolean recordRemoved = false;
                            while (e.hasMoreElements()) {
                                boolean different = false;
                                int recIndex = ((Integer) e.nextElement()).intValue(); //2.0.1 ((Record) e.nextElement()).getIndex();
*/
//*                                System.out.println("Checking record #" + recIndex);
/*                                Object secondaryKeyValue;
                                Object secondaryKeyValue2;
                                for (int k=0; k<keyFieldIndices.size(); k++) {
                                    secondaryKeyValue = riskyGetCell(keyFieldIndices.get(k), i);
                                    secondaryKeyValue2 = riskyGetCell(keyFieldIndices.get(k), recIndex);
*/
//*                                    System.out.println("Comparing " + secondaryKeyValue + " to " + secondaryKeyValue2 + " : "
//*                                    + secondaryKeyValue.equals(secondaryKeyValue2));
/*                                    if (!secondaryKeyValue.equals(secondaryKeyValue2)) {
                                        different = different || true;
                                        break;
                                    }
                                }
                                //Remove from the table any record that has the same key values as this one
                                if (!different) {
                                    hasKey = false; //Trick in order to succesfully call removeRecord()
*/
//*                                    System.out.println("Removing record index: " + i);
/*                                    try{
                                        removeRecord(i, -1, true);
                                    }catch (TableNotExpandableException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (3)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        throw new TableNotExpandableException(bundle.getString("CTableMsg70"));
                                    }
                                    catch (InvalidRecordIndexException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (4)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        System.out.println("Inconsistensy error: " + ex.message);
                                    }
                                    hasKey = true;
                                    recordRemoved = true;
                                    i--;
                                    break;
                                }
                                if (!recordRemoved) {
                                    //Add this record's primary key value to the primaryKeyMap
                                    primaryKeyMap.add(new Integer(keyValue.hashCode()), new Integer(i)); //2.0.1 riskyGetRecord(i));
                                }
                            }
                        }
                        i++;
                    }//first while
                }else{ //if the key field being removed is not the primary key field
//                    System.out.println("Removed from keyFieldIndices: " + keyFieldIndices.remove(fieldIndex) + " elements");
                    keyFieldIndices.remove(fieldIndex);

                    Integer keyValueHashCode;
                    Integer dublicateValueRecordIndex;
//                    OrderedMap temp = new OrderedMap(true);
//                    Hashtable temp = new Hashtable(new EqualTo(), true, 10, (float) 1.0);
//                    primaryKeyMap.swap(temp);
                    Hashtable temp = primaryKeyMap;
                    primaryKeyMap = new Hashtable(new EqualTo(), true, 10, (float) 1.0);
//                    Enumeration e1 = temp.elements();
                    HashMapIterator iter1 = temp.begin();
                    while (iter1.hasMoreElements()) {
                        keyValueHashCode = new Integer(iter1.key().hashCode());
//2.0.1                        Record rec = (Record) iter1.value();
*/
//*                        System.out.println("Processing record #" + rec.getIndex() + " keyValue: " + iter1.key());
//2.0.1                        int i = rec.getIndex();
/*                        Integer i = (Integer) iter1.value(); //2.0.1 rec.getIndex();
                        if (temp.count(keyValueHashCode) <= 1) {
                            primaryKeyMap.add(keyValueHashCode, i); //2.0.1 rec);
                            temp.remove(keyValueHashCode);
                            iter1.advance();
                        }else{
                            int newPrimaryKeyFieldIndex = keyFieldIndices.get(0);
                            Enumeration e = temp.values(keyValueHashCode);
                            if (e.hasMoreElements())
                                e.nextElement();
                            //e contains all the records with the same keyValue as the current one
//                            boolean recordRemoved = false;
                            while (e.hasMoreElements()) {
                                boolean different = false;
                                int recIndex = ((Integer) e.nextElement()).intValue(); //((Record) e.nextElement()).getIndex();
//*                                System.out.println("Checking record #" + recIndex);
                                Object secondaryKeyValue;
                                Object secondaryKeyValue2;
                                for (int k=0; k<keyFieldIndices.size(); k++) {
                                    secondaryKeyValue = riskyGetCell(keyFieldIndices.get(k), i.intValue());
                                    secondaryKeyValue2 = riskyGetCell(keyFieldIndices.get(k), recIndex);
//*                                    System.out.println("Comparing " + secondaryKeyValue + " to " + secondaryKeyValue2 + " : "
//*                                    + secondaryKeyValue.equals(secondaryKeyValue2));
                                    if (!secondaryKeyValue.equals(secondaryKeyValue2)) {
                                        different = different || true;
                                        break;
                                    }
                                }
                                //Remove from the table any record that has the same key values as this one
                                if (!different) {
                                    hasKey = false; //Trick in order to succesfully call removeRecord()
*/
//*                                    System.out.println("Removing record index: " + recIndex);
/*                                    try{
                                        removeRecord(recIndex, -1, true);
                                    }catch (TableNotExpandableException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (5)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear();
                                        temp.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        throw new TableNotExpandableException(bundle.getString("CTableMsg70"));
                                    }
                                    catch (InvalidRecordIndexException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (6)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear(); temp.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        System.out.println("Inconsistensy error: " + ex.message);
                                    }
                                    hasKey = true;
                                }else
                                    primaryKeyMap.add(keyValueHashCode, new Integer(recIndex)); //2.0.1 riskyGetRecord(recIndex));
                            }//second while
                            primaryKeyMap.add(keyValueHashCode, i); //2.0.1 rec);
                            temp.remove(keyValueHashCode);
                            iter1 = temp.begin();
                        }
                    }//first while
                }
            }
*/
            /* Adjust the active record.
            */
/*            if (activeRecord != -1) {
                if (activeRecord > recordCount) {
                    setActiveRecord(recordCount);
                }else{
                    if (activeRecord + 1 <= recordCount) {
                        setActiveRecord(activeRecord+1);
                    }else{
                        setActiveRecord(activeRecord);
                    }
                }
            }
            setModified();
            fireColumnKeyChanged(f, false);
        }
*/
    }


    /**
     *  Decrements the indices of the some of the key fields in <code>keyFieldIndices</code> Array.
     *  @param index Every field's index which is greater than <code>index</code> will be reduced
     *         by 1.
     */
    //decrementRestKeyFieldIndices(int)
    //Decrements the field index of all the entries in the "keyFieldIndices" Array
    //after the specified index
/*    private void decrementRestKeyFieldIndices(int index) {
        for (int i=index; i<keyFieldIndices.size(); i++)
            keyFieldIndices.set(i, (keyFieldIndices.get(i)-1));
    }
*/

    /**
     *  Changes a field's data type. This directly affects the data of the field and may result
     *  to loss of data. What actually happens when a field's data tyoe changes, is that the field
     *  is replaced by a new field of the new data type. The data of the old field are copied to the new
     *  field. The method returns the new field.
     *  @param fieldName The name of the field whose type will be changed.
     *  @param newDataType One of <code>integer</code>, <code>double</code>, <code>string</code>, <code>boolean</code>, <code>url</code>, <code>date</code>, <code>time</code>, <code>image</code> and <code>sound</code>.
     *  @param permitDataLoss <code>true</code>, if data loss is permitted. <code>false</code>, if the operation should
     *                        fail, in case data loss occurs.
     *  @return The new field.
     *  @exception InvalidFieldNameException If no field with the specified name exists in the table.
     *  @exception InvalidFieldTypeException If the supplied <code>type</code> is invalid.
     *  @exception FieldIsKeyException If the specified field is part of the table's key.
     *  @exception InvalidTypeConversionException If the conversion from the field's data type to the specified <code>type</code> is not supported.
     *  @exception FieldNotEditableException If the field is not editable.
     *  @exception DataLossException If changing the data type of the field leads to data loss.
     *  @exception DependingCalcFieldsException If an attempt is made to change the data type of a field, on
     *             which calculated fields depend.
     *  @exception AttributeLockedException If the field's data type is locked.
     *  @see AbstractTableField#isEditable()
     */
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
//        if (newDataType.isAssignableFrom(f.getDataType()))
//            throw new InvalidTypeConversionException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg78"));

        Object value;
        Class[] cl = new Class[1];
        Object[] val = new Object[1];
        Constructor con;
        AbstractTableField newField = AbstractTableField.createNewField(f.getName(), newDataType, f.isEditable, f.isRemovable, f.isHidden);
        newField.setTable(this);
        newField.indexInTable = fieldIndex;
//        ArrayList transformedField = new ArrayList();
//        transformedField.ensureCapacity(((ArrayList) tableData.get(fieldIndex)).size());

//String
        if (f.getDataType().equals(StringTableField.DATA_TYPE)) {
            StringTableField stringField = (StringTableField) f;
//t            if (fieldType.getName().equals("java.util.Date")) {
//t                if (date) {
            if (newDataType.equals(DateTableField.DATA_TYPE)) {
                CDate d = new CDate();
                for (int i=0; i<recordCount; i++) {
                    value = f.getObjectAt(i);
                    if (value == null)
                        d =null;
                    else{
                        try{
                            d = new CDate(dateFormat.parse(((String) value), new ParsePosition(0)));
                            calendar.setTime(d);
                            calendar.add(Calendar.HOUR, 10);
                            d = new CDate(calendar.getTime());
                        }catch (Exception e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else{
                                newField.set(i, null);
                                continue;
                            }
                        }
                    }
                    newField.set(i, d);
                }
                tableFields.set(fieldIndex, newField);
                setModified();
//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(TimeTableField.DATA_TYPE)) {
                TimeTableField newTimeField = (TimeTableField) newField;
                CTime d = new CTime();
                for (int i=0; i<recordCount; i++) {
                    String cellValue = stringField.get(i);
                    if (cellValue == null)
                        d = null;
                    else{
                        try{
                            d = new CTime(timeFormat.parse(cellValue, new ParsePosition(0)));
                        }catch (Exception e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else{
                                newTimeField.set(i, null);
                                continue;
                            }
                        }
                    }
                    newTimeField.set(i, d);
                }
                tableFields.set(fieldIndex, newField);
                setModified();
//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(DoubleTableField.DATA_TYPE)) {
                DoubleTableField doubleNewField = (DoubleTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    String cellValue = stringField.get(i);
                    if (cellValue == null)
                        doubleNewField.set(i, doubleNewField.NULL);
                    else{
                        try {
//                            value = new Double(numberFormat.parse((String) value).doubleValue());
                            doubleNewField.set(i, numberFormat.parse(cellValue));
//                          System.out.println(transformedField.at(i) + " --- " + transformedField.at(i).getClass().getName());
                        }catch (ParseException e) {
//                        }catch (DoubleNumberFormatParseException e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else{
                                doubleNewField.set(i, doubleNewField.NULL);
                            }
                        }
                    }
                } //for
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(FloatTableField.DATA_TYPE)) {
                FloatTableField floatNewField = (FloatTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    String cellValue = stringField.get(i);
                    if (cellValue == null)
                        floatNewField.set(i, floatNewField.NULL);
                    else{
                        try {
//                            value = new Double(numberFormat.parse((String) value).doubleValue());
                            floatNewField.set(i, numberFormat.parse(cellValue).floatValue());
//                          System.out.println(transformedField.at(i) + " --- " + transformedField.at(i).getClass().getName());
                        }catch (ParseException e) {
//                        }catch (DoubleNumberFormatParseException e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else{
                                floatNewField.set(i, floatNewField.NULL);
                            }
                        }
                    }
                } //for
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else{
                for (int i=0; i<recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value == null)
                        newField.set(i, null);
                    else{
                        //If the "value"'s class is different from the field's class then:
                        //* get the field's and the value's Class,
                        //* get a constructor of the field's Class that takes one
                        //  parameter of the Class of the "value", if one exists,
                        //* call the constructor, with the "value" object as argument and
                        //  insert the returned value to the field.
                        cl[0] = value.getClass();
                        try {
                            con = newDataType.getConstructor(cl);
                            val[0] = value;
                            try {
                                newField.set(i, con.newInstance(val));
//                                System.out.println(transformedField.at(i) + " --- " + transformedField.at(i).getClass().getName());
                            }catch (InvocationTargetException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    newField.set(i, null);
                            }
                            catch (InstantiationException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    newField.set(i, null);
                            }
                            catch (IllegalAccessException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    newField.set(i, null);
                            }
                            catch (UnableToCreateImageIconException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    newField.set(i, null);
                            }
                        }catch (NoSuchMethodException e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else
                                newField.set(i, null);
                        }
                    }
                }//for
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();
//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }
//Integer
        }else if (f.getDataType().equals(IntegerTableField.DATA_TYPE)) {
            IntegerTableField intField = (IntegerTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    int cellValue = intField.get(i);
                    if (cellValue == IntegerTableField.NULL)
                        newStringField.set(i, null);
                    else
                        newStringField.set(i, String.valueOf(cellValue));
/*                    value = riskyGetCell(fieldIndex, i);
                    if (value != null)
                        newField.set(i, value.toString());
                    else
                        newField.set(i, null);
*/
                }
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(DoubleTableField.DATA_TYPE)) {
                DoubleTableField newDoubleField = (DoubleTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    int cellValue = intField.get(i);
                    if (intField.isNull(cellValue))
                        newDoubleField.set(i, newDoubleField.NULL);
                    else
                        newDoubleField.set(i, new Integer(cellValue).doubleValue());
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null) {
                        ((ArrayList) tableData.get(fieldIndex)).set(i, new Double(((Integer) ((ArrayList) tableData.get(fieldIndex)).get(i)).doubleValue()));
                    }else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(FloatTableField.DATA_TYPE)) {
                FloatTableField newFloatField = (FloatTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    int cellValue = intField.get(i);
                    if (intField.isNull(cellValue))
                        newFloatField.set(i, newFloatField.NULL);
                    else
                        newFloatField.set(i, new Integer(cellValue).floatValue());
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null) {
                        ((ArrayList) tableData.get(fieldIndex)).set(i, new Double(((Integer) ((ArrayList) tableData.get(fieldIndex)).get(i)).doubleValue()));
                    }else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(BooleanTableField.DATA_TYPE)) {
                BooleanTableField newBooleanField = (BooleanTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    int cellValue = intField.get(i);
                    if (intField.isNull(cellValue))
                        newBooleanField.set(i, null);
                    else{
                        if (cellValue == 0)
                            newBooleanField.set(i, Boolean.FALSE);
                        else
                            newBooleanField.set(i, Boolean.TRUE);
                    }
                }
/*                    if (value != null) {
                        if (((Integer) value).intValue() == 0)
                            transformedField.add(new Boolean(false));
                        else
                            transformedField.add(new Boolean(true));
                    }else
                        transformedField.add(null);
                }
                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg80") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Double
        }else if (f.getDataType().equals(DoubleTableField.DATA_TYPE)) {
            DoubleTableField doubleField = (DoubleTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    double cellValue = doubleField.get(i);
                    if (!doubleField.isNull(cellValue))
                        newStringField.set(i, numberFormat.format(new Double(cellValue)));
                    else
                        newStringField.set(i, null);
                }
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(IntegerTableField.DATA_TYPE)) {
                IntegerTableField newIntegerField = (IntegerTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    double cellValue = doubleField.get(i);
                    if (doubleField.isNull(cellValue))
                        newIntegerField.set(i, newIntegerField.NULL);
                    else{
                        try{
                            newIntegerField.set(i, new Double(cellValue).intValue());
                        }catch (Exception e) {
                            // Very big double values cannot be represented as integers.
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else
                                newIntegerField.set(i, newIntegerField.NULL);
                        }
                    }
                }
                tableFields.set(fieldIndex, newField);
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null) {
                        try{
                            ((ArrayList) tableData.get(fieldIndex)).set(i, new Integer(numberFormat.format((Double) ((ArrayList) tableData.get(fieldIndex)).get(i)))); //((Double) ((ArrayList) tableData.get(fieldIndex)).at(i)).intValue()));
                        }catch (Exception e) {
                            // Very big double values cannot be represented as integers.
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else
                                transformedField.add(null);
                        }
                    }else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
                }
*/
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(BooleanTableField.DATA_TYPE)) {
                BooleanTableField newBooleanField = (BooleanTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    double cellValue = doubleField.get(i);
                    if (doubleField.isNull(cellValue))
                        newBooleanField.set(i, null);
                    else{
                        if (cellValue == 0)
                            newBooleanField.set(i, Boolean.FALSE);
                        else
                            newBooleanField.set(i, Boolean.TRUE);
                    }
/*                    if (value != null) {
                        if (((Double) value).doubleValue() == 0)
                            transformedField.add(new Boolean(false));
                        else
                            transformedField.add(new Boolean(true));
                    }else
                        transformedField.add(null);
*/
                }
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
//                ((ArrayList) tableData.get(fieldIndex)).copy(transformedField);
                tableData.set(fieldIndex, transformedField.clone());
*/
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg95") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Float
        }else if (f.getDataType().equals(FloatTableField.DATA_TYPE)) {
            FloatTableField floatField = (FloatTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    float cellValue = floatField.get(i);
                    if (!floatField.isNull(cellValue))
                        newStringField.set(i, numberFormat.format(new Double(cellValue)));
                    else
                        newStringField.set(i, null);
                }
                tableFields.set(fieldIndex, newField);
                setModified();
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(IntegerTableField.DATA_TYPE)) {
                IntegerTableField newIntegerField = (IntegerTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    float cellValue = floatField.get(i);
                    if (floatField.isNull(cellValue))
                        newIntegerField.set(i, newIntegerField.NULL);
                    else{
                        try{
                            newIntegerField.set(i, new Float(cellValue).intValue());
                        }catch (Exception e) {
                            // Very big double values cannot be represented as integers.
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else
                                newIntegerField.set(i, newIntegerField.NULL);
                        }
                    }
                }
                tableFields.set(fieldIndex, newField);
                setModified();
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(BooleanTableField.DATA_TYPE)) {
                BooleanTableField newBooleanField = (BooleanTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    float cellValue = floatField.get(i);
                    if (floatField.isNull(cellValue))
                        newBooleanField.set(i, null);
                    else{
                        if (cellValue == 0)
                            newBooleanField.set(i, Boolean.FALSE);
                        else
                            newBooleanField.set(i, Boolean.TRUE);
                    }
                }
                tableFields.set(fieldIndex, newField);
                setModified();
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg95") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Boolean
        }else if (f.getDataType().equals(BooleanTableField.DATA_TYPE)) {
            BooleanTableField booleanField = (BooleanTableField) f;
//            System.out.println(fieldType.getName().equals("java.lang.Integer") + "  " + fieldName);
            if (newDataType.equals(IntegerTableField.DATA_TYPE)) {
                IntegerTableField newIntegerField = (IntegerTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    Boolean cellValue = booleanField.get(i);
                    if (cellValue == null)
                        newIntegerField.set(i, newIntegerField.NULL);
                    else{
                        if (cellValue.booleanValue())
                            newIntegerField.set(i, 1);
                        else
                            newIntegerField.set(i, 0);
                    }
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null) {
                        if (((Boolean) ((ArrayList) tableData.get(fieldIndex)).get(i)).booleanValue())
                            ((ArrayList) tableData.get(fieldIndex)).set(i, new Integer(1));
                        else
                            ((ArrayList) tableData.get(fieldIndex)).set(i, new Integer(0));
                    }else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(DoubleTableField.DATA_TYPE)) {
                DoubleTableField newDoubleField = (DoubleTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    Boolean cellValue = booleanField.get(i);
                    if (cellValue == null)
                        newDoubleField.set(i, newDoubleField.NULL);
                    else{
                        if (cellValue.booleanValue())
                            newDoubleField.set(i, 1);
                        else
                            newDoubleField.set(i, 0);
                    }
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null) {
                        if (((Boolean) ((ArrayList) tableData.get(fieldIndex)).get(i)).booleanValue())
                            ((ArrayList) tableData.get(fieldIndex)).set(i, new Double(1));
                        else
                            ((ArrayList) tableData.get(fieldIndex)).set(i, new Double(0));
                    }else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                setModified();
                tableFields.set(fieldIndex, newField);
//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(FloatTableField.DATA_TYPE)) {
                FloatTableField newFloatField = (FloatTableField) newField;
                for (int i=0; i<recordCount; i++) {
                    Boolean cellValue = booleanField.get(i);
                    if (cellValue == null)
                        newFloatField.set(i, newFloatField.NULL);
                    else{
                        if (cellValue.booleanValue())
                            newFloatField.set(i, 1);
                        else
                            newFloatField.set(i, 0);
                    }
                }
                setModified();
                tableFields.set(fieldIndex, newField);
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
                String trueStr = bundle.getString("true");
                String falseStr = bundle.getString("false");
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    Boolean cellValue = booleanField.get(i);
                    if (cellValue == null)
                        newStringField.set(i, null);
                    else{
                        if (cellValue.booleanValue())
                            newStringField.set(i, trueStr);
                        else
                            newStringField.set(i, falseStr);
                    }
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null)
                        ((ArrayList) tableData.get(fieldIndex)).set(i, ((Boolean) ((ArrayList) tableData.get(fieldIndex)).get(i)).toString());
                    else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg96") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//URL
        }else if (f.getDataType().equals(URLTableField.DATA_TYPE)) {
            URLTableField urlField = (URLTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    URL url = urlField.get(i);
                    if (url == null)
                        newStringField.set(i, null);
                    else
                        newStringField.set(i, url.toString());
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null)
                        ((ArrayList) tableData.get(fieldIndex)).set(i, ((java.net.URL) ((ArrayList) tableData.get(fieldIndex)).get(i)).toString());
                    else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg97") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Date
//t        }else if (f.getFieldType().getName().equals("java.util.Date")) {
//t            if (f.isDate()) {  //Date
        }else if (f.getDataType().equals(DateTableField.DATA_TYPE)) {
            DateTableField dateField = (DateTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
//                    sdf2.setLenient(false);
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    CDate cellValue = dateField.get(i);
                    if (cellValue == null)
                        newStringField.set(i, null);
                    else
                        newStringField.set(i, dateFormat.format(cellValue));
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null)
                        ((ArrayList) tableData.get(fieldIndex)).set(i, dateFormat.format(((Date) ((ArrayList) tableData.get(fieldIndex)).get(i))));
                    else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
//                f.setDate(false);
                tableFields.set(fieldIndex, newField);
                setModified();

//t                    if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg94") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Time
        }else if (f.getDataType().equals(TimeTableField.DATA_TYPE)) {//Time
            TimeTableField timeField = (TimeTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    CTime cellValue = timeField.get(i);
                    if (cellValue == null)
                        newStringField.set(i, null);
                    else
                        newStringField.set(i, timeFormat.format(cellValue));
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null)
                        ((ArrayList) tableData.get(fieldIndex)).set(i, timeFormat.format(((Date) ((ArrayList) tableData.get(fieldIndex)).get(i))));
                    else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
//                f.setDate(false);
                tableFields.set(fieldIndex, newField);
                setModified();

//t                    if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg94") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Image
        }else if (f.getDataType().equals(ImageTableField.DATA_TYPE)) {
            ImageTableField imageField = (ImageTableField) f;
            if (newDataType.equals(StringTableField.DATA_TYPE)) {
                StringTableField newStringField = (StringTableField) newField;
/*                try{
                    f = this.changeFieldDataType(f, newDataType);
//                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in Table _changeFieldType: (1)");
                    return;
                }
*/
                for (int i=0; i<recordCount; i++) {
                    CImageIcon cellValue = imageField.get(i);
                    if (cellValue == null)
                        newStringField.set(i, null);
                    else
                        newStringField.set(i, cellValue.getFileName());
/*                    if (((ArrayList) tableData.get(fieldIndex)).get(i) != null)
                        ((ArrayList) tableData.get(fieldIndex)).set(i, ((CImageIcon) ((ArrayList) tableData.get(fieldIndex)).get(i)).getFileName());
                    else
                        ((ArrayList) tableData.get(fieldIndex)).set(i, null);
*/
                }
                tableFields.set(fieldIndex, newField);
                setModified();

//t                if (database != null)
                fireColumnTypeChanged(f, newDataType);
                return newField;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg97") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
//Everything else: Sound ...?
        }else
            throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg94") + AbstractTableField.localizedNameForDataType(newDataType) + "\"");
    }


    /**
     *  Returns the data of the Table in the form of an Array of Arrays.
     */
/*    protected ArrayList getTableData() {
        return tableData;
    }
*/

    /**
     *  Returns an array with the names of the fields of the Table.
     */
    public StringBaseArray getFieldNames() {
        int numOfFields = tableFields.size();
        StringBaseArray names = new StringBaseArray(numOfFields);
        for (int i=0; i<numOfFields; i++)
            names.add(tableFields.get(i).getName());

        return names;
    }

    /**
     *  Returns the <code>title</code> of the Table.
     */
    public String getTitle() {
        return title;
    }

    /**
     *  Sets the title of the Table. If the supplied <code>title</code> is <code>null</code>, an
     *  InvalidTitleException is thrown. Every time the table's title changes, a
     *  VetoableChangeEvent is fired, which gives the opportunity to any one interested
     *  to veto the change of the title. This is useful when the table belongs to one
     *  or more BDases, where each table must have a unique name. Therefore the containing
     *  DBase(s) have the chance veto the change of table's name, in order to assure
     *  name uniqueness for all the tables they contain. An PropertyVetoException is
     *  thrown when the change is vetoed. The name of the property for which the
     *  VetoableChangeEvent is fired is "Table name". If the title of the table
     *  changes, a TableRenamedEvent is fired.
     *  @param title The new title.
     *  @exception InvalidTitleException Thrown, when the new non-null title, is already acquired by
     *             another table in the database.
     *  @exception RuntimeException Thrown, when the name of the Table is not editable.
     *             This is determined by the <code>tableRenamingAllowed</code> flag.
     */
    public void setTitle(String title) throws InvalidTitleException, PropertyVetoException {
        if (title == null)
            throw new InvalidTitleException(bundle.getString("CTableMsg102"));

        if (title != null && getTitle() != null && title.equals(getTitle()))
            return;
        if (!tableRenamingAllowed)
            throw new RuntimeException(bundle.getString("CTableMsg104"));


/*        if (title != null) {
boolean sameTitleFound = false;
if (database != null) {
Array databaseTablesTitles = database.getTableTitles();
for (int i =0; i<databaseTablesTitles.size(); i++) {
if (((String) databaseTablesTitles.at(i)).equals(title)) {
sameTitleFound = true;
break;
}
}
}
if (sameTitleFound)
throw new InvalidTitleException(bundle.getString("CTableMsg84") + title + bundle.getString("CTableMsg85"));
}
*/
        PropertyChangeEvent event = new PropertyChangeEvent(this,
                "Table name",
                getTitle(),
                title);
//System.out.println("Firing veto event");
        tableVetoableChangeSupport.fireVetoableChange(event);
//System.out.println("After veto event");
        //No one vetoed, so go ahead and make the change
        String oldTitle = getTitle();
        this.title = title;
        setModified();

//t        if (database != null)
        fireTableRenamed(oldTitle); //database.CTables.indexOf(this), oldTitle, title));
        setTableHandleNameToTitle(oldTitle, true);
    }

    void setTableHandleNameToTitle(String oldTitle, boolean fireTableRenamnedEvent) {
        if (handle != null) {
            if (!handle.getComponentName().equals(title))
                try{
//System.out.println("Table setTableHandleNameToTitle() title: " + title);
//System.out.println("--- readExternal() 1.1.1.1 : " + tableTimer.lapse());
                    handle.setComponentName(title);
//System.out.println("--- readExternal() 1.1.1.2 : " + tableTimer.lapse());
                }catch (RenamingForbiddenException exc) {
                    exc.printStackTrace();
                    return;
                }catch (NameUsedException exc) {}
            /* The Table title and its handle's name have to be the same.
            * 'setUniqueComponentName()' may have set a slighlty different title
            * from the one requested. If this is the case, then we set againg the
            * title of the Table.
            */
            if (!handle.getComponentName().equals(title)) {
                if (fireTableRenamnedEvent) {
                    try{
                        setTitle(handle.getComponentName());
                    }catch (PropertyVetoException exc) {
                        ESlateOptionPane.showMessageDialog(null, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        try{
                            if (oldTitle != null)
                                handle.setUniqueComponentName(oldTitle);
                            System.out.println("1. Called setUniqueComponentName");
                        }catch (RenamingForbiddenException exc1) {
                            System.out .println("Inconsistency between the title of the Table + " + title + " and the name of its ESlateHandle");
                        }
                    }catch (InvalidTitleException exc) {
                        ESlateOptionPane.showMessageDialog(null, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        try{
                            if (oldTitle != null)
                                handle.setUniqueComponentName(oldTitle);
                            System.out.println("2. Called setUniqueComponentName");
                        }catch (RenamingForbiddenException exc1) {
                            System.out .println("Inconsistency between the title of the Table + " + title + " and the name of its ESlateHandle");
                        }
                    }catch (Throwable exc) {
                        ESlateOptionPane.showMessageDialog(null, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        try{
                            if (oldTitle != null)
                                handle.setUniqueComponentName(oldTitle);
                            System.out.println("3. Called setUniqueComponentName");
                        }catch (RenamingForbiddenException exc1) {
                            System.out .println("Inconsistency between the title of the Table + " + title + " and the name of its ESlateHandle");
                        }
                    }
                }else
                    _setTitle(handle.getComponentName());
            }
        }
    }

    protected void _setTitle(String title) {
        String oldTitle = this.title;
//System.out.println("Table _setTitle() title: " + title + ", oldTitle: " + oldTitle);
        this.title = title;
        setTableHandleNameToTitle(oldTitle, false);
        if (tablePlug != null) {
            try{
                tablePlug.setName(title);
                tablePlug.setNameLocaleIndependent(title);
            }catch (Throwable thr) {
                thr.printStackTrace();
            }
        }
    }


    /**
     *  Get the metadata for the Table.
     */
    public String getMetadata() {
        return metadata;
    }


    /**
     *  Sets the Table's metadata.
     */
    public void setMetadata(String m) {
        if (m == null && this.metadata != null) {
            this.metadata = "";
            setModified();
        }
        if (m == this.metadata) //null case
            return;
        if (m != null && this.metadata != null && !m.equals(this.metadata)) {
            this.metadata = m;
            setModified();
        }
        if (m != null && this.metadata == null) {
            this.metadata = m;
            setModified();
        }
    }


    /**
     *  Returns the field at <code>fieldIndex</code>.
     *  @param fieldIndex The index of the field.
     *  @exception InvalidFieldIndexException If <code>fieldIndex<0</code> or <code>fieldIndex>#Fields</code>.
     */
    public AbstractTableField getTableField(int fieldIndex) throws InvalidFieldIndexException {
        if (fieldIndex < tableFields.size() && fieldIndex >= 0)
            return tableFields.get(fieldIndex);
        else
            throw new InvalidFieldIndexException(bundle.getString("CTableMsg51") + fieldIndex);
    }

    /**
     *  Returns the field named <code>fieldName</code>.
     *  @param fieldName The name of the field.
     *  @exception InvalidFieldNameException If no field with the specified <code>fieldName</code> exists in the table.
     */
    public AbstractTableField getTableField(String fieldName) throws InvalidFieldNameException {
        if (fieldName == null || fieldName.trim().length() == 0)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg81") + fieldName + "\"");

        AbstractTableField f = null;
        for (int i=0; i<tableFields.size(); i++) {
            f = tableFields.get(i);
            if (f.getName().equals(fieldName)) {
                return f;
            }
        }
        throw new InvalidFieldNameException(bundle.getString("CTableMsg81") + fieldName + "\"");
    }

    /**
     *  Renames a field.
     *  @param existingName The current name of the field to be renamed.
     *  @param newName The new name.
     *  @exception InvalidFieldNameException If no field with the specified <code>fieldName</code> exists in the table.
     *  @exception FieldNameInUseException If the <code>newName</code> is already used by another field in the table.
     */
    public void renameField(String existingName, String newName)
            throws InvalidFieldNameException, FieldNameInUseException {
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

        AbstractTableField f;

        try{
            f = getTableField(existingName);
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(e.message);}
//        System.out.println(f.getName() + ", " + f.getFieldType() + ", " + f.isCalculated() + ", " + f.isEditable());
        /* Check if the "newName" is already used by another field in the
        * same table.
        */
        if (getFieldNames().indexOf(newName) != -1)
            throw new FieldNameInUseException(bundle.getString("CTableMsg82") + newName + bundle.getString("CTableMsg83"));

        setModified();
        String oldName = f.getName();
        f.setName(newName);

        /* Change the name of this field in the textual formulas of all the calculated fields,
        * which depend on it.
        */
        oldName = '[' + oldName + ']';
        IntBaseArray dependentFieldIndices = f.getDependentCalcFields();
        AbstractTableField f1 = null;
        String textFormula;
        for (int i=0; i<dependentFieldIndices.size(); i++) {
            try{
                f1 = getTableField(dependentFieldIndices.get(i));
            }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in Table renameField() : (1)"); continue;}

            textFormula = f1.textFormula;
            int index = 0;
            while (true) {
                if (index >= textFormula.length() || (index = textFormula.indexOf(oldName, index)) == -1)
                    break;
//                System.out.println("textFormula before: " + textFormula);
                textFormula = textFormula.substring(0, index) + '[' + newName + ']' + textFormula.substring(index + oldName.length());
//                System.out.println("textFormula after: " + textFormula);
                index++;
            }
//            System.out.println("Setting textFormula to: " + textFormula);
            f1.textFormula = textFormula;
        }

        int fieldIndex = -1;
        try{
            fieldIndex = getFieldIndex(newName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in Table renameField: (2)");}
//t        if (database != null)
        fireColumnRenamed(f, existingName); //database.CTables.indexOf(this), existingName, newName));
    }


    /** Specifies if the field's data is editable or not.
     *  @return <code>true</code>, if the operation succeeds.
     *  @exception InvalidFieldNameException If the supplied field name is not found in the Table.
     *  @exception AttributeLockedException If the field's <code>Editable</code> attribute is locked.
     */
    public boolean setFieldEditable(String fieldName, boolean isEditable)
            throws InvalidFieldNameException, AttributeLockedException {
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

        AbstractTableField f;
        f = getTableField(fieldName);

        if (f.isEditable() == isEditable)
            return false;
        else{
            if (f.setEditable(isEditable)) {
                setModified();
//t                if (this.database != null)
                fireColumnEditableStateChanged(f);
                return true;
            }else
                return false;
        }
    }


    /** Specifies if the field's data is removable or not.
     *  @exception InvalidFieldNameException If the supplied field name is not found in the Table.
     *  @exception AttributeLockedException If the field's <code>Removable</code> attribute is locked.
     */
    public void setFieldRemovable(String fieldName, boolean isRemovable)
            throws InvalidFieldNameException, AttributeLockedException {
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

        AbstractTableField f;
        f = getTableField(fieldName);

        if (f.isRemovable() == isRemovable)
            return;
        else{
            setModified();
            f.setRemovable(isRemovable);
//t            if (this.database != null)
            fireColumnRemovableStateChanged(f); //database.CTables.indexOf(this), fieldName, isRemovable));
        }
    }


    /** Specifies if the field is hidden or not.
     *  @exception InvalidFieldNameException If the supplied field name is not found in the Table.
     *  @exception AttributeLockedException If the field's <code>Hidden</code> attribute is locked.
     */
    public void setFieldHidden(String fieldName, boolean isHidden)
            throws InvalidFieldNameException, AttributeLockedException {
        AbstractTableField f;
        f = getTableField(fieldName);
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');

        if (f.isHidden() == isHidden)
            return;
        else{
            setModified();
            f.setHidden(isHidden);
//t            if (this.database != null)
            fireColumnHiddenStateChanged(f); //database.CTables.indexOf(this), fieldName, isHidden));
        }
    }

/* The follqwing two methods were temporarily added to the Table's API as a means
* to provide single-step setting of the visibility of all the Table's fields. It
* was introduced because of ETE and has been removed for now, so that it won't
* conflict with setting the field visibility atomically. The conflict exists in that
* this API notifies listeners through a PropertyChangeEvent, without any
* ColumnHiddenStateChangedEvent being fired.
public FieldVisibilityStruct getFieldVisibility() {
return new FieldVisibilityStruct(this);
}

public void setFieldVisibility(FieldVisibilityStruct struct) {
int fieldCount = tableFields.size();
boolean visibilityChanged = false;
for (int i=0; i<fieldCount; i++) {
TableField f = tableFields.get(i);
Boolean visible = struct.isVisible(f.getName());
if (visible != null) {
if (f.isHidden() != !visible.booleanValue()) {
try{
f.setHidden(!visible.booleanValue());
setModified();
visibilityChanged = true;
}catch (AttributeLockedException exc) {}
}
}
}
if (visibilityChanged) {
PropertyChangeEvent event = new PropertyChangeEvent(this,
"Table's field visibility changed",
Boolean.TRUE,
Boolean.FALSE);
tablePropertyChangeSupport.firePropertyChange(event);
}
}
*/
    /** Returns the names of all the currently visible fields of the Table.
     */
    public String[] getVisibleFieldNames() {
        StringBaseArray array = new StringBaseArray();
        int fieldCount = tableFields.size();
        for (int i=0; i<fieldCount; i++) {
            AbstractTableField f = tableFields.get(i);
            if (!f.isHidden())
                array.add(f.getName());
        }
        return array.toArray();
    }


    /** Specifies if a field's data is stored outside the database. In this case the
     *  database stores references to these data and not the data itself. Currently
     *  this stands only for image fields.
     */
    public void setFieldContainsLinksToExternalData(String fieldName, boolean containsLinks)
            throws InvalidFieldNameException {
        if (!fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(bundle.getString("CTableMsg103") + " \"" + getTitle() + '\"');
        AbstractTableField f;
        f  = getTableField(fieldName);

        if (!f.getDataType().equals(ImageTableField.DATA_TYPE) || f.containsLinksToExternalData() == containsLinks)
            return;
        else{
            if (containsLinks) {
                /* Check if the field contains images. If it does the warn the user that maybe
                * images or changes to images will be lost, if the field stores references instead
                * of the actual images.
                */
                CImageIconBaseArray images = (CImageIconBaseArray) getField(fieldName);
                boolean containsImages = false;
                for (int i=0; i<images.size(); i++) {
                    if (images.get(i) != null) {
                        containsImages = true;
                        break;
                    }
                }
                if (containsImages) {
                    if (!(ESlateOptionPane.showConfirmDialog(new JFrame(), bundle.getString("ImageFieldMsg1"), DBase.resources.getString("Warning"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION))
                        return;
                }
            }

            setModified();
            f.setContainsLinksToExternalData(containsLinks);
            CImageIconBaseArray images = (CImageIconBaseArray) getField(fieldName);
//            ArrayList fieldData = (ArrayList) tableData.get(tableFields.indexOf(f));
            for (int i=0; i<images.size(); i++) {
                if (images.get(i) != null)
                    images.get(i).setReferenceToExternalFile(this, f, containsLinks);
            }
        }
    }


    /**
     *  Enables or disables modification of the field's <code>Editable</code> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Field editability change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldEditabilityChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldEditabilityChangeAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isFieldEditabilityChangeAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field editability change allowed",
                    f,
                    new Boolean(allowed));
//            System.out.println("setFieldEditabilityChangeAllowed() event.getOldValue(): " + event.getOldValue());
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setFieldEditabilityChangeAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <code>Removable</code> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Field removability change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldRemovabilityChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldRemovabilityChangeAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isFieldRemovabilityChangeAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field removability change allowed",
                    f,
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setFieldRemovabilityChangeAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's data type. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Field data type change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldDataTypeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldDataTypeChangeAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isFieldDataTypeChangeAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field data type change allowed",
                    f,
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setFieldDataTypeChangeAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Enables or disables the tranformation of a calculated field to a normal one. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Calculated field reset allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setCalcFieldResetAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || !f.isCalculated() || f.isCalcFieldResetAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isCalcFieldResetAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Calculated field reset allowed",
                    f,
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setCalcFieldResetAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <code>Key</code> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Key attribute change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldKeyAttributeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldKeyAttributeChangeAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isFieldKeyAttributeChangeAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Key attribute change allowed",
                    f,
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setFieldKeyAttributeChangeAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <code>Key</code> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Field hidden attribute change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldHiddenAttributeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldHiddenAttributeChangeAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isFieldHiddenAttributeChangeAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Field hidden attribute change allowed",
                    f,
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setFieldHiddenAttributeChangeAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <code>Key</code> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <code>recordRemovalAllowed</code>. The name of the property is
     *  "Formula attribute change allowed" and the event is fired by the Table. Before the
     *  property changes it value, any registered VetoableChangeListeners have the
     *  chance to veto the change. In this case, the change will not take place.
     *  This is meaningful when the Table belongs to a DBase. In this case the DBase
     *  defines the permission rights for all the tables it contains. So if a Table
     *  receives a request to alter its permissions, the containing DBase (which can
     *  be more than one) has the chance to veto the change, if it is locked.
     *  The PropertyChangeEvent has been twicked, so that the <code>getOldValue()</code> method
     *  returns the AbstractTableField instance whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setCalcFieldFormulaChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException, PropertyVetoException {
        AbstractTableField f;
        f = getTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isCalcFieldFormulaChangeAllowed() == allowed)
return;
else{
t*/
//t        if (database != null && database.isLocked()) return;
        if (f.isCalcFieldFormulaChangeAllowed() != allowed) {
            PropertyChangeEvent event = new PropertyChangeEvent(this,
                    "Formula attribute change allowed",
                    f,
                    new Boolean(allowed));
            tableVetoableChangeSupport.fireVetoableChange(event);
            //No one vetoed, so go ahead and make the change
            setModified();
            f.setCalcFieldFormulaChangeAllowed(allowed);
//t            if (database != null) {
//t                if (this.database != null)
            tablePropertyChangeSupport.firePropertyChange(event);
//t            }
        }
    }


    /**
     *  Reports if the table contains a field named <code>fieldName</code>.
     *  @param fieldName The name of the field.
     *  @return <code>true</code>, if the field exists. <code>false</code>, otherwise.
     */
    public boolean containsField(String fieldName) {
        try{
            getField(fieldName);
        }catch (InvalidFieldNameException e) {return false;}
        return true;
    }


    /** Returns the array with the indices of the selected records of the Table.
     */
    public IntBaseArrayDesc getSelectedSubset() {
/*        Array a = new Array(selectedSubset.size())
for (int i=0; i < selectedSubset.size(); i++)
a.add(new Integer(selectedSubset.get(i));
return a;
*/
        return (IntBaseArrayDesc) selectedSubset.clone();
    }

    public IntBaseArrayDesc getUnSelectedSubset() {
/*        Array a = new Array(selectedSubset.size())
for (int i=0; i < selectedSubset.size(); i++)
a.add(new Integer(selectedSubset.get(i));
return a;
*/
        return (IntBaseArrayDesc) unselectedSubset.clone();
    }

    /** Returns the array with the indices of the selected records of the Table.
     */
/*    public IntBaseArray getSelectedSubset() {
return (Array) selectedSubset.clone();
}
*/
    /** Returns the number of selected records in the Table.
     */
    public int getSelectedRecordCount() {
        return selectedSubset.size();
    }


    /**
     *  Checks if two tables have fields with identical names.
     *  @param table The second table.
     */
    protected ArrayList equalFieldNames(Table table) {
        StringBaseArray fieldNames1 = getFieldNames();
        StringBaseArray fieldNames2 = table.getFieldNames();
        ArrayList commonFieldNames = new ArrayList();

        for (int i=0; i<fieldNames1.size(); i++) {
            String fieldName1 = fieldNames1.get(i);
            for (int j=0; j<fieldNames2.size(); j++) {
                if (fieldName1.equals(fieldNames2.get(j))) {
                    commonFieldNames.add(fieldName1);
                    break;
                }
            }
        }
/*        OrderedSet fieldNames1 = new OrderedSet( new LessString() );
        OrderedSet fieldNames2 = new OrderedSet( new LessString() );
        ArrayList commonFieldNames = new ArrayList();

        ArrayList temp = this.getFieldNames();
        for (int i=0; i<temp.size(); i++)
            fieldNames1.add((String) temp.get(i));
        temp = table.getFieldNames();
        for (int i=0; i<temp.size(); i++)
            fieldNames2.add((String) temp.get(i));

        OrderedSet commonFields = fieldNames1.intersection(fieldNames2);
        OrderedSetIterator iter = commonFields.begin();
        while (iter.hasMoreElements()) {
            commonFieldNames.add(iter.get());
            iter.advance();
        }
*/
        return commonFieldNames;
    }


    /** Returns an Array with the unique, non-null values of field <code>fieldName</code>. The
     *  values are sorted in ascending order.
     *  @param fieldName The name of the field.
     *  @param selectedRecordsOnly Specifies whether all the table's records will be searched
     *         for unique values. The search will be limited to the selected records only
     *         if the value of this parameter is true.
     */
    public final ArrayList getUniqueFieldValuesList(String fieldName, boolean selectedRecordsOnly) throws InvalidFieldNameException {
        int fieldIndex = -1;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {throw e;}

        AbstractTableField f = tableFields.get(fieldIndex);

/*        String fieldType = f.getDataType().getName();
        if (fieldType.equals("java.lang.Boolean")) {
            ArrayList result = new ArrayList(2);
            result.add(new Boolean(false));
            result.add(new Boolean(true));
            return result;
        }
        if (fieldType.equals("gr.cti.eslate.database.engine.CImageIcon"))
            return new ArrayList();

        ArrayList nonNullFieldData = new ArrayList();
//        ArrayList fieldData = (ArrayList) tableData.get(fieldIndex);
        ArrayList fieldData = f.getDataArrayList();
        nonNullFieldData.ensureCapacity(fieldData.size());

//        System.out.println("Getting rid of null...");
        if (!selectedRecordsOnly) {
            for (int i=0; i<fieldData.size(); i++) {
                if (fieldData.get(i) != null)
                    nonNullFieldData.add(fieldData.get(i));
            }
        }else{
            for (int i=0; i<fieldData.size(); i++) {
                if (isRecordSelected(i) && fieldData.get(i) != null)
                    nonNullFieldData.add(fieldData.get(i));
            }
        }

//        System.out.println("Sorting....");
        if (fieldType.equals("java.lang.Integer")) {
            ArrayListSorting.sort(nonNullFieldData, new LessNumber(java.lang.Integer.class));
        }else if (fieldType.equals("java.lang.Double")) {
            ArrayListSorting.sort(nonNullFieldData, new LessNumber(java.lang.Double.class));
        }else if (fieldType.equals("java.lang.String")) {
            ArrayListSorting.sort(nonNullFieldData, new LessString());
        }else if (fieldType.equals("java.net.URL"))
            ArrayListSorting.sort(nonNullFieldData, new LessString());
//t        else if (fieldType.equals("java.util.Date"))
        else if (fieldType.equals("gr.cti.eslate.database.engine.CDate") ||
                fieldType.equals("gr.cti.eslate.database.engine.CTime"))
            ArrayListSorting.sort(nonNullFieldData, new LessDate());
        else{
            System.out.println("Serious inconsistency error in Table getUniqueFieldValues(): (1)");
            return null;
        }

        fieldData = new ArrayList();
        int i=0, k = -1;
//        System.out.println("Removing dublicate values");
        while (i<nonNullFieldData.size()) {
            fieldData.add(nonNullFieldData.get(i));
            i++;
            k++;
            while (i<nonNullFieldData.size() && fieldData.get(k).equals(nonNullFieldData.get(i)))
                i++;
        }

//        System.out.println("Done.");
*/
        ObjectComparator comparator = f.getComparatorFor("<");
        IntBaseArray orderArray = f.getOrder(comparator);
        int dataSize = orderArray.size();
        ArrayList sortedFieldData = new ArrayList(dataSize);
        Object prevVal = null;
        for (int i=0; i<dataSize; i++) {
            if (selectedRecordsOnly && !recordSelection.get(orderArray.get(i)))
                continue;
            try{
                Object val = f.getCellObject(orderArray.get(i));
                if (val != null && !val.equals(prevVal)) {
                    sortedFieldData.add(val);
                    prevVal = val;  // Remove dublicate values
                }
            }catch (InvalidCellAddressException exc) {exc.printStackTrace();}
        }
        return sortedFieldData;
    }


    /** Returns an array with the unique, non-null values of field <code>fieldName</code>. The
     *  values are sorted in ascending order.
     *  @param fieldName The name of the field.
     *  @param selectedRecordsOnly Specifies whether all the table's records will be searched
     *         for unique values. The search will be limited to the selected records only
     *         if the value of this parameter is true.
     */
    public final Object[] getUniqueFieldValues(String fieldName, boolean selectedRecordsOnly) throws InvalidFieldNameException {
        return getUniqueFieldValuesList(fieldName, selectedRecordsOnly).toArray();
/*        int fieldIndex = -1;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {throw e;}

        AbstractTableField f = tableFields.get(fieldIndex);

        String fieldType = f.getDataType().getName();
        if (fieldType.equals("java.lang.Boolean")) {
            Object result[] = new Object[2];
            result[0] = new Boolean(false);
            result[1] = new Boolean(true);
            return result;
        }
        if (fieldType.equals("gr.cti.eslate.database.engine.CImageIcon"))
            return new Object[0];

        ArrayList nonNullFieldData = new ArrayList();
//        ArrayList fieldData = (ArrayList) tableData.get(fieldIndex);
        ArrayList fieldData = f.getDataArrayList();
        nonNullFieldData.ensureCapacity(fieldData.size());

//        System.out.println("Getting rid of null...");
        if (!selectedRecordsOnly) {
            for (int i=0; i<fieldData.size(); i++) {
                if (fieldData.get(i) != null)
                    nonNullFieldData.add(fieldData.get(i));
            }
        }else{
            for (int i=0; i<fieldData.size(); i++) {
                if (isRecordSelected(i) && fieldData.get(i) != null)
                    nonNullFieldData.add(fieldData.get(i));
            }
        }

//        System.out.println("Sorting....");
        if (fieldType.equals("java.lang.Integer")) {
            ArrayListSorting.sort(nonNullFieldData, new LessNumber(java.lang.Integer.class));
        }else if (fieldType.equals("java.lang.Double")) {
            ArrayListSorting.sort(nonNullFieldData, new LessNumber(java.lang.Double.class));
        }else if (fieldType.equals("java.lang.String")) {
            ArrayListSorting.sort(nonNullFieldData, new LessString());
        }else if (fieldType.equals("java.net.URL"))
            ArrayListSorting.sort(nonNullFieldData, new LessString());
//t        else if (fieldType.equals("java.util.Date"))
        else if (fieldType.equals("gr.cti.eslate.database.engine.CDate") ||
                fieldType.equals("gr.cti.eslate.database.engine.CTime"))
            ArrayListSorting.sort(nonNullFieldData, new LessDate());
        else{
            System.out.println("Serious inconsistency error in Table getUniqueFieldValues(): (1)");
            return null;
        }
*/
/*        if (fieldType.equals("java.lang.Integer")) {
Sorting.sort(nonNullFieldData, new LessNumber(java.lang.Integer.class));
}else if (fieldType.equals("java.lang.Double")) {
Sorting.sort(nonNullFieldData, new LessNumber(java.lang.Double.class));
}else if (fieldType.equals("java.lang.String")) {
Sorting.sort(nonNullFieldData, new LessString());
}else if (fieldType.equals("java.net.URL"))
Sorting.sort(nonNullFieldData, new LessString());
//t        else if (fieldType.equals("java.util.Date"))
else if (fieldType.equals("gr.cti.eslate.database.engine.CDate") ||
fieldType.equals("gr.cti.eslate.database.engine.CTime"))
Sorting.sort(nonNullFieldData, new LessDate());
else{
System.out.println("Serious inconsistency error in Table getUniqueFieldValues(): (1)");
return null;
}
*/
/*        fieldData = new ArrayList();
        int i=0, k = -1;
//        System.out.println("Removing dublicate values");
        while (i<nonNullFieldData.size()) {
            fieldData.add(nonNullFieldData.get(i));
            i++;
            k++;
            while (i<nonNullFieldData.size() && fieldData.get(k).equals(nonNullFieldData.get(i)))
                i++;
        }

        Object[] values = fieldData.toArray(); //new Object[fieldData.size()]; // = new Object[k];
//        for (i=0; i<k; i++)
//            values[i] = fieldData.at(i);
//        fieldData.copyTo(values);

//        System.out.println("Done.");

        return values;
*/
    }


    /** Returns the format used for the String representation of dates.
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }


    /** Sets the format used to produce the String representation of dates to
     *  <code>dateForm</code>.
     *  @param dateForm The new date format.
     *  @return <code>true</code>, if the supplied format is supported, i.e. is contained in
     *          <code>dateFormats</code> list. <code>false</code>, if an unknown format is provided.
     */
    public boolean setDateFormat(String dateForm) {
//        System.out.println("In setDateFormat: " + dateFormats);
        if (dateFormats.indexOf(dateForm) == -1)
            return false;
//        System.out.println("In setDateFormat 2");
        dateFormat = new SimpleDateFormat(dateForm, Locale.getDefault());
        setGreekEraSymbols(dateFormat);
        dateFormat.setLenient(false);
        CDate.dateFormat = dateFormat;
//        System.out.println("In setDateFormat 3");
        return true;
    }


    /** Returns the format used for the String representation of time values.
     */
    public SimpleDateFormat getTimeFormat() {
        return timeFormat;
    }


    /** Returns the current format used to represent numeric values.
     */
    public TableNumberFormat getNumberFormat() {
        return numberFormat;
    }


    /** Specifies if the thousands delimeter is used or not.
     */
    public void setThousandSeparatorUsed(boolean on) {
        if (on != isThousandSeparatorUsed()) {
            numberFormat.setThousandSeparatorUsed(on);
            setModified();
        }
    }


    public boolean isThousandSeparatorUsed() {
        return numberFormat.isThousandSeparatorUsed();
    }

    /** Sets the character used as the thousand separator in numeric values.
     */
    public void setThousandSeparator(char separator) {
        if (separator != getThousandSeparator()) {
/*            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
dfs.setGroupingSeparator(separator);
dfs.setDecimalSeparator(getDecimalSeparator());
numberFormat.setDecimalFormatSymbols(dfs);
*/
            numberFormat.setThousandSeparator(separator);
            setModified();
        }
    }


    public char getThousandSeparator() {
        return numberFormat.getThousandSeparator();
    }


    /** Sets the character used as the decimal separator in numeric values.
     */
    public void setDecimalSeparator(char separator) {
        if (separator != getDecimalSeparator()) {
//            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
//            dfs.setDecimalSeparator(separator);
//            dfs.setGroupingSeparator(getThousandSeparator());
//            numberFormat.setDecimalFormatSymbols(dfs);
            numberFormat.setDecimalSeparator(separator);
            setModified();
        }
    }


    public char getDecimalSeparator() {
        return numberFormat.getDecimalSeparator();
    }


    /** Determines whether the decimal part of numbers will be displayed or not.
     */
    public void setShowIntegerPartOnly(boolean integerPartOnly) {
        if (integerPartOnly != numberFormat.isShowIntegerPartOnly()) {
            numberFormat.setShowIntegerPartOnly(integerPartOnly);
            setModified();
        }
    }


    public boolean isShowIntegerPartOnly() {
        return numberFormat.isShowIntegerPartOnly();
    }


    /** Sets whether the decimal separator shown be showed as needed or always.
     */
    public void setDecimalSeparatorAlwaysShown(boolean shown) {
        if (shown != isDecimalSeparatorAlwaysShown()) {
            numberFormat.setDecimalSeparatorAlwaysVisible(shown);
            setModified();
        }
    }


    public boolean isDecimalSeparatorAlwaysShown() {
        return numberFormat.isDecimalSeparatorAlwaysVisible();
    }


    /** Determines whether numeric values in the table should be displayed
     * in exponential format.
     */
    public void setExponentialFormatUsed(boolean exponential) {
        if (exponential != isExponentialFormatUsed()) {
            numberFormat.setExponentialFormatUsed(exponential);
            setModified();
        }
    }


    public boolean isExponentialFormatUsed() {
        return numberFormat.isExponentialFormatUsed();
    }


    /** Sets the format used to produce the String representation of time values to
     *  <code>timeForm</code>.
     *  @param timeForm The new time format.
     *  @return <code>true</code>, if the supplied format is supported, i.e. is contained in
     *          <code>timeFormats</code> list. <code>false</code>, if an unknown format is provided.
     */
    public boolean setTimeFormat(String timeForm) {
        if (timeFormats.indexOf(timeForm) == -1)
            return false;
        timeFormat = new SimpleDateFormat(timeForm);
        timeFormat.setLenient(false);
        CTime.timeFormat = timeFormat;
        return true;
    }


    /** Returns the list of supported date formats.
     *  @see #dateFormats
     */
    public ArrayList getDateFormats() {
        return dateFormats;
    }

    /** Returns the list of supported time formats.
     *  @see #timeFormats
     */
    public ArrayList getTimeFormats() {
        return timeFormats;
    }


    /** Returns the <code>sampleDates</code> list.
     *  @see #sampleDates
     */
    public String[] getSampleDates() {
        return Table.sampleDates;
    }


    /** Reports whether the table has been modified in any way.
     *  @see #isModified
     */
    public boolean isModified() {
        return isModified;
    }

    /** Sets the <code>isModified</code> flag.
     *  @see #isModified
     */
    public void setModified() {
        isModified = true;
    }

    /** Resets the <code>isModified</code> flag.
     *  @see #isModified
     */
    public void resetModified() {
        isModified = false;
    }


    /** Returns the <code>DBase</code>, this Table belongs to.
     *
     */
/*    public DBase getDBase() {
//        System.out.println("in getDBase()");
//        System.out.println(this.database);
return this.database;
}
*/

    public ArrayList fillRecordEntryFormSTDIN() {
        ArrayList recEntryForm = new ArrayList();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s = new String("");

        //Examining if the table has fields
        if (tableFields.size() == 0) {
            System.out.println("No columns in the table. Record insertion failed!");
            return null;
        }

        //Filling the Record Entry Form from standard input
        for (int i=0; i<tableFields.size(); i++) {
            if ((tableFields.get(i)).isCalculated()) continue;
            System.out.print((tableFields.get(i)).getName() + ":  ");
            try {
                s = in.readLine();
            }catch(IOException e) {
                e.printStackTrace();
                return null;
            }
            if (s.equals(""))
                recEntryForm.add(null);
            else
                recEntryForm.add(s);

        }
        return recEntryForm;
    }


    /**
     *  Prints the contents of the Table.
     */
    public void printTable() {
        try {
            for (int i=0; i<recordCount; i++)
                System.out.println(getRow(i));
        }catch (InvalidRecordIndexException e) {System.out.println(e.message);}
    }


    protected final String nameForDataType(AbstractTableField f) {
        String fldType = "";
        if (f.getDataType().equals(IntegerTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Integer");
        else if (f.getDataType().equals(StringTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Alphanumeric");
        else if (f.getDataType().equals(DoubleTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Number");
        else if (f.getDataType().equals(FloatTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Float");
        else if (f.getDataType().equals(ImageTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Image");
        else if (f.getDataType().equals(URLTableField.DATA_TYPE))
            fldType = DBase.resources.getString("URL");
        else if (f.getDataType().equals(BooleanTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Boolean");
//t        else if (f.getFieldType().equals(java.util.Date.class)) {
//t            if (f.isDate())
        else if (f.getDataType().equals(DateTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Date");
        else if (f.getDataType().equals(TimeTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Time");

        return fldType;
    }


    protected final String nameForDataTypeInAitiatiki(AbstractTableField f) {
        String fldType = "";
        if (f.getDataType().equals(IntegerTableField.DATA_TYPE))
            fldType = DBase.resources.getString("IntegerAit");
        else if (f.getDataType().equals(StringTableField.DATA_TYPE))
            fldType = DBase.resources.getString("AlphanumericAit");
        else if (f.getDataType().equals(DoubleTableField.DATA_TYPE))
            fldType = DBase.resources.getString("NumberAit");
        else if (f.getDataType().equals(ImageTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Image");
        else if (f.getDataType().equals(URLTableField.DATA_TYPE))
            fldType = DBase.resources.getString("URL");
        else if (f.getDataType().equals(BooleanTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Boolean");
//t        else if (f.getFieldType().equals(java.util.Date.class)) {
//t            if (f.isDate())
        else if (f.getDataType().equals(DateTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Date");
        else if (f.getDataType().equals(TimeTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Time");

        return fldType;
    }

/*    public void printPrimaryKeyMap() {
        if (primaryKeyMap != null) {
            Enumeration e = primaryKeyMap.elements();
            HashMapIterator iter = (HashMapIterator) e;
            while (!iter.atEnd()) {
                System.out.println(iter.key() + ",  " + ((Integer) iter.value()).intValue()); //2.0.1 ((Record) iter.value()).getIndex());
                iter.advance();
            }
        }else
            System.out.println("primaryKeyMap = null");

    }
*/
    /** Returns the state of the Table in a StorageStructure.
     */
    public StorageStructure recordState() throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION); //, 33); //String.valueOf(STR_FORMAT_VERSION), 33);
///        fieldMap.put("Table data", tableData);
        fieldMap.put("Table fields", tableFields);
//2.0.1        fieldMap.put("Table records", tableRecords);
        fieldMap.put("Record addition allowed", recordAdditionAllowed);
        fieldMap.put("Table key", tableKey);
///        fieldMap.put("Primary key hash", primaryKeyMap);
///        fieldMap.put("Key field indices", keyFieldIndices);
        fieldMap.put("Key exists", hasKey());
        fieldMap.put("Record count", recordCount);
        fieldMap.put("Title", title);
        fieldMap.put("Metadata", metadata);
        fieldMap.put("Selected subset", selectedSubset);
        fieldMap.put("Unselected subset", unselectedSubset);
//1        fieldMap.put("Date format 1", sdf.toPattern());
        fieldMap.put("Date format 2", dateFormat.toPattern());
//1        fieldMap.put("Time format 1", stf.toPattern());
        fieldMap.put("Time format 2", timeFormat.toPattern());
        fieldMap.put("Number format", numberFormat);
        fieldMap.put("Calculated field count", numOfCalculatedFields);
        fieldMap.put("Calculated fields modified", calculatedFieldsChanged);
        fieldMap.put("Calculated field cells modified", changedCalcCells);
//1        fieldMap.put("New record pending", pendingNewRecord);
//1        fieldMap.put("UIProperties", UIProperties);
        fieldMap.put("isHidden", isHidden);
        fieldMap.put("Active record", activeRecord);
        fieldMap.put("Record removal allowed", recordRemovalAllowed);
        fieldMap.put("Field addition allowed", fieldAdditionAllowed);
        fieldMap.put("Field removal allowed", fieldRemovalAllowed);
        fieldMap.put("Field reordering allowed", fieldReorderingAllowed);
        fieldMap.put("Field property editing allowed", fieldPropertyEditingAllowed);
        fieldMap.put("Key change allowed", keyChangeAllowed);
        fieldMap.put("Data editable", dataChangeAllowed);
        fieldMap.put("Display hidden fields", hiddenFieldsDisplayed);
        fieldMap.put("Hidden state editable", hiddenAttributeChangeAllowed);
        fieldMap.put("Record index", recordIndex);
//1        fieldMap.put("Table View", tableView);
        fieldMap.put("Table renaming allowed", tableRenamingAllowed);

        // Save the TableView children of the Table
        if (tableViews != null) {
            ArrayList tableViewHandles = new ArrayList();
            for (int i=0; i<tableViews.size(); i++) {
                tableViewHandles.add(((TableView) tableViews.get(i)).getESlateHandle());
            }
            getESlateHandle().saveChildObjects(fieldMap, "Table Views", tableViewHandles);
        }

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

    public void applyState(StorageStructure fieldMap) throws IOException {
        String dataVersionStr = fieldMap.getDataVersion();
        int dataVersion = fieldMap.getDataVersionID();
///        int dataVersion = fieldMap.getDataVersionID();
/*       if (dataVersion == -1) {
///            String dataVersionStr = fieldMap.getDataVersion();

// Before version 2, the data version was a string of the type "1.1". These
// strings cannot be converted to ints, so a value -1 for data version means
// pre 2 data version.
try{
dataVersion = Integer.valueOf(dataVersionStr).intValue();
}catch (Throwable thr) {}
}
*/
//System.out.println("Table readExternal() dataVersion: " + dataVersion);
///        tableData.clear();
        tableFields.clear();
/*        if (primaryKeyMap != null) {
            primaryKeyMap.clear();
            primaryKeyMap = null;
        }
*/
        selectedSubset.clear();
        unselectedSubset.clear();
        if (changedCalcCells != null) {
            changedCalcCells.clear();
            changedCalcCells = null;
        }
//1        UIProperties.clear();
        recordSelection.clear();

//        System.out.println("dataVersion: " + dataVersion);
        if (dataVersion >= 4) {
            tableFields = (TableFieldBaseArray) fieldMap.get("Table fields");
        }else{
            Array tableFlds = (Array) fieldMap.get("Table fields");
            /* If the 'tableFields' contains CTableFields instead of TableFields, then
            * convert them TableFields.
            */
            for (int i=0; i<tableFlds.size(); i++) {
                Object t = tableFlds.at(i);
                if (CTableField.class.isAssignableFrom(t.getClass())) {
                    AbstractTableField field = AbstractTableField.createNewField(this, (CTableField) t);
                    field.indexInTable = i;
                    field.table = this;
                    tableFlds.put(i, field); /*new TableField((CTableField) t));*/
                }else{
                    // In this case TableFields were restored. We have to convert them to the new sub-class of
                    // TableField, i.e. StringTableField, DoubleTableField, ...
                    AbstractTableField field = AbstractTableField.createProperSubClassField(this, (TableField) t);
                    field.indexInTable = i;
                    field.table = this;
                    tableFlds.put(i, field);
                }
            }
            tableFields.clear();
            for (int i=0; i<tableFlds.size(); i++) {
                AbstractTableField fld = (AbstractTableField) tableFlds.at(i);
                fld.table = this;
                fld.indexInTable = tableFields.size();
                tableFields.add(fld);
            }
        }

        for (int i=0; i<tableFields.size(); i++) {
            AbstractTableField field = tableFields.get(i);
            field.table = this; //Re-assign the field's table
            if (field.oe != null) {
                field.oe.table = this;
            }
        }

        if (dataVersion < 3) {
            Object data = fieldMap.get("Table data");
            if (Array.class.isAssignableFrom(data.getClass())) {
                Array oldTypeData = (Array) data;
                for (int i=0; i<oldTypeData.size(); i++) {
                    Array ar = (Array) oldTypeData.at(i);
                    ArrayList al = new ArrayList();
                    //            tableData.add
                    for (int k=0; k<ar.size(); k++)
                        al.add(ar.at(k));
//                    tableData.add(al);
                    tableFields.get(i).setData(al);
                }
            }else{
                ArrayList oldTypeData = (ArrayList) data;
                for (int i=0; i<oldTypeData.size(); i++) {
                    tableFields.get(i).setData(oldTypeData);
                }
//                tableData = (ArrayList) data;
            }
        }else if (dataVersion == 3) {
//System.out.println("Reading ArrayList");
            Object data = fieldMap.get("Table data");
            ArrayList oldTypeData = (ArrayList) data;
            for (int i=0; i<oldTypeData.size(); i++) {
                tableFields.get(i).setData((ArrayList) oldTypeData.get(i));
            }
//            tableData = (ArrayList) fieldMap.get("Table data");
        }

//System.out.println("readExternal() 1 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 1 : " + tableTimer.lapse());
//2.0.1        tableRecords = (Array) fieldMap.get("Table records");
//2.0.1        for (int i=0; i<tableRecords.size(); i++)
//2.0.1            ((Record) tableRecords.at(i)).table = this;
        recordAdditionAllowed = fieldMap.get("Record addition allowed", true);

/*      Pre-4 version keyed Tables not supported. The keys won't be restored.
       HashMap tmpKeyMap = (HashMap) fieldMap.get("Primary key map", (Object) null);
        if (dataVersion == -1 && tmpKeyMap != null) {
*/            /* This is a pre 2 storage version, where the 'primaryKeyMap' stored
            * Records instead of Integer indices. Make the transformation.
            */
/*            primaryKeyMap = new HashMap(new EqualTo(), true, tmpKeyMap.size(), (float)1.0);
            HashMapIterator iter = tmpKeyMap.begin();
            while (!iter.atEnd()) {
                primaryKeyMap.add(iter.key(), new Integer(((Record) iter.value()).getIndex()));
                iter.advance();
            }
        }else
            primaryKeyMap = tmpKeyMap;
*/
        if (dataVersion >= 4) {
            TableKey key = (TableKey) fieldMap.get("Table key");
            if (key != null) {
                tableKey = key;
                tableKey.table = this;
            }
/*            keyFieldIndices = (IntBaseArray) fieldMap.get("Key field indices");
            hasKey = fieldMap.get("Key exists", false);
            primaryKeyMap = (Hashtable) fieldMap.get("Primary key hash", (Object) null);
*/
        }else{
            if (tableKey == null)
                tableKey = new TableKey(this);
            else{
                tableKey.clear();
                tableKey.table= this;
            }
/*            keyFieldIndices = new IntBaseArray();
//            keyFieldIndices = Table.convertArrayToIntBaseArray((Array) fieldMap.get("Key field indices"));
            hasKey = false;
            primaryKeyMap = null;
*/
        }
        recordCount = fieldMap.get("Record count", 0);
        // Before storage format version 4 (code version 2.1.0), the recordCount took values between -1 and #records-1.
        // Since version 4, the recordCount takes value from 1 to #records. So for pre 3 storage format version Tables,
        // the read record count has to be incremented by 1.
        if (dataVersion < 4)
            recordCount++;
//        System.out.println("recordCount: " + recordCount);
//System.out.println("-------- readExternal() 1.1.1 : " + tableTimer.lapse() + ", dataVersion: " + dataVersion);
        _setTitle(fieldMap.get("Title", ""));
//System.out.println("-------- readExternal() 1.1.2 : " + tableTimer.lapse() + ", dataVersion: " + dataVersion);
        metadata = fieldMap.get("Metadata", (String) null);
//System.out.println("readExternal() 1.2 : " + tableTimer.lapse());
        if (dataVersionStr.equals("1.0")) {
            /* Convert the JGL Arrays with the indexes of the selected and
            * unselected records to IntBaseArrayDescs
            */
            System.out.println("Converting Arrays...");
            Array selectedSubsetArray = (Array) fieldMap.get("Selected subset");
            Array unselectedSubsetArray = (Array) fieldMap.get("Unselected subset");
            selectedSubset = new IntBaseArrayDesc(selectedSubsetArray.size());
            for (int i=0; i<selectedSubsetArray.size(); i++)
                selectedSubset.add(((Integer) selectedSubsetArray.at(i)).intValue());
            unselectedSubset = new IntBaseArrayDesc(unselectedSubsetArray.size());
            for (int i=0; i<unselectedSubsetArray.size(); i++)
                unselectedSubset.add(((Integer) unselectedSubsetArray.at(i)).intValue());
        }else{
            selectedSubset = (IntBaseArrayDesc) fieldMap.get("Selected subset");
            unselectedSubset = (IntBaseArrayDesc) fieldMap.get("Unselected subset");
        }
//            System.out.println("readObject: " + selectedSubset);
//            System.out.println("readObject: " + unselectedSubset);
//System.out.println("readExternal() 2 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 2 : " + tableTimer.lapse());
/*1        try{
            Object o = fieldMap.get("Date format 1");
            if (o instanceof SimpleDateFormat)
                sdf = (SimpleDateFormat) o;
            else
                sdf = new SimpleDateFormat((String) o);
        }catch (Throwable e) {
            sdf = new SimpleDateFormat(); //"dd/MM/yyyy");
        }
1*/
        try{
            Object o = fieldMap.get("Date format 2");
            if (o instanceof SimpleDateFormat)
                dateFormat = (SimpleDateFormat) o;
            else{
                dateFormat = new SimpleDateFormat((String) o, Locale.getDefault());
                setGreekEraSymbols(dateFormat);
            }
        }catch (Throwable e) {
            dateFormat = new SimpleDateFormat(); //"dd/MM/yyyy");
            setGreekEraSymbols(dateFormat);
        }
/*1        try{
            Object o = fieldMap.get("Time format 1");
            if (o instanceof SimpleDateFormat)
                stf = (SimpleDateFormat) o;
            else
                stf = new SimpleDateFormat((String) o);
        }catch (Exception e) {
            stf = new SimpleDateFormat(); //"hh:mm");
        }
1*/
        try{
            Object o = fieldMap.get("Time format 2");
            if (o instanceof SimpleDateFormat)
                timeFormat = (SimpleDateFormat) o;
            else
                timeFormat = new SimpleDateFormat((String) o);
        }catch (Exception e) {
            timeFormat = new SimpleDateFormat(); //"hh:mm");
        }
//System.out.println("readExternal() 3 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 3 : " + tableTimer.lapse());
        if (dataVersion <= 4)
            numberFormat = new TableNumberFormat((DoubleNumberFormat) fieldMap.get("Number format"));
        else
            numberFormat = (TableNumberFormat) fieldMap.get("Number format");
        numOfCalculatedFields = fieldMap.get("Calculated field count", 0);
        calculatedFieldsChanged = fieldMap.get("Calculated fields modified", false);
        if (dataVersion < 4) {
            com.objectspace.jgl.HashMap tmpMap = (com.objectspace.jgl.HashMap) fieldMap.get("Calculated field cells modified");
            com.objectspace.jgl.HashMapIterator iter = tmpMap.begin();
            while (!iter.atEnd()) {
                changedCalcCells.put(iter.key(), iter.value());
                iter.advance();
            }
        }else
            changedCalcCells = (Hashtable) fieldMap.get("Calculated field cells modified");
//1        pendingNewRecord = fieldMap.get("New record pending", false);
//1        UIProperties = (Array) fieldMap.get("UIProperties");
        isHidden = fieldMap.get("isHidden", false);
        activeRecord = fieldMap.get("Active record", -1);
        recordRemovalAllowed = fieldMap.get("Record removal allowed", true);
        fieldAdditionAllowed = fieldMap.get("Field addition allowed", true);
        fieldRemovalAllowed = fieldMap.get("Field removal allowed", true);
        fieldReorderingAllowed = fieldMap.get("Field reordering allowed", true);
        fieldPropertyEditingAllowed = fieldMap.get("Field property editing allowed", true);
        keyChangeAllowed = fieldMap.get("Key change allowed", true);
        dataChangeAllowed = fieldMap.get("Data editable", true);
        hiddenFieldsDisplayed = fieldMap.get("Display hidden fields", false);
        hiddenAttributeChangeAllowed = fieldMap.get("Hidden state editable", true);
        hiddenAttributeChangeAllowed = fieldMap.get("Hidden state editable", true);
        if (dataVersion < 4) {
            int[] tmp = (int[]) fieldMap.get("Record index", (Object) null);
            if (tmp == null) {
                recordIndex = new IntBaseArray(recordCount);
                for (int i=0; i<recordCount; i++)
                    recordIndex.add(i);
            }else{
                for (int i=0; i<tmp.length; i++)
                    recordIndex.add(tmp[i]);
            }
        }else{
            recordIndex = (IntBaseArray) fieldMap.get("Record index");
        }
//1        tableView = (TableView) fieldMap.get("Table View");
        tableRenamingAllowed = fieldMap.get("Table renaming allowed", true);

//System.out.println("readExternal() 4 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 4 : " + tableTimer.lapse());
        /* Initializing Arrays with alternative date and time formats.
        */
        fillSupportedDateFormats();
        createSampleDates();
//            System.out.println("1Creating date formats");
        fillSupportedTimeFormats();

//System.out.println("readExternal() 5 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 5 : " + tableTimer.lapse());
        recordSelection = new BoolBaseArray();
        if (recordCount > 0)
            recordSelection.setSize(recordCount); //ensureCapacity(recordCount);
        for (int i=0; i<recordCount; i++)
            recordSelection.add(false); //Boolean.FALSE);
        for (int i=0; i<selectedSubset.size(); i++)
            recordSelection.set(selectedSubset.get(i), true); //set(((Integer) selectedSubset.at(i)).intValue(), true);//Boolean.TRUE);

//System.out.println("readExternal() 6 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 6 : " + tableTimer.lapse());
        if (tablePlug != null) {
            tablePlug.createFieldPlugs();
//            tablePlug.updatePlugValues(getActiveRecord());
        }

        // Restore the TableView children of the Table
        Object[] views =  getESlateHandle().restoreChildObjects(fieldMap, "Table Views");
        if (views != null && views.length > 0) {
            tableViews = new ArrayList(views.length);
            for (int i=0; i<views.length; i++) {
/*                if (views[0].getClass().equals(TableView.class)) {
                    TableView view = (TableView) views[i];
                    tableViews.add(views[i]);
                    view.setViewTable(this);
                    getESlateHandle().add(view.getESlateHandle());
                }else{
*/                    TableView view = (TableView) ((ESlateHandle) views[i]).getComponent();
                    tableViews.add(view);
                    view.setViewTable(this);
//                }
            }
        }
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        ESlateHandle prevStaticParent = ESlateHandle.nextParent;
        ESlateHandle.nextParent = getESlateHandle();
//System.out.println("---------------------------- TABLE READEXTERNAL ------------------------------------");
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        ESlateHandle h = getESlateHandle();
        pm.init(loadTimer);
        long start = System.currentTimeMillis();
//long start = tableTimer.getTime();
        tableTimer.init();
        pm.init(fieldMapTimer);
//        StorageStructure fieldMap = (StorageStructure) in.readObject();
        long s = System.currentTimeMillis();
        StorageStructure fieldMap = (StorageStructure) in.readObject();
//System.out.println("DBase tabledata read time: " + (System.currentTimeMillis()-s));
        pm.stop(fieldMapTimer);
        pm.displayTime(fieldMapTimer, h, "", "ms");
//System.out.println("readExternal() 0 : " + tableTimer.lapse());

        applyState(fieldMap);
        isModified = false;
//System.out.println("readExternal() 7 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 7 : " + tableTimer.lapse());
        ESlateMicroworld mwd = h.getESlateMicroworld();
//System.out.println("readExternal() 7.1 : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() 7.1 : " + tableTimer.lapse());
        if (mwd != null) {
            try{
                mwd.setPlugAliasForLoading(tablePlug.getRecordPlug(),
                        h,
                        new String[] {
                            bundle.getString("Record")+" "+bundle.getString("of")+" \""+getTitle()+"\"",
                            getTitle()}
                );
            }catch (BadPlugAliasException exc) {
                System.out.println("Unable to set plug alias for the record plug of table \"" + title + "\"");
            }
        }

//System.out.println("readExternal() end : " + (tableTimer.getTime()-start));
//System.out.println("readExternal() end : " + tableTimer.lapse());
        pm.stop(loadTimer);
//System.out.println("Table readExternal() getESlateHandle(): " + getESlateHandle() + ", time: " + (System.currentTimeMillis()-start));
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
        ESlateHandle.nextParent = prevStaticParent;
    }


    /** Returns <code>true</code> if the table is hidden, <code>false</code> otherwise.
     */
    public boolean isHidden() {
        return isHidden;
    }


    /** Sets the <code>isHidden</code> flag.
     *  @see #isHidden
     */
    public void setHidden(boolean isHidden) throws AttributeLockedException {
        if (!hiddenAttributeChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg91"));

        if (this.isHidden != isHidden) {
/*t            int tableIndex = 0, visibleIndex = 0;
if (database != null) {
tableIndex = database.CTables.indexOf(this);
visibleIndex = database.toVisibleCTableIndex(tableIndex);
}
t*/
            this.isHidden = isHidden;
            setModified();
//t            if (database != null)
            fireTableHiddenStateChanged(); //tableIndex, visibleIndex, isHidden));
        }
    }


    /** Returns the index of the active record of the table. If no
     * active record exists, -1 is returned.
     */
    public int getActiveRecord() {
        return activeRecord;
    }


    /** Sets the active record of the table. The active record number
     *  is set to -1, if the given record index is out of range,
     *  i.e. less than 0, or greater than the number of records minus 1.
     */
    public void setActiveRecord(int activeRecord) {
        if (activeRecord == this.activeRecord)
            return;
        int previousActiveRecord = this.activeRecord;
        if (activeRecord < 0 || activeRecord > recordCount)
            this.activeRecord = -1;
        else
            this.activeRecord = activeRecord;
        setModified();
//t        if (this.database != null)
        fireActiveRecordChanged(previousActiveRecord); //database.CTables.indexOf(this), previousActiveRecord, activeRecord));
        return;
    }

    /** Finds the cell with the specified value. The searched value may contain '*' or '^'. The first means 0 or more
     *  characters, while the second means exactly one character.
     *  @param fieldNames The names of the fields which will be searched.
     *  @param recordIndices The indices of the records to search.
     *  @param findWhat The value to search for.
     *  @param caseSensitive Determines if the search will be case sensitive.
     *  @param startFromFieldIndex The index of the field in <code>fieldNames</code> from which the search will start.
     *         Only the fields before or after (depending on the direction of the search) <code>startFromFieldIndex</code>
     *         in the <code>fieldNames</code> array will be searched.
     *  @param startFromRecordIndex The index of the record in <code>recordIndices</code> from which the search will start.
     *         Only the records before or after (depending on the direction of the search) <code>startFromRecordIndex</code>
     *         in the <code>recordIndices</code> array will be searched.
     *  @param downwards Determines the direction of the search.
     *  @exception InvalidFieldNameException If any of the field names in the <code>fieldNames</code> is invalid.
     *  @return The address of the first found cell. If no cell is found to contain the requested value, <code>null</code>
     *         is returned.
     */
    public CellAddress find(StringBaseArray fieldNames, int[] recordIndices, String findWhat, boolean caseSensitive, int startFromFieldIndex, int startFromRecordIndex, boolean downwards) throws InvalidFieldNameException {
        if (fieldNames.size() == 0 || recordIndices.length == 0)
            return null;

/*System.out.println("findNext() recordIndices: ");
for (int i=0; i<recordIndices.length; i++)
    System.out.print(recordIndices[i] + ",");
System.out.println();
System.out.println("Fields: " + fieldNames);
System.out.println("downwards: " + downwards);
*/
        AbstractTableField fld;
        for (int i=0; i<fieldNames.size(); i++) {
            fld = getTableField(fieldNames.get(i));
            if (fld.getDataType().equals(ImageTableField.DATA_TYPE) || (fld.isHidden() && !hiddenFieldsDisplayed)) {
                if (!downwards && startFromFieldIndex >= i)
                    startFromFieldIndex--;
                fieldNames.remove(i);
                i--;
            }
        }

//        System.out.println("fieldNames: " + fieldNames);

        int[] fieldIndices = new int[fieldNames.size()];

        for (int i=0; i<fieldNames.size(); i++)
            fieldIndices[i] = getFieldIndex(fieldNames.get(i));

        /* Create the EqualFormattedString predicate
        */
        ArrayList anyCharIndex = new ArrayList();
        ArrayList anyString = new ArrayList();
        boolean quotedOperand = false;

        if (findWhat.charAt(0) == '"' &&
                findWhat.charAt(findWhat.length()-1)== '"' &&
                findWhat.substring(1, findWhat.length()-1).indexOf('"') == -1 ) {
            findWhat = findWhat.substring(1, findWhat.length()-1);
            quotedOperand = true;
        }

        if (caseSensitive)
            findWhat = findWhat.toLowerCase();

        if (!quotedOperand && ((findWhat.indexOf('^') != -1) || (findWhat.indexOf('*') != -1))) {
//                System.out.println("Got in !quotedOperand &&");

            int index=0;
            int lastIndexValue = 0;
            while ((index = findWhat.indexOf('*', index)) != -1) {
                anyString.add(findWhat.substring(lastIndexValue, index));
                lastIndexValue = index+1;
                if (!(index == findWhat.length()-1))
                    index++;
                else
                    break;
            }
            if (lastIndexValue != 0) {
                anyString.add(findWhat.substring(lastIndexValue, findWhat.length()));
            }

            index = 0;
            if (anyString.size() == 0) {
                while ((index = findWhat.indexOf('^', index)) != -1) {
                    anyCharIndex.add(new Integer(index));
                    if (!(index == findWhat.length()-1))
                        index++;
                    else
                        break;
                }
            }else{
                for (int i=0; i<anyString.size(); i++) {
                    while ((index = ((String) anyString.get(i)).indexOf('^', index)) != -1) {
                        anyCharIndex.add(new Integer(index));
//                            anyCharStringIndex.add(new Integer(i));
                        if (!(index == findWhat.length()-1))
                            index++;
                        else
                            break;
                    }
                }
            }
//            System.out.println("Formatted string: " + anyCharIndex + " " + anyString);
        }

        EqualFormattedString efs = new EqualFormattedString(anyCharIndex, anyString);

//        System.out.println("startFromFieldIndex: " + startFromFieldIndex + ", startFromRecordIndex: " + startFromRecordIndex);
        /* Search the rest of the startFrom record
        */
        String str;
        int recIndex, fieldIndex;
        Object o;
        if (downwards) {
//            if (startFromFieldIndex+1 < getFieldCount()-1) {
            for (int i=startFromFieldIndex; i<fieldIndices.length; i++) {
                fieldIndex = fieldIndices[i];
                o = riskyGetCell(fieldIndex, recordIndices[startFromRecordIndex]);
                if (o == null)
                    continue;
                else{
                    fld = tableFields.get(fieldIndex);
                    if (fld.getDataType().equals(DoubleTableField.DATA_TYPE))
                        str = getNumberFormat().format((Double) o);
                    else
                        str = o.toString();
                }
                if (caseSensitive)
                    str = str.toLowerCase();
//                    System.out.println("str: " + str + ", findWhat: " + findWhat);
                if (efs.execute(str, findWhat)) {
                    CellAddress result = new CellAddress(recordIndices[startFromRecordIndex], fieldIndex);
                    return result;
                }
            }
//            }
        }else{
//            if (startFrom[1]-1 >= 0) {
            for (int i=startFromFieldIndex; i>=0; i--) {
                fieldIndex = fieldIndices[i];
                o = riskyGetCell(fieldIndex, recordIndices[startFromRecordIndex]);
                if (o == null)
                    continue;
                else{
                    fld = tableFields.get(fieldIndex);
                    if (fld.getDataType().equals(DoubleTableField.DATA_TYPE))
                        str = getNumberFormat().format((Double) o);
                    else
                        str = o.toString();
                }
                if (caseSensitive)
                    str = str.toLowerCase();
//                    System.out.println("str: " + str + ", findWhat: " + findWhat);
                if (efs.execute(str, findWhat)) {
                    CellAddress result = new CellAddress(recordIndices[startFromRecordIndex], fieldIndex);
                    return result;
                }
            }
//            }
        }

        /* Search on the specified fields and records to the specified direction
        */
        if (downwards) {
            for (int i=startFromRecordIndex+1; i<recordIndices.length; i++) {
                recIndex = recordIndices[i];
                for (int k = 0; k<fieldIndices.length; k++) {
                    fieldIndex = fieldIndices[k];
                    o = riskyGetCell(fieldIndex, recIndex);
                    if (o == null)
                        continue;
                    else{
                        fld = tableFields.get(fieldIndex);
                        if (fld.getDataType().equals(DoubleTableField.DATA_TYPE))
                            str = getNumberFormat().format((Double) o);
                        else
                            str = o.toString();
                    }
                    if (caseSensitive)
                        str = str.toLowerCase();
//                    System.out.println("str: " + str + ", findWhat: " + findWhat);
                    if (efs.execute(str, findWhat)) {
                        CellAddress result = new CellAddress(recIndex, fieldIndex);
                        return result;
                    }
                }
            }
        }else{
            for (int i=startFromRecordIndex-1; i>=0; i--) {
                recIndex = recordIndices[i];
                for (int k = fieldIndices.length-1; k>=0; k--) {
                    fieldIndex = fieldIndices[k];
                    o = riskyGetCell(fieldIndex, recIndex);
                    if (o == null)
                        continue;
                    else{
                        fld = tableFields.get(fieldIndex);
                        if (fld.getDataType().equals(DoubleTableField.DATA_TYPE))
                            str = getNumberFormat().format((Double) o);
                        else
                            str = o.toString();
                    }
                    if (caseSensitive)
                        str = str.toLowerCase();
//                   System.out.println("str: " + str + ", findWhat: " + findWhat);
                    if (efs.execute(str, findWhat)) {
                        CellAddress result = new CellAddress(recIndex, fieldIndex);
                        return result;
                    }
                }
            }
        }

        return null;
    }

    public boolean hasKey() {
        return tableKey.keyExists();
    }

    /** Returns the structure which facilitates the addition of new records to the Table. This is the recommended
     *  mechanism to add new records to the Table. It is preferred to the use of <code>addRecord()</code>.
     *  @return The structure through which the values of the new record are set.
     *  @see RecordEntryStructure
     */
    public RecordEntryStructure getRecordEntryStructure() {
        if (res == null)
            res = new RecordEntryStructure(this);
        return res;
    }

    /** Initializes a new Table instance.
     */
    protected void initTable() {
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        //Initializing the records' count
        recordCount = 0;
//        primaryKeyMap = null;
//        hasKey = false;
        numOfCalculatedFields = 0;
        recordAdditionAllowed = true;
        recordRemovalAllowed = true;
        fieldAdditionAllowed = true;
        fieldRemovalAllowed = true;
        fieldReorderingAllowed = true;
        fieldPropertyEditingAllowed = true;
        tableRenamingAllowed = true;
        keyChangeAllowed = true;
        dataChangeAllowed = true;
        hiddenFieldsDisplayed = false;
        hiddenAttributeChangeAllowed = true;
//        file = null;
//t        database = null;
        //Initializing properly Time and Date entry formats
//1        sdf.setLenient(false);
//1        stf.setLenient(false);
//1        stf.getTimeZone().setRawOffset(0);
//        System.out.println("Creating dateformat");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        setGreekEraSymbols(dateFormat);
        dateFormat.setLenient(false);
        timeFormat = new SimpleDateFormat("H:mm");
        timeFormat.setLenient(false);
        CDate.dateFormat = dateFormat;
        CTime.timeFormat = timeFormat;
//        System.out.println("Created dateformat");
        numberFormat = new TableNumberFormat();
//        System.out.println("MAx: " + numberFormat.getMaximumIntegerDigits() + ", " + numberFormat.getMaximumFractionDigits());
//        numberFormat.setMaximumIntegerDigits(40);
//        numberFormat.setMaximumFractionDigits(20);
//        numberFormat.setMinimumFractionDigits(1);
        calculatedFieldsChanged = false;
        changedCalcCells = new Hashtable();
//1        pendingNewRecord = false;

        /* Initializing Arrays with alternative date and time formats.
        */
        fillSupportedDateFormats();
        createSampleDates();
        fillSupportedTimeFormats();
//        System.out.println("Created sample dates");

        for (int i=0; i<recordCount; i++)
            recordSelection.add(false); //Boolean.FALSE);

        pm.constructionEnded(this);
        pm.stop(constructorTimer);
        pm.displayTime(constructorTimer, "��������� ������", "ms");
//        pm.displayTime(constructorTimer, getESlateHandle(), "", "ms");
    }

    private void setGreekEraSymbols(SimpleDateFormat format) {
        /* The date sympols for the Greek locale do not contain "�.�." and "�.�.". So if the
        * locale is Greek, we set these date format symbols below.
        */
        if (Locale.getDefault().toString().equals("el_GR")) {
            java.text.DateFormatSymbols dfs = new java.text.DateFormatSymbols();
            dfs.setEras(new String[] {"π.Χ.", "μ.Χ."});
            format.setDateFormatSymbols(dfs);
        }
    }

    private static final void fillSupportedDateFormats() {
        if (dateFormats != null) return;
        dateFormats = new ArrayList();
        dateFormats.ensureCapacity(8);
        dateFormats.add("dd/MM/yyyy");
        dateFormats.add("dd-MM-yyyy");
        dateFormats.add("MM/dd/yyyy");
        dateFormats.add("MM-dd-yyyy");
        dateFormats.add("dd/MM/yy");
        dateFormats.add("dd-MM-yy");
        dateFormats.add("MMM d, ''yy");
        dateFormats.add("EEE, MMM d, ''yy");
        dateFormats.add("dd/MM/yyyy G");
        dateFormats.add("dd-MM-yyyy G");
        dateFormats.add("MM/dd/yyyy G");
        dateFormats.add("MM-dd-yyyy G");
        dateFormats.add("yyyy G");
    }

    private static final void createSampleDates() {
        if (Table.sampleDates != null) return;
        Date today = new Date(System.currentTimeMillis());
        String prefix = "   (";
        String suffix = ")";
        sampleDates = new String[dateFormats.size()];
//            System.out.println("Creating date formats");
        sampleDates[0] = prefix + (new SimpleDateFormat((String) dateFormats.get(0))).format(today) + suffix;
        sampleDates[1] = prefix + (new SimpleDateFormat((String) dateFormats.get(1))).format(today) + suffix;
        sampleDates[2] = prefix + (new SimpleDateFormat((String) dateFormats.get(2))).format(today) + suffix;
        sampleDates[3] = prefix + (new SimpleDateFormat((String) dateFormats.get(3))).format(today) + suffix;
        sampleDates[4] = prefix + (new SimpleDateFormat((String) dateFormats.get(4))).format(today) + suffix;
        sampleDates[5] = prefix + (new SimpleDateFormat((String) dateFormats.get(5))).format(today) + suffix;
        sampleDates[6] = prefix + (new SimpleDateFormat((String) dateFormats.get(6))).format(today) + suffix;
        sampleDates[7] = prefix + (new SimpleDateFormat((String) dateFormats.get(7))).format(today) + suffix;
        sampleDates[8] = prefix + (new SimpleDateFormat((String) dateFormats.get(8))).format(today) + suffix;
        sampleDates[9] = prefix + (new SimpleDateFormat((String) dateFormats.get(9))).format(today) + suffix;
        sampleDates[10] = prefix + (new SimpleDateFormat((String) dateFormats.get(10))).format(today) + suffix;
        sampleDates[11] = prefix + (new SimpleDateFormat((String) dateFormats.get(11))).format(today) + suffix;
        sampleDates[12] = prefix + (new SimpleDateFormat((String) dateFormats.get(12))).format(today) + suffix;
//            System.out.println("Created date formats");
    }

    private static final void fillSupportedTimeFormats() {
        if (Table.timeFormats != null) return;
        timeFormats = new ArrayList();
        timeFormats.ensureCapacity(3);
        timeFormats.add("H:mm");
        timeFormats.add("H:mm:ss");
        timeFormats.add("H:mm:ss:SSS");
        timeFormats.add("hh:mm");
        timeFormats.add("hh:mm:ss");
        timeFormats.add("hh:mm:ss:SSS");
        timeFormats.add("hh:mm aaa");
        timeFormats.add("hh:mm:ss aaa");
        timeFormats.add("hh:mm:ss:SSS aaa");
    }

    /**
     *  Instantiates a new Table.
     */
    public Table() {
        title = bundle.getString("Table");
        initTable();
        setModified();
    }


    /**
     *  Instantiates a new Table with the specified title.
     */
    public Table(String title) {
        _setTitle(title);
        initTable();
        setModified();
    }

    Table(CTable ctable) {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this); //.init(constructorTimer);
//        System.out.println("Converting ctable : " + ctable.getTitle());

        Array tableFlds = ctable.CTableFields;
        /* Convert the CTableFields contained in 'tableFields' to TableFields
        */
        tableFields.clear();
        for (int i=0; i<tableFlds.size(); i++) {
//			TableField fld = new TableField((CTableField) tableFlds.at(i));
            AbstractTableField fld = AbstractTableField.createNewField(this, (CTableField) tableFlds.at(i));
            fld.indexInTable = tableFields.size();
            fld.table = this;
            tableFields.add(fld);
        }

        for (int i=0; i<tableFields.size(); i++) {
            AbstractTableField field = tableFields.get(i);
            if (field.oe != null) {
                field.oe.table = this;
            }
        }

//        tableData = ctable.tableData;
        Array oldTypeData = ctable.tableData;
        for (int i=0; i<oldTypeData.size(); i++) {
            Array ar = (Array) oldTypeData.at(i);
            ArrayList al = new ArrayList();
//            tableData.add
            for (int k=0; k<ar.size(); k++)
                al.add(ar.at(k));
            tableFields.get(i).setData(al);
//            tableData.add(al);
        }

//2.0.1        tableRecords = ctable.CTableRecords;
//2.0.1        for (int i=0; i<tableRecords.size(); i++)
//2.0.1            ((Record) tableRecords.at(i)).table = this;

        recordAdditionAllowed = ctable.isRecordAdditionAllowed();
        tableKey.clear();
        tableKey.table = this;
/*        com.objectspace.jgl.HashMap tmpMap = ctable.primaryKeyMap;
        com.objectspace.jgl.HashMapIterator iter = tmpMap.begin();
        while (!iter.atEnd()) {
            primaryKeyMap.put(iter.key(), iter.value());
            iter.advance();
        }
        keyFieldIndices = Table.convertArrayToIntBaseArray(ctable.keyFieldIndices);
        hasKey = ctable.hasKey();
*/
        recordCount = ctable.getRecordCount()-1;
        _setTitle(ctable.getTitle());
        metadata = ctable.getMetadata();
        selectedSubset = ctable.getSelectedSubset();
        unselectedSubset = ctable.getUnSelectedSubset();
//1        sdf = ctable.sdf;
        dateFormat = ctable.dateFormat;
//1        stf = ctable.stf;
        timeFormat = ctable.timeFormat;
        numberFormat = new TableNumberFormat(ctable.numberFormat);
        numOfCalculatedFields = ctable.numOfCalculatedFields;
        calculatedFieldsChanged = ctable.haveCalculatedFieldsChanged();
//        changedCalcCells = ctable.getChangedCalcFieldCells();
        com.objectspace.jgl.HashMap tmpMap = ctable.getChangedCalcFieldCells();
        com.objectspace.jgl.HashMapIterator iter = tmpMap.begin();
        while (!iter.atEnd()) {
            changedCalcCells.put(iter.key(), iter.value());
            iter.advance();
        }
//1        pendingNewRecord = ctable.pendingNewRecord;
//1        UIProperties = ctable.UIProperties;
        isHidden = ctable.isHidden;
        activeRecord = ctable.getActiveRecord();
        recordRemovalAllowed = ctable.isRecordRemovalAllowed();
        fieldAdditionAllowed = ctable.isFieldAdditionAllowed();
        fieldRemovalAllowed = ctable.isFieldRemovalAllowed();
        fieldReorderingAllowed = ctable.isFieldReorderingAllowed();
        fieldPropertyEditingAllowed = true;
        tableRenamingAllowed = true;
        keyChangeAllowed = ctable.isKeyChangeAllowed();
        dataChangeAllowed = ctable.isDataChangeAllowed();
        hiddenFieldsDisplayed = ctable.isHiddenFieldsDisplayed();
        hiddenAttributeChangeAllowed = ctable.isHiddenAttributeChangeAllowed();
        recordIndex = new IntBaseArray(ctable.recordIndex.length);
        for (int i=0; i<ctable.recordIndex.length; i++)
            recordIndex.add(ctable.recordIndex[i]);
        /* Initializing Arrays with alternative date and time formats.
        */
        fillSupportedDateFormats();
        createSampleDates();
        fillSupportedTimeFormats();

        recordSelection = ctable.recordSelection;
        pm.constructionEnded(this);// .stop(constructorTimer);
//        pm.displayTime(constructorTimer, getESlateHandle(), "", "ms");
    }

    /* Called whenever the order of the records in the Table changes.
    */
    public void rowOrderChanged() {
        fireRowOrderChanged();
    }

    public static ArrayList convertArrayToArrayList(Array array) {
        if (array == null) return null;
        int size = array.size();
        ArrayList arrList = new ArrayList(size);
        for (int i=0; i<size; i++)
            arrList.add(array.at(i));
        return arrList;
    }

    public static Array convertArrayListToArray(ArrayList arrList) {
        int size = arrList.size();
        Array array = new Array();
        for (int i=0; i<size; i++)
            array.add(arrList.get(i));
        return array;
    }

    public static IntBaseArray convertArrayToIntBaseArray(Array array) {
        if (array == null) return null;
        int size = array.size();
        IntBaseArray intArr = new IntBaseArray(size);
        for (int i=0; i<size; i++)
            intArr.add(((Integer) array.at(i)).intValue());
        return intArr;
    }

    /**
     *  @see gr.cti.eslate.tableModel.ITableModel#getTypeForClass(Class)
     */
    public byte getTypeForClass(Class cls) {
        if (Double.class.isAssignableFrom(cls))
            return ITableModel.NUMERIC_TYPE;
        else if (Integer.class.isAssignableFrom(cls))
            return ITableModel.INTEGER_TYPE;
        else if (Float.class.isAssignableFrom(cls))
            return ITableModel.FLOAT_TYPE;
        else if (String.class.isAssignableFrom(cls))
            return ITableModel.ALPHANUMERIC_TYPE;
        else if (Boolean.class.isAssignableFrom(cls))
            return ITableModel.BOOLEAN_TYPE;
        else if (CDate.class.isAssignableFrom(cls))
            return ITableModel.DATE_TYPE;
        else if (CTime.class.isAssignableFrom(cls))
            return ITableModel.TIME_TYPE;
        else if (URL.class.isAssignableFrom(cls))
            return ITableModel.URL_TYPE;
        else if (CImageIcon.class.isAssignableFrom(cls))
            return ITableModel.ICON_TYPE;
        return ITableModel.NO_TYPE;
    }

    public Class getColumnType(int colIndex) {
        try{
            AbstractTableField f = getTableField(colIndex);
            return f.getDataType();
        }catch (InvalidFieldIndexException exc) {
            return null;
        }
    }

    /**
     *  Returns the number of records in the Table. Method alias for <code>getRecordCount()</code>.
     */
    public int getRowCount() {
        return getRecordCount();
    }

    /**
     *  Returns the number of fields in the Table. Method alias for <code>getFieldCount()</code>.
     */
    public int getColumnCount() {
        return getFieldCount();
    }

    /**
     *  Returns the name of the column with index <code>colIndex</code>. Method alias for
     *  <code>getFieldName</code>.
     *  @param colIndex The index of the field.
     */
    public String getColumnName(int colIndex) {
        try{
            return getFieldName(colIndex);
        }catch (InvalidFieldIndexException exc) {
            return null;
        }
    }

    /**
     *  Sets the name of the column with index <code>colIndex</code> to <code>colName</code>.
     *  @param colIndex The index of the column.
     *  @param colName The new name of the column.
     */
    public void setColumnName(int colIndex, String colName) {
        try{
            String currentName = getFieldName(colIndex);
            renameField(currentName, colName);
        }catch (InvalidFieldIndexException exc) {}
        catch (FieldNameInUseException exc) {}
        catch (InvalidFieldNameException exc) {}
    }

    /**
     *  Returns the data of the column named <code>colName</code>. Method alias for
     *  <code>getField()</code>.
     *  @param colName The name of the column.
     */
    public ArrayList getColumn(String colName) {
        try{
            int fieldIndex = getFieldIndex(colName);
            if (fieldIndex == -1) return null;
            return tableFields.get(fieldIndex).getDataArrayList();
//            return (ArrayList) tableData.get(fieldIndex);
//            return convertArrayToArrayList(getField(colName));
        }catch (InvalidFieldNameException exc) {return null;}
    }

    /**
     *  Returns the data of the column at index <code>colIndex</code>. Method alias for
     *  <code>getField()</code>.
     *  @param colIndex The index of the column.
     */
    public ArrayList getColumn(int colIndex) {
        try{
//            return convertArrayToArrayList(getField(colIndex));
//            return (ArrayList) tableData.get(colIndex);
            return tableFields.get(colIndex).getDataArrayList();
        }catch (Throwable exc) {
            return null;
        }
    }

    /**
     *  Sets the value of a cell of the table. This is an alias method for
     *  <code>setCell()</code>. The method is defined by the <code>ITableModel</code> interface.
     *  @param row The index of the row to which the cell belongs.
     *  @param col The index of the column to which the cell belongs.
     *  @param value The new value of the cell.
     *  @param insert The Table implementation of this method does not make use of this
     *                parameter.
     */
    public void setCellValue(int row, int col, Object value, boolean insert) {
        try{
            setCell(col, row, value);
        }catch (Exception exc) {
        }
    }

    /**
     *  Returns the value of a cell of the table. This is an alias method for
     *  <code>getCell()</code>. The method is defined by the <code>ITableModel</code> interface.
     *  @param row The index of the row to which the cell belongs.
     *  @param col The index of the column to which the cell belongs.
     */
    public Object getCellValue(int row, int col) {
        try{
            return getCell(col, row);
        }catch (Exception exc) {
            return null;
        }
    }

    public int getColumnIndex(String colName) {
        try{
            return getFieldIndex(colName);
        }catch (Exception exc) {
            return -1;
        }
    }

    public int getInternalRowNumber(int row) {
        return recordForRow(row);
    }

    public int getExternalRowNumber(int record) {
        return rowForRecord(record);
    }

    /** 'row' version of <code>setActiveRecord()</code>. Delegates all its functionality to
     *  <code>setActiveRecord()</code>.
     */
    public void setActiveRow(int record) {
        setActiveRecord(record);
    }

    /** 'row' version of <code>getActiveRecord()</code>. Delegates all its functionality to
     *  <code>getActiveRecord()</code>.
     */
    public int getActiveRow() {
        return activeRecord;
    }

    /** 'row' version of <code>isRecordSelected()</code>. Delegates all its functionality to
     *  <code>isRecordSelected()</code>.
     */
    public boolean isRowSelected(int record) {
        return isRecordSelected(record);
    }

/*1    public void setTableView(TableView view) {
        tableView = view;
    }

    public TableView getTableView() {
        return tableView;
    }
1*/
    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);
            long start = System.currentTimeMillis();
//long start = System.currentTimeMillis();
//System.out.println("getESlateHandle() 0: " + (System.currentTimeMillis()-start));
            handle = ESlate.registerPart(this);
//System.out.println("getESlateHandle() 0.1: " + (System.currentTimeMillis()-start));
//            attachTimers();
//System.out.println("getESlateHandle() 1: " + (System.currentTimeMillis()-start));

            try{
                if (getTitle() != null){
//System.out.println("getESlateHandle() 1.1: " + (System.currentTimeMillis()-start) + ", title: " + getTitle());
                    setTableHandleNameToTitle(null, true);
//System.out.println("getESlateHandle() 1.2: " + (System.currentTimeMillis()-start));
                }else{
                    handle.setUniqueComponentName(bundle.getString("Table"));
                }
            }catch (RenamingForbiddenException exc) {exc.printStackTrace();}
//System.out.println("getESlateHandle() 2: " + (System.currentTimeMillis()-start));
            try {
//                try {
//                Class soClass=Class.forName("gr.cti.eslate.sharedObject.TableSO");
//                TableSO tableSO=new TableSO(handle);
//                tableSO.setTable(this);
//long s = System.currentTimeMillis();
                tablePlug = new TablePlug(this); //soClass, tableSO);
//System.out.println("TablePlug time: " + (System.currentTimeMillis()-s));
//                tablePlug.updatePlugValues(getActiveRecord());
                handle.addPlug(tablePlug);
                handle.addPlug(tablePlug.getRecordPlug());
            }catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("Something went wrong during TablePlug creation: " + exc.getMessage());
            }

//System.out.println("getESlateHandle() 3: " + (System.currentTimeMillis()-start));
            handle.addESlateListener(new ESlateAdapter() {
                public void disposingHandle(HandleDisposalEvent e) {
//    System.out.println("Table disposingHandle() : " + handle.getComponentName() + ", this: " + this);
                    e.stateChanged = isModified;
                }
                public void handleDisposed(HandleDisposalEvent e) {
                    PerformanceManager pm = PerformanceManager.getPerformanceManager();
/*                    PerformanceTimerGroup ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
pm.removePerformanceTimerGroup(ptg, constructorTimer);
ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
pm.removePerformanceTimerGroup(ptg, loadTimer);
ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
pm.removePerformanceTimerGroup(ptg, saveTimer);
ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
//                    pm.removePerformanceTimerGroup(ptg, initESlateAspectTimer);
*/
//Thread.currentThread().dumpStack();
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
//    System.out.println("Table handleDisposed(): " + getTitle());
                }
                public void componentNameChanged(ComponentNameChangedEvent e) {
                    String newName = e.getNewName();
                    if (getTitle().equals(newName)) return;
                    try{
                        setTitle(newName);
                    }catch (PropertyVetoException exc) {
                        ESlateOptionPane.showMessageDialog(null, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        try{
                            handle.setUniqueComponentName(e.getOldName());
//System.out.println("5. Called setUniqueComponentName");
                        }catch (RenamingForbiddenException exc1) {
                            System.out .println("Inconsistency between the title of the Table + " + title + " and the name of its ESlateHandle");
                        }
                    }catch (InvalidTitleException exc) {
                        ESlateOptionPane.showMessageDialog(null, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        try{
                            handle.setUniqueComponentName(e.getOldName());
//System.out.println("6. Called setUniqueComponentName");
                        }catch (RenamingForbiddenException exc1) {
                            System.out .println("Inconsistency between the title of the Table + " + title + " and the name of its ESlateHandle");
                        }
                    }catch (Throwable exc) {
                        ESlateOptionPane.showMessageDialog(null, exc.getMessage(), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        try{
                            handle.setUniqueComponentName(e.getOldName());
//System.out.println("7. Called setUniqueComponentName");
                        }catch (RenamingForbiddenException exc1) {
                            System.out .println("Inconsistency between the title of the Table + " + title + " and the name of its ESlateHandle");
                        }
                    }
                }
            });
//System.out.println("getESlateHandle() 4: " + (System.currentTimeMillis()-start));
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.TablePrimitives");

//System.out.println("getESlateHandle() 5: " + (System.currentTimeMillis()-start));
            pm.eSlateAspectInitEnded(this);
            pm.stop(initESlateAspectTimer);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
//System.out.println("Table getESlateHandle() end: " + (System.currentTimeMillis()-start));
        }

        return handle;
    }

    /** Return the Pin that exports the Table. If the E-Slate side of the Table has
     *  not been initialized, <code>null</code> is returned.
     */
    public TablePlug getTablePlug() {
        if (handle == null || handle.getComponentName() == null) return null;
        return tablePlug;
    }

    /** Creates a TableView on this Table.
     * @param fieldIndices The indices of the Table's fields, which will be included in the TableView.
     * @param recordIndises The indices of the records of the Table, which will be included in the TaleView.
     * @return The new TableView.
     * @throws InvalidRecordIndexException If any of the record indices in <code>recordIndises</code> is invalid.
     * @throws InvalidFieldIndexException If any of the field indices in <code>fieldIndices</code> is invalid.
     * @see TableView
     */
    public TableView createTableView(IntBaseArray fieldIndices, IntBaseArray recordIndises)
            throws InvalidRecordIndexException, InvalidFieldIndexException {
        TableView tableView = new TableView(this, fieldIndices, recordIndises);
        getESlateHandle().add(tableView.getESlateHandle());
        if (tableViews == null)
            tableViews = new ArrayList();
        tableViews.add(tableView);
//System.out.println("createTableView() tableViews.size(): " + tableViews.size());
        setModified();
        return tableView;
    }

    /** Returns the list of the children table views of this Table. These views were created by <code>createTableView</code>.
     * The list may be null, if there are no children table views.
     * @see #createTableView(IntBaseArray, IntBaseArray)
     */
    public ArrayList getTableViews() {
        return tableViews;
    }

    /** Removes the specified TableView from the list of views of this Table.
     * @param view The TableView to be removed.
     * @return true, if the view was part of the list of views of this Table and was succesfully removed.
     *         false, in any other case.
     */
    public boolean removeTableView(TableView view) {
        if (view == null) return false;
        boolean removed = tableViews.remove(view);
        if (removed) {
            getESlateHandle().remove(view.getESlateHandle());
            view.getESlateHandle().toBeDisposed(false, new BooleanWrapper(false));
            view.getESlateHandle().dispose();
        }
        if (tableViews.size() == 0)
            tableViews = null;
        setModified();
        return removed;
    }


    /** Updates this Table to have exactly the same data as the provided Table.
     *  This is done by removing all the fields of the Table, adding the fields
     *  of the provided Table and then all the records of the provided Table.
     */
    public void setTable(Table table) {
        String[] fieldNames = new String[getFieldCount()];
        for (int i=0; i<tableFields.size(); i++)
            fieldNames[i] = tableFields.get(i).getName();

        // Back-up the 'fieldRemovalAllowed' attribute. Set it to true, so that
        // the fields of the Table can be removed.
        boolean fieldRemovalAllowedTmp = fieldRemovalAllowed;
        fieldRemovalAllowed = true;
        // Back-up the 'recordRemovalAllowed' attribute. Set it to true, so that
        // the records of the Table can be removed. This will happen after the last
        // field is removed.
        boolean recordRemovalAllowedTmp = recordRemovalAllowed;
        recordRemovalAllowed = true;
        // Back-up the 'keyChangeAllowed' attribute. Set it to true, so that
        // key of the Table can change. Since the Table will loose all its fields,
        // its key will inevitably have to change.
        boolean keyChangeAllowedTmp = keyChangeAllowed;
        keyChangeAllowed = true;

        for (int i=fieldNames.length-1; i>=0; i--) {
            AbstractTableField field = tableFields.get(i);
            field.setFieldKeyAttributeChangeAllowed(true);
            field.setFieldRemovabilityChangeAllowed(true);
            try{
                field.setRemovable(true);
            }catch (AttributeLockedException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 0. The Table may be damaged");
                exc.printStackTrace();
                return;
            }
            //Normally no exception should occur. They should all be controlled
            //not to happen in any case.
            try{
                removeField(fieldNames[i], false);
            }catch (InvalidFieldNameException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 1. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (TableNotExpandableException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 2. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (CalculatedFieldExistsException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 3. The Table may be damaged");
                exc.printStackTrace();
            }catch (FieldNotRemovableException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 4. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (AttributeLockedException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 5. The Table may be damaged");
                exc.printStackTrace();
                return;
            }
        }

        /* Vetos to propertyChanges are prohibited, since the whole work is done
        * undex the hood and HAS to succeed.
        */
        tableVetoableChangeSupport.staySilent = true;
        // Back-up the 'fieldAdditionAllowed' attribute. Set it to true, so that
        // key the new fields can be added to the table.
        boolean fieldAdditionAllowedTmp = fieldAdditionAllowed;
        fieldAdditionAllowed = true;
        TableFieldBaseArray newTableFields = table.tableFields;
        for (int i=0; i<newTableFields.size(); i++) {
            //Normally all the exceptions should be controlled and none should occur.
            try{
                addField(newTableFields.get(i));
            }catch (AttributeLockedException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 6. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (IllegalCalculatedFieldException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 7. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (InvalidFormulaException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 8. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (InvalidFieldNameException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 9. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (InvalidKeyFieldException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 10. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (InvalidFieldTypeException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 11. The Table may be damaged");
                exc.printStackTrace();
                return;
            }
        }
        tableVetoableChangeSupport.staySilent = false;
        // Back-up the 'recordAdditionAllowed' attribute. Set it to true, so that
        // the records acan be added to the table.
        boolean recordAdditionAllowedTmp = recordAdditionAllowed;
        recordAdditionAllowed = true;
        for (int i=0; i<table.getRecordCount(); i++) {
            try{
                ArrayList r = table.getRow(i);
                addRecord(r, false);
            }catch (InvalidRecordIndexException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 12. The Table may be damaged");
            }catch (InvalidDataFormatException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 13. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (NoFieldsInTableException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 14. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (NullTableKeyException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 15. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (DuplicateKeyException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 16. The Table may be damaged");
                exc.printStackTrace();
                return;
            }catch (TableNotExpandableException exc) {
                System.out.println("Serious inconsistency error in Table setTable(): 16. The Table may be damaged");
                exc.printStackTrace();
                return;
            }
        }

        // Restore the backed-up attributes.
        fieldRemovalAllowed = fieldRemovalAllowedTmp;
        recordRemovalAllowed = recordRemovalAllowedTmp;
        keyChangeAllowed = keyChangeAllowedTmp;
        fieldAdditionAllowed = fieldAdditionAllowedTmp;
        recordAdditionAllowed = recordAdditionAllowedTmp;
    }

    /** Replaces in this Table the supplied field <code>f</code> with a new AbstractTableField, which is of the specified data
     * type. If <code>f</code> does not belong to this Table, nothing happens. Also nothing happends if <code>f</code>
     * is of the same type as the <code>newDataType</code>.
     * @return  The new field of the specified data type.
     */
    AbstractTableField changeFieldDataType(AbstractTableField f, Class newDataType) throws AttributeLockedException, InvalidFieldTypeException {
        if (!f.isFieldDataTypeChangeAllowed())
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg2"));

        int index = tableFields.indexOf(f);
        if (index == -1)
            return null;
        if (f.getDataType().equals(newDataType))
            return f;
        AbstractTableField newField = AbstractTableField.createNewField(f.getName(), newDataType, f.isEditable(), f.isRemovable, f.isHidden);
        newField.setTable(this);
        newField.indexInTable = index;
        newField.oe = f.oe;
        newField.dependentCalcFields = f.dependentCalcFields;
        newField.dependsOnFields = f.dependsOnFields;
        newField.formula = f.formula;
        newField.textFormula = f.textFormula;
        newField.calculatedValue = f.calculatedValue;
        newField.setFieldEditabilityChangeAllowed(f.isFieldEditabilityChangeAllowed());
        newField.setFieldRemovabilityChangeAllowed(f.isFieldRemovabilityChangeAllowed());
        newField.setFieldDataTypeChangeAllowed(f.isFieldDataTypeChangeAllowed());
        newField.setCalcFieldResetAllowed(f.isCalcFieldResetAllowed());
        newField.setFieldKeyAttributeChangeAllowed(f.isFieldKeyAttributeChangeAllowed());
        newField.setFieldHiddenAttributeChangeAllowed(f.isFieldHiddenAttributeChangeAllowed());
        newField.setCalcFieldFormulaChangeAllowed(f.isCalcFieldFormulaChangeAllowed());
        newField.linkToExternalData = f.linkToExternalData;
        newField.sorted = f.sorted;
        newField.sortDirection = f.sortDirection;

        tableFields.set(index, newField);
        return newField;
    }

    /**
     *  Exports a Table to an delimited ascii file. Two delimeters are supported: ';' and
     *  ','. Data may be stored in the file quoted or as is.
     *  @param fileName The absolute or relative path of the ascii file, to which the table will be exported.
     *  @param delim The delimiter character to be used.
     *  @param quoteData <i>true</i> to generated quoted data. <i>false</i>, to store data as is.
     *  @return <i>true</i>, if the operation is succesfull. <i>false</i>, if the Table contains
     *          no fields and therefore no data.
     *  @exception  UnableToWriteFileException If the supplied path is incorrect, or the
     *              name of the file is invalid, or an I/O problem occurs.
     *  @exception  InvalidDelimeterException If an unsupported delimiter is supplied.
     */
    public boolean exportTableToTextFile(String fileName, char delim, boolean quoteData)
    throws UnableToWriteFileException, InvalidDelimeterException, InsufficientPrivilegesException {
        int fieldCount;
        if ((fieldCount = getFieldCount()) == 0)
            return false;

        if (fileName.indexOf(".txt") == -1)
            fileName = fileName + ".txt";
        else if (fileName.indexOf(".txt") != fileName.length()-4)
            fileName = fileName + ".txt";

        FileWriter fw = null;
        try{
            fw = new FileWriter(fileName);
        }catch (IOException e) {throw new UnableToWriteFileException(DBase.resources.getString("CDatabaseMsg32") + fileName + DBase.resources.getString("CDatabaseMsg33"));}

        BufferedWriter bw = new BufferedWriter(fw, 30000);
        PrintWriter pw = new PrintWriter(bw);

        String line = "";
//        boolean quoteData = false;

        /* Supported delimeters: ',' and ';'
         */
        try{
            if (!(delim==',') && !(delim == ';')) {
                bw.close();
                throw new InvalidDelimeterException(DBase.resources.getString("CDatabaseMsg19") + delim + DBase.resources.getString("CDatabaseMsg20"));
            }

//            quoteData = true;


            /* Create an int array which will hold null and boolean values. This array
             * will be used to spot Date and Time values, so that they won't be writen
             * in the text file, as they are (through a call to method "toString()", but
             * they will be properly formatted through SimpleDateFormat classes "sdf1"
             * and "stf1". In this array which has as many entries as the fields of the
             * table, an entry can be:
             *     - 0, if the corresponding field is not of Date or Time or Image data type,
             *     - 1, if the field is of Date data type, and
             *     - 2, if the field is of Time data type.
             *     - 3, if the field is of Image data type.
             */
            int[] specialFields = new int[fieldCount];
            for (int i=0; i<fieldCount; i++) {
                if (tableFields.get(i).getDataType().getName().equals("gr.cti.eslate.database.engine.CDate")) { //java.util.Date")) {
//                    if (((TableField) TableFields.get(i)).isDate())
                        specialFields[i] = 1;
                }else if (tableFields.get(i).getDataType().getName().equals("gr.cti.eslate.database.engine.CTime")) { //java.util.Date")) {
                        specialFields[i] = 2;
                }else if (tableFields.get(i).getDataType().equals(CImageIcon.class))
                    specialFields[i] = 3;
                else if (tableFields.get(i).getDataType().equals(Double.class))
                    specialFields[i] = 4;
                else
                    specialFields[i] = 0;
//                System.out.println(((TableField) CTableFields.get(i)).getFieldType().getName() + " " + specialFields[i]);
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy G");
            SimpleDateFormat stf1 = new SimpleDateFormat("H:mm");
            TableNumberFormat dnf = getNumberFormat();


            StringBaseArray fieldNames = getFieldNames();
            if (quoteData) {
                int i;
                for (i=0; i<fieldNames.size()-1; i++)
                    line = line + "\"" + fieldNames.get(i) + "\"" + delim;
                line = line +"\"" + fieldNames.get(i) + "\"";
            }else{
                int i;
                for (i=0; i<fieldNames.size()-1; i++)
                    line = line + fieldNames.get(i) + delim;
                line = line + fieldNames.get(i);
            }
            pw.println(line);

            ArrayList rec = new ArrayList();
            rec.ensureCapacity(fieldCount);
            String tata = "";
            for (int i=0; i<fieldCount; i++)
                rec.add(tata);

            Object data;
            int k;
            if (quoteData) {
                for (int i=0; i<getRecordCount(); i++) {
                    line = "";
                    /* Fill Array "rec" with the contents of the "i"th record
                     * of the "table".
                     */
                    for (int n=0; n<fieldCount; n++)
                        rec.set(n, tableFields.get(n).getObjectAt(i));
//                        rec.set(n, ((ArrayList) tableData.get(n)).get(i));

                    /* Write the contents of Array "rec" to string "line.
                     */
                    for (k=0; k<fieldCount-1; k++) {
                        data = rec.get(k);
                        if (data != null) {
//                            System.out.println(data + " " + specialFields[k]);
                            if (specialFields[k] == 0)
                                line = line + "\"" + data.toString() + "\"" + delim;
                            else{
                                if (specialFields[k] == 1)
                                    line = line + "\"" + sdf1.format(data) + "\"" + delim;
                                else if (specialFields[k] == 2)
                                    line = line + "\"" + stf1.format(data) + "\"" + delim;
                                else if (specialFields[k] == 4)
                                    line = line + "\"" + dnf.format((Double) data) + "\"" + delim;
                                else
                                    line = line + "\"" + ((CImageIcon) data).getFileName() + "\"" + delim;
                            }
                        }else
                            line = line + delim;
                    }

                    data = rec.get(k);
                    if (data != null) {
//                        System.out.println(data + " " + specialFields[k]);
                        if (specialFields[k] == 0)
                            line = line + "\"" + data.toString() + "\"";
                        else{
                            if (specialFields[k] == 1)
                                line = line + "\"" + sdf1.format(data) + "\"";
                            else if (specialFields[k] == 2)
                                line = line + "\"" + stf1.format(data) + "\"";
                            else if (specialFields[k] == 4)
                                line = line + "\"" + dnf.format((Double) data) + "\"";
                            else
                                line = line + "\"" + ((CImageIcon) data).getFileName() + "\"";
                        }
                    }else
                        line = line;

                    /* Write the "line" to the PrintWriter "pw".
                     */
                    pw.println(line);
                }
            }else{
                for (int i=0; i<getRecordCount(); i++) {
                    line = "";
                    /* Fill Array "rec" with the contents of the "i"th record
                     * of the "table".
                     */
                    for (int n=0; n<fieldCount; n++)
                        rec.set(n, tableFields.get(n).getObjectAt(i));
//                        rec.set(n, ((ArrayList) tableData.get(n)).get(i));

                    /* Write the contents of Array "rec" to string "line.
                     */
                    for (k=0; k<fieldCount-1; k++) {
                        data = rec.get(k);
                        if (data != null) {
//                            System.out.println(data + " " + specialFields[k]);
                            if (specialFields[k] == 0)
                                line = line + data.toString() + delim;
                            else{
                                if (specialFields[k] == 1)
                                    line = line + sdf1.format(data) + delim;
                                else if (specialFields[k] == 2)
                                    line = line + stf1.format(data) + delim;
                                else if (specialFields[k] == 4)
                                    line = line + dnf.format((Double) data) + delim;
                                else
                                    line = line + ((CImageIcon) data).getFileName() + delim;
                            }
                        }else
                            line = line + delim;
                    }

                    data = rec.get(k);
                    if (data != null) {
//                        System.out.println(data + " " + specialFields[k]);
                        if (specialFields[k] == 0)
                            line = line + data.toString();
                        else{
                            if (specialFields[k] == 1)
                                    line = line + sdf1.format(data);
                            else if (specialFields[k] == 2)
                                line = line + stf1.format(data);
                            else if (specialFields[k] == 4)
                                line = line + dnf.format((Double) data);
                            else
                                line = line + ((CImageIcon) data).getFileName();
                        }
                    }else
                        line = line;

                    /* Write the "line" to the PrintWriter "pw".
                     */
                    pw.println(line);
                }
            }

            pw.close();

        }catch (IOException e) {throw new UnableToWriteFileException(DBase.resources.getString("CDatabaseMsg34") + fileName + "\"");}

        return true;
    }

    /**
     *  Exports the specified Table to the specified <i>.ctb</i> file.
     *  @param fileName The name of the file.
     *  @return <i>true</i>, if the table is saved succesfully. <i>false</i>, if the operation
     *          was cancelled by the user.
     *  @exception  InvalidPathException If the supplied file name is incorrect.
     *  @exception  UnableToSaveException If the file cannot be writen because it is in use,
     *              or some I/O problem occurs.
     *  @see    DBase#analyzeFileName(java.lang.String, java.lang.String)
     */
    public boolean exportTable(String fileName, boolean noOverwriteConfirmation)
    throws InvalidPathException, UnableToSaveException, InsufficientPrivilegesException {
        File file = null;
//        System.out.println("fileName: " + fileName);
        try{
            file = DBase.analyzeFileName(fileName, "ctb");
        }catch (InvalidPathException e) {throw new InvalidPathException(e.message);}

        if (file.exists()) {
            if (!file.canWrite())
                throw new UnableToSaveException(DBase.resources.getString("CDatabaseMsg43") + file.getPath() + "\"");
            else{
//                System.out.print("Overwrite existing file? ");
//                String s2 = getStringSTDIN();
//                if (!s2.trim().toLowerCase().equals("y"))
                if (!noOverwriteConfirmation) {
                    String title;
                    if (getTitle() == null)
                        title = DBase.resources.getString("CDatabaseMsg44");
                    else
                        title = DBase.resources.getString("CDatabaseMsg44") + " \"" + getTitle() + "\"";

                    if ((ESlateOptionPane.showConfirmDialog(new JFrame(), DBase.resources.getString("CDatabaseMsg58"), title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION))
                        return false;
                }
            }
        }

//        System.out.println(file.getPath());

//        File previousFile = null;
        boolean titleSetToFileName = false;
        try{
            /* If the title of the Table is not set, then set it to the name
             * of the file in which it is stored.
             */
            if (getTitle() == null || getTitle().trim().length() == 0) {
//                System.out.println("Separator: " + System.getProperty("file.separator"));
                String title = fileName.substring(fileName.lastIndexOf(System.getProperty("file.separator"))+1, fileName.lastIndexOf('.'));
                try{
                    setTitle(title);
///                    if (!hasUniqueTitle(table, this))
///                        table.setTitle(generateUniqueTitle(table, this)); //_setTitle
                    titleSetToFileName = true;
                }catch (InvalidTitleException e) {
                    //leave the table untitled
                }
                catch (PropertyVetoException e) {}
            }

            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file), 30000));
            /* Set this Table data members here, so as to be stored.
             */
//            previousFile = activeTable.file;
//            activeTable.file = file;
//            System.out.println("Saving active ctable to : " + file);
            out.writeObject(this);
//            table.writeExternal(out);
            out.flush();
            out.close();
            isModified = false;
        }catch (IOException e) {
            e.printStackTrace();
//1            try{
//1            if (titleSetToFileName)
//1                table.setTitle(generateUniqueTitle(table, this));
//1            }catch (InvalidTitleException exc) {}
//1             catch (PropertyVetoException exc) {}
//            activeTable.file = previousFile;
            throw new UnableToSaveException(DBase.resources.getString("CDatabaseMsg48") + getTitle() + DBase.resources.getString("CDatabaseMsg49") + file.getPath() + "\"");
        }
        return true;
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener != null) return;
        perfListener = new PerformanceAdapter() {
            public void performanceManagerStateChanged(PropertyChangeEvent e) {
                boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
                // When the Performance Manager is enabled, try to attach the
                // timers.
                if (enabled) {
                    attachTimers();
                }
            }
        };
        pm.addPerformanceListener(perfListener);
    }

    /**
     * This method creates and attaches the component's timers. The timers are
     * created only once and are assigned to global variables. If the timers
     * have been already created, they are not re-created. If the timers have
     * been already attached, they are not attached again.
     * This method does not create any timers while the PerformanceManager is
     * disabled.
     */
    private void attachTimers() {
        long start = System.currentTimeMillis();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        boolean pmEnabled = pm.isEnabled();

        // If the performance manager is disabled, install a listener which will
        // re-invoke this method when the performance manager is enabled.
        if (!pmEnabled && (perfListener == null)) {
            createPerformanceManagerListener(pm);
        }

        // Do nothing if the PerformanceManager is disabled.
        if (!pmEnabled) {
            return;
        }

        boolean timersCreated = (loadTimer != null);
        // If the timers have already been constructed and attached, there is
        // nothing to do.
        if (timersCreated) {
            return;
        }
        // Get the default global performance timer groups for:
        // 1. Component construction
        // 2. Construction of the E-Slate aspect of the components
        // 3. Component save
        // 4. Component restore.
/*        PerformanceTimerGroup constructorGlobalGroup =
pm.getPerformanceTimerGroupByID(PerformanceManager.CONSTRUCTOR);
PerformanceTimerGroup eslateAspectGlobalGroup =
pm.getPerformanceTimerGroupByID(PerformanceManager.INIT_ESLATE_ASPECT);
PerformanceTimerGroup saveGlobalGroup =
pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
PerformanceTimerGroup loadGlobalGroup =
pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
*/
        // Special case: E-Slate aspect initialization timer. This timer is
        // created individually, because it has to be added to the default global
        // group INIT_ESLATE_ASPECT first and then to the performance timer group
        // of the component. It is the only way to get a measurement by this
        // timer: if the timer is created as a child of the performance timer
        // group of the component, then the component's E-Slate handle (and
        // therefore its E-Slate aspect) will have been created before the timer,
        // making it impossible to measure the time required for the
        // initialization of the component's E-Slate aspect using this timer.
/*        initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
eslateAspectGlobalGroup, bundle.getString("InitESlateAspectTimer"), true
);
*/
        // Get the performance timer group for this component.
        PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
        // Construct and attach the component's timers.
        constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("ConstructorTimer"), true
        );
        loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("LoadTimer"), true
        );
        saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("SaveTimer"), true
        );
        initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                compoTimerGroup, bundle.getString("InitESlateAspectTimer"), true
        );
        fieldMapTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
                loadTimer, bundle.getString("FieldMapTimer"), true
        );
        pm.registerPerformanceTimerGroup(
                PerformanceManager.CONSTRUCTOR, constructorTimer, this
        );
        pm.registerPerformanceTimerGroup(
                PerformanceManager.LOAD_STATE, loadTimer, this
        );
        pm.registerPerformanceTimerGroup(
                PerformanceManager.SAVE_STATE, saveTimer, this
        );
        pm.registerPerformanceTimerGroup(
                PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
        );

//        ESlateHandle h = getESlateHandle();
//        PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(h);
        // Add the "initESlateAspectTimer" to the component's performance timer
        // group.
//        pm.addPerformanceTimerGroup(compoTimerGroup, initESlateAspectTimer);

        // If the component's timers have not been constructed yet, then
        // construct them. During constuction, the timers are also attached.
//        constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
//          compoTimerGroup, bundle.getString("ConstructorTimer"), true
//        );
/*        loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
compoTimerGroup, bundle.getString("LoadTimer"), true
);
saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
compoTimerGroup, bundle.getString("SaveTimer"), true
);
*/
//        pm.registerPerformanceTimerGroup(pm.CONSTRUCTOR, constructorTimer, h);
/*        pm.registerPerformanceTimerGroup(pm.LOAD_STATE, loadTimer, h);
pm.registerPerformanceTimerGroup(pm.SAVE_STATE, saveTimer, h);
*/
        attachTimersTime = attachTimersTime + (System.currentTimeMillis()-start);
//        pm.addPerformanceTimerGroup(constructorGlobalGroup, constructorTimer);
//        pm.addPerformanceTimerGroup(loadGlobalGroup, loadTimer);
//        pm.addPerformanceTimerGroup(saveGlobalGroup, saveTimer);
    }

/*    private int indexOf(int start, int end, ArrayList al, Object value) {
        for (int i=start; i<=end; i++) {
            if (al.get(i) == value)
                return i;
        }
        return -1;
    }
*/
/*    private void copyArrayList(ArrayList source, ArrayList target) {
if (source == target)
return;

synchronized( target ) {
if ( target.size() > source.size() ) {
myStorage = new Object[ array.myLength ];
System.arraycopy( array.myStorage, 0, myStorage, 0, array.myLength );
}
else if ( myLength > array.myLength )
{
System.arraycopy( array.myStorage, 0, myStorage, 0, array.myLength );

for ( int i = array.myLength; i < myLength; i++ )
myStorage[ i ] = null; // To allow garbage collection.
}
else
{
System.arraycopy( array.myStorage, 0, myStorage, 0, array.myLength );
}

myLength = array.myLength;
}
}
}
*/
}

class TableVetoableChangeSupport extends VetoableChangeSupport {
    boolean staySilent = false;
    public TableVetoableChangeSupport(Object sourceBean) {
        super(sourceBean);
    }
    public void fireVetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (staySilent) return;
        super.fireVetoableChange(evt);
    }
}
