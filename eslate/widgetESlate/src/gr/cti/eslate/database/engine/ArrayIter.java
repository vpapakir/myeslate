package gr.cti.eslate.database.engine;


import java.util.ArrayList;

import com.objectspace.jgl.*;

import gr.cti.typeArray.IntBaseArray;

import java.io.Serializable;

/**
 * A ArrayIterator is a random access iterator that allows you to iterate through
 * the contents of a Array.
 * <p>
 * @see com.objectspace.jgl.RandomAccessIterator
 * @version 2.0.2
 * @author ObjectSpace, Inc.
 */

//RandomAccessIterator,
@SuppressWarnings("serial")
public final class ArrayIter implements Serializable {
    IntBaseArray indexArray;
    int myIndex;
    @SuppressWarnings("rawtypes")
	ArrayList fieldData;

    /**
     * Construct myself to be an iterator with no associated data structure or position.
     */
    public ArrayIter() {
    }

    /**
     * Construct myself to be a copy of an existing iterator.
     * @param iterator The iterator to copy.
     */
    public ArrayIter( ArrayIter iterator ) {
        indexArray = iterator.indexArray;
        myIndex = iterator.myIndex;
        fieldData = iterator.fieldData;
    }

    /**
     * Construct myself to be positioned at a particular index of a specific Array.
     * @param vector My associated Array.
     * @param index My associated index.
     */
    public ArrayIter( IntBaseArray vector, int index, ArrayList fldData ) {
        indexArray = vector;
        myIndex = index;
        fieldData = fldData;
   }

  /**
   * Return a clone of myself.
   */
  public Object clone()
    {
    return new ArrayIter( this );
    }

  /**
   * Return true if a specified object is the same kind of iterator as me
   * and is positioned at the same element.
   * @param object Any object.
   */
  public boolean equals( Object object )
    {
    return object instanceof ArrayIterator && equals( (ArrayIterator)object );
    }

  /**
   * Return true if iterator is positioned at the same element as me.
   * @param iterator The iterator to compare myself against.
   */
  public boolean equals( ArrayIter iterator )
    {
    return iterator.myIndex == myIndex && iterator.indexArray == indexArray;
    }

  public boolean equals( ArrayIterator iterator )
    {
    return iterator.index() == myIndex && iterator.getContainer() == indexArray;
    }

  /**
   * Return true if I'm before a specified iterator.
   * @param iterator The iterator to compare myself against.
   */
/*  public boolean less( RandomAccessIterator iterator )
    {
    return myIndex < ((ArrayIterator) iterator).myIndex;
    }
*/
  /**
   * Return the object that is a specified distance from my current position.
   * @param offset The offset from my current position.
   * @exception ArrayIndexOutOfBoundsException If the adjusted index is invalid.
   */
  public Object get( int offset )
    {
    return fieldData.get(indexArray.get( myIndex + offset )); //(((Integer) indexArray.at( myIndex + offset )).intValue());
    }

  /**
   * Write an object at a specified distance from my current position.
   * @param offset The offset from my current position.
   * @param object The object to write.
   * @exception ArrayIndexOutOfBoundsException If the adjusted index is invalid.
   */
/*  public void put( int offset, Object object )
    {
    indexArray.put( myIndex + offset, object );
    }
*/
    /**
     * Return true if I'm positioned at the first item of my input stream.
     */
    public boolean atBegin() {
        return myIndex == 0;
    }

    /**
     * Return true if I'm positioned after the last item in my input stream.
     */
    public boolean atEnd() {
        return myIndex == indexArray.size();
    }

    /**
     * Return true if there are more elements in my input stream.
     */
    public boolean hasMoreElements() {
        return myIndex < indexArray.size();
    }

    /**
     * Advance by one.
     */
    public void advance() {
        ++myIndex;
    }

    /**
     * Advance by a specified amount.
     * @param n The amount to advance.
     */
    public void advance( int n ) {
        myIndex += n;
    }

    /**
     * Retreat by one.
     */
    public void retreat() {
        --myIndex;
    }

    /**
     * Retreat by a specified amount.
     * @param n The amount to retreat.
     */
    public void retreat( int n ) {
        myIndex -= n;
    }

    /**
     * Return the next element in my input stream.
     * @exception ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
     */
    public Object nextElement() {
        return fieldData.get(indexArray.get( myIndex++ )); //((Integer) indexArray.at( myIndex++ )).intValue());
    }

    /**
     * Return the object at my current position.
     * @exception ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
     */
    public Object get() {
        return fieldData.get(indexArray.get( myIndex )); //((Integer) indexArray.at( myIndex )).intValue());
    }

  /**
   * Set the object at my current position to a specified value.
   * @param object The object to be written at my current position.
   * @exception ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
   */
/*  public void put( Object object )
    {
    indexArray.put( myIndex, object );
    }
*/
  /**
   * Return the distance from myself to another iterator.
   * I should be before the specified iterator.
   * @param iterator The iterator to compare myself against.
   */
/*  public int distance( ForwardIterator iterator )
    {
    return ((ArrayIterator) iterator).myIndex - myIndex;
    }
*/
    /**
     * Return my current index.
     */
    public int index() {
        return myIndex;
    }

    /**
     * Return my associated array.
     */
/*    public Container getContainer() {
        return indexArray;
    }
*/
}
