package gr.cti.eslate.database.engine;

import gr.cti.eslate.base.CurrencyManager;
/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * To change this template use Options | File Templates.
 */
public class CurrencyTableField extends DoubleTableField {
    int currency = CurrencyManager.EUR;
    String currencySymbol = CurrencyManager.getCurrencyManager().getSymbol(CurrencyManager.EUR);

    /**
     * Returns the current currency of the field.
     * @return The currency id.
     * @see gr.cti.eslate.base.CurrencyManager
     */
    public int getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the field.
     * @param currency The id of the new currency.
     */
    public void setCurrency(int currency) {
        if (this.currency == currency) return;
        this.currency = currency;
        currencySymbol = CurrencyManager.getCurrencyManager().getSymbol(CurrencyManager.EUR);
    }

    /**
     * Returns the symbol for the current currency.
     * @return
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }
}
