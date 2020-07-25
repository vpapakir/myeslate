package gr.cti.eslate.planar;

import gr.cti.eslate.planar.Planar;
import java.awt.Dimension;
import gr.cti.eslate.birbilis.*;

public class PlanarApplet extends RunnerApplet //4Jun1999
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 Planar planar;

 public PlanarApplet(){
  super();
  setBaseComponent(planar=new Planar());
  parseHTML();
 }

 public void parseHTML(){ //16Oct1999: allow hiding of menubar & toolbar (moved to PlanarApplet from main Planar class and broke "MenusVisible" HTML param into "MenuBarVisible" and "ToolBarVisible" params
  try{planar.setMenuBarVisible(getParameter("MenuBarVisible").equalsIgnoreCase("true"));}catch(Exception e){} //in case there is a problem, let PLANAR do what is default
  try{planar.setToolBarVisible(getParameter("ToolBarVisible").equalsIgnoreCase("true"));}catch(Exception e){} //in case there is a problem, let PLANAR do what is default
 }

  /**
   * Applet information.
   * @return	Applet information string.
   */
  public String getAppletInfo()
  {return "Planar component, by George Birbilis";}

  /**
   * Applet parameter information.
   * @return	Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name	  Kind of Value	  Description
     //{"is2D","Boolean","2D slider mode [default=false]"}, //9-10-1998
    };
    return info;
  }


/////////////////////

  public static void main(String[] args) {
   gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.slider.PlanarApplet","Slider",new String[]{});
   gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return getBaseComponent().getPreferredSize();} //11Jun1999: not using hard-coded size, but delegating to the hosted component

}

