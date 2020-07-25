//TO DO: not all ESlateHandle owners are ESlateParts, should change the NAME/SETNAME code to work directly with handles and not their owners

//problem with EACH: maybe that a null class or i/f is passed to getComponentsToTell when used with the NAME primitive ???
//also check syntax for ASK and EACH...

/*
14-12-1998 - remove the constructors??? Now setting the variable "theESlateMicroworld" at "setup()"
27-3-1999  - adding support for "not necesserily ESlateComponent" objects that are regisered to a NameService (and thus have a unique name)
31Aug1999  - TELL and ASK allow for their 1st param to be a word, not only a list
20Sep1999  - now NAME/SETNAME also handle registered scriptable objects that don't implement the HasName interface, plus registered E-Slate objects that don't implement the ESlatePart interface (they talk to the respective NameServices directly instead talking to the objects themselves for renaming: the objects should get notifications from the name services when they are renamed, if they are supposed to be displaying their name and get notified for renames done through the NameService in order to update their displayed name)
29Nov1999  - added support for objects constructing through aggregation (using facets)
07Apr2000  - addded "PRIMITIVES" primitive, so that one can ask a set of component what primitives they implement (and how many params each one of them takes by default)
           - fixed NAME/SETNAME primitives to work with objects which are assigned an ESlateHandle but their classes don't implement ESlatePart
*/

package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.base.*;

import java.util.*;
import virtuoso.logo.*;
import virtuoso.logo.Console;
import java.io.*;

import gr.cti.utils.VectorUtil;
import gr.cti.utils.CaseInsensitiveNameService;
import gr.cti.utils.INameService;

import gr.cti.eslate.scripting.HasFacets; //29Nov1999

import gr.cti.eslate.scripting.LogoScriptable;
import gr.cti.eslate.scripting.logo.convertions.LogoLogo;

/**
 * @version     2.0.1, 19-Dec-2006
 */
@SuppressWarnings(value={"unchecked"})
public class ComponentPrimitives extends PrimitiveGroup
{
    Machine _machine;

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("TELL", "pTELL", 1); //18-7-1998: changed to take 1 param //31Aug1999: now allows param to be a word instead of a list
        registerPrimitive("ASK",  "pASK", 2); //18-7-1998 //31Aug1999: now allows 1st param to be a word instead of a list
        registerPrimitive("EACH", "pEACH", 1); //18-7-1998
        registerPrimitive("TELLALL","pTELLALL", 0); //27-7-1998
        registerPrimitive("√—¡ÿ≈", "pGRAPSE", 1); //11-9-1998
        registerPrimitive("NAME", "pNAME", 0); //30-9-1998
        registerPrimitive("SETNAME", "pSETNAME", 1); //30-9-1998
        registerPrimitive("PRIMITIVES","pPRIMITIVES",0); //7Apr2000: returns a list (no duplicate entries) with all the primitives supporting by the current TELL-set

        _machine = machine;
        _console = console;

        //_machine.loadPrimitiveGroups(scriptableObjects); //don't do that!!! would try to call loadPrimitives again which is a synchronized method and would lock-up (we're already by an installPrimitives call from the spawner of this machine)

        //18May1999: ESlateII// theESlateMicroworld=ESlateMicroworld.init(); //14-12-1998: bug-fix: removed "ESlateConnection" [ESlateMicroworld] from the start of this line (now should be able to remove the constructors!)
        if(console != null)
            console.putStatusMessage("Loaded ESlate's TELL/ASK/EACH/TELLALL and NAME/SETNAME primitives"); //18-7-1998
    }

