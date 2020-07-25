/*
  3Jul1999 - first creation - moved applet specific code here from Canvas class
 27Nov1999 - moved applet specific methods (getAppletInfo/getParameterInfo) here from Canvas class
*/

package gr.cti.eslate.canvas;

import java.awt.Color;
import java.awt.Dimension;
import gr.cti.eslate.birbilis.*;
import gr.cti.utils.ImageFile;

public class CanvasApplet extends RunnerApplet //3Jun1999
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
 Canvas canvas;

 public CanvasApplet(){
  setBaseComponent(canvas=new Canvas());
 }

  /**
   * Applet information.
   * @return	Applet information string.
   */
  public String getAppletInfo()
  {return "Canvas component v"+Canvas.VERSION+", by George Birbilis";}

  /**
   * Applet parameter information.
   * @return	Array of parameter information strings.
   */
  public String[][] getParameterInfo() //27Nov1999: moved here from Canvas class
  {
    String[][] info =
    {
      // Parameter Name	  Kind of Value	  Description
      {"PageI","String","Page(I)'s name (I is a continuous number>=1)"},
      {"ImageI","String","Page(I)'s image [optional]"},
      {"WidthI","Integer","Page(I)'s width [default=max visible or Image Width] (I is a continuous number>=1)"},
      {"HeightI","Integer","Page(I)'s height [default=max visible or Image Height] (I is a continuous number>=1)"},
      {"PenColor","Hex RGB (e.g. FF0000 for red)","Pen (foreground) color [default is black]"},
      {"FillColor","Hex RGB (e.g. 00FF00 for green)","Fill color [default is red]"},
      {"RubberColor","Hex RGB (e.g. 0000FF for blue)","Rubber (background) color [default is white]"},
      {"FilledShapes","Boolean","Shape interior's filling [default=true]"},
      {"Opaque","Boolean","Opaque TurtlePages [default=false]"},
      {"PenSize","Integer","Pen's size [default=1]"},
      {"RubberSize","Integer","Rubber's size [default=5]"},
      {"Tracks","Integer","Turtle drawing Tracks shown [default=0]"}
    };
    return info;
  }

public void start(){ //HTML parsing//
 // System.out.println("Canvas::start"); //this may get called many times
 //don't put this code in init() -- applet hasn't got a graphics assigned to it at that time
 //  //27-1-1999// super.start();
  if (canvas.appletHasDoneStartup) return;

  String pageName,imagePath;
  int width,height,i;

  int loadedPages=0; //14-1-1999: moved here
  while(true){
   try{
    i=loadedPages+1; //don't increment loadPages till we have loaded it OK
    pageName=getParameter("Page"+i);
    if (pageName==null) break; //20-1-1999

    if (pageName.startsWith("_")){ //if starting with _ then it's a Turtle Page
     if (pageName.equals("_")) canvas.newTurtlePage(); //if its name is "_" it's a Turtle Page with a default name
     else canvas.newTurtlePage(pageName.substring(1)); //else is a named Turtle Page (crop the _ from the start of the name)
    }
    else{ //if not starting with "_" it's a drawing page...

     if (pageName.equals("")) pageName=canvas.generateNewDrawingPageName(); //20-1-1999 "" as page name means we want Canvas to generate a default name //21-1-1999: bug-fix: was using =="" and that was working only in AppletViewer with not-valued params

     //System.out.println("New Page: '"+pageName+"'"); //21-1-1999: place inside '' to see if we get pageName="" or pageName=" "
     try{imagePath=getParameter("Image"+i);
         if (imagePath.equals("")) imagePath=null;}
      catch(Exception e){imagePath=null;}
     try{width=Integer.parseInt(getParameter("Width"+i),10);}
      catch(Exception e){width=0;}
      //System.out.println(" Width="+width);
     try{height=Integer.parseInt(getParameter("Height"+i),10);}
      catch(Exception e){height=0;}
     //System.out.println(" Height="+height);
     canvas.wsp.addCanvasPage(pageName,imagePath,width,height); //6-1-1999
    }
    loadedPages++; //14-1-1999: addCanvas doesn't increment loadedPages any more
   }catch(Exception e){break;} //break from the while loop if some error occurs
  }
 //DEMO-->// dtp.addCanvasPage(new DrawingPanel("Hand Drawing"));
 canvas.appletHasDoneStartup=true; //we've finished out startup (loaded any pages)
 canvas.updateMenus(); //20-1-1999: refresh-menus: if non-opaque TurtlePanel is over some DrawingPanel must show tools of that page too
 //synch problems with MC//
 super.start(); //1-12-1998: moved this here (so that listeners don't think we are started before we are ready) //27-1-1999: restored this
}

  public void init(){

  ImageFile.dir=getAppletPath();

  try{
  //HTML parsing//
   //-- put these after creating the dtp & before doing start (where pages hardcoded in HTML start loading)
   try {canvas.wsp.setForegroundColor(new Color(Integer.decode("0x"+getParameter("PenColor")).intValue())); /*RGB*/} catch (NumberFormatException e) {}
   try {canvas.wsp.setFillColor(new Color(Integer.decode("0x"+getParameter("FillColor")).intValue())); /*RGB*/} catch (NumberFormatException e) {}
   try {canvas.wsp.setBackgroundColor(new Color(Integer.decode("0x"+getParameter("RubberColor")).intValue())); /*RGB*/} catch (NumberFormatException e) {}
   //-- put this after creating the menubar (to have FilledShapes&PenSize&Opaque instantiated first)
   try {canvas.filledShapes.setSelected(getParameter("FilledShapes").equalsIgnoreCase("true"));}catch (Exception e){}
   try {canvas.Opaque.setSelected(getParameter("Opaque").equalsIgnoreCase("true"));}catch (Exception e){}
   try {canvas.penSize.setNumber(Integer.parseInt(getParameter("PenSize"),10));}catch(Exception e){}
   try {canvas.rubberSize.setNumber(Integer.parseInt(getParameter("RubberSize"),10));}catch(Exception e){}
   try {canvas.tracks.setNumber(Integer.parseInt(getParameter("Tracks"),10));}catch(Exception e){}
  }catch(Exception e){} //25-3-1999: catching the null pointer exceptions thrown when we're running as an application
 }

/////////////////////

  public static void main(String[] args) {
   gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.canvas.CanvasApplet","Canvas",new String[]{});
   gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(400,400);}

}

