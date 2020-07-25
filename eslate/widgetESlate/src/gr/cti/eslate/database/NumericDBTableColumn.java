package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.CurrencyField;
import gr.cti.eslate.base.CurrencyManager;
import gr.cti.eslate.utils.ESlateFieldMap;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * Date: 28 Ιουλ 2003
 * Time: 11:42:26 πμ
 * To change this template use Options | File Templates.
 */
public abstract class NumericDBTableColumn extends AbstractDBTableColumn {
    /** Constant used by setCurrencyDisplayMode() */
    public static final int DISPLAY_CURRENCY_SYMBOL_IN_CELL = 1;
    /** Constant used by setCurrencyDisplayMode() */
    public static final int DISPLAY_CURRENCY_SYMBOL_IN_HEADER = 2;

    // Δημιουργήθηκε για να υπάρχει κοινός απόγονος για όλα τα αριθμητικά πεδία.
    // Στην κλάση αυτή θα μπουν visual χαρακτηριστικά των πεδίων, όπως π.χ. το format των αριθμών
    DBTable dbTable = null;
    /**
     * The currency to which the numbers of the column refer to. By default no currency is specified (-1).
     */
    int currency = -1;
    /**
     * The currency symbol.
     */
    String currencySymbol = "";
    /**
     * The maximum number of franction digits, this numeric column displays for its data.
     */
    int maximumFractionDigits = 5;
    /**
     * True if the numbers of this column are percentages. A number which refers to a percentage is displayed multiplied
     * by 100. The number is followed by the '%' symbol
     */
    boolean percentageFormatUsed = false;
    /**
     * The currency symbol display mode.
     */
    int currencyDisplayMode = DISPLAY_CURRENCY_SYMBOL_IN_HEADER;

    /**
     * Sets the currency of this column. A currency column can't be a percentage column at the same time.
     * @param currency The currency in which the data of this column are displayed
     * @see CurrencyManager
     */
    public void setCurrency(int currency) {
//System.out.println("setCurrency() field: " + tableField.getName() + ", this.currency: " + this.currency + ", currency: " + currency + ", currencySymbol: " + this.currencySymbol);
        if (this.currency == currency) return;
        if (((CurrencyField) tableField).getCurrency() == -1 && currency != -1) {
            System.out.println("The TableField is not in currency mode, so you can't set the currency in which it is viewed. Use TableField.setCurrency(int) first.");
            return;
        }
        // Check if th currency id is valid
        String symbol = "";
        if (currency != -1) {
            symbol = CurrencyManager.getCurrencyManager().getSymbol(currency);
            if (symbol == null || symbol.length() == 0) return;
        }
        this.currency = currency;
        if (currency != -1 && percentageFormatUsed) percentageFormatUsed = false;
        currencySymbol = symbol;
        dbTable.refreshField(tableColumn);
        adjustHeaderText();
    }

    /**
     * Returns the currency of the column or -1 if the numeric data do not refer to a currency.
     * @return
     */
    public int getCurrency() {
        return currency;
    }

    /**
     * The symbol of the current currency.
     * @see CurrencyManager#getSymbol(int)
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * Sets the maximum number of franction digits that this column can display.
     * @param numOfDigits
     */
    public void setMaximumFractionDigits(int numOfDigits) {
        if (maximumFractionDigits == numOfDigits) return;
        maximumFractionDigits = numOfDigits;
//System.out.println("dbTable: " + dbTable + ", field: " + tableField.getName());
        dbTable.refreshField(tableColumn);
    }

    /**
     * Returns the maximum number of franction digits displayed for this numeric column.
     * @return
     */
    public int getMaximumFractionDigits() {
        return maximumFractionDigits;
    }

    /**
     * Determines if the column displays it's data as percentages. A percentage column
     * can't be a currency column at the same time.
     * @param used
     */
    public void setPercentageFormatUsed(boolean used) {
        if (percentageFormatUsed == used) return;
        percentageFormatUsed = used;
        if (currency != -1) setCurrency(-1);
        dbTable.refreshField(tableColumn);
    }

    /**
     * Determines if the currency symbol will be displayed in every cell or in the header of the numeric column.
     * @param mode One of <code>DISPLAY_CURRENCY_SYMBOL_IN_CELL</code>, <code>DISPLAY_CURRENCY_SYMBOL_IN_HEADER</code>.
     * @see #DISPLAY_CURRENCY_SYMBOL_IN_CELL
     * @see #DISPLAY_CURRENCY_SYMBOL_IN_HEADER
     */
    public void setCurrencyDisplayMode(int mode) {
        if (currencyDisplayMode == mode) return;
        if (mode != DISPLAY_CURRENCY_SYMBOL_IN_CELL && mode != DISPLAY_CURRENCY_SYMBOL_IN_HEADER) return;
        currencyDisplayMode = mode;
        if (currencyDisplayMode == DISPLAY_CURRENCY_SYMBOL_IN_HEADER)
            adjustHeaderText();
    }

    private void adjustHeaderText() {
        if (currencyDisplayMode != DISPLAY_CURRENCY_SYMBOL_IN_HEADER) return;
        HeaderRenderer hr = (HeaderRenderer) tableColumn.getHeaderRenderer();
        if (currencySymbol.length() == 0)
            hr.setText(tableField.getName());
        else{
            String fieldName = tableField.getName();
            hr.setText(fieldName + " (" + currencySymbol + ')');
        }
    }

    /**
     * Reports if the column displays number in percentage format.
     * @return
     */
    public boolean isPercentageFormatUsed() {
        return percentageFormatUsed;
    }

    public void recordState(ESlateFieldMap2 state) {
        state.put("currency", currency);
        state.put("currencySymbol", currencySymbol);
        state.put("maximumFractionDigits", maximumFractionDigits);
        state.put("percentageFormatUsed", percentageFormatUsed);
        state.put("currencyDisplayMode", currencyDisplayMode);
    }

    public void applyState(StorageStructure state) {
        currency = state.get("currency", -1);
        currencySymbol = state.get("currencySymbol", "");
        maximumFractionDigits = state.get("maximumFractionDigits", 5);
        percentageFormatUsed = state.get("percentageFormatUsed", false);
        currencyDisplayMode = DISPLAY_CURRENCY_SYMBOL_IN_CELL;
        setCurrencyDisplayMode(state.get("currencyDisplayMode", DISPLAY_CURRENCY_SYMBOL_IN_HEADER));
    }
}
