/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 20, 2002
 * Time: 2:18:26 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database.engine;

import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.ObjectBaseArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.eslate.utils.StorageStructure;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectInput;

/** IntegerTableFieldView implements a view on a IntegerTableField, called the base field. A view of a table field is
 *  an AbstractTableField itself which:
 *  <ul>
 *  <li> Shares the dame data with base field. The data of the field view is a subset of the data of the base field.
 *  <li> Share the same name.
 *  <li> Their 'calculated' status is the synchronized.
 *  </ul>
 *  Field views are used as the fields of <code>TableView</code>s. A field view can not be attached to a Table.
 */
// The 'data' IntBaseArray of the IntegerTableFieldView is always empty.
public class IntegerTableFieldView extends IntegerTableField implements TableFieldView {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    /** The field on which this view is based. */
    IntegerTableField baseField = null;
    /** This has the same value as the <code>table</code> data member inherited from <code>AbstractTableField</code>.
     *  It is used to avoid continuous casts of the field views's <code>table</code> to <code>TableView</code>.
     */
    private TableView tableView = null;
    /** This is valid only while <code>baseField</code> is null. And this happens only while a TableView is restored.
     *  The TableFieldViews are restored as part of the TableView restore process. However the <code>baseTable</code>
     *  of the TableView is assigned after the TableView is restored, cause the TableView does not persist the Table.
     *  The TableFieldView stores the name of its <code>baseField</code>, so that when the Table is assigned back to
     *  the TableView, the TableFieldView can find its <code>baseField</code>.
     */
    private String baseFieldName = null;

    public IntegerTableFieldView() {
    }

    protected IntegerTableFieldView(IntegerTableField field) {
        super(field.getName(), field.isEditable(), field.isRemovable(), field.isHidden());
        baseField = field;
    }

    /** Sets the TableView of the IntegerTableFieldView */
    void setTable(Table table) {
        if (tableView == table) return;
        this.table = table;
        tableView = (TableView) this.table;
        // If the 'baseField' has not been set, then the TableFieldView was constructed with the 0-arg constructor
        // which is used only by the Externalization mechanism. In this case the name of the <code>baseField</code>
        // was stored and restored, so find it in the <code>table</code>.
        if (baseField == null && baseFieldName != null) {
            try{
                baseField = (IntegerTableField) tableView.getBaseTable().getTableField(baseFieldName);
            }catch (InvalidFieldNameException exc) {
                System.out.println("InvalidFieldNameException in IntegerTableFieldView setTable(). Should never occur...");
            }
            baseFieldName = null;
        }
    }

    /**
     * @see TableFieldView#getBaseField()
     */
    public AbstractTableField getBaseField() {
        return baseField;
    }

    protected void setName(String s) {
//        baseField.setName(s);
        super.setName(s);
    }

/*    protected boolean setEditable(boolean isEditable) throws AttributeLockedException {
        if (!baseField.setEditable(isEditable))
            return false;
        return super.setEditable(isEditable);
    }
*/
    // No need to override. The removability of the field views in a TableView is irrelevant to the removability of the
    // source field in the Table.
//    protected void setRemovable(boolean removable) throws AttributeLockedException {}

    // No need to override. The hidden state of the field views in a TableView is irrelevant to the hidden state of the
    // source field in the Table.
//    protected void setHidden(boolean hidden) throws AttributeLockedException {}

    // Changing a field to calculated or vice-versa can cause the data of the field to change. This attribute has to be
    // synchronized with the corresponding attribute of the source field.
    protected void setCalculated(boolean isCalc, String formula, String textFormula, IntBaseArray dependentFields)
            throws AttributeLockedException {
        baseField.setCalculated(isCalc, formula, textFormula, dependentFields);
        super.setCalculated(isCalc, formula, textFormula, dependentFields);
    }

