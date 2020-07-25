/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 22 Οκτ 2002
 * Time: 3:21:09 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ObjectBaseArray;
import gr.cti.typeArray.ArrayBase;
//1 import gr.cti.eslate.database.view.FieldView;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.utils.ESlateFieldMap2;

import java.io.Externalizable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.net.URL;

import com.objectspace.jgl.Array;

/**
 * @version 2.0, May 01
 */
public abstract class AbstractTableField {
    /** The version of the storage format of the AbstractTableField class
     */
//    public static final String STR_FORMAT_VERSION = "1.0";
    // 2 --> 3 'dependsOnFields' was converted to IntBaseArray from Array.
	//         'dependentCalcFields' was alaso conveted to IntBaseArray from Array.
	//         'index' was added to the class and is stored.
    public static final int FORMAT_VERSION = 1;
    public static final int ASCENDING = 1;
    public static final int DESCENDING = 2;
    static final int stlThreshold = 16;


    /** The name of the field.
     */
    String fieldName;
    /** Specifies if the field is editable.
     */
    boolean isEditable;
    /** Specifies if the field is part of the table's key.
     */
//    boolean isKey;
    /** Specifies if the field is removable.
     */
    boolean isRemovable;
    /** Specifies if the field is calculated.
     */
    boolean isCalculated = false;
    /** Specified if the field is hidden.
     */
    boolean isHidden = false;
    /** The field's formula in internal format, if it is calculated.
     */
    String formula = null;
    /** The field's formula, if it is calculated.
     */
    String textFormula = null;
    /** The list of the indices of the fields on which this field depends, if it is calculated.
     */
    /** The list of the indices of the fields on which this field depends, if it is calculated.
     */
    protected IntBaseArray dependsOnFields = null;
    /** The list of the indices of the calculated fields, which depend on this field.
     */
    protected IntBaseArray dependentCalcFields = new IntBaseArray();
    /** The <A HREF="OperandExpression.html">OperandExpression</A> generated from the field's formula, if the field is calculated.
     */
    transient protected OperandExpression oe = null;
    /** The value of the cells of a calculated field, if it does not depend on any other fields of the table.
     */
    protected Object calculatedValue = null;
    /** This Array stores field-specific attributes, which are not used by the database engine, but
     *  by the database UI. So it's up to the UI builder to store any attributes in this Array, which
     *  need to persist from session to session.
     */
//1    public Array UIProperties = new Array();
    private boolean fieldEditabilityChangeAllowed = true;
    private boolean fieldRemovabilityChangeAllowed = true;
    private boolean fieldDataTypeChangeAllowed = true;
    private boolean calcFieldResetAllowed = false;
    private boolean fieldKeyAttributeChangeAllowed = true;
    private boolean fieldHiddenAttributeChangeAllowed = true;
    private boolean calcFieldFormulaChangeAllowed = false;
    boolean linkToExternalData = false;

    /** Contains all the UI settings of the AbstractTableField. This class gives the AbstractTableField the
     *  ability to 'carry' the settings which adjust its view in a Database component.
     */
//1    FieldView fieldView = null;
    static final long serialVersionUID = 12;

    boolean sorted = false;
    int sortDirection = ASCENDING;
	/**
	 * The index of the field in the Table. This does not change once the field
	 * is added to the Table.
	 */
	int indexInTable = -1;
    // The Table this field belongs to.
    Table table = null;


    /** This constructor exists only for the Extrernalization mechanism.
     *  It should not be used as part of the API.
     */
//    public TableField() {
//    }

    /**
     *  Creates an new TableField instance.
     *  @param  colName The name of the new field.
     *  @param  typeName One of <i>integer</i>, <i>double</i>, <i>string</i>, <i>boolean</i>, <i>date</i>, <i>time</i>, <i>url</i> and <i>image</i>.
     *  @param  isEdit Specifies if the new field is editable.
     *  @param  key    Specifies if the new field is part of the key.
     *  @param  isRemov Specifies if the new field is removable.
     *  @param  isHidden Specifies if the new field is hidden.
     *  @exception InvalidFieldTypeException If an invalid data type is supplied.
     */
/*    protected TableField(String colName, String typeName, boolean isEdit, boolean Key, boolean isRemov, boolean isHidden)
    throws InvalidFieldTypeException {
        String typeNameInLowerCase = typeName.toLowerCase();

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
        else if (typeNameInLowerCase.equals("date")) {    //date
                fieldType = Class.forName("gr.cti.eslate.database.engine.CDate"); //java.util.Date");
                date = true;
        }else if (typeNameInLowerCase.equals("time")) {    //time
                fieldType = Class.forName("gr.cti.eslate.database.engine.CTime"); //java.util.Date");
                date = false;
        }else
            throw new InvalidFieldTypeException(". " + Table.bundle.getString("CTableMsg77") + typeName + "\"");
//        System.out.println(fieldType);
        }catch (ClassNotFoundException e) {e.printStackTrace();}
//         catch (InstantiationException e) {}
//         catch (IllegalAccessException e) {}

        fieldName = colName;
        isEditable = isEdit;
        isRemovable = isRemov;
        isKey = Key;
        this.isHidden = isHidden;
    }
*/

/*    TableField(CTableField f) {
        fieldType = f.fieldType;
        date = f.isDate();
        fieldName = f.getName();
        isEditable = f.isEditable();
        isKey = f.isKey();
        isRemovable = f.isRemovable();
        isCalculated = f.isCalculated();
        formula = f.formula;
        textFormula = f.textFormula;
        dependsOnFields = Table.convertArrayToIntBaseArray(f.getDependsOnFields());
        dependentCalcFields = Table.convertArrayToIntBaseArray(f.getDependentCalcFields());
        calculatedValue = f.calculatedValue;
        UIProperties = f.UIProperties;
        oe = f.oe;
        isHidden = f.isHidden();
        fieldEditabilityChangeAllowed = f.isFieldEditabilityChangeAllowed();
        fieldRemovabilityChangeAllowed = f.isFieldRemovabilityChangeAllowed();
        fieldDataTypeChangeAllowed = f.isFieldDataTypeChangeAllowed();
        calcFieldResetAllowed = f.isCalcFieldResetAllowed();
        fieldKeyAttributeChangeAllowed = f.isFieldKeyAttributeChangeAllowed();
        fieldHiddenAttributeChangeAllowed = f.isFieldHiddenAttributeChangeAllowed();
        calcFieldFormulaChangeAllowed = f.isCalcFieldFormulaChangeAllowed();
        linkToExternalData = f.containsLinksToExternalData();
    }
*/

    /** Returns the Java class type of the data the field stores. */
    public abstract Class getDataType();

