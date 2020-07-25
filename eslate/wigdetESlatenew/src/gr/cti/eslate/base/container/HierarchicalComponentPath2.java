package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/* This class was introduced in order to provide writeExternal() and readExternal()
 * methods for its ancestor, so that it has version-safe storage format. The changes
 * not be done directly to the HierarchicalComponentPath class, cause this would break
 * the storage format compatibility with microworlds that were created with versions
 * of the Container less that or equal to 0.9.8.
 */
public class HierarchicalComponentPath2 extends HierarchicalComponentPath implements Externalizable {
    static final long serialVersionUID = 12;
    /* 2.0: methodIndex int array was added to the superclass */
//    public static final String STR_FORMAT_VERSION_2 = "2.0";
    static final int FORMAT_VERSION = 3;

    public HierarchicalComponentPath2() {
        super();
    }

    public HierarchicalComponentPath2(String[] path, int[] typeArray) {
        super(path, typeArray, null);
    }

    public HierarchicalComponentPath2(String[] path, int[] typeArray, int[] indexArray) {
        super(path, typeArray, indexArray);
    }

    public HierarchicalComponentPath2(String[] path) {
        super(path);
    }

    /** Constructs a new HierarchicalComponentPath which is based on an existing one.
     *  The last 'fromDepth' elements of the original path are included in the new
     *  path.
     */
    public HierarchicalComponentPath2(HierarchicalComponentPath hp, int fromDepth) {
        super(hp, fromDepth);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 3);
        fieldMap.put("Path", path);
        fieldMap.put("Node type array", nodeType);
        fieldMap.put("Method index array", methodIndex);
        out.writeObject(fieldMap);
//        System.out.println("HierarchicalComponentPath writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object o = in.readObject();
//        if (ESlateFieldMap.class.isAssignableFrom(o.getClass())) {
        if (StorageStructure.class.isAssignableFrom(o.getClass())) {
//            ESlateFieldMap fieldMap = (ESlateFieldMap) o;
            StorageStructure fieldMap = (StorageStructure) o;
            path = (String[]) fieldMap.get("Path");
            nodeType = (int[]) fieldMap.get("Node type array");
            int dataVersionID = fieldMap.getDataVersionID();
            String dataVersion = fieldMap.getDataVersion();
//            if (fieldMap.getDataVersion().startsWith("2."))
            if (dataVersion.startsWith("2.") || dataVersionID >= 3)
                methodIndex = (int[]) fieldMap.get("Method index array");
            else{
                methodIndex = new int[nodeType.length];
                for (int i=0; i<methodIndex.length; i++)
                    methodIndex[i] = -1;
            }
        }else{
            path = (String[]) o;
            nodeType = (int[]) in.readObject();
            methodIndex = new int[nodeType.length];
            for (int i=0; i<methodIndex.length; i++)
                methodIndex[i] = -1;
        }
    }

}