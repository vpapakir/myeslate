package gr.cti.eslate.stage.constraints.exception;

import gr.cti.eslate.stage.constraints.models.IConstraint;

public class InvalidConstraintMembersException extends ConstraintSetupException //29Jul1999
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private Object[] members;

 public InvalidConstraintMembersException(String message,IConstraint constraint,Object[] members){
  super(message,constraint);
  this.members=members;
 }

 public Object[] getMembers(){ //those are not the same as getConstraint().getMembers(), since they didn't make it to become constraint members
  return members;
 }

}
