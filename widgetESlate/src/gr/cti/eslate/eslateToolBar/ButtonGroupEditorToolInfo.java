package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.beans.*;
import javax.swing.*;

/**
 * Tool information required by the button group editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 */
class ButtonGroupEditorToolInfo
{
  /**
   * The name of the tool.
   */
  String name;
  /**
   * The tool. This is a reference to the tool itself.
   */
  AbstractButton tool;
  /**
   * The icon for the tool, taken from its BeanInfo.
   */
  Image image;

  /**
   * Construct a ButtonGroupToolInfo instance.
   * @param     name    The name of the tool.
   * @param     tool    The tool. This is a reference to the tool itself.
   */
  ButtonGroupEditorToolInfo(String name, AbstractButton tool)
  {
    this.name = name;
    this.tool = tool;
    Class<?> cl = tool.getClass();
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
