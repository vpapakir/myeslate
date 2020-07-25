package gr.cti.eslate.utils.browser.SWT;

import java.beans.*;
import java.util.*;
import java.lang.reflect.*;

import org.eclipse.swt.browser.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.browser.*;

/**
 * BeanInfo for IEBrowser.
 *
 * @version     2.0.12, 31-Oct-2006
 * @author	Kriton Kyrimis
 */

public class IEBrowserBeanInfo extends SimpleBeanInfo
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
   * @return	An array of EventSetDescriptors describing the kinds of events
   *		fired by the browser.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    ArrayList<EventSetDescriptor> descriptors =
      new ArrayList<EventSetDescriptor>();
    EventSetDescriptor esd = null;
    try {
      Method listenerMethod;
      Method listenerMethod2;
      Method addListenerMethod;
      Method removeListenerMethod;
      MethodDescriptor md;
      MethodDescriptor md2;

      // HistoryChanged

      listenerMethod = HistoryChangeListener.class.getMethod(
        "historyChanged", new Class[] {HistoryChangedEvent.class}
      );
      addListenerMethod = IEBrowser.class.getMethod(
        "addHistoryChangeListener", new Class[] {HistoryChangeListener.class}
      );
      removeListenerMethod = IEBrowser.class.getMethod(
        "removeHistoryChangeListener", new Class[] {HistoryChangeListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("historyChanged"));
      esd = new EventSetDescriptor(
        "HistoryChange", HistoryChangeListener.class,
	new MethodDescriptor[] {md}, addListenerMethod, removeListenerMethod
      );
      descriptors.add(esd);


      // LinkFollowed

      listenerMethod = LinkFollowedListener.class.getMethod(
        "linkFollowed", new Class[] {LinkFollowedEvent.class}
      );
      addListenerMethod = IEBrowser.class.getMethod(
        "addLinkFollowedListener", new Class[] {LinkFollowedListener.class}
      );
      removeListenerMethod = IEBrowser.class.getMethod(
        "removeLinkFollowedListener", new Class[] {LinkFollowedListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("linkFollowed"));
      esd = new EventSetDescriptor(
        "LinkFollowed", LinkFollowedListener.class,
	new MethodDescriptor[] {md}, addListenerMethod, removeListenerMethod
      );
      descriptors.add(esd);


      // Location

      listenerMethod = LocationListener.class.getMethod(
        "changed", new Class[] {LocationEvent.class}
      );
      addListenerMethod = IEBrowser.class.getMethod(
        "addLocationListener", new Class[] {LocationListener.class}
      );
      removeListenerMethod = IEBrowser.class.getMethod(
        "removeLocationListener", new Class[] {LocationListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("changedLocation"));
      listenerMethod2 = LocationListener.class.getMethod(
        "changing", new Class[] {LocationEvent.class}
      );
      md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("changingLocation"));
      esd = new EventSetDescriptor(
        "Location", LocationListener.class,
	new MethodDescriptor[] {md, md2},
        addListenerMethod, removeListenerMethod
      );
      descriptors.add(esd);


      // Title

      listenerMethod = TitleListener.class.getMethod(
        "changed", new Class[] {TitleEvent.class}
      );
      addListenerMethod = IEBrowser.class.getMethod(
        "addTitleListener", new Class[] {TitleListener.class}
      );
      removeListenerMethod = IEBrowser.class.getMethod(
        "removeTitleListener", new Class[] {TitleListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("changedTitle"));
      esd = new EventSetDescriptor(
        "Title", TitleListener.class,
	new MethodDescriptor[] {md},
        addListenerMethod, removeListenerMethod
      );
      descriptors.add(esd);


      // StatusText

      listenerMethod = StatusTextListener.class.getMethod(
        "changed", new Class[] {StatusTextEvent.class}
      );
      addListenerMethod = IEBrowser.class.getMethod(
        "addStatusTextListener", new Class[] {StatusTextListener.class}
      );
      removeListenerMethod = IEBrowser.class.getMethod(
        "removeStatusTextListener", new Class[] {StatusTextListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("changedStatusText"));
      esd = new EventSetDescriptor(
        "StatusText", StatusTextListener.class,
	new MethodDescriptor[] {md},
        addListenerMethod, removeListenerMethod
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
