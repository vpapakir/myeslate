//Title:        E-Slate
//Version:      29Aug1999
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  ScriptableObjectsNameSupport class

package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.scripting.NameSupport;

public class ScriptableObjectsNameSupport extends NameSupport{ //should do as in ESlateNameSupport, that is register when setName is called for the first time (instead of doing a rename) and don't need to overrride the "register" method

 public void register(Object object,String name){
  this.object=object; //keep the object param and don't call super.register at any point, since we're registering the object with a unique name... the ancestor would try to rename the object again 
  try{
   gr.cti.eslate.scripting.logo.ComponentPrimitives.scriptableObjects.putNumbered(name,object);
  }catch(Exception e){
   e.printStackTrace(); //17May2000
   System.err.println("Couldn't register "+name);
  }; //use this for now, then use putUnique that will rename the object if needed [and return the new name] (maybe NameService should also have some putName, putUniqueName that changes the registered object's name field)
 }

 public void unregister(){
  ComponentPrimitives.scriptableObjects.removeByReference(object);
 }

// HasName ////////////

  public String getName(){
   return ComponentPrimitives.scriptableObjects.getName(object); //map the current instance to a name from the nameService and return it
  }

  public void setName(String value){
   try{
    ComponentPrimitives.scriptableObjects.setUniqueName(object,value); //rename the current instance in the nameService //29Aug1999: using setUniqueName
   }catch(Exception e){
    e.printStackTrace(); //17May2000
    System.err.println("Couldn't set object's name to "+value); //29Aug1999
   }
  }

}
