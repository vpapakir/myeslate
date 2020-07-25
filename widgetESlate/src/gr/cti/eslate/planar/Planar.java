//BUG: lastPoint seems to get desynchronized from cur Point some times
//TO-DO: show lastPoint in RED to see if when it doesn't match the curPoint
//...can't do... lastPoint is set to null when we release the mouse (to make a new line next time we drag)

/*
 2.7 - 24Feb1999 - fixed menus to show localized
                 - corrected About dialogs
                 - allowing axis showing/hiding
                 - created View menu and moved there showing/hiding of view items (axis,grid,control points)
                 - added xrange and yrange Vector in-out pins
 2.8 - 25Feb1999 - can detect SO events coming from the xrange and yrange pins
                 - when changing location of a point it's old connection vertex are cleared (the old area is repainted, not only the new one)
                 - fixed SO detection, now works OK (was mixing the 3 pins as a single 1)
                 - changed xrange,yrange SOs to use fromPoint,toPoint SOs
 2.9 - 26Feb1999 - now mapping works OK!
 3.0 -  1Mar1999 - now "Select All" works OK... "New" also works OK (and then deselects)
                 - "Select All" doesn't just make selector the size of the PlotPanel, but may make it bigger and with a negative offset location so that it contains all PlotPoints inside the PlotPanel
 3.1 -  2Mar1999 - now dragging up&left wards works OK too
 3.2 - 27Mar1999 - now can start as an application and also load frames with additional applets specified at the command line
                 - added icons for "Control Points", "Axis", "Grid"
 3.3 -  4Apr1999 - Axis is now painted over the Grid and not under it
 3.4 - 13May1999 - dragging out of the visible area doesn't do anything
                 - a mouse release causes a new line to be started at the next mouse drag (the last point doesn't get connected to the new one). Mouse clicks now do isolated points, instead of the old "lineto" behaviour
 3.5 - 14May1999 - now changing an axis-var current value from the Slider after a New at Planar, isn't causing a null pointer exception (PlotPanel was keeping the old currentPoint and was trying to move it and it tried to tell its father to redraw, but it didn't have a father anymore)
                 - now when getting new values from the "value" vector pin, if we haven't selected some older point than the last point (that is curPoint if doesn't exist or it's not the last point) then we're adding new points, else we're moving curPoint arround (changing its value: old behaviour was always changing its value, not adding new points when new data came in from the Pin)
 4.0 - 19May1999 - moving to E-Slate II
 4.1 -  2Jun1999 - not an applet anymore
 4.2 -  4Jun1999 - runs OK in "E-Slate Container" now
                 - now combinations of right button mouse clicks&drags draw a continous line
                 - selections are now done holding down SHIFT
                 - Planar now functions As a Graph2dPlotter and supports the Graph2dPlotterPrimitives primitive group
 4.3 - 11Jun1999 - PlanarApplet now asks Planar for its preferred size
 4.4 - 13Aug1999 - moved to package gr.cti.eslate
 4.5 - 20Aug1999 - now when mouse is over a control point (and it is visible), cursor changes to a HAND_CURSOR
 4.6 - 15Oct1999 - Planar now has storable "MenuBarVisible" and "ToolBarVisible" properties
     - 16Oct1999 - Planar's "MenuBarVisible" and "ToolBarVisible" properties now work OK
                 - added SerialVersionUID field to Planar, so that future versions work OK with older saved data (and reverse)
                 - moved localization code to gr.cti.eslate.planar.Res class
 4.7 - 28Mar2000 - removed output capability from From & To plugs (not implemented correctly yet)
 4.8 - 03Apr2000 - implementing the Externalizable interface (to state that this component isn't serializable)
                 - telling Planar's ancestor, BaseComponent, not to write(read) any data during externalization(internalization)
 4.9 - 23May2000 - keeping loading compatibility with saved-data from previous beta versions of BaseComponent & Planar
 5.0 - 29Jun2000 - added beanInfo and icon
                 - moved actions to separate class
                 - added fromX/toX, fromY/toY properties to PlotPanel and to Planar class
     - 30Jun2000 - added the fromX, toX, fromY and toY properties to the bean info
*/

