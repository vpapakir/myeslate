//Title:        E-Slate
//Version:      27Aug1999
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  ESlateNameSupport class

package gr.cti.eslate.scripting;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.ESlate;

public class ESlateNameSupport extends NameSupport implements ESlatePart {

 public void unregister(){
  if (handle!=null) {
   handle.dispose();
   handle=null;
  }
 }

// ESlatePart ////////////

 private transient ESlateHandle handle;

 public ESlateHandle getESlateHandle(){
  if(object==null) handle=null; //if object is null, then return null handle
  else if (handle==null) handle=ESlate.registerPart(object); //if object non null and handle is null, make new handle and keep it
  return handle; //return current handle
 }

// HasName ////////////

  public String getName(){
   return getESlateHandle().getComponentName();
  }

  public void setName(String value){
   try{
    getESlateHandle().setUniqueComponentName(value);
   }catch(gr.cti.eslate.base.RenamingForbiddenException e){
    System.err.println("Coundn't change "+getESlateHandle().getComponentName()+" to "+value);
   }
  }

}
