/*
 29May1998 - added to package gr.cti.eSlate.slider
 22Jun1998 - now value label is shown in black & it clears a rectangle behind it before drawn
 11Sep1998 - now leftVal>rightVal should work OK
 20Sep1999 - added empty constructor
 22Sep1999 - adding from/to/step properties and firing corresponding property change events
 11Oct1999 - added serialVersionUID field, so that future versions will load from older saved states
           - made inner class Double2IntMapping serializable
           - removed setStep(int), cause "Step" property wouldn't show up in E-Slate's property editor (had a "setStep(double)" and a "double getStep()"... this may be a Java bug)
           - removed "int" accessor methods, did "int->double" conversion for calling the internal logic implementation methods anyway
           - added "RealValue" property
 12Nov1999 - overriden ancestor's minimum/maximum properties to use our mapped double routines (cropping to int) [be sure to use super.XXX to call the original ancestor implementations of these methods]
 25Nov1999 - bug fix: getMinimum/getMaximum were doing an infinite recursion
           - minimum/maximum int properties override has been removed cause it was resulting in an incorrect current value label position
 18Dec2001 - added "usingValueAsToolTip" boolean property (not persistent, for backwards compatibility with older saved streams)
*/

package gr.cti.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

public class ValueSlider extends JSlider {

 static final long serialVersionUID = 11101999L; //11Oct1999: serial-version, so that future versions will load from older saved states

// Constructors ////////////////////////////

 public ValueSlider(){ //20Sep1999: added empty constructor, so that this class can be used as a JavaBean
  this(0,100,1);
 }

 public ValueSlider(double min,double max,double step) {
  this(min,(min+max)/2,max,step);
 }

 public ValueSlider(double min,double value,double max,double step) { //28-8-1998
  super();

  setSnapToTicks(true); //knob shall move to the closest tick
/*  setPaintTicks(false); //*/ setPaintTicks(true);  //for very big min/max distance this is problematic
  //setPaintLabels(true); //for big min/max distance this is problematic

  setMinValueMaxStep(min,value,max,step);
//  listener=new StepListener(step);
//  addChangeListener(listener);
  //...use getLabelTable to change the labels...
 }

// PROPERTIES ////////////////////////////////////   //these should fire property change events

 public static final String REALVALUE_PROPERTY = "realvalue"; //11Oct1999
 public static final String FROM_PROPERTY = "from";
 public static final String TO_PROPERTY   = "to";
 public static final String STEP_PROPERTY = "step";

 //realValue//

 public double getRealValue(){ //11Oct1999
  return myGetDoubleValue();
 }

 public void setRealValue(double value){ //11Oct1999
  double old=getRealValue();
  if(value!=old){
   mySetDoubleValue(value);
   firePropertyChange(REALVALUE_PROPERTY, old, value);
  }
 }

 //from//

 public double getFrom(){
  return left;
 }

 public void setFrom(double from){
  double old=getFrom();
  if(from!=old){
   setMinimum(from);
   firePropertyChange(FROM_PROPERTY, old, from);
  }
 }

 //to//

 public double getTo(){
  return right;
 }

 public void setTo(double to){
  double old=getTo();
  if(to!=old){
   setMaximum(to);
   firePropertyChange(TO_PROPERTY, old, to);
  }
 }

 //step//

 public double getStep(){
  return stepValue;
 }

 public void setStep(double step){
  double old=getStep();
  if(step!=old){
   setMinValueMaxStep(left,myGetDoubleValue(),right,step);
   firePropertyChange(STEP_PROPERTY, old, step);
  }
 }

 //usingValueAsToolTip property//

 public void setUsingValueAsToolTip(boolean flag){
  usingValueAsToolTip=flag;
 }

 public boolean isUsingValueAsToolTip(){
  return usingValueAsToolTip;
 }

 //12Nov1999: override ancestor's minimum/maximum properties to use our mapped double routines (cropping to int) [be sure to use super.XXX to call the original ancestor implementations of these methods]
/* //removed cause it was resulting in an incorrect current value label position
 public int getMinimum(){return (int)minValue;} //25Nov1999: bug-fix: was doing an infinite loop
 public void setMinimum(int min){setMinimum((double)min);}

 public void setMaximum(int max){setMaximum((double)max);}
 public int getMaximum(){return (int)maxValue;} //25Nov1999: bug-fix: was doing an infinite loop
*/ 

//////////////////////////////////////////////////////////

 //SET// (most of these should be private and change names... setMinimum should be _setFrom

 //11-9-1998:using left,right instead of minValue,maxValue-----------
 public void setMinimum(double min) {setMinValueMaxStep(min,myGetDoubleValue(),right,stepValue);}
 public void setMaximum(double max) {setMinValueMaxStep(left,myGetDoubleValue(),max,stepValue);}

/* //can't add these: our ancestor (JSlider) already has such methods that return an "int"
 public double getMinimum(){return minValue;} //25Nov1999
 public double getMaximum(){return maxValue;} //25Nov1999
*/

 //------------------------------------------------------------------

 public void setMinMaxStep(double min,double max,double step) { //28-8-1998
  setMinValueMaxStep(min,(min+max)/2,max,step);
 }

