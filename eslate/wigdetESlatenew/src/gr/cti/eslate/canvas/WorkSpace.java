//Title:        gr.cti.eslate.canvas.WorkSpace
//Version:      30Mar2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  Avakeeo's Canvas component

package gr.cti.eslate.canvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.util.Vector;
import gr.cti.typeArray.BoolBaseArray;

import java.util.NoSuchElementException;
import java.util.Properties;

import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import gr.cti.utils.ImageFile;

import gr.cti.eslate.base.Plug;
import gr.cti.eslate.logo.ProcedureCall;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import java.awt.SystemColor;

/**
 * @version     2.0.7, 14-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
//@SuppressWarnings(value={"unchecked"})
public class WorkSpace extends JPanel //30Mar2000: separated from gr.cti.eslate.Canvas (WorkSpace was an inner class of it, now it's a separate class)
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  public Vector panels=new Vector();
  public Color fgr=Color.black,bkgr=Color.white,fill=Color.red; //22-10-1998: made public to use from "Canvas.saveState"
  public Rectangle selection=null; //17-8-1998 (!!! not sure if can save null fields, maybe should set that to an empty Rectangle [must check all other places where this is used though)
  private Canvas canvas;
  private static Properties printprefs = new Properties(); //static so that all panels share printprefs

  public WorkSpace(Canvas canvas){
   this.canvas=canvas;
   setLayout(null); //The top element will be the one added first
   enableEvents(ComponentEvent.COMPONENT_RESIZED); //25May1999 (put first of all other commands that might resize the panel, as listeners must be notified of changes)
   setBorder(new EtchedBorder(EtchedBorder.RAISED,Color.lightGray,Color.darkGray)); //CanvasPages and their JScrollPanes won't have any border, their container, WorkSpace, will have one (needed in case it runs as an Applet)
   setBackground(SystemColor.controlShadow);
   // Make the component opaque, otherwise weirdness will ensue when pages
   // smaller than the canvas are added.
   setOpaque(true);
  }

  /*protected void validateTree() {
      super.validateTree();
  } */

  public boolean isOptimizedDrawingEnabled() { //27Jul1999: tell Swing that our children are not tiled, but overlap
   return false;
  }

  public void processComponentEvent(ComponentEvent e){ //25May1999: when resizing, resize scrollPanes too to fit the visible area
   //System.out.println("DTP Resized");
   super.processComponentEvent(e); //place first!!!
   if (e.getID()==ComponentEvent.COMPONENT_RESIZED) {
     if (!canvas.isFixedTurtlePageSizes()) {
       updateTurtlesAfterResize();
     }
     resizeScrollPanesToFit();
   }
  }

  private void updateTurtlesAfterResize()
  {
    for(int i=panels.size();i-->0;) {
      CanvasPage cp = (CanvasPage)panels.elementAt(i);
      if (cp instanceof TurtlePanel) {
        TurtlePanel tp = (TurtlePanel)cp;
        // Clear any drawn lines: they are drawn using the saved state of
        // the turtle that drew them, which has two side-effects: drawn lines
        // are not moved relative to the new value of "home", and reexecuting
        // the procedure that created them, using the slider, sets the value of
        // "home" to where it was before the resize, causing confusion.
        //tp.clearAll();

        // Instead of clearing the turtle page, we can translate every single
        // line ever drawn. This is is more computationally heavy, but
        // looks "right".
        Dimension s = tp.getSize();
        int xHalf = s.width / 2;
        int yHalf = s.height / 2;
        Vector<?> groups = tp.groups;
        int nGroups = groups.size();
        for (int j=0; j<nGroups; j++) {
          LineGroup lg = (LineGroup)groups.get(j);
          Vector<?> st = lg.savedTurtles;
          int nTurtles = st != null ? st.size() : 0;
          for (int k=0; k<nTurtles; k++) {
            gr.cti.eslate.turtle.TurtleState ts = (gr.cti.eslate.turtle.TurtleState)((TurtlePanel.SavedTurtle)st.get(i)).state;
            ts.xhalfsize = xHalf;
            ts.yhalfsize = yHalf;
          }
        }

        gr.cti.eslate.base.ESlateHandle[] h = tp.turtlePin.getProtocolHandles();
        for(int j=0; j<h.length; j++) {
          gr.cti.eslate.turtle.Turtle t =
            (gr.cti.eslate.turtle.Turtle)(h[j].getComponent());
          // Reset xhalfsize and yhalfsize, forcing setTurtlePanel to
          // recalculate them, thus resetting the value of "home" to the
          // center of the resized turtle panel.
          t.state.xhalfsize = 0;
          t.state.yhalfsize = 0;
          t.setTurtlePanel(tp);
        }
      }
    }
  }

  public Dimension getClientAreaSize(){ //20Jul1999
   Border border=getBorder(); //20Jul1999: now WorkSpace has a border, so must make the scrollpanes same size as it minus the border insets
   Insets insets;
   if (border!=null) insets=getBorder().getBorderInsets(this); else insets=new Insets(0,0,0,0);
   return new Dimension(getWidth()-insets.left-insets.right,getHeight()-insets.top-insets.bottom);
  }

  public void resizeScrollPanesToFit(){ //25May1999
   for(int i=panels.size();i-->0;) //optimal!
    ((CanvasPage)panels.elementAt(i)).setScrollerSize(getClientAreaSize()); //20Jul1999: fixed to use getClientAreaSize instead of getSize (to subtract the border size) //20Jul1999: calling the setScrollerSize instead of doing a setSize on the scroller
   //repaint(); //!!!
  }

  public Canvas getCanvas(){ //3Jun1999: used by CanvasPage.getCanvas()
   return canvas;
  }

  public void setPaintingDisabled(boolean flag){ //2-12-1998
   for(int i=panels.size();i-->0;) //optimal!
    ((CanvasPage)panels.elementAt(i)).paintingDisabled=flag;
  }

  public void print(){ //Originated from PixMaker.java
   //System.out.println("dtp.print");
   Toolkit toolkit = Toolkit.getDefaultToolkit();
	 PrintJob job = toolkit.getPrintJob(new JFrame(),"Print Dialog?",printprefs);
	 if (job == null) {System.out.println("Canceled printing..."); return;}

   Graphics page = job.getGraphics();
	 Dimension pagesize = job.getPageDimension();

   try{

   for (int i=panels.size();i-->0;){ //25-2-1999: optimized
    CanvasPage p=(CanvasPage)panels.elementAt(i);
    p.printIt(page,pagesize);
    //p.printAll(page); //?
    //if (p.isOpaque()) break; //till first opaque page (should count from top and stop at first opaque: what happens with non-same sized panels)
   }

   }catch(Exception e){System.err.println(e+"\nPrinting failed");}

 	 page.dispose();
   job.end();
 //   job.finalize();//job.end();
 }

