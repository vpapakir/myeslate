package gr.cti.eslate.sharedObject;

import java.util.*;
import java.text.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;

/**
 * This class implements a shared object that encapsulates a number.
 *
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class NumberSO extends SharedObject
{
  /**
   * The encapsulated number.
   */
  private Number num;
  /**
   * The number format to use when displaying the encapsulated number.
   */
  private NumberFormat nf = null;

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     n       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, Number n)
  {
    super(handle);
    num = n;
  }

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     b       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, byte b)
  {
    super(handle);
    num = new Byte(b);
  }

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     d       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, double d)
  {
    super(handle);
    num = new Double(d);
  }

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     f       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, float f)
  {
    super(handle);
    num = new Float(f);
  }

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     i       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, int i)
  {
    super(handle);
    num = new Integer(i);
  }

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     l       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, long l)
  {
    super(handle);
    num = new Long(l);
  }

  /**
   * Creates a NumberSO shared object.
   * @param     handle  The E-Slate handle of the component to which the
   *                    shared object belongs.
   * @param     s       The number encapsulated by the shared object.
   */
  public NumberSO(ESlateHandle handle, short s)
  {
    super(handle);
    num = new Short(s);
  }

  /**
   * Returns the value of the encapsulated number as a Number.
   * @return    The requested Number.
   */
  public Number value()
  {
    return num;
  }

  /**
   * Returns the value of the encapsulated number as a byte.
   * @return    The requested byte.
   */
  public byte byteValue()
  {
    return num.byteValue();
  }

  /**
   * Returns the value of the encapsulated number as a double.
   * @return    The requested double.
   */
  public double doubleValue()
  {
    return num.doubleValue();
  }

  /**
   * Returns the value of the encapsulated number as a float.
   * @return    The requested float.
   */
  public float floatValue()
  {
    return num.floatValue();
  }

  /**
   * Returns the value of the encapsulated number as an int.
   * @return    The requested int.
   */
  public int intValue()
  {
    return num.intValue();
  }

  /**
   * Returns the value of the encapsulated number as a long.
   * @return    The requested long.
   */
  public long longValue()
  {
    return num.longValue();
  }

  /**
   * Returns the value of the encapsulated number as a short.
   * @return    The requested short.
   */
  public short shortValue()
  {
    return num.shortValue();
  }

  /**
   * Returns a string representation of the encapsulated number.
   * @return    The requested string.
   */
  public String toString()
  {
    if (nf != null) {
      if (num instanceof Byte || num instanceof Integer ||
          num instanceof Short || num instanceof Long) {
        return nf.format(num.longValue());
      }else{
        return nf.format(num.doubleValue());
      }
    }else{
      return num.toString();
    }
  }

  /**
   * Set the value of the encapsulated number.
   * @param     num     The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  @SuppressWarnings(value={"deprecation"})
  public void setValue(Number num, Vector path)
  {
    if (areDifferent(this.num, num)) {
      this.num = num;

      // Create an event.
      SharedObjectEvent soe = new SharedObjectEvent(this, path);

      fireSharedObjectChanged(soe);     // Notify the listeners.
    }
  }

  /**
   * Set the value of the encapsulated number.
   * @param     num     The new value of the encapsulated number.
   */
  public void setValue(Number num)
  {
    if (areDifferent(this.num, num)) {
      this.num = num;

      // Create an event.
      SharedObjectEvent soe = new SharedObjectEvent(this);

      fireSharedObjectChanged(soe);     // Notify the listeners.
    }
  }

  /**
   * Set the value of the encapsulated number.
   * @param     b       The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  public void setValue(byte b, Vector path)
  {
    setValue(new Byte(b), path);
  }

  /**
   * Set the value of the encapsulated number.
   * @param     b       The new value of the encapsulated number.
   */
  public void setValue(byte b)
  {
    setValue(new Byte(b));
  }

  /**
   * Set the value of the encapsulated number.
   * @param     d       The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  public void setValue(double d, Vector path)
  {
    setValue(new Double(d), path);
  }

  /**
   * Set the value of the encapsulated number.
   * @param     d       The new value of the encapsulated number.
   */
  public void setValue(double d)
  {
    setValue(new Double(d));
  }

  /**
   * Set the value of the encapsulated number.
   * @param     f       The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  public void setValue(float f, Vector path)
  {
    setValue(new Float(f), path);
  }

  /**
   * Set the value of the encapsulated number.
   * @param     f       The new value of the encapsulated number.
   */
  public void setValue(float f)
  {
    setValue(new Float(f));
  }

  /**
   * Set the value of the encapsulated number.
   * @param     i       The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  public void setValue(int i, Vector path)
  {
    setValue(new Integer(i), path);
  }

  /**
   * Set the value of the encapsulated number.
   * @param     i       The new value of the encapsulated number.
   */
  public void setValue(int i)
  {
    setValue(new Integer(i));
  }

  /**
   * Set the value of the encapsulated number.
   * @param     l       The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  public void setValue(long l, Vector path)
  {
    setValue(new Long(l), path);
  }

  /**
   * Set the value of the encapsulated number.
   * @param     l       The new value of the encapsulated number.
   */
  public void setValue(long l)
  {
    setValue(new Long(l));
  }

  /**
   * Set the value of the encapsulated number.
   * @param     s       The new value of the encapsulated number.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  public void setValue(short s, Vector path)
  {
    setValue(new Short(s), path);
  }

  /**
   * Set the value of the encapsulated number.
   * @param     s       The new value of the encapsulated number.
   */
  public void setValue(short s)
  {
    setValue(new Short(s));
  }

  /**
   * Set the number format to use when displaying the encapsulated number.
   * @param     nf      The number format to use.
   */
  public void setNumberFormat(NumberFormat nf)
  {
    if (areDifferent(this.nf, nf)) {
      this.nf = nf;

      // Create an event.
      SharedObjectEvent soe = new SharedObjectEvent(this);

      fireSharedObjectChanged(soe);     // Notify the listeners.
    }
  }

  /**
   * Set the number format to use when displaying the encapsulated number.
   * @param     nf      The number format to use.
   * @param     path    Change propagation path, used when transferring
   *                    a change from the input part of a pin to its output
   *                    part.
   */
  @SuppressWarnings(value={"deprecation"})
  public void setNumberFormat(NumberFormat nf, Vector path)
  {
    if (areDifferent(this.nf, nf)) {
      this.nf = nf;

      // Create an event.
      SharedObjectEvent soe = new SharedObjectEvent(this, path);

      fireSharedObjectChanged(soe);     // Notify the listeners.
    }
  }

  /**
   * Return the number format to use when displaying the encapsulated number.
   * @return    The requested number format.
   */
  public NumberFormat getNumberFormat()
  {
    return nf;
  }
}
