/*
* Created by IntelliJ IDEA.
* User: tsironis
* Date: 23 Οκτ 2002
* Time: 12:41:03 μμ
* To change template for new class use
* Code Style | Class Templates options (Tools | IDE Options).
*/
package gr.cti.eslate.database.engine;

import gr.cti.typeArray.IntBaseArray;
import gr.cti.eslate.tableModel.event.ColumnKeyChangedEvent;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/** TableKey class takes care of the manipulation of keys in a Table. It guarantees key uniqueness in the Table */
// The implementation at this point is very simple. The primary key is separated from the rest of the key fields.
// For each value in the primary key, its hashcode is stored in a HashMap and it is mapped to the indices of the
// records which share this primary key. So whenever a new record is added, looking up it's key is fast and we easily
// get the indices of the records which share the same primary key, in order to check the key values of the new record for
// uniqueness.
public class TableKey implements Externalizable {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;
    /** The index of the primary key field of the Table.*/
    int primaryKeyFieldIndex = -1;
    /** The indices of the secondary key fields */
    IntBaseArray secondaryKeyFieldIndices = new IntBaseArray();
    /** Stores the hash codes of the values of the primary key field of the Table. Each hash code is
     *  mapped to the indices of the records in the Table that share the same primary key.
     */
    HashMap primaryKeyFieldHash = null;
    /** The table for which this TableKey instance provides key support */
    Table table = null;

    // Constructor for Externalization mechanism
    public TableKey() {
    }

    public TableKey(Table table) {
        this.table = table;
    }

    public boolean keyExists() {
        return (primaryKeyFieldIndex != -1);
    }

    boolean isPartOfTableKey(AbstractTableField field) {
//        System.out.println("field.indexInTable: " + field.indexInTable);
//        System.out.println("primaryKeyFieldIndex: " + primaryKeyFieldIndex);
//        System.out.print("secondaryKeyFieldIndices: ");
//        for (int i=0; i<secondaryKeyFieldIndices.size(); i++)
//            System.out.println(secondaryKeyFieldIndices.get(i));
//        System.out.println();
        if (primaryKeyFieldIndex == field.indexInTable || (secondaryKeyFieldIndices.indexOf(field.indexInTable) != -1))
            return true;
        return false;
    }

