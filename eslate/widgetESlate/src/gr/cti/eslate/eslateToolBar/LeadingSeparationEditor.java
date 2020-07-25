package gr.cti.eslate.eslateToolBar;

/**
 * Custom editor for the "leading separation" property, which is a Dimension
 * with a valid <code>null</code> value.
 * The editor takes the form of two spin buttons (width and height), and a
 * check box. When the check box is checked, the value of the separation is
 * <code>null</code>, otherwise it is the value specified by the spin boxes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class LeadingSeparationEditor extends SeparationEditor
{
  /**
   * Create a LeadingSeparationEditor instance.
   */
  public LeadingSeparationEditor()
  {
    super();
    propertyName = "LeadingSeparation";
  }
}
