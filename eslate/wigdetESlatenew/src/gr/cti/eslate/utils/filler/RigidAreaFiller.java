package gr.cti.eslate.utils.filler;

import java.awt.Dimension;

/**
 * Corresponds to the shape created by
 * <code>Box.createRigidArea(Dimension dim)</code>. This shape is useful when
 * aligning components in an AWT container that uses the
 * <code>BoxLayout</code> layout manager.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 */

public class RigidAreaFiller extends Filler
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = -3294628928945794842L;
  
  Dimension area = new Dimension(10, 10);

  public RigidAreaFiller()
  {
    super(
      new Dimension(10, 10),
      new Dimension(10, 10),
      new Dimension(10, 10)
    );
  }

  /**
   * Sets the size of the rigid area.
   */
  public void setArea(Dimension area)
  {
    if (this.area.width == area.width && this.area.height == area.height) {
      return ;
    }
    this.area = area;
    reqMin = area;
    reqPref = area;
    reqMax = area;
  }

  /**
   * Returns the size of the rigid area.
   */
  public Dimension getArea()
  {
    return area;
  }

}
