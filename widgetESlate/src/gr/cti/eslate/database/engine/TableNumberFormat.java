/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 28, 2002
 * Time: 3:14:56 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database.engine;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

public class TableNumberFormat extends DecimalFormat implements Externalizable {
    static final long serialVersionUID = 12;
    private static final int FORMAT_VERSION = 1;
    private String nonExpPattern = null;

    public TableNumberFormat() {
        super();
        setMaximumFractionDigits(5);
    }

    public TableNumberFormat(DoubleNumberFormat format) {
        setDecimalSeparator(format.getDecimalSeparator());
        setThousandSeparator(format.getThousandSeparator());
        setThousandSeparatorUsed(format.isThousandSeparatorUsed());
        setDecimalSeparatorAlwaysShown(format.isDecimalSeparatorAlwaysShown());
        setShowIntegerPartOnly(format.isShowIntegerPartOnly());
        setExponentialFormatUsed(format.isExponentialFormatUsed());
        setMaximumFractionDigits(5);
    }

    public char getDecimalSeparator() {
//System.out.println("getDecimalSeparator(): " + getDecimalFormatSymbols().getDecimalSeparator());
        return getDecimalFormatSymbols().getDecimalSeparator();
    }

    public char getThousandSeparator() {
        return getDecimalFormatSymbols().getGroupingSeparator();
    }

    public void setThousandSeparator(char c) {
        DecimalFormatSymbols symbols = getDecimalFormatSymbols();
        DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();
        newSymbols.setDecimalSeparator(symbols.getDecimalSeparator());
        newSymbols.setGroupingSeparator(c);
        setDecimalFormatSymbols(newSymbols);
    }

    public void setDecimalSeparator(char c) {
        DecimalFormatSymbols symbols = getDecimalFormatSymbols();
        DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();
        newSymbols.setGroupingSeparator(symbols.getGroupingSeparator());
        newSymbols.setDecimalSeparator(c);
//System.out.println("setDecimalSeparator(): " + c);
        setDecimalFormatSymbols(newSymbols);
    }

    public boolean isThousandSeparatorUsed() {
        return isGroupingUsed();
    }

    public void setThousandSeparatorUsed(boolean use) {
        setGroupingUsed(use);
    }

    public void setDecimalSeparatorAlwaysVisible(boolean always) {
        setDecimalSeparatorAlwaysShown(always);
        if (always)
            setMinimumFractionDigits(1);
        else
            setMinimumFractionDigits(0);
    }

    public boolean isDecimalSeparatorAlwaysVisible() {
        return isDecimalSeparatorAlwaysShown();
    }

    public void setShowIntegerPartOnly(boolean integerPartOnly) {
        if (integerPartOnly)
            setMaximumFractionDigits(0);
        else
            setMaximumFractionDigits(30);
    }

    public boolean isShowIntegerPartOnly() {
        return (getMaximumFractionDigits() == 0);
    }

    public void setExponentialFormatUsed(boolean use) {
        if (isExponentialFormatUsed() == use) return;
        if (use) {
            nonExpPattern = toPattern();
            applyPattern("##0.##E0");
        }else{
            applyPattern(nonExpPattern);
        }
    }

    public boolean isExponentialFormatUsed() {
        return (toPattern().indexOf('E') != -1); //exponentialFormatUsed;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Decimal separator", new Character(getDecimalSeparator())); // new Character(decimalSeparator));
        fieldMap.put("Thousand separator", new Character(getThousandSeparator())); //new Character(thousandSeparator));
        fieldMap.put("Thousand separator used", isThousandSeparatorUsed()); //thousandSeparatorUsed);
        fieldMap.put("Decimal separator always shown", isDecimalSeparatorAlwaysVisible()); //decimalSeparatorAlwaysShown);
        fieldMap.put("Exponential format used", isExponentialFormatUsed()); //exponentialFormatUsed);
        fieldMap.put("Show integer part only", isShowIntegerPartOnly()); //showIntegerPartOnly);
        fieldMap.put("MaximumFractionDigits", getMaximumFractionDigits());
        fieldMap.put("MinimumFractionDigits", getMinimumFractionDigits());
        fieldMap.put("PreviousPattern", nonExpPattern);
        out.writeObject(fieldMap);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure ss = (StorageStructure) in.readObject();
        nonExpPattern = (String) ss.get("PreviousPattern");
//System.out.println("prevPattern: " + nonExpPattern);
        Character decSeparator = (Character) ss.get("Decimal separator");
        if (decSeparator != null)
            setDecimalSeparator(decSeparator.charValue());
        Character thSeparator = (Character) ss.get("Thousand separator");
        if (thSeparator != null)
            setThousandSeparator(thSeparator.charValue());
        setThousandSeparatorUsed(ss.get("Thousand separator used", isThousandSeparatorUsed()));
        setDecimalSeparatorAlwaysVisible(ss.get("Decimal separator always shown", isDecimalSeparatorAlwaysVisible()));
        setExponentialFormatUsed(ss.get("Exponential format used", isExponentialFormatUsed()));
        setShowIntegerPartOnly(ss.get("Show integer part only", isShowIntegerPartOnly()));
        setMaximumFractionDigits(ss.get("MaximumFractionDigits", getMaximumFractionDigits()));
        setMinimumFractionDigits(ss.get("MinimumFractionDigits", getMinimumFractionDigits()));
    }


}