 public void setMinValueMaxStep(double min,double value,double max,double step) { //28-8-1998: added value param //29-9-1998: renamed from setMinMaxStep
  accuracy=(byte)Math.max(Math.max(NumberUtil.fractionDigits(min),NumberUtil.fractionDigits(max)),NumberUtil.fractionDigits(step));
  left=min; right=max; //11-9-1998
  if (min<=max) {
   minValue=min;
   maxValue=max;
   setInverted(false); //11-9-1998: must do this in case any setInverted(true) has been done previously
  }else{
   minValue=max;
   maxValue=min;
   setInverted(true); //shall work&show in reverse order (min..max as we wanted) //11-9-1998:placed above set-Min/Max
  }
  //11-9-1998:moved-here-start---------------
  m=new Double2IntMapping(minValue,step);
  super.setMinimum(m.mapDouble2Int(minValue));
  super.setMaximum(m.mapDouble2Int(maxValue)); //using minValue,maxValue and not min,max (might have Inverted=true)
  //----------moved-here-end-----------------
  stepValue=step;
//28-8-1998//  setValue((super.getMinimum()+super.getMaximum())/2);
  mySetDoubleValue(value); //28-8-1998
  setMajorTickSpacing(1);
  //setMinorTickSpacing(1);
  //setExtent(0); //default Extent=0, so not needed
  repaint();
 }

 //synchronized???
 public synchronized void setValue(int value) { //17-7-1998 (might solve {Slider moving too fast for Logo to handle} problem)
  //overriding slider's setValue, so that when user move knob, we make new tooltip
  int max=super.getMaximum();
  if (value>max) value=max;
  //8-7-1998: moved the following below the value=max setting
  if (lastValue!=null) changed=(lastValue.intValue()!=value); //used from outside to avoid some false notifications
  else changed=true; //!!!
  lastValue=new Integer(value); //internal: only to check if slider value changed (this value isn't the value we return - it's slider's pos)
  //
  super.setValue(value);
  if(usingValueAsToolTip) //18Dec2001: Birb: added property (not persistent) to control if tooltip is automatically set with the current value
   setToolTipText(getValueAsText()); //if remove this move string val to paint
  repaint(); //to show value [see paint()]
 };

 public void mySetIntValue(int value){setValue(m.mapDouble2Int(value));}
 public void mySetDoubleValue(double value){setValue(m.mapDouble2Int(value));}
 public int myGetIntValue(){return (int)Math.round(m.mapInt2Double(getValue()));}
 public double myGetDoubleValue(){return m.mapInt2Double(getValue());}
 public String getValueAsText(){return NumberUtil.num2str(NumberUtil.round_n(myGetDoubleValue(),accuracy));}

/*
 //needed???
 class StepListener implements ChangeListener {
  double step, prevValue;
  public StepListener(double stepValue){step=stepValue;}
  public void stateChanged(ChangeEvent e) {
   //...
  }
 }
*/

 public final class Double2IntMapping implements java.io.Serializable //11Oct1999: made serializable (else ValueSlider wouldn't serialize/deserialize) [also made this class final]
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
   
  private double min,step;
  public Double2IntMapping(double min,double step) {this.min=min; this.step=step;}
  public int mapDouble2Int(double x) {if (step==0) return 0; else return (int)((x-min)/step);}
  public double mapInt2Double(int x) {return min+x*step;}
 }

 public void paint(Graphics g){
  super.paint(g); //paint first of all, next code might take a bit long...

  Graphics2D g2 = (Graphics2D)g;
  FontRenderContext frc = g2.getFontRenderContext();
  String tipText = getToolTipText();
  Font font = g2.getFont();
  LineMetrics lm = font.getLineMetrics(tipText, frc);

  int w = font.getStringBounds(tipText, frc).getBounds().width;
  int h = (int)Math.round(lm.getHeight());
  Dimension size=new Dimension(w, h);

  int pos, mm=super.getMaximum()-super.getMinimum();
  double val;
  if (getInverted()) val=super.getMaximum()-super.getValue(); //if display inverted, invert pos too
   else val=super.getValue();
  if (mm!=0) pos=(int)(getWidth()*(val/mm))-size.width/2; //val must be a double
   else pos=0;
  if (pos<0) pos=0; else
  if (pos>getWidth()-size.width-1) pos=getWidth()-size.width-1; //-1 used cause "all left"=0

  //now paint the label...
  g.setColor(getBackground());
  g.fillRect(pos, getHeight()-size.height+1, size.width, size.height); //16-7-1998: added +1 to avoid slider trimming (???)
  g.setColor(Color.black); //g.setColor(Color.red);//
  g.drawString(tipText, pos, getHeight());
 }

// FIELDS /////////////////

// StepListener listener;
 byte accuracy;
 public double stepValue=1; //init to 1 for (extra) safety from "x modulo 0" actions
 public double minValue=0, maxValue=0;
 private double left=0,right=0; //11-9-1998
 Double2IntMapping m=new Double2IntMapping(0,1); //no-effect initial mapping
 //
 public boolean changed=false; //(must be false)
 private Integer lastValue=null; //internal (must be null)
 private transient boolean usingValueAsToolTip=true; //don't store, for backwards compatibility (not corrupt during loading any streams saved in the past)

}
