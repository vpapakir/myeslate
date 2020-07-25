package gr.cti.eslate.database.engine;

import java.util.ArrayList;
import java.util.ListIterator;
import com.objectspace.jgl.BinaryPredicate;

public class MyArrayListSorting {
    static final int stlThreshold = 16;
    ArrayList base;
    ArrayList rowIndex;
    BinaryPredicate comparator;

    public MyArrayListSorting() {
    }

    /**
     * Sort the elements in a ArrayList container according to their hash code. The
     * object with the smallest hash code is placed first. The time complexity is O(NlogN)
     * and the space complexity is constant.
     * @param container A Sequence container.
     */
    public static void sort(ArrayList container, ArrayList rowSN) {
        sort(container, container.listIterator(), container.listIterator(container.size()-1), new com.objectspace.jgl.HashComparator(), rowSN );
    }

    public static void sort(ArrayList container, BinaryPredicate comparator, ArrayList rowSN) {
        sort(container, container.listIterator(), container.listIterator(container.size()-1), comparator, rowSN);
    }

    public static void sort(ArrayList container, ListIterator first, ListIterator last, BinaryPredicate comparator, ArrayList rowSN) {
        new MyArrayListSorting(container, first, last, comparator, rowSN);
    }

    private MyArrayListSorting(ArrayList container, ListIterator first, ListIterator last, BinaryPredicate comparit, ArrayList rowSN) {
        base = container;
        rowIndex = rowSN;
        this.comparator = comparit;

        // calculate first and last index into the sequence.
        int start = distance(base.listIterator(), first);
        int finish = distance(base.listIterator(), last)+1;

        quickSortLoop( start, finish );
//System.out.println("here 2.1");
        finalInsertionSort( start, finish );
//System.out.println("here 2.2");
    }


    void finalInsertionSort( int first, int last )
      {
      if ( last - first > stlThreshold ) {
        int limit = first + stlThreshold;

        for ( int i = first + 1; i < limit; i++ )
          linearInsert( first, i );

        if (rowIndex != null) {
            for ( int i = limit; i < last; i++ )
                unguardedLinearInsert( i, base.get( i ), rowIndex.get( i ) );
        }else{
            for ( int i = limit; i < last; i++ )
                unguardedLinearInsert( i, base.get( i ));
        }
      }else{
        for ( int i = first + 1; i < last; i++ )
          linearInsert( first, i );
        }
      }

    void unguardedLinearInsert( int last, Object value, Object value2 )
      {
      int next = last - 1;

      while ( comparator.execute( value, base.get( next ) ) ) {
        rowIndex.set( last, rowIndex.get( next ) );
        base.set( last--, base.get( next-- ) );
      }

      base.set( last, value );
      rowIndex.set( last, value2 );
      }

    void unguardedLinearInsert(int last, Object value) {
      int next = last - 1;

      while ( comparator.execute( value, base.get( next ) ) ) {
        base.set( last--, base.get( next-- ) );
      }

      base.set( last, value );
    }

    void linearInsert( int first, int last )
      {
      Object value = base.get( last );
      Object value2 = null;
      if (rowIndex != null)
          value2 = rowIndex.get( last );

      if ( comparator.execute( value, base.get( first ) ) )
        {
        for ( int i = last; i > first; i-- ) {
          base.set( i, base.get( i - 1 ) );
          if (rowIndex != null)
              rowIndex.set( i, rowIndex.get( i - 1 ) );
        }

        base.set( first, value );
        if (rowIndex != null)
            rowIndex.set( first, value2);
        }
      else
        {
        unguardedLinearInsert( last, value, value2 );
        }
      }

    void quickSortLoop( int first, int last )
      {
  //System.out.println("here 2.4");

      while ( last - first > stlThreshold )
        {
        Object pivot;
        Object a = base.get( first );
        Object b = base.get( first + (last - first ) / 2 );
        Object c = base.get( last - 1 );

        if ( comparator.execute( a, b ) )
          {
          if ( comparator.execute( b, c ) )
            pivot = b;
          else if ( comparator.execute( a, c ) )
            pivot = c;
          else
            pivot = a;
          }
        else if ( comparator.execute( a, c ) )
          pivot = a;
        else if ( comparator.execute( b, c ) )
          pivot = c;
        else
          pivot = b;

        int cut = first;
        int lastx = last;

  //System.out.println("here 2.5");

        while ( true )
          {
          while ( comparator.execute( base.get( cut ), pivot ) )
            ++cut;

          --lastx;

          while ( comparator.execute( pivot, base.get( lastx ) ) )
            --lastx;

          if ( cut >= lastx )
            break;

          Object tmp = base.get( cut );
          Object tmp2 = null;
          if (rowIndex != null) {
              tmp2 = rowIndex.get(cut);
              rowIndex.set(cut, rowIndex.get( lastx ) );
          }
          base.set( cut++, base.get( lastx ) );
          if (rowIndex != null)
              rowIndex.set( lastx, tmp2 );
          base.set( lastx, tmp );

          }

        if ( cut - first >= last - cut )
          {
          quickSortLoop( cut, last );
          last = cut;
          }
        else
          {
          quickSortLoop( first, cut );
          first = cut;
          }
        }
      }

      int distance(ListIterator first, ListIterator last) {
          int pos = first.nextIndex()-1;
          int pos2 = last.nextIndex()-1;
          return pos2-pos;
      }
}
