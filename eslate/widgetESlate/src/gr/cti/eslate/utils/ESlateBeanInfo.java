package gr.cti.eslate.utils;

import java.beans.*;
import java.util.*;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class provides a bean info class that supports some basic events:
 * java.beans.PropertyChangeListener.propertyChange,
 * java.beans.VetoableChangeListener.vetoableChange,
 * java.awt.event.MouseListener.mouseEntered,
 * java.awt.event.MouseListener.mouseExited,
 * java.awt.event.mouseMotionListener.mouseMoved,
 * java.awt.event.ComponentListener.componentHidden, and
 * java.awt.event.ComponentListener.componentShown.
 *
 * @version     2.0.21, 28-Sep-2007
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */

public class ESlateBeanInfo extends SimpleBeanInfo
{
  /**
   * Localized resources.
   */
  private ResourceBundle bundle;
  /**
   * The class of the component to which this BeanInfo refers.
   */
  private Class<?> componentClass;
  /**
   * 16x16 monochrome icon.
   */
  Image mono16Image = null;
  /**
   * 16x16 color icon.
   */
  Image color16Image = null;
  /**
   * 32x32 monochrome icon.
   */
  Image mono32Image = null;
  /**
   * 32x32 color icon.
   */
  Image color32Image = null;

  /**
   * Construct an ESlateBeanInfo instance.
   */
  public ESlateBeanInfo()
  {
    super();
    bundle = ResourceBundle.getBundle(
      "gr.cti.eslate.utils.ESlateBeanUtilResources",
      ESlateMicroworld.getCurrentLocale()
    );
    // Find the class whose BeanInfo this class represents. For class Foo,
    // a descendant of this class <EM>must</EM> have been loaded as class
    // FooBeanInfo.
    String BeanInfo = "BeanInfo";
    Class<?> myClass = getClass();
    String className = myClass.getName();
    if (className.endsWith(BeanInfo)) {
      className = className.substring(0, className.length()-BeanInfo.length());
      try {
        this.componentClass = Class.forName(className);
      } catch (ClassNotFoundException e) {
        // This won't work, but then again, this shouldn't happen.
        this.componentClass = myClass;
      }
    }else{
      // This won't work, but then again, this shouldn't happen.
      this.componentClass = myClass;
    }
  }

  /**
   * Gets the bean's <code>PropertyDescriptor</code>s.
   * @return    An array of PropertyDescriptors describing the editable
   *            properties supported by this bean.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    return new PropertyDescriptor[0];
  }

  /**
   * Sets the 16x16 monochrome icon.
   * @param     icon    The icon to set.
   */
  protected void set16x16MonoIcon(ImageIcon icon)
  {
    if (icon != null) {
      mono16Image = icon.getImage();
    }
  }

  /**
   * Sets the 16x16 color icon.
   * @param     icon    The icon to set.
   */
  protected void set16x16ColorIcon(ImageIcon icon)
  {
    if (icon != null) {
      color16Image = icon.getImage();
    }
  }

  /**
   * Sets the 32x32 monochrome icon.
   * @param     icon    The icon to set.
   */
  protected void set32x32MonoIcon(ImageIcon icon)
  {
    if (icon != null) {
      mono32Image = icon.getImage();
    }
  }

  /**
   * Sets the 32x32 color icon.
   * @param     icon    The icon to set.
   */
  protected void set32x32ColorIcon(ImageIcon icon)
  {
    if (icon != null) {
      color32Image = icon.getImage();
    }
  }

  /**
   * Gets the bean's <code>EventSetDescriptor</code>s.
   * @return    An array of EventSetDescriptors describing the kinds of events
   *            fired by this bean.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    ArrayList<EventSetDescriptor> descriptors =
      new ArrayList<EventSetDescriptor>();
    EventSetDescriptor esd = null;
    try {
      Method listenerMethod = PropertyChangeListener.class.getMethod(
        "propertyChange", new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = componentClass.getMethod(
        "addPropertyChangeListener", new Class[] {PropertyChangeListener.class}
      );
      Method removelistenerMethod = componentClass.getMethod(
        "removePropertyChangeListener",
        new Class[] {PropertyChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(bundle.getString("propertyChange"));
      esd = new EventSetDescriptor(
        "propertyChange", PropertyChangeListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      //th.printStackTrace();
    }

    try {
      Method listenerMethod = VetoableChangeListener.class.getMethod(
        "vetoableChange", new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = componentClass.getMethod(
        "addVetoableChangeListener", new Class[] {VetoableChangeListener.class}
      );
      Method removelistenerMethod = componentClass.getMethod(
        "removeVetoableChangeListener",
        new Class[] {VetoableChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(bundle.getString("vetoableChange"));
      esd = new EventSetDescriptor(
        "vetoableChange", VetoableChangeListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      //th.printStackTrace();
    }

    try {
      Method listenerMethod1 = MouseListener.class.getMethod(
        "mouseEntered", new Class[] {MouseEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(bundle.getString("mouseEntered"));
      Method listenerMethod2 = MouseListener.class.getMethod(
        "mouseExited", new Class[] {MouseEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(bundle.getString("mouseExited"));
      Method addListenerMethod = componentClass.getMethod(
        "addMouseListener", new Class[] {MouseListener.class}
      );
      Method removelistenerMethod = componentClass.getMethod(
        "removeMouseListener", new Class[] {MouseListener.class}
      );
      esd = new EventSetDescriptor(
        "mouse", MouseListener.class, new MethodDescriptor[] {md1, md2},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      //th.printStackTrace();
    }

    try {
      Method listenerMethod = MouseMotionListener.class.getMethod(
        "mouseMoved", new Class[] {MouseEvent.class}
      );
      Method addListenerMethod = componentClass.getMethod(
        "addMouseMotionListener", new Class[] {MouseMotionListener.class}
      );
      Method removelistenerMethod = componentClass.getMethod(
        "removeMouseMotionListener", new Class[] {MouseMotionListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(bundle.getString("mouseMoved"));
      esd = new EventSetDescriptor(
        "mouseMotion", MouseMotionListener.class, new MethodDescriptor[] {md},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      //th.printStackTrace();
    }

    try {
      Method listenerMethod1 = ComponentListener.class.getMethod(
        "componentHidden", new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(bundle.getString("componentHidden"));
      Method listenerMethod2 = ComponentListener.class.getMethod(
        "componentShown", new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(bundle.getString("componentShown"));
      Method addListenerMethod = componentClass.getMethod(
        "addComponentListener", new Class[] {ComponentListener.class}
      );
      Method removelistenerMethod = componentClass.getMethod(
        "removeComponentListener", new Class[] {ComponentListener.class}
      );
      esd = new EventSetDescriptor(
        "component", ComponentListener.class, new MethodDescriptor[] {md1, md2},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      //th.printStackTrace();
    }

    EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
    for (int i=0; i<d.length; i++) {
      d[i] = descriptors.get(i);
    }
    return d;
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
      case ICON_MONO_16x16:
        return mono16Image;
      case ICON_MONO_32x32:
        return mono32Image;
      case ICON_COLOR_16x16:
        return color16Image;
      case ICON_COLOR_32x32:
        return color32Image;
      default:
        return null;
    }
  }

}
