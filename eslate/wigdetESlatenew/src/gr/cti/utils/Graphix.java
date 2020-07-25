//add Round rectangles?

//32-bit copy-paste and saves don't work
//drawOval is a shit... create my own!!!
//if an oval is one of the first things drawn on a page it crashes it

//@16-7-1998: no more (g!=null) checking in fatXXX procs...
//            ...they are private and before calling them we ensure g!=null

package gr.cti.utils; //1Mar1999: moved to package gr.cti.utils

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;
import java.lang.Math;

public class Graphix{ //19Aug1999

 public static Image Clipboard; //static: shared by all Graphix instances
 public Color fgrColor=Color.black,bkgrColor=Color.white,fillColor=Color.red;
 private int penSize=1; //20-10-1998: made private (should use setPenSize to change)
 private int rubberSize=5; //23-12-1998
 public boolean filled=false;
 public int MaxX,MaxY;

// PixelGrabber pg;
 public Image img;
 public Graphics g;
 public ColorModel colorModel;
// ImageObserver observer;

 boolean firstPoint=true;
 int lastx,lasty;

 public int getWidth(){ //5-9-1998
  if (img!=null) return img.getHeight(null); else return 0;
 }
 
 public int getHeight(){ //5-9-1998
  if (img!=null) return img.getWidth(null); else return 0;
 }
 
 public Graphix(/*ImageObserver o,*/Image i,ColorModel c){
  g=null;
  img=i;
  colorModel=c; //!!!
  //observer=o;

//  pg=new PixelGrabber(img,0,0,-1,-1,true); //don't use this: caches images (would need flush before grabs)

  MaxX=0;
  MaxY=0;
  if (i!=null) {
   g=i.getGraphics();
   MaxX=i.getWidth(null)-1;
   MaxY=i.getHeight(null)-1;
   //???if not ready returns -1 from getWidth/getHeight.... should have some ImageObserver
   if (g==null) System.out.println("Graphix: couldn't get Graphics from supplied image");
  }//else System.out.println("Grahpix: null Image supplied"); //11-7-98: don't show this (CanvasPage/TurtlePanel have null graphics, only DrawingPanel hasn't)
  firstPoint=true;
  filled=true; //!!!
  setColor(Color.black);
//  fgrColor.setAlpha
  setPenSize(1); //20-10-1998
  setRubberSize(5); //23-12-1998
}

 public void dispose(){ //3-9-1998: Must call this to release resources!!!
  //don't do Clipboard.flush() here: Clipboard is static and is shared among many Graphix instances
  if(g!=null) g.dispose();
  if(img!=null) img.flush();
 }

 public void setMaxXY(int maxx,int maxy){
  MaxX=maxx;
  MaxY=maxy;
  if (g!=null) g.setClip(0,0,maxx,maxy); else System.out.println("setMaxXY: Graphics is null!");
 }

///////////////////////////////////////////////////////////////////////

 public void setXOR(){setXOR(Color.orange);}

 public void setXOR(Color color){
  if (g!=null) {
   Color c=g.getColor();
   g.setColor(Color.yellow);
   g.setXORMode(color);
   g.setColor(c);
  }
 }

 public void setPAINT(){if (g!=null) g.setPaintMode();}

///////////////////////////////////////////////////////////////////////

 public void moveTo(int x,int y){
  lastx=x;
  lasty=y;
  firstPoint=false; //30-8-1998: this was missing and was causing the first Line to draw just a point at its end
 }

 public int getX() {return lastx;}
 public int getY() {return lasty;}

///////////////////////////////////////////////////////////////////////

