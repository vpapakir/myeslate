package gr.cti.eslate.database.engine;


import com.objectspace.jgl.*;

/**
 * The Finding class contains generic Finding algorithms.
 * <p>
 * @version 2.0.2
 * @author ObjectSpace, Inc.
 */

public final class MyFinding
  {
  private MyFinding()
    {
    }

    /**
     * Find the first element in a sequence that satisfies a predicate.
     * The time complexity is linear and the space complexity is constant.
     * @param first An iterator positioned at the first element of the sequence.
     * @param last An iterator positioned immediately after the last element of the sequence.
     * @param predicate A unary predicate.
     * @return An iterator positioned at the first element that matches. If no match is
     * found, return an iterator positioned immediately after the last element of
     * the sequence.
     */
    public static ArrayIter findIf( ArrayIter first, InputIterator last, UnaryPredicate predicate ) {
        ArrayIter firstx = (ArrayIter) first.clone();

        while (!firstx.equals( last ) && (firstx.get()== null ||  !predicate.execute( firstx.get() ) ))
          firstx.advance();

        return firstx;
    }

    public static ArrayIter findIf( ArrayIter first, int lastIndex, UnaryPredicate predicate ) {
        ArrayIter firstx = (ArrayIter) first.clone();

        while (!(firstx.index() == lastIndex)  && (firstx.get()== null ||  !predicate.execute( firstx.get() ) ))
          firstx.advance();

        return firstx;
    }

  /**
   * Find the first element in a container that matches a particular object using equals().
   * The time complexity is linear and the space complexity is constant.
   * @param container The container.
   * @param object The object to find.
   * @return An iterator positioned at the first element that matches. If no match is
   * found, return an iterator positioned immediately after the last element of
   * the container.
   */
//  public static InputIterator find( Container container, Object object )
//    {
//    return find( container.start(), container.finish(), object );
//    }

  }
