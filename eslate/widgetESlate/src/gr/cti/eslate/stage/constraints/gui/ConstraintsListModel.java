package gr.cti.eslate.stage.constraints.gui;

import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import gr.cti.eslate.stage.constraints.models.IConstraintContainer;

public class ConstraintsListModel implements ListModel {

  private IConstraintContainer container;

  public ConstraintsListModel(IConstraintContainer container) {
  }

  public int getSize() {
   return container.getConstraintsCount();
  }

  public Object getElementAt(int index) {
   return container.getConstraint(index);
  }

/////////////////////

  public void addListDataListener(ListDataListener l) {
   listeners.add(l);
  }

  public void removeListDataListener(ListDataListener l) {
   listeners.remove(l);
  }

  private ArrayList<ListDataListener> listeners =
    new ArrayList<ListDataListener>();

}