//PRIMITIVES//

 private LogoScriptable getLogoScriptable(Object o){ //7Apr2000: if the object has an ESlateHandle, get LogoScriptable instance from the handle, since even if the object is itself LogoScriptable, the handle might contain some extra dynamically attatched primitive groups
  ESlateHandle handle=theESlateMicroworld.getComponentHandle(o);
  return (handle!=null)? handle :
                         (o instanceof LogoScriptable)? (LogoScriptable)o : null; //if object doesn't have ESlateHandle, check if it is itself LogoScriptable and return it else return null
 }

 public final LogoObject pPRIMITIVES(InterpEnviron interpenviron, LogoObject params[])
  throws LanguageException
 {
  Vector v=getComponentsToTell(null); //get all telling-components as a vector copy
  Vector tellGroups=new Vector();

  for(int i=0,icount=v.size();i<icount;i++){
   LogoScriptable lso=getLogoScriptable(v.elementAt(i)); //get LogoScriptable instance from an ESlateHandle or a scriptable object's LogoScriptable implementation
   if(lso!=null){
    String[] compGroups=lso.getSupportedPrimitiveGroups();
    for(int j=0,jcount=compGroups.length;j<jcount;j++){
     PrimitiveGroup p=_machine.getPrimitiveGroup(compGroups[j]);
     if(p!=null){
      if(!tellGroups.contains(p)) tellGroups.addElement(p); //don't add more than once
     } else System.err.println("Assertion failed at ComponentPrimitives:pPRIMITIVES: tried to use some non-existing PrimitiveGroup name");
    }
   } //for objects that don't have an ESlateHandle (which is LogoScriptable) or are not LogoScriptable themselves we can't do something, unless each PrimitiveGroup describes which Java classes/interfaces it knows: we'd then enumerate on all primitive group in the Logo Machine and use only those that can talk to objects in the current tell-set (this would assume though that all primitives of a primitive group talk only to one interface/class which isn't necessarily true [at this moment]: if it was true, each primitive group could implement an interface called "IComponentPrimitiveGroup" returning the ONE class/interface it knows to talk to)
  }

  for (int i=0,icount=tellGroups.size();i<icount;i++) {
    PrimitiveGroup group=(PrimitiveGroup)tellGroups.elementAt(i);
    CaselessString[] names=group.getPrimitiveNames();
    PrimitiveSpec[] specs=group.getPrimitiveSpecs();
    // Sort the primitives before displaying them
    String[] output = new String[names.length];
    for (int j=0,jcount=names.length;j<jcount;j++) {
      output[j] = names[j]+" (default #parameters="+specs[j].numArgs()+")";
    }
    Arrays.sort(output);
    for (int j=0,jcount=names.length;j<jcount;j++) {
      _console.putLine(output[j]);
    }
  }

  return LogoVoid.obj;
 }

//NAME:set//

    private void setName(Object obj, String name) throws Exception{ //17Sep1999
     if(name!=null){ //check for null name passed (just in case)
      if(scriptableObjects.contains(obj)){
       scriptableObjects.setName(obj,name);
       return;
      }else
       /*if (obj instanceof ESlatePart)*/ { //07Apr2000: removed check on whether the object is an ESlatePart: it doesn't have to be one (e.g. a JButton)
        ESlateHandle handle=theESlateMicroworld.getComponentHandle(obj);
        if(handle!=null) { //25Feb2000: safety-checking whether we did get a non-null handle from the microworld (that is whether the object belongs to the microworld)
         handle.setComponentName(name);
         return;
        }
       }
      throw new Exception("Can't rename an unregistered object: "+obj); //25Feb2000: Throwing exception if the object isn't registered with the scripting service
     }
     throw new Exception("Can't set a null string as a name"); //throwing exception if a null name was passed in (this is either very rare or can never happen)
    }

    public final LogoObject pSETNAME(InterpEnviron interpenviron, LogoObject params[]) //30-9-1998
        throws LanguageException
    {
      testNumParams(params,1);
      String name = params[0].toString();

      Vector v=getComponentsToTell(null); //get all the components (get a copy, don't call "getComponentToTell()" directly)
      if (v.size()!=0){ //if TELL/ASK=[] then just ignore the command (no one heard it)
       if (v.size()>1) throw new LanguageException("You must tell this to only one component, cause components must have unique names");

       Object obj=v.firstElement();
       try{
        setName(obj,name);
       }catch(Exception e){throw new LanguageException("Couldn't rename object (maybe this name is already in use by some other component)");} //8-4-1999: catching all exceptions

      }
      return LogoVoid.obj;
    }

