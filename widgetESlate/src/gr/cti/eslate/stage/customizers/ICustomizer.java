//Title:        Stage
//Version:      17May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.customizers;

public interface ICustomizer {

 public String getTitle(); //14Dec1999

 public void setParent(ICustomizer parent); //17May2000

 public Object getObject();
 public void setObject(Object o);

 public void setupWidgetsFromObject();

}