    /**
     *  Returns the field's name.
     */
    public final String getName() {
        return fieldName;
    }


    /**
     *  Sets the field's name.
     */
    protected void setName(String s) {
        fieldName = s;
    }


    /**
     *  Returns the field's <i>isKey</i> attribute.
     */
/*    public final boolean isKey() {
        return table.tableKey.isPartOfTableKey(this); //isKey;
    }
*/

    /**
     *  Sets the fields <i>isKey</i> attribute.
     */
//    protected void setKey(boolean key) throws InvalidKeyFieldException, AttributeLockedException, FieldNotEditableException,
//            FieldAlreadyInKeyException, FieldContainsEmptyCellsException, TableNotExpandableException {
//        table.tableKey.addKeyField(this);
/*        if (!fieldKeyAttributeChangeAllowed)
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg1"));

        if (key && getDataType().equals(CImageIcon.class))
            throw new InvalidKeyFieldException(Table.bundle.getString("CTableMsg67"));
        isKey = key;
*/
//    }


    /**
     *  Returns the field's data type.
     */
/*    public final Class getFieldType() {
        return fieldType;
    }
*/

    /**
     *  Set's the field's data type.
     *  @param c One of the following java classes:
     *              <ul>
     *              <li>java.lang.Integer
     *              <li>java.lang.Double
     *              <li>java.lang.String
     *              <li>java.lang.Boolean
     *              <li>gr.cti.eslate.database.engine.CDate
     *              <li>gr.cti.eslate.database.engine.CTime
     *              <li>java.net.URL
     *              <li>gr.cti.eslate.database.engine.CImageIcon
     *              </ul>
     */
/*    protected void setFieldType(Class c) throws AttributeLockedException {
        if (!fieldDataTypeChangeAllowed)
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg2"));
        fieldType = c;
    }
*/

    /**
     *  Returns <i>true</i> if the field is editable, <i>false</i> otherwise.
     */
    public final boolean isEditable() {
        return isEditable;
    }


    /**
     *  Sets the field's <i>isEditable</i> attribute.
     *  @return Returns <i>false</i> if the field is calculated.
     */
    protected boolean setEditable(boolean isEditable) throws AttributeLockedException {
        if (!fieldEditabilityChangeAllowed)
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg3"));

        if (isCalculated)
            return false;
        this.isEditable = isEditable;
        return true;
    }


    /**
     *  Returns <i>true</i> if the field is removable, <i>false</i> otherwise.
     */
    public final boolean isRemovable() {
        return isRemovable;
    }


    /**
     *  Sets the field's <i>isRemovable</i> attribute.
     */
    protected void setRemovable(boolean isRemov) throws AttributeLockedException {
        if (!fieldRemovabilityChangeAllowed)
            throw new AttributeLockedException(DBase.resources.getString("CTableFieldMsg4"));
        isRemovable = isRemov;
    }


    /**
     *  Returns <i>true</i> if the field is hidden, <i>false</i> otherwise.
     */
    public final boolean isHidden() {
        return isHidden;
    }


    /**
     *  Sets the field's <i>isHidden</i> attribute.
     */
    protected void setHidden(boolean isHidden) throws AttributeLockedException {
        if (!fieldHiddenAttributeChangeAllowed)
            throw new AttributeLockedException(DBase.resources.getString("CTableFieldMsg6"));
        this.isHidden = isHidden;
    }


    /**
     *  Returns <i>true</i> if this field is equal to the given one. Field equality is defined as follows:
     *          <ul>
     *          <li> They have the same name and data type.
     *          <li> They are both calculated or not.
     *          <li> If they are calculated, their formulas are identical.
     *          </ul>
     *  @param f The second field.
     */
    public boolean equals(AbstractTableField f) {
        if (!f.getName().equals(fieldName))
            return false;
        if (!f.getDataType().getName().equals(getDataType().getName()))
            return false;
//t        if (fieldType.getName().equals("java.util.Date")) {
//t            if (!(f.isDate()==date))
//t                return false;
//t        }
        if (f.isCalculated != isCalculated)
            return false;
        if (f.formula == null) {
            if (formula != null)
                return false;
        }else{
            if (formula == null)
                return false;
            else{
                if (!f.formula.equals(formula))
                    return false;
            }
        }
        return true;
    }


    /**
     *  Returns <i>true</i> if this field has the same data type as the given one.
     *  @param f The second field.
     */
    public boolean hasEqualType(AbstractTableField f) {
        if (!f.getDataType().getName().equals(getDataType().getName()))
            return false;
//t        if (fieldType.getName().equals("java.util.Date")) {
//t            if (!(f.isDate()==date))
//t                return false;
//t        }
        return true;
    }


    /**
     *  Converts a normal field to calculated and vice-versa.
     *  @param  isCalc <i>true</i>, if the field is converted to calculated. <i>false</i>,
     *          if a calculated field is turned into a normal one.
     *  @param  formula The formula of the field.
     *  @param  dependentFields The Array of the indices of the fields on which this fields depends.
     */
    protected void setCalculated(boolean isCalc, String formula, String textFormula, IntBaseArray dependentFields)
    throws AttributeLockedException {
        if (isCalc) {
            if (isCalculated && !calcFieldFormulaChangeAllowed)
                throw new AttributeLockedException(DBase.resources.getString("CTableFieldMsg7"));
            this.formula = formula;
            this.textFormula = textFormula;
            dependsOnFields = dependentFields;
            calcFieldResetAllowed = true;
            calcFieldFormulaChangeAllowed = true;
        }else{
            if (!calcFieldResetAllowed)
                throw new AttributeLockedException(DBase.resources.getString("CTableFieldMsg5"));

            formula = null;
            textFormula = null;
            dependsOnFields = null;
            oe = null;
            calcFieldResetAllowed = false;
            calcFieldFormulaChangeAllowed = false;
        }

        isCalculated = isCalc;
    }


    /**
     *  Returns <i>true</i> if the field is calculated.
     */
    public final boolean isCalculated() {
        return isCalculated;
    }


    /**
     *  Returns the field's formula.
     */
    public final String getTextFormula() {
        return textFormula;
    }


    /**
     *  Returns the Array of the indices of the fields on which this field depends.
     */
    public final IntBaseArray getDependsOnFields() {
        return dependsOnFields;
    }


    /**
     *  Returns the Array of the indices of the calculated fields, which depend on
     *  this field.
     */
    public final IntBaseArray getDependentCalcFields() {
        return dependentCalcFields;
    }


    /**
     *  Defines the ability to change the field's editable status, regarding its content.
     */
    protected void setFieldEditabilityChangeAllowed(boolean allowed) {
        fieldEditabilityChangeAllowed = allowed;
    }


