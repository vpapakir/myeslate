package gr.cti.eslate.vector;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Vector component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 14-Nov-2006
 */
public class VectorComponentBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.vector.VectorResource",
    ESlateMicroworld.getCurrentLocale()
  );

  /**
   * Construct the BeanInfo.
   */
  public VectorComponentBeanInfo()
  {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("vector16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(getClass().getResource("vector16m.gif"))
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
        "east", VectorComponent.class,
        "getEast", "setEast"
      );
      pd1.setDisplayName(resources.getString("setEast"));
      pd1.setShortDescription(resources.getString("setEastTip"));

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "north", VectorComponent.class,
        "getNorth", "setNorth"
      );
      pd2.setDisplayName(resources.getString("setNorth"));
      pd2.setShortDescription(resources.getString("setNorthTip"));

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "length", VectorComponent.class,
        "getLength", "setLength"
      );
      pd3.setDisplayName(resources.getString("setLength"));
      pd3.setShortDescription(resources.getString("setLengthTip"));

      PropertyDescriptor pd4 = new PropertyDescriptor(
        "angle", VectorComponent.class,
        "getAngle", "setAngle"
      );
      pd4.setDisplayName(resources.getString("setAngle"));
      pd4.setShortDescription(resources.getString("setAngleTip"));

      PropertyDescriptor pd5 = new PropertyDescriptor(
        "scale", VectorComponent.class,
        "getScale", "setScale"
      );
      pd5.setDisplayName(resources.getString("setScale"));
      pd5.setShortDescription(resources.getString("setScaleTip"));

      PropertyDescriptor pd6 = new PropertyDescriptor(
        "precision", VectorComponent.class,
        "getPrecision", "setPrecision"
      );
      pd6.setDisplayName(resources.getString("setPrecision"));
      pd6.setShortDescription(resources.getString("setPrecisionTip"));

      PropertyDescriptor pd7 = new PropertyDescriptor(
        "editable", VectorComponent.class,
        "isEditable", "setEditable"
      );
      pd7.setDisplayName(resources.getString("setEditable"));
      pd7.setShortDescription(resources.getString("setEditableTip"));

      PropertyDescriptor pd8 = new PropertyDescriptor(
        "graphVisible", VectorComponent.class,
        "isGraphVisible", "setGraphVisible"
      );
      pd8.setDisplayName(resources.getString("graphVisible"));
      pd8.setShortDescription(resources.getString("graphVisibleTip"));

      PropertyDescriptor pd9 = new PropertyDescriptor(
        "componentsVisible", VectorComponent.class,
        "isComponentsVisible", "setComponentsVisible"
      );
      pd9.setDisplayName(resources.getString("componentsVisible"));
      pd9.setShortDescription(resources.getString("componentsVisibleTip"));

      PropertyDescriptor pd10 = new PropertyDescriptor(
        "eastName", VectorComponent.class,
        "getEastName", "setEastName"
      );
      pd10.setDisplayName(resources.getString("eastName"));
      pd10.setShortDescription(resources.getString("eastNameTip"));

      PropertyDescriptor pd11 = new PropertyDescriptor(
        "northName", VectorComponent.class,
        "getNorthName", "setNorthName"
      );
      pd11.setDisplayName(resources.getString("northName"));
      pd11.setShortDescription(resources.getString("northNameTip"));

      PropertyDescriptor pd12 = new PropertyDescriptor(
        "angleName", VectorComponent.class,
        "getAngleName", "setAngleName"
      );
      pd12.setDisplayName(resources.getString("angleName"));
      pd12.setShortDescription(resources.getString("angleNameTip"));

      PropertyDescriptor pd13 = new PropertyDescriptor(
        "lengthName", VectorComponent.class,
        "getLengthName", "setLengthName"
      );
      pd13.setDisplayName(resources.getString("lengthName"));
      pd13.setShortDescription(resources.getString("lengthNameTip"));

      PropertyDescriptor pd14 = new PropertyDescriptor(
        "maxLength", VectorComponent.class,
        "getMaxLength", "setMaxLength"
      );
      pd14.setDisplayName(resources.getString("maxLength"));
      pd14.setShortDescription(resources.getString("maxLengthTip"));

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 14];
      for (int i=0; i<defaultProperties.length; i++) {
        pd[i] = defaultProperties[i];
      }
      int i = defaultProperties.length;
      pd[i++] = pd1;
      pd[i++] = pd2;
      pd[i++] = pd3;
      pd[i++] = pd4;
      pd[i++] = pd5;
      pd[i++] = pd6;
      pd[i++] = pd7;
      pd[i++] = pd8;
      pd[i++] = pd9;
      pd[i++] = pd10;
      pd[i++] = pd11;
      pd[i++] = pd12;
      pd[i++] = pd13;
      pd[i++] = pd14;
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
      Method listenerMethod1 = VectorListener.class.getMethod(
        "valueChanged", new Class[] {VectorEvent.class}
      );
      Method addListenerMethod = VectorComponent.class.getMethod(
        "addVectorListener", new Class[] {VectorListener.class}
      );
      Method removeListenerMethod = VectorComponent.class.getMethod(
        "removeVectorListener", new Class[] {VectorListener.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("valueChanged"));
      EventSetDescriptor esd1 = new EventSetDescriptor(
        "vector", VectorListener.class,
        new MethodDescriptor[] {md1},
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
