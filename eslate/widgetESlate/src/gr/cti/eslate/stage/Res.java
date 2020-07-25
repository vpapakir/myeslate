//Title:        Physics Resources Proxy
//Version:      2Dec1999
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  provides static methods to access the localization & icon resources of the package gr.cti.eslate.physics

package gr.cti.eslate.stage;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.AbstractButton;
import java.net.URL;

import java.awt.Graphics;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage; //Java2 only

public class Res {

//////////// LOCALIZATION-START ///////////////////////////////////////////////

 transient static protected ResourceBundle m;

 public static Image getSnapshotImage(Component c){
  BufferedImage img=new BufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_RGB);
  if(img!=null){
   Graphics g=img.getGraphics(); //same as doing "img.createGraphics()"
   if(g!=null){
    c.paint(g); //draw the component's contents on the buffered image //seems to fail with null pointer exception in paint when using Java1.2, but it works OK with Java1.3rc3
    return img; //the one who called us should use the image and then dispose it's buffer calling flush() on it
   }
   img.flush(); //couldn't paint the component's contents, so dispose the image and return null
  }
  return null;
 }

 public static String[] localizeArray(String s){
  try{return m.getStringArray(s);}
  catch(Exception e){
   return null;
  }
 }

 public static String localize(String s){
  try{return m.getString(s);}
  catch(Exception e){
   //System.out.println("Couldn't localize "+s);
   return s;
  }
 }

 static{
  try{
   m = ResourceBundle.getBundle("gr.cti.eslate.stage.MessagesBundle", Locale.getDefault());
  }catch(/*MissingResource*/Exception e){ //catching all Exceptions cause in MS-JVM some "bad path" is thrown when a ResourceBundle is missing
   System.err.println("Couldn't find messages resource");
  } //if localized bundle is not found it shall load MessagesBundle.class
 }

//////////// LOCALIZATION-END ///////////////////////////////////////////////////////////////////////////////////////////////////////

 public static Action setupAction(Action a,String name){ //27Jul1999: made static in order to have access from PhysicsPanel and other classes //2Dec1999: returning the Action passed to it as a result
  String text=Res.localize(name);
  //System.out.println(name+"->"+text);
  a.putValue(Action.NAME,text);
  //putValue(SHORT_DESCRIPTION,text); //?
  //putValue(LONG_DESCRIPTION,text); //?
  ImageIcon icon=loadImageIcon("images/"+name+".gif",text);
  if (icon!=null) a.putValue(Action.SMALL_ICON,icon); //check for null else will throw exception
  return a;
 }

 public static AbstractButton setupButton(AbstractButton b,String name){ //2Dec1999
  String text=Res.localize(name);
  //System.out.println(name+"->"+text);
  b.setText(text);
  b.setToolTipText(text);
  b.setIcon(loadImageIcon("images/"+name+".gif",text));
  return b;
 }

 ////////////

  public static ImageIcon loadImageIcon(String filename, String description) { //got from Canvas //27Jul1999: made static
   try{
    URL u=Res.class.getResource(filename);
    //System.out.println(u);
    if (u!=null) return new ImageIcon(u,description);
    else return null;
   }catch(Exception e){return null;} //18-12-1998: catching Exception thrown by the MS-JVM when filename not found in .jar
  }

}
