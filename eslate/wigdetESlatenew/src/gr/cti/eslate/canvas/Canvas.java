//!!! there are some blue mask remaining bugs when dragging Greek bold 30-point text to place with the Text tool !!!

/*
?.?  - 14May1998 - now removing selection before print,restoring it after that
                 - now disabling tools before printing, enabling them after that (to fix a bug)
     - 21May1998 - adding Font Option
     -  2Jun1998 - by now have added Turtle pin, plus fixed some size problems
     -  3Jun1998 - now when closing the top page, the new top page's pin is unHidden as it should
     -  7Jul1998 - finishing up with Canvas/Turtle/Slider/Logo component's interaction
     -  9Jul1998 - setting pin colors for LogoCallSO and ITurtle pins
     - 14Jul1998 - fixed a nasty bug that kept menus from updating when a page came to front (forgot to set menu var after createMenus)
     - 15Aug1998 - replaced println(ex.getMessage()... with println(ex... [cause NullPointer exception didn't give any message]
     - 27Aug1998 - adding STAMP tool
                 - toolbar buttons don't have a focus rectangle painted any more
         ...

6.6  - 24Feb1999 - corrected About dialogs
6.7  - 25Feb1999 - optimized (memory+speed) looping among contained Turtles done in TurtlePanel's code
                 - made a bit faster a for loop used in the Print routine
6.8  -  2Mar1999 - moved Graphix class to utilsBirb.jar & to the gr.cti.utils package
6.9  - 25Mar1999 - using JAppletFrame to be able to run as an application
                 - getAppletPath now doesn't throw null pointer exception when Canvas is run as an application
                 - HTML parameter parsing code now doesn't throw a null pointer exception when Canvas is run as an application
6.10 - 27Mar1999 - now can load extra applets specified at the command line when running as an application
6.11 - 18May1999 - fix: when top turtlepage is invisible, stamp tool is not displayed on the menu (had been made uneffective, but was still on the menus)
7.0  - 18May1999 - porting to E-Slate II
7.01 - 19May1999 - now descending from ESlateApplet (SO's should be inited OK)
7.02 - 22May1999 - now ImageChange SO's source at events is Canvas itself and not its ESlateHandle
     - 23May1999 - now outgoing SOs pass the component as the event source, not its handle
7.1  - 25May1999 - MusicTool menuItem&button has been enabled again (since QT4Java is at public-beta phase)
                 - When resizing Canvas, the scrollerpanes of the pages do resizing too to fit the new client area
                 - using the new scripting mechanism
                 - CanvasPage::getCanvas now returns null without a warning if CanvasPage hasn't been added to a peer yet
                 - TurtlePanel::newLine uses a try/catch, so that any exceptions thrown aren't propagated to the Virtuoso console
7.2  -  2Jun1999 - not an applet anymore
     -  3Jun1999 - CanvasPage doesn't expect Canvas to be its grand-xxx-parent, gets it from the DrawinArea.getCanvas() by locating its WorkSpace parent
                 - bug-fix: was overriding getWidth/getHeight of Canvas component
7.3  -  2Jul1999 - added SQUARE primitive to LOGO (Canvas already exposed a Square method)
                 - decapitalized all Canvas methods exposed for "scripting"
7.4  - 20Jul1999 - adding a border to WorkSpace (useful when it runs as an applet) and making the CanvasPages and their ScrollPanes with no border (should fix the bug where after saving, the loaded picture showed scrollbars
     - 27Jul1999 - the WorkSpace panel now tells Swing that its children are not tiled, but overlap (the scrollpanes where the images are in)
                 - moved init() & start() methods to CanvasApplet class (HTML parameter parsing)
8.0  -  3Aug1999 - moved to package gr.cti.eslate
8.1  - 19Aug1999 - fixed-bug: tools weren't showing up on the toolbar
                 - now using JOptionPane instead of ESlateOptionPane (the last one is not needed now that we've localized Swing properties)
                 - added Font Chooser dialog at Edit menu
8.2  - 31Aug1999 - some menu decorative fixes (caption renames, icons changed/added)
                 - fix: tool buttons now show hints OK
                 - added a space char at the end of hint [tooltip] messages, to show better
     -  6Oct1999 - setHelp call removed, new help system is automatic
8.3  - 14Oct1999 - now using E-Slate's current locale [instead of using Locale.getDefault()]
                 - now can disable repaints caused when scripting a Canvas page, by using CanvasPage.EnableRefresh & CanvasPage.DisableRefresh primitives
8.4  - 15Oct1999 - in CanvasPageWidth & CanvasPageHeight Logo primitives' error messages it was saying "Turtle" instead of "Canvas"
                 - Canvas now has storable "MenuBarVisible" and "ToolBarVisible" properties
8.5  - 16Oct1999 - MenuBarVisible and ToolBarVisible now work OK when recreating the menus (at "pages stack" order changes)
8.6  - 19Oct1999 - fixed CanvasPage class to have a field called "serialVersionUID" and not "serialVersionUIDoy" [a typo had been introduced somehow]
                 - added "serialVersionUID" field to main Canvas class
8.7  -  1Nov1999 - renamed newDrawingPanel and newTurtlePanel to newDrawingPage and newCanvasPage respectively
                 - added newDrawingPage(String name) method and changed the newDrawingPage() method to call this one passing it an autogenerated name
                 - made "newDrawingPage" and "newTurtlePage" methods public
8.8  - 26Nov1999 - internal changes in LineGroup & TurtlePanel classes
     - 27Nov1999 - moved applet specific methods (getAppletInfo/getParameterInfo) to CanvasApplet class
8.9  - 21Dec1999 - added safety checks at LineGroup class
9.0  - 05Jan2000 - changed TurtlePanel to use the changed ITurtle i/f ("setVisibility" method of ITurtle was renamed to "setVisible")
10.0 - 11Feb2000 - removed all usage of "Vector.elements() --> java.lang.Enumeration" usage in getVisibleDrawingPanel() method (critical method for scripting: is called too often and was generating lots of temporary objects)
                 - cleaned up code of wsp.bringToFront() method and made simpler
                 - made all menubar/toolbar icons transparent
10.1 - 12Feb2000 - make wider, so that it shows all of its toolbar buttons
                 - made toolbar buttons less wide
                 - made all button images the same size
                 - made all button images transparent
10.2 - 27Mar2000 - added BeanInfo to the Canvas component
     - 28Mar2000 - added new Icon to the BeanInfo of the Canvas component
                 - Canvas now works OK with the new Turtle version (iTurtle interface was renamed to ITurtle)
                 - renamed iCanvas and iCanvasTool interfaces to ICanvas an ICanvasTool respectively
                 - added "saveAllImages" and "loadImageFiles" public methods
                 - added NewPageDialog to ask a new page's name and size
10.3 - 29Mar2000 - optimized LineGroup's "draw" and "getBoundingRectangle" methods by removing java.lang.Enumeration usage
                 - made TurtlePanel's repainting more polite
                 - optimized TurtlePanel's repainting by using LineGroup.getBoundingRectangle and Line.getBoundingRectangle methods to repaint only the invalidated areas of the TurtlePanel
                 - optimized LineGroup's repainting by using Line.getBoundingRectangle and not repainting all lines of a lineGroup
                 - optimized LineGroup's and Line's "getBoundingRectangle" methods by caching and returning the same boundsRect while the respective objects are not dirty
                 - made "getBoundingRectangle" method of LineGroup thread-safe by using a synchronization block
                 - made NewPageDialog more usable
10.4 - 30Mar2000 - keeping the current selection area in the WorkSpace inner class
                 - removed all remaining usage of Enumeration (using direct Vector access instead)
                 - moved WorkSpace inner class code of Canvas class to a separate class file
                 - fixed-bug (was introduced in ver#10.3) in TurtlePage: when reexecuting a routine/drawing at past time (e.g. using the Slider/Variation tool), wasn't clearing the old lines related to that routine/drawing before repainting
10.5 - 03Apr2000 - telling Canvas' ancestor, BaseComponent, to not write(read) any data during externalization(internalization)
10.6 - 04Apr2000 - added <Exception>.printStackTrace() calls at readExternal/writeExternal methods' try/catch blocks
                 - fixed-bug at "loadImageFiles" and "setPagesFromVector" methods of WorkSpace: now Canvas should load its saved pages OK from persistence data
10.7 - 10May2000 - moved ImageFile class to utilsBirb.jar (moved to package gr.cti.utils)
     - 15May2000 - had to rebuild and repack adding an "import gr.cti.utils.ImageFile" to the Canvas.java source cause JBuilder was insisting on making compiled code that referenced the removed gr.cti.eslate.canvas.ImageFile class (which is now at the gr.cti.utils package)
10.8 - 23May2000 - keeping loading compatibility with saved-data from previous beta versions of BaseComponent & Canvas
                 - changed the component's greek name to "Κάναβος"
10.9 -  8Jun2000 - changed greek name to "Καμβάς"
                 - using a new transparent icon
11.0 -  9Jun2000 - using a new icon for the turtle page

TO DO: add grouping buttons with drop-downs
       make scrollbars show OK
       /....../

1.0.10 - 27Apr2001 - Αλλαγή όλων των διαλόγων ώστε να υποστηρίζουν owner, όπως και διατήρηση global μεταβλητής
για το topFrame, έτσι ώστε να μην κρύβονται οι modal διάλογοι πίσω από το βασικό παράθυρο.
*/

package gr.cti.eslate.canvas;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.sharedObject.*;

import gr.cti.eslate.birbilis.BaseComponent; //2Jun1999
import gr.cti.utils.*;

import gr.cti.eslate.canvas.gui.*; //28Mar2000

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import gr.cti.typeArray.*;

import gr.cti.utils.ImageFile; //15May2000: had to use this, cause JBuilder kept on assuming that ImageFile referenced meant the removed gr.cti.eslate.canvas.ImageFile class!

/* 28Aug2000: StorageVersion = 2
              ComponentVersion = 1.0.3
*/

