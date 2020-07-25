/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 17 Οκτ 2002
 * Time: 5:46:23 μμ
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

import java.util.ArrayList;

public abstract class FieldDataIterator {
    /** The array of the indices of the field's data on which the iteration takes place.
     */
    IntBaseArrayDesc recIndexArray = null;
    /** The current recorde index */
    int recIndex = 0;

    /**
     * Return a clone of myself.
     */
    public abstract Object clone();// {
//        return new FieldDataIterator(this);
//    }

    /**
     * Return true if a specified object is the same kind of iterator as me
     * and is positioned at the same element.
     * @param object Any object.
     */
    public boolean equals( Object object ) {
        return object instanceof FieldDataIterator && equals((FieldDataIterator)object);
    }

    /**
     * Return true if I'm positioned at the first item of my input stream.
     */
    public boolean atBegin() {
        return recIndex == 0;
    }

    /**
     * Return true if I'm positioned after the last item in my input stream.
     */
    public boolean atEnd() {
        return recIndex == recIndexArray.size();
    }

    /**
     * Return true if there are more elements in my input stream.
     */
    public boolean hasMoreElements() {
        return recIndex < recIndexArray.size();
    }

    /**
     * Advance by one.
     */
    public void advance() {
        ++recIndex;
    }

    /**
     * Advance by a specified amount.
     * @param n The amount to advance.
     */
    public void advance( int n ) {
        recIndex += n;
    }

    /**
     * Retreat by one.
     */
    public void retreat() {
        --recIndex;
    }

    /**
     * Retreat by a specified amount.
     * @param n The amount to retreat.
     */
    public void retreat( int n ) {
        recIndex -= n;
    }

    /**
     * Return my current index.
     */
    public int getIndex() {
        return recIndex;
    }

    /** Returns the distance between two field iterators */
    public int distance(FieldDataIterator first, FieldDataIterator last) {
        return last.getIndex()-first.getIndex();
    }

    public abstract AbstractTableField getField();
}
