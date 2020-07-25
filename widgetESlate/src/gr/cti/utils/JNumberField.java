// JNumberField.java

/*
 11Mar1998 - first creation by G.Birbilis
 19Mar1998 - now restricts kbd entry to numbers
 21Mar1998 - now constructor can be passed a string too
 24Mar1998 - added INTEGER_NUMBER & FLOAT_NUMBER
 29Apr1998 - added to package gr.cti.avakeeo.slider
 13May1998 - moved to package gr.cti.avakeeo.utils, added final to static fields
 14Jul1998 - fixed getinteger & getshort to return correct type (returned float & long)
  8Dec1999 - added constructor with no parameters
 15Apr2000 - fixed-bug: wasn't allowing user to enter signed numbers
           - added decimalDigits private field and respective public property
           - added serial-verion
 16May2000 - now fires PropertyChange event for property "value" when setNumber is called or when CR is pressed or focus is lost
 17May2000 - not trying to fire a PropertyChange event the first time a number is set (cause old value is "")
           - fixed-bug: now not consuming the arrow keys
           - if the user leaves the edit box empty, set it to 0
*/

package gr.cti.utils;

import java.awt.event.*;
import javax.swing.*;

public class JNumberField
 extends JTextField
 implements ActionListener,
            FocusListener
{

 static final long serialVersionUID = 15042000L; //15Apr2000: added serial-version

 public static final String VALUE_PROPERTY="value";

 public static final String SIGN="+-";
 public static final String OCTAL_DIGITS="01234567";
 public static final String DECIMAL_DIGITS="0123456789";
 public static final String HEXADECIMAL_DIGITS="ABCDEF"+DECIMAL_DIGITS;
 public static final String FLOAT_DIGITS=DECIMAL_DIGITS+'.';
 //
 public static final String INTEGER_NUMBER=DECIMAL_DIGITS+SIGN;
 public static final String FLOAT_NUMBER=FLOAT_DIGITS+SIGN;

 private String validChars=FLOAT_NUMBER; //use setValidChars for the constants above //15Apr2000: fixed-bug: was using FLOAT_DIGITS instead of FLOAT_NUMBER and thus wasn't allowing the user to give signed numbers
 private int decimalDigits=-1; //15Apr2000: default to infinite decimal digits

 public JNumberField(){ //8Dec1999: added constructor with no parameters
  this(0);
 }

 public JNumberField(Number value) {super(); setNumber(value); init();}
 public JNumberField(byte value)   {super(); setNumber(value); init();}
 public JNumberField(double value) {super(); setNumber(value); init();}
 public JNumberField(float value)  {super(); setNumber(value); init();}
 public JNumberField(int value)    {super(); setNumber(value); init();}
 public JNumberField(long value)   {super(); setNumber(value); init();}
 public JNumberField(short value)  {super(); setNumber(value); init();}
 public JNumberField(String value) {super(); setNumber(value); init();} //29-9-1998:using setNumber instead of setText

 public void init(){ //16May2000: listen for "CR press" or "focus lost / TAB"
  addFocusListener(this);
  addActionListener(this);
 }

//SET//

 public void setNumber(Number value) {
  double dvalue=value.doubleValue();
  if(decimalDigits>=0) dvalue=NumberUtil.round_n(dvalue,decimalDigits); //15Apr2000
  if(!getText().equals("")){ //17May2000: check this, cause the getdouble() method throws NumberFormatException at the component's startup (cause initial old value is "") 
   double oldValue=getdouble();
   if(dvalue!=oldValue){ //16May2000
    super.setText(NumberUtil.num2str(dvalue)); //use this one instead of "String.valueOf(dvalue)", since it doesn't add ".0" to a number if it has no fractional part (needed, else Canvas component throws error when loading its serialized state)
    firePropertyChange(VALUE_PROPERTY,oldValue,dvalue); //16May2000
   }
  }else super.setText(NumberUtil.num2str(dvalue)); //use this one instead of "String.valueOf(dvalue)", since it doesn't add ".0" to a number if it has no fractional part (needed, else Canvas component throws error when loading its serialized state)
 }
 public void setNumber(byte value)   {setNumber(new Byte(value));}
 public void setNumber(double value) {setNumber(new Double(value));}
 public void setNumber(float value)  {setNumber(new Float(value));}
 public void setNumber(int value)    {setNumber(new Integer(value));}
 public void setNumber(long value)   {setNumber(new Long(value));}
 public void setNumber(short value)  {setNumber(new Short(value));}
 public void setNumber(String value) {super.setText(value);} //29-9-1998

 
//GET//

 public Number getNumber(){ //16May2000: to match "setNumber(Number)" and define a "number" property
  return getDouble(); //use as much accuracy as possible
 }

 public Byte getByte(){
  return Byte.valueOf(getText());
 }

 public Double getDouble(){
  return Double.valueOf(getText());
 }

 public Float getFloat(){
  return Float.valueOf(getText());
 }

 public Integer getInteger(){
  return Integer.valueOf(getText());
 }

 public Long getLong(){
  return Long.valueOf(getText());
 }

 public Short getShort(){
  return Short.valueOf(getText());
 }

 //

 public byte getbyte(){
  return getByte().byteValue();
 }

 public double getdouble(){
  return getDouble().doubleValue();
 }

 public float getfloat(){
  return getFloat().floatValue();
 }

 public int getinteger(){
  return getInteger().intValue();
 }

 public long getlong(){
  return getLong().longValue();
 }

 public short getshort(){
  return getShort().shortValue();
 }

 //validChars property//
 public void setValidChars(String s) {validChars=s;}
 public String getValidChars(){return validChars;} //15Apr2000

 //decimalDigits property//
 public void setDecimalDigits(int count){decimalDigits=count;} //15Apr2000: give a negative number if you want any number of decimal digits
 public int getDecimalDigits(){return decimalDigits;}

/*
 public void setMinusSignAllowed(boolean flag){
  ...remove any "-" from the "validChars" string field
 }

 public boolean isMinusSignAllowed(){
  return validChars.indexOf("-")>0;
 }          
*/ 

 //if a non-action key is entered, it's processed only if its corresponding char is contained in validChars
 protected void processComponentKeyEvent(KeyEvent e) {
  char c=e.getKeyChar();
  if (!Character.isISOControl(c) && //needed to catch CR, ESC, BACKSPACE, DEL etc.
      !e.isActionKey() && //17May2000: had to add this in order for arrow keys to not get consumed
      !inString(validChars,c)
     ) e.consume();
   else super.processComponentKeyEvent(e);
 }

 private boolean inString(String s,char c) {
  for(int i=0;i<s.length();i++) if (s.charAt(i)==c) return true;
  return false;
 }

 //LISTENER//

 private void firePropertyChange(){
  if(getText().equals("")) setNumber(0); //17May2000: if the user leaves the edit box empty, set it to 0
  firePropertyChange(VALUE_PROPERTY,null,null); //don't pass any old or new value //must always fire this! (don't rely on "setNumber(0)": it won't: cause it has a special check for the "" value)
 }

 public void actionPerformed(ActionEvent e) {
  firePropertyChange();
 }

 public void focusGained(FocusEvent e){} //NOP

 public void focusLost(FocusEvent e) {
  firePropertyChange();
 }

}
