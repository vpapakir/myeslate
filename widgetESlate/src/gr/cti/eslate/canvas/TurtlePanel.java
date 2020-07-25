//should migrate Turtle coordinate->Canvas coordinates methods to the turtlePage from Turtle (turtlepage should have a newLine(double,double,double,double) method, not a newLine(int,int,int,int) one)

//--- [Note] 2/12/1998 ---
//When sliding a Turtle SWING still redraws all underlying pages
//but now I'm blocking the repaint just once at the start of the proc reexecution
//blocking all drawing-pages re when a line is drawn at "past" time
//seems that the turtle+slider causes paint to be called on all pages (even hidden ones!)
//that's why we are blocking all of them (maybe has to do with the fact that the TurtlePage seems to be
//a bit taller than the other pages???)

//MultiThreading won't work: each line should carry info on it's top level clock

//-----Done----- 9/7/98
//at newLine: when first line comes save all connected Turtles' state
//in the LineGroup data... don't put this in NewCall (too much overhead
//for procs not doing any drawing)... when respawning
//some call at some previous clock (when new call comes
//check if that clock exists) restore Turtles' state before executing
//any more drawing...

//@13/7/98 : fixed clearAll() - now TurtlePanel can be cleared
//@14/7/98 : fixed new bug at clearAll() - now lines created out of some proc are displayed OK
//@31/8/98 : decreased accuracy to 0.08 [easier line selection]
//@1/9/98: decreased accuracy a bit more
//@4/12/98: moved LineData inner class to a separate file (now not an inner class any more)

//note: when a line is drawn in past time we have to do a repaint,
//      since this line needs to be below newer lines (if the proc is slow one will
//      see the shape drawing behind other shapes... else we could draw all lines up to
//      the current past-time proc, then do the proc reexecution, then draw the top lines...
//      but if the proc took some time [using WAIT prim, or cause it does complex calculations]
//      then the lines of procs above that time would not show till the proc-reexecution is over
//      and that would be very ugly... However the current approach costs, cause the page below is
//      repainted too ???

package gr.cti.eslate.canvas;

import java.io.*;
import java.awt.*; //Graphics
import java.awt.event.*;
import java.util.*; //Vector,Enumeration
import javax.swing.*;

import gr.cti.eslate.base.*;

import gr.cti.eslate.sharedObject.LogoCallSO;
import gr.cti.eslate.logo.ProcedureCall;

import gr.cti.eslate.turtle.ITurtle;
import gr.cti.eslate.turtle.TurtleState;
import gr.cti.eslate.turtle.TurtleIcon;

import gr.cti.eslate.utils.*;

/* 28Aug2000: StorageVersion = 1
*/

