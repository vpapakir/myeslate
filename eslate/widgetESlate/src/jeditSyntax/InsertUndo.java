package jeditSyntax;
/*
 * InsertUndo - Undo edit for SyntaxDocument.
 * Copyright (C) 2006 Kriton Kyrimis
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

import javax.swing.text.*;
import javax.swing.undo.*;

class InsertUndo extends AbstractUndoableEdit
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Offset of altered text.
   */
  int offset = -1;
  /**
   * Removed text.
   */
  String removedText = "";
  /**
   * Inserted text.
   */
  String insertedText = "";
  /**
   * Document on which the change was made.
   */
  private SyntaxDocument document;

  InsertUndo(SyntaxDocument document)
  {
    this.document = document;
  }

  public boolean isSignificant()
  {
    return true;
  }

  public String getPresentationName()
  {
    return "document edit";
  }

  public void undo() throws CannotUndoException
  {
    super.undo();
    if (offset >= 0) {
      int l = insertedText.length();
      try {
        if (l > 0) {
          document.remove(offset, l);
        }
        document.insertString(offset, removedText, null);
      } catch (BadLocationException e) {
        // Shouldn't happen.
        e.printStackTrace();
      }
    }
  }

  public void redo() throws CannotRedoException
  {
    super.redo();
    if (offset >= 0) {
      int l = removedText.length();
      try {
        if (l > 0) {
          document.remove(offset, l);
        }
        document.insertString(offset, insertedText, null);
      } catch (BadLocationException e) {
        // Shouldn't happen.
        e.printStackTrace();
      }
    }
  }

  public boolean addEdit(UndoableEdit edit)
  {
    return false;
  }
}
