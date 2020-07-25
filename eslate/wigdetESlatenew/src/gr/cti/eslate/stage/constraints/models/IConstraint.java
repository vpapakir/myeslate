package gr.cti.eslate.stage.constraints.models;

import gr.cti.eslate.stage.constraints.exception.*;

import java.beans.PropertyChangeEvent;

public interface IConstraint { //20Oct1999: renamed to "IConstraint" from "Constraint"

 public void forceMembers(Object[] o) throws InvalidConstraintMembersException,UnforcableConstraintException; //29Jul1999: sets Members and should try to force the constrain to hold else throw exception
 public void setMembers(Object[] o) throws InvalidConstraintMembersException;
 public Object[] getMembers();
 public boolean hasMember(Object o); //19May2000

 public void enforce(PropertyChangeEvent e) throws UnforcableConstraintException; //if e is null, assume values are already set, but established constraint may not yet hold (that is may be "false") //29Jul1999: should try to force the constrain to hold else throw exception (passing the event to tell which constaint member changed)
 public boolean isValid(PropertyChangeEvent e);
 public boolean holds(); //29Jul1999

 public void dispose();

}