    // No need to override. The edita of the field views in a TableView is irrelevant to the hidden state of the
    // source field in the Table.
/*    protected void setFieldEditabilityChangeAllowed(boolean allowed) {
        baseField.setFieldEditabilityChangeAllowed(allowed);
        super.setFieldEditabilityChangeAllowed(allowed);
    }
*/
/*    protected void setFieldRemovabilityChangeAllowed(boolean allowed) {
        baseField.setFieldRemovabilityChangeAllowed(allowed);
        super.setFieldRemovabilityChangeAllowed(allowed);
    }

    protected void setFieldDataTypeChangeAllowed(boolean allowed) {
        baseField.setFieldDataTypeChangeAllowed(allowed);
        super.setFieldDataTypeChangeAllowed(allowed);
    }
*/
    // Synchronized with the base field.
    protected void setCalcFieldResetAllowed(boolean allowed) {
        baseField.setCalcFieldResetAllowed(allowed);
        super.setCalcFieldResetAllowed(allowed);
    }

    /** Keys in TableViews are not allowed. This method is overriden to ensure the field will not become part of the
     *  TableView's key.
     **/
    protected void setFieldKeyAttributeChangeAllowed(boolean allowed) {
        super.setFieldKeyAttributeChangeAllowed(false);
    }

/*    protected void setFieldHiddenAttributeChangeAllowed(boolean allowed) {
        super.setFieldHiddenAttributeChangeAllowed(allowed);
    }
*/
    protected void setCalcFieldFormulaChangeAllowed(boolean allowed) {
        super.setCalcFieldFormulaChangeAllowed(allowed);
    }

    void updateDependentCalculatedFields(int recIndex) throws DuplicateKeyException, NullTableKeyException {
        throw new RuntimeException("Invalid method for TableFieldViews updateDependentCalculatedFields()");
    }

    public int getValue(int recIndex) throws InvalidCellAddressException {
        return baseField.getValue(tableView.records.get(recIndex));
    }

    public Integer getCell(int recIndex) throws InvalidCellAddressException {
        return baseField.getCell(tableView.records.get(recIndex));
    }

    public Object getCellObject(int recIndex) throws InvalidCellAddressException {
        return baseField.getCellObject(tableView.records.get(recIndex));
    }

    Integer getValueAt(int recIndex) {
        return baseField.getValueAt(tableView.records.get(recIndex));
    }

    Object getObjectAt(int recIndex) {
        return baseField.getObjectAt(tableView.records.get(recIndex));
    }

    public boolean setCell(int recIndex, Object value) throws AttributeLockedException, InvalidCellAddressException,
            InvalidDataFormatException, NullTableKeyException, DuplicateKeyException {
        return baseField.setCell(tableView.records.get(recIndex), value);
    }

    public boolean setCell(int recIndex, int value) throws AttributeLockedException, InvalidCellAddressException,
            InvalidDataFormatException, NullTableKeyException, DuplicateKeyException {
        return baseField.setCell(tableView.records.get(recIndex), value);
    }

    void updateDependentCalculatedFields(int recIndex, int previousValue) throws DuplicateKeyException, NullTableKeyException {
        throw new RuntimeException("Invalid method for TableFieldViews updateDependentCalculatedFields()");
//        super.updateDependentCalculatedFields(recIndex, previousValue);
    }

    boolean equalsValueAt(int recIndex, Integer value) {
        return baseField.equalsValueAt(tableView.records.get(recIndex), value);
    }

    void set(int recIndex, Object value) {
        baseField.set(tableView.records.get(recIndex), value);
    }

    void set(int recIndex, int value) {
        baseField.set(tableView.records.get(recIndex), value);
    }

    int get(int recIndex) {
        return baseField.get(tableView.records.get(recIndex));
    }

    void add(Object value) {
        baseField.add(value);
    }

    // Removing a record from a TableFieldView does not remove it from the underlying Table.
    void remove(int recIndex) {
//        baseField.remove(recIndex);
    }

    int size() {
        return tableView.records.size();
    }

