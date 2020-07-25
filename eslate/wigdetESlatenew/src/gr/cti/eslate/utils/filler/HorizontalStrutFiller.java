package gr.cti.eslate.utils.filler;

import java.awt.Dimension;

/**
 * Corresponds to the shape created by
 * <code>Box.createHorizontalStrut(int width)</code>.
 * This shape is useful when aligning components in an AWT container that
 * uses the <code>BoxLayout</code> layout manager.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 */

public class HorizontalStrutFiller extends Filler
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 9087164253399645756L;
  
  int width = 10;

  public HorizontalStrutFiller()
  {
    super(
      new Dimension(10, 0),
      new Dimension(10, 0),
      new Dimension(10, Short.MAX_VALUE)
    );
  }

  /**
   * Adjusts the width of the horizontal strut.
   */
  public void setWidth(int width)
  {
    if (this.width == width) {
      return ;
    }
    this.width = width;
    reqMin = new Dimension(width, 0);
    reqPref = new Dimension(width, 0);
    reqMax = new Dimension(width, Short.MAX_VALUE);
  }

  /**
   * Returns the width of the horizontal strut.
   */
  public int getWidth()
  {
    return width;
  }
}
