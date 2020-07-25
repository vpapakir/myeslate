//ClipboardPrimitives.java - 22Aug2001

package gr.cti.eslate.scripting.logo;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import virtuoso.logo.*;

public class ClipboardPrimitives
 extends PrimitiveGroup
{

 //private MyMachine myMachine; //new-scripting-mechanism
 private Clipboard clipboard;

 protected void setup(Machine machine, Console console)
  throws SetupException
 {
  registerPrimitive("CLIPBOARD.READ","pREAD",0);
  registerPrimitive("CLIPBOARD.WRITE", "pWRITE", 1);

  clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  //...place extra init code here...

  //myMachine=(MyMachine)machine; //new-scripting-mechanism

  if(console != null)
   console.putSetupMessage("Loaded ESlate's clipboard primitives");
 }

//CLIPBOARD.READ//

 public final LogoObject pREAD(InterpEnviron interpenviron, LogoObject params[]) //31May2000
  throws LanguageException
 {
  testNumParams(params,0);
  try{
   String value=(String)clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor);
   return new LogoWord(value);
  }catch(Exception e){
   throw new LanguageException("Couldn't read string data from the clipboard");
  }
 }

//CLIPBOARD.WRITE//

 public final LogoObject pWRITE(InterpEnviron interpenviron, LogoObject params[])
  throws LanguageException
 {
  testNumParams(params,1);
  String value=params[0].toString();
  StringSelection transferable  = new StringSelection(value);
  clipboard.setContents(transferable,transferable);
  return LogoVoid.obj;
 }

}

