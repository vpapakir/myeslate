package gr.cti.eslate.database.engine;

import gr.cti.eslate.tableModel.ITableModel;
import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.utils.*;
import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.TableSO;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import com.objectspace.jgl.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.Serializable;
import java.io.Externalizable;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import java.awt.Color;


/**
 * @version	1.4, 1-Nov-98
 */
/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.Table.
 */
class CTable implements Serializable, ITableModel, ESlatePart {
    /** The version of the storage format of the CTable class
     */
    // 1.0 --> 1.1 selectedSubset and unselectedSubset turned to IntBaseArrayDescs from JGL Arrays
    public static final String STR_FORMAT_VERSION = "1.1";

    /** The DBase to which the CTable belongs.
     */
    protected transient DBase database;
    /** The CTable's data container.
     */
    protected Array tableData = new Array(); //"protected" in order to speed-up access from other classes
    /** The Array of fields of the CTable.
     */
    protected Array CTableFields = new Array();
    /** The Array of records in the CTable.
     */
    protected Array CTableRecords = new Array();
    /** The Array of record selection status in the CTable.
     */
    protected BoolBaseArray recordSelection = new BoolBaseArray();
//    private Array recordEntryForm = new Array();
    /** Specifies whether records can be added to the table.
     */
    private boolean recordAdditionAllowed; //recordAdditionAllowed
    /** Specifies whether records can be removed from the table.
     */
    private boolean recordRemovalAllowed;
    /** Specifies whether fields can be added to the table.
     */
    private boolean fieldAdditionAllowed;
    /** Specifies whether fields can be removed from the table.
     */
    private boolean fieldRemovalAllowed;
    /** Specifies whether fields can be reordered in the table.
     */
    private boolean fieldReorderingAllowed;
    /** Specifies whether the table's key can be changed.
     */
    private boolean keyChangeAllowed;
    /** Specifies whether the table's hidden fields are displayed or not.
     */
    private boolean hiddenFieldsDisplayed;
    /** Specifies whether the table's hidden fields are displayed or not.
     */
    private boolean hiddenAttributeChangeAllowed;
    /** Specifies whether the table's data can be changed.
     */
    private boolean dataChangeAllowed;
    /** Contains primary key's values.
     */
    protected HashMap primaryKeyMap;
    /** Contains the indices of the fields which constitute the CTable's key.
     */
    protected Array keyFieldIndices = new Array();
    /** Specifies whether the CTable has a key.
     */
    private boolean hasKey;
    /** The number of records in the CTable.
     */
    private int recordCount;
    /** The CTable's title.
     */
    private String title;
    /** The CTable's metadata.
     */
    private String metadata;
    /** The subset of selected records of the CTable.
     */
    protected IntBaseArrayDesc selectedSubset = new IntBaseArrayDesc();
    /** The subset of unselected records of the CTable.
     */
    protected IntBaseArrayDesc unselectedSubset = new IntBaseArrayDesc();
    /** Reports whether the CTable has been modified.
     */
    protected transient boolean isModified = false;
    /** The file which stores the CTable.
     */
//    public File file;


    //Date and Time formats for data entry
    /** The format in which dates are processed in the CTable.
     */
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH");

    /** The format used for the String representation of dates.
     *  @see #setDateFormat(String)
     *  @see #getDateFormat()
     */
    SimpleDateFormat dateFormat;

    /** The format in which time values are processed in the CTable.
     */
    SimpleDateFormat stf = new SimpleDateFormat("H:mm");

    /** The format used for the String representation of time values.
     *  @see #setTimeFormat(String)
     *  @see #getTimeFormat()
     */
    SimpleDateFormat timeFormat;

    /** Alternative date formats.
     */
    static Array dateFormats = null;

    /** Alternative time formats.
     */
    static Array timeFormats = null;

    /** Date samples for each of the supported date formats.
     */
    static String[] sampleDates = null;

    /** The CTable Number format.
     */
    public DoubleNumberFormat numberFormat;

    protected int numOfCalculatedFields;

    private static short timeToGarbageCollect = 0;

    /** Used to correct input date values.
     */
    private static Calendar calendar = new GregorianCalendar();

    private boolean calculatedFieldsChanged;
    private HashMap changedCalcCells;
    protected boolean pendingNewRecord;

    /** This Array stores table-specific attributes, which are not used by the database engine, but
     *  by the database UI. So it's up to the UI builder to store any attributes in this Array, which
     *  need to persist from session to session.
     */
    public Array UIProperties = new Array();

    /** This int array stores the correspondence between the record's of the CTable and the rows
     *  at which they are displayed. The record order inside the CTable never changes. The first
     *  record inserted will always be the first record of the CTable, until it is removed from it.
     *  However the row at which the record is displayed may change. The term 'row' does not only refer
     *  to the rows of a tabular UI. It may refer to columns in a column chart, etc. The CTable API
     *  provides basic support for the record's visual position. For any CTable, the visual position
     *  of any of its record is the same accross all the visual representations of the CTable. The
     *  <i>recordIndex</i> array stores the actual, constant record index of each row, e.g.
     *  <i>recordIndex[k]</i> returns the record index of the k-th row. When the row order changes
     *  a RowOrderChanged event is send to all the visual representations of the CTable.
     */
    public int recordIndex[];

    /** Defines a table as hidden. This attribute may be used in different
     *  ways. It is not used by the API itsef.
     */
    protected boolean isHidden = false;

    static final long serialVersionUID = 12;

    /* The active record of the table
     */
    protected int activeRecord = -1;
    protected static ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.database.engine.CTableResourceBundle", Locale.getDefault());
    /* The array of the TableModelListeners */
    protected transient ArrayList tableModelListeners = new ArrayList();
    /* The array of the DatabaseTableModelListeners */
    protected transient ArrayList databaseTableModelListeners = new ArrayList();
    /* The array of the CTable propertyChange listeners */
    protected transient ArrayList tablePropertyChangeListeners = new ArrayList();
    protected PropertyChangeSupport tablePropertyChangeSupport = new PropertyChangeSupport(this);

    private ESlateHandle handle = null;
    TableSO tableSO;


    public synchronized void addTableModelListener(TableModelListener dtl) {
        boolean isDatabaseTableModelListener = DatabaseTableModelListener.class.isAssignableFrom(dtl.getClass());
        if (isDatabaseTableModelListener) {
            if (databaseTableModelListeners.indexOf(dtl) == -1)
                databaseTableModelListeners.add(dtl);
        }
        if (tableModelListeners.indexOf(dtl) == -1)
            tableModelListeners.add(dtl);
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

    protected void fireTableRenamed(TableRenamedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).tableRenamed(e);
    }

