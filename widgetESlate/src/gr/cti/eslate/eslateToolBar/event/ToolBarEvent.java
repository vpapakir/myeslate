package gr.cti.eslate.eslateToolBar.event;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import gr.cti.eslate.base.*;

/**
 * Event fired by the tools contained in the E-Slate toolbar.
 * This event encapsulates either a MouseEvent or an ActionEvent, providing
 * the methods of both these classes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class ToolBarEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The encapsulated MouseEvent.
   */
  private MouseEvent me = null;
  /**
   * The encapsulated ActionEvent.
   */
  private ActionEvent ae = null;
  /**
   * The E-Slate handle of the tool that generated the event.
   */
  private ESlateHandle toolHandle;
  /**
   * The text associated with the tool that generated the event.
   */
  private String text;
  /**
   * Indicates that the event encapsulates a MouseEvent.
   */
  private final static int MOUSE_EVENT = 0;
  /**
   * Indicates that the event encapsulates an ActionEvent.
   */
  private final static int ACTION_EVENT = 1;
  /**
   * The type of the encapsulated event. One of <code>MOUSE_EVENT</code> or
   * <code>ACTION_EVENT</code>.
   */
  private int type;

  /**
   * Creates a ToolBarEvent instance.
   * @param     me      The encapsulated MouseEvent.
   * @param     handle  The E-Slate handle of the tool that generated the
   *                    event.
   * @param     text    The text that has been associated with the tool by
   *                    the toolbar.
   */
  public ToolBarEvent(MouseEvent me, ESlateHandle handle, String text)
  {
    super(me.getSource());

    type = MOUSE_EVENT;
    this.me = me;
    ae = null;
    this.toolHandle = handle;
    this.text = text;
  }

  /**
   * Creates a ToolBarEvent instance.
   * @param     ae      The encapsulated ActionEvent
   * @param     handle  The E-Slate handle of the tool that generated the
   *                    event.
   * @param     text    The text that has been associated with the tool by
   *                    the toolbar.
   */
  public ToolBarEvent(ActionEvent ae, ESlateHandle handle, String text)
  {
    super(ae.getSource());

    type = ACTION_EVENT;
    this.ae = ae;
    me = null;
    this.toolHandle = handle;
    this.text = text;
  }

  /**
   * Returns the type of event encapsulated by this event.
   * @return    One of <code>MOUSE_EVENT</code>, <code>ACTION_EVENT</code>.
   */
  public int getType()
  {
    return type;
  }

  /**
   * Returns the tool that generated the event.
   * @return    The tool that generated the event.
   */
  public Component getTool()
  {
    return (Component)(toolHandle.getComponent());
  }

  /**
   * Returns the name of the tool that generated the event.
   * @return    The name of the tool that generated the event.
   */
  public String getToolName()
  {
    return toolHandle.getComponentName();
  }

  /**
   * Returns the E-Slate handle of the tool that generated the event.
   * @return    The E-Slate handle of the tool that generated the event.
   */
  public ESlateHandle getToolHandle()
  {
    return toolHandle;
  }

  /**
   * Returns the text associated by the toolbar with the tool that generated
   * the event.
   * @return    The text associated by the toolbar with the tool that
   *            generated the event.
   */
  public String getAssociatedText()
  {
    return text;
  }

  /**
   * Returns the horizontal x position of the event relative to the tool that
   * generated the event.
   * @return    The horizontal x position of the event relative to the tool
   *            that generated the event. If the encapsulated event is not a
   *            <code>MouseEvent</code>, -1 is returned.
   */
  public int getX()
  {
    if (type == MOUSE_EVENT) {
      return me.getX();
    }else{
      return -1;
    }
  }

  /**
   * Returns the vertical y position of the event relative to the tool that
   * generated the event.
   * @return    The vertical y position of the event relative to the tool
   *            that generated the event. If the encapsulated event is not a
   *            <code>MouseEvent</code>, -1 is returned.
   */
  public int getY()
  {
    if (type == MOUSE_EVENT) {
      return me.getY();
    }else{
      return -1;
    }
  }

  /**
   * Returns the x,y position of the event relative to the tool that
   * generated the event.
   * @return    A <code>Point</code> object containing the x and y coordinates
   *            relative to the tool that generated the event. If the
   *            encapsulated event is not a <code>MouseEvent</code>,
   *            <code>null</code> is returned.
   */
  public Point getPoint()
  {
    if (type == MOUSE_EVENT) {
      return me.getPoint();
    }else{
      return null;
    }
  }

  /**
   * Translates the event's coordinates to a new position by adding specified
   * <code>x</code> (horizontal) and <code>y</code> (vertical) offsets.
   * If the encapsulated event is not a <code>MouseEvent</code>, this method
   * does nothing.
   * @param     x       The horizontal x value to add to the current x
   *                    coordinate position.
   * @param     y       The vertical y value to add to the current y
   *                    coordinate position.
   */
  public void translatePoint(int x, int y)
  {
    if (type == MOUSE_EVENT) {
      me.translatePoint(x, y);
    }
  }

  /**
   * Returns the number of mouse clicks associated with this event.
   * @return    Integer value for the number of clicks. If the encapsulated
   * event is not a <code>MouseEvent</code>, -1 is returned.
   */
  public int getClickCount()
  {
    if (type == MOUSE_EVENT) {
      return me.getClickCount();
    }else{
      return -1;
    }
  }

  /**
   * Returns whether or not this event is the popup menu trigger event for the
   * platform.
   * @return    True if this event is the popup menu trigger for this
   *            platform, false otherwise.
   */
  public boolean isPopupTrigger()
  {
    if (type == MOUSE_EVENT) {
      return me.isPopupTrigger();
    }else{
      return false;
    }
  }

  /**
   * Returns a parameter string identifying this event. This method is useful
   * for event-logging and for debugging.
   * @return    The requested string.
   */
  public String paramString()
  {
    if (type == MOUSE_EVENT) {
      return me.paramString();
    }else{
      return ae.paramString();
    }
  }

  /**
   * Returns the command string associated with this action. This string
   * allows a "modal" component to specify one of several commands, depending
   * on its state. For example, a single button might toggle between "show
   * details" and "hide details". The source object and the event would be the
   * same in each case, but the command string would identify the intended
   * action.
   * @return    The requested string. If the encapsulated event is not an
   *            <code>ActionEvent</code>, <code>null</code> is returned.
   */
  public String getActionCommand()
  {
    if (type == ACTION_EVENT) {
      return ae.getActionCommand();
    }else{
      return null;
    }
  }

  /**
   * Returns the modifier keys held down during this event.
   * @return    The bitwise-or of the modifier constants. If the encapsulated
   *            event is not an <code>ActionEvent</code>, <code>0</code> is
   *            returned.
   */
  public int getModifiers()
  {
    if (type == ACTION_EVENT) {
      return ae.getModifiers();
    }else{
      return 0;
    }
  }

}
