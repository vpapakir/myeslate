/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 18 Οκτ 2002
 * Time: 1:31:53 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

import gr.cti.typeArray.IntBaseArray;

public class DateFieldDataIterator extends FieldDataIterator {
    /** The field on which the iteration occurs */
    DateTableField field = null;

    /**
     * Construct myself to be a copy of an existing iterator.
     * @param iterator The iterator to copy.
     */
    public DateFieldDataIterator(DateFieldDataIterator iterator) {
        recIndexArray = iterator.recIndexArray;
        recIndex = iterator.recIndex;
        field = iterator.field;
    }

    /**
     * Construct myself to be positioned at a particular index of a specific Array.
     * @param recIndexArray The list of field records which will be iterated.
     * @param index The index at which the iterator is positioned.
     */
    public DateFieldDataIterator(IntBaseArrayDesc recIndexArray, int index, DateTableField field) {
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
        return new DateFieldDataIterator(this);
    }

    /**
     * Return true if iterator is positioned at the same element as me.
     * @param iterator The iterator to compare myself against.
     */
    public boolean equals(FieldDataIterator iterator) {
        return iterator.recIndex == recIndex && iterator.recIndexArray == recIndexArray && iterator.getField() == field;
    }

    /**
     * Return the CDate at my current position.
     * @exception ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
     */
    public CDate get() {
        return field.get(recIndexArray.get(recIndex));
    }

    /**
     * Return the next element in my input stream.
     * @exception ArrayIndexOutOfBoundsException If I'm positioned at an invalid index.
     */
    public CDate nextElement() {
        return field.get(recIndexArray.get(recIndex++));
    }

    /**
     * Return the int that is a specified distance from my current position.
     * @param offset The offset from my current position.
     * @exception ArrayIndexOutOfBoundsException If the adjusted index is invalid.
     */
    public CDate get(int offset) {
        return field.get(recIndexArray.get(recIndex + offset));
    }
}
