package gr.cti.eslate.stage.constraints.base;

import gr.cti.eslate.stage.models.HasBoundProperties;
import gr.cti.eslate.stage.models.HasVetoableProperties;
import gr.cti.eslate.stage.constraints.exception.*;

public abstract class AbstractMastersSlavesConstraint
 extends AbstractConstraint
{

 static final long serialVersionUID = -4316013804704051591L; //8Jun2000: serial-version, so that new vers load OK

 protected Object[] members; //storable

//////////////////////////////////

 protected void storeMembers(Object o[]) throws InvalidConstraintMembersException{ //must copy the objects, since we're passed an array reference and its entries might change in the future, resulting in it pointing to other objects
  members=o;
  addToPropertyChangeNotifier();
  addToVetoableChangeNotifier();
 }

/////////////////////////////////////

 public Object[] getMembers(){
  return members;
 }

 public abstract Object[] getMasters();
 public abstract Object[] getSlaves();

////////////////////////////////////////

 protected Object[] getMembersPart(int fromi,int toi){
  int count=toi-fromi+1;
  Object[] part=new Object[count];
  System.arraycopy(members,fromi,part,0,count);
  return part;
 }

///////////////////////////////////////////

 private void addToVetoableChangeNotifier(){
  try{
   Object slaves[]=getSlaves();
   if(slaves!=null){
    for(int i=slaves.length;i-->0;)
     ((HasVetoableProperties)slaves[i]).addVetoableChangeListener(this);
   }
  }catch(Exception e){
   System.err.println("Exception caught by "+getClass()+"addToVetoableChangeNotifier"); //11Aug1999: ignore any exceptions thrown (in case some getMasters/getSlaves implementation or some Notifiers throw a RuntimeException)
  }
 }

 private void addToPropertyChangeNotifier(){
  try{
   Object masters[]=getMasters();
   if(masters!=null){
    for(int i=masters.length;i-->0;)
     ((HasBoundProperties)masters[i]).addPropertyChangeListener(this);
   }
  }catch(Exception e){
   System.err.println("Exception caught by "+getClass()+"addToPropertyChangeNotifier"); //11Aug1999: ignore any exceptions thrown (in case some getMasters/getSlaves implementation or some Notifiers throw a RuntimeException)
  }
 }

 ////////////////////////////

 private void removeFromVetoableChangeNotifier(){
  try{
   Object slaves[]=getSlaves();
   if(slaves!=null){
    for(int i=slaves.length;i-->0;)
     ((HasVetoableProperties)slaves[i]).removeVetoableChangeListener(this);
   }
  }catch(Exception e){
   System.err.println("Exception caught by "+getClass()+"removeFromVetoableChangeNotifier"); //11Aug1999: ignore any exceptions thrown (in case some getMasters/getSlaves implementation or some Notifiers throw a RuntimeException)
  }
 }

 private void removeFromPropertyChangeNotifier(){
  try{
   Object masters[]=getMasters();
   if(masters!=null){
    for(int i=masters.length;i-->0;)
     ((HasBoundProperties)masters[i]).removePropertyChangeListener(this);
   }
  }catch(Exception e){
   System.err.println("Exception caught by "+getClass()+"removeFromPropertyChangeNotifier"); //11Aug1999: ignore any exceptions thrown (in case some getMasters/getSlaves implementation or some Notifiers throw a RuntimeException)
  }
 }

////////////////////////////////

 public void dispose(){
  removeFromPropertyChangeNotifier();
  removeFromVetoableChangeNotifier();
  members=null; //release the members array
 }

}