 public void Point(int x,int y){
//  if (g!=null) g.setClip(x,y,1,1); //???
  Line(x,y,x,y);
 }

//WARNING: this routine depends on the display device's color delpth
//         you might loose some color components' info
 public Color getPoint(int x,int y){
// System.out.println("Graphix:getPoint");
  int[] pixel=new int[1];
  if (img!=null){
   PixelGrabber pg = new PixelGrabber(img, x, y, 1, 1, pixel, 0, 1);
   pg.startGrabbing(); //DON'T FORGET TO DO THIS!!!
   if (pg!=null) try{pg.grabPixels();}catch(InterruptedException e){
    System.out.println("Graphix.getPoint: Interrupted exception");
    return fgrColor;}
   else System.out.println("Graphix.getPoint: Couldn't make PixelGrabber");
// System.out.println(pixel[0]+"\n"+(new Color(pixel[0])));
   return new Color(pixel[0]);
  } else System.out.println("img was null!!!");
  return fgrColor; //?
 }

// 20-10-1998: spray //////////////////////////////////////////////////////////////////

private void _Point(int x,int y){ //20-10-1998: internal: no null check, no fat points
 g.drawLine(x,y,x,y);
}

private int R=10;
public double density=1/2d;
private int area=calculateArea();

private void RPoint(int cx,int cy,int x,int y){ //20-10-1998: to use with Spray
 if (((cx-x)^2+(cy-y)^2)<=R*R) _Point(x,y);
}

private int calculateArea(){return (int)(Math.PI*R*R*density/4);} //16-7-1998 //15-8-1998:added /4 cause drawing quadruples of points //20-10-1998: moved here from CanvasSpray

public void Spray(int x,int y){ //20-10-1998
 for (int i=0;i<area;i++){ //16-7-1998: added "area"
  RPoint(x,y,x+(int)(Math.random()*R),(int)(y+Math.random()*R));
  RPoint(x,y,x-(int)(Math.random()*R),(int)(y-Math.random()*R));
  RPoint(x,y,x+(int)(Math.random()*R),(int)(y-Math.random()*R));
  RPoint(x,y,x-(int)(Math.random()*R),(int)(y+Math.random()*R));
 }
 moveTo(x,y); //21-10-1998
}

///////////////////////////////////////////////////////////////////////

 public Color getColor(){return fgrColor;}

 public void setColor(Color c){fgrColor=c; if (g!=null) g.setColor(c);}

 public int getPenSize(){return penSize;} //20-10-1998
 public void setPenSize(int p){ //20-10-1998
  if (p>0) penSize=p; //23-12-1998: ensure penSize>0

  R=Math.max(penSize,10)/2; //20-10-1998: copied from Canvas.Action(TOOL_SPRAY) [16-7-1998: spray has pen's size (but min value=10)]
  //this.density=density;
  area=calculateArea(); //16-7-1998: place after R&density setting
  //Math.randomize();
 }

 public int getRubberSize(){return rubberSize;}//23-12-1998
 public void setRubberSize(int p){ //23-12-1998
  if (p>0) rubberSize=p; //ensure rubberSize>0
 }

///////////////////////////////////////////////////////////////////////

/*
 public void handlesinglepixel(int x, int y, int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red   = (pixel >> 16) & 0xff;
        int green = (pixel >>  8) & 0xff;
        int blue  = (pixel      ) & 0xff;
        // Deal with the pixel as necessary...
 }

 public void handlepixels(Image img, int x, int y, int w, int h) {
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return;
        }
        if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");
            return;
        }
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                handlesinglepixel(x+i, y+j, pixels[j * w + i]);
            }
        }
 }
*/

// FAT SHAPES (with PenSize) //////////////////////////////

void fatLine(int x1,int y1,int x2,int y2){
  g.drawLine(x1,y1,x2,y2);
  for (int i=0;i<(penSize/2);i++) {
   g.drawLine(x1+i,y1,x2+i,y2);
   g.drawLine(x1-i,y1,x2-i,y2);
   g.drawLine(x1,y1+i,x2,y2+i);
   g.drawLine(x1,y1-i,x2,y2-i);
  }
  //14-7-1998: added the following to have rounded line edges
  g.fillOval(x1-penSize/2+1,y1-penSize/2+1,penSize-2,penSize-2);
  g.fillOval(x2-penSize/2+1,y2-penSize/2+1,penSize-2,penSize-2);
}

void fatRect(int x,int y,int w,int h){
  g.drawRect(x,y,w,h); //if <0 ??? 
  for(int i=0;i<(penSize/2);i++) {
   g.drawRect(x-i,y-i,w+2*i,h+2*i); //if <0 ???
   g.drawRect(x+i,y+i,w-2*i,h-2*i); //if <0 ???
  }
}

void fatOval(int x,int y,int w,int h){
//  System.out.println(x+" "+y+" "+w+" "+h);
  g.drawOval(x,y,w,h); //if <0 ??? 
  for(int i=0;i<(penSize/2);i++) {
   g.drawOval(x-i,y-i,w+2*i,h+2*i); //if <0 ???
   g.drawOval(x+i,y+i,w-2*i,h-2*i); //if <0 ???
  }
}

///////////////////////////////////////////////////////////////////////

