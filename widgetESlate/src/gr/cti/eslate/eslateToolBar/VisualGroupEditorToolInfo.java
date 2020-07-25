package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.beans.*;

/**
 * Tool information required by the visual group editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 */
class VisualGroupEditorToolInfo
{
  /**
   * The name of the tool.
   */
  String name;
  /**
   * The tool. This is either a reference to the tool itself, if the tool is
   * already on the toolbar, or a reference to the tool's class, if the tool
   * has yet to be placed on the toolbar.
   */
  Object tool;
  /**
   * The text associated with the tool.
   */
  String text;
  /**
   * Tool visibility flag.
   */
  boolean visible;
  /**
   * The icon for the tool, taken from its BeanInfo.
   */
  Image image;

  /**
   * Construct a VisualGroupToolInfo instance.
   * @param     name    The name of the tool.
   * @param     tool    The tool. This is either a reference to the tool
   *                    itself, if the tool is already on the toolbar, or a
   *                    reference to the tool's class, if the tool has yet
   *                    to be placed on the toolbar.
   * @param     text    The text associated with the tool.
   * @param     visible Tool visibility flag.
   */
  VisualGroupEditorToolInfo(
    String name, Object tool, String text, boolean visible)
  {
    this.name = name;
    this.tool = tool;
    this.text = text;
    this.visible = visible;
    Class<?> cl;
    if (tool instanceof Class) {
      cl = (Class<?>)tool;
    }else{
      cl = tool.getClass();
    }
    try {
      BeanInfo bi = Introspector.getBeanInfo(cl);
      image = bi.getIcon(BeanInfo.ICON_COLOR_16x16);
      if (image == null) {
        image = bi.getIcon(BeanInfo.ICON_MONO_16x16);
      }
    }catch (Exception e) {
      image = null;
    }
  }

}
