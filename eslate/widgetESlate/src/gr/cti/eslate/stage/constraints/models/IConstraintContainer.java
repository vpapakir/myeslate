//Title:        Stage
//Version:      20Apr2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Some object that hosts IConstraint implementator objects

package gr.cti.eslate.stage.constraints.models;

import gr.cti.eslate.stage.constraints.models.IConstraint;

public interface IConstraintContainer {

 public int getConstraintsCount(); //needed for Lists, need to know size without getting all the objects
 public IConstraint getConstraint(int index); //needed for Lists, need to get just one element at a certain index

 public void addConstraint(IConstraint c);
 public void removeConstraint(IConstraint constraint);

}
