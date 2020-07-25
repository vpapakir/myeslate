package gr.cti.eslate.spinButton;


public interface DataModelInterface {

    /**
     * Returns the model's value
     */

    public abstract Object getValue();

    /**
     * Returns a string representation of the model's value
     */

    public abstract String getStringValue();

    /**
     * Sets the model's value
     */

    public abstract void setValue(Object value) throws IllegalArgumentException, OutOfBoundsException;

    /**
     * Increases the model's value by d or by d.intValue(), depending from the models type
     */

    public abstract void increase(double value);

    /**
     * Decreases the model's value by d or by d.intValue(), depending from the models type
     */

    public abstract void decrease(double value);

    /**
     * Adds a SpinModelDataListener to the model
     * @param listener The SpinModelDataListener
     *
     */

    public abstract void addSpinModelDataListener(SpinModelDataListener listener);

    /**
     * Removes a SpinModelDataListener from the model
     * @param listener The SpinModelDataListener
     *
     */
    public abstract void removeSpinModelDataListener(SpinModelDataListener listener);

    public abstract void setMaximumValue(Object value);

    public abstract void setMinimumValue(Object value);

    public abstract Object getMaximumValue();

    public abstract Object getMinimumValue();

    public abstract int getDataModelType();
}
