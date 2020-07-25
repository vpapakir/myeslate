package gr.cti.typeArray;

/**
 * 
 * A collection of chars, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */

public class CharBaseArray extends ArrayBase
{
    static final long serialVersionUID = -6505709119684172034L;

    protected char[] baseArray;

    public CharBaseArray(int size, int growth) {
        super(size, growth, Character.TYPE);
    }

    public CharBaseArray(int size) {
        super(size, Character.TYPE);
    }

    public CharBaseArray() {
        super(Character.TYPE);
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
        baseArray = (char[]) array;
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
    public int add(char value) {
        int index = getAddIndex();
        baseArray[index] = value;
        return index;
    }

    /**
     * Insert a value into the collection.
     */
    public void add(int index, char value) {
        makeInsertSpace(index);
        baseArray[index] = value;
    }

    /**
     * Get value from the collection.
     */
    public char get(int index) {
        if (index < countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Set the value at a position in the collection.
     */
    public void set(int index, char value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Convert to an array.
     */
    public char[] toArray() {
        return (char[]) toArray();
    }

    /**
     * Return the first element in the collection.
     */
    public char front()
    {
      return get(0);
    }

    /**
     * Return the last char in the collection.
     * @return  The requested char.
     */
    public char back()
    {
      return get(countPresent - 1);
    }

    /**
     * Remove and return the first char in the collection.
     * @return  The removed char.
     */
    public char popFront()
    {
      char first = get(0);
      remove(0);
      return first;
    }

    /**
     * Insert a char in front of the first element in the collection.
     * @param   value   The char to insert.
     */
    public void pushFront(char value)
    {
      add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular char.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The char to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of chars that were removed.
     */
    public int removeElements(int first, int last, char element, int count)
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
     * Remove all elements that match a particular char.
     * @param   element The char to match.
     * @return  The number of chars that were removed.
     */
    public int removeElements(char element)
    {
      return removeElements(0, countPresent-1, element, countPresent);
    }

    /**
     * Remove at most a given number of elements that match a particular char.
     * @param   element The char to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of chars that were removed.
     */
    public int removeElements(char element, int count)
    {
      return removeElements(0, countPresent-1, element, count);
    }

    /**
     * Remove all elements within a range of indices that match a particular
     * char.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The char to match.
     * @return  The number of chars that were removed.
     */
    public int removeElements(int first, int last, char element)
    {
      return removeElements(first, last, element, countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * char with a new value.
     * @param   first           The index of the first element to consider.
     * @param   last            The index of the last element to consider.
     * @param   oldValue        The char to match.
     * @param   newValue        The new value.
     * @return  The number of chars that were replaced.
     */
    public int replace(int first, int last, char oldValue, char newValue)
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
     * Replace all elements that match a particular char with a new value.
     * @param   oldValue        The char to match.
     * @param   newValue        The new value.
     * @return  The number of chars that were replaced.
     */
    public int replace(char oldValue, char newValue)
    {
      return replace(0, countPresent-1, oldValue, newValue);
    }

    /**
     * Count all elements within a range of indices that match a particular
     * char.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The char to match.
     * @return  The number of chars matched.
     */
    public int count(int first, int last, char value)
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
     * Count all elements that match a particular char.
     * @param   value   The char to match.
     * @return  The number of chars matched.
     */
    public int count(char value)
    {
      return count(0, countPresent-1, value);
    }

    /**
     * Return the index of the first object within a range of indices that
     * matches a particular char.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The char to match.
     * @return  The requested index or -1 if the char is not found.
     * @exception       ArrayIndexOutOfBoundsException  Thrown if
     *                  any of the specified indices is less than 0 or
     *                  greater or equal to the number of elements in the
     *                  collection.
     */
    public int indexOf(int first, int last, char value)
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
     * Return the index of the first object that matches a particular char.
     * @param   value   The char to match.
     * @return  The requested index or -1 if the char is not found.
     */
    public int indexOf(char value)
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
     * Check if the collection contains a particular char.
     * @param   value   The char to match.
     * @return  True if the char is contained in the collection, false
     *          otherwise.
     */
    public boolean contains(char value)
    {
      return (indexOf(value) != -1);
    }
}
