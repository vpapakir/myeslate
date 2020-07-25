package gr.cti.eslate.eslateToolBar;

import gr.cti.typeArray.*;

/**
 * 
 * A collection of ButtonGroupEditorGroupInfos, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */

public class ButtonGroupEditorGroupInfoBaseArray extends ArrayBase
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    protected ButtonGroupEditorGroupInfo[] baseArray;

    public ButtonGroupEditorGroupInfoBaseArray(int size, int growth) {
        super(size, growth, ButtonGroupEditorGroupInfo.class);
    }

    public ButtonGroupEditorGroupInfoBaseArray(int size) {
        super(size, ButtonGroupEditorGroupInfo.class);
    }

    public ButtonGroupEditorGroupInfoBaseArray() {
        super(ButtonGroupEditorGroupInfo.class);
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
        baseArray = (ButtonGroupEditorGroupInfo[]) array;
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
    public int add(ButtonGroupEditorGroupInfo value) {
        int index = getAddIndex();
        baseArray[index] = value;
        return index;
    }

    /**
     * Insert a value into the collection.
     */
    public void add(int index, ButtonGroupEditorGroupInfo value) {
        makeInsertSpace(index);
        baseArray[index] = value;
    }

    /**
     * Get value from the collection.
     */
    public ButtonGroupEditorGroupInfo get(int index) {
        if (index < countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Set the value at a position in the collection.
     */
    public void set(int index, ButtonGroupEditorGroupInfo value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Convert to an array.
     */
    public ButtonGroupEditorGroupInfo[] toArray() {
        return (ButtonGroupEditorGroupInfo[]) buildArray();
    }

    /**
     * Return the first element in the collection.
     */
    public ButtonGroupEditorGroupInfo front()
    {
      return get(0);
    }

    /**
     * Return the last ButtonGroupEditorGroupInfo in the collection.
     * @return  The requested ButtonGroupEditorGroupInfo.
     */
    public ButtonGroupEditorGroupInfo back()
    {
      return get(countPresent - 1);
    }

    /**
     * Remove and return the first ButtonGroupEditorGroupInfo in the collection.
     * @return  The removed ButtonGroupEditorGroupInfo.
     */
    public ButtonGroupEditorGroupInfo popFront()
    {
      ButtonGroupEditorGroupInfo first = get(0);
      remove(0);
      return first;
    }

    /**
     * Insert a ButtonGroupEditorGroupInfo in front of the first element in the collection.
     * @param   value   The ButtonGroupEditorGroupInfo to insert.
     */
    public void pushFront(ButtonGroupEditorGroupInfo value)
    {
      add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular ButtonGroupEditorGroupInfo.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The ButtonGroupEditorGroupInfo to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of ButtonGroupEditorGroupInfos that were removed.
     */
    public int removeElements(int first, int last, ButtonGroupEditorGroupInfo element, int count)
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
     * Remove all elements that match a particular ButtonGroupEditorGroupInfo.
     * @param   element The ButtonGroupEditorGroupInfo to match.
     * @return  The number of ButtonGroupEditorGroupInfos that were removed.
     */
    public int removeElements(ButtonGroupEditorGroupInfo element)
    {
      return removeElements(0, countPresent-1, element, countPresent);
    }

    /**
     * Remove at most a given number of elements that match a particular ButtonGroupEditorGroupInfo.
     * @param   element The ButtonGroupEditorGroupInfo to match.
     * @param   count   The maximum number of elements to remove.
     * @return  The number of ButtonGroupEditorGroupInfos that were removed.
     */
    public int removeElements(ButtonGroupEditorGroupInfo element, int count)
    {
      return removeElements(0, countPresent-1, element, count);
    }

    /**
     * Remove all elements within a range of indices that match a particular
     * ButtonGroupEditorGroupInfo.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   element The ButtonGroupEditorGroupInfo to match.
     * @return  The number of ButtonGroupEditorGroupInfos that were removed.
     */
    public int removeElements(int first, int last, ButtonGroupEditorGroupInfo element)
    {
      return removeElements(first, last, element, countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * ButtonGroupEditorGroupInfo with a new value.
     * @param   first           The index of the first element to consider.
     * @param   last            The index of the last element to consider.
     * @param   oldValue        The ButtonGroupEditorGroupInfo to match.
     * @param   newValue        The new value.
     * @return  The number of ButtonGroupEditorGroupInfos that were replaced.
     */
    public int replace(int first, int last, ButtonGroupEditorGroupInfo oldValue, ButtonGroupEditorGroupInfo newValue)
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
     * Replace all elements that match a particular ButtonGroupEditorGroupInfo with a new value.
     * @param   oldValue        The ButtonGroupEditorGroupInfo to match.
     * @param   newValue        The new value.
     * @return  The number of ButtonGroupEditorGroupInfos that were replaced.
     */
    public int replace(ButtonGroupEditorGroupInfo oldValue, ButtonGroupEditorGroupInfo newValue)
    {
      return replace(0, countPresent-1, oldValue, newValue);
    }

    /**
     * Count all elements within a range of indices that match a particular
     * ButtonGroupEditorGroupInfo.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The ButtonGroupEditorGroupInfo to match.
     * @return  The number of ButtonGroupEditorGroupInfos matched.
     */
    public int count(int first, int last, ButtonGroupEditorGroupInfo value)
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
     * Count all elements that match a particular ButtonGroupEditorGroupInfo.
     * @param   value   The ButtonGroupEditorGroupInfo to match.
     * @return  The number of ButtonGroupEditorGroupInfos matched.
     */
    public int count(ButtonGroupEditorGroupInfo value)
    {
      return count(0, countPresent-1, value);
    }

    /**
     * Return the index of the first object within a range of indices that
     * matches a particular ButtonGroupEditorGroupInfo.
     * @param   first   The index of the first element to consider.
     * @param   last    The index of the last element to consider.
     * @param   value   The ButtonGroupEditorGroupInfo to match.
     * @return  The requested index or -1 if the ButtonGroupEditorGroupInfo is not found.
     * @exception       ArrayIndexOutOfBoundsException  Thrown if
     *                  any of the specified indices is less than 0 or
     *                  greater or equal to the number of elements in the
     *                  collection.
     */
    public int indexOf(int first, int last, ButtonGroupEditorGroupInfo value)
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
     * Return the index of the first object that matches a particular ButtonGroupEditorGroupInfo.
     * @param   value   The ButtonGroupEditorGroupInfo to match.
     * @return  The requested index or -1 if the ButtonGroupEditorGroupInfo is not found.
     */
    public int indexOf(ButtonGroupEditorGroupInfo value)
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
     * Check if the collection contains a particular ButtonGroupEditorGroupInfo.
     * @param   value   The ButtonGroupEditorGroupInfo to match.
     * @return  True if the ButtonGroupEditorGroupInfo is contained in the collection, false
     *          otherwise.
     */
    public boolean contains(ButtonGroupEditorGroupInfo value)
    {
      return (indexOf(value) != -1);
    }
}