    /**
     *  Returns if the editable status of the field can be changed.
     */
    public final boolean isFieldEditabilityChangeAllowed() {
        return fieldEditabilityChangeAllowed;
    }


    /**
     *  Defines the ability to change the field's "removable" attribute.
     */
    protected void setFieldRemovabilityChangeAllowed(boolean allowed) {
        fieldRemovabilityChangeAllowed = allowed;
    }


    /**
     *  Returns if the "removal" attribute of the field can be changed.
     */
    public final boolean isFieldRemovabilityChangeAllowed() {
        return fieldRemovabilityChangeAllowed;
    }


    /**
     *  Defines the ability to change the field's data type.
     */
    protected void setFieldDataTypeChangeAllowed(boolean allowed) {
        fieldDataTypeChangeAllowed = allowed;
    }


    /**
     *  Returns if the field's data type can be changed.
     */
    public final boolean isFieldDataTypeChangeAllowed() {
        return fieldDataTypeChangeAllowed;
    }


    /**
     *  Defines the ability to tranform a calculated field to normal.
     *  This attribute is always true for non-calculated fields.
     */
    protected void setCalcFieldResetAllowed(boolean allowed) {
        if (isCalculated())
            calcFieldResetAllowed = allowed;
    }


    /**
     *  Returns if a calculated field can be transformed to normal.
     */
    public final boolean isCalcFieldResetAllowed() {
        return calcFieldResetAllowed;
    }


    /**
     *  Locks the field's "key" attribute.
     */
    protected void setFieldKeyAttributeChangeAllowed(boolean allowed) {
        fieldKeyAttributeChangeAllowed = allowed;
    }


    /**
     *  Returns if the field's key attribute is locked, i.e. it can't change.
     */
    public final boolean isFieldKeyAttributeChangeAllowed() {
        return fieldKeyAttributeChangeAllowed;
    }


    /**
     *  Locks the field's "Hidden" attribute.
     */
    protected void setFieldHiddenAttributeChangeAllowed(boolean allowed) {
        fieldHiddenAttributeChangeAllowed = allowed;
    }


    /**
     *  Returns if the field's "Hidden" attribute is locked, i.e. it can't change.
     */
    public final boolean isFieldHiddenAttributeChangeAllowed() {
        return fieldHiddenAttributeChangeAllowed;
    }


    /**
     *  Locks the calculated field's formula.
     */
    protected void setCalcFieldFormulaChangeAllowed(boolean allowed) {
        calcFieldFormulaChangeAllowed = allowed;
    }


    /**
     *  Returns if the calculated field's formula is locked, i.e. it can't change.
     */
    public final boolean isCalcFieldFormulaChangeAllowed() {
        return calcFieldFormulaChangeAllowed;
    }


    /**
     * Returns if the contents of the fields is stored outside the cdb archive. In this
     * case the database stores references to the actual data. This stands only for
     * image data, which can be stored in external files. The database stores just
     * refrebces to these files and dynamically loads the images from them.
     */
    public final boolean containsLinksToExternalData() {
        return linkToExternalData;
    }


    protected final void setContainsLinksToExternalData(boolean containsLinks) {
        if (getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
            if (linkToExternalData != containsLinks)
                linkToExternalData = containsLinks;
        }
    }

    /** Returns the contents of the cell in textual format.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value of the cell as a String.
     */
    public abstract String getCellAsString(int recIndex);


    public void applyState(StorageStructure fieldMap) {
		int dataVersion = fieldMap.getDataVersionID();
//        oldFieldType = (Class) fieldMap.get("Field type");
//        date = fieldMap.get("Is date field", true);
        fieldName = (String) fieldMap.get("Field name");
//System.out.println("Restoring field: " + fieldName + ", of type: " + fieldType);
        isEditable = fieldMap.get("Editable", true);
//        isKey = fieldMap.get("Key", false);
        isRemovable = fieldMap.get("Removable", true);
        isCalculated = fieldMap.get("Calculated", false);
        formula = fieldMap.get("Formula", (String) null);
        textFormula = fieldMap.get("Text formula", (String) null);
        dependsOnFields = (IntBaseArray) fieldMap.get("Depends on fields");
		dependentCalcFields = (IntBaseArray) fieldMap.get("Dependent fields");
        calculatedValue = (Object) fieldMap.get("Calculated value");
//1        UIProperties = (Array) fieldMap.get("UIProperties");

        isHidden = fieldMap.get("Hidden", false);
        fieldEditabilityChangeAllowed = fieldMap.get("Field editability change allowed", true);
        fieldRemovabilityChangeAllowed = fieldMap.get("Field removability change allowed", true);
        fieldDataTypeChangeAllowed = fieldMap.get("Field data type change allowed", true);
        calcFieldResetAllowed = fieldMap.get("Calc field reset allowed", isCalculated);
        fieldKeyAttributeChangeAllowed = fieldMap.get("Field key attrib change allowed", true);
        fieldHiddenAttributeChangeAllowed = fieldMap.get("Field hidden attrib change allowed", true);
        calcFieldFormulaChangeAllowed = fieldMap.get("Calc field formula change allowed", isCalculated);
        linkToExternalData = fieldMap.get("Link to external data", false);
//1        fieldView = (FieldView) fieldMap.get("Field View");
		indexInTable = fieldMap.get("Field Index", -1);
//System.out.println("Read field: " + fieldName + ", indexInTable: " + indexInTable);
    }


    public StorageStructure recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
//        fieldMap.put("Field type", oldFieldType);
//        fieldMap.put("Is date field", date);
        fieldMap.put("Field name", fieldName);
        fieldMap.put("Editable", isEditable);
//        fieldMap.put("Key", isKey);
        fieldMap.put("Removable", isRemovable);
        fieldMap.put("Calculated", isCalculated);
        fieldMap.put("Formula", formula);
        fieldMap.put("Text formula", textFormula);
        fieldMap.put("Depends on fields", dependsOnFields);
        fieldMap.put("Dependent fields", dependentCalcFields);
        fieldMap.put("Calculated value", calculatedValue);
//1        fieldMap.put("UIProperties", UIProperties);

        fieldMap.put("Hidden", isHidden);
        fieldMap.put("Field editability change allowed", fieldEditabilityChangeAllowed);
        fieldMap.put("Field removability change allowed", fieldRemovabilityChangeAllowed);
        fieldMap.put("Field data type change allowed", fieldDataTypeChangeAllowed);
        fieldMap.put("Calc field reset allowed", calcFieldResetAllowed);
        fieldMap.put("Field key attrib change allowed", fieldKeyAttributeChangeAllowed);
        fieldMap.put("Field hidden attrib change allowed", fieldHiddenAttributeChangeAllowed);
        fieldMap.put("Calc field formula change allowed", calcFieldFormulaChangeAllowed);
        fieldMap.put("Link to external data", linkToExternalData);
		fieldMap.put("Field Index", indexInTable);
//System.out.println("Writing field: " + fieldName + ", indexInTable: " + indexInTable);

//1        fieldMap.put("Field View", fieldView);

