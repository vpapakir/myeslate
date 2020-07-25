package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import javax.swing.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.Map.Entry;


public class LayerInfo implements Externalizable {
    public static final Integer DEFAULT_LAYER_Z_ORDER = JLayeredPane.DEFAULT_LAYER; //new Integer(3);
	public static final Integer ICON_LAYER_Z_ORDER = new Integer(999);
	public static final Integer GLASS_FRAME_LAYER_Z_ORDER = new Integer(1000);
//    OrderedMap layers = new OrderedMap(new com.objectspace.jgl.LessNumber());
    TreeMap layers = new TreeMap(new IntegerComparator());
    ResourceBundle layerInfoBundle;
    ESlateContainer container;
    static final long serialVersionUID = 6247740240326465697L;
    public static final String STR_FORMAT_VERSION = "1.0";
    static final int FORMAT_VERSION = 2;

    public LayerInfo() {
        layerInfoBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LayerInfoBundle", Locale.getDefault());
        initLayerInfo();
    }

    public LayerInfo(ESlateContainer container) {
        layerInfoBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LayerInfoBundle", Locale.getDefault());
        initLayerInfo();
        this.container = container;
    }

    private void initLayerInfo() {
        layers.put(DEFAULT_LAYER_Z_ORDER, layerInfoBundle.getString("Default"));
    }

    public final void reset() {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");
        resetInternal();
    }

    final void resetInternal() {
        layers.clear();
        initLayerInfo();
        if (container != null) container.setMicroworldChanged(true);
    }

    public int getTopLayerLevel() {
        return ((Integer) layers.lastKey()).intValue();
    }

    public int getBottomLayerLevel() {
//System.out.println("layers: " + layers.size());
//System.out.println("layers.lastKey(): " + layers.lastKey());
        return ((Integer) layers.firstKey()).intValue();
    }

