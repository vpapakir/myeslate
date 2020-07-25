package gr.cti.eslate.scripting;

import java.awt.Color;
import gr.cti.eslate.canvas.DrawingPanel; //1Nov1999
import gr.cti.eslate.canvas.TurtlePanel; //1Nov1999

public interface AsCanvas{ //21-8-98: Created by George Birbilis

 public void point(int x,int y);
 public void line(int x1,int y1,int x2,int y2);

 public void lineTo(int x,int y);

 public void rectangle(int x1,int y1,int x2,int y2);
 public void square(int x1,int y1,int x2,int y2); //2Jul1999

 public void circle(int cx,int cy, int r);
 public void circle(int cx,int cy, int rx,int ry);
 public void oval(int x1,int y1,int x2,int y2);

 public void drawString(String s,int x,int y);

 public void canonicalPolygon(int cx,int cy, int ex,int ey, int edges);

 public void spray(int x,int y); //20-10-1998

 public void copy(int x1,int y1,int x2,int y2);
 public void paste(int x,int y);
 public void cut(int x1,int y1,int x2,int y2);
 public void clear(int x1,int y1,int x2,int y2);

 public void stamp();

 public void setPenSize(int s);

 public void setSize(int x,int y);
 public void clearAll();
 public void setForegroundColor(Color c);
 public void setBackgroundColor(Color c);
 public Color getFillColor();
 public Color getForegroundColor();
 public Color getBackgroundColor();
 public void setFillColor(Color c);
 public void setPAINT();
 public void setXOR();

 public Color getPoint(int x,int y);
 public int getPageWidth();
 public int getPageHeight();

 public void moveTo(int x,int y); //5-9-1998

 public void setRefreshEnabled(boolean enabled); //14Oct1999

 public DrawingPanel newDrawingPage(); //1Nov1999
 public DrawingPanel newDrawingPage(String name); //1Nov1999

 public TurtlePanel newTurtlePage(); //1Nov1999
 public TurtlePanel newTurtlePage(String name); //1Nov1999

}