/**
 * @version     2.0.9, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class Canvas extends BaseComponent //27Jul1999
                    implements java.io.Externalizable,
                               SharedObjectListener,
                               gr.cti.eslate.scripting.LogoScriptable, //21-8-1998
                               gr.cti.eslate.scripting.AsCanvas, //21-8-1998
                               ICanvas //place last: casting to the last interface is faster (in some environments)
{

 private boolean fixedTurtlePageSizes = false;
 public final static String VERSION="2.0.9";
 public static final int STORAGE_VERSION = 4;

 static final long serialVersionUID = 19101999L; //19Oct1999: added serial-version, so that new versions load OK

 CanvasPage canvasPage;

 Frame topFrame;

    AncestorListener ancestorListener = new AncestorListener() {
        public void ancestorAdded(AncestorEvent event) {
            topFrame = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, Canvas.this);
        }
        public void ancestorMoved(AncestorEvent event){
        }
        public void ancestorRemoved(AncestorEvent event){
        }
    };



 protected boolean isOldStylePersisting(){
  return true; //03Apr2000: keep compatibility with old persistence data
 }

 public final static boolean contextMenus=true; //15-8-1998



 public String[] getSupportedPrimitiveGroups(){ //SCRIPTING//
  String[] s={"gr.cti.eslate.scripting.logo.CanvasPrimitives"}; //21-8-1998
  return s;
 }

//SCRIPTING-START//
 public void point(int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null){ p.gx.Point(x,y); p.refresh();}}

 public void line(int x1,int y1,int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null){p.gx.Line(x1,y1,x2,y2); p.refresh();}}
 public void lineTo(int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null){p.gx.LineTo(x,y); p.refresh();}}

 public void rectangle(int x1,int y1, int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.Rectangle(x1,y1,x2,y2); p.refresh();}}

 public void circle(int cx,int cy,int r){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.Circle(cx,cy,r); p.refresh();}}
 public void circle(int cx,int cy,int rx,int ry){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.Circle(cx,cy,rx,ry); p.refresh();}}
 public void oval(int x1,int y1, int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.Oval(x1,y1,x2,y2); p.refresh();}}

 public void drawString(String s,int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.drawString(s,x,y); p.refresh();}}

 public void canonicalPolygon(int cx,int cy, int ex,int ey, int edges){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.CanonicalPolygon(cx,cy,ex,ey,edges); p.refresh();}}

 public void spray(int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.Spray(x,y); p.refresh();}}; //20-10-1998

 public void copy(int x1,int y1,int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.copy(x1,y1,x2,y2); p.refresh();}}
 public void paste(int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.paste(x,y); p.refresh();}}
 public void cut(int x1,int y1,int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.cut(x1,y1,x2,y2); p.refresh();}}
 public void clear(int x1,int y1,int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.ClearRect(x1,y1,x2,y2); p.refresh();} }

 public void stamp(){wsp.stamp();}
 public void setPenSize(int s){wsp.setPenSize(s);}
 public void setRubberSize(int s){wsp.setRubberSize(s);}
 public void setTracks(int s){wsp.setTracks(s);}

 //2-9-1998:start//
 public void setSize(int x,int y){DrawingPanel p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.setSiz(x,y); p.refresh();}}
 public void clearAll(){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.gx.clearAll(); p.refresh();}}
 public void setForegroundColor(Color c){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.setForegroundColor(c); p.refresh();}}
 public void setBackgroundColor(Color c){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.setBackgroundColor(c); p.refresh();}}
 public void setFillColor(Color c){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.setFillColor(c); p.refresh();}}
 public void setPAINT(){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.gx.setPAINT(); p.refresh();}}
 public void setXOR(){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { p.gx.setXOR(); p.refresh();}}

 public Color getPoint(int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) { return p.gx.getPoint(x,y);} return Color.black;}
 public Color getForegroundColor(){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) return p.gx.fgrColor; else return Color.black;}
 public Color getBackgroundColor(){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) return p.gx.bkgrColor; else return Color.black;}
 public Color getFillColor(){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) return p.gx.fillColor; else return Color.black;}
  public int getPageWidth()
  {
    //CanvasPage p=wsp.getVisibleDrawingPanel();
    CanvasPage p=wsp.getTopPanel();
    if (p!=null) {
      return p.PageWidth;
    }else{
      return 0;
    }
  }
  public int getPageHeight()
  {
    //CanvasPage p=wsp.getVisibleDrawingPanel();
    CanvasPage p=wsp.getTopPanel();
    if (p!=null) {
      return p.PageHeight;
    }else{
      return 0;
    }
  }
 //2-9-1998:end//
 public void moveTo(int x,int y){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {p.gx.moveTo(x,y); p.refresh();} } //5-9-1998
 public void square(int x1,int y1, int x2,int y2){CanvasPage p=wsp.getVisibleDrawingPanel(); if (p!=null) {  p.gx.Square(x1,y1,x2,y2); p.refresh();}} //23-12-1998 //2Jul1999: CanvasPrimitives now calls this for SQUARE primitive

 public void setRefreshEnabled(boolean enabled)
 {
   CanvasPage p = wsp.getVisibleDrawingPanel();
   if (p!=null) {
     p.setRefreshEnabled(enabled); //14Oct1999
   }
   revalidate();
 }
//SCRIPTING-END//

//AVAKEEO-START//

  /**
   * Returns Copyright information.
   * @return	The Copyright information.
   */
  public ESlateInfo getInfo()
  {return new ESlateInfo(Res.localize("title")+VERSION, Res.localizeArray("info"));}

 //PrimitiveGroup primitives; //LOGO// //28-10-1998: removed: needed VIRTUOSO to run Canvas
 ProtocolPlug turtlePin; //!!!
 LogoCallSO LOGOcall=new LogoCallSO(getESlateHandle()); //for slider// //18May1999: ESlateII

////////////////////////////////////////////////////

 //MenuBarVisible property//

 public void setMenuBarVisible(boolean visible){ //15Oct1999
  if(menubar!=null) {
      menubar.setVisible(visible);
//      if (!visible && toolbar!= null && !toolbar.isVisible())
//          menus.getParent().remove(menus);
  }
 }

 public boolean isMenuBarVisible(){ //15Oct1999
  return (menubar!=null)?menubar.isVisible():false;
 }

 //ToolBarVisible property//

 public void setToolBarVisible(boolean visible){ //15Oct1999
  if(toolbar!=null) {
      toolbar.setVisible(visible);
//      if (!visible && toolbar!= null && !toolbar.isVisible())
//          menus.getParent().remove(menus);
  }
 }

 public boolean isToolBarVisible(){ //15Oct1999
  return (toolbar!=null)?toolbar.isVisible():false;
 }

 // FixedTurtlePageSizes property

  public boolean isFixedTurtlePageSizes()
  {
    return fixedTurtlePageSizes;
  }

  public void setFixedTurtlePageSizes(boolean fixed)
  {
    fixedTurtlePageSizes = fixed;
  }

////////////////////////////////////////////////////

public Canvas(){
 super();

 setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));
 addAncestorListener(ancestorListener);

 try{ //3Jun1999
  setUniqueComponentName(Res.localize("name"));
 }catch(RenamingForbiddenException e){} //18May1999: ESlateII

 //PaletteColor input pin// (from[to] Palette)
 try {
  //Pin pin=new SingleInputPin(getESlateHandle(),Res.localize("colorpin"),Color.gray,Class.forName("gr.cti.eslate.sharedObject.PaletteColorChange"),this); //18May1999: ESlateII
  Plug plug=new SingleInputPlug(getESlateHandle(),Res.getRBundle(), "colorpin",Color.gray,Class.forName("gr.cti.eslate.sharedObject.PaletteColorChange"),this); //18May1999: ESlateII
  plug.setNameLocaleIndependent("Χρώματα");
  addPlug(plug);
  plug.addConnectionListener(new ConnectionListener(){
      public void handleConnectionEvent(ConnectionEvent e){
          PaletteColorChange so = (PaletteColorChange) ((SharedObjectPlug)e.getPlug()).getSharedObject();
          Colors=so;
          ICanvasTool t=wsp.getTopPanelTool();
          if (t instanceof CanvasColorChooser)
              ((CanvasColorChooser)t).setPaletteColorChangeObject(Colors);
      }
  });
 }catch(Exception ex){System.err.println(ex+"\nException at PaletteColor input pin creation!");}

//LOGOCallSO input pin// (from Logo)
 try {
    //Pin pin=new SingleInputPin(getESlateHandle(),Res.localize("logocallinput"),new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),this); //18May1999: ESlateII
    Plug plug=new SingleInputPlug(getESlateHandle(),Res.getRBundle(), "logocallinput",new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),this); //18May1999: ESlateII
    plug.setNameLocaleIndependent("Ειδοποίηση για εκτέλεση ρουτίνας LOGO");

    addPlug(plug);
    plug.addDisconnectionListener(new DisconnectionListener(){
        public void handleDisconnectionEvent(DisconnectionEvent e){
            wsp.newCall(null); //any more turtle lines drawn will not belong to any proc
        }
    });
 }catch(Exception ex){System.err.println(ex+"\nError creating LogoCallSO input pin");} //14-7-1998: in case the class is missing

 //LOGOCallSO output pin// (to Slider)
 try {
      //Pin pin=new SingleOutputPin(getESlateHandle(),Res.localize("sliderpin"),new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),LOGOcall); //18May1999: ESlateII
      Plug plug=new SingleOutputPlug(getESlateHandle(),Res.getRBundle(), "sliderpin",new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),LOGOcall); //18May1999: ESlateII
      plug.setNameLocaleIndependent("Μεταβολέας");
      addPlug(plug);
  }catch(Exception ex){System.err.println(ex+"\nError creating LogoCallSO output pin");} //14-7-1998: in case the class is missing

  setOpaque(true);
}

///////////

