//Title:        E-Slate
//Version:      14Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      Computer Technology Institute
//Description:  Navigator (web browser etc.) component for E-Slate

package gr.cti.eslate.navigator.models; //14Jun2000: moved to "models" from "event" package

public interface INavigatorEventSource {

 public void setNavigatorEventSink(INavigatorEventSink eventSink); //pass null to release the previous event sink and stop listening for events

}
