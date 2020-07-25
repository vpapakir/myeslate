/*
  6Oct1999 - setHelp code removed, help system now is automatic
 29Oct1999 - should now start OK even when it can't register with E-Slate
  2Nov1999 - added "ComponentBarVisible" property
 25Nov1999 - using ObjectHash (Hashtable) for storing/loading propeties (so that we can add properties to the ancestor without breaking the descendents' loading of older stored state)
 25Jan2000 - added "protected void log(Object)" and "protected void errlog(Object)" methods
 12Feb2000 - now readExternal error messages show the classname of the descendent for which the method was called
            (descendents who want to keep compatibility with their old data, should override readExternal/writeExternal to do nothing!)

 03Apr2000 - compatibility with old Canvas, Logo, Turtle etc. persistence data (readExternal/writeExternal do nothing when "isOldStylePersisting" method returns "true" (default is "false", old-style descendents can override it to return "true")
 06Apr2000 - fixed-bug, "readExternal" and "writeExternal" was returning without doing anything when "oldStylePersisting" gave "false" instead of "true" (now does the opposite)
 15Apr2000 - now if readExternal catches some exception, it prints a stack trace
*/

/* 28Aug2000: StorageVersion = 1
              ComponentVersion = 1.0.0
*/


//...should copy the after 29Oct code to BaseApplet and BaseHeavyApplet too (or move all code into an ESlateProxy object and do delegation)...

package gr.cti.eslate.birbilis;

import java.awt.*;
import java.net.*;
import java.util.Locale;
import java.io.*;

import javax.swing.*;
import javax.swing.JComponent;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.utils.*;

public class BaseComponent extends JPanel //30Jul1999: removed abstract, so that JBuilder can use this in the designed to create visual subcomponents
  implements ESlatePart,
             java.io.Externalizable
{

//  private static final String VERSION="4.0"; //12Feb2000 (make private, since descendents will be using such a VERSION field and will most often have it public)
    private static final String VERSION="1.0.4"; //12Feb2000 (make private, since descendents will be using such a VERSION field and will most often have it public)
    public static final int STORAGE_VERSION = 1;



  static final long serialVersionUID = 03042000L; //03Apr2000: added serial-version, so that new versions load OK ???

  public JComponent getContent(){return null;} //30Jul1999: not abstract anymore, default empty implementation just returns null

  //ABOUT//

  public ESlateInfo getInfo()
  {
    return new ESlateInfo("E-Slate Component Skeleton ("+VERSION+")",new String[]{"Copyright © 1999-2002","birbilis@cti.gr"});
  }

  /**
   * Creates a new component.
   */
  public BaseComponent()
  {
    super();
    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);

    //System.out.println("setting up ESlateFinalizer");

    new ESlateFinalizer(getESlateHandle(),this); //22Sep1999 //29Oct1999: this will create an object that does nothing if passed a null handle (the object will be grabbed by next GC invocation, since we're not holding it and it isn't attatched as a listener to some handle if we passed a null handle to it)

    //System.out.println("ESlateFinalizer was setup OK");

    setOpaque(true);

    ESlateHandle handle=getESlateHandle(); //29Oct1999: not getting it in a private variable, so that we don't fail when E-Slate is missing (or in case it fails to give us a handle)
    if(handle!=null) handle.setInfo(getInfo()); //29Oct1999: only if we're registered with E-Slate

    GridBagLayout layout = new GridBagLayout();
    try { setLayout(layout); } catch (AWTError e) {}

    // Add the menu panel
    GridBagConstraints constraints = new GridBagConstraints();
    // Place in first column (only one column exists)
    constraints.gridx = 0;
    // Place below previous one
    constraints.gridy = GridBagConstraints.RELATIVE;
    // Take up all the row (1 column)
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    // Expand area for component only in x direction
    constraints.fill = GridBagConstraints.HORIZONTAL;
    // Don't expand component in y direction
    constraints.weighty = 0;
    // Expand component in x direction
    constraints.weightx = 1;

/*
    if(handle!=null) layout.setConstraints(add(handle.getMenuPanel()), constraints); //29Oct1999: only if we're registered with E-Slate
*/

    // Add the normal view panel

    // Take up all remaining y space
    constraints.gridheight = GridBagConstraints.REMAINDER;
    // Expand area for component in both x and y directions
    constraints.fill = GridBagConstraints.BOTH;
    // Expand component in x direction
    constraints.weightx = 1;
    // Expand component in y direction
    constraints.weighty = 1;
    JComponent content=getContent();
    if(content!=null) layout.setConstraints(add(content), constraints); //30Jul1999: checking for getContent() returning null (the default implementation does this)
  }

  //DESTRUCTOR//

  public void destroy(){ //must have this (even an empty one) to avoid exceptions thrown and caught by the ESlateFinalizer (it is supposed to call "dispose" when the handle is disposed)
  }

  public void finalize(){ //22Sep1999
   destroy(); //just call destroy()
  }

