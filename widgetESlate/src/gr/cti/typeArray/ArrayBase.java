package gr.cti.typeArray;

import java.lang.reflect.*;
import java.io.*;

/**
 * 
 * The base class for type-specific collections.
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public abstract class ArrayBase implements Cloneable, Serializable
{

    protected int countPresent;
    protected int countLimit;
    protected int maximumGrowth;
    static final long serialVersionUID = -7984746972315131545L;

    public ArrayBase(int size, int growth, Class type) {
        Object array = Array.newInstance(type, size);
        countLimit = size;
        maximumGrowth = growth;
        setArray(array);
    }

    public ArrayBase(int size, Class type) {
        this(size, Integer.MAX_VALUE, type);
    }

    public ArrayBase(Class type) {
        this(10, Integer.MAX_VALUE, type);
    }
    protected abstract Object getArray();

    protected abstract void setArray(Object array);

    protected abstract void discardValues(int from, int to);

    /**
     * Implementation method to increase the underlying array size.
     */
    protected void growArray(int required) {
        Object base = getArray();
        int size = Math.max(required,
            countLimit + Math.min(countLimit, maximumGrowth));
        Class type = base.getClass().getComponentType();
        Object grown = Array.newInstance(type, size);
        System.arraycopy(base, 0, grown, 0, countLimit);
        countLimit = size;
        setArray(grown);
    }

    /**
     * Get next add position for appending, increasing size if needed.
     */
    protected int getAddIndex() {
        int index = countPresent++;
        if (countPresent > countLimit) {
            growArray(countPresent);
        }
        return index;
    }

    /**
     * Make room to insert a value at a specified index.
     */
    protected void makeInsertSpace(int index) {
        if (index >= 0 && index <= countPresent) {
            if (++countPresent > countLimit) {
                growArray(countPresent);
            }
            if (index < countPresent - 1) {
                Object array = getArray();
                System.arraycopy(array, index, array, index + 1,
                    countPresent - index - 1);
            }
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }

    /**
     * Remove a value from the collection.
     */
    public void remove(int index) {
      remove(index, index);
    }

    /**
     * Remove from the collection a set of values within a range of indices.
     */
    public void remove(int firstIndex, int secondIndex)
    {
      if (firstIndex >= 0 && firstIndex < countPresent &&
          secondIndex >= 0 && secondIndex < countPresent) {
        if (firstIndex > secondIndex) {
          int tmp = firstIndex;
          firstIndex = secondIndex;
          secondIndex = tmp;
        }
        int nDeleted = secondIndex - firstIndex + 1;
        int origCount = countPresent;
        countPresent -= nDeleted;
        if (firstIndex < countPresent) {
          Object base = getArray();
          System.arraycopy(
            base,  secondIndex + 1,
            base,  firstIndex,
            origCount - 1 - secondIndex
          );
        }
        discardValues(countPresent, countPresent+nDeleted);
      } else {
          throw new ArrayIndexOutOfBoundsException("Invalid index value");
      }
    }

    /**
     * Make sure we have at least a specified capacity.
     */
    public void ensureCapacity(int min) {
        if (min > countLimit) {
            growArray(min);
        }
    }

    /**
     * Set the collection empty.
     */
    public void clear() {
        setSize(0);
        discardValues(0, countLimit);
    }

    /**
     * Get number of values in collection.
     */
    public int size() {
        return countPresent;
    }

    /**
     * Set the size of the collection.
     */
    public void setSize(int count) {
        if (count > countLimit) {
            growArray(count);
        } else if (count < countPresent) {
            discardValues(count, countPresent);
        }
        countPresent = count;
    }

    /**
     * Convert to an array.
     */
    protected Object buildArray()
    {
        Class type = getArray().getClass().getComponentType();
        Object copy = Array.newInstance(type, countPresent);
        System.arraycopy(getArray(), 0, copy, 0, countPresent);
        return copy;
    }

    /**
     * Create a copy of the collection. The copy will contain a new set of
     * references to the same objects as the original collection. Thus,
     * referencing an object in the copy is the same as referencing the
     * corresponding object in the original collection. On the other hand,
     * adding or subtracting elements from the collection will leave the
     * original collection unmodified.
     */
    synchronized public Object clone()
    {
      ArrayBase ab = null;
      try {
        // Clone the object. At this point, the array containing the
        // collection is the same as that in the original.
        ab = (ArrayBase)(super.clone());

        // Figure out the type and dimension of the array of the original
        // collection.
        Object array = getArray();
        Class type = array.getClass().getComponentType();
        int length = Array.getLength(array);

        // Create a new array having the same type and dimension as the array
        // of the original collection and copy the data from the original
        // array to the new array.
        Object newArray = Array.newInstance(type, length);
        System.arraycopy(array, 0, newArray, 0, length);

        // Assign this new array to the cloned copy.
        ab.setArray(newArray);
      } catch (CloneNotSupportedException cnse) {
        // This shouldn't happen, since we are Cloneable.
        throw new InternalError();
      }
      return ab;
    }

    /**
     * If currently allocated storage space is larger than the space required
     * for the total number of elements, then reallocate the elenments of the
     * collection into a storage space that is exactly the right size.
     */
    public void trimToSize()
    {
      if (countPresent < countLimit) {
        Object array = getArray();
        Class type = array.getClass().getComponentType();
        Object newArray = null;
        newArray = Array.newInstance(type, countPresent);
        System.arraycopy(array, 0, newArray, 0, countPresent);
        setArray(newArray);
        countLimit = countPresent;
      }
    }
}
