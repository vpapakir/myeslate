package gr.cti.eslate.base.container;

import com.objectspace.jgl.SList;


public class HistoryList {
  private SList history;
  private int historyIndex;
  private int historySize = 30;

////nikosM
  private class HistoryObj {
////the microworld
      private String historyString;
////true if the microworld is remote
      private boolean remote;
      public String getHistoryObjString(){
         return historyString;
      }

      public void setHistoryObjString(String string){
         historyString=string;
      }

      public void remoteNameToLocalName(){
         historyString=historyString.substring(0,historyString.length()-1);
      }

      public boolean isRemote(){
         return remote;
      }

      public void setRemote(boolean remoteFlag){
         remote=remoteFlag;
      }
  }
////nikosM end

  public HistoryList() {
      history = new SList();
      historyIndex = -1;
  }

  public int getHistorySize() {
      return historySize;
  }

  public void setHistorySize(int size) {
      if (size < 10) {
          System.out.println("History size cannot be less that 10");
          return;
      }
      historySize = size;
  }

/////nikosM
  public void addToHistory(String mwd) {
      HistoryObj newHistoryObj = new HistoryObj();
      newHistoryObj.setHistoryObjString(mwd);
      if (mwd.endsWith("*"))
          newHistoryObj.setRemote(true);
      else
          newHistoryObj.setRemote(false);
      if (newHistoryObj.isRemote())
          newHistoryObj.remoteNameToLocalName();
      if (history.size() < historySize) {
          history.pushFront(newHistoryObj);
          historyIndex = 0;
      }else{
          history.popBack();
          history.pushFront(newHistoryObj);
          historyIndex = 0;
      }
  }
////nikosM end

  public void back(boolean exitsOpenMwd) {
      if (!exitsOpenMwd)
          return;
      if (historyIndex < historySize)
          historyIndex++;
  }

  public boolean canGoBack(boolean existsOpenMwd) {
      if (history.size() == 0)
          return false;
      if (!existsOpenMwd)
          return (historyIndex < history.size());
      else
          return ((historyIndex+1) < history.size());
  }

  public boolean canGoForward() {
      if (history.size() == 0)
          return false;
      return (historyIndex > 0);
  }

  public void forward() {
      if (historyIndex > 0)
          historyIndex--;
  }

////NikosM
  public String getCurrentMicroworld() {
      if (historyIndex == -1)
          return null;
      return (String) ((HistoryObj)history.at(historyIndex)).getHistoryObjString();
  }

  public boolean isCurrentMicroworldRemote(){
      if (historyIndex == -1)
          return false;
      return ((HistoryObj)history.at(historyIndex)).isRemote();
  }

  public int getHistoryIndex() {
      return historyIndex;
  }

  public String getMicroworldAt(int index) {
//      if (index < 0) return null;
//      if (index >= historySize) return null;
//      return (String) history.at(index);
      return ((HistoryObj) history.at(index)).getHistoryObjString();
  }

  public boolean isRemoteMicroworldAt(int index){
      return ((HistoryObj)history.at(index)).isRemote();
  }
////NikosM end


  public int getItemCount() {
      return history.size();
  }

  protected void setCurrentMicroworld(int index) {
      if (index < 0 || index >= history.size()) return;
      historyIndex = index;
  }
////nikosM
  public java.util.Vector getContents() {
      java.util.Vector contents = new java.util.Vector(history.size());
      for (int i=0; i<history.size(); i++) {
          HistoryObj historyObj=(HistoryObj) history.at(i);
          contents.addElement(historyObj.getHistoryObjString());
      }
      return contents;
  }
/////nikosM end
}