 public void LineTo(int x,int y){
//  System.out.println("Graphix::LineTo");
  if (firstPoint) {lastx=x; lasty=y;firstPoint=false;}
  if (g!=null) fatLine(lastx,lasty,x,y); //g.drawLine(lastx,lasty,x,y);
  moveTo(x,y);
 }

 public void Line(int x1,int y1, int x2,int y2){
  moveTo(x1,y1);
  LineTo(x2,y2);
 }

///////////////////////////////////////////////////////////////////////

 public static Rectangle xyxy2rect(int x1,int y1,int x2,int y2){ //17-8-1998: now is static and returns a Rectangle object
 //result is returned in _x,_y,_width,_height vars of this class instance
 //System.out.println("("+x1+","+y1+")-("+x2+","+y2+")");
  int dx,dy;
  dx=x2-x1;
  dy=y2-y1;
  Rectangle r=new Rectangle(); //17-8-1998
  r.x=(dx<0)?x2:x1;
  r.y=(dy<0)?y2:y1;
  r.width=Math.abs(dx);  //don't use +1 (non-filled shapes get one more column)
  r.height=Math.abs(dy); //don't use +1 (non-filled shapes get one more row)
  return r;
 }

 //(x1,y1)=one corner, (x2,y2)=opposite corner
 public void Rectangle(int x1,int y1, int x2,int y2){
  //moveTo((x1+x2)/2,(y1+y2)/2);
  moveTo(x2,y2);
  if (g!=null){
   Rectangle r=xyxy2rect(x1,y1,x2,y2);
   if (filled){
    g.setColor(fillColor);
    g.fillRect(r.x,r.y,r.width,r.height);
    g.setColor(fgrColor);}
//   g.drawRect(_x,_y,_width,_height); //in fact it's a row,col bigger than the filled rect
    fatRect(r.x,r.y,r.width,r.height);
  }
 }

 public void Rect(int x1,int y1, int x2,int y2){ //used by CanvasSelection (no fat, always fill)
  //moveTo((x1+x2)/2,(y1+y2)/2);
  moveTo(x2,y2);
  if (g!=null){
   Rectangle r=xyxy2rect(x1,y1,x2,y2);
   g.setColor(fillColor);
   g.fillRect(r.x,r.y,r.width,r.height); //always filled
   g.setColor(fgrColor);
   g.drawRect(r.x,r.y,r.width,r.height); //in fact it's a row,col bigger than the filled rect
  }
 }

///////////////////////////////////////////////////////////////////////

 public int sign(int x){
  return (x>0)?1:((x<0)?-1:0);
 }

 public void Square(int x1,int y1, int x2,int y2){
  int dx=x2-x1;
  int dy=y2-y1;
  int d=Math.max(Math.abs(dx),Math.abs(dy)); //8-1-1998: fixed-bug: d must be a non-negative value
  Rectangle(x1,y1, x1+d*sign(dx),y1+d*sign(dy)); //need the *sign, else won't draw when cur point is left or above than the point we started dragging from
 }

///////////////////////////////////////////////////////////////////////


/*
 public void Oval(int x1,int y1, int x2,int y2){
  //moveTo((x1+x2)/2,(y1+y2)/2);
  moveTo(x2,y2);
  if (g!=null){
   Rectangle r=xyxy2rect(x1,y1,x2,y2);
   g.setColor(fgrColor); //23-12-1998: moved here
   if (filled){
    int ps2=penSize/2;
    if (penSize!=1)
     g.fillOval(r.x-ps2,r.y-ps2,r.width+2*ps2,r.height+2*ps2); //23-12-1998: now better fat outline at filled ovals
    else {
     ps2=1;
     g.drawOval(r.x,r.y,r.width,r.height); //in fact it's a row,col bigger than the filled rect
    }
    g.setColor(fillColor);
    g.fillOval(r.x+ps2,r.y+ps2,r.width-2*ps2,r.height-2*ps2);
   }
   else //23-12-1998
//  g.drawOval(r.x,r.y,r.width,r.height); //in fact it's a row,col bigger than the filled rect
    fatOval(r.x,r.y,r.width,r.height);
  }
 }
 */

