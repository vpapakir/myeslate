package gr.cti.eslate.mapViewer;

import java.util.EventListener;

public interface SelectionShapeListener extends EventListener {
    public abstract void shapeGeometryChanged(SelectionShapeEvent e);
}