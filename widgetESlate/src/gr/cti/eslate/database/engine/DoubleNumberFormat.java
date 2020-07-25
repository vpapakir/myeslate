package gr.cti.eslate.database.engine;

import java.lang.Double;
import java.lang.Long;
import java.lang.String;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;


/**
 * @version	2.0, May 01
 */
public class DoubleNumberFormat implements Serializable, Cloneable {
    /** The version of the storage format of the DoubleNumberFormat class
     */
//    public static final String STR_FORMAT_VERSION = "1.0";
    public static final int FORMAT_VERSION = 2;

    char decimalSeparator;
    char thousandSeparator;
    boolean thousandSeparatorUsed;
    boolean decimalSeparatorAlwaysShown;
    boolean exponentialFormatUsed;
    boolean showIntegerPartOnly;
    static final long serialVersionUID = 12;
    static NumberFormat englishNumberFormat = NumberFormat.getInstance(Locale.ENGLISH);

    public DoubleNumberFormat() {
        decimalSeparator = ',';
        thousandSeparator = '.';
        thousandSeparatorUsed = false;
        decimalSeparatorAlwaysShown = false;
        exponentialFormatUsed = false;
        showIntegerPartOnly = false;
    }

    public final Number parse(String string) throws DoubleNumberFormatParseException {
        string = string.trim();
        if (string.length() == 0)
            return null;
        String stringBackUp = new String(string);

        char[] str = new char[string.length()];
        int k = 0;
        char c;
        /* Get rid of the thousand separators.
         */
        for (int i=0; i<string.length(); i++) {
            c = string.charAt(i);
            if (c != thousandSeparator)
                str[i-k] = c;
            else
                k++;
        }

        string = new String(str, 0, string.length()-k);
//        System.out.println("Without thousand separators: " + string);

        if (decimalSeparator != '.' && string.indexOf('.') != -1)
            throw new DoubleNumberFormatParseException("\"" + stringBackUp + DBase.resources.getString("CDoubleNumberFormatMsg1"));


        /* Check if there exist more than one decimal separators ('.'). If yes, throw
         * an exception. If no, replace the separator, if it exists and create the double.
         */
        if ((k = string.indexOf(decimalSeparator)) != -1) {
            if (k < string.length()-1 && (string.indexOf(k+1, decimalSeparator) != -1))
                throw new DoubleNumberFormatParseException("\"" + stringBackUp + DBase.resources.getString("CDoubleNumberFormatMsg1"));
            else
                string = string.replace(decimalSeparator, '.');
        }

        System.out.println("Turning to double string: " + string);
        try{
//            System.out.println("Produced double: " + new Double(string));
            return englishNumberFormat.parse(string);
//            return new Double(string);
        }catch (ParseException e) {
            throw new DoubleNumberFormatParseException("\"" + stringBackUp + DBase.resources.getString("CDoubleNumberFormatMsg1"));
        }catch (NumberFormatException e) {
            e.printStackTrace();
            throw new DoubleNumberFormatParseException("\"" + stringBackUp + DBase.resources.getString("CDoubleNumberFormatMsg1"));
        }

    }

