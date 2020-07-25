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
public class StackElement implements Serializable {
    /** The version of the storage format of the StackElement class
     */
//    public static final String STR_FORMAT_VERSION = "1.0";
    public static final int FORMAT_VERSION = 2;

    protected Object element;
    protected boolean isFieldIndex = false;
    static final long serialVersionUID = 12;

    protected StackElement(Object elem, boolean isFld) {
        element = elem;
        isFieldIndex = isFld;
    }

    protected StackElement(Object elem) {
        element = elem;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Element", element);
        fieldMap.put("Field index", isFieldIndex);
        out.writeObject(fieldMap);

/*        out.writeObject(element);
        out.writeObject(new Boolean(isFieldIndex));
*/
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            StorageStructure fieldMap = (StorageStructure) firstObj;
            element = fieldMap.get("Element");
            isFieldIndex = fieldMap.get("Field index", false);
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("StackElement oldReadObject()");
        try{
            element = firstObj; //in.readObject();
            isFieldIndex = ((Boolean) in.readObject()).booleanValue();
        }catch (Exception e) {
//            System.out.println(e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }
}
