package gr.cti.eslate.base.container;

import gr.cti.typeArray.*;

/**
 * 
 * A collection of StringBaseArrays, similar to Vector.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class SBABaseArray extends ArrayBase
{
  static final long serialVersionUID = 1257602082070977263L;

  protected StringBaseArray[] baseArray;

  public SBABaseArray(int size, int growth)
  {
    super(size, growth, StringBaseArray.class);
  }

  public SBABaseArray(int size)
  {
    super(size, StringBaseArray.class);
  }

  public SBABaseArray()
  {
    super(StringBaseArray.class);
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
    baseArray = (StringBaseArray [])array;
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
  public int add(StringBaseArray value)
  {
    int index = getAddIndex();
    baseArray[index] = value;
    return index;
  }

  /**
   * Insert a value into the collection.
   */
  public void add(int index, StringBaseArray value)
  {
    makeInsertSpace(index);
    baseArray[index] = value;
  }

  /**
   * Get value from the collection.
   */
  public StringBaseArray get(int index)
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
  public void set(int index, StringBaseArray value)
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
  public StringBaseArray[] toArray()
  {
    return (StringBaseArray []) buildArray();
  }

  /**
   * Return the first element in the collection.
   */
  public StringBaseArray front()
  {
    return get(0);
  }

  /**
   * Return the last StringBaseArray in the collection.
   * @return    The requested Object.
   */
  public StringBaseArray back()
  {
    return get(countPresent - 1);
  }

  /**
   * Remove and return the first StringBaseArray in the collection.
   * @return    The removed Object.
   */
  public StringBaseArray popFront()
  {
    StringBaseArray first = get(0);
    remove(0);
    return first;
  }

  /**
   * Insert a StringBaseArray in front of the first element in the
   * collection.
   * @param     value   The StringBaseArray to insert.
   */
  public void pushFront(StringBaseArray value)
  {
    add(0, value);
  }

  /**
   * Remove at most a given number of elements within a range of indices
   * that match a particular StringBaseArray.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     element The StringBaseArray to match.
   * @param     count   The maximum number of elements to remove.
   * @return    The number of StringBaseArrays that were removed.
   */
  public int removeElements(
    int first, int last, StringBaseArray element, int count)
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
   * Remove all elements that match a particular StringBaseArray.
   * @param     element The StringBaseArray to match.
   * @return    The number of StringBaseArrays that were removed.
   */
  public int removeElements(StringBaseArray element)
  {
    return removeElements(0, countPresent - 1, element, countPresent);
  }

  /**
   * Remove at most a given number of elements that match a particular
   * StringBaseArray.
   * @param     element The StringBaseArray to match.
   * @param     count   The maximum number of elements to remove.
   * @return    The number of StringBaseArrays that were removed.
   */
  public int removeElements(StringBaseArray element, int count)
  {
    return removeElements(0, countPresent - 1, element, count);
  }

  /**
   * Remove all elements within a range of indices that match a particular
   * StringBaseArray.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     element The StringBaseArray to match.
   * @return    The number of StringBaseArrays that were removed.
   */
  public int removeElements(
    int first, int last, StringBaseArray element)
  {
    return removeElements(first, last, element, countPresent);
  }

  /**
   * Replace all elements within a range of indices that match a particular
   * StringBaseArray with a new value.
   * @param     first           The index of the first element to consider.
   * @param     last            The index of the last element to consider.
   * @param     oldValue        The StringBaseArray to match.
   * @param     newValue        The new value.
   * @return    The number of StringBaseArrays that were replaced.
   */
  public int replace(
    int first, int last, StringBaseArray oldValue,
    StringBaseArray newValue)
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
   * Replace all elements that match a particular StringBaseArray
   * with a new value.
   * @param     oldValue        The StringBaseArray to match.
   * @param     newValue        The new value.
   * @return    The number of StringBaseArrays that were replaced.
   */
  public int replace(
    StringBaseArray oldValue, StringBaseArray newValue)
  {
    return replace(0, countPresent - 1, oldValue, newValue);
  }

  /**
   * Count all elements within a range of indices that match a particular
   * StringBaseArray.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     value   The StringBaseArray to match.
   * @return    The number of StringBaseArrays matched.
   */
  public int count(int first, int last, StringBaseArray value)
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
   * Count all elements that match a particular StringBaseArray.
   * @param     value   The StringBaseArray to match.
   * @return    The number of StringBaseArrays matched.
   */
  public int count(StringBaseArray value)
  {
    return count(0, countPresent - 1, value);
  }

  /**
   * Return the index of the first object within a range of indices that
   * matches a particular StringBaseArray.
   * @param     first   The index of the first element to consider.
   * @param     last    The index of the last element to consider.
   * @param     value   The StringBaseArray to match.
   * @return    The requested index or -1 if the StringBaseArray
   * is not found.
   * @exception ArrayIndexOutOfBoundsException  Thrown if
   *                    any of the specified indices is less than 0 or
   *                    greater or equal to the number of elements in the
   *                    collection.
   */
  public int indexOf(int first, int last, StringBaseArray value)
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
   * StringBaseArray.
   * @param     value   The StringBaseArray to match.
   * @return    The requested index or -1 if the StringBaseArray
   * is not found.
   */
  public int indexOf(StringBaseArray value)
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
   * Check if the collection contains a particular StringBaseArray.
   * @param     value   The StringBaseArray to match.
   * @return    True if the StringBaseArray is contained in the
   * collection, false otherwise.
   */
  public boolean contains(StringBaseArray value)
  {
    return (indexOf(value) != -1);
  }
}
