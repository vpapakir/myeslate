package gr.cti.eslate.utils.filler;

import java.awt.Dimension;

/**
 * Corresponds to the shape created by
 * <code>Box.createVerticalStrut(int height).</code>
 * This shape is useful when aligning components in an AWT container that
 * uses the <code>BoxLayout</code> layout manager.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 */

public class VerticalStrutFiller extends Filler
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = -7987152834761600073L;
  
  int height = 10;

  public VerticalStrutFiller()
  {
    super(
      new Dimension(0, 10),
      new Dimension(0, 10),
      new Dimension(Short.MAX_VALUE, 10)
    );
  }

  /**
   * Adjusts the height of the vertical strut.
   */
  public void setHeight(int height)
  {
    if (this.height == height) {
      return ;
    }
    this.height = height;
    reqMin = new Dimension(0, height);
    reqPref = new Dimension(0, height);
    reqMax = new Dimension(Short.MAX_VALUE, height);
  }

  /**
   * Returns the height of the vertical strut.
   */
  public int getHeight()
  {
    return height;
  }
}
