package gr.cti.eslate.database.engine;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.base.CurrencyManager;
import gr.cti.typeArray.*;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 14, 2002
 * Time: 1:29:35 PM
 * To change this template use Options | File Templates.
 */
public class FloatTableField extends AbstractTableField implements Externalizable, CurrencyField {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    public static final float NULL = Float.NEGATIVE_INFINITY+1.21f;
    /** The data type of the data of the FloatTableField. */
    public static final Class DATA_TYPE = Float.class;
    private FltBaseArray data = new FltBaseArray();
    private static final HashMap queryComparators = new HashMap();
    /** While adding a new record and before this is validated against the key constaint of the Table, this
     *  variable holds the value of the cell of the new record for this field.
     */
    private float newCellValue = NULL;
    /** The id of the currency, the data of this field refer to. This property is valid only while the field
     * is in currency mode. A value of -1, means that the field is not in currency mode.
     */
    int currency = -1;
    /** The symbol of the current currency. This property is valid only while the field
     * is in currency mode.
     */
    String currencySymbol = ""; //CurrencyManager.getCurrencyManager().getSymbol(CurrencyManager.EUR);


    static {
        queryComparators.put("<", new LessFloat());
        queryComparators.put("<=", new LessEqualFloat());
        queryComparators.put(">", new GreaterFloat());
        queryComparators.put(">=", new GreaterEqualFloat());
        queryComparators.put("!=", new NotEqualFloat());
        queryComparators.put("=", new EqualFloat());
    }

	public FloatTableField() {
	}

	protected FloatTableField(String name, boolean editable, boolean removable, boolean hidden) {
		fieldName = name;
		isEditable = editable;
		isRemovable = removable;
//		isKey = key;
		this.isHidden = hidden;
//        this.table = table;
//        for (int i=0; i<table.recordCount; i++)
//            data.add(NULL);
	}

    void setTable(Table table) {
        if (this.table == table) return;
        this.table = table;
        // Make sure the field as many records as the record count of the Table it is added to.
        // At the same time keep as many of it's data as possible.
        if (data.size() > table.recordCount)
            data.setSize(table.recordCount);

        for (int i=data.size(); i<table.recordCount; i++)
            data.add(NULL);
    }
    /** Returns the class <code>java.lang.Float</code>. */
	public final Class getDataType() {
		return DATA_TYPE;
	}

    /** Returns the localized data type name of the FloatTableField. */
    public String getLocalizedDataTypeName() {
        return DBase.resources.getString("Float");
    }

    /** Checks the supplied float value against the <code>NULL</code> float value of the FloatTableField.
     *  @return true, if <code>value</code> is equal to <code>NULL</code>. False, otherwise.
     */
    public static final boolean isNull(float value) {
        return (value == NULL);
    }

    /** Checks if the field's cell at <code>recordIndex</code> is null.
     *  @return true, if the value of the field cell at <code>recordIndex</code> is <code>null</code> or contains the
     *  <code>NULL</code> float value. False, otherwise.
     *  @see #isNull(float)
     */
    public final boolean isCellNull(int recordIndex) {
        return isNull(get(recordIndex));
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The float value of the cell.
     *  @exception gr.cti.eslate.database.engine.InvalidCellAddressException Invalid cell address.
     */
    public float getValue(int recIndex) throws InvalidCellAddressException {
        if (recIndex < 0 || recIndex >= size())
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);
        return get(recIndex);
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value as a Float object.
     *  @exception gr.cti.eslate.database.engine.InvalidCellAddressException Invalid cell address.
     */
    public Float getCell(int recIndex)  throws InvalidCellAddressException {
        if (recIndex < 0 || recIndex >= size())
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);
        return getValueAt(recIndex);
    }