/**
 * @version     2.0.8, 16-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
@SuppressWarnings(value={"unchecked"})
public class TurtlePanel extends CanvasPage
                         implements Externalizable, //!!!
                                    ConnectionListener //14-1-1999
{

 static final long serialVersionUID = 2711999L; //27-1-1999: serial-version, so that new vers load OK
  //public static final String STORAGE_VERSION = "1"; //28Aug2000
  public static final int STORAGE_VERSION = 1; //28Aug2000

 public TurtlePanel(){}; //12-1-1999: fixed-bug: must have empty constructor to be Externalizable (else readExternal throws an Error)

 public TurtlePanel(String name){ //14-1-1999: removed turtlePin param from constructor //5-2-1999: removed LOGOcall from constructor (addCanvasPage will do that assignment)
  super(name);

  setDoubleBuffered(true); //29Mar2000: must be double-buffered, else garbage occurs when using the Slider

  //enableEvents(ComponentEvent.COMPONENT_RESIZED); //31Aug1999: check for resizes

  setLayout(null); //!!!
  setForeground(Color.white); //22-9-1998: when Opaque is set from the menus, paint TurtlePage white (till a bkgr color selection is done from the menus to change this color)

  groups=new Vector();   //of LineGroup (this and the following line always come in pairs)
  newTopLevelProc(null); //for lines not belonging to some call (drawn by top level primitive invocation)
                         //...(that ProcedureCall is null - not available to Slider!!!)
  //this.callSO=callSO;

 /*
  //TEST//
  newLine(0,0,200,100,Color.red);
  newLine(0,0,100,100,Color.green);
  newLine(0,0,100,200,Color.blue);
 */
}

 public void close(){ //14-1-1999
  super.close();
  if(turtlePin!=null) turtlePin.disconnect();
 }

 public void newTopLevelProc(ProcedureCall call){ //called by Canvas' LogoCallS
  try{
   if (curGroup!=null && curGroup.isEmpty()) //if no line was added... //4-2-1999: prevGroup local var removed, since wasn't used anywhere else
    groups.removeElement(curGroup);  //...remove prev LineGroup

   LineGroup g;
   if (call==null) g=null; //in case not belonging to any group (not executed inside some proc, for example, user gave an FD at the top-level)
   else {
     g=selectByTime(call.time); //find if this is a reinvocation of some old call (usually from Slider)...
     //System.out.println(call.time);
    }
   if (g!=null) { //...is a reinvocation
    //System.out.println("Found a group");
    curGroup=g; //...if it is make that group the current one (new lines are drawn at that depth)
    //!!!...should clear the group if tracking is false...!!!
    //???...how would slider pass that value to us? --> by passing us the track val decrementing it: if it's 0 then do a clear
    g.call.procArgs=call.procArgs; //9-7-98: added to keep the new params of that [old]call (which was reexecuted)

    //-- restore saved Turtle state --// (for tracking too?)
    restoreTurtleStates();
    turtles_lockVisibility(false); //4-2-1999: hide turtles, if Slider is reexecuting some routine (not doing for all routines, cause it's not nice to the user)
            //8-2-1999: have some flashing with hiding and showing back the turtle, but can't set to true, since we'd see the turtle moving arround while dragging (too slow)
            //29Mar2000: moved the turtles_lockVisibility(false) before the repaint call so that Turtles are already hidden when this repaint is done!

    Rectangle boundsRect=g.getBoundingRectangle(); //30Mar2000: keep the boundsRect and then clear the old lines before repainting!!!
    g.setEmpty(); //!no tracking for now: clearing always!// //30Mar2000: fixed-bug: clearing of the old lines is must be done before repainting!!!

    repaint(boundsRect); //do a repaint, as lines drawn while the proc executes should show without the old ones (of the same proc) being displayed (also do a repaint after drawing a single line in past time)
                         //29Mar2000: repainting only the invalidated area
   }else{
    curGroup=new LineGroup(call); //5-2-1999: this must be before the saveTurtleStates command
    groups.addElement(curGroup);
    if (call==null) //if we returned to top-level (a null call is sent when proc finishes, so that next FDs etc. [lines not in a proc] don't get attatched to the previous proc)
     turtles_unlockVisibility(); //restoreTurtleVisibilities(); //5-2-1999: unlocking the turtles visibility (any showturtle/hideturtle executed by the routine will now show their effect)
    else //6-2-1999: (curGroup.call!=null && curGroup.isEmpty()) is true at this point //save connected Turtles' state only if a line comes from a top level proc (now not saving at the first line in newLine, since rotations might have been done before the 1st line was drawn)
     saveTurtleStates(); //Save turtles' state... (only if no TurtleState exits [has ever been saved] in this LineGroup)
   }
  }catch(Exception e){e.printStackTrace(); System.err.println("TurtlePanel: exception at newTopLevelProc routine");} //23May1999: printing a stack trace (there was a bug in ESlate's Pin::getProtocolComponents(), stack trace revelead it)
 }

 public void newLine(int x1,int y1,int x2,int y2,Color c){
  //System.out.println("newLine "+x1+" "+y1+" "+x2+" "+y2+" "+c.getRed()+c.getGreen()+c.getBlue());

  //???how come curGroup.call throw an exception at the first FD? (curGroup is null at start!)

  try{ //25May1999: wrapping it all in a try-catch, cause if Virtuoso catches an exception, shows only its name and does it on the Logo console

  /* 6-2-1999: moved to newTopLevelProc, since a Turtle may be rotated, before a line is drawn, so we wouldn't save the originl state at the proc's start, but the rotated one!
  if (curGroup.call!=null && curGroup.isEmpty()) //save connected Turtles' state only if a line comes from a top level proc (and only at the first line!!!)
   saveTurtleStates(); //Save turtles' state... (only if no TurtleState exits [has ever been saved] in this LineGroup)
  */

   LineData line=new LineData(x1,y1,x2,y2,c); //1-12-1998: keeping the LineData object to call its "draw" method
   try{
    curGroup.add(line);
   }catch(Exception e){
    System.err.println("Error at TurtlePanel::newLine, curGroup is null?");
   }
   repaint(line.getBoundingRectangle()); //29Mar2000: replaced all old painting code with a more polite one

  }catch(Exception e){
   System.err.println("Error at TurtlePanel::newLine");
   e.printStackTrace();
  }

 }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 //should set curGroup? (so that future lines are done here)
 public LineGroup select(int x,int y){
  LineGroup group;
  //Vector v;
  for (int i=groups.size();i-->0;){ //search groups from top to bottom
   group=(LineGroup)groups.elementAt(i);
   if (group.call==null) continue; //skip any LineGroups not belonging to some call (drawn by top level primitive invocation)
   if (group.contains(x,y)) return group; //exits the for loop //26Nov1999: moved logic to LineGroup.contains(x,y) and using that one
  }
  return null; //no line contains this point
 }

 public LineGroup selectByTime(int time){
  LineGroup group;
  ProcedureCall p;
  int t;
  for (int i=groups.size();i-->0;){ //search groups from top to bottom
   group=(LineGroup)groups.elementAt(i);
   p=group.call;
   if (p!=null) t=p.time; else continue; //skip any LineGroups not belonging to some call (drawn by top level primitive invocation)
   if (t==time) return group; //exits the for loop
   else if (t<time) return null; //from top to bottom we get decreasing "times", so won't be found anywhere deeper
  }
  return null; //no group was built for a call at that "time"
 }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 public void clearAll(){
  callSO.set_call(null); //5-2-1999: clear Slider's display
  groups=new Vector(); //of LineGroup (13-7-1998: fixed bug - was declaring a new local var called groups, instead of clearing the groups class var)
  ProcedureCall curCall=curGroup.call;
  curGroup=null;
  newTopLevelProc(curCall); //continue with a fresh group for the current call (doing curGroup=null so that Turtle states aren't saved to this "curGroup" when the proc starts... they will be saved at its end)
  repaint(); //15-1-1999: do a refresh!
 }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 public void drawAllLines(Graphics g){
  Rectangle clip=g.getClipBounds(); //11-2-1999: cliping //29Mar2000: enabled cliping again
  for (int i=0;i<groups.size();i++){ //draw groups from bottom to top
   LineGroup group=(LineGroup)groups.elementAt(i);
   if (group.getBoundingRectangle().intersects(clip)) //11-2-1999: cliping //29Mar2000: enable cliping again: draw a lineGroup only if its boundingRectangle is inside the current clipBounds
    group.draw(g); //11-2-1999: moved drawing code to LineGroup class
  }
 }

 public void paintComponent(Graphics g){ //"super.paint" not called, since it does nothing
  //System.out.println("TurtlePanel::paintComponent "+name);
  //System.out.println("-->"+g.getClipBounds());

  //29Mar2000//if (isOpaque()) { g.setColor(getBackground()); g.fillRect(0,0,getWidth(),getHeight()); } //use instead of super.paintComponent?
  super.paintComponent(g); //13-1-1999: to fill our background in case we are opaque

  drawAllLines(g);
 }

 public void printIt(Graphics page,Dimension pagesize){ //24-12-1998
 //System.out.println("Printing page "+name);
/*
 Dimension size = this.getSize(); //not using PageWidth,PageHeight (should remove from DrawingPanel too?)
// if (size.width<pagesize.width && size.height<pagesize.height){
		page.translate((pagesize.width - size.width)/2,(pagesize.height - size.height)/2);
		page.drawRect(-1,-1,size.width+1,size.height+1);
		page.setClip(0,0,size.width,size.height);
*/
    //drawAllLines(page);
    paint(page);
 }

