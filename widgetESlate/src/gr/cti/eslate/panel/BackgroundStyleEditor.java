package gr.cti.eslate.panel;

import java.util.*;

import gr.cti.eslate.utils.*;

/**
 * Custom editor for the Panel component's "background image style" property.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class BackgroundStyleEditor extends TaggedIntegerPropertyEditor
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = PanelComponent.resources;

  /**
   * Construct the custom editor.
   */
  public BackgroundStyleEditor()
  {
    super("backgroundImageStyle", getStyles(), getStyleNames());
  }

  /**
   * Returns an array with the possible styles.
   * @return    The requested array.
   */
  private static int[] getStyles()
  {
    int[] style = new int[4];
    style[0] = PanelComponent.BG_NONE;
    style[1] = PanelComponent.BG_CENTERED;
    style[2] = PanelComponent.BG_STRETCHED;
    style[3] = PanelComponent.BG_TILED;
    return style;
  }

  /**
   * Returns an array with the names of the possible styles.
   * @return    The requested array.
   */
  private static String[] getStyleNames()
  {
    String[] name = new String[4];
    name[0] = resources.getString("none");
    name[1] = resources.getString("centered");
    name[2] = resources.getString("stretched");
    name[3] = resources.getString("tiled");
    return name;
  }
}
