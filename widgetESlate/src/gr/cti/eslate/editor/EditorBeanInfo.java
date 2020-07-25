package gr.cti.eslate.editor;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

/**
 * Beaninfo for the Editor component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class EditorBeanInfo extends SimpleBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.editor.EditorResource",
    Locale.getDefault()
  );

  private Image mono16Image;
  private Image color16Image;

  /**
   * Construct the BeanInfo.
   */
  public EditorBeanInfo()
  {
    super();
    try {
      color16Image =
        new ImageIcon(getClass().getResource("res/editor16c.gif")).getImage();
      mono16Image =
        new ImageIcon(getClass().getResource("res/editor16m.gif")).getImage();
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
      case ICON_MONO_16x16:
        return mono16Image;
      case ICON_COLOR_16x16:
        return color16Image;
      default:
        return null;
    }
  }

  /**
   * Gets the bean's BeanDescriptor.
   * @return    A BeanDescriptor providing overall information about the bean,
   *            such as its displayName, its customizer, etc. May return null
   *            if the information should be obtained by automatic analysis.
   */
  public BeanDescriptor getBeanDescriptor()
  {
    return new BeanDescriptor(Editor.class, EditorCustomizer.class);
  }

  /**
   * Gets the bean's <code>PropertyDescriptor</code>s.
   * @return    An array of PropertyDescriptors describing the editable
   *            properties supported by this bean.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    try {
      PropertyDescriptor[] pd = new PropertyDescriptor[4];

      pd[0] = new PropertyDescriptor(
        "menuBarVisible", Editor.class,
        "isMenuBarVisible", "setMenuBarVisible"
      );
      pd[0].setDisplayName(resources.getString("menuBarVisible"));
      pd[0].setShortDescription(resources.getString("menuBarVisibleTip"));

      pd[1] = new PropertyDescriptor(
        "toolBarVisible", Editor.class,
        "isToolBarVisible", "setToolBarVisible"
      );
      pd[1].setDisplayName(resources.getString("toolBarVisible"));
      pd[1].setShortDescription(resources.getString("toolBarVisibleTip"));

      pd[2] = new PropertyDescriptor(
        "statusBarVisible", Editor.class,
        "isStatusBarVisible", "setStatusBarVisible"
      );
      pd[2].setDisplayName(resources.getString("statusBarVisible"));
      pd[2].setShortDescription(resources.getString("statusBarVisibleTip"));

      pd[3] = new PropertyDescriptor(
        "multipleEditor", Editor.class,
        "isMultipleFileEditor", "setMultipleFileEditor"
      );
      pd[3].setDisplayName(resources.getString("multipleEditor"));
      pd[3].setShortDescription(resources.getString("multipleEditorTip"));

      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
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
      Method addListenerMethod = Editor.class.getMethod(
        "addPropertyChangeListener", new Class[] {PropertyChangeListener.class}
      );
      Method removelistenerMethod = Editor.class.getMethod(
        "removePropertyChangeListener",
        new Class[] {PropertyChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("propertyChange"));
      esd = new EventSetDescriptor(
        "propertyChange", PropertyChangeListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      th.printStackTrace();
    }

    try {
      Method listenerMethod = VetoableChangeListener.class.getMethod(
        "vetoableChange", new Class[] {PropertyChangeEvent.class}
      );
      Method addListenerMethod = Editor.class.getMethod(
        "addVetoableChangeListener", new Class[] {VetoableChangeListener.class}
      );
      Method removelistenerMethod = Editor.class.getMethod(
        "removeVetoableChangeListener",
        new Class[] {VetoableChangeListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("vetoableChange"));
      esd = new EventSetDescriptor(
        "vetoableChange", VetoableChangeListener.class,
        new MethodDescriptor[] {md}, addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      th.printStackTrace();
    }

    try {
      Method listenerMethod1 = MouseListener.class.getMethod(
        "mouseEntered", new Class[] {MouseEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("mouseEntered"));
      Method listenerMethod2 = MouseListener.class.getMethod(
        "mouseExited", new Class[] {MouseEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("mouseExited"));
      Method addListenerMethod = Editor.class.getMethod(
        "addMouseListener", new Class[] {MouseListener.class}
      );
      Method removelistenerMethod = Editor.class.getMethod(
        "removeMouseListener", new Class[] {MouseListener.class}
      );
      esd = new EventSetDescriptor(
        "mouse", MouseListener.class, new MethodDescriptor[] {md1, md2},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      th.printStackTrace();
    }

    try {
      Method listenerMethod = MouseMotionListener.class.getMethod(
        "mouseMoved", new Class[] {MouseEvent.class}
      );
      Method addListenerMethod = Editor.class.getMethod(
        "addMouseMotionListener", new Class[] {MouseMotionListener.class}
      );
      Method removelistenerMethod = Editor.class.getMethod(
        "removeMouseMotionListener", new Class[] {MouseMotionListener.class}
      );
      MethodDescriptor md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("mouseMoved"));
      esd = new EventSetDescriptor(
        "mouseMotion", MouseMotionListener.class, new MethodDescriptor[] {md},
        addListenerMethod, removelistenerMethod
      );
      descriptors.add(esd);
    } catch (Throwable th) {
      th.printStackTrace();
    }

    try {
      Method listenerMethod1 = ComponentListener.class.getMethod(
        "componentHidden", new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("componentHidden"));
      Method listenerMethod2 = ComponentListener.class.getMethod(
        "componentShown", new Class[] {ComponentEvent.class}
      );
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("componentShown"));
      Method addListenerMethod = Editor.class.getMethod(
        "addComponentListener", new Class[] {ComponentListener.class}
      );
      Method removelistenerMethod = Editor.class.getMethod(
        "removeComponentListener", new Class[] {ComponentListener.class}
      );
      esd = new EventSetDescriptor(
        "component", ComponentListener.class, new MethodDescriptor[] {md1, md2},
        addListenerMethod, removelistenerMethod
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
