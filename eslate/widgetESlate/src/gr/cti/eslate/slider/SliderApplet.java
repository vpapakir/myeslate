//...move this to SliderApplet:  //if(getParameter("demo").equalsIgnoreCase("true")){ /*!!*/
//have to call demo()


/*
 try{is2D=getParameter("is2D").equalsIgnoreCase("true");}catch(Exception e){System.out.println("Using 1D Slider GUI");} //9-10-1998: must send "true" | "false" and not true | false to Boolean.valueOf
  //9-10-1998: don't use Boolean.getBoolean here (it's for System properties)
have to call set2D(...) proc
*/

package gr.cti.eslate.slider;

import java.awt.Dimension;
import gr.cti.eslate.birbilis.*;

public class SliderApplet extends RunnerApplet //3Jun1999
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
 public SliderApplet(){
  super();
  setBaseComponent(new Slider());
 }

  /**
   * Applet information.
   * @return	Applet information string.
   */
  public String getAppletInfo()
  {return "Slider component, by George Birbilis";}

  /**
   * Applet parameter information.
   * @return	Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name	  Kind of Value	  Description
     {"is2D","Boolean","2D slider mode [default=false]"}, //9-10-1998
     {"demo","Boolean","initial demo mode [default=false]"}, //15-12-1998     
    };
    return info;
  }

/////////////////////

  public static void main(String[] args) {
   gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.slider.SliderApplet","Slider",new String[]{});
   gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return getBaseComponent().getPreferredSize();} //11Jun1999: not using hard-coded size, but delegating to the hosted component

}

