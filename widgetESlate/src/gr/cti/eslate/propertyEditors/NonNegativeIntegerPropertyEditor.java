package gr.cti.eslate.propertyEditors;

/**
 * Property editor for non-negative integer properties.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class NonNegativeIntegerPropertyEditor extends IntegerPropertyEditor
{
  /**
   * Construct a <code>NonNegativeIntegerPropertyEditor</code> instance.
   */
  public NonNegativeIntegerPropertyEditor()
  {
    super();
    setMinValue(0);
  }
}
