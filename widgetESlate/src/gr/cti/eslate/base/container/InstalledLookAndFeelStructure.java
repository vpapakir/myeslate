package gr.cti.eslate.base.container;

import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.util.Enumeration;

import com.objectspace.jgl.OrderedMap;

/* This class stores the information about the registered components. This information
 * includes the name, class name and the availability for each of the registered components.
 * The data structure chosen to store this information ensures:
 * 1. That the registered components are always sorted on one of the fields og the information.
 *    The order is respected when new components are registered.
 * 2. The field upon which the components are ordered and the sort direction can change.
 */
public class InstalledLookAndFeelStructure implements Cloneable {
    public static final int NAME = 1;
    public static final int CLASSNAME = 2;
    public static final int AVAILABILITY = 3;
    private BoolBaseArray lookAndFeelAvailability;
    private StringBaseArray lookAndFeelNames;
    private StringBaseArray lookAndFeelClassNames;
    private OrderedMap indexMap;
    private boolean sortDirectionUp = true;
    private int sortField = NAME;
    private boolean indexMapNeedsRevalidation = false;

    public InstalledLookAndFeelStructure() {
        lookAndFeelAvailability = new BoolBaseArray();
        lookAndFeelNames = new StringBaseArray();
        lookAndFeelClassNames = new StringBaseArray();
        indexMap = new OrderedMap(new com.objectspace.jgl.LessString(), true);
    }

    public void add(String name, String className, boolean available) throws DuplicateEntryException {
        if (name == null || name.trim().length() == 0) return;
        if (className == null || className.trim().length() == 0) return;
//        System.out.println("Adding name: " + name + ", componentNames.size(): " + componentNames.size());
        if (lookAndFeelNames.size() != 0 && lookAndFeelNames.contains(name))
            throw new DuplicateEntryException(NAME);
        if (lookAndFeelClassNames.size() != 0 && lookAndFeelClassNames.contains(className))
            throw new DuplicateEntryException(CLASSNAME);

        lookAndFeelNames.add(name);
        lookAndFeelClassNames.add(className);
        lookAndFeelAvailability.add(available);
        if (sortField == NAME)
            indexMap.add(name, new Integer(lookAndFeelNames.size()-1));
        else if (sortField == CLASSNAME)
            indexMap.add(className, new Integer(lookAndFeelNames.size()-1));
        else
            indexMap.add(new Boolean(available), new Integer(lookAndFeelNames.size()-1));
    }

    public void remove(String name) {
        if (name == null || name.trim().length() == 0) return;
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return;
        Enumeration en = indexMap.keys(new Integer(index));
        indexMap.remove(en.nextElement());
        lookAndFeelNames.remove(index);
        lookAndFeelClassNames.remove(index);
        lookAndFeelAvailability.remove(index);
        recreateIndexMap();
    }

    public void remove(int index) {
        if (index < 0 || index >= lookAndFeelNames.size()) return;
        Enumeration en = indexMap.keys(new Integer(index));
        indexMap.remove(en.nextElement());
        lookAndFeelNames.remove(index);
        lookAndFeelClassNames.remove(index);
        lookAndFeelAvailability.remove(index);
        recreateIndexMap();
    }

    public String getClassName(String name) {
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return null;
        return lookAndFeelClassNames.get(index);
    }

    public void setClassName(String name, String className) {
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return;
        lookAndFeelClassNames.set(index, className);
        if (sortField == CLASSNAME)
            indexMapNeedsRevalidation = true;
    }

    public boolean getAvailability(String name) {
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return false;
        return lookAndFeelAvailability.get(index);
    }

    public void setAvailability(String name, boolean bool) {
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return;
        lookAndFeelAvailability.set(index, bool);
        if (sortField == AVAILABILITY)
            indexMapNeedsRevalidation = true;
    }

    public String getName(String className) {
        int index = lookAndFeelClassNames.indexOf(className);
        if (index == -1) return null;
        return lookAndFeelNames.get(index);
    }

