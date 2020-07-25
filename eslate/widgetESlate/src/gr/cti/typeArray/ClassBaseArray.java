package gr.cti.typeArray;

/**
 *
 * An array of Class objects
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @author      Giorgos Vasiliou
 * @version     2.0.0, 19-May-2006
 */

public class ClassBaseArray extends gr.cti.typeArray.ArrayBase
{
    static final long serialVersionUID = 4621274198217662881L;

    protected Class[] baseArray;

    public ClassBaseArray(int size,int growth) {
        super(size,growth,Class.class);
    }

    public ClassBaseArray(int size) {
        super(size,Class.class);
    }

    public ClassBaseArray() {
        super(Class.class);
    }
    /**
     * Implementation of callout to get the underlying array.
     */
    protected Object getArray() {
        return baseArray;
    }
    /**
     * Implementation of callout to set the underlying array.
     */
    protected void setArray(Object array) {
        baseArray = (Class[]) array;
    }
    /**
     * Implementation of callout to initialize a portion of the array.
     */
    protected void discardValues(int from, int to) {
        for (int i=from;i<to;i++)
            baseArray[i]=null;
    }
    /**
     * Append a value to the collection.
     */
    public int add(Class value) {
        int index=getAddIndex();
        baseArray[index]=value;
        return index;
    }
    /**
     * Insert a value into the collection.
     */
    public void add(int index,Class value) {
        makeInsertSpace(index);
        baseArray[index]=value;
    }
    /**
     * Get value from the collection.
     */
    public Class get(int index) {
        if (index<countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }
    /**
     * Set the value at a position in the collection.
     */
    public void set(int index,Class value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }
    /**
     * Convert to an array.
     */
    public Class[] toArray() {
        return (Class[]) buildArray();
    }
    /**
     * Return the first element in the collection.
     */
    public Class front() {
        return get(0);
    }
    /**
     * Return the last Class in the collection.
     * @return  The requested Class.
     */
    public Class back() {
        return get(countPresent-1);
    }
    /**
     * Remove and return the first Class in the collection.
     * @return  The removed Class.
     */
    public Class popFront() {
        Class first=get(0);
        remove(0);
        return first;
    }

    /**
     * Insert a Class in front of the first element in the collection.
     * @param   value   The Class to insert.
     */
    public void pushFront(Class value) {
        add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular Class.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The Class to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of Classs that were removed.
     */
    public int removeElements(int first,int last,Class element,int count) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
                if (first > last) {
                    int tmp=first;
                    first=last;
                    last=tmp;
                }
                int deleted = 0;
                if (element == null) {
                    for (int i=first; i<=last && count>0; i++)
                        if (baseArray[i] == null) {
                            remove(i);
                            deleted++;
                            count--;
                            i--; last--; // i-th element was deleted, so retry
                        }
                } else {
                    for (int i=first; i<=last && count>0; i++)
                        if (element.equals(baseArray[i])) {
                        remove(i);
                        deleted++;
                        count--;
                        i--; last--; // i-th element was deleted, so retry
                        }
            }
                return deleted;
        } else
                throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Remove all elements that match a particular Class.
     * @param   element The Class to match.
     * @return  The number of Classs that were removed.
     */
    public int removeElements(Class element) {
        return removeElements(0,countPresent-1,element,countPresent);
    }
    /**
     * Remove at most a given number of elements that match a particular Class.
     * @param   element The Class to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of Classes that were removed.
     */
    public int removeElements(Class element, int count) {
        return removeElements(0,countPresent-1,element,count);
    }
    /**
     * Remove all elements within a range of indices that match a particular
     * Class.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The Class to match.
     * @return  The number of Class that were removed.
     */
    public int removeElements(int first,int last,Class element) {
        return removeElements(first,last,element,countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * Class with a new value.
     * @param   first           The index of the first element to consider.
     * @param   last            The index of the last element to consider.
     * @param   oldValue        The Class to match.
     * @param   newValue        The new value.
     * @return  The number of Classs that were replaced.
     */
    public int replace(int first, int last, Class oldValue, Class newValue) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
            int replaced = 0;
                if (oldValue == null) {
                    for (int i=first; i<=last; i++)
                        if (baseArray[i] == null) {
                            baseArray[i] = newValue;
                            replaced++;
                        }
                } else {
                    for (int i=first; i<=last; i++)
                        if (oldValue.equals(baseArray[i])) {
                            baseArray[i] = newValue;
                            replaced++;
                        }
            }
                return replaced;
        } else
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Replace all elements that match a particular Class with a new value.
     * @param   oldValue        The Class to match.
     * @param   newValue        The new value.
     * @return  The number of Class that were replaced.
     */
    public int replace(Class oldValue,Class newValue) {
        return replace(0,countPresent-1,oldValue,newValue);
    }
    /**
     * Count all elements within a range of indices that match a particular
     * Class.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The Class to match.
     * @return  The number of Classes matched.
     */
    public int count(int first,int last,Class value) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
            int found = 0;
            if (value == null) {
                for (int i=first; i<=last; i++)
                    if (baseArray[i] == null)
                        found++;
            } else {
                for (int i=first; i<=last; i++)
                    if (value.equals(baseArray[i]))
                        found++;
            }
            return found;
        } else
                throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Count all elements that match a particular Class.
     * @param   value   The Class to match.
     * @return  The number of Classs matched.
     */
    public int count(Class value) {
        return count(0,countPresent-1,value);
    }
    /**
     * Return the index of the first object within a range of indices that
     * matches a particular Class.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The Class to match.
     * @return  The requested index or -1 if the Class is not found.
     */
    public int indexOf(int first,int last,Class value) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
                if (value == null) {
                    for (int i=first; i<=last; i++)
                        if (baseArray[i] == null)
                            return i;
                } else {
                for (int i=first; i<=last; i++)
                        if (value.equals(baseArray[i]))
                            return i;
            }
                return -1;
        } else
                throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Return the index of the first object that matches a particular Class.
     * @param   value   The Class to match.
     * @return  The requested index or -1 if the Class is not found.
     */
    public int indexOf(Class value) {
      return indexOf(0, countPresent-1, value);
    }
    /**
     * Check if the collection contains a particular Class.
     * @param   value   The Class to match.
     * @return  True if the Class is contained in the collection, false
     *          otherwise.
     */
    public boolean contains(Class value) {
        return (indexOf(value)!=-1);
    }
}
