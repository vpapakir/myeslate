//It seems that when even a dot is drawn the whole off-screen image is repainted!!!

//20Jul1999: changed all getWidth(null) to getWidth(this), same for getHeight(...)

package gr.cti.eslate.canvas;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import gr.cti.eslate.utils.*;

import gr.cti.utils.*;

/* 28Aug2000: StorageVersion = 1
*/

/**
 * @version     2.0.4, 26-Jun-2007
 * @author      George Birbilis
 * @author      George Vasiliou
 * @author      Kriton Kyrimis
 */
public class DrawingPanel extends CanvasPage
                          implements Externalizable //!!!
{

 static final long serialVersionUID = 2711999L; //27-1-1999: serial-version, so that new vers load OK
 //public static final String STORAGE_VERSION = "1"; //28Aug2000
 public static final int STORAGE_VERSION = 1; //28Aug2000



// CONSTRUCTORs ////////////////////////////////////////////////////////////////////

 public DrawingPanel(){}; //12-1-1999: fixed-bug: must have empty constructor to be Externalizable (else readExternal throws an Error)

 public DrawingPanel(String name){
  super(name);
  setOpaque(true); //tell Swing that pages under it are hidden!
 }

////////////////////////////////////////////////////////////////////////////////////

 /*public ImageIcon getImageIcon(){ //16-12-1998
  ImageIcon icon=new ImageIcon(img);
  icon.setDescription(name);
  return icon;
 } */

 public NewRestorableImageIcon getImageIcon(){ //16-12-1998
  NewRestorableImageIcon icon=new NewRestorableImageIcon(img);
  icon.setDescription(name);
  return icon;
 }

 public String getIconName() {
    return name;
 }


  public void setImageIcon(NewRestorableImageIcon icon){ //6-1-1998
  name=icon.getDescription();
  img=icon.getImage();

 }

 public void setIconName(String n) {
    name = n;
 }

 public void setGIFImageIcon(GIFImageIcon icon){ //6-1-1998
  name=icon.getDescription();
  img=icon.getImage();
 }

 public GIFImageIcon getGIFImageIcon(){ //16-12-1998
  GIFImageIcon icon=new GIFImageIcon(img);
  icon.setDescription(name);
  return icon;
 }


/* public void setImage(ImageIcon icon,int width,int height){ //16-12-1998
  if (icon!=null){
   setImage(icon.getImage(),width,height); //if width,height==0 replaced by respective image size
   name=icon.getDescription();
  }
 }*/
 public void setImage(NewRestorableImageIcon icon,int width,int height){ //16-12-1998
  if (icon!=null){
   setImage(icon.getImage(),width,height); //if width,height==0 replaced by respective image size
   name=icon.getDescription();
  }
 }

////////////////////////////////////////////////////////////////////////////////////

 public void paintComponent(Graphics g){
//System.out.println("DrawingPanel::paintComponent "+name);
//System.out.println("-->"+g.getClipBounds());
 //  if (paintingDisabled) return; //2-12-1998: turtlePanel temporarily blocks the repainting of the drawingpanel that's visible under it (used when reexecuting procs at past time)
  if (img!=null) {
//  System.out.println("Image redrawn");

//  System.out.println("height: "+getHeight());
//  System.out.println("visibleRect: "+getVisibleRect());

//  System.out.println("pageHeight: "+PageHeight);
//Filling-bkgr-START (in case off-screen image less size than page) //14-1-1999
  Dimension size = getSize();
  if (size.width != PageWidth || size.height != PageHeight) {
    g.setColor(getCanvas().wsp.getBackground());
    g.fillRect(0, 0, size.width, size.height);
  }
//Filling-bkgr-END (always filling bkgr even if non-opaque page [don't care])

   g.drawImage(img,0,0,this); //no transparency for now
   //g.drawImage(img,0,0, PageWidth, PageHeight, this); //no transparency for now

//   System.out.println("imgH: "+img.getHeight(this));
 //g.setColor(Color.blue);g.drawLine(0,0,(myid==0)?100:200,(myid==0)?200:100);
 /* //too slow
   int [] pixels=new int[(PageHeight)*(PageWidth)];
   PixelGrabber pg = new PixelGrabber(img, 0, 0, PageWidth, PageHeight, pixels, 0, PageWidth);
   pg.startGrabbing(); //DON'T FORGET TO DO THIS!!!
   if (pg!=null) try{pg.grabPixels();}catch(InterruptedException e){
    System.out.println("Graphix.getPoint: Interrupted exception");
    return;
   }
   int x,y;
   Color c=gx.fgrColor;
   for (y=0;y<PageHeight;y++) //< or <= ???
    for (x=0;x<PageWidth;x++) //< or <= ???
    {gx.fgrColor=new Color(pixels[y*PageWidth+x]);
      if (gx.fgrColor!=gx.bkgrColor) gx.Point(x,y);}
   gx.fgrColor=c;
 */
 }else super.paintComponent(g);//12-1-1999: optimized, img!=null calls it only if needed (calls paintComponent, was calling paint() by mistake)
}


public void repaint(){ //15-8-1998 (refresh all drawing area - we have transparent pages) //1-12-1998: moved the repaint() method to DrawingPanel from the CanvasPage class
// System.out.println("DrawingPanel::repaint "+name);
 Canvas c;
 if ((c=getCanvas())!=null) //remove the next check when full transparent pages
  if (c.wsp.getTopPanel()==this) //first check if it is a DrawingPanel (faster)
   super.repaint();
  else{ //if no CanvasPage on top, do an updating of the turtle panels
   c.wsp.updateTurtlePanels();
//   canvas.dtp.validate();
//   canvas.dtp.repaint();
  }
 else super.repaint();
}

////////////////////////////////////////////////////////////////////////////////////

 public void setImage(Image i,int w,int h){
//  System.out.println("DrawingPanel:SetImage(Image,int,int)");
  if (i==null) return; //7-1-1999: fixed:was checking 'img' instead of 'i'
  if (w==0) w=i.getWidth(this);
  if (h==0) h=i.getHeight(this);
  setSiz(w,h);

/* //not used: would need if off-screen image took up the whole panel (costly in memory)... now doing our stuff in paintComponent()
  if(gx.g==null) return; //14-1-1999
  Color c=gx.g.getColor();
  gx.g.setColor(getBackground());
  gx.g.fillRect(0,0,getWidth(),getHeight()); //Clear the offscreen Image first (it contains garbage)
  gx.g.setColor(c);
*/

  gx.setPAINT();
  paintImage(i,w,h); //12-1-1999
 }

 public void paintImage(Image i,int w,int h){ //12-1-1999
  if (gx.g!=null) gx.g.drawImage(i,0,0,w,h,null); //???not gx: bugs for now
  //double -1 needed for now...
  repaint();
 }

 public void setImage(Image i){
//  System.out.println("DrawingPanel:SetImage(Image)");
  if (i==null) return; //7-1-1999: fixed:was checking 'img' instead of 'i'
  if (getTool() instanceof CanvasSelection &&
   ((CanvasSelection)getTool()).selected){
   CanvasSelection s=(CanvasSelection)getTool();
   s.drawImage(i);
   repaint();
  }else setImage(i,0,0);
 }

/******************************************************************************
 Call this only after adding the component to some container (which
 must be added to some container, etc... all must lead to something on screen
 in order to get a valid graphics object)
******************************************************************************/
 public void setSiz(int width,int height){ //6-1-1999: moved here from CanvasPage
  //System.out.println("setSiz called for "+name);
//26-1-1999//  width++;//?
//26-1-1999//  height++;//?
//  System.out.println("setSiz height: "+height);
  Dimension d=new Dimension(width,height);
  setSize(d); //setPreferredSize(d); setMinimumSize(d); setMaximumSize(d);
  try{
   Image oldImg=img; //6-1-1999
   if (gx!=null) gx.dispose(); //3-9-1998: images and graphics don't get garbage collected (use this to avoid Out Of Memory errors)
   //img = createImage(width,height); //14-1-1999: should be creating off-screen image of size same as the panel? costs much, so we're now clearing bkgr in paintComponent()
   img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

   gx=new Graphix(img,this.getColorModel());
   PageWidth=width; PageHeight=height;
   gx.setMaxXY(width-1,height-1); //needed in filling
   //(do this here, gx needs to have been initiallized first)
   gx.clear(getBackground()); //14-7-1998: clear with background color (need to have set MaxXY first)
   paintImage(oldImg,0,0); //6-1-1999: safe if oldImg==null //12-1-1999:fixed-bug: don't call setImage (infinite recursion), call this instead
  }catch(Exception e){
   e.printStackTrace();
   System.err.println(e+"\nCouldn't create offscreen buffer (image)");
   System.err.println("-> "+width+"x"+height);
   gx=new Graphix(null,null); //!
  }
 }

// PRINTING ////////////////////////////////////////////////////////////////////////

public void printIt(Graphics page,Dimension pagesize){
 //System.out.println("Printing page "+name);
 Dimension size = new Dimension(PageWidth,PageHeight); //this.getSize();
// if (size.width<pagesize.width && size.height<pagesize.height){
/*
		page.translate((pagesize.width - size.width)/2,(pagesize.height - size.height)/2);
		page.drawRect(-1,-1,size.width+1,size.height+1);
		page.setClip(0,0,size.width,size.height);
*/

    int x1,y1,x2,y2;

        int ysteps=size.height/80;
        int xsteps=size.width/80;
        for(int y=0;y<ysteps+1;y++)
         for(int x=0;x<xsteps+1;x++) {
            x1=x*80; y1=y*80; x2=Math.min((x+1)*80/*-1*/,size.width/*-1*/); y2=Math.min((y+1)*80/*-1*/,size.height/*-1*/);
            page.drawImage(img, x1,y1,x2,y2, x1,y1,x2,y2, null); //draw 80x80 tiles
            //Graphics.drawImage needs a +(1,1)
         }
/*
 }
 else{
      int ysteps=Math.min(size.height,pagesize.height)/80;
      int xsteps=Math.min(size.width,pagesize.width)/80;
      for (int xpage=0;xpage<pagesize.width/size.width+(((pagesize.width%size.width)==0)?0:1);xpage++){
       for (int ypage=0;ypage<pagesize.height/size.height+(((pagesize.height%size.height)==0)?0:1);ypage++){
        for(int y=0;y<ysteps+1;y++)
         for(int x=0;x<xsteps+1;x++) {
*///            x1=x*80; y1=y*80; x2=Math.min((x+1)*80/*-1*/,size.width/*-1*/); y2=Math.min((y+1)*80/*-1*/,size.height/*-1*/);
/*            page.drawImage(img,
                           x1,y1,x2,y2,
                           xpage*pagesize.width+x1,ypage*pagesize.height+y1,xpage*pagesize.width+x2,ypage*pagesize.height+y2,
                           null); //draw 80x80 tiles
            //Graphics.drawImage needs a +(1,1)
         }
//  		page = job.getGraphics();}
//       job.finalize();//job.end();
//       page = job.getGraphics();}
 }
*/

//      page.drawImage(img,size.width-10,size.height-10,this); //this???
//     	this.printAll(page); //this works for small images (80x80 or a bit more)

}

// PERSISTENCE /////////////////////////////////////////////////////////////////////

  // readExternal and writeExternal do not work properly across Java versions.
  // Use setProperties and getProperties, instead.

  public StorageStructure getProperties()
  {
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
    fieldMap.put("BaseProperties", super.getProperties());
    fieldMap.put("Image", getImageIcon());
    fieldMap.put("Name", getIconName());
    return fieldMap;
  }

  public void setProperties(StorageStructure fieldMap)
  {
    super.setProperties((StorageStructure)fieldMap.get("BaseProperties"));
    //doesn't draw cause off-screen buffer might not exist yet: next call to setSize will draw
    setImageIcon((NewRestorableImageIcon)fieldMap.get("Image"));
    setIconName((String)fieldMap.get("Name"));
  }


 public void writeExternal(ObjectOutput out) throws IOException{
    //System.out.println("DrawingPanel:writeExternal");
    super.writeExternal(out);
    ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
    fieldMap.put("Image", getImageIcon());
    fieldMap.put("Name", getIconName());
    out.writeObject(fieldMap);
    out.flush();


  //super.writeExternal(out);// correct
  //out.writeObject(getGIFImageIcon()); //correct


//  out.writeObject(getForegroundColor());
//  out.writeObject(getBackgroundColor());
//  out.writeObject(getFillColor());
//  out.writeObject(new Boolean(getFilledShapes()));
//  out.writeObject(new Integer(getPenSize()));
//  out.writeObject(new Integer(getRubberSize()));
  //add more fields to bottom...
 }

 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
  super.readExternal(in);
  Object firstObj = in.readObject();
  if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
      // oldreadExternal
      setGIFImageIcon((GIFImageIcon)firstObj); //doesn't draw cause off-screen buffer might not exist yet: next call to setSize will draw
      //setImageIcon((NewRestorableImageIcon)firstObj); //doesn't draw cause off-screen buffer might not exist yet: next call to setSize will draw
  }
  else{
      //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
      StorageStructure fieldMap = (StorageStructure) firstObj;
      //setGIFImageIcon((GIFImageIcon)fieldMap.get("GifImage")); //doesn't draw cause off-screen buffer might not exist yet: next call to setSize will draw
      setImageIcon((NewRestorableImageIcon)fieldMap.get("Image")); //doesn't draw cause off-screen buffer might not exist yet: next call to setSize will draw
      setIconName((String)fieldMap.get("Name"));
  }
  //SwingUtilities.invokeLater(new Runnable() {
  //    public void run() {
  //        revalidate();
  //    }
  //});
/*
  try{
   super.readExternal(in);
   setImageIcon((GIFImageIcon)in.readObject()); //doesn't draw cause off-screen buffer might not exist yet: next call to setSize will draw
//   setForegroundColor((Color)in.readObject());
//   setBackgroundColor((Color)in.readObject());
//   setFillColor((Color)in.readObject());
//   setFilledShapes(((Boolean)in.readObject()).booleanValue());
//   setPenSize(((Integer)in.readObject()).intValue());
//   setRubberSize(((Integer)in.readObject()).intValue());
  }catch(Exception e){System.err.println("Error reading some fields at DrawingPanel:readExternal");}*/
 }

}
