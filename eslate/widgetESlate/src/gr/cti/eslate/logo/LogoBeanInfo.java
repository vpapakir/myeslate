package gr.cti.eslate.logo;

import java.beans.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Locale;
import java.lang.reflect.Method;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;

import gr.cti.eslate.logo.Res;

/**
 * BeanInfo for Logo component.
 * @version     2.0.0, 24-May-2006
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K. Kyrimis
 */
public class LogoBeanInfo extends SimpleBeanInfo
{
  ResourceBundle bundle;
  ImageIcon icon16x16;

  public LogoBeanInfo()
  {
    bundle = ResourceBundle.getBundle("gr.cti.eslate.logo.LogoBeanInfoBundle", Locale.getDefault());
  }

  public String localize(String s)
  {
    try {
      return bundle.getString(s);
    } catch (Exception e) {
      //System.out.println("Couldn't localize "+s);
      return s;
    }
  }

  private PropertyDescriptor makePropertyDescriptor(String property, String category)
    throws IntrospectionException
  {
    PropertyDescriptor pd = new PropertyDescriptor(property, Logo.class);
    pd.setDisplayName(localize(property));
    pd.setShortDescription(localize(property + "Tip"));
    pd.setValue("propertyCategory", localize(category));
    return pd;
  }

  public PropertyDescriptor[] getPropertyDescriptors()
  {
    try {
      return new PropertyDescriptor[] {
        makePropertyDescriptor("menuBarVisible", "MenuBar"),
        makePropertyDescriptor("toolBarVisible", "ToolBar"),
        makePropertyDescriptor("hasFontSelector", "ToolBar"),
        makePropertyDescriptor("execQueueMaxSize", "Logo"),
        makePropertyDescriptor("border", "border"), //12May2000
        makePropertyDescriptor("lineNumbersVisibleInProgramArea", "Editor"),
        makePropertyDescriptor("lineNumbersVisibleInOutputArea", "Editor"),
        makePropertyDescriptor("statusBarVisible", "StatusBar")
        //...more properties here...//
      };
    } catch (IntrospectionException exc) {
      System.out.println("IntrospectionException: " + exc.getMessage());
      return null;
    }
  }

  public EventSetDescriptor[] getEventSetDescriptors()
  {
    ArrayList<EventSetDescriptor> descriptors =
      new ArrayList<EventSetDescriptor>();
/*
    ExtendedEventSetDescriptor esd1 = new ExtendedEventSetDescriptor(
      Logo.class,
      "propertyChange",
      PropertyChangeListener.class,
      "propertyChange",
      localize("propertyChange")
    );
*/
    EventSetDescriptor esd = null;
    try {
      Method listenerMethod = PropertyChangeListener.class.getMethod(
        "propertyChange",
        new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = Logo.class.getMethod(
        "addPropertyChangeListener",
        new Class[] {PropertyChangeListener.class}
      );
      Method removelistenerMethod = Logo.class.getMethod(
        "removePropertyChangeListener",
        new Class[] {PropertyChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(localize("propertyChange"));
      esd = new EventSetDescriptor(
        "propertyChange",
        PropertyChangeListener.class,
        new MethodDescriptor[] {md},
        addListenerMethod,
        removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Exception exc) {
      exc.printStackTrace();
    }

    try {
      Method listenerMethod = VetoableChangeListener.class.getMethod(
        "vetoableChange",
        new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = Logo.class.getMethod(
        "addVetoableChangeListener",
        new Class[] {VetoableChangeListener.class}
      );
      Method removelistenerMethod = Logo.class.getMethod(
        "removeVetoableChangeListener",
        new Class[] {VetoableChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(localize("vetoableChange"));
      esd = new EventSetDescriptor(
        "vetoableChange",
        VetoableChangeListener.class,
        new MethodDescriptor[] {md},
        addListenerMethod,
        removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Exception exc) {
      exc.printStackTrace();
    }

    try {
      Method listenerMethod1 = MouseListener.class.getMethod(
        "mouseEntered",
        new Class[] {MouseEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(localize("mouseEntered"));
      Method listenerMethod2 = MouseListener.class.getMethod(
        "mouseExited",
        new Class[] {MouseEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(localize("mouseExited"));
      Method addListenerMethod = Logo.class.getMethod(
        "addMouseListener",
        new Class[] {MouseListener.class}
      );
      Method removelistenerMethod = Logo.class.getMethod(
        "removeMouseListener",
        new Class[] {MouseListener.class}
      );
      esd = new EventSetDescriptor(
        "mouse",
        MouseListener.class,
        new MethodDescriptor[] {md1, md2},
        addListenerMethod,
        removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Exception exc) {
      exc.printStackTrace();
    }

    try {
      Method listenerMethod = MouseMotionListener.class.getMethod(
        "mouseMoved",
        new Class[] {MouseEvent.class}
      );
      Method addListenerMethod = Logo.class.getMethod(
        "addMouseMotionListener",
        new Class[] {MouseMotionListener.class}
      );
      Method removelistenerMethod = Logo.class.getMethod(
        "removeMouseMotionListener",
        new Class[] {MouseMotionListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(localize("mouseMoved"));
      esd = new EventSetDescriptor(
        "mouseMotion",
        MouseMotionListener.class,
        new MethodDescriptor[] {md},
        addListenerMethod,
        removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Exception exc) {
      exc.printStackTrace();
    }

    try {
      Method listenerMethod1 = ComponentListener.class.getMethod(
        "componentHidden",
        new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(localize("componentHidden"));
      Method listenerMethod2 = ComponentListener.class.getMethod(
        "componentShown",
        new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(localize("componentShown"));
      Method addListenerMethod = Logo.class.getMethod(
        "addComponentListener",
        new Class[] {ComponentListener.class}
      );
      Method removelistenerMethod = Logo.class.getMethod(
        "removeComponentListener",
        new Class[] {ComponentListener.class}
      );
      esd = new EventSetDescriptor(
        "component",
        ComponentListener.class,
        new MethodDescriptor[] {md1, md2},
        addListenerMethod,
        removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Exception exc) {
      exc.printStackTrace();
    }

    EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
    for (int i = 0; i < d.length; i++) {
      d[i] = descriptors.get(i);
    }
    return d;
  }

  public Image getIcon(int iconKind)
  {
    if (iconKind == BeanInfo.ICON_MONO_16x16 ||
        iconKind == BeanInfo.ICON_COLOR_16x16) {
      if (icon16x16 == null) {
        icon16x16 =
          Res.loadImageIcon("images/LogoIcon16x16c.gif", Res.localize("Logo"));
      }
      return (icon16x16 != null) ? icon16x16.getImage() : null;
    }
    return null;
  }

/*
  public BeanDescriptor getBeanDescriptor()
  {
    return new BeanDescriptor(Logo.class, LogoCustomizer.class);
  }
*/

/*
  public BeanInfo[] getAdditionalBeanInfo()
  {
    Class superclass = Button.class.getSuperclass();
    try{
      BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
      return new BeanInfo[] {superBeanInfo};
    } catch (IntrospectionException exc) {
      return null;
    }
  }
*/
}
