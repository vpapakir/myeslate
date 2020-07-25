package gr.cti.eslate.eslateToolBar;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;
import gr.cti.eslate.eslateToolBar.event.*;

/**
 * BeanInfo for ToolBar component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 */
public class ESlateToolBarBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.eslateToolBar.ToolBarResource",
    Locale.getDefault()
  );
    
  /**
   * Construct the BeanInfo.
   */
  public ESlateToolBarBeanInfo()
  {
    super();
    try {
      Class<?> c = ESlateToolBarBeanInfo.class;
      set16x16ColorIcon(new ImageIcon(c.getResource("images/toolbar16c.gif")));
      set16x16MonoIcon(new ImageIcon(c.getResource("images/toolbar16m.gif")));
    } catch (Exception e) {
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
        "Orientation", ESlateToolBar.class,
        "getOrientation", "setOrientation"
      );
      pd1.setDisplayName(resources.getString("orientation"));
      pd1.setShortDescription(resources.getString("orientationTip"));
      pd1.setPropertyEditorClass(OrientationEditor.class);

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "DynamicBorders", ESlateToolBar.class,
        "isDynamicBorders", "setDynamicBorders"
      );
      pd2.setDisplayName(resources.getString("dynBorders"));
      pd2.setShortDescription(resources.getString("dynBordersTip"));

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "Border", ESlateToolBar.class,
        "getBorder", "setBorder"
      );
      pd3.setDisplayName(resources.getString("border"));
      pd3.setShortDescription(resources.getString("borderTip"));

      PropertyDescriptor pd4 = new PropertyDescriptor(
        "Separation", ESlateToolBar.class,
        "getSeparation", "setSeparation"
      );
      pd4.setDisplayName(resources.getString("separation"));
      pd4.setShortDescription(resources.getString("separationTip"));
      pd4.setPropertyEditorClass(SeparationEditor.class);

      PropertyDescriptor pd5 = new PropertyDescriptor(
        "LeadingSeparation", ESlateToolBar.class,
        "getLeadingSeparation", "setLeadingSeparation"
      );
      pd5.setDisplayName(resources.getString("leadingSeparation"));
      pd5.setShortDescription(resources.getString("leadingSeparationTip"));
      pd5.setPropertyEditorClass(LeadingSeparationEditor.class);

      PropertyDescriptor pd6 = new PropertyDescriptor(
        "VisualGroupLayout", ESlateToolBar.class,
        "getVisualGroupLayout", "setVisualGroupLayout"
      );
      pd6.setDisplayName(resources.getString("vgLayout"));
      pd6.setShortDescription(resources.getString("vgLayoutTip"));
      pd6.setPropertyEditorClass(VisualGroupEditor.class);

      PropertyDescriptor pd7 = new PropertyDescriptor(
        "ButtonGroupLayout", ESlateToolBar.class,
        "getButtonGroupLayout", "setButtonGroupLayout"
      );
      pd7.setDisplayName(resources.getString("bgLayout"));
      pd7.setShortDescription(resources.getString("bgLayoutTip"));
      pd7.setPropertyEditorClass(ButtonGroupEditor.class);

      PropertyDescriptor pd8 = new PropertyDescriptor(
        "Centered", ESlateToolBar.class,
        "isCentered", "setCentered"
      );
      pd8.setDisplayName(resources.getString("centered"));
      pd8.setShortDescription(resources.getString("centeredTip"));

      PropertyDescriptor pd9 = new PropertyDescriptor(
        "BorderPainted", ESlateToolBar.class,
        "isBorderPainted", "setBorderPainted"
      );
      pd9.setDisplayName(resources.getString("borderPainted"));
      pd9.setShortDescription(resources.getString("borderPaintedTip"));

      PropertyDescriptor pd10 = new PropertyDescriptor(
        "Floatable", ESlateToolBar.class,
        "isFloatable", "setFloatable"
      );
      pd10.setDisplayName(resources.getString("floatable"));
      pd10.setShortDescription(resources.getString("floatableTip"));

      PropertyDescriptor pd11 = new PropertyDescriptor(
        "PreferredSize", ESlateToolBar.class,
        "getPreferredSize", "setPreferredSize"
      );
      pd11.setDisplayName(resources.getString("preferredSize"));
      pd11.setShortDescription(resources.getString("preferredSizeTip"));
      pd11.setExpert(true);
      pd11.setPropertyEditorClass(DimensionEditor.class);

      PropertyDescriptor pd12 = new PropertyDescriptor(
        "MinimumSize", ESlateToolBar.class,
        "getMinimumSize", "setMinimumSize"
      );
      pd12.setDisplayName(resources.getString("minimumSize"));
      pd12.setShortDescription(resources.getString("minimumSizeTip"));
      pd12.setExpert(true);
      pd12.setPropertyEditorClass(DimensionEditor.class);

      PropertyDescriptor pd13 = new PropertyDescriptor(
        "MaximumSize", ESlateToolBar.class,
        "getMaximumSize", "setMaximumSize"
      );
      pd13.setDisplayName(resources.getString("maximumSize"));
      pd13.setShortDescription(resources.getString("maximumSizeTip"));
      pd13.setExpert(true);
      pd13.setPropertyEditorClass(DimensionEditor.class);

      PropertyDescriptor pd14 = new PropertyDescriptor(
        "Modified", ESlateToolBar.class,
        "isModified", "setModified"
      );
      pd14.setDisplayName(resources.getString("modified"));
      pd14.setShortDescription(resources.getString("modifiedTip"));
      pd14.setExpert(true);

      PropertyDescriptor pd15 = new PropertyDescriptor(
        "MenuEnabled", ESlateToolBar.class,
        "isMenuEnabled", "setMenuEnabled"
      );
      pd15.setDisplayName(resources.getString("menuEnabled"));
      pd15.setShortDescription(resources.getString("menuEnabledTip"));
      pd15.setExpert(true);

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 15];
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
      pd[i++] = pd15;
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
      Method listenerMethod1 = ToolBarListener.class.getMethod(
        "mouseClicked", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod2 = ToolBarListener.class.getMethod(
        "mousePressed", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod3 = ToolBarListener.class.getMethod(
        "mouseReleased", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod4 = ToolBarListener.class.getMethod(
        "mouseEntered", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod5 = ToolBarListener.class.getMethod(
        "mouseExited", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod6 = ToolBarListener.class.getMethod(
        "mouseDragged", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod7 = ToolBarListener.class.getMethod(
        "mouseMoved", new Class[] {ToolBarEvent.class}
      );
      Method listenerMethod8 = ToolBarListener.class.getMethod(
        "actionPerformed", new Class[] {ToolBarEvent.class}
      );

      Method addListenerMethod = ESlateToolBar.class.getMethod(
        "addToolBarListener", new Class[] {ToolBarListener.class}
      );
      Method removeListenerMethod = ESlateToolBar.class.getMethod(
        "removeToolBarListener", new Class[] {ToolBarListener.class}
      );

      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("mouseClickedOnTool"));
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("mousePressedOnTool"));
      MethodDescriptor md3 = new MethodDescriptor(listenerMethod3);
      md3.setDisplayName(resources.getString("mouseReleasedOnTool"));
      MethodDescriptor md4 = new MethodDescriptor(listenerMethod4);
      md4.setDisplayName(resources.getString("mouseEnteredOnTool"));
      MethodDescriptor md5 = new MethodDescriptor(listenerMethod5);
      md5.setDisplayName(resources.getString("mouseExitedOnTool"));
      MethodDescriptor md6 = new MethodDescriptor(listenerMethod6);
      md6.setDisplayName(resources.getString("mouseDraggedOnTool"));
      MethodDescriptor md7 = new MethodDescriptor(listenerMethod7);
      md7.setDisplayName(resources.getString("mouseMovedOnTool"));
      MethodDescriptor md8 = new MethodDescriptor(listenerMethod8);
      md8.setDisplayName(resources.getString("actionPerformedOnTool"));

      EventSetDescriptor esd1 = new EventSetDescriptor(
        "toolbar", ToolBarListener.class,
        new MethodDescriptor[] {md1, md2, md3, md4, md5, md6, md7, md8},
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
