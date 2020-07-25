package gr.cti.eslate.eslateSplitPane;

import java.util.*;
import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * Custom property editor for the "orientation" property.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 */
public class OrientationPropertyEditor extends TaggedIntegerPropertyEditor
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = ESlateSplitPane.resources;

  /**
   * Construct the custom editor.
   */
  public OrientationPropertyEditor()
  {
    super(
      JSplitPane.ORIENTATION_PROPERTY,
      new int[] {JSplitPane.HORIZONTAL_SPLIT, JSplitPane.VERTICAL_SPLIT},
      new String[] {
        resources.getString("horizontal"),
        resources.getString("vertical")
      }
    );
  }
}
