package gr.cti.eslate.eslateSplitPane;

import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.propertyEditors.*;
import gr.cti.eslate.utils.*;

/**
 * BeanInfo for split pane component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 26-May-2006
 */
public class ESlateSplitPaneBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ESlateSplitPane.resources;
    
  /**
   * Construct the BeanInfo.
   */
  public ESlateSplitPaneBeanInfo()
  {
    super();
    try {
      Class<?> c = ESlateSplitPaneBeanInfo.class;
      set16x16ColorIcon(
        new ImageIcon(c.getResource("images/splitpane16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(c.getResource("images/splitpane16m.gif"))
      );
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
        "PreferredSize", ESlateSplitPane.class,
        "getPreferredSize", "setPreferredSize"
      );
      pd1.setDisplayName(resources.getString("preferredSize"));
      pd1.setShortDescription(resources.getString("preferredSizeTip"));
      pd1.setExpert(true);
      pd1.setPropertyEditorClass(DimensionPropertyEditor.class);

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "MinimumSize", ESlateSplitPane.class,
        "getMinimumSize", "setMinimumSize"
      );
      pd2.setDisplayName(resources.getString("minimumSize"));
      pd2.setShortDescription(resources.getString("minimumSizeTip"));
      pd2.setExpert(true);
      pd2.setPropertyEditorClass(DimensionPropertyEditor.class);

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "MaximumSize", ESlateSplitPane.class,
        "getMaximumSize", "setMaximumSize"
      );
      pd3.setDisplayName(resources.getString("maximumSize"));
      pd3.setShortDescription(resources.getString("maximumSizeTip"));
      pd3.setExpert(true);
      pd3.setPropertyEditorClass(DimensionPropertyEditor.class);

      PropertyDescriptor pd4 = new PropertyDescriptor(
        "FirstComponentClassName", ESlateSplitPane.class,
        "getFirstComponentClassName", "setFirstComponentClassName"
      );
      pd4.setDisplayName(resources.getString("firstComponent"));
      pd4.setShortDescription(resources.getString("firstComponentTip"));
      pd4.setPropertyEditorClass(StringPropertyEditor2.class);

      PropertyDescriptor pd5 = new PropertyDescriptor(
        "SecondComponentClassName", ESlateSplitPane.class,
        "getSecondComponentClassName", "setSecondComponentClassName"
      );
      pd5.setDisplayName(resources.getString("secondComponent"));
      pd5.setShortDescription(resources.getString("secondComponentTip"));
      pd5.setPropertyEditorClass(StringPropertyEditor2.class);

      PropertyDescriptor pd6 = new PropertyDescriptor(
        JSplitPane.CONTINUOUS_LAYOUT_PROPERTY, ESlateSplitPane.class,
        "isContinuousLayout", "setContinuousLayout"
      );
      pd6.setDisplayName(resources.getString("continuousLayout"));
      pd6.setShortDescription(resources.getString("continuousLayoutTip"));

      PropertyDescriptor pd7 = new PropertyDescriptor(
        JSplitPane.DIVIDER_LOCATION_PROPERTY, ESlateSplitPane.class,
        "getDividerLocation", "setDividerLocation"
      );
      pd7.setDisplayName(resources.getString("dividerLocation"));
      pd7.setShortDescription(resources.getString("dividerLocationTip"));
      pd7.setPropertyEditorClass(DividerLocationPropertyEditor.class);

      PropertyDescriptor pd8 = new PropertyDescriptor(
        JSplitPane.DIVIDER_SIZE_PROPERTY, ESlateSplitPane.class,
        "getDividerSize", "setDividerSize"
      );
      pd8.setDisplayName(resources.getString("dividerSize"));
      pd8.setShortDescription(resources.getString("dividerSizeTip"));
      pd8.setPropertyEditorClass(PositiveIntegerPropertyEditor.class);

      PropertyDescriptor pd9 = new PropertyDescriptor(
        JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY, ESlateSplitPane.class,
        "isOneTouchExpandable", "setOneTouchExpandable"
      );
      pd9.setDisplayName(resources.getString("oneTouchExpandable"));
      pd9.setShortDescription(resources.getString("oneTouchExpandableTip"));

      PropertyDescriptor pd10 = new PropertyDescriptor(
        JSplitPane.ORIENTATION_PROPERTY, ESlateSplitPane.class,
        "getOrientation", "setOrientation"
      );
      pd10.setDisplayName(resources.getString("orientation"));
      pd10.setShortDescription(resources.getString("orientationTip"));
      pd10.setPropertyEditorClass(OrientationPropertyEditor.class);

      PropertyDescriptor pd11 = new PropertyDescriptor(
        JSplitPane.RESIZE_WEIGHT_PROPERTY, ESlateSplitPane.class,
        "getResizeWeight", "setResizeWeight"
      );
      pd11.setDisplayName(resources.getString("resizeWeight"));
      pd11.setShortDescription(resources.getString("resizeWeightTip"));
      pd11.setExpert(true);

/*
      PropertyDescriptor pd12 = new PropertyDescriptor(
        "Border", ESlateSplitPane.class,
        "getBorder", "setBorder"
      );
      pd12.setDisplayName(resources.getString("border"));
      pd12.setShortDescription(resources.getString("borderTip"));
*/

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 11];
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
      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultProperties;
    }
  }

}
