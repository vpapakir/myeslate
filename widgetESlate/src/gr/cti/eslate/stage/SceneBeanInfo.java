package gr.cti.eslate.stage;

import java.beans.*;

/**
 * BeanInfo for the Scene component.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class SceneBeanInfo extends SimpleBeanInfo
{
  /**
   * Gets the bean's <code>PropertyDescriptor</code>s.
   * @return    An array of PropertyDescriptors describing the editable
   *            properties supported by this bean.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    // Access the scene's properties via the parent Stage component.
    return new PropertyDescriptor[0];
  }

  /**
   * Gets the bean's <code>EventSetDescriptor</code>s.
   * @return    An array of EventSetDescriptors describing the kinds of events
   *            fired by this bean.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    // Access the scene's events via the parent Stage component.
    return new EventSetDescriptor[0];
  }
}
