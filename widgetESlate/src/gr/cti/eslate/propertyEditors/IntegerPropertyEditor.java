package gr.cti.eslate.propertyEditors;

import gr.cti.eslate.spinButton.*;
import java.awt.*;
import java.beans.*;

/**
 * Custom editor for integer properties.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class IntegerPropertyEditor extends PropertyEditorSupport
{
  /**
   * Used to implement <code>addPropertyChangeListener()</code> and
   * <code>removePropertyChangeListener()</code>.
   */
  private PropertyChangeSupport pcs;
  /**
   * The integer being edited.
   */
  private Integer integer;
  /**
   * The spin button for modifying the integer.
   */
  private SpinButton spin;
  /**
   * The name of the property being edited.
   */
  protected String propertyName = "Number";

  /**
   * Create an IntegerPropertyEditor instance.
   */
  public IntegerPropertyEditor()
  {
    super();

    pcs=new PropertyChangeSupport(this);

    spin = new SpinButton(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    //Dimension d = spin.getPreferredSize();
    //spin.setPreferredSize(new Dimension(100, d.height));
    spin.addValueChangedListener(new ValueChangedListener(){
      public void valueChanged(ValueChangedEvent e)
      {
        int n = ((Number)(spin.getValue())).intValue();
        Integer newN = new Integer(n);
        Integer oldN = integer;
        pcs.firePropertyChange(propertyName, oldN, newN);
        integer = newN;
      }
    });
  }

  /**
   * Set the minimum allowed value.
   * @param     n       The minimum allowed value.
   */
  public void setMinValue(int n)
  {
    spin.setMinimumValue(new Integer(n));
  }

  /**
   * Set the maximum allowed value.
   * @param     n       The maximum allowed value.
   */
  public void setMaxValue(int n)
  {
    spin.setMaximumValue(new Integer(n));
  }

  /**
   * Set or change the integer that is to be edited.
   * @param     value   The new value of the integer.
   */
  public void setValue(Object value)
  {
    if (value instanceof Integer) {
      Integer i = (Integer)value;
      spin.setValue(i);
      integer = i;
    }
  }

  /**
   * Return the current value of the property.
   * @return    The requested value.
   */
  public Object getValue()
  {
    return integer;
  }

  /**
   * Returns the java.awt.Component that will allow a human to directly edit
   * the current property value.
   * @return    The requested component.
   */
  public Component getCustomEditor()
  {
    return spin;
  }

  /**
   * Determines whether the propertyEditor can provide a custom editor.
   * @return    True.
   */
  public boolean supportsCustomEditor()
  {
    return true;
  }

  /**
   * Register a listener for the PropertyChange event. The class will fire a
   * PropertyChange value whenever the value is updated.
   * @param     l       An object to be invoked when a PropertyChange event is
   *                    fired.
   */
  public synchronized void addPropertyChangeListener(PropertyChangeListener l)
  {
    pcs.addPropertyChangeListener(l);
  }

  /**
   * Remove a listener for the PropertyChange event.
   * @param     l       The PropertyChange listener to be removed.
   */
  public synchronized void removePropertyChangeListener(
    PropertyChangeListener l)
  {
    pcs.removePropertyChangeListener(l);
  }
}
