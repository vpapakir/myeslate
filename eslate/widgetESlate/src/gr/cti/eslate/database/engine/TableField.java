package gr.cti.eslate.database.engine;

import java.net.URL;
import java.awt.Image;
import java.util.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.objectspace.jgl.Array;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.database.view.FieldView;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.ObjectBaseArray;

/**
 * @version 2.0, May 01
 */
public class TableField implements Externalizable {
    /** The version of the storage format of the TableField class
     */
//    public static final String STR_FORMAT_VERSION = "1.0";
    // 2 --> 3 'dependsOnFields' was converted to IntBaseArray from Array.
	//         'dependentCalcFields' was alaso conveted to IntBaseArray from Array.
	//         'index' was added to the class and is stored.
    public static final int FORMAT_VERSION = 3;
    public static final int ASCENDING = 1;
    public static final int DESCENDING = 2;
    static final int stlThreshold = 16;


    /** The Java class to which the field's data type is mapped.
     */
    protected Class oldFieldType;
    /** @deprecated  As of version 1.5 of the TableField class (or version 1.7.3 of
     * the Database Engine) this field is no longer neeed. The <i>fieldType</i> variable
     * returns <i>gr.cti.eslate.database.engine.CDate</i> for Date fields and
     * <i>gr.cti.eslate.database.engine.CTime</i> for Time fields.
     * Distinguishes date from time fields. These two field data types are mapped to the
     *  same underlying Java class (java.util.Date).
     */
    private boolean date = false;
//    public Object inst;
    /** The name of the field.
     */
    String fieldName;
    /** Specifies if the field is editable.
     */
    boolean isEditable;
    /** Specifies if the field is part of the table's key.
     */
    boolean isKey;
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
    private String formula = null;
    /** The field's formula, if it is calculated.
     */
    protected String textFormula = null;
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
    public Array UIProperties = new Array();
    private boolean fieldEditabilityChangeAllowed = true;
    private boolean fieldRemovabilityChangeAllowed = true;
    private boolean fieldDataTypeChangeAllowed = true;
    private boolean calcFieldResetAllowed = false;
    private boolean fieldKeyAttributeChangeAllowed = true;
    private boolean fieldHiddenAttributeChangeAllowed = true;
    private boolean calcFieldFormulaChangeAllowed = false;
    boolean linkToExternalData = false;

    /** Contains all the UI settings of the TableField. This class gives the TableField the
     *  ability to 'carry' the settings which adjust its view in a Database component.
     */
    FieldView fieldView = null;
    static final long serialVersionUID = 12;

    boolean sorted = false;
    int sortDirection = ASCENDING;
	/**
	 * The index of the field in the Table. This does not change once the field
	 * is added to the Table.
	 */
	int index = -1;
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

    // Should be sbstract
    public static Class getDataType() {return null;}

    /**
     *  Returns the field's <i>date</i> attribute.
     *  @deprecated  As of version 1.5 of the TableField class (or version 1.7.3 of
     *  the Database Engine) the <i>date</i> field  is no longer neeed. The <i>fieldType</i> field
     *  returns <i>gr.cti.eslate.database.engine.CDate</i> for Date fields and
     *  <i>gr.cti.eslate.database.engine.CTime</i> for Time fields.
     *  @see #date
     */
    public boolean isDate() {
        return date;
    }


    /**
     *  @deprecated Sets the field's <i>date</i> attribute.
     *  @see #date
     */
    protected void setDate(boolean d) {
       date = d;
    }


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
    public final boolean isKey() {
        return isKey;
    }