 public void Oval(int x1,int y1, int x2,int y2){
  //moveTo((x1+x2)/2,(y1+y2)/2);
  moveTo(x2,y2);
  if (g!=null){
   Rectangle r=xyxy2rect(x1,y1,x2,y2);
   if (filled){
    g.setColor(fillColor);
    g.fillOval(r.x,r.y,r.width,r.height);
    g.setColor(fgrColor);}
//   g.drawOval(r.x,r.y,r.width,r.height); //in fact it's a row,col bigger than the filled rect
    fatOval(r.x,r.y,r.width,r.height);
  }
 }

 public void ClearRect(int x1,int y1,int x2,int y2){
//   System.out.println("ClearRect");
  if (g!=null) {
   setPAINT();
   Rectangle r=xyxy2rect(x1,y1,x2,y2);
   g.setColor(bkgrColor);
   g.fillRect(r.x,r.y,r.width+1,r.height+1); //xyxy2rect returns (1,1) less
   g.setColor(fgrColor);
  }
 }
 
 public void clearAll(){ClearRect(0,0,MaxX,MaxY);}
 
 public void clear(Color c){
  if (g!=null) {
   setPAINT();
   g.setColor(c);
   g.fillRect(0,0,MaxX+1,MaxY+1);
   g.setColor(fgrColor);
  }
 }

///////////////////////////////////////////////////////////////////////

/*
 //(cx,cy)=center, R=radius
 public void Circle(int cx,int cy, int R){
  moveTo(cx,cy);
  if (g!=null){
   g.setColor(fgrColor); //23-12-1998: moved here
   if (filled) {
    if (penSize!=1)
     int ps2=penSize/2;
     g.fillOval(cx-R-ps2,cy-R-ps2, 2*R+2*ps2,2*R+2*ps2); //23-12-1998: now better fat outline at circles
    else {
     ps2=1;
     g.drawOval(cx-R,cy-R,2*R,2*R);     
    }
    g.setColor(fillColor);
    g.fillOval(cx-R+ps2,cy-R+ps2, 2*R-2*ps2,2*R-2*ps2);
   } else //23-12-1998
   fatOval(cx-R,cy-R, 2*R,2*R); //g.drawOval(...)
  }
 }
 */

 //(cx,cy)=center, R=radius
 public void Circle(int cx,int cy, int R){
  moveTo(cx,cy);
  if (g!=null){
   if (filled) {
    g.setColor(fillColor);
    g.fillOval(cx-R,cy-R, 2*R,2*R);
    g.setColor(fgrColor);}
   fatOval(cx-R,cy-R, 2*R,2*R); //g.drawOval(...)
  }
 }

 //(cx,cy)=center, (rx,ry)=point on circumference
 public void Circle(int cx,int cy, int rx,int ry){
  int dx=cx-rx;
  int dy=cy-ry;
  Circle(cx,cy,(int)Math.round(Math.sqrt(dx*dx+dy*dy)));
 }

/*//////////////////////////////////////////////////////////////////////

 //(cx,cy)=center, Rx=Xrange, Ry=Yrange
 public void Ellipse(int cx,int cy,int Rx,int Ry){ //same as Oval for now: removed
  moveTo(cx,cy);
  if (g!=null){
   if (filled){
    g.setColor(fillColor); 
    g.fillOval(cx-Rx,cy-Ry, 2*Rx,2*Ry);
    g.setColor(fgrColor);}
   fatOval(cx-Rx,cy-Ry, 2*Rx,2*Ry); //g.drawOval(...)
  }
 }

 //(cx1,cy1)=center1, (cx2,cy2)=center2, (rx,ry)=point on circumference
 public void Ellipse(int cx1,int cy1, int cx2,int cy2, int rx,int ry){
  //?
 }

*///////////////////////////////////////////////////////////////////////

