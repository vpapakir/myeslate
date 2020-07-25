package virtuoso.logo;

import gr.cti.eslate.sharedObject.LogoCallSO;
import gr.cti.eslate.scripting.logo.*;//ComponentPrimitives;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;

@SuppressWarnings(value={"unchecked"})
public class MyMachine extends Machine{
 public LogoCallSO callSO;

 public MyMachine(LogoCallSO callSO,Console console1, PrimitiveGroup aprimitivegroup[]){
  super(console1,aprimitivegroup);
  this.callSO=callSO; //keep this so that InterpreterThread instances can get it from us (doing mach().callSO)
  gr.cti.eslate.scripting.logo.ComponentPrimitives.machines.addElement(this); //27-3-1999: add to the machines list kept at ComponentPrimitives //28-3-1999: bug-fix: was using Java2's add (Collection interface implemented by java.util.Vector
  //should load all available objects' and ESlateComponents' primitives... the Logo component does that for now
}

 public MyMachine(Console console1, PrimitiveGroup aprimitivegroup[]){
  this(null,console1,aprimitivegroup); //1Jun1999: this is an allowed constructor, can be called by machines not having a LogoCallSO (no procedure call notifications)
  //System.err.println("Shouldn't be instantiated with that one");
 }

 public final void cleanup(){ //27-3-1999
  super.cleanup(); //!!!
  gr.cti.eslate.scripting.logo.ComponentPrimitives.machines.removeElement(this); //27-3-1999: remove from the machines list kept at ComponentPrimitives //28-3-1999: bug-fix: was using Java2's Vector.remove method (at Java2 the Collection interface is implemented by java.util.Vector)
 }

 public final void loadPrimitivesSilent(String s){ //Birb: 31May2000: doesn't throw exceptions, just shows error messages on the console
  try{
   try{loadPrimitives(s);}
   catch(LanguageException e){
    e.printStackTrace();
    Console c=console();
    if(c!=null) c.putLine(e.getLocalizedMessage());
   }
  }catch(Throwable t){
   t.printStackTrace();
  }
 }

 public final void loadPrimitives(String s[]) //27-3-1999: load many primitive groups in a batch (moved this code here from gr.cti.eslate.logo.Logo)
 {
    for(int i=0;i<s.length;i++){
     try{
      loadPrimitives(s[i]);
      //System.out.println("Loaded primitive group "+s[i]); //8-4-1999: not printing to console any more, cause slows down (most PrimitiveGroups print a setup message to Virtuoso's console anyway)
     }catch(Throwable th){
      System.err.println(th+"\nProblem loading primitive group "+s[i]);} //3-9-1998:printing exception
    }
 }

 public final synchronized void installPrimitives(PrimitiveGroup p)
                                                 throws LanguageException {
  try{
   p.setup((Machine)this,console()); //must be in same package as PrimitiveGroup class to compile this
  }catch(Exception e){

   System.err.println("install Primitives caucht an exception:\n");
   e.printStackTrace(); //26May1999: printing the exception (registerPrimitives call in a primGroup might fail, due to a missing primitive implementation, without this message its hard to track the error)

   throw new LanguageException("Primitive group "+p.getClass().getName()+" refused to load.");
  }
  _primitiveGroups.addElement(p);
  _clock++; //?
 }

 public final synchronized void unInstallPrimitives(PrimitiveGroup p)
                                           throws LanguageException {
  _clock++;
  for(int i = 0; i < _primitiveGroups.size(); i++)
   if(_primitiveGroups.elementAt(i).equals(p)) { //is this OK?
    ((PrimitiveGroup)_primitiveGroups.elementAt(i)).exiting();
     _primitiveGroups.removeElementAt(i);
     return;
    }
 }

 public final void loadDefaultPrimitives(){ //Birb: 31May2000
  loadPrimitivesSilent("virtuoso.logo.lib.StandardPrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.FilePrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.NetworkPrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.ShellPrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.LoaderPrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.LibraryPrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.ExtFilePrimitives");
  loadPrimitivesSilent("virtuoso.logo.lib.ThreadPrimitives"); //23Jun1999: now loading ThreadPrimitives too!
  loadPrimitivesSilent("virtuoso.logo.lib.TurtlePrimitives"); //31May2000: loading Azuma's Turtle primitives: if a Turtle component is in the microworld, it's queried about its primitives later and they'll get loaded then and override these ones: if there's no Turtle component we have access to the original Turtle primitives that draw in a separate window (shown using the DRAW LogoTurtle command)
  loadPrimitivesSilent("gr.cti.eslate.scripting.logo.ReflectionPrimitives"); //28Jun2000
  loadPrimitivesSilent("gr.cti.eslate.scripting.logo.ListPrimitives"); //31May2000
  //8Jun2000: removed for now, tries to bring up QT// loadPrimitivesSilent("gr.cti.eslate.scripting.logo.SoundPrimitives"); //31May2000
  loadPrimitivesSilent("gr.cti.eslate.scripting.logo.ClipboardPrimitives");
 } //WARNING: don't load "gr.cti.eslate.scripting.logo.ComponentPrimitives": it needs a special sequence to get constructed and added to logo and is done by the Logo machine spawner (e.g. by the Logo component) 

 public int level1clock=0;
 public ComponentPrimitives componentPrimitives; //25May1999


 //Birb-start: 31-7-1998 //28Jun2000: moved from virtuoso.logo.PrimitiveGroup class here
 private static java.util.ResourceBundle globalTranslationBundle; //2-10-1998: now using a global and separate (optional) local translation bundles for each primitive group

 public static ResourceBundle getGlobalTranslationBundle(){ //must be static: don't load the global table again for each PrimitiveGroup (it's the same one for all prim-groups)
  if(globalTranslationBundle==null){
   try{
    globalTranslationBundle = ResourceBundle.getBundle("gr.cti.eslate.scripting.logo.PrimitivesBundle", Locale.getDefault()); //20Sep1999: moved global translation bundle to LogoEngine (and changed package)
   }catch(MissingResourceException e) {
    //24-12-1998:console is too slow// System.err.println("...couldn't find any Logo localization bundle (gr.cti.eslate.logo.PrimitivesBundle)");
    globalTranslationBundle=null;
   }
  }
   //if localized bundle is not found it shall load the english version and if none shall throw an exception???
  return globalTranslationBundle;
 }

 public static String localize(String s){
  ResourceBundle m=getGlobalTranslationBundle(); //try to load it if it's not loaded
  try{return (m!=null)?m.getString(s):null;}
  catch(Exception e){return null;} //must unfortunately have a missing resource exception being thrown and caught for every localization that's missing: fix-this: use some ResourceBundle that doesn't throw exceptions for missing resources...
 }
 //Birb-end

}