//////////////////////////

  public Vector saveAllImages(){ //28Mar2000: extracted from old "saveImages" routine comments
   String filename;
   Vector filenames=new Vector(panels.size());
   for(int i=0,count=panels.size();i<count;i++) { //optimal! (must count upwards, cause the returned Vector must contain the filenames in the order of the pages stack)
    CanvasPage p=(CanvasPage)panels.elementAt(i);
    if (p instanceof DrawingPanel){
     if((filename=ImageFile.saveGIF(p.getName(),p.img))!=null){ //appends ".gif" (if needed) and saves as GIF image file
      p.dirty=false;
      try{filenames.addElement(filename);}catch(Exception ex){System.err.println(ex);}
     }
    }
   }
   return filenames;
  }

  //04Apr2000: fixed-bug: was using "panels.size()" and "panels.elementAt" instead of "v.size()" and "v.elementAt" respectively
  public void loadImageFiles(Vector v){ //28Mar2000: extracted from old "loadImages" routine comments
   for(int i=0,count=v.size();i<count;i++) { //optimal! (must count upwards, cause the pages must be loaded in the stack in the order their filenames are in the input vector)
    String filename=(String)v.elementAt(i);
    addCanvasPage(filename,filename,0,0);
   }
  }

//////////////////////////

  public Vector pagesToVector(){ //22-10-1998 //28Mar2000: renamed to "pagesToVector" from "saveImages"
   //System.out.println("WorkSpace.pagesToVector");

   //6-1-1999: now externalizing both drawing&turtle pages//
   Vector v=new Vector();
   for(int i=0,count=panels.size();i<count;i++) { //optimal! (must count upwards)
    CanvasPage cp=(CanvasPage)panels.elementAt(i);
    v.addElement(cp);
    //System.out.println("saved page "+cp.getName());
    cp.dirty=false;
   }
   return v;
  }


  //04Apr2000: fixed-bug: was using "panels.size()" and "panels.elementAt" instead of "v.size()" and "v.elementAt" respectively
  public void setPagesFromVector(Vector v){ //22-10-1998
   //System.out.println("WorkSpace.setPagesFromVector");
   removeAllPanels(); //??? ask user to save first ???
   for(int i=0,count=v.size();i<count;i++) { //optimal! (must count upwards)
    CanvasPage cp=(CanvasPage)v.elementAt(i);
    addCanvasPage(cp,(cp instanceof DrawingPanel)?((DrawingPanel)cp).getImageIcon():null,0,0); //6-1-1999: now loading both drawing&turtle pages
   }
  }

  public BoolBaseArray opaquenessToArray(){ //22-10-1998 //28Mar2000: renamed to "pagesToVector" from "saveImages"
   BoolBaseArray v=new BoolBaseArray();
   for(int i=0,count=panels.size();i<count;i++) { //optimal! (must count upwards)
    CanvasPage cp=(CanvasPage)panels.elementAt(i);
    v.add(cp.isOpaque());
   }
   return v;
  }

  public void setOpaquenessFromArray(BoolBaseArray v){ //22-10-1998
   for(int i=0,count=v.size();i<count;i++) { //optimal! (must count upwards)
      CanvasPage cp=(CanvasPage)panels.elementAt(i);
      cp.setOpaque(v.get(i));
   }
  }


