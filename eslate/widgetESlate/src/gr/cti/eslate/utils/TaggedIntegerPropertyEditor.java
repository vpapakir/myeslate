package gr.cti.eslate.utils;

import java.beans.*;
import java.util.*;

import gr.cti.eslate.base.*;

/**
 * Custom property editor for properties that are integers that take values
 * from a specific set.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class TaggedIntegerPropertyEditor extends PropertyEditorSupport
{
  /**
   * The permissible integers.
   */
  private int[] ints;
  /**
   * The names associated with the permissible integers.
   */
  private String[] intNames;
  /**
   * Localized resources.
   */
  private ResourceBundle resources;
  /**
   * Property change support.
   */
  private PropertyChangeSupport pcs;
  /**
   * The current string.
   */
  Integer myInt;
  /**
   * The name of the property for which this class is a custom editor.
   */
  String pName;

  /**
   * Construct the custom editor.
   * @param     pName   The name of the property for which this class is a
   *                    custom editor.
   * @param     ints    An array containing the permissible integers.
   * @param     names   An array containing the corresponding names of the
   *                    permissible integers.
   */
  public TaggedIntegerPropertyEditor(String pName, int[] ints, String[] names)
  {
    super();
    this.pName = pName;
    this.ints = ints;
    if (names == null) {
      intNames = new String[ints.length];
      for (int i=0; i<ints.length; i++) {
        intNames[i] = "" + ints[i];
      }
    }else{
      intNames = names;
    }
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.utils.ESlateBeanUtilResources",
      ESlateMicroworld.getCurrentLocale()
    );
    pcs = new PropertyChangeSupport(this);
  }

  /**
   * Construct the custom editor.
   * @param     pName   The name of the property for which this class is a
   *                    custom editor.
   * @param     ints    An array containing the permissible integers.
   */
  public TaggedIntegerPropertyEditor(String pName, int[] ints)
  {
    this(pName, ints, null);
  }

  /**
   * Return an array of the allowable strings.
   * @return    The requested array.
   */
  public String[] getTags()
  {
    return intNames;
  }

  /**
   * Return the current value of the property.
   * @return    The requested value.
   */
  public Object getValue()
  {
    return myInt;
  }

  /**
   * Set (or change) the object that is to be edited.
   * @param     value The new target object to be edited.
   */
  public void setValue(Object value)
  {
    if (!Integer.class.isInstance(value)) {
      return;
    }
    if ((myInt != null) && myInt.equals(value)) {
      return;
    }
    Integer oldInt = myInt;
    myInt = (Integer)value;
    pcs.firePropertyChange(pName, oldInt, myInt);
  }

  /**
   * Sets the property value by parsing a given String.
   * @param     text    The string to be parsed.
   * @exception IllegalArgumentException        Thrown if either the String is
   *                    badly formatted or if this kind of property can't be
   *                    expressed as text.
   */
  public void setAsText(String text) throws IllegalArgumentException
  {
    for (int i=0; i<intNames.length; i++) {
      if (ESlateStrings.areEqualIgnoreCase(
            intNames[i], text, ESlateMicroworld.getCurrentLocale())){
        Integer oldInt = myInt;
        myInt = new Integer(ints[i]);
        pcs.firePropertyChange(pName, oldInt, myInt);
        return;
      }
    }
    throw new IllegalArgumentException(resources.getString("illegalValue"));
  }

  /**
   * Gets the property value as a string suitable for presentation to a human
   * to edit.
   * @return    The requested value.
   */
  public String getAsText()
  {
    int num = myInt.intValue();
    for (int i=0; i<ints.length; i++) {
      if (num == ints[i]) {
        return intNames[i];
      }
    }
    return "";
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
  public synchronized void
    removePropertyChangeListener(PropertyChangeListener l)
  {
    pcs.removePropertyChangeListener(l);
  }
}
