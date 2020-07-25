package gr.cti.eslate.mapViewer;

import java.util.EventListener;

public interface SelectionShapeTypeListener extends EventListener {
    public abstract void shapeTypeChanged(SelectionShapeTypeEvent e);
}