//Title:        E-Slate
//Version:      24Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      Computer Technology Institute
//Description:  Navigator (web browser etc.) component for E-Slate

package gr.cti.eslate.navigator.models; //moved to "models" package

import gr.cti.eslate.utils.browser.*;

public interface INavigator {

 public void setCurrentLocation(String location) throws Exception; //true if went to new location, else false
 public String getCurrentLocation();

 public String home();

 public String forward(); //nil if nothing to go forward to, else return the new location

 public String back(); //nil if nothing to go back to, else return the new location

 public void stop(); //15Mar2000

 public void refresh(); //24Jun2000

 public ESlateBrowser getESlateBrowser();

}

