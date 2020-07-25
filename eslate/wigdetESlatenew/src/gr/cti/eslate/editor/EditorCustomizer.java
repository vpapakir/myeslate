package gr.cti.eslate.editor;

import java.beans.*;
import java.util.*;
import javax.swing.*;

/**
 * Customizer for the Editor component.
 *
 * @version     2.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class EditorCustomizer extends JTabbedPane implements Customizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  ///**
  // * The editor component that is being customized.
  // */
  //private Editor editor = null;
  /**
   * Localized resources.
   */
  private ResourceBundle resources;
  ///**
  // * The property change listeners.
  // */
  //private ArrayList listeners = new ArrayList();

  /**
   * Create a customizer.
   */
  public EditorCustomizer()
  {
    super();
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.editor.EditorResource", Locale.getDefault()
    );
    add(
      resources.getString("plain"),
      new KitCustomizer(gr.cti.eslate.editor.PlainKit.class)
    );
    add(
      resources.getString("java"),
      new KitCustomizer(gr.cti.eslate.editor.JavaKit.class)
    );
  }

  /**
   * Not used.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
  }

  /**
   * Not used.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
  }

  /**
   * Set the object to be customized. This method should be called only once,
   * before the Customizer has been added to any parent AWT container.
   * @param     bean    The object to be customized.
   */
  public void setObject(Object bean)
  {
    //if (bean instanceof Editor) {
    //  editor = (Editor)bean;
    //}
  }
}