//NAME:get//

    private String getName(Object obj){ //17Sep1999
     String name=scriptableObjects.getName(obj);
     if(name==null){
      //if (obj instanceof ESlatePart) //07Apr2000: removed check on whether the object is an ESlatePart: it doesn't have to be one (e.g. a JButton)      if (obj instanceof ESlatePart)
       name=theESlateMicroworld.getComponentHandle(obj).getComponentName();
      //else name=(obj!=null)?obj.toString():""; //don't return null!
     }
     return (name!=null)?name:""; //don't return null (check, just in case)
    }

    public final LogoObject pNAME(InterpEnviron interpenviron, LogoObject params[]) //30-9-1998
        throws LanguageException
    {
     testNumParams(params,0);
     Vector v=getComponentsToTell(null); //get all the components (get a copy, don't call "getComponentToTell()" directly)
     try{
      return new LogoWord(getName(v.firstElement()));
     }catch(NoSuchElementException e){ //20Sep1999: fix, "throw new LanguageException" was outside of the "catch" block
      throw new LanguageException("There is no Object to TELL this"); //throw this when there are no registered named objects
     }
   }

//√—¡ÿ≈//

    public final LogoObject pGRAPSE(InterpEnviron interpenviron, LogoObject params[])
        throws LanguageException
    {
        testNumParams(params,0,1); //7Apr2000: using "testNumParams(params,min,max)" instead of calling both "testMaxParams(params,max)" and "testMinParams(params,min)" 

        if(params.length == 0) //???
            interpenviron.mach().console().createEditor("");
        else
        if(params[0] instanceof LogoWord)
        {
            CaselessString caselessstring = params[0].toCaselessString();
            Procedure procedure = interpenviron.mach().resolveProc(caselessstring);
            if(procedure != null)
            {
                interpenviron.mach().console().createEditor(procedure.toLocalizedString());
            }
            else
            {
                Procedure.checkName(caselessstring.str);
                interpenviron.mach().console().createEditor("√…¡ " + caselessstring.str + Machine.LINE_SEPARATOR);
            }
        }
        else
        {
            if(params[0].length() != 3)
                throw new LanguageException("Contents list expected");
            StringWriter stringwriter = new StringWriter();
            IOStream iostream = new IOStream(new BufferedWriter(stringwriter));
            interpenviron.mach().printout(iostream, ((LogoList)params[0]).pickInPlace(0), ((LogoList)params[0]).pickInPlace(1), ((LogoList)params[0]).pickInPlace(2));
            iostream.close();
            interpenviron.mach().console().createEditor(stringwriter.toString());
        }
        return LogoVoid.obj;
    }

//TELL//

    public final LogoObject pTELL(InterpEnviron interpenviron, LogoObject params[])
        throws LanguageException
    {
     //System.out.println("TELL");
     testNumParams(params,1); //18-7-1998 //1-9-1998: changed to use testNumParams
     setComponentsToTell(LogoLogo.toLogoObjectArray(params[0])); //18-7-1998 //31Aug1999: now allowing TELL "x, not only TELL [x]
     return LogoVoid.obj;
    }

//TELLALL//

    public final LogoObject pTELLALL(InterpEnviron interpenviron, LogoObject params[]) //25-7-1998
        throws LanguageException
    { //this should be the same as TELL []
     testNumParams(params,0); //1-9-1998: changed to use testNumParams
     tellingComponents=null; //30-9-1998: now TELLALL is only when null vector (and not with an empty one) //no synchronize(tellingComponents) cause tellingComponents is changing... would use synchronize(this) but not needed (setvalue should be an atomic operation)
     return LogoVoid.obj;
    }