    void addKeyField(AbstractTableField f) throws InvalidKeyFieldException, AttributeLockedException, FieldNotEditableException,
            FieldAlreadyInKeyException, FieldContainsEmptyCellsException, TableNotExpandableException {
        if (f.getDataType().equals(CImageIcon.class))
            throw new InvalidKeyFieldException(table.bundle.getString("CTableMsg67"));
        if (!f.isFieldKeyAttributeChangeAllowed())
            throw new AttributeLockedException(table.bundle.getString("CTableFieldMsg1"));
        if (!table.fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(table.bundle.getString("CTableMsg103") + " \"" + table.getTitle() + '\"');

//        System.out.println("Field " + fieldName + " isKey: " + f.isKey());
        if (isPartOfTableKey(f))
            throw new FieldAlreadyInKeyException(table.bundle.getString("CTableMsg100") + f.getName() + table.bundle.getString("CTableMsg68"));
        else{
            //Check if this column contains any "null" values
            for (int i = 0; i<table.recordCount; i++) {
                if (f.getObjectAt(i) == null)
                    throw new FieldContainsEmptyCellsException(table.bundle.getString("CTableMsg100") + f.getName() + table.bundle.getString("CTableMsg69"));
            }

/*            try{
f.setKey(true);
}catch (AttributeLockedException e) {System.out.println("Serious inconsistency error in CTable _addToKey() : (0.5)"); return;}
*/
            boolean keyExists = keyExists();
            if (keyExists) {
                secondaryKeyFieldIndices.add(f.indexInTable);
            }else{
//                OrderedMap temp2 = new OrderedMap();
//                primaryKeyMap = new OrderedMap(true);
//                HashMap temp2 = new HashMap(new EqualTo());
//                primaryKeyMap = new HashMap(new EqualTo(), true, 10, (float) 1.0);
//                Hashtable temp2 = new Hashtable();
                primaryKeyFieldHash = new HashMap(10, (float) 1.0);
//                System.out.println(primaryKeyMap);
//                ArrayList temp = (ArrayList) tableData.get(fieldIndex);
                //Parse all the records, but the last one
                Object res;
                Integer hashCode = null;
                int i=0;
                while (i<table.recordCount) {
                    hashCode = new Integer(f.getObjectAt(i).hashCode());
                    // Gets the records which share the same primary key.
                    IntBaseArray recIndices = (IntBaseArray) primaryKeyFieldHash.get(hashCode);
//                    res = temp2.add(f.getObjectAt(i), new Integer(i)); //2.0.1 riskyGetRecord(i));
//                    System.out.println("KeyValue: " + temp.at(i) + ", " + res + " Index: " + i);
                    // This is the first field in the Table which becomes part of the key.
                    if (recIndices != null) {
//                        System.out.println("Removing record #" + i);
                        try{
                            table.removeRecord(i, -1, true);
                        }catch (InvalidRecordIndexException e) {
                            primaryKeyFieldHash = null;
//                            System.out.println(e.message);
/*                            try{
f.setKey(false);
}catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in CTable addToKey() : (1)");}
catch (AttributeLockedException e1) {System.out.println("Serious inconsistency error in Table addToKey() : (1.5)");}
*/
                        }
                        catch (TableNotExpandableException e) {
                            primaryKeyFieldHash = null;
/*                            try{
f.setKey(false);
}catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table addToKey() : (2)");}
catch (AttributeLockedException e1) {System.out.println("Serious inconsistency error in Table addToKey() : (2.5)");}
*/
                            throw new TableNotExpandableException(table.bundle.getString("CTableMsg70"));
                        }
                        i--;
                    }
                    // Insert the hashcode of the primary key value for this record into the primaryKeyHashCodes
                    // associated with the record index.
                    addRecordToPrimaryKeyHash(hashCode, i);
                    i++;
                }

                //Copy the contents of the "temp2" OrderedMap to "primaryKeyMap" using
                //the hash codes of the keys of the elements of "temp2" as keys in "primaryKeyMap"
//                OrderedMapIterator iter = temp2.begin();
/*                HashMapIterator iter = temp2.begin();
while (!iter.atEnd()) {
primaryKeyMap.add(new Integer(iter.key().hashCode()), iter.value());
iter.advance();
}
*/
//                hasKey = true;
                primaryKeyFieldIndex = f.indexInTable;
//                keyFieldIndices.add(fieldIndex);
//                System.out.println("Field " + fieldName + " isKey: " + f.isKey());
            }

            /* Adjust the active record.
            */
            if (table.activeRecord != -1) {
                if (table.activeRecord > (table.recordCount-1)) {
                    table.setActiveRecord(table.recordCount-1);
                }else{
                    if (table.activeRecord + 1 < table.recordCount) {
                        table.setActiveRecord(table.activeRecord+1);
                    }else{
                        table.setActiveRecord(table.activeRecord);
                    }
                }
            }

            table.setModified();
            table.fireColumnKeyChanged(f, true);
        }
    }

    void removeKeyField(AbstractTableField f) throws AttributeLockedException, FieldNotEditableException, FieldIsNotInKeyException,
            TableNotExpandableException {
        if (!f.isFieldKeyAttributeChangeAllowed())
            throw new AttributeLockedException(table.bundle.getString("CTableFieldMsg1"));

        if (!table.fieldPropertyEditingAllowed)
            throw new FieldNotEditableException(table.bundle.getString("CTableMsg103") + " \"" + table.getTitle() + '\"');

        if (!isPartOfTableKey(f))
            throw new FieldIsNotInKeyException(table.bundle.getString("CTableMsg100") + f.getName() + table.bundle.getString("CTableMsg71"));

System.out.println("primaryKeyFieldIndex: " + primaryKeyFieldIndex+ ", secondaryKeyFieldIndices.size(): " + secondaryKeyFieldIndices.size() + ", field index: " + f.indexInTable);
        // If there is no secondary key in the Table, then this has to be the primary key of the Table.
        // which is simply removed and the Table contains no key.
        if (secondaryKeyFieldIndices.size() == 0) {
//                keyFieldIndices.clear();
            primaryKeyFieldHash.clear();
            primaryKeyFieldHash = null;
            primaryKeyFieldIndex = -1;
//                hasKey = false;
            /* No exception should be thrown here, because only "setKey(true)" can
            * fire an exception.
            */
/*                try{
f.setKey(false);
}catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table removeFromKey() : (1)");}
*/
            table.setModified();
        }else{ // There exist secondary keys in the table
            //Keep this back-up copy of primaryKeyMap and keyFieldIndices
            int primaryKeyFieldIndexBackUp = primaryKeyFieldIndex;
            HashMap primaryKeyHashBackUp = (HashMap) primaryKeyFieldHash.clone();
            IntBaseArray secondaryKeyFieldIndicesBackUp = (IntBaseArray) secondaryKeyFieldIndices.clone();

            /* No exception should be thrown here, because only "setKey(true)" can
            * fire an exception.
            */
/*                try{
f.setKey(false);
}catch (InvalidKeyFieldException e) {System.out.println("Serious inconsistency error in Table removeFromKey() : (2)");}
*/
            table.setModified();
            int fieldIndex = f.indexInTable;
            // In this case the primary key of the Table is removed, but there exist secondary keys. The first of these
            // will become the next primary key of the Table.
            if (primaryKeyFieldIndex == fieldIndex) {
//                    System.out.println("Removed from keyFieldIndices: " + keyFieldIndices.remove(new Integer(fieldIndex)) + " elements");
//                    keyFieldIndices.remove(fieldIndex);
                primaryKeyFieldIndex = secondaryKeyFieldIndices.get(0);
System.out.println("1. primaryKeyFieldIndex: " + primaryKeyFieldIndex);
                secondaryKeyFieldIndices.remove(0);
                AbstractTableField newPrimaryKeyField = table.tableFields.get(primaryKeyFieldIndex);

                primaryKeyFieldHash.clear();
                Integer primKeyValueHashCode;
                Integer dublicateValueRecordIndex;
                int i = 0;
                while (i<table.recordCount) {
                    primKeyValueHashCode = new Integer(newPrimaryKeyField.getObjectAt(i).hashCode());
//*                        System.out.println("Processing record #" + i + " with keyValue: " + keyValue);
                    IntBaseArray recIndices = (IntBaseArray) primaryKeyFieldHash.get(primKeyValueHashCode);
                    if (recIndices == null) {
                        addRecordToPrimaryKeyHash(primKeyValueHashCode, i);
//                    if (primaryKeyMap.count(new Integer(keyValue.hashCode())) == 0) {
//                        primaryKeyMap.add(new Integer(keyValue.hashCode()), new Integer(i)); //2.0.1 riskyGetRecord(i));
                    }else{
//                        int newPrimaryKeyFieldIndex = keyFieldIndices.get(0);
//                        Enumeration e = primaryKeyMap.values(new Integer(keyValue.hashCode()));
                        //e contains all the records with the same keyValue as the current one
                        boolean recordRemoved = false;
                        IntBaseArray recordWithSameSecondaryKeyValues = getRecordsWithSameSecondaryKeyValues(i, recIndices);
//                        while (e.hasMoreElements()) {
                        for (int k=0; k<recordWithSameSecondaryKeyValues.size(); k++) {
/*                            boolean different = false;
int recIndex = ((Integer) e.nextElement()).intValue(); //2.0.1 ((Record) e.nextElement()).getIndex();
//*                                System.out.println("Checking record #" + recIndex);
Object secondaryKeyValue;
Object secondaryKeyValue2;
for (int k=0; k<keyFieldIndices.size(); k++) {
secondaryKeyValue = riskyGetCell(keyFieldIndices.get(k), i);
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
*/
//                                hasKey = false; //Trick in order to succesfully call removeRecord()
                            int tmp = primaryKeyFieldIndex;
                            primaryKeyFieldIndex = -1; //Trick in order to succesfully call removeRecord()
//*                                    System.out.println("Removing record index: " + i);
                            try{
                                table.removeRecord(recordWithSameSecondaryKeyValues.get(k), -1, true);
                            }catch (TableNotExpandableException ex) {
/*                                    try{
f.setKey(true);
}catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (3)");}
*/
                                secondaryKeyFieldIndices = secondaryKeyFieldIndicesBackUp;
                                primaryKeyFieldHash.clear();
                                primaryKeyFieldHash = primaryKeyHashBackUp;
                                primaryKeyFieldIndex = primaryKeyFieldIndexBackUp;
                                throw new TableNotExpandableException(table.bundle.getString("CTableMsg70"));
                            }
                            catch (InvalidRecordIndexException ex) {
                                ex.printStackTrace();
/*                                    try{
f.setKey(true);
}catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (4)");}
*/
                                secondaryKeyFieldIndices = secondaryKeyFieldIndicesBackUp;
                                primaryKeyFieldHash.clear();
                                primaryKeyFieldHash = primaryKeyHashBackUp;
                                System.out.println("Inconsistensy error: " + ex.message);
                            }
//                                hasKey = true;
                            primaryKeyFieldIndex = tmp; //Trick in order to succesfully call removeRecord()
                            recordRemoved = true;
                            i--;
                            break;
                        }
                        if (!recordRemoved) {
                            //Add this record's primary key value to the primaryKeyMap
                            addRecordToPrimaryKeyHash(primKeyValueHashCode, i);
//                                primaryKeyMap.add(new Integer(keyValue.hashCode()), new Integer(i)); //2.0.1 riskyGetRecord(i));
                        }
                    }
                    i++;
                }//first while
            }else{ //if the key field being removed is not the primary key field
//                    System.out.println("Removed from keyFieldIndices: " + keyFieldIndices.remove(fieldIndex) + " elements");
                int i = 0;
                AbstractTableField primaryKeyField = table.tableFields.get(primaryKeyFieldIndex);
                secondaryKeyFieldIndices.removeElements(fieldIndex);
                while (i<table.recordCount) {
                    Integer primKeyValueHashCode = new Integer(primaryKeyField.getObjectAt(i).hashCode());
//*                        System.out.println("Processing record #" + i + " with keyValue: " + keyValue);
                    IntBaseArray recIndices = (IntBaseArray) primaryKeyFieldHash.get(primKeyValueHashCode);

                    IntBaseArray recordWithSameSecondaryKeyValues = getRecordsWithSameSecondaryKeyValues(i, recIndices);
for (int q=0;q<recordWithSameSecondaryKeyValues.size(); q++)
    System.out.println("recordWithSameSecondaryKeyValues("+ q + "): " + recordWithSameSecondaryKeyValues.get(q));
                    int tmp = primaryKeyFieldIndex;
                    primaryKeyFieldIndex = -1; //Trick in order to succesfully call removeRecord()
                    for (int k=0; k<recordWithSameSecondaryKeyValues.size(); k++) {
//                    hasKey = false; //Trick in order to succesfully call removeRecord()
//*                                    System.out.println("Removing record index: " + recIndex);
                        try{
System.out.println("1. Removing record: " + recordWithSameSecondaryKeyValues.get(k));
                            table.removeRecord(recordWithSameSecondaryKeyValues.get(k), -1, true);
                        }catch (TableNotExpandableException ex) {
                            secondaryKeyFieldIndices = secondaryKeyFieldIndicesBackUp;
                            primaryKeyFieldHash.clear();
                            primaryKeyFieldHash = primaryKeyHashBackUp;
                            primaryKeyFieldIndex = primaryKeyFieldIndexBackUp;
                            throw new TableNotExpandableException(table.bundle.getString("CTableMsg70"));
                        }
                        catch (InvalidRecordIndexException ex) {
//                            try{
//                                f.setKey(true);
//                            }catch (InvalidKeyFieldException e1) {System.out.println("Serious inconsistency error in Table removeFromKey() : (6)");}
                            secondaryKeyFieldIndices = secondaryKeyFieldIndicesBackUp;
                            primaryKeyFieldHash.clear();
                            primaryKeyFieldHash = primaryKeyHashBackUp;
                            primaryKeyFieldIndex = primaryKeyFieldIndexBackUp;
                            System.out.println("Inconsistensy error: " + ex.message);
                        }
//                    hasKey = true;
                    }
                    primaryKeyFieldIndex = tmp;
                    i++;
                }
/*                Integer keyValueHashCode;
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
//*                        System.out.println("Processing record #" + rec.getIndex() + " keyValue: " + iter1.key());
//2.0.1                        int i = rec.getIndex();
Integer i = (Integer) iter1.value(); //2.0.1 rec.getIndex();
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
//*                                    System.out.println("Removing record index: " + recIndex);
try{
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
*/
            }
        }

        /* Adjust the active record.
        */
        if (table.activeRecord != -1) {
            if (table.activeRecord > (table.recordCount-1)) {
                table.setActiveRecord(table.recordCount-1);
            }else{
                if (table.activeRecord + 1 < table.recordCount) {
                    table.setActiveRecord(table.activeRecord+1);
                }else{
                    table.setActiveRecord(table.activeRecord);
                }
            }
        }

//t            if (database != null)
        table.fireColumnKeyChanged(f, false);
System.out.println("primaryKeyFieldIndex: " + primaryKeyFieldIndex);

    }

    /** Adds a record at <code>recIndex</code> to the <code>primaryKeyFieldHash</code>.
     *  @param hashCode The hash code of the value of the primary key field of the record.
     *  @param recIndex The index of the record.
     */
    void addRecordToPrimaryKeyHash(Integer hashCode, int recIndex) {
        IntBaseArray recIndices = (IntBaseArray) primaryKeyFieldHash.get(hashCode);
        if (recIndices == null) {
            recIndices = new IntBaseArray(1);
            primaryKeyFieldHash.put(hashCode, recIndices);
        }
        recIndices.add(recIndex);
    }

    void removeRecordFromPrimaryKeyHash(int recIndex) {
        // Do nothing, if the table is not keyed
        if (primaryKeyFieldIndex == -1)
            return;
        // Get the hash code of the primary key field value
        Integer primaryKeyValueHashCode = new Integer(table.tableFields.get(primaryKeyFieldIndex).getObjectAt(recIndex).hashCode());
        // Get the indices of the records which share the same primary key.
        IntBaseArray recIndices = (IntBaseArray) primaryKeyFieldHash.get(primaryKeyValueHashCode);
        // If only this record has the specific key, then just remove the entry for this key from the 'primaryKeyFieldHash'
        if (recIndices.size() == 1)
            primaryKeyFieldHash.remove(primaryKeyValueHashCode);
        else // Otherwise leave the entry there, but remove the index of the record from the record indices that share this primary key
            recIndices.remove(recIndex);
    }

    /** Resets the key of the Table */
    void clear() {
        secondaryKeyFieldIndices.clear();
        if (primaryKeyFieldHash != null) primaryKeyFieldHash.clear();
        primaryKeyFieldIndex = -1;
    }

    /** Checks if the record at <code>recIndex</code> has different secondary key values from all the
     *  records whose indices are in <code>recIndices</code> array.
     */
    private boolean secondaryKeyValuesExist(int recIndex, IntBaseArray recIndices) {
        if (recIndices == null) return false;

        for (int i=0; i<recIndices.size(); i++) {
            int recordIndex = recIndices.get(i);
            if (recordIndex == recIndex)
                continue;

            boolean same = true;
            for (int j=0; j<secondaryKeyFieldIndices.size(); j++) {
                int fldIndex = secondaryKeyFieldIndices.get(j);
                AbstractTableField f = table.tableFields.get(fldIndex);
                if (!f.getObjectAt(recordIndex).equals(f.getObjectAt(recIndex))) {
                    same = false;
                    break;
                }
            }
            if (same) return true;
        }

        return false;
    }

    /** Returns the indices of the records in <code>recIndices</code> array which have the same secondary key values as
     *  the record at <code>recIndex</code>.
     */
    private IntBaseArray getRecordsWithSameSecondaryKeyValues(int recIndex, IntBaseArray recIndices) {
        IntBaseArray recsWithSameSecondaryKeys = new IntBaseArray();
        for (int i=0; i<recIndices.size(); i++) {
            int recordIndex = recIndices.get(i);
            if (recordIndex == recIndex)
                continue;
            boolean same = true;
            for (int j=0; j<secondaryKeyFieldIndices.size(); j++) {
                int fldIndex = secondaryKeyFieldIndices.get(j);
                AbstractTableField f = table.tableFields.get(fldIndex);
                if (!f.getObjectAt(recordIndex).equals(f.getObjectAt(recIndex))) {
                    same = false;
                    break;
                }
            }
            if (same) recsWithSameSecondaryKeys.add(recordIndex);
        }

        return recsWithSameSecondaryKeys;
    }

    /** Checks whether the record at <code>recIndex</code> does not violate the uniqueness of the Table's key.
     *  @param recIndex The index of the record to be checked.
     *  @exception  NullTableKeyException, if the record has null values for some of the key fields.
     *  @exception  DuplicateKeyException, if the key uniqueness is violated.
     */
    void checkRecordKey(int recIndex) throws NullTableKeyException, DuplicateKeyException {
        // First check for null values
        AbstractTableField primaryKeyField = table.tableFields.get(primaryKeyFieldIndex);
        Object primaryKeyValue = primaryKeyField.getObjectAt(recIndex);
        if (primaryKeyValue == null) {
            throw new NullTableKeyException(Table.bundle.getString("CTableMsg106") + primaryKeyField.getName() + Table.bundle.getString("CTableMsg107"));
        }

        for (int i=0; i<secondaryKeyFieldIndices.size(); i++) {
            if (table.tableFields.get(secondaryKeyFieldIndices.get(i)).getObjectAt(recIndex) == null) {
                throw new NullTableKeyException(Table.bundle.getString("CTableMsg106") + table.tableFields.get(secondaryKeyFieldIndices.get(i)).getName() + Table.bundle.getString("CTableMsg107"));
            }
        }
        Object primaryKeyValueHashCode = new Integer(primaryKeyValue.hashCode());
        IntBaseArray recIncicesWithSamePrimaryKey = (IntBaseArray) primaryKeyFieldHash.get(primaryKeyValueHashCode);

        if (recIncicesWithSamePrimaryKey == null)
            return;

        recIncicesWithSamePrimaryKey.removeElements(recIndex);
        // Now check for key uniqueness.
        if (secondaryKeyValuesExist(recIndex, recIncicesWithSamePrimaryKey))
            throw new DuplicateKeyException(Table.bundle.getString("CTableMsg105"));

        return;
    }

    /** Checks if new key value <code>value</code> is unique.
     *  @param fieldIndex The index of the field, whose value changed.
     *  @param recIndex The index of the record, whose value changed.
     *  @param value The new value of the cell at <code>fieldIndex</code>, <code>recIndex</code>.
     *  @return true, if the new value violates the key uniqueness, false otherwise.
     */
    boolean violatesKeyUniqueness(int fieldIndex, int recIndex, Object value) { //, boolean insertingNewRecord) {
        AbstractTableField f = table.tableFields.get(fieldIndex);
        if (primaryKeyFieldIndex == -1)
            return false;
        if (!isPartOfTableKey(f))
            return false;
        if (value == null)
            return true;

/*System.out.println("violatesKeyUniqueness() value: " + value + ", hashCode: " + value.hashCode() + ", primaryKeyFieldHash.get(value): " + primaryKeyFieldHash.get(new Integer(value.hashCode())));
java.util.Iterator enum = primaryKeyFieldHash.entrySet().iterator();
while (enum.hasNext()) {
    Map.Entry e = (Map.Entry) enum.next();
    System.out.println("key: " + e.getKey() + " --> " + e.getValue());
}
*/
        if (fieldIndex == primaryKeyFieldIndex)
            return (primaryKeyFieldHash.get(new Integer(value.hashCode())) != null);

        Integer primaryKeyValueHashCode = new Integer(table.tableFields.get(primaryKeyFieldIndex).getObjectAt(recIndex).hashCode());
        IntBaseArray recIndices = (IntBaseArray) primaryKeyFieldHash.get(primaryKeyValueHashCode);
        if (recIndices != null)
            recIndices.removeElements(recIndex);
        // Temporarily set the cell at recIndex of f to 'value', so that 'secondaryKeyValuesExist' can
        // determine if there is another record with the same values for secondary key fields.
        Object currentValue = f.getObjectAt(recIndex);
        f.set(recIndex, value);
        boolean violates = secondaryKeyValuesExist(recIndex, (IntBaseArray) primaryKeyFieldHash.get(primaryKeyValueHashCode));
        f.set(recIndex, currentValue);
        return violates;
    }

    //Decrements the field index of all the entries in the  secondaryKeyFieldIndices array
    //after the specified index. Also decrements the primaryKeyFieldIndex, if it is bigger that index.
    void decrementRestKeyFieldIndices(int index) {
        if (primaryKeyFieldIndex > index) primaryKeyFieldIndex--;
        for (int i=0; i<secondaryKeyFieldIndices.size(); i++) {
            int k = secondaryKeyFieldIndices.get(i);
            if (k > index)
                secondaryKeyFieldIndices.set(i, k-1);
//            keyFieldIndices.set(i, (keyFieldIndices.get(i)-1));
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Primary key index", primaryKeyFieldIndex);
        fieldMap.put("Secondary key indices", secondaryKeyFieldIndices);
        fieldMap.put("Primary key hash codes", primaryKeyFieldHash);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        primaryKeyFieldIndex = fieldMap.get("Primary key index", -1);
        secondaryKeyFieldIndices = (IntBaseArray) fieldMap.get("Secondary key indices");
        primaryKeyFieldHash = (HashMap) fieldMap.get("Primary key hash codes");
    }
}


