//Title:        NameService
//Version:      18May2000
//Copyright:    Copyright (c)1999-2000
//Author:       George Birbilis
//Company:      CTI
//Description:  A naming service that guarantees unique naming

package gr.cti.utils;

import java.util.Vector;

@SuppressWarnings(value={"unchecked"})
public class NameService implements INameService{

 protected boolean areEqual(String name1,String name2){ //29Aug1999: override this at a subclass to have a case-insensitive name service

  return name1.equals(name2);
 }

// CONSTRUCTOR //////////////////////////

 public NameService(){
  names=new Vector();
  objects=new Vector();
 }

 //////////////////////////////////////////////////

 private boolean containsName(String name){ //29Aug1999
  return(get(name)!=null); //no need to call a non-synchronized "get", to avoid lock-up (cause this gets called from inside other synchronized methods), Java does supports reentrant locks when we're on the same thread!
 }

 public boolean contains(Object obj){ //17Sep1999
  return (objects.indexOf(obj)>=0);
 }

 ///////////////////////////////////////////////

 private String generateNumberedName(String name){ //29Aug1999
  int counter=1; //start with 1, e.g. for "Ball" have Ball1, Ball2, etc.
  String theName;
  do{
   theName=name+String.valueOf(counter++);
  }while(containsName(theName));
  //System.out.println(theName);
  return theName;
 }

 private String generateUniqueName(String name){ //29Aug1999
  int counter=2; //start with 2, e.g. for "Ball" have Ball, Ball2, Ball3, etc.
  String theName=name;
  while(containsName(theName)){
   theName=name+String.valueOf(counter++);
  }
  //System.out.println(theName);
  return theName;
 }

 /////////////////////////////////////////////////

 private void _put(String name,Object obj){ //internal, non-synchronized "put"
  names.addElement(name);
  objects.addElement(obj);
 }

 synchronized public void put(String name, Object obj) throws INameService.NameExistsException{
  if(containsName(name)) throw new INameService.NameExistsException(name);
  _put(name,obj);
 }

 synchronized public void putNumbered(String name,Object obj){
  _put(generateNumberedName(name),obj); //don't call put, would lock-up cause we're both synchronized //this isn't true: Java supports reentrant locks: since we're on the same thread!
 }

 synchronized public void putUnique(String name,Object obj){
  _put(generateUniqueName(name),obj); //don't call put, would lock-up cause we're both synchronized
 }

 /////////////////////////////////////

 private int findName(String name){ //29Aug1999
  for(int i=names.size();i-->0;)
   if(areEqual((String)names.elementAt(i),name)) return i;
  return -1;
 }

 synchronized public Object get(String name){
  int index=findName(name);
  if (index<0) return null; else return objects.elementAt(index);
 }

 synchronized public String getName(Object obj){ //returns null when "obj" not found (when object is not registered to the name service)
  int index=objects.indexOf(obj);
  if (index<0) return null; else return (String)names.elementAt(index);
 }

 ///////////////////////////////////////////

 synchronized public void setName(Object obj,String name) throws INameService.NameExistsException{ //maybe this should do a "put" as well if the object is not registered (instead of ignoring)
  //09May2000: removed, want to allow changes from M1 to m1 for example for aesthetic reasons// if(areEqual(name,getName(obj))) return; //29Aug1999: if trying to set its own name just ignore and return
  Object o=get(name);
  if(o!=null && o!=obj) //9May2000
   throw new INameService.NameExistsException(name); //29Aug1999: if trying to set to some other object's name, throw exception
  int index=objects.indexOf(obj);
  if (index>=0) names.setElementAt(name,index); //28Aug1999: added check for non-existent objects (do-nothing instead of throwing an array-index-out-of-bounds exception)
  else System.err.println("NameService.setName: ignoring request to set the name of an unregistered object"); //11May2000
 }

 public void setUniqueName(Object obj,String name){ //29Aug1999
  if(areEqual(name,getName(obj))) return; //20Sep1999: if trying to set its own name just ignore and return (cause else it would damage its existing name, e.g. Ball would get a Ball1, Ball1 would get a Ball12, Ball2 would get a Ball22 etc.)
  try{   
   setName(obj,generateUniqueName(name));
  }catch(Exception e){
   System.err.println("NameService::setUniqueName failed for name "+name); //if this gets printout, it must be some internal error (generateUniqueName couldn't generate a unique one and setName failed)
  }
 }

 /////////////////////////////////////////////////

 synchronized public Object removeByName(String name){ //30Jun1999: now returning the removed object and returning null if object was not found (wasn't checking for indexOf returning a "-1") //29Aug1999: changed so that it doesn't do name comparisons, but just delegates to "get" and "removeByReference" method calls
  Object o=get(name);
  return removeByReference(o);
 }

 synchronized public Object removeByReference(Object obj){ //30Jun1999: now returning the removed object and returning null if object was not found (wasn't checking for indexOf returning a "-1")
  int index=objects.indexOf(obj);
  if(index<0) return null;
  else{
   names.removeElementAt(index); //26May1999: fixed-was-using-Java2-only-method
   objects.removeElementAt(index); //26May1999: fixed-was-using-Java2-only-method
   return obj; //if did find&remove, return the object we removed (the one passed as the argument to this method)
  }
 }

 ///////////////////////////////////////////////

 public Object[] getAllObjects(){ //26Jul1999: return all objects, according to the implemented INameService i/f
  return VectorUtil.getElements(objects);
 }

 public String[] getAllNames(){ //26Jul1999: return all objects' names, according to the implemented INameService i/f
  Object[] o=VectorUtil.getElements(names); //18May2000: fixed-bug: was using "objects" field instead of "names"
  String[] s=new String[o.length];
  try{
   System.arraycopy(o,0,s,0,o.length);
  }
  catch(Exception e){
   System.err.println("Internal error at NameService.getAllNames() implementation");
   return null;
  }
  return s;
 }

 public Vector getAll(){ //doesn't need to be synchronized
  return (Vector)objects.clone();
 }


// FIELDS ////////////////////////////////

 Vector names;
 Vector objects;

}
