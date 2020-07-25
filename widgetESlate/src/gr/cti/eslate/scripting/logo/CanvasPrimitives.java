//add: bringToFront "name command and sendToBack command

//use "Canvas." at primitive names start?

// 21-8-1998: Created by George Birbilis
// 25-8-1998: Added more primitives (LINETO,RECTANGLE,OVAL,TEXT,CANONICALPOLYGON,COPY,PASTE,CUT,CLEAR)
// 28-8-1998: Added STAMP
// 30-8-1998: Added SETPENSIZE
//  2-9-1998: added lots of primitives
//  5-9-1998: Added MOVETO
//17-10-1998: Renamed POINT to PLOTPIXEL
//            Added PIXELCOLOR, CANVASPAINTMODE, CANVASXORMODE, (CIRCLE cx cy rx ry)
//20-10-1998: Added SPRAY
//3Jun1999: bug-fix: was overriding getWidth/getHeight of Canvas component
//2Jul1999: added Square primitive
//15Oct1999: in CanvasPageWidth&CanvasPageHeight's error messages it was saying "Turtle" instead of "Canvas"

package gr.cti.eslate.scripting.logo; /**/

import java.awt.*;
import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.logo.convertions.*; //for LogoAWT
import gr.cti.eslate.scripting.AsCanvas; /**/

public class CanvasPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //25May1999: new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("PLOTPIXEL", "pPLOTPIXEL", 2); //17-10-1998: renamed from POINT
        registerPrimitive("PIXELCOLOR","pPIXELCOLOR", 2); //17-10-1998
        registerPrimitive("MOVETO", "pMOVETO",2); //5-9-1998
        registerPrimitive("LINE", "pLINE", 4);
        registerPrimitive("LINETO","pLINETO",2);
        registerPrimitive("RECTANGLE", "pRECTANGLE",4);
        registerPrimitive("SQUARE", "pSQUARE",4); //2Jul1999
        registerPrimitive("CIRCLE", "pCIRCLE", 3); //17-10-1998: default 3 params (now extra use with 4 params)
        registerPrimitive("OVAL","pOVAL",4);
        registerPrimitive("TEXT","pTEXT",3);
        registerPrimitive("CANONICALPOLYGON","pCANONICALPOLYGON",5);
        registerPrimitive("SPRAY","pSPRAY",2); //20-10-1998
        registerPrimitive("COPY","pCOPY",4);
        registerPrimitive("PASTE","pPASTE",2);
        registerPrimitive("CUT","pCUT",4);
        registerPrimitive("CLEAR","pCLEAR",4);
        registerPrimitive("STAMP","pSTAMP",0);
        registerPrimitive("SETPENSIZE","pSETPENSIZE",1);
        registerPrimitive("SETCANVASPAGESIZE", "pSETCANVASPAGESIZE", 2);
        registerPrimitive("CANVASPAGEWIDTH", "pCANVASPAGEWIDTH", 0);
        registerPrimitive("CANVASPAGEHEIGHT", "pCANVASPAGEHEIGHT", 0);
        registerPrimitive("CLEARCANVASPAGE", "pCLEARCANVASPAGE", 0);

        registerPrimitive("SETCANVASFOREGROUND", "pSETCANVASFOREGROUND", 1); /**/
        registerPrimitive("CANVASFOREGROUND", "pCANVASFOREGROUND", 0);       /**/
        registerPrimitive("SETCANVASBACKGROUND", "pSETCANVASBACKGROUND", 1); /**/
        registerPrimitive("CANVASBACKGROUND", "pCANVASBACKGROUND", 0);       /**/
        registerPrimitive("SETCANVASFILL", "pSETCANVASFILL", 1); /**/
        registerPrimitive("CANVASFILL", "pCANVASFILL", 0);       /**/

        registerPrimitive("CANVASPAINTMODE", "pCANVASPAINTMODE", 0); //17-10-1998
        registerPrimitive("CANVASXORMODE", "pCANVASXORMODE", 0); //17-10-1998

        registerPrimitive("CanvasPage.ENABLEREFRESH","pENABLEREFRESH",0); //14Oct1999
        registerPrimitive("CanvasPage.DISABLEREFRESH","pDISABLEREFRESH",0); //14Oct1999

        registerPrimitive("NEWDRAWINGPAGE","pNEWDRAWINGPAGE",0); //1Nov199
        registerPrimitive("NEWTURTLEPAGE","pNEWTURTLEPAGE",0); //1Nov199

        myMachine=(MyMachine)machine; //25May1999: new-scripting-mechanism

        if(console != null)
            console.putSetupMessage("Loaded Avakeeo's Canvas component primitives"); //31-8-1998: now say Canvas (instead of wrongly saying Palette)
    }