//AVAKEEO-START//
package gr.cti.eslate.planar;

import gr.cti.eslate.base.*;
import gr.cti.eslate.sharedObject.VectorData;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.birbilis.BaseComponent; //2Jun1999
//AVAKEEO-END//

import javax.swing.*;

import java.io.*; //19May1999: for Externalizable - ESlateII
import java.awt.*;
import java.util.*;
import gr.cti.eslate.utils.*;

import gr.cti.utils.*;

/* 28Aug2000: StorageVersion = 1
              ComponentVersion = 1.0.0
*/

/**
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Kriton Kyrimis"
 * @version     2.0.1, 23-Jan-2008
 */
public class Planar extends BaseComponent //2Jun1999
                    implements SharedObjectListener,
                               gr.cti.eslate.scripting.LogoScriptable, //4Jun1999
                               gr.cti.eslate.scripting.AsGraph2dPlotter, //4Jun1999
                               java.io.Externalizable //03Apr2000: added this, to eliminate the possibility that Java tries to serialize this component
{

 //public final static String VERSION="5.0"; //30Jun2000
 public final static String VERSION="2.0.1"; //28Aug2000
 //public static final String STORAGE_VERSION = "2";
 public static final int STORAGE_VERSION = 2;

 static final long serialVersionUID = 16101999L; //16Oct1999: serial-version, so that new vers load OK

 protected boolean isOldStylePersisting(){
  return true; //03Apr2000: keep compatibility with old persistence data
 }


  /**
   * Returns Copyright information.
   * @return	The Copyright information.
   */
  public ESlateInfo getInfo()
  {return new ESlateInfo(Res.localize("title")+VERSION,Res.localizeArray("info"));}

//////

 private JPanel PLANAR;

 public JComponent getContent(){
  if (PLANAR==null){PLANAR=new JPanel();}
  return PLANAR;
 }

////////////////////////////////////////////////////

 //MenuBarVisible property//

 public void setMenuBarVisible(boolean visible){ //15Oct1999
  if(menubar!=null) menubar.setVisible(visible);
 }

 public boolean isMenuBarVisible(){ //15Oct1999
  return (menubar!=null)?menubar.isVisible():false;
 }

 //ToolBarVisible property//

 public void setToolBarVisible(boolean visible){ //15Oct1999
  if(toolbar!=null) toolbar.setVisible(visible);
 }

 public boolean isToolBarVisible(){ //15Oct1999
  return (toolbar!=null)?toolbar.isVisible():false;
 }

 //fromX property//

 public double getFromX(){ //29Jun2000
  return plot.getFromX();
 }

 public void setFromX(double value){ //29Jun2000
  plot.setFromX(value);
 }

 //fromY property//

 public double getFromY(){ //29Jun2000
  return plot.getFromY();
 }

 public void setFromY(double value){ //29Jun2000
  plot.setFromY(value);
 }

 //toX property//

 public double getToX(){ //29Jun2000
  return plot.getToX();
 }

 public void setToX(double value){ //29Jun2000
  plot.setToX(value);
 }

 //toY property//

 public double getToY(){ //29Jun2000
  return plot.getToY();
 }

 public void setToY(double value){ //29Jun2000
  plot.setToY(value);
 }

////////////////////////////////////////////////////

 public PlanarActions actions=new PlanarActions(this); //29Jun2000: separated actions in "PlanarActions" class

 public Planar(){
  super();

  setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));

  try{
   setUniqueComponentName(Res.localize("Planar"));
  }catch(RenamingForbiddenException e){} //19May1999: ESlateII

  createPlugs();

   //GUI//

   Container c=PLANAR; //4Jun1999 - moved all the init() code here

   c.setLayout(new BorderLayout()); //24-2-1999: removed GridBagLayout (caused menu bar to disappear when resizing to a small height)

   c.add(createMenus(),BorderLayout.NORTH); //16Oct1999
   c.add(plot=new PlotPanel(vect),BorderLayout.CENTER);
   //c.add(makeStatusBar(),BorderLayout.SOUTH);

   actions.toggleAction_viewAxis.setSelected(true);
 }

 public void createPlugs(){
 //VECTOR PIN//
    try {
      Plug vPlug = new SingleInputMultipleOutputPlug(
		  getESlateHandle(), null, Res.localize("Vector"), Color.green,
		  gr.cti.eslate.sharedObject.VectorData.class, vect,this); //19May1999: ESlateII

      //vPlug.setNameLocaleIndependent("Διάνυσμα");
      vPlug.setNameLocaleIndependent("Vector");
      addPlug(vPlug);
      vPlug.addConnectionListener(new ConnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleConnectionEvent(ConnectionEvent e){
          //System.out.println("Planar: Current point pin connected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION){
              vSO=((SharedObjectPlug)e.getPlug()).getSharedObject(); //this works only for single-input pin !$#!#^@@ (in the future have SOlisteners for each pin)
              double hor=((VectorData)vSO).getX();
              double ver=((VectorData)vSO).getY();
              vect.setVectorData(hor, ver);//, soe.getPath()); //copy data to output vector
              plot.newVectorData();
          }
          /*else
       	  if (type == Plug.OUTPUT_CONNECTION) { // make sure connected component gets value of vector when connected
    	     SharedObjectEvent soe = new SharedObjectEvent(vect); //22May1999: using new SO constructor
    	     ((SharedObjectListener)(e.getPlug().getHandle().getComponent())).handleSharedObjectEvent(soe); //19May1999: ESlateII
      	  }*/
   	     }
        } );
      vPlug.addDisconnectionListener(new DisconnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleDisconnectionEvent(DisconnectionEvent e){
          //System.out.println("Planar: Current point pin disconnected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION) vSO=null; //free the remote SO reference when input gets disconnected
   	     }
        } );
    } catch (InvalidPlugParametersException e) {System.err.println(e.getMessage());
    } catch (PlugExistsException e) {System.err.println(e.getMessage());}

 //FROM PIN//
    try {
      SingleInputPlug fromPlug = new SingleInputPlug/*SingleInputMultipleOutputPin*/( //28Mar2000: removed output capability
		  getESlateHandle(), null, Res.localize("From"), Color.green,
		  //gr.cti.eslate.sharedObject.VectorData.class, fromPoint,this); //19May1999: ESlateII
          gr.cti.eslate.sharedObject.VectorData.class, this); //19May1999: ESlateII
      fromPlug.setNameLocaleIndependent("From");
      //fromPlug.setNameLocaleIndependent("Από");
      addPlug(fromPlug);
      fromPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e){
          //System.out.println("Planar: from pin connected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION){
             fromSO=((SharedObjectPlug)e.getPlug()).getSharedObject(); //this works only for single-input pin !$#!#^@@ (in the future have SOlisteners for each pin)
             double hor=((VectorData)fromSO).getX();
             double ver=((VectorData)fromSO).getY();
             fromPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output xrange
             plot.setFromPoint(hor,ver); //bottom-left end
          }
          /*else
       	  if (type == Plug.OUTPUT_CONNECTION) { // make sure connected component gets value of vector when connected
    	     SharedObjectEvent soe = new SharedObjectEvent(fromPoint); //22May1999: using new SO constructor
    	     ((SharedObjectListener)(e.getPlug().getHandle().getComponent())).handleSharedObjectEvent(soe); //19May1999: ESlateII
      	  }*/
   	     }
        } );
      fromPlug.addDisconnectionListener(new DisconnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleDisconnectionEvent(DisconnectionEvent e){
          //System.out.println("Planar: from pin disconnected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION) fromSO=null; //free the remote SO reference when input gets disconnected
   	     }
        } );
    } catch (InvalidPlugParametersException e) {System.err.println(e.getMessage());
    } catch (PlugExistsException e) {System.err.println(e.getMessage());}

 //TO PIN//
    try {
      Plug toPlug = new SingleInputPlug/*SingleInputMultipleOutputPin*/( //28Mar2000: removed output capability
		  getESlateHandle(), null, Res.localize("To"), Color.green,
		  //gr.cti.eslate.sharedObject.VectorData.class, toPoint, this); //19May1999: ESlateII
          gr.cti.eslate.sharedObject.VectorData.class, this); //19May1999: ESlateII
      toPlug.setNameLocaleIndependent("To");
      //toPlug.setNameLocaleIndependent("Μέχρι");
      addPlug(toPlug);
      toPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e){
          //System.out.println("Planar: to pin connected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION){
              toSO=((SharedObjectPlug)e.getPlug()).getSharedObject(); //this works only for single-input pin !$#!#^@@ (in the future have SOlisteners for each pin)
              double hor=((VectorData)toSO).getX();
              double ver=((VectorData)toSO).getY();
              toPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output yrange
              plot.setToPoint(hor,ver); //top-right end
          }
          /*else
       	  if (type == Plug.OUTPUT_CONNECTION) { // make sure connected component gets value of vector when connected
    	     SharedObjectEvent soe = new SharedObjectEvent(toPoint); //22May1999: using new SO constructor
    	     ((SharedObjectListener)(e.getPlug().getHandle().getComponent())).handleSharedObjectEvent(soe);
      	  }*/
   	     }
        } );
      toPlug.addDisconnectionListener(new DisconnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleDisconnectionEvent(DisconnectionEvent e){
          //System.out.println("Planar: to pin disconnected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION) toSO=null; //free the remote SO reference when input gets disconnected
   	     }
        } );
    } catch (InvalidPlugParametersException e) {System.err.println(e.getMessage());
    } catch (PlugExistsException e) {System.err.println(e.getMessage());}
 }

