package gr.cti.eslate.eslateToolBar;

import java.util.*;

/**
 * Custom editor for the "separation" property, which is a Dimension with
 * a valid <code>null</code> value.
 * The editor takes the form of two spin buttons (width and height), and a
 * check box. When the check box is checked, the value of the separation is
 * <code>null</code>, otherwise it is the value specified by the spin boxes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class SeparationEditor extends DimensionEditor
{
  public SeparationEditor()
  {
    super();
    propertyName = "Separation";
  }

  /**
   * Initialize resources.
   */
  protected void initResources()
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.eslateToolBar.SeparationEditorResource",
      Locale.getDefault()
    );
  }
}
