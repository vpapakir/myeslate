package gr.cti.eslate.canvas;

import java.awt.*;
import java.awt.event.*;

public class TurtleLineChooser extends CustomCanvasTool{

 public String getToolName(){return "TURTLE LINE CHOOSER";};

 public void mouseClicked(MouseEvent e){
  try {
   if (!(c instanceof TurtlePanel)){
    System.out.println("This isn't a Turtle Page! This tool only works with Turtle Pages");
    return;
   }
   TurtlePanel p=(TurtlePanel)c;
   LineGroup g=p.select(e.getX(),e.getY());
   if (g!=null){
    Toolkit.getDefaultToolkit().beep();    
    //System.out.println("Turtle Line Chooser found a line and beeped!");
    p.callSO.set_call(g.call); //pass null calls too (slider should clear its display for lines drawn by primitives executed alone at the top level)
   }
   else System.out.println("Please select a line (drawn by a LOGO procedure - not by any primitive calls you executed directly)");
  }catch(Exception ex){System.err.println(ex+"\nThis tool works only in Turtles' Pages! (used to select Turtle lines)");} //23-12-1998: printing exception message 
 }

}