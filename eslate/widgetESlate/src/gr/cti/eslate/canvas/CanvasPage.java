//problem with multi-print (4-12-1998-note: JDK<1.2 showed multiple print dialog for multiple pages printing)
//prints when JVM closes (use end instead of finalize?)
//prints bad...

package gr.cti.eslate.canvas;

//AVAKEEO-START//
import gr.cti.eslate.base.*;
import gr.cti.eslate.sharedObject.*;
//AVAKEEO-END//

import java.lang.Exception;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import gr.cti.eslate.utils.*;


import gr.cti.utils.Graphix;

/* 28Aug2000: StorageVersion = 1

*/

/**
 * @version     2.0.4, 26-Jun-2007
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
abstract public class CanvasPage extends JComponent //4-12-1998: now extends JComponent!
                                 implements Externalizable //6-1-1999: descendents should override (and call super)
{

 static final long serialVersionUID = 2711999L; //27-1-1999: serial-version, so that new vers load OK //19Oct1999: fixed-typo: had "serialVersionUIDoy" instead of "serialVersionUID"
 //public static final String STORAGE_VERSION = "1"; //28Aug2000
 public static final int STORAGE_VERSION = 1; //6/6/2002


// CONSTRUCTORs ////////////////////////////////////////////////////////////////////

 public CanvasPage(){}; //12-1-1999: fixed-bug: must have empty constructor to be Externalizable (else readExternal throws an Error)

 //public CanvasPage(String name){
 public CanvasPage(String name){
  super();
  if (name!=null)
      this.name=name; //13-1-1999: null names can't be added in menus, using default name in such cases
  plugInTool(new CustomCanvasTool()); //add a do-nothing tool;
 //don't forget to add this tool! (pages with no tool give their actions
 //to pages under them!!!)
}

////////////////////////////////////////////////////////////////////////////////////

 // REFRESH DISABLING FOR SCRIPTING [BATCH/OFF-SCREEN DRAWING/UPDATING] //

 private boolean refreshEnabled=true;

 public void setRefreshEnabled(boolean enabled){ //1Jun1999: batch-drawing
  if(enabled && !refreshEnabled) repaint(); //shouldn't check if is dirty to avoid extra repaints? [maybe not, cause if a script used a refresh_disabled / refresh_enaled block, then it must have done some drawing in the block which it needed to be batch/offscreen drawn and updated using only one repaint at the end (when drawing was completed)]
  refreshEnabled=enabled;
 }

 public void refresh(){ //1Jun1999: batch-drawing
  if(refreshEnabled) repaint();
 }

////////////////////////////////////////////////////////////////////////////////////

 public void setScrollerSize(Dimension d){setScrollerSize(d.width,d.height);} //20Jul1999

 public void setScrollerSize(int width, int height){ //20Jul1999: apart from the border, the component also may have its own insets
  //Insets insets=scroller.getInsets();
  //scroller.setSize(new Dimension(width-insets.left-insets.right,height-insets.top-insets.bottom));

  scroller.setSize(new Dimension(width,height)); //29Mar2000
 }

 public void makeScroller(Dimension d){makeScroller(d.width,d.height);} //20Jul1999

 public void setOpaque(boolean opaqueness) {
    if (scroller instanceof JScrollPane){
        ((JScrollPane)scroller).setOpaque(opaqueness);
        //((JScrollPane)scroller).getViewport().setOpaque(opaqueness);
        ((JScrollPane)scroller).setViewportView(this);
    }
    super.setOpaque(opaqueness);
//    revalidate();
//    repaint();

 }

 public void makeScroller(int width, int height){ //20Jul1999: not returning insets, since the scrollers won't have a border anymore
  setBorder(null); //15-1-1999 //don't use a border, else user shall draw on it //20Jul1999: wasn't setting the border to null
                               //just place this inside a JScrollPane (which should have its own border)
  JScrollPane s=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //29Mar2000: explicitly specifying the scrollbars' policy (just for sure)
  s.setOpaque(false);
  s.getViewport().setOpaque(false);

  s.setBackground(Color.white);
  s.getViewport().setBackground(Color.white);


  scroller=s; //scroller var is a java.awt.Container, s is a descendent of it javax.swing.JScrollPane
  //scroller.setBackground(Color.white);
  setScrollerSize(width,height); //29Mar200: doing before assiging a content to the scrollpane

  s.setViewportView(this);
  s.setBorder(null); //20Jul19999: not having a border for this page's ScrollPane, the WorkSpace container that contains all ScrollPanes will have one
 }

 public void close(){ //3-9-1998: must call this to free resources
  unplugTool(); //5-9-1998: must call this to close QT in MusicTool
  if(imagePin!=null) imagePin.disconnect(); //14-1-1999
  if (gx!=null) gx.dispose();
 } //13-1-1999: this won't free QT... shouldn't anyway... close isn't called when unplugging a tool

// TOOLS ///////////////////////////////////////////////////////////////////////////

 public void unplugTool(){ //13-1-1999
  if (tool!=null){
   disableTool(); //!!! don't forget to do this !!!
   tool.reset(); //needed in Selections to remove selection when changing tool
   tool=null;
  }
 }

 public void disableTool(){
  removeMouseListener(tool);
  removeMouseMotionListener(tool);
 }

 public void enableTool(){ //calls' count must match disableTool() calls' count
  addMouseListener(tool);
  addMouseMotionListener(tool);
 }

 public void plugInTool(ICanvasTool tool){
  unplugTool();
  this.tool=tool;
  if (tool!=null){
   tool.setGraphix(gx);
   tool.setComponent(this);
   enableTool();
  }
 }

 public ICanvasTool getTool(){return tool;} //19-1-1999

////////////////////////////////////////////////////////////////////////////////////

 abstract public void printIt(Graphics page,Dimension pagesize);

/*
public void paint(Graphics g){ //15-8-1998
 super.paint(g);
 if (selection!=null){
  //
  g.setXORMode(Color.orange);
  g.setColor(getBackground());
  //
  g.fillRect(selection.x,selection.y,selection.width,selection.height);
 }
}
*/

 public void DidSomeChange(){
  //System.out.println("Image changed");
  dirty=true; //image has been changed since its last save
  // **** nikos
  //imageChange.set_image(img);


  if (img != null) {
    //ImageIcon pic = new ImageIcon(img);
    NewRestorableImageIcon pic = new NewRestorableImageIcon(img);
    //System.out.println("pic: "+pic);
    //System.out.println("pic width: "+pic.getIconWidth());
    iconSO.setIconSO(pic);
  }

 }

