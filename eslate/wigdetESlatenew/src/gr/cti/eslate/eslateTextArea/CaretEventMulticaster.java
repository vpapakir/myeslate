package gr.cti.eslate.eslateTextArea;

import java.util.*;
import javax.swing.event.*;


@SuppressWarnings("unchecked")
public class CaretEventMulticaster extends HashSet implements CustomCaretListener {

	static final long serialVersionUID = -1L;
    public CaretEventMulticaster() {
        super();
    }

    public void caretUpdate(CaretEvent e) {
        Iterator it = iterator();

        while (it.hasNext())
            ((CustomCaretListener) it.next()).caretUpdate(e);
    }
}