    /**
     * @see AbstractTableField#getCellAsString(int)
     */
    public String getCellAsString(int recIndex) {
        Float value = getValueAt(recIndex);
        if (value == null) return null;
        return table.numberFormat.format(value);
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value as an Object.
     *  @exception gr.cti.eslate.database.engine.InvalidCellAddressException Invalid cell address.
     */
    public Object getCellObject(int recIndex)  throws InvalidCellAddressException {
        return getCell(recIndex);
    }

    /** Returns the value of this field at <code>recIndex</code> as a Float */
    Float getValueAt(int recIndex) {
        float value = get(recIndex);
        if (isNull(value)) return null;
        return new Float(value);
    }

    /** Returns the value of this field at <code>recIndex</code> as an Object */
    Object getObjectAt(int recIndex) {
        return getValueAt(recIndex);
    }

    /**
     *  Sets the specified cell of the FloatTableField to <code>value</code>.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
     *  @exception gr.cti.eslate.database.engine.InvalidCellAddressException Invalid cell address.
     *  @exception gr.cti.eslate.database.engine.NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <code>value</code> is <code>null</code>.
     *  @exception gr.cti.eslate.database.engine.InvalidDataFormatException If the supplied <code>value</code> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception gr.cti.eslate.database.engine.DuplicateKeyException If the cell is part of the record's key and the new <code>value</code> violates the key uniqueness.
     */
    public boolean setCell(int recIndex, Object value) throws AttributeLockedException, InvalidCellAddressException,
            InvalidDataFormatException, NullTableKeyException, DuplicateKeyException {
        // Convert the Object 'value' to int and call the setCell(int, int) method.
        if (value != null && value instanceof String && ((String) value).trim().length() == 0)
            value = null;

        float newValue = 0;
        if (value == null)
            newValue = NULL;
        else if (!getDataType().isInstance(value)) {
            newValue = ((Number) table.convertToFieldType(value, this)).floatValue();
        }else
            newValue = ((Number) value).floatValue();

        return setCell(recIndex, newValue);
    }

    /**
     *  Sets the specified cell of the FloatTableField to <code>value</code>.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
     *  @exception gr.cti.eslate.database.engine.InvalidCellAddressException Invalid cell address.
     *  @exception gr.cti.eslate.database.engine.NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <code>value</code> is <code>null</code>.
     *  @exception gr.cti.eslate.database.engine.InvalidDataFormatException If the supplied <code>value</code> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception gr.cti.eslate.database.engine.DuplicateKeyException If the cell is part of the record's key and the new <code>value</code> violates the key uniqueness.
     */
    public boolean setCell(int recIndex, float value) throws AttributeLockedException, InvalidCellAddressException,
            InvalidDataFormatException, NullTableKeyException, DuplicateKeyException {

//System.out.println("setCell() value: " + value);
        boolean pendingNewRecord = table.getRecordEntryStructure().isRecordAdditionPending();
        if (!table.dataChangeAllowed && !pendingNewRecord)
            throw new AttributeLockedException(Table.bundle.getString("CTableMsg89"));

        if (recIndex > table.recordCount || recIndex < 0)
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);

        if (!isEditable)
            return false;

        float currentValue = get(recIndex);
        /*If the new value of the cell is equal to existing then
         *do nothing.
         */
        if (currentValue == value)
            return true;

        // If the field is part of the Table's key, then we do create a Float object to assert the
        // uniqueness of the key. This should avoided in the future, perhaps by creating special versions
        // of the 'checkKeyUniquenessOnNewValue()' and 'updatePrimaryKeyMapForCalcField()' which take also
        // primitives values, or by changing the key mechanism itself.
        if (table.tableKey.isPartOfTableKey(this)) { //isKey()) {
            if (isNull(value))
                throw new NullTableKeyException(Table.bundle.getString("CTableMsg58") + fieldName + Table.bundle.getString("CTableMsg59"));

            Float newValue = new Float(value);
            if (table.tableKey.violatesKeyUniqueness(indexInTable, recIndex, newValue))
                throw new DuplicateKeyException(Table.bundle.getString("CTableMsg60") + value + Table.bundle.getString("CTableMsg61"));

            /*If field f is part of the table's key and is also the field whose values
             *are used in the primarykeyMap HashMap, then the entry for this record in
             *the HashMap has to be deleted and a new entry with the correct new key
             *value has to be entered.
             */
            table.updatePrimaryKeyMapForCalcField(indexInTable, recIndex, newValue);
        }

        set(recIndex, value);
        updateDependentCalculatedFields(recIndex, currentValue);

        table.setModified();
//t            if (this.database != null)
        boolean affectsOtherCells = (dependentCalcFields.size() != 0);
        table.fireFloatCellValueChanged(fieldName, recIndex, currentValue, affectsOtherCells);
        return true;

    }

