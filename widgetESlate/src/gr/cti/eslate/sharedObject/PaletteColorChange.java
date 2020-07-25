package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;

import java.awt.*;

import gr.cti.eslate.base.ESlateHandle; //26Apr1999

/**
 * Palette shared object.
 * @version     5.0.0, 11-Jul-2006
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class PaletteColorChange extends SharedObject
{
 public Color color,fgrColor,fillColor,bkgrColor;

 public static final int NEW_FGR=1;
 public static final int NEW_FILL=2;
 public static final int NEW_BKGR=3;
 public static final int NEW_COLOR=4; //mainly used to set the current(=fgr,bkgr,fill) color on the palette

 public PaletteColorChange(ESlateHandle app,Color fgr,Color bkgr,Color fill){ //26Apr1999: changed ESlateComponent to ESlateHandle
  super(app);
  this.fgrColor=fgr;
  this.bkgrColor=bkgr;
  this.fillColor=fill;
 }

 //

 public void set_fillColor(Color fill){
  fillColor=fill;
  fireSharedObjectChanged(new SharedObjectEvent(this,NEW_FILL)); //26Apr1999: changed getHandle to getHandle //22May1999: using new SO constructor
 }

 public void set_fgrColor(Color fgr){
  fgrColor=fgr;
  fireSharedObjectChanged(new SharedObjectEvent(this,NEW_FGR)); //26Apr1999: changed getComponent to getHandle //22May1999: using new SO constructor
 }

 public void set_bkgrColor(Color bkgr){
  bkgrColor=bkgr;
  fireSharedObjectChanged(new SharedObjectEvent(this,NEW_BKGR)); //26Apr1999: changed getHandle to getHandle //22May1999: using new SO constructor
 }

 @SuppressWarnings(value={"deprecation"})
 public void set_color(ESlateHandle sender,Color c){ //26Apr1999: changed ESlateComponent to ESlateHandle
  System.out.println(c);
  color=c;
  fireSharedObjectChanged(new SharedObjectEvent(sender,this,NEW_COLOR)); //22May1999: using new SO constructor
 }

 //

 public Color get_fgrColor() {return fgrColor;}
 public Color get_bkgrColor() {return bkgrColor;}
 public Color get_fillColor() {return fillColor;}
 public Color get_color() {return color;}

}