    protected void fireTableHiddenStateChanged(TableHiddenStateChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).tableHiddenStateChanged(e);
    }

    protected void fireColumnRenamed(ColumnRenamedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).columnRenamed(e);
    }

    protected void fireColumnTypeChanged(ColumnTypeChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).columnTypeChanged(e);
    }

    protected void fireColumnKeyChanged(ColumnKeyChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).columnKeyChanged(e);
    }

    protected void fireColumnEditableStateChanged(ColumnEditableStateChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).columnEditableStateChanged(e);
    }

    protected void fireColumnRemovableStateChanged(ColumnRemovableStateChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).columnRemovableStateChanged(e);
    }

    protected void fireColumnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).columnHiddenStateChanged(e);
    }

    protected void fireColumnRemoved(ColumnRemovedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).columnRemoved(e);
    }

    protected void fireColumnAdded(ColumnAddedEvent e) {
        ArrayList dl;
//t        if (database == null)
//t            return;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).columnAdded(e);
    }

    protected void fireCalcColumnReset(CalcColumnResetEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).calcColumnReset(e);
    }


    protected void fireCalcColumnFormulaChanged(CalcColumnFormulaChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).calcColumnFormulaChanged(e);
    }


    protected void fireSelectedRecordSetChanged(SelectedRecordSetChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).selectedRecordSetChanged(e);
    }


    protected void fireCellValueChanged(CellValueChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).cellValueChanged(e);
    }

    protected void fireRecordAdded(RecordAddedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).recordAdded(e);
    }

    protected void fireEmptyRecordAdded(RecordAddedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++) {
//            System.out.println("Firing record added");
//            ((DatabaseTableModelListener) dl.get(i)).emptyRecordAdded(e);
        }
    }

    protected void fireRecordRemoved(RecordRemovedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).recordRemoved(e);
    }

    protected void fireActiveRecordChanged(ActiveRecordChangedEvent e) {
        ArrayList dl;
/*test        synchronized(this) {dl = (Array) tableListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableListener) dl.at(i)).activeRecordChanged(e);
*/
        synchronized(this) {dl = (ArrayList) databaseTableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((DatabaseTableModelListener) dl.get(i)).activeRecordChanged(e);

    }

    protected void fireRowOrderChanged(RowOrderChangedEvent e) {
        ArrayList dl;
        synchronized(this) {dl = (ArrayList) tableModelListeners.clone();}
        for (int i=0; i<dl.size(); i++)
            ((TableModelListener) dl.get(i)).rowOrderChanged(e);
    }

    /**
     * Adds a new non-calculated field to the CTable. The field is added at the end of the Array of fields
     * of the CTable. If the field is of type <i>image</i>, then it is configured so that the image data
     * are stored in the database itself.
     *
     * @param     fieldName   the name of the field.
     * @param     typeName    the data type of the new field, i.e. one of
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     * @param     isEditable  defines if the new field can be edited or not. When a field is not editable,
     *            neither its data, nor its attributes (e.g. name) can be edited.
     * @param     isKey       determines if the new field will be included in the <b>key</b> of the table.
     * @param     isRemovable determines if this field can be deleted.
     * @param     isHidded determines if this field should be hidden.
     * @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     * @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     * @exception InvalidFieldTypeException If the type of the new field cannot be determined, i.e. different from
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     * @exception AttributeLockedException If new fields cannot be added to the CTable.
     * @see       #addCalculatedField(java.lang.String, java.lang.String, boolean, boolean, boolean)
     * @see       #addImageField(java.lang.String, boolean, boolean, boolean, boolean)
     */
    public void addField(String fieldName, String typeName, boolean isEditable, boolean isKey, boolean isRemovable, boolean isHidden)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
    AttributeLockedException {
            _addField(fieldName, typeName, isEditable, isKey, isRemovable, isHidden, true);
    }

    /**
     * Adds a new Image field to the CTable. The field is added at the end of the Array of fields
     * of the CTable. Whether the database stores the actual data or references to the data of
     * the image field, is determined be the <i>containsLinksToExternalData</i> parameter.
     *
     * @param     fieldName   the name of the field.
     * @param     isEditable  defines if the new field can be edited or not. When a field is not editable,
     *            neither its data, nor its attributes (e.g. name) can be edited.
     * @param     isRemovable determines if this field can be deleted.
     * @param     isHidded determines if this field should be hidden.
     * @param     containsLinksToExternalData determines if this field stores actual image data or references to image files.
     * @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     * @exception AttributeLockedException If new fields cannot be added to the CTable.
     */
    public void addImageField(String fieldName, boolean isEditable, boolean isRemovable, boolean isHidden, boolean containsLinksToExternalData)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
    AttributeLockedException {
            _addField(fieldName, "image", isEditable, false, isRemovable, isHidden, true);
            ((CTableField) CTableFields.at(CTableFields.size()-1)).setContainsLinksToExternalData(containsLinksToExternalData);
    }

    /**
     * Adds a new field to the CTable. The field is added at the end of the Array of fields
     * of the CTable. The new field is a clone of the given field. If this
     * method is given a CTableField of the same CTable, then an exception will be triggered,
     * since two fields cannot have the same name in the same CTable.
     * @param     field   The field to be replicated in the current CTable.
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
    public CTableField addField(CTableField field) throws IllegalCalculatedFieldException, InvalidFormulaException,
    InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {

        if (!fieldAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg88"));

        if (!field.isCalculated()) {
	        _addField(field.getName(),
	                 CTableField.getInternalDataTypeName(field),
	                 field.isEditable(),
    	             field.isKey(),
	                 field.isRemovable(),
	                 field.isHidden(),
	                 true);
        }else{
            try{
                addCalculatedField(field.getName(),
	                               field.getTextFormula(),
                                   field.isKey(),
                                   field.isRemovable(),
	                               field.isHidden());
	        }catch (AttributeLockedException exc) {
	            System.out.println("Serious inconsisency error in CTable addField() : 0.5");
	            return null;
	        }
        }
        CTableField newField;
        try{
            newField = getCTableField(getFieldCount()-1);
        }catch (InvalidFieldIndexException e) {
            System.out.println("Serious inconsistency error in CTable addField(CTableField): (1)");
            return null;
        }
        newField.setFieldEditabilityChangeAllowed(field.isFieldEditabilityChangeAllowed());
        newField.setFieldRemovabilityChangeAllowed(field.isFieldRemovabilityChangeAllowed());
        newField.setFieldDataTypeChangeAllowed(field.isFieldDataTypeChangeAllowed());
        newField.setCalcFieldResetAllowed(field.isCalcFieldResetAllowed());
        newField.setFieldKeyAttributeChangeAllowed(field.isFieldKeyAttributeChangeAllowed());
        newField.setFieldHiddenAttributeChangeAllowed(field.isFieldHiddenAttributeChangeAllowed());
        newField.setCalcFieldFormulaChangeAllowed(field.isCalcFieldFormulaChangeAllowed());
        newField.UIProperties = (Array) field.UIProperties.clone();
        newField.setContainsLinksToExternalData(field.containsLinksToExternalData());
        return newField;
    }


    protected final void _addField(String fieldName, String typeName, boolean isEditable, boolean isKey, boolean isRemovable, boolean isHidden, boolean fireEvent)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException,
    AttributeLockedException {
        CTableField newField;
System.out.println("CTable _addField() fieldName: " + fieldName);
        if (!fieldAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg88"));

//        System.out.println("FieldName: " + fieldName);
        //Checking for a valid field name
        if (fieldName.equals("") || fieldName == null) {
//            System.out.println("No name supplied for the new field");
            throw new InvalidFieldNameException(bundle.getString("CTableMsg1"));
        }

        if (typeName.toLowerCase().equals("image") && isKey)
            throw new InvalidKeyFieldException(bundle.getString("CTableMsg67"));

        //Checking if another column with the same name exists
        for (int i = 0; i<CTableFields.size(); i++) {
            if (((CTableField) CTableFields.at(i)).getName().equals(fieldName)) {
//                System.out.println("Another field with the same name exists");
                throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg2"));
            }
        }

        //Checking if the table already contains records, in case a key field is added
        if (isKey && recordCount >=0) {
            throw new InvalidKeyFieldException(bundle.getString("CTableMsg3"));
        }

        try {
            newField = new CTableField(fieldName, typeName, isEditable, isKey, isRemovable, isHidden);
        }catch (InvalidFieldTypeException e) {
            throw new InvalidFieldTypeException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg4") + e.message);
        }
        //Adding the new column to the CTableFields array
        CTableFields.add(newField);

        //Initializing the primaryKey OrderedSet, if not already initialized
        if (primaryKeyMap == null && isKey) {
            hasKey = true;
            primaryKeyMap = new HashMap(new EqualTo(), true, 10, (float)1.0);
        }

        //Add the column to the Array of the key field indices, if it's a key
        if (isKey)
            keyFieldIndices.add(new Integer(tableData.size()));

        //Filling the record entry form HashMap
//        recordEntryForm.add("");
        //Adding a new Array for the new Field to the tableData HashMap
        Array fld = new Array();
        for (int k=0; k<=recordCount; k++)
            fld.add(null);
        tableData.add(fld);
        setModified();
        /* An event shouldn't be fired by _addField() if the new field is calculated. In this case the event
         * is fired by addCalculatedField().
         */
        if (fireEvent) {// && database != null)
            try{
                fireColumnAdded(new ColumnAddedEvent(this, getFieldIndex(newField), false)); //database.CTables.indexOf(this), newField));
            }catch (InvalidFieldException exc) {}
        }
System.out.println("addField() getfieldCout(): " + getFieldCount());
//        System.out.println("Defined field: \"" + fieldName + "\" --- " + typeName + " " +
//            isEditable + " " + isKey + " " + isRemovable);

    }


    /**
     *  Adds a key field to the CTable.The table has to be empty whenever a key field is
     *  added to it. The new field is by default removable.
     *  @param fieldName   the name of the key field.
     *  @param typeName    the data type of the new field.
     *  @param isEditable  determines whether the field's attributes and data are editable.
     *  @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     *  @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     *  @exception InvalidFieldTypeException If the type of the new field cannot be determined, i.e. different from
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     *  @exception AttributeLockedException If new fields cannot be added to the CTable.
     */
    public void addKeyField(String fieldName, String typeName, boolean isEditable) throws InvalidFieldNameException,
    InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
        if (recordCount < 0) {
            _addField(fieldName, typeName, isEditable, true, false, false, true);
        }else{
            throw new InvalidKeyFieldException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg5"));
        }
    }


    protected static boolean creatingCalculatedField = false;
    /**
     *  Adds an ordinary field to the CTable. An ordinary field is editable, removable and
     *  not part of the table's key.
     *  @param fieldName  the name of the new field.
     *  @param type  the data type of the column, i.e. one of
     *               <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     *  @exception InvalidFieldNameException If:
     *            <ul>
     *            <li>No name is supplied for the new field
     *            <li>Another field with the same name exists in the table.
     *            </ul>
     *  @exception InvalidKeyFieldException If a key-field is added into a non-empty table.
     *  @exception InvalidFieldTypeException If the type of the new field cannot be determined, i.e. different from
     *            <b>integer</b>, <b>double</b>, <b>string</b>, <b>boolean</b>, <b>url</b>, <b>date</b>, <b>time</b>, <b>image</b> and <b>sound</b>.
     *  @exception AttributeLockedException If new fields cannot be added to the CTable.
     */
    public void addCustomField(String fieldName, String type) throws InvalidFieldNameException,
    InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException {
        _addField(fieldName, type, true, false, true, false, true);
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
     * @param isKey      determines if the calculated field will be part of the table's key
     * @param isRemovable determines if the calculated field can be deteted from the table or not
     * @param isEditingFormula a flag, which determines if the calculated field is actually
     *                         being added to the table, or if its formula is being re-evaluated.
     * @exception IllegalCalculatedFieldException This exception is thrown, when
     *              the formula of a calculated field, which does not exist, is being edited.
     * @exception InvalidFormulaException If the supplied formula is incorrect.
     * @exception InvalidFieldNameException If no name was supplied for the new field, or another field
     *              with the same name is already defined in the table.
     * @exception InvalidKeyFieldException If the new calculated field is also introduced as part of
     *              of the key of a non-empty table.
     * @exception AttributeLockedException If a calculated field's formula is being edited, but it is locked.
     * @see   #addField(java.lang.String, java.lang.String, boolean, boolean, boolean, boolean)
     */
    public void addCalculatedField(String fieldName, String formula, boolean isKey, boolean isRemovable, boolean isEditingFormula)
    throws IllegalCalculatedFieldException, InvalidFormulaException, InvalidFieldNameException,
    InvalidKeyFieldException, AttributeLockedException {
        /* Check if the table contains any other columns. If not then abort.
         */
//        if (CTableFields.isEmpty())
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
        Array fieldIndices = new Array();

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
                                fieldIndices.add(new Integer(operandFieldIndex));
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


        String calcFieldType;
        boolean isDate = false;
        boolean integerFound = false;
        String firstOperand = "";
        boolean openingQuote = false;
        int openingQuoteIndex = 0;
        boolean moreThan1DateFields = false;
        int firstIndex = 0;

        if (!fieldIndices.isEmpty()) {
            calcFieldType = ((CTableField) CTableFields.at(((Integer) fieldIndices.at(0)).intValue())).getFieldType().getName();
            isDate = ((CTableField) CTableFields.at(((Integer) fieldIndices.at(0)).intValue())).isDate();
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
                            calcFieldType = "java.lang.Boolean";
                        else
                            calcFieldType = "java.lang.String";
                    }else
                        calcFieldType = "java.lang.Boolean";
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
                                calcFieldType = "java.lang.Integer";
                                firstOperand = formula.substring(firstIndex);
                            }else{
                                String validOperators = "+/*%$-) ";
                                if (validOperators.indexOf(formula.charAt(r)) != -1) {
                                    calcFieldType = "java.lang.Integer";
                                    firstOperand = formula.substring(firstIndex, r);
//                                    System.out.println("s"+firstOperand+"e");
                                }else if (formula.charAt(r) == '.') {
                                        r++;
                                        for (; (r<formula.length() && formula.charAt(r) <= '9' && formula.charAt(r) >= '0'); r++);

//                                        System.out.println("r: " + ", "+ formula.length());
                                        if (r==formula.length()) {
                                            calcFieldType = "java.lang.Double";
                                            firstOperand = formula.substring(firstIndex);
                                        }else{
                                            if (validOperators.indexOf(formula.charAt(r)) != -1) {
                                                firstOperand = formula.substring(firstIndex, r);
                                                calcFieldType = "java.lang.Double";
                                            }else
                                                calcFieldType = "java.lang.String";
                                        }
                                }else
                                    calcFieldType = "java.lang.String";
                            }
                        }else{
                            Integer k = new Integer(firstOperand);
                            calcFieldType = "java.lang.Integer";
                        }
                    }catch (NumberFormatException e1) {
                        try{
                            Double f = new Double(firstOperand);
                            calcFieldType = "java.lang.Double";
                        }catch (NumberFormatException e2) {
                            try{
                                URL u = new URL(firstOperand);
                                calcFieldType = "java.net.URL";
                                int z;
//                                System.out.println(lastIndex+1);
                                for (z=lastIndex+1; z<formula.length() && formula.charAt(z)!=' ' && formula.charAt(z)!='+'; z++);
                                firstOperand = firstOperand + new String(c, lastIndex, z-lastIndex);
                            }catch (MalformedURLException e3) {
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
                        }
                    }
                }
            }else{
                openingQuote = true;
                openingQuoteIndex = i;
                calcFieldType = "java.lang.String";
            }
        }

        String textFormula = formula;
        if (!fieldIndices.isEmpty())
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
            if (calcFieldType.equals("gr.cti.eslate.database.engine.CDate") ||
                calcFieldType.equals("gr.cti.eslate.database.engine.CTime")) { //"java.util.Date")) {
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
            }else if (calcFieldType.equals("java.net.URL")) {
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
                if (calcFieldType.equals("java.lang.String") || calcFieldType.equals("java.lang.Integer") || calcFieldType.equals("java.lang.Double"))
                    operatorsString = "+-*/$%()";
                else
                    operatorsString = "+-*/$%() ";

                for (int i=0; i<formula.length(); i++) {
                    if (formula.charAt(i) == '"') {
                        while (++i<formula.length() && formula.charAt(i) != '"');
                    }
                    int operatorIndex;
                    if (i<formula.length() && (operatorIndex = operatorsString.indexOf(formula.charAt(i))) != -1) {
                        if (i==firstIndex && formula.charAt(i) == '-' && (calcFieldType.equals("java.lang.Integer") || calcFieldType.equals("java.lang.Double")))
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
        if (singleOperandFormula && (fieldIndices.size() == 1) && (fieldIndices.at(0).toString().length()+2)!=formula.trim().length())
            singleOperandFormula = false;
        /* firstOperand.length()!=0: we perform this test because in the case that the
         * "formula" starts with a quote, "firstOperand" contains no value.
         */
        if (singleOperandFormula && fieldIndices.size() == 0 && calcFieldType.equals("java.lang.String") && firstOperand.length()!=formula.trim().length()) {
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
                singleOperand = (Integer) fieldIndices.at(0);
            else{
                if (calcFieldType.equals("java.lang.Integer")) {
                    try{
                        singleOperand = new Integer(firstOperand);
                    }catch (Exception e) {throw new InvalidFormulaException(bundle.getString("CTableMsg11") + firstOperand);}
                }else if (calcFieldType.equals("java.lang.Double")) {
                    try{
                        singleOperand = new Double(firstOperand);
                    }catch (Exception e) {throw new InvalidFormulaException(bundle.getString("CTableMsg12") + firstOperand);}
                }else if (calcFieldType.equals("java.lang.String")) {
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
                }else if (calcFieldType.equals("java.lang.Boolean"))
                    singleOperand = new Boolean(firstOperand);
                else if (calcFieldType.equals("java.net.URL")) {
                    try{
                        singleOperand = new URL(firstOperand);
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
//t                }else if (calcFieldType.equals("java.util.Date")) {
                }else if (calcFieldType.equals("gr.cti.eslate.database.engine.CDate")) {
//t                    if (isDate)
                    singleOperand = new CDate(dateFormat.parse(firstOperand, new ParsePosition(0)));
                }else if (calcFieldType.equals("gr.cti.eslate.database.engine.CTime")) {
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
            if (calcFieldType.equals("java.lang.Integer") || calcFieldType.equals("java.lang.Double")) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (!dataType.equals("java.lang.Integer") && !dataType.equals("java.lang.Double"))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg13") + fieldName + "\". " +  bundle.getString("CTableMsg100") + ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + bundle.getString("CTableMsg14") + dataType.substring(dataType.lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                    else if (dataType.equals("java.lang.Double"))
                        calcFieldType = "java.lang.Double";
                }
                try{
                    oe = new NumericOperandExpression(formula, null); //table this);
//                    System.out.println("Creating NumericOperandExpression....");
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

                /* Check if the operand expression contains any double operand (i.e. 2.3).
                 * In this case the calculated field's type will be Double.
                 */
                if (((NumericOperandExpression) oe).containsDoubleOperand)
                    calcFieldType = "java.lang.Double";
            // Boolean
            }else if (calcFieldType.equals("java.lang.Boolean")) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (!dataType.equals("java.lang.Boolean"))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg16") + fieldName + "\". " + bundle.getString("CTableMsg100") + ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + bundle.getString("CTableMsg14") + dataType.substring(dataType.lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                }
                try{
                    oe = new BooleanOperandExpression(formula, null); //table this);
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

            // String
            }else if (calcFieldType.equals("java.lang.String")) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (dataType.equals("gr.cti.eslate.database.engine.CImageIcon") || dataType.equals("Sound data type ???"))
                        throw new InvalidFormulaException(bundle.getString("CTableMsg19") + fieldName + "\". " +  bundle.getString("CTableMsg100") + ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + bundle.getString("CTableMsg14") + dataType.substring(dataType.lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                }
                try {
                    oe = new StringOperandExpression(formula, null); //table this);
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}
            // Date or Time
//t            }else if (calcFieldType.equals("java.util.Date")) {
            }else if (calcFieldType.equals("gr.cti.eslate.database.engine.CDate") ||
                calcFieldType.equals("gr.cti.eslate.database.engine.CTime")) {
                String dataType;
                int numOfDateFields = 0;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (!(dataType.equals("java.lang.Integer") || dataType.equals("java.lang.Double"))) {
//                        System.out.println(dataType + ", " + (isDate== ((CTableField) _getFields().at(((Integer) fieldIndices.at(i)).intValue())).isDate()));
                        if (!dataType.equals("java.lang.Date") && (!(isDate== ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).isDate()))) {
                            String dataType2 = dataType.substring(dataType.lastIndexOf('.')+1);
                            if (dataType2.equals("Date")) {
                                if (!(((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).isDate()))
                                    dataType2 = "Time";
                            }

//                            System.out.println("Hereee");
                            if (isDate)
                                throw new InvalidFormulaException(bundle.getString("CTableMsg17") + fieldName + "\". " + bundle.getString("CTableMsg100") + ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + bundle.getString("CTableMsg14") + dataType2 + bundle.getString("CTableMsg15"));
                            else
                                throw new InvalidFormulaException(bundle.getString("CTableMsg18") + fieldName + "\". " + bundle.getString("CTableMsg100") + ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + bundle.getString("CTableMsg14") + dataType2 + bundle.getString("CTableMsg15"));
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
                        oe = new DateOperandExpression(textFormula, formula, true, null, numOfDateFields); //table this, numOfDateFields);
                    else
                        oe = new DateOperandExpression(textFormula, formula, false, null, numOfDateFields); //table this, numOfDateFields);
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
                        calcFieldType = calcDateFieldClass.getName();
                }
            // URL
            }else if (calcFieldType.equals("java.net.URL")) {
                String dataType;
                for (int i=0; i<fieldIndices.size(); i++) {
                    dataType = ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getFieldType().getName();
                    if (!dataType.equals("java.net.URL") && !dataType.equals("java.lang.String"))
                            throw new InvalidFormulaException(bundle.getString("CTableMsg21") + fieldName + "\". " + bundle.getString("CTableMsg100") + ((CTableField) getFields().at(((Integer) fieldIndices.at(i)).intValue())).getName() + bundle.getString("CTableMsg14") + dataType.substring(dataType.lastIndexOf('.')+1) + bundle.getString("CTableMsg15"));
                }
                try {
                    oe = new StringOperandExpression(formula, null); //table this);
                }catch (InvalidDataFormatException e) {throw new InvalidFormulaException(e.message);}
                 catch (InvalidOperandExpressionException e) {throw new InvalidFormulaException(e.message);}

            // any other
            }else{
                if ((calcFieldType.equals("gr.cti.eslate.database.engine.CImageIcon")))
                    throw new InvalidFormulaException(bundle.getString("CTableMsg22"));
                else
                    throw new InvalidFormulaException(bundle.getString("CTableMsg23") + calcFieldType.substring(calcFieldType.lastIndexOf('.') + 1) + bundle.getString("CTableMsg24"));
            }
        }

//        System.out.println("oe: " + oe + ", singleOperand: " + singleOperand+ " singleOperandFormula: " + singleOperandFormula);
        int calcFieldIndex;
        CTableField calcField;
        String _prevDataType=null;
        if (!isEditingFormula) {
            try{
                _addField(fieldName, calcFieldType.substring(calcFieldType.lastIndexOf('.')+1), false, isKey, isRemovable, false, false);
            }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(e.message);}
             catch (InvalidKeyFieldException e) {throw new InvalidKeyFieldException(e.message);}
             catch (InvalidFieldTypeException e) {System.out.println("Serious inconsistency error in CTable addCalculatedField(): (5)"); return;}

             /* Increase the count of calculated fields in this table.
              */
             numOfCalculatedFields++;

            /* The just added calculated field is the last in the "CTableFields" Array.
             */
            calcFieldIndex = CTableFields.size()-1;
            calcField = (CTableField) CTableFields.at(calcFieldIndex);
//            System.out.println("isDate: " + isDate);
            calcField.setDate(isDate);
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
//t            if (database != null)
            try{
                fireColumnAdded(new ColumnAddedEvent(this, getFieldIndex(calcField), false)); //database.CTables.indexOf(this), calcField));
            }catch (InvalidFieldException exc) {}
        }else{
            CTableField f = null;
            int fIndex = 0;
            try{
                f = getCTableField(fieldName);
                fIndex = getFieldIndex(fieldName);
            }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in CTable addCalculatedField(): (2)");}
            String previousDataType = CTableField.getInternalDataTypeName(f);

//            System.out.println("calcFieldType: " + calcFieldType + ", " + "currenttType: " + f.getFieldType().getName());
//            System.out.println("isDate: " + isDate + ", " + "f.isDate(): " + f.isDate());
//            if (!f.getFieldType().getName().equals(calcFieldType) && !isDate==f.isDate()) {
            if (!f.getFieldType().getName().equals(calcFieldType)) { //t || isDate!=f.isDate()) {
//                System.out.println("sd f.dependentCalcFields.isEmpty(): " + f.dependentCalcFields.isEmpty() + "," +
//                "moreThan1DateFields: " + moreThan1DateFields);

                // 24/1/1998 PENDING: possible error. I removed  "|| moreThan1DateFields)" from the following if-statement, without knowing why it is here                if (!f.dependentCalcFields.isEmpty())
                if (!f.getDependentCalcFields().isEmpty())
                    throw new InvalidFormulaException(bundle.getString("CTableMsg25"));
                else{
                    try{
                        f.setFieldType(Class.forName(calcFieldType));
                    }catch (ClassNotFoundException e) {System.out.println("Serious inconsistency error in CTable addCalculatedField() : (3)"); return;}
                     catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable addCalculatedField() : (4)"); return;}
                    f.setDate(isDate);
                }
            }

            /* Remove the index of this field from the "dependentCalcFields" lists of all
             * the fields on which it depends.
             */
            Array a = f.getDependsOnFields();
            Integer i1 = new Integer(fIndex);
            for (int i=0; i<a.size(); i++)
                ((CTableField) CTableFields.at(((Integer) a.at(i)).intValue())).dependentCalcFields.remove(i1);

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
            CTableField f1 = (CTableField) CTableFields.at(((Integer) fieldIndices.at(i)).intValue());
            if (f1.dependentCalcFields.indexOf(new Integer(calcFieldIndex)) == -1)
                f1.dependentCalcFields.add(new Integer(calcFieldIndex));
        }

//        System.out.println(isDate? : " + calcField.isDate());
        /* Calculate the values of the cells of the calculated field.
         */
        if (singleOperandFormula) {
            if (fieldIndices.isEmpty()) {
                for (int i=0; i<=recordCount; i++)
                    ((Array) tableData.at(calcFieldIndex)).put(i, singleOperand);
                calcField.calculatedValue = singleOperand;
            }else{
                int singleFieldIndex = ((Integer) singleOperand).intValue();
                if (calcFieldType.equals("java.net.URL") && ((CTableField) CTableFields.at(singleFieldIndex)).getFieldType().getName().equals("java.lang.String"))
                    throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");


                for (int i=0; i<=recordCount; i++)
                    ((Array) tableData.at(calcFieldIndex)).put(i, ((Array) tableData.at(singleFieldIndex)).at(i));
            }
        }else{
            if (fieldIndices.isEmpty()) {
                if (calcFieldType.equals("java.lang.Integer")) {
                    Object o = null;
                    boolean dateExpressionFlag = false;
                    if (oe.getClass().getName().equals("gr.cti.eslate.database.engine.DateOperandExpression"))
                        dateExpressionFlag = true;

                    if (dateExpressionFlag) {
                        for (int i=0; i<=recordCount; i++) {
                            o = oe.execute(i, true);
                            if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            ((Array) tableData.at(calcFieldIndex)).put(i, o);
                        }
                    }else{
                        for (int i=0; i<=recordCount; i++) {
                            o = oe.execute(i, false);
                            if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            ((Array) tableData.at(calcFieldIndex)).put(i, o);
                        }
                    }
                    if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                      o = (o==null)? null:new Integer(((Double) o).intValue());
                        o = new Integer(((Double) o).intValue());
                    }
                    calcField.calculatedValue = o;
                }else if (calcFieldType.equals("java.net.URL")) {
                    try{
                        for (int i=0; i<=recordCount; i++)
                            ((Array) tableData.at(calcFieldIndex)).put(i, new URL((String)oe.execute(i, false)));
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
                    try{
                        calcField.calculatedValue = new URL((String)oe.execute(0, false));
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
                }else{
                    for (int i=0; i<=recordCount; i++)
                        ((Array) tableData.at(calcFieldIndex)).put(i, oe.execute(i, false));
//                    System.out.println("calculatedValue: " + calcField.calculatedValue);
                    calcField.calculatedValue = oe.execute(0, false);
                }

/*                if (moreThan1DateFields) {
                    System.out.println("Evaluating date calc field type to: " + ((DateOperandExpression) oe).getCalcFieldType(true));
                    Integer tmp = new Integer(2);
                    int firstNonNull;
                    for (firstNonNull=0; firstNonNull<=recordCount && ((Array) tableData.at(calcFieldIndex)).at(firstNonNull) == null; firstNonNull++);
                    System.out.print("tmp: ");
                    if (firstNonNull <= recordCount) {
                        System.out.println(((Array) tableData.at(calcFieldIndex)).at(firstNonNull));
                        if (tmp.getClass().isInstance(((Array) tableData.at(calcFieldIndex)).at(firstNonNull)))
                            calcField.setFieldType(tmp.getClass());
                    }
                }
*/
            }else{
                if (calcFieldType.equals("java.lang.Integer")) {
                    Object o;
                    boolean dateExpressionFlag = false;
                    if (oe.getClass().getName().equals("gr.cti.eslate.database.engine.DateOperandExpression"))
                        dateExpressionFlag = true;
                    if (dateExpressionFlag)  {
                        for (int i=0; i<=recordCount; i++) {
                            o = oe.execute(i, true);
                            if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            ((Array) tableData.at(calcFieldIndex)).put(i, o);
                        }
                    }else{
                        for (int i=0; i<=recordCount; i++) {
                            o = oe.execute(i, false);
                            if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                              o = (o==null)? null:new Integer(((Double) o).intValue());
                                o = new Integer(((Double) o).intValue());
                            }
                            ((Array) tableData.at(calcFieldIndex)).put(i, o);
                        }
                    }
                }else if (calcFieldType.equals("java.net.URL")) {
                    String u;
                    try{
                        for (int i=0; i<=recordCount; i++) {
                            u = (String) oe.execute(i, false);
                            if (u!=null)
                                ((Array) tableData.at(calcFieldIndex)).put(i, new URL(u));
                            else
                                ((Array) tableData.at(calcFieldIndex)).put(i, null);
                        }
                    }catch (MalformedURLException e) {throw new InvalidFormulaException(bundle.getString("CTableMsg10") + formula + "\"");}
                }else{
                    for (int i=0; i<=recordCount; i++)
                        ((Array) tableData.at(calcFieldIndex)).put(i, oe.execute(i, false));
                }

  /*              if (moreThan1DateFields) {
                    System.out.println("Evaluating date calc field type to: " + ((DateOperandExpression) oe).getCalcFieldType(true));
                    Integer tmp = new Integer(2);
                    int firstNonNull;
                    for (firstNonNull=0; firstNonNull<=recordCount && ((Array) tableData.at(calcFieldIndex)).at(firstNonNull) == null; firstNonNull++);

                    System.out.print("tmp: ");

                    if (firstNonNull <= recordCount) {
                        System.out.println(((Array) tableData.at(calcFieldIndex)).at(firstNonNull));
                        if (tmp.getClass().isInstance(((Array) tableData.at(calcFieldIndex)).at(firstNonNull)))
                            calcField.setFieldType(tmp.getClass());
                    }
                }
*/
            }
        }

        if (isEditingFormula) {
//t            if (this.database != null)
                fireCalcColumnFormulaChanged(new CalcColumnFormulaChangedEvent(this, calcField.getName(), textFormula, _prevDataType)); //this.database.CTables.indexOf(this), calcField.getName(), textFormula, _prevDataType));
        }
//        System.out.println("Type of calculated field: " + calcField.getFieldType());
    }


    /**
     *  Changes the formula of a calculated field.
     *  @param fieldName the name of the calculated field.
     *  @param formula the new formula.
     *  @return Returns <i>true</i>, if the formula of the calculated field is succesfully edited.
     *          Returns <i>false</i>, if the specified field is not calculated, or if it is
     *          part of the table's key.
     *  @exception InvalidFieldNameException If no field exists in the table with the supplied <i>fieldName</i>.
     *  @exception InvalidFormulaException If the new formula is not valid.
     *  @exception AttributeLockedException If the formula is locked.
     *  @see #addCalculatedField(java.lang.String, java.lang.String, boolean, boolean, boolean)
     */
    public boolean changeCalcFieldFormula(String fieldName, String formula)
    throws InvalidFieldNameException, InvalidFormulaException, AttributeLockedException {
        CTableField f;
        try{
            f = getCTableField(fieldName);
        }catch (InvalidFieldNameException e) {throw new InvalidFieldNameException(e.message);}

        if (!f.isCalculated() || f.isKey())
            return false;

        try{
            addCalculatedField(fieldName, formula, false, false, true);
        }catch (IllegalCalculatedFieldException e) {System.out.println("Serious inconsistency error in CTable changeCalcFieldFormula(): (1)");}
         catch (InvalidFormulaException e) {throw new InvalidFormulaException(e.message);}
         catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in CTable changeCalcFieldFormula(): (2)");}

        for (int i=0; i<f.dependentCalcFields.size(); i++)
            evaluateCalculatedField(((Integer) f.dependentCalcFields.at(i)).intValue());

        setModified();
        return true;
    }


    /**
     *  Turns a calculated field into a normal one.
     *  @param fieldName The name of the calculated field
     *  @return <i>true</i>, if the operation is succesful. <i>false</i>, if the field is
     *          not calculated.
     *  @exception InvalidFieldNameException If no field exists in the table with the supplied <i>fieldName</i>.
     *  @exception AttributeLockedException If the field's <i>Calculated</i> attribute is locked.
     */
    public boolean switchCalcFieldToNormal(String fieldName)
    throws InvalidFieldNameException, AttributeLockedException {
        CTableField f;
        f = getCTableField(fieldName);

        Integer index = new Integer(CTableFields.indexOf(f));

        if (!f.isCalculated())
            return false;

        f.setCalculated(false, null, null, null);
//        f.setEditable(true);
        numOfCalculatedFields--;

        /* Remove the field's index from the "dependentCalcFields" list of all
         * the fields it depends on.
         */
        CTableField f1;
        for (int i=0; i<CTableFields.size(); i++) {
            f1 = (CTableField) CTableFields.at(i);
            if (!f1.dependentCalcFields.isEmpty())
                f1.dependentCalcFields.remove(index);
        }

        setModified();
//t        if (this.database != null)
            fireCalcColumnReset(new CalcColumnResetEvent(this, fieldName)); //database.CTables.indexOf(this), fieldName));
        return true;
    }


    /**
     *  Evaluates all the cell of a calculated field.
     *  @param fieldIndex The index of the calculated field.
     */
    protected void evaluateCalculatedField(int fieldIndex) {
        CTableField f = (CTableField) CTableFields.at(fieldIndex);

//        System.out.println("Evaluating field: " + fieldIndex);
        if (f.oe == null) {
            if (f.dependsOnFields.size() == 1) {
                int secFieldIndex = ((Integer) f.dependsOnFields.at(0)).intValue();
                for (int i=0; i<=recordCount; i++)
                    ((Array) tableData.at(fieldIndex)).put(i, ((Array) tableData.at(secFieldIndex)).at(i));
            }
        }else{
            if (!f.dependsOnFields.isEmpty()) {
                if (f.getFieldType().getName().equals("java.lang.Integer")) {
                    Object o;
                    for (int i=0; i<=recordCount; i++) {
                        o = f.oe.execute(i, false);
                        if (o != null && o.getClass().getName().equals("java.lang.Double")) {
//                          o = (o==null)? null:new Integer(((Double) o).intValue());
                            o = new Integer(((Double) o).intValue());
                        }
                        ((Array) tableData.at(fieldIndex)).put(i, o);
                    }
                }else if (f.getFieldType().getName().equals("java.net.URL")) {
                    String u;
                    try{
                        for (int i=0; i<=recordCount; i++) {
                            u = (String) f.oe.execute(i, false);
                            if (u!=null)
                                ((Array) tableData.at(fieldIndex)).put(i, new URL(u));
                            else
                                ((Array) tableData.at(fieldIndex)).put(i, null);
                        }
                    }catch (MalformedURLException e) {}
                }else{
                    for (int i=0; i<=recordCount; i++)
                        ((Array) tableData.at(fieldIndex)).put(i, f.oe.execute(i, false));
                }

            }
        }

        for (int i=0; i<f.dependentCalcFields.size(); i++)
            evaluateCalculatedField(((Integer) f.dependentCalcFields.at(i)).intValue());

        setModified();

    }


    protected void updatePrimaryKeyMapForCalcField(int fieldIndex, int recIndex, Object value) {
//        System.out.println("Entering updatePrimaryKeyMapForCalcField()");
        if (pendingNewRecord) {
            /* If a new record is being interactively inserted in table which contains a key, then
             * there is not entry in the "primaryKeyMap" for this record. So just insert the new
             * entry.
             */
            if (((Integer) keyFieldIndices.at(0)).intValue() == fieldIndex)
                primaryKeyMap.add(new Integer(value.hashCode()), CTableRecords.at(recIndex));
        }else{
            /*If field f is part of the table's key and is also the field whose values
             *are used in the primarykeyMap HashMap, then the entry for this record in
             *the HashMap has to be deleted and a new entry with the correct new key
             *value has to be entered.
             */
            if (((Integer) keyFieldIndices.at(0)).intValue() == fieldIndex) {
                Object prevValue = riskyGetCell(fieldIndex, recIndex);
//              System.out.println(prevValue + ", " + prevValue.hashCode() + value.hashCode());
                HashMapIterator iter = primaryKeyMap.find(new Integer(prevValue.hashCode()));
                while (((Record) iter.value()).getIndex() != recIndex && !iter.atEnd())
                    iter.advance();
                primaryKeyMap.remove(iter);
//                System.out.println("Removing: " + iter.key() + ", " + ((Record) iter.value()).getIndex());
                primaryKeyMap.add(new Integer(value.hashCode()), CTableRecords.at(recIndex));
//                System.out.println("Inserting : " + ((Record) CTableRecords.at(recIndex)).getIndex() + " with key: " + value);
            }
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
     *                                   calculated value of the cell is <i>null</i>.
     */
    protected void evaluateCalculatedFieldCell(int fieldIndex, int rowIndex)
    throws DuplicateKeyException, NullTableKeyException {
        CTableField f = (CTableField) CTableFields.at(fieldIndex);
        Object oldValue = riskyGetCell(fieldIndex, rowIndex);

//        System.out.println("Evaluating field: " + fieldIndex);
        if (f.oe == null) {
            if (f.dependsOnFields.size() == 1) {
                if (f.isKey()) {
                    int secFieldIndex = ((Integer) f.dependsOnFields.at(0)).intValue();
                    Object value = riskyGetCell(secFieldIndex, rowIndex);
                    if (value == null)
                        throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                    if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, value)) {
                        updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                        ((Array) tableData.at(fieldIndex)).put(rowIndex, ((Array) tableData.at(secFieldIndex)).at(rowIndex));
                    }else
                        throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                }else{
                    int secFieldIndex = ((Integer) f.dependsOnFields.at(0)).intValue();
//                  for (int i=0; i<=recordCount; i++)
                    ((Array) tableData.at(fieldIndex)).put(rowIndex, ((Array) tableData.at(secFieldIndex)).at(rowIndex));
                }
            }
        }else{
            if (!f.dependsOnFields.isEmpty()) {

                if (f.getFieldType().getName().equals("java.lang.Integer")) {
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
                    if (f.isKey()) {
                        if (o == null)
                            throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                        else{
                            if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, o)) {
                                updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, o);
                                ((Array) tableData.at(fieldIndex)).put(rowIndex, o);
                            }else
                                throw new DuplicateKeyException(bundle.getString("CTableMsg27") + o + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                        }
                    }else
                        ((Array) tableData.at(fieldIndex)).put(rowIndex, ((o==null)? null: o));

                }else if (f.getFieldType().getName().equals("java.net.URL")) {
                    String u;
                    try{
                            u = (String) f.oe.execute(rowIndex, false);
                            if (u!=null) {
                                if (f.isKey()) {
                                    if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, u)) {
                                        updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, new URL(u));
                                        ((Array) tableData.at(fieldIndex)).put(rowIndex, new URL(u));
                                    }else
                                        throw new DuplicateKeyException(bundle.getString("CTableMsg29") + u + bundle.getString("CTableMsg27") + riskyGetField(fieldIndex).getName() + "\"");
                                }else
                                    ((Array) tableData.at(fieldIndex)).put(rowIndex, new URL(u));
                            }else{
                                if (f.isKey())
                                    throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                                else
                                    ((Array) tableData.at(fieldIndex)).put(rowIndex, null);
                            }
                    }catch (MalformedURLException e) {throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");}

                }else{

                    if (f.isKey()) {
                        Object value = f.oe.execute(rowIndex, false);
                        if (value == null)
                            throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                        if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, value)) {
                            updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                            ((Array) tableData.at(fieldIndex)).put(rowIndex, value);
                        }else
                            throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                    }else
                        ((Array) tableData.at(fieldIndex)).put(rowIndex, f.oe.execute(rowIndex, false));
                }
            }
        }

        if (f.getDependsOnFields().size() == 0) {
//            System.out.println("Calculating cell " + rowIndex + " for field: " + f.getName());
            if (f.isKey()) {
                if (f.calculatedValue == null)
                    throw new NullTableKeyException(bundle.getString("CTableMsg30") + f.getName() +"\"");
                else
                    throw new DuplicateKeyException(bundle.getString("CTableMsg31") + f.calculatedValue + bundle.getString("CTableMsg28") + f.getName() + "\"");
            }else
                ((Array) tableData.at(fieldIndex)).put(rowIndex, f.calculatedValue);
        }

        Object newValue = riskyGetCell(fieldIndex, rowIndex);