//////////////////////////

  public CanvasPage getTopPanel(){
   try{
    return (CanvasPage)panels.firstElement();
   }catch(NoSuchElementException e){return null;}
   //maybe should always create a blank new top panel and return it
   //(else methods might be called on this null value)
  }

  public DrawingPanel getVisibleDrawingPanel(){ //14-8-1998
   for(int i=0,count=panels.size();i<count;i++) { //optimal! (must count upwards)
    CanvasPage p=(CanvasPage)panels.elementAt(i);
    if(p instanceof DrawingPanel) return (DrawingPanel)p;
    else if(p.isOpaque()) return null; //14-1-1999
   }
   return null;
  }

  /*
  public CanvasPage getOpaquePanel(){ //24-12-1998
   for(int i=0,count=panels.size();i<count;i++) { //optimal! (must count upwards)
    CanvasPage p=(CanvasPage)panels.elementAt(i);
    if (p.isOpaque()) return p;
   }
   return null;
  }
  */

  public CanvasPage findPageByPin(Plug plug){
   for(int i=panels.size();i-->0;) { //optimal!
    CanvasPage p=(CanvasPage)panels.elementAt(i);
    if (p.imagePin==plug) return p; //WARNING: don't use ==, only equals!
   }
   return null;
  }

  public ICanvasTool getTopPanelTool(){
   CanvasPage p=getTopPanel();
   if (p!=null) return p.getTool();
   else return null;
  }

  public int getPanelCount(){return panels.size();}

//////////////////////////

  public void updateTurtlePanels(){ //17-7-1998
   //1-1-1998: not using try-catch and ClassCastExceptions anymore... it costs too much (now checking for TurtlePanels ourselves)
   for(int i=panels.size();i-->0;) { //optimal! (should repaint from bottom to top anyway, shouldn't we?)
    CanvasPage p=(CanvasPage)panels.elementAt(i);
    if (p instanceof TurtlePanel) ((TurtlePanel)p).repaint(); //validate not needed: turtlePanel contains no components to update
   }
  }

//////////////////////////

  public void removeTopPanel(){ //UPDATES PAGES MENU//
   CanvasPage p=getTopPanel(); //3-9-1998
   if(p!=null){ //19Aug1999
    canvas.removeImagePin(p); //removePin(getTopPanel().imagePin);
    if (p instanceof TurtlePanel) canvas.removeTurtlePin((TurtlePanel)p);
    remove(p.scroller);
    panels.removeElementAt(0);
    p.close(); //3-9-1998: must do this to free resources
    if (getPanelCount()>0) bringToFront(0); //bring next one to front
    else{canvas.updateMenus();/*validate(); repaint();*/} //14-7-1998: redraw "dtp" //29-12-1998: updating menus+redrawing
   }
  }

  public void removeAllPanels(){ //16-12-1998:now clearing the counters
   canvas.newPageCounter=0;
   canvas.newTurtlePageCounter=0;
   while(getPanelCount()!=0) removeTopPanel();
  }

