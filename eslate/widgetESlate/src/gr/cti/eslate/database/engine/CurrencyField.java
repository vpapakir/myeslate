package gr.cti.eslate.database.engine;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * To change this template use Options | File Templates.
 */
public interface CurrencyField {
    /**
     * Sets the current currency of the field.
     * @param currency The id of the new currency.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public void setCurrency(int currency);

    /** Returns the current currency of the field, or -1 if the field is not in currency mode.
     * @return The currency of the field.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public int getCurrency();

    /**
     * The symbol of the current currency. An empty string is returned if the field is not in currency mode.
     * @return The currency symbol.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public String getCurrencySymbol();
}