//        System.out.println("newValue: " + newValue + ", oldValue: " + oldValue);
        if ((oldValue == null && newValue != null) || (oldValue != null && newValue == null) ||
           (oldValue != null && newValue != null && !oldValue.equals(newValue))) {
//t            if (this.database != null)
                fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), rowIndex, /*newValue,*/ oldValue, (f.dependentCalcFields.size() != 0))); //this.database.CTables.indexOf(this), f.getName(), rowIndex, newValue, oldValue, (f.dependentCalcFields.size() != 0)));

        }

        try {
            for (int i=0; i<f.dependentCalcFields.size(); i++) {
                CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), rowIndex);
                changedCalcCells.add(f1.getName(), new Integer(rowIndex));
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
     *                                   calculated value of the cell is <i>null</i>.
     */
    protected void evaluateAndAddCalculatedFieldCell(int fieldIndex, int rowIndex)
    throws DuplicateKeyException, NullTableKeyException {
        CTableField f = (CTableField) CTableFields.at(fieldIndex);

//        System.out.println("Evaluating field: " + fieldIndex);
        if (f.oe == null) {
            if (f.dependsOnFields.size() == 1) {
                if (f.isKey()) {
                    int secFieldIndex = ((Integer) f.dependsOnFields.at(0)).intValue();
                    Object value = riskyGetCell(secFieldIndex, rowIndex);
                    if (value == null)
                        throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                    if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, value)) {
                        updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                        ((Array) tableData.at(fieldIndex)).add(((Array) tableData.at(secFieldIndex)).at(rowIndex));
                    }else
                        throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                }else{
                    int secFieldIndex = ((Integer) f.dependsOnFields.at(0)).intValue();
//                  for (int i=0; i<=recordCount; i++)
                    ((Array) tableData.at(fieldIndex)).add(((Array) tableData.at(secFieldIndex)).at(rowIndex));
                }
            }
        }else{
            if (!f.dependsOnFields.isEmpty()) {

                if (f.getFieldType().getName().equals("java.lang.Integer")) {
                    Object o;
//                    System.out.println("rowIndex: " + rowIndex);
                    o = f.oe.execute(rowIndex, false);
//                    System.out.println("o= " + o + ", " + o.getClass());
                    if (f.isKey()) {
                        if (o == null)
                            throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                        else{
                            if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, o)) {
                                updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, o);
                                ((Array) tableData.at(fieldIndex)).add(o);
                            }else
                                throw new DuplicateKeyException(bundle.getString("CTableMsg27") + o + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                        }
                    }else
                        ((Array) tableData.at(fieldIndex)).add(((o==null)? null: o));

                }else if (f.getFieldType().getName().equals("java.net.URL")) {
                    String u;
                    try{
                            u = (String) f.oe.execute(rowIndex, false);
                            if (u!=null) {
                                if (f.isKey()) {
                                    if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, u)) {
                                        updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, new URL(u));
                                        ((Array) tableData.at(fieldIndex)).add(new URL(u));
                                    }else
                                        throw new DuplicateKeyException(bundle.getString("CTableMsg29") + u + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                                }else
                                    ((Array) tableData.at(fieldIndex)).add(new URL(u));
                            }else{
                                if (f.isKey())
                                    throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                                else
                                    ((Array) tableData.at(fieldIndex)).add(null);
                            }
                    }catch (MalformedURLException e) {throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");}

                }else{

                    if (f.isKey()) {
                        Object value = f.oe.execute(rowIndex, false);
                        if (value == null)
                            throw new NullTableKeyException(bundle.getString("CTableMsg26") + riskyGetField(fieldIndex).getName() + "\"");
                        if (checkKeyUniquenessOnNewValue(fieldIndex, rowIndex, value)) {
                            updatePrimaryKeyMapForCalcField(fieldIndex, rowIndex, value);
                            ((Array) tableData.at(fieldIndex)).add(value);
                        }else
                            throw new DuplicateKeyException(bundle.getString("CTableMsg27") + value + bundle.getString("CTableMsg28") + riskyGetField(fieldIndex).getName() + "\"");
                    }else
                        ((Array) tableData.at(fieldIndex)).add(f.oe.execute(rowIndex, false));
                }
            }
        }

    }


    /**
     *  Removes a field from the CTable structure. However, it does not remove the data of the field
     *  from the CTable data repository. This method should not be used together with the <i>removeField</i>
     *  method. It is useful only in these cases, where the application which uses this API, e.g. the database
     *  GUI, needs to delete the field's data itself. This case was raised using the <i>Java foundation classes' JTable</i> component.
     *  @param fieldName The name of the field to be deleted.
     *  @exception InvalidFieldNameException If no field exists with the specified <i>fieldName</i>.
     *  @exception TableNotExpandableException If the CTable is not expandable.
     *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this table.
     */
    public void prepareRemoveField(String fieldName) throws InvalidFieldNameException,
    TableNotExpandableException, CalculatedFieldExistsException,
    FieldNotRemovableException, AttributeLockedException {
        _removeField(fieldName, false);
    }

    /**
     *  Removes a field from the CTable.
     *  @param fieldName The name of the field to be deleted.
     *  @exception InvalidFieldNameException If no field exists with the specified <i>fieldName</i>.
     *  @exception TableNotExpandableException If the CTable is not expandable.
     *  @exception CalculatedFieldExistsException If there exists a calculated field which depends on the field to be removed.
     *  @exception FieldNotRemovableException If the field is not removable.
     *  @exception AttributeLockedException If fields cannot be removed from this table.
     */
    public void removeField(String fieldName) throws InvalidFieldNameException,
    TableNotExpandableException, CalculatedFieldExistsException,
    FieldNotRemovableException, AttributeLockedException {
        _removeField(fieldName, true);
    }

    private final void _removeField(String fieldName, boolean removeData) throws InvalidFieldNameException,
    TableNotExpandableException, CalculatedFieldExistsException,
    FieldNotRemovableException, AttributeLockedException {
        //Checking for a valid field name
        if (fieldName.equals("") || fieldName == null)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg32"));

        if (!fieldRemovalAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg87"));

        /* If this is the only field in a CTable, which contains records, then the
         * records will have to be removed. However, if the table is not expandable,
         * the records cannot be removed and thus the field cannot be removed either.
         */
        if (CTableFields.size() ==1 && recordCount > 0 && !recordRemovalAllowed)
            throw new TableNotExpandableException(bundle.getString("CTableMsg86"));

        //Checking if a column with the this name exists
        boolean fieldFound = false;
        int index = 0;
        for (int i = 0; i<CTableFields.size(); i++) {
            if (((CTableField) CTableFields.at(i)).getName().equals(fieldName)) {
                fieldFound = true; index = i;
                break;
            }
        }
        if (!fieldFound)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg33") + fieldName + "\"");

        int removedFieldIndex = index;
        //Setting the field's name to null, so that it cannot be used
        //with the CTable API anymore
        CTableField f = (CTableField) CTableFields.at(index);

        if (f.isKey() && !keyChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg90"));

        if (!f.isRemovable())
            throw new FieldNotRemovableException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg34"));

        /* Check if there exist other calculated fields in this table, which depend on
         * the field to be removed.
         */
        if (!f.dependentCalcFields.isEmpty())
            throw new CalculatedFieldExistsException(bundle.getString("CTableMsg35"));

        //Removing the index of the field from "keyFieldIndices" Array, if the table is KEYyed
        if (f.isKey() && hasKey) {
            boolean keyChangeAllowedReset = false;
            try {
                if (!f.isFieldKeyAttributeChangeAllowed()) {
                    f.setFieldKeyAttributeChangeAllowed(true);
                    keyChangeAllowedReset = true;
                }
                removeFromKey(f.getName());
            }catch (FieldIsNotInKeyException e) {System.out.println("Serious inconsistency error in CTable removeField(): (1)."); e.printStackTrace(); return;}
             catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in CTable removeField(): (2)."); e.printStackTrace(); return;}
             catch (TableNotExpandableException e) {
                if (keyChangeAllowedReset)
                    f.setFieldKeyAttributeChangeAllowed(false);
                throw new TableNotExpandableException(bundle.getString("CTableMsg36"));}
             catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable removeField(): (2.5)."); e.printStackTrace(); return;}
        }

        //Removing the field from the recordEntryForm HashMap
//        recordEntryForm.remove(index);

        //Removing the field from the CTableFields array
        if (removeData) {
//            System.out.println("Removing index: " + index + " from CTableFields");
            CTableFields.remove(index);
        }

        //Removing the field from the tableData Array
        if (CTableFields.size() != 0) {
//            if (removeData) {
//                System.out.println("Removing data");
                tableData.remove(index);
//            }
            decrementRestKeyFieldIndices(index);
        }else{//This is the last field of the table, so remove all the table's contents
            /* Do some housekeeping.
             */
            tableData.clear();
            CTableFields.clear();
            CTableRecords.clear();
            recordSelection.clear();
            selectedSubset.clear();
            unselectedSubset.clear();
            recordCount = -1;
            hasKey = false;
            keyFieldIndices.clear();
            primaryKeyMap = null;
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
        CTableField f1;
        StackElement se;
        for (int i=0; i<CTableFields.size(); i++) {
            f1 = (CTableField) CTableFields.at(i);
//            System.out.println("-------"+f1.getName()+"-------" + f1.oe);
            if (f1.isCalculated()) {
                if (f1.oe!=null) {
                    Stack postfix = null;
                    if (f1.fieldType.getName().equals("java.lang.Integer") || f1.fieldType.getName().equals("java.lang.Double"))
                        try{
                            postfix = ((NumericOperandExpression) f1.oe).postfix;
                        }catch (ClassCastException e) {
                            postfix = ((DateOperandExpression) f1.oe).postfix;
                        }
                    else if (f1.fieldType.getName().equals("java.lang.String") || f1.fieldType.getName().equals("java.net.URL"))
                        postfix = ((StringOperandExpression) f1.oe).postfix;
                    else if (f1.fieldType.getName().equals("java.lang.Boolean"))
                        postfix = ((BooleanOperandExpression) f1.oe).postfix;
//t                    else if (f1.fieldType.getName().equals("java.util.Date"))
                    else if (f1.fieldType.getName().equals("gr.cti.eslate.database.engine.CDate") ||
                        f1.fieldType.getName().equals("gr.cti.eslate.database.engine.CTime"))
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
                                int i1 = index;
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

                                if (i1 > index) {
                                    if (dateField != ']')
                                        se.element = '[' + new Integer(i1-1).toString() + ']' + dateField;
                                    else
                                        se.element = '[' + new Integer(i1-1).toString() + ']';
//                                    System.out.println("Tranformed se.element: " + se.element);
                                    /* Reduce each the corresponding field indices in the
                                     * "dependsOnFields" list of the calculated field.
                                     */
//                                    System.out.println("dependsOnFields BEFORE: " + f1.dependsOnFields);
                                    f1.dependsOnFields.put(f1.dependsOnFields.indexOf(new Integer(i1)), new Integer(i1-1));
//                                    System.out.println("dependsOnFields AFTER: " + f1.dependsOnFields);
                                }
                            /* General case.
                             */
                            }else{
                                int i1 = new Integer((String) se.element).intValue();
                                if (i1 > index) {
                                    se.element = new Integer(i1-1).toString();
                                    /* Reduce each the corresponding field indices in the
                                     * "dependsOnFields" list of the calculated field.
                                     */
//                                    System.out.println("dependsOnFields BEFORE: " + f1.dependsOnFields);
                                    f1.dependsOnFields.put(f1.dependsOnFields.indexOf(new Integer(i1)), new Integer(i1-1));
//                                    System.out.println("dependsOnFields AFTER: " + f1.dependsOnFields);
                                }
                            }
                        }

                        fi.advance();
                    }
                }else{
                    /* A calculated field without "OperandExpression".
                     */
                    if (!f1.dependsOnFields.isEmpty()) {
//                        System.out.println(" dependsOnFields BEFORE: " + f1.dependsOnFields);
                        int e = ((Integer) f1.dependsOnFields.at(0)).intValue();
                        if (e > index)
                            f1.dependsOnFields.put(0, new Integer(e-1));
//                        System.out.println(" dependsOnFields AFTER: " + f1.dependsOnFields);
                    }
                }
            }
        }


//        System.out.println("AFTER CHECK 1");
        /* If the removed field was a calculated one, then we have to remove it from
         * the "dependentCalcFields" lists of all the fields, on which it depends.
         */
        if (f.isCalculated()) {
            /* Reduce by 1 the number of calculated fields in this CTable.
             */
            numOfCalculatedFields--;
//            System.out.println("Field under removal, dependsOnFields: " + f.dependsOnFields);
            for (int i=0; i<f.dependsOnFields.size(); i++) {
//                System.out.println("Field: "+ ((CTableField) CTableFields.at(((Integer) f.dependsOnFields.at(i)).intValue())).getName() + " dependentCalcFields: " + ((CTableField) CTableFields.at(((Integer) f.dependsOnFields.at(i)).intValue())).dependentCalcFields);
                ((CTableField) CTableFields.at(((Integer) f.dependsOnFields.at(i)).intValue())).dependentCalcFields.remove(new Integer(index));
//                    System.out.println("Serious inconsistensy error in CTable() removeField(): (1)");
//                System.out.println("Field: "+ ((CTableField) CTableFields.at(((Integer) f.dependsOnFields.at(i)).intValue())).getName() + " dependentCalcFields: " + ((CTableField) CTableFields.at(((Integer) f.dependsOnFields.at(i)).intValue())).dependentCalcFields);
            }
        }

//        System.out.println("AFTER CHECK 2");

        /* Reduce by 1 the indices of the calculated fields, whose indices are greater
         * than "index", in the "dependentCalcFields" of each field.
         */
        for (int i=0; i<CTableFields.size(); i++) {
            f1 = (CTableField) CTableFields.at(i);
            if (!f1.dependentCalcFields.isEmpty()) {
//                System.out.print(f1.getName() +": ");
                Array a = f1.dependentCalcFields;
//                System.out.print(a);
                int n;
                for (int k=0; k<a.size(); k++) {
                    n = ((Integer) a.at(k)).intValue();
                    if (n > index)
                        a.put(k, new Integer(n-1));
                }
//                System.out.println("  " +a);
            }
        }

//        System.out.println("Field \"" + fieldName + "\" was removed.");
        setModified();

//t        if (database != null)
            fireColumnRemoved(new ColumnRemovedEvent(this, fieldName, removedFieldIndex, false)); //database.CTables.indexOf(this), fieldName));
//        System.out.println("After fireFieldRemoved");

        //Invalidating this field
        f.setName(null);
        return;

    }


    /**
     *  Adds a record to the CTable.
     *  @param recEntryForm An Array which contains the data of the new record.
     *  @return <i>true</i>, if the record is added. <i>false</i>, if a null or empty
     *  or with different size from the standard table's records size <i>recEntryForm</i>
     *  is provided.
     *  @exception InvalidDataFormatException If a piece of the data for the new record cannot be cast to the proper data type, i.e.the data type of the field where the data will be stored.
     *  @exception NoFieldsInTableException If the table contains no fields.
     *  @exception NullTableKeyException If the table has a key and the new record's key is null.
     *  @exception DuplicateKeyException If the new record's key is the same as the key of an existing record in the table.
     *  @exception TableNotExpandableException If the table is not expandable.
     */
    public boolean addRecord(Array recEntryForm) throws InvalidDataFormatException,
    NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException {
        if (!recordAdditionAllowed) {
            throw new TableNotExpandableException(bundle.getString("CTableMsg37"));
        }

        if (recEntryForm == null || recEntryForm.isEmpty() || recEntryForm.size() != (CTableFields.size()-numOfCalculatedFields)) {
//            System.out.println(" Incorrect record entry form. Record insertion failed!");
            return false;
        }

        if  (timeToGarbageCollect == 5000) {
            System.gc();
            timeToGarbageCollect = 0;
        }else
            timeToGarbageCollect++;

        //Examining if the table has fields
        if (CTableFields.isEmpty())
            throw new NoFieldsInTableException(bundle.getString("CTableMsg38"));

//        Object[] recordData = new Object[recEntryForm.size()]; //temporary storage for the values of the new record
        boolean keyOK = true;
        Class[] cl = new Class[1];
        Object[] val = new Object[1];
        Constructor con;
//        Integer recPrimaryKey = null;

        Object value;
        CTableField f;
        for (int k=0; k<recEntryForm.size(); k++) {
            value = recEntryForm.at(k);
//            System.out.println("value: " + value);
            f = (CTableField) CTableFields.at(k);

            /* Skip any calculated field in the way. The recordEntryForm does not contains
             * values for the calculated fields.
             */
            int l =0;
            while (f.isCalculated()) {
                f = (CTableField) CTableFields.at(k+l);
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
//            System.out.print(value.getClass() + "     " + f.fieldType);
            /*If the "value" object is of the same type as the field
             *to be inserted to, then no data type conversion is required.
             */
            if (!f.fieldType.isInstance(value)) {
//t                if (f.fieldType.getName().equals("java.util.Date")) {
//t                    if (f.isDate()) {
                if (f.fieldType.getName().equals("gr.cti.eslate.database.engine.CDate")) {
                    CDate d = new CDate();
                    try{
                        d = new CDate(dateFormat.parse(((String) value), new ParsePosition(0)));
                        calendar.setTime(d);
                        calendar.add(Calendar.HOUR, 10);
                        d = new CDate(calendar.getTime());
                    }catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg39") + "\"" + value + "\"" + bundle.getString("CTableMsg40"));}

                    if (d == null)
                        throw new InvalidDataFormatException(bundle.getString("CTableMsg39") + "\"" + value + "\"" + bundle.getString("CTableMsg40"));

                    recEntryForm.put(k, d);
                }else if (f.fieldType.getName().equals("gr.cti.eslate.database.engine.CTime")) {
                    CTime t = new CTime();
                    try{
                        t = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
                    }catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg39") + "\"" + value + "\"" + bundle.getString("CTableMsg41"));}

                    if (t == null)
                        throw new InvalidDataFormatException(bundle.getString("CTableMsg39") + "\"" + value + "\"" + bundle.getString("CTableMsg41"));

                    recEntryForm.put(k, t);
                }else if (f.getFieldType().getName().equals("java.lang.Double") && java.lang.String.class.isInstance(value)) {
                    try{
                        value = new Double(numberFormat.parse((String) value).doubleValue());
                    }catch (DoubleNumberFormatParseException e) {
                        throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));
                    }
                //If the "value"'s class is different from the field's class then:
                //* get the field's and the value's Class,
                //* get a constructor of the field's Class that takes one
                //  parameter of the Class of the "value", if one exists,
                //* call the constructor, with the "value" object as argument and
                //  insert the returned value to the field.
                }else{
//                    cl[0] = value.getClass();
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
                        con = f.fieldType.getConstructor(cl);
//                        val[0] = value;
                        try {
                            recEntryForm.put(k, con.newInstance(val));
                        }catch (InvocationTargetException e) {
                            throw new InvalidDataFormatException(bundle.getString("CTableMsg44") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));
                         }
                         catch (InstantiationException e) {
                            throw new InvalidDataFormatException(bundle.getString("CTableMsg44") + value + bundle.getString("CTableMsg43") +nameForDataTypeInAitiatiki(f));
                         }
                         catch (IllegalAccessException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg44") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                         catch (UnableToCreateImageIconException e) {throw new InvalidDataFormatException(e.getMessage());}
                    }catch (NoSuchMethodException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg44") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                }
            }else{
                /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
                 * attribute has to be synchronized against the field's "linkToExternalData" attribute.
                 */
                if (f.fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                    if (f.containsLinksToExternalData())
                        ((CImageIcon) value).referenceToExternalFile = true;
                    else
                        ((CImageIcon) value).referenceToExternalFile = false;
                    recEntryForm.put(k, value);
                }
            }
        }//for

        int newRecordIndex = ((Array) tableData.at(0)).size();

        /*All the values have been succesfully processed, i.e. cast to the
         *proper field data types. So insert them in the table's data array.
         */
        int k =0;
//        System.out.println(recEntryForm.size() +", " + newRecordIndex);
        for (int i=0; i<recEntryForm.size(); i++) {
            while (((CTableField) CTableFields.at(i+k)).isCalculated()) k++;
            ((Array) tableData.at(i+k)).add(recEntryForm.at(i));
        }

        if (numOfCalculatedFields > 0) {

            /* Add a new entry to the Arrays of the calculated fields. This entry will
             * be calculated properly by the following "evaluateCalculatedFieldCell".
             */
/*            for (int i=0; i<CTableFields.size(); i++) {
                if (((CTableField) CTableFields.at(i)).isCalculated())
                    ((Array) tableData.at(i)).add(null);
            }
*/
            CTableField calcField;
            boolean tempKeyReset = false;
            boolean tempKeyChangeAllowedReset = false;
            for (int i=0; i<CTableFields.size(); i++) {
                calcField = (CTableField) CTableFields.at(i);
                if (calcField.isCalculated()) {
                    if (calcField.isKey()) {
                        tempKeyReset = true;
                        try{
                            if (!calcField.isFieldKeyAttributeChangeAllowed()) {
                                calcField.setFieldKeyAttributeChangeAllowed(true);
                                tempKeyChangeAllowedReset = true;
                            }
                            calcField.setKey(false);
                        }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in CTable addRecord() : (1)");}
                         catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable addRecord() : (1.5)");}
                    }

                    try{
//                        System.out.println("FieldIndex: " + i);
                        evaluateAndAddCalculatedFieldCell(i, newRecordIndex);
                    }catch (DuplicateKeyException e) {System.out.println("Serious inconsistency error in CTable addRecord(): (2)");}
                     catch (NullTableKeyException e) {System.out.println("Serious inconsistency error in CTable addRecord(): (3)");}

                    if (tempKeyReset) {
                        try{
                            calcField.setKey(true);
                            if (tempKeyChangeAllowedReset)
                                calcField.setFieldKeyAttributeChangeAllowed(false);
                        }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in CTable addRecord() : (4)");}
                         catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable addRecord() : (5)");}
                        tempKeyReset = false;
                    }
                }
            }
        }

        //If the table is KEYed, check for key uniqueness
        Object primaryKeyValue;
        Integer primaryKeyValueHashCode = null;
        int primaryKeyIndex;

        if (hasKey) {
            keyOK=true;

            for (int i=0; i<keyFieldIndices.size(); i++) {
                if (riskyGetCell(((Integer) keyFieldIndices.at(i)).intValue(), newRecordIndex) == null) {
                    keyOK = false;
                    break;
                }
            }

            if (!keyOK) {
                for (int i=0; i<CTableFields.size(); i++)
                    ((Array) tableData.at(i)).remove(newRecordIndex);
                throw new NullTableKeyException(bundle.getString("CTableMsg45"));
            }else{

                primaryKeyIndex = ((Integer) keyFieldIndices.at(0)).intValue();
                primaryKeyValue = ((Array) tableData.at(primaryKeyIndex)).at(newRecordIndex);
                primaryKeyValueHashCode = new Integer(primaryKeyValue.hashCode());

                Enumeration en = primaryKeyMap.values(primaryKeyValueHashCode);
                int dublicatePrimaryKeyValueRecIndex, i=0;
//                System.out.println("Searching for records with primary key: " + recPrimaryKey);
                while (en.hasMoreElements()) {
                    dublicatePrimaryKeyValueRecIndex = (int) (((Record) en.nextElement()).getIndex());
//                if (((Array) tableData.at(fieldIndex)).indexOf(0, newRecordIndex-1,
//                   primaryKeyValue) != -1) {
//                    int index = -1, i=0;;
                    if (keyFieldIndices.size() > 1) {
                        boolean different = false;
//                        System.out.println("In here");
//                        while ((index = ((Array) tableData.at(fieldIndex)).indexOf(index+1, newRecordIndex-1, primaryKeyValue)) != -1) {
//                            System.out.println("Found " + primaryKeyValue + " at index: " + dublicatePrimaryKeyValueRecIndex);
                            while (i<keyFieldIndices.size()) {
                                int nextKeyFldIndex = ((Integer) keyFieldIndices.at(i)).intValue();
                                /* Don't compare the two records on the field that has changed. Equality
                                 * on these fields is guaranteed by the first while-loop condition.
                                 */
                                if (nextKeyFldIndex == primaryKeyIndex)  {
                                    i++;
                                    continue;
                                }

//                                System.out.println("Comparing on field " + riskyGetField(i).getName() + " records " + new Integer(dublicatePrimaryKeyValueRecIndex).toString() + " and " + new Integer(newRecordIndex).toString() + " : " + riskyGetCell(nextKeyFldIndex, dublicatePrimaryKeyValueRecIndex) + " - " + riskyGetCell(nextKeyFldIndex, newRecordIndex));
                                if ((!riskyGetCell(nextKeyFldIndex, dublicatePrimaryKeyValueRecIndex).equals(riskyGetCell(nextKeyFldIndex, newRecordIndex)))) {
                                    different = true;
                                    break;
                                }
                                i++;
                            }
                            if (!different) {
                                keyOK = false;
                                break;
                            }
                            different = false;
//                        }
                    }else
                        keyOK = false;
                }
            }
            if (!keyOK) {
                for (int i=0; i<CTableFields.size(); i++)
                    ((Array) tableData.at(i)).remove(newRecordIndex);
                throw new DuplicateKeyException(bundle.getString("CTableMsg46"));
            }
        } //If (hasKey()

        recordCount++;
        unselectedSubset.add(recordCount); //new Integer(recordCount));
//        System.out.println("unselected subset: " + unselectedSubset);
        Record r = new Record(null, recordCount); //this, recordCount);

        if (hasKey) {
            primaryKeyMap.add(primaryKeyValueHashCode, r);
        }

        CTableRecords.add(r);
        recordSelection.add(false); //Boolean.FALSE);

        // Update the recordIndex
        int[] tmp = new int[getRecordCount()];
        for (int i =0; i<tmp.length-1; i++)
            tmp[i] = recordIndex[i];
        tmp[tmp.length-1] = getRecordCount()-1;
        recordIndex = tmp;

//        recEntryForm.clear();
        setModified();
