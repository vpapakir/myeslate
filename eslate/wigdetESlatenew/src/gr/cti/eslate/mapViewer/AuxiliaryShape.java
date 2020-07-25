package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.ILayerView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

class AuxiliaryShape {
	AuxiliaryShape(Shape shape,Stroke stroke,Color outline,Color fill,boolean paintBorder) {
		this.shape=shape;
		this.stroke=stroke;
		this.outline=outline;
		this.fill=fill;
		this.paintBorder=paintBorder;
	}

	AuxiliaryShape(ILayerView lv,Shape shape) {
		this.lv=lv;
		this.shape=shape;
	}

	protected Stroke getStroke() {
		if (stroke==null)
			stroke=new BasicStroke();
		return stroke;
	}

	protected Color getFillColor() {
		if (fill==null)
			fill=new Color(255,255,255,64);
		return fill;
	}

	protected Color getOutlineColor() {
		if (outline==null)
			outline=new Color(32,32,32,128);
		return outline;
	}

	protected boolean isBorderPainted() {
		return paintBorder;
	}

	protected ILayerView lv;
	protected Shape shape;
	private Stroke stroke;
	Color outline;
	Color fill;
	private boolean paintBorder;
}