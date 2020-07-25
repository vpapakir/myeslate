//Title:        Stage/BallCustomizer
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
import gr.cti.eslate.stage.models.AsBall;

import com.thwt.layout.*;

public class BallCustomizer
 extends BaseCustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private AsBall object; /**/

  private JLabel lblRadius; private JNumberField nfRadius;
  private JLabel lblAngle;  private JNumberField nfAngle;

  private SmartLayout smartLayout;
  private LayoutConstraint c1,c2,
                           d1,d2;

////////////////////////////////

  public BallCustomizer() {
   try{jbInit();}
   catch(Exception ex){ex.printStackTrace();}
  }

/////////////////////////////////////////////////

 public String getTitle(){
  return Res.localize("Ball");
 }

 public void setupWidgetsFromObject(){
  if(object==null) return;
  //radius//
  nfRadius.setNumber(object.getRadius());
  //angle//
  nfAngle.setNumber(object.getAngle());
 }

 public void setObject(Object o){
  object=(AsBall)o;
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

    lblRadius=new JLabel(Res.localize("Radius")+sc);
    lblAngle=new JLabel(Res.localize("Angle")+sc);

    ////////////

    nfRadius=new JNumberField();
    nfRadius.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        nfRadius_propertyChange(e);
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

    nfRadius.setDecimalDigits(3); //15Apr2000
    nfAngle.setDecimalDigits(3); //15Apr2000

    lblRadius.setHorizontalAlignment(RIGHT);
    lblAngle.setHorizontalAlignment(RIGHT);

    smartLayout=new SmartLayout();
    this.setLayout(smartLayout); //using SmartLayout Manager

    //1st column//

    c1=new LayoutConstraint();
    c1.anchorToContainerTop(STEPY);
    c1.anchorToContainerLeft(PADX);
    c1.setFixedWidth(LBLWIDTH);
    c1.setFixedHeight(LBLHEIGHT);
    this.add(lblRadius,c1);

    c2=new LayoutConstraint();
    c2.anchorToBottomOf(lblRadius,STEPY);
    c2.anchorToContainerLeft(PADX);
    c2.setFixedWidth(LBLWIDTH);
    c2.setFixedHeight(LBLHEIGHT);
    this.add(lblAngle,c2);

    //2nd column//

    d1=new LayoutConstraint();
    d1.sameHeightAs(lblRadius);
    d1.anchorToContainerTop(STEPY);
    d1.anchorToContainerLeft(COLUMN2X);
    d1.anchorToContainerRight(PADX);
    this.add(nfRadius,d1);

    d2=new LayoutConstraint();
    d2.sameHeightAs(lblAngle);
    d2.anchorToBottomOf(lblRadius,STEPY);
    d2.anchorToContainerLeft(COLUMN2X);
    d2.anchorToContainerRight(PADX);
    this.add(nfAngle,d2);

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

  void nfRadius_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setRadius(nfRadius.getdouble());
   updateParent();
  }

  void nfAngle_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setAngle(nfAngle.getdouble());
   updateParent();
  }

}