//utility method//

 private Vector<?> getKnownObjects(){ //1Nov1999
  return myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.scripting.AsCanvas.class);
 }

//PLOTPIXEL//

    private void point(final AsCanvas c, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.point(x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.point(x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pPLOTPIXEL(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
       testNumParams(alogoobject,2);

       int x=alogoobject[0].toInteger();
       int y=alogoobject[1].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         point((AsCanvas)v.elementAt(i), x, y);
       }
       return LogoVoid.obj;
    }

//PIXELCOLOR//

   public final LogoObject pPIXELCOLOR(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
      testNumParams(alogoobject,2);
      int x=alogoobject[0].toInteger();
      int y=alogoobject[1].toInteger();
      Vector<?> v=getKnownObjects();
      try{
       Color color = ((AsCanvas)v.firstElement()).getPoint(x, y);
       return LogoAWT.toLogoObject(color);
      }catch(NoSuchElementException e){throw new LanguageException("There is no CANVAS to TELL this to");}
    }

//MOVETO//

    private void moveTo(final AsCanvas c, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.moveTo(x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.moveTo(x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pMOVETO(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
       testNumParams(alogoobject,2);

       int x=alogoobject[0].toInteger();
       int y=alogoobject[1].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         moveTo((AsCanvas)v.elementAt(i), x, y);
       }
       return LogoVoid.obj;
    }

//LINE//

    private void line
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.line(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.line(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pLINE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         line((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//LINETO//

    private void lineTo(final AsCanvas c, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.lineTo(x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.lineTo(x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pLINETO(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
       testNumParams(alogoobject,2);

       int x=alogoobject[0].toInteger();
       int y=alogoobject[1].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         lineTo((AsCanvas)v.elementAt(i), x, y);
       }
       return LogoVoid.obj;
    }

//RECTANGLE//

    private void rectangle
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.rectangle(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.rectangle(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pRECTANGLE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         rectangle((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//SQUARE//

    private void square
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.square(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.square(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSQUARE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         square((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//CIRCLE//

    private void circle
      (final AsCanvas c, final int cx, final int cy, final int r)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.circle(cx, cy, r);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.circle(cx, cy, r);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    private void circle
      (final AsCanvas c, final int cx, final int cy, final int rx, final int ry)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.circle(cx, cy, rx, ry);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.circle(cx, cy, rx, ry);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pCIRCLE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
     testNumParams(alogoobject,3,4); //change testMinParams(...,3) & testMaxParams(...,4) into testNumParams(...,3,4)

     Vector<?> v=getKnownObjects();

     int cx=alogoobject[0].toInteger();
     int cy=alogoobject[1].toInteger();

     if(alogoobject.length == 3)
     {
      int r=alogoobject[2].toInteger();
      for(int i=0;i<v.size();i++) {
        circle((AsCanvas)v.elementAt(i), cx, cy, r);
      }
     }
     else //if (alogoobject.length==4)
     {
      int rx=alogoobject[2].toInteger();
      int ry=alogoobject[3].toInteger();
      for(int i=0;i<v.size();i++) {
        circle((AsCanvas)v.elementAt(i), cx, cy, rx, ry);
      }
     }

     return LogoVoid.obj;
    }

//OVAL//

    private void oval
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.oval(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.oval(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pOVAL(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         oval((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//TEXT//

    private void drawString
      (final AsCanvas c, final String text, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.drawString(text, x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.drawString(text, x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,3);

       String text=alogoobject[0].toString();
       int x=alogoobject[1].toInteger();
       int y=alogoobject[2].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         drawString((AsCanvas)v.elementAt(i), text, x, y);
       }
       return LogoVoid.obj;
    }

//CANONICALPOLYGON//

    private void canonicalPolygon
      (final AsCanvas c, final int cx, final int cy, final int ex,
       final int ey, final int edges)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.canonicalPolygon(cx, cy, ex, ey, edges);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.canonicalPolygon(cx, cy, ex, ey, edges);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pCANONICALPOLYGON(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,5);

       int cx=alogoobject[0].toInteger();
       int cy=alogoobject[1].toInteger();
       int ex=alogoobject[2].toInteger();
       int ey=alogoobject[3].toInteger();
       int edges=alogoobject[4].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         canonicalPolygon((AsCanvas)v.elementAt(i), cx, cy, ex, ey, edges);
       }
       return LogoVoid.obj;
    }

//SPRAY// --- //20-10-1998

    private void spray(final AsCanvas c, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.spray(x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.spray(x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSPRAY(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
       testNumParams(alogoobject,2);

       int x=alogoobject[0].toInteger();
       int y=alogoobject[1].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         spray((AsCanvas)v.elementAt(i), x, y);
       }
       return LogoVoid.obj;
    }

//COPY//

    private void copy
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.copy(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.copy(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pCOPY(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         copy((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//PASTE//

    private void paste(final AsCanvas c, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.paste(x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.paste(x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pPASTE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,2);

       int x=alogoobject[0].toInteger();
       int y=alogoobject[1].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         paste((AsCanvas)v.elementAt(i), x, y);
       }
       return LogoVoid.obj;
    }

//CUT//

    private void cut
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.cut(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.cut(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pCUT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         cut((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//CLEAR//

    private void clear
      (final AsCanvas c, final int x1, final int y1, final int x2, final int y2)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.clear(x1, y1, x2, y2);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.clear(x1, y1, x2, y2);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pCLEAR(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,4);

       int x1=alogoobject[0].toInteger();
       int y1=alogoobject[1].toInteger();
       int x2=alogoobject[2].toInteger();
       int y2=alogoobject[3].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         clear((AsCanvas)v.elementAt(i), x1, y1, x2, y2);
       }
       return LogoVoid.obj;
    }

//STAMP//

    private void stamp(final AsCanvas c)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.stamp();
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.stamp();
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSTAMP(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         stamp((AsCanvas)v.elementAt(i));
       }
       return LogoVoid.obj;
    }

//SETPENSIZE//

    private void setPenSize(final AsCanvas c, final int size)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setPenSize(size);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setPenSize(size);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSETPENSIZE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);
       int size=alogoobject[0].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setPenSize((AsCanvas)v.elementAt(i), size);
       }
       return LogoVoid.obj;
    }

//SETCANVASPAGESIZE//

    private void setSize(final AsCanvas c, final int x, final int y)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setSize(x, y);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setSize(x, y);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSETCANVASPAGESIZE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
       testNumParams(alogoobject,2);

       int x=alogoobject[0].toInteger();
       int y=alogoobject[1].toInteger();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setSize((AsCanvas)v.elementAt(i), x, y);
       }
       return LogoVoid.obj;
    }

//CANVASPAGEWIDTH//

    public final LogoObject pCANVASPAGEWIDTH(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
      testNumParams(alogoobject,0);

      Vector<?> v=getKnownObjects();
      try{return new LogoWord(((AsCanvas)v.firstElement()).getPageWidth());
      }catch(NoSuchElementException e){throw new LanguageException("There is no Canvas to TELL this");}
    }

//CANVASPAGEHEIGHT//

    public final LogoObject pCANVASPAGEHEIGHT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
      testNumParams(alogoobject,0);

      Vector<?> v=getKnownObjects();
      try{return new LogoWord(((AsCanvas)v.firstElement()).getPageHeight());
      }catch(NoSuchElementException e){throw new LanguageException("There is no Canvas to TELL this");}
    }

//CLEARCANVASPAGE//

    private void clearAll(final AsCanvas c) throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.clearAll();
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.clearAll();
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pCLEARCANVASPAGE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
            throws LanguageException
    {
       testNumParams(alogoobject,0);

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         clearAll((AsCanvas)v.elementAt(i));
       }
       return LogoVoid.obj;
    }

/////

//CANVASFOREGROUND//

    private void setForegroundColor(final AsCanvas c, final Color color)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setForegroundColor(color);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setForegroundColor(color);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSETCANVASFOREGROUND(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);
       Color color = LogoAWT.toColor(alogoobject[0]); //may throw a LanguageException
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setForegroundColor((AsCanvas)v.elementAt(i), color);
       }
       return LogoVoid.obj;
    }

    public final LogoObject pCANVASFOREGROUND(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
      testNumParams(alogoobject,0);
      Color color;
      Vector<?> v=getKnownObjects();
      try{color=((AsCanvas)v.firstElement()).getForegroundColor();
      }catch(NoSuchElementException e){throw new LanguageException("There is no CANVAS to TELL this to");}
      return LogoAWT.toLogoObject(color);
    }

//CANVASBACKGROUND//

    private void setBackgroundColor(final AsCanvas c, final Color color)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setBackgroundColor(color);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setBackgroundColor(color);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSETCANVASBACKGROUND(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);
       Color color = LogoAWT.toColor(alogoobject[0]); //may throw a LanguageException
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setBackgroundColor((AsCanvas)v.elementAt(i), color);
       }
       return LogoVoid.obj;
    }

    public final LogoObject pCANVASBACKGROUND(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
      testNumParams(alogoobject,0);
      Color color;
      Vector<?> v=getKnownObjects();
      try{color=((AsCanvas)v.firstElement()).getBackgroundColor();
      }catch(NoSuchElementException e){throw new LanguageException("There is no CANVAS to TELL this to");}
      return LogoAWT.toLogoObject(color);
    }

//CANVASFILL//

    private void setFillColor(final AsCanvas c, final Color color)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setFillColor(color);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setFillColor(color);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pSETCANVASFILL(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);
       Color color = LogoAWT.toColor(alogoobject[0]); //may throw a LanguageException
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setFillColor((AsCanvas)v.elementAt(i), color);
       }
       return LogoVoid.obj;
    }

    public final LogoObject pCANVASFILL(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
      testNumParams(alogoobject,0);
      Color color;
      Vector<?> v=getKnownObjects();
      try{color=((AsCanvas)v.firstElement()).getFillColor();
      }catch(NoSuchElementException e){throw new LanguageException("There is no CANVAS to TELL this to");}
      return LogoAWT.toLogoObject(color);
    }
    
//CANVASPAINTMODE//

    private void setPAINT(final AsCanvas c) throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setPAINT();
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setPAINT();
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

   public final LogoObject pCANVASPAINTMODE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
   {
    testNumParams(alogoobject,0);
    Vector<?> v=getKnownObjects();
    for(int i=0;i<v.size();i++) {
      setPAINT((AsCanvas)v.elementAt(i));
    }
    return LogoVoid.obj;
   }

//CANVASXORMODE//   

    private void setXOR(final AsCanvas c) throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setXOR();
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setXOR();
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

   public final LogoObject pCANVASXORMODE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
   {
    testNumParams(alogoobject,0);
    Vector<?> v=getKnownObjects();
    for(int i=0;i<v.size();i++) {
      setXOR((AsCanvas)v.elementAt(i));
    }
    return LogoVoid.obj;
   }

//REFRESH//

    private void setRefreshEnabled(final AsCanvas c, final boolean enabled)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.setRefreshEnabled(enabled);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.setRefreshEnabled(enabled);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pENABLEREFRESH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,0);
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setRefreshEnabled((AsCanvas)v.elementAt(i), true);
       }
       return LogoVoid.obj;
    }

    //

    public final LogoObject pDISABLEREFRESH(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) {
         setRefreshEnabled((AsCanvas)v.elementAt(i), false);
       }
       return LogoVoid.obj;
    }

//NEWDRAWINGPAGE//

    private void newDrawingPage(final AsCanvas c)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.newDrawingPage();
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.newDrawingPage();
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    private void newDrawingPage(final AsCanvas c, final String name)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.newDrawingPage(name);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.newDrawingPage(name);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    public final LogoObject pNEWDRAWINGPAGE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0,1);

       Vector<?> v=getKnownObjects();

       if(alogoobject.length == 1) {
         String name=alogoobject[0].toString();
         for(int i=0;i<v.size();i++) {
           newDrawingPage((AsCanvas)v.elementAt(i), name);
         }
       }else{
         for(int i=0;i<v.size();i++) {
           newDrawingPage((AsCanvas)v.elementAt(i));
         }
       }
       return LogoVoid.obj;
    }

//NEWTURTLEPAGE//

    private void newTurtlePage(final AsCanvas c)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.newTurtlePage();
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.newTurtlePage();
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }

    private void newTurtlePage(final AsCanvas c, final String name)
      throws LanguageException
    {
      if (EventQueue.isDispatchThread()) {
        c.newTurtlePage(name);
      }else{
        try {
          EventQueue.invokeAndWait(new Runnable()
          {
            public void run()
            {
              c.newTurtlePage(name);
            }
          });
        } catch (Exception e) {
          throw new LanguageException(e);
        }
      }
    }


    public final LogoObject pNEWTURTLEPAGE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0,1);

       Vector<?> v=getKnownObjects();

       if(alogoobject.length == 1) {
         String name=alogoobject[0].toString();
         for(int i=0;i<v.size();i++) {
           newTurtlePage((AsCanvas)v.elementAt(i), name);
         }
       }else{
         for(int i=0;i<v.size();i++) {
           newTurtlePage((AsCanvas)v.elementAt(i));
         }
       }
       return LogoVoid.obj;
    }

}
