package gr.cti.eslate.navigator.suppliers;

import java.beans.*;
import java.lang.reflect.*;

/**
 * BeanInfo for SwingNavigator.
 *
 * @version     3.0.0, 2-Jun-2006
 * @author      Kriton Kyrimis
 */

public class SwingNavigatorBeanInfo extends SimpleBeanInfo
{
  /**
   * Gets the navigator's <code>PropertyDescriptor</code>s.
   * @return    An array of PropertyDescriptors describing the editable
   *            properties supported by this bean.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    PropertyDescriptor pd = null;
    try {
      Method getter = SwingNavigator.class.getMethod(
        "getESlateBrowser", new Class[] {}
      );
      pd = new PropertyDescriptor("ESlateBrowser", getter, null);
    } catch (Throwable th) {
      th.printStackTrace();
    }
    PropertyDescriptor[] p = new PropertyDescriptor[1];
    p[0] = pd;
    return p;
  }
}
