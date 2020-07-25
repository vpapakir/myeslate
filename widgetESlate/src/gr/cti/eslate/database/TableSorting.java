package gr.cti.eslate.database;

import com.objectspace.jgl.*;

public final class TableSorting
  {
  static final int stlThreshold = 16;
  Sequence base;
  Sequence rowIndex;
  BinaryPredicate comparator;

  private TableSorting()
    {
    }

  /**
   * Sort the elements in a sequence according to their hash code. The object with the
   * smallest hash code is placed first. The time complexity is O(NlogN) and the space
   * complexity is constant.
   * @param first An iterator positioned at the first element of the sequence.
   * @param last An iterator positioned immediately after the last element of the sequence.
   * @exception java.lang.IllegalArgumentException is thrown if the iterators do not have
   * Sequence containers -or- if there containers are different.
   */
  public static void sort( ForwardIterator first, ForwardIterator last , Sequence rowSN)
    {
    sort( first, last, new HashComparator(), rowSN );
    }

  /**
   * Sort the elements in a sequence using a comparator. The time complexity is O(NlogN)
   * and the space complexity is constant.
   * @param first An iterator positioned at the first element of the sequence.
   * @param last An iterator positioned immediately after the last element of the sequence.
   * @param comparator A binary function that returns true if its first operand should be positioned before its second operand.
   * @exception java.lang.IllegalArgumentException is thrown if the iterators do not have
   * Sequence containers.
   */
  public static void sort( ForwardIterator first, ForwardIterator last, BinaryPredicate comparator, Sequence rowSN )
    {
    new TableSorting( first, last, comparator, rowSN );
    }

  /**
   * Sort the elements in a Sequence container according to their hash code. The
   * object with the smallest hash code is placed first. The time complexity is O(NlogN)
   * and the space complexity is constant.
   * @param container A Sequence container.
   */
  public static void sort( Sequence container, Sequence rowSN )
    {
    sort( container.start(), container.finish(), new HashComparator(), rowSN );
    }

  /**
   * Sort the elements in a random access container using a comparator. The time
   * complexity is O(NlogN) and the space complexity is constant.
   * @param container A random access container.
   * @param comparator A BinaryFunction that returns true if its first operand should be positioned before its second operand.
   */
  public static void sort( Sequence container, BinaryPredicate comparator, Sequence rowSN )
    {
    sort( container.start(), container.finish(), comparator, rowSN );
    }

  private TableSorting( ForwardIterator first, ForwardIterator last, BinaryPredicate comparit, Sequence rowSN )
    {
    if ( ( !(first.getContainer() instanceof Sequence) ) ||
         ( !(last.getContainer() instanceof Sequence) )   ||
         ( !(rowSN instanceof Sequence) ) )
      throw new IllegalArgumentException( "iterator containers must be a Sequence" );

    base = (Sequence) first.getContainer();
    rowIndex = rowSN;
    comparator = comparit;

    // calculate first and last index into the sequence.
    int start = (base.start()).distance( first );
    int finish = (base.start()).distance( last );

    quickSortLoop( start, finish );
    finalInsertionSort( start, finish );
    }

  void finalInsertionSort( int first, int last )
    {
    if ( last - first > stlThreshold )
      {
      int limit = first + stlThreshold;

      for ( int i = first + 1; i < limit; i++ )
        linearInsert( first, i );

      for ( int i = limit; i < last; i++ )
        unguardedLinearInsert( i, base.at( i ), rowIndex.at( i ) );
      }
    else
      {
      for ( int i = first + 1; i < last; i++ )
        linearInsert( first, i );
      }
    }

  void unguardedLinearInsert( int last, Object value, Object value2 )
    {
    int next = last - 1;

    while ( comparator.execute( value, base.at( next ) ) ) {
      rowIndex.put( last, rowIndex.at( next ) );
      base.put( last--, base.at( next-- ) );
    }

    base.put( last, value );
    rowIndex.put( last, value2 );
    }

  void linearInsert( int first, int last )
    {
    Object value = base.at( last );
    Object value2 = rowIndex.at( last );

    if ( comparator.execute( value, base.at( first ) ) )
      {
      for ( int i = last; i > first; i-- ) {
        base.put( i, base.at( i - 1 ) );
        rowIndex.put( i, rowIndex.at( i - 1 ) );
      }

      base.put( first, value );
      rowIndex.put( first, value2);
      }
    else
      {
      unguardedLinearInsert( last, value, value2 );
      }
    }

  void quickSortLoop( int first, int last )
    {
    while ( last - first > stlThreshold )
      {
      Object pivot;
      Object a = base.at( first );
      Object b = base.at( first + (last - first ) / 2 );
      Object c = base.at( last - 1 );

//        System.out.println(comparator);
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

      while ( true )
        {
        while ( comparator.execute( base.at( cut ), pivot ) )
          ++cut;

        --lastx;

        while ( comparator.execute( pivot, base.at( lastx ) ) )
          --lastx;

        if ( cut >= lastx )
          break;

        Object tmp = base.at( cut );
        Object tmp2 = rowIndex.at(cut);
        rowIndex.put(cut, rowIndex.at( lastx ) );
        base.put( cut++, base.at( lastx ) );
        rowIndex.put( lastx, tmp2 );
        base.put( lastx, tmp );

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
  }