    public final boolean renameLayer(String oldName, String newName) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");
        return renameLayerInternal(oldName, newName);
    }

    final boolean renameLayerInternal(String oldName, String newName) {
//        if (!existsLayer(oldName))
//            return false;
        int layerLevel = getLayerLevel(oldName);
        if (layerLevel == -1) return false;
//        Iterator iter = layers.keys(oldName);
//        Integer layerLevel = (Integer) enum.nextElement();
        layers.put(new Integer(layerLevel), newName);
        if (container != null) container.setMicroworldChanged(true);
        return true;
    }

    public int getLayerLevel(String layerName) {
//        if (!existsLayer(layerName)) return -1;
        Iterator iter = layers.keySet().iterator();
        Object key = null;
        while (iter.hasNext()) {
            key = iter.next();
            if (layers.get(key).equals(layerName))
                break;
            key = null;
        }
        if (key != null)
            return ((Integer)  key).intValue();
        else
            return -1;
//        return ((Integer) layers.keys(layerName).nextElement()).intValue();
    }

    public boolean existsLayer(String layerName) {
        return (layers.containsValue(layerName));
    }

    /* Adds a layer to the list of the layers. The added layer
     * by default becomes the bottom-most layer.
     */
    public final boolean addLayer(String layerName) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");
        return addLayerInternal(layerName);
    }

    final boolean addLayerInternal(String layerName) {
        if (existsLayer(layerName)) return false;
        layers.put(new Integer(getBottomLayerLevel()-1), layerName);
        if (container != null) container.setMicroworldChanged(true);
        return true;
    }

    public String getDefaultLayerName() {
        return (String) layers.get(DEFAULT_LAYER_Z_ORDER);
    }

    public void setDefaultLayerName(String name) {
        layers.put(DEFAULT_LAYER_Z_ORDER, name);
    }

    /* Returns the layer names ordered. The first is the topmost */
    public String[] getLayerNames() {
        String[] layerNames1 = new String[layers.size()];
        Iterator iter = layers.entrySet().iterator();
        int i =0;
        while (iter.hasNext()) {
            layerNames1[i] = (String) ((Entry) iter.next()).getValue();
            i++;
        }

/*        Enumeration enum = layers.elements();
        int count = 0;
        while (enum.hasMoreElements()) {
            layerNames1[count] = (String) enum.nextElement();
            count++;
        }
*/
        /* Reverse the layerNames1 array */
        String[] layerNames = new String[layerNames1.length];
        for (i=0; i<layerNames1.length; i++)
            layerNames[layerNames.length-1-i] = layerNames1[i];
        return layerNames;
    }

    /* Re-initializes the LayerInfo with the specifies layers. The order of the
     * layer names in the String array is significant. The last layer in the array
     * becomes the bottom-most layer. The defaultLayerName is used to number the
     * rest of the layers around the DEFAULT_LAYER_Z_ORDER.
     */
    public final boolean setLayers(String[] layerNames, String defaultLayerName) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");
        return setLayersInternal(layerNames, defaultLayerName);
    }

    final boolean setLayersInternal(String[] layerNames, String defaultLayerName) {
//        System.out.println("setLayers: " );
//        for (int i=0; i<layerNames.length; i++)
//            System.out.println("layerNames[" + i + "]: " + layerNames[i]);
        if (layerNames == null || layerNames.length == 0) return false;
        if (defaultLayerName == null) return false;
        int defaultLayerIndex = -1;
        boolean defaultLayerExists = false;
        for(int i=0; i<layerNames.length; i++) {
            if (layerNames[i].equals(defaultLayerName)) {
                defaultLayerIndex = i;
                defaultLayerExists = true;
                break;
            }
        }
        if (!defaultLayerExists) return false;

        resetInternal();
        renameLayerInternal(layerInfoBundle.getString("Default"), defaultLayerName);
        int defaultLayerZOrder = DEFAULT_LAYER_Z_ORDER.intValue();
        for (int i=0; i<defaultLayerIndex; i++)
            layers.put(new Integer(defaultLayerZOrder+defaultLayerIndex-i), layerNames[i]);
        for (int i=defaultLayerIndex+1; i<layerNames.length; i++)
            layers.put(new Integer(defaultLayerZOrder+defaultLayerIndex-i), layerNames[i]);
//        System.out.println(this);

        if (container != null) container.setMicroworldChanged(true);
        return true;
    }

    public void moveLayerAfterLayer(String movedLayer, String afterLayer) {
        if (movedLayer == null) return;
//        if (!existsLayer(movedLayer)) return;
        int layerLevel = getLayerLevel(movedLayer);
        if (layerLevel == -1) return;

        /* Remove the layer to be moved from the 'layers' Orderedmap. Get the
         * layer names in a string array, get the defaultLayerName, insert the
         * 'movedLayer' in the string array after the 'afterLayer' and call setlayers().
         */
        layers.remove(new Integer(layerLevel)); //layers.keys(movedLayer).nextElement());
        String[] layerNames = getLayerNames();
        String defaultLayerName = getDefaultLayerName();
        String[] allLayerNames = new String[layerNames.length];
        int insertionIndex = -1;
        if (afterLayer == null)
            insertionIndex = 0;
        else{
            for (int i=0; i<layerNames.length; i++) {
                if (layerNames[i].equals(afterLayer)) {
                    insertionIndex = i;
                    break;
                }
            }
        }
        for (int i=0; i<insertionIndex; i++)
            allLayerNames[i] = layerNames[i];
        allLayerNames[insertionIndex] = movedLayer;
        for (int i=insertionIndex+1; i<allLayerNames.length; i++)
            allLayerNames[i] = layerNames[i-1];

        setLayers(allLayerNames, defaultLayerName);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 2);
        fieldMap.put("Layer names", getLayerNames());
//.        out.writeObject(getLayerNames());
        fieldMap.put("Default layer name", getDefaultLayerName());
//.        out.writeObject(getDefaultLayerName());
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String[] layerNames = null;
        String defaultLayerName = null;
        Object o = in.readObject();
        if (StorageStructure.class.isAssignableFrom(o.getClass())) {
            StorageStructure fieldMap = (StorageStructure) o;
            layerNames = (String[]) fieldMap.get("Layer names");
            defaultLayerName = (String) fieldMap.get("Default layer name");
        }else{
            layerNames = (String[]) in.readObject();
            defaultLayerName = (String) in.readObject();
        }
        setLayersInternal(layerNames, defaultLayerName);
//        System.out.println("Restored layers: ");
//        System.out.println(this);
    }

    public String toString() {
//        Enumeration enum = layers.elements();
//        Enumeration keys = layers.keys();
        Iterator entries = layers.entrySet().iterator();
        StringBuffer str = new StringBuffer();
        while (entries.hasNext()) {
            Entry entry = (Entry) entries.next();
            str.append("Z-Order: " + entry.getKey() + ",  Layer: " + entry.getValue() + '\n');
        }
//        while (enum.hasMoreElements())
//            str.append("Z-Order: " + keys.nextElement() + ",  Layer: " + enum.nextElement() + '\n');
        return str.toString();
    }
}

class IntegerComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        int o1Value = ((Integer) o1).intValue();
        int o2Value = ((Integer) o2).intValue();
        if (o1Value < o2Value)
            return -1;
        else if (o1Value == o2Value)
            return 0;
        else
            return 1;
    }

    public boolean equals(Object obj) {
        return false;
    }
}