        return fieldMap;
    }

    public abstract String getLocalizedDataTypeName();

    public final static String localizedNameForDataType(Class dataType) {
        String fldType = "";
        if (dataType.equals(IntegerTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Integer");
        else if (dataType.equals(StringTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Alphanumeric");
        else if (dataType.equals(DoubleTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Double");
        else if (dataType.equals(FloatTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Float");
        else if (dataType.equals(ImageTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Image");
        else if (dataType.equals(URLTableField.DATA_TYPE))
            fldType = DBase.resources.getString("URL");
        else if (dataType.equals(BooleanTableField.DATA_TYPE))
            fldType = DBase.resources.getString("Boolean");
        else if (dataType.equals(DateTableField.DATA_TYPE)) //java.util.Date.class)) {
            fldType = DBase.resources.getString("Date");
        else if (dataType.equals(TimeTableField.DATA_TYPE)) //java.util.Date.class)) {
            fldType = DBase.resources.getString("Time");

        return fldType;
    }

    public final static String localizedNameForDataType(AbstractTableField f) {
        return localizedNameForDataType(f.getDataType());
    }

    /** @deprecated Replaced by <i>getInternalDataTypeName(Class fieldType)</i>
     */
    public final static String getInternalDataTypeName(Class fieldType, boolean isDate) {
        return getInternalDataTypeName(fieldType);
    }

    public final static String getInternalDataTypeName(Class fieldType) {
        String fldType = "";
        if (fieldType.equals(IntegerTableField.DATA_TYPE))
            fldType = "Integer";
        else if (fieldType.equals(StringTableField.DATA_TYPE))
            fldType = "String";
        else if (fieldType.equals(DoubleTableField.DATA_TYPE))
            fldType = "Double";
        else if (fieldType.equals(FloatTableField.DATA_TYPE))
            fldType = "Float";
        else if (fieldType.equals(ImageTableField.DATA_TYPE))
            fldType = "Image";
        else if (fieldType.equals(URLTableField.DATA_TYPE))
            fldType = "URL";
        else if (fieldType.equals(BooleanTableField.DATA_TYPE))
            fldType = "Boolean";
        else if (fieldType.equals(DateTableField.DATA_TYPE))
            fldType = "Date";
        else if (fieldType.equals(TimeTableField.DATA_TYPE))
            fldType = "Time";

        return fldType;
    }

	public static final Class getInternalDataType(String typeName) {
		Class fldType = null;
		typeName = typeName.toLowerCase();
		if (typeName.equals("integer") || typeName.equals(DBase.resources.getString("Integer").toLowerCase()))
			fldType = IntegerTableField.DATA_TYPE;
		else if (typeName.equals("string") || typeName.equals(DBase.resources.getString("Alphanumeric").toLowerCase()))
			fldType = StringTableField.DATA_TYPE;
		else if (typeName.equals("double") || typeName.equals(DBase.resources.getString("Number").toLowerCase()))
			fldType = DoubleTableField.DATA_TYPE;
        else if (typeName.equals("float") || typeName.equals(DBase.resources.getString("Float").toLowerCase()))
            fldType = FloatTableField.DATA_TYPE;
		else if (typeName.equals("image") || typeName.equals(DBase.resources.getString("Image").toLowerCase()))
			fldType = ImageTableField.DATA_TYPE;
		else if (typeName.equals("url") || typeName.equals(DBase.resources.getString("URL").toLowerCase()))
			fldType = URLTableField.DATA_TYPE;
		else if (typeName.equals("boolean") || typeName.equals(DBase.resources.getString("Boolean").toLowerCase()))
			fldType = BooleanTableField.DATA_TYPE;
		else if (typeName.equals("date") || typeName.equals(DBase.resources.getString("Date").toLowerCase()))
			fldType = DateTableField.DATA_TYPE;
		else if (typeName.equals("time") || typeName.equals(DBase.resources.getString("Time").toLowerCase()))
			fldType = TimeTableField.DATA_TYPE;

		return fldType;

	}

    public final static String getInternalDataTypeName(AbstractTableField field) {
        String fldType = "";
        if (field.getDataType().equals(IntegerTableField.DATA_TYPE))
            fldType = "Integer";
        else if (field.getDataType().equals(StringTableField.DATA_TYPE))
            fldType = "String";
        else if (field.getDataType().equals(DoubleTableField.DATA_TYPE))
            fldType = "Double";
        else if (field.getDataType().equals(ImageTableField.DATA_TYPE))
            fldType = "Image";
        else if (field.getDataType().equals(URLTableField.DATA_TYPE))
            fldType = "URL";
        else if (field.getDataType().equals(BooleanTableField.DATA_TYPE))
            fldType = "Boolean";
        else if (field.getDataType().equals(DateTableField.DATA_TYPE))
            fldType = "Date";
        else if (field.getDataType().equals(TimeTableField.DATA_TYPE))
            fldType = "Time";

        return fldType;
    }

	public int getFieldIndex() {
		return indexInTable;
	}

/*1    public void setFieldView(FieldView view) {
        fieldView = view;
    }

    public FieldView getFieldView() {
        return fieldView;
    }
1*/
    /** Returns if the Table this field belongs to is sorted on this field.
     */
    public boolean isSorted() {
        return sorted;
    }

    /** Return the direction of the sorting which may have happened on this field.
     *  The direction is valid only while <code>isSorted()</code> returns true.
     *  @return One of <code>AbstractTableField.ASCENDING</code>, <code>AbstractTableField.DESCENDING</code>.
     */
    public int getSortDirection() {
        return sortDirection;
    }

    /** Creates a field view for the supplied <code>field</code>.
     * @param field The AbstractTableField which is the base for the new field view.
     * @return The field view which corresponds to the to the data type of the <code>field</code>.
     */
    protected static TableFieldView createNewFieldView(AbstractTableField field) {
        AbstractTableField fld = null;
        if (field.getDataType().equals(IntegerTableField.DATA_TYPE))
            fld = new IntegerTableFieldView((IntegerTableField) field);
        else if (field.getDataType().equals(StringTableField.DATA_TYPE))
            fld = new StringTableFieldView((StringTableField) field);
        else if (field.getDataType().equals(DoubleTableField.DATA_TYPE))
            fld = new DoubleTableFieldView((DoubleTableField) field);
        else if (field.getDataType().equals(FloatTableField.DATA_TYPE))
            fld = new FloatTableFieldView((FloatTableField) field);
        else if (field.getDataType().equals(ImageTableField.DATA_TYPE))
            fld = new ImageTableFieldView((ImageTableField) field);
        else if (field.getDataType().equals(URLTableField.DATA_TYPE))
            fld = new URLTableFieldView((URLTableField) field);
        else if (field.getDataType().equals(BooleanTableField.DATA_TYPE))
            fld = new BooleanTableFieldView((BooleanTableField) field);
        else if (field.getDataType().equals(DateTableField.DATA_TYPE))
            fld = new DateTableFieldView((DateTableField) field);
        else if (field.getDataType().equals(TimeTableField.DATA_TYPE))
            fld = new TimeTableFieldView((TimeTableField) field);

        return (TableFieldView) fld;
    }

    /** Creates a new TableFieldView, which is based on the same AbstractTableField as the <code>fieldView</code>.
     * @param fieldView The TableFieldView for whose underlying AbstractTableField a new TableFieldView will be created.
     * @return The new TableFieldView.
     */
    protected static TableFieldView createNewFieldView(TableFieldView fieldView) {
        AbstractTableField fld = null;
        AbstractTableField field = (AbstractTableField) fieldView;
        AbstractTableField baseField = fieldView.getBaseField();
        if (field.getDataType().equals(IntegerTableField.DATA_TYPE))
            fld = new IntegerTableFieldView((IntegerTableField) baseField);
        else if (field.getDataType().equals(StringTableField.DATA_TYPE))
            fld = new StringTableFieldView((StringTableField) baseField);
        else if (field.getDataType().equals(DoubleTableField.DATA_TYPE))
            fld = new DoubleTableFieldView((DoubleTableField) baseField);
        else if (field.getDataType().equals(FloatTableField.DATA_TYPE))
            fld = new FloatTableFieldView((FloatTableField) baseField);
        else if (field.getDataType().equals(ImageTableField.DATA_TYPE))
            fld = new ImageTableFieldView((ImageTableField) baseField);
        else if (field.getDataType().equals(URLTableField.DATA_TYPE))
            fld = new URLTableFieldView((URLTableField) baseField);
        else if (field.getDataType().equals(BooleanTableField.DATA_TYPE))
            fld = new BooleanTableFieldView((BooleanTableField) baseField);
        else if (field.getDataType().equals(DateTableField.DATA_TYPE))
            fld = new DateTableFieldView((DateTableField) baseField);
        else if (field.getDataType().equals(TimeTableField.DATA_TYPE))
            fld = new TimeTableFieldView((TimeTableField) baseField);

        return (TableFieldView) fld;
    }

	/** Returns a new field of the type specified by the <code>fieldType</code> parameter.
	 * @param  name        The name of the new field.
	 * @param  fieldType   The type of the field, expressed a a Java class. The class of the new field
	 * will be one of the following, depending on the value of this parameter:
	 * <ul>
	 * <li> StringTableField, if <code>fieldType</code> is String.class.
	 * <li> DoubleTableField, if <code>fieldType</code> is Double.class.
     * <li> FloatTableField, if <code>fieldType</code> is Float.class.
	 * <li> IntegerTableField, if <code>fieldType</code> is Integer.class.
	 * <li> BooleanTableField, if <code>fieldType</code> is Boolean.class.
	 * <li> DateTableField, if <code>fieldType</code> is gr.cti.eslate.database.engine.CDate.class, or a sub-class.
	 * <li> TimeTableField, if <code>fieldType</code> is gr.cti.eslate.database.engine.CTime.class, or a sub-class.
	 * <li> URLTableField, if <code>fieldType</code> is java.net.URL.class.
	 * <li> ImageTableField, if <code>fieldType</code> is gr.cti.eslate.database.engine.CImageIcon.class, or a sub-class.
	 * </ul>
	 * @param  editable    Determines if the attributes of the new field will be editable.
	 * @param  removable   Determines if the new field can be deleted.
	 * @param  hidden      Determines if the new field is hidden.
	 * @exception InvalidFieldTypeException If <code>fieldType</code> is not one of the supported types.
	 */
	static AbstractTableField createNewField(String name, Class fieldType, boolean editable, boolean removable, boolean hidden) throws InvalidFieldTypeException {
//        if (table == null)
//            throw new NullPointerException();
		AbstractTableField fld = null;
		if (StringTableField.DATA_TYPE.isAssignableFrom(fieldType)) {      //string
			fld = new StringTableField(name, editable, removable, hidden);
		}else if (DoubleTableField.DATA_TYPE.isAssignableFrom(fieldType)) {//double
			fld = new DoubleTableField(name, editable, removable, hidden);
        }else if (FloatTableField.DATA_TYPE.isAssignableFrom(fieldType)) {//double
            fld = new FloatTableField(name, editable, removable, hidden);
		}else if (IntegerTableField.DATA_TYPE.isAssignableFrom(fieldType)) { //integer
			fld = new IntegerTableField(name, editable, removable, hidden);
		}else if (BooleanTableField.DATA_TYPE.isAssignableFrom(fieldType)) {  //boolean
			fld = new BooleanTableField(name, editable, removable, hidden);
		}else if (DateTableField.DATA_TYPE.isAssignableFrom(fieldType)) {    //date
			fld = new DateTableField(name, editable, removable, hidden);
		}else if (TimeTableField.DATA_TYPE.isAssignableFrom(fieldType)) {    //time
			fld = new TimeTableField(name, editable, removable, hidden);
		}else if (URLTableField.DATA_TYPE.isAssignableFrom(fieldType)) {    //url
			fld = new URLTableField(name, editable, removable, hidden);
		}else if (ImageTableField.DATA_TYPE.isAssignableFrom(fieldType)) {  //image
			fld = new ImageTableField(name, editable, removable, hidden);
		}else{
			throw new InvalidFieldTypeException(". " + Table.bundle.getString("CTableMsg77") + fieldType.getName() + "\"");
		}
//        fld.table = table;
		return fld;
	}

	static AbstractTableField createNewField(Table table, CTableField f) {
		AbstractTableField field = null;
		try{
			field = createNewField(f.getName(), f.getFieldType(), f.isEditable(), f.isRemovable(), f.isHidden());
		}catch (InvalidFieldTypeException exc) {
			System.out.println("Converting a CTableField to TableField. This exception should never occur");
			exc.printStackTrace();
		}
		field.isCalculated = f.isCalculated();
		field.formula = f.formula;
		field.textFormula = f.textFormula;
		field.dependsOnFields = Table.convertArrayToIntBaseArray(f.getDependsOnFields());
		field.dependentCalcFields = Table.convertArrayToIntBaseArray(f.getDependentCalcFields());
		field.calculatedValue = f.calculatedValue;
//1		field.UIProperties = f.UIProperties;
		field.oe = f.oe;
		field.fieldEditabilityChangeAllowed = f.isFieldEditabilityChangeAllowed();
		field.fieldRemovabilityChangeAllowed = f.isFieldRemovabilityChangeAllowed();
		field.fieldDataTypeChangeAllowed = f.isFieldDataTypeChangeAllowed();
		field.calcFieldResetAllowed = f.isCalcFieldResetAllowed();
		field.fieldKeyAttributeChangeAllowed = f.isFieldKeyAttributeChangeAllowed();
		field.fieldHiddenAttributeChangeAllowed = f.isFieldHiddenAttributeChangeAllowed();
		field.calcFieldFormulaChangeAllowed = f.isCalcFieldFormulaChangeAllowed();
		field.linkToExternalData = f.containsLinksToExternalData();
		return field;
	}

    static AbstractTableField createProperSubClassField(Table table, TableField f) {
		AbstractTableField field = null;
		try{
			field = createNewField(f.getName(), f.oldFieldType, f.isEditable(), f.isRemovable(), f.isHidden());
		}catch (InvalidFieldTypeException exc) {
			System.out.println("Converting a CTableField to TableField. This exception should never occur");
			exc.printStackTrace();
		}
		field.isCalculated = f.isCalculated();
		field.formula = f.getFormula();
		field.textFormula = f.textFormula;
		field.dependsOnFields = f.getDependsOnFields();
		field.dependentCalcFields = f.getDependentCalcFields();
		field.calculatedValue = f.calculatedValue;
//1		field.UIProperties = f.UIProperties;
		field.oe = f.oe;
		field.fieldEditabilityChangeAllowed = f.isFieldEditabilityChangeAllowed();
		field.fieldRemovabilityChangeAllowed = f.isFieldRemovabilityChangeAllowed();
		field.fieldDataTypeChangeAllowed = f.isFieldDataTypeChangeAllowed();
		field.calcFieldResetAllowed = f.isCalcFieldResetAllowed();
		field.fieldKeyAttributeChangeAllowed = f.isFieldKeyAttributeChangeAllowed();
		field.fieldHiddenAttributeChangeAllowed = f.isFieldHiddenAttributeChangeAllowed();
		field.calcFieldFormulaChangeAllowed = f.isCalcFieldFormulaChangeAllowed();
		field.linkToExternalData = f.containsLinksToExternalData();
		return field;
	}

    /** Returns the class of the field which stores data of the specified <code>dataType</code>.
     *  @param dataType The java data type.
     *  @return The class of the AbstractTableField subclass, which stores values of type <code>dataType</code>.
     */
    public static Class getFieldClassForDataType(Class dataType) {
        if (DoubleTableField.DATA_TYPE.isAssignableFrom(dataType))
            return DoubleTableField.class;
        else if (FloatTableField.DATA_TYPE.isAssignableFrom(dataType))
            return IntegerTableField.class;
        else if (IntegerTableField.DATA_TYPE.isAssignableFrom(dataType))
            return IntegerTableField.class;
        else if (BooleanTableField.DATA_TYPE.isAssignableFrom(dataType))
            return BooleanTableField.class;
        else if (StringTableField.DATA_TYPE.isAssignableFrom(dataType))
            return StringTableField.class;
        else if (DateTableField.DATA_TYPE.isAssignableFrom(dataType))
            return DateTableField.class;
        else if (TimeTableField.DATA_TYPE.isAssignableFrom(dataType))
            return TimeTableField.class;
        else if (URLTableField.DATA_TYPE.isAssignableFrom(dataType))
            return URLTableField.class;
        else if (ImageTableField.DATA_TYPE.isAssignableFrom(dataType))
            return ImageTableField.class;
        return null;
    }

    /** Returns the comparators supported by the AbstractTableField of class <code>fieldType</code>.
     */
    public static HashMap getComparatorsForFieldType(Class fieldType) {
        if (IntegerTableField.class.equals(fieldType))
            return IntegerTableField.getComparators();
        else if (FloatTableField.class.equals(fieldType))
            return FloatTableField.getComparators();
        else if (DoubleTableField.class.equals(fieldType))
            return DoubleTableField.getComparators();
        else if (BooleanTableField.class.equals(fieldType))
            return BooleanTableField.getComparators() ;
        else if (StringTableField.class.equals(fieldType))
            return StringTableField.getComparators();
        else if (DateTableField.class.equals(fieldType))
            return DateTableField.getComparators();
        else if (TimeTableField.class.equals(fieldType))
            return TimeTableField.getComparators();
        else if (URLTableField.class.equals(fieldType))
            return URLTableField.getComparators();
        else if (ImageTableField.class.equals(fieldType))
            return ImageTableField.getComparators();
        return null;
    }


    /** Updates the calculated fields which depend on this field. This method is called after the value of a cell of this
     *  field changes. The method accepts as parameter the previous value of the cell. If while updating the calculated
     *  fields an error occurs, the value of the cell is reset to its previous value and the dependent calculated
     *  fields are re-calculated.
     *  @param recIndex       The index og the cell of this field that changed value.
     *  @exception  DuplicateKeyException If the new value of a dependent calculated field, which is also part of the
     *                                    Table's key, violates the key uniqueness.
     *  @exception  NullTableKeyException If the new value of a dependent calculated field, which is also part of the
     *                                    Table's key, is null.
     */
    void updateDependentCalculatedFields(int recIndex) throws DuplicateKeyException, NullTableKeyException {
        if (!(dependentCalcFields.size() == 0)) {
            for (int i=0; i<dependentCalcFields.size(); i++) {
                table.evaluateCalculatedFieldCell(dependentCalcFields.get(i), recIndex);

                AbstractTableField f1 = (AbstractTableField) table.tableFields.get(dependentCalcFields.get(i));
                table.changedCalcCells.put(f1.getName(), new Integer(recIndex));
            }
            table.calculatedFieldsChanged = true;
        }
    }

    static final SecondPredicateComparator createSecondOperandOperator(AbstractTableField f, String operator, Object secondOperand) {
        SecondPredicateComparator predicate = null;
        if (f.getDataType().equals(StringTableField.DATA_TYPE))
            predicate = new StringSecondPredicateComparator(((StringTableField) f).getComparator(operator), (String) secondOperand);
        else if (f.getDataType().equals(IntegerTableField.DATA_TYPE))
            predicate = new IntegerSecondPredicateComparator(((IntegerTableField) f).getComparator(operator), (Integer) secondOperand);
        else if (f.getDataType().equals(DoubleTableField.DATA_TYPE))
            predicate = new DoubleSecondPredicateComparator(((DoubleTableField) f).getComparator(operator), (Double) secondOperand);
        else if (f.getDataType().equals(FloatTableField.DATA_TYPE))
            predicate = new FloatSecondPredicateComparator(((FloatTableField) f).getComparator(operator), (Float) secondOperand);
        else if (f.getDataType().equals(BooleanTableField.DATA_TYPE))
            predicate = new BooleanSecondPredicateComparator(((BooleanTableField) f).getComparator(operator), (Boolean) secondOperand);
        else if (f.getDataType().equals(DateTableField.DATA_TYPE))
            predicate = new DateSecondPredicateComparator(((DateTableField) f).getComparator(operator), (CDate) secondOperand);
        else if (f.getDataType().equals(TimeTableField.DATA_TYPE))
            predicate = new TimeSecondPredicateComparator(((TimeTableField) f).getComparator(operator), (CTime) secondOperand);
        else if (f.getDataType().equals(URLTableField.DATA_TYPE))
            predicate = new StringSecondPredicateComparator(((URLTableField) f).getComparator(operator), secondOperand.toString());
//        else if (f.getDataType().equals(CImageIcon.class))
//            predicate = new DoubleSecondPredicateComparator(((DoubleTableField) f).getComparator(Operator), (Double) operand);
        return predicate;
    }

    // Sorting utility method
    static void finalInsertionSort(ObjectBaseArray data, int first, int last, ObjectComparator comparator, IntBaseArray indices) {
        if (last - first > stlThreshold) {
            int limit = first + stlThreshold;

            for (int i = first + 1; i < limit; i++)
                linearInsert(data, first, i, comparator, indices);

            if (indices != null) {
                for (int i = limit; i < last; i++)
                    unguardedLinearInsert(data, i, data.get(i), comparator, indices, indices.get(i));
            }else{
                for ( int i = limit; i < last; i++ )
                    unguardedLinearInsert(data, i, data.get(i), comparator, null, -1);
            }
        }else{
            for (int i = first + 1; i < last; i++)
                linearInsert(data, first, i, comparator, indices);
        }
    }

    // Sorting utility method
    static void unguardedLinearInsert(ObjectBaseArray data, int last, Object value, ObjectComparator comparator, IntBaseArray indices, int index) {
        int next = last - 1;

/*System.out.print("unguardedLinearInsert() data: ");
for (int i=0; i<data.size(); i++)
    System.out.print(data.get(i) + ", ");
System.out.print(", last: " + last + ", comparator: " + comparator.getClass() + ", indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println(", index: " + index + ", value: " + value);
*/
        if (indices == null) {
            while (comparator.execute(value, data.get(next )))
                data.set(last--, data.get(next--));
        }else{
//System.out.println("next: " + next + ", data.get(next ): " + data.get(next ));
            while (comparator.execute(value, data.get(next ))) {
              indices.set(last, indices.get(next));
              data.set(last--, data.get( next--));
//System.out.println("next: " + next + ", data.get(next ): " + data.get(next ));
            }
        }

        data.set(last, value);
        if (indices != null)
            indices.set(last, index);
/*System.out.print("2. unguardedLinearInsert() data: ");
for (int i=0; i<data.size(); i++)
    System.out.print(data.get(i) + ", ");
System.out.print(", last: " + last + ", comparator: " + comparator.getClass() + ", indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println();
System.out.println();
*/
    }

    // Sorting utility method
    static void linearInsert(ObjectBaseArray data, int first, int last, ObjectComparator comparator, IntBaseArray indices) {
        Object value = data.get(last);
        int index = 0;
        if (indices != null)
            index = indices.get(last);

        if (comparator.execute(value, data.get(first))) {
            for (int i = last; i > first; i--) {
                data.set(i, data.get(i - 1));
                if (indices != null)
                    indices.set(i, indices.get(i - 1));
            }
            data.set(first, value);
            if (indices != null)
                indices.set(first, index);
        }else{
            unguardedLinearInsert(data, last, value, comparator, indices, index);
        }
    }

    // Sorting utility method
    static void quickSortLoop(ObjectBaseArray data, int first, int last, ObjectComparator comparator, IntBaseArray indices) {
/*System.out.print("quickSortLoop() data: ");
for (int i=0; i<data.size(); i++)
    System.out.print(data.get(i) + ", ");
System.out.print("first: " + first + ", last: " + last + ", comparator: " + comparator.getClass() + ", indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println();
*/
        while (last - first > stlThreshold) {
            Object pivot;
            Object a = data.get(first);
            Object b = data.get(first + (last - first) / 2);
            Object c = data.get(last - 1);
//System.out.println("a: " + a + ", b: " + b + ", c: " + c + ", indx b: " + (first + (last - first) / 2));
            if (comparator.execute(a, b)) {
                if (comparator.execute(b, c))
                    pivot = b;
                else if (comparator.execute(a, c ))
                    pivot = c;
                else
                    pivot = a;
            }else if (comparator.execute(a, c))
                pivot = a;
            else if (comparator.execute(b, c))
                pivot = c;
            else
                pivot = b;
//System.out.println("pivot: " + pivot);
            int cut = first;
            int lastx = last;

            while (true) {
//System.out.println("cut: " + cut);
                while (comparator.execute(data.get(cut), pivot))
                    ++cut;
                --lastx;
//System.out.println("2. cut: " + cut + ", lastx: " + lastx);

                while (comparator.execute(pivot, data.get(lastx)))
                    --lastx;

//System.out.println("3. cut: " + cut + ", lastx: " + lastx);
                if (cut >= lastx)
                    break;

                Object tmp = data.get(cut);
                int tmp2 = 0;
                if (indices != null) {
                    tmp2 = indices.get(cut);
                    indices.set(cut, indices.get(lastx));
                }
                data.set(cut++, data.get(lastx));
                if (indices != null)
                    indices.set(lastx, tmp2);
                data.set(lastx, tmp);
/*System.out.print("data: ");
for (int i=0; i<data.size(); i++)
    System.out.print(data.get(i) + ", ");
System.out.print(", indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println();
*/
            }

            if ( cut - first >= last - cut ) {
                quickSortLoop(data, cut, last, comparator, indices);
                last = cut;
            }else{
                quickSortLoop(data, first, cut, comparator, indices);
                first = cut;
            }
        }
    }

    /** Sorts all the elements of <code>data<code> array. The <code>data</code> is sorted, after the method finishes
     *  execution.
     *  @param data The ObjectBaseArray to sort.
     *  @param comparator The ObjectComparator used to sort the array.
     *  @see #sort(ObjectBaseArray, int, int, ObjectComparator)
     */
    public static void sort(ObjectBaseArray data, ObjectComparator comparator) {
        sort(data, 0, data.size()-1, comparator);
    }
    /** Sorts the elements of <code>data<code> array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code> using the specified comparator. The <code>data</code> is sorted, after the method finishes
     *  execution.
     *  @param data The ObjectBaseArray to sort.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The ObjectComparator used to sort the array.
     */
    public static void sort(ObjectBaseArray data, int first, int last, ObjectComparator comparator) {
        // calculate first and last index into the sequence.
        int start = first;
        int finish = last + 1; //distance(base.listIterator(), last)+1;
        quickSortLoop(data, start, finish, comparator, null);
        finalInsertionSort(data, start, finish, comparator, null);
    }

    /** Sorts the elements of <code>data<code> array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code> using the specified comparator. The <code>data</code> is sorted, after the method finishes
     *  execution. The method returns an array with the new order of the elements inside <code>data</code>.
     *  @param data The ObjectBaseArray to sort.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     */
    public IntBaseArray getOrder(ObjectBaseArray data, int first, int last, ObjectComparator comparator) {
        IntBaseArray indices = new IntBaseArray(last-first+1);
        for (int i=first; i<=last; i++)
            indices.add(i);

        // calculate first and last index into the sequence.
        int start = first   ;
        int finish = last + 1; //distance(base.listIterator(), last)+1;
//        int start = (base.start()).distance( first );
//        int finish = (base.start()).distance( last );

        quickSortLoop(data, start, finish, comparator, indices);
        finalInsertionSort(data, start, finish, comparator, indices);
        return indices;
    }

    /** Sorts all the cells of this field using the specified comparator. The order of the data of the field,
     *  does not change. The method returns an array with the order the cells of the field would have, when sorted by the
     *  <code>comparator</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     */
    public abstract IntBaseArray getOrder(ObjectComparator comparator);

    /** Sorts the cells of this field between <code>first</code> and <code>last</code> using the specified comparator.
     *  The order of the data of the field, does not change. The method returns an array with the order the cells of
     *  the field would have, when sorted by the <code>comparator</code>.
     *  @param first The index of the first record.
     *  @param last The index of the second record.
     *  @param comparator The Comparator used to sort the array.
     *  @return The order the cells between <code>first</code> and <code>last</code> would have, when sorted by the
     *  specified <code>comparator</code>.
     */
    public abstract IntBaseArray getOrder(int first, int last, ObjectComparator comparator);

    /**
     * Find the first element in a sequence that satisfies a query. The search starts at the current position of the
     * <code>iterator</code>. After the search finishes the iterator is positioned at the first occurence of the
     * requested operand, or after the end of the list of the records it looks into if no match was found.
     * The time complexity is linear and the space complexity is constant.
     * @param iterator An iterator positioned at the first element of the sequence.
     * @param operandOperator The query.
     */
    public abstract void findNext(FieldDataIterator iterator, SecondPredicateComparator operandOperator);

    /** Creates an iterator on the field's data. Each AbstractTableField sub-class defines it's own iterator class. The iterator
     *  passes through all the data of the field.
     *  @return The iterator.
     */
    public abstract FieldDataIterator iterator();

    /** Creates an iterator on the field's data. Each AbstractTableField sub-class defines it's own iterator class. The iterator
     *  will go through all the data of the field and will be positioned at <code>startIndex</code>.
     *  @return The iterator.
     */
    public abstract FieldDataIterator iterator(int firstIndex);

    /** Creates an iterator on the field's data. Each AbstractTableField sub-class defines it's own iterator class. The iterator
     *  will go through only the field data of the records specified by <code>recIndices</code> and it will begin the
     *  iteration from the record at <code>firstIndex</code> in the <code>recIndices</code> array.
     *  @param recIndices The indices of the records on which the iteration will take place.
     *  @param firstIndex The index of the record in the <code>recIndices</code> array, where the iteration will start from.
     *  @return The iterator.
     */
    public abstract FieldDataIterator iterator(IntBaseArrayDesc recIndices, int firstIndex);

    public abstract boolean setCell(int recIndex, Object value) throws AttributeLockedException, InvalidCellAddressException,
    InvalidDataFormatException, NullTableKeyException, DuplicateKeyException;

    public abstract Object getCellObject(int recIndex) throws InvalidCellAddressException;

    abstract Object getObjectAt(int recIndex);

    abstract void set(int recIndex, Object value);

    abstract void add(Object obj);

    abstract void remove(int recIndex);

    abstract int size();

    public abstract ArrayBase getData();

    public abstract int indexOf(int start, int end, Object value);

    public abstract int indexOf(Object value);

    public abstract Object[] getDataArray();

    public abstract ObjectBaseArray getDataBaseArray();

    public abstract ArrayList getDataArrayList();

    abstract void setData(ArrayList al);

    /** Returns the comparator class which corresponds to the comparison denoted by <code>comparatorStr</code>.
     */
    public abstract ObjectComparator getComparatorFor(String comparatorStr);

    /** Returns the comparators supported by the AbstractTableField.
     */
    public static HashMap getComparators() {return null;}

    /** Compares the cell at <code>recIndex</code> of this field to the cell at <code>recIndex2</code> of AbstractTableField
     *  <code>f</code> using the specified comparator.
     *  @param recIndex The record index of the cell of this field.
     *  @param comparator The comparator.
     *  @param f The second field.
     *  @param recIndex2 The record index of the cell of the second field.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparatorFor(String)
     */
    abstract boolean compareTo(int recIndex, ObjectComparator comparator, AbstractTableField f, int recIndex2);

    /** Compares the value at <code>recIndex</code> of the AbstractTableField to the given <code>value</code> using the
     *  specified <code>comparator</code>.
     *  @param recIndex The record index of the data of the field, which will be compared against <code>value</code>.
     *  @param comparator The comparator used.
     *  @param value The second operand of the operation.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparatorFor(String)
     */
    public abstract boolean compareTo(int recIndex, ObjectComparator comparator, Object value);

    /** Determines if the field's cell at <code>recordIndex</code> is <code>null</code>. AbstractTableField sub-classes which
     *  store primitive data types, define their own value for <code>null</code> cells.
     */
    public abstract boolean isCellNull(int recordIndex);

    abstract void setTable(Table table);

    /** Sets the value of the field for the new record which is being added to the Table.
     */
    abstract void setNewCellValue(Object value);

    /** Returns the value of the field for the new record which is being added to the Table.
     */
    abstract Object getNewCellValue();
    /** Returns the value of the field for the new record which is being added to the Table as a String.
     */
    abstract String getNewCellValueAsString();
    /** The value of this field which has been set while adding a new record is commited to the field, i.e. it
     *  is added to the field's <code>data</code> array.
     */
    abstract void commitNewCellValue();
}
