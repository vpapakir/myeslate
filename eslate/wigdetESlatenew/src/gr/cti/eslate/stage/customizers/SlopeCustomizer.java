//Title:        Stage/SlopeCustomizer
//Version:      17May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage's Slope customizer

package gr.cti.eslate.stage.customizers;

import java.awt.*;
import javax.swing.*;
import java.beans.*;

import gr.cti.utils.*;

import gr.cti.eslate.stage.Res;
import gr.cti.eslate.stage.models.AsSlope;

import com.thwt.layout.*;

public class SlopeCustomizer
 extends BaseCustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private AsSlope object; /**/
  private JLabel lblWidth;    private JNumberField nfWidth;
  private JLabel lblHeight;    private JNumberField nfHeight;
  private JLabel lblLength;    private JNumberField nfLength; //temp: read-only
  private JLabel lblAngle;     private JNumberField nfAngle;

  private SmartLayout smartLayout;
  private LayoutConstraint c1,c2,c3,c4,
                           d1,d2,d3,d4;

////////////////////////////////

  public SlopeCustomizer() {
   try{jbInit();}
   catch(Exception ex){ex.printStackTrace();}
  }

/////////////////////////////////////////////////

 public String getTitle(){
  return Res.localize("Slope");
 }

 public void setupWidgetsFromObject(){
  if(object==null) return;
  //angle//
  nfAngle.setNumber(object.getAngle());
  //width//
  nfWidth.setNumber(object.getObjectWidth());
  //height//
  nfHeight.setNumber(object.getObjectHeight());
  //length//
  nfLength.setNumber(object.getLength());
 }

 public void setObject(Object o){
  object=(AsSlope)o;
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

    lblAngle=new JLabel(Res.localize("Angle")+sc);
    lblWidth=new JLabel(Res.localize("Width")+sc);
    lblHeight=new JLabel(Res.localize("Height")+sc);
    lblLength=new JLabel(Res.localize("Length")+sc);

    nfAngle=new JNumberField();
    nfAngle.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfAngle_propertyChange(e);
     }
    });

    ///////////

    nfWidth=new JNumberField();
    nfWidth.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfWidth_propertyChange(e);
     }
    });

    ///////////

    nfHeight=new JNumberField();
    nfHeight.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfHeight_propertyChange(e);
     }
    });

    ///////////

    nfLength=new JNumberField();
    nfLength.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfLength_propertyChange(e);
     }
    });

    ///////////

    nfAngle.setDecimalDigits(3); //15Apr2000
    nfWidth.setDecimalDigits(3); //15Apr2000
    nfHeight.setDecimalDigits(3); //15Apr2000
    nfLength.setDecimalDigits(3); //15Apr2000

    lblAngle.setHorizontalAlignment(RIGHT);
    lblWidth.setHorizontalAlignment(RIGHT);
    lblHeight.setHorizontalAlignment(RIGHT);
    lblLength.setHorizontalAlignment(RIGHT);

    smartLayout=new SmartLayout();
    this.setLayout(smartLayout); //using SmartLayout Manager

    Component comp;

    //1st column//

    c1=new LayoutConstraint();
    c1.anchorToContainerTop(STEPY);
    c1.anchorToContainerLeft(PADX);
    c1.setFixedWidth(LBLWIDTH);
    c1.setFixedHeight(LBLHEIGHT);
    comp=lblAngle;
    this.add(comp,c1);

    c2=new LayoutConstraint();
    c2.anchorToBottomOf(comp,STEPY);
    c2.anchorToContainerLeft(PADX);
    c2.setFixedWidth(LBLWIDTH);
    c2.setFixedHeight(LBLHEIGHT);
    comp=lblWidth;
    this.add(comp,c2);

    c3=new LayoutConstraint();
    c3.anchorToBottomOf(comp,STEPY);
    c3.anchorToContainerLeft(PADX);
    c3.setFixedWidth(LBLWIDTH);
    c3.setFixedHeight(LBLHEIGHT);
    comp=lblHeight;
    this.add(comp,c3);

    c4=new LayoutConstraint();
    c4.anchorToBottomOf(comp,STEPY);
    c4.anchorToContainerLeft(PADX);
    c4.setFixedWidth(LBLWIDTH);
    c4.setFixedHeight(LBLHEIGHT);
    comp=lblLength;
    this.add(comp,c4);


    //2nd column//

    d1=new LayoutConstraint();
    d1.sameHeightAs(lblAngle);
    d1.anchorToContainerTop(STEPY);
    d1.anchorToContainerLeft(COLUMN2X);
    d1.anchorToContainerRight(PADX);
    comp=nfAngle;
    this.add(comp,d1);

    d2=new LayoutConstraint();
    d2.sameHeightAs(lblWidth);
    d2.anchorToBottomOf(comp,STEPY);
    d2.anchorToContainerLeft(COLUMN2X);
    d2.anchorToContainerRight(PADX);
    comp=nfWidth;
    this.add(comp,d2);

    d3=new LayoutConstraint();
    d3.sameHeightAs(lblHeight);
    d3.anchorToBottomOf(comp,STEPY);
    d3.anchorToContainerLeft(COLUMN2X);
    d3.anchorToContainerRight(PADX);
    comp=nfHeight;
    this.add(comp,d3);

    d4=new LayoutConstraint();
    d4.sameHeightAs(lblLength);
    d4.anchorToBottomOf(comp,STEPY);
    d4.anchorToContainerLeft(COLUMN2X);
    d4.anchorToContainerRight(PADX);
    comp=nfLength;
    this.add(nfLength,d4);

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

  void nfWidth_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setObjectWidth(nfWidth.getdouble());
   updateParent();
  }

  void nfHeight_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setObjectHeight(nfHeight.getdouble());
   updateParent();
  }

  void nfAngle_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setAngle(nfAngle.getdouble());
   updateParent();
  }

  void nfLength_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setLength(nfLength.getdouble());
   updateParent();
  }

}

