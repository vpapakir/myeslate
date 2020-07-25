package gr.cti.eslate.spinButton;


import java.util.*;


public class SpinModelDataEventMulticaster extends HashSet implements SpinModelDataListener {
	
	static final long serialVersionUID = 73231L;
	
    public SpinModelDataEventMulticaster() {
        super();
    }

    public void spinModelDataChanged(SpinModelDataEvent e) {
        Iterator it = iterator();

        while (it.hasNext())
            ((SpinModelDataListener) it.next()).spinModelDataChanged(e);
    }
}