    public ArrayBase getData() {
        int dataSize = size();
        IntBaseArray array = new IntBaseArray(dataSize);
        for (int i=0; i<dataSize; i++)
            array.add(get(i));
        return array;
    }

    public int indexOf(int start, int end, Object value) {
        int index = baseField.indexOf(start, end, value);
        int viewIndex = tableView.records.indexOf(start, end, index);
        return viewIndex;
    }

    public int indexOf(Object value) {
        return indexOf(0, size()-1, value);
    }

    public Object[] getDataArray() {
        Integer[] ar = new Integer[size()];
        for (int i=0; i<ar.length; i++)
            ar[i] = new Integer(get(i));
        return ar;
    }

    public ObjectBaseArray getDataBaseArray() {
        int dataSize = size();
        ObjectBaseArray ar = new ObjectBaseArray(dataSize);
        for (int i=0; i<dataSize; i++)
            ar.add(i, new Integer(get(i)));
        return ar;
    }

    public ArrayList getDataArrayList() {
        int dataSize = size();
        ArrayList ar = new ArrayList(dataSize);
        for (int i=0; i<dataSize; i++)
            ar.add(i, new Integer(get(i)));
        return ar;
    }

    void setData(ArrayList al) {
        throw new RuntimeException("Invalid method for TableFieldViews setData(ArralyList)");
    }

/*    public void findNext(FieldDataIterator iterator, SecondPredicateComparator predicateComparator) {
        super.findNext(iterator, predicateComparator);
    }

    public FieldDataIterator iterator() {
        return super.iterator();
    }

    public FieldDataIterator iterator(int firstIndex) {
        return super.iterator(firstIndex);
    }

    public FieldDataIterator iterator(IntBaseArrayDesc recIndices, int firstIndex) {
        return super.iterator(recIndices, firstIndex);
    }
*/
    public boolean compareTo(int recIndex, ObjectComparator comparator, Object value) {
        return baseField.compareTo(tableView.records.get(recIndex), comparator, value);
    }

    public boolean compareTo(int recIndex, IntegerComparator comparator, int value) {
        return baseField.compareTo(tableView.records.get(recIndex), comparator, value);
    }

    boolean compareTo(int recIndex, ObjectComparator comparator, AbstractTableField f, int recIndex2) {
        return baseField.compareTo(tableView.records.get(recIndex), comparator, f, recIndex2);
    }

    public IntBaseArray getOrder(ObjectComparator comparator) {
        IntBaseArray data = (IntBaseArray) getData();
        return getOrder(data, 0, data.size()-1, (IntegerComparator) comparator);
    }

    public IntBaseArray getOrder(int first, int last, ObjectComparator comparator) {
        IntBaseArray data = (IntBaseArray) getData();
        return getOrder(data, first, last, (IntegerComparator) comparator);
    }

    void setNewCellValue(Object value) {
        baseField.setNewCellValue(value);
    }

    void setNewCellValue(int value) {
        baseField.setNewCellValue(value);
    }

    Object getNewCellValue() {
        return baseField.getNewCellValue();
    }

    void commitNewCellValue() {
        baseField.commitNewCellValue();
    }

    public StorageStructure recordState() {
        StorageStructure ss = super.recordState();
        ss.put("Base Field Name", baseField.getName());
        return ss;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(recordState());
    }

    public void applyState(StorageStructure fieldMap) {
        super.applyState(fieldMap);
        baseFieldName = (String) fieldMap.get("Base Field Name");
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        applyState(fieldMap);
    }

    /**
     * Dispatches the method call to the base field.
     * @return The currency of the base field.
     * @see IntegerTableField#getCurrency()
     */
    public int getCurrency() {
        return baseField.currency;
    }

    /**
     * Dispatches the method call to the base field.
     * @see IntegerTableField#setCurrency(int)
     */
    public void setCurrency(int currency) {
        baseField.setCurrency(currency);
    }

}
