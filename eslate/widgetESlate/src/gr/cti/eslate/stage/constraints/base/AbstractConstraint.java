//!!! TEMP: readExternal works only with Scripting namespace: when BaseObject gets itself an ESlateHandle some other solution must be provided here !!!

//2Dec1999 - localized
//11May2000 - externalizable!
//08Jun2000 - using ObjectHash mechanism
//29Mar2001 - added compatibility with old ObjectHash mechanism (now using ESlateFieldMap)

package gr.cti.eslate.stage.constraints.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;

import gr.cti.eslate.stage.constraints.models.IConstraint;
import gr.cti.eslate.stage.constraints.exception.*;

import gr.cti.utils.ObjectHash;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import java.io.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public abstract class AbstractConstraint
 implements java.io.Externalizable, //11May2000
            IConstraint,
            PropertyChangeListener,
            VetoableChangeListener
{

 static final long serialVersionUID = 20101999L; //20Oct1999: serial-version, so that new vers load OK
  /**
   * Temporary storage for member names.
   */
  private String[] pendingNames = null;
 //public final static String STORAGE_VERSION="1"; //29Aug2000
 public final static int STORAGE_VERSION=1; //6/6/2002
/*
 This class is abstract, descendents must define these methods:
 - getTitle
 - force
 - isValid
 - storeMembers
 - getMembers
 - dispose
*/

 public static String getTitle(){return MessagesBundle.localize("Unknown");} //29Jul1999: unfortunately can't have both abstract&static modifiers, so must provide an implementation here (also, note that we need to have this static and didn't put it at the IConstraint i/f cause we need to check the title without instantiating the constraint)

 public AbstractConstraint(){ //must call "setMembers" or "forceMembers" after constructing the constraint //29Jul1999: have a public constructor, may want to externalize or something
 }

 @SuppressWarnings("unchecked")
public static IConstraint createConstraint(Class constraintClass)
  throws IllegalAccessException,InstantiationException //8Aug1999
 {
  //System.out.println("Creating "+constraintClass+" instance");
  if(IConstraint.class.isAssignableFrom(constraintClass))
   return (IConstraint)constraintClass.newInstance();
  else throw new RuntimeException("Tried to use \"createConstraint\" passing it a class object that doesn't implement the IConstraint interface");
 }

 @SuppressWarnings("unchecked")
public static IConstraint createConstraint(Object[] members,Class constraintClass) //8Aug1999: now using the createConstraint(Class) method
  throws IllegalAccessException,InstantiationException,InvalidConstraintMembersException
 {
  IConstraint c=createConstraint(constraintClass); //may throw IllegalAcccessException or InstantiationException
  //System.out.println("Setting constraint members...");
  try{ c.setMembers(members); }
  catch(InvalidConstraintMembersException e){
   c.dispose(); //8Aug1999: if members are invalid do dispose the constraint, in case it did get to hold some of the members (not really needed)
   throw e; //rethrow the exception
  }
  return c;
 }

 @SuppressWarnings("unchecked")
public static IConstraint createAndEnforceConstraint(Object[] members,Class constraintClass)
  throws IllegalAccessException,InstantiationException,InvalidConstraintMembersException,UnforcableConstraintException //29Jul1999
 {
  IConstraint c=createConstraint(members,constraintClass);
  //System.out.println("Trying to enforce constraint...");
  try{ c.enforce(null); }
  catch(UnforcableConstraintException e){
   c.dispose(); //8Aug1999: if the constraint can't be enforced, dispose it (cause it's a [nonvetoable/vetoable] property listener to its members)
   throw e; //rethrow the exception
  }
  return c;
 }

 public void forceMembers(Object[] o) throws InvalidConstraintMembersException,UnforcableConstraintException { //29Jul1999
  setMembers(o);
  enforce(null); //constraint must hold!
 }

 ////////////////////

 protected abstract void force(PropertyChangeEvent e); //29Jul1999: sets Members and should try to force the constrain to hold else throw exception (the event tells us which constraint member has changed)

 public final void enforce(PropertyChangeEvent e) throws UnforcableConstraintException{ //29Jul1999: should try to force the constrain to hold else throw exception
  force(e);
  if(!holds()) throw new UnforcableConstraintException(MessagesBundle.localize("cantEnforce"),this);
 }

// IConstraint //////////////////

 public boolean hasMember(Object o){ //19May2000
  Object members[]=getMembers();
  for(int i=members.length;i-->0;)
   if(members[i]==o) return true;
  return false;
 }

 protected abstract void storeMembers(Object[] o) throws InvalidConstraintMembersException; //descendents must do the storage as they wish

 public final void setMembers(Object[] o) throws InvalidConstraintMembersException{ //this is final, couldn't rely on descendents not forgetting to call super.setMember(), so we only allow them to override the storeMembers protected proc
  dispose(); //remove old members
  if(o!=null) //11May2000: calling "setMembers(null)" on a constraint is now the same as calling "dispose()" on it
   storeMembers(o);
 } //so that descendents can call this instead of the above constructor which also calls enforce(null) [and other parameters from descendent's constructor may have not been kept for the enforce implementation to use them]

// PropertyChangeListener ////////////

 public void propertyChange(PropertyChangeEvent e){ //PropertyChangeListener//
  //System.out.println("AbstractConstraint got PropertyChange event");
  force(e); //try to keep the constraint to hold, don't check if succeeded (don't call "enforce", befause properyChangeEvent is just a notification of a de-facto change which we can't stop, so having the "enforce" call check using "isValid" if the constraint was forced OK and throw an exception which we would ignore wouldn't be of any use)
 }

// VetoableChangeListener ////////////

 public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException{ //VetoableChangeListener//
  //System.out.println("AbstractConstraint got VetoableChange event");
  if(!isValid(e)) throw new PropertyVetoException(/*MessagesBundle.localize("notAllowed")+*/this.toString(),e);
 }

/////////////////////////////////////////////////////////////////////////////

 public String[] getMemberNames(){
  Object members[]=getMembers();
  int count=(members!=null)?members.length:0; //do check for null and write #members=0 in such a case
  String result[]=new String[count];
  for(int i=count;i-->0;){
   result[i]=members[i].toString(); //implemented to return object's name in BaseObject from which both SceneObject and ControlPoint descend (these are the only supported constraint members right now)
  }
  return result; //don't return null
 }

  /**
   * When invoking <code>setMembersByName()</code> while
   * loading a <code>Scene</code> component from disk, the method will fail,
   * as it will try to locate components that have not yet been added to the
   * microworld's * component hierarchy. In this case, the provided member
   * names will be stored. After the <code><Scene</code> component has been
   * loaded, invoke <code>setMembersByName()</code> using these stored names,
   * which will now be resolved correctly.
   * @return    The member names that failed to be resolved.
   */
  public String[] getPendingNames()
  {
    return pendingNames;
  }

  public void setMembersByName(String[] memberNames)
    throws InvalidConstraintMembersException
  {
    setMembersByName(memberNames, null);
  }

  public void setMembersByName(String[] memberNames, ESlateHandle sceneHandle)
    throws InvalidConstraintMembersException
  {
    if (memberNames != null) {
      int count = memberNames.length;
      if (count != 0) {
        Object members[] = new Object[count];
        ESlateHandle topHandle = getTopHandle(memberNames, sceneHandle);
        boolean failed = false;
        for(int i=count; i-->0; ) {
          // !!! TEMP: works only with Scripting namespace: when BaseObject
          // gets itself an ESlateHandle some other solution must be provided
          // here !!!
          //members[i] = gr.cti.eslate.scripting.logo.ComponentPrimitives.scriptableObjects.get(memberNames[i]);
          ESlateHandle h = topHandle.getChildHandle(memberNames[i]);
          if (h != null) {
            members[i] = h.getComponent();
          }else{
            members[i] = null;
            failed = true;
          }
        }
        if (failed) {
          pendingNames = memberNames;
        }else{
          setMembers(members);
          pendingNames = null;
        }
        return;
      }
    }
    setMembers(null);
  }

  /**
   * Returns the topmost available E-Slate handle in the component hierarchy
   * of the scene containing a given set of components.
   * @param     names   The names of the components. If
   *                    <code>sceneHandle</code> is not <code>null</code>,
   *                    the names should be the full path names of the
   *                    components. If <code>sceneHandle</code> is
   *                    <code>null</code>, then the names should be simple
   *                    names.
   * @param     sceneHandle     The E-Slate handle of the scene where the
   *                    components are shown.
   * @return    If <code>sceneHandle</code> is not <code>null</code>, then
   *            the topmost reachable parent of <code>sceneHandle</code> is
   *            returned. If <code>sceneHandle</code> is <code>null</code>,
   *            then the E-Slate handle of the microworld containing the
   *            components is returned.
   */
  private static ESlateHandle getTopHandle
    (String[] names, ESlateHandle sceneHandle)
  {
    if (sceneHandle == null) {
      return getESlateMicroworldHandle(names);
    }else{
      ESlateHandle top = sceneHandle;
      for (ESlateHandle h=sceneHandle; h!=null; h=h.getParentHandle())
      {
        top = h;
      }
      return top;
    }
  }

  /**
   * Returns the E-Slate handle of the microworld containing a given set of
   * components.
   * @param     names   The full path names of the components.
   * @return    The E-Slate handle of the microworld containing the largest
   *            number of components matching the given names. In regular use,
   *            there should only be one microworld, and this method will
   *            provide a reference to that instance.
   */
  private static ESlateHandle getESlateMicroworldHandle(String[] names)
  {
    int nNames = names.length;
    ESlateMicroworld[] microworlds = ESlateMicroworld.getMicroworlds();
    ESlateHandle result = null;
    int nMicroworlds = microworlds.length;
    int maxMatch = -1;
    for (int i=0; i<nMicroworlds; i++) {
      ESlateHandle h = microworlds[i].getESlateHandle();
      int total = 0;
      for (int j=0; j<nNames; j++) {
        if (h.getChildHandle(names[j]) != null) {
          total++;
        }
      }
      if (total > maxMatch) {
        maxMatch = total;
        result = h;
      }
    }
    return result;
  }

 //PERSISTENCE//

 public static final String MEMBER_NAMES_PROPERTY="memberNames";


/*

 public ObjectHash getProperties(){

  ObjectHash properties=new ObjectHash();
  properties.put(MEMBER_NAMES_PROPERTY,getMemberNames());
  return properties;
 }
*/

 public void setProperties(ObjectHash properties){ //29Mar2001-FINAL-VERSION: compatibility with old persistence code
  if(properties==null) return;
  try{setMembersByName((String[])properties.get(MEMBER_NAMES_PROPERTY));}catch(Exception e){try{setMembers(null);}catch(Exception ex){}}
 }; //descendent must call this


 public ESlateFieldMap2 getProperties(){
  ESlateFieldMap2 properties=new ESlateFieldMap2(STORAGE_VERSION);
  properties.put("MEMBER_NAMES_PROPERTY",getMemberNames());
  return properties;
 }

 public void setProperties(StorageStructure properties){
  if(properties==null) return;
  try{
      setMembersByName((String[])properties.get("MEMBER_NAMES_PROPERTY"));
  }catch(Exception e){try{setMembers(null);}catch(Exception ex){}}
 }; //descendent must call this



 //Externalizable//

 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
  try{
   Object o=in.readObject();
   if(o instanceof StorageStructure) //29Mar2001: compatibility
    setProperties((StorageStructure)o);
    //setProperties((ESlateFieldMap)o);
   else
    setProperties((ObjectHash)o);
  }catch(Exception e){
   //e.printStackTrace();
   System.err.println(getClass().getName()+"@AbstractConstraint: Error loading properties hashtable");
  }
 }

 public void writeExternal(ObjectOutput out) throws IOException {
  try{
   ESlateFieldMap2 properties=getProperties();
   if(properties!=null) out.writeObject(properties);
  }catch(Exception e){
   //e.printStackTrace();
   System.err.println(getClass().getName()+"@AbstractConstraint: Error saving properties hashtable");
  } //19Apr2000: added try-catch
 }


}
