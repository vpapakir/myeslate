package gr.cti.eslate.palette;

import java.awt.Dimension;
import gr.cti.eslate.birbilis.*;
import java.awt.Color;

public class PaletteApplet extends RunnerApplet //11Jun1999
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
 protected Palette palette;

 public PaletteApplet(){
  super();
  setBaseComponent(palette=new Palette());
 }

 public void init() //29Jun2000: moved here from Palette class
 {
  super.init();

  try{
   //HTML parsing// -- put this before creating the (myJColorChooser)s
   try {palette.setForegroundColor(new Color(Integer.decode("0x"+getParameter("PenColor")).intValue())); /*RGB*/} catch (NumberFormatException e) {}
   try {palette.setFillColor(new Color(Integer.decode("0x"+getParameter("FillColor")).intValue())); /*RGB*/} catch (NumberFormatException e) {}
   try {palette.setBackgroundColor(new Color(Integer.decode("0x"+getParameter("RubberColor")).intValue())); /*RGB*/} catch (NumberFormatException e) {}
  }catch(Exception e){} //14May1999: to allow running as an application, cause getParameter throws null pointer exceptions

 }

 /**
  * Applet information.
  * @return	Applet information string.
  */
 public String getAppletInfo() //29Jun2000: moved here from Palette class
 {return "Palette component v"+Palette.VERSION+", by George Birbilis";}

 /**
  * Applet parameter information.
  * @return	Array of parameter information strings.
  */
 public String[][] getParameterInfo() //29Jun2000: moved here from Palette class
 {
   String[][] info =
   {
    // Parameter Name	  Kind of Value	  Description
     {"PenColor","Hex RGB (e.g. FF0000 for red)","Pen (foreground) color [default is black]"},
     {"FillColor","Hex RGB (e.g. 00FF00 for green)","Fill color [default is red]"},
     {"RubberColor","Hex RGB (e.g. 0000FF for blue)","Rubber (background) color [default is white]"}
   };
   return info;
 }

/////////////////////

  public static void main(String[] args) {
   gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.palette.PaletteApplet","Palette",new String[]{});
   gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return getBaseComponent().getPreferredSize();} //11Jun1999: not using hard-coded size, but delegating to the hosted component

}

