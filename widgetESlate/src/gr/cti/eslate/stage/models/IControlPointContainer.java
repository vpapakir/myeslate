//Title:        Stage
//Version:      18May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Some object that hosts ControlPoint objects

package gr.cti.eslate.stage.models;

import gr.cti.eslate.stage.ControlPoint;

public interface IControlPointContainer
{
 public ControlPoint[] getControlPoints();
 public void setControlPoints(ControlPoint[] controlPointLocations) throws Exception;
 public void removeAllControlPoints(); //18May2000: this is different than "setControlPoints(null)" which will check to make sure that the wanted control points are available
}
