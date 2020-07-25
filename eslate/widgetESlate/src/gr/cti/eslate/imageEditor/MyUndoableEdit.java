package gr.cti.eslate.imageEditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

public class MyUndoableEdit implements UndoableEdit {
	CanvasPanel canvas;

	ArrayList oldPixels;

	ArrayList oldValue;

	Color previousColor;

	String operation;

	boolean isAlive=true;

	Point restorePoint, pressPoint;

	BufferedImage undoIm;

	ImageEditor iEditor;

	double scalingFactor;

	Shape selection;

	public MyUndoableEdit(ImageEditor iEditor,ArrayList oldPixels,ArrayList oldValue,String operation) {
		this.iEditor=iEditor;
		this.oldPixels=(ArrayList) oldPixels.clone();
		this.oldValue=(ArrayList) oldValue.clone();
		this.operation=operation;
	}

	public MyUndoableEdit(ImageEditor iEditor,BufferedImage im,Point pressPoint,Point restorePoint,double scalingFactor,String operation) {
		this.iEditor=iEditor;
		this.scalingFactor=scalingFactor;
		undoIm=im;
		this.restorePoint=restorePoint;
		this.pressPoint=pressPoint;
		this.operation=operation;
	}

	public MyUndoableEdit(ImageEditor iEditor,BufferedImage im,String operation) {
		this.iEditor=iEditor;
		undoIm=im;
		this.operation=operation;
	}

	public MyUndoableEdit(ImageEditor iEditor,Shape selection,String operation) {
		this.iEditor=iEditor;
		this.selection=selection;
		this.operation=operation;
	}

	public void setCanvas(CanvasPanel canv) {
		canvas=canv;
	}

	public boolean addEdit(UndoableEdit anEdit) {
		// no absorbtion supported
		return false;
	}

	public boolean canRedo() {
		return isAlive;
	}

	public boolean canUndo() {
		return isAlive;
	}

	public void die() {
		isAlive=false;
	}

	public String getPresentationName() {
		return operation;
	}

	public String getRedoPresentationName() {
		return operation;
	}

	public String getUndoPresentationName() {
		return operation;
	}

	public boolean isSignificant() {
		return true;
	}

	public void redo() throws CannotRedoException {
	}

	public boolean replaceEdit(UndoableEdit anEdit) {
		return false;
	}

	public void undo() throws CannotUndoException {
		if (!canUndo())
			throw new CannotUndoException();
		if (operation.equals("shape")) {
			try {
				canvas.restoreImage(undoIm,pressPoint,restorePoint,scalingFactor);
			} catch (Exception e) {
				throw new CannotUndoException();
			}
			;
		} else if (operation.equals("image")) {
			try {
				canvas.setCanvasImage(undoIm);
				canvas.repaint();
			} catch (Exception e) {
				throw new CannotUndoException();
			}
			;
		} else if (operation.equals("select")) {
			try {
				canvas.setToSelectedShapes(selection);
				canvas.repaint();
			} catch (Exception e) {
				throw new CannotUndoException();
			}
			;
		} else if (operation.equals("palette")) {
			try {
				canvas.restoreImage(undoIm,pressPoint,restorePoint,scalingFactor);
				canvas.deselectAll();
				canvas.repaint();
			} catch (Exception e) {
				throw new CannotUndoException();
			}
			;
		} else {
			try {
				canvas.setCanvasImage(undoIm);
				iEditor.xSize=undoIm.getWidth();
				iEditor.ySize=undoIm.getHeight();
				iEditor.setImageSize(iEditor.xSize,iEditor.ySize);
			} catch (Exception e) {
				throw new CannotUndoException();
			}
			;
		}
		iEditor.preview.setPreviewImage(canvas.getImage());

		// }
		// if (iEditor.realTimeUpdate && !iEditor.pinDisconnected)
		// iEditor.sendIcon();
	}

}
