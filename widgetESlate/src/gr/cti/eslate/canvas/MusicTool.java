package gr.cti.eslate.canvas;

import quicktime.QTSession;
import quicktime.QTException;
import quicktime.std.music.NoteAllocator;
import quicktime.std.music.NoteChannel;
import quicktime.std.StdQTException;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

public class MusicTool extends CustomCanvasTool{

 public MusicTool(){
    super();
    try{QTSession.open();
 	    try {
	        na = new NoteAllocator();
            nc = new NoteChannel(na, 25);
        } catch (Exception er) {

	            System.out.println("Cannot create noteChannel");
	        }

 }catch(Exception e){System.err.println("QuickTime init error!!!");}
}
 
 public String getToolName(){return "MUSICTOOL";}

 public void reset(){QTSession.close();}

 public void mouseEntered(MouseEvent e){c.setCursor(new Cursor(Cursor.HAND_CURSOR));}
 public void mouseExited(MouseEvent e){c.setCursor(new Cursor(Cursor.HAND_CURSOR)); }

 public void mouseClicked(MouseEvent e){
  if (SwingUtilities.isLeftMouseButton(e)) {
	            try{
	                notechannel --;
	                if (notechannel <0) {
	                    notechannel = 0;
	                }
	                nc = new NoteChannel(na, notechannel);
	            } catch (StdQTException ed) {
	                System.out.println("Cannot change instrument");
	            }
	            catch (QTException es)
	            {
	                System.out.println("Cannot create noteChannel");
	            }
  }else{
	            try {
	                //nc.pickEditInstrument("Instrument",);
	                //notechannel++;
	                if (notechannel <=100)
	                {
	                    notechannel++;
	                }
	                else
	                    notechannel =1;
	                nc = new NoteChannel(na, notechannel);

	            } catch (StdQTException ed) {
	                System.out.println("Cannot change instrument");
	            }
	            catch (QTException es)
	            {
	                System.out.println("Cannot create noteChannel");
	            }
  }
 }

 public void mouseDragged(MouseEvent e){ //MouseMotionListener//
  position = e.getX();
  int vertical = e.getY();
  playRandom(position,vertical);
 }
   
/////

	int position;
	NoteChannel nc;
	int notechannel;
	NoteAllocator na;

    public void playRandom(int pos, int vert)
    {
//       System.out.println(pos+" "+vert);
        try {
            int x = (int)((128*pos)/(c.getWidth()));
            //nc.playNote(30+pos,127);
            nc.playNote(x,127);
            delay(na, 150+vert);
            //nc.playNote(30+pos,0);
            nc.playNote(x,0);
        }catch(QTException e) {
            System.out.println("Exception occurred in playRandom :"+e);
        }
    }
    
	static void delay (NoteAllocator na, long ms) {
		try { na.task(); Thread.sleep(ms); }
		catch (Exception e) { e.printStackTrace(); }
	}

}