package gr.cti.eslate.editor;

import java.util.*;

/**
 * Listener for undo/redo status change events.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public interface UndoRedoStatusListener extends EventListener
{
  /**
   * Invoked when the undo/redo status of the editor has changed.
   * @param     evt     The event describing the change.
   */
  public void undoRedoStatusChanged(UndoRedoStatusEvent evt);
}
