package gr.cti.eslate.database.engine;

import java.util.ArrayList;
import com.objectspace.jgl.BinaryPredicate;
import com.objectspace.jgl.HashComparator;
import java.util.ListIterator;

/**
 * The Sorting class contains generic sorting algorithms.
 * <p>
 */

public final class ArrayListSorting {
    static final int stlThreshold = 16;
    ArrayList base;
    BinaryPredicate comparator;

    private ArrayListSorting() {}

    /**
     * Sort the elements in a sequence according to their hash code. The object with the
     * smallest hash code is placed first. The time complexity is O(NlogN) and the space
     * complexity is constant.
     * @param first An iterator positioned at the first element of the sequence.
     * @param last An iterator positioned immediately after the last element of the sequence.
     * @exception java.lang.IllegalArgumentException is thrown if the iterators do not have
     * Sequence containers -or- if there containers are different.
     */
    public static void sort(ArrayList container, ListIterator first, ListIterator last) {
        sort(container, first, last, new HashComparator());
    }

    /**
     * Sort the elements in anb ArrayList using a comparator. The time complexity is O(NlogN)
     * and the space complexity is constant.
     * @param first An iterator positioned at the first element of the ArrayList.
     * @param last An iterator positioned immediately after the last element of the ArrayList.
     * @param comparator A binary function that returns true if its first operand should be positioned before its second operand.
     * @exception java.lang.IllegalArgumentException is thrown if the iterators do not have
     * Sequence containers.
     */
    public static void sort(ArrayList container, ListIterator first, ListIterator last, BinaryPredicate comparator ) {
        new ArrayListSorting(container, first, last, comparator);
    }

    /**
     * Sort the elements in an ArrayList container according to their hash code. The
     * object with the smallest hash code is placed first. The time complexity is O(NlogN)
     * and the space complexity is constant.
     * @param container An ArrayList container.
     */
    public static void sort(ArrayList container) {
        if (container.size() == 0) return;
        sort(container, container.listIterator(), container.listIterator(container.size()-1), new HashComparator());
    }

    /**
     * Sort the elements in a random access container using a comparator. The time
     * complexity is O(NlogN) and the space complexity is constant.
     * @param container A random access container.
     * @param comparator A BinaryFunction that returns true if its first operand should be positioned before its second operand.
     */
    public static void sort(ArrayList container, BinaryPredicate comparator) {
        if (container.size() == 0) return;
        sort(container, container.listIterator(), container.listIterator(container.size()-1), comparator);
    }

    private ArrayListSorting(ArrayList container, ListIterator first, ListIterator last, BinaryPredicate comparit ) {
        base = container;
        this.comparator = comparit;

        // calculate first and last index into the sequence.
        int start = distance(base.listIterator(), first);
        int finish = distance(base.listIterator(), last)+1;
//        int start = (base.start()).distance( first );
//        int finish = (base.start()).distance( last );

        quickSortLoop(start, finish);
        finalInsertionSort(start, finish);
    }

    void finalInsertionSort(int first, int last) {
        if (last - first > stlThreshold) {
            int limit = first + stlThreshold;

            for (int i = first + 1; i < limit; i++)
                linearInsert(first, i);

            for (int i = limit; i < last; i++)
                unguardedLinearInsert(i, base.get(i));
        }else{
            for (int i = first + 1; i < last; i++)
                linearInsert(first, i);
        }
    }

    void unguardedLinearInsert(int last, Object value) {
        int next = last - 1;

        while (comparator.execute(value, base.get(next )))
            base.set(last--, base.get(next--));

        base.set(last, value);
    }

    void linearInsert(int first, int last) {
        Object value = base.get(last);

        if (comparator.execute(value, base.get( first ))) {
            for (int i = last; i > first; i--)
                base.set(i, base.get(i - 1));

            base.set(first, value);
        }else{
            unguardedLinearInsert(last, value);
        }
    }

    void quickSortLoop(int first, int last) {
        while (last - first > stlThreshold) {
            Object pivot;
            Object a = base.get(first);
            Object b = base.get(first + (last - first) / 2);
            Object c = base.get(last - 1);

            if (comparator.execute(a, b)) {
                if (comparator.execute(b, c))
                    pivot = b;
                else if (comparator.execute(a, c ))
                    pivot = c;
                else
                    pivot = a;
            }else if (comparator.execute(a, c))
                pivot = a;
            else if (comparator.execute(b, c))
                pivot = c;
            else
                pivot = b;

            int cut = first;
            int lastx = last;

            while (true) {
                while (comparator.execute(base.get(cut), pivot))
                    ++cut;
                --lastx;

                while (comparator.execute(pivot, base.get(lastx)))
                    --lastx;

                if (cut >= lastx)
                    break;

                Object tmp = base.get(cut);
                base.set(cut++, base.get(lastx));
                base.set(lastx, tmp);
            }

            if ( cut - first >= last - cut ) {
                quickSortLoop(cut, last);
                last = cut;
            }else{
                quickSortLoop(first, cut);
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