public synchronized void handleSharedObjectEvent(SharedObjectEvent soe){
 ///??? check soe instance and do actions ... PalleteColorChange/LogoCallSO
 SharedObject s=soe.getSharedObject();
 if (s instanceof LogoCallSO) {
    wsp.newCall(((LogoCallSO)s).get_call()); //inform every Turtle Panel on the new call
 }
 else if (s instanceof PaletteColorChange){
  Colors=(PaletteColorChange)(soe.getSharedObject());
  ICanvasTool t=wsp.getTopPanelTool();
  if (t instanceof CanvasColorChooser) ((CanvasColorChooser)t).setPaletteColorChangeObject(Colors);
  switch (soe.type){
   case PaletteColorChange.NEW_FGR:
    wsp.setForegroundColor(Colors.get_fgrColor());
    break;
   case PaletteColorChange.NEW_BKGR:
    wsp.setBackgroundColor(Colors.get_bkgrColor());
    break;
   case PaletteColorChange.NEW_FILL:
    wsp.setFillColor(Colors.get_fillColor());
    break;
  }
 }
}

  /**
   * Save the component's state.
   * @param	out	The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput out) throws IOException //18May1999: from old ESlate's saveState
  {
   ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
   //fieldMap.put("Pages", wsp.pagesToVector());
   Vector<?> pages = wsp.pagesToVector();
   int nPages = pages.size();
   fieldMap.put("Pages", nPages);
   for (int i=0; i<nPages; i++) {
     fieldMap.put("Page" + i, ((CanvasPage)pages.get(i)).getProperties());
   }
   fieldMap.put("Fgr", wsp.fgr);
   fieldMap.put("Bkgr", wsp.bkgr);
   fieldMap.put("Fill", wsp.fill);
   fieldMap.put("PenSize", penSize.getText());
   fieldMap.put("RubberSize", rubberSize.getText());
   fieldMap.put("Tracks", tracks.getText());
   fieldMap.put("FilledShapesSelected", filledShapes.isSelected());
   fieldMap.put("Selection", wsp.selection);
   fieldMap.put("NewPageCounter", newPageCounter);
   fieldMap.put("NewTurtlePageCounter", newTurtlePageCounter);
   fieldMap.put("MenuBarVisible", isMenuBarVisible());
   fieldMap.put("ToolBarVisible", isToolBarVisible());

   fieldMap.put("Opaqueness", wsp.opaquenessToArray());

   fieldMap.put("FixedTurtlePageSizes", fixedTurtlePageSizes);

   //if (getBorder() != null) {
        try{
            BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
            fieldMap.put("BorderDescriptor", bd);
        }catch(Throwable thr){
            thr.printStackTrace();
        }
   // }
    out.writeObject(fieldMap);
    out.flush();
//    System.out.println("Canvas::writeExternal end");

/*
   try{
    out.writeObject(wsp.pagesToVector()); //save Images and keep image filenames...
    out.writeObject(wsp.fgr);
    out.writeObject(wsp.bkgr);
    out.writeObject(wsp.fill);
    out.writeObject(penSize.getText()); //29-12-1998
    out.writeObject(rubberSize.getText()); //29-12-1998
    out.writeObject(tracks.getText()); //18-1-1999
    out.writeObject(new Boolean(filledShapes.isSelected())); //29-12-1998
    out.writeObject(wsp.selection);
    out.writeObject(new Integer(newPageCounter)); //14-1-1999: must save this, cause two pages can't have same name
    out.writeObject(new Integer(newTurtlePageCounter)); //14-1-1999: must save this, cause two pages can't have same name
    out.writeObject(new Boolean(isMenuBarVisible())); //15Oct1999
    out.writeObject(new Boolean(isToolBarVisible())); //15Oct1999
   }
   catch(Exception e){
    System.err.println("Error saving Canvas state");
    e.printStackTrace(); //4Apr2000
   }*/
  }

  /**
   * Load the component's state.
   * @param	in	The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException //18May1999: from old ESlate's loadState
  {
      Object firstObj = in.readObject();
        if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
            // oldreadExternal
            try{
                fixedTurtlePageSizes = true;
                if(firstObj instanceof gr.cti.utils.ObjectHash)
                    firstObj=in.readObject(); //23May2000: compatibility with previous beta versions: ignore any ObjectHash saved by the BaseComponent ancestor

                //28-1-1999:this blocks the component// while(!appletHasDoneStartup); //1-12-1998: wait till applet has started //27-1-1999: !!!
                wsp.setPagesFromVector((Vector<?>)firstObj); //load images from filenames... (before setting colors)
                wsp.setForegroundColor((Color)in.readObject());
                wsp.setBackgroundColor((Color)in.readObject());
                wsp.setFillColor((Color)in.readObject());
                penSize.setText((String)in.readObject()); //29-12-1998
                rubberSize.setText((String)in.readObject()); //29-12-1998
                tracks.setText((String)in.readObject()); //18-1-1999
                filledShapes.setSelected(((Boolean)in.readObject()).booleanValue()); //29-12-1998
                wsp.setSelection((Rectangle)in.readObject());
                newPageCounter=((Integer)in.readObject()).intValue(); //14-1-1999
                newTurtlePageCounter=((Integer)in.readObject()).intValue(); //14-1-1999
                setMenuBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
                setToolBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
                updateMenus(); //15-1-1999: refresh-menus: if non-opaque TurtlePanel is over some DrawingPanel must show tools of that page too

            }catch(Exception e){
              System.err.println("Error loading all of Canvas state");
              e.printStackTrace(); //4Apr2000
            }
//            System.out.println("Canvas old readExternal end ");
        }else{
            //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
            StorageStructure fieldMap = (StorageStructure) firstObj;
            int version = fieldMap.getDataVersionID();
            Vector<?> v;
            if (version < 4) {
              fixedTurtlePageSizes = true;
              v = (Vector<?>)fieldMap.get("Pages");
            }else{
              // Variable turtle page sizes may cause alignment problems with
              // drawing pages, if this is not taken into account, so, if
              // the "fixed turtle page sizes" property has not been stored in
              // the microworld, set it to true, instead of false, which is the
              // default for newly created canvases.
              fixedTurtlePageSizes = fieldMap.get("FixedTurtlePageSizes", true);
              int nPages = fieldMap.get("Pages", 0);
              Vector<CanvasPage> vcp = new Vector<CanvasPage>(nPages);
              for (int i=0; i<nPages; i++) {
                StorageStructure props =
                  (StorageStructure)fieldMap.get("Page" + i);
                StorageStructure baseProps =
                  (StorageStructure)props.get("BaseProperties");
                String className = (String)baseProps.get("Class");
                try {
                  CanvasPage cp =
                    (CanvasPage)Class.forName(className).newInstance();
                  cp.setProperties(props);
                  vcp.add(cp);
                }catch (Exception e) {
                  e.printStackTrace();
                }
              }
              v = vcp;
            }
            wsp.setPagesFromVector(v); //load images from filenames... (before setting colors)
            if (!v.isEmpty())
                wsp.setBackground(Color.white);
            wsp.setForegroundColor((Color)fieldMap.get("Fgr"));
            wsp.setBackgroundColor((Color)fieldMap.get("Bkgr"));
            wsp.setFillColor((Color)fieldMap.get("Fill"));
            penSize.setText((String)fieldMap.get("PenSize")); //29-12-1998
            rubberSize.setText((String)fieldMap.get("RubberSize")); //29-12-1998

            tracks.setText((String)fieldMap.get("Tracks")); //18-1-1999
            filledShapes.setSelected(fieldMap.get("FilledShapesSelected", true)); //29-12-1998
            wsp.setSelection((Rectangle)fieldMap.get("Selection"));
            newPageCounter= fieldMap.get("NewPageCounter", 1);
            newTurtlePageCounter= fieldMap.get("NewTurtlePageCounter", 1);

            setMenuBarVisible(fieldMap.get("MenuBarVisible", true));
            setToolBarVisible(fieldMap.get("ToolBarVisible", true));


            try{
                if (Integer.valueOf(fieldMap.getDataVersion()).intValue() >= 3)
                    wsp.setOpaquenessFromArray((BoolBaseArray)fieldMap.get("Opaqueness"));
            }catch(Exception e){
                System.out.println("older canvas version");
            }

//            updateMenus(); //15-1-1999: refresh-menus: if non-opaque TurtlePanel is over some DrawingPanel must show tools of that page too

            BorderDescriptor bd = (BorderDescriptor)fieldMap.get("BorderDescriptor");
            //if (bd != null){
                try{
                    setBorder(bd.getBorder());
                }catch(Throwable thr){thr.printStackTrace();}
            //}
        }
        //SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
                revalidate();
        //    }
        //});

        // No matter what we do to revalidate the canvas after loading,
        // the second time we load a microworld containing a canvas, the
        // canvas does not display properly. This can be fixed by resizing the
        // canvas by hand, which causes the component to revalidate after
        // something critical (what?) has happened, thus allowing the
        // revalidation to complete. We simulate this by spawning a thread
        // that waits for a bit, then revalidates the component. Yuk!
        new Thread()
        {
          public void run()
          {
            try {
              Thread.sleep(1000);
              Canvas.this.revalidate();
            } catch (InterruptedException e) {
            }
          }
        }.start();


        //        System.out.println("Canvas new readExternal end");
/*
   //System.out.println("Canvas::readExternal");
   try{
    Object o=in.readObject(); if(o instanceof gr.cti.utils.ObjectHash) o=in.readObject(); //23May2000: compatibility with previous beta versions: ignore any ObjectHash saved by the BaseComponent ancestor

    //28-1-1999:this blocks the component// while(!appletHasDoneStartup); //1-12-1998: wait till applet has started //27-1-1999: !!!
    wsp.setPagesFromVector((Vector)o); //load images from filenames... (before setting colors)
    wsp.setForegroundColor((Color)in.readObject());
    wsp.setBackgroundColor((Color)in.readObject());
    wsp.setFillColor((Color)in.readObject());
    penSize.setText((String)in.readObject()); //29-12-1998
    rubberSize.setText((String)in.readObject()); //29-12-1998
    tracks.setText((String)in.readObject()); //18-1-1999
    filledShapes.setSelected(((Boolean)in.readObject()).booleanValue()); //29-12-1998
    wsp.setSelection((Rectangle)in.readObject());
    newPageCounter=((Integer)in.readObject()).intValue(); //14-1-1999
    newTurtlePageCounter=((Integer)in.readObject()).intValue(); //14-1-1999
    setMenuBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
    setToolBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999

    //wsp.repaint(); //14-1-1999
//    GUI.validate(); GUI.repaint(); //the above won't work, this does
//    wsp.getVisibleDrawingPanel().repaint();
    updateMenus(); //15-1-1999: refresh-menus: if non-opaque TurtlePanel is over some DrawingPanel must show tools of that page too
   }
   catch(Exception e){
    System.err.println("Error loading all of Canvas state");
    e.printStackTrace(); //4Apr2000
   }*/

  }


