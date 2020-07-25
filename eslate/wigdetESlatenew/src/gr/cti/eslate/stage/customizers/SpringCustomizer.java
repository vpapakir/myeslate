//Title:        Stage/SpringCustomizer
//Version:      17May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage's Spring customizer

package gr.cti.eslate.stage.customizers;

import javax.swing.*;
import java.beans.*;

import gr.cti.utils.*;

import gr.cti.eslate.stage.Res;
import gr.cti.eslate.stage.models.AsSpring;

import com.thwt.layout.*;

public class SpringCustomizer
 extends BaseCustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private AsSpring object; /**/
  private JLabel lblSpringConstant;   private JNumberField nfSpringConstant;
  private JLabel lblNaturalLength;    private JNumberField nfNaturalLength;
  private JLabel lblLength;    private JNumberField nfLength;
  private JLabel lblDisplacement;     private JNumberField nfDisplacement;
  private JLabel lblAngle;     private JNumberField nfAngle;

  private SmartLayout smartLayout;
  private LayoutConstraint c1,c2,c3,c4,c5,
                           d1,d2,d3,d4,d5;

////////////////////////////////

  public SpringCustomizer() {
   try{jbInit();}
   catch(Exception ex){ex.printStackTrace();}
  }

/////////////////////////////////////////////////

 public String getTitle(){
  return Res.localize("Spring");
 }

 public void setupWidgetsFromObject(){
  if(object==null) return;
  //spring constant//
  nfSpringConstant.setNumber(object.getSpringConstant());
  //natural length//
  nfNaturalLength.setNumber(object.getNaturalLength());
  //maximum length//
  nfLength.setNumber(object.getLength());
  //displacement//
  nfDisplacement.setNumber(object.getDisplacement());
  //angle//
  nfAngle.setNumber(object.getAngle());
 }

 public void setObject(Object o){
  object=(AsSpring)o;
  setupWidgetsFromObject();
 }

 public Object getObject(){
  return object;
 }

