package gr.cti.eslate.database.engine;

import java.util.Date;
import java.text.SimpleDateFormat;


/** Class CTime is used to store time values in Tables. Basically it
 *  overrides class Date's <i>toString()</i> method, returning a String
 *  representation of a time value, which is has the format specified
 *  by Table's <i>timeFormat</i> variable.
 *
 * @version	2.0, May 01
 *
 */
public class CTime extends Date {
    protected static SimpleDateFormat timeFormat;
    static final long serialVersionUID = 12;

    /** Create a new CTime instance.
     */
    public CTime() {
        super();
    }

    /** Create a new CTime instance, set at the specified time.
     */
    public CTime(Date d) {
        super(d.getTime());
    }

    /** Create a new CTime instance, set at the specified time.
     */
    public CTime(long time) {
        super(time);
    }

    /** Returns a String representation of the CTime, which is based on the current
     *  <i>timeFormat</i> of the CTable.
     *  @see CTable#timeFormat
     */
    public String toString() {
        return(timeFormat.format(this));
    }

}