///////////////////////////////////

 private Canvas canvas=null; //15-8-1998

 public Canvas getCanvas(){ //15-8-1998
  //2Jun1999: getPeer() deprecated in JDK1.1.8// if (getPeer()==null) return null; //25May1999
 if (canvas==null){ //locate the canvas only the first time needed, then cache its reference
  Container c=scroller;
  if (c!=null) //25May1999: seems scroller is null some times?
  do{
   try{c=c.getParent();}catch(Exception e){/**/e.printStackTrace();/**/ return null;}//11-1-1999: Swing1.1 threw exception here //24May1999: printing exception here
//   if (c instanceof Canvas.WorkSpace) {return canvas=((Canvas.WorkSpace)c).getCanvas();} //3Jun1999: avoid searching backwards for Canvas, get it from the WorkSpace instead (was using this cause ESlate's Container was getting our NormalViewPanel and adding it to some other parent than the Canvas one)
   if (c instanceof Canvas) {return canvas=(Canvas)c;}
  }while (c!=null);
 }
 if (canvas==null) System.err.println("CanvasPage:getCanvas returned null - page may have not been added to Canvas yet");
 return canvas; //may be null
}

///////////////////////////////////

 //safe even for Turtles (use some other Graphix implementation for them?)
 public void setForegroundColor(Color c){
  setForeground(c); //14-1-1999
  gx.setColor(c);
 }
 public Color getForegroundColor(){return gx.getColor();}

 public void setBackgroundColor(Color c){
  setBackground(c); //14-1-1999
  gx.bkgrColor=c;
 }
 public Color getBackgroundColor(){return gx.bkgrColor;}

 public void setFillColor(Color c){gx.fillColor=c;}
 public Color getFillColor(){return gx.fillColor;}

 public void setFilledShapes(boolean f){gx.filled=f;}
 public boolean getFilledShapes(){return gx.filled;}

 public void setPenSize(int s){gx.setPenSize(s);}
 public int getPenSize(){return gx.getPenSize();}

 public void setRubberSize(int s){gx.setRubberSize(s);}
 public int getRubberSize(){return gx.getRubberSize();}

 public void setFont(Font f){ //19Aug1999: override "setFont"
  super.setFont(f);
  gx.setFont(f);
 }

 //Name property//   //30Mar2000

 public String getName(){
/* System.out.println("getting name: "+name);
 int length = findStringLength(name);
 String lastPart = name.substring(length);
 System.out.println("lastPart: "+lastPart);
 String firstPart = name.substring(0,length);
 System.out.println("firstPart: "+firstPart);
 if (firstPart.equals("Turtles") || firstPart.equals("Челюнет"))
    return Res.localize("newturtlepage")+lastPart;
 else
    return Res.localize("newpage")+lastPart;
 */
  return name;
 }
