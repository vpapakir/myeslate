package gr.cti.eslate.eslateTextArea;


import java.util.*;
import javax.swing.event.*;


public interface CustomCaretListener extends EventListener {

    public abstract void caretUpdate(CaretEvent e);

}
