package gr.cti.eslate.editor;

import java.util.*;

/**
 * Event triggered by changes in the undo/redo status of the editor.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class UndoRedoStatusEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  boolean undoState;
  boolean redoState;

  /**
   * Create an undo/redo status event.
   * @param     source          The source of the event.
   * @param     undoState       True if undo is currently enabled, false
   *                            otherwise.
   * @param     redoState       True if redo is currently enabled, false
   *                            otherwise.
   */
  public UndoRedoStatusEvent(Object source, boolean undoState,
                             boolean redoState)
  {
    super(source);
    this.undoState = undoState;
    this.redoState = redoState;
  }

  /**
   * Checks whether undo is currently enabled.
   * @return    True if yes, false otherwise.
   */
  public boolean canUndo()
  {
    return undoState;
  }

  /**
   * Checks whether redo is currently enabled.
   * @return    True if yes, false otherwise.
   */
  public boolean canRedo()
  {
    return redoState;
  }
}
