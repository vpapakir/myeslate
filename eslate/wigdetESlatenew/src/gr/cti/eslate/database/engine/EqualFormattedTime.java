package gr.cti.eslate.database.engine;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * EqualFormattedTime is a binary predicate that
 * compares a time value(first) to a formatted time value(second).
 * A formatted time value
 * contains the wildcard character '*' in one of or both the fields of a time value,
 * instead of an actual value.
 * EqualFormattedTime compares only the parts
 * of the two time values for which actual values exist. Returns true if the
 * the two time values agree in these fields.
 */

public final class EqualFormattedTime extends TimeComparator {
    boolean formattedHour;
    boolean formattedMinute;
    private static Calendar c1 = new GregorianCalendar();
    private static Calendar c2 = new GregorianCalendar();

    public EqualFormattedTime(boolean fHour, boolean fMinute) {
        formattedHour = fHour;
        formattedMinute = fMinute;
    }

      /**
       * Returns true if the first time value is equal to the second time value in the fields which
       * do not contain wildcard '*'.
       */
    public boolean execute(CTime first, CTime second) {
        c1.setTime(first);
        c2.setTime(second);

    //        System.out.println("Time1: " + first + " c1: " + c1.get(Calendar.HOUR) + ":" + c1.get(Calendar.MINUTE) + "  " + c1.get(Calendar.AM_PM));
    //        System.out.println("Time2: " + second + " c2: " + c2.get(Calendar.HOUR) + ":" + c2.get(Calendar.MINUTE) + "  " + c2.get(Calendar.AM_PM));

        boolean result = true;
        if (!formattedHour)
            result = ((c1.get(Calendar.HOUR) == c2.get(Calendar.HOUR)) && (c1.get(Calendar.AM_PM) == c2.get(Calendar.AM_PM)));
        if (!formattedMinute)
            result = (result && (c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)) && (c1.get(Calendar.AM_PM) == c2.get(Calendar.AM_PM)));

        return result;
    }
}
