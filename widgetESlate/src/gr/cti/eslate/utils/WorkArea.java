package gr.cti.eslate.utils;

import java.awt.*;

/**
 * This class provides a method to obtain the size of the work area, i.e., the
 * size of the screen, taking the task bar into account.
 *
 * @version     2.0.6, 16-Jun-2006
 * @author      Kriton Kyrimis
 */
public class WorkArea
{
  /**
   * The constructor is private, as this class only provides a static method.
   */
  private WorkArea()
  {
  }

  /**
   * Returns the bounds of the work area.
   * @return    The bounds of the work area. This method will use native code,
   *            if available, or Java 1.4 functionality, otherwise. If neither
   *            is available, then the bounds of the screen will be returned,
   *            instead.
   */
  public static Rectangle getWorkArea()
  {
    return getWorkArea(new Rectangle());
  }

  /**
   * Returns the bounds of the work area.
   * @param     rect    The <code>Rectangle</code> to use to return the
   *                    results.
   * @return    A reference to the <code>rect</code> argument, which will be
   *            filled with containing the bounds of the work area.
   */
  public static Rectangle getWorkArea(Rectangle rect)
  {
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension screenSize = tk.getScreenSize();
    Insets ins;
    try {
      // Java 1.4 can obtain the work area's insets, which account for the
      // task bars.
      GraphicsConfiguration gc =
        GraphicsEnvironment.getLocalGraphicsEnvironment().
          getDefaultScreenDevice().getDefaultConfiguration();
      ins = tk.getScreenInsets(gc);
    } catch (Throwable th) {
      // If we are running under an earlier version of Java, the above will
      // fail. Assume zero insets, i.e., do not account for the task bar.
      ins = new Insets(0, 0, 0, 0);
    }
    // The size of the work area is the size of the screen minus any insets.
    int w = screenSize.width - ins.left - ins.right;
    int h = screenSize.height - ins.top - ins.bottom;
    rect.x = ins.left;
    rect.y = ins.top;
    rect.width = w;
    rect.height = h;
    return rect;
  }

}