//AVAKEEO-END//

 JPanel GUI;
 JPanel menus;
 public WorkSpace wsp; //11-7-1998 //15-8-1998:made public to access from CanvasPage
 JMenu pages; //11-7-1998
 int newPageCounter=0,newTurtlePageCounter=0;
 PaletteColorChange Colors;
 boolean appletHasDoneStartup=true; //27Jul1999: will use this from CanvasApplet if loaded as an Applet to block
 JCheckBoxMenuItem filledShapes; //11-7-1998: its instance must be created each time menu is recreated (initially null)
 JCheckBoxMenuItem Opaque;
 JNumberField penSize; //14-7-98
 JNumberField rubberSize; //23-12-1998
 JNumberField tracks; //18-1-1999

//level 0///////////////////////////////////////////

 String getAppletPath(){ //2-9-1998:adapted from one sent by K.Kyrimis
  try {return new File(getCodeBase().getFile()).getCanonicalPath();}
  catch (Exception e){return "";} //25-3-1999: catching all Exceptions and not only IOException, since when run as an application a null pointer exception is thrown (JDK1.2)
 }

 public JComponent getContent(){
  if(GUI==null) {
   GUI=new JPanel();
   //GUI.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
   GUI.setLayout(new BorderLayout());
   wsp=new WorkSpace(this); //18-8-1998: need this here, cause createMenus references wsp var
   GUI.add(BorderLayout.NORTH,menus=createMenus());
   GUI.add(BorderLayout.CENTER,wsp);
  }
  return GUI;
 }

////////////////////

  public void updatePagesMenu(){
   if (pages==null) return; //11-7-1998: if called before menu is created, just exit
   try{
    pages.removeAll(); //empty pages menu: shall insert all pages in their current order
   }catch(Exception e){} //11-1-1999: Swing1.1 threw exception when menu is empty and we do a removeAll()
   for(int i=0,count=wsp.panels.size();i<count;i++) //optimal! (must count upwards)
    pages.add(menuItem(((CanvasPage)wsp.panels.elementAt(i)).getName(),' ',PAGES+i));
   /////
   try{Opaque.setSelected(wsp.getTopPanel().isOpaque());}catch(Exception e){} //!!!must be instantiated
  }

 public void updateMenus(){ //29-12-1998
//  System.out.println("updateMenus contextMenus: " + contextMenus);
  if (contextMenus){ //15-8-1998
  //new toolbar:  do this before updating the pages menu!!!
   GUI.remove(menus); //remove the old menubar

   boolean menubarVisible=isMenuBarVisible(); //16Oct1999: keep the current menuBar visibility setting to set it again after recreating the menuBar
   boolean toolbarVisible=isToolBarVisible(); //16Oct1999: keep the current toolBar visibility setting to set it again after recreating the toolBar
   menus=createMenus();
   setMenuBarVisible(menubarVisible);
   setToolBarVisible(toolbarVisible);

   GUI.add(BorderLayout.NORTH,menus); //add new menubar+toolbar for the top panel (14-7-1998: bug fixed - was forgetting to update menus var)
  }
  updatePagesMenu(); //pages menu should have been (re)created in the above command
  //redraw all
  if (contextMenus){
//      System.out.println("updating menus");
      GUI.revalidate(); GUI.repaint();
  } //15-8-1998
 }

//////////////////////////////////////////////////

public void removeImagePin(CanvasPage p){ //needed by RemovePanel
 if (p!=null) try{
  removePlug(p.imagePin);}
 catch(/*NoSuchPin*/Exception e){/*System.err.println("Couldn't remove the Image pin\n"+e);*/} //27-1-1999: no messages shown
}

public void removeTurtlePin(TurtlePanel p){ //needed by RemovePanel
 if (p!=null) try{
  removePlug(p.turtlePin);}
 catch(/*NoSuchPin*/Exception e){/*System.err.println("Couldn't remove the Turtle pin\n"+e);*/} //27-1-1999: catching all exceptions, no messages shown
}

/////////////////////////

public void addImagePin(CanvasPage p){
 if (p!=null) try{
  addPlug(p.imagePin);
 }catch(PlugExistsException e){System.err.println("Couldn't add the Image pin\n"+e);}
}

public void addTurtlePin(TurtlePanel p){
 //System.out.println("addTurtlePin");
 if (p!=null) try{
  addPlug(p.turtlePin);
 }catch(PlugExistsException e){System.err.println("Couldn't add the Turtle pin\n"+e);}
}

////////////////////

public void makeNewImagePin(CanvasPage p){
// System.out.println("makeNewImagePin");
 canvasPage = p;
 if (p==null) return;
 //p.imageChange=new ImageChange(Canvas.this.getESlateHandle(),p.img/*null*/); //no image at start //18May1999: ESlateII
 p.iconSO = new IconSO(Canvas.this.getESlateHandle()); //no image at start //18May1999: ESlateII
 canvasPage.DidSomeChange();

 try{
  p.imagePin=new SingleOutputPlug(Canvas.this.getESlateHandle(), //18May1999: ESlateII
      null, p.getName()+"."+Res.localize("imagepin"),
      //p.getName()+"."+Res.localize("imagepin"), //must add .+..., cause else imagepin has same name
      //13-1-1999: now using page's-name for imagePin-name// Res.localize("imagepin")+(++loadedPages),
      //Color.red,gr.cti.eslate.sharedObject.ImageChange.class,p.imageChange);
      new Color(50,151,220),gr.cti.eslate.sharedObject.IconSO.class,p.iconSO);
      p.imagePin.setNameLocaleIndependent(p.getName()+"."+"Εικόνα");
  /*p.imagePin.addConnectionListener(new ConnectionListener(){
   public void handleConnectionEvent(ConnectionEvent e){
    try{
     //SharedObjectEvent soe=new SharedObjectEvent(wsp.findPageByPin(e.getPin().getProviders()[0]).imageChange,ImageChange.NEW_IMAGE); //22May1999: using new SO constructor //25May1999: ESlate1.5.19: getProviders() now returns an array instead of a Vector
     //SharedObjectEvent soe=new SharedObjectEvent(wsp.findPageByPin(e.getPin().getProviders()[0]).iconSO); //22May1999: using new SO constructor //25May1999: ESlate1.5.19: getProviders() now returns an array instead of a Vector
     canvasPage.DidSomeChange();
     //((SharedObjectListener)(e.getPin().getHandle().getComponent())).handleSharedObjectEvent(soe); //18May1999: ESlateII
    }catch(Exception ex){System.err.println("Couldn't send image initialization event at image pin connection!");}
   } });*/
  addImagePin(p);
/**/  p.imagePin.setVisible(false); //have this pin initially hidden
 }catch(InvalidPlugParametersException e){System.err.println(e.getMessage());}
//  catch(PinExistsException e){System.err.println(e.getMessage());}
 catch(Exception e){System.err.println("Exception at makeNewImagePin");} //play it safe in case opposite end's interface class is missing
}

public void makeNewTurtlePin(TurtlePanel p){
// System.out.println("makeNewTurtlePin");
 if (p==null) return;

 try {
  p.turtlePin=new LeftMultipleConnectionProtocolPlug(getESlateHandle(), //18May1999: ESlateII
   null, p.getName()/*+"."+Res.localize("turtlepin")*/, //must add .+..., cause else imagepin has same name
   //Res.localize("newturtlepage")+newTurtlePageCounter,
   new Color(60,200,170),
   Class.forName("gr.cti.eslate.turtle.ITurtle")); //28Mar2000: fixed to use "ITurtle" instead of "iTurtle"


  p.turtlePin.addConnectionListener(p); //14-1-1999: TurtlePanels are "ConnectionLister"s
  addTurtlePin(p); //no connection listener: canvas is automated by the Turtle to create a new turtle page if needed
/**/  p.turtlePin.setVisible(false); //have this pin initially hidden
 }catch(Exception ex){System.err.println(ex+"\nException at makeNewTurtlePin!");}
}

// WORKSPACE //////////////////////////////////////////////////////////////////////

public CanvasPage getTopPanel(){
 if(wsp!=null)
  return wsp.getTopPanel();
 else {
  System.err.println("wsp=null at getTopPanel");
  return null;
 }
} //delegate call to wsp

public TurtlePanel getTopMostTurtlePanel(){
 return null; //1-12-1998: not implemented
 /*
 synchronized(wsp.panels){
  CanvasPage p; //1-1-1998: not using try-catch and ClassCastExceptions anymore... it costs too much (now checking for TurtlePanels ourselves)
  //don't-use-constly-enumeration// for (Enumeration e=panels.elements();e.hasMoreElements();){
  for(int i=0,count=panels.size();i<count;i++) { //optimal! (must count upwards)
   p=(CanvasPage)panels.elementAt(i); //<--optimal// e.nextElement();
   if (p instanceof TurtlePanel) ((TurtlePanel)p).repaint(); //validate not needed: turtlePanel contains no components to update
  }
 }
*/
}