//////////////////////////

  public void disableTools(){
   for(int i=panels.size();i-->0;) //optimal!
    ((CanvasPage)panels.elementAt(i)).disableTool();
  }

  public void enableTools(){
   for(int i=panels.size();i-->0;) //optimal!
    ((CanvasPage)panels.elementAt(i)).enableTool();
  }

////////////////////   //these UPDATE PAGES MENU

  public void addCanvasPage(CanvasPage p){
   //addCanvasPage(p,(ImageIcon)null,0,0); //0,0 shall get replaced by default values
   addCanvasPage(p,(NewRestorableImageIcon)null,0,0); //0,0 shall get replaced by default values
  }

  /*public void addCanvasPage(ImageIcon icon,int width,int height){ //16-12-1998
   addCanvasPage(null,icon,width,height);
  } */
  public void addCanvasPage(NewRestorableImageIcon icon,int width,int height){ //16-12-1998
   addCanvasPage(null,icon,width,height);
  }


  public void addCanvasPage(String name,String path,int width,int height){ //6-1-1999
   if (path!=null) {
    //ImageIcon icon=new ImageIcon(path);
    NewRestorableImageIcon icon=new NewRestorableImageIcon(path);
    icon.setDescription(name);
    addCanvasPage(null,icon,width,height);
   }
   else addCanvasPage(new DrawingPanel(name),null,width,height);
  }

  //public void addCanvasPage(CanvasPage cp,ImageIcon icon,int width,int height){ //20Jul1999: removed code that asked CanvasPage's scroller for its insets, since the scrollpanes don't have a scroller anymore
  public void addCanvasPage(CanvasPage cp,NewRestorableImageIcon icon,int width,int height){ //20Jul1999: removed code that asked CanvasPage's scroller for its insets, since the scrollpanes don't have a scroller anymore

   if (cp instanceof TurtlePanel) //13-11-1999: adding again //14-1-1999: passing only callSO, no turtlePin (makeNewTurtlePin is creating it from now on)
   ((TurtlePanel)cp).callSO=canvas.LOGOcall;

   if (cp==null) cp=new DrawingPanel("[*]"); //6-1-1999: to allow addition of TurtlePanels too //14-1-1999: marked as "[*]" to easily locate if kept this page name by some bug

   cp.setBackground(bkgr); //14-7-1998: setSiz will use this to set the background of the new page

   panels.addElement(cp);

   cp.makeScroller(getClientAreaSize()); //do this before setSiz //17-8-1998 //20Jul1999: making the scroller same as the free area (subtracting the border size from the WorkSpace size)
   add(cp.scroller); //add the scroller where p is embedded
//   System.out.println(i);
   if (cp instanceof DrawingPanel){
    DrawingPanel p=(DrawingPanel)cp;
    if (icon!=null) p.setImage(icon,width,height); //if width,height==0 replaced by respective image size //14-1-1999: passing icon, instead of icon.getImage(), so that page name is set to icon's description
    else {
     if (width==0) width=/*CANVAS*/p.scroller.getSize().width/*-1*/; //11-7-1998
     if (height==0) height=/*CANVAS*/p.scroller.getSize().height/*-1*/; //11-7-1998
     p.setSiz(width,height);
    }
    p.setFilledShapes(canvas.filledShapes.isSelected()); //!!!must be instantiated
    //p.setOpaque(Opaque.isSelected()); //!!!must be instantiated
    p.setPenSize(canvas.penSize.getinteger()); //!!!must be instantiated
    p.setRubberSize(canvas.rubberSize.getinteger()); //!!!must be instantiated
    p.setForegroundColor(fgr);
    p.setBackgroundColor(bkgr);
    p.setFillColor(fill);
   }else{
     if (width==0) width=/*CANVAS*/cp.scroller.getSize().width/*-1*/; //11-7-1998
     if (height==0) height=/*CANVAS*/cp.scroller.getSize().height/*-1*/; //11-7-1998
     cp.PageWidth = width;
     cp.PageHeight = height;
   }
   //
   //
   canvas.makeNewImagePin(cp); //IMAGE output pin
   if (cp instanceof TurtlePanel){
    canvas.makeNewTurtlePin((TurtlePanel)cp);
    ((TurtlePanel)cp).setTracks(canvas.tracks.getinteger()); //18-1-1999 //!!!must be instantiated
   }
   if (getPanelCount()==1) bringToFront(0); //bring to front to make it add its Image pin
   //maybe here we should do a createMenus() always instead of those two lines and remove the createMenus from slt loading and HTML parsing (but those two would take long cause menus would be created and destroyed while loading)
   canvas.updatePagesMenu(); //6-1-1999
  }