////////////////////////////////////////////////////////////////////

  //PINS//
  public void addPlug(Plug plug)
    throws PlugExistsException, IllegalArgumentException
  {
    if(avHandle!=null) avHandle.addPlug(plug); //29Oct1999: only if we're registered with E-Slate
  }

  public void removePlug(Plug plug)
    throws NoSuchPlugException, IllegalArgumentException
  {
    if(avHandle!=null) avHandle.removePlug(plug); //29Oct1999: only if we're registered with E-Slate
  }

  public Plug getPlug(String pinName)
  {
    return(avHandle!=null)?avHandle.getPlug(pinName):null; //29Oct1999: if we're not registered with E-Slate return null
  }

  public Plug[] getPlugs()
  {
    return(avHandle!=null)?avHandle.getPlugs():null; //29Oct1999: if we're not registered with E-Slate return null
  }

  //MICROWORLD//

  public ESlateMicroworld getESlateMicroworld()
  {
    return (avHandle!=null)?avHandle.getESlateMicroworld():null; //29Oct1999: if we're not registered with E-Slate, return null
  }

  //NAMING//

  public String getName() //29Oct1999: renamed to "getName"
  {
   return (avHandle!=null)?avHandle.getComponentName():super.getName(); //29Oct1999: if we're not registered with E-Slate, return super.getName()
  }

  public void setName(String name) //29Oct1999: renamed to "setName" and removed the "thow" stuff, cause our Component ancestor doesn't throw such stuff (throwing a RuntimeException instead, which we don't need to declare)
  {
   if(avHandle!=null)
    try{ getESlateHandle().setComponentName(name); }catch(Exception e){throw new RuntimeException(e.getMessage());}
   else
    super.setName(name); //29Oct1999: if we're not registered with E-Slate, call super.setName()
  }

  public void setUniqueComponentName(String suggestedName)
    throws RenamingForbiddenException
  {
   if(avHandle!=null)
    getESlateHandle().setUniqueComponentName(suggestedName);
   else
    super.setName(suggestedName); //29Oct1999: if we're not registered with E-Slate, call super.setName()
  }

  //LOCALE//

  public Locale getLocale()
  {
   return (avHandle!=null)?avHandle.getLocale():Locale.getDefault(); //29Oct1999: if we're not registered with E-Slate, return Locale.getLocale()
  }

  /**
   * Specifies whether debugging messages should be printed.
   * @param     status  True if yes, false if not.
   */
  public void setDebugStatus(boolean status)
  {
   if(avHandle!=null) avHandle.setDebugStatus(status); //29Oct1999: if we're registered with E-Slate
  }

  //DEBUG//

  public boolean getDebugStatus()
  {
   return (avHandle!=null)?getESlateHandle().getDebugStatus():false; //29Oct1999: if we're not registered with E-Slate, return false
  }

  //HANDLE//

  protected transient ESlateHandle avHandle; //20May1999: made transient, in case the component is used as a Serializable

  public ESlateHandle getESlateHandle()
  {
    if (avHandle!=null) return avHandle;
    try{ //29Oct1999: added try-catch, since ESlate.registerPart fails if HelpSystemViewer is missing
     avHandle = ESlate.registerPart(this);
     return avHandle;
    }catch(Exception e){
     return null; //29Oct1999: if we can't register with E-Slate return null
    }
  }

//  private void disposeESlateHandle(){
//   if(avHandle!=null){ //29Oct1999: if not registered with E-Slate, then do nothing
//    avHandle.dispose();
//    avHandle=null;
//   }
//  }

/////////////////////////////////////////////////////////////////

  //ComponentBarVisible property//

  public boolean isComponentBarVisible()
  {
    if ((avHandle != null) && (avHandle.isMenuPanelCreated())) {
      Component componentBar = avHandle.getMenuPanel();
      return componentBar.isVisible();
    }else{
      return false;
    }
  }

  public void setComponentBarVisible(boolean visible)
  {
    if ((avHandle != null) && (avHandle.isMenuPanelCreated())) {
      Component componentBar = avHandle.getMenuPanel();
      componentBar.setVisible(visible);
    }
  }

/////////////////////////////////////////////////////////////////

  public static final String COMPONENT_BAR_VISIBLE_PROPERTY="componentBarVisible";

  //Externalizable//