//t        if (this.database != null)
            fireRecordAdded(new RecordAddedEvent(this, recordCount, false)); //this.database.CTables.indexOf(this), recordCount));
        return true;
    }


    /**
     *  Removes the record at <i>recIndex</i> from the CTable.
     *  @param recIndex The index of the record to be deleted.
     *  @param rowIndex The index of the record to be deleted in the GUI component. This index
     *  is not used by <i>removeRecord</i>. It is simply passed as an argument to the generated
     *  <i>RecordRemovedEvent</i>. <i>rowIndex</i> defaults to -1 for calls to <i>removeRecord</i>,
     *  which are made internally by other methods of the CTable API.
     *  @param isChanging This parameter is also of no importance to <i>removeRecord</i>. It is
     *  passed to the generated event and is used by the listeners in order to decide when
     *  the GUI component that displays the CTable is to be resized, as a result of the record
     *  removal. When multiple records are removed one after another, the GUI component can
     *  watch this flag in order to get redrawn once, after the last record removal.
     *  @exception TableNotExpandableException If the table is not expandable.
     *  @exception InvalidRecordIndexException If <i>recIndex<0</i> or <i>recIndex>#Records</i>.
     */
    public void removeRecord(int recIndex, int rowIndex, boolean isChanging) throws TableNotExpandableException,
    InvalidRecordIndexException {
        if (recIndex <= recordCount && recIndex >= 0) {
            if (recordRemovalAllowed) {

                /* Removing the record's possible key from "primaryKeyMap". If the record to be removed is
                 * the "pendingNewRecord", which is being interactively added to the table, then the possible
                 * record's key may not have yet been filled by the user. In this case we ckeck if this key
                 * is null, before removing it from "primaryKeyMap".
                 */
                if (hasKey) {
                    Object keyValue = riskyGetCell(((Integer) keyFieldIndices.at(0)).intValue(), recIndex);
//*                    System.out.println("KeyValue: " + keyValue + " Hashcode: " + keyValue.hashCode() + " Class: " + keyValue.getClass().getName());
                    if (!(pendingNewRecord && keyValue == null)) {
                        Integer recPrimaryKey = new Integer(keyValue.hashCode());
                        if ((primaryKeyMap.count(recPrimaryKey)) > 1) {
//                            OrderedMapIterator e = primaryKeyMap.find(recPrimaryKey);
                            HashMapIterator e = primaryKeyMap.find(recPrimaryKey);
                            while (!e.atEnd()) {
                                if ( ( (Record) ((Pair) e.get()).second).getIndex() == recIndex) {
//*                                    System.out.println("Removing from primaryKeyMap: " + primaryKeyMap.remove(e));
                                    primaryKeyMap.remove(e);
                                    break;
                                }
                                e.advance();
                            }
                        }else
                            primaryKeyMap.remove(recPrimaryKey);
//*                            System.out.println("Removing from primaryKeyMap: " + primaryKeyMap.remove(recPrimaryKey));
                    }

                }

                Array removedRecord = new Array();
                for (int i=0; i<tableData.size(); i++) {
                    removedRecord.add(((Array) tableData.at(i)).at(recIndex));
                    ((Array) tableData.at(i)).remove(recIndex);
                }

                //Set the index of the record to be removed to an invalid
                //value(-1), so as it cannot be used with the CTable API anymore
                ((Record) CTableRecords.at(recIndex)).invalidateIndex();
                //Remove the column from the CTableRecords Array
                CTableRecords.remove(recIndex);
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
                //CTableRecords Array
                for (int i=recIndex; i<=recordCount; i++)
                    ((Record) CTableRecords.at(i)).decrementRecordIndex(1);

                if (!isChanging && activeRecord != -1) {
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

                // Find the rowIndex of the supplied record index, if it's not given
//                System.out.println("rowIndex: " + rowIndex);
                if (rowIndex == -1) {
                    for (int i=0; i<recordIndex.length;i++) {
                        if (recordIndex[i] == recIndex) {
                            rowIndex = i;
                            break;
                        }
                    }
                }

                // Update the recordIndex
                int tmp[] = new int[getRecordCount()];
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


//t                if (this.database != null)
                    fireRecordRemoved(new RecordRemovedEvent(this, recIndex, rowIndex, convertArrayToArrayList(removedRecord), isChanging)); //this.database.CTables.indexOf(this), recIndex, rowIndex, removedRecord, isChanging));
                setModified();
                return;
            }else{
                throw new TableNotExpandableException(bundle.getString("CTableMsg47"));
            }
        }else{
            throw new InvalidRecordIndexException(bundle.getString("CTableMsg48") + recIndex);
        }
    }

    /** Sorts a set of records of the CTable in ascending or descending order based on the contents of
     *  one of the CTable's fields.
     *  @param fieldName The name of the field
     *  @param ascending <i>true</i> for ascending sorting. <i>false</i> for descending sorting
     *  @param fromRowIndex The index of the first row in the set of the rows to be sorted
     *  @param toRowIndex The index of the last row.
     *  @exception InvalidFieldException  If the specified field is of <i>image</i> data type.
     */
    public void sortOnField(String fieldName, boolean ascending, int fromRowIndex, int toRowIndex)
    throws InvalidFieldException {
/*        if (fromRowIndex >= toRowIndex)
            return;

        Array fieldContents;
        CTableField f1;
        try{
            fieldContents = getField(fieldName);
            f1 = getCTableField(fieldName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in CTable sortOnField() : (1)"); return;}
        String fieldType = f1.getFieldType().getName();

        if (f1.getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            throw new InvalidFieldException(bundle.getString("CTableMsg98"));

        Array fieldCopy = new Array();
        fieldCopy.ensureCapacity(fieldContents.size());

//        System.out.println("fromRowIndex: " + fromRowIndex + ", toRowIndex: " + toRowIndex);
//        System.out.println("recordCount: " + recordCount + ", recordIndex.length: " + recordIndex.length);
        for (int i=fromRowIndex; i<=toRowIndex; i++)
            fieldCopy.add(fieldContents.at(recordIndex[i]));


        String className = fieldType.substring(fieldType.lastIndexOf('.') + 1);
        BinaryPredicate comparator = null;
        Object valueToReplaceNull;
        if (ascending) {
            if (className.equals("Integer")) {
                try{
                    comparator = new LessNumber(java.lang.Class.forName("java.lang.Integer"));
                }catch (ClassNotFoundException e) {}
                valueToReplaceNull = DBase.INTEGER_FOR_NULL_VALUE;
            }else if (className.equals("Double")) {
                try{
                    comparator = new LessNumber(java.lang.Class.forName("java.lang.Double"));
                }catch (ClassNotFoundException e) {}
                valueToReplaceNull = DBase.DOUBLE_FOR_NULL_VALUE;
            }else if (className.equals("String")) {
                comparator = new LessString();
                valueToReplaceNull = DBase.STRING_FOR_NULL_VALUE;
            }else if (className.equals("Boolean")) {
                comparator = new LessString();
                valueToReplaceNull = DBase.BOOLEAN_FOR_NULL_VALUE;
            }else if (className.equals("URL")) {
                comparator = new LessString();
                valueToReplaceNull = DBase.URL_FOR_NULL_VALUE;
            }else if (className.equals("CDate")) {
                comparator = new LessDate();
                valueToReplaceNull = DBase.DATE_FOR_NULL_VALUE;
            }else if (className.equals("Time")) {
                comparator = new LessDate();
                valueToReplaceNull = DBase.TIME_FOR_NULL_VALUE;
            }else if (className.equals("CImageIcon")) {
                comparator = new LessHashComparator();
                valueToReplaceNull = DBase.IMAGE_FOR_NULL_VALUE;
            }else{
                System.out.println("Serious inconsistency error in DBTable sortOnField() : (2)");
                return;
            }
        }else{
            if (className.equals("Integer")) {
                try{
                    comparator = new GreaterNumber(java.lang.Class.forName("java.lang.Integer"));
                }catch (ClassNotFoundException e) {}
                valueToReplaceNull = DBase.INTEGER_FOR_NULL_VALUE;
            }else if (className.equals("Double")) {
                try{
                    comparator = new GreaterNumber(java.lang.Class.forName("java.lang.Double"));
                }catch (ClassNotFoundException e) {}
                valueToReplaceNull = DBase.DOUBLE_FOR_NULL_VALUE;
            }else if (className.equals("String")) {
                comparator = new GreaterString();
                valueToReplaceNull = DBase.STRING_FOR_NULL_VALUE;
            }else if (className.equals("Boolean")) {
                comparator = new GreaterString();
                valueToReplaceNull = DBase.BOOLEAN_FOR_NULL_VALUE;
            }else if (className.equals("URL")) {
                comparator = new GreaterString();
                valueToReplaceNull = DBase.URL_FOR_NULL_VALUE;
            }else if (className.equals("CDate")) {
                comparator = new GreaterDate();
                valueToReplaceNull = DBase.DATE_FOR_NULL_VALUE;
            }else if (className.equals("CDate")) {
                comparator = new GreaterDate();
                valueToReplaceNull = DBase.TIME_FOR_NULL_VALUE;
            }else if (className.equals("CImageIcon")) {
                comparator = new GreaterHashComparator();
                valueToReplaceNull = DBase.IMAGE_FOR_NULL_VALUE;
            }else{
                System.out.println("Serious inconsistency error in DBTable sortOnField() : (3)");
                return;
            }
        }

        for (int i=0; i<fieldCopy.size(); i++) {
            if (fieldCopy.at(i) == null)
                fieldCopy.put(i, valueToReplaceNull);
        }

        Array intArray = new Array();
        for (int k=fromRowIndex; k<=toRowIndex; k++)
            intArray.add(new Integer(recordIndex[k]));

        MySorting.sort(fieldCopy, comparator, intArray);
        fieldCopy.clear();

        for (int k=fromRowIndex; k<=toRowIndex; k++)
            recordIndex[k] = ((Integer) intArray.at(k-fromRowIndex)).intValue(); // intArray.add(new Integer(recordIndex[k]));

//t        if (this.database != null)
            rowOrderChanged(); //, database.CTables.indexOf(this)));
//        System.out.println("sortOnField. updateActiveRow: " + updateActiveRow + ", activeRow: " + activeRow);
*/
    }


    /** Promotes the selected records to the beginning of the CTable. The actual record index
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
                tmp = recordIndex[destinationIndex];
                recordIndex[destinationIndex] = recordIndex[recIndices[i]];

//                System.out.println("Setting recordIndex["+destinationIndex+"] to : " + recIndices[i]);
                int k = destinationIndex + 1;
                while (k<=recIndices[i]) {
                    tmp2 = recordIndex[k];
                    recordIndex[k] = tmp;
//                    System.out.println("Setting recordIndex["+k+"] to : " + tmp);
                    if (k+1 > recIndices[i])
                        break;
                    tmp = recordIndex[k+1];
                    recordIndex[k+1] = tmp2;
//                    System.out.println("Setting recordIndex["+(k+1)+"] to : " + tmp2);
                    k = k + 2;
                }
                destinationIndex++;
            }
        }
//t        if (this.database != null)
            rowOrderChanged(); //, database.CTables.indexOf(this)));

        }catch (Exception e) {System.out.println(e.getClass().getName() + ", " + e.getMessage());}
    }


    /* Returns the index of the row at which the CTable record at <i>recordIndex</i>
     * is displayed.
     */
    public int rowForRecord(int recordIndex) {
        /* The 'recordIndex' array is created when the first record is added in the CTable. Therefore
         * 'recordIndex' is null for a new CTable without any records inside it.
         */
        if (this.recordIndex == null)
            return -1;
//        System.out.println("CTable: " + title + ", recordCount: " + recordCount + ", recordIndex: " + recordIndex);
        IntArray recordIndexArray = new IntArray(this.recordIndex);
//        System.out.println("record: " + recordIndex + ", row: " + recordIndexArray.indexOf(new Integer(recordIndex)));
        return recordIndexArray.indexOf(new Integer(recordIndex));
    }

    /* Returns the index of the row at which the Table record at <i>recordIndex</i>
     * is displayed.
     */
    public int recordForRow(int row) {
        if (row < 0 || row >= recordIndex.length)
            return -1;
        return recordIndex[row];
    }


    /** This method bypasses any integrity controls and inserts an empty record to the end of the table.
     *  It is used to initiate the interactive insertion of a new record, whch initially is empty
     *  and the user fills its fields one by one. This method should only be used for this purpose.
     *  Inproper use of this method can lead to serious table integrity errors.
     *  <i>addEmptyRecord()</i> returns true, if any other operation should block until the
     *  insertion of the new record finishes. This should happen if the table contains keys and
     *  should last until the user has filled all the key values. If the table is not keyed, this
     *  operation returns false.
     *  @return <i>true</i>, if the table has key, which means that the <i>pendingNewRecord</i> flag is
     *          set. <i>false</i>, if the table does not have a key.
     *  @exception UnableToAddNewRecordException If any of the fields that belong to the table's key is
     *             not editable.
     *  @exception DuplicateKeyException If the insertion of the empty record, violates the key uniqueness
     *             in a table with key. This happens, if the table contains a calculated field, which is
     *             also key, and this field's formula evaluates to a constant value i.e. the formula does
     *             not reference any other field in the table.
     *  @exception NullTableKeyException If the insertion of the empty record, causes a calculated, key field
     *             which does not depend on any other field of the table, to receive the <i>null</i> value.
     */
    public boolean addEmptyRecord() throws UnableToAddNewRecordException, DuplicateKeyException,
    NullTableKeyException, AttributeLockedException {
//        System.out.println("IN ADDEMPTYRECORD-----");
        if (!recordAdditionAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg37"));

        if (hasKey && !dataChangeAllowed)
            throw new UnableToAddNewRecordException(bundle.getString("CTableMsg93"));

        /* If the table has a key and the insertion of the previous record has not
         * finished, then reject the addition of a new empty record.
         */
        if (hasKey && pendingNewRecord)
            throw new UnableToAddNewRecordException(bundle.getString("CTableMsg99"));

        CTableField f;
        for (int i=0; i<CTableFields.size(); i++) {
            f = (CTableField) CTableFields.at(i);
            if (f.isKey() && !f.isEditable() && !f.isCalculated())
                throw new UnableToAddNewRecordException(bundle.getString("CTableMsg49") + f.getName() + "\"");
            /* If a record is inserted in a table, whose hidden fields are not displayed and at least
             * one of its key fields are hidden, then throw an exception.
             */
            if (f.isKey() && f.isHidden() && !hiddenFieldsDisplayed)
                throw new UnableToAddNewRecordException(bundle.getString("CTableMsg92"));

        }


        for (int i=0; i<CTableFields.size(); i++) {
            f = (CTableField) CTableFields.at(i);
            ((Array) tableData.at(i)).add(null);
//            System.out.println(f.getName() + ", " + f.isCalculated() + ", " + f.getDependsOnFields() + ", " + f.oe + ", calculatedValue: " + f.calculatedValue + ", " + f);
            if (f.isCalculated() && f.getDependsOnFields().isEmpty()) {
                try{
                    evaluateCalculatedFieldCell(i, ((Array) tableData.at(i)).size()-1);
                }catch (DuplicateKeyException e) {
                    /* Remove any null values that have already been inserted.
                     */
                    for (int k=0; k<=i; k++)
                        ((Array) tableData.at(k)).remove(recordCount+1);
                    throw new DuplicateKeyException(e.message);
                }
                 catch (NullTableKeyException e) {
                    /* Remove any null values that have already been inserted.
                     */
                    for (int k=0; k<=i; k++)
                        ((Array) tableData.at(k)).remove(recordCount+1);
                    throw new NullTableKeyException(e.message);
                 }
            }
        }

        recordCount++;
        unselectedSubset.add(recordCount); //new Integer(recordCount));
//        System.out.println("unselected subset: " + unselectedSubset);
        Record r = new Record(null, recordCount); //table this, recordCount);

        CTableRecords.add(r);
        recordSelection.add(false); //Boolean.FALSE);

        //Update the recordIndex
        int[] tmp = new int[getRecordCount()];
        for (int i =0; i<tmp.length-1; i++)
            tmp[i] = recordIndex[i];
        tmp[tmp.length-1] = getRecordCount()-1;
        recordIndex = tmp;

        setModified();

//t        if (this.database != null)
            fireEmptyRecordAdded(new RecordAddedEvent(this, recordCount, false)); //this.database.CTables.indexOf(this), recordCount));
        if (!hasKey)
            return false;
        else{
            pendingNewRecord = true;
            return true;
        }
    }


    /** Resets the <i>pendingNewRecord</i> flag. This flag is set when the interactive entry of a new
     *  record starts in a table which has key.
     */
    public void resetPendingNewRecord() {
//        System.out.println("RESET PENDING NEW RECORD");
        pendingNewRecord = false;
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
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           null,
                                                                           null)); //this.database.CTables.indexOf(this), null));
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
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                       SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                                                                       null,
//                                                                       (IntBaseArray) selectedSubset.clone(),
                                                                       prevSelectedSet,
                                                                       null,
                                                                       null)); //this.database.CTables.indexOf(this), null));
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
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                       SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                                                                       null,
//                                                                       (IntBaseArray) selectedSubset.clone(),
                                                                       prevSelectedSet,
                                                                       null,
                                                                       null));
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
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           recordsAddedToSelection,
                                                                           null));
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
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           recordsAddedToSelection,
                                                                           null));
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
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           recordsAddedToSelection,
                                                                           null));
        }
//        System.out.println("Firing event for recindex: " + recIndex);
//        System.out.println(this.database);
//        System.out.println(this.database.CTables.indexOf(this));
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    public void removeFromSelectedSubset(Array recIndices) {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray recordsRemovedFromSelection = new IntBaseArray(recIndices.size());
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
            if (selectedSubset.indexOf(recIndex) != -1) {
                recordSelection.set(recIndex, false); //Boolean.FALSE);
                selectedSubset.removeElements(recIndex); //remove(i);
                unselectedSubset.add(recIndex); //add(i);
                recordsRemovedFromSelection.add(recIndex);
                if (!selectionChanged)
                    selectionChanged = true;
            }
        }

//t        if (this.database != null)
        if (selectionChanged) {
            setModified();
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           null,
                                                                           recordsRemovedFromSelection));
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
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
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           null,
                                                                           recordsRemovedFromSelection));
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
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                           SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                                                                           null,
//                                                                           (IntBaseArray) selectedSubset.clone(),
                                                                           prevSelectedSet,
                                                                           null,
                                                                           recordsRemovedFromSelection));
        }
//t        if (this.database != null)
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    public void resetSelectedSubset() {
        if (selectedSubset.size() == 0) return;
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        for (int i=0; i<selectedSubset.size(); i++) {
            unselectedSubset.add(selectedSubset.get(i));
            recordSelection.set(selectedSubset.get(i), false); //(((Integer) selectedSubset.at(i)).intValue(), false); //Boolean.FALSE);
        }
        selectedSubset.clear();
        setModified();
//t        if (this.database != null)
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                       SelectedRecordSetChangedEvent.RECORDS_REMOVED_FROM_SELECTION,
                                                                       null,
//                                                                       null,
                                                                       prevSelectedSet,
                                                                       null,
                                                                       prevSelectedSet));
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
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                       SelectedRecordSetChangedEvent.RECORDS_ADDED_TO_SELECTION,
                                                                       null,