/////////////////////

 public void bringLastToFront() { //UPDATES PAGES MENU//
  bringToFront(getPanelCount()-1); //bring last page to front
 }

  public void bringToFront(int num){ //UPDATES PAGES MENU//
   if (num>=getPanelCount()){ //14-7-1998
    System.err.println("bringToFront: Received request to bring to top an unknown page [number="+num+"]. Ignoring!!!");
    return;
   }

   //don't use this check, cause we call this functions to create the menus for new pages and repaint them -->
   //--> if (num==0) return; //just do nothing if trying to bring to front the first page

/**/ CanvasPage p=getTopPanel(); //sure it is !=null, cause checked for i< panel count
/**/ p.imagePin.setVisible(false); //hide the old pin
/**/ if(p instanceof TurtlePanel) ((TurtlePanel)p).turtlePin.setVisible(false); //hide the old pin

   CanvasPage tofront=(CanvasPage)panels.elementAt(num); //read before removing/rearranging the pages

   //put our component as first in panels' vector
   panels.removeElementAt(num);
   panels.insertElementAt(tofront,0);

   //remove(tofront.scroller);
   add(tofront.scroller,0); //this should remove the panel's scroller from its parent and reinsert it to the front! (must add "tofront.scroller", not "tofront" itself!)

   canvas.updateMenus();

/**/ tofront.imagePin.setVisible(true); //show the new pin  //don't need to get the front panel again, know it's the "tofront" one since we've just sent it to the front of the stack
/**/ if(tofront instanceof TurtlePanel) ((TurtlePanel)tofront).turtlePin.setVisible(true); //show the new pin

  }

///////////////////////////////////////////////////

  public Rectangle getSelection(){ //30Mar2000
   return selection;
  }

  public void setSelection(Rectangle r){ //17-8-1998
   selection=r; //30Mar2000: must keep the selection somewhere!!!
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).selection=r; //don't call CanvasPage.setSelection (would call this again)
   /*validate();*/ repaint();
  }

///////////////////////////////////////////////////

  public void stamp(){ //27-8-1998
   DrawingPanel dp=getVisibleDrawingPanel();
   if (dp!=null){
    Graphics g=dp.gx.g;
    for(int i=0,count=panels.size();i<count;i++){ //optimal! (must count upwards)
     CanvasPage p=(CanvasPage)panels.elementAt(i);
     if (p instanceof DrawingPanel) {p.repaint(); break;}
     if (p instanceof TurtlePanel) ((TurtlePanel)p).drawAllLines(g);
    }
   }
  }

///////////////////////////////////////////////////

  public void setForegroundColor(Color c){
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).setForegroundColor(c);
   fgr=c; //panels created in the future will use this for fgr
  }

  public void setBackgroundColor(Color c){
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).setBackgroundColor(c);
   bkgr=c; //panels created in the future will use this for bkgr
  }

  public void setFillColor(Color c){
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).setFillColor(c);
   fill=c; //panels created in the future will use this for fill
  }

  public void setFilledShapes(boolean f){
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).setFilledShapes(f);
  }

  public void setPenSize(int s){ //gets called twice by enter+focus lost on PenSizePanel (can't fix)
 //System.out.println("dtp.setPenSize");
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).setPenSize(s);
  }

  public void setRubberSize(int s){ //gets called twice by enter+focus lost on RubberSizePanel (can't fix)
 //System.out.println("dtp.setRubberSize");
   for(int i=panels.size();i-->0;) //30Mar2000: optimal, not using an Enumeration anymore!
    ((CanvasPage)panels.elementAt(i)).setRubberSize(s);
  }

  public void setTracks(int s){ //18-1-1999 //gets called twice by enter+focus lost on TracksPanel (can't fix)
 //System.out.println("dtp.setTracks");
   for(int i=panels.size();i-->0;){ //30Mar2000: optimal, not using an Enumeration anymore!
    Object o=panels.elementAt(i);
    if (o instanceof TurtlePanel)
     ((TurtlePanel)o).setTracks(s);
   }
  }

  public void newCall(ProcedureCall p){
   for(int i=panels.size();i-->0;){ //30Mar2000: optimal, not using an Enumeration anymore!
    Object o=panels.elementAt(i);
    if (o instanceof TurtlePanel) ((TurtlePanel)o).newTopLevelProc(p);
   }
  }

 }

