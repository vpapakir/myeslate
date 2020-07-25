//Title:        Stage
//Version:      12Feb2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.models;

import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;

public interface HasVetoableProperties
{
 public boolean hasVetoableChangeListeners(String property);
 public void addVetoableChangeListener(VetoableChangeListener l);
 public void removeVetoableChangeListener(VetoableChangeListener l);
 public void fireVetoableChange(String property,Object oldvalue,Object newvalue) throws PropertyVetoException;
}
