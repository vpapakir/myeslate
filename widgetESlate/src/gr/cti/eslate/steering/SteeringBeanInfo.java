package gr.cti.eslate.steering;

import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Steering component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class SteeringBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.steering.SteeringResource",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Construct the BeanInfo.
   */
  public SteeringBeanInfo()
  {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("steering16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(getClass().getResource("steering16m.gif"))
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
        "direction", Steering.class,
        "getDirection", "setDirection"
      );
      pd1.setDisplayName(resources.getString("setDirection"));
      pd1.setShortDescription(resources.getString("setDirectionTip"));
      pd1.setPropertyEditorClass(DirectionEditor.class);

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

}
