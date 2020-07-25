//Title:        Your Product Name
//Version:      19Apr2000
//Copyright:    Copyright (c) 1998
//Author:       G.Birbilis
//Company:      CTI
//Description:  calls "destroy" on a collection of components when an ESlateHandle gets disposed of

package gr.cti.eslate.birbilis;

import java.util.Vector;

import gr.cti.utils.BeanUtil;
import gr.cti.utils.VectorUtil;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateAdapter;

@SuppressWarnings(value={"unchecked"})
public class ESlateFinalizer extends ESlateAdapter{ //only for components, applets don't need a finalizer (container should call the "destroy" method on them)

  public Vector objects=new Vector();

  public ESlateFinalizer(ESlateHandle handle){
   super();
   if(handle!=null) handle.addESlateListener(this); //29Oct1999: don't fail to create ESlateFinalizer if given a null handle (return an object that does nothing - it will be grabbed by next GC invocation, since the client isn't supposed to hold it in a class field and cause it isn't attatched as a listener to some handle if a null handle was passed to it)
  }

  public ESlateFinalizer(ESlateHandle handle,Object objectToDestroy){
   this(handle);
   objects.addElement(objectToDestroy);
  }

  public ESlateFinalizer(ESlateHandle handle,Vector objectsToDestroy){
   this(handle);
   VectorUtil.addAll(objects,objectsToDestroy);
  }

  public void handleDisposed(gr.cti.eslate.base.HandleDisposalEvent p0){ //16Sep1999: does this get notifications only for our handle or for others too?
   //System.out.println("ESlateHandle disposed, doing cleanup..."); //19Apr2000: print only when debugging, commented-out
   for(int i=0,count=objects.size();i<count;i++)
    try{
     //System.out.println("ESlateFinalizer: calling an object's \"destroy\" method");
     BeanUtil.doVerb(objects.elementAt(i),"destroy");
    }catch(Exception e){
    }finally{
     objects.setElementAt(null,i); //release the objects (just in case this class stays hanging from somewhere)
    }
  }

}
