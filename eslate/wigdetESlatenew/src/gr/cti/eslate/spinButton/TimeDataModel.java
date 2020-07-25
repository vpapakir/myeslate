package gr.cti.eslate.spinButton;


import java.util.Date;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.text.*;


public class TimeDataModel implements DataModelInterface {
    int step;
    public static final int SECONDSTIMERATE = 0;
    public static final int MINUTESTIMERATE = 1;
    public static final int HOURSTIMERATE = 2;
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    GregorianCalendar calendar;
    GregorianCalendar maxCalendar = null; //new GregorianCalendar(2000,1,1,23,59,59);
    GregorianCalendar minCalendar = null; //new GregorianCalendar(2000,1,1,0,0,0);
    Date value = null;

    Locale locale;
    Date time;
    String pattern;
    protected SpinModelDataEventMulticaster spinModelDataListener = new SpinModelDataEventMulticaster();
    int timeRate = 0;

    /**
     * Constructs a new TimeDataModel with data value the "now" time.
     *
     */
    public TimeDataModel() {
        calendar = new GregorianCalendar();
        calendar.clear();
        value = new Date();
        calendar.setTime(value);
        calendar.set(2000, 1, 1);
        value = calendar.getTime();
    }

    /**
     * Returns the calendar of the model's time
     *
     */
    public Object getValue() {
        return value; //calendar.getTime();
    }

