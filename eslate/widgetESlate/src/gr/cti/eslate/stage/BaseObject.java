//Title:        Stage/BaseObject
//Version:      2Apr2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage;

import gr.cti.eslate.stage.models.HasBoundProperties;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import gr.cti.eslate.base.*;
import gr.cti.eslate.stage.models.HasVetoableProperties;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.beans.VetoableChangeListener;
import gr.cti.eslate.stage.models.*;
import gr.cti.eslate.utils.*;

/**
 * @version     2.0.0, 25-May-2006
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public abstract class BaseObject implements AsBaseObject, //18May2000
                                          HasBoundProperties, //12Feb2000
                                          HasVetoableProperties, //12Feb2000
                                          ESlatePart,
                                          gr.cti.eslate.scripting.LogoScriptable{ //16May2000

  //12May2000: serial-version, so that new vers load OK
  static final long serialVersionUID = 6365704663715202820L;
  //public final static String STORAGE_VERSION="1"; //29Aug2000
  public final static int STORAGE_VERSION=1; //6/6/2002
  //11May2000: moved here from SceneObject descendent: the ControlPoint descendent needs to
  //save it name too (needed for Constraints' persistence)
  public static final String NAME_PROPERTY="name";
  //private static final String PROPERTY_CHANGE_LISTENERS="propertyChangeListeners"; //19Apr2000
  //private static final String VETOABLE_CHANGE_LISTENERS="vetoableChangeListeners"; //19Apr2000
  private PropertyChangeSupport propertyChangeSupport=new PropertyChangeSupport(this); //12Feb2000
  private VetoableChangeSupport vetoableChangeSupport=new VetoableChangeSupport(this); //8Aug1999
  protected ESlateHandle handle;

  //CONSTRUCTOR
  public BaseObject(){
  }

  //DESCTRUCTOR
  public void dispose(){
  }

  //8Aug1999 //26Feb2000: moved here from SceneObject class
  public String toString(){
    //must return the object's name cause the Constraints externalization relies on this for
    //a constraint to get and save its members' names (assumes "toString" returns a controlPoint
    //or sceneObject's name)
    //return getName();
    return handle.getComponentPathName();
  }

  //HasName
  //30Jun1999: override this to provide a base name for a descendent
  //11May2000: moved here from SceneObject descendent
  public abstract String getBaseName();

  public String getName(){
    return handle.getComponentName();
  }

  public void setName(String value){
    if(value==null || value.length()==0){
      System.err.println("BaseObject.setName: ignoring effort to set an empty or null name");
      return;
    }
    try{
      //handle.setComponentName(value);
      //System.out.println("getESlateHandle(): "+getESlateHandle());
      getESlateHandle().setComponentName(value);
    }catch(Exception ex){
        System.out.println("Exception in setting the name here!!");
        ex.printStackTrace();
    }
  }

  //LogoScriptable
  public String[] getSupportedPrimitiveGroups(){
    return new String[]{"gr.cti.eslate.scripting.logo.Location2DPrimitives",
                        "gr.cti.eslate.scripting.logo.AltitudePrimitives"}; //2Nov1999
  }

  //BOUND PROPERTIES
  //not using JComponent's VetoableChangeSupport anymore, neither Component's PropertyChangeSupport,
  //since those components don't have a method we could use to check on whether there are any
  //vetoable change listeners (and their support classes are private!)

  public boolean hasPropertyChangeListeners(String property){
    return propertyChangeSupport.hasListeners(property);
  }

  public void addPropertyChangeListener(PropertyChangeListener l){ //12Feb2000
    propertyChangeSupport.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l){ //12Feb2000
    propertyChangeSupport.removePropertyChangeListener(l);
  }

  public void firePropertyChange(String prop,Object oldvalue,Object newvalue) { //12Feb2000
    propertyChangeSupport.firePropertyChange(prop,oldvalue,newvalue);
  }

  //VETOABLE PROPERTIES
  public boolean hasVetoableChangeListeners(String property){
    return vetoableChangeSupport.hasListeners(property);
  }

  public void addVetoableChangeListener(VetoableChangeListener l){
    vetoableChangeSupport.addVetoableChangeListener(l);
  }

  public void removeVetoableChangeListener(VetoableChangeListener l){
    vetoableChangeSupport.removeVetoableChangeListener(l);
  }

  public void fireVetoableChange(String prop,Object oldvalue,Object newvalue) throws PropertyVetoException{
    vetoableChangeSupport.fireVetoableChange(prop,oldvalue,newvalue);
  }

  //ESlatePart
  public ESlateHandle getESlateHandle() {
    if (handle == null){
      handle = ESlate.registerPart(this);
      try {
        handle.setUniqueComponentName(getBaseName());
      } catch (RenamingForbiddenException e) {
      }
      handle.addPrimitiveGroup(getSupportedPrimitiveGroups());
    }
    return handle;
  }

  //Cloning
  public Object clone() throws CloneNotSupportedException {
    //don't call super.clone() it will copy to the cloned object the nameSupport etc. fields
    //of this object!
    BaseObject o;
    try{
      o=(BaseObject)getClass().newInstance();
      o.setName(getName()); //base the clone's name on the original object's name
      return o;
    }catch(Exception e){
      throw new CloneNotSupportedException("Error at BaseObject.clone()");
    }
  }

  public ESlateFieldMap2 getProperties(){
    ESlateFieldMap2 properties=new ESlateFieldMap2(STORAGE_VERSION);
    properties.put("NAME_PROPERTY",getName());
    return properties;
  }

  public void setProperties(StorageStructure properties){
    if(properties==null)
      return;
    //must use try-catch for eacb property, since it might be missing (when loading some older
    //saved state) set default value in case this property is missing (due to some or old or
    //corrupted/wrong state loading)
    try{
      setName((String)properties.get("NAME_PROPERTY"));
    }catch(Exception e){
      System.out.println("Exception in setProperties...");
      setName(getBaseName());//will make a unique name from the BaseName
    }
  } //descendent must call this
}
