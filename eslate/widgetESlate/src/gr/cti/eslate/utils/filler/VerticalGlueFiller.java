package gr.cti.eslate.utils.filler;

import java.awt.Dimension;

/**
 * Corresponds to the shape created by <code>Box.createVerticalGlue().</code>
 * This shape is useful when aligning components in an AWT container that
 * uses the <code>BoxLayout</code> layout manager.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 */

public class VerticalGlueFiller extends Filler
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = -5110096350831000315L;
  
  public VerticalGlueFiller()
  {
    super(
     new Dimension(0, 0),
     new Dimension(0, 0),
     new Dimension(0, Short.MAX_VALUE)
    );
  }
}