    public void setName(String name, String newName) {
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return;
        lookAndFeelNames.set(index, newName);
        if (sortField == NAME)
            indexMapNeedsRevalidation = true;
    }

    StringBaseArray _getNames() {
        return lookAndFeelNames;
    }

    StringBaseArray _getClassNames() {
        return lookAndFeelClassNames;
    }

    BoolBaseArray _getAvailability() {
        return lookAndFeelAvailability;
    }

    public StringBaseArray getNames() {
        return (StringBaseArray) lookAndFeelNames.clone();
    }

    public StringBaseArray getAvailableNames() {
        StringBaseArray availableNames = new StringBaseArray();
        for (int i =0; i<lookAndFeelNames.size(); i++) {
            if (lookAndFeelAvailability.get(i))
                availableNames.add(lookAndFeelNames.get(i));
        }
        return availableNames;
    }

    public int getAvailableCount() {
        int nAvailable = 0;
        for (int i =0; i<lookAndFeelNames.size(); i++) {
            if (lookAndFeelAvailability.get(i)) {
                nAvailable++;
            }
        }
        return nAvailable;
    }

/*    public int getIndex(String name) {
        for (int i=0; i<lookAndFeelNames.size(); i++) {
            if (lookAndFeelNames.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }
*/
    public int getSize() {
        return lookAndFeelNames.size();
    }

    public boolean containsLookAndFeelName(String name) {
        if (lookAndFeelNames.size() == 0)
            return false;
        int index = lookAndFeelNames.indexOf(name);
        if (index == -1) return false;
        return true;
    }

    public boolean containsLookAndFeelClassName(String className) {
        if (lookAndFeelClassNames.size() == 0)
            return false;
        int index = lookAndFeelClassNames.indexOf(className);
        if (index == -1) return false;
        return true;
    }

    class LAFEntry {
        public String name, className;
        public boolean availability;
        public LAFEntry(String s1, String s2, boolean b) {
            name = s1;
            className = s2;
            availability = b;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(name);
            sb.append('\t');
            sb.append(className);
            sb.append('\t');
            sb.append(availability);
            return sb.toString();
        }
    }

    public LAFEntry[] getSortedEntries() {
        Enumeration en = indexMap.elements();
        LAFEntry[] entries = new LAFEntry[getSize()];
        int i=0;
        while (en.hasMoreElements()) {
            int index = ((Integer) en.nextElement()).intValue();
            entries[i] = new LAFEntry(lookAndFeelNames.get(index),
                                   lookAndFeelClassNames.get(index),
                                   lookAndFeelAvailability.get(index));
//            System.out.println("i: " + i + " --> " + entries[i].name + ", " + entries[i].className + ", " + entries[i].availability);
            i++;
        }
        return entries;
    }

    /* Sort direction does not generally recreate the 'indexMap', cause this isn't really
     * needed. All that changes is the short direction and not the sort field.
     * However if the contents of the 'indexMap' have changed (throught setAvailability(),
     * 'setName()' or 'setClassName()') then it is invalid and has to be revalidated, that
     * is recreated.
     */
    public void setSortDirection(boolean up) {
//        System.out.println("setSortDirection: " + indexMapNeedsRevalidation);
        if (indexMapNeedsRevalidation) {
            sortOnField(getCurrentSortField(), up);
            indexMapNeedsRevalidation = false;
            return;
        }
        if (sortDirectionUp == up) return;
//        System.out.println("InstalledLookAndFeelStructure setSortDirection up? " + up);
        sortDirectionUp = up;
        OrderedMap tmpMap;
        if (up)
            tmpMap = new OrderedMap(new com.objectspace.jgl.LessString(), true);
        else
            tmpMap = new OrderedMap(new com.objectspace.jgl.GreaterString(), true);
        com.objectspace.jgl.OrderedMapIterator iter = indexMap.begin();
        while (!iter.atEnd()) {
            Object key = iter.key();
            Object index = iter.value();
            tmpMap.add(key, index);
            iter.advance();
        }
        indexMap.clear();
        indexMap = tmpMap;
    }

