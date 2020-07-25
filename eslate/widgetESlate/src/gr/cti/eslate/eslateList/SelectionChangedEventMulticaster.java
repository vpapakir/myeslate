package gr.cti.eslate.eslateList;


import java.util.*;


public class SelectionChangedEventMulticaster extends HashSet implements SelectionChangedListener {

	private static final long serialVersionUID = 2171627996951652594L;

	public SelectionChangedEventMulticaster() {
        super();
    }

    public void selectionChanged(SelectionChangedEvent e) {
        Iterator it = iterator();

        while (it.hasNext())
            ((SelectionChangedListener) it.next()).selectionChanged(e);
    }
}
