package gr.cti.eslate.stage.constraints.exception;

import gr.cti.eslate.stage.constraints.models.IConstraint;

public class ConstraintSetupException extends Exception //29Jul1999
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private IConstraint constraint;

 public ConstraintSetupException(String message,IConstraint constraint){
  super(message);
  this.constraint=constraint;
 }

 public IConstraint getConstraint(){
  return constraint;
 }

}