// MENUS ////////////////////////////////////////////////////////////////

 private transient boolean noTool=true;
 private void selectCurrentAction(){ //19-1-1999: this replaced showSelectedTool()
  noTool=true;
  CanvasPage p=wsp.getTopPanel();
  if(p==null) return;
  ICanvasTool t=p.getTool();
  if(t==null){
   DrawingPanel dp=wsp.getVisibleDrawingPanel();
   if (dp!=null) t=dp.getTool(); //14-1-1999: checking for dp==null
  }
  if(t!=null){ //19-1-1999: now if no tool, deselect any selected action
   noTool=false;
   // System.out.println(t);
   if(t instanceof CanvasPen) action_SelectPenTool.setSelected(true);                  //UGLY CODE//
   else if(t instanceof CanvasRectangle) action_SelectRectangleTool.setSelected(true);
   else if(t instanceof CanvasRubber) action_SelectRubberTool.setSelected(true);
   else if(t instanceof CanvasSelection) action_SelectSelectionTool.setSelected(true);
   else if(t instanceof CanvasSpray) action_SelectSprayTool.setSelected(true);
   else if(t instanceof CanvasSquare) action_SelectSquareTool.setSelected(true);
   else if(t instanceof CanvasText) action_SelectTextTool.setSelected(true);
   else if(t instanceof TurtleLineChooser) action_SelectTurtleLineChooserTool.setSelected(true);
   else if(t instanceof MusicTool) action_SelectMusicTool.setSelected(true);
   else if(t instanceof CanvasLine) action_SelectLineTool.setSelected(true);
   else if(t instanceof CanvasFill) action_SelectFillTool.setSelected(true);
   else if(t instanceof CanvasColorChooser) action_SelectColorChooserTool.setSelected(true);
   else if(t instanceof CanvasCircle) action_SelectCircleTool.setSelected(true);
   else if(t instanceof CanvasCanonicalPolygon) action_SelectCanonicalPolygonTool.setSelected(true);
   else if(t instanceof CanvasOval) action_SelectOvalTool.setSelected(true);
   else noTool=true;
  }
 }

 private JPanel createMenus(){ //15Oct1999: made private
  JPanel p=new JPanel();
  p.setLayout(new BorderLayout());
  selectCurrentAction(); //19-1-1999: instead of showSelectedTool() - this must be before the creation of the menus (before actions are added to menus)
  p.add(BorderLayout.NORTH,menubar=new CanvasMenuBar()); //15Oct1999: keeping menubar reference, to use with setMenuBarVisible()/isMenuBarVisible()
  p.add(BorderLayout.SOUTH,toolbar=new CanvasToolBar()); //15Oct1999: keeping toolbar reference, to use with setToolBarVisible()/isToolbarVisible()
  if(toolsToolBar!=null && noTool) toolsToolBar.setSelection(null); //19-1-1999
  return p;
 }

///////////////////////////////////////////////////////////////////////

 public NewPageDialog showNewPageDialog(int type){
  if (type == NewPageDialog.TURTLE_PAGE && fixedTurtlePageSizes) {
    // Setting the type to TURTLE_PAGE hides the controls that allow the
    // user to enter the page size. Resetting it to DRAWING_PAGE will
    // show these controls.
    type = NewPageDialog.DRAWING_PAGE;
  }
  NewPageDialog npd=new NewPageDialog(topFrame, type);
  npd.setVisible(true);
  npd.dispose();
  return npd;
 }

///////////////////////////////////////////////////////////////////////

 String generateNewDrawingPageName(){ //20-1-1999 //1Nov1999: renamed from generateNewDrawingPanelName
 //IDName generateNewDrawingPageName(){ //20-1-1999 //1Nov1999: renamed from generateNewDrawingPanelName
  //IDName idName = new IDName(ResourceBundle.getBundle("gr.cti.eslate.canvas.MessagesBundle", Locale.getDefault()),
  //                           newpage, ++newPageCounter);
  return Res.localize("newpage")+(++newPageCounter);
 // return idName;
 }

 public DrawingPanel newDrawingPageAskUser(){ //28Mar2000
  NewPageDialog npd=showNewPageDialog(NewPageDialog.DRAWING_PAGE);
  if (npd.isCanceled()) return null;
  return newDrawingPage(npd.getPageName(),npd.getPageWidth(),npd.getPageHeight());
 }

 public DrawingPanel newDrawingPage(){ //1Nov1999: made public (and renamed from newDrawingPanel)
  return newDrawingPage(generateNewDrawingPageName()); //1Nov1999: now calling the "newDrawingPage(String name)" implementation
 }

 public DrawingPanel newDrawingPage(String name){ //1Nov1999
  return newDrawingPage(name,0,0);
 }

 public DrawingPanel newDrawingPage(String name,int width,int height){ //28Mar2000
  DrawingPanel p=new DrawingPanel((name!=null)?name:generateNewDrawingPageName()); //28Mar2000: not using an empty name
  wsp.addCanvasPage(p,null,width,height);
  wsp.setBackground(Color.white);
  return p;
 }

 //////////////////////

 String generateNewTurtlePageName(){ //20-1-1999 //1Nov1999: renamed from generateNewTurtlePanelName
  return Res.localize("newturtlepage")+(++newTurtlePageCounter);
 }

 public TurtlePanel newTurtlePageAskUser(){ //28Mar2000
  NewPageDialog npd=showNewPageDialog(NewPageDialog.TURTLE_PAGE);
  if (npd.isCanceled()) return null;
  return newTurtlePage(npd.getPageName(),npd.getPageWidth(),npd.getPageHeight());
 }

 public TurtlePanel newTurtlePage(){ //20-1-1999 //1Nov1999: renamed from newTurtlePanel
  return newTurtlePage(generateNewTurtlePageName());
 }

 public TurtlePanel newTurtlePage(String name){ //20-1-1999 //1Nov1999: renamed from newTurtlePanel
  return newTurtlePage(name,0,0);
 }

 public TurtlePanel newTurtlePage(String name,int width,int height){ //28Mar2000
  TurtlePanel p=new TurtlePanel((name!=null)?name:generateNewTurtlePageName()); //28Mar2000: not using an empty name
  wsp.addCanvasPage(p,null,width,height);
  wsp.setBackground(Color.white);
  return p;
 }

///////////////////////////////////////////////////////////////////////

 public void plugInDrawingTool(ICanvasTool t){
  CanvasPage cp=getTopPanel();
  if(cp!=null) cp.unplugTool(); //13-1-1999: changed from disableTool() [need to check for tool==null elsewhere]
  DrawingPanel p=wsp.getVisibleDrawingPanel();
  if (p==null) p=newDrawingPage(); //Create a new page!!! (???)
  p.plugInTool(t); //??? just made a new page if p was null ???
 }

//ACTIONS/////////////////////////////////////////////////////////////////

 abstract class CanvasToolAction extends ToggleAction{
  public CanvasToolAction(String name){
   String text=Res.localize(name);
   putValue(NAME,text);
   //putValue(SHORT_DESCRIPTION,text); //?
   //putValue(LONG_DESCRIPTION,text); //?
   ImageIcon icon=Res.loadImageIcon("images/"+name+".gif",text);
   if (icon!=null) putValue(SMALL_ICON,icon); //check for null else will throw exception
  }
  public void act(boolean selected){ //only for DrawingTools : others must override
   if (!selected) plugInDrawingTool(new CustomCanvasTool()); //add a do-nothing tool: else page passes clicks to pages under it
   else act();
  }
  public void act(){};
 }

 CanvasToolAction
/*
  action_SelectPasteTool=new CanvasToolAction("Paste"){ //19-1-1999
   public void act(){
    DrawingPanel p;
    try{p=(DrawingPanel)wsp.getVisibleDrawingPanel();}catch(Exception e){break;}
    CanvasSelection sel;
    if ((p.tool instanceof CanvasSelection)&&
       (sel=(CanvasSelection)p.tool).selected){
     sel.paste();
     action_SelectSelectionTool


   plugInDrawingTool(new CanvasPaste());
   }
  },*/
  action_SelectRubberTool=new CanvasToolAction("Rubber"){
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   public void act(){
   plugInDrawingTool(new CanvasRubber());
   }
  }
  ,
  action_SelectTextTool=new CanvasToolAction("Text"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    String text=(String)JOptionPane.showInputDialog( //26-1-1999: fully Res.localized dialog
     Canvas.this,
     Res.localize("Give me the text")+" : ",
     Res.localize("Text"),
     JOptionPane.QUESTION_MESSAGE,
     null,
     null,
     "");
    if (text!=null) plugInDrawingTool(new CanvasText(text)); //16-7-1998: now have to select Text tool again to change string (Swing bug: message still is in English) //18-8-1998: now dialog has Canvas as parent //23-12-1998: now Res.localized msg
    else setSelected(false); //4-2-1999: if CANCEL pressed, depress the button: hope this doesn't do spurious or other wrong GUI sync events
   }
  }
  ,
  action_SelectFillTool=new CanvasToolAction("Fill"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasFill());
   }
  }
  ,
  action_SelectPenTool=new CanvasToolAction("Pen"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasPen());
   }
  }
  ,
  action_SelectLineTool=new CanvasToolAction("Line"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasLine());
   }
  }
  ,
  action_SelectSquareTool=new CanvasToolAction("Square"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasSquare());
   }
  }
  ,
  action_SelectRectangleTool=new CanvasToolAction("Rectangle"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasRectangle());
   }
  }
  ,
  action_SelectCircleTool=new CanvasToolAction("Circle"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasCircle());
   }
  }
  ,
  action_SelectOvalTool=new CanvasToolAction("Oval"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasOval());
   }
  }
  ,
  action_SelectSprayTool=new CanvasToolAction("Spray"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasSpray());
   }
  }
  ,
  action_SelectSelectionTool=new CanvasToolAction("Selection"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasSelection());
   }
  }
  ,
  action_SelectColorChooserTool=new CanvasToolAction("ColorChooser"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    plugInDrawingTool(new CanvasColorChooser(Canvas.this,Colors));
   }
  }
  ,
  action_SelectCanonicalPolygonTool=new CanvasToolAction("CanonicalPolygon"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(){
    String angles=(String)JOptionPane.showInputDialog( //26-1-1999: fully Res.localized dialog
     Canvas.this,
     Res.localize("Give me the number of angles")+" : ",
     Res.localize("CanonicalPolygon"),
     JOptionPane.QUESTION_MESSAGE,
     null,
     null,
     "3");
    if (angles!=null) plugInDrawingTool(new CanvasCanonicalPolygon(Integer.parseInt(angles)));
    else setSelected(false); //4-2-1999: if CANCEL pressed, depress the button: hope this doesn't do spurious or other wrong GUI sync events
   }
  }
  ,
  action_SelectTurtleLineChooserTool=new CanvasToolAction("TurtleLineChooser"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  public void act(boolean selected){
   if (!selected) wsp.getTopPanel().plugInTool(new CustomCanvasTool()); //add a do-nothing tool: else page passes clicks to pages under it
   else
    wsp.getTopPanel().plugInTool(new TurtleLineChooser()); //if it gets plugged to other than TurtlePanel, then simply does nothing
   }
  }
  ,
  action_SelectMusicTool=new CanvasToolAction("Music"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
   if (!selected) wsp.getTopPanel().plugInTool(new CustomCanvasTool()); //add a do-nothing tool: else page passes clicks to pages under it
   else
    wsp.getTopPanel().plugInTool(new MusicTool()); //5-9-1998 //22-9-1998:changed to work with all pages (TurtlePages too)
   }
  }
  ;