    public void setSortField(int fld, boolean up) {
        if (fld == sortField && sortDirectionUp == up) return;
        if (fld != NAME && fld != CLASSNAME && fld != AVAILABILITY) return;
        sortOnField(fld, up);
    }

    private void sortOnField(int fld, boolean up) {
        sortField = fld;
        sortDirectionUp = up;
        indexMap.clear();
        if (up)
            indexMap = new OrderedMap(new com.objectspace.jgl.LessString(), true);
        else
            indexMap = new OrderedMap(new com.objectspace.jgl.GreaterString(), true);
        if (sortField == NAME) {
            for (int i=0; i<lookAndFeelNames.size(); i++)
                indexMap.add(lookAndFeelNames.get(i), new Integer(i));
        }else if (sortField == CLASSNAME) {
            for (int i=0; i<lookAndFeelClassNames.size(); i++)
                indexMap.add(lookAndFeelClassNames.get(i), new Integer(i));
        }else{
            for (int i=0; i<lookAndFeelAvailability.size(); i++)
                indexMap.add(new Boolean(lookAndFeelAvailability.get(i)), new Integer(i));
        }
    }

    public int getCurrentSortField() {
        return sortField;
    }

    public boolean isSortDirectionUp() {
        return sortDirectionUp;
    }

    public String toString() {
        LAFEntry[] entries = getSortedEntries();
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<entries.length; i++) {
            sb.append(entries[i].name);
            sb.append('\t');
            sb.append(entries[i].className);
            sb.append('\t');
            sb.append(entries[i].availability);
            sb.append('\n');
        }
        return sb.toString();
    }

    private void recreateIndexMap() {
        indexMap.clear();
        if (sortField == NAME) {
            for (int i=0; i<lookAndFeelNames.size(); i++)
                indexMap.add(lookAndFeelNames.get(i), new Integer(i));
        }else if (sortField == CLASSNAME) {
            for (int i=0; i<lookAndFeelClassNames.size(); i++)
                indexMap.add(lookAndFeelClassNames.get(i), new Integer(i));
        }else{
            for (int i=0; i<lookAndFeelAvailability.size(); i++)
                indexMap.add(new Boolean(lookAndFeelAvailability.get(i)), new Integer(i));
        }
    }

    public Object clone() throws CloneNotSupportedException {
        InstalledLookAndFeelStructure ilafs = (InstalledLookAndFeelStructure) super.clone();
//        InstalledLookAndFeelStructure ics = new InstalledLookAndFeelStructure();
        ilafs.lookAndFeelAvailability = (BoolBaseArray) lookAndFeelAvailability.clone(); //new BoolBaseArray(lookAndFeelAvailability.size()); //(BoolBaseArray) lookAndFeelAvailability.clone();
        ilafs.lookAndFeelNames = (StringBaseArray) lookAndFeelNames.clone();
        ilafs.lookAndFeelClassNames = (StringBaseArray) lookAndFeelClassNames.clone();

/*        for (int i=0; i<lookAndFeelAvailability.size(); i++)
            ics.lookAndFeelAvailability.add(lookAndFeelAvailability.get(i));
        ics.lookAndFeelNames = new StringBaseArray(lookAndFeelNames.size()); //(StringBaseArray) lookAndFeelNames.clone();
        for (int i=0; i<lookAndFeelNames.size(); i++)
            ics.lookAndFeelNames.add(lookAndFeelNames.get(i));
        ics.lookAndFeelClassNames = new StringBaseArray(lookAndFeelClassNames.size()); //(StringBaseArray) lookAndFeelClassNames.clone();
        for (int i=0; i<lookAndFeelClassNames.size(); i++)
            ics.lookAndFeelClassNames.add(lookAndFeel
        ClassNames.get(i));
*/
        ilafs.indexMap = (OrderedMap) indexMap.clone();
//        ics.setSortField(sortField, sortDirectionUp);

        return ilafs;
    }
}
