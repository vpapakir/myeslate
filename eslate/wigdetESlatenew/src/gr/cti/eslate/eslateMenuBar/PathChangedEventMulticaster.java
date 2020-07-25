package gr.cti.eslate.eslateMenuBar;


import java.util.*;


public class PathChangedEventMulticaster extends HashSet implements PathChangedListener {

    public PathChangedEventMulticaster() {
        super();
    }

    public void pathChanged(PathChangedEvent e) {
        Iterator it = iterator();

        while (it.hasNext())
            ((PathChangedListener) it.next()).pathChanged(e);
    }

}    