/////////////////////////////

  /**
   * Handle an event from some SO
   * @param	soe	The event
   */
  public synchronized void handleSharedObjectEvent(SharedObjectEvent soe){
   SharedObject so=soe.getSharedObject();
   if (so instanceof VectorData) //? how do I distinguish between the 3 vectors?
   {
    VectorData newVect = (VectorData)(soe.getSharedObject()); //get input vector
    //Component c=newVect.getComponent();
    double hor=newVect.getX();
    double ver=newVect.getY();

    //System.out.println(c+" "+vComponent+xComponent+" "+yComponent);
    //System.out.println(c+" "+vSO+xSO+" "+ySO);

    if (newVect==vSO){
     //System.out.println("Planar: got current point");
     vect.setVectorData(hor, ver);//, soe.getPath()); //copy data to output vector
     plot.newVectorData();
    }
    else
    if (newVect==fromSO){
     //System.out.println("Planar: got FROM point");
     fromPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output xrange
     //plot.setXrange(hor,ver); //vect.x=left range edge, vect.y=right range edge
     plot.setFromPoint(hor,ver); //bottom-left end
    }
    else
    if (newVect==toSO){
     //System.out.println("Planar: got TO point");
     toPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output yrange
     //plot.setYrange(hor,ver); //vect.x=left range edge, vect.y=right range edge
     plot.setToPoint(hor,ver); //top-right end
    }
    else {System.out.println("Internal error at handleSharedObjectEvent!");} //@#%@#%#@$% (won't be inited with data at the first connection, cause our handleSO will be called by the other end before our connection event has been called)
   }

  }

  /**
   * Save the component's state.
   * @param	out	The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput out) throws IOException { //19May1999: from old ESlate's saveState
      try{
          ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
          fieldMap.put("MenuBarVisible", isMenuBarVisible());
          fieldMap.put("ToolbarVisible", isToolBarVisible());
          //if (getBorder() != null) {
              try{
                  BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
                  fieldMap.put("BorderDescriptor", bd);
              }catch(Throwable thr){
                thr.printStackTrace();
              }
          //}
          out.writeObject(fieldMap);
          out.flush();
      }catch(Exception e){System.err.println(e+"\nError saving Planar's state");}

   /*try{
    out.writeObject(new Boolean(isMenuBarVisible())); //15Oct1999
    out.writeObject(new Boolean(isToolBarVisible())); //15Oct1999
    //out.writeObject(varsPanel); //15-10-1998
   }catch(Exception e){System.err.println(e+"\nError saving Planar's state");}
   */
  }

  /**
   * Load the component's state.
   * @param	in	The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException { //19May1999: from old ESlate's loadState
      Object firstObj = in.readObject();
      if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
      // oldreadExternal
          try{
              if(firstObj instanceof gr.cti.utils.ObjectHash)
                  firstObj=in.readObject(); //23May2000: compatibility with previous beta versions: ignore any ObjectHash saved by the BaseComponent ancestor
              //28-1-1999:this blocks the component// while(!appletHasDoneStartup); //1-12-1998: wait till applet has started //27-1-1999: !!!
              setMenuBarVisible(((Boolean)firstObj).booleanValue()); //15Oct1999
              setToolBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
          }catch(Exception e){System.err.println(e+"\nError loading all of Planar's state");}
      }else{
          //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
          StorageStructure fieldMap = (StorageStructure) firstObj;

          setMenuBarVisible(fieldMap.get("MenuBarVisible",true)); //15Oct1999
          setToolBarVisible(fieldMap.get("ToolbarVisible",true)); //15Oct1999
          BorderDescriptor bd = (BorderDescriptor)fieldMap.get("BorderDescriptor");
          //if (bd != null){
              try{
                  setBorder(bd.getBorder());
              }catch(Throwable thr){}
          //}
      }
//


  /*
   try{
    Object o=in.readObject(); if(o instanceof gr.cti.utils.ObjectHash) o=in.readObject(); //23May2000: compatibility with previous beta versions: ignore any ObjectHash saved by the BaseComponent ancestor
    setMenuBarVisible(((Boolean)o).booleanValue()); //15Oct1999
    setToolBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
   //vspane.setVarsPanel((VarsPanel)in.readObject()); //15-10-1998
   }catch(Exception e){System.err.println(e+"\nError loading all of Planar's state");}
   */
  }

