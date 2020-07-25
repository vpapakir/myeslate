package gr.cti.eslate.stage;

import java.awt.Dimension;
import javax.swing.*;
//import gr.cti.eslate.birbilis.*;

public class StageApplet extends JApplet //21Jun1999
{
 /**
   * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public StageApplet(){
  super();
  //setBaseComponent(new Stage());
 }

  /**
   * Applet information.
   * @return    Applet information string.
   */
  public String getAppletInfo()
  {return "Stage component v"+Stage.VERSION+", by George Birbilis";}

  /**
   * Applet parameter information.
   * @return    Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name   Kind of Value   Description
     //{"is2D","Boolean","2D slider mode [default=false]"}, //9-10-1998
    };
    return info;
  }

/////////////////////

  public static void main(String[] args) {
   gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.stage.StageApplet","Scene",new String[]{});
   gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(400,400);}

}

