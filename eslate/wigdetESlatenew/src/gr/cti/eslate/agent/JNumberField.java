package gr.cti.eslate.agent;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class JNumberField extends JTextField
{

    public JNumberField(Number number)
    {
        validChars = "0123456789.";
        setNumber(number);
    }

    public JNumberField(byte byte0)
    {
        validChars = "0123456789.";
        setNumber(byte0);
    }

    public JNumberField(double d)
    {
        validChars = "0123456789.";
        setNumber(d);
    }

    public JNumberField(float f)
    {
        validChars = "0123456789.";
        setNumber(f);
    }

    public JNumberField(int i)
    {
        validChars = "0123456789.";
        setNumber(i);
    }

    public JNumberField(long l)
    {
        validChars = "0123456789.";
        setNumber(l);
    }

    public JNumberField(short word0)
    {
        validChars = "0123456789.";
        setNumber(word0);
    }

    public JNumberField(String s)
    {
        validChars = "0123456789.";
        setNumber(s);
    }

    public void setNumber(Number number)
    {
        setText(number.toString());
    }

    public void setNumber(byte byte0)
    {
        setNumber(new Byte(byte0));
    }

    public void setNumber(double d)
    {
        setNumber(new Double(d));
    }

    public void setNumber(float f)
    {
        setNumber(new Float(f));
    }

    public void setNumber(int i)
    {
        setNumber(new Integer(i));
    }

    public void setNumber(long l)
    {
        setNumber(new Long(l));
    }

    public void setNumber(short word0)
    {
        setNumber(new Short(word0));
    }

    public void setNumber(String s)
    {
        setText(s);
    }

    public Byte getByte()
    {
        return Byte.valueOf(getText());
    }

    public Double getDouble()
    {
        return Double.valueOf(getText());
    }

    public Float getFloat()
    {
        return Float.valueOf(getText());
    }

    public Integer getInteger()
    {
        return Integer.valueOf(getText());
    }

    public int getInt() {
        try {
            return Integer.valueOf(getText()).intValue();
        } catch(Exception e) {};
        return 0;
    }

    public Long getLong()
    {
        return Long.valueOf(getText());
    }

    public Short getShort()
    {
        return Short.valueOf(getText());
    }

    public byte getbyte()
    {
        return getByte().byteValue();
    }

    public double getdouble()
    {
        return getDouble().doubleValue();
    }

    public float getfloat()
    {
        return getFloat().floatValue();
    }

    public int getinteger()
    {
        return getInteger().intValue();
    }

    public long getlong()
    {
        return getLong().longValue();
    }

    public short getshort()
    {
        return getShort().shortValue();
    }

    public void setValidChars(String s)
    {
        validChars = s;
    }

    public void setMaxDigits(int max) {
        maxDigits=max;
    }

    protected void processComponentKeyEvent(KeyEvent keyevent)
    {
        char c = keyevent.getKeyChar();

        int kc=keyevent.getKeyCode();
        //Handle arrows
        if ((kc==KeyEvent.VK_LEFT) || (kc==KeyEvent.VK_RIGHT) || (kc!=0 && kc<32))
            super.processComponentKeyEvent(keyevent);
        //Only one "."!
        else if ((c=='.') && (inString(validChars,c))) {
            if (getText().indexOf('.')!=-1)
                keyevent.consume();
            else
                super.processComponentKeyEvent(keyevent);
        //Ignore unwanted chars
        } else if (Character.isISOControl(c))
            super.processComponentKeyEvent(keyevent);
        else if (!inString(validChars, c)) {
            Toolkit.getDefaultToolkit().beep();
            keyevent.consume();
        //Or keep the "good" ones!
        } else if (getText().length()==maxDigits)
            keyevent.consume();
         else
            super.processComponentKeyEvent(keyevent);

    }

    private boolean inString(String s, char c)
    {
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i) == c)
                return true;

        return false;
    }

    public static final String OCTAL_DIGITS = "01234567";
    public static final String DECIMAL_DIGITS = "0123456789";
    public static final String HEXADECIMAL_DIGITS = "ABCDEF0123456789";
    public static final String FLOAT_DIGITS = "0123456789.";
    public static final String INTEGER_NUMBER = "0123456789+-";
    public static final String FLOAT_NUMBER = "0123456789.+-";
    private String validChars;
    private int maxDigits=100;
}