///////////////////////////////////////////////////////////////////////

public JPanel createMenus(){ //16Oct1999: renamed to "createMenus" from "makeMenus"
 JPanel p=new JPanel();
 p.setLayout(new BorderLayout());
 p.add(menubar=makeMenuBar(),BorderLayout.NORTH);
 p.add(toolbar=makeToolBar(),BorderLayout.SOUTH);
 return p;
}

public JMenuBar makeMenuBar(){
 JMenuBar mb=new JMenuBar();
 JMenu m;
 JCheckMenu cm;

 m=mb.add(new JMenu(Res.localize("File")));
  m.add(actions.action_New);
 //m.add(actions.action_Open);
  m.add(actions.action_Print);

 m=mb.add(new JMenu(Res.localize("Edit")));
 //m.add(actions.action_Undo);
 //m.add(actions.action_Redo);
 //m.addSeparator();
 m.add(actions.action_Cut);
 m.add(actions.action_Copy);
 m.add(actions.action_Paste);
 m.add(actions.action_Clear);
 m.add(actions.action_SelectAll);

/* //bug: JRadioMenu&JCheckMenu seems to need a JToggleToolBar too in order for events to be dispatched ok
 m=mb.add(new JCheckMenu(Res.localize("Tools")));
 m.add(actions.toggleAction_useSelectionTool);
 m.add(actions.toggleAction_useZoomTool);
*/

 m=mb.add(new JMenu(Res.localize("View")));
 m.add(new ToggleActionCheckMenuItem(actions.toggleAction_viewControlPoints));
 m.add(new ToggleActionCheckMenuItem(actions.toggleAction_viewAxis));

 cm=new JCheckMenu(Res.localize("Mapping"));
 cm.add(actions.toggleAction_selectCartesianMapping);
 cm.add(actions.toggleAction_selectPolarMapping);
 m.add(cm);

 cm=new JCheckMenu(Res.localize("Grid"));
 cm.add(actions.toggleAction_selectDotGrid);
 cm.add(actions.toggleAction_selectLineGrid);
 m.add(cm);

 return mb;
}

