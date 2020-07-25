package gr.cti.eslate.eslateSplitPane;

import gr.cti.eslate.propertyEditors.*;

/**
 * Property editor for dividider location property. This property is an
 * integer with a minimum allowed value of -1.
 *
 * @version     2.0.0, 25-May-2006
 * @author      Kriton Kyrimis
 */
public class DividerLocationPropertyEditor extends IntegerPropertyEditor
{
  /**
   * Property name.
   */
  protected String propertyName = "DividerLocation";

  /**
   * Create a <code>DividerLocationPropertyEditor</code> instance.
   */
  public DividerLocationPropertyEditor()
  {
    super();
    setMinValue(-1);
  }
}
