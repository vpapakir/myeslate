package gr.cti.eslate.set;

import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Set component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 */
public class SetBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.set.SetResource",
    Locale.getDefault()
  );

  /**
   * Construct the BeanInfo.
   */
  public SetBeanInfo()
  {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("images/set16c.gif"))
      );
      set16x16MonoIcon(
        new ImageIcon(getClass().getResource("images/set16m.gif"))
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
        "calcKey", Set.class,
        "getCalcKey", "setCalcKey"
      );
      pd1.setDisplayName(resources.getString("setCalcKey"));
      pd1.setShortDescription(resources.getString("setCalcKeyTip"));

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "calcOp", Set.class,
        "getCalcOp", "setCalcOp"
      );
      pd2.setDisplayName(resources.getString("setCalcOp"));
      pd2.setShortDescription(resources.getString("setCalcOpTip"));
      pd2.setPropertyEditorClass(CalcOpEditor.class);

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "projectionField", Set.class,
        "getProjectionField", "setProjectionField"
      );
      pd3.setDisplayName(resources.getString("setProjectionField"));
      pd3.setShortDescription(resources.getString("setProjectionFieldTip"));

      PropertyDescriptor pd4 = new PropertyDescriptor(
        "selectedTable", Set.class,
        "getSelectedTable", "setSelectedTable"
      );
      pd4.setDisplayName(resources.getString("setSelectedTable"));
      pd4.setShortDescription(resources.getString("setSelectedTableTip"));

      PropertyDescriptor pd5 = new PropertyDescriptor(
        "showLabels", Set.class,
        "isShowLabels", "setShowLabels"
      );
      pd5.setDisplayName(resources.getString("setShowLabels"));
      pd5.setShortDescription(resources.getString("setShowLabelsTip"));

      PropertyDescriptor pd6 = new PropertyDescriptor(
        "uniformProjection", Set.class,
        "isUniformProjection", "setUniformProjection"
      );
      pd6.setDisplayName(resources.getString("uniformProjection"));
      pd6.setShortDescription(resources.getString("uniformProjectionTip"));

      PropertyDescriptor pd7 = new PropertyDescriptor(
        "background", Set.class,
        "getBackgroundColor", "setBackgroundColor"
      );
      pd7.setDisplayName(resources.getString("background"));
      pd7.setShortDescription(resources.getString("backgroundTip"));

      PropertyDescriptor pd8 = new PropertyDescriptor(
        "selectionColor", Set.class,
        "getSelectionColor", "setSelectionColor"
      );
      pd8.setDisplayName(resources.getString("selectionColor"));
      pd8.setShortDescription(resources.getString("selectionColorTip"));

      PropertyDescriptor pd9 = new PropertyDescriptor(
        "toolBarVisible", Set.class,
        "isToolBarVisible", "setToolBarVisible"
      );
      pd9.setDisplayName(resources.getString("toolBarVisible"));
      pd9.setShortDescription(resources.getString("toolBarVisibleTip"));

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 9];
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
      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultProperties;
    }
  }

}