//                                                                       (IntBaseArray) selectedSubset.clone(),
                                                                       prevSelectedSet,
                                                                       recordsAddedToSelection,
                                                                       null));
    }


    public void invertSelection() {
        IntBaseArray prevSelectedSet = (IntBaseArray) selectedSubset.clone();
        IntBaseArray prevUnselectedSet = (IntBaseArray) unselectedSubset.clone();
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
//            fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this, null)); //this.database.CTables.indexOf(this), null));
        fireSelectedRecordSetChanged(new SelectedRecordSetChangedEvent(this,
                                                                       SelectedRecordSetChangedEvent.RECORD_SELECTION_CHANGED,
                                                                       null,
//                                                                       prevUnselectedSet,
                                                                       prevSelectedSet,
                                                                       null,
                                                                       null));
//        System.out.println("Selected subset: " + selectedSubset);
//        System.out.println("Unelected subset: " + unselectedSubset);
    }


    /** Reports the selection status of a record in the CTable. If <i>recIndex</i> is
     *  less than 0, or greater then the index of the last record, then <i>false</i> is
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


    /**
     *  Removes a record from the CTable.
     *  @param r The record to be deleted.
     *  @exception TableNotExpandableException If the table is not expandable.
     *  @exception InvalidRecordIndexException If the provided record is not valid, e.g. is already deleted.
     */
    public void removeRecord(Record r, int rowIndex, boolean isChanging) throws TableNotExpandableException,
    InvalidRecordIndexException {
//        try {
            removeRecord(r.getIndex(), rowIndex, isChanging);
//        }catch (TableNotExpandableException e) {throw new TableNotExpandableException(e.message);}
//         catch (InvalidRecordIndexException e) {throw new InvalidRecordIndexException(bundle.getString("CTableMsg50"));}
    }


    /**
     *  Enables or disables record addition. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Record addition allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setRecordAdditionAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && recordAdditionAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (recordAdditionAllowed != allowed) {
            recordAdditionAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Record addition allowed", this, new Boolean(allowed));
        }
    }


    /**
     *  Reports if records can be added to the CTable.
     *  @return <i>true</i>, if records can be added to the table. <i>false</i>, otherwise.
     */
    public final boolean isRecordAdditionAllowed() {
        return recordAdditionAllowed;
    }


    /**
     *  Enables or disables record removal. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Record removal allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setRecordRemovalAllowed(boolean allowed) {
        if (database != null && database.isLocked()) return;
        if (recordRemovalAllowed != allowed) {
            recordRemovalAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Record removal allowed", this, new Boolean(allowed));
        }
    }


    /**
     *  Reports if records can be removed from the CTable.
     *  @return <i>true</i>, if records can be added to the table. <i>false</i>, otherwise.
     */
    public boolean isRecordRemovalAllowed() {
        return recordRemovalAllowed;
    }


    /**
     *  Enables or disables field addition. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Field addition allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setFieldAdditionAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && fieldAdditionAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (fieldAdditionAllowed != allowed) {
            fieldAdditionAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Field addition allowed", this, new Boolean(allowed));
        }
    }


    /**
     *  Reports if fields can be added to the CTable.
     *  @return <i>true</i>, if fields can be added to the table. <i>false</i>, otherwise.
     */
    public boolean isFieldAdditionAllowed() {
        return fieldAdditionAllowed;
    }


    /**
     *  Enables or disables field removal. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Field removal allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setFieldRemovalAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && fieldRemovalAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (fieldRemovalAllowed != allowed) {
            fieldRemovalAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Field removal allowed", this, new Boolean(allowed));
        }
    }


    /**
     *  Reports if fields can be removed from the CTable.
     *  @return <i>true</i>, if fields can be removed from the table. <i>false</i>, otherwise.
     */
    public final boolean isFieldRemovalAllowed() {
        return fieldRemovalAllowed;
    }


    /**
     *  Enables or disables field re-ordering. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Field reordering allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setFieldReorderingAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && fieldReorderingAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (fieldReorderingAllowed != allowed) {
            fieldReorderingAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Field reordering allowed", this, new Boolean(allowed));
        }
    }


    /**
     *  Reports if fields can be re-ordered in the CTable.
     *  @return <i>true</i>, if fields can be re-ordered. <i>false</i>, otherwise.
     */
    public final boolean isFieldReorderingAllowed() {
        return fieldReorderingAllowed;
    }


    /**
     *  Enables or disables the change of the CTable's key. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Key change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setKeyChangeAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && keyChangeAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (keyChangeAllowed != allowed) {
            keyChangeAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Key change allowed", this, new Boolean(allowed));
        }
    }


    /**
     *  Reports if the key of the table can be changed.
     *  @return <i>true</i>, if the key can change. <i>false</i>, otherwise.
     */
    public final boolean isKeyChangeAllowed() {
        return keyChangeAllowed;
    }


    /**
     *  Enables or disables hidden fields' display. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Hidden fields displayed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param displayed
     */
    public void setHiddedFieldsDisplayed(boolean displayed) {
//t        if (database != null && !database.isLocked() && hiddenFieldsDisplayed != displayed) {
        if (database != null && database.isLocked()) return;
        if (hiddenFieldsDisplayed != displayed) {
            hiddenFieldsDisplayed = displayed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Hidden fields displayed", this, new Boolean(displayed));
        }
    }


    /**
     *  Reports if the tables hidden fields are displayed
     *  @return <i>true</i>, if the key can change. <i>false</i>, otherwise.
     */
    public final boolean isHiddenFieldsDisplayed() {
        return hiddenFieldsDisplayed;
    }


    /**
     *  Enables or disables editing of the CTable's data. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Data change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setDataChangeAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && dataChangeAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (dataChangeAllowed != allowed) {
            dataChangeAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Data change allowed", this, new Boolean(allowed));
        }
    }

    /**
     *  Reports if the table's data is editable.
     *  @return <i>true</i>, if the data is editable. <i>false</i>, otherwise.
     */
    public final boolean isDataChangeAllowed() {
        return dataChangeAllowed;
    }


    /**
     *  Enables or disables modification of the table's <i>Hidden</i> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Hidden attribute change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the instance of the CTable in the
     *  CDatabase, whose property was changed.
     *  @param allowed
     */
    public void setHiddenAttributeChangeAllowed(boolean allowed) {
//t        if (database != null && !database.isLocked() && hiddenAttributeChangeAllowed != allowed) {
        if (database != null && database.isLocked()) return;
        if (hiddenAttributeChangeAllowed != allowed) {
            hiddenAttributeChangeAllowed = allowed;
            setModified();
//t            if (database != null)
                tablePropertyChangeSupport.firePropertyChange("Hidded attribute change allowed", this, new Boolean(allowed));
        }
    }

    /**
     *  Reports if the table's <i>Hidden</i> attribute can be modified.
     *  @return <i>true</i>, if the data is editable. <i>false</i>, otherwise.
     */
    public final boolean isHiddenAttributeChangeAllowed() {
        return hiddenAttributeChangeAllowed;
    }


    /**
     *  Returns an Array with the contents of a CTable's field.
     *  @param fieldIndex The index of the field.
     *  @return The contents of the field.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
     */
    public Array getField(int fieldIndex) throws InvalidFieldIndexException {
        try{
            return ((Array)tableData.at(fieldIndex));
        }catch (Exception e) {throw new InvalidFieldIndexException(bundle.getString("CTableMsg51"));}
    }


    /**
     *  Returns an Array with the contents of a CTable's field.
     *  @param fieldName The name of the field.
     *  @return The contents of the field.
     *  @exception InvalidFieldNameException If no field <i>fieldName</i> exists in the table.
     */
    public Array getField(String fieldName) throws InvalidFieldNameException {
        if (fieldName == null || fieldName.equals(""))
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));

        boolean fieldFound = false;
        int fieldIndex;
        for (fieldIndex=0; fieldIndex<CTableFields.size(); fieldIndex++) {
            if (((CTableField) CTableFields.at(fieldIndex)).getName().equals(fieldName)) {
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
     *  Get the index of the field <i>fieldName</i> of the CTable. The index of a field
     *  does not change throughout the field's lifetime in the table.
     *  @param fieldName The name of the field.
     *  @return The field's index in the table.
     *  @exception InvalidFieldNameException If the table contains no field named <i>fieldName</i>.
     */
    public int getFieldIndex(String fieldName) throws InvalidFieldNameException {
        if (fieldName == null || fieldName.equals(""))
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));

        boolean fieldFound = false;
        int fieldIndex;
        for (fieldIndex=0; fieldIndex<CTableFields.size(); fieldIndex++) {
            if (((CTableField) CTableFields.at(fieldIndex)).getName().equals(fieldName)) {
               fieldFound = true;
               break;
            }
        }

        if (!fieldFound)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg101"));

        return fieldIndex;
    }


    /**
     *  Returns the name of the field with index <i>fieldIndex</i>.
     *  @param fieldIndex The index of the field.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
     */
    public String getFieldName(int fieldIndex) throws InvalidFieldIndexException {
        try{
            String fieldName = ((CTableField) CTableFields.at(fieldIndex)).getName();
            return fieldName;
        }catch (Exception e) {throw new InvalidFieldIndexException(bundle.getString("CTableMsg51") + " \"" + fieldIndex + "\".");}
    }


    /**
     *  Returns an Array with the contents of the record at <i>recIndex</i>.
     *  @param recIndex The index of the record.
     *  @exception InvalidRecordIndexException If <i>recIndex<0</i> or <i>recIndex>#Records</i>.
     */
    public Array getRecord(int recIndex) throws InvalidRecordIndexException {
        if (recIndex <= recordCount && recIndex >= 0) {
            Array record = new Array();
            for (int i=0; i<tableData.size(); i++)
                record.add(((Array) tableData.at(i)).at(recIndex));
            return record;
        }else
            throw new InvalidRecordIndexException(bundle.getString("CTableMsg50") + recIndex);
    }


    /**
     *  Returns an Array with the contents of record <i>r</i>.
     *  @param r The record, whose contents will be returned.
     *  @exception InvalidRecordIndexException If an invalid record is provided, e.g. a deleted one.
     */
    public Array getRecord(Record r) throws InvalidRecordIndexException {
        try{
            return getRecord(r.getIndex());
        }catch (InvalidRecordIndexException e) {throw new InvalidRecordIndexException(e.message);}
    }


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
     *  Returns the number of records in the CTable.
     */
    public int getRecordCount() {
        return recordCount+1;
    }


    /**
     *  Returns the number of fields in the CTable.
     */
    public int getFieldCount() {
        return CTableFields.size();
    }

    /**
     *  Returns the number of <i>Image</i> fields in the CTable.
     */
    public int getImageFieldCount() {
        int imageFieldCount = 0;
        for (int i=0; i<CTableFields.size(); i++) {
            if (((CTableField) CTableFields.at(i)).getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
                imageFieldCount++;
        }

        return imageFieldCount;
    }


    /**
     *  Returns an Array of the fields of the CTable.
     */
    public Array getFields() {
        return CTableFields;
    }

    /**
     *  Creates an empty Array whose capacity is set to the number of fields in the CTable.
     *  This Array can be used to insert new records in the table.
     *  @see #addRecord(com.objectspace.jgl.Array)
     */
    public Array createEmptyRecordEntryForm() {
        Array recEntryForm = new Array();
        recEntryForm.ensureCapacity(CTableFields.size());
        return recEntryForm;
    }


    /** Returns the contents of the cell at <i>fieldIndex</i> and <i>recIndex</i>.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public Object getCell(int fieldIndex, int recIndex) throws InvalidCellAddressException {
        if (fieldIndex < CTableFields.size() && fieldIndex >= 0 && recIndex <= recordCount && recIndex >= 0)
            return ((Object) ((Array) tableData.at(fieldIndex)).at(recIndex));
        else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
    }


    /**
     *  Returns the contents of the cell  at <i>fieldIndex</i> and <i>recIndex</i>. This method
     *  is a faster version of <b>getCell()</b>. It is faster because no checks are
     *  made whether the given cell address is correct. Therefore it may fail, if not
     *  used carefully.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     */
    public Object riskyGetCell(int fieldIndex, int recIndex) {
        return ((Object) ((Array) tableData.at(fieldIndex)).at(recIndex));
    }

    /** Returns the contents of the cell which belongs to field <i>fieldName</i> and
     *  record <i>recIndex</i>.
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


    /** Returns the contents of the cell which belongs to field <i>f</i> and
     *  record <i>r</i>.
     *  @param f The field to which the cell belongs.
     *  @param r The record which contains the cell.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public Object getCell(CTableField f, Record r) throws InvalidCellAddressException {
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


    /**
     *  Sets the specified cell to <i>value</i>.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <i>true</i> if the cell is set. <i>false</i>, if the field is not editable.
     *  @exception InvalidCellAddressException Invalid cell address.
     *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <i>value</i> is <i>null</i>.
     *  @exception InvalidDataFormatException If the supplied <i>value</i> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception DuplicateKeyException If the cell is part of the record's key and the new <i>value</i> violates the key uniqueness.
     */
    public boolean setCell(int fieldIndex, int recIndex, Object value) throws InvalidCellAddressException,
    NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException {
        if (!dataChangeAllowed && !pendingNewRecord)
            throw new AttributeLockedException(bundle.getString("CTableMsg89"));

/*        Array calculatedCellAddresses = new Array();
        Array calculatedCellAffectOthers = new Array();
        Array oldValues = new Array();
        Array newValues = new Array();
*/
        if (fieldIndex < CTableFields.size() && fieldIndex >= 0 && recIndex <= recordCount && recIndex >= 0) {
            Object oldValue = riskyGetCell(fieldIndex, recIndex);
            boolean affectsOtherCells = false;
            Class[] cl = new Class[1];
            Object[] val = new Object[1];
            Constructor con;

            CTableField f = (CTableField) CTableFields.at(fieldIndex);

            if (!f.isEditable())
                return false;

            if (value == null || (value.getClass().isInstance("a string") && ((String) value).equals(""))) {
                if (f.isKey())
                    throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));
                else{
                    Object previousValue = ((Array) tableData.at(fieldIndex)).at(recIndex);
                    ((Array) tableData.at(fieldIndex)).put(recIndex, null);
                    if (!f.dependentCalcFields.isEmpty()) {
                        affectsOtherCells = true;
                        try{
                            for (int i=0; i<f.dependentCalcFields.size(); i++) {
                                evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

                                CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                                changedCalcCells.add(f1.getName(), new Integer(recIndex));
///                                calculatedCellAddresses.add(new int[] {((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex});
                            }
                            calculatedFieldsChanged = true;
                        }catch (DuplicateKeyException e) {
                            changedCalcCells.clear();
                            /* Undo the change. Recalculate the dependent calculated fields.
                             */
                            ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                            calculatedFieldsChanged = false;
                            try{
                                for (int i=0; i<f.dependentCalcFields.size(); i++)
                                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (1)");}
                             catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (1)");}
                            throw new DuplicateKeyException(e.message);
                        }
                         catch (NullTableKeyException e) {
                            changedCalcCells.clear();
                            /* Undo the change. Recalculate the dependent calculated fields.
                             */
                            ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                            calculatedFieldsChanged = false;
                            try{
                                for (int i=0; i<f.dependentCalcFields.size(); i++)
                                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                            }catch (DuplicateKeyException e1) {
                                if (!pendingNewRecord)
                                    System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (2)");
                             }
                             catch (NullTableKeyException e1) {
                                if (!pendingNewRecord)
                                    System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (2)");
                             }

                            throw new NullTableKeyException(e.message);
                        }
                    }
                    setModified();
//t                    if (this.database != null)
                        fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, /*value,*/ oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));

                    return true;
                }
            }

            //If the "value" object is of the same type as the field
            //to be inserted to, then don't perform any data type conversion
            if (!f.fieldType.isInstance(value)) {
//t              if (f.fieldType.getName().equals("java.util.Date")) {
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
//                        System.out.println("value: " + value);
                    t = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
                }catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));}

                if (t == null)
                    throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));

                value = t;
              }else if (f.getFieldType().getName().equals("java.lang.Double") && java.lang.String.class.isInstance(value)) {
                try{
//                    System.out.println("value: " + value + ", " + value.getClass());
//                    System.out.println(numberFormat.parse((String) value) + ", " + numberFormat.parse((String) value).getClass() + ", " + numberFormat.parse((String) value).doubleValue() +
//                    ", " + numberFormat.parse((String) value).doubleValue());
                    value = new Double(numberFormat.parse((String) value).doubleValue());
//                    System.out.println("value: " + value + ", " + value.getClass());
                }catch (DoubleNumberFormatParseException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}

              /* If the "value"'s class is different from the field's class then:
               *    * get the field's and the value's Class,
               *    * get a constructor of the field's Class that takes one
               *      parameter of the Class of the "value", if one exists,
               *    * call the constructor, with the "value" object as argument and
               *      insert the returned value to the field.
               */
              }else{
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
                    con = f.fieldType.getConstructor(cl);
                    try {
//                        System.out.println("Supplied value: " + value);
                        value =  con.newInstance(val);
//                        System.out.println("value: " + value + ", " + value.getClass());
                    }catch (InvocationTargetException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                     catch (InstantiationException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                     catch (IllegalAccessException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                     catch (UnableToCreateImageIconException e) {throw new InvalidDataFormatException(e.getMessage());}
                  }catch (NoSuchMethodException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
              }
            }else{
                /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
                 * attribute has to be synchronized against the field's "linkToExternalData" attribute.
                 */
                if (f.fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                    if (f.containsLinksToExternalData())
                        ((CImageIcon) value).referenceToExternalFile = true;
                    else
                        ((CImageIcon) value).referenceToExternalFile = false;
                }
            }

            /*If the new value of the cell is equal to existing then
             *do nothing
             */
            if (value.equals(riskyGetCell(fieldIndex, recIndex)))
                return true;

            if (f.isKey()) {
                if (checkKeyUniquenessOnNewValue(fieldIndex, recIndex, value) == false)
                    throw new DuplicateKeyException(bundle.getString("CTableMsg60") + value + bundle.getString("CTableMsg61"));

                /*If field f is part of the table's key and is also the field whose values
                 *are used in the primarykeyMap HashMap, then the entry for this record in
                 *the HashMap has to be deleted and a new entry with the correct new key
                 *value has to be entered.
                 */
                updatePrimaryKeyMapForCalcField(fieldIndex, recIndex, value);
            }

            Object previousValue = ((Array) tableData.at(fieldIndex)).at(recIndex);
            ((Array) tableData.at(fieldIndex)).put(recIndex, value);
            try{
                if (!f.dependentCalcFields.isEmpty()) {
                    affectsOtherCells = true;
                    for (int i=0; i<f.dependentCalcFields.size(); i++) {
                        evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

                        CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                        changedCalcCells.add(f1.getName(), new Integer(recIndex));
                    }
                    calculatedFieldsChanged = true;
                }
            }catch (DuplicateKeyException e) {
                if (!pendingNewRecord) {
                    changedCalcCells.clear();
                    /* Undo the change. Recalculate the dependent calculated fields.
                     */
                    ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                    try{
                        for (int i=0; i<f.dependentCalcFields.size(); i++)
                            evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                    }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (3)");}
                     catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (3)");}

                }
                throw new DuplicateKeyException(e.message);
            }
             catch (NullTableKeyException e) {
                if (!pendingNewRecord) {
                    changedCalcCells.clear();
                    ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                    try{
                        for (int i=0; i<f.dependentCalcFields.size(); i++)
                            evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                    }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (4)");}
                     catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (4)");}

                }
                throw new NullTableKeyException(e.message);
            }

            setModified();
//t            if (this.database != null)
                fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, /*value,*/ oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));
            return true;

        }else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
    }


    /** Checks if new key value <i>value</i> is unique. Flag <i>insertingNewRecord</i> is set when
     *  an interactive record insertion takes place. In this case an empty record has been inserted
     *  in the table prior to setting of the new record's values, so <i>checkKeyUniquenessOnNewValue</i>
     *  will search all the records but the one which is being added. In the common case where the value
     *  of a key field is changed, <i>insertingNewRecord</i> should be <i>false</i>.
     */
    private boolean checkKeyUniquenessOnNewValue(int fieldIndex, int recIndex, Object value) { //, boolean insertingNewRecord) {
//        System.out.println("checkKeyUniquenessOnNewValue value: " + value + " on fieldIndex: " + fieldIndex);
//        System.out.println("recIndex: " + recIndex + " recordCount: " + recordCount);
//        System.out.println(((Array) tableData.at(fieldIndex)));

        boolean valueExists = false;
        if (recIndex == 0) {
            valueExists = (((Array) tableData.at(fieldIndex)).indexOf(1, recordCount, value) != -1);
//            System.out.println("1Checking from : 1 to : " + (recordCount));
        }else if (recIndex == recordCount) {
//            System.out.println(tableData.at(fieldIndex) + "   fieldIndex: " + fieldIndex);
//            System.out.println("Found value: " + value + "(" + value.getClass() + ")" + "? " + ((Array) tableData.at(fieldIndex)).indexOf(0, recordCount-1, value));
            valueExists = (((Array) tableData.at(fieldIndex)).indexOf(0, recordCount-1, value) != -1);
//            System.out.println("2-----------Checking from : 0 to : " + (recordCount-1));
        }else{
            valueExists = ((((Array) tableData.at(fieldIndex)).indexOf(0, recIndex-1, value) != -1) ||
                           (((Array) tableData.at(fieldIndex)).indexOf(recIndex+1, recordCount, value) != -1));
//            System.out.println("3Checking from : 0  to : " + (recIndex-1) + " and from : " + (recIndex+1) + " to : " + (recordCount));
        }
//        if (((Array) tableData.at(fieldIndex)).indexOf(0, recIndex-1, value) != -1 || ((Array) tableData.at(fieldIndex)).indexOf(recIndex+1, recordCount, value) != -1)           contains(value)) {
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
                while ((index = ((Array) tableData.at(fieldIndex)).indexOf(index+1, searchUpToRecord, value)) != -1 &&
                        index != recIndex) {
//                    System.out.println("Found " + value + " at index: " + index);
//                    System.out.println("keyFieldIndices: " + keyFieldIndices);
                    while (i<keyFieldIndices.size()) {
                        int nextKeyFldIndex = ((Integer) keyFieldIndices.at(i)).intValue();
                        /* Don't compare the two records on the field that has changed. Equality
                         * on these fields is guaranteed by the first while-loop condition.
                         */
                        if (nextKeyFldIndex == fieldIndex)  {
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


    /**
     *  Sets the specified cell to <i>value</i>.
     *  @param fieldName The name of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <i>true</i> if the cell is set. <i>false</i>, if the field is not editable.
     *  @exception InvalidCellAddressException Invalid cell address.
     *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <i>value</i> is <i>null</i>.
     *  @exception InvalidDataFormatException If the supplied <i>value</i> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception DuplicateKeyException If the cell is part of the record's key and the new <i>value</i> violates the key uniqueness.
     */
    public boolean setCell(String fieldName, int recIndex, Object value) throws InvalidCellAddressException,
    NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException {
        if (!dataChangeAllowed && !pendingNewRecord)
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

            CTableField f = (CTableField) CTableFields.at(fieldIndex);

            if (!f.isEditable())
                return false;

//            System.out.println("value: " + value + ", value class: " + value.getClass());
            if (value == null || (value.getClass().isInstance("a string") && ((String) value).equals(""))) {
                if (f.isKey())
                    throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));
                else{
                    Object previousValue = ((Array) tableData.at(fieldIndex)).at(recIndex);
                    ((Array) tableData.at(fieldIndex)).put(recIndex, null);
                    if (!f.dependentCalcFields.isEmpty()) {
                        affectsOtherCells = true;
                        try{
                            for (int i=0; i<f.dependentCalcFields.size(); i++) {
                                evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

                                CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                                changedCalcCells.add(f1.getName(), new Integer(recIndex));
                            }
                            calculatedFieldsChanged = true;
                        }catch (DuplicateKeyException e) {
                            changedCalcCells.clear();
                            /* Undo the change. Recalculate the dependent calculated fields.
                             */
                            ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                            try{
                                for (int i=0; i<f.dependentCalcFields.size(); i++)
                                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (1)");}
                             catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (1)");}
                            throw new DuplicateKeyException(e.message);
                        }
                         catch (NullTableKeyException e) {
                            changedCalcCells.clear();
                            /* Undo the change. Recalculate the dependent calculated fields.
                             */
                            ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                            try{
                                for (int i=0; i<f.dependentCalcFields.size(); i++)
                                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (2)");}
                             catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (2)");}

                            throw new NullTableKeyException(e.message);
                        }
                    }
                    setModified();
//t                    if (this.database != null)
                        fireCellValueChanged(new CellValueChangedEvent(this, fieldName, recIndex, /*value,*/ oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), fieldName, recIndex, value, oldValue, affectsOtherCells));
                    return true;
                }
            }

//            System.out.print(value.getClass() + "     " + f.fieldType);
            //If the "value" object is of the same type as the field
            //to be inserted to, then don't perform any data type conversion
            if (!f.fieldType.isInstance(value)) {
            //if the field's type is Date ot Time
//t              if (f.fieldType.getName().equals("java.util.Date")) {
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

/*                    ((Array) tableData.at(fieldIndex)).put(recIndex, d);
                  if (!f.dependentCalcFields.isEmpty()) {
                      for (int i=0; i<f.dependentCalcFields.size(); i++)
                          evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                  }
                  setModified();
                  return true;
*/
                  value = d;
              }else if (f.fieldType.getName().equals("gr.cti.eslate.database.engine.CTime")) {
                  CTime t = new CTime();
                  try{
                      t = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
                  }catch (Exception e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));}

                  if (t == null)
                      throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + "\"" + bundle.getString("CTableMsg41"));

/*                    ((Array) tableData.at(fieldIndex)).put(recIndex, t);
                  if (!f.dependentCalcFields.isEmpty()) {
                      for (int i=0; i<f.dependentCalcFields.size(); i++)
                          evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                  }
                  setModified();
                  return true;
*/
                  value = t;
              }else if (f.getFieldType().getName().equals("java.lang.Double") && java.lang.String.class.isInstance(value)) {
                try{
                    value = new Double(numberFormat.parse((String) value).doubleValue());
                }catch (DoubleNumberFormatParseException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}

              /* If the "value"'s class is different from the field's class then:
               *    * get the field's and the value's Class,
               *    * get a constructor of the field's Class that takes one
               *      parameter of the Class of the "value", if one exists,
               *    * call the constructor, with the "value" object as argument and
               *      insert the returned value to the field.
               */
              }else{
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
                        con = f.fieldType.getConstructor(cl);
                        try {
                            value =  con.newInstance(val);
                        }catch (InvocationTargetException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                         catch (InstantiationException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                         catch (IllegalAccessException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
                         catch (UnableToCreateImageIconException e) {throw new InvalidDataFormatException(e.getMessage());}
                    }catch (NoSuchMethodException e) {throw new InvalidDataFormatException(bundle.getString("CTableMsg42") + value + bundle.getString("CTableMsg43") + nameForDataTypeInAitiatiki(f));}
              }
           }else{
                /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
                 * attribute has to be synchronized against the field's "linkToExternalData" attribute.
                 */
                if (f.fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                    if (f.containsLinksToExternalData())
                        ((CImageIcon) value).referenceToExternalFile = true;
                    else
                        ((CImageIcon) value).referenceToExternalFile = false;
                }
            }

            /*If the new value of the cell is equal to existing then
             *do nothing
             */
            if (value.equals(riskyGetCell(fieldIndex, recIndex)))
                return true;

            if (f.isKey()) {
                if (checkKeyUniquenessOnNewValue(fieldIndex, recIndex, value) == false)
                    throw new DuplicateKeyException(bundle.getString("CTableMsg60") + value + bundle.getString("CTableMsg61"));

                /*If field f is part of the table's key and is also the field whose values
                 *are used in the primarykeyMap HashMap, then the entry for this record in
                 *the HashMap has to be deleted and a new entry with the correct new key
                 *value has to be entered.
                 */
                updatePrimaryKeyMapForCalcField(fieldIndex, recIndex, value);
            }

            Object previousValue = ((Array) tableData.at(fieldIndex)).at(recIndex);
            ((Array) tableData.at(fieldIndex)).put(recIndex, value);
            try{
                if (!f.dependentCalcFields.isEmpty()) {
                    affectsOtherCells = true;
                    for (int i=0; i<f.dependentCalcFields.size(); i++) {
                        evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

                        CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                        changedCalcCells.add(f1.getName(), new Integer(recIndex));
                    }
                    calculatedFieldsChanged = true;
                }
            }catch (DuplicateKeyException e) {
                changedCalcCells.clear();
                /* Undo the change. Recalculate the dependent calculated fields.
                 */
                ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                try{
                    for (int i=0; i<f.dependentCalcFields.size(); i++)
                        evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (3)");}
                 catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (3)");}

                throw new DuplicateKeyException(e.message);
            }
             catch (NullTableKeyException e) {
                changedCalcCells.clear();
                ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                try{
                    for (int i=0; i<f.dependentCalcFields.size(); i++)
                        evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (4)");}
                 catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (4)");}

                throw new NullTableKeyException(e.message);
            }
            setModified();
//t            if (this.database != null)
                fireCellValueChanged(new CellValueChangedEvent(this, fieldName, recIndex, /*value,*/ oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), fieldName, recIndex, value, oldValue, affectsOtherCells));
            return true;


        }else
            throw new InvalidCellAddressException(bundle.getString("CTableMsg52") + fieldIndex + bundle.getString("CTableMsg53") + recIndex);
    }


    /**
     *  Returns the index of field <i>f</i>.
     *  @param f The field.
     *  @return -1, if field <i>f</i> does not belong to the CTable.
     *  @exception InvalidFieldException If the supplied field is invalid, e.g. has been deleted.
     */
    //getFieldIndex(CTableField)
    public int getFieldIndex(CTableField f) throws InvalidFieldException {
        String fieldName = f.getName();
        if (fieldName != null) {
            for (int i=0; i<CTableFields.size(); i++) {
                if (((CTableField) CTableFields.at(i)).getName().equals(fieldName))
                    return i;
            }
            System.out.println("Serious inconsistency error. The supplied field, though valid, is not included in the CTableFields Array.");
            return -1;
        }else
            throw new InvalidFieldException(bundle.getString("CTableMsg62"));
    }

    /**
     *  Sets the specified cell to <i>value</i>.
     *  @param fld The field to which the cell belongs.
     *  @param r The record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <i>true</i> if the cell is set. <i>false</i>, if the field is not editable.
     *  @exception InvalidCellAddressException Invalid cell address.
     *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <i>value</i> is <i>null</i>.
     *  @exception InvalidDataFormatException If the supplied <i>value</i> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception DuplicateKeyException If the cell is part of the record's key and the new <i>value</i> violates the key uniqueness.
     */
    //setCell(CTableField, Record, Object)
    public boolean setCell(CTableField fld, Record r, Object value) throws InvalidCellAddressException,
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

        CTableField f = (CTableField) CTableFields.at(fieldIndex);

        if (!f.isEditable())
            return false;

        if (value == null || (value.getClass().isInstance("a string") && ((String) value).equals(""))) {
            if (f.isKey())
                throw new NullTableKeyException(bundle.getString("CTableMsg58") + f.getName() + bundle.getString("CTableMsg59"));
            else{
                Object previousValue = ((Array) tableData.at(fieldIndex)).at(recIndex);
                ((Array) tableData.at(fieldIndex)).put(recIndex, null);
                if (!f.dependentCalcFields.isEmpty()) {
                    affectsOtherCells = true;
                    try{
                        for (int i=0; i<f.dependentCalcFields.size(); i++) {
                            evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

                            CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                            changedCalcCells.add(f1.getName(), new Integer(recIndex));
                        }
                        calculatedFieldsChanged = true;
                    }catch (DuplicateKeyException e) {
                        changedCalcCells.clear();
                        /* Undo the change. Recalculate the dependent calculated fields.
                         */
                        ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                        try{
                            for (int i=0; i<f.dependentCalcFields.size(); i++)
                                evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                        }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (1)");}
                         catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (1)");}
                        throw new DuplicateKeyException(e.message);
                    }
                     catch (NullTableKeyException e) {
                        changedCalcCells.clear();
                        /* Undo the change. Recalculate the dependent calculated fields.
                         */
                        ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
                        try{
                            for (int i=0; i<f.dependentCalcFields.size(); i++)
                                evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
                        }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (2)");}
                         catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (2)");}

                        throw new NullTableKeyException(e.message);
                     }
                }
                setModified();
//t                if (this.database != null)
                    fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, /*value,*/ oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));
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
                /* If the "value"'s class is different from the field's class then:
                 *    * get the field's and the value's Class,
                 *    * get a constructor of the field's Class that takes one
                 *      parameter of the Class of the "value", if one exists,
                 *    * call the constructor, with the "value" object as argument and
                 *      insert the returned value to the field.
                 */

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
            /* When a CImageIcon is added to the ctable, its "referenceToExternalFile"
             * attribute has to be synchronized against the field's "linkToExternalData" attribute.
             */
            if (f.fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                if (f.containsLinksToExternalData())
                    ((CImageIcon) value).referenceToExternalFile = true;
                else
                    ((CImageIcon) value).referenceToExternalFile = false;
            }
        }

        /*If the new value of the cell is equal to existing then
         *do nothing
         */
        if (value.equals(riskyGetCell(fieldIndex, recIndex)))
            return true;

        if (f.isKey()) {
            if (checkKeyUniquenessOnNewValue(fieldIndex, recIndex, value) == false)
                throw new DuplicateKeyException(bundle.getString("CTableMsg60") + value + bundle.getString("CTableMsg61"));

            /* If field f is part of the table's key and is also the field whose values
             * are used in the primarykeyMap HashMap, then the entry for this record in
             * the HashMap has to be deleted and a new entry with the correct new key
             * value has to be entered.
             */
            updatePrimaryKeyMapForCalcField(fieldIndex, recIndex, value);
        }

        Object previousValue = ((Array) tableData.at(fieldIndex)).at(recIndex);
        ((Array) tableData.at(fieldIndex)).put(recIndex, value);
        try{
            if (!f.dependentCalcFields.isEmpty()) {
                affectsOtherCells = true;
                for (int i=0; i<f.dependentCalcFields.size(); i++) {
                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);

                    CTableField f1 = (CTableField) CTableFields.at(((Integer) f.dependentCalcFields.at(i)).intValue());
                    changedCalcCells.add(f1.getName(), new Integer(recIndex));
                }
                calculatedFieldsChanged = true;
            }
        }catch (DuplicateKeyException e) {
            changedCalcCells.clear();
            /* Undo the change. Recalculate the dependent calculated fields.
             */
            ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
            try{
                for (int i=0; i<f.dependentCalcFields.size(); i++)
                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (3)");}
             catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (3)");}

            throw new DuplicateKeyException(e.message);
        }
        catch (NullTableKeyException e) {
            changedCalcCells.clear();
            ((Array) tableData.at(fieldIndex)).put(recIndex, previousValue);
            try{
                for (int i=0; i<f.dependentCalcFields.size(); i++)
                    evaluateCalculatedFieldCell(((Integer) f.dependentCalcFields.at(i)).intValue(), recIndex);
            }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (4)");}
             catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in CTable setCell(int, int, Object): (4)");}

            throw new NullTableKeyException(e.message);
        }

        setModified();
//t        if (this.database != null)
            fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, /*value,*/ oldValue, affectsOtherCells)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, affectsOtherCells));
        return true;
    }


    /**
     *  Sets the specified cell to <i>value</i>. This method is a faster version of setCell().
     *  It is faster because it performs no checks regarding the address of the cell, the
     *  validity and the uniqueness of the key or the <i>value</i>'s data type. Therefore
     *  it should be used very carefully.
     *  @param fieldIndex The index of the field to which the cell belongs.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <i>true</i> if the cell is set. <i>false</i>, if the field is not editable.
     *  @see #setCell(int, int, java.lang.Object)
     */
    //riskySetCell(int, int, Object)
    private void riskySetCell(int fieldIndex, int recIndex, Object value) {
        Object oldValue = riskyGetCell(fieldIndex, recIndex);
        ((Array) tableData.at(fieldIndex)).put(recIndex, value);
        try{
            CTableField f = getCTableField(fieldIndex);
//t            if (this.database != null) {
                if (f.dependentCalcFields.isEmpty())
                    fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, /*value,*/ oldValue, false)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, false));
                else
                    fireCellValueChanged(new CellValueChangedEvent(this, f.getName(), recIndex, /*value,*/ oldValue, true)); //this.database.CTables.indexOf(this), f.getName(), recIndex, value, oldValue, true));
