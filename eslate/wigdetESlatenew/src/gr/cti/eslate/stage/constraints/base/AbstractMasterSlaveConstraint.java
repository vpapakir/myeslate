//2Dec1999 - localized

package gr.cti.eslate.stage.constraints.base;

import gr.cti.eslate.stage.constraints.exception.*;

public abstract class AbstractMasterSlaveConstraint
 extends AbstractMastersSlavesConstraint
{

 static final long serialVersionUID = 5071282266746200304L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("UnknownMasterSlave");} //2Dec1999

 public Object[] getMasters(){
  try{ //this will be thrown everytime dispose is called, maybe should implement a better checking instead
   return new Object[]{members[0]};
  }catch(Exception e){
   return null;
  }
 }

 public Object[] getSlaves(){
  try{ //this will be thrown everytime dispose is called, maybe should implement a better checking instead
   return new Object[]{members[1]};
  }catch(Exception e){
   return null;
  }
 }

 public void setMasterSlave(Object master,Object slave)throws InvalidConstraintMembersException{ //for a Master-Slave 2 member relationship (for compatibility with the original 2-member Master-Slave constraint class descendents)
  setMembers(new Object[]{master,slave});
 }

 protected void storeMembers(Object[] o) throws InvalidConstraintMembersException{ //29Jul1999
  if (o.length!=2) throw new InvalidConstraintMembersException(MessagesBundle.localize("need2members"),this,o);
  super.storeMembers(o);
 }

}
