//Title:        LabelNumberSlider
//Version:      1Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.beans.*;
import java.io.*;

public class LabelNumberSlider extends JPanel implements Externalizable {

  static final long serialVersionUID = -1067758432731903016L; //1Jun2000: added serial version id

  JPanel LabelNumber = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel lblDescription = new JLabel();
  JNumberField nfValue = new JNumberField();
  ValueSlider sldValue = new ValueSlider();

  public LabelNumberSlider() {
   try{jbInit();}
   catch(Exception e){e.printStackTrace();}
  }

  //GUI//

  private void jbInit(){
    this.setLayout(borderLayout1);
    LabelNumber.setLayout(borderLayout2);
    gridLayout1.setColumns(1);
    gridLayout1.setRows(0);
    nfValue.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nfValue_actionPerformed(e);
      }
    });
    nfValue.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        nfValue_focusLost(e);
      }
    });
    sldValue.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        sldValue_stateChanged(e);
      }
    });
    this.setPreferredSize(new Dimension(190, 90));
    sldValue.setOpaque(false);
    sldValue.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        sldValue_propertyChange(e);
      }
    });
    LabelNumber.setOpaque(false);
    this.add(LabelNumber, BorderLayout.NORTH);
    LabelNumber.add(lblDescription, BorderLayout.WEST);
    LabelNumber.add(nfValue, BorderLayout.CENTER);
    this.add(sldValue, BorderLayout.CENTER);
    //this.revalidate(); //1Jun2000: patch to always show the slider in Java1.2+
  }

  //RealValue property//

  public void setRealValue(double value){
   sldValue.setRealValue(value);
   //nfValue.setNumber(value); //not needed, the setting of the value to the slider will cause an state changed event (since both the slider and the text field start with 0 value, they always stay in sync)
  }

  public double getRealValue(){
   return sldValue.getRealValue();
  }

  //Description property//

  public void setDescription(String description){
   lblDescription.setText(description);
  }

  public String getDescription(){
   return lblDescription.getText();
  }

  //From property//

  public void setFrom(double value){
   sldValue.setFrom(value);
  }

  public double getFrom(){
   return sldValue.getFrom();
  }

  //To property//

  public void setTo(double value){
   sldValue.setTo(value);
  }

  public double getTo(){
   return sldValue.getTo();
  }

  //Step property//

  public void setStep(double value){
   sldValue.setStep(value);
  }

  public double getStep(){
   return sldValue.getStep();
  }

  //Foreground property (override)//

  public void setForeground(Color c){ //must check for fields not being null, since this method is called automatically by Swing before the component's constructor has been called to build its children
   if(sldValue!=null) lblDescription.setForeground(c);
   if(nfValue!=null) nfValue.setForeground(c);
   if(sldValue!=null) sldValue.setForeground(c);
  }

  //Background property (override)//

  public void setBackground(Color c){ //must check for fields not being null, since this method is called automatically by Swing before the component's constructor has been called to build its children
   if(sldValue!=null) lblDescription.setBackground(c);
   //if(nfValue!=null) nfValue.setBackground(c); //don't change the textbox's background color
   if(sldValue!=null) sldValue.setBackground(c);
  }

  public void setFont(Font f){
   super.setFont(f);
   if(lblDescription!=null) lblDescription.setFont(f);
   if(sldValue!=null) sldValue.setFont(f);
  }

  ////////////////////

  void nfValue_actionPerformed(ActionEvent e) {
   setRealValue(nfValue.getdouble());
  }

  void nfValue_focusLost(FocusEvent e) {
   setRealValue(nfValue.getdouble());
  }

  void sldValue_stateChanged(ChangeEvent e) {
   nfValue.setNumber(sldValue.getRealValue());
  }

  //catch value slider's propertyChange events and rethrow them as if they were this component's events

  void sldValue_propertyChange(PropertyChangeEvent e) {
   firePropertyChange(e.getPropertyName(),e.getOldValue(),e.getNewValue());
  }

 //Externalizable//

 public static final String OPAQUE_PROPERTY="opaque";
 public static final String FOREGROUND_PROPERTY="foreground";
 public static final String BACKGROUND_PROPERTY="background";

 public static final String REAL_VALUE_PROPERTY="readValue";
 public static final String FROM_PROPERTY="from";
 public static final String TO_PROPERTY="to";
 public static final String STEP_PROPERTY="step";
 public static final String DESCRIPTION_PROPERTY="description";

 public static final String FONT_PROPERTY="font"; //22Apr2000

 public void writeExternal(ObjectOutput out) throws IOException{
  ObjectHash properties=new ObjectHash();

  properties.putBoolean(OPAQUE_PROPERTY,isOpaque());
  properties.putColor(FOREGROUND_PROPERTY,getForeground());
  properties.putColor(BACKGROUND_PROPERTY,getBackground());

  properties.putDouble(REAL_VALUE_PROPERTY,getRealValue());
  properties.putDouble(FROM_PROPERTY,getFrom());
  properties.putDouble(TO_PROPERTY,getTo());
  properties.putDouble(STEP_PROPERTY,getStep());
  properties.putString(DESCRIPTION_PROPERTY,getDescription());

  properties.put(FONT_PROPERTY,getFont());

  out.writeObject(properties);
 }

 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
  ObjectHash properties=(ObjectHash)in.readObject();

  setOpaque(properties.getBoolean(OPAQUE_PROPERTY,true));
  setForeground(properties.getColor(FOREGROUND_PROPERTY,Color.black));
  setBackground(properties.getColor(BACKGROUND_PROPERTY,Color.lightGray));

  setRealValue(properties.getDouble(REAL_VALUE_PROPERTY,0));
  setFrom(properties.getDouble(FROM_PROPERTY,0));
  setTo(properties.getDouble(TO_PROPERTY,100));
  setStep(properties.getDouble(STEP_PROPERTY,1));
  setDescription(properties.getString(DESCRIPTION_PROPERTY,""));
  try{setFont((Font)properties.get(FONT_PROPERTY));}catch(Exception e){} //reset to some default font???
 }

}