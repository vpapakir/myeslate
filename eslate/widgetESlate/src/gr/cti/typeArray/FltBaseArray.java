package gr.cti.typeArray;

/**
 * 
 * A collection of floats, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */

public class FltBaseArray extends ArrayBase
{
    static final long serialVersionUID = 1650613218413691117L;

    protected float[] baseArray;

    public FltBaseArray(int size, int growth) {
        super(size, growth, Float.TYPE);
    }

    public FltBaseArray(int size) {
        super(size, Float.TYPE);
    }

    public FltBaseArray() {
        super(Float.TYPE);
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
        baseArray = (float[]) array;
    }

    /**
     * Implementation of callout to initialize a portion of the array.
     */
    protected void discardValues(int from, int to) {
        for (int i = from; i < to; i++) {
            baseArray[i] = 0.0f;
        }
    }

    /**
     * Append a value to the collection.
     */
    public int add(float value) {
        int index = getAddIndex();
        baseArray[index] = value;
        return index;
    }

    /**
     * Insert a value into the collection.
     */
    public void add(int index, float value) {
        makeInsertSpace(index);
        baseArray[index] = value;
    }

    /**
     * Get value from the collection.
     */
    public float get(int index) {
        if (index < countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Set the value at a position in the collection.
     */
    public void set(int index, float value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Convert to an array.
     */
    public float[] toArray() {
        return (float[]) buildArray();
    }

    /**
     * Return the first element in the collection.
     */
    public float front()
    {
      return get(0);
    }

    /**
     * Return the last float in the collection.
     * @return  The requested float.
     */
    public float back()
    {
      return get(countPresent - 1);
    }

    /**
     * Remove and return the first float in the collection.
     * @return  The removed float.
     */
    public float popFront()
    {
      float first = get(0);
      remove(0);
      return first;
    }

    /**
     * Insert a float in front of the first element in the collection.
     * @param   value   The float to insert.
     */
    public void pushFront(float value)
    {
      add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular float.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The float to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of floats that were removed.
     */
    public int removeElements(int first, int last, float element, int count)
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
     * Remove all elements that match a particular float.
     * @param   element The float to match.
     * @return  The number of floats that were removed.
     */
    public int removeElements(float element)
    {
      return removeElements(0, countPresent-1, element, countPresent);
    }

    /**
     * Remove at most a given number of elements that match a particular float.
     * @param   element The float to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of floats that were removed.
     */
    public int removeElements(float element, int count)
    {
      return removeElements(0, countPresent-1, element, count);
    }

    /**
     * Remove all elements within a range of indices that match a particular
     * float.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The float to match.
     * @return  The number of floats that were removed.
     */
    public int removeElements(int first, int last, float element)
    {
      return removeElements(first, last, element, countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * float with a new value.
     * @param   first           The index of the first element to consider.
     * @param   last            The index of the last element to consider.
     * @param   oldValue        The float to match.
     * @param   newValue        The new value.
     * @return  The number of floats that were replaced.
     */
    public int replace(int first, int last, float oldValue, float newValue)
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
     * Replace all elements that match a particular float with a new value.
     * @param   oldValue        The float to match.
     * @param   newValue        The new value.
     * @return  The number of floats that were replaced.
     */
    public int replace(float oldValue, float newValue)
    {
      return replace(0, countPresent-1, oldValue, newValue);
    }

    /**
     * Count all elements within a range of indices that match a particular
     * float.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The float to match.
     * @return  The number of floats matched.
     */
    public int count(int first, int last, float value)
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
     * Count all elements that match a particular float.
     * @param   value   The float to match.
     * @return  The number of floats matched.
     */
    public int count(float value)
    {
      return count(0, countPresent-1, value);
    }

    /**
     * Return the index of the first object within a range of indices that
     * matches a particular float.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The float to match.
     * @return  The requested index or -1 if the float is not found.
     * @exception       ArrayIndexOutOfBoundsException  Thrown if
     *                  any of the specified indices is less than 0 or
     *                  greater or equal to the number of elements in the
     *                  collection.
     */
    public int indexOf(int first, int last, float value)
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
     * Return the index of the first object that matches a particular float.
     * @param   value   The float to match.
     * @return  The requested index or -1 if the float is not found.
     */
    public int indexOf(float value)
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
     * Check if the collection contains a particular float.
     * @param   value   The float to match.
     * @return  True if the float is contained in the collection, false
     *          otherwise.
     */
    public boolean contains(float value)
    {
      return (indexOf(value) != -1);
    }
}