/*  public ObjectHash getProperties(){
      System.out.println("Base getProperties");
   ObjectHash properties=new ObjectHash();
   properties.putBoolean(COMPONENT_BAR_VISIBLE_PROPERTY,isComponentBarVisible());
   return properties;
  } //25Nov1999: descendents should override that to return a hashtable with all the properties' name-value pairs (they should call super.getProperties first and add entries to the hashtable it returns and return that one - if super.getProperties() returns null, then create and return a new Hashtable instance)

  public void setProperties(ObjectHash properties){
    System.out.println("Base setProperties");
   try{setComponentBarVisible(properties.getBoolean(COMPONENT_BAR_VISIBLE_PROPERTY));}catch(Exception e){setComponentBarVisible(true);} //must use try-catch for eacb property, since it might be missing (when loading some older saved state) //set default value in case this property is missing (due to some or old or corrupted/wrong state loading)
  } //25Nov1999: descendents should override that to set all the properties for the hashtable with the name-value pairs (they should call super.setProperties too)
  */
  public StorageStructure getProperties(){
   System.out.println("Base getProperties");
   ESlateFieldMap2 properties=new ESlateFieldMap2(STORAGE_VERSION);
   properties.put("COMPONENT_BAR_VISIBLE_PROPERTY",isComponentBarVisible());
   return properties;
  } //25Nov1999: descendents should override that to return a hashtable with all the properties' name-value pairs (they should call super.getProperties first and add entries to the hashtable it returns and return that one - if super.getProperties() returns null, then create and return a new Hashtable instance)

  public void setProperties(StorageStructure properties){
    System.out.println("Base setProperties");
    setComponentBarVisible(properties.get("COMPONENT_BAR_VISIBLE_PROPERTY",true));
  } //25Nov1999: descendents should override that to set all the properties for the hashtable with the name-value pairs (they should call super.setProperties too)

  //

  protected boolean isOldStylePersisting(){ //03Mar2000: compatibility: descendents who don't wish their BaseComponent ancestor to read/write any data, should override this method to return "true"
   return false;
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException { //descendents who want to keep compatibility with their old data, should override this to do nothing!
      if(isOldStylePersisting()) return;
      //System.out.println("BirbBase continue read External");

      Object firstObj = in.readObject();
      //System.out.println("BirbBase Here");
      if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
          // oldreadExternal
          //System.out.println("old read External");
          try{
              setProperties((StorageStructure)firstObj); //25Nov1999
          }catch(Exception e){
             errlog(getClass().getName()+"@BaseComponent: Error loading properties hashtable");
             e.printStackTrace(); //15Apr2000 (the exception's message is printed too, don't need to print it ourselves)
          }
      }
      //else{
          //System.out.println("new read External");
          //StorageStructure fieldMap = (StorageStructure) firstObj;
      //}


  /* if(isOldStylePersisting()) return; //03Apr2000: compatibility with old Canvas, Logo, Turtle etc. persistence data   //06Apr2000: fixed-bug, was returning when "oldStylePersisting" gave false instead of true (now does the opposite)
   try{
    setProperties((ObjectHash)in.readObject()); //25Nov1999
   }catch(Exception e){
    errlog(getClass().getName()+"@BaseComponent: Error loading properties hashtable");
    e.printStackTrace(); //15Apr2000 (the exception's message is printed too, don't need to print it ourselves)
   }*/
  }

  public void writeExternal(ObjectOutput out) throws IOException { //descendents who want to keep compatibility with their old data, should override this to do nothing!
      if(isOldStylePersisting()) return; //03Apr2000: compatibility with old Canvas, Logo, Turtle etc. persistence data //06Apr2000: fixed-bug, was returning when "oldStylePersisting" gave false instead of true (now does the opposite)
      ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
      out.writeObject(fieldMap);
      out.flush();


  /*System.out.println(" BirbBase old writeExternal");
   if(isOldStylePersisting()) return; //03Apr2000: compatibility with old Canvas, Logo, Turtle etc. persistence data //06Apr2000: fixed-bug, was returning when "oldStylePersisting" gave false instead of true (now does the opposite)
      ObjectHash properties=getProperties(); //25Nov1999

     if(properties!=null) out.writeObject(properties); //25Nov1999
     if(properties!=null) out.writeObject(properties); //25Nov1999
     System.out.println(" BirbBase old writeExternal end");
    */
  }

/////////////////////////////////////////////////////////////////

  //ex-APPLET//

  public void init(){}
  public void start(){}

  public String getParameter(String value){
   return null;
  }

  public URL getCodeBase(){
   return getESlateHandle().getESlateMicroworld().getDocumentBase(); //???
  }


///////////////////////////////////////////////////////////////////


  //utils//


    protected void log(Object message){

     System.out.println(message);
    }

    protected void errlog(Object error){
     System.err.println(error);
    }

    /**
     * Override this method to attach the component's performance timers, as
     * described in the E-Slate cookbook.
     */
    protected void attachTimers()
    {
    }

}

