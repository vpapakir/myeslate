package gr.cti.eslate.propertyEditors;

/**
 * Property editor for positive integer properties.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class PositiveIntegerPropertyEditor extends IntegerPropertyEditor
{
  /**
   * Construct a <code>PositiveIntegerPropertyEditor</code> instance.
   */
  public PositiveIntegerPropertyEditor()
  {
    super();
    setMinValue(1);
  }
}