//ASK//

    public final LogoObject pASK(InterpEnviron interpenviron, LogoObject params[]) //18-7-1998
        throws LanguageException,ThrowException
    {
     testNumParams(params,2); //1-9-1998: changed to use testNumParams

     Object obj;
     Vector v=getTellingComponentsCopy(); //keep previous TELL vector
     try{
      setComponentsToTell(LogoLogo.toLogoObjectArray(params[0])); //31Aug1999: now allowing ASK "x [commands], not only ASK [x] [commands]

      //adapted from StandardPrimitives.pREPEAT//
      InterpEnviron interpenviron1 = new InterpEnviron(interpenviron); //keep the current InterpEnviron's settings
      obj = LogoVoid.obj;
      try{obj = params[1].getRunnable(interpenviron.mach()).execute(interpenviron1);}
      finally{interpenviron.setTestState(interpenviron1.getTestState());}

     }
     finally{tellingComponents=v;} //restore previous TELL vector (finally for extra safety)
     return (LogoObject)obj; //return anything the ASK returned (if many components then their primitives that return results (operations) should output reply of only the first one's on the TELL set -> this may even propagate to ASK's output)
    }

//EACH//

    public final LogoObject pEACH(InterpEnviron interpenviron, LogoObject params[]) //18-7-1998
        throws LanguageException,ThrowException
    {
     testNumParams(params,1); //1-9-1998: changed to use testNumParams
     Vector vv=getTellingComponentsCopy(); //keep previous TELL vector
     Vector v=getComponentsToTell(null); //30-9-1998: get components of any class from TELL vector (in a COPY, don't call getComponentsToTell() directly)
     if (v.size()==0) throw new LanguageException("No components to TELL to"); //30-9-1998:checking for zero size
     try{
      tellingComponents=new Vector(1); //preallocate space for just one object
      tellingComponents.addElement(null); //18-7-1998: fixed bug: used elementAt(...,0) but the Vector had no elements added to it
      for(int i=0; i<v.size(); i++){
       tellingComponents.setElementAt(v.elementAt(i),0);

       //adapted from StandardPrimitives.pREPEAT//
       InterpEnviron interpenviron1 = new InterpEnviron(interpenviron); //keep the current InterpEnviron's settings
       Object obj = LogoVoid.obj;
       try{obj = params[0].getRunnable(interpenviron.mach()).execute(interpenviron1);}
       finally{interpenviron.setTestState(interpenviron1.getTestState());}
       if(obj != LogoVoid.obj)
        throw new LanguageException("You don't say what to do with " + obj.toString());

      }
     }
     finally{tellingComponents=vv;} //restore previous TELL vector (finally used cause the above throw command needs to process this line too)
     return LogoVoid.obj; //EACH is a command, not an operation (doesn't OUTPUT a result)
    }

// CONSTRUCTORs ///////////////////////////////////////////////////////////////////

    public ComponentPrimitives()
    {
    //maybe on standard constructor check a static field of Logo component class to get its
    //ESlate Microworld? and remove the next one
    //Logo should set the field when it's first instantiated (the class should set it to null
    //originally)
    }

    public ComponentPrimitives(ESlateMicroworld c){ //14-12-1998: is this needed??? had a bug at the Setup method, that was initializing a local var called theESlateMicroworld, instead of the global static one! //18May1999: ESlateII
     // if (c==null) System.out.println("Error: ESlatePrimitives constructor got a null ESlateMicroworld!!!");
     theESlateMicroworld=c;
    }
//called from outside... instantiated by LOGO component...passing it's ESlateConnetion
//...then installing the instance to the LOGO machine (with InstallPrimitiveGroup)
//...+uninstall on unload?

