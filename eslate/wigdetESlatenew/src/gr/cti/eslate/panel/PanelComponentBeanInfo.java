package gr.cti.eslate.panel;

import java.beans.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;
import gr.cti.eslate.shapedComponent.*;

/**
 * BeanInfo for Panel component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class PanelComponentBeanInfo extends ESlateBeanInfo
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = PanelComponent.resources;
    
  /**
   * Construct the BeanInfo.
   */
  public PanelComponentBeanInfo()
  {
    super();
    try {
      Class<?> c = PanelComponentBeanInfo.class;
      set16x16ColorIcon(new ImageIcon(c.getResource("images/panel16c.gif")));
      set16x16MonoIcon(new ImageIcon(c.getResource("images/panel16m.gif")));
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
        "border", PanelComponent.class,
        "getBorder", "setBorder"
      );
      pd1.setDisplayName(resources.getString("border"));
      pd1.setShortDescription(resources.getString("borderTip"));

      PropertyDescriptor pd2 = new PropertyDescriptor(
        "designMode", PanelComponent.class,
        "isDesignMode", "setDesignMode"
      );
      pd2.setDisplayName(resources.getString("designMode"));
      pd2.setShortDescription(resources.getString("designModeTip"));

      PropertyDescriptor pd3 = new PropertyDescriptor(
        "contentPaneBackground", PanelComponent.class,
        "getContentPaneBackground", "setContentPaneBackground"
      );
      pd3.setDisplayName(resources.getString("background"));
      pd3.setShortDescription(resources.getString("backgroundTip"));

      PropertyDescriptor pd4 = new PropertyDescriptor(
        "contentPaneLayout", PanelComponent.class,
        "getContentPaneLayout", "setContentPaneLayout"
      );
      pd4.setDisplayName(resources.getString("layout"));
      pd4.setShortDescription(resources.getString("layoutTip"));

      PropertyDescriptor pd5 = new PropertyDescriptor(
        "backgroundImage", PanelComponent.class,
        "getBackgroundImage", "setBackgroundImage"
      );
      pd5.setDisplayName(resources.getString("backgroundImage"));
      pd5.setShortDescription(resources.getString("backgroundImageTip"));

      PropertyDescriptor pd6 = new PropertyDescriptor(
        "transparent", PanelComponent.class,
        "isTransparent", "setTransparent"
      );
      pd6.setDisplayName(resources.getString("transparent"));
      pd6.setShortDescription(resources.getString("transparentTip"));

      PropertyDescriptor pd7 = new PropertyDescriptor(
        "backgroundImageStyle", PanelComponent.class,
        "getBackgroundImageStyle", "setBackgroundImageStyle"
      );
      pd7.setDisplayName(resources.getString("bgStyle"));
      pd7.setShortDescription(resources.getString("bgStyleTip"));
      pd7.setPropertyEditorClass(BackgroundStyleEditor.class);

      PropertyDescriptor pd8 = new PropertyDescriptor(
        "gridVisible", PanelComponent.class,
        "isGridVisible", "setGridVisible"
      );
      pd8.setDisplayName(resources.getString("gridVisible"));
      pd8.setShortDescription(resources.getString("gridVisibleTip"));

      PropertyDescriptor pd9 = new PropertyDescriptor(
        "gridStep", PanelComponent.class,
        "getGridStep", "setGridStep"
      );
      pd9.setDisplayName(resources.getString("gridStep"));
      pd9.setShortDescription(resources.getString("gridStepTip"));

      PropertyDescriptor pd10 = new PropertyDescriptor(
        "gridColor", PanelComponent.class,
        "getGridColor", "setGridColor"
      );
      pd10.setDisplayName(resources.getString("gridColor"));
      pd10.setShortDescription(resources.getString("gridColorTip"));

      PropertyDescriptor pd11 = new PropertyDescriptor(
        "alignToGrid", PanelComponent.class,
        "isAlignToGrid", "setAlignToGrid"
      );
      pd11.setDisplayName(resources.getString("alignToGrid"));
      pd11.setShortDescription(resources.getString("alignToGridTip"));

      PropertyDescriptor pd12 = new PropertyDescriptor(
        "selectionColor", PanelComponent.class,
        "getSelectionColor", "setSelectionColor"
      );
      pd12.setDisplayName(resources.getString("selectionColor"));
      pd12.setShortDescription(resources.getString("selectionColorTip"));

      PropertyDescriptor pd13 = new PropertyDescriptor(
        "PreferredSize", PanelComponent.class,
        "getPreferredSize", "setPreferredSize"
      );
      pd13.setDisplayName(resources.getString("preferredSize"));
      pd13.setShortDescription(resources.getString("preferredSizeTip"));
      pd13.setExpert(true);

      PropertyDescriptor pd14 = new PropertyDescriptor(
        "MinimumSize", PanelComponent.class,
        "getMinimumSize", "setMinimumSize"
      );
      pd14.setDisplayName(resources.getString("minimumSize"));
      pd14.setShortDescription(resources.getString("minimumSizeTip"));
      pd14.setExpert(true);

      PropertyDescriptor pd15 = new PropertyDescriptor(
        "MaximumSize", PanelComponent.class,
        "getMaximumSize", "setMaximumSize"
      );
      pd15.setDisplayName(resources.getString("maximumSize"));
      pd15.setShortDescription(resources.getString("maximumSizeTip"));
      pd15.setExpert(true);

      PropertyDescriptor pd16 = new PropertyDescriptor(
        "ClipShapeType", PanelComponent.class,
        "getClipShapeType", "setClipShapeType"
      );
      pd16.setDisplayName(resources.getString("clipShapeType"));
      pd16.setShortDescription(resources.getString("clipShapeTypeTip"));
      pd16.setPropertyEditorClass(ShapeTypeEditor.class);
      pd16.setExpert(true);

      PropertyDescriptor[] pd =
        new PropertyDescriptor[defaultProperties.length + 16];
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
      pd[i++] = pd16;
      return pd;
    } catch (Exception e) {
      e.printStackTrace();
      return defaultProperties;
    }
  }

}
