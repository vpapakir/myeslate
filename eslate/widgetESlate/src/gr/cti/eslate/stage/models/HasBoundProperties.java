//Title:        Stage
//Version:      12Feb2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.models;

import java.beans.PropertyChangeListener;

public interface HasBoundProperties
{
 public boolean hasPropertyChangeListeners(String property);
 public void addPropertyChangeListener(PropertyChangeListener l);
 public void removePropertyChangeListener(PropertyChangeListener l);
 public void firePropertyChange(String property,Object oldvalue,Object newvalue);
}
