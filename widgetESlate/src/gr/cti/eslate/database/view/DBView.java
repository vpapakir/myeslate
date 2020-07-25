/* Stores all the UI settings for a DBEngine.
 */
package gr.cti.eslate.database.view;

import java.io.IOException;
import java.io.Externalizable;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

public class DBView implements Externalizable {
//    public static final String STR_FORMAT_VERSION = "1.0";
    public static final int FORMAT_VERSION = 2;
    static final long serialVersionUID = 12L;
    public static final int DATABASE_SCOPE = 0;
    public static final int TABLE_SCOPE = 1;
    private int colorPropertiesScope = DATABASE_SCOPE;
    private int fieldPropertiesScope = DATABASE_SCOPE;
    private int advancedPropertiesScope = DATABASE_SCOPE;

    public DBView() {
    }

    public void setColorPropertiesScope(int scope) {
        if (scope != DATABASE_SCOPE && scope != TABLE_SCOPE) return;
        colorPropertiesScope = scope;
    }

    public int getColorPropertiesScope() {
        return colorPropertiesScope;
    }

    public void setFieldPropertiesScope(int scope) {
        if (scope != DATABASE_SCOPE && scope != TABLE_SCOPE) return;
        fieldPropertiesScope = scope;
    }

    public int getFieldPropertiesScope() {
        return fieldPropertiesScope;
    }

    public void setAdvancedPropertiesScope(int scope) {
        if (scope != DATABASE_SCOPE && scope != TABLE_SCOPE) return;
        advancedPropertiesScope = scope;
    }

    public int getAdvancedPropertiesScope() {
        return advancedPropertiesScope;
    }

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("ColorPropertiesScope", colorPropertiesScope);
        fieldMap.put("FieldPropertiesScope", fieldPropertiesScope);
        fieldMap.put("AdvancedPropertiesScope", advancedPropertiesScope);
        out.writeObject(fieldMap);
    }
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
//long start = System.currentTimeMillis();
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        colorPropertiesScope = fieldMap.get("ColorPropertiesScope", DATABASE_SCOPE);
        fieldPropertiesScope = fieldMap.get("FieldPropertiesScope", DATABASE_SCOPE);
        advancedPropertiesScope = fieldMap.get("AdvancedPropertiesScope", DATABASE_SCOPE);
//System.out.println("DBView readExternal(): " + (System.currentTimeMillis()-start));
    }
}