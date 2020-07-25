package gr.cti.eslate.eslateToolBar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.eslateToolBar.event.*;

/**
 * A wrapper that converts a <code>ToolBarListener</code> into a
 * <code>MouseListener</code>, <code>MouseMotionListener</code>, and an
 * <code>AcctionListener</code>.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class TBLWrapper
  implements EventListener, MouseListener, MouseMotionListener, ActionListener
{
  /**
   * The encapsulated <code>ToolBarListener</code>.
   */
  private ToolBarListener tbl;

  /**
   * Construct a <code>TBLWrapper</code> instance.
   * @param     tbl     The encapsulated <code>ToolBarListener</code>.
   */
  public TBLWrapper(ToolBarListener tbl)
  {
    super();
    this.tbl = tbl;
  }

  /**
   * Invoked when the mouse button has been clicked (pressed and released) on
   * a tool.
   * @param     e       The event signaling the click.
   */
  public void mouseClicked(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mouseClicked(tbe);
  }

  /**
   * Invoked when a mouse button has been pressed on a tool.
   * @param     e       The event signaling the press.
   */
  public void mousePressed(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mousePressed(tbe);
  }

  /**
   * Invoked when a mouse button has been released on a tool.
   * @param     e       The event signaling the press.
   */
  public void mouseReleased(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mouseReleased(tbe);
  }

  /**
   * Invoked when the mouse enters a tool.
   * @param     e       The event signaling the entry.
   */
  public void mouseEntered(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mouseEntered(tbe);
  }

  /**
   * Invoked when the mouse exits a tool.
   * @param     e       The event signaling the exit.
   */
  public void mouseExited(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mouseExited(tbe);
  }

  /**
   * Invoked when a mouse button is pressed on a tool and then dragged.
   * <code>MOUSE_DRAGGED</code> events will continue to be delivered to the
   * component where the drag originated until the mouse button is released
   * (regardless of whether the mouse position is within the bounds of the
   * component).
   * <P>
   * Due to platform-dependent Drag&amp;Drop implementations,
   * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
   * Drag&amp;Drop operation.
   * @param     e       The event signaling the drag.
   */
  public void mouseDragged(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mouseDragged(tbe);
  }

  /**
   * Invoked when the mouse button has been moved on a tool (with no
   * buttons down).
   * @param     e       The event signaling the move.
   */
  public void mouseMoved(MouseEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.mouseMoved(tbe);
  }

  /**
   * Invoked when an action occurs on a tool.
   */
  public void actionPerformed(ActionEvent e)
  {
    ToolBarEvent tbe = makeToolBarEvent(e);
    tbl.actionPerformed(tbe);
  }

  /**
   * Constructs a <code>ToolBarEvent</code> from a <code>MouseEvent</code>.
   * @param     me      The <code>MouseEvent</code> to convert.
   */
  private ToolBarEvent makeToolBarEvent(MouseEvent me)
  {
    Component c = (Component)(me.getSource());
    ESlateToolBar tBar = (ESlateToolBar)(c.getParent());
    ESlateHandle h = tBar.getESlateHandle(c);
    String text = tBar.getAssociatedText((Component)(h.getComponent()));
    return new ToolBarEvent(me, h, text);
  }

  /**
   * Constructs a <code>ToolBarEvent</code> from an <code>ActionEvent</code>.
   * @param     ae      The <code>ActionEvent</code> to convert.
   */
  private ToolBarEvent makeToolBarEvent(ActionEvent ae)
  {
    Component c = (Component)(ae.getSource());
    ESlateToolBar tBar = (ESlateToolBar)(c.getParent());
    ESlateHandle h = tBar.getESlateHandle(c);
    String text = tBar.getAssociatedText((Component)(h.getComponent()));
    return new ToolBarEvent(ae, h, text);
  }
  
}