// case TOOL_HAND: CanvasPage panel=wsp.getTopPanel(); /*panel.plugInTool(new CustomCanvasTool());*/panel.disableTool(); panel.scroller.setAutoscrolls(true);/*?*/ panel.setAutoscrolls(true); break; //do nothing tool, set autoscrolls for hand dragging

//////////////////////////////////////////////////////////////////////////

 public void Action(int actionID){
//  while(!appletHasDoneStartup) //--removed...seems to lock up MasterControl for a long period...
//   System.out.println("Canvas.Action:waiting"); //1-12-1998: now waiting for applet to startup, instead of returning (caused problem with Turtle)
  if(!appletHasDoneStartup)return;
  //NEW caused some bugs when other pages hadn't finished loading
  //same might happen with OPEN file... so better ignore actions
  //if applet hasn't finished its startup (that is loading any initial pages)
   DrawingPanel p;
   CanvasPage cp;
   CanvasSelection s;
    //System.out.println("action");
  try{
   switch(actionID){

    //*** FILE ***//
    case FILE_NEW_TURTLEPAGE: //creates a new turtle page// //20-1-1999
     if (newTurtlePageAskUser()!=null) { //28Mar2000
      wsp.bringLastToFront();
      action_SelectTurtleLineChooserTool.setSelected(true); //21-1-1999: this is the correct way to plug-in the tool (to update the button-bar and show it pressed)
     }
     break;
    case FILE_NEW: //creates a new page//
//     wsp.removeTabAt(0);
//     wsp.insertTab(paneNames[0],null,new DrawingPanel()/*panes[0]*/,paneNames[0],1/*0*/);
     if(newDrawingPageAskUser()!=null) //28Mar2000
      wsp.bringLastToFront();
     break;
    case FILE_OPEN: //if selection loads into selection else opens a new page//
//swing// JFileChooser chooser=new JFileChooser();
//swing// if(chooser.showDialog(Canvas.this)==0) /*loadImage(chooser.getSelectedFile().getName())*/;
     wsp.disableTools(); //to fix a bug that gets extra mousePressed&mouseDragged events
     Image i=ImageFile.loadImage(Res.localize("Open"));
     wsp.enableTools(); //to fix a bug that gets extra mousePressed&mouseDragged events
     if (i==null) break;
     try{p=(DrawingPanel)wsp.getTopPanel();}catch(Exception e){p=null;} //7-7-1998: exception now set's p to null instead of doing break
     if (p==null || !(p.getTool() instanceof CanvasSelection &&
      ((CanvasSelection)p.getTool()).selected)){
/* //20-1-1999
       //String name=JOptionPane.showInputDialog(Canvas.this,Res.localize("Page name? "));
       //if (name==null) break; //13-1-1999: if CANCEL pressed, abort opening new image/page
       p=new DrawingPanel(//name
        generateNewDrawingPageName() //14-1-1999: don't allowing user to give page name, cause we'd have to check it's unique (maybe in the future)
       ); //29-12-1998: not using filename for page name
      wsp.addCanvasPage(p); //add the panel before setting the image
*/
      p=newDrawingPage(); //20-1-1999: replaced the above with this one //21-1-1999: bug-fix: was forgetting to assign the new drawing panel to the p variable
      p.setImage(i); //only then set the image
      //do before bringLastToFront... this fixes a bug that at load gets at the
      //left an extra part of the page under it... the 1st one loaded gets all!!! (not just a part)
      wsp.bringLastToFront(); //it's added as the last panel, bring it to front
     } else p.setImage(i); //load the image into the selection (handled by DrawingPanel::setImage)
     break;
    case FILE_CLOSE: //closes top page// //14-1-1999: now closes TurtlePanels too
     cp=wsp.getTopPanel();
     if (cp instanceof DrawingPanel){
      p=(DrawingPanel)cp;
      if (p.dirty){ //check if hasn't been saved since last change
       wsp.disableTools(); //bug-fix//
       int result=JOptionPane.showConfirmDialog(
          Canvas.this,
          Res.localize("lastchangesnotsaved")+"...\n"+
                Res.localize("savefile")+" "+p.getName()+Res.localize("questionmark"),
          Res.localize("warning"),
          JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.WARNING_MESSAGE
          );
       wsp.enableTools(); //bug-fix//
       if (result==JOptionPane.CANCEL_OPTION) break;
       if (result==JOptionPane.YES_OPTION) ImageFile.saveImage(p.img,Res.localize("Save")); //p.gx.img should be equivalent
      }
     }
     wsp.removeTopPanel();
     //System.out.println("closing");
     if (wsp.getPanelCount() == 0)
        //wsp.setBackground(SystemColor.controlShadow);
        wsp.setBackground(UIManager.getColor("controlShadow"));

     break;
    case FILE_SAVE: //if selection saves it else saves whole image//
     wsp.disableTools(); //to fix a bug that gets extra mousePressed&mouseDragged events
     try{p=(DrawingPanel)wsp.getTopPanel();}catch(Exception e){break;}
     if ((p.getTool() instanceof CanvasSelection) && (s=(CanvasSelection)p.getTool()).selected){
      s.copy(); //copy the selection to Clipboard
      s.removeOldSelection();//?
      ImageFile.saveImage(Graphix.Clipboard,Res.localize("Save"));
      s.restoreSelection();//?
      }
     else ImageFile.saveImage(p.img,Res.localize("Save")); //p.gx.img should be equivalent
     p.dirty=false; //!for now: assume the image was saved OK
     //29-12-1998:removed(use in saveAs? [only filename-not path])// p.name=ImageFile.filename; wsp.updatePagesMenu(); //11-9-1998: saving renames the image and updates the PagesMenu to show the new name
     wsp.enableTools(); //to fix a bug that gets extra mousePressed&mouseDragged events
     break;
    case FILE_PRINT: //24-12-1998: fixing to print TurtlePanels too (when TurtlePanel not-opaque, prints all underlying till first non-opaque page (move this to wsp.print)
     //System.out.println("FILE/PRINT");
     wsp.disableTools(); //to fix a bug that gets extra mousePressed&mouseDragged events
     cp=wsp.getTopPanel();
     if ((cp.getTool() instanceof CanvasSelection) && (s=(CanvasSelection)cp.getTool()).selected) {
      s.removeOldSelection();
      wsp.print();
      s.restoreSelection();
      } else wsp.print();
     wsp.enableTools(); //to fix a bug that gets extra mousePressed&mouseDragged events
     break;

    //*** EDIT ***// //24-12-1998: fixed to work with DrawingPanels when under a TurtlePanel
    case EDIT_CUT:
    case EDIT_COPY:
    case EDIT_CLEAR:
/**/    case EDIT_PASTE:
     try{p=(DrawingPanel)wsp.getVisibleDrawingPanel();}catch(Exception e){break;}
     CanvasSelection sel;
     if ((p.getTool() instanceof CanvasSelection)&&
         (sel=(CanvasSelection)p.getTool()).selected)
       switch(actionID){
        case EDIT_CUT: sel.cut();break;
        case EDIT_COPY: sel.copy();break;
        case EDIT_CLEAR: sel.clear();break;
/**/        case EDIT_PASTE: sel.paste();break; //if selection paste stretched in it
       }
/**/      else if (actionID==EDIT_PASTE){ //else use paste tool
            toolsToolBar.setSelection(null); //remove any selected tool
            p.plugInTool(new CanvasPaste()); //this must be done after removing the selected tool, else CanvasPaste tool will be removed too
           }
/**/
     break;
    case EDIT_SELECTALL: //make a selection of the whole image//
     action_SelectSelectionTool.setSelected(true); //26-1-1999: can't call Action(TOOL_SELECTION) any more: code for TOOL_SELECTION has been removed
     try{p=(DrawingPanel)wsp.getVisibleDrawingPanel();}catch(Exception e){break;}
     ((CanvasSelection)p.getTool()).selectArea(0,0,p.gx.MaxX,p.gx.MaxY);
     break;
    case EDIT_STAMP: wsp.stamp(); break; //27-8-1998
    case EDIT_FONT: //19Aug1999
     cp=wsp.getTopPanel();
     Font font=cp.getFont();
     if(JOptionPane.showOptionDialog(
      Canvas.this,
      new FontToolBar(getTopPanel()),
      Res.localize("font"),
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,
      null,
      null
     )==JOptionPane.CANCEL_OPTION) cp.setFont(font);

    //*** SETTINGS ***//
    case SETTINGS_FILLEDSHAPES: wsp.setFilledShapes(filledShapes.isSelected());break;
    case SETTINGS_OPAQUE:
     cp=wsp.getTopPanel();
     cp.setOpaque(Opaque.isSelected());
     if (cp.isOpaque() && (cp instanceof TurtlePanel) && (!(cp.getTool() instanceof TurtleLineChooser))) //19-1-1999: fixed-bug: was checking for existence of TurtleLineChooser, should check for non-existence
      cp.plugInTool(new CustomCanvasTool()); //unselect any selected drawing tool if on top is an opaque turtle page //the updateMenus that follows will show the tool untoggled
     updateMenus(); //15-1-1999// cp.repaint();
     break; //22-9-1998
    case SETTINGS_PENSIZE: //14-7-1998
     wsp.setPenSize(penSize.getinteger());

     if (penSize.isShowing()){
        try{
          MenuSelectionManager.defaultManager().clearSelectedPath(); //hide the opened menu
        }catch(Exception e){e.printStackTrace();} //29-12-1998: rubberSize or tracks might have closed the menu (bug: "calls" rubberSize too)
     }
     break;
    case SETTINGS_RUBBERSIZE: //23-12-1998
     wsp.setRubberSize(rubberSize.getinteger());
     if (rubberSize.isShowing()){
        try{
          MenuSelectionManager.defaultManager().clearSelectedPath(); //hide the opened menu
        }catch(Exception e){} //29-12-1998: penSize or tracks might have closed the menu (bug: "calls" penSize too)
     }
     break;
    case SETTINGS_TRACKS: //18-1-1999
     wsp.setTracks(tracks.getinteger());
     if (tracks.isShowing()){
        try{
          MenuSelectionManager.defaultManager().clearSelectedPath(); //hide the opened menu
        }catch(Exception e){} //penSize or rubberSize might have closed the menu (bug: "calls" penSize too)
     }
     break;
    case SETTINGS_FGRCOLOR: wsp.setForegroundColor(JColorChooser.showDialog(Canvas.this, Res.localize("Foreground Color"), wsp.fgr));break;  //14-7-1998
    case SETTINGS_BKGRCOLOR: wsp.setBackgroundColor(JColorChooser.showDialog(Canvas.this, Res.localize("Background Color"), wsp.bkgr));break; //14-7-1998
    case SETTINGS_FILLCOLOR: wsp.setFillColor(JColorChooser.showDialog(Canvas.this, Res.localize("Fill Color"), wsp.fill));break; //14-7-1998
/*
    case SETTINGS_FONT:
//     Graphics g=wsp.getVisibleDrawingPanel().gx.g;
//     g.setFont(FontChooser.ask(null,Res.localize("Font"),g.getFont(),null));
                                   new fontChooser(m,250),
                                   Res.localize("Font"),
                                   JOptionPane.OK_CANCEL_OPTION
                                   );
     break;
*/
    default:
     //*** PAGES ***//
     if (actionID>=PAGES && actionID<PAGES+wsp.getPanelCount())
      wsp.bringToFront(actionID-PAGES); //bring selected page to front
     //
     break;
   }
   }catch(Throwable e){e.printStackTrace(); System.err.println(e+"\nError/Exception: No pages loaded yet? Try New/Load");} //5-9-1998: now catching both Exceptions and Errors (in case a Tools class can't load [MusicTool:QT may be missing])
   //System.out.println("exiting");
  }

