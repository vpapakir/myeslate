package gr.cti.eslate.database.engine;

import java.net.URL;
import java.awt.Image;
import java.util.Date;
import com.objectspace.jgl.Array;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.ESlateFieldMap;

/**
 * @version	1.5, 1-Nov-98
 */
/**
 * @deprecated  As of DBEngine version 1.9, replaced by gr.cti.eslate.database.engine.TableField.
 */
class CTableField implements Serializable {
    /** The version of the storage format of the CTableField class
     */
    public static final String STR_FORMAT_VERSION = "1.0";

    /** The Java class to which the field's data type is mapped.
     */
    protected Class fieldType;
    /** @deprecated  As of version 1.5 of the CTableField class (or version 1.7.3 of
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
    private String fieldName;
    /** Specifies if the field is editable.
     */
    private boolean isEditable;
    /** Specifies if the field is part of the table's key.
     */
    private boolean isKey;
    /** Specifies if the field is removable.
     */
    private boolean isRemovable;
    /** Specifies if the field is calculated.
     */
    private boolean isCalculated = false;
    /** Specified if the field is hidden.
     */
    private boolean isHidden = false;
    /** The field's formula in internal format, if it is calculated.
     */
    protected String formula = null;
    /** The field's formula, if it is calculated.
     */
    protected String textFormula = null;
    /** The list of the indices of the fields on which this field depends, if it is calculated.
     */
    /** The list of the indices of the fields on which this field depends, if it is calculated.
     */
    protected Array dependsOnFields = null;
    /** The list of the indices of the calculated fields, which depend on this field.
     */
    protected Array dependentCalcFields = new Array();
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
    private boolean linkToExternalData = false;

    static final long serialVersionUID = 12;


    /**
     *  Creates an new CTableField instance.
     *  @param  colName The name of the new field.
     *  @param  typeName One of <i>integer</i>, <i>double</i>, <i>string</i>, <i>boolean</i>, <i>date</i>, <i>time</i>, <i>url</i> and <i>image</i>.
     *  @param  isEdit Specifies if the new field is editable.
     *  @param  key    Specifies if the new field is part of the key.
     *  @param  isRemov Specifies if the new field is removable.
     *  @param  isHidden Specifies if the new field is hidden.
     *  @exception InvalidFieldTypeException If an invalid data type is supplied.
     */
    protected CTableField(String colName, String typeName, boolean isEdit, boolean Key, boolean isRemov, boolean isHidden)
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
            throw new InvalidFieldTypeException(". " + CDatabase.resources.getString("CTableMsg77") + typeName + "\"");
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


