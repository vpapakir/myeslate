package gr.cti.eslate.set;

import gr.cti.eslate.utils.*;

/**
 * Custom editor for the Set component's "calcOp" property.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 */
public class CalcOpEditor extends TaggedStringPropertyEditor
{
  /**
   * Construct the custom editor.
   */
  public CalcOpEditor()
  {
    super("calcOpt", Set.getCalculationOperations());
  }
}
