package gr.cti.eslate.utils.browser.swing;

import java.beans.*;
import java.util.*;
import java.lang.reflect.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.browser.*;

/**
 * BeanInfo for SwingBrowser.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */

public class SwingBrowserBeanInfo extends SimpleBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.utils.browser.ESlateBrowserResource",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Gets the browser's <code>EventSetDescriptor</code>s.
   * @return    An array of EventSetDescriptors describing the kinds of events
   *            fired by the browser.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    ArrayList<EventSetDescriptor> descriptors =
      new ArrayList<EventSetDescriptor>();
    EventSetDescriptor esd = null;
    try {
      Method listenerMethod, addListenerMethod, removeListenerMethod;
      MethodDescriptor md;

      // LinkFollowed

      listenerMethod = LinkFollowedListener.class.getMethod(
        "linkFollowed", new Class[] {LinkFollowedEvent.class}
      );
      addListenerMethod = SwingBrowser.class.getMethod(
        "addLinkFollowedListener", new Class[] {LinkFollowedListener.class}
      );
      removeListenerMethod = SwingBrowser.class.getMethod(
        "removeLinkFollowedListener", new Class[] {LinkFollowedListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("linkFollowed"));
      esd = new EventSetDescriptor(
        "LinkFollowed", LinkFollowedListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removeListenerMethod
      );
      descriptors.add(esd);

    } catch (Throwable th) {
      th.printStackTrace();
    }

    EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
    for (int i=0; i<d.length; i++) {
      d[i] = descriptors.get(i);
    }
    return d;
  }
}
