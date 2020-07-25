package gr.cti.eslate.tableInspector;

import com.objectspace.jgl.Array;

public class RecordCarrier implements java.io.Serializable {
    public Array fieldNames;
    private Array fieldTypes;
    private Array fieldValues;

    public RecordCarrier(Array fn, Array ft, Array fv) {
        fieldNames=fn;
        fieldTypes=ft;
        fieldValues=fv;
    }

    public Array getFieldNames() {
        return fieldNames;
    }

    public Array getFieldTypes() {
        return fieldTypes;
    }

    public Array getFieldValues() {
        return fieldValues;
    }
}