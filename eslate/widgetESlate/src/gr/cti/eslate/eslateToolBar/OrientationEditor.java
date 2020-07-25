package gr.cti.eslate.eslateToolBar;

import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * Custom editor for the Toolbar component's "orientation" property.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class OrientationEditor extends TaggedIntegerPropertyEditor
{
  /**
   * Construct the custom editor.
   */
  public OrientationEditor()
  {
    super("orientation", getOrientations(), getOrientationNames());
  }

  /**
   * Returns an array with the possible orientations.
   * @return    The requested array.
   */
  private static int[] getOrientations()
  {
    int[] orientation = new int[2];
    orientation[0] = JToolBar.HORIZONTAL;
    orientation[1] = JToolBar.VERTICAL;
    return orientation;
  }

  /**
   * Returns an array with the names of the possible orientations.
   * @return    The requested array.
   */
  private static String[] getOrientationNames()
  {
    ResourceBundle resources = ResourceBundle.getBundle(
      "gr.cti.eslate.eslateToolBar.ToolBarResource",
      Locale.getDefault()
    );
    String[] name = new String[2];
    name[0] = resources.getString("horizontal");
    name[1] = resources.getString("vertical");
    return name;
  }
}