public JToolBar makeToolBar(){ //add tools in their separate toggle toolbar, for now
 MyToolBar m=new MyToolBar();
 m.add(actions.action_New);
 //m.add(actions.action_Open);
 m.add(actions.action_Print);
 m.addSeparator(); //don't use "add(new JSeparator())" cause stretches the separators horizontally
 m.add(actions.action_Cut);
 m.add(actions.action_Copy);
 m.add(actions.action_Paste);
 //m.add(toggleAction_Select);
 m.addSeparator(); //don't use "add(new JSeparator())" cause stretches the separators horizontally
 m.add(actions.toggleAction_viewAxis);
 m.add(actions.toggleAction_viewGrid);
 m.add(actions.toggleAction_viewControlPoints);
 return m;
}

 ///////

 public void printPlot(){ //Originated from PixMaker.java
  //System.out.println("PlotPanel:print");
  Toolkit toolkit = Toolkit.getDefaultToolkit();
  PrintJob job = toolkit.getPrintJob(new JFrame(),"Print Dialog?",printprefs); //the "Print Dialog?" doesn't show up anywhere???
  if (job == null) {System.out.println("Canceled printing..."); return;}

  Graphics page = job.getGraphics();
  //Dimension pagesize = job.getPageDimension();

  try{
   //plot.print(page); //this one shows the invisible controls as well
   plot.printAll(page); //this seems OK
   //plot.paintAll(page);
   //plot.paint(page);
  }catch(Exception e){System.err.println(e+"\nPrinting failed");}

  page.dispose();
  job.end();
  //job.finalize();//job.end();
 }