 //(cx,cy)=center, (ex,ey)=an edge, edges=#edges
 public void CanonicalPolygon(int cx,int cy, int ex,int ey, int edges){ //16-7-1998
  Polygon(calcCanonicalPolygon(cx,cy,ex,ey,edges));
  moveTo(cx,cy); //place after Polygon call: in case Polygon calls moveTo by its own (extra safety: it doesn't call moveTo)
 }
 
 public void Polygon(Polygon p){
  //no moveTo!!!
  if (g!=null){
   if (filled){
    g.setColor(fillColor); 
    g.fillPolygon(p);
    g.setColor(fgrColor);}
   g.drawPolygon(p); //fatPolygon(cx,cy,ex,ey,edges);
  }
 }

///////////////////////////////////////////////////////////////////////

/*
 class myImageObserver implements ImageObserver{ //4-9-1998: doesn't work!!!
  boolean noexitflag=false;
  int info;
  
  public myImageObserver(int infoflags){
   info=infoflags|ImageObserver.ERROR|ImageObserver.ABORT;}
  public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height){
   noexitflag=((infoflags & info)==0);
   return noexitflag;
  }
  public void waitImage(){while(noexitflag);}
 }
 */

 public void drawImage(Image i,int x,int y){
  if (g!=null){
   //myImageObserver o;  
   if (i!=null) //12-1-1999: added check for null image
    g.drawImage(i,x,y,null/*o=new myImageObserver(ImageObserver.ALLBITS)*/);
   //o.waitImage();
  }
 }

 public void drawImage(Image i,int x1,int y1, int x2,int y2){
  if (g!=null) {
   //myImageObserver o;
   Rectangle r=xyxy2rect(x1,y1,x2,y2); //returns a width,height -(1,1)
   g.drawImage(i,r.x,r.y,r.width+1,r.height+1,null/*o=new myImageObserver(ImageObserver.ALLBITS)*/); //+(1,1) needed
      //4-9-1998: fixed bug -- had changed xyxy2rect to return a rectangle and here was still using the old globals _x,_y,_width,_height
   //o.waitImage();
  }
 }

 public void setFont(Font f){ //19Aug1999
  if(g!=null) g.setFont(f);
 }

 public void drawString(String s,int x,int y){
  if (g!=null){
   g.drawString(s,x,y);
  }
 }

 public void drawString(String s,Font f,int x,int y){ //19Aug1999
  if (g!=null){
   g.setFont(f);
   g.drawString(s,x,y);
  }
 }

///////////////////////////////////////////////////////////////////////

public void copy(int x1,int y1,int x2,int y2){
 try{
  Rectangle r=xyxy2rect(x1,y1,x2,y2);
  ImageFilter filter=new CropImageFilter(r.x,r.y,r.width+1,r.height+1);
 
  Toolkit toolkit=Toolkit.getDefaultToolkit();
 // filter.setColorModel(colorModel/*toolkit.getColorModel()*/); //else we'll get monochrome pastes in 32bit color
  ImageProducer producer=new FilteredImageSource(
   img.getSource(),filter);
  if (Clipboard!=null) Clipboard.flush(); //11-9-1998: free memory taken up by the clipboard's image!
  Clipboard=toolkit.createImage(producer);
 }catch(Exception e){System.out.println("Couldn't copy to clipboard");}
 paste(x1,y1);
}

public void cut(int x1,int y1,int x2,int y2){ //25-8-1998
 copy(x1,y1,x2,y2);
 ClearRect(x1,y1,x2,y2);
}

public void paste(int x,int y){
 if (Clipboard!=null) drawImage(Clipboard,x,y); //12-1-1999: now checking for empty Clipboard
}

//paste stretched
public void paste(int x1,int y1,int x2,int y2){
 if (Clipboard!=null) drawImage(Clipboard,x1,y1,x2,y2); //12-1-1999: now checking for empty Clipboard
}

///////////////////////////////////////////////////////////////////////

 public boolean stopFilling=false;
 public Color boundary,fill;
 public JComponent component;
 
 //MyFourFiller constants
 final static int FILL_ARROUND=0;
 final static int NO_LEFT_FILL=1;
 final static int NO_RIGHT_FILL=2;
 final static int NO_UP_FILL=3;
 final static int NO_DOWN_FILL=4;
 
