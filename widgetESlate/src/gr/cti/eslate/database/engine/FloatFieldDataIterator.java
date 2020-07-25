/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 14, 2002
 * Time: 1:55:51 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database.engine;

public class FloatFieldDataIterator extends FieldDataIterator {
    /** The field on which the iteration occurs */
    FloatTableField field = null;

    /**
     * Construct myself to be a copy of an existing iterator.
     * @param iterator The iterator to copy.
     */
    public FloatFieldDataIterator(FloatFieldDataIterator iterator) {
        recIndexArray = iterator.recIndexArray;
        recIndex = iterator.recIndex;
        field = iterator.field;
    }

    /**
     * Construct myself to be positioned at a particular index of a specific Array.
     * @param recIndexArray The list of field records which will be iterated.
     * @param index The index at which the iterator is positioned.
     */
    public FloatFieldDataIterator(IntBaseArrayDesc recIndexArray, int index, FloatTableField field) {
        this.recIndexArray = recIndexArray;
        recIndex = index;
        this.field = field;
   }

    public AbstractTableField getField() {
        return field;
    }

    /**
     * Return a clone of myself.
     */
    public Object clone() {
        return new FloatFieldDataIterator(this);
    }

    /**
     * Return true if iterator is positioned at the same element as me.
     * @param iterator The iterator to compare myself against.
     */
    public boolean equals(FieldDataIterator iterator ) {
        return iterator.recIndex == recIndex && iterator.recIndexArray == recIndexArray && iterator.getField() == field;
    }

    /**
     * Return the float at my current position.
     * @exception java.lang.ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
     */
    public float get() {
        return field.get(recIndexArray.get(recIndex));
    }

    /**
     * Return the next element in my input stream.
     * @exception java.lang.ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
     */
    public float nextElement() {
        return field.get(recIndexArray.get(recIndex++));
    }

    /**
     * Return the float that is a specified distance from my current position.
     * @param offset The offset from my current position.
     * @exception java.lang.ArrayIndexOutOfBoundsException If the adjusted index is invalid.
     */
    public float get( int offset ) {
        return field.get(recIndexArray.get(recIndex + offset));
    }
}
