package gr.cti.eslate.canvas;

public interface ICanvas{

 public void Action(int actionID);
 public CanvasPage getTopPanel();
 public TurtlePanel getTopMostTurtlePanel(); 
 
 final static int FILE_NEW_TURTLEPAGE=-1; //adding backwards...
 final static int FILE_NEW=0x00;
 final static int FILE_OPEN=0x01;
 final static int FILE_CLOSE=0x02;
 final static int FILE_SAVE=0x03;
 final static int FILE_PRINT=0x04;

 final static int EDIT_CUT=0x10;
 final static int EDIT_COPY=0x11;
 final static int EDIT_PASTE=0x12;
 final static int EDIT_CLEAR=0x13;
 final static int EDIT_SELECTALL=0x14;
 final static int EDIT_STAMP=0x15;
 final static int EDIT_FONT=0x16; //19Aug1999

/* //26-1-1999: removed: now using Actions for the ToolsToolBar
 final static int TOOL_RUBBER=0x20;
 final static int TOOL_TEXT=0x21;
 final static int TOOL_FILL=0x22;
 final static int TOOL_PEN=0x23;
 final static int TOOL_LINE=0x24;
 final static int TOOL_RECTANGLE=0x25;
 final static int TOOL_CIRCLE=0x26;
 final static int TOOL_OVAL=0x27;
 final static int TOOL_SPRAY=0x28;
 final static int TOOL_ZOOM=0x29;
 final static int TOOL_SELECTION=0x2A;
 final static int TOOL_COLORCHOOSER=0x2B;
 final static int TOOL_TURTLELINECHOOSER=0x2C;
 final static int TOOL_CANONICALPOLYGON=0x2D;
 final static int TOOL_SQUARE=0x2E;
 //...//
 final static int TOOL_MUSIC=0x2F; //5-9-1998
// final static int TOOL_HAND=0x2F; //???
*/

 final static int SETTINGS_FILLEDSHAPES=0x30;
 final static int SETTINGS_OPAQUE=0x31;
 final static int SETTINGS_PENSIZE=0x32;
 final static int SETTINGS_RUBBERSIZE=0x33; //23-12-1998
 final static int SETTINGS_TRACKS=0x34; //18-1-1999
 final static int SETTINGS_FGRCOLOR=0x35;
 final static int SETTINGS_BKGRCOLOR=0x36;
 final static int SETTINGS_FILLCOLOR=0x37;
 final static int SETTINGS_FONT=0x3F;

// final static int HELP_ABOUT=0x40; //not needed (Avakeeo component has its own help button)

 final static int PAGES=0x90; //base number for pages menu: 100+0,100+1,...

}