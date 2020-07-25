package gr.cti.eslate.imageEditor;

import javax.swing.undo.UndoManager;

public class MyUndoManager extends UndoManager {

	public void setCanvasArgument(CanvasPanel canvas) {
		MyUndoableEdit edit=(MyUndoableEdit) editToBeUndone();
		try {
			edit.setCanvas(canvas);
		} catch (Exception e) {
		}
	}
}