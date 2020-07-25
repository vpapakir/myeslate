//Title:        Stage/BaseObjectCustomizer
//Version:      18May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage's SceneObject customizer

package gr.cti.eslate.stage.customizers;

import java.awt.event.*;
import javax.swing.*;
import java.beans.*;


import gr.cti.eslate.stage.Res;
import gr.cti.eslate.stage.models.AsBaseObject;
import gr.cti.eslate.stage.widgets.*;

import com.thwt.layout.*;

public class BaseObjectCustomizer
 extends BaseCustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private AsBaseObject object; /**/
  private JLabel lblName;       private JTextField tfName;
  private JLabel lblLocation;   private DoublePoint2DWidget wgtLocation;

  private LayoutConstraint c1,c2,
                           d1,d2;

////////////////////////////////

  public BaseObjectCustomizer() {
   try{jbInit();}
   catch(Exception ex){ex.printStackTrace();}
  }

/////////////////////////////////////////////////

 public String getTitle(){
  return Res.localize("Object"); //say this "Object" and not "Base Object" (more user friendly: the "Object" type is the base of the whole hierarchy)
 }

 public void setupWidgetsFromObject(){
  if(object==null) return;
  //name//
  tfName.setText(object.getName());
  //location2D//
  wgtLocation.setPoint(object.getLocation2D());
 }

 public void setObject(Object o){
  object=(AsBaseObject)o;
  setupWidgetsFromObject();
 }

 public Object getObject(){
  return null;
 }

/////////////////////////////////////////////////

  private static final int PADX=2;
  private static final int COLUMN2X=80;
  private static final int STEPY=10;
  private static final int LBLWIDTH=COLUMN2X-PADX-10; //12May2000
  private static final int LBLHEIGHT=20;
  //private static final int IMAGE_HEIGHT=80;
  SmartLayout smartLayout = new SmartLayout(); //26Apr2000

  private void jbInit() throws Exception {
    String sc=":";
    //String dots="...";

    ////////////////

    lblName = new JLabel(Res.localize("Name")+sc);

    tfName = new JTextField();
    tfName.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(ActionEvent e) {
      tfName_actionPerformed(e);
     }
    });
    tfName.addFocusListener(new java.awt.event.FocusAdapter() {
     public void focusLost(FocusEvent e) {
      tfName_focusLost(e);
     }
    });

    ////////////////

    lblLocation = new JLabel(Res.localize("Location")+sc);

    wgtLocation= new DoublePoint2DWidget();
    wgtLocation.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
     public void propertyChange(PropertyChangeEvent e) {
      wgtLocation_propertyChange(e);
     }
    });

    ////////////////

    lblName.setHorizontalAlignment(RIGHT);
    lblLocation.setHorizontalAlignment(RIGHT);

    smartLayout=new SmartLayout();
    this.setLayout(smartLayout); //using SmartLayout Manager

    //1st column//

    c1=new LayoutConstraint();
    c1.anchorToContainerTop(STEPY);
    c1.anchorToContainerLeft(PADX);
    c1.setFixedWidth(LBLWIDTH);
    c1.setFixedHeight(LBLHEIGHT);
    this.add(lblName,c1);

    c2=new LayoutConstraint();
    c2.anchorToBottomOf(lblName,STEPY);
    c2.anchorToContainerLeft(PADX);
    c2.setFixedWidth(LBLWIDTH);
    c2.setFixedHeight(LBLHEIGHT);
    this.add(lblLocation,c2);

    //2nd column//

    d1=new LayoutConstraint();
    d1.sameHeightAs(lblName);
    d1.anchorToContainerTop(STEPY);
    d1.anchorToContainerLeft(COLUMN2X);
    d1.anchorToContainerRight(PADX); //26Apr2000: fixed name number field whose end wasn't showing right aligned with the other number fields (had harcoded number 10, changed to PADX)
    this.add(tfName,d1);

    d2=new LayoutConstraint();
    d2.sameHeightAs(lblLocation); //21Dec1999: was doing d2.sameHeightAs(wgtLocation) and that caused an infinite recursion
    d2.anchorToBottomOf(lblName,STEPY);
    d2.anchorToContainerLeft(COLUMN2X);
    d2.anchorToContainerRight(PADX);
    this.add(wgtLocation, d2);

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

  //

  private void nameChange() {
   if(object!=null) object.setName(tfName.getText());
   updateParent();
  }

  void tfName_actionPerformed(ActionEvent e) {
   nameChange();
  }

  void tfName_focusLost(FocusEvent e) {
   nameChange();
  }

  //

  void wgtLocation_propertyChange(PropertyChangeEvent e) {
   if(object!=null) object.setLocation2D(wgtLocation.getPoint());
   updateParent();
  }

}