////////////////////////////////////////////////////////////////////////

 class CanvasMenuListener implements ActionListener{ //this listener calls Canvas.Action
  int menuID;
  public CanvasMenuListener(int menuID){super();this.menuID=menuID;}
  public void actionPerformed(ActionEvent e) {
   Action(menuID); //6-11-1998: set again not to use ToggleButtons
  }
 }


 JMenuItem menuItem(String item,char mnemonic,int menuID){
  JMenuItem mi;
  String text=Res.localize(item);
  ImageIcon i=Res.loadImageIcon("images/"+item+".gif",text);
  if (i!=null) {
   mi=new JMenuItem(text,i);
   mi.setHorizontalTextPosition(JButton.RIGHT);
  } else mi=new JMenuItem(text);
//  System.out.println(text);
  if (mnemonic!=' ') mi.setMnemonic(mnemonic);
  mi.addActionListener(new CanvasMenuListener(menuID)); //
  return mi;
 }

 public void destroy(){
  wsp.removeAllPanels(); //3-9-1998: must do this to free resources
  if (Graphix.Clipboard!=null) Graphix.Clipboard.flush(); //11-9-1998: free memory taken up by the clipboard's image!
  //super.destroy(); //do this after wsp.removeAllPanels and not before...
  //...(cause removes all the pins and our routine tre CanvasPage's imagepin again)
 }

 private class CanvasMenuBar extends JMenuBar{
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;

//  public CanvasMenuBar(){this(null);}

  public CanvasMenuBar(){
   super();
   CanvasPage page=getTopPanel(); //29-12-1998

   setAlignmentX(LEFT_ALIGNMENT);
   setAlignmentY(TOP_ALIGNMENT);

   JMenu file = add(new JMenu(Res.localize("File")));
   file.setMnemonic('F');
   file.add(menuItem("New",'N',FILE_NEW));
   file.add(menuItem("New Turtles' page",'T',FILE_NEW_TURTLEPAGE)); //14-1-1999
   file.add(menuItem("Open",'O',FILE_OPEN));
   if (!contextMenus || page!=null){ //29-12-1998
    file.add(menuItem("Close",'C',FILE_CLOSE));
    if (!contextMenus || page instanceof DrawingPanel) //14-1-1999
     file.add(menuItem("Save",'S',FILE_SAVE));
    file.add(menuItem("Print",'P',FILE_PRINT));
   }

 if (!contextMenus || page!=null){
  JMenu edit = add(new JMenu(Res.localize("Edit")));
  edit.setMnemonic('E');
   if (!contextMenus || page instanceof DrawingPanel || (!page.isOpaque() && wsp.getVisibleDrawingPanel()!=null)) { //18-8-1998
    edit.add(menuItem("Cut",'t',EDIT_CUT));
    edit.add(menuItem("Copy",'C',EDIT_COPY));
    edit.add(menuItem("Paste",'P',EDIT_PASTE));
    edit.add(menuItem("Clear",'a',EDIT_CLEAR));
    edit.add(new JSeparator());
    edit.add(menuItem("Select All",'l',EDIT_SELECTALL));
    edit.add(new JSeparator());
    edit.add(menuItem("Font",'F',EDIT_FONT)); //19Aug1999: maybe should place at settings menu, or keep different settings for each page, or keep same font (now have different) for each page
   }
  if (!contextMenus || page instanceof TurtlePanel){ //27-8-1998
   edit.add(menuItem("Stamp",'A',EDIT_STAMP));
  }
 }

 if (!contextMenus || page!=null){ //19-2-1999: using JCheckMenu instead of JRadioMenu, cause looks nicer to the eye
  JCheckMenu/*JRadioMenu*/ tool = (JCheckMenu)/*(JRadioMenu)*/add(new JCheckMenu/*JRadioMenu*/(Res.localize("Tool")));
  tool.setMnemonic('T');
//  tool.add(menuItem("Hand",'H',TOOL_HAND));
   //??move selection here?(turtles)

  if (!contextMenus || page instanceof DrawingPanel || (!page.isOpaque() && wsp.getVisibleDrawingPanel()!=null)){ //18-8-1998
   tool.add(action_SelectSelectionTool); //16-7-1998: put close to Cut/Copy/Paste
   tool.add(action_SelectRubberTool);
   tool.add(action_SelectTextTool);
   tool.add(action_SelectFillTool);
   tool.add(new JSeparator());
   tool.add(action_SelectPenTool);
   tool.add(action_SelectLineTool);
   tool.add(action_SelectSquareTool); //23-12-1998
   tool.add(action_SelectRectangleTool);
   tool.add(action_SelectCircleTool);
   tool.add(action_SelectOvalTool);
   tool.add(action_SelectCanonicalPolygonTool);
   tool.add(action_SelectSprayTool);
   tool.add(new JSeparator());
 //  tool.add(action_SelectZoomTool);
   tool.add(action_SelectColorChooserTool);
   tool.add(action_SelectMusicTool); //5-9-1998
  }
  if (!contextMenus || page instanceof TurtlePanel){ //15-8-1998
   tool.add(action_SelectTurtleLineChooserTool);
  }
 }

 if (!contextMenus || page!=null){
  add(pages=new JMenu(Res.localize("Page"))); //11-7-1998
  pages.setMnemonic('P');
 }

  JMenu settings = add(new JMenu(Res.localize("Settings")));

  settings.setMnemonic('S');

  filledShapes=new JCheckBoxMenuItem(Res.localize("Filled Shapes"),(filledShapes!=null)?filledShapes.isSelected():true); //if any previous menubar keep setting, else set to true
  (settings.add(filledShapes)).addActionListener(new CanvasMenuListener(SETTINGS_FILLEDSHAPES)); //listen for changes of filledShapes

  Opaque=new JCheckBoxMenuItem(Res.localize("Opaque"),(Opaque!=null)?Opaque.isSelected():true); //if any previous menubar keep setting, else set to true
  (settings.add(Opaque)).addActionListener(new CanvasMenuListener(SETTINGS_OPAQUE)); //listen for changes of filledShapes

  //are these three causing problems when Canvas is in IE together with the DB component?
  settings.add(new PenSizePanel()); //14-7-1998
  settings.add(new RubberSizePanel()); //23-12-1998
  settings.add(new TracksPanel()); //18-1-1999

  settings.add(new JSeparator()); //15-8-1998
  settings.add(menuItem("Foreground Color",'F',SETTINGS_FGRCOLOR)); //14-7-1998
  settings.add(menuItem("Background Color",'B',SETTINGS_BKGRCOLOR)); //14-7-1998
  settings.add(menuItem("Fill Color",'F',SETTINGS_FILLCOLOR)); //14-7-1998
//  settings.add(menuItem("Font",'F',SETTINGS_FONT));

//  menuBar.setAlignmentX(LEFT_ALIGNMENT);
/* //not needed (ESlate component has its own help button)
  JMenu help = add(new JMenu(Res.localize("Help")));
  help.setMnemonic('H');
  help.add(menuItem("About",'A',HELP_ABOUT));
*/
 }
}