//t            }
        }catch (InvalidFieldIndexException e) {}

    }


    /**
     *  Returns the field at <i>fieldIndex</i>. It may fail, so it should be used
     *  carefully.
     *  @param fieldIndex The index of the field.
     */
    protected CTableField riskyGetField(int fieldIndex) {
        return (CTableField) CTableFields.at(fieldIndex);
    }

    /**
     *  Returns the record at <i>recIndex</i>. It may fail, so it should be used
     *  carefully.
     *  @param recIndex The index of the record.
     */
    private Record riskyGetRecord(int recIndex) {
        return (Record) CTableRecords.at(recIndex);
    }


    /**
     *  Returns a record as an Object Array. It may fail if an invalid record index is supplied,
     *  so it should be used carefully.
     *  @param recIndex The index of the record.
     */
     public Object[] riskyGetRecObjects(int recIndex) {
        Object[] rec = new Object[getFieldCount()];
        for (int i=0; i<getFieldCount(); i++)
            rec[i] = ((Array) tableData.at(i)).at(recIndex);

        return rec;
     }


    /** Reports whether some value(s) in one or more calculated fields have changed as a result
     *  of editing a value in a non-calculated field. It does not report any change when all the
     *  values of a calculated field have changed as a result of editing the field's formula.
     */
    public boolean haveCalculatedFieldsChanged() {
        return calculatedFieldsChanged;
    }


    /** Returns a HashMap with the addresses of the cells of the calculated fields of the table, whose
     *  values have been changed as a result of editing a value of a non-calculated field. The cell
     *  address is provided as an entry in the HashMap, whose key is the name of the field and its
     *  value is the record index of the cell.
     */
    public HashMap getChangedCalcFieldCells() {
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
     *  Add field <i>fieldName</i> to the CTable's key.
     *  @param fieldName The name of the field.
     *  @exception FieldAlreadyInKeyException If this field is already part of the table's key.
     *  @exception FieldContainsEmptyCellsException If field <i>fieldName</i> contains empty cells.
     *  @exception InvalidFieldNameException If the table contains no field named <i>fieldName</i>.
     *  @exception TableNotExpandableException Adding a new field to the table's (existing or unexisting) key may
     *             result in the removal of several records, in order to ensure key uniqueness. This is
     *             done seamlessly. However if the table is not record expandable and the addition
     *             of this field to the table's key requires the removal of some records, then
     *             this exception will be thrown.
     *  @exception InvalidKeyFieldException If field <i>fieldName</i>, is of <i>Image</i> data type.
     *  @exception AttributeLockedException If the field's <i>key</i> attribute is locked.
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

        CTableField f = (CTableField) CTableFields.at(fieldIndex);

        if (f.getFieldType().equals(CImageIcon.class))
            throw new InvalidKeyFieldException(bundle.getString("CTableMsg67"));
        if (!f.isFieldKeyAttributeChangeAllowed())
            throw new AttributeLockedException(bundle.getString("CTableFieldMsg1"));

//        System.out.println("Field " + fieldName + " isKey: " + f.isKey());
        if (f.isKey())
            throw new FieldAlreadyInKeyException(bundle.getString("CTableMsg100") + f.getName() + bundle.getString("CTableMsg68"));
        else{
            //Check if this column contains any "null" values
            for (int i = 0; i<=recordCount; i++) {
                if ( ((Array) tableData.at(fieldIndex)).at(i) == null)
                    throw new FieldContainsEmptyCellsException(bundle.getString("CTableMsg100") + f.getName() + bundle.getString("CTableMsg69"));
            }

            try{
                f.setKey(true);
            }catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable _addToKey() : (0.5)"); return;}

            if (this.hasKey) {
                keyFieldIndices.add(new Integer(fieldIndex));
            }else{
//                OrderedMap temp2 = new OrderedMap();
//                primaryKeyMap = new OrderedMap(true);
                HashMap temp2 = new HashMap(new EqualTo());
                primaryKeyMap = new HashMap(new EqualTo(), true, 10, (float) 1.0);
//                System.out.println(primaryKeyMap);
                Array temp = (Array) tableData.at(fieldIndex);
                //Parse all the records, but the last one
                Object res;
                int i=0;
                while (i<=recordCount) {
                    res = temp2.add(temp.at(i), riskyGetRecord(i));
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
                             catch (AttributeLockedException e1) {System.out.println("Serious inconsistency error in CTable addToKey() : (1.5)");}
                        }
                        catch (TableNotExpandableException e) {
                            primaryKeyMap = null;
                            try{
                                f.setKey(false);
                            }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable addToKey() : (2)");}
                             catch (AttributeLockedException e1) {System.out.println("Serious inconsistency error in CTable addToKey() : (2.5)");}
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
                keyFieldIndices.add(new Integer(fieldIndex));
//                System.out.println("Field " + fieldName + " isKey: " + f.isKey());
            }

            /* Adjust the active record.
             */
            if (activeRecord != -1) {
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
//t            if (database != null)
                fireColumnKeyChanged(new ColumnKeyChangedEvent(this, f.getName(), true)); //database.CTables.indexOf(this), f.getName(), true));
        }
    }


    /**
     *  Removes field <i>fieldName</i> from the CTable's key.
     *  @param fieldName The name of the field.
     *  @exception FieldIsNotInKeyException If field <i>fieldName</i> is not part of the
     *             table's key.
     *  @exception InvalidFieldNameException If the table does not contain any field named
     *             <i>fieldName</i>.
     *  @exception TableNotExpandableException When a field is removed from the table's key
     *             (in the case of composite keys) several records may have to be removed
     *             from the table, in order to ensure key uniqueness. This is transparent
     *             to the user. However if the table is not record expandable and the addition
     *             of this field to the table's key requires the removal of some records, then
     *             this exception will be thrown.
     *  @exception AttributeLockedException If field's <i>key</i> attribute is locked.
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

        CTableField f = (CTableField) CTableFields.at(fieldIndex);
        if (!f.isFieldKeyAttributeChangeAllowed())
            throw new AttributeLockedException(bundle.getString("CTableFieldMsg1"));

        if (!f.isKey())
            throw new FieldIsNotInKeyException(bundle.getString("CTableMsg100") + f.getName() + bundle.getString("CTableMsg71"));
        else{
            if (keyFieldIndices.size() == 1) {
                keyFieldIndices.clear();
                primaryKeyMap.clear();
                primaryKeyMap = null;
                hasKey = false;
                /* No exception should be thrown here, because only "setKey(true)" can
                 * fire an exception.
                 */
                try{
                    f.setKey(false);
                }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in CTable removeFromKey() : (1)");}
                setModified();
            }else{

                //Keep this back-up copy of primaryKeyMap and keyFieldIndices
                HashMap primaryKeyMapBackUp = (HashMap) primaryKeyMap.clone();
                Array keyFieldIndicesBackUp = (Array) keyFieldIndices.clone();

                /* No exception should be thrown here, because only "setKey(true)" can
                 * fire an exception.
                 */
                try{
                    f.setKey(false);
                }catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in CTable removeFromKey() : (2)");}
                setModified();
                if (((Integer)keyFieldIndices.at(0)).intValue() == fieldIndex) {
//                    System.out.println("Removed from keyFieldIndices: " + keyFieldIndices.remove(new Integer(fieldIndex)) + " elements");
                    keyFieldIndices.remove(new Integer(fieldIndex));

                    primaryKeyMap.clear();
                    Object keyValue;
                    Integer dublicateValueRecordIndex;
                    int i = 0;
                    while (i<=recordCount) {
                        keyValue = riskyGetCell(((Integer) keyFieldIndices.at(0)).intValue(), i);
//*                        System.out.println("Processing record #" + i + " with keyValue: " + keyValue);
//*                        System.out.println("Count: " + primaryKeyMap.count(keyValue));
                        if (primaryKeyMap.count(new Integer(keyValue.hashCode())) == 0) {
                            primaryKeyMap.add(new Integer(keyValue.hashCode()), riskyGetRecord(i));
                        }else{
                            int newPrimaryKeyFieldIndex = ((Integer) keyFieldIndices.at(0)).intValue();
                            Enumeration e = primaryKeyMap.values(new Integer(keyValue.hashCode()));
//                            if (e.hasMoreElements())
//                                e.nextElement();
                            //e contains all the records with the same keyValue as the current one
                            boolean recordRemoved = false;
                            while (e.hasMoreElements()) {
                                boolean different = false;
                                int recIndex = ((Record) e.nextElement()).getIndex();
//*                                System.out.println("Checking record #" + recIndex);
                                Object secondaryKeyValue;
                                Object secondaryKeyValue2;
                                for (int k=0; k<keyFieldIndices.size(); k++) {
                                    secondaryKeyValue = riskyGetCell(((Integer) keyFieldIndices.at(k)).intValue(), i);
                                    secondaryKeyValue2 = riskyGetCell(((Integer) keyFieldIndices.at(k)).intValue(), recIndex);
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
//*                                    System.out.println("Removing record index: " + i);
                                    try{
                                        removeRecord(i, -1, true);
                                    }catch (TableNotExpandableException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable removeFromKey() : (3)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        throw new TableNotExpandableException(bundle.getString("CTableMsg70"));
                                     }
                                     catch (InvalidRecordIndexException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable removeFromKey() : (4)");}
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
                                    primaryKeyMap.add(new Integer(keyValue.hashCode()), riskyGetRecord(i));
                                }
                            }
                        }
                        i++;
                    }//first while
                }else{ //if the key field being removed is not the primary key field
//                    System.out.println("Removed from keyFieldIndices: " + keyFieldIndices.remove(new Integer(fieldIndex)) + " elements");
                    keyFieldIndices.remove(new Integer(fieldIndex));

                    Integer keyValueHashCode;
                    Integer dublicateValueRecordIndex;
//                    OrderedMap temp = new OrderedMap(true);
                    HashMap temp = new HashMap(new EqualTo(), true, 10, (float) 1.0);
                    primaryKeyMap.swap(temp);
//                    Enumeration e1 = temp.elements();
                    HashMapIterator iter1 = temp.begin();
                    while (iter1.hasMoreElements()) {
                        keyValueHashCode = new Integer(iter1.key().hashCode());
                        Record rec = (Record) iter1.value();
//*                        System.out.println("Processing record #" + rec.getIndex() + " keyValue: " + iter1.key());
                        int i = rec.getIndex();
                        if (temp.count(keyValueHashCode) <= 1) {
                            primaryKeyMap.add(keyValueHashCode, rec);
                            temp.remove(keyValueHashCode);
                            iter1.advance();
                        }else{
                            int newPrimaryKeyFieldIndex = ((Integer) keyFieldIndices.at(0)).intValue();
                            Enumeration e = temp.values(keyValueHashCode);
                            if (e.hasMoreElements())
                                e.nextElement();
                            //e contains all the records with the same keyValue as the current one
//                            boolean recordRemoved = false;
                            while (e.hasMoreElements()) {
                                boolean different = false;
                                int recIndex = ((Record) e.nextElement()).getIndex();
//*                                System.out.println("Checking record #" + recIndex);
                                Object secondaryKeyValue;
                                Object secondaryKeyValue2;
                                for (int k=0; k<keyFieldIndices.size(); k++) {
                                    secondaryKeyValue = riskyGetCell(((Integer) keyFieldIndices.at(k)).intValue(), i);
                                    secondaryKeyValue2 = riskyGetCell(((Integer) keyFieldIndices.at(k)).intValue(), recIndex);
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
//*                                    System.out.println("Removing record index: " + recIndex);
                                    try{
                                        removeRecord(recIndex, -1, true);
                                    }catch (TableNotExpandableException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable removeFromKey() : (5)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear();
                                        temp.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        throw new TableNotExpandableException(bundle.getString("CTableMsg70"));
                                     }
                                     catch (InvalidRecordIndexException ex) {
                                        try{
                                            f.setKey(true);
                                        }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable removeFromKey() : (6)");}
                                        keyFieldIndices = keyFieldIndicesBackUp;
                                        primaryKeyMap.clear(); temp.clear();
                                        primaryKeyMap = primaryKeyMapBackUp;
                                        System.out.println("Inconsistensy error: " + ex.message);
                                     }
                                    hasKey = true;
                                }else
                                    primaryKeyMap.add(keyValueHashCode, riskyGetRecord(recIndex));
                            }//second while
                            primaryKeyMap.add(keyValueHashCode, rec);
                            temp.remove(keyValueHashCode);
                            iter1 = temp.begin();
                        }
                    }//first while
                }
            }

            /* Adjust the active record.
             */
            if (activeRecord != -1) {
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

//t            if (database != null)
                fireColumnKeyChanged(new ColumnKeyChangedEvent(this, f.getName(), false)); //database.CTables.indexOf(this), f.getName(), false));
        }

    }


    /**
     *  Decrements the indices of the some of the key fields in <i>keyFieldIndices</i> Array.
     *  @param index Every field's index which is greater than <i>index</i> will be reduced
     *         by 1.
     */
    //decrementRestKeyFieldIndices(int)
    //Decrements the field index of all the entries in the "keyFieldIndices" Array
    //after the specified index
    private void decrementRestKeyFieldIndices(int index) {
        for (int i=index; i<keyFieldIndices.size(); i++)
            keyFieldIndices.put(i, new Integer((((Integer)keyFieldIndices.at(i)).intValue() -1)));
    }


    /**
     *  Changes a field's data type. This directly affects the data of the field and may result
     *  to loss of data.
     *  @param fieldName The name of the field whose type will be changed.
     *  @param type One of <i>integer</i>, <i>double</i>, <i>string</i>, <i>boolean</i>, <i>url</i>, <i>date</i>, <i>time</i>, <i>image</i> and <i>sound</i>.
     *  @param permitDataLoss <i>true</i>, if data loss is permitted. <i>false</i>, if the operation should
     *                        fail, in case data loss occurs.
     *  @return  <i>true</i>, if the operation succeeds. <i>false</i>, if a value of the field cannot be cast to the specified <i>type</i>.
     *  @exception InvalidFieldNameException If no field with the specified name exists in the table.
     *  @exception InvalidFieldTypeException If the supplied <i>type</i> is invalid.
     *  @exception FieldIsKeyException If the specified field is part of the table's key.
     *  @exception InvalidTypeConversionException If the conversion from the field's data type to the specified <i>type</i> is not supported.
     *  @exception FieldNotEditableException If the field is not editable.
     *  @exception DataLossException If changing the data type of the field leads to data loss.
     *  @exception DependingCalcFieldsException If an attempt is made to change the data type of a field, on
     *             which calculated fields depend.
     *  @exception AttributeLockedException If the field's data type is locked.
     *  @see CTableField#isEditable()
     */
    public void changeFieldType(String fieldName, String type, boolean permitDataLoss) throws InvalidFieldNameException,
    InvalidFieldTypeException, FieldIsKeyException, InvalidTypeConversionException,
    FieldNotEditableException, DataLossException, DependingCalcFieldsException, AttributeLockedException {
        if (!dataChangeAllowed)
            throw new AttributeLockedException(bundle.getString("CTableMsg89"));

        int fieldIndex;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {
            e.printStackTrace();
            throw new InvalidFieldNameException(bundle.getString("CTableMsg65") + fieldName + bundle.getString("CTableMsg66"));
        }

        CTableField f = (CTableField) CTableFields.at(fieldIndex);
        if (!f.isFieldDataTypeChangeAllowed())
            throw new AttributeLockedException(bundle.getString("CTableFieldMsg2"));

        if (f.isCalculated())
            throw new FieldNotEditableException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg73"));

        if (f.isKey())
            throw new FieldIsKeyException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg74"));

        if (!f.isEditable())
            throw new FieldNotEditableException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg75"));

        if (f.getDependentCalcFields() != null && f.getDependentCalcFields().size() != 0)
            throw new DependingCalcFieldsException(bundle.getString("CTableMsg72") + fieldName + bundle.getString("CTableMsg76"));


        String typeNameInLowerCase = type.toLowerCase();
        Class fieldType = null;
        boolean date = false;
        try{
            if (typeNameInLowerCase.equals("string")) {      //string
                fieldType = Class.forName("java.lang.String");
//                inst = fieldType.newInstance();
            }else if (typeNameInLowerCase.equals("boolean")) {//boolean
                fieldType = Class.forName("java.lang.Boolean");
//                inst = new Boolean(true);
            }else if (typeNameInLowerCase.equals("integer")) { //integer
                fieldType = Class.forName("java.lang.Integer");
//                inst = new Integer(0);
            }else if (typeNameInLowerCase.equals("double"))   //double
                fieldType = Class.forName("java.lang.Double");
            else if (typeNameInLowerCase.equals("image"))   //image
                fieldType = Class.forName("gr.cti.eslate.database.engine.CImageIcon");
            else if (typeNameInLowerCase.equals("url"))     //url
                fieldType = Class.forName("java.net.URL");
            else if (typeNameInLowerCase.equals("date")) {    //url
                fieldType = gr.cti.eslate.database.engine.CDate.class; //Class.forName("java.util.Date");
                date = true;
            }else if (typeNameInLowerCase.equals("time")) {    //url
                fieldType = gr.cti.eslate.database.engine.CTime.class;
                date = false;
            }else
                throw new InvalidFieldTypeException(bundle.getString("CTableMsg77") + DBase.resources.getString(type));
        }catch (ClassNotFoundException e) {e.printStackTrace();}

        Class previousDataType = f.getFieldType();
        boolean wasDate = f.isDate();
//        System.out.println(f.getFieldType().getName() + " --- " + fieldType.getName());

/*t        if (f.fieldType.getName().equals(fieldType.getName()) && (!f.fieldType.getName().equals("java.util.Date"))) {
//            System.out.println(f.getFieldType().getName() + " = " + fieldType.getName());
            throw new InvalidTypeConversionException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg78"));
        }else if (f.fieldType.getName().equals(fieldType.getName()) && f.fieldType.getName().equals("java.util.Date")) {
            if (date == f.isDate())
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg78"));
        }
t*/
        if (f.fieldType.getName().equals(fieldType.getName()))
            throw new InvalidTypeConversionException(bundle.getString("CTableMsg100") + fieldName + bundle.getString("CTableMsg78"));

        Object value;
        Class[] cl = new Class[1];
        Object[] val = new Object[1];
        Constructor con;
        Array tranformedField = new Array();
        tranformedField.ensureCapacity(((Array) tableData.at(fieldIndex)).size());

//String
        if (f.getFieldType().getName().equals("java.lang.String")) {
//t            if (fieldType.getName().equals("java.util.Date")) {
//t                if (date) {
            if (fieldType.getName().equals("gr.cti.eslate.database.engine.CDate")) {
                CDate d = new CDate();
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
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
                                tranformedField.add(null);
                                continue;
                            }
                        }
                    }
                    tranformedField.add(d);
                }
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
//t                if (date)
                    f.setDate(true);
                setModified();
//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("gr.cti.eslate.database.engine.CTime")) {
                CTime d = new CTime();
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value == null)
                        d = null;
                    else{
                        try{
                            d = new CTime(timeFormat.parse((String) value, new ParsePosition(0)));
                        }catch (Exception e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else{
                                tranformedField.add(null);
                                continue;
                            }
                        }
                    }
                    tranformedField.add(d);
                }
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
//t                if (date)
                    f.setDate(false);
                setModified();
//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.Double")) {
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value == null)
                        tranformedField.add(null);
                    else{
                        try {
//                            value = new Double(numberFormat.parse((String) value).doubleValue());
                            tranformedField.add(new Double(numberFormat.parse((String) value).doubleValue()));
//                          System.out.println(tranformedField.at(i) + " --- " + tranformedField.at(i).getClass().getName());
                        }catch (DoubleNumberFormatParseException e) {
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else
                                tranformedField.add(null);
                        }
                    }
                } //for
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
                setModified();
//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else{
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value == null)
                        tranformedField.add(null);
                    else{
                        //If the "value"'s class is different from the field's class then:
                        //* get the field's and the value's Class,
                        //* get a constructor of the field's Class that takes one
                        //  parameter of the Class of the "value", if one exists,
                        //* call the constructor, with the "value" object as argument and
                        //  insert the returned value to the field.
                        cl[0] = value.getClass();
                        try {
                            con = fieldType.getConstructor(cl);
                            val[0] = value;
                            try {
                                tranformedField.add(con.newInstance(val));
//                                System.out.println(tranformedField.at(i) + " --- " + tranformedField.at(i).getClass().getName());
                            }catch (InvocationTargetException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    tranformedField.add(null);
                            }
                             catch (InstantiationException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    tranformedField.add(null);
                            }
                             catch (IllegalAccessException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    tranformedField.add(null);
                            }
                             catch (UnableToCreateImageIconException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    tranformedField.add(null);
                            }
                        }catch (NoSuchMethodException e) {
                                if (!permitDataLoss)
                                    throw new DataLossException();
                                else
                                    tranformedField.add(null);
                            }
                    }
                }//for
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
                setModified();
//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }
//Integer
        }else if (f.getFieldType().getName().equals("java.lang.Integer")) {
            if (fieldType.getName().equals("java.lang.String")) {
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value != null)
                        tranformedField.add(value.toString());
                    else
                        tranformedField.add(null);
                }
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.Double")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null) {
                        ((Array) tableData.at(fieldIndex)).put(i, new Double(((Integer) ((Array) tableData.at(fieldIndex)).at(i)).doubleValue()));
                    }else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.Boolean")) {
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value != null) {
                        if (((Integer) value).intValue() == 0)
                            tranformedField.add(new Boolean(false));
                        else
                            tranformedField.add(new Boolean(true));
                    }else
                        tranformedField.add(null);
                }
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg80") + bundle.getString(type) + "\"");
//Double
        }else if (f.getFieldType().getName().equals("java.lang.Double")) {
            if (fieldType.getName().equals("java.lang.String")) {
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value != null)
                        tranformedField.add(numberFormat.format((Double) value)); //.toString());
                    else
                        tranformedField.add(null);
                }
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(
                        this,
                        f.getName(),
                        previousDataType.getName(),
                        f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.Integer")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null) {
                        try{
                            ((Array) tableData.at(fieldIndex)).put(i, new Integer(numberFormat.format((Double) ((Array) tableData.at(fieldIndex)).at(i)))); //((Double) ((Array) tableData.at(fieldIndex)).at(i)).intValue()));
                        }catch (Exception e) {
                            /* Very big double values cannot be represented as integers.
                             */
                            if (!permitDataLoss)
                                throw new DataLossException();
                            else
                                tranformedField.add(null);
                        }
                    }else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.Boolean")) {
                for (int i=0; i<=recordCount; i++) {
                    value = riskyGetCell(fieldIndex, i);
                    if (value != null) {
                        if (((Double) value).doubleValue() == 0)
                            tranformedField.add(new Boolean(false));
                        else
                            tranformedField.add(new Boolean(true));
                    }else
                        tranformedField.add(null);
                }
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                ((Array) tableData.at(fieldIndex)).copy(tranformedField);
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg95") + bundle.getString(type) + "\"");
//Boolean
        }else if (f.getFieldType().getName().equals("java.lang.Boolean")) {
//            System.out.println(fieldType.getName().equals("java.lang.Integer") + "  " + fieldName);
            if (fieldType.getName().equals("java.lang.Integer")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null) {
                        if (((Boolean) ((Array) tableData.at(fieldIndex)).at(i)).booleanValue())
                            ((Array) tableData.at(fieldIndex)).put(i, new Integer(1));
                        else
                            ((Array) tableData.at(fieldIndex)).put(i, new Integer(0));
                    }else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.Double")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null) {
                        if (((Boolean) ((Array) tableData.at(fieldIndex)).at(i)).booleanValue())
                            ((Array) tableData.at(fieldIndex)).put(i, new Double(1));
                        else
                            ((Array) tableData.at(fieldIndex)).put(i, new Double(0));
                    }else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else if (fieldType.getName().equals("java.lang.String")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null)
                        ((Array) tableData.at(fieldIndex)).put(i, ((Boolean) ((Array) tableData.at(fieldIndex)).at(i)).toString());
                    else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg96") + bundle.getString(type) + "\"");
//URL
        }else if (f.getFieldType().getName().equals("java.net.URL")) {
            if (fieldType.getName().equals("java.lang.String")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null)
                        ((Array) tableData.at(fieldIndex)).put(i, ((java.net.URL) ((Array) tableData.at(fieldIndex)).at(i)).toString());
                    else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg97") + bundle.getString(type) + "\"");
//Date
//t        }else if (f.getFieldType().getName().equals("java.util.Date")) {
//t            if (f.isDate()) {  //Date
        }else if (f.getFieldType().getName().equals("gr.cti.eslate.database.engine.CDate")) {
            if (fieldType.getName().equals("java.lang.String")) {
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
//                    sdf2.setLenient(false);

                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null)
                        ((Array) tableData.at(fieldIndex)).put(i, dateFormat.format(((Date) ((Array) tableData.at(fieldIndex)).at(i))));
                    else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                f.setDate(false);
                setModified();

//t                    if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg94") + bundle.getString(type) + "\"");
//Time
        }else if (f.getFieldType().getName().equals("gr.cti.eslate.database.engine.CTime")) {//Time
            if (fieldType.getName().equals("java.lang.String")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null)
                        ((Array) tableData.at(fieldIndex)).put(i, timeFormat.format(((Date) ((Array) tableData.at(fieldIndex)).at(i))));
                    else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                f.setDate(false);
                setModified();

//t                    if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg94") + bundle.getString(type) + "\"");
//Image
        }else if (f.getFieldType().equals(CImageIcon.class)) {
            if (fieldType.getName().equals("java.lang.String")) {
                try{
                    f.setFieldType(fieldType);
                }catch (AttributeLockedException e) {
                    System.out.println("Serious inconsistency error in CTable _changeFieldType: (1)");
                    return;
                }
                for (int i=0; i<=recordCount; i++) {
                    if (((Array) tableData.at(fieldIndex)).at(i) != null)
                        ((Array) tableData.at(fieldIndex)).put(i, ((CImageIcon) ((Array) tableData.at(fieldIndex)).at(i)).getFileName());
                    else
                        ((Array) tableData.at(fieldIndex)).put(i, null);
                }
                setModified();

//t                if (database != null)
                    fireColumnTypeChanged(new ColumnTypeChangedEvent(this, f.getName(), previousDataType.getName(), f.getFieldType().getName())); //database.CTables.indexOf(this), f.getName(), previousDataType, wasDate, f.getFieldType(), f.isDate()));
                return;
            }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg97") + bundle.getString(type) + "\"");
