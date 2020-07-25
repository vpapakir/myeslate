package gr.cti.eslate.base.container;

import gr.cti.typeArray.*;

/**
 * 
 * A collection of DeferredPTGs, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class DeferredPTGBaseArray extends ArrayBase
{
  static final long serialVersionUID = 1L;

  protected DeferredPTG[] baseArray;

  public DeferredPTGBaseArray(int size, int growth)
  {
    super(size, growth, DeferredPTG.class);
  }

  public DeferredPTGBaseArray(int size)
  {
    super(size, DeferredPTG.class);
  }

  public DeferredPTGBaseArray()
  {
    super(DeferredPTG.class);
  }

  /**
   *  Implementation of callout to get the underlying array.
   */
  protected Object getArray()
  {
    return baseArray;
  }

  /**
   * Implementation of callout to set the underlying array.
   */
  protected void setArray(Object array)
  {
    baseArray = (DeferredPTG [])array;
  }

  /**
   * Implementation of callout to initialize a portion of the array.
   */
  protected void discardValues(int from, int to)
  {
    for (int i = from; i < to; i++) {
      baseArray[i] = null;
    }
  }

  /**
   * Append a value to the collection.
   */
  public int add(DeferredPTG value)
  {
    int index = getAddIndex();
    baseArray[index] = value;
    return index;
  }

  /**
   * Insert a value into the collection.
   */
  public void add(int index, DeferredPTG value)
  {
    makeInsertSpace(index);
    baseArray[index] = value;
  }

  /**
   * Get value from the collection.
   */
  public DeferredPTG get(int index)
  {
    if (index < countPresent) {
      return baseArray[index];
    } else {
      throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
  }

  /**
   * Set the value at a position in the collection.
   */
  public void set(int index, DeferredPTG value)
  {
    if (index < countPresent) {
      baseArray[index] = value;
    } else {
      throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
  }

  /**
   * Convert to an array.
   */
  public DeferredPTG[] toArray()
  {
    return (DeferredPTG []) buildArray();
  }

  /**
   * Return the first element in the collection.
   */
  public DeferredPTG front()
  {
    return get(0);
  }

  /**
   * Return the last DeferredPTG in the collection.
   * @return    The requested Object.
   */
  public DeferredPTG back()
  {
    return get(countPresent - 1);
  }

  /**
   * Remove and return the first DeferredPTG in the collection.
   * @return    The removed Object.
   */
  public DeferredPTG popFront()
  {
    DeferredPTG first = get(0);
    remove(0);
    return first;
  }

  /**
   * Insert a DeferredPTG in front of the first element in the
   * collection.
   * @param     value   The DeferredPTG to insert.
   */
  public void pushFront(DeferredPTG value)
  {
    add(0, value);
  }

  /**
   * Remove at most a given number of elements within a range of indices
   * that match a particular DeferredPTG.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     element The DeferredPTG to match.
   * @param     count   The maximum number of elements to remove.
   * @return    The number of DeferredPTGs that were removed.
   */
  public int removeElements(
    int first, int last, DeferredPTG element, int count)
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
        for (int i = first; i <= last && count > 0; i++) {
          if (baseArray[i] == null) {
            remove(i);
            deleted++;
            count--;
            i--;
            last--; // i-th element was deleted, so retry
          }
        }
      }
      else {
        for (int i = first; i <= last && count > 0; i++) {
          if (element.equals(baseArray[i])) {
            remove(i);
            deleted++;
            count--;
            i--;
            last--; // i-th element was deleted, so retry
          }
        }
      }
      return deleted;
    } else {
      throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
  }

  /**
   * Remove all elements that match a particular DeferredPTG.
   * @param     element The DeferredPTG to match.
   * @return    The number of DeferredPTGs that were removed.
   */
  public int removeElements(DeferredPTG element)
  {
    return removeElements(0, countPresent - 1, element, countPresent);
  }

  /**
   * Remove at most a given number of elements that match a particular
   * DeferredPTG.
   * @param     element The DeferredPTG to match.
   * @param     count   The maximum number of elements to remove.
   * @return    The number of DeferredPTGs that were removed.
   */
  public int removeElements(DeferredPTG element, int count)
  {
    return removeElements(0, countPresent - 1, element, count);
  }

  /**
   * Remove all elements within a range of indices that match a particular
   * DeferredPTG.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     element The DeferredPTG to match.
   * @return    The number of DeferredPTGs that were removed.
   */
  public int removeElements(
    int first, int last, DeferredPTG element)
  {
    return removeElements(first, last, element, countPresent);
  }

  /**
   * Replace all elements within a range of indices that match a particular
   * DeferredPTG with a new value.
   * @param     first           The index of the first element to consider.
   * @param     last            The index of the last element to consider.
   * @param     oldValue        The DeferredPTG to match.
   * @param     newValue        The new value.
   * @return    The number of DeferredPTGs that were replaced.
   */
  public int replace(
    int first, int last, DeferredPTG oldValue,
    DeferredPTG newValue)
  {
    if (first >= 0 && first < countPresent &&
        last >= 0 && last < countPresent) {
      int replaced = 0;
      if (oldValue == null) {
        for (int i = first; i <= last; i++) {
          if (baseArray[i] == null) {
            baseArray[i] = newValue;
            replaced++;
          }
        }
      } else {
        for (int i = first; i <= last; i++) {
          if (oldValue.equals(baseArray[i])) {
            baseArray[i] = newValue;
            replaced++;
          }
        }
      }
      return replaced;
    } else {
      throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
  }

  /**
   * Replace all elements that match a particular DeferredPTG
   * with a new value.
   * @param     oldValue        The DeferredPTG to match.
   * @param     newValue        The new value.
   * @return    The number of DeferredPTGs that were replaced.
   */
  public int replace(
    DeferredPTG oldValue, DeferredPTG newValue)
  {
    return replace(0, countPresent - 1, oldValue, newValue);
  }

  /**
   * Count all elements within a range of indices that match a particular
   * DeferredPTG.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     value   The DeferredPTG to match.
   * @return    The number of DeferredPTGs matched.
   */
  public int count(int first, int last, DeferredPTG value)
  {
    if (first >= 0 && first < countPresent &&
        last >= 0 && last < countPresent) {
      int found = 0;
      if (value == null) {
        for (int i = first; i <= last; i++) {
          if (baseArray[i] == null) {
            found++;
          }
        }
      } else {
        for (int i = first; i <= last; i++) {
          if (value.equals(baseArray[i])) {
            found++;
          }
        }
      }
      return found;
    } else {
      throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
  }

  /**
   * Count all elements that match a particular DeferredPTG.
   * @param     value   The DeferredPTG to match.
   * @return    The number of DeferredPTGs matched.
   */
  public int count(DeferredPTG value)
  {
    return count(0, countPresent - 1, value);
  }

  /**
   * Return the index of the first object within a range of indices that
   * matches a particular DeferredPTG.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     value   The DeferredPTG to match.
   * @return    The requested index or -1 if the DeferredPTG
   * is not found.
   * @exception ArrayIndexOutOfBoundsException  Thrown if
   *                    any of the specified indices is less than 0 or
   *                    greater or equal to the number of elements in the
   *                    collection.
   */
  public int indexOf(int first, int last, DeferredPTG value)
  {
    if (first >= 0 && first < countPresent &&
        last >= 0 && last < countPresent) {
      if (value == null) {
        for (int i = first; i <= last; i++) {
          if (baseArray[i] == null) {
            return i;
          }
        }
      } else {
        for (int i = first; i <= last; i++) {
          if (value.equals(baseArray[i])) {
            return i;
          }
        }
      }
      return -1;
    } else {
      throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
  }

  /**
   * Return the index of the first object that matches a particular
   * DeferredPTG.
   * @param     value   The DeferredPTG to match.
   * @return    The requested index or -1 if the DeferredPTG
   * is not found.
   */
  public int indexOf(DeferredPTG value)
  {
    int result;
    try {
      result = indexOf(0, countPresent - 1, value);
    } catch (ArrayIndexOutOfBoundsException e) {
      result = -1;
    }
    return result;
  }

  /**
   * Check if the collection contains a particular DeferredPTG.
   * @param     value   The DeferredPTG to match.
   * @return    True if the DeferredPTG is contained in the
   * collection, false otherwise.
   */
  public boolean contains(DeferredPTG value)
  {
    return (indexOf(value) != -1);
  }
}