    public final String format(Number num) {
        String string = num.toString();
//        System.out.println("1. string: " + string);

        int Eindex;
        if (!exponentialFormatUsed) {
            if ((Eindex = string.indexOf('E')) == -1) {
//                string = string.replace('.', decimalSeparator);
                StringBuffer str = new StringBuffer(string);
                int decimalIndex = string.indexOf('.');
                int separatorsInserted = 0;
                /* If thousandSeparators are used, insert them.
                 */
                if (thousandSeparatorUsed) {
                    int k = decimalIndex;
                    k=k-3;
                    int firstDigitPos = (num.doubleValue() < 0)? 1:0;
                    while (k > firstDigitPos) {
                        str.insert(k, thousandSeparator);
                        k = k-3;
                        separatorsInserted++;
//                        System.out.println(str);
                    }
                }

                if (!showIntegerPartOnly) {
                    /* Replace the '.' with the "decimalSeparator".
                     */
                    str.setCharAt(decimalIndex+separatorsInserted, decimalSeparator);
                }else
                    return str.toString().substring(0, decimalIndex+separatorsInserted);

                /* If "decimalIndexAlwaysUsed" is reset, then leave off the standard for integer
                 * values ",0". For non-integer values, do nothing.
                 */
                if (!decimalSeparatorAlwaysShown) {
                    string = str.toString();
//                    System.out.println("Fraction: " + string.substring(decimalIndex+separatorsInserted+1));
                    double franction = new Double(string.substring(decimalIndex+separatorsInserted+1)).doubleValue();
                    if (franction == 0)
                        string = string.substring(0, decimalIndex+separatorsInserted);
                    return string;
                }

//                System.out.println(str);
                return str.toString();
            }else{
                StringBuffer base;
                /* Calculate the exponent.
                 */
                int exponent = new Integer(string.substring(Eindex+1)).intValue();
//                System.out.println("Number: " + string + ", Exponent: " + exponent);
                int decimalIndex = string.indexOf('.');
//                System.out.println("decimalIndex: " + decimalIndex);
                /* If there exists a decimal separator ('.'), then remove it totally from the
                 * base.
                 */
                if (decimalIndex != -1) {
                    base = new StringBuffer(string.substring(0, decimalIndex));
                    if (decimalIndex + 1 < Eindex)
                        base.append(string.substring(decimalIndex+1, Eindex));
                }else
                    base = new StringBuffer(string.substring(0, Eindex));

//                System.out.println("base1: " + base + "  length: " + base.length());

                // Positive exponentials
                if (exponent >= 0) {
                    int i;
                    for (i=decimalIndex; i<base.length() && exponent > 0; i++)
                        exponent--;

//                    System.out.println("exponent: " + exponent + " i: " + i);

                    if (exponent == 0) {
                        if (i != base.length()) {
                            base.insert(i, decimalSeparator);
                            decimalIndex = i;
                        }else
                            decimalIndex = -1;
                    }else
                        decimalIndex = -1;

//                    System.out.println("base2: " + base);

                    for (i=0; i<exponent; i++)
                        base.append('0');
//                    System.out.println("base3: " + base);
                }else{
                    // Negative exponentials
                    int i;
                    for (i=decimalIndex; i>=0 && exponent < 0; i--)
                        exponent++;

//                    System.out.println("exponent: " + exponent + " i: " + i);

                    for (i=exponent; i<=0; i++)
                        base.insert(0, '0');

                    base.insert(0, decimalSeparator);
                    base.insert(0, '0');
                    decimalIndex = 1;
//                    System.out.println("base3: " + base);
                }

                if (thousandSeparatorUsed) {
                    int k = decimalIndex == -1? base.length():decimalIndex;
                    k=k-3;
                    while (k > 0) {
                        base.insert(k, thousandSeparator);
                        k = k-3;
                    }
                }

                if (showIntegerPartOnly) {
                    if (decimalIndex != -1) {
                        String s = base.toString();
                        decimalIndex = s.indexOf(decimalSeparator);
                        return s.substring(0, decimalIndex);
                    }else
                        return base.toString();
                }

                if (decimalSeparatorAlwaysShown && decimalIndex == -1) {
                    base.append(decimalSeparator);
                    base.append('0');
                }

//                System.out.println("base4: " + base);
                return base.toString();
            }
        }else{
            /* Create exponential format of numeric values.
             */
            if (string.indexOf('E') != -1) { //Already in exponential format
                /* Replace the decimal character (standard '.'), with the
                 * currently used decimal character.
                 */
                int decimalIndex = string.indexOf('.');
                StringBuffer sb = new StringBuffer(string);
                sb.setCharAt(decimalIndex, decimalSeparator);
                return sb.toString();
            }

            int decimalIndex = string.indexOf('.');
//            System.out.println("string: " + string + ", decimalIndex: " + decimalIndex);
            String fractionPart = string.substring(decimalIndex+1);
            double franction = new Double(fractionPart).doubleValue();
            if (franction == 0) {
                string = string.substring(0, decimalIndex);
                StringBuffer sb = new StringBuffer(string);
                if (string.length() > 1)
                    sb.insert(1, decimalSeparator);
                sb.append("E");
                sb.append(string.length()-1);
                if (decimalSeparatorAlwaysShown) {
                    if (string.length() == 1)
                        sb.insert(1, decimalSeparator);
                    if (sb.charAt(2) == 'E')
                        sb.insert(2, '0');
                }
//                System.out.println("sb: " + sb);
                return sb.toString();
            }else{
                string = string.substring(0, decimalIndex) + fractionPart;
                StringBuffer sb = new StringBuffer(string);
                sb.append("E");
                sb.append(decimalIndex-1);
                sb.insert(1, decimalSeparator);
//                System.out.println("sb: " + sb);
                return sb.toString();
            }
        }

    }

