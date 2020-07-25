package gr.cti.eslate.clock;


import java.util.*;
import java.io.*;


/**
 * This class implements an hours/minutes/seconds counter that can be advanced
 * by an amount of microseconds. Values less than 1000000 are handled
 * correctly.
 *
 * @author	Kriton Kyrimis
 * @version	1.5.16, 3-Jul-2000
 */
public class TimeCount implements Serializable {

    /**
     * Counter's "hours" value.
     */
    public int hour;

    /**
     * Counter's "minutes" value.
     */
    public int min;

    /**
     * Counter's "seconds" value.
     */
    public int sec;

    /**
     * Counter's "microseconds" value.
     */
    public long usec;

    //static final long serialVersionUID = -5258232139260829431L;
    static final long serialVersionUID = -6250791545516997473L;

    /**
     * Create a TimeCount instance with a given initial value.
     * @param	h	The "hours" value of the counter.
     * @param	m	The "minutes" value of the counter.
     * @param	s	Th "seconds" value of the counter.
     */
    public TimeCount(int h, int m, int s) {
        hour = h;
        min = m;
        sec = s;
        usec = 0;
    }

    /**
     * Create a TimeCount instance initialized to the system time.
     */
    public TimeCount() {
        GregorianCalendar now = new GregorianCalendar();

        hour = now.get(GregorianCalendar.HOUR_OF_DAY);
        min = now.get(GregorianCalendar.MINUTE);
        sec = now.get(GregorianCalendar.SECOND);
        usec = 0;
    }

    /**
     * Set the counter to a given value.
     * @param	h	The "hours" value of the counter.
     * @param	m	The "minutes" value of the counter.
     * @param	s	Th "seconds" value of the counter.
     */
    public void set(int h, int m, int s) {
        hour = h;
        min = m;
        sec = s;
        usec = 0;
    }

    /**
     * Advance the counter by a given number of microseconds. Values less than
     * 1000000 are handled correctly.
     * @param	microseconds	The number of microseconds by which the
     *				counter should be advanced.
     */
    public void advance(long microseconds) {
        usec += microseconds;
        if (usec >= 1000000) {
            sec += (usec / 1000000);
            usec %= 1000000;
            if (sec >= 60) {
                min += (sec / 60);
                sec %= 60;
                if (min >= 60) {
                    hour += (min / 60);
                    min %= 60;
                    if (hour >= 24) {
                        hour %= 24;
                    }
                }
            }
        }
    }

    public void retreat(long microseconds) {
        //  System.out.println("go back method");
        usec += microseconds;
        if (usec >= 1000000) {
            sec -= (usec / 1000000);
            usec %= 1000000;
            if (sec >= 60) {
                min -= (sec / 60);
                sec %= 60;
                if (min >= 60) {
                    hour -= (min / 60);
                    min %= 60;
                    if (hour >= 24) {
                        hour %= 24;
                    }
                }
            }
        }
    }
}
