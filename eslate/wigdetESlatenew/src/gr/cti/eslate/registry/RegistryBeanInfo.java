package gr.cti.eslate.registry;

import java.awt.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.registry.event.*;

/**
 * BeanInfo for the registry component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 */
public class RegistryBeanInfo extends SimpleBeanInfo
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = Registry.resources;

  private Image mono32Image;
  private Image color32Image;
  private Image mono16Image;
  private Image color16Image;

  /**
   * Construct the BeanInfo.
   */
  public RegistryBeanInfo()
  {
    super();
    try {
      color32Image = new
        ImageIcon(getClass().getResource("images/registry32c.gif")).getImage();
      mono32Image = new
        ImageIcon(getClass().getResource("images/registry32m.gif")).getImage();
      color16Image = new
        ImageIcon(getClass().getResource("images/registry16c.gif")).getImage();
      mono16Image = new
        ImageIcon(getClass().getResource("images/registry16m.gif")).getImage();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method returns an image object that can be used to represent the
   * bean in toolboxes, toolbars, etc.
   * @param     iconKind        The kind of icon requested. This should be one
   *                            of the constant values ICON_COLOR_16x16,
   *                            ICON_COLOR_32x32, ICON_MONO_16x16, or
   *                            ICON_MONO_32x32.
   * @return    An image object representing the requested icon. May return
   *            null if no suitable icon is available.
   */
  public Image getIcon(int iconKind)
  {
    switch (iconKind) {
      case ICON_MONO_32x32:
        return mono32Image;
      case ICON_COLOR_32x32:
        return color32Image;
      case ICON_MONO_16x16:
        return mono16Image;
      case ICON_COLOR_16x16:
        return color16Image;
      default:
        return null;
    }
  }

  /**
   * Gets the bean's <code>EventSetDescriptor</code>s.
   * @return    An array of EventSetDescriptors describing the kinds of events
   *            fired by this bean
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    try {
      Method listenerMethod1 = RegistryListener.class.getMethod(
        "valueChanged", new Class[] {RegistryEvent.class}
      );
      Method listenerMethod2 = RegistryListener.class.getMethod(
        "commentChanged", new Class[] {RegistryEvent.class}
      );
      Method listenerMethod3 = RegistryListener.class.getMethod(
        "persistenceChanged", new Class[] {RegistryEvent.class}
      );
      Method listenerMethod4 = RegistryListener.class.getMethod(
        "variableAdded", new Class[] {RegistryEvent.class}
      );
      Method listenerMethod5 = RegistryListener.class.getMethod(
        "variableRemoved", new Class[] {RegistryEvent.class}
      );
      Method listenerMethod6 = RegistryListener.class.getMethod(
        "registryCleared", new Class[] {RegistryEvent.class}
      );
      Method addListenerMethod = Registry.class.getMethod(
        "addRegistryListener", new Class[] {RegistryListener.class}
      );
      Method removeListenerMethod = Registry.class.getMethod(
        "removeRegistryListener", new Class[] {RegistryListener.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("valueChanged"));
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("commentChanged"));
      MethodDescriptor md3 = new MethodDescriptor(listenerMethod3);
      md3.setDisplayName(resources.getString("persistenceChanged"));
      MethodDescriptor md4 = new MethodDescriptor(listenerMethod4);
      md4.setDisplayName(resources.getString("variableAdded"));
      MethodDescriptor md5 = new MethodDescriptor(listenerMethod5);
      md5.setDisplayName(resources.getString("variableRemoved"));
      MethodDescriptor md6 = new MethodDescriptor(listenerMethod6);
      md6.setDisplayName(resources.getString("registryCleared"));
      EventSetDescriptor esd1 = new EventSetDescriptor(
        "registry", RegistryListener.class,
        new MethodDescriptor[] {md1, md2, md3, md4, md5, md6},
        addListenerMethod, removeListenerMethod
      );
      return new EventSetDescriptor[] {esd1};
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
