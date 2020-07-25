package gr.cti.eslate.mapViewer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;

public class TaggedPropertyEditor extends PropertyEditorSupport {
    private String[] tags;
    private String property;
    private Integer tagIndex;
    private PropertyChangeSupport pcs;
    private String noviceStr, advancedStr;

    public TaggedPropertyEditor(String property,String[] tags) {
        super();
        pcs=new PropertyChangeSupport(this);
        this.property=property;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

/*    public void setValue(Object value) {
        if (!Integer.class.isInstance(value))
            return;
        if (tagIndex!=null && tagIndex.equals(value)) return;

        Integer oldTagIndex=tagIndex;
        this.tagIndex=(Integer) value;
        pcs.firePropertyChange(property, oldTagIndex, tagIndex);
    }

    public Object getValue() {
        return tagIndex;
    }*/

    public java.awt.Component getCustomEditor() {
        return null;
    }

    public String getAsText() {
        return tags[tagIndex.intValue()];
    }

    public void setAsText(String value) {
        Integer prevTagIndex = tagIndex;
        int id=-1;
        for (int i=0;i<tags.length;i++)
            if (value.equalsIgnoreCase(tags[i])) {
                id=i;
                break;
            }
        if (id==-1)
            throw new IllegalArgumentException("MAPVIEWER#200004052048: Bad value for "+property+".");
        tagIndex=new Integer(id);
        pcs.firePropertyChange(property,prevTagIndex,tagIndex);
    }

    public boolean supportsCustomEditor() {
        return false;
    }

    public String[] getTags() {
        return tags;
    }
}
