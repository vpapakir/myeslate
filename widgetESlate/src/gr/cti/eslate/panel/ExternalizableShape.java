package gr.cti.eslate.panel;

import java.awt.*;

/**
 * This class encapsulates a shape, providing methods for reading and writing
 * the shape to a file. This is just a wrapper for
 * <code>gr.cti.eslate.shapedComponent.ExternalizableShape</code>, used for
 * compatibility with panels when this class was in the
 * <code>gr.cti.eslate.panel</code> package.
 *
 * @version     2.0.0, 24-May-2006
 * @author      Kriton Kyrimis
 */
public class ExternalizableShape
  extends gr.cti.eslate.shapedComponent.ExternalizableShape
{
  static final long serialVersionUID = 7879536194076778220L;

  /**
   * Construct an <code>ExternalizableShape</code> instance.
   * @param     shape   The shape to encapsulate.
   */
  public ExternalizableShape(Shape shape)
  {
    super(shape);
  }

  /**
   * Empty constructor, for use in combination with <code>readExternal</code>.
   */
  public ExternalizableShape()
  {
    super();
  }

}