    public char getDecimalSeparator() {
        return decimalSeparator;
    }

    public char getThousandSeparator() {
        return thousandSeparator;
    }

    public void setThousandSeparator(char c) {
        thousandSeparator = c;
    }

    public void setDecimalSeparator(char c) {
        decimalSeparator = c;
    }

    public boolean isThousandSeparatorUsed() {
        return thousandSeparatorUsed;
    }

    public void setThousandSeparatorUsed(boolean use) {
        thousandSeparatorUsed = use;
    }

    public void setDecimalSeparatorAlwaysShown(boolean always) {
        decimalSeparatorAlwaysShown = always;
    }

    public boolean isDecimalSeparatorAlwaysShown() {
        return decimalSeparatorAlwaysShown;
    }

    public void setShowIntegerPartOnly(boolean integerPartOnly) {
        showIntegerPartOnly = integerPartOnly;
    }

    public boolean isShowIntegerPartOnly() {
        return showIntegerPartOnly;
    }

    public void setExponentialFormatUsed(boolean use) {
        exponentialFormatUsed = use;
    }

    public boolean isExponentialFormatUsed() {
        return exponentialFormatUsed;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Decimal separator", new Character(decimalSeparator));
        fieldMap.put("Thousand separator", new Character(thousandSeparator));
        fieldMap.put("Thousand separator used", thousandSeparatorUsed);
        fieldMap.put("Decimal separator always shown", decimalSeparatorAlwaysShown);
        fieldMap.put("Exponential format used", exponentialFormatUsed);
        fieldMap.put("Show integer part only", showIntegerPartOnly);

        out.writeObject(fieldMap);

/*        out.writeObject(new Character(decimalSeparator));
        out.writeObject(new Character(thousandSeparator));
        out.writeObject(new Boolean(thousandSeparatorUsed));
        out.writeObject(new Boolean(decimalSeparatorAlwaysShown));
        out.writeObject(new Boolean(exponentialFormatUsed));
        out.writeObject(new Boolean(showIntegerPartOnly));
*/
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            StorageStructure fieldMap = (StorageStructure) firstObj;
            decimalSeparator = ((Character) fieldMap.get("Decimal separator")).charValue();
            thousandSeparator = ((Character) fieldMap.get("Thousand separator")).charValue();
            thousandSeparatorUsed = fieldMap.get("Thousand separator used", true);
            decimalSeparatorAlwaysShown = fieldMap.get("Decimal separator always shown", true);
            exponentialFormatUsed = fieldMap.get("Exponential format used", false);
            showIntegerPartOnly = fieldMap.get("Show integer part only", false);
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException {
        System.out.println("DoubleNumberFormat oldReadObject()");
        try{
            decimalSeparator = ((Character) firstObj).charValue(); //in.readObject()).charValue();
            thousandSeparator = ((Character) in.readObject()).charValue();
            thousandSeparatorUsed = ((Boolean) in.readObject()).booleanValue();
            decimalSeparatorAlwaysShown = ((Boolean) in.readObject()).booleanValue();
            exponentialFormatUsed = ((Boolean) in.readObject()).booleanValue();
            showIntegerPartOnly = ((Boolean) in.readObject()).booleanValue();
        }catch (Exception e) {
//            System.out.println(e.getClass() + ", " + e.getMessage());
            throw new IOException(e.getMessage());
        }
    }

    public Object clone() {
        Object o = null;
        try{
            o = super.clone();
        }catch (CloneNotSupportedException exc) {}
        return o;
    }

}