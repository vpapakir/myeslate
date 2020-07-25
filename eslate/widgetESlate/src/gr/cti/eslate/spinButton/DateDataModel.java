package gr.cti.eslate.spinButton;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.text.*;


public class DateDataModel implements DataModelInterface {
    int step;
    public static final int DAYSTIMERATE = 0;
    public static final int MONTHSTIMERATE = 1;
    public static final int YEARSTIMERATE = 2;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    GregorianCalendar calendar; // = new GregorianCalendar();
    GregorianCalendar maxCalendar = new GregorianCalendar(3000, 12, 30, 0, 0, 0);
    GregorianCalendar minCalendar = new GregorianCalendar(3000, 1, 1, 0, 0, 0);
    Date value = null;
    Locale locale;
    int dateRate = 0;
    SpinModelDataEventMulticaster spinModelDataListener = new SpinModelDataEventMulticaster();

    /**
     * Constructs a new DateDataModel with date value the "today" date.
     *
     */
    public DateDataModel() {

        /* The date sympols for the Greek locale do not contain "ì.×." and "ð.×.". So if the
         * locale is Greek, we set these date format symbols below.
         */
        if (Locale.getDefault().toString().equals("el_GR")) {
            DateFormatSymbols dfs = new DateFormatSymbols();

            dfs.setEras(new String[] {"ð.×.", "ì.×."}
            );
            //          eras = dfs.getEras();
            //          for (int i=0; i<eras.length; i++)
            //              System.out.println("era " + i + ": " + eras[i]);
            dateFormat.setDateFormatSymbols(dfs);
        }
        maxCalendar.set(Calendar.ERA, GregorianCalendar.AD);
        minCalendar.set(Calendar.ERA, GregorianCalendar.BC);
        calendar = new GregorianCalendar();
        calendar.clear();
        value = new Date();
        calendar.setTime(value);
    }

    /**
     * Returns the calendar of the model's date
     *
     */
    public Object getValue() {
        return value; //calendar.getTime();
    }

    /**
     * Sets the model's current date
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
        if (this.value.equals(d)) return;
        if (d.before(minCalendar.getTime()) || d.after(maxCalendar.getTime()))
            throw new OutOfBoundsException("Date is after (or before) the maximum (minimum) allowed date");
        Date prevValue = this.value;

        this.value = d;
        if (spinModelDataListener != null) {
            SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.VALUE_CHANGED, d, prevValue);
            spinModelDataListener.spinModelDataChanged(en);
        }
    }

    /**
     * Returns the String value of the model's date value using the current date format
     *
     */
    public String getStringValue() {
        return new String(dateFormat.format(value)); //calendar.getTime()));
    }

    /**
     * Returns a String specifying the model's data type
     *
     */
    public int getDataModelType() {
        return SpinButton.DATE_DATA_MODEL;
        //      return new String("DateDataModel");
    }

    /**
     * Increases the model's date value by d (depending from dateRate)
     * E.g. if dateRate = 1, then increase(10) increases months value by 10
     *
     */
    public void increase(double d) {
        step = (new Double(d)).intValue();
        increase();
    }

    /**
     * Increases the model's date value by default step (depending from dateRate)
     * E.g. if dateRate = 1, then increase() increases months value by the default step
     *
     */
    public void increase() {
        calendar.setTime(value);
        if (dateRate == 0)
            calendar.add(Calendar.DAY_OF_MONTH, step);
        else if (dateRate == 1)
            calendar.add(Calendar.MONTH, step);
        else if (dateRate == 2)
            calendar.add(Calendar.YEAR, step);
        try {
            setValue(calendar.getTime());
        } catch (Exception exc) {//exc.printStackTrace();
        }
    }

    /**
     * Decreases the model's date value by d (depending from dateRate)
     * E.g. if dateRate = 1, then decrease(10) decreases months value by 10
     *
     */
    public void decrease(double d) {
        step = (new Double(d)).intValue();
        decrease();
    }

    /**
     * Decreases the model's date value by default step (depending from dateRate)
     * E.g. if dateRate = 1, then decrease() decreases months value by the default step
     *
     */
    public void decrease() {
        calendar.setTime(value);
        if (dateRate == 0)
            calendar.add(Calendar.DAY_OF_MONTH, -step);
        else if (dateRate == 1)
            calendar.add(Calendar.MONTH, -step);
        else if (dateRate == 2)
            calendar.add(Calendar.YEAR, -step);
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
     * Sets the format of the model's date value
     * @param pattern The format pattern
     *
     */
    public void setFormat(String pattern) {
        dateFormat.applyPattern(pattern);
    }

    /**
     * Gets the format of the model's date value
     *
     */
    public String getFormat() {
        return dateFormat.toPattern();
    }

    /**
     * Sets the rate that increases/decreases the model's date value
     * @param rate The rate
     * E.g. rate = 0 means that time is incresed/decreased by days, rate = 1 by months, rate = 2 by years.
     */
    public void setDateRate(int rate) {
        dateRate = rate;
    }

    /**
     * Returns the rate that increases/decreases the model's date value
     *
     */
    public int getDateRate() {
        return dateRate;
    }

    /**
     * Sets the maximum date value of the model
     * @param d The maximum date
     *
     */
    public void setMaximumValue(Object d) {
        Date date = (Date) parse(d);

        if (date != null && (date.after(minCalendar.getTime()) || date.getTime() == minCalendar.getTime().getTime())) {
            Date prevMaxValue = maxCalendar.getTime();

            maxCalendar.setTime((Date) d);
            if (spinModelDataListener != null) {
                SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.MAX_VALUE_CHANGED, d, prevMaxValue);

                spinModelDataListener.spinModelDataChanged(en);
            }

            /* If the current value is bigger then maximum value, then set it to the
             * maximum value */
            if (value.after(maxCalendar.getTime())) {
                try {
                    //System.out.println("before setMaximumValue() value: " + getStringValue());
                    setValue(maxCalendar.getTime());
                    //System.out.println("after setMaximumValue() value: " + getStringValue());
                } catch (Exception exc) {}
            }
        } else
            throw new RuntimeException("Maximum value cannot be less than minimum value");
    }

    /**
     * Returns the maximum date value of the model
     *
     */
    public Object getMaximumValue() {
        return  maxCalendar.getTime();
    }

    /**
     * Sets the minimum date value of the model
     * @param d The minimum date
     *
     */
    public void setMinimumValue(Object d) {
        Date date = (Date) parse(d);

        if (date != null && (date.before(maxCalendar.getTime()) || date.getTime() == maxCalendar.getTime().getTime())) {
            Date prevMinValue = minCalendar.getTime();

            minCalendar.setTime((Date) d);
            if (spinModelDataListener != null) {
                SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.MIN_VALUE_CHANGED, d, prevMinValue);

                spinModelDataListener.spinModelDataChanged(en);
            }

            /* If the current value is less than the minimum value, then set it to the
             * minimum value */
            if (value.before(minCalendar.getTime())) {
                try {
                    //System.out.println("before setMinimumValue() value: " + getStringValue());
                    setValue(minCalendar.getTime());
                    //System.out.println("after setMinimumValue() value: " + getStringValue());
                } catch (Exception exc) {}
            }
        } else
            throw new RuntimeException("Minimum value cannot be greater than maximum value");
    }

    /**
     * Returns the minimum date value of the model
     *
     */
    public Object getMinimumValue() {
        return minCalendar.getTime();
    }

    private Object parse(Object s) throws IllegalArgumentException {
        Date d = null;

        if (s instanceof String) {
            SimpleDateFormat formatter = this.dateFormat;

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
