//Title:        Planar
//Version:
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  Avakeeo's Planar component's resources

package gr.cti.eslate.planar;

import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import java.net.URL;

public class Res { //16Oct1999: moved resource handling routines here from main Planar class (using source from gr.cti.canvas.Res class)

//////////// LOCALIZATION-START ///////////////////////////////////////////////

 transient static protected ResourceBundle m; //GREEK// //7-1-1999: made static

 public static String[] localizeArray(String s){
  try{return m.getStringArray(s);}
  catch(Exception e){
   return null;
  }
 }

 public static String localize(String s){ //7-1-1999: made static
  try{return m.getString(s);}
  catch(Exception e){
   //System.out.println("Couldn't localize "+s);
   return s;
  }
 }

 static{ //7-1-1999: made this static: localize(...) is used at variables' initialization that are set before the constructor gets called
  try{
   m = ResourceBundle.getBundle("gr.cti.eslate.planar.MessagesBundle", gr.cti.eslate.base.ESlateMicroworld.getCurrentLocale()); //14Oct1999: using E-Slate's locale setting
  }catch(/*MissingResource*/Exception e){ //catching all Exceptions cause in MS-JVM some "bad path" is thrown when a ResourceBundle is missing
   System.err.println("Couldn't find messages resource");
  } //if localized bundle is not found it shall load MessagesBundle.class
 }

//////////// LOCALIZATION-END ///////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////

//routines...
  public static ImageIcon loadImageIcon(String filename, String description) {
   //AVAKEEO-START//
  try{
   URL u=Res.class.getResource(filename);
//   System.out.println(u);
   if (u!=null) return new ImageIcon(u,description);
   else return null;
  }catch(Exception e){return null;} //18-12-1998: catching Exception thrown by the MS-JVM when filename not found in .jar
   //AVAKEEO-END// //15-8-1998: removed commented code and deleted the "applet" variable from the Canvas class
  }

}
