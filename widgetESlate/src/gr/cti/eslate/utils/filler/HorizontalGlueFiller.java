package gr.cti.eslate.utils.filler;

import java.awt.Dimension;

/**
 * Corresponds to the shape created by Box.createHorizontalGlue(). This shape
 * is useful when aligning components in an AWT container that uses the
 * <code>BoxLayout</code> layout manager.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 */

public class HorizontalGlueFiller extends Filler
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = -5396086989654248560L;
  
  public HorizontalGlueFiller()
  {
    super(
      new Dimension(0, 0),
      new Dimension(0, 0),
      new Dimension(Short.MAX_VALUE, 0)
    );
  }
}