/////////////////////////////////////////////////////////////////////////////////////////////////////

 class SavedTurtle{
  ITurtle turtle;
  TurtleState state;

  public SavedTurtle(ITurtle turtle){
   this.turtle=turtle;
   saveState();
  }
  public void saveState(){state=new TurtleState(turtle.getState());} //make a copy of the turtle state data
  public void restoreState(){turtle.setState(state);} //restore the turtle state data from their kept copy
  public void restoreVisibility(){turtle.setVisible(state.turtleVisible);} //4-2-1999
 }

///////////////////////////////////////////////////////

//---- Turtles management -----------------------------------//

/* //5-2-1999: not used any more
public void turtles_setVisible(boolean flag){
 Component[] c=getComponents();
 for(int i=0;i<c.length;i++)
  c[i].setVisible(flag);
}
*/

 public void turtles_lockVisibility(boolean visible){ //5-2-1999
/*
  Component[] c=getComponents(); //costly, makes array copies
  for(int i=0;i<c.length;i++)
   ((TurtleIcon)c[i]).lockVisibility(visible);
*/
 for(int i=getComponentCount();i-->0;) //25-2-1999: optimized for memory usage and speed
  ((TurtleIcon)getComponent(i)).lockVisibility(visible);
 }

 public void turtles_unlockVisibility(){ //5-2-1999
/*
  Component[] c=getComponents(); //costly, makes array copies
  for(int i=0;i<c.length;i++)
   ((TurtleIcon)c[i]).unlockVisibility();
*/
 for(int i=getComponentCount();i-->0;) //25-2-1999: optimized for memory usage and speed
  ((TurtleIcon)getComponent(i)).unlockVisibility();
 }