    /**
     *  Sets the fields <i>isKey</i> attribute.
     */
    protected void setKey(boolean key) throws InvalidKeyFieldException, AttributeLockedException {
        if (!fieldKeyAttributeChangeAllowed)
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg1"));

        if (key && getDataType().equals(CImageIcon.class))
            throw new InvalidKeyFieldException(Table.bundle.getString("CTableMsg67"));
        isKey = key;
    }


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
    public boolean equals(TableField f) {
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
    public boolean hasEqualType(TableField f) {
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
     *  @param  form The formula of the field.
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

    // Added while transitioning to AbstractTableField
    final String getFormula() {
        return formula;
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


//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
		int dataVersion = fieldMap.getDataVersionID();
        oldFieldType = (Class) fieldMap.get("Field type");
        date = fieldMap.get("Is date field", true);
        fieldName = (String) fieldMap.get("Field name");
//System.out.println("Restoring field: " + fieldName + ", of type: " + fieldType);
        isEditable = fieldMap.get("Editable", true);
        isKey = fieldMap.get("Key", false);
        isRemovable = fieldMap.get("Removable", true);
        isCalculated = fieldMap.get("Calculated", false);
        formula = fieldMap.get("Formula", (String) null);
        textFormula = fieldMap.get("Text formula", (String) null);
		if (dataVersion >= 3) {
			dependsOnFields = (IntBaseArray) fieldMap.get("Depends on fields");
			dependentCalcFields = (IntBaseArray) fieldMap.get("Dependent fields");
		}else{
			dependsOnFields = Table.convertArrayToIntBaseArray((Array) fieldMap.get("Depends on fields"));
			dependentCalcFields = Table.convertArrayToIntBaseArray((Array) fieldMap.get("Dependent fields"));
		}
        calculatedValue = (Object) fieldMap.get("Calculated value");
        UIProperties = (Array) fieldMap.get("UIProperties");

        if (oldFieldType.getName().equals("java.lang.Integer")) {
            Object o = null;
            try{
//                    System.out.println("Reading o");
                o = (Object) fieldMap.get("Operand expression");
//                    System.out.println("o: " + o.getClass());
                oe = (NumericOperandExpression) o;
            }catch (Exception e1) {
                try{
                    oe = (DateOperandExpression) o;
                }catch (Exception e2) {throw new IOException();}
            }
        }else if (oldFieldType.getName().equals("java.lang.Double")) {
            oe = (NumericOperandExpression) fieldMap.get("Operand expression");
//                System.out.println("Field type: " + fieldType + ", oe: " + oe);
        }else if (oldFieldType.getName().equals("java.lang.String") || oldFieldType.getName().equals("java.net.URL"))
            oe = (StringOperandExpression) fieldMap.get("Operand expression");
        else if (oldFieldType.getName().equals("java.lang.Boolean"))
            oe = (BooleanOperandExpression) fieldMap.get("Operand expression");
        else if (java.util.Date.class.isAssignableFrom(oldFieldType)) // fieldType.getName().equals("java.util.Date"))
            oe = (DateOperandExpression) fieldMap.get("Operand expression");

        isHidden = fieldMap.get("Hidden", false);
        fieldEditabilityChangeAllowed = fieldMap.get("Field editability change allowed", true);
        fieldRemovabilityChangeAllowed = fieldMap.get("Field removability change allowed", true);
        fieldDataTypeChangeAllowed = fieldMap.get("Field data type change allowed", true);
        calcFieldResetAllowed = fieldMap.get("Calc field reset allowed", isCalculated);
        fieldKeyAttributeChangeAllowed = fieldMap.get("Field key attrib change allowed", true);
        fieldHiddenAttributeChangeAllowed = fieldMap.get("Field hidden attrib change allowed", true);
        calcFieldFormulaChangeAllowed = fieldMap.get("Calc field formula change allowed", isCalculated);
        linkToExternalData = fieldMap.get("Link to external data", false);
        fieldView = (FieldView) fieldMap.get("Field View");
		index = fieldMap.get("Field Index", -1);
    }


    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Field type", oldFieldType);
        fieldMap.put("Is date field", date);
        fieldMap.put("Field name", fieldName);
        fieldMap.put("Editable", isEditable);
        fieldMap.put("Key", isKey);
        fieldMap.put("Removable", isRemovable);
        fieldMap.put("Calculated", isCalculated);
        fieldMap.put("Formula", formula);
        fieldMap.put("Text formula", textFormula);
        fieldMap.put("Depends on fields", dependsOnFields);
        fieldMap.put("Dependent fields", dependentCalcFields);
        fieldMap.put("Calculated value", calculatedValue);
        fieldMap.put("UIProperties", UIProperties);

        if (oldFieldType.getName().equals("java.lang.Integer") || oldFieldType.getName().equals("java.lang.Double")) {
            fieldMap.put("Operand expression", oe);
        }else if (oldFieldType.getName().equals("java.lang.String") || oldFieldType.getName().equals("java.net.URL")) {
            fieldMap.put("Operand expression", (StringOperandExpression) oe);
        }else if (oldFieldType.getName().equals("java.lang.Boolean")) {
            fieldMap.put("Operand expression", (BooleanOperandExpression) oe);
        }else if (Date.class.isAssignableFrom(oldFieldType)) {//fieldType.getName().equals("java.util.Date")) {
            fieldMap.put("Operand expression", (DateOperandExpression) oe);
        }

        fieldMap.put("Hidden", isHidden);
        fieldMap.put("Field editability change allowed", fieldEditabilityChangeAllowed);
        fieldMap.put("Field removability change allowed", fieldRemovabilityChangeAllowed);
        fieldMap.put("Field data type change allowed", fieldDataTypeChangeAllowed);
        fieldMap.put("Calc field reset allowed", calcFieldResetAllowed);
        fieldMap.put("Field key attrib change allowed", fieldKeyAttributeChangeAllowed);
        fieldMap.put("Field hidden attrib change allowed", fieldHiddenAttributeChangeAllowed);
        fieldMap.put("Calc field formula change allowed", calcFieldFormulaChangeAllowed);
        fieldMap.put("Link to external data", linkToExternalData);
		fieldMap.put("Field Index", index);

        fieldMap.put("Field View", fieldView);
        out.writeObject(fieldMap);
    }


    public final static String localizedNameForDataType(Class dataType) {
        String fldType = "";
        if (dataType.equals(java.lang.Integer.class))
            fldType = DBase.resources.getString("Integer");
        else if (dataType.equals(java.lang.String.class))
            fldType = DBase.resources.getString("Alphanumeric");
        else if (dataType.equals(java.lang.Double.class))
            fldType = DBase.resources.getString("Number");
        else if (dataType.equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = DBase.resources.getString("Image");
        else if (dataType.equals(java.net.URL.class))
            fldType = DBase.resources.getString("URL");
        else if (dataType.equals(java.lang.Boolean.class))
            fldType = DBase.resources.getString("Boolean");
        else if (dataType.equals(CDate.class)) //java.util.Date.class)) {
//t            if (f.isDate())
            fldType = DBase.resources.getString("Date");
        else if (dataType.equals(CTime.class)) //java.util.Date.class)) {
            fldType = DBase.resources.getString("Time");

        return fldType;
    }

    public final static String localizedNameForDataType(TableField f) {
        return localizedNameForDataType(f.getDataType());
    }

    /** @deprecated Replaced by <i>getInternalDataTypeName(Class fieldType)</i>
     */
    public final static String getInternalDataTypeName(Class fieldType, boolean isDate) {
        return getInternalDataTypeName(fieldType);
    }

    public final static String getInternalDataTypeName(Class fieldType) {
        String fldType = "";
        if (fieldType.equals(java.lang.Integer.class))
            fldType = "Integer";
        else if (fieldType.equals(java.lang.String.class))
            fldType = "String";
        else if (fieldType.equals(java.lang.Double.class))
            fldType = "Double";
        else if (fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = "Image";
        else if (fieldType.equals(java.net.URL.class))
            fldType = "URL";
        else if (fieldType.equals(java.lang.Boolean.class))
            fldType = "Boolean";
        else if (fieldType.equals(CDate.class)) //java.util.Date.class)) {
//t            if (isDate)
            fldType = "Date";
        else if (fieldType.equals(CTime.class)) //java.util.Date.class)) {
            fldType = "Time";

        return fldType;
    }

	public static final Class getInternalDataType(String typeName) {
		Class fldType = null;
		typeName = typeName.toLowerCase();
		if (typeName.equals("integer") || typeName.equals(DBase.resources.getString("Integer").toLowerCase()))
			fldType = java.lang.Integer.class;
		else if (typeName.equals("string") || typeName.equals(DBase.resources.getString("Alphanumeric").toLowerCase()))
			fldType = java.lang.String.class;
		else if (typeName.equals("double") || typeName.equals(DBase.resources.getString("Number").toLowerCase()))
			fldType = java.lang.Double.class;
		else if (typeName.equals("image") || typeName.equals(DBase.resources.getString("Image").toLowerCase()))
			fldType = gr.cti.eslate.database.engine.CImageIcon.class;
		else if (typeName.equals("url") || typeName.equals(DBase.resources.getString("URL").toLowerCase()))
			fldType = java.net.URL.class;
		else if (typeName.equals("boolean") || typeName.equals(DBase.resources.getString("Boolean").toLowerCase()))
			fldType = java.lang.Boolean.class;
		else if (typeName.equals("date") || typeName.equals(DBase.resources.getString("Date").toLowerCase()))
			fldType = CDate.class;
		else if (typeName.equals("time") || typeName.equals(DBase.resources.getString("Time").toLowerCase()))
			fldType = CTime.class;

		return fldType;

	}

    public final static String getInternalDataTypeName(TableField field) {
        String fldType = "";
        if (field.getDataType().equals(java.lang.Integer.class))
            fldType = "Integer";
        else if (field.getDataType().equals(java.lang.String.class))
            fldType = "String";
        else if (field.getDataType().equals(java.lang.Double.class))
            fldType = "Double";
        else if (field.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = "Image";
        else if (field.getDataType().equals(java.net.URL.class))
            fldType = "URL";
        else if (field.getDataType().equals(java.lang.Boolean.class))
            fldType = "Boolean";
        else if (field.getDataType().equals(CDate.class)) //java.util.Date.class)) {
//t            if (field.isDate())
            fldType = "Date";
        else if (field.getDataType().equals(CTime.class)) //java.util.Date.class)) {
            fldType = "Time";

        return fldType;
    }

	public int getFieldIndex() {
		return index;
	}

    public void setFieldView(FieldView view) {
        fieldView = view;
    }

    public FieldView getFieldView() {
        return fieldView;
    }

    /** Returns if the Table this field belongs to is sorted on this field.
     */
    public boolean isSorted() {
        return sorted;
    }

    /** Return the direction of the sorting which may have happened on this field.
     *  The direction is valid only while <code>isSorted()</code> returns true.
     *  @returns One of <code>TableField.ASCENDING</code>, <code>TableField.DESCENDING</code>.
     */
    public int getSortDirection() {
        return sortDirection;
    }

/*    protected TableField(String colName, String typeName, boolean isEdit, boolean Key, boolean isRemov, boolean isHidden)
	throws InvalidFieldTypeException {*/
	/** Returns a new field of the type specified by the <code>fieldType</code> parameter.
	 * @param  name        The name of the new field.
	 * @param  fieldType   The type of the field, expressed a a Java class. The class of the new field
	 * will be one of the following, depending on the value of this parameter:
	 * <ul>
	 * <li> StringTableField, if <code>fieldType</code> is String.class.
	 * <li> DoubleTableField, if <code>fieldType</code> is Double.class.
	 * <li> IntegerTableField, if <code>fieldType</code> is Integer.class.
	 * <li> BooleanTableField, if <code>fieldType</code> is Boolean.class.
	 * <li> DateTableField, if <code>fieldType</code> is gr.cti.eslate.database.engine.CDate.class, or a sub-class.
	 * <li> TimeTableField, if <code>fieldType</code> is gr.cti.eslate.database.engine.CTime.class, or a sub-class.
	 * <li> URLTableField, if <code>fieldType</code> is java.net.URL.class.
	 * <li> ImageTableField, if <code>fieldType</code> is gr.cti.eslate.database.engine.CImageIcon.class, or a sub-class.
	 * </ul>
	 * @param  editable    Determines if the attributes of the new field will be editable.
	 * @param  key         Determines if the new field will have it's key flag set.
	 * @param  removable   Determines if the new field can be deleted.
	 * @param  hidden      Determines if the new field is hidden.
	 * @exception InvalidFieldTypeException If <code>fieldType</code> is not one of the supported types.
	 */
/*	static TableField createNewField(Table table, String name, Class fieldType, boolean editable, boolean key, boolean removable, boolean hidden) throws InvalidFieldTypeException {
        if (table == null)
            throw new NullPointerException();
		TableField fld = null;
		if (String.class.isAssignableFrom(fieldType)) {      //string
			fld = new StringTableField(table, name, editable, key, removable, hidden);
		}else if (Double.class.isAssignableFrom(fieldType)) {//double
			fld = new DoubleTableField(table, name, editable, key, removable, hidden);
		}else if (Integer.class.isAssignableFrom(fieldType)) { //integer
			fld = new IntegerTableField(table, name, editable, key, removable, hidden);
		}else if (Boolean.class.isAssignableFrom(fieldType)) {  //boolean
			fld = new BooleanTableField(table, name, editable, key, removable, hidden);
		}else if (CDate.class.isAssignableFrom(fieldType)) {    //date
			fld = new DateTableField(table, name, editable, key, removable, hidden);
		}else if (CTime.class.isAssignableFrom(fieldType)) {    //time
			fld = new TimeTableField(table, name, editable, key, removable, hidden);
		}else if (URL.class.isAssignableFrom(fieldType)) {    //url
			fld = new URLTableField(table, name, editable, key, removable, hidden);
		}else if (CImageIcon.class.isAssignableFrom(fieldType)) {  //image
			fld = new ImageTableField(table, name, editable, key, removable, hidden);
		}else{
			throw new InvalidFieldTypeException(". " + Table.bundle.getString("CTableMsg77") + fieldType.getName() + "\"");
		}
//        fld.table = table;
		return fld;
	}

	static TableField createNewField(Table table, CTableField f) {
		TableField field = null;
		try{
			field = createNewField(table, f.getName(), f.getFieldType(), f.isEditable(), f.isKey(), f.isRemovable(), f.isHidden());
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
		field.UIProperties = f.UIProperties;
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

    static TableField createProperSubClassField(Table table, TableField f) {
		TableField field = null;
		try{
			field = createNewField(table, f.getName(), f.oldFieldType, f.isEditable(), f.isKey(), f.isRemovable(), f.isHidden());
		}catch (InvalidFieldTypeException exc) {
			System.out.println("Converting a CTableField to TableField. This exception should never occur");
			exc.printStackTrace();
		}
		field.isCalculated = f.isCalculated();
		field.formula = f.formula;
		field.textFormula = f.textFormula;
		field.dependsOnFields = f.getDependsOnFields();
		field.dependentCalcFields = f.getDependentCalcFields();
		field.calculatedValue = f.calculatedValue;
		field.UIProperties = f.UIProperties;
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
*/
    /** Returns the class of the field whic stores data of the specified <code>dataType</code>.
     *  @param dataType The java data type.
     *  @return The class of the TableField subclass, which stores values of type <code>dataType</code>.
     */
/*    public static Class getFieldClassForDataType(Class dataType) {
        if (Integer.class.isAssignableFrom(dataType))
            return IntegerTableField.class;
        else if (Double.class.isAssignableFrom(dataType))
            return DoubleTableField.class;
        else if (Boolean.class.isAssignableFrom(dataType))
            return BooleanTableField.class;
        else if (String.class.isAssignableFrom(dataType))
            return StringTableField.class;
        else if (CDate.class.isAssignableFrom(dataType))
            return DateTableField.class;
        else if (CTime.class.isAssignableFrom(dataType))
            return TimeTableField.class;
        else if (URL.class.isAssignableFrom(dataType))
            return URLTableField.class;
        else if (CImageIcon.class.isAssignableFrom(dataType))
            return ImageTableField.class;
        return null;
    }
*/
    /** Returns the comparators supported by the TableField of class <code>fieldType</code>.
     */
/*    public static Hashtable getComparatorsForFieldType(Class fieldType) {
        if (IntegerTableField.class.equals(fieldType))
            return IntegerTableField.getComparators();
        else if (DoubleTableField.class.equals(fieldType))
            return DoubleTableField.getComparators();
        else if (BooleanTableField.class.equals(fieldType))
            return BooleanTableField.getComparators();
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
*/

    /** Updates the calculated fields which depend on this field. This method is called after the value of a cell of this
     *  field changes. The method accepts as parameter the previous value of the cell. If while updating the calculated
     *  fields an error occurs, the value of the cell is reset to its previous value and the dependent calculated
     *  fields are re-calculated.
     *  @param recIndex       The index og the cell of this field that changed value.
     *  @param previousValue  The previous value of the cell.
     *  @exception  DuplicateKeyException If the new value of a dependent calculated field, which is also part of the
     *                                    Table's key, violates the key uniqueness.
     *  @exception  NullTableKeyException If the new value of a dependent calculated field, which is also part of the
     *                                    Table's key, is null.
     */
/*    void updateDependentCalculatedFields(int recIndex) throws DuplicateKeyException, NullTableKeyException {
        if (!(dependentCalcFields.size() == 0)) {
            for (int i=0; i<dependentCalcFields.size(); i++) {
                table.evaluateCalculatedFieldCell(dependentCalcFields.get(i), recIndex);

                TableField f1 = (TableField) table.tableFields.get(dependentCalcFields.get(i));
                table.changedCalcCells.add(f1.getName(), new Integer(recIndex));
            }
            table.calculatedFieldsChanged = true;
        }
    }

    static final SecondPredicateComparator createSecondOperandOperator(TableField f, String operator, Object secondOperand) {
        SecondPredicateComparator predicate = null;
        if (f.getDataType().equals(String.class))
            predicate = new StringSecondPredicateComparator(((StringTableField) f).getComparator(operator), (String) secondOperand);
        else if (f.getDataType().equals(Integer.class))
            predicate = new IntegerSecondPredicateComparator(((IntegerTableField) f).getComparator(operator), (Integer) secondOperand);
        else if (f.getDataType().equals(Double.class))
            predicate = new DoubleSecondPredicateComparator(((DoubleTableField) f).getComparator(operator), (Double) secondOperand);
        else if (f.getDataType().equals(Boolean.class))
            predicate = new BooleanSecondPredicateComparator(((BooleanTableField) f).getComparator(operator), (Boolean) secondOperand);
        else if (f.getDataType().equals(CDate.class))
            predicate = new DateSecondPredicateComparator(((DateTableField) f).getComparator(operator), (CDate) secondOperand);
        else if (f.getDataType().equals(CTime.class))
            predicate = new TimeSecondPredicateComparator(((TimeTableField) f).getComparator(operator), (CTime) secondOperand);
        else if (f.getDataType().equals(URL.class))
            predicate = new StringSecondPredicateComparator(((URLTableField) f).getComparator(operator), ((URL) secondOperand).toString());
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

            for (int i = limit; i < last; i++)
                unguardedLinearInsert(data, i, data.get(i), comparator, indices, 0);
        }else{
            for (int i = first + 1; i < last; i++)
                linearInsert(data, first, i, comparator, indices);
        }
    }

    // Sorting utility method
    static void unguardedLinearInsert(ObjectBaseArray data, int last, Object value, ObjectComparator comparator, IntBaseArray indices, int index) {
        int next = last - 1;

        if (indices == null) {
            while (comparator.execute(value, data.get(next )))
                data.set(last--, data.get(next--));
        }else{
            while (comparator.execute(value, data.get(next ))) {
              indices.set(last, indices.get(next));
              data.set(last--, data.get( next--));
            }
        }

        data.set(last, value);
        if (indices != null)
            indices.set(last, index);
    }

    // Sorting utility method
    static void linearInsert(ObjectBaseArray data, int first, int last, ObjectComparator comparator, IntBaseArray indices) {
        Object value = data.get(last);
        int index = 0;
        if (indices != null)
            index = indices.get(last);

        if (comparator.execute(value, data.get( first ))) {
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
        while (last - first > stlThreshold) {
            Object pivot;
            Object a = data.get(first);
            Object b = data.get(first + (last - first) / 2);
            Object c = data.get(last - 1);

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

            int cut = first;
            int lastx = last;

            while (true) {
                while (comparator.execute(data.get(cut), pivot))
                    ++cut;
                --lastx;

                while (comparator.execute(pivot, data.get(lastx)))
                    --lastx;

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
*/
    /** Sorts all the elements of <code>data<code> array. The <code>data</code> is sorted, after the method finishes
     *  execution.
     *  @param data The ObjectBaseArray to sort.
     *  @param comparator The ObjectComparator used to sort the array.
     *  @see #sort(ObjectBaseArray, int, int, ObjectComparator)
     */
/*    public static void sort(ObjectBaseArray data, ObjectComparator comparator) {
        sort(data, 0, data.size()-1, comparator);
    }
*/
    /** Sorts the elements of <code>data<code> array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code> using the specified comparator. The <code>data</code> is sorted, after the method finishes
     *  execution.
     *  @param data The ObjectBaseArray to sort.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The ObjectComparator used to sort the array.
     */
/*    public static void sort(ObjectBaseArray data, int first, int last, ObjectComparator comparator) {
        // calculate first and last index into the sequence.
        int start = first;
        int finish = last + 1; //distance(base.listIterator(), last)+1;
        quickSortLoop(data, start, finish, comparator, null);
        finalInsertionSort(data, start, finish, comparator, null);
    }
*/
    // Should be abstract
    /** Sorts all the cells of this field using the specified comparator. The order of the data of the field,
     *  does not change. The method returns an array with the order the cells of the field would have, when sorted by the
     *  <code>comparator</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     *  @see #getOrder(int, int, ImageComparator)
     */
//    public IntBaseArray getOrder(ObjectComparator comparator) {return null;}

    // Should be abstract
    /**
     * Find the first element in a sequence that satisfies a query.
     * The time complexity is linear and the space complexity is constant.
     * @param first An iterator positioned at the first element of the sequence.
     * @param last An iterator positioned immediately after the last element of the sequence.
     * @param operandOperator The query.
     * @return An iterator positioned at the first element that matches. If no match is
     * found, return an iterator positioned immediately after the last element of
     * the sequence.
     */
//    public static FieldDataIterator findNext(FieldDataIterator first, FieldDataIterator last, SecondPredicateComparator operandOperator) {return null;}

    // Should be abstract
    /** Creates an iterator on the field's data. Each TableField sub-class defines it's own iterator class. The iterator
     *  passes through all the data of the field.
     *  @return The iterator.
     */
//    public FieldDataIterator iterator() {return null;}

    // Should be abstract
    /** Creates an iterator on the field's data. Each TableField sub-class defines it's own iterator class. The iterator
     *  will go through all the data of the field and will be positioned at <code>startIndex</code>.
     *  @return The iterator.
     */
//    public FieldDataIterator iterator(int firstIndex) {return null;}

    // Should be abstract
    /** Creates an iterator on the field's data. Each TableField sub-class defines it's own iterator class. The iterator
     *  will go through only the field data of the records specified by <code>recIndices</code> and it will begin the
     *  iteration from the record at <code>firstIndex</code> in the <code>recIndices</code> array.
     *  @param recIndices The indices of the records on which the iteration will take place.
     *  @param firstIndex The index of the record in the <code>recIndices</code> array, where the iteration will start from.
     *  @return The iterator.
     */
//    public FieldDataIterator iterator(IntBaseArrayDesc recIndices, int firstIndex) {return null;}

    // Should be abstract
//    public boolean setCell(int recIndex, Object value) throws AttributeLockedException, InvalidCellAddressException,
//    InvalidDataFormatException, NullTableKeyException, DuplicateKeyException {return false;}

    // Should be abstract
//    public Object getCellObject(int recIndex) throws InvalidCellAddressException {return null;}

    // Should be declared abstract
//    Object getObjectAt(int recIndex) {return null;}

    // Should be abstract
//    void set(int recIndex, Object value) {}

    // Should be abstract
//    void add(Object obj) {}

    // Should be abstract
//    void remove(int recIndex) {}

    // Should be abstract
//    int size() {return 0;}

    // Should be abstract
//    public ArrayBase getData() {return null;}

    // Should be abstract
//    public int indexOf(int start, int end, Object value) {return -1;}

    // Should be abstract
//    public int indexOf(Object value) {return -1;}

    // Should be abstract
//    public Object[] getDataArray() {return null;}

    // Should be abstract
//    public ObjectBaseArray getDataBaseArray() {return null;}

    // Should be abstract
//    public ArrayList getDataArrayList() {return null;}

    // Should be abstract
//    void setData(ArrayList al) {}

    // Should be abstract
    /** Returns the comparator class which corresponds to the comparison denoted by <code>comparatorStr</code>.
     */
//    public static ObjectComparator getComparatorFor(String comparatorStr) {return null;}

    // Should be abstract
    /** Returns the comparators supported by the TableField.
     */
//    public static Hashtable getComparators() {return null;}

    // Should be abstract
    /** Compares the cell at <code>recIndex</code> of this field to the cell at <code>recIndex2</code> of TableField
     *  <code>f</code> using the specified comparator.
     *  @param recIndex The record index of the cell of this field.
     *  @param comparator The comparator.
     *  @param f The second field.
     *  @param recIndex2 The record index of the cell of the second field.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparatorFor(String)
     */
//    boolean compareTo(int recIndex, ObjectComparator comparator, TableField f, int recIndex2) {return false;}

    // Should be abstract
    /** Compares the value at <code>recIndex</code> of the TableField to the given <code>value</code> using the
     *  specified <code>comparator</code>.
     *  @param recIndex The record index of the data of the field, which will be compared against <code>value</code>.
     *  @param comparator The comparator used.
     *  @param value The second operand of the operation.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparatorFor(String)
     */
//    public boolean compareTo(int recIndex, ObjectComparator comparator, Object value) {return false;}

    // Should be abstract
    /** Determines if the argument is the <code>null</code> value of the TableField. TableField sub-classes which
     *  store primitive data types, define their own value for <code>null</code> cells.
     */
//    public static boolean isNull(Object obj) {return false;}
}

