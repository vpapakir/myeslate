package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

/**
 * This class implements the buttons that appear on the toolbar when its tools
 * do not fit in the available space.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
class ScrollButton extends JButton
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Scroll forward.
   */
  public final static int FORWARD = 0;
  /**
   * Scroll back.
   */
  public final static int BACK = 1;
  /**
   * The toolbar on which the button appears.
   */
  private ESlateToolBar toolBar;
  /**
   * The direction in which to roll the toolbar when pressing the button.
   */
  private int direction;
  /**
   * The timer used to roll the toolbar at regular intervals while the mouse
   * is inside the button.
   */
  private Timer rollTimer;

  /**
   * Create a ScrollButton.
   * @param     toolBar         The toolbar on which the button will appear.
   * @param     direction       The direction in which to roll the toolbar
   *                            when pressing the button. One of
   *                            <code>FORWARD</code>, <code>BACK</code>.
   */
  ScrollButton(ESlateToolBar toolBar, int direction)
  {
    super();
    this.toolBar = toolBar;
    this.direction = direction;
    Dimension d = getPreferredSize();
    Dimension prefSize = toolBar.actualPrefSize;
    int orientation = toolBar.getOrientation();
    ImageIcon icon;
    Border border = new CompoundBorder(
      new SoftBevelBorder(BevelBorder.LOWERED),
      new BasicBorders.MarginBorder()
    );
    setBorder(border);
    if (orientation == ESlateToolBar.HORIZONTAL) {
      int fill = prefSize.height - d.height;
      int fill1 = fill / 2;
      int fill2 = fill - fill1;
      setMargin(new Insets(fill1, 0, fill2, 0));
      if (direction == FORWARD) {
        icon =
          new ImageIcon(ScrollButton.class.getResource("images/right.gif"));
      }else{
        icon = new ImageIcon(ScrollButton.class.getResource("images/left.gif"));
      }
    }else{
      int fill = prefSize.width - d.width;
      int fill1 = fill / 2;
      int fill2 = fill - fill1;
      setMargin(new Insets(0, fill1, 0, fill2));
      if (direction == FORWARD) {
        icon = new ImageIcon(ScrollButton.class.getResource("images/down.gif"));
      }else{
        icon = new ImageIcon(ScrollButton.class.getResource("images/up.gif"));
      }
    }
    setIcon(icon);
    setFocusPainted(false);
    setBorderPainted(false);
    rollTimer = new Timer(500, new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        ScrollButton.this.toolBar.scroll(ScrollButton.this.direction);
      }
    });
    addMouseListener(new MouseAdapter(){
      public void mouseEntered(MouseEvent e)
      {
        setBorderPainted(true);
        rollTimer.start();
      }
      public void mouseExited(MouseEvent e)
      {
        setBorderPainted(false);
        rollTimer.stop();
      }
    });
  }

  /**
   * Performs the cleanup required after removing the button from its parent.
   */
  void cleanup()
  {
    rollTimer.stop();
    setBorderPainted(false);
  }

}
