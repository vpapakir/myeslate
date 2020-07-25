package gr.cti.eslate.database.engine;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;


/**
 * @version	2.0, May 01
 */
public class Record implements Serializable {
    /** The version of the storage format of the Record class
     */
//    public static final String STR_FORMAT_VERSION = "1.1";
    public static final int FORMAT_VERSION = 2;

    Table table;
    private int recIndex;
    static final long serialVersionUID = 12;

    protected Record(Table t, int index) {
System.out.println("Record was instantiated");
        table = t;
        recIndex = index;
    }


    public int getIndex() {//throws InvalidRecordIndexException {
//        if (recIndex < 0)
//            throw new InvalidRecordIndexException("This record is not valid.");
        return recIndex;
    }


    protected void setIndex(int newIndex) throws InvalidRecordIndexException {
        if (newIndex >=0)
            recIndex = newIndex;
        else
            throw new InvalidRecordIndexException(DBase.resources.getString("RecordMsg1"));
    }


    protected void invalidateIndex() {
        recIndex = -1;
    }


    protected void decrementRecordIndex(int difference) {
        recIndex = recIndex - difference;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        /* In 1.0 version of the storage format the 'table' field was also
         * stored. This stopped in version 1.1. Instead the CTable which
         * restores every Record also assigns its 'table' field.
         */
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
//1.1        fieldMap.put("Table", table);
        fieldMap.put("Index", recIndex);
        out.writeObject(fieldMap);

/*        out.writeObject(table);
        out.writeObject(new Integer(recIndex));
*/
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            StorageStructure fieldMap = (StorageStructure) firstObj;
//1.1            table = (CTable) fieldMap.get("Table");
            try{
                recIndex = fieldMap.getInteger("Index");
            }catch (Exception exc) {
                System.out.println("Record readObject exception: " + exc.getClass() + ", " + exc.getMessage());
                throw new IOException(exc.getMessage());
            }
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("Record oldReadObject()");
        try{
//t            table = (CTable) firstObj; //in.readObject();
            Object tableStructure = in.readObject();
            if (CTable.class.isAssignableFrom(tableStructure.getClass()))
                table = new Table((CTable) tableStructure);
            else
                table = (Table) tableStructure;
            recIndex = ((Integer) in.readObject()).intValue();
//            System.out.println("Read record: " + recIndex);
        }catch (Exception e) {
            System.out.println("Record readObject exception: " + e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }
}