    /** Updates the calculated fields which depend on this field. This method is called after the value of a cell of this
     *  field changes. The method accepts as parameter the previous value of the field. If while updating the calculated
     *  fields an error occurs, the value of the cell is reset to its previous value and the dependent calculated
     *  fields are re-calculated.
     *  @param recIndex       The index og the cell of this field that changed value.
     *  @param previousValue  The previous value of the cell.
     *  @exception  gr.cti.eslate.database.engine.DuplicateKeyException If the new value of a dependent calculated field, which is also part of the
     *                                    Table's key, violates the key uniqueness.
     *  @exception  gr.cti.eslate.database.engine.NullTableKeyException If the new value of a dependent calculated field, which is also part of the
     *                                    Table's key, is null.
     */
    void updateDependentCalculatedFields(int recIndex, float previousValue) throws DuplicateKeyException, NullTableKeyException {
        try{
            if (!(dependentCalcFields.size() == 0)) {
                for (int i=0; i<dependentCalcFields.size(); i++) {
                    table.evaluateCalculatedFieldCell(dependentCalcFields.get(i), recIndex);

                    AbstractTableField f1 = table.tableFields.get(dependentCalcFields.get(i));
                    table.changedCalcCells.put(f1.getName(), new Integer(recIndex));
                }
                table.calculatedFieldsChanged = true;
            }
        }catch (DuplicateKeyException e) {
            boolean pendingNewRecord = table.getRecordEntryStructure().isRecordAdditionPending();
            if (!pendingNewRecord) {
                table.changedCalcCells.clear();
                /* Undo the change. Recalculate the dependent calculated fields.
                 */
                set(recIndex, previousValue);
                try{
                    for (int i=0; i<dependentCalcFields.size(); i++)
                        table.evaluateCalculatedFieldCell(dependentCalcFields.get(i), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (3)");}
                 catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (3)");}

            }
            throw new DuplicateKeyException(e.message);
        }
         catch (NullTableKeyException e) {
            boolean pendingNewRecord = table.getRecordEntryStructure().isRecordAdditionPending();
            if (!pendingNewRecord) {
                table.changedCalcCells.clear();
                set(recIndex, previousValue);
                try{
                    for (int i=0; i<dependentCalcFields.size(); i++)
                        table.evaluateCalculatedFieldCell(dependentCalcFields.get(i), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}
                 catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}

            }
            throw new NullTableKeyException(e.message);
        }
    }

    /** Compares the value of the cell at <code>recIndex</code> with the supplied Float for equality */
    boolean equalsValueAt(int recIndex, Float value) {
        float val = get(recIndex);
        if (value == null) {
            if (isNull(val)) return true;
            return false;
        }else
            return (val == value.intValue());
    }

    /** Directly sets the value of the cell at <code>recIndex</code> of this field.
     */
    void set(int recIndex, Object value) {
        if (value == null)
            set(recIndex, NULL);
        else
            set(recIndex, ((Number) value).floatValue());
    }

    /** Directly sets the value of the cell at <code>recIndex</code> of this field.
     */
    void set(int recIndex, float value) {
//System.out.println("set() recIndex: " + recIndex + ", size(): " + data.size());
        data.set(recIndex, value);
    }

    /** Returns the value of the cell at <code>recIndex</code>. */
    float get(int recIndex) {
        return data.get(recIndex);
    }

    void add(Object value) {
        float val = NULL;
        if (value != null)
            val = ((Float) value).floatValue();
        data.add(val);
    }

    void remove(int recIndex) {
        data.remove(recIndex);
    }

    int size() {
        return data.size();
    }

    public ArrayBase getData() {
        return data;
    }

    public int indexOf(int start, int end, Object value) {
        if (value != null && !Float.class.isAssignableFrom(value.getClass()))
            return -1;
        float val = NULL;
        if (value != null)
            val = ((Float) value).floatValue();
        return data.indexOf(start, end, val);
    }

    public int indexOf(Object value) {
        return indexOf(0, size()-1, value);
    }

    public Object[] getDataArray() {
        Float[] ar = new Float[size()];
        for (int i=0; i<ar.length; i++) {
            if (isNull(get(i)))
                ar[i] = null;
            else
                ar[i] = new Float(get(i));
        }
        return ar;
    }

    public ObjectBaseArray getDataBaseArray() {
        int dataSize = size();
        ObjectBaseArray ar = new ObjectBaseArray(dataSize);
        for (int i=0; i<dataSize; i++) {
            if (isNull(get(i)))
                ar.add(null);
            else
                ar.add(new Float(get(i)));
        }
        return ar;
    }

    public ArrayList getDataArrayList() {
        ArrayList al = new ArrayList(size());
        for (int i=0; i<size(); i++) {
            if (isNull(get(i)))
                al.add(null);
            else
                al.add(new Float(get(i)));
        }
        return al;
    }

    // Should be abstract
    void setData(ArrayList al) {
        data.clear();
        Float val = null;
        for (int i=0; i<al.size(); i++) {
            val = (Float) al.get(i);
            if (val == null)
                data.add(NULL);
            else
                data.add(val.floatValue());
        }
    }

    /** Creates an iterator on the field's data. The iterator will go through all the data of the field.
     *  @return An instance of FloatFieldDataIterator.
     *  @see #iterator(gr.cti.eslate.database.engine.IntBaseArrayDesc, int)
     */
    public FieldDataIterator iterator() {
        return iterator(0);
    }

    /** Creates an iterator on the field's data. The iterator will go through all the data of the field and
     *  will be positioned at <code>startIndex</code>.
     *  @return An instance of FloatFieldDataIterator.
     *  @see #iterator(gr.cti.eslate.database.engine.IntBaseArrayDesc, int)
     */
    public FieldDataIterator iterator(int firstIndex) {
        int dataSize = size();
        IntBaseArrayDesc recIndices = new IntBaseArrayDesc(dataSize);
        for (int i=0; i<dataSize; i++)
            recIndices.add(i);
        return new FloatFieldDataIterator(recIndices, firstIndex, this);
    }

    /** Creates an iterator on the field's data. The iterator
     *  will go through only the field data of the records specified by <code>recIndices</code> and it will begin the
     *  iteration from the record at <code>firstIndex</code> in the <code>recIndices</code> array.
     *  @param recIndices The indices of the records on which the iteration will take place.
     *  @param firstIndex The index of the record in the <code>recIndices</code> array, where the iteration will start from.
     *  @return An instance of FloatFieldDataIterator.
     */
    public FieldDataIterator iterator(IntBaseArrayDesc recIndices, int firstIndex) {
        return new FloatFieldDataIterator(recIndices, firstIndex, this);
    }

    /**
     * Find the first element in a sequence that satisfies a comparison(query).
     * The time complexity is linear and the space complexity is constant.
     * @param iterator An iterator positioned at the first element of the sequence.
     * @param predicateComparator The comparison(query).
     *  @see #iterator()
     *  @see AbstractTableField#findNext(FieldDataIterator, SecondPredicateComparator)
     *  @see AbstractTableField#createSecondOperandOperator(AbstractTableField, String, Object)
     */
    public void findNext(FieldDataIterator iterator, SecondPredicateComparator predicateComparator) {
        while (!iterator.atEnd() && (!((FloatSecondPredicateComparator) predicateComparator).execute(((FloatFieldDataIterator) iterator).get())))
          iterator.advance();
    }
    // Old implementation, based on the architecture of the JGL ArrayIterator.
/*    public FieldDataIterator findNext(FieldDataIterator first, FieldDataIterator last, SecondPredicateComparator predicateComparator) {
        FloatFieldDataIterator firstx = (FloatFieldDataIterator) first.clone();
        while (!firstx.equals(last) && (*//*firstx.get() == NULL || */ /*!((FloatSecondPredicateComparator) predicateComparator).execute(firstx.get())))
          firstx.advance();

        return firstx;
    }
*/
    /** Returns the comparator class, which corresponds to the supplied string representation of the comparator.
     *  The comparators supported for FloatTableFields are:
        <table border="1" width="100%">
          <tr>
            <td width="50%" align="center"><i>Comparator string representation</i></td>
            <td width="50%" align="center"><i>Comparators java class</i></td>
          </tr>
          <tr>
            <td width="50%" align="center">=</td>
            <td width="50%" align="center">EqualFloat</td>
          </tr>
          <tr>
            <td width="50%" align="center">!=</td>
            <td width="50%" align="center">NotEqualFloat</td>
          </tr>
          <tr>
            <td width="50%" align="center">&lt;</td>
            <td width="50%" align="center">LessFloat</td>
          </tr>
          <tr>
            <td width="50%" align="center">&lt;=</td>
            <td width="50%" align="center">LessEqualFloat</td>
          </tr>
          <tr>
            <td width="50%" align="center">></td>
            <td width="50%" align="center">GreaterFloat</td>
          </tr>
          <tr>
            <td width="50%" align="center">>=</td>
            <td width="50%" align="center">GreaterEqualFloat</td>
          </tr>
        </table>
     */
    public static final FloatComparator getComparator(String comparatorStr) {
        return (FloatComparator) queryComparators.get(comparatorStr);
    }

    /** Delegates functionality to <code>getComparator()</code>.
     *  @see #getComparator(java.lang.String)
     */
    public final ObjectComparator getComparatorFor(String comparatorStr) {
        return getComparator(comparatorStr);
    }

    /** Returns a copy of the comparators supported by the FloatTableField.
     */
    public static final HashMap getComparators() {
        return (HashMap) queryComparators.clone();
    }

    /** Compares the value at <code>recIndex</code> of the AbstractTableField to the given <code>value</code> using the
     *  specified <code>comparator</code>.
     *  @param recIndex The record index of the data of the field, which will be compared against <code>value</code>.
     *  @param comparator The comparator used.
     *  @param value The second operand of the operation.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(java.lang.String)
     */
    public boolean compareTo(int recIndex, ObjectComparator comparator, Object value) {
        return ((FloatComparator) comparator).execute(get(recIndex), value);
    }

    /** Compares the value at <code>recIndex</code> of the AbstractTableField to the given <code>value</code> using the
     *  specified <code>comparator</code>.
     *  @param recIndex The record index of the data of the field, which will be compared against <code>value</code>.
     *  @param comparator The comparator used.
     *  @param value The second operand of the operation.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(java.lang.String)
     */
    public boolean compareTo(int recIndex, FloatComparator comparator, float value) {
        return comparator.execute(get(recIndex), value);
    }

    /** Compares the cell at <code>recIndex</code> of this field to the cell at <code>recIndex2</code> of AbstractTableField
     *  <code>f</code> using the specified comparator.
     *  @param recIndex The record index of the cell of this field.
     *  @param comparator The comparator.
     *  @param f The second field.
     *  @param recIndex2 The record index of the cell of the second field.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(java.lang.String)
     *  @see #compareTo(int, gr.cti.eslate.database.engine.ObjectComparator, java.lang.Object)
     */
    boolean compareTo(int recIndex, ObjectComparator comparator, AbstractTableField f, int recIndex2) {
        if (f.getDataType().equals(Float.class))
            return compareTo(recIndex, (FloatComparator) comparator, ((FloatTableField) f).get(recIndex2));
        return compareTo(recIndex, comparator, f.getObjectAt(recIndex2));
    }

    /** Sorts the elements of <code>data<code> array using the specified comparator. The <code>data</code> is sorted,
     *  after the method finishes execution.
     *  @param data The FltBaseArray to sort.
     *  @param comparator The ObjectComparator used to sort the array.
     *  @see #sort(gr.cti.typeArray.FltBaseArray, int, int, gr.cti.eslate.database.engine.FloatComparator)
     */
    public void sort(FltBaseArray data, FloatComparator comparator) {
        sort(data, 0, size()-1, comparator);
    }

    /** Sorts the elements of <code>data<code> array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code> using the specified comparator. The <code>data</code> is sorted, after the method finishes
     *  execution.
     *  @param data The IntBaseArray to sort.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The Comparator used to sort the array.
     */
    public void sort(FltBaseArray data, int first, int last, FloatComparator comparator) {
        // calculate first and last index into the sequence.
        int start = first;
        int finish = last + 1; //distance(base.listIterator(), last)+1;
//        int start = (base.start()).distance( first );
//        int finish = (base.start()).distance( last );

        quickSortLoop(data, start, finish, comparator, null);
        finalInsertionSort(data, start, finish, comparator, null);
    }

    /** Sorts all the cells of this field array using the specified comparator. The order of the data of the field,
     *  does not change. The method returns an array with the order the cells of the field would have, when sorted by the
     *  <code>comparator</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     *  @see #getOrder(int, int, gr.cti.eslate.database.engine.ObjectComparator)
     */
    public IntBaseArray getOrder(ObjectComparator comparator) {
        return getOrder((FltBaseArray) data.clone(), 0, data.size()-1, (FloatComparator) comparator);
    }

    /** Sorts the cells of this field array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code>, using the specified comparator. The order of the data of the field, does not change.
     *  The method returns an array with the order the cells of the field would have, when sorted by the
     *  <code>comparator</code>.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     *  @see #getOrder(gr.cti.typeArray.FltBaseArray, int, int, gr.cti.eslate.database.engine.FloatComparator)
     */
    public IntBaseArray getOrder(int first, int last, ObjectComparator comparator) {
        return getOrder((FltBaseArray) data.clone(), first, last, (FloatComparator) comparator);
    }

    /** Sorts the elements of <code>data<code> array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code>, using the specified comparator. The <code>data</code> is sorted, after the method finishes
     *  execution. The method returns an array with the new order of the elements inside <code>data</code>.
     *  @param data The IntBaseArray to sort.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     */
    public static IntBaseArray getOrder(FltBaseArray data, int first, int last, FloatComparator comparator) {
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

    private static void finalInsertionSort(FltBaseArray data, int first, int last, FloatComparator comparator, IntBaseArray indices) {
/*System.out.print("finalInsertionSort() indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println();
*/
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

    private static void unguardedLinearInsert(FltBaseArray data, int last, float value, FloatComparator comparator, IntBaseArray indices, int index) {
/*System.out.print("1. unguardedLinearInsert() indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println(", index: " + index +", last: " + last);
*/
        int next = last - 1;

        if (indices == null) {
            while (comparator.execute(value, data.get(next )))
                data.set(last--, data.get(next--));
        }else{
            while (comparator.execute(value, data.get(next ))) {
//System.out.println("Moving index " + indices.get(next) + " from " + next + " to " + last);
              indices.set(last, indices.get(next));
              data.set(last--, data.get( next--));
            }
        }

        data.set(last, value);
        if (indices != null) {
//System.out.println("Setting " + last + " to value: " + index);
            indices.set(last, index);
        }
/*System.out.print("2. unguardedLinearInsert() indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println(", index: " + index);
*/
    }

    private static void linearInsert(FltBaseArray data, int first, int last, FloatComparator comparator, IntBaseArray indices) {
/*System.out.print("linearInsert() indices: ");
for (int i=0; i<indices.size(); i++)
    System.out.print(indices.get(i) + ", ");
System.out.println();
*/
        float value = data.get(last);
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

    private static void quickSortLoop(FltBaseArray data, int first, int last, FloatComparator comparator, IntBaseArray indices) {
        while (last - first > stlThreshold) {
            float pivot;
            float a = data.get(first);
            float b = data.get(first + (last - first) / 2);
            float c = data.get(last - 1);

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

                float tmp = data.get(cut);
                int tmp2 = 0;
                if (indices != null) {
                    tmp2 = indices.get(cut);
                    indices.set(cut, indices.get(lastx));
                }
                data.set(cut++, data.get(lastx));
                if (indices != null)
                    indices.set(lastx, tmp2);
                data.set(lastx, tmp);
/*System.out.print("quickSortLoop() indices: ");
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

    /**
     *  @see gr.cti.eslate.database.engine.AbstractTableField#setNewCellValue(java.lang.Object)
     */
    void setNewCellValue(Object value) {
        if (value == null)
            newCellValue = NULL;
        else
            newCellValue = ((Number) value).floatValue();
    }

    /**
     *  @see AbstractTableField#setNewCellValue(Object)
     */
    void setNewCellValue(float value) {
        newCellValue = value;
    }

    /**
     *  @see gr.cti.eslate.database.engine.AbstractTableField#getNewCellValue()
     */
    Object getNewCellValue() {
        if (newCellValue == NULL)
            return null;
        return new Float(newCellValue);
    }

    /**
     *  @see AbstractTableField#getNewCellValueAsString()
     */
    String getNewCellValueAsString() {
        if (newCellValue == NULL)
            return null;
        return table.numberFormat.format(newCellValue);
    }

    /**
     *  @see gr.cti.eslate.database.engine.AbstractTableField#commitNewCellValue()
     */
    void commitNewCellValue() {
        if (!isCalculated)
            data.add(newCellValue);
        else{
            if (dependsOnFields.size() == 0)
                data.add(((Number) calculatedValue).floatValue());
            else{
                Object value = oe.execute(data.size(), false);
                if (value == null)
                    newCellValue = NULL;
                else
                    newCellValue = ((Number) value).floatValue();
                data.add(newCellValue);
            }
        }
    }

    public StorageStructure recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("Common State", super.recordState());
        fieldMap.put("Operand expression", oe);
        fieldMap.put("Data", data);
        fieldMap.put("Currency", currency);
        fieldMap.put("CurrencySymbol", currencySymbol);
        return fieldMap;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(recordState());
    }

    public void applyState(StorageStructure fieldMap) {
        super.applyState((StorageStructure) fieldMap.get("Common State"));
        oe = (NumericOperandExpression) fieldMap.get("Operand expression");
        data = (FltBaseArray) fieldMap.get("Data");
        currency = fieldMap.get("Currency", -1);
        currencySymbol = fieldMap.get("CurrencySymbol", "");
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        applyState(fieldMap);
    }

    /** Sets the currency of the field. While the currency of the field is -1, the field is not in
     * currency mode.The first time the currency is set, the data of the field remain intact. If the
     * currency is in currency mode and a new currency is set, then the data of the field are converted
     * tothe new currency.
     * @param currency The new currency or -1 to exit the currency mode.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public void setCurrency(int currency) {
        if (this.currency == currency) return;
        CurrencyManager mgr = CurrencyManager.getCurrencyManager();
        if (currency != -1 && this.currency != -1) {
            for (int i=0; i<data.size(); i++) {
                data.set(i, (float) mgr.convert(data.get(i), this.currency, currency));
            }
        }
        this.currency = currency;
        if (currency == -1)
            currencySymbol = "";
        else
            currencySymbol = mgr.getSymbol(currency);
        table.fireCurrencyFieldChanged(getName());
    }

    /** Returns the current currency of the field, or -1 if the field is not in currency mode.
     * @return The currency of the field.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public int getCurrency() {
        return currency;
    }

    /**
     * The symbol of the current currency. An empty string is returned if the field is not in currency mode.
     * @return The currency symbol.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }
}

class FloatSecondPredicateComparator implements SecondPredicateComparator {
    float secondPredicate = 0;
    FloatComparator comparator = null;

    public FloatSecondPredicateComparator(FloatComparator comparator, Float secondOperand) {
        this.secondPredicate = secondOperand.floatValue();
        this.comparator = comparator;
    }

    public boolean execute(float firstPredicate) {
        return comparator.execute(firstPredicate, secondPredicate);
    }

    public ObjectComparator getComparator() {
        return comparator;
    }
}

abstract class FloatComparator implements ObjectComparator {
    /** float comparison operation. */
    public abstract boolean execute(float first, float second);
    /** More general comparison operation, which can compare a float to a Number object. */
    public boolean execute(float first, Object second) {
        if (second == null)
            return execute(first, FloatTableField.NULL);
        return execute(first, ((Number) second).floatValue());
    }
    public boolean execute(Object first, Object second) {
        float firstFlt = FloatTableField.NULL, secondFlt = FloatTableField.NULL;
        if (first != null) firstFlt = ((Number) first).floatValue();
        if (second != null) secondFlt = ((Number) second).floatValue();
        return execute(firstFlt, secondFlt);
    }
}

class LessFloat extends FloatComparator {
    public boolean execute(float first, float second) {
        if (first == FloatTableField.NULL) return false;
        return (first < second);
    }
}

class LessEqualFloat extends FloatComparator {
    public boolean execute(float first, float second) {
        if (first == FloatTableField.NULL) return false;
        return (first <= second);
    }
}

class GreaterFloat extends FloatComparator {
    public boolean execute(float first, float second) {
        return (first > second);
    }
}

class GreaterEqualFloat extends FloatComparator {
    public boolean execute(float first, float second) {
        return (first >= second);
    }
}

class EqualFloat extends FloatComparator {
    public boolean execute(float first, float second) {
        return (first == second);
    }
}

class NotEqualFloat extends FloatComparator {
    public boolean execute(float first, float second) {
        return (first != second);
    }
}
