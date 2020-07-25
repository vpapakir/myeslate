package gr.cti.eslate.database.engine;

import gr.cti.typeArray.*;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

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

public class StringTableField extends AbstractTableField implements Externalizable {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    /** The data type of the data of the StringTableField. */
    public static final Class DATA_TYPE = String.class;
    private StringBaseArray data = new StringBaseArray();
    private static final HashMap queryComparators = new HashMap();
    /** While adding a new record and before this is validated against the key constaint of the Table, this
     *  variable holds the value of the cell of the new record for this field.
     */
    private String newCellValue = null;

    static {
        queryComparators.put("=", new EqualString());
        queryComparators.put("<=", new LessEqualString());
        queryComparators.put("<", new LessString());
        queryComparators.put(">", new GreaterString());
        queryComparators.put(">=", new GreaterEqualString());
        queryComparators.put("!=", new NotEqualString());
        queryComparators.put(DBase.resources.getString("contained"), new ContainedInString());
        queryComparators.put(DBase.resources.getString("contains"), new ContainsString());
    }

	public StringTableField() {
	}

	protected StringTableField(String name, boolean editable, boolean removable, boolean hidden) {
		fieldName = name;
		isEditable = editable;
		isRemovable = removable;
//		isKey = key;
		this.isHidden = hidden;
//        this.table = table;
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

    /** Returns the class <code>java.lang.String</code>. */
	public final Class getDataType() {
		return DATA_TYPE;
	}

    /** Returns the localized data type name of the StringTableField. */
    public String getLocalizedDataTypeName() {
        return DBase.resources.getString("Alphanumeric");
    }

    /**
     *  @return Returns true, if <code>value</code> is null. <code>false</code> otherwise.
     */
    public static final boolean isNull(String value) {
        return (value == null);
    }

    /** Checks if the field's cell at <code>recordIndex</code> is null.
     *  @return true, if the value of the field at <code>recordIndex</code> is <code>null</code>. False, otherwise.
     */
    public final boolean isCellNull(int recordIndex) {
        return isNull(data.get(recordIndex));
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value of the cell as a String.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public String getCell(int recIndex)  throws InvalidCellAddressException {
        if (recIndex < 0 || recIndex >= data.size())
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);
        return getValueAt(recIndex);
    }

    /**
     * @see AbstractTableField#getCellAsString(int)
     */
    public String getCellAsString(int recIndex) {
        return getValueAt(recIndex);
    }

    /** Returns the contents of the cell at <code>recIndex</code> of this field.
     *  @param recIndex The index of the record which contains the cell.
     *  @return The value of the cell as an Object.
     *  @exception InvalidCellAddressException Invalid cell address.
     */
    public Object getCellObject(int recIndex)  throws InvalidCellAddressException {
        return getCell(recIndex);
    }

    /** Returns the value of this field at <code>recIndex</code> as a Sring. */
    String getValueAt(int recIndex) {
        return data.get(recIndex);
    }

    /** Returns the value of this field at <code>recIndex</code> as an Object. */
    Object getObjectAt(int recIndex) {
//System.out.println("getObjectAt recIndex: " + recIndex + ", data: " + getDataArrayList() + ", field: " + fieldName + ", size: " + data.size());
        return data.get(recIndex);
    }

    /**
     *  Sets the specified cell of the StringTableField to <code>value</code>.
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
//System.out.println("setCell(" + recIndex + ", " + value + ")");
        // Convert the Object 'value' to String.
        String newValue = null;
        if (value != null) {
            if (!getDataType().isInstance(value)) {
                newValue = (String) table.convertToFieldType(value, this);
            }else
                newValue = (String) value;
        }
        if (newValue != null && newValue.length() == 0)
            newValue = null;

        boolean pendingNewRecord = table.getRecordEntryStructure().isRecordAdditionPending();
        if (!table.dataChangeAllowed && !pendingNewRecord)
            throw new AttributeLockedException(Table.bundle.getString("CTableMsg89"));

        if (recIndex > table.recordCount || recIndex < 0)
            throw new InvalidCellAddressException(Table.bundle.getString("CTableMsg54") + fieldName + Table.bundle.getString("CTableMsg53") + recIndex);

        if (!isEditable)
            return false;

        String currentValue = data.get(recIndex);
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

        data.set(recIndex, newValue);
        try{
            updateDependentCalculatedFields(recIndex);
        }catch (Throwable e) {
            if (!pendingNewRecord) {
                table.changedCalcCells.clear();
                data.set(recIndex, currentValue);
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
        String newValue = (String) value;
        if (newValue != null && newValue.length() == 0)
            newValue = null;
        data.set(recIndex, newValue);
    }

    /** Directly sets the value of the cell at <code>recIndex</code> of this field. */
    void set(int recIndex, String value) {
        data.set(recIndex, value);
    }

    /** Returns the value of the cell at <code>recIndex</code>. */
    String get(int recIndex) {
        return data.get(recIndex);
    }

    void add(Object value) {
        data.add((String) value);
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
        if (value != null && !String.class.isAssignableFrom(value.getClass()))
            return -1;
        String s = (String) value;
        return data.indexOf(start, end, s);
    }

    public int indexOf(Object value) {
        return indexOf(0, data.size()-1, value);
    }

    public Object[] getDataArray() {
        return data.toArray();
    }

    public ObjectBaseArray getDataBaseArray() {
        int dataSize = data.size();
        ObjectBaseArray ar = new ObjectBaseArray(dataSize);
        for (int i=0; i<dataSize; i++)
            ar.add(i, data.get(i));
        return ar;
    }

    public ArrayList getDataArrayList() {
        ArrayList al = new ArrayList(data.size());
        for (int i=0; i<data.size(); i++)
            al.add(data.get(i));
        return al;
    }

    // Should be abstract
    void setData(ArrayList al) {
        data.clear();
        for (int i=0; i<al.size(); i++)
            data.add((String) al.get(i));
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
        while (!iterator.atEnd() && (!((StringSecondPredicateComparator) predicateComparator).execute(((StringFieldDataIterator) iterator).get())))
          iterator.advance();
    }
    // Old implementation, based on the architecture of the JGL ArrayIterator.
/*    public FieldDataIterator findNext(FieldDataIterator first, FieldDataIterator last, SecondPredicateComparator operandComparator) {
        StringFieldDataIterator firstx = (StringFieldDataIterator) first.clone();
        while (!firstx.equals(last) && (*//*firstx.get() == null || */ /*!((StringSecondPredicateComparator) operandComparator).execute(firstx.get())))
          firstx.advance();

        return firstx;
    }
*/
    /** Returns the Comparator class, which corresponds to the supplied string representation of the comparator.
     *  The comparators supported for StringTableFields are:
        <table border="1" width="100%">
          <tr>
            <td width="50%" align="center"><i>Comparator string representation</i></td>
            <td width="50%" align="center"><i>Comparator java class</i></td>
          </tr>
          <tr>
            <td width="50%" align="center">=</td>
            <td width="50%" align="center">EqualString</td>
          </tr>
          <tr>
            <td width="50%" align="center">!=</td>
            <td width="50%" align="center">NotEqualString</td>
          </tr>
          <tr>
            <td width="50%" align="center">&lt;</td>
            <td width="50%" align="center">LessString</td>
          </tr>
          <tr>
            <td width="50%" align="center">&lt;=</td>
            <td width="50%" align="center">LessEqualString</td>
          </tr>
          <tr>
            <td width="50%" align="center">></td>
            <td width="50%" align="center">GreaterString</td>
          </tr>
          <tr>
            <td width="50%" align="center">>=</td>
            <td width="50%" align="center">GreaterEqualString</td>
          </tr>
          <tr>
            <td width="50%" align="center">contains*</td>
            <td width="50%" align="center">ContainsString</td>
          </tr>
          <tr>
            <td width="50%" align="center">contained*</td>
            <td width="50%" align="center">ContainedInString</td>
          </tr>
        </table>
        <p><font size="1">* Localized version of these keywords</font></p>
     */
    public static final StringComparator getComparator(String comparatorStr) {
        return (StringComparator) queryComparators.get(comparatorStr);
    }

    /** Delegates functionality to <code>getComparator()</code>.
     *  @see #getComparator(String)
     */
    public final ObjectComparator getComparatorFor(String comparatorStr) {
        return getComparator(comparatorStr);
    }

    /** Returns a copy of the comparators supported by the StringTableField.
     */
    public static final HashMap getComparators() {
        return (HashMap) queryComparators.clone();
    }


    /** Creates an iterator on the field's data. The iterator will go through all the data of the field.
     *  @return An instance of StringFieldDataIterator.
     *  @see #iterator(IntBaseArrayDesc, int)
     */
    public FieldDataIterator iterator() {
        return iterator(0);
    }

    /** Creates an iterator on the field's data. The iterator will go through all the data of the field and
     *  will be positioned at <code>startIndex</code>.
     *  @return An instance of StringFieldDataIterator.
     *  @see #iterator(IntBaseArrayDesc, int)
     */
    public FieldDataIterator iterator(int firstIndex) {
        int dataSize = data.size();
        IntBaseArrayDesc recIndices = new IntBaseArrayDesc(dataSize);
        for (int i=0; i<dataSize; i++)
            recIndices.add(i);
        return new StringFieldDataIterator(recIndices, firstIndex, this);
    }

    /** Creates an iterator on the field's data. The iterator
     *  will go through only the field data of the records specified by <code>recIndices</code> and it will begin the
     *  iteration from the record at <code>firstIndex</code> in the <code>recIndices</code> array.
     *  @param recIndices The indices of the records on which the iteration will take place.
     *  @param firstIndex The index of the record in the <code>recIndices</code> array, where the iteration will start from.
     *  @return An instance of StringFieldDataIterator.
     */
    public FieldDataIterator iterator(IntBaseArrayDesc recIndices, int firstIndex) {
        return new StringFieldDataIterator(recIndices, firstIndex, this);
    }

    /** Compares the value at <code>recIndex</code> of the AbstractTableField to the given <code>value</code> using the
     *  specified <code>comparator</code>.
     *  @param recIndex The record index of the data of the field, which will be compared against <code>value</code>.
     *  @param comparator The comparator used.
     *  @param value The second operand of the operation.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(String)
     */
    public boolean compareTo(int recIndex, StringComparator comparator, String value) {
        return comparator.execute(data.get(recIndex), value);
    }

    /** Compares the cell at <code>recIndex</code> of this field to the cell at <code>recIndex2</code> of AbstractTableField
     *  <code>f</code> using the specified comparator.
     *  @param recIndex The record index of the cell of this field.
     *  @param comparator The comparator.
     *  @param f The second field.
     *  @param recIndex2 The record index of the cell of the second field.
     *  @return <code>true</code> or <code>false</code>, depending on the semantics of the <code>comparator</code>.
     *  @see #getComparator(String)
     *  @see #compareTo(int, StringComparator, String)
     */
    boolean compareTo(int recIndex, ObjectComparator comparator, AbstractTableField f, int recIndex2) {
        return compareTo(recIndex, (StringComparator) comparator, f.getObjectAt(recIndex2).toString());
    }

    /** @see #compareTo(int, StringComparator, String)
     */
    public boolean compareTo(int recIndex, ObjectComparator comparator, Object value) {
        if (value == null)
            return compareTo(recIndex, (StringComparator) comparator, (String) null);
        return compareTo(recIndex, (StringComparator) comparator, value.toString());
    }

    /** Sorts the elements of <code>data<code> array using the specified comparator. The <code>data</code> is sorted,
     *  after the method finishes execution.
     *  @param data The IntBaseArray to sort.
     *  @param comparator The Comparator used to sort the array.
     *  @see #sort(ObjectBaseArray, int, int, StringComparator)
     */
    public void sort(ObjectBaseArray data, StringComparator comparator) {
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
    public void sort(ObjectBaseArray data, int first, int last, StringComparator comparator) {
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
//System.out.println("getOrder() comparator: " + comparator.getClass() + " size: " + data.size());
        return getOrder(0, data.size()-1, comparator);
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
     *  @see #getOrder(ObjectBaseArray, int, int, StringComparator)
     */
    public IntBaseArray getOrder(int first, int last, ObjectComparator comparator) {
        int dataSize = data.size();
        ObjectBaseArray dataClone = new ObjectBaseArray(dataSize);
//System.out.print("dataClone: ");
        for (int i=0; i<dataSize; i++) {
            dataClone.add(data.get(i));
//System.out.print(dataClone.get(i) + ", ");
        }
//System.out.println();

        return getOrder(dataClone, first, last, (StringComparator) comparator);
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
    public IntBaseArray getOrder(ObjectBaseArray data, int first, int last, StringComparator comparator) {
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
        newCellValue = (String) value;
    }

    /**
     *  @see AbstractTableField#getNewCellValue()
     */
    Object getNewCellValue() {
        return newCellValue;
    }

    /**
     *  @see AbstractTableField#getNewCellValue()
     */
    String getNewCellValueAsString() {
        return newCellValue;
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
        fieldMap.put("Operand expression", (StringOperandExpression) oe);
        fieldMap.put("Data", data);
        return fieldMap;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(recordState());
    }

    public void applyState(StorageStructure fieldMap) {
        super.applyState((StorageStructure) fieldMap.get("Common State"));
        oe = (StringOperandExpression) fieldMap.get("Operand expression");
        data = (StringBaseArray) fieldMap.get("Data");
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        applyState(fieldMap);
    }

}

class StringSecondPredicateComparator implements SecondPredicateComparator {
    String secondPredicate = null;
    StringComparator comparator = null;

    public StringSecondPredicateComparator(StringComparator comparator, String secondPredicate) {
        this.secondPredicate = secondPredicate;
        this.comparator = comparator;
    }

    public boolean execute(String firstPredicate) {
        return comparator.execute(firstPredicate, secondPredicate);
    }

    public ObjectComparator getComparator() {
        return comparator;
    }
}

abstract class StringComparator implements ObjectComparator {
    /** String comparison operation. */
    public abstract boolean execute(String first, String second);
    /** More general comparison operation, which compares a String to any object. */
    public boolean execute(String first, Object second) {
        if (second == null)
            return execute(first, null);
        return execute(first, second.toString());
    }
    public boolean execute(Object first, Object second) {
        String firstStr = null, secondStr = null;
        if (first != null) firstStr = first.toString();
        if (second != null) secondStr = second.toString();
//System.out.println("firstStr: " + firstStr + ", secondStr: " + secondStr + ", execute: " + execute(firstStr, secondStr));
        return execute(firstStr, secondStr);
    }
}

class LessString extends StringComparator {
    public boolean execute(String first, String second) {
        if (first == null) {
            if (second == null) return false;
            return true;
        }
        if (second == null) return false;
        return (first.compareTo(second) < 0);
    }
}

class LessEqualString extends StringComparator {
    public boolean execute(String first, String second) {
        if (first == null) {
            if (second == null) return true;
            return true;
        }
        if (second == null) return false;
        return (first.compareTo(second) <= 0);
    }
}

class GreaterString extends StringComparator {
    public boolean execute(String first, String second) {
        if (first == null) {
            if (second == null) return false;
            return false;
        }
        if (second == null) return true;
        return (first.compareTo(second) > 0);
    }
}

class GreaterEqualString extends StringComparator {
    public boolean execute(String first, String second) {
        if (first == null) {
            if (second == null) return true;
            return false;
        }
        if (second == null) return true;
        return (first.compareTo(second) >= 0);
    }
}

class EqualString extends StringComparator {
    public boolean execute(String first, String second) {
        if (first == null) {
            if (second == null) return true;
            return false;
        }
        if (second == null) return false;
        return (first.compareTo(second) == 0);
    }
}

class NotEqualString extends StringComparator {
    public boolean execute(String first, String second) {
        if (first == null) {
            if (second == null) return false;
            return true;
        }
        if (second == null) return true;
        return (first.compareTo(second) != 0);
    }
}

