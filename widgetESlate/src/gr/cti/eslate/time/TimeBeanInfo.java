package gr.cti.eslate.time;

import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Time component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class TimeBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.time.TimeResource",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Construct the BeanInfo.
   */
  public TimeBeanInfo()
  {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("time16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(getClass().getResource("time16m.gif"))
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
        "time", Time.class,
        "getTime", "setTime"
      );
      pd1.setDisplayName(resources.getString("setTime"));
      pd1.setShortDescription(resources.getString("setTimeTip"));

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "stopAtLandmarks", Time.class,
        "getStopAtLandmarks", "setStopAtLandmarks"
      );
      pd2.setDisplayName(resources.getString("setStopAtLandmarks"));
      pd2.setShortDescription(resources.getString("setStopAtLandmarksTip"));

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "unit", Time.class,
        "getUnit", "setUnit"
      );
      pd3.setDisplayName(resources.getString("setUnit"));
      pd3.setShortDescription(resources.getString("setUnitTip"));
      pd3.setPropertyEditorClass(UnitEditor.class);

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 3];
      for (int i=0; i<defaultProperties.length; i++) {
        pd[i] = defaultProperties[i];
      }
      int i = defaultProperties.length;
      pd[i++] = pd1;
      pd[i++] = pd2;
      pd[i++] = pd3;
      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultProperties;
    }
  }

}
