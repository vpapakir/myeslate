package gr.cti.eslate.logo;

import java.awt.Dimension;
import gr.cti.eslate.birbilis.*;

public class LogoApplet extends RunnerApplet //21Jun1999
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private Logo logo;

 public LogoApplet(){
  super();
  setBaseComponent(logo=new Logo());
 }

  /**
   * Applet information.
   * @return    Applet information string.
   */
  public String getAppletInfo()
  {
    return "Logo component v"+Logo.VERSION+", by George Birbilis";
  }

  /**
   * Applet parameter information.
   * @return    Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name   Kind of Value   Description
      {"PrimitiveGroupI","String","A Primitive Group java class name (I is a continuous number>=1)"}
    };
    return info;
  }

 /////////////////////

  public void init()
 {
  super.init();

  logo.curDIR=getAppletPath(); //17-10-1998
  loadPrimitivesFromHTML(); //20-7-1998
 }

 public void loadPrimitivesFromHTML(){ //29-7-1998
  //LOAD PRIMITIVES FROM HTML PARAMETERS//
  int i=0,loadedPrimitives=0;
  String primitiveGroupClassName;
  while(true){
   try {
    i++; //don't increment the loadedPrimitives counter till we have loaded it OK
    primitiveGroupClassName=getParameter("PrimitiveGroup"+i); //will throw a Null Pointer Exception if not running in a browser (however, we are catching it)
    if (primitiveGroupClassName.equals("")) break;
   }catch(Exception e){break;} //break from the while loop when no more primitive groups (or other error)
   try{
    logo.loadPrimitiveGroups(new String[]{primitiveGroupClassName});
    System.out.println("Loaded Primitive Group: "+primitiveGroupClassName);
    loadedPrimitives++;
   }catch(Exception e){System.out.println("Couldn't load primitive group "+primitiveGroupClassName);}
  }
  if (loadedPrimitives>0) System.out.println("Loaded "+loadedPrimitives+" primitives");
  //...needs some delay to make sure no more output comes out from LOGO after selecting all...
  logo.Action(Logo.EDIT_SELECTALL); //20-7-1998: select all messages (user can clear just by pressing ENTER) - Do with Action: give focus to text area
 }

 /////////////////////

  public static void main(String[] args) {
   gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.logo.LogoApplet","Logo",new String[]{});
   gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(400,400);}

}

