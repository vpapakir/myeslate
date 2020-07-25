package gr.cti.eslate.distance;

import gr.cti.eslate.utils.*;

/**
 * Custom editor for the Distance component's "unit" property.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class UnitEditor extends TaggedStringPropertyEditor
{
  /**
   * Construct the custom editor.
   */
  public UnitEditor()
  {
    super("unit", Distance.getUnitNames());
  }
}