/////////////////////////////////////////////////////////////////////

    private Console _console;
    public ESlateMicroworld theESlateMicroworld=null; //logo should clear this too when it ends //18May1999: ESlateII
    public Vector tellingComponents=null; //the ESlateComponents mentioned in last TELL command //30-9-1998:now initing to null (all components) //2-10-1998:made public again to clear when Logo ends up

    /** Gets all the components
     *  @return a vector that contains all the components
     */
    public Vector getAllTheComponents(){ //28-7-1998 (theESlateMicroworld should be instantiated first)
     Vector v=scriptableObjects.getAll(); //27-3-1999: now using the scriptableObjects NameService
     // Append the ESlateMicroworld contents to the Vector we got from the
     // scriptableObjects when asked it for its contents
     // 8-4-1999: fix: VectorUtil.addAll now checks for v==null and makes
     // a new Vector if needed
     // 18May1999: ESlateII
     ESlateHandle[] handles = theESlateMicroworld.getESlateHandle().getChildHandles();
     int nCompos = handles.length;
     Object[] compos = new Object[nCompos];
     for (int i=0; i<nCompos; i++) {
       compos[i] = handles[i].getComponent();
     }
     return VectorUtil.addAll(v, compos);
    } //NEVER RETURNS NULL: should return at least one component: Logo

    private Vector getComponentsToTell(){ //17Sep1999: this is private, cause doesn't return a copy of the "tellingComponents" vector object //if tellingComponent=null this tells all (different than getComponentsToTell(null): that one returns a copy of the tellingComponents vector!
     return (tellingComponents!=null)?tellingComponents:getAllTheComponents();
    } //NEVER RETURNS NULL: should return at least one component: Logo

    ////////

    public Object getFirstComponentToTell(Class theClassOrInterface) throws LanguageException { //1Apr2000: temp implementation: don't get a whole vector first to just return the first element (too much temp memory consumption: just find the first suitable element and return it)
     Vector v=getComponentsToTell(theClassOrInterface);
     try{ //prefer try-catch over checking v's size (it's faster, since the no-elements case is much rare and results to an stop-error [no speed is affected that is, since program stops])
      return v.firstElement();
     }catch(Exception e){
      throw new LanguageException("There is no object to TELL this to");
     }
    }

    public Vector getComponentsToTell(Class theClassOrInterface){ //30-9-1998: tellingComponents can be null
     Vector components=getComponentsToTell();
     int size=components.size();
     Vector v;
     if (theClassOrInterface!=null){ //30-9-1998
      v=new Vector(size); //alloc max space for speed
      for (int i=0;i<size;i++){
       Object c=components.elementAt(i); //27-3-1999: using Object and not ESlateComponent variable (want to have scriptable objects that are not ESlateComponents)
       if (theClassOrInterface.isInstance(c)) //IF WE WANT to force only interfaces, we'd also REQUIRE that (the/*ClassOr*/Interface.isInterface()) is true
        v.addElement(c);
       else
        if (c instanceof HasFacets){ //29Nov1999: support for objects constructing through aggregation (using facets)
         Object facet=((HasFacets)c).getFacet(theClassOrInterface);
         if(facet!=null) v.addElement(facet); //if facet not existing, just ignore
        }
      }
     }else{
      v=(Vector)components.clone(); //return a copy!
     } //30-9-1998: if used with getComponentsToTell(null) consider same as getComponentToTell(java.lang.Object) and return all of them
     //if (v!=null) System.out.println("getComponentsToTell("+theClassOrInterface.getName()+") returned : "+v); else println("The result of getComponentsToTell("+theClassOrInterface.getName()+") is null");
     return v;
    }

