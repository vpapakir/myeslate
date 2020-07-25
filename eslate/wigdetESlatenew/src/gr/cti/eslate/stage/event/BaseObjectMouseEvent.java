//Title:        Stage
//Version:      26Feb2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.event;

import java.util.EventObject;
import gr.cti.eslate.stage.BaseObject;

public class BaseObjectMouseEvent extends EventObject
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private double x,y;

 public BaseObjectMouseEvent(BaseObject source, double x, double y) { //x,y are coordinates relative to the BaseObject's center point (location)
  super(source);
  this.x=x;
  this.y=y;
 }

 //

 public String getSourceName(){
  return ((BaseObject)source).getName();
 }

 public double getX(){
  return x;
 }

 public double getY(){
  return y;
 }

}