//Everything else: Sound ...?
        }else
                throw new InvalidTypeConversionException(bundle.getString("CTableMsg79") + fieldName + bundle.getString("CTableMsg94") +bundle.getString(type) + "\"");
    }


    /**
     *  Returns the data of the CTable in the form of an Array of Arrays.
     */
    protected Array getTableData() {
        return tableData;
    }


    /**
     *  Returns an Array with the names of the fields of the CTable.
     */
    public Array getFieldNames() {
        Array names = new Array();
        for (int i=0; i<CTableFields.size(); i++)
            names.add(((CTableField) CTableFields.at(i)).getName());

        return names;
    }

    /**
     *  Returns the <i>title</i> of the CTable.
     */
    public String getTitle() {
        return title;
    }

    /**
     *  Sets the title of the CTable. If the supplied <i>title</i> is <i>null</i>, then
     *  the table is given a unique localized variation of the default title 'Untitled',
     *  e.g. "Untitled;1".
     *  @param title The new title.
     *  @exception InvalidTitleException Thrown, when the new non-null title, is already acquired by
     *             another table in the database.
     */
    public void setTitle(String title) throws InvalidTitleException {
/*        if (title == null && _getTitle() == null)
            return;
*/
        if (title != null && getTitle() != null && title.equals(getTitle()))
            return;

        if (title != null) {
            boolean sameTitleFound = false;
            if (database != null) {
                ArrayList databaseTablesTitles = database.getTableTitles();
                for (int i =0; i<databaseTablesTitles.size(); i++) {
                    if (((String) databaseTablesTitles.get(i)).equals(title)) {
                        sameTitleFound = true;
                        break;
                    }
                }
            }
            if (sameTitleFound)
                throw new InvalidTitleException(bundle.getString("CTableMsg84") + title + bundle.getString("CTableMsg85"));
        }

        String oldTitle = getTitle();
        this.title = title;
        setModified();

//t        if (database != null)
            fireTableRenamed(new TableRenamedEvent(this, oldTitle, title)); //database.CTables.indexOf(this), oldTitle, title));
    }


    protected void _setTitle(String title) {
        this.title = title;
    }


    /**
     *  Get the metadata for the CTable.
     */
    public String getMetadata() {
        return metadata;
    }


    /**
     *  Sets the CTable's metadata.
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
     *  Returns the field at <i>fieldIndex</i>.
     *  @param fieldIndex The index of the field.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
     */
    public CTableField getCTableField(int fieldIndex) throws InvalidFieldIndexException {
        if (fieldIndex < CTableFields.size() && fieldIndex >= 0)
            return (CTableField) CTableFields.at(fieldIndex);
        else
            throw new InvalidFieldIndexException(bundle.getString("CTableMsg51") + fieldIndex);
    }

    /**
     *  Returns the field named <i>fieldName</i>.
     *  @param fieldName The name of the field.
     *  @exception InvalidFieldNameException If no field with the specified <i>fieldName</i> exists in the table.
     */
    public CTableField getCTableField(String fieldName) throws InvalidFieldNameException {
        if (fieldName == null || fieldName.equals(""))
            throw new InvalidFieldNameException(bundle.getString("CTableMsg81") + fieldName + "\"");

        boolean fieldFound = false;
        int fieldIndex;
        for (fieldIndex=0; fieldIndex<CTableFields.size(); fieldIndex++) {
            if (((CTableField) CTableFields.at(fieldIndex)).getName().equals(fieldName)) {
               fieldFound = true;
               break;
            }
        }

        if (!fieldFound)
            throw new InvalidFieldNameException(bundle.getString("CTableMsg81") + fieldName + "\"");

        return (CTableField) CTableFields.at(fieldIndex);
    }

    /**
     *  Renames a field.
     *  @param existingName The current name of the field to be renamed.
     *  @param newName The new name.
     *  @exception InvalidFieldNameException If no field with the specified <i>fieldName</i> exists in the table.
     *  @exception FieldNameInUseException If the <i>newName</i> is already used by another field in the table.
     */
    public void renameField(String existingName, String newName)
    throws InvalidFieldNameException, FieldNameInUseException {
        CTableField f;

        try{
            f = getCTableField(existingName);
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
        Array dependentFieldIndices = f.getDependentCalcFields();
        CTableField f1 = null;
        String textFormula;
        for (int i=0; i<dependentFieldIndices.size(); i++) {
            try{
                f1 = getCTableField(((Integer) dependentFieldIndices.at(i)).intValue());
            }catch (InvalidFieldIndexException e) {System.out.println("Serious inconsistency error in CTable renameField() : (1)"); continue;}

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
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in CTable renameField: (2)");}
System.out.println("CTable renameField() getFieldNames(): " + getFieldNames());
//t        if (database != null)
            fireColumnRenamed(new ColumnRenamedEvent(this, existingName, newName)); //database.CTables.indexOf(this), existingName, newName));
    }


    /** Specifies if the field's data is editable or not.
     *  @returns <i>true</i>, if the operation succeeds.
     *  @exception InvalidFieldNameException If the supplied field name is not found in the CTable.
     *  @exception AttributeLockedException If the field's <i>Editable</i> attribute is locked.
     */
    public boolean setFieldEditable(String fieldName, boolean isEditable)
    throws InvalidFieldNameException, AttributeLockedException {
        CTableField f;
        f = getCTableField(fieldName);

        if (f.isEditable() == isEditable)
            return false;
        else{
            if (f.setEditable(isEditable)) {
                setModified();
//t                if (this.database != null)
                    fireColumnEditableStateChanged(new ColumnEditableStateChangedEvent(this, fieldName, isEditable)); //database.CTables.indexOf(this), fieldName, isEditable));
                return true;
            }else
                return false;
        }
    }


    /** Specifies if the field's data is removable or not.
     *  @exception InvalidFieldNameException If the supplied field name is not found in the CTable.
     *  @exception AttributeLockedException If the field's <i>Removable</i> attribute is locked.
     */
    public void setFieldRemovable(String fieldName, boolean isRemovable)
    throws InvalidFieldNameException, AttributeLockedException {
        CTableField f;
        f = getCTableField(fieldName);

        if (f.isRemovable() == isRemovable)
            return;
        else{
            setModified();
            f.setRemovable(isRemovable);
//t            if (this.database != null)
                fireColumnRemovableStateChanged(new ColumnRemovableStateChangedEvent(this, fieldName, isRemovable)); //database.CTables.indexOf(this), fieldName, isRemovable));
        }
    }


    /** Specifies if the field is hidden or not.
     *  @exception InvalidFieldNameException If the supplied field name is not found in the CTable.
     *  @exception AttributeLockedException If the field's <i>Hidden</i> attribute is locked.
     */
    public void setFieldHidden(String fieldName, boolean isHidden)
    throws InvalidFieldNameException, AttributeLockedException {
        CTableField f;
        f = getCTableField(fieldName);

        if (f.isHidden() == isHidden)
            return;
        else{
            setModified();
            f.setHidden(isHidden);
//t            if (this.database != null)
                fireColumnHiddenStateChanged(new ColumnHiddenStateChangedEvent(this, fieldName, isHidden)); //database.CTables.indexOf(this), fieldName, isHidden));
        }
    }


    /** Specifies if a field's data is stored outside the database. In this case the
     *  database stores references to these data and not the data itself. Currently
     *  this stands only for image fields.
     */
    public void setFieldContainsLinksToExternalData(String fieldName, boolean containsLinks)
    throws InvalidFieldNameException {
        CTableField f;
        f  = getCTableField(fieldName);

        if (!f.getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class) || f.containsLinksToExternalData() == containsLinks)
            return;
        else{
            if (containsLinks) {
                /* Check if the field contains images. If it does the warn the user that maybe
                 * images or changes to images will be lost, if the field stores references instead
                 * of the actual images.
                 */
                Array images = getField(fieldName);
                boolean containsImages = false;
                for (int i=0; i<images.size(); i++) {
                    if (images.at(i) != null) {
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
            Array fieldData = (Array) tableData.at(CTableFields.indexOf(f));
            for (int i=0; i<fieldData.size(); i++) {
                if (fieldData.at(i) != null)
                    ((ReferenceToExternalFile) fieldData.at(i)).setReferenceToExternalFile(null, null, containsLinks); //table this, f, containsLinks);
            }
        }
    }


    /**
     *  Enables or disables modification of the field's <i>Editable</i> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Field editability change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldEditabilityChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldEditabilityChangeAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isFieldEditabilityChangeAllowed() != allowed) {
            setModified();
            f.setFieldEditabilityChangeAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Field editability change allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <i>Removable</i> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Field removability change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldRemovabilityChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldRemovabilityChangeAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isFieldRemovabilityChangeAllowed() != allowed) {
            setModified();
            f.setFieldRemovabilityChangeAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Field removability change allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's data type. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Field data type change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldDataTypeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldDataTypeChangeAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isFieldDataTypeChangeAllowed() != allowed) {
            setModified();
            f.setFieldDataTypeChangeAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Field data type change allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Enables or disables the tranformation of a calculated field to a normal one. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Calculated field reset allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setCalcFieldResetAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || !f.isCalculated() || f.isCalcFieldResetAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isCalcFieldResetAllowed() != allowed) {
            setModified();
            f.setCalcFieldResetAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Calculated field reset allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <i>Key</i> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Key attribute change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldKeyAttributeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldKeyAttributeChangeAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isFieldKeyAttributeChangeAllowed() != allowed) {
            setModified();
            f.setFieldKeyAttributeChangeAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Key attribute change allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <i>Key</i> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Key attribute change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setFieldHiddenAttributeChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isFieldHiddenAttributeChangeAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isFieldHiddenAttributeChangeAllowed() != allowed) {
            setModified();
            f.setFieldHiddenAttributeChangeAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Field hidden attribute change allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Enables or disables modification of the field's <i>Key</i> attribute. Triggers a PropertyChangeEvent for the
     *  bound property <i>recordRemovalAllowed</i>. The name of the property is
     *  "Formula attribute change allowed" and the event is thrown by the  CDatabase instance
     *  which contains the CTable. The PropertyChangeEvent has been twicked, so that
     *  the <i>getOldValue()</i> method returns the an Array, which contains two elements:
     *  the first is the CTable instance and the second is the CTableField instance,
     *  whose property was changed.
     *  @param fieldName The name of the field
     *  @param allowed
     */
    public void setCalcFieldFormulaChangeAllowed(String fieldName, boolean allowed) throws InvalidFieldNameException {
        CTableField f;
        f = getCTableField(fieldName);

/*t        if (database == null || database.isLocked() || f.isCalcFieldFormulaChangeAllowed() == allowed)
            return;
        else{
t*/
        if (database != null && database.isLocked()) return;
        if (f.isCalcFieldFormulaChangeAllowed() != allowed) {
            setModified();
            f.setCalcFieldFormulaChangeAllowed(allowed);
//t            if (database != null) {
                Array identity = new Array();
                identity.add(this); identity.add(f);
//t                if (this.database != null)
                    tablePropertyChangeSupport.firePropertyChange("Formula attribute change allowed", identity, new Boolean(allowed));
//t            }
        }
    }


    /**
     *  Reports if the table contains a field named <i>fieldName</i>.
     *  @param fieldName The name of the field.
     *  @return <i>true</i>, if the field exists. <i>false</i>, otherwise.
     */
    public boolean containsField(String fieldName) {
        try{
            getField(fieldName);
        }catch (InvalidFieldNameException e) {return false;}
        return true;
    }


    /** Returns the array with the indices of the selected records of the CTable.
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

    /** Returns the array with the indices of the selected records of the CTable.
     */
/*    public IntBaseArray getSelectedSubset() {
        return (Array) selectedSubset.clone();
    }
*/
    /** Returns the number of selected records in the CTable.
     */
    public int getSelectedRecordCount() {
        return selectedSubset.size();
    }


    /**
     *  Checks if two tables have fields with identical names.
     *  @param table The second table.
     */
    protected Array equalFieldNames(CTable table) {
/*        OrderedSet fieldNames1 = new OrderedSet( new LessString() );
        OrderedSet fieldNames2 = new OrderedSet( new LessString() );
        Array commonFieldNames = new Array();

        Array temp = this.getFieldNames();
        for (int i=0; i<temp.size(); i++)
            fieldNames1.add((String) temp.at(i));
        temp = table.getFieldNames();
        for (int i=0; i<temp.size(); i++)
            fieldNames2.add((String) temp.at(i));

        OrderedSet commonFields = fieldNames1.intersection(fieldNames2);
        OrderedSetIterator iter = commonFields.begin();
        while (iter.hasMoreElements()) {
            commonFieldNames.add(iter.get());
            iter.advance();
        }

        return commonFieldNames;
*/
        return null;
    }


    /** Returns an Array with the unique, non-null values of field <i>fieldName</i>. The
     *  values are sorted in ascending order.
     *  @param fieldName The name of the field.
     *  @param selectedRecordsOnly Specifies whether all the table's records will be searched
     *         for unique values. The search will be limited to the selected records only
     *         if the value of this parameter is true.
     */
    public final Array getUniqueFieldValuesArray(String fieldName, boolean selectedRecordsOnly) throws InvalidFieldNameException {
/*        int fieldIndex = -1;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {throw e;}

        CTableField f = (CTableField) CTableFields.at(fieldIndex);

        String fieldType = f.getFieldType().getName();
        if (fieldType.equals("java.lang.Boolean")) {
            Array result = new Array();
            result.add(new Boolean(false));
            result.add(new Boolean(true));
            return result;
        }
        if (fieldType.equals("gr.cti.eslate.database.engine.CImageIcon"))
            return new Array();

        Array nonNullFieldData = new Array();
        Array fieldData = (Array) tableData.at(fieldIndex);
        nonNullFieldData.ensureCapacity(fieldData.size());

//        System.out.println("Getting rid of null...");
        if (!selectedRecordsOnly) {
            for (int i=0; i<fieldData.size(); i++) {
                if (fieldData.at(i) != null)
                    nonNullFieldData.add(fieldData.at(i));
            }
        }else{
            for (int i=0; i<fieldData.size(); i++) {
                if (isRecordSelected(i) && fieldData.at(i) != null)
                    nonNullFieldData.add(fieldData.at(i));
            }
        }

//        System.out.println("Sorting....");
        if (fieldType.equals("java.lang.Integer")) {
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
            System.out.println("Serious inconsistency error in CTable getUniqueFieldValues(): (1)");
            return null;
        }

        fieldData = new Array();
        int i=0, k = -1;
//        System.out.println("Removing dublicate values");
        while (i<nonNullFieldData.size()) {
            fieldData.add(nonNullFieldData.at(i));
            i++;
            k++;
            while (i<nonNullFieldData.size() && fieldData.at(k).equals(nonNullFieldData.at(i)))
                i++;
        }

//        System.out.println("Done.");

        return fieldData;
*/
        return null;
    }


    /** Returns an array with the unique, non-null values of field <i>fieldName</i>. The
     *  values are sorted in ascending order.
     *  @param fieldName The name of the field.
     *  @param selectedRecordsOnly Specifies whether all the table's records will be searched
     *         for unique values. The search will be limited to the selected records only
     *         if the value of this parameter is true.
     */
    public final Object[] getUniqueFieldValues(String fieldName, boolean selectedRecordsOnly) throws InvalidFieldNameException {
/*        int fieldIndex = -1;
        try{
            fieldIndex = getFieldIndex(fieldName);
        }catch (InvalidFieldNameException e) {throw e;}

        CTableField f = (CTableField) CTableFields.at(fieldIndex);

        String fieldType = f.getFieldType().getName();
        if (fieldType.equals("java.lang.Boolean")) {
            Object result[] = new Object[2];
            result[0] = new Boolean(false);
            result[1] = new Boolean(true);
            return result;
        }
        if (fieldType.equals("gr.cti.eslate.database.engine.CImageIcon"))
            return new Object[0];

        Array nonNullFieldData = new Array();
        Array fieldData = (Array) tableData.at(fieldIndex);
        nonNullFieldData.ensureCapacity(fieldData.size());

//        System.out.println("Getting rid of null...");
        if (!selectedRecordsOnly) {
            for (int i=0; i<fieldData.size(); i++) {
                if (fieldData.at(i) != null)
                    nonNullFieldData.add(fieldData.at(i));
            }
        }else{
            for (int i=0; i<fieldData.size(); i++) {
                if (isRecordSelected(i) && fieldData.at(i) != null)
                    nonNullFieldData.add(fieldData.at(i));
            }
        }

//        System.out.println("Sorting....");
        if (fieldType.equals("java.lang.Integer")) {
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
            System.out.println("Serious inconsistency error in CTable getUniqueFieldValues(): (1)");
            return null;
        }

        fieldData = new Array();
        int i=0, k = -1;
//        System.out.println("Removing dublicate values");
        while (i<nonNullFieldData.size()) {
            fieldData.add(nonNullFieldData.at(i));
            i++;
            k++;
            while (i<nonNullFieldData.size() && fieldData.at(k).equals(nonNullFieldData.at(i)))
                i++;
        }

        Object[] values = new Object[fieldData.size()]; // = new Object[k];
//        for (i=0; i<k; i++)
//            values[i] = fieldData.at(i);
        fieldData.copyTo(values);

//        System.out.println("Done.");

        return values;
*/
        return null;
    }


    /** Returns the format used for the String representation of dates.
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }


    /** Sets the format used to produce the String representation of dates to
     *  <i>dateForm</i>.
     *  @param dateForm The new date format.
     *  @return <i>true</i>, if the supplied format is supported, i.e. is contained in
     *          <i>dateFormats</i> list. <i>false</i>, if an unknown format is provided.
     */
    public boolean setDateFormat(String dateForm) {
//        System.out.println("In setDateFormat: " + dateFormats);
        if (dateFormats.indexOf(dateForm) == -1)
            return false;
//        System.out.println("In setDateFormat 2");
        dateFormat = new SimpleDateFormat(dateForm);
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
    public DoubleNumberFormat getNumberFormat() {
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
            numberFormat.setDecimalSeparatorAlwaysShown(shown);
            setModified();
        }
    }


    public boolean isDecimalSeparatorAlwaysShown() {
        return numberFormat.isDecimalSeparatorAlwaysShown();
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
     *  <i>timeForm</i>.
     *  @param timeForm The new time format.
     *  @return <i>true</i>, if the supplied format is supported, i.e. is contained in
     *          <i>timeFormats</i> list. <i>false</i>, if an unknown format is provided.
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
    public Array getDateFormats() {
        return dateFormats;
    }

    /** Returns the list of supported time formats.
     *  @see #timeFormats
     */
    public Array getTimeFormats() {
        return timeFormats;
    }


    /** Returns the <i>sampleDates</i> list.
     *  @see #sampleDates
     */
    public String[] getSampleDates() {
        return CTable.sampleDates;
    }


    /** Reports whether the table has been modified in any way.
     *  @see #isModified
     */
    public boolean isModified() {
        return isModified;
    }

    /** Sets the <i>isModified</i> flag.
     *  @see #isModified
     */
    public void setModified() {
        isModified = true;
    }


    /** Returns the <i>DBase</i>, this CTable belongs to.
     *
     */
    public DBase getDBase() {
//        System.out.println("in getDBase()");
//        System.out.println(this.database);
        return this.database;
    }


    public Array fillRecordEntryFormSTDIN() {
        Array recEntryForm = new Array();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s = new String("");

        //Examining if the table has fields
        if (CTableFields.isEmpty()) {
            System.out.println("No columns in the table. Record insertion failed!");
            return null;
        }

        //Filling the Record Entry Form from standard input
        for (int i=0; i<CTableFields.size(); i++) {
            if (((CTableField) CTableFields.at(i)).isCalculated()) continue;
            System.out.print(((CTableField) CTableFields.at(i)).getName() + ":  ");
            try {
                s = in.readLine();
            }catch(IOException e) {
                e.printStackTrace();
                return null;
            }
            if (s.equals(""))
                recEntryForm.add(null);
            else
                recEntryForm.add((Object) s);

        }
        return recEntryForm;
    }


    /**
     *  Prints the contents of the CTable.
     */
    public void printTable() {
        try {
            for (int i=0; i<=recordCount; i++)
                System.out.println(getRecord(i));
        }catch (InvalidRecordIndexException e) {System.out.println(e.message);}
    }


    protected final String nameForDataType(CTableField f) {
        String fldType = "";
        if (f.getFieldType().equals(java.lang.Integer.class))
            fldType = DBase.resources.getString("Integer");
        else if (f.getFieldType().equals(java.lang.String.class))
            fldType = DBase.resources.getString("Alphanumeric");
        else if (f.getFieldType().equals(java.lang.Double.class))
            fldType = DBase.resources.getString("Number");
        else if (f.getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = DBase.resources.getString("Image");
        else if (f.getFieldType().equals(java.net.URL.class))
            fldType = DBase.resources.getString("URL");
        else if (f.getFieldType().equals(java.lang.Boolean.class))
            fldType = DBase.resources.getString("Boolean");
//t        else if (f.getFieldType().equals(java.util.Date.class)) {
//t            if (f.isDate())
        else if (f.getFieldType().equals(CDate.class))
            fldType = DBase.resources.getString("Date");
        else if (f.getFieldType().equals(CTime.class))
            fldType = DBase.resources.getString("Time");

        return fldType;
    }


    protected final String nameForDataTypeInAitiatiki(CTableField f) {
        String fldType = "";
        if (f.getFieldType().equals(java.lang.Integer.class))
            fldType = DBase.resources.getString("IntegerAit");
        else if (f.getFieldType().equals(java.lang.String.class))
            fldType = DBase.resources.getString("AlphanumericAit");
        else if (f.getFieldType().equals(java.lang.Double.class))
            fldType = DBase.resources.getString("NumberAit");
        else if (f.getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = DBase.resources.getString("Image");
        else if (f.getFieldType().equals(java.net.URL.class))
            fldType = DBase.resources.getString("URL");
        else if (f.getFieldType().equals(java.lang.Boolean.class))
            fldType = DBase.resources.getString("Boolean");
//t        else if (f.getFieldType().equals(java.util.Date.class)) {
//t            if (f.isDate())
        else if (f.getFieldType().equals(CDate.class))
            fldType = DBase.resources.getString("Date");
        else if (f.getFieldType().equals(CTime.class))
            fldType = DBase.resources.getString("Time");

        return fldType;
    }

    public void printPrimaryKeyMap() {
        if (primaryKeyMap != null) {
            Enumeration e = primaryKeyMap.elements();
            HashMapIterator iter = (HashMapIterator) e;
            while (!iter.atEnd()) {
                System.out.println(iter.key() + ",  " + ((Record) iter.value()).getIndex());
                iter.advance();
            }
        }else
            System.out.println("primaryKeyMap = null");

    }


    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        writeObject((ObjectOutputStream) out);
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
//            out.defaultWriteObject();
        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 33);
        fieldMap.put("Table data", tableData);
        fieldMap.put("Table fields", CTableFields);
        fieldMap.put("Table records", CTableRecords);
        fieldMap.put("Record addition allowed", recordAdditionAllowed);
        fieldMap.put("Primary key map", primaryKeyMap);
        fieldMap.put("Key field indices", keyFieldIndices);
        fieldMap.put("Key exists", hasKey);
        fieldMap.put("Record count", recordCount);
        fieldMap.put("Title", title);
        fieldMap.put("Metadata", metadata);
        fieldMap.put("Selected subset", selectedSubset);
        fieldMap.put("Unselected subset", unselectedSubset);
        fieldMap.put("Date format 1", sdf.toPattern());
        fieldMap.put("Date format 2", dateFormat.toPattern());
        fieldMap.put("Time format 1", stf.toPattern());
        fieldMap.put("Time format 2", timeFormat.toPattern());
        fieldMap.put("Number format", numberFormat);
        fieldMap.put("Calculated field count", numOfCalculatedFields);
        fieldMap.put("Calculated fields modified", calculatedFieldsChanged);
        fieldMap.put("Calculated field cells modified", changedCalcCells);
        fieldMap.put("New record pending", pendingNewRecord);
        fieldMap.put("UIProperties", UIProperties);
        fieldMap.put("isHidden", isHidden);
        fieldMap.put("Active record", activeRecord);
        fieldMap.put("Record removal allowed", recordRemovalAllowed);
        fieldMap.put("Field addition allowed", fieldAdditionAllowed);
        fieldMap.put("Field removal allowed", fieldRemovalAllowed);
        fieldMap.put("Field reordering allowed", fieldReorderingAllowed);
        fieldMap.put("Key change allowed", keyChangeAllowed);
        fieldMap.put("Data editable", dataChangeAllowed);
        fieldMap.put("Display hidden fields", hiddenFieldsDisplayed);
        fieldMap.put("Hidden state editable", hiddenAttributeChangeAllowed);
        fieldMap.put("Record index", recordIndex);

        out.writeObject(fieldMap);

/*        out.writeObject(tableData);
        out.writeObject(CTableFields);
        out.writeObject(CTableRecords);
        out.writeObject(new Boolean(recordAdditionAllowed));
        out.writeObject(primaryKeyMap);
        out.writeObject(keyFieldIndices);
        out.writeObject(new Boolean(hasKey));
        out.writeObject(new Integer(recordCount));
        out.writeObject(title);
        out.writeObject(metadata);
        out.writeObject(selectedSubset);
        out.writeObject(unselectedSubset);
//        out.writeObject(file);
        out.writeObject(sdf.toPattern());
        out.writeObject(dateFormat.toPattern());
        out.writeObject(stf.toPattern());
        out.writeObject(timeFormat.toPattern());
        out.writeObject(numberFormat);
        out.writeObject(new Integer(numOfCalculatedFields));
        out.writeObject(new Boolean(calculatedFieldsChanged));
        out.writeObject(changedCalcCells);
        out.writeObject(new Boolean(pendingNewRecord));
        out.writeObject(UIProperties);
        out.writeObject(new Boolean(isHidden));
        out.writeObject(new Integer(activeRecord));
        out.writeObject(new Boolean(recordRemovalAllowed));
        out.writeObject(new Boolean(fieldAdditionAllowed));
        out.writeObject(new Boolean(fieldRemovalAllowed));
        out.writeObject(new Boolean(fieldReorderingAllowed));
        out.writeObject(new Boolean(keyChangeAllowed));
        out.writeObject(new Boolean(dataChangeAllowed));
        out.writeObject(new Boolean(hiddenFieldsDisplayed));
        out.writeObject(new Boolean(hiddenAttributeChangeAllowed));
        out.writeObject(recordIndex);
*/
    }


    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("CTable readExternal()");
        readObject((ObjectInputStream) in);
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!ESlateFieldMap.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
            String dataVersion = fieldMap.getDataVersion();

            tableData = (Array) fieldMap.get("Table data");
            CTableFields = (Array) fieldMap.get("Table fields");
            for (int i=0; i<CTableFields.size(); i++) {
                CTableField field = (CTableField) CTableFields.at(i);
                if (field.oe != null) {
                    field.oe.table = null; //table this;
                }
            }
            CTableRecords = (Array) fieldMap.get("Table records");
            for (int i=0; i<CTableRecords.size(); i++)
                ((Record) CTableRecords.at(i)).table = null; //tables this;
            recordAdditionAllowed = fieldMap.get("Record addition allowed", true);
            primaryKeyMap = (HashMap) fieldMap.get("Primary key map");
            keyFieldIndices = (Array) fieldMap.get("Key field indices");
            hasKey = fieldMap.get("Key exists", false);
            recordCount = fieldMap.get("Record count", 0);
            title = fieldMap.get("Title", "");
            metadata = fieldMap.get("Metadata", (String) null);
            if (dataVersion.equals("1.0")) {
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
            try{
                Object o = (Object) fieldMap.get("Date format 1");
                if (o instanceof SimpleDateFormat)
                    sdf = (SimpleDateFormat) o;
                else
                    sdf = new SimpleDateFormat((String) o);
            }catch (Throwable e) {
                sdf = new SimpleDateFormat(); //"dd/MM/yyyy");
            }
            try{
                Object o = (Object) fieldMap.get("Date format 2");
                if (o instanceof SimpleDateFormat)
                    dateFormat = (SimpleDateFormat) o;
                else
                    dateFormat = new SimpleDateFormat((String) o);
            }catch (Throwable e) {
                dateFormat = new SimpleDateFormat(); //"dd/MM/yyyy");
            }
            try{
                Object o = (Object) fieldMap.get("Time format 1");
                if (o instanceof SimpleDateFormat)
                    stf = (SimpleDateFormat) o;
                else
                    stf = new SimpleDateFormat((String) o);
            }catch (Exception e) {
                stf = new SimpleDateFormat(); //"hh:mm");
            }
            try{
                Object o = (Object) fieldMap.get("Time format 2");
                if (o instanceof SimpleDateFormat)
                    timeFormat = (SimpleDateFormat) o;
                else
                    timeFormat = new SimpleDateFormat((String) o);
            }catch (Exception e) {
                timeFormat = new SimpleDateFormat(); //"hh:mm");
            }
            numberFormat = (DoubleNumberFormat) fieldMap.get("Number format");
            numOfCalculatedFields = fieldMap.get("Calculated field count", 0);
            calculatedFieldsChanged = fieldMap.get("Calculated fields modified", false);
            changedCalcCells = (HashMap) fieldMap.get("Calculated field cells modified");
            pendingNewRecord = fieldMap.get("New record pending", false);
            UIProperties = (Array) fieldMap.get("UIProperties");
            isHidden = fieldMap.get("isHidden", false);
            activeRecord = fieldMap.get("Active record", -1);
            recordRemovalAllowed = fieldMap.get("Record removal allowed", true);
            fieldAdditionAllowed = fieldMap.get("Field addition allowed", true);
            fieldRemovalAllowed = fieldMap.get("Field removal allowed", true);
            fieldReorderingAllowed = fieldMap.get("Field reordering allowed", true);
            keyChangeAllowed = fieldMap.get("Key change allowed", true);
            dataChangeAllowed = fieldMap.get("Data editable", true);
            hiddenFieldsDisplayed = fieldMap.get("Display hidden fields", false);
            hiddenAttributeChangeAllowed = fieldMap.get("Hidden state editable", true);
            hiddenAttributeChangeAllowed = fieldMap.get("Hidden state editable", true);
            recordIndex = (int[]) fieldMap.get("Record index", (Object) null);
            if (recordIndex == null) {
                recordIndex = new int[recordCount+1];
                for (int i=0; i<recordIndex.length; i++)
                    recordIndex[i] = i;
            }
        }

        /* Initializing Arrays with alternative date and time formats.
         */
        if (CTable.dateFormats == null) {
//            System.out.println("1Creating date formats");
            dateFormats = new Array();
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
        if (CTable.sampleDates == null) {
            Date today = new Date(System.currentTimeMillis());
            String prefix = "   (";
            String suffix = ")";
            sampleDates = new String[dateFormats.size()];
//            System.out.println("Creating date formats");
            sampleDates[0] = prefix + (new SimpleDateFormat((String) dateFormats.at(0))).format(today) + suffix;
            sampleDates[1] = prefix + (new SimpleDateFormat((String) dateFormats.at(1))).format(today) + suffix;
            sampleDates[2] = prefix + (new SimpleDateFormat((String) dateFormats.at(2))).format(today) + suffix;
            sampleDates[3] = prefix + (new SimpleDateFormat((String) dateFormats.at(3))).format(today) + suffix;
            sampleDates[4] = prefix + (new SimpleDateFormat((String) dateFormats.at(4))).format(today) + suffix;
            sampleDates[5] = prefix + (new SimpleDateFormat((String) dateFormats.at(5))).format(today) + suffix;
            sampleDates[6] = prefix + (new SimpleDateFormat((String) dateFormats.at(6))).format(today) + suffix;
            sampleDates[7] = prefix + (new SimpleDateFormat((String) dateFormats.at(7))).format(today) + suffix;
            sampleDates[8] = prefix + (new SimpleDateFormat((String) dateFormats.at(8))).format(today) + suffix;
            sampleDates[9] = prefix + (new SimpleDateFormat((String) dateFormats.at(9))).format(today) + suffix;
            sampleDates[10] = prefix + (new SimpleDateFormat((String) dateFormats.at(10))).format(today) + suffix;
            sampleDates[11] = prefix + (new SimpleDateFormat((String) dateFormats.at(11))).format(today) + suffix;
            sampleDates[12] = prefix + (new SimpleDateFormat((String) dateFormats.at(12))).format(today) + suffix;
//            System.out.println("Created date formats");
        }
        if (CTable.timeFormats == null) {
            timeFormats = new Array();
            timeFormats.ensureCapacity(3);
            timeFormats.add("H:mm");
            timeFormats.add("hh:mm");
            timeFormats.add("hh:mm aaa");
        }

        recordSelection = new BoolBaseArray();
        if (recordCount > 0)
            recordSelection.setSize(recordCount); //ensureCapacity(recordCount);
        for (int i=0; i<=recordCount; i++)
            recordSelection.add(false); //Boolean.FALSE);
        for (int i=0; i<selectedSubset.size(); i++)
            recordSelection.set(selectedSubset.get(i), true); //set(((Integer) selectedSubset.at(i)).intValue(), true);//Boolean.TRUE);

        databaseTableModelListeners = new ArrayList();
        tableModelListeners = new ArrayList();
        tablePropertyChangeSupport = new PropertyChangeSupport(this);
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("CTable oldReadObject");
        try{
//            in.defaultReadObject();
            tableData = (Array) firstObj; //in.readObject();
//            System.out.println("Reading table... 1");
            CTableFields = (Array) in.readObject();
//            System.out.println("Reading table... 2");
            CTableRecords = (Array) in.readObject();
//            System.out.println("Reading table... 3");
            recordAdditionAllowed = ((Boolean) in.readObject()).booleanValue();
//            System.out.println("Reading table... 4");
            primaryKeyMap = (HashMap) in.readObject();
            keyFieldIndices = (Array) in.readObject();
            hasKey = ((Boolean) in.readObject()).booleanValue();
            recordCount = ((Integer) in.readObject()).intValue();
            title = (String) in.readObject();
//            System.out.println("Reading table.... " + title);
            metadata = (String) in.readObject();

            /* Convert the stored JGL Array to IntBaseArrayDesc */
            Array selectedSubsetArray = (Array) in.readObject();
            selectedSubset = new IntBaseArrayDesc(selectedSubsetArray.size());
            for (int i=0; i<selectedSubsetArray.size(); i++)
                selectedSubset.add(((Integer) selectedSubsetArray.at(i)).intValue());
//            System.out.println("selectedSubset: " + selectedSubset);

            /* Convert the stored JGL Array to IntBaseArrayDesc */
            Array unselectedSubsetArray = (Array) in.readObject();
            unselectedSubset = new IntBaseArrayDesc(unselectedSubsetArray.size());
            for (int i=0; i<unselectedSubsetArray.size(); i++)
                unselectedSubset.add(((Integer) unselectedSubsetArray.at(i)).intValue());

//            file = (File) in.readObject();
//            System.out.println("Reading sdf");
            try{
                Object o = (Object) in.readObject();
                if (o instanceof SimpleDateFormat)
                    sdf = (SimpleDateFormat) o;
                else
                    sdf = new SimpleDateFormat((String) o);
            }catch (Throwable e) {
//                System.out.println(e.getMessage() + ", " + e.getClass().getName());
//                System.out.println("Initializing sdf");
                sdf = new SimpleDateFormat(); //"dd/MM/yyyy");
//                System.out.println("Initialized sdf");
            }
//            System.out.println("Reading dateFormat");
            try{
//                dateFormat = (SimpleDateFormat) in.readObject();
                Object o = (Object) in.readObject();
                if (o instanceof SimpleDateFormat)
                    dateFormat = (SimpleDateFormat) o;
                else
                    dateFormat = new SimpleDateFormat((String) o);
            }catch (Exception e) {
//                System.out.println("Initializing dateFormat");
                dateFormat = new SimpleDateFormat(); //"dd/MM/yyyy");
//                System.out.println("Initialized dateFormat");
            }
//            System.out.println("Reading stf");
            try{
//                stf = (SimpleDateFormat) in.readObject();
                Object o = (Object) in.readObject();
                if (o instanceof SimpleDateFormat)
                    stf = (SimpleDateFormat) o;
                else
                    stf = new SimpleDateFormat((String) o);
            }catch (Exception e) {
//                System.out.println("Initializing stf");
                stf = new SimpleDateFormat(); //"hh:mm");
            }
//            System.out.println("Reading timeFormat");
            try{
//                timeFormat = (SimpleDateFormat) in.readObject();
                Object o = (Object) in.readObject();
                if (o instanceof SimpleDateFormat)
                    timeFormat = (SimpleDateFormat) o;
                else
                    timeFormat = new SimpleDateFormat((String) o);
            }catch (Exception e) {
//                System.out.println("Initializing timeFormat");
                timeFormat = new SimpleDateFormat(); //"hh:mm");
            }
            numberFormat = (DoubleNumberFormat) in.readObject();
            numOfCalculatedFields = ((Integer) in.readObject()).intValue();
            calculatedFieldsChanged = ((Boolean) in.readObject()).booleanValue();
            changedCalcCells = (HashMap) in.readObject();
            pendingNewRecord = ((Boolean) in.readObject()).booleanValue();
            UIProperties = (Array) in.readObject();
//            System.out.println("Reading table 5...  " + title);
            try{
                isHidden = ((Boolean) in.readObject()).booleanValue();
            }catch (Exception e) {
                isHidden = false;
            }
            try{
                activeRecord = ((Integer) in.readObject()).intValue();
            }catch (Exception e) {
                activeRecord = -1;
            }
            try{
                recordRemovalAllowed = ((Boolean) in.readObject()).booleanValue();
                fieldAdditionAllowed = ((Boolean) in.readObject()).booleanValue();
                fieldRemovalAllowed = ((Boolean) in.readObject()).booleanValue();
                fieldReorderingAllowed = ((Boolean) in.readObject()).booleanValue();
                keyChangeAllowed = ((Boolean) in.readObject()).booleanValue();
                dataChangeAllowed = ((Boolean) in.readObject()).booleanValue();
                hiddenFieldsDisplayed = ((Boolean) in.readObject()).booleanValue();
                hiddenAttributeChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            }catch (Exception e) {
                recordRemovalAllowed = true;
                fieldAdditionAllowed = true;
                fieldRemovalAllowed = true;
                fieldReorderingAllowed = true;
                keyChangeAllowed = true;
                dataChangeAllowed = true;
                hiddenFieldsDisplayed = false;
                hiddenAttributeChangeAllowed = true;
            }
//            System.out.println("Reading table 6...  " + title);
        }catch (ClassNotFoundException e) {throw new IOException(e.getMessage());}

        /* Read-in the recordIndex array
         */
        try{
            recordIndex = (int[]) in.readObject();
        }catch (Exception exc) {
            /* Initialize the recordIndex.
             */
            recordIndex = new int[recordCount+1];
            for (int i=0; i<recordIndex.length; i++) {
                recordIndex[i] = i;
            }
        }

        /* This is a temporary patch. DBEngine vers 1.2 had the folowing bug:
         * When a selected record was removed from a CTable, the selectedSubset
         * was not updated too. This resulted in the creation and storage of
         * tables which have no records, but their selected subset has elements.
         * This causes exceptions. Therefore I introduce the following partial patch.
         */
        if (recordCount == -1 || selectedSubset.size() > (recordCount+1)) {
            for (int i=0; i<selectedSubset.size(); i++)
                unselectedSubset.add(selectedSubset.get(i));
            selectedSubset.clear();
        }

        /* Initializing Arrays with alternative date and time formats.
         */
/*        if (CTable.dateFormats == null) {
//            System.out.println("1Creating date formats");
            dateFormats = new Array();
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
        if (CTable.sampleDates == null) {
            Date today = new Date(System.currentTimeMillis());
            String prefix = "   (";
            String suffix = ")";
            sampleDates = new String[dateFormats.size()];
//            System.out.println("Creating date formats");
            sampleDates[0] = prefix + (new SimpleDateFormat((String) dateFormats.at(0))).format(today) + suffix;
            sampleDates[1] = prefix + (new SimpleDateFormat((String) dateFormats.at(1))).format(today) + suffix;
            sampleDates[2] = prefix + (new SimpleDateFormat((String) dateFormats.at(2))).format(today) + suffix;
            sampleDates[3] = prefix + (new SimpleDateFormat((String) dateFormats.at(3))).format(today) + suffix;
            sampleDates[4] = prefix + (new SimpleDateFormat((String) dateFormats.at(4))).format(today) + suffix;
            sampleDates[5] = prefix + (new SimpleDateFormat((String) dateFormats.at(5))).format(today) + suffix;
            sampleDates[6] = prefix + (new SimpleDateFormat((String) dateFormats.at(6))).format(today) + suffix;
            sampleDates[7] = prefix + (new SimpleDateFormat((String) dateFormats.at(7))).format(today) + suffix;
            sampleDates[8] = prefix + (new SimpleDateFormat((String) dateFormats.at(8))).format(today) + suffix;
            sampleDates[9] = prefix + (new SimpleDateFormat((String) dateFormats.at(9))).format(today) + suffix;
            sampleDates[10] = prefix + (new SimpleDateFormat((String) dateFormats.at(10))).format(today) + suffix;
            sampleDates[11] = prefix + (new SimpleDateFormat((String) dateFormats.at(11))).format(today) + suffix;
            sampleDates[12] = prefix + (new SimpleDateFormat((String) dateFormats.at(12))).format(today) + suffix;
//            System.out.println("Created date formats");
        }
        if (CTable.timeFormats == null) {
            timeFormats = new Array();
            timeFormats.ensureCapacity(3);
            timeFormats.add("H:mm");
            timeFormats.add("hh:mm");
            timeFormats.add("hh:mm aaa");
        }

        recordSelection = new Array();
        if (recordCount > 0)
            recordSelection.ensureCapacity(recordCount);
        for (int i=0; i<=recordCount; i++)
            recordSelection.add(Boolean.FALSE);
        for (int i=0; i<selectedSubset.size(); i++)
            recordSelection.put(((Integer) selectedSubset.at(i)).intValue(), Boolean.TRUE);
*/
//        System.out.println("Reading table... " + title + " finished");
    }


    /** Returns <i>true</i> if the table is hidden, <i>false</i> otherwise.
     */
    public boolean isHidden() {
        return isHidden;
    }


    /** Sets the <i>isHidden</i> flag.
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
                fireTableHiddenStateChanged(new TableHiddenStateChangedEvent(this, isHidden)); //tableIndex, visibleIndex, isHidden));
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
            fireActiveRecordChanged(new ActiveRecordChangedEvent(this, previousActiveRecord, activeRecord)); //database.CTables.indexOf(this), previousActiveRecord, activeRecord));
        return;
    }


    public int[] findFirst(Array fieldNames, int[] recordIndices, String findWhat, boolean caseSensitive) throws InvalidFieldNameException {
        int[] nothingFound = {-1, -1};
        if (fieldNames.size() == 0 || recordIndices.length == 0)
            return nothingFound;

        CTableField fld;
        for (int i=0; i<fieldNames.size(); i++) {
            fld = getCTableField((String) fieldNames.at(i));
            if (fld.getFieldType().getName().equals("gr.cti.eslate.database.engine.CImageIcon") || (fld.isHidden() && !hiddenFieldsDisplayed)) {
                fieldNames.remove(i);
                i--;
            }
        }

//        System.out.println("fieldNames: " + fieldNames);

        int[] fieldIndices = new int[fieldNames.size()];

        for (int i=0; i<fieldNames.size(); i++)
            fieldIndices[i] = getFieldIndex((String) fieldNames.at(i));

        /* Create the EqualFormattedString predicate
         */
        Array anyCharIndex = new Array();
        Array anyString = new Array();
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
                    while ((index = ((String) anyString.at(i)).indexOf('^', index)) != -1) {
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

//        System.out.println("anyCharIndex: " + anyCharIndex + ", anyString: " + anyString);
        EqualFormattedString efs = new EqualFormattedString(Table.convertArrayToArrayList(anyCharIndex), Table.convertArrayToArrayList(anyString));

        /* Search on the specified field and records
         */
        String str;
        int recIndex, fieldIndex;
        Object o;
        for (int i=0; i<recordIndices.length; i++) {
            recIndex = recordIndices[i];
            for (int k = 0; k<fieldIndices.length; k++) {
                fieldIndex = fieldIndices[k];
                o = riskyGetCell(fieldIndex, recIndex);
                if (o == null)
                    continue;
                else{
                    fld = riskyGetField(fieldIndex);
                    if (fld.getFieldType().getName().equals("java.lang.Double"))
                        str = getNumberFormat().format((Double) o);
                    else
                        str = o.toString();
                }
                if (caseSensitive)
                    str = str.toLowerCase();
//                System.out.println("str: " + str + ", findWhat: " + findWhat + ", efs: " + efs.execute(str, findWhat));
                if (efs.execute(str, findWhat)) {
                    int[] result = {recIndex, fieldIndex};
                    return result;
                }
            }
        }

        return nothingFound;
    }


    public int[] findNext(Array fieldNames, int[] recordIndices, String findWhat, boolean caseSensitive, int startFromFieldIndex, int startFromRecordIndex, boolean downwards) throws InvalidFieldNameException {
        int[] nothingFound = {-1, -1};
        if (fieldNames.size() == 0 || recordIndices.length == 0)
            return nothingFound;

        CTableField fld;
        for (int i=0; i<fieldNames.size(); i++) {
            fld = getCTableField((String) fieldNames.at(i));
            if (fld.getFieldType().getName().equals("gr.cti.eslate.database.engine.CImageIcon") || (fld.isHidden() && !hiddenFieldsDisplayed)) {
                if (!downwards && startFromFieldIndex >= i)
                    startFromFieldIndex--;
                fieldNames.remove(i);
                i--;
            }
        }

//        System.out.println("fieldNames: " + fieldNames);

        int[] fieldIndices = new int[fieldNames.size()];

        for (int i=0; i<fieldNames.size(); i++)
            fieldIndices[i] = getFieldIndex((String) fieldNames.at(i));

        /* Create the EqualFormattedString predicate
         */
        Array anyCharIndex = new Array();
        Array anyString = new Array();
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
                    while ((index = ((String) anyString.at(i)).indexOf('^', index)) != -1) {
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

        EqualFormattedString efs = new EqualFormattedString(Table.convertArrayToArrayList(anyCharIndex), Table.convertArrayToArrayList(anyString));

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
                        fld = riskyGetField(fieldIndex);
                        if (fld.getFieldType().equals(Double.class))
                            str = getNumberFormat().format((Double) o);
                        else
                            str = o.toString();
                    }
                    if (caseSensitive)
                        str = str.toLowerCase();
//                    System.out.println("str: " + str + ", findWhat: " + findWhat);
                    if (efs.execute(str, findWhat)) {
                        int[] result = {recordIndices[startFromRecordIndex], fieldIndex};
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
                        fld = riskyGetField(fieldIndex);
                        if (fld.getFieldType().equals(Double.class))
                            str = getNumberFormat().format((Double) o);
                        else
                            str = o.toString();
                    }
                    if (caseSensitive)
                        str = str.toLowerCase();
//                    System.out.println("str: " + str + ", findWhat: " + findWhat);
                    if (efs.execute(str, findWhat)) {
                        int[] result = {recordIndices[startFromRecordIndex], fieldIndex};
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
                        fld = riskyGetField(fieldIndex);
                        if (fld.getFieldType().equals(Double.class))
                            str = getNumberFormat().format((Double) o);
                        else
                            str = o.toString();
                    }
                    if (caseSensitive)
                        str = str.toLowerCase();
//                    System.out.println("str: " + str + ", findWhat: " + findWhat);
                    if (efs.execute(str, findWhat)) {
                        int[] result = {recIndex, fieldIndex};
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
                        fld = riskyGetField(fieldIndex);
                        if (fld.getFieldType().equals(Double.class))
                            str = getNumberFormat().format((Double) o);
                        else
                            str = o.toString();
                    }
                    if (caseSensitive)
                        str = str.toLowerCase();
//                   System.out.println("str: " + str + ", findWhat: " + findWhat);
                    if (efs.execute(str, findWhat)) {
                        int[] result = {recIndex, fieldIndex};
                        return result;
                    }
                }
            }
        }

        return nothingFound;
    }

    public boolean hasKey() {
        return hasKey;
    }

    /** Initializes a new CTable instance.
     */
    protected void initCTable() {
        //Initializing the records' count
        recordCount = -1;
        primaryKeyMap = null;
        hasKey = false;
        numOfCalculatedFields = 0;
        recordAdditionAllowed = true;
        recordRemovalAllowed = true;
        fieldAdditionAllowed = true;
        fieldRemovalAllowed = true;
        fieldReorderingAllowed = true;
        keyChangeAllowed = true;
        dataChangeAllowed = true;
        hiddenFieldsDisplayed = false;
        hiddenAttributeChangeAllowed = true;
//        file = null;
        database = null;
        //Initializing properly Time and Date entry formats
        sdf.setLenient(false);
        stf.setLenient(false);
        stf.getTimeZone().setRawOffset(0);
//        System.out.println("Creating dateformat");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        timeFormat = new SimpleDateFormat("H:mm");
        timeFormat.setLenient(false);
        CDate.dateFormat = dateFormat;
        CTime.timeFormat = timeFormat;
//        System.out.println("Created dateformat");
        numberFormat = new DoubleNumberFormat();
//        System.out.println("MAx: " + numberFormat.getMaximumIntegerDigits() + ", " + numberFormat.getMaximumFractionDigits());
//        numberFormat.setMaximumIntegerDigits(40);
//        numberFormat.setMaximumFractionDigits(20);
//        numberFormat.setMinimumFractionDigits(1);
        calculatedFieldsChanged = false;
        changedCalcCells = new HashMap();
        pendingNewRecord = false;

        /* Initializing Arrays with alternative date and time formats.
         */
        if (dateFormats == null) {
            dateFormats = new Array();
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
        if (sampleDates == null) {
            Date today = new Date(System.currentTimeMillis());
            String prefix = "   (";
            String suffix = ")";
            sampleDates = new String[dateFormats.size()];
//            System.out.println("Creating sample dates");
            sampleDates[0] = prefix + (new SimpleDateFormat((String) dateFormats.at(0))).format(today) + suffix;
//            sampleDates[0] = "   (18/02/1998)";
            sampleDates[1] = prefix + (new SimpleDateFormat((String) dateFormats.at(1))).format(today) + suffix;
//            sampleDates[1] = "   (18-02-1998)";
            sampleDates[2] = prefix + (new SimpleDateFormat((String) dateFormats.at(2))).format(today) + suffix;
//            sampleDates[2] = "   (02/18/1998)";
            sampleDates[3] = prefix + (new SimpleDateFormat((String) dateFormats.at(3))).format(today) + suffix;
//            sampleDates[3] = "   (02-18-1998)";
            sampleDates[4] = prefix + (new SimpleDateFormat((String) dateFormats.at(4))).format(today) + suffix;
//            sampleDates[4] = "   (18/02/98)";
            sampleDates[5] = prefix + (new SimpleDateFormat((String) dateFormats.at(5))).format(today) + suffix;
//            sampleDates[5] = "   (18-02-98)";
            sampleDates[6] = prefix + (new SimpleDateFormat((String) dateFormats.at(6))).format(today) + suffix;
//            sampleDates[6] = "   (Feb 02, '98)";
            sampleDates[7] = prefix + (new SimpleDateFormat((String) dateFormats.at(7))).format(today) + suffix;
//            sampleDates[7] = "   (Wed, Feb 02, ''98)";
            sampleDates[8] = prefix + (new SimpleDateFormat((String) dateFormats.at(8))).format(today) + suffix;
//            sampleDates[8] = "   (18/02/1998 AD)";
            sampleDates[9] = prefix + (new SimpleDateFormat((String) dateFormats.at(9))).format(today) + suffix;
//            sampleDates[9] = "   (18-02-1998 AD)";
            sampleDates[10] = prefix + (new SimpleDateFormat((String) dateFormats.at(10))).format(today) + suffix;
//            sampleDates[10] = "   (02/18/1998 AD)";
            sampleDates[11] = prefix + (new SimpleDateFormat((String) dateFormats.at(11))).format(today) + suffix;
//            sampleDates[11] = "   (02-18-1998 AD)";
            sampleDates[12] = prefix + (new SimpleDateFormat((String) dateFormats.at(12))).format(today) + suffix;
//            sampleDates[12] = "   (1998 AD)";
        }
        if (timeFormats == null) {
            timeFormats = new Array();
            timeFormats.ensureCapacity(3);
            timeFormats.add("H:mm");
            timeFormats.add("hh:mm");
            timeFormats.add("hh:mm aaa");
        }
//        System.out.println("Created sample dates");

        for (int i=0; i<=recordCount; i++)
            recordSelection.add(false); //Boolean.FALSE);
    }


    /**
     *  Instantiates a new CTable.
     */
    public CTable() {
        initCTable();
        setModified();
    }


    /**
     *  Instantiates a new CTable with the specified title.
     */
    public CTable(String title) {
        initCTable();
        this.title = title;
        setModified();
    }

    /* Called whenever the order of the records in the CTable changes.
     */
    public void rowOrderChanged() {
        fireRowOrderChanged(new RowOrderChangedEvent(this));
    }


    ArrayList convertArrayToArrayList(Array array) {
        int size = array.size();
        ArrayList arrList = new ArrayList(size);
        for (int i=0; i<size; i++)
            arrList.add(array.at(i));
        return arrList;
    }

    public byte getTypeForClass(Class cls) {
        if (Double.class.isAssignableFrom(cls))
            return ITableModel.NUMERIC_TYPE;
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
            CTableField f = getCTableField(colIndex);
            return f.fieldType;
        }catch (InvalidFieldIndexException exc) {
            return null;
        }
    }

    /**
     *  Returns the number of records in the CTable. Method alias for <i>getRecordCount()</i>.
     */
    public int getRowCount() {
        return getRecordCount();
    }

    /**
     *  Returns the number of fields in the CTable. Method alias for <i>getFieldCount()</i>.
     */
    public int getColumnCount() {
        return getFieldCount();
    }

    /**
     *  Returns the name of the column with index <i>colIndex</i>. Method alias for
     *  <i>getFieldName</i>.
     *  @param fieldIndex The index of the field.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
     */
    public String getColumnName(int colIndex) {
        try{
            return getFieldName(colIndex);
        }catch (InvalidFieldIndexException exc) {
            return null;
        }
    }

    /**
     *  Sets the name of the column with index <i>colIndex</i> to <i>colName</i>.
     *  @param colIndex The index of the column.
     *  @param colName The new name of the column.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
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
     *  Returns the data of the column named <i>colName</i>. Method alias for
     *  <i>getField()</i>.
     *  @param colName The name of the column.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
     */
    public ArrayList getColumn(String colName) {
        try{
            return convertArrayToArrayList(getField(colName));
        }catch (InvalidFieldNameException exc) {return null;}
    }

    /**
     *  Returns the data of the column at index <i>colIndex</i>. Method alias for
     *  <i>getField()</i>.
     *  @param colIndex The index of the column.
     *  @exception InvalidFieldIndexException If <i>fieldIndex<0</i> or <i>fieldIndex>#Fields</i>.
     */
    public ArrayList getColumn(int colIndex) {
        try{
            return convertArrayToArrayList(getField(colIndex));
        }catch (InvalidFieldIndexException exc) {
            return null;
        }
    }

    /**
     *  Sets the value of a cell of the table. This is an alias method for
     *  <i>setCell()</i>. The method is defined by the <i>ITableModel</i> interface.
     *  @param row The index of the row to which the cell belongs.
     *  @param col The index of the column to which the cell belongs.
     *  @param value The new value of the cell.
     *  @param insert The CTable implementation of this method does not make use of this
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
     *  <i>getCell()</i>. The method is defined by the <i>ITableModel</i> interface.
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

    /** 'row' version of <i>setActiveRecord()</i>. Delegates all its functionality to
     *  <i>setActiveRecord()</i>.
     */
    public void setActiveRow(int record) {
        setActiveRecord(record);
    }

    /** 'row' version of <i>getActiveRecord()</i>. Delegates all its functionality to
     *  <i>getActiveRecord()</i>.
     */
    public int getActiveRow() {
        return activeRecord;
    }

    /** 'row' version of <i>isRecordSelected()</i>. Delegates all its functionality to
     *  <i>isRecordSelected()</i>.
     */
    public boolean isRowSelected(int record) {
        return isRecordSelected(record);
    }

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            handle = ESlate.registerPart(this);
            tableSO = new TableSO(handle);
            tableSO.setTable(null); //table this);
            try {
                Plug tablePin = new MultipleOutputPlug(
                              handle,
                              bundle,
                              "Table",
                              new Color(102, 88, 187),
                              TableSO.class,
                              tableSO);
                tablePin.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.OUTPUT_CONNECTION) {
                            SharedObjectEvent soe = new SharedObjectEvent(tableSO);
                            ((SharedObjectPlug) e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe);
                        }
                    }
                });
                handle.addPlug(tablePin);
            }catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("Something went wrong during pin creation: " + exc.getMessage());
            }
            try{
                if (getTitle() != null)
                    handle.setUniqueComponentName(bundle.getString("Table") + getTitle());
                else
                    handle.setUniqueComponentName(bundle.getString("Table"));
            }catch (RenamingForbiddenException exc) {exc.printStackTrace();}

        }
        return handle;
    }
}


