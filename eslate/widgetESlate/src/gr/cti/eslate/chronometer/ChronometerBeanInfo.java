package gr.cti.eslate.chronometer;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Chronometer component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ChronometerBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.chronometer.ChronometerResource",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Construct the BeanInfo.
   */
  public ChronometerBeanInfo()
  {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("images/chronometer16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(getClass().getResource("images/chronometer16m.gif"))
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the bean's <code>PropertyDescriptor</code>s.
   * @return    An array of PropertyDescriptors describing the editable
   *            properties supported by this bean.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    PropertyDescriptor[] defaultProperties = super.getPropertyDescriptors();
      
    try {
      PropertyDescriptor pd1 = new PropertyDescriptor(
        "milliseconds", Chronometer.class,
        "getMilliseconds", "setMilliseconds"
      );
      pd1.setDisplayName(resources.getString("setMilliseconds"));
      pd1.setShortDescription(resources.getString("setMillisecondsTip"));

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 1];
      for (int i=0; i<defaultProperties.length; i++) {
        pd[i] = defaultProperties[i];
      }
      int i = defaultProperties.length;
      pd[i++] = pd1;
      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultProperties;
    }
  }

  /**
   * Gets the bean's <code>EventSetDescriptor</code>s.
   * @return    An array of EventSetDescriptors describing the kinds of events
   *            fired by this bean.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    EventSetDescriptor[] defaultDescriptors = super.getEventSetDescriptors();

    try {
      Method listenerMethod1 = ChronometerListener.class.getMethod(
        "chronometerStarted", new Class[] {ChronometerEvent.class}
      );
      Method listenerMethod2 = ChronometerListener.class.getMethod(
        "chronometerStopped", new Class[] {ChronometerEvent.class}
      );
      Method listenerMethod3 = ChronometerListener.class.getMethod(
        "valueChanged", new Class[] {ChronometerEvent.class}
      );
      Method addListenerMethod = Chronometer.class.getMethod(
        "addChronometerListener", new Class[] {ChronometerListener.class}
      );
      Method removeListenerMethod = Chronometer.class.getMethod(
        "removeChronometerListener", new Class[] {ChronometerListener.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("chronometerStarted"));
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("chronometerStopped"));
      MethodDescriptor md3 = new MethodDescriptor(listenerMethod3);
      md3.setDisplayName(resources.getString("valueChanged"));
      EventSetDescriptor esd1 = new EventSetDescriptor(
        "chronometer", ChronometerListener.class,
        new MethodDescriptor[] {md1, md2, md3},
        addListenerMethod, removeListenerMethod
      );

      EventSetDescriptor[] esd =
        new EventSetDescriptor[defaultDescriptors.length + 1];
      for (int i=0; i<defaultDescriptors.length; i++) {
        esd[i] = defaultDescriptors[i];
      }
      int i = defaultDescriptors.length;
      esd[i++] = esd1;
      return esd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultDescriptors;
    }
  }
}
