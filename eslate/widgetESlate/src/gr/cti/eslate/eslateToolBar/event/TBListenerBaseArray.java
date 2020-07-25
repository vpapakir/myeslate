package gr.cti.eslate.eslateToolBar.event;

import gr.cti.typeArray.*;

/**
 * 
 * A collection of ToolBarListeners, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */

public class TBListenerBaseArray extends ArrayBase
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    protected ToolBarListener[] baseArray;

    public TBListenerBaseArray(int size, int growth) {
        super(size, growth, ToolBarListener.class);
    }

    public TBListenerBaseArray(int size) {
        super(size, ToolBarListener.class);
    }

    public TBListenerBaseArray() {
        super(ToolBarListener.class);
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
        baseArray = (ToolBarListener[]) array;
    }

    /**
     * Implementation of callout to initialize a portion of the array.
     */
    protected void discardValues(int from, int to) {
        for (int i = from; i < to; i++) {
            baseArray[i] = null;
        }
    }

    /**
     * Append a value to the collection.
     */
    public int add(ToolBarListener value) {
        int index = getAddIndex();
        baseArray[index] = value;
        return index;
    }

    /**
     * Insert a value into the collection.
     */
    public void add(int index, ToolBarListener value) {
        makeInsertSpace(index);
        baseArray[index] = value;
    }

    /**
     * Get value from the collection.
     */
    public ToolBarListener get(int index) {
        if (index < countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Set the value at a position in the collection.
     */
    public void set(int index, ToolBarListener value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Convert to an array.
     */
    public ToolBarListener[] toArray() {
        return (ToolBarListener[]) buildArray();
    }

    /**
     * Return the first element in the collection.
     */
    public ToolBarListener front()
    {
      return get(0);
    }

    /**
     * Return the last ToolBarListener in the collection.
     * @return  The requested ToolBarListener.
     */
    public ToolBarListener back()
    {
      return get(countPresent - 1);
    }

    /**
     * Remove and return the first ToolBarListener in the collection.
     * @return  The removed ToolBarListener.
     */
    public ToolBarListener popFront()
    {
      ToolBarListener first = get(0);
      remove(0);
      return first;
    }

    /**
     * Insert a ToolBarListener in front of the first element in the collection.
     * @param   value   The ToolBarListener to insert.
     */
    public void pushFront(ToolBarListener value)
    {
      add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular ToolBarListener.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The ToolBarListener to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of ToolBarListeners that were removed.
     */
    public int removeElements(int first, int last, ToolBarListener element, int count)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        if (first > last) {
          int tmp = first;
          first = last;
          last = tmp;
        }
        int deleted = 0;
        if (element == null) {
          for (int i=first; i<=last && count>0; i++) {
            if (baseArray[i] == null) {
              remove(i);
              deleted++;
              count--;
              i--; last--; // i-th element was deleted, so retry
            }
          }
        }else{
          for (int i=first; i<=last && count>0; i++) {
            if (element.equals(baseArray[i])) {
              remove(i);
              deleted++;
              count--;
              i--; last--; // i-th element was deleted, so retry
            }
          }
        }
        return deleted;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Remove all elements that match a particular ToolBarListener.
     * @param   element The ToolBarListener to match.
     * @return  The number of ToolBarListeners that were removed.
     */
    public int removeElements(ToolBarListener element)
    {
      return removeElements(0, countPresent-1, element, countPresent);
    }

    /**
     * Remove at most a given number of elements that match a particular
     * ToolBarListener.
     * @param   element The ToolBarListener to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of ToolBarListeners that were removed.
     */
    public int removeElements(ToolBarListener element, int count)
    {
      return removeElements(0, countPresent-1, element, count);
    }

    /**
     * Remove all elements within a range of indices that match a particular
     * ToolBarListener.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The ToolBarListener to match.
     * @return  The number of ToolBarListeners that were removed.
     */
    public int removeElements(int first, int last, ToolBarListener element)
    {
      return removeElements(first, last, element, countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * ToolBarListener with a new value.
     * @param   first           The index of the first element to consider.
     * @param   last            The index of the last element to consider.
     * @param   oldValue        The ToolBarListener to match.
     * @param   newValue        The new value.
     * @return  The number of ToolBarListeners that were replaced.
     */
    public int replace(int first, int last, ToolBarListener oldValue, ToolBarListener newValue)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        int replaced = 0;
        if (oldValue == null) {
          for (int i=first; i<=last; i++) {
            if (baseArray[i] == null) {
              baseArray[i] = newValue;
              replaced++;
            }
          }
        }else{
          for (int i=first; i<=last; i++) {
            if (oldValue.equals(baseArray[i])) {
              baseArray[i] = newValue;
              replaced++;
            }
          }
        }
        return replaced;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Replace all elements that match a particular ToolBarListener with a new value.
     * @param   oldValue        The ToolBarListener to match.
     * @param   newValue        The new value.
     * @return  The number of ToolBarListeners that were replaced.
     */
    public int replace(ToolBarListener oldValue, ToolBarListener newValue)
    {
      return replace(0, countPresent-1, oldValue, newValue);
    }

    /**
     * Count all elements within a range of indices that match a particular
     * ToolBarListener.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The ToolBarListener to match.
     * @return  The number of ToolBarListeners matched.
     */
    public int count(int first, int last, ToolBarListener value)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        int found = 0;
        if (value == null) {
          for (int i=first; i<=last; i++) {
            if (baseArray[i] == null) {
              found++;
            }
          }
        }else{
          for (int i=first; i<=last; i++) {
            if (value.equals(baseArray[i])) {
              found++;
            }
          }
        }
        return found;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Count all elements that match a particular ToolBarListener.
     * @param   value   The ToolBarListener to match.
     * @return  The number of ToolBarListeners matched.
     */
    public int count(ToolBarListener value)
    {
      return count(0, countPresent-1, value);
    }

    /**
     * Return the index of the first object within a range of indices that
     * matches a particular ToolBarListener.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The ToolBarListener to match.
     * @return  The requested index or -1 if the ToolBarListener is not found.
     * @exception       ArrayIndexOutOfBoundsException  Thrown if
     *                  any of the specified indices is less than 0 or
     *                  greater or equal to the number of elements in the
     *                  collection.
     */
    public int indexOf(int first, int last, ToolBarListener value)
    {
      if (first >= 0 && first < countPresent &&
          last >= 0 && last < countPresent) {
        if (value == null) {
          for (int i=first; i<=last; i++) {
            if (baseArray[i] == null) {
              return i;
            }
          }
        }else{
          for (int i=first; i<=last; i++) {
            if (value.equals(baseArray[i])) {
              return i;
            }
          }
        }
        return -1;
      }else{
        throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Return the index of the first object that matches a particular ToolBarListener.
     * @param   value   The ToolBarListener to match.
     * @return  The requested index or -1 if the ToolBarListener is not found.
     */
    public int indexOf(ToolBarListener value)
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
     * Check if the collection contains a particular ToolBarListener.
     * @param   value   The ToolBarListener to match.
     * @return  True if the ToolBarListener is contained in the collection, false
     *          otherwise.
     */
    public boolean contains(ToolBarListener value)
    {
      return (indexOf(value) != -1);
    }
}
