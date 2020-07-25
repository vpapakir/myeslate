/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 9 ��� 2002
 * Time: 4:38:00 ��
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;


import java.io.IOException;
import java.io.Externalizable;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

/** Stores the view proporties of the columns of a DBTable. */
public class FieldViewProperties implements Externalizable {
    // FROM 1 --> 2
    // Added the field resizable property
    public static final int FORMAT_VERSION = 2; //1;
    static final long serialVersionUID = 12L;
    public static final int DEFAULT_FIELD_WIDTH = 110;
    public static final int DEFAULT_FIELD_MIN_WIDTH = 40;
    public static final int DEFAULT_FIELD_MAX_WIDTH = 1000;
    private int fieldWidth = DEFAULT_FIELD_WIDTH;
    private int fieldPreferredWidth = DEFAULT_FIELD_WIDTH;
    private int fieldMinWidth = DEFAULT_FIELD_MIN_WIDTH;
    private int fieldMaxWidth = DEFAULT_FIELD_MAX_WIDTH;
    private boolean resizable = true;


    public FieldViewProperties() {
    }

    public FieldViewProperties(FieldViewProperties copiedView) {
        super();
        if (copiedView == null) return;
        fieldWidth = copiedView.fieldWidth;
        fieldMinWidth = copiedView.fieldMinWidth;
        fieldMaxWidth = copiedView.fieldMaxWidth;
        resizable = copiedView.resizable;
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
    public void setFieldPreferredWidth(int width) {
        fieldPreferredWidth = width;
    }
    public int getPreferredWidth() {
        return fieldPreferredWidth;
    }
    public boolean isResizable() {
        return resizable;
    }
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("FieldWidth", fieldWidth);
        fieldMap.put("FieldMinWidth", fieldMinWidth);
        fieldMap.put("FieldMaxWidth", fieldMaxWidth);
        fieldMap.put("FieldPreferredWidth", fieldPreferredWidth);
        fieldMap.put("Resizable", resizable);
        out.writeObject(fieldMap);
    }
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        fieldWidth = fieldMap.get("FieldWidth", DEFAULT_FIELD_WIDTH);
        fieldMinWidth = fieldMap.get("FieldMinWidth", DEFAULT_FIELD_MIN_WIDTH);
        fieldMaxWidth = fieldMap.get("FieldMaxWidth", DEFAULT_FIELD_MAX_WIDTH);
        fieldPreferredWidth = fieldMap.get("FieldPreferredWidth", 75);
        resizable = fieldMap.get("Resizable", true);
    }

    public String toString() {
        return super.toString() + ", width: " + fieldWidth + ", minWidth: " + fieldMinWidth + ", maxWidth: " + fieldMaxWidth + ", preferredWidth: " + fieldPreferredWidth + ", resizable? " + resizable;
    }

}
