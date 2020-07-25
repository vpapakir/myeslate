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
public class InstalledComponentStructure implements Cloneable {
    public static final int NAME = 1;
    public static final int CLASSNAME = 2;
    public static final int AVAILABILITY = 3;
    private BoolBaseArray componentAvailability;
    private StringBaseArray componentNames;
    private StringBaseArray componentClassNames;
    private StringBaseArray componentGroup;
    private BoolBaseArray componentVisual;
    private OrderedMap indexMap;
    private boolean sortDirectionUp = true;
    private int sortField = NAME;
    private boolean indexMapNeedsRevalidation = false;

    public InstalledComponentStructure() {
        componentAvailability = new BoolBaseArray();
        componentNames = new StringBaseArray();
        componentClassNames = new StringBaseArray();
        componentVisual = new BoolBaseArray();
        componentGroup=new StringBaseArray();
        indexMap = new OrderedMap(new com.objectspace.jgl.LessString(), true);
    }

    public void add(String name, String className, boolean available, boolean visual,String group) throws DuplicateEntryException {
        if (name == null || name.trim().length() == 0) return;
        if (className == null || className.trim().length() == 0) return;
//System.out.println("Adding name: " + name + ", componentNames.size(): " + componentNames.size());
        if (componentNames.size() != 0 && componentNames.contains(name))
            throw new DuplicateEntryException(NAME);
        if (componentClassNames.size() != 0 && componentClassNames.contains(className))
            throw new DuplicateEntryException(CLASSNAME);

        componentNames.add(name);
        componentClassNames.add(className);
        componentAvailability.add(available);
        componentVisual.add(visual);
        componentGroup.add(group);
/*        try{
            Class cls = Class.forName(className);
            if (java.awt.Component.class.isAssignableFrom(cls))
                componentVisual.add(true);
            else
                componentVisual.add(false);
        }catch (Throwable exc) {
            componentVisual.add(false);
        }
*/
        if (sortField == NAME)
            indexMap.add(name, new Integer(componentNames.size()-1));
        else if (sortField == CLASSNAME)
            indexMap.add(className, new Integer(componentNames.size()-1));
        else
            indexMap.add(new Boolean(available), new Integer(componentNames.size()-1));
    }

    public void remove(String name) {
        if (name == null || name.trim().length() == 0) return;
        int index = componentNames.indexOf(name);
        if (index == -1) return;
        Enumeration enumeration = indexMap.keys(new Integer(index));
        indexMap.remove(enumeration.nextElement());
        componentNames.remove(index);
        componentClassNames.remove(index);
        componentAvailability.remove(index);
        componentVisual.remove(index);
        componentGroup.remove(index);
        recreateIndexMap();
    }

    public void remove(int index) {
        if (index < 0 || index >= componentNames.size()) return;
        Enumeration enumeration = indexMap.keys(new Integer(index));
        indexMap.remove(enumeration.nextElement());
        componentNames.remove(index);
        componentClassNames.remove(index);
        componentAvailability.remove(index);
        componentVisual.remove(index);
        componentGroup.remove(index);
        recreateIndexMap();
    }

    public String getClassName(String name) {
        int index = componentNames.indexOf(name);
        if (index == -1) return null;
        return componentClassNames.get(index);
    }

    public void setClassName(String name, String className) {
        int index = componentNames.indexOf(name);
        if (index == -1) return;
        componentClassNames.set(index, className);
        if (sortField == CLASSNAME)
            indexMapNeedsRevalidation = true;
    }

    public boolean getAvailability(String name) {
        int index = componentNames.indexOf(name);
        if (index == -1) return false;
        return componentAvailability.get(index);
    }

    public void setAvailability(String name, boolean bool) {
        int index = componentNames.indexOf(name);
        if (index == -1) return;
        componentAvailability.set(index, bool);
        if (sortField == AVAILABILITY)
            indexMapNeedsRevalidation = true;
    }

    public String getName(String className) {
        int index = componentClassNames.indexOf(className);
        if (index == -1) return null;
        return componentNames.get(index);
    }

    public void setName(String name, String newName) {
        int index = componentNames.indexOf(name);
        if (index == -1) return;
        componentNames.set(index, newName);
        if (sortField == NAME)
            indexMapNeedsRevalidation = true;
    }

    StringBaseArray _getNames() {
        return componentNames;
    }

    StringBaseArray _getClassNames() {
        return componentClassNames;
    }

    public StringBaseArray getNames() {
        return (StringBaseArray) componentNames.clone();
    }

/*    public int getIndex(String name) {
        for (int i=0; i<componentNames.size(); i++) {
            if (componentNames.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }
*/
    public int getSize() {
        return componentNames.size();
    }

    /* Returns the names of those components among the installed which are either
     * visual or non-visual(invisible). The category is adjusted by the argument
     * of this method.
     */
    public StringBaseArray getNames(boolean visual) {
        StringBaseArray componentNames = new StringBaseArray();
        for (int i=0; i<componentNames.size(); i++) {
            if (componentVisual.get(i) == visual)
                componentNames.add(componentNames.get(i));
        }
        return componentNames;
    }

