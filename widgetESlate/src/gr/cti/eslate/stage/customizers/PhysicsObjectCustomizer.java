//Title:        Stage
//Version:      17May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage's PhysicsObject customizer

package gr.cti.eslate.stage.customizers;

import javax.swing.*;
import java.beans.*;

import gr.cti.utils.*;

import gr.cti.eslate.stage.Res;
import gr.cti.eslate.stage.models.AsPhysicsObject;
import gr.cti.eslate.stage.widgets.*;

import com.thwt.layout.*;

public class PhysicsObjectCustomizer
 extends BaseCustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private AsPhysicsObject object; /**/
  private JLabel lblMass;                private JNumberField nfMass;
  private JLabel lblVelocity;            private DoublePoint2DWidget wgtVelocity;
  private JLabel lblAcceleration;        private DoublePoint2DWidget wgtAppliedForce;
  private JLabel lblAppliedForce;        private DoublePoint2DWidget wgtAcceleration;
  private JLabel lblKineticEnergy;       private JNumberField nfKineticEnergy;
  private JLabel lblAltitude;            private JNumberField nfAltitude;

  private SmartLayout smartLayout;
  private LayoutConstraint c1,c2,c3,c4,c5,c6,
                           c7,c8,c9,c10,c11,c12;

////////////////////////////////

  public PhysicsObjectCustomizer() {
    try  {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

/////////////////////////////////////////////////

 public String getTitle(){
  return Res.localize("Physics Object");
 }

 public void setupWidgetsFromObject(){
  if(object==null) return;
  //mass//
  nfMass.setNumber(object.getMass());
  //velocity//
  wgtVelocity.setPoint(object.getVelocity());
  //acceleration//
  wgtAcceleration.setPoint(object.getAcceleration());
  //appliedForce//
  wgtAppliedForce.setPoint(object.getAppliedForce());
  //kineticEnergy//
  nfKineticEnergy.setNumber(object.getKineticEnergy());
  //altitude//
  nfAltitude.setNumber(object.getAltitude());
 }

 public void setObject(Object o){
  object=(AsPhysicsObject)o;
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

    lblMass=new JLabel(Res.localize("Mass")+sc);
    lblVelocity=new JLabel(Res.localize("Velocity")+sc);
    lblAcceleration=new JLabel(Res.localize("Acceleration")+sc);
    lblAppliedForce=new JLabel(Res.localize("Applied Force")+sc);
    lblKineticEnergy=new JLabel(Res.localize("Kinetic Energy")+sc);
    lblAltitude=new JLabel(Res.localize("Altitude")+sc);

    nfMass=new JNumberField();
    nfMass.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfMass_propertyChange(e);
     }
    });

    wgtVelocity=new DoublePoint2DWidget();
    wgtVelocity.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      wgtVelocity_propertyChange(e);
     }
    });

    wgtAcceleration=new DoublePoint2DWidget();
    wgtAcceleration=new DoublePoint2DWidget();
    wgtAcceleration.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      wgtAcceleration_propertyChange(e);
     }
    });

    wgtAppliedForce=new DoublePoint2DWidget();
    wgtAppliedForce=new DoublePoint2DWidget();
    wgtAppliedForce.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      wgtAppliedForce_propertyChange(e);
     }
    });

    nfKineticEnergy=new JNumberField();
    nfKineticEnergy.setEnabled(false); //17May2000: don't use "setEditable(false)", cause in that case the object will still get focus when moving between the fields with TAB

    nfAltitude=new JNumberField();
    nfAltitude.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      nfAltitude_propertyChange(e);
     }
    });

    nfMass.setDecimalDigits(3); //15Apr2000
    nfKineticEnergy.setDecimalDigits(3); //15Apr2000
    nfAltitude.setDecimalDigits(3); //15Apr2000

    lblMass.setHorizontalAlignment(RIGHT);
    lblVelocity.setHorizontalAlignment(RIGHT);
    lblAcceleration.setHorizontalAlignment(RIGHT);
    lblAppliedForce.setHorizontalAlignment(RIGHT);
    lblKineticEnergy.setHorizontalAlignment(RIGHT);
    lblAltitude.setHorizontalAlignment(RIGHT);

    smartLayout=new SmartLayout();
    this.setLayout(smartLayout); //using SmartLayout Manager

    //1st column//

    c1=new LayoutConstraint();
    c1.anchorToContainerTop(STEPY);
    c1.anchorToContainerLeft(PADX);
    c1.setFixedWidth(LBLWIDTH);
    c1.setFixedHeight(LBLHEIGHT);
    this.add(lblMass,c1);

    c2=new LayoutConstraint();
    c2.anchorToBottomOf(lblMass,STEPY);
    c2.anchorToContainerLeft(PADX);
    c2.setFixedWidth(LBLWIDTH);
    c2.setFixedHeight(LBLHEIGHT);
    this.add(lblVelocity,c2);

    c3=new LayoutConstraint();
    c3.anchorToBottomOf(lblVelocity,STEPY);
    c3.anchorToContainerLeft(PADX);
    c3.setFixedWidth(LBLWIDTH);
    c3.setFixedHeight(LBLHEIGHT);
    this.add(lblAcceleration,c3);

    c4=new LayoutConstraint();
    c4.anchorToBottomOf(lblAcceleration,STEPY);
    c4.anchorToContainerLeft(PADX);
    c4.setFixedWidth(LBLWIDTH);
    c4.setFixedHeight(LBLHEIGHT);
    this.add(lblAppliedForce,c4);

    c5=new LayoutConstraint();
    c5.anchorToBottomOf(lblAppliedForce,STEPY);
    c5.anchorToContainerLeft(PADX);
    c5.setFixedWidth(LBLWIDTH);
    c5.setFixedHeight(LBLHEIGHT);
    this.add(lblKineticEnergy,c5);

    c6=new LayoutConstraint();
    c6.anchorToBottomOf(lblKineticEnergy,STEPY);
    c6.anchorToContainerLeft(PADX);
    c6.setFixedWidth(LBLWIDTH);
    c6.setFixedHeight(LBLHEIGHT);
    this.add(lblAltitude,c6);


    //2nd column//

    c7=new LayoutConstraint();
    c7.sameHeightAs(lblMass);
    c7.anchorToContainerTop(STEPY);
    c7.anchorToContainerLeft(COLUMN2X);
    c7.anchorToContainerRight(PADX);
    this.add(nfMass,c7);

    c8=new LayoutConstraint();
    c8.sameHeightAs(lblVelocity);
    c8.anchorToBottomOf(lblMass,STEPY);
    c8.anchorToContainerLeft(COLUMN2X);
    c8.anchorToContainerRight(PADX);
    this.add(wgtVelocity,c8);

    c9=new LayoutConstraint();
    c9.sameHeightAs(lblAcceleration);
    c9.anchorToBottomOf(lblVelocity,STEPY);
    c9.anchorToContainerLeft(COLUMN2X);
    c9.anchorToContainerRight(PADX);
    this.add(wgtAcceleration,c9);

    c10=new LayoutConstraint();
    c10.sameHeightAs(lblAppliedForce);
    c10.anchorToBottomOf(lblAcceleration,STEPY);
    c10.anchorToContainerLeft(COLUMN2X);
    c10.anchorToContainerRight(PADX);
    this.add(wgtAppliedForce,c10);

    c11=new LayoutConstraint();
    c11.sameHeightAs(lblKineticEnergy);
    c11.anchorToBottomOf(lblAppliedForce,STEPY);
    c11.anchorToContainerLeft(COLUMN2X);
    c11.anchorToContainerRight(PADX);
    this.add(nfKineticEnergy,c11);

    c12=new LayoutConstraint();
    c12.sameHeightAs(lblAltitude);
    c12.anchorToBottomOf(lblKineticEnergy,STEPY);
    c12.anchorToContainerLeft(COLUMN2X);
    c12.anchorToContainerRight(PADX);
    this.add(nfAltitude,c12);

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

  void nfMass_propertyChange(PropertyChangeEvent e) {
  if(object!=null) object.setMass(nfMass.getdouble());
   updateParent();
  }

  void wgtVelocity_propertyChange(PropertyChangeEvent e) {
  if(object!=null) object.setVelocity(wgtVelocity.getPoint());
   updateParent();
  }

  void wgtAcceleration_propertyChange(PropertyChangeEvent e) {
  if(object!=null) object.setAcceleration(wgtAcceleration.getPoint());
   updateParent();
  }

  void wgtAppliedForce_propertyChange(PropertyChangeEvent e) {
  if(object!=null) object.setAppliedForce(wgtAppliedForce.getPoint());
   updateParent();
  }

  void nfAltitude_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setAltitude(nfAltitude.getdouble());
   updateParent();
  }

}