/*
 private int findStringLength(String s) {
    char[] sChar = s.toCharArray();
    for (int i=0; i < sChar.length; i++){
        int num = sChar[i] - '0';
        if ( num >= 0 && num <= 9 )
            return i;
    }
    return -1;
 }
*/
 public void setName(String name){
  this.name=name;
 }

 //Dirty property//  //30Mar2000

 public boolean isDirty(){
  return dirty;
 }

 public void setDirty(boolean dirty){
  this.dirty=dirty;
 }

 //Selection property//  //30Mar2000

 public Rectangle getSelection(){
  return getCanvas().wsp.getSelection();
 }

 public void setSelection(Rectangle r){ //?
  //System.out.println("CanvasPage::setSelection");
  getCanvas().wsp.setSelection(r); //set selection for all pages
 }

//////////////////////////////////////////////////////////////////////

 protected String name="***"; //a default name (if a null is placed on a menu causes errors) //30Mar2000: changed scope to "protected" from "public"
 public Rectangle selection=null; //must be public, since Canvas.dtp.setSelection sets it (and can't call CanvasPage.setSelection cause that one is calling back into Canvas.dtp.setSelection and would make an infinite loop)
 protected boolean dirty=false; //indicates that this page has been changed since its last save //30Mar2000: changed scope to "protected" from "public"
 public transient Plug imagePin; //6-1-1999: not-serialized
 //public transient ImageChange imageChange; //6-1-1999: not-serialized // nikos
 public transient IconSO iconSO; //6-1-1999: not-serialized
 public transient Container scroller=null;//15-1-1999: keeping scroller in a Container var (temp: needed for TurtlePanels)
 private transient ICanvasTool tool; //can't serialize this: is a listener for mouse clicks
 public transient Graphix gx=new Graphix(null,null); //it's pretty safe like that even for Turtle pages (does nothing)
 public boolean paintingDisabled; //2-12-1998: enables/disables the "paint" method (default value=false) --used only in DrawingPanel (placed here to avoid casts from CanvasPage to DrawingPanel at the "dtp.setPaintingDisabled" method)

 transient Image img=null;
 int PageWidth,PageHeight;

// PERSISTENCE /////////////////////////////////////////////////////////////////////

  // readExternal and writeExternal do not work properly across Java versions.
  // Use setProperties and getProperties, instead.

  public StorageStructure getProperties()
  {
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
    fieldMap.put("Class", getClass().getName());
    fieldMap.put("Name", name);
    fieldMap.put("Opaque", isOpaque());
    fieldMap.put("Width", getWidth());
    fieldMap.put("Height", getHeight());
    return fieldMap;
  }

  public void setProperties(StorageStructure fieldMap)
  {
    if (fieldMap != null) {
      name=(String)fieldMap.get("Name");
      setOpaque(fieldMap.get("Opaque", true));
      setSize(
        fieldMap.get("Width", 50),
        fieldMap.get("Height", 50)
      );
    }
  }

 public void writeExternal(ObjectOutput out) throws IOException{
  //System.out.println("CanvasPage:write");
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
    fieldMap.put("Name", name);
    fieldMap.put("Opaque", isOpaque());
    fieldMap.put("Width", getWidth());
    fieldMap.put("Height", getHeight());
    out.writeObject(fieldMap);
    out.flush();
/*
  out.writeObject(name);
  out.writeObject(new Boolean(isOpaque())); //13-1-1999
  out.writeObject(new Integer(getWidth())); //14-1-1999: must save size, crucial for TurtlePanels (so that connected Turtles show their icons at the right place)
  out.writeObject(new Integer(getHeight())); //14-1-1999
*/
 }

 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
    //System.out.println("CanvasPage:read");
   Object firstObj = in.readObject();
    if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
        // oldreadExternal
        name=(String)firstObj;

        setOpaque(((Boolean)in.readObject()).booleanValue()); //13-1-1999
        setSize(
          ((Integer)in.readObject()).intValue(),
          ((Integer)in.readObject()).intValue()); //14-1-1999
    }
    else{
        //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
        StorageStructure fieldMap = (StorageStructure) firstObj;

        name=(String)fieldMap.get("Name");
        setOpaque(fieldMap.get("Opaque", true)); //13-1-1999
        setSize(
          fieldMap.get("Width", 50),
          fieldMap.get("Height", 50)); //14-1-1999
    }
    //SwingUtilities.invokeLater(new Runnable() {
    //    public void run() {
    //        revalidate();
    //    }
    //});
/*

  name=(String)in.readObject();
  setOpaque(((Boolean)in.readObject()).booleanValue()); //13-1-1999
  setSize(
   ((Integer)in.readObject()).intValue(),
   ((Integer)in.readObject()).intValue()); //14-1-1999
  */
 }

///////////// DEBUG ///////////////////

/*
public void update(Graphics g){
 System.out.println("CanvasPage::update "+name);
 super.update(g);
}

public void paint(Graphics g){
 System.out.println("CanvasPage::paint "+name);
 super.paint(g);
}

public void paintComponent(Graphics g{
 System.out.println("CanvasPage::paintComponent "+name);
 super.paintComponent(g);
}

public void repaint(){
 System.out.println("CanvasPage::repaint "+name);
 super.repaint();
}
*/

}



