/* Stores all the UI settings for a DBEngine TableField.
 */
package gr.cti.eslate.database.view;

import java.io.IOException;
import java.io.Externalizable;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

public class FieldView implements Externalizable {
//    public static final String STR_FORMAT_VERSION = "1.0";
    public static final int FORMAT_VERSION = 2;
    static final long serialVersionUID = 12L;
    public static final int DEFAULT_FIELD_WIDTH = 110;
    public static final int DEFAULT_FIELD_MIN_WIDTH = 40;
    public static final int DEFAULT_FIELD_MAX_WIDTH = 1000;
    private int fieldWidth = DEFAULT_FIELD_WIDTH;
    private int fieldMinWidth = DEFAULT_FIELD_MIN_WIDTH;
    private int fieldMaxWidth = DEFAULT_FIELD_MAX_WIDTH;


    public FieldView() {
    }

    public FieldView(FieldView copiedView) {
        super();
        if (copiedView == null) return;
        fieldWidth = copiedView.fieldWidth;
        fieldMinWidth = copiedView.fieldMinWidth;
        fieldMaxWidth = copiedView.fieldMaxWidth;
    }

    public void setFieldWidth(int width) {
        fieldWidth = width;
    }
    public int getFieldWidth() {
        return fieldWidth;
    }
    public void setFieldMinWidth(int width) {
        fieldMinWidth = width;
    }
    public int getFieldMinWidth() {
        return fieldMinWidth;
    }
    public void setFieldMaxWidth(int width) {
        fieldMaxWidth = width;
    }
    public int getFieldMaxWidth() {
        return fieldMaxWidth;
    }

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("FieldWidth", fieldWidth);
        fieldMap.put("FieldMinWidth", fieldMinWidth);
        fieldMap.put("FieldMaxWidth", fieldMaxWidth);
        out.writeObject(fieldMap);
    }
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        fieldWidth = fieldMap.get("FieldWidth", DEFAULT_FIELD_WIDTH);
        fieldMinWidth = fieldMap.get("FieldMinWidth", DEFAULT_FIELD_MIN_WIDTH);
        fieldMaxWidth = fieldMap.get("FieldMaxWidth", DEFAULT_FIELD_MAX_WIDTH);
    }

    public String toString() {
        return super.toString() + ", width: " + fieldWidth;
    }

}