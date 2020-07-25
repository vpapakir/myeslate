package gr.cti.eslate.mapViewer;

import java.util.HashSet;
import java.util.Iterator;

public class SelectionShapeTypeEventMulticaster extends HashSet implements SelectionShapeTypeListener {

    public SelectionShapeTypeEventMulticaster() {
        super();
    }
    public void shapeTypeChanged(SelectionShapeTypeEvent e) {
        Iterator it=iterator();
        while (it.hasNext())
            ((SelectionShapeTypeListener) it.next()).shapeTypeChanged(e);
    }
}