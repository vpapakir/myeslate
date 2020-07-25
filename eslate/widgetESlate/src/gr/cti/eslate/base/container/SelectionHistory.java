package gr.cti.eslate.base.container;

import com.objectspace.jgl.DList;



public class SelectionHistory {
  private DList history;

  public SelectionHistory() {
      history = new DList();
  }

//  public void addFrame(JInternalFrame fr) {
  /* Only ESlateComponents should be added in the SelectionHistory.
   */
  public void addComponent(ESlateComponent compo) {
      int index = history.indexOf(compo);
      if (index != -1)
          history.remove(index);
      history.pushFront(compo);
      /* There exists a case when the first frame on the stack
       * is the ESlateContainer's EMPTY_FRAME. If this is the
       * case, remove this frame from the stack.
       */
/*      if (compo != ESlateContainer.EMPTY_FRAME) {
          index = history.indexOf(ESlateContainer.EMPTY_FRAME);
          if (index != -1)
              history.remove(index);
      }
*/
  }

//  public void removeFrame(Object compo) {
  public void removeComponent(ESlateComponent compo) {
      history.remove(compo);
  }

  public void setFirst(ESlateComponent compo) {
      addComponent(compo);
//      printSelectionHistory();
  }

//  public ESlateInternalFrame getFirst() {
  public ESlateComponent getFirst() {
      if (history.size() == 0)
          return null;
      return (ESlateComponent) history.at(0);
  }

//  public ESlateInternalFrame get(int index) {
  public ESlateComponent get(int index) {
      if (history.size() <= index )
          return null;
      return (ESlateComponent) history.at(index);
  }

  /* Returns the first not-iconified component in the component activation history.
   * If the supplied ESlateComponent is not null, then this component will be neglected.
   */
  public ESlateComponent getFirstNotIconified(ESlateComponent neglectedComponent) {
//      System.out.println("getFirstNotIconified() history.size(): " + history.size());
      if (history.size() == 0)
          return null;

      ESlateComponent component = null;
      for (int i = 0; i<history.size(); i++) {
          component = (ESlateComponent) history.at(i);
          if (!component.desktopItem.isIcon() && component != neglectedComponent) {
//1          if (!fr.isIcon() && fr != ESlateContainer.EMPTY_FRAME) {
//              System.out.println("getFirstNotIconified() returning: " + fr.getTitle() + " isIcon: " + fr.isIcon());
                  return component;
          }
      }
//      System.out.println("getFirstNotIconified() returning: null");
      return null;
  }

  public void clear() {
      history.clear();
  }

  protected void printSelectionHistory() {
      for (int i=0; i<history.size(); i++)
          System.out.println(((ESlateComponent) history.at(i)).handle.getComponentName());
//          System.out.print(((JInternalFrame) history.at(i)).getTitle() + ", ");
      System.out.println();
  }

  public int size() {
      return history.size();
  }

}
