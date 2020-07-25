package gr.cti.eslate.navigator;

import java.beans.*;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;

import gr.cti.eslate.navigator.Res;
import gr.cti.eslate.propertyEditors.*;

/**
 * BeanInfo for Navigator component.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2006
 */
public class NavigatorBeanInfo extends SimpleBeanInfo
{
  ResourceBundle bundle;
  ImageIcon icon16x16;

  /**
   * Construct the BeanInfo.
   */
  public NavigatorBeanInfo()
  {
    bundle = ResourceBundle.getBundle(
      "gr.cti.eslate.navigator.NavigatorBeanInfoBundle", Locale.getDefault()
    );
  }

  /**
   * Create a localized version of a string.
   * @param     s       The English version of the string to localize.
   * @return    The localized version of the string.
   */
  public String localize(String s)
  {
    try{
      return bundle.getString(s);
    } catch(Exception e) {
      //System.out.println("Couldn't localize "+s);
      return s;
    }
  }

  /**
   * Create a property descriptor.
   * @param     property        The English name of the property.
   * @param     category        The english name of the property's category.
   * @return    A property decsriptor corresponding to the given property.
   */
  private PropertyDescriptor makePropertyDescriptor(String property,
                                                    String category)
    throws IntrospectionException
  {
    PropertyDescriptor pd = new PropertyDescriptor(property, Navigator.class);
    pd.setDisplayName(localize(property));
    pd.setShortDescription(localize(property + "Tip"));
    pd.setBound(true);
    pd.setValue("propertyCategory", localize(category));
    return pd;
  }