/// SCRIPTING //////////////////////////////////////////////////////////////////////////////////

 public String[] getSupportedPrimitiveGroups(){
  return new String[]{"gr.cti.eslate.scripting.logo.Graph2dPlotterPrimitives"};
 }

 public void newGraph(String name){
  plot.newGraph();
 }

 public void newPoint(double x,double y){
  plot.newWorldPoint(x,y);
 }

 public void clearAll(){
  plot.clearAll();
 }

////////////////////////////////////////////////////////////////////////////////////////////////

  //private JPanel menus;
  public PlotPanel plot;
  private VectorData vect=new VectorData(getESlateHandle());
  private VectorData fromPoint=new VectorData(getESlateHandle()); //24-2-1999 //25-2-1999: renamed to fromPoint
  private VectorData toPoint=new VectorData(getESlateHandle()); //24-2-1999 //25-2-1999: renamed to toPoint
  private SharedObject vSO,fromSO,toSO; //25-2-1999: used to detect from where a sharedObjectChanged event is coming

  private static Properties printprefs = new Properties(); //static so that all panels share printprefs

/////////////////////////////////////////////////

 private transient JMenuBar menubar; //15Oct1999
 private transient JToolBar toolbar; //15Oct1999

/////////////////////////////////////////////////

  public static void main(String[] args) {
   JComponentFrame.startJComponent("gr.cti.eslate.planar.Planar",Res.localize("title"),new String[]{});
   JComponentFrame.startJComponents(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(400,400);}

}
