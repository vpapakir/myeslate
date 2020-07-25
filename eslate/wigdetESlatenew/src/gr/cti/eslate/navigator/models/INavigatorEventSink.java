//Title:        E-Slate
//Version:      14Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      Computer Technology Institute
//Description:  Navigator (web browser etc.) component for E-Slate

package gr.cti.eslate.navigator.models; //14Jun2000: moved to "models" from "event" package

public interface INavigatorEventSink {

 public void beforeNavigation(String newLocation, boolean[] cancel);
 public void navigationComplete(String newLocation);
 public void titleChange(String text); //14Jun2000
 public void statusTextChange(String text); //14Jun2000 

} 