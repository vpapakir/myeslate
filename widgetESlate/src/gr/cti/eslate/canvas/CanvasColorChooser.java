package gr.cti.eslate.canvas;

import gr.cti.eslate.sharedObject.PaletteColorChange;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CanvasColorChooser extends CustomCanvasTool{
    
 public String getToolName(){return "COLOR CHOOSER";};
 public PaletteColorChange colors=null;
 public Canvas canvas;

 public CanvasColorChooser(Canvas canvas,PaletteColorChange colors){
  super();
  this.colors=colors;
  this.canvas=canvas;
}

 public void mousePressed(MouseEvent e){ //MouseListener//
 // System.out.println("ColorChooser:mousePressed");
   Color color=gx.getPoint(e.getX(),e.getY()); //WARNING: this depends on the display device's color delpth

  if (SwingUtilities.isRightMouseButton(e)){ //18-8-1998
   c.getCanvas().wsp.setFillColor(color); //27-1-1999: right click now sets fill color instead of background color
   try{colors.set_fillColor(color);}catch(Exception ex){} //27-1-1999: won't work, must make Canvas a color provider
  }else{
   c.getCanvas().wsp.setForegroundColor(color);
   try{colors.set_fgrColor(color);}catch(Exception ex){} //27-1-1999: won't work, must make Canvas a color provider
  }

/* //27-1-1999: now just setting fgr&fill color depending on left/right click
  try{
   //System.out.println(color);
   colors.set_fgrcolor(canvas,color); //depends on other end what it shall do with it
  }catch(Exception ex){
   //System.err.println("Note: 'Colors' pin is disconnected"); //in case we are disconnected
  }
*/

 }

 public void setPaletteColorChangeObject(PaletteColorChange colors){this.colors=colors;}
}
