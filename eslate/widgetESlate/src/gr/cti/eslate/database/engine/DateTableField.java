package gr.cti.eslate.database.engine;

import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.ObjectBaseArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.eslate.tableModel.event.CellValueChangedEvent;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DateTableField extends AbstractTableField implements Externalizable {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    /** The data type of the data of the DateTableField. */
    public static final Class DATA_TYPE = CDate.class;
    private CDateBaseArray data = new CDateBaseArray();
    private static final HashMap queryComparators = new HashMap();
    /** While adding a new record and before this is validated against the key constaint of the Table, this
     *  variable holds the value of the cell of the new record for this field.
     */
    CDate newCellValue = null;

    static{
        queryComparators.put("=", new EqualDate());
        queryComparators.put("!=", new NotEqualDate());
        queryComparators.put("<", new LessDate());
        queryComparators.put("<=", new LessEqualDate());
        queryComparators.put(">", new GreaterDate());
        queryComparators.put(">=", new GreaterEqualDate());
    }

	public DateTableField() {
	}

	protected DateTableField(String name, boolean editable, boolean removable, boolean hidden) {
		fieldName = name;
		isEditable = editable;
		isRemovable = removable;
//		isKey = key;
		this.isHidden = hidden;
//        this.table = table;
//        for (int i=0; i<table.recordCount; i++)
//            data.add(null);
	}

    void setTable(Table table) {
        if (this.table == table) return;
        this.table = table;
        // Make sure the field as many records as the record count of the Table it is added to.
        // At the same time keep as many of it's data as possible.
        if (data.size() > table.recordCount)
            data.setSize(table.recordCount);

        for (int i=data.size(); i<table.recordCount; i++)
            data.add(null);
    }

    /** Returns the class <code>gr.cti.eslate.database.engine.CDate</code>. */
	public final Class getDataType() {
		return DATA_TYPE;
	}

    /** Returns the localized data type name of the DateTableField. */
    public String getLocalizedDataTypeName() {
        return DBase.resources.getString("Date");
    }

    /**
     *  @return Returns true, if <code>value</code> is null. <code>false</code> otherwise.
     */
    public static final boolean isNull(CDate value) {
        return (value == null);
    }

    /** Checks if the field's cell at <code>recordIndex</code> is null.
     *  @return true, if the value of the field at <code>recordIndex</code> is <code>null</code>. False, otherwise.
     */
    public final boolean isCellNull(int recordIndex) {
        return isNull(get(recordIndex));
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value of the cell as a CDate.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public CDate getCell(int recIndex)  throws InvalidCellAddressException {
        if (recIndex < 0 || recIndex >= size())
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);
        return getValueAt(recIndex);
    }

    /**
     * @see AbstractTableField#getCellAsString(int)
     */
    public String getCellAsString(int recIndex) {
        CDate value = getValueAt(recIndex);
        if (value == null) return null;
        return value.toString();
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value of the cell as an Object.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public Object getCellObject(int recIndex)  throws InvalidCellAddressException {
        return getCell(recIndex);
    }

    /** Returns the value of this field at <code>recIndex</code> as a CDate. */
    CDate getValueAt(int recIndex) {
        return get(recIndex);
    }

    /** Returns the value of this field at <code>recIndex</code> as an Object. */
    Object getObjectAt(int recIndex) {
        return get(recIndex);
    }

    /**
     *  Sets the specified cell of the DateTableField to <code>value</code>.
     *  @param recIndex The index of the record which contains the cell.
     *  @param value The new value of the cell.
     *  @return <code>true</code> if the cell is set. <code>false</code>, if the field is not editable.
     *  @exception InvalidCellAddressException Invalid cell address.
     *  @exception NullTableKeyException If the cell whose value is set belongs to the record's key and the provided <code>value</code> is <code>null</code>.
     *  @exception InvalidDataFormatException If the supplied <code>value</code> does not have or cannot be cast to the data type of the field, to which the cell belongs.
     *  @exception DuplicateKeyException If the cell is part of the record's key and the new <code>value</code> violates the key uniqueness.
     */
    public boolean setCell(int recIndex, Object value) throws AttributeLockedException, InvalidCellAddressException,
    InvalidDataFormatException, NullTableKeyException, DuplicateKeyException {
        // Convert the Object 'value' to CDate.
        if (value != null && value instanceof String && ((String) value).trim().length() == 0)
            value = null;

        CDate newValue = null;
        if (value != null) {
            if (!getDataType().isInstance(value)) {
                newValue = (CDate) table.convertToFieldType(value, this);
            }else
                newValue = (CDate) value;
        }

        boolean pendingNewRecord = table.getRecordEntryStructure().isRecordAdditionPending();
        if (!table.dataChangeAllowed && !pendingNewRecord)
            throw new AttributeLockedException(Table.bundle.getString("CTableMsg89"));

        if (recIndex > table.recordCount || recIndex < 0)
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);

        if (!isEditable)
            return false;

        CDate currentValue = get(recIndex);
        /*If the new value of the cell is equal to existing then
         *do nothing.
         */
        if (currentValue == value)
            return true;

        if (table.tableKey.isPartOfTableKey(this)) { //isKey()) {
            if (isNull(newValue))
                throw new NullTableKeyException(Table.bundle.getString("CTableMsg58") + fieldName + Table.bundle.getString("CTableMsg59"));

            if (table.tableKey.violatesKeyUniqueness(indexInTable, recIndex, newValue))
                throw new DuplicateKeyException(Table.bundle.getString("CTableMsg60") + value + Table.bundle.getString("CTableMsg61"));

            /*If field f is part of the table's key and is also the field whose values
             *are used in the primarykeyMap HashMap, then the entry for this record in
             *the HashMap has to be deleted and a new entry with the correct new key
             *value has to be entered.
             */
            table.updatePrimaryKeyMapForCalcField(indexInTable, recIndex, newValue);
        }

        set(recIndex, newValue);
        try{
            updateDependentCalculatedFields(recIndex);
        }catch (Throwable e) {
            if (!pendingNewRecord) {
                table.changedCalcCells.clear();
                set(recIndex, currentValue);
                try{
                    for (int i=0; i<dependentCalcFields.size(); i++)
                        table.evaluateCalculatedFieldCell(dependentCalcFields.get(i), recIndex);
                }catch (DuplicateKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}
                 catch (NullTableKeyException e1) {System.out.println("Serious inconsistency error in Table setCell(int, int, Object): (4)");}

            }
            if (DuplicateKeyException.class.isAssignableFrom(e.getClass()))
                throw new DuplicateKeyException(e.getMessage());
            else if (NullTableKeyException.class.isAssignableFrom(e.getClass()))
                throw new NullTableKeyException(e.getMessage());
            else
                throw new RuntimeException(e.getMessage());
        }

        table.setModified();
        boolean affectsOtherCells = (dependentCalcFields.size() != 0);
        table.fireCellValueChanged(fieldName, recIndex, currentValue, affectsOtherCells);
        return true;
    }

    /** Directly sets the value of the cell at <code>recIndex</code> of this field.
     */
    void set(int recIndex, Object value) {
        set(recIndex, (CDate) value);
    }

    /** Directly sets the value of the cell at <code>recIndex</code> of this field. */
    void set(int recIndex, CDate value) {
        data.set(recIndex, value);
    }

    /** Returns the value of the cell at <code>recIndex</code>. */
    CDate get(int recIndex) {
        return data.get(recIndex);
    }
    void add(Object value) {
        data.add((CDate) value);
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
        if (value != null && !CDate.class.isAssignableFrom(value.getClass()))
            return -1;
        CDate d = (CDate) value;
        return data.indexOf(start, end, d);
    }

    public int indexOf(Object value) {
        return indexOf(0, size()-1, value);
    }

    public Object[] getDataArray() {
        return data.toArray();
    }

    public ObjectBaseArray getDataBaseArray() {
        int dataSize = size();
        ObjectBaseArray ar = new ObjectBaseArray(dataSize);
        for (int i=0; i<dataSize; i++)
            ar.add(i, get(i));
        return ar;
    }

    public ArrayList getDataArrayList() {
        ArrayList al = new ArrayList(size());
        for (int i=0; i<size(); i++)
            al.add(get(i));
        return al;
    }

    // Should be abstract
    void setData(ArrayList al) {
        data.clear();
        for (int i=0; i<al.size(); i++)
            data.add((CDate) al.get(i));
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
        while (!iterator.atEnd() && (!((DateSecondPredicateComparator) predicateComparator).execute(((DateFieldDataIterator) iterator).get())))
          iterator.advance();
    }
    // Old implementation, based on the architecture of the JGL ArrayIterator.
/*    public FieldDataIterator findNext(FieldDataIterator first, FieldDataIterator last, SecondPredicateComparator predicateComparator) {
        DateFieldDataIterator firstx = (DateFieldDataIterator) first.clone();
        while (!firstx.equals(last) && (*//*firstx.get() == null || *//* !((DateSecondPredicateComparator) predicateComparator).execute(firstx.get())))
          firstx.advance();

        return firstx;
    }
*/
    /** Returns the comparator class, which corresponds to the supplied string representation of the comparator.
     *  The comparators supported for DateTableFields are:
        <table border="1" width="100%">
          <tr>
            <td width="50%" align="center"><i>Comparator string representation</i></td>
            <td width="50%" align="center"><i>Comparator java class</i></td>
          </tr>
          <tr>
            <td width="50%" align="center">=</td>
            <td width="50%" align="center">EqualDate</td>
          </tr>
          <tr>
            <td width="50%" align="center">!=</td>
            <td width="50%" align="center">NotEqualDate</td>
          </tr>
          <tr>
            <td width="50%" align="center">&lt;</td>
            <td width="50%" align="center">LessDate</td>
          </tr>
          <tr>
            <td width="50%" align="center">&lt;=</td>
            <td width="50%" align="center">LessEqualDate</td>
          </tr>
          <tr>
            <td width="50%" align="center">></td>
            <td width="50%" align="center">GreaterDate</td>
          </tr>
          <tr>
            <td width="50%" align="center">>=</td>
            <td width="50%" align="center">GreaterEqualDate</td>
          </tr>
        </table>
     */
    public static final DateComparator getComparator(String comparatorStr) {
        return (DateComparator) queryComparators.get(comparatorStr);
    }

    /** Returns a copy of the comparators supported by the DateTableField.
     */
    public static final HashMap getComparators() {
        return (HashMap) queryComparators.clone();
    }

    /** Delegates functionality to <code>getComparator()</code>.
     *  @see #getComparator(String)
     */
    public final ObjectComparator getComparatorFor(String comparatorStr) {
        return getComparator(comparatorStr);
    }

    /** Creates an iterator on the field's data. The iterator will go through all the data of the field.
     *  @return An instance of DateFieldDataIterator.
     *  @see #iterator(IntBaseArrayDesc, int)
     */
    public FieldDataIterator iterator() {
        return iterator(0);
    }

    /** Creates an iterator on the field's data. The iterator will go through all the data of the field and
     *  will be positioned at <code>startIndex</code>.
     *  @return An instance of DateFieldDataIterator.
     *  @see #iterator(IntBaseArrayDesc, int)
     */
    public FieldDataIterator iterator(int firstIndex) {
        int dataSize = size();
        IntBaseArrayDesc recIndices = new IntBaseArrayDesc(dataSize);
        for (int i=0; i<dataSize; i++)
            recIndices.add(i);
        return new DateFieldDataIterator(recIndices, firstIndex, this);
    }

    /** Creates an iterator on the field's data. The iterator
     *  will go through only the field data of the records specified by <code>recIndices</code> and it will begin the
     *  iteration from the record at <code>firstIndex</code> in the <code>recIndices</code> array.
     *  @param recIndices The indices of the records on which the iteration will take place.
     *  @param firstIndex The index of the record in the <code>recIndices</code> array, where the iteration will start from.
     *  @return An instance of DateFieldDataIterator.
     */
    public FieldDataIterator iterator(IntBaseArrayDesc recIndices, int firstIndex) {
        return new DateFieldDataIterator(recIndices, firstIndex, this);
    }

    /** Compares the value at <code>recIndex</code> of the AbstractTableField to the given <code>value</code> using the
     *  specified <code>comparator</code>.
     *  @param recIndex The record index of the data of the field, which will be compared against <code>value</code>.
     *  @param comparator The comparator used.
     *  @param value The second operand of the operation.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(String)
     */
    public boolean compareTo(int recIndex, DateComparator comparator, CDate value) {
        return comparator.execute(get(recIndex), value);
    }

    /** Compares the cell at <code>recIndex</code> of this field to the cell at <code>recIndex2</code> of AbstractTableField
     *  <code>f</code> using the specified comparator.
     *  @param recIndex The record index of the cell of this field.
     *  @param comparator The comparator.
     *  @param f The second field.
     *  @param recIndex2 The record index of the cell of the second field.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(String)
     *  @see #compareTo(int, ObjectComparator, Object)
     */
    boolean compareTo(int recIndex, ObjectComparator comparator, AbstractTableField f, int recIndex2) {
        return compareTo(recIndex, comparator, f.getObjectAt(recIndex2));
    }

    /** @see #compareTo(int, DateComparator, CDate)
     */
    public boolean compareTo(int recIndex, ObjectComparator comparator, Object value) {
        return compareTo(recIndex, (DateComparator) comparator, (CDate) value);
    }

    /** Sorts the elements of <code>data<code> array using the specified comparator. The <code>data</code> is sorted,
     *  after the method finishes execution.
     *  @param data The IntBaseArray to sort.
     *  @param comparator The Comparator used to sort the array.
     *  @see #sort(ObjectBaseArray, int, int, DateComparator)
     */
    public void sort(ObjectBaseArray data, DateComparator comparator) {
        sort(data, 0, data.size()-1, comparator);
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
    public void sort(ObjectBaseArray data, int first, int last, DateComparator comparator) {
        // calculate first and last index into the sequence.
        int start = first;
        int finish = last + 1; //distance(base.listIterator(), last)+1;
//        int start = (base.start()).distance( first );
//        int finish = (base.start()).distance( last );

        super.quickSortLoop(data, start, finish, comparator, null);
        finalInsertionSort(data, start, finish, comparator, null);
    }

    /** Sorts all the cells of this field array using the specified comparator. The order of the data of the field,
     *  does not change. The method returns an array with the order the cells of the field would have, when sorted by the
     *  <code>comparator</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     *  @see #getOrder(int, int, ObjectComparator)
     */
    public IntBaseArray getOrder(ObjectComparator comparator) {
        return getOrder(0, size()-1, comparator);
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
     *  @see #getOrder(ObjectBaseArray, int, int, DateComparator)
     */
    public IntBaseArray getOrder(int first, int last, ObjectComparator comparator) {
        int dataSize = size();
        ObjectBaseArray dataClone = new ObjectBaseArray(dataSize);
        for (int i=0; i<dataSize; i++)
            dataClone.add(get(i));
        return getOrder(dataClone, first, last, (DateComparator) comparator);
    }

    /** Sorts the elements of <code>data<code> array between indices <code>start</code> and <code>last</code>, including
     *  <code>last</code> using the specified comparator. The <code>data</code> is sorted, after the method finishes
     *  execution. The method returns an array with the new order of the elements inside <code>data</code>.
     *  @param data The IntBaseArray to sort.
     *  @param first The index of the first element in the array to sort.
     *  @param last The index of the last element in the array to be sorted. The sort operation will take place only on
     *  the elements of the array between <code>start</code> and <code>last</code>.
     *  @param comparator The Comparator used to sort the array.
     *  @return The new order of the elements of the array. For example the first int in this array contains the index
     *  that the first element in the sorted <code>data</code> array had before sorting.
     */
    public static IntBaseArray getOrder(ObjectBaseArray data, int first, int last, DateComparator comparator) {
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

    /**
     *  @see AbstractTableField#setNewCellValue(Object)
     */
    void setNewCellValue(Object value) {
        newCellValue = (CDate) value;
    }

    /**
     *  @see AbstractTableField#getNewCellValue()
     */
    Object getNewCellValue() {
        return newCellValue;
    }

    /**
     *  @see AbstractTableField#getNewCellValueAsString()
     */
    String getNewCellValueAsString() {
        if (newCellValue == null)
            return null;
        return newCellValue.toString();
    }

    /**
     *  @see AbstractTableField#commitNewCellValue()
     */
    void commitNewCellValue() {
        data.add(newCellValue);
    }

    public StorageStructure recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("Common State", super.recordState());
        fieldMap.put("Operand expression", (DateOperandExpression) oe);
        fieldMap.put("Data", data);
        return fieldMap;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(recordState());
    }

    public void applyState(StorageStructure fieldMap) {
        super.applyState((StorageStructure) fieldMap.get("Common State"));
        oe = (DateOperandExpression) fieldMap.get("Operand expression");
        data = (CDateBaseArray) fieldMap.get("Data");
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        applyState(fieldMap);
    }
}

class DateSecondPredicateComparator implements SecondPredicateComparator {
    CDate secondPredicate = null;
    DateComparator comparator = null;

    public DateSecondPredicateComparator(DateComparator comparator, CDate secondPredicate) {
        this.secondPredicate = secondPredicate;
        this.comparator = comparator;
    }

    public boolean execute(CDate firstPredicate) {
        return comparator.execute(firstPredicate, secondPredicate);
    }

    public ObjectComparator getComparator() {
        return comparator;
    }
}

abstract class DateComparator implements ObjectComparator {
    public abstract boolean execute(CDate first, CDate second);
    public boolean execute(Object first, Object second) {
        return execute((CDate) first, (CDate) second);
    }
}

class LessDate extends DateComparator {
    public boolean execute(CDate first, CDate second) {
        if (first == null) {
            if (second == null) return false;
            return true;
        }
        if (second == null) return false;
        return (first.compareTo(second) < 0);
    }
}

class LessEqualDate extends DateComparator {
    public boolean execute(CDate first, CDate second) {
        if (first == null) {
            if (second == null) return true;
            return true;
        }
        if (second == null) return false;
        return (first.compareTo(second) <= 0);
    }
}

class GreaterDate extends DateComparator {
    public boolean execute(CDate first, CDate second) {
        if (first == null) {
            if (second == null) return false;
            return false;
        }
        if (second == null) return true;
        return (first.compareTo(second) > 0);
    }
}

class GreaterEqualDate extends DateComparator {
    public boolean execute(CDate first, CDate second) {
        if (first == null) {
            if (second == null) return true;
            return false;
        }
        if (second == null) return true;
        return (first.compareTo(second) >= 0);
    }
}

class EqualDate extends DateComparator {
    public boolean execute(CDate first, CDate second) {
        if (first == null) {
            if (second == null) return true;
            return false;
        }
        if (second == null) return false;
        return (first.compareTo(second) == 0);
    }
}

class NotEqualDate extends DateComparator {
    public boolean execute(CDate first, CDate second) {
        if (first == null) {
            if (second == null) return false;
            return true;
        }
        if (second == null) return true;
        return (first.compareTo(second) != 0);
    }
}