    /**
     *  Returns the field's <i>date</i> attribute.
     *  @deprecated  As of version 1.5 of the CTableField class (or version 1.7.3 of
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

        if (key && getFieldType().equals(CImageIcon.class))
            throw new InvalidKeyFieldException(CDatabase.resources.getString("CTableMsg67"));
        isKey = key;
    }


    /**
     *  Returns the field's data type.
     */
    public final Class getFieldType() {
        return fieldType;
    }


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
    protected void setFieldType(Class c) throws AttributeLockedException {
        if (!fieldDataTypeChangeAllowed)
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg2"));
        fieldType = c;
    }


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
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg4"));
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
            throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg6"));
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
    public boolean equals(CTableField f) {
        if (!f.getName().equals(fieldName))
            return false;
        if (!f.getFieldType().getName().equals(fieldType.getName()))
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
    public boolean hasEqualType(CTableField f) {
        if (!f.getFieldType().getName().equals(fieldType.getName()))
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
    protected void setCalculated(boolean isCalc, String formula, String textFormula, Array dependentFields)
    throws AttributeLockedException {
        if (isCalc) {
            if (isCalculated && !calcFieldFormulaChangeAllowed)
                throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg7"));
            this.formula = formula;
            this.textFormula = textFormula;
            dependsOnFields = dependentFields;
            calcFieldResetAllowed = true;
            calcFieldFormulaChangeAllowed = true;
        }else{
            if (!calcFieldResetAllowed)
                throw new AttributeLockedException(CDatabase.resources.getString("CTableFieldMsg5"));

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
    public final Array getDependsOnFields() {
        return dependsOnFields;
    }


    /**
     *  Returns the Array of the indices of the calculated fields, which depend on
     *  this field.
     */
    public final Array getDependentCalcFields() {
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
        if (fieldType.equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
            if (linkToExternalData != containsLinks)
                linkToExternalData = containsLinks;
        }
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!ESlateFieldMap.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
            fieldType = (Class) fieldMap.get("Field type");
            date = fieldMap.get("Is date field", true);
            if (fieldType.getName().equals("java.util.Date")) {
                if (date)
                    fieldType = CDate.class;
                else
                    fieldType = CTime.class;
            }
            fieldName = (String) fieldMap.get("Field name");
//            System.out.println("reading CTableField  ... " + fieldName + " of type: " + fieldType);
            isEditable = fieldMap.get("Editable", true);
            isKey = fieldMap.get("Key", false);
            isRemovable = fieldMap.get("Removable", true);
            isCalculated = fieldMap.get("Calculated", false);
            formula = fieldMap.get("Formula", (String) null);
            textFormula = fieldMap.get("Text formula", (String) null);
            dependsOnFields = (Array) fieldMap.get("Depends on fields");
            dependentCalcFields = (Array) fieldMap.get("Dependent fields");
            calculatedValue = (Object) fieldMap.get("Calculated value");
            UIProperties = (Array) fieldMap.get("UIProperties");

            if (fieldType.getName().equals("java.lang.Integer")) {
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
            }else if (fieldType.getName().equals("java.lang.Double")) {
                oe = (NumericOperandExpression) fieldMap.get("Operand expression");
//                System.out.println("Field type: " + fieldType + ", oe: " + oe);
            }else if (fieldType.getName().equals("java.lang.String") || fieldType.getName().equals("java.net.URL"))
                oe = (StringOperandExpression) fieldMap.get("Operand expression");
            else if (fieldType.getName().equals("java.lang.Boolean"))
                oe = (BooleanOperandExpression) fieldMap.get("Operand expression");
            else if (java.util.Date.class.isAssignableFrom(fieldType)) // fieldType.getName().equals("java.util.Date"))
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
        }
    }


//OptionalDataException
    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("CTableField oldReadObject");
        try{
//            in.defaultReadObject();
            fieldType = (Class) firstObj; //in.readObject();
            date = ((Boolean) in.readObject()).booleanValue();
            fieldName = (String) in.readObject();
//            System.out.println("reading CTableField  ... " + fieldName + " of type: " + fieldType);
            isEditable = ((Boolean) in.readObject()).booleanValue();
            isKey = ((Boolean) in.readObject()).booleanValue();
            isRemovable = ((Boolean) in.readObject()).booleanValue();
            isCalculated = ((Boolean) in.readObject()).booleanValue();
            formula = (String) in.readObject();
            textFormula = (String) in.readObject();
            dependsOnFields = (Array) in.readObject();
            dependentCalcFields = (Array) in.readObject();
            calculatedValue = (Object) in.readObject();
            UIProperties = (Array) in.readObject();

            if (fieldType.getName().equals("java.lang.Integer")) {
                Object o = null;
                try{
//                    System.out.println("Reading o");
                    o = in.readObject();
//                    System.out.println("o: " + o.getClass());
                    oe = (NumericOperandExpression) o;
                }catch (Exception e1) {
                    try{
                        oe = (DateOperandExpression) o;
                    }catch (Exception e2) {throw new Exception();}
                }
            }else if (fieldType.getName().equals("java.lang.Double")) {
                oe = (NumericOperandExpression) in.readObject();
//                System.out.println("Field type: " + fieldType + ", oe: " + oe);
            }else if (fieldType.getName().equals("java.lang.String") || fieldType.getName().equals("java.net.URL"))
                oe = (StringOperandExpression) in.readObject();
            else if (fieldType.getName().equals("java.lang.Boolean"))
                oe = (BooleanOperandExpression) in.readObject();
            else if (Date.class.isAssignableFrom(fieldType)) //fieldType.getName().equals("java.util.Date"))
                oe = (DateOperandExpression) in.readObject();


        }catch (Exception e) {
            System.out.println("CTableField readObject() exception : " + e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
        try{
            isHidden = ((Boolean) in.readObject()).booleanValue();
        }catch (Exception e) {
            isHidden = false;
        }
        try{
            fieldEditabilityChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            fieldRemovabilityChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            fieldDataTypeChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            calcFieldResetAllowed = ((Boolean) in.readObject()).booleanValue();
            fieldKeyAttributeChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            fieldHiddenAttributeChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            calcFieldFormulaChangeAllowed = ((Boolean) in.readObject()).booleanValue();
            linkToExternalData = ((Boolean) in.readObject()).booleanValue();
        }catch (Exception exc) {
            fieldEditabilityChangeAllowed = true;
            fieldRemovabilityChangeAllowed = true;
            fieldDataTypeChangeAllowed = true;
            fieldKeyAttributeChangeAllowed = true;
            fieldHiddenAttributeChangeAllowed = true;
            if (isCalculated) {
                calcFieldResetAllowed = true;
                calcFieldFormulaChangeAllowed = true;
            }else{
                calcFieldResetAllowed = false;
                calcFieldFormulaChangeAllowed = false;
            }
            linkToExternalData = false;
        }

    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 23);
        fieldMap.put("Field type", fieldType);
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

//            out.defaultWriteObject();
/*            out.writeObject(fieldType);
            out.writeObject(new Boolean(date));
            out.writeObject(fieldName);
            out.writeObject(new Boolean(isEditable));
            out.writeObject(new Boolean(isKey));
            out.writeObject(new Boolean(isRemovable));
            out.writeObject(new Boolean(isCalculated));
            out.writeObject(formula);
            out.writeObject(textFormula);
            out.writeObject(dependsOnFields);
            out.writeObject(dependentCalcFields);
            out.writeObject(calculatedValue);
            out.writeObject(UIProperties);
*/
        if (fieldType.getName().equals("java.lang.Integer") || fieldType.getName().equals("java.lang.Double")) {
//                System.out.println("Writing NumericOperandExpression: " + oe);
//                out.writeObject(oe);
            fieldMap.put("Operand expression", oe);
        }else if (fieldType.getName().equals("java.lang.String") || fieldType.getName().equals("java.net.URL")) {
//                out.writeObject((StringOperandExpression) oe);
            fieldMap.put("Operand expression", (StringOperandExpression) oe);
        }else if (fieldType.getName().equals("java.lang.Boolean")) {
//                out.writeObject((BooleanOperandExpression) oe);
            fieldMap.put("Operand expression", (BooleanOperandExpression) oe);
        }else if (Date.class.isAssignableFrom(fieldType)) {//fieldType.getName().equals("java.util.Date")) {
//                out.writeObject((DateOperandExpression) oe);
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

/*            out.writeObject(new Boolean(isHidden));
            out.writeObject(new Boolean(fieldEditabilityChangeAllowed));
            out.writeObject(new Boolean(fieldRemovabilityChangeAllowed));
            out.writeObject(new Boolean(fieldDataTypeChangeAllowed));
            out.writeObject(new Boolean(calcFieldResetAllowed));
            out.writeObject(new Boolean(fieldKeyAttributeChangeAllowed));
            out.writeObject(new Boolean(fieldHiddenAttributeChangeAllowed));
            out.writeObject(new Boolean(calcFieldFormulaChangeAllowed));
            out.writeObject(new Boolean(linkToExternalData));
*/
        out.writeObject(fieldMap);
    }


    public final static String localizedNameForDataType(CTableField f) {
        String fldType = "";
        if (f.getFieldType().equals(java.lang.Integer.class))
            fldType = CDatabase.resources.getString("Integer");
        else if (f.getFieldType().equals(java.lang.String.class))
            fldType = CDatabase.resources.getString("Alphanumeric");
        else if (f.getFieldType().equals(java.lang.Double.class))
            fldType = CDatabase.resources.getString("Number");
        else if (f.getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = CDatabase.resources.getString("Image");
        else if (f.getFieldType().equals(java.net.URL.class))
            fldType = CDatabase.resources.getString("URL");
        else if (f.getFieldType().equals(java.lang.Boolean.class))
            fldType = CDatabase.resources.getString("Boolean");
        else if (f.getFieldType().equals(CDate.class)) //java.util.Date.class)) {
//t            if (f.isDate())
            fldType = CDatabase.resources.getString("Date");
        else if (f.getFieldType().equals(CTime.class)) //java.util.Date.class)) {
            fldType = CDatabase.resources.getString("Time");

        return fldType;
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


    public final static String getInternalDataTypeName(CTableField field) {
        String fldType = "";
        if (field.getFieldType().equals(java.lang.Integer.class))
            fldType = "Integer";
        else if (field.getFieldType().equals(java.lang.String.class))
            fldType = "String";
        else if (field.getFieldType().equals(java.lang.Double.class))
            fldType = "Double";
        else if (field.getFieldType().equals(gr.cti.eslate.database.engine.CImageIcon.class))
            fldType = "Image";
        else if (field.getFieldType().equals(java.net.URL.class))
            fldType = "URL";
        else if (field.getFieldType().equals(java.lang.Boolean.class))
            fldType = "Boolean";
        else if (field.getFieldType().equals(CDate.class)) //java.util.Date.class)) {
//t            if (field.isDate())
            fldType = "Date";
        else if (field.getFieldType().equals(CTime.class)) //java.util.Date.class)) {
            fldType = "Time";

        return fldType;
    }


/*    public Object clone() {
        Object field = null;
        try{
            field = super.clone();
        }catch (CloneNotSupportedException e) {}

        System.out.println("UIProperties: " + UIProperties);
        ((CTableField) field).UIProperties = (Array) ((CTableField) field).UIProperties.clone();
        System.out.println("field UIProperties: " + ((CTableField) field).UIProperties);
*/
/*
    protected Array dependsOnFields = null;
    protected Array dependentCalcFields = new Array();
    transient protected OperandExpression oe = null;
*/
/*    return field;
    }
*/
}

