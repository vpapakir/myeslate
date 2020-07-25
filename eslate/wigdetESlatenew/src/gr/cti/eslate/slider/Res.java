//Title:        Slider
//Version:      27Nov1999
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  Resource Proxy

package gr.cti.eslate.slider;

import java.util.Locale;
import java.util.ResourceBundle;

public class Res {

//////////// LOCALIZATION-START ///////////////////////////////////////////////

 transient static protected ResourceBundle m; //GREEK//

 public static String[] localizeArray(String s){ //27Nov1999: copied here from gr.cti.eslate.canvas.Res
  try{return m.getStringArray(s);}
  catch(Exception e){
   return null;
  }
 }

 public static ResourceBundle getRBundle() {
    return m;
 }

 public static String localize(String s){ //27-3-1999: localization code made static (source copied from Canvas)
  try{return m.getString(s);}
  catch(Exception e){
   //System.out.println("Couldn't localize "+s);
   return s;
  }
 }

 static{ //made this static: localize(...) is used at variables' initialization that are set before the constructor gets called
  try{
   m = ResourceBundle.getBundle("gr.cti.eslate.slider.MessagesBundle", Locale.getDefault()); //19May1999: fixed, was using Canvas's messages bundle
  }catch(/*MissingResource*/Exception e){ //catching all Exceptions cause in MS-JVM some "bad path" is thrown when a ResourceBundle is missing
   System.err.println("Couldn't find messages resource");
  } //if localized bundle is not found it shall load MessagesBundle.class
 }

//////////// LOCALIZATION-END ///////////////////////////////////////////////////////////////////////////////////////////////////////

} 