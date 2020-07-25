package gr.cti.eslate.mapViewer;

import java.util.HashSet;
import java.util.Iterator;

public class SelectionShapeEventMulticaster extends HashSet implements SelectionShapeListener {

    public SelectionShapeEventMulticaster() {
        super();
    }
    public void shapeGeometryChanged(SelectionShapeEvent e) {
        Iterator it=iterator();
        while (it.hasNext())
            ((SelectionShapeListener) it.next()).shapeGeometryChanged(e);
    }
}