package gr.cti.eslate.database.engine;


import com.objectspace.jgl.*;
import com.objectspace.jgl.BinaryPredicate;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * EqualFormattedDate is a binary predicate that
 * compares a date(first) to a formatted date(second). A formatted date is a date which
 * in some fields contains the wildcard character '*' instead of an actual value.
 * EqualFormattedDate compares only the parts
 * of the two dates for which actual values exist. Returns true if the
 * the two dates agree in these fields.
 */

public final class EqualFormattedDate extends DateComparator {
    boolean formattedDay;
    boolean formattedMonth;
    boolean formattedYear;
    private static Calendar c1 = new GregorianCalendar();
    private static Calendar c2 = new GregorianCalendar();

    public EqualFormattedDate(boolean fDay, boolean fMonth, boolean fYear) {
        formattedDay = fDay;
        formattedMonth = fMonth;
        formattedYear = fYear;
    }
  /**
   * Returns true if the first date is equal to the second date in the fields which
   * do not have the wildcard character '*'.
   */
  public boolean execute(CDate first, CDate second) {
        c1.setTime(first);
        c2.setTime(second);

//        System.out.println("Date1: " + first + " c1: " + c1.get(Calendar.DATE) + "/" + c1.get(Calendar.MONTH) + "/" + c1.get(Calendar.YEAR));

        boolean result = true;
        if (!formattedDay)
            result = (c1.get(Calendar.DATE) == c2.get(Calendar.DATE));
        if (!formattedMonth)
            result = (result && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)));
        if (!formattedYear) {
            result = (result && (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)));
        }
//        System.out.println("Date2: " + second + " c2: " + c2.get(Calendar.DATE) + "/" + c2.get(Calendar.MONTH) + "/" + c2.get(Calendar.YEAR) + "  result: " + result);

        return result;
    }
}