  /**
   * Returns the component's property descriptors.
   * @return    An array containing the requested property descriptors.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    try {
      PropertyDescriptor currentLocationProperty =
        makePropertyDescriptor("currentLocation", "Navigation"); //26Jun2000
      currentLocationProperty.setBound(true); //???
      currentLocationProperty.setPropertyEditorClass(
        StringPropertyEditor2.class
      );

      PropertyDescriptor navigatorClassProperty =
        makePropertyDescriptor("navigatorClass", "Navigation");
      navigatorClassProperty.setBound(true);
      // 26Jun2000: marked as a property for "expert" users only
      navigatorClassProperty.setExpert(true);
      //26Jun2000: added custom property editor (drop-down list)
      navigatorClassProperty.setPropertyEditorClass(
        NavigatorClassPropertyEditor.class
      );

      Method getter = Navigator.class.getMethod("getNavigator", new Class[] {});
      PropertyDescriptor navigatorProperty =
        new PropertyDescriptor("Navigator", getter, null);
      navigatorProperty.setDisplayName("Navigator");

      return new PropertyDescriptor[] {
        makePropertyDescriptor("menuBarVisible", "MenuBar"),
        makePropertyDescriptor("toolBarVisible", "ToolBar"),
        makePropertyDescriptor("addressBarVisible", "AddressBar"),
        makePropertyDescriptor("statusBarVisible", "StatusBar"),
        navigatorClassProperty,
        makePropertyDescriptor("home", "Navigation"), //26Jun2000
        currentLocationProperty,
        navigatorProperty
        //...more properties here...//
      };
    }catch (Throwable th) {
      th.printStackTrace();
      return null;
    }
  }

  /**
   * Returns the component's event set descriptors.
   * @return    An array containing the requested event set descriptors.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    ArrayList<EventSetDescriptor> descriptors =
      new ArrayList<EventSetDescriptor>();
/*
    ExtendedEventSetDescriptor esd1 = new ExtendedEventSetDescriptor(
      Navigator.class,
      "propertyChange",
      PropertyChangeListener.class,
      "propertyChange",
      localize("propertyChange"));
*/
    EventSetDescriptor esd = null;
    try{
      Method listenerMethod = PropertyChangeListener.class.getMethod(
        "propertyChange", new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = Navigator.class.getMethod(
        "addPropertyChangeListener", new Class[] {PropertyChangeListener.class}
      );
      Method removelistenerMethod = Navigator.class.getMethod(
        "removePropertyChangeListener",
        new Class[] {PropertyChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(localize("propertyChange"));
      esd = new EventSetDescriptor(
        "propertyChange", PropertyChangeListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    }catch (Exception exc) {
      exc.printStackTrace();
    }

    try{
      Method listenerMethod = VetoableChangeListener.class.getMethod(
        "vetoableChange", new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = Navigator.class.getMethod(
        "addVetoableChangeListener", new Class[] {VetoableChangeListener.class}
      );
      Method removelistenerMethod = Navigator.class.getMethod(
        "removeVetoableChangeListener",
        new Class[] {VetoableChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(localize("vetoableChange"));
      esd = new EventSetDescriptor(
        "vetoableChange", VetoableChangeListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    }catch (Exception exc) {
      exc.printStackTrace();
    }

    try{
      Method listenerMethod1 = MouseListener.class.getMethod(
        "mouseEntered", new Class[] {MouseEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(localize("mouseEntered"));
      Method listenerMethod2 = MouseListener.class.getMethod(
        "mouseExited", new Class[] {MouseEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(localize("mouseExited"));
      Method addListenerMethod = Navigator.class.getMethod(
        "addMouseListener", new Class[] {MouseListener.class}
      );
      Method removelistenerMethod = Navigator.class.getMethod(
        "removeMouseListener", new Class[] {MouseListener.class}
      );
      esd = new EventSetDescriptor(
        "mouse", MouseListener.class, new MethodDescriptor[] {md1, md2},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    }catch (Exception exc) {
      exc.printStackTrace();
    }

    try{
      Method listenerMethod = MouseMotionListener.class.getMethod(
        "mouseMoved", new Class[] {MouseEvent.class}
      );
      Method addListenerMethod = Navigator.class.getMethod(
        "addMouseMotionListener", new Class[] {MouseMotionListener.class}
      );
      Method removelistenerMethod = Navigator.class.getMethod(
        "removeMouseMotionListener", new Class[] {MouseMotionListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(localize("mouseMoved"));
      esd = new EventSetDescriptor(
        "mouseMotion", MouseMotionListener.class, new MethodDescriptor[] {md},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    }catch (Exception exc) {
      exc.printStackTrace();
    }

    try{
      Method listenerMethod1 = ComponentListener.class.getMethod(
        "componentHidden", new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(localize("componentHidden"));
      Method listenerMethod2 = ComponentListener.class.getMethod(
        "componentShown", new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(localize("componentShown"));
      Method addListenerMethod = Navigator.class.getMethod(
        "addComponentListener", new Class[] {ComponentListener.class}
      );
      Method removelistenerMethod = Navigator.class.getMethod(
        "removeComponentListener", new Class[] {ComponentListener.class}
      );
      esd = new EventSetDescriptor(
        "component", ComponentListener.class, new MethodDescriptor[] {md1, md2},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    }catch (Exception exc) {
      exc.printStackTrace();
    }

    EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
    for (int i=0; i<d.length; i++) {
      d[i] = (EventSetDescriptor) descriptors.get(i);
    }
    return d;
  }

  /**
   * Returns the component's icon.
   * @return    The requested icon.
   */
  public Image getIcon(int iconKind)
  {
    if (iconKind == BeanInfo.ICON_MONO_16x16 ||
        iconKind == BeanInfo.ICON_COLOR_16x16) {
      if (icon16x16==null) {
        icon16x16=Res.loadImageIcon(
          "images/NavigatorIcon16x16c.gif", Res.localize("Navigator")
        );
      }
      return (icon16x16 != null) ? icon16x16.getImage() : null;
    }
    return null;
  }

/*
  public BeanDescriptor getBeanDescriptor()
  {
    return new BeanDescriptor(Navigator.class, NavigatorCustomizer.class);
  }
*/

/*
  public BeanInfo[] getAdditionalBeanInfo()
  {
    Class superclass = Button.class.getSuperclass();
    try{
      BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
      return new BeanInfo[] {superBeanInfo};
    }catch (IntrospectionException exc) {
      return null;
    }
  }
*/
}