    /**
     * Sets the model's current time
     * @param value The object date
     */
    public void setValue(Object val) throws IllegalArgumentException, OutOfBoundsException {
        Date d = null;

        try {
            d = (Date) parse(val);
        } catch (Exception exc) {
            throw new IllegalArgumentException("Not a valid argument, is " + val.getClass() + ", needed String or Date");
        }
        if (d == null)
            throw new NullPointerException("Null or inparceable value");

        calendar.setTime(d);
        calendar.set(2000, 1, 1);
        d = calendar.getTime();
        if (value.equals(d)) return;
        if ((minCalendar != null && d.before(minCalendar.getTime())) || (maxCalendar != null && d.after(maxCalendar.getTime())))
            throw new OutOfBoundsException("Date is before (or after) the maximum (minimum) allowed date");
        Date prevValue = value; //calendar.getTime();

        value = d;
        if (spinModelDataListener != null) {
            SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.VALUE_CHANGED, d, prevValue);

            spinModelDataListener.spinModelDataChanged(en);
        }
    }

    /**
     * Returns the String value of the model's time value using the current time format
     *
     */

    public String getStringValue() {
        return new String(timeFormat.format(value)); //calendar.getTime()));
    }

    /**
     * Returns a String specifying the model's data type
     *
     */
    public int getDataModelType() {
        return SpinButton.TIME_DATA_MODEL;
        //      return new String("TimeDataModel");
    }

    /**
     * Increases the model's time value by d (depending from timeRate)
     * E.g. if timeRate = 1, then increase(10) increases minutes value by 10
     *
     */

    public void increase(double d) {
        step = (new Double(d)).intValue();
        increase();
    }

    /**
     * Increases the model's time value by default step (depending from timeRate)
     * E.g. if timeRate = 1, then increase() increases minutes value by the default step
     *
     */
    public void increase() {
        calendar.setTime(value);
        if (timeRate == 0)
            calendar.add(GregorianCalendar.SECOND, step);
        else if (timeRate == 1)
            calendar.add(GregorianCalendar.MINUTE, step);
        else if (timeRate == 2)
            calendar.add(GregorianCalendar.HOUR, step);
        try {
            setValue(calendar.getTime());
        } catch (Exception exc) {//exc.printStackTrace();
        }
    }

    /**
     * Decreases the model's time value by d (depending from timeRate)
     * E.g. if timeRate = 1, then decrease(10) decreases minutes value by 10
     *
     */

    public void decrease(double d) {
        step = (new Double(d)).intValue();
        decrease();
    }

    /**
     * Decreases the model's time value by default step (depending from timeRate)
     * E.g. if timeRate = 1, then decrease() decreases minutes value by the default step
     *
     */
    public void decrease() {
        calendar.setTime(value);
        if (timeRate == 0)
            calendar.add(GregorianCalendar.SECOND, -step);
        else if (timeRate == 1)
            calendar.add(GregorianCalendar.MINUTE, -step);
        else if (timeRate == 2)
            calendar.add(GregorianCalendar.HOUR, -step);
        try {
            setValue(calendar.getTime());
        } catch (Exception exc) {//exc.printStackTrace();
        }
    }

    /**
     * Adds a SpinModelDataListener to this dateDataModel
     * @param listener The SpinModelDataListener
     *
     */
    public void addSpinModelDataListener(SpinModelDataListener listener) {
        spinModelDataListener.add(listener);
    }

    /**
     * Removes a SpinModelDataListener from this dateDataModel
     * @param listener The SpinModelDataListener
     *
     */
    public void removeSpinModelDataListener(SpinModelDataListener listener) {
        spinModelDataListener.remove(listener);
    }

    /**
     * Sets the format of the model's time value
     * @param pattern The format pattern
     *
     */
    public void setFormat(String pattern) {
        timeFormat.applyPattern(pattern);
        this.pattern = pattern;

        /* The value does not change, so there is no need to fire an event
         if (spinModelDataListener !=null ){
         SpinModelDataEvent en = new SpinModelDataEvent(this,SpinModelDataEvent.DATA_CHANGED_LAST,pattern);
         spinModelDataListener.spinModelDataChanged(en);
         }
         */
    }

    /**
     * Returns the format of the model's time value
     *
     */
    public String getFormat() {
        return timeFormat.toPattern();
    }

    /**
     * Sets the rate that increases/decreases the model's time value
     * @param rate The rate
     * E.g. rate = 0 means that time is incresed/decreased by seconds, rate = 1 by minutes, rate = 2 by hours.
     */
    public void setTimeRate(int rate) {
        timeRate = rate;
    }

    /**
     * Returns the rate that increases/decreases the model's time value
     *
     */
    public int getTimeRate() {
        return timeRate;
    }

    /**
     * Sets the maximum time value of the model
     * @param d The maximum time
     *
     */
    public void setMaximumValue(Object d) {
        Date date = (Date) parse(d);

        if (date != null && (minCalendar == null || (date.after(minCalendar.getTime()) || date.getTime() == minCalendar.getTime().getTime()))) {

            Date prevMaxValue = null;

            if (maxCalendar != null)
                maxCalendar.getTime();
            else
                maxCalendar = new GregorianCalendar(2000, 1, 1, 23, 59, 59);
            maxCalendar.setTime((Date) d);
            maxCalendar.set(2000, 1, 1);
            if (spinModelDataListener != null) {
                SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.MAX_VALUE_CHANGED, d, prevMaxValue);

                spinModelDataListener.spinModelDataChanged(en);
            }

            /* If the current value is bigger then maximum value, then set it to the
             * maximum value */
            if (value.after(maxCalendar.getTime())) {
                try {
                    setValue(maxCalendar.getTime());
                } catch (Exception exc) {}
            }
        } else
            throw new RuntimeException("Maximum value cannot be less than minimum value");
    }

    /**
     * Returns the maximum time value of the model. Null, if it hasn't been set.
     *
     */
    public Object getMaximumValue() {
        if (maxCalendar == null) return null;
        return maxCalendar.getTime();
    }

    /**
     * Sets the minimum time value of the model
     * @param d The minimum time
     *
     */
    public void setMinimumValue(Object d) {
        Date date = (Date) parse(d);

        if (date != null && (maxCalendar == null || (date.before(maxCalendar.getTime()) || date.getTime() == maxCalendar.getTime().getTime()))) {
            Date prevMinValue = null;

            if (minCalendar != null)
                prevMinValue = minCalendar.getTime();
            else
                minCalendar = new GregorianCalendar(2000, 1, 1, 0, 0, 0);
            minCalendar.setTime((Date) d);
            minCalendar.set(2000, 1, 1);
            if (spinModelDataListener != null) {
                SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.MIN_VALUE_CHANGED, d, prevMinValue);

                spinModelDataListener.spinModelDataChanged(en);
            }

            /* If the current value is less than the minimum value, then set it to the
             * minimum value */
            if (value.before(minCalendar.getTime())) {
                try {
                    setValue(minCalendar.getTime());
                } catch (Exception exc) {
                    System.out.println("EXCEPTION");
                }
            }
        } else
            throw new RuntimeException("Minimum value cannot be greater than maximum value");
    }

    /**
     * Returns the minimum time value of the model. Null, if it hasn't been set
     *
     */
    public Object getMinimumValue() {
        if (minCalendar == null) return null;
        return minCalendar.getTime();
    }

    private Object parse(Object s) throws IllegalArgumentException {
        Date d = null;

        if (s instanceof String) {
            SimpleDateFormat formatter = this.timeFormat;

            try {
                d = formatter.parse(s.toString());
            } catch (ParseException e) {
                d = null;
            }
        } else if (s instanceof Date) {
            return s;
        } else throw new IllegalArgumentException("Not a valid argument, is " + s.getClass() + ", needed String or Date");
        return d;
    }

}
