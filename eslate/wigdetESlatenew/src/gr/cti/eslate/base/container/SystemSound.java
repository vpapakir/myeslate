package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map.Entry;

public class SystemSound implements Entry, Cloneable, Externalizable {
    static final long serialVersionUID = 12;
    public static final int FORMAT_VERSION = 1;
    String soundName;
    String soundFileName;
    String pathToSound = null;
    boolean modified = false;

    /** Exists only for use by the Externalization mechanism.
     */
    public SystemSound() {
    }

    public SystemSound(String soundName, String soundFileName) {
        this.soundName = soundName;
        this.soundFileName = soundFileName;
    }

    public int hashCode() {
        return (soundName==null   ? 0 : soundName.hashCode()) ^
               (soundFileName==null ? 0 : soundFileName.hashCode());
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!Entry.class.isAssignableFrom(o.getClass()))
            return false;

        Entry e2 = (Entry) o;
        return (soundName==null ? e2.getKey()==null : soundName.equals(e2.getKey()))  &&
               (soundFileName==null ? e2.getValue()==null : soundFileName.equals(e2.getValue()));
    }

    public Object setValue(Object value) {
        String oldVal = soundFileName;
        soundFileName = (String) value;
        modified = true;
        return oldVal;
    }

    public Object getValue() {
        return soundFileName;
    }

    public Object getKey() {
        return soundName;
    }

    public Object clone() {
        try{
            return super.clone();
        }catch (CloneNotSupportedException exc) {
            return null;
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("SoundName", soundName);
        fieldMap.put("SoundFileName", soundFileName);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        soundName = (String) fieldMap.get("SoundName");
        soundFileName = (String) fieldMap.get("SoundFileName");
    }
}