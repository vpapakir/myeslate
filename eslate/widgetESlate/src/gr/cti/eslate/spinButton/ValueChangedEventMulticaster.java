package gr.cti.eslate.spinButton;


import java.util.*;


public class ValueChangedEventMulticaster extends HashSet implements ValueChangedListener {

	static final long serialVersionUID = 90212L;
	
    public void valueChanged(ValueChangedEvent e) {
        Iterator it = iterator();

        while (it.hasNext())
            ((ValueChangedListener) it.next()).valueChanged(e);
    }
}
