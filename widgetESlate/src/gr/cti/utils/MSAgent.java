package gr.cti.utils;

import java.awt.*;
import java.awt.datatransfer.*;

public class MSAgent{

/////////
 private static ClipboardOwner defaultClipboardOwner = new ClipboardObserver();

 static class ClipboardObserver implements ClipboardOwner {
  public void lostOwnership(Clipboard clipboard, Transferable contents) {}
 }
/////////

private static void copy(String s){
 Toolkit.getDefaultToolkit().getSystemClipboard().
  setContents(new StringSelection(s), defaultClipboardOwner);    
}

public static void say(String s){
 try{copy('@'+s);}catch(Exception e){}
}

public String hear(){
 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();    
 Transferable content = clipboard.getContents(this);
 String s=null;
 if (content != null) {
  try {
   s = (String)(content.getTransferData(DataFlavor.stringFlavor));
   if (s.startsWith("@")) {
    s=s.substring(1); //skip starting @
    copy("");
   }
  }catch (Exception e) {
//   System.err.println("Couldn't get clipboard contents in format: "+
//                       DataFlavor.stringFlavor.getHumanPresentableName());
  }
 }
 return s;
}

}