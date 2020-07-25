package gr.cti.eslate.eslateList;


import java.util.*;


public interface SelectionChangedListener extends EventListener {
    public abstract void selectionChanged(SelectionChangedEvent e);
}
