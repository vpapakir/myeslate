package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.beans.*;
import javax.swing.*;

/**
 * A label representing a class.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 */
class ClassLabel extends JLabel implements Comparable<ClassLabel>
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The class for the label.
   */
  Class<?> cls;

  /**
   * Construct a label.
   * @param     cl      The class for the label.
   * @param     name    The text for the label.
   */
  ClassLabel(Class<?> cl, String name)
  {
    super(name);
    cls = cl;
    setOpaque(true);
    try {
      BeanInfo bi = Introspector.getBeanInfo(cl);
      Image image = bi.getIcon(BeanInfo.ICON_COLOR_16x16);
      if (image == null) {
        image = bi.getIcon(BeanInfo.ICON_MONO_16x16);
      }
      if (image != null) {
        setIcon(new ImageIcon(image));
      }
    }catch (Exception e) {
    }
  }

  /**
   * Compares this object with the specified <code>ClassLabel</code> object
   * for order.
   * @param     lab     The <code>ClassLabel</code> object with which to
   *                    compare this object.
   * @return    A negative integer, zero, or a positive integer as this object
   *            is less than, equal to, or greater than the specified object.
   */
  public int compareTo(ClassLabel lab)
  {
    return getText().compareTo(lab.getText());
  }
}
