package gr.cti.eslate.database.engine;

import gr.cti.typeArray.IntBaseArray;

public class IntBaseArrayDesc extends IntBaseArray {
    transient IntegerComparator comparator = null;
//    transient BinaryIntPredicate comparator = null;
    static final int stlThreshold = 16;
    static final long serialVersionUID = 12;

    public IntBaseArrayDesc() {
        super();
    }

    public IntBaseArrayDesc(int size) {
        super(size);
    }

    public IntBaseArrayDesc(int size, int growth) {
        super(size, growth);
    }

    /**
     * Create a shallow copy of an existing Array.
     */
    public void copy(IntBaseArrayDesc array) {
        if ( this == array )
            return;

        int arrayLength = array.size();
        int myLength = size();
//        System.out.println("arrayLength: " + arrayLength);
//        System.out.println("baseArray.length: " + baseArray.length);
        setSize(arrayLength);
        System.arraycopy( array.baseArray, 0, baseArray, 0, arrayLength );

//        System.out.println("COPY END this: " + this + ", size(): " + size());
    }

    /**
     * Swap my contents with another Array.
     * @param array The Array that I will swap my contents with.
     */
    public synchronized void swap( IntBaseArrayDesc array ) {
//        System.out.println("swap BEFORE this: " + this);
//        System.out.println("swap BEFORE array: " + array);
        int countPresentTmp = countPresent;
        int countLimitTmp = countLimit;
        int[] baseArrayTmp = baseArray;

        countPresent = array.countPresent;
        countLimit = array.countLimit;
        baseArray = array.baseArray;

        array.countPresent = countPresentTmp;
        array.countLimit = countLimitTmp;
        array.baseArray = baseArrayTmp;
//        System.out.println("swap AFTER this: " + this);
//        System.out.println("swap AFTER array: " + array);
    }

    public void sort( boolean ascending ) {
        if (ascending)
            comparator = new LessInt();
        else
            comparator = new GreaterInt();

        IntegerTableField.sort(this, comparator);

        // calculate first and last index into the sequence.
/*        int start = 0; //(base.start()).distance( first );
        int finish = size(); //(base.start()).distance( last );
//        System.out.println("BaseArraySorting  start: " + start + ", finish: " + finish);
//        System.out.println("Sequence: " + base);

        quickSortLoop( start, finish );
        finalInsertionSort( start, finish );
//        System.out.println("2. Sequence: " + base);
*/
    }

/*    void finalInsertionSort( int first, int last ) {
        if ( last - first > stlThreshold ) {
            int limit = first + stlThreshold;

            for ( int i = first + 1; i < limit; i++ )
              linearInsert( first, i );

            for ( int i = limit; i < last; i++ )
              unguardedLinearInsert( i, get( i ) );
        }else{
            for ( int i = first + 1; i < last; i++ )
              linearInsert( first, i );
        }
    }

    void unguardedLinearInsert( int last, int value ) {
        int next = last - 1;

        while ( comparator.execute( value, get( next ) ) )
          set( last--, get( next-- ) );

        set( last, value );
    }

    void linearInsert( int first, int last ) {
        int value = get( last );

        if ( comparator.execute( value, get( first ) ) ) {
            for ( int i = last; i > first; i-- )
                set( i, get( i - 1 ) );

            set( first, value );
        }else{
            unguardedLinearInsert( last, value );
        }
    }

    void quickSortLoop( int first, int last ) {
        while ( last - first > stlThreshold ) {
            int pivot;
            int a = get( first );
            int b = get( first + (last - first ) / 2 );
            int c = get( last - 1 );

            if ( comparator.execute( a, b ) ) {
                if ( comparator.execute( b, c ) )
                    pivot = b;
                else if ( comparator.execute( a, c ) )
                    pivot = c;
                else
                    pivot = a;
            }else if ( comparator.execute( a, c ) )
                pivot = a;
            else if ( comparator.execute( b, c ) )
                pivot = c;
            else
                pivot = b;

            int cut = first;
            int lastx = last;

            while ( true ) {
                while ( comparator.execute( get( cut ), pivot ) )
                    ++cut;

                --lastx;

                while ( comparator.execute( pivot, get( lastx ) ) )
                    --lastx;

                if ( cut >= lastx )
                    break;

                int tmp = get( cut );
                set( cut++, get( lastx ) );
                set( lastx, tmp );
            }

            if ( cut - first >= last - cut ) {
                quickSortLoop( cut, last );
                last = cut;
            }else{
                quickSortLoop( first, cut );
                first = cut;
            }
        }
    }
*/
    public String toString() {
        StringBuffer strBuff = new StringBuffer();
        for (int i=0; i<size(); i++) {
            strBuff.append(get(i));
            if (i != size()-1)
                strBuff.append(',');
        }
        return strBuff.toString();
    }

}

/*abstract class BinaryIntPredicate {
    public abstract boolean execute(int first, int second);
}

class GreaterInt extends BinaryIntPredicate {
    public boolean execute(int first, int second) {
        if (first > second) return true;
        return false;
    }
}

class LessInt extends BinaryIntPredicate {
    public boolean execute(int first, int second) {
        if (first < second) return true;
        return false;
    }
}
*/
