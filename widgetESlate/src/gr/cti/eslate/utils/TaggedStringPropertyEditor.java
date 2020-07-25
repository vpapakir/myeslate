package gr.cti.eslate.utils;

import java.beans.*;
import java.util.*;

import gr.cti.eslate.base.*;

/**
 * Custom property editor for properties that are strings that take values
 * from a specific set.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class TaggedStringPropertyEditor extends PropertyEditorSupport
{
  /**
   * The permissible strings.
   */
  private String[] strings;
  /**
   * The names associated with the permissible strings.
   */
  private String[] stringNames;
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
  String myString;
  /**
   * The name of the property for which this class is a custom editor.
   */
  String pName;

  /**
   * Construct the custom editor.
   * @param     pName   The name of the property for which this class is a
   *                    custom editor.
   * @param     strings An array containing the permissible strings.
   */
  public TaggedStringPropertyEditor(String pName, String[] strings)
  {
    this(pName, strings, strings);
  }

  /**
   * Construct the custom editor.
   * @param     pName   The name of the property for which this class is a
   *                    custom editor.
   * @param     strings An array containing the permissible strings.
   * @param     names   An array containing the corresponding names of the
   *                    permissible strings.
   */
  public TaggedStringPropertyEditor(String pName, String[] strings,
                                    String[] names)
  {
    super();
    this.pName = pName;
    this.strings = strings;
    if (names == null) {
      stringNames = new String[strings.length];
      for (int i=0; i<strings.length; i++) {
        stringNames[i] = strings[i];
      }
    }else{
      stringNames = names;
    }
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.utils.ESlateBeanUtilResources",
      ESlateMicroworld.getCurrentLocale()
    );
    pcs = new PropertyChangeSupport(this);
  }

  /**
   * Return an array of the allowable strings.
   * @return    The requested array.
   */
  public String[] getTags()
  {
    return stringNames;
  }

  /**
   * Return the current value of the property.
   * @return    The requested value.
   */
  public Object getValue()
  {
    return myString;
  }

  /**
   * Set (or change) the object that is to be edited.
   * @param     value The new target object to be edited.
   */
  public void setValue(Object value)
  {
    if (!String.class.isInstance(value)) {
      return;
    }
    if ((myString != null) && myString.equals(value)) {
      return;
    }
    String oldString = myString;
    myString = (String)value;
    pcs.firePropertyChange(
      pName, convertName(oldString), convertName(myString)
    );
  }

  /**
   * Sets the property value by parsing a given string.
   * @param     text    The string to be parsed.
   * @exception IllegalArgumentException        Thrown if either the string is
   *                    badly formatted or if this kind of property can't be
   *                    expressed as text.
   */
  public void setAsText(String text) throws IllegalArgumentException
  {
    for (int i=0; i<stringNames.length; i++) {
      if (ESlateStrings.areEqualIgnoreCase(
            stringNames[i], text, ESlateMicroworld.getCurrentLocale())){
        String oldString = myString;
        myString = strings[i];
        pcs.firePropertyChange(
          pName, convertName(oldString), convertName(myString)
        );
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
    for (int i=0; i<strings.length; i++) {
      if (myString.equals(strings[i])) {
        return stringNames[i];
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

  /**
   * Converts a string name to the corresponding string.
   * @param     name    The string name.
   * @return    The string corresponding to the given name. If the name cannot
   *            be matched, it is returned as is.
   */
  private String convertName(String name)
  {
    int n = strings.length;
    for (int i=0; i<n; i++) {
      if (stringNames[i].equals(name)) {
        return strings[i];
      }
    }
    return name;
  }
}