 public void FourFill(JComponent c,int x,int y,Color boundary,boolean replace){
  this.component=c;
  stopFilling=false;
//  Color old=getColor();
  int penSizeOld=penSize; penSize=1;
  if (!replace){
   //patch-start// -- getPoint depends on the device (so put the needed colors and read them again)    
   g.setColor(boundary);
   Point(x,y);
   component.paintImmediately(x,y,1,1); //!!!
  }   
  this.boundary=getPoint(x,y);
  if(!replace){
   //   
   g.setColor(fillColor);
   Point(x,y);
   component.paintImmediately(x,y,1,1); //!!!
   this.fill=getPoint(x,y);
   //patch-end//
   g.setColor(bkgrColor); //?????????
   Point(x,y); //patch//
   component.paintImmediately(x,y,1,1); //!!!
//  four_fill2(x,y,FILL_ARROUND);
  four_fill(x,y);
  }else four_replace(x,y);
  g.setColor(fgrColor); //restore the graphics color to the graphix fgrColor
  //release the global objects//
  this.component=null;
  this.boundary=null;
  this.fill=null;
  penSize=penSizeOld;
 }
    
  void four_fill2(int x,int y,int flag){
   if (stopFilling ||x<0 || y<0 || x>MaxX || y>MaxY) return;
   Color color=getPoint(x,y);
   if (!color.equals(boundary)/* && !color.equals(fill)*/){
    g.setColor(fillColor);
    Point(x,y);
//    System.out.println("("+x+","+y+")");
    component.paintImmediately(x,y,1,1); //!!!
    if (flag!=NO_RIGHT_FILL) four_fill2(x+1,y,NO_LEFT_FILL);
    if (flag!=NO_LEFT_FILL) four_fill2(x-1,y,NO_RIGHT_FILL);
    if (flag!=NO_DOWN_FILL) four_fill2(x,y+1,NO_UP_FILL);
    if (flag!=NO_UP_FILL) four_fill2(x,y-1,NO_DOWN_FILL);
   }
  }

  void four_fill(int x,int y){
   if (stopFilling ||x<0 || y<0 || x>MaxX || y>MaxY) return;
   Color color=getPoint(x,y);
   if (!color.equals(boundary) && !color.equals(fill)){ //can't use fillColor check due to the above patch
    g.setColor(fillColor);
    Point(x,y);
 //   System.out.println("("+x+","+y+")");
    component.paintImmediately(x,y,1,1); //!!!
    four_fill(x+1,y);
    four_fill(x-1,y);
    four_fill(x,y+1);
    four_fill(x,y-1);
   }
  }

  void four_replace(int x,int y){
   if (stopFilling ||x<0 || y<0 || x>MaxX || y>MaxY) return;
   Color color=getPoint(x,y);
   if (color.equals(boundary)){//boundary is a bad name (just replacing this color)
    g.setColor(fillColor);
    Point(x,y);
 //   System.out.println("("+x+","+y+")");
    component.paintImmediately(x,y,1,1); //!!!
    four_fill(x+1,y);
    four_fill(x-1,y);
    four_fill(x,y+1);
    four_fill(x,y-1);
   }
  }

////////////////////////

 public static int sqr(int x){return x*x;} //1Mar1999: moved to Graphix class

 //(cx,cy)=center, (ex,ey)=an edge, edges=#edges(>0)
 public static Polygon calcCanonicalPolygon(int cx,int cy,int x,int y,int edges){ //1Mar1999: moved to Graphix class
  int[] xpoints=new int[edges];
  int[] ypoints=new int[edges];
  double angleoffs=Math.atan2(y-cy,x-cx); //angle(cx,cy,x,y);
  double R=Math.sqrt(sqr(x-cx)+sqr(y-cy));
  double anglestep=Math.PI*2/edges;
  double angle;
  for(int i=0;i<edges;i++){
   angle=i*anglestep+angleoffs;
   xpoints[i]=(int)(cx+Math.cos(angle)*R);
   ypoints[i]=(int)(cy+Math.sin(angle)*R);
  }
  return new Polygon(xpoints,ypoints,edges);
 }

}