//////////////////////////////////////////////////////////////////////////////////////

   private final void setComponentsToTell(LogoObject params[]){
    try{
    int total=params.length;
    tellingComponents=new Vector(total);
    //System.out.println("Installing new TELL vector...");
    LogoObject o;
    Object a; //27-3-1999: using Object and not ESlateComponent variable (want to have scriptable objects that are not ESlateComponents)
    String componentName;
    for(int i = 0; i<total; i++){
     o=params[i];
     if(o instanceof LogoList) componentName=((LogoList)o).toStringOpen();
     else componentName=((LogoWord)o).toString();
     // theESlateMicroworld should be not null
     // 18May1999: ESlateII
     ESlateHandle h =
       theESlateMicroworld.getESlateHandle().getChildHandle(componentName);
     if (h != null) {
       a = h.getComponent();
     }else{
       a = null;
     }
     if (a==null) {
      a=scriptableObjects.get(componentName); //27-3-1999: if no ESlateComponent with that name, try the scriptableObjects (other named objects)
      if (a==null){
       _console.putLine("There is no available component named "+componentName);
       continue;
      }
     }
     tellingComponents.addElement(a);
     //System.out.println("...added to TELL vector: "+componentName); //27-3-1999: using componentName instead of a.getComponentName(), since a might not be an ESlateComponent any more
    }
    }catch(Exception e){System.err.println("setComponentsToTell failed!\n");e.printStackTrace();} //8-4-1999
   }

   private Vector getTellingComponentsCopy(){ //needed in ASK&EACH to keep the old value of "tellingComponents" vector (if any) and when finished with what they do have it restored
    return (tellingComponents!=null)?(Vector)tellingComponents.clone():null;
   }

   //

   public ESlateHandle[] getHandlesToTell(){ //17Sep1999
    Vector v=getComponentsToTell();
    int count=v.size();
    ESlateHandle[] handles=new ESlateHandle[count];
    for(int i=0;i<count;i++) handles[i]=theESlateMicroworld.getComponentHandle(v.elementAt(i));
    return handles;
   }

////////////////////////////////////////////////////////////////////////////////////


   // SCRIPTING for not necessarily E-Slate components //////////////////

   public static class ScriptingNameService extends CaseInsensitiveNameService{ //27-3-1999 //!!!should register as a listener on the NameService instead of subclassing it //29Aug1999: using "CaseInsensitiveNameService" instead of the plain, case-sensitive "NameService"

    public void put(String name, Object obj) throws INameService.NameExistsException{
     super.put(name,obj);
     if (obj instanceof LogoScriptable)
      loadPrimitives(((LogoScriptable)obj).getSupportedPrimitiveGroups()); //install primitive in all LOGO machines
    }

    public void putUnique(String name, Object obj){
     super.putUnique(name,obj);
     if (obj instanceof LogoScriptable)
      loadPrimitives(((LogoScriptable)obj).getSupportedPrimitiveGroups()); //install primitive in all LOGO machines
    }

    public void putNumbered(String name, Object obj){
     super.putNumbered(name,obj);
     if (obj instanceof LogoScriptable){
      loadPrimitives(((LogoScriptable)obj).getSupportedPrimitiveGroups()); //install primitive in all LOGO machines
     }
    }

   }

   public static ScriptingNameService scriptableObjects=new ScriptingNameService(); //27-3-1999: a scriptable object that is not an ESlate component must register here (also, if it wants to load some primitive groups it must impementing LogoScriptbale)

   public static Vector machines=new Vector(); //27-3-1999: keep pointer to all LOGO machines

   public static void loadScriptableObjectsPrimitives(MyMachine m){ //8-4-1999 //Logo reimplements this at startWatchingMicroworld (merge those two & remove from there?)
    Vector v=scriptableObjects.getAll();
    for(int i=v.size();i-->0;){
     Object obj=v.elementAt(i);
     if(obj instanceof LogoScriptable)
      m.loadPrimitives(((LogoScriptable)obj).getSupportedPrimitiveGroups());
    }
   }

/*
   public static void loadAllComponentsPrimitives(MyMachine m){ //8-4-1999 //???
    Vector v=getAllTheComponents();
    for(int i=v.size();i-->0;){
     Object obj=v.elementAt(i);
     if(obj instanceof LogoScriptable)
      m.loadPrimitives(((LogoScriptable)obj).getSupportedPrimitiveGroups());
    }
   }
*/

   public static void loadPrimitives(String[] p){ //27-3-1999: install primitives in all LOGO machines
    for(int i=machines.size();i-->0;)
     ((MyMachine)machines.elementAt(i)).loadPrimitives(p);
   }

}
