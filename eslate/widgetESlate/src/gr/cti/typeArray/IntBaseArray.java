package gr.cti.typeArray;

/**
 * 
 * A collection of ints, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @version     2.0.0, 19-May-2006
 */

public class IntBaseArray extends ArrayBase
{
    static final long serialVersionUID = 663357122772378605L;

    protected int[] baseArray;

    public IntBaseArray(int size, int growth) {
        super(size, growth, Integer.TYPE);
    }

    public IntBaseArray(int size) {
        super(size, Integer.TYPE);
    }

    public IntBaseArray() {
        super(Integer.TYPE);
    }

    /**
     *  Implementation of callout to get the underlying array.
     */
    protected Object getArray() {
        return baseArray;
    }

    /**
     * Implementation of callout to set the underlying array.
     */
    protected void setArray(Object array) {
        baseArray = (int[]) array;
    }

    /**
     * Implementation of callout to initialize a portion of the array.
     */
    protected void discardValues(int from, int to) {
        for (int i = from; i < to; i++) {
            baseArray[i] = 0;
        }
    }

    /**
     * Append a value to the collection.
     */
    public int add(int value) {
        int index = getAddIndex();
        baseArray[index] = value;
        return index;
    }

    /**
     * Insert a value into the collection.
     */
    public void add(int index, int value) {
        makeInsertSpace(index);
        baseArray[index] = value;
    }

    /**
     * Get value from the collection.
     */
    public int get(int index) {
        if (index < countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Set the value at a position in the collection.
     */
    public void set(int index, int value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Convert to an array.
     */
    public int[] toArray() {
        return (int[]) buildArray();
    }

    /**
     * Return the first element in the collection.
     */
    public int front()
    {
      return get(0);
    }

    /**
     * Return the last int in the collection.
     * @return  The requested int.
     */
    public int back()
    {
      return get(countPresent - 1);
    }

    /**
     * Remove and return the first int in the collection.
     * @return  The removed int.
     */
    public int popFront()
    {
      int first = get(0);
      remove(0);
      return first;
    }

    /**
     * Insert a int in front of the first element in the collection.
     * @param   value   The int to insert.
     */
    public void pushFront(int value)
    {
      add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular int.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The int to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of ints that were removed.
     */
    public int removeElements(int first, int last, int element, int count)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        if (first > last) {
          int tmp = first;
          first = last;
          last = tmp;
        }
        int deleted = 0;
        for (int i=first; i<=last && count>0; i++) {
          if (element == baseArray[i]) {
            remove(i);
            deleted++;
            count--;
            i--; last--; // i-th element was deleted, so retry
          }
        }
        return deleted;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Remove all elements that match a particular int.
     * @param   element The int to match.
     * @return  The number of ints that were removed.
     */
    public int removeElements(int element)
    {
      return removeElements(0, countPresent-1, element, countPresent);
    }

    /**
     * Remove at most a given number of elements that match a particular int.
     * @param   element The int to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of ints that were removed.
     */
    public int removeElements(int element, int count)
    {
      return removeElements(0, countPresent-1, element, count);
    }

    /**
     * Remove all elements within a range of indices that match a particular
     * int.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The int to match.
     * @return  The number of ints that were removed.
     */
    public int removeElements(int first, int last, int element)
    {
      return removeElements(first, last, element, countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * int with a new value.
     * @param   first           The index of the first element to consider.
     * @param   last            The index of the last element to consider.
     * @param   oldValue        The int to match.
     * @param   newValue        The new value.
     * @return  The number of ints that were replaced.
     */
    public int replace(int first, int last, int oldValue, int newValue)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        int replaced = 0;
        for (int i=first; i<=last; i++) {
          if (oldValue == baseArray[i]) {
            baseArray[i] = newValue;
            replaced++;
          }
        }
        return replaced;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Replace all elements that match a particular int with a new value.
     * @param   oldValue        The int to match.
     * @param   newValue        The new value.
     * @return  The number of ints that were replaced.
     */
    public int replace(int oldValue, int newValue)
    {
      return replace(0, countPresent-1, oldValue, newValue);
    }

    /**
     * Count all elements within a range of indices that match a particular
     * int.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The int to match.
     * @return  The number of ints matched.
     */
    public int count(int first, int last, int value)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        int found = 0;
        for (int i=first; i<=last; i++) {
          if (value == baseArray[i]) {
            found++;
          }
        }
        return found;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Count all elements that match a particular int.
     * @param   value   The int to match.
     * @return  The number of ints matched.
     */
    public int count(int value)
    {
      return count(0, countPresent-1, value);
    }

    /**
     * Return the index of the first object within a range of indices that
     * matches a particular int.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The int to match.
     * @return  The requested index or -1 if the int is not found.
     * @exception       ArrayIndexOutOfBoundsException  Thrown if
     *                  any of the specified indices is less than 0 or
     *                  greater or equal to the number of elements in the
     *                  collection.
     */
    public int indexOf(int first, int last, int value)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        for (int i=first; i<=last; i++) {
          if (value == baseArray[i]) {
            return i;
          }
        }
        return -1;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Return the index of the first object that matches a particular int.
     * @param   value   The int to match.
     * @return  The requested index or -1 if the int is not found.
     */
    public int indexOf(int value)
    {
      int result;
      try {
        result = indexOf(0, countPresent-1, value);
      } catch (ArrayIndexOutOfBoundsException e) {
        result = -1;
      }
      return result;
    }

    /**
     * Check if the collection contains a particular int.
     * @param   value   The int to match.
     * @return  True if the int is contained in the collection, false
     *          otherwise.
     */
    public boolean contains(int value)
    {
      return (indexOf(value) != -1);
    }
}
