package gr.cti.eslate.eslateToolBar.event;

import java.util.*;

/**
 * Listener for events fired by the tools contained in the E-Slate
 * toolbar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public interface ToolBarListener extends EventListener
{
  /**
   * Invoked when the mouse button has been clicked (pressed and released) on
   * a tool.
   * @param     e       The event signaling the click.
   */
  public void mouseClicked(ToolBarEvent e);

  /**
   * Invoked when a mouse button has been pressed on a tool.
   * @param     e       The event signaling the press.
   */
  public void mousePressed(ToolBarEvent e);

  /**
   * Invoked when a mouse button has been released on a tool.
   * @param     e       The event signaling the press.
   */
  public void mouseReleased(ToolBarEvent e);

  /**
   * Invoked when the mouse enters a tool.
   * @param     e       The event signaling the entry.
   */
  public void mouseEntered(ToolBarEvent e);

  /**
   * Invoked when the mouse exits a tool.
   * @param     e       The event signaling the exit.
   */
  public void mouseExited(ToolBarEvent e);

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
  public void mouseDragged(ToolBarEvent e);

  /**
   * Invoked when the mouse button has been moved on a tool (with no
   * buttons down).
   * @param     e       The event signaling the move.
   */
  public void mouseMoved(ToolBarEvent e);

  /**
   * Invoked when an action occurs on a tool.
   */
  public void actionPerformed(ToolBarEvent e);
}
