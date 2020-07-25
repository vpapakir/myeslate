//Title:        E-Slate
//Version:      27Aug1999
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  NameSupport class

package gr.cti.eslate.scripting;

import gr.cti.eslate.scripting.HasName;

public abstract class NameSupport implements HasName {

 protected Object object;

/*  //don't have a "register" procedure, user could call register("name") by accident instead of register(obj) or register(obj,"name") and thus have some String object registered... this is quite hard an error to find, cause printing out the object will print the name (this string) and user might be fooled in thinking he passed his object OK to the register routine
 public void register(Object object){
  this.object=object;
 }
*/

 public void register(Object object, String name){
  this.object=object; //28Aug1999: don't call register(object), cause if that one is overriden to call this one with a default name, then an infinite loop will happen 
  setName(name); //the setName should do the registering, if the object hasn't already been registered (else the subclass can override this method, add code to register, then call super.register)
 }

 public abstract void unregister();

}
