package gr.cti.eslate.stage.constraints.exception;

import gr.cti.eslate.stage.constraints.models.IConstraint;

public class UnforcableConstraintException extends ConstraintSetupException //29Jul1999
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public UnforcableConstraintException(String message,IConstraint constraint){
  super(message,constraint);
 }

}