////////

 public void saveTurtleStates(){
  if (curGroup.savedTurtles!=null) return; //save turtle state only once!!!
  Vector v=new Vector();
  ESlateHandle[] handles=turtlePin.getProtocolHandles(); //25May1999: ESlate1.5.19
  for(int i=0;i<handles.length;i++) //25May1999: ESlate1.5.19: getProtocolComponent renamed to getProtocolHandles, and returning an ESlateHandle[] instead of a Vector
   try{ //try-catch inside the for: if a Turtle doesn't respond OK, just ignore it
    v.addElement(new SavedTurtle((ITurtle)(handles[i].getComponent())));
   }catch(Exception ex){System.err.println("Error saving a turtle's state");}
  curGroup.savedTurtles=v;
 }

 public void restoreTurtleStates(){
  if (curGroup.savedTurtles==null) return; //no turtle state has been saved!!!
  for(Enumeration e=curGroup.savedTurtles.elements();e.hasMoreElements();)
   try{ //try-catch inside the for: if a Turtle doesn't respond OK, just ignore it
    ((SavedTurtle)e.nextElement()).restoreState();
   }catch(Exception ex){System.err.println("Error restoring turtles' state");}
 }

/* //5-2-1999: not used any more, will add visibility overriding at Turtle component
 public void restoreTurtleVisibilities(){ //4-2-1999
  if (curGroup==null //must check if the curGroup is null, since this is called, even when we're not playing with the Slider, or made any previous call (could move calling of this proc, after making a new curGroup, but test for null here, just to play it safe
      || curGroup.savedTurtles==null) return; //no turtle state has been saved!!! (OK, need to check this, when restoring Turtle visiblities - only way to say if it's a new proc ending, or some reexecution)
  for(Enumeration e=curGroup.savedTurtles.elements();e.hasMoreElements();)
   try{ //try-catch inside the for: if a Turtle doesn't respond OK, just ignore it
    ((SavedTurtle)e.nextElement()).restoreVisibility();
   }catch(Exception ex){System.err.println("Error restoring turtles' visibility");}
 }
*/

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // readExternal and writeExternal do not work properly across Java versions.
  // Use setProperties and getProperties, instead.

  public StorageStructure getProperties()
  {
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
    fieldMap.put("BaseProperties", super.getProperties());
    fieldMap.put("Groups", groups);
    return fieldMap;
  }

  public void setProperties(StorageStructure fieldMap)
  {
    super.setProperties((StorageStructure)fieldMap.get("BaseProperties"));
    groups=(Vector)fieldMap.get("Groups");
    newTopLevelProc(null);
  }

 public void writeExternal(ObjectOutput out) throws IOException{
//  System.out.println("TurtlePanel:writeExternal!!!!");
    super.writeExternal(out);
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
    fieldMap.put("Groups", groups);
    out.writeObject(fieldMap);
    out.flush();

  //super.writeExternal(out);
  //out.writeObject(groups);
 }

 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
    super.readExternal(in);
    Object firstObj = in.readObject();
    if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
        // oldreadExternal
        groups=(Vector)firstObj;
        newTopLevelProc(null);
  }
  else{
      //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
      StorageStructure fieldMap = (StorageStructure) firstObj;
      groups=(Vector)fieldMap.get("Groups");
      newTopLevelProc(null);
  } 
  //SwingUtilities.invokeLater(new Runnable() {
  //    public void run() {
  //        revalidate();
  //    }
  //});



  //System.out.println("TurtlePanel:readExternal");