/////////////////////////////////////////////////////////////////////

 class PenSizePanel extends JPanel //14-7-1998
                    implements FocusListener //16-7-1998
 {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  public PenSizePanel(){
   super();
   setLayout(new FlowLayout(FlowLayout.RIGHT)); //18-1-1999: aligning to right
   add(new JLabel(Res.localize("Pen size")));
   penSize=new JNumberField((penSize!=null)?penSize.getinteger():1);
   penSize.setColumns(2);
   add(penSize);
   penSize.addActionListener(new CanvasMenuListener(SETTINGS_PENSIZE)); //listen for changes of PenSize
   penSize.addFocusListener(this);
  }
  //FocusListener// --- //16-7-98
  public void focusGained(FocusEvent event){} //do nothing
  public void focusLost(FocusEvent event){ //now wsp's pensize is always set from the penSize field when menu closes
   Canvas.this.Action(SETTINGS_PENSIZE); //want focus lost to do same as enter pressed
  }
 }

 class RubberSizePanel extends JPanel //23-12-1998: same as PenSizePanel, but for rubberSize
                    implements FocusListener
 {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  public RubberSizePanel(){ //29-12-1998: fixed bug: changed SETTINGS_PENSIZE to SETTINGS_RUBBERSIZE
   super();
   setLayout(new FlowLayout(FlowLayout.RIGHT)); //18-1-1999: aligning to right
   add(new JLabel(Res.localize("Rubber size")));
   rubberSize=new JNumberField((rubberSize!=null)?rubberSize.getinteger():5); //29-12-1998: initial rubber value=5
   rubberSize.setColumns(2);
   add(rubberSize);
   rubberSize.addActionListener(new CanvasMenuListener(SETTINGS_RUBBERSIZE)); //listen for changes of RubberSize
   rubberSize.addFocusListener(this);
  }
  public void focusGained(FocusEvent event){} //do nothing
  public void focusLost(FocusEvent event){ //now wsp's rubbersize is always set from the rubberSize field when menu closes
   Canvas.this.Action(SETTINGS_RUBBERSIZE); //want focus lost to do same as enter pressed
  }
 }

 class TracksPanel extends JPanel //18-1-1999: same as PenSizePanel, but for tracks
                    implements FocusListener
 {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  public TracksPanel(){
   super();
   setLayout(new FlowLayout(FlowLayout.RIGHT)); //18-1-1999: aligning to right
   add(new JLabel(Res.localize("Tracks")));
   tracks=new JNumberField((tracks!=null)?tracks.getinteger():0); //initial tracks value=0
   tracks.setColumns(2);
   add(tracks);
   tracks.addActionListener(new CanvasMenuListener(SETTINGS_TRACKS)); //listen for changes of Tracks
   tracks.addFocusListener(this);
  }
  public void focusGained(FocusEvent event){} //do nothing
  public void focusLost(FocusEvent event){ //now wsp's trackssize is always set from the tracks field when menu closes
   Canvas.this.Action(SETTINGS_TRACKS); //want focus lost to do same as enter pressed
  }
 }

///////////////////////////////////////////////////////////////////////

 class CanvasToolBar extends JToolBar{
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;

  public AbstractButton addTool(String name,int menuID) {
   String text=Res.localize(name); //17-2-1999: removed try-catch for MissingResourceException, since Res.localize handles that
   AbstractButton b;
/* --- 6-11-1998: commented out again
 //---not using radio buttons for tools: have no way to set a button if that tool is selected from the menus (what's more, JToggleButton looks nicer)
   if (menuID>=TOOL_RUBBER && menuID<=TOOL_RUBBER+0x0F){ //14-7-1998
    add(b=new JToggleButton(Res.loadImageIcon("images/" + name + ".gif",text))); //3-11-1998:now using Toggle-buttons (still have to sync with Menus!!! +allow togglebutton to untoggle)
    //toolButtonGroup.add(b); //3-11-1998: this doesn't allow depressing of a toggle-button...
    }
   else
*/
   add(b=new JButton(Res.loadImageIcon("images/" + name + ".gif",text)));
   b.setToolTipText(text+" "); //31Aug1999: added a space char at the end of setToolTipText, cause it shows better (Swing's hints eat up part of the last letter)
   b.setMargin(new Insets(0,0,0,0));
 //b.getAccessibleContext().setAccessibleName(name);
   b.addActionListener(new CanvasMenuListener(menuID)); //
//   if (!(menuID>=TOOL_RUBBER && menuID<=TOOL_RUBBER+0x0F)) { //6-11-1998: allow focus for tool buttons to show selected tool
    b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
    b.setRequestFocusEnabled(false); //2-9-1998
//   }
   return b;
  }

  class ToolBar_Tools extends MyToggleToolBar{ //7-1-1999 //19Aug1999: changed to subclass MyToggleToolBar instead of JToggleToolBar (cause the toggleButtons weren't showing up anymore)
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   int buttonCount; //12-1-1999

   public AbstractButton add(ToggleAction a){ //overriding add2(Action) to customize the buttons' appearance (remove text labels etc.)
    buttonCount++;
    AbstractButton b=super.add(a);
    b.setToolTipText((String)a.getValue(Action.NAME)+" "); //31Aug1999: hints were showing-up empty??? //b.getText()); //should use the SHORT_DESCRIPTION(?) //31Aug1999: added a space char at the end of setToolTipText, cause it shows better (Swing's hints eat up part of the last letter)
    b.setText(""); //don't show text label

    b.setMaximumSize(new Dimension(22,23));
    b.setPreferredSize(new Dimension(22,23));

    b.setMargin(new Insets(0,0,0,0));
    b.setFocusPainted(false); //27-8-1998: don't show focus rectangle
    b.setRequestFocusEnabled(false); //2-9-1998
    return b;
   }

   public ToolBar_Tools(){
    super();

    setBorder(null); //14-11-1998: no border, since this toolbar is being added into the main toolbar

    CanvasPage page=getTopPanel(); //29-12-1998

    if (page==null) return; //15-1-1999 !!!

    if (!contextMenus || page instanceof DrawingPanel || (!page.isOpaque() && wsp.getVisibleDrawingPanel()!=null)){ //18-8-1998
     add(action_SelectSelectionTool); //16-7-1998: put close to Cut/Copy/Paste
     add(action_SelectRubberTool);
     add(action_SelectTextTool);
     //20Jul1999: removed from toolbar (cause too slow)// add(action_SelectFillTool);
    //addSeparator(); //not enough space
     add(action_SelectPenTool);
     add(action_SelectLineTool);
     add(action_SelectSquareTool); //23-12-1998
     add(action_SelectRectangleTool);
     add(action_SelectCircleTool);
     add(action_SelectOvalTool);
     add(action_SelectCanonicalPolygonTool);
     add(action_SelectSprayTool);
   //addSeparator(); //not enough space
      //add(action_SelectZoomTool);
     add(action_SelectColorChooserTool);
     add(action_SelectMusicTool); //5-9-1998
    }
    if (!contextMenus || page instanceof TurtlePanel){ //18-8-1998
     add(action_SelectTurtleLineChooserTool);
     //double selection tool???
    }

    //addTool("Hand",TOOL_HAND);
    //??move selection here?(turtles)

    setMaximumSize(new Dimension(buttonCount*23+4,23+2)); //12-1-1999
    setPreferredSize(new Dimension(buttonCount*23+4,23+2)); //12-1-1999
   }

 }

  public CanvasToolBar(){
   super();
   toolsToolBar=null; //19-1-1999

   CanvasPage page=getTopPanel(); //29-12-1998

   setFloatable(false); //fixed-place toolbar

   addTool("New",FILE_NEW);
   addTool("New Turtles' page",FILE_NEW_TURTLEPAGE); //31Aug1999
   addTool("Open",FILE_OPEN);

   if(page==null) return; //15-1-1999:!!!

   if (!contextMenus || page instanceof DrawingPanel) //14-1-1999
    addTool("Save",FILE_SAVE);

   if (!contextMenus || page!=null) //29-12-1998
    addTool("Print",FILE_PRINT);

   if (!contextMenus || page instanceof DrawingPanel || (!page.isOpaque() && wsp.getVisibleDrawingPanel()!=null)){ //18-8-1998
    addSeparator();
    addTool("Cut",EDIT_CUT);
    addTool("Copy",EDIT_COPY);
    addTool("Paste",EDIT_PASTE);
    //addTool("Font",EDIT_FONT); //19Aug1999
   }
   if (!contextMenus || (page instanceof TurtlePanel && !page.isOpaque() && wsp.getVisibleDrawingPanel()!=null) ){ //12-1-1999 //18May1999: now Stamp tool won't show-up when the Turtle Page is opaque, or when it's not but there is no drawing page under it
    addTool("Stamp",EDIT_STAMP); //27-8-1998 //12-1-1999: moved here, as it's an EDIT tool
   }

   addSeparator();
   add(toolsToolBar=new ToolBar_Tools()); //7-1-1999 //19-1-1999: keeping tools_ToolBar
   toolsToolBar.setFloatable(false); //28-1-1999
  }

 }

 private transient JToggleToolBar toolsToolBar; //15Oct1999: made transient
 private transient JMenuBar menubar; //15Oct1999
 private transient JToolBar toolbar; //15Oct1999

/////////////////////

  public static void main(String[] args) {
   JComponentFrame.startJComponent("gr.cti.eslate.canvas.Canvas",Res.localize("title"),new String[]{});
   JComponentFrame.startJComponents(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(520,400);}

}



//Change PASTE to be a TOOL or add a BRUSH tool???

//make canvas a color provider/consumer

//?remove focus-changed listeners from penSize-rubberSize? (causing bugs?)

//scripting: add Square,setRubberSize
//update scripting docs, html params docs


//--- [Note] 2/12/1998 ---
// Should catch the canvas resizing to resize the scrollPanes
// to get all the canvas size... this is also needed by the TurtlePanel's
// PaintDisabled action on all panels except the one under it... if more
// drawing panels than one show under a big TurtlePage, some of them would
// fail to redraw...

//add a hand tool to pan big images

//On resize should resize all pages' scrollers

//shouldn't be able to connect while loading pages...
//else bringLastToFront shall fail...

//add component name... Check the count problem

//add the font chooser... fix it...
//default name at save dialog: page name...
//all setMnemonic are invalid in GREEK!!!
//*** Add an Image input pin (to inport images from other painters
// or from the TV)
//Fill&ColorChooser bugs related to display device's Color depth

