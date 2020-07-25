package gr.cti.eslate.spinButton;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class NumberDataModel implements DataModelInterface {
    double step;
    double number = 0.0;
    //   double backUpNumber;
    double maxNumber = 9000000000d; //Double.MAX_VALUE;
    double minNumber = -9000000000d; //Double.MAX_VALUE;
    Locale locale;
    boolean enumerationMode;
    SpinModelDataEventMulticaster spinModelDataListener = new SpinModelDataEventMulticaster();

    /**
     * Constructs a new NumberDataModel with number value = 0.0.
     *
     */

    public NumberDataModel() {
        number = 0.0;
        //      backUpNumber=0.0;
    }

    /**
     * Returns the model's numeric value
     *
     */

    public Object getValue() {
        return new Double(number);
    }

    /**
     * Sets the model's numeric value
     *
     */

    public void setValue(Object value) throws IllegalArgumentException, OutOfBoundsException {
        //System.out.println("setValue() called " + value + ", minNumber: " + minNumber);
        Number d = null;

        try {
            d = (Number) parse(value);
        } catch (Exception exc) {
            throw new IllegalArgumentException("Not a valid argument.The type of the argument is " + value.getClass() + ", while it should be String or Number");
        }
        if (d == null)
            throw new NullPointerException("Null value or unable to parse value");
        if (d.doubleValue() == number) return;
        if (d.doubleValue() > maxNumber)
            throw new OutOfBoundsException("Value: " + d.doubleValue() + " is bigger that the maximum allowed number(" + maxNumber + ")");
        if (d.doubleValue() < minNumber)
            throw new OutOfBoundsException("Value: " + d.doubleValue() + " is smaller that the minimum allowed number(" + minNumber + ")");
        double prevValue = number;

        number = d.doubleValue();
        if (spinModelDataListener != null) {
            SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.VALUE_CHANGED, d, new Double(prevValue));

            spinModelDataListener.spinModelDataChanged(en);
        }
    }

    /**
     * Returns the string representation of the model's numeric value
     *
     */

    public String getStringValue() {
        return new String("" + NumberFormat.getInstance(Locale.getDefault()).format(number));
    }

    /**
     * Returns a String specifying the model's data type
     *
     */

    public int getDataModelType() {
        return SpinButton.NUMBER_DATA_MODEL;
        //      return new String("NumberDataModel");
    }

    /**
     * Increases the model's numeric value by d
     */

    public void increase(double d) {
        if ((number + d) <= maxNumber)
            try {
                setValue(new Double(number + d));
            } catch (Exception exc) {
                exc.printStackTrace();
            }
    }

    /**
     * Decreases the model's numeric value by d
     */


    public void decrease(double d) {
        if ((number + d) >= minNumber)
            try {
                setValue(new Double(number - d));
            } catch (Exception exc) {//exc.printStackTrace();
            }
    }

    /**
     * Returns the double value of this model's value
     */

    public double doubleValue() {
        return number;
    }

    /**
     * Returns the float value of this model's value
     */

    public float floatValue() {
        return (new Double(number)).floatValue();
    }

    /**
     * Returns the long value of this model's value
     */

    public long longValue() {
        return (new Double(number)).longValue();
    }

    /**
     * Returns the int value of this model's value
     */

    public int intValue() {
        return (new Double(number)).intValue();
    }

    /**
     * Adds a SpinModelDataListener to this numberDataModel
     * @param listener The SpinModelDataListener
     *
     */

    public void addSpinModelDataListener(SpinModelDataListener listener) {
        spinModelDataListener.add(listener);
    }

    /**
     * Removes a SpinModelDataListener from this numberDataModel
     * @param listener The SpinModelDataListener
     *
     */

    public void removeSpinModelDataListener(SpinModelDataListener listener) {
        spinModelDataListener.remove(listener);
    }

    /**
     * Sets the maximum value of the model
     *
     */
    public void setMaximumValue(Object o) {
        Number n = (Number) parse(o);

        if (n != null && n.doubleValue() >= minNumber) {
            double prevMaxValue = maxNumber;

            maxNumber = n.doubleValue();
            if (spinModelDataListener != null) {
                SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.MAX_VALUE_CHANGED, n, new Double(prevMaxValue));

                spinModelDataListener.spinModelDataChanged(en);
            }

            /* If the current value is bigger then maximum value, then set it to the
             * maximum value
             */
            if (number > maxNumber) {
                try {
                    setValue(new Double(maxNumber));
                } catch (Exception exc) {}
            }
        } else
            throw new RuntimeException("Maximum value cannot be less than minimum value");
    }

    /**
     * Returns the maximum value of the model
     *
     */

    public Object getMaximumValue() {
        return new Double(maxNumber);
    }

    /**
     * Sets the minimum value of the model
     *
     */

    public void setMinimumValue(Object o) {
        Number n = (Number) parse(o);

        if (n != null && n.doubleValue() <= maxNumber) {
            double prevMinValue = minNumber;

            minNumber = n.doubleValue();
            if (spinModelDataListener != null) {
                SpinModelDataEvent en = new SpinModelDataEvent(this, SpinModelDataEvent.MIN_VALUE_CHANGED, n, new Double(prevMinValue));

                spinModelDataListener.spinModelDataChanged(en);
            }

            /* If the current value is less than the minimum value, then set it to the
             * minimum value */
            if (number < minNumber) {
                try {
                    setValue(new Double(minNumber));
                } catch (Exception exc) {}
            }
        } else
            throw new RuntimeException("Minimum value cannot be greater than maximum value");
    }

    /**
     * Returns the minimum value of the model
     *
     */

    public Object getMinimumValue() {
        return new Double(minNumber);
    }

    private Object parse(Object s) throws IllegalArgumentException {
        Number d = null;

        if (s instanceof String) {
            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());

            try {
                d = formatter.parse(s.toString());
            } catch (ParseException e) {
                d = null;
            }
        } else if (s instanceof Number) {
            return s;
        } else throw new IllegalArgumentException("Not a valid argument, The data type of the argument is " + s.getClass() + ". It should be String or Number");
        return d;
    }

}