/////////////////////////////////////////////////

  private static final int PADX=2;
  private static final int COLUMN2X=120;
  private static final int STEPY=10;
  private static final int LBLWIDTH=COLUMN2X-PADX-10; //12May2000
  private static final int LBLHEIGHT=20;

  private void jbInit() throws Exception {
    String sc=":";

    lblSpringConstant=new JLabel(Res.localize("Spring Constant")+sc);
    lblNaturalLength=new JLabel(Res.localize("Natural Length")+sc);
    lblLength=new JLabel(Res.localize("Length")+sc);
    lblDisplacement=new JLabel(Res.localize("Displacement")+sc);
    lblAngle=new JLabel(Res.localize("Angle")+sc);

    ////////////    

    nfSpringConstant=new JNumberField();
    nfSpringConstant.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfSpringConstant_propertyChange(e);
     }
    });

    ////////////

    nfNaturalLength=new JNumberField();
    nfNaturalLength.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfNaturalLength_propertyChange(e);
     }
    });

    ////////////

    nfDisplacement=new JNumberField();
    nfDisplacement.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfDisplacement_propertyChange(e);
     }
    });

    ////////////

    nfLength=new JNumberField();
    nfLength.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfLength_propertyChange(e);
     }
    });

    ////////////

    nfAngle=new JNumberField();
    nfAngle.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfAngle_propertyChange(e);
     }
    });

    ////////////

    nfSpringConstant.setDecimalDigits(3); //15Apr2000
    nfNaturalLength.setDecimalDigits(3); //15Apr2000
    nfDisplacement.setDecimalDigits(3); //15Apr2000
    nfLength.setDecimalDigits(3); //15Apr2000
    nfAngle.setDecimalDigits(3); //15Apr2000

    lblSpringConstant.setHorizontalAlignment(RIGHT);
    lblNaturalLength.setHorizontalAlignment(RIGHT);
    lblLength.setHorizontalAlignment(RIGHT);
    lblDisplacement.setHorizontalAlignment(RIGHT);
    lblAngle.setHorizontalAlignment(RIGHT);

    smartLayout=new SmartLayout();
    this.setLayout(smartLayout); //using SmartLayout Manager

    //1st column//

    c1=new LayoutConstraint();
    c1.anchorToContainerTop(STEPY);
    c1.anchorToContainerLeft(PADX);
    c1.setFixedWidth(LBLWIDTH);
    c1.setFixedHeight(LBLHEIGHT);
    this.add(lblSpringConstant,c1);

    c2=new LayoutConstraint();
    c2.anchorToBottomOf(lblSpringConstant,STEPY);
    c2.anchorToContainerLeft(PADX);
    c2.setFixedWidth(LBLWIDTH);
    c2.setFixedHeight(LBLHEIGHT);
    this.add(lblLength,c2);

    c3=new LayoutConstraint();
    c3.anchorToBottomOf(lblLength,STEPY);
    c3.anchorToContainerLeft(PADX);
    c3.setFixedWidth(LBLWIDTH);
    c3.setFixedHeight(LBLHEIGHT);
    this.add(lblNaturalLength,c3);

    c4=new LayoutConstraint();
    c4.anchorToBottomOf(lblNaturalLength,STEPY);
    c4.anchorToContainerLeft(PADX);
    c4.setFixedWidth(LBLWIDTH);
    c4.setFixedHeight(LBLHEIGHT);
    this.add(lblDisplacement,c4);

    c5=new LayoutConstraint();
    c5.anchorToBottomOf(lblDisplacement,STEPY);
    c5.anchorToContainerLeft(PADX);
    c5.setFixedWidth(LBLWIDTH);
    c5.setFixedHeight(LBLHEIGHT);
    this.add(lblAngle,c5);

    //2nd column//

    d1=new LayoutConstraint();
    d1.sameHeightAs(lblSpringConstant);
    d1.anchorToContainerTop(STEPY);
    d1.anchorToContainerLeft(COLUMN2X);
    d1.anchorToContainerRight(PADX);
    this.add(nfSpringConstant,d1);

    d2=new LayoutConstraint();
    d2.sameHeightAs(lblLength);
    d2.anchorToBottomOf(lblSpringConstant,STEPY);
    d2.anchorToContainerLeft(COLUMN2X);
    d2.anchorToContainerRight(PADX);
    this.add(nfLength,d2);

    d3=new LayoutConstraint();
    d3.sameHeightAs(lblNaturalLength);
    d3.anchorToBottomOf(lblLength,STEPY);
    d3.anchorToContainerLeft(COLUMN2X);
    d3.anchorToContainerRight(PADX);
    this.add(nfNaturalLength,d3);

    d4=new LayoutConstraint();
    d4.sameHeightAs(lblDisplacement);
    d4.anchorToBottomOf(lblNaturalLength,STEPY);
    d4.anchorToContainerLeft(COLUMN2X);
    d4.anchorToContainerRight(PADX);
    this.add(nfDisplacement,d4);

    d5=new LayoutConstraint();
    d5.sameHeightAs(lblAngle);
    d5.anchorToBottomOf(lblDisplacement,STEPY);
    d5.anchorToContainerLeft(COLUMN2X);
    d5.anchorToContainerRight(PADX);
    this.add(nfAngle,d5);

    //this.setMinimumSize(new Dimension(228, 136));
    //this.setPreferredSize(new Dimension(228, 134));

    /*
    java.util.Vector errors = smartLayout.checkLayout();
    if ((errors != null) && (errors.size() > 0)) {
       for (int i=0; i<errors.size(); i++) {
           System.err.println(errors.elementAt(i));
       }
    }
    */
 }

  void nfSpringConstant_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setSpringConstant(nfSpringConstant.getdouble());
   updateParent();
  }

  void nfLength_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setLength(nfLength.getdouble());
   updateParent();
  }

  void nfNaturalLength_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setNaturalLength(nfNaturalLength.getdouble());
   updateParent();
  }

  void nfDisplacement_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setDisplacement(nfDisplacement.getdouble());
   updateParent();
  }

  void nfAngle_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setAngle(nfAngle.getdouble());
   updateParent();
  }

}

