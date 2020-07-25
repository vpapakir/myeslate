package gr.cti.eslate.masterclock;

import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Master Clock component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 30-May-2006
 */
public class MasterClockBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.masterclock.MasterClockResource",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Construct the BeanInfo.
   */
  public MasterClockBeanInfo()
  {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("images/masterclock16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(getClass().getResource("images/masterclock16m.gif"))
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
        "setMinimumTimeScale", MasterClock.class,
        "getMinimumTimeScale", "setMinimumTimeScale"
      );
      pd1.setDisplayName(resources.getString("setMinimumTimeScale"));
      pd1.setShortDescription(resources.getString("setMinimumTimeScaleTip"));

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "setMaximumTimeScale", MasterClock.class,
        "getMaximumTimeScale", "setMaximumTimeScale"
      );
      pd2.setDisplayName(resources.getString("setMaximumTimeScale"));
      pd2.setShortDescription(resources.getString("setMaximumTimeScaleTip"));

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "setTimeScale", MasterClock.class,
        "getTimeScale", "setTimeScale"
      );
      pd3.setDisplayName(resources.getString("setTimeScale"));
      pd3.setShortDescription(resources.getString("setTimeScaleTip"));

      PropertyDescriptor pd4 = new PropertyDescriptor(
        "startStop", MasterClock.class,
        "isSaveStartStop", "setSaveStartStop"
      );
      pd4.setDisplayName(resources.getString("startStop"));
      pd4.setShortDescription(resources.getString("startStopTip"));

      PropertyDescriptor pd5 = new PropertyDescriptor(
        "setSleepInterval", MasterClock.class,
        "getSleepInterval", "setSleepInterval"
      );
      pd5.setDisplayName(resources.getString("setSleepInterval"));
      pd5.setShortDescription(resources.getString("setSleepIntervalTip"));
      pd5.setExpert(true);

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 5];
      for (int i=0; i<defaultProperties.length; i++) {
        pd[i] = defaultProperties[i];
      }
      int i = defaultProperties.length;
      pd[i++] = pd1;
      pd[i++] = pd2;
      pd[i++] = pd3;
      pd[i++] = pd4;
      pd[i++] = pd5;
      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultProperties;
    }
  }

}