//  super.readExternal(in);
//  groups=(Vector)in.readObject();
  //14-1-1999:dtp will repaint them all// repaint(); //!!!
//  newTopLevelProc(null); //14-1-1999: must do this to init curGroup (remove in the future if saving old curGroup id)
 }

////////////////////// //????

/*
 public void setSiz(int width,int height){ //6-1-1999: moved here from CanvasPage
  //System.out.println("setSiz called for "+name);
  width++;//?
  height++;//?
  Dimension d=new Dimension(width,height);
  setSize(d); setPreferredSize(d); setMinimumSize(d); setMaximumSize(d);
 }
*/

/*
  public void processComponentEvent(ComponentEvent e){ //31Aug1999: when resizing, notify turtles of new size (coordinate space got translated)
   //System.out.println("TurtlePanel Resized");
   super.processComponentEvent(e); //place first!!!
   //...translate all line drawings... (get lastsize-cursize)
   //...turtles should be listeners for turtle panel resizes and by themselves change their coord space (translate only) and place their icons appropriately on the turtle panel
  }
*/

  private Dimension oldSize = null;

  public void processComponentEvent(ComponentEvent e)
  {
    super.processComponentEvent(e); //place first!!!

    Canvas canvas = (Canvas)SwingUtilities.getAncestorOfClass(
      gr.cti.eslate.canvas.Canvas.class, this
    );
    if (canvas == null || canvas.isFixedTurtlePageSizes()) {
      return;
    }
    if (e.getID()==ComponentEvent.COMPONENT_RESIZED) {
      Dimension size = getSize();
      PageWidth = size.width;
      PageHeight = size.height;
      if (oldSize != null) {
        int oldX = oldSize.width / 2;
        int oldY = oldSize.height / 2;
        int newX = size.width / 2;
        int newY = size.height / 2;
        int dX = newX - oldX;
        int dY = newY - oldY;
        int nGroups = groups.size();
        for (int i=0; i<nGroups; i++) {
          LineGroup lg = (LineGroup)groups.get(i);
          lg.translateLines(dX, dY);
        }
        oldSize = size;
      }
      oldSize = getSize();
    }
  }

///////////////////////////////////////////////////////////////////////////////////////////////////////////

 public void handleConnectionEvent(ConnectionEvent e){
  try{
//   ((ITurtle)e.getOwnPin().getProtocolComponent()).setTurtlePanel(this); //!!! This will always get the 1st Turtle that was connected to us and not next Turtles
   ((ITurtle)e.getPlug().getHandle().getComponent()).setTurtlePanel(this); //4-2-1999: this allows more than one turtle to connect and show into a turtle panel //18May1999: ESlateII
  }catch(Exception ex){System.err.println(ex+"\nError at TurtlePanel's turtle connection event handler");}
 }

//////////////////////////////////////

 public int getTracks(){return tracks;} //18-1-1999
 public void setTracks(int s){tracks=s;} //18-1-1999

// FIELDS /////////////////////////////////

 int tracks; //18-1-1999
 Vector groups;
 LineGroup curGroup=null;
 public transient LogoCallSO callSO=null; //6-1-1999: not-serialized
 public transient ProtocolPlug turtlePin; //6-1-1999: not-serialized

}
