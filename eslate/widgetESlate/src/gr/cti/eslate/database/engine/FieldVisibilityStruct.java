package gr.cti.eslate.database.engine;

import java.util.HashMap;

class FieldVisibilityStruct {
    HashMap displayStatus = null;

    FieldVisibilityStruct(Table table) {
        int fieldCount = table.getFieldCount();
        displayStatus = new HashMap();
        for (int i=0; i<fieldCount; i++) {
            AbstractTableField f = table.tableFields.get(i);
            if (f.isHidden())
                displayStatus.put(f.getName(), Boolean.FALSE);
            else
                displayStatus.put(f.getName(), Boolean.TRUE);
        }
    }

    public Boolean isVisible(String fieldName) {
        Object obj = displayStatus.get(fieldName);
        if (obj == null)
            return null;
//            throw new IllegalArgumentException("The table does not contain field \"" + fieldName + "\".");
        return (Boolean) obj;
    }

    public void setVisible(String fieldName, boolean visible) {
        if (displayStatus.containsKey(fieldName)) {
            if (visible)
                displayStatus.put(fieldName, Boolean.TRUE);
            else
                displayStatus.put(fieldName, Boolean.FALSE);
        }else
            throw new IllegalArgumentException("The field visibility structure does not contain field \"" + fieldName + "\".");
    }

    public int size() {
        return displayStatus.size();
    }

}