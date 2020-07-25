package gr.cti.eslate.database.engine;


import java.util.Date;
import java.text.SimpleDateFormat;


/** Class CDate is used to store date values in Tables. Basically it
 *  overrides class Date's <i>toString()</i> method, returning a String
 *  representation of a date value, which is has the format specified
 *  by Table's <i>dateFormat</i> variable.
 *
 * @version	2.0, May 01
 *
 */
public class CDate extends Date {
    protected static SimpleDateFormat dateFormat;
    static final long serialVersionUID = 12;

    /** Create a new CDate instance.
     */
    public CDate() {
        super();
    }

    /** Create a new CDate instance, set at the specified date.
     */
    public CDate(Date d) {
        super(d.getTime());
    }

    /** Create a new CDate instance, set at the specified date.
     */
    public CDate(long date) {
        super(date);
    }

    /** Returns a String representation of the CDate, which is based on the current
     *  <i>dateFormat</i> of the CTable.
     *  @see CTable#dateFormat
     */
    public String toString() {
        return(dateFormat.format(this));
    }

}