    /* Returns the number of those components among the installed which are either
     * visual or non-visual(invisible). The category is adjusted by the argument
     * of this method.
     */
    public int getSize(boolean visual) {
        int compoCount = 0;
        for (int i=0; i<componentNames.size(); i++) {
            if (componentVisual.get(i) == visual)
                compoCount++;
        }
        return compoCount;
    }

    public boolean containsComponentName(String name) {
        if (componentNames.size() == 0)
            return false;
        int index = componentNames.indexOf(name);
        if (index == -1) return false;
        return true;
    }

    public boolean containsComponentClassName(String className) {
        if (componentClassNames.size() == 0)
            return false;
        int index = componentClassNames.indexOf(className);
        if (index == -1) return false;
        return true;
    }

    public CompoEntry[] getSortedEntries() {
        Enumeration enumeration = indexMap.elements();
        CompoEntry[] entries = new CompoEntry[getSize()];
        int i=0;
        while (enumeration.hasMoreElements()) {
            int index = ((Integer) enumeration.nextElement()).intValue();
            entries[i] = new CompoEntry(componentNames.get(index),
                                   componentClassNames.get(index),
                                   componentAvailability.get(index),
                                   componentVisual.get(index),
                                   componentGroup.get(index));
//            System.out.println("i: " + i + " --> " + entries[i].name + ", " + entries[i].className + ", " + entries[i].availability);
            i++;
        }
        return entries;
    }

    /* Returns a sorted array of all the components which are either visual or
     * non-visual. The sorting is based on the current sorting in the
     * InstalledComponentStructure.
     */
    public CompoEntry[] getSortedEntries(boolean visual) {
        Enumeration enumeration = indexMap.elements();
        CompoEntry[] entries = new CompoEntry[getSize(visual)];
        int i=0;
        while (enumeration.hasMoreElements()) {
            int index = ((Integer) enumeration.nextElement()).intValue();
            if (componentVisual.get(index) == visual) {
                entries[i] = new CompoEntry(componentNames.get(index),
                                       componentClassNames.get(index),
                                       componentAvailability.get(index),
                                       visual,
                                       componentGroup.get(index));
    //            System.out.println("i: " + i + " --> " + entries[i].name + ", " + entries[i].className + ", " + entries[i].availability);
                i++;
            }
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
//        System.out.println("InstalledComponentStructure setSortDirection up? " + up);
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
            for (int i=0; i<componentNames.size(); i++)
                indexMap.add(componentNames.get(i), new Integer(i));
        }else if (sortField == CLASSNAME) {
            for (int i=0; i<componentClassNames.size(); i++)
                indexMap.add(componentClassNames.get(i), new Integer(i));
        }else{
            for (int i=0; i<componentAvailability.size(); i++)
                indexMap.add(new Boolean(componentAvailability.get(i)), new Integer(i));
        }
    }

    public int getCurrentSortField() {
        return sortField;
    }

    public boolean isSortDirectionUp() {
        return sortDirectionUp;
    }

    public String toString() {
        CompoEntry[] entries = getSortedEntries();
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
            for (int i=0; i<componentNames.size(); i++)
                indexMap.add(componentNames.get(i), new Integer(i));
        }else if (sortField == CLASSNAME) {
            for (int i=0; i<componentClassNames.size(); i++)
                indexMap.add(componentClassNames.get(i), new Integer(i));
        }else{
            for (int i=0; i<componentAvailability.size(); i++)
                indexMap.add(new Boolean(componentAvailability.get(i)), new Integer(i));
        }
    }

    public Object clone() throws CloneNotSupportedException {
        InstalledComponentStructure ics = (InstalledComponentStructure) super.clone();
//        InstalledComponentStructure ics = new InstalledComponentStructure();
        ics.componentAvailability = (BoolBaseArray) componentAvailability.clone(); //new BoolBaseArray(componentAvailability.size()); //(BoolBaseArray) componentAvailability.clone();
        ics.componentNames = (StringBaseArray) componentNames.clone();
        ics.componentClassNames = (StringBaseArray) componentClassNames.clone();
        ics.componentVisual = (BoolBaseArray) componentVisual.clone();

/*        for (int i=0; i<componentAvailability.size(); i++)
            ics.componentAvailability.add(componentAvailability.get(i));
        ics.componentNames = new StringBaseArray(componentNames.size()); //(StringBaseArray) componentNames.clone();
        for (int i=0; i<componentNames.size(); i++)
            ics.componentNames.add(componentNames.get(i));
        ics.componentClassNames = new StringBaseArray(componentClassNames.size()); //(StringBaseArray) componentClassNames.clone();
        for (int i=0; i<componentClassNames.size(); i++)
            ics.componentClassNames.add(componentClassNames.get(i));
*/
        ics.indexMap = (OrderedMap) indexMap.clone();
//        ics.setSortField(sortField, sortDirectionUp);

        return ics;
    }
}
