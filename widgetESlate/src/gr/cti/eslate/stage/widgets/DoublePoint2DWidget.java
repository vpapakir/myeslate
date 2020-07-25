//Title:        Stage/DoublePoint2DWidget
//Version:      17May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.widgets;

import javax.swing.*;
import gr.cti.utils.JNumberField;
import java.awt.geom.Point2D;

import com.thwt.layout.*;
import java.beans.*;

public class DoublePoint2DWidget extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JLabel lblX, lblY;
  private JNumberField nfX, nfY;

  private static final int WIDTH=30;
  private static final int PADX=0;
  private static final int PADY=0;

  public DoublePoint2DWidget(){
   super();
   try{jbInit();}catch(Exception e){e.printStackTrace();}
  }

  private void jbInit(){
   setLayout(new SmartLayout());

   lblX=new JLabel("X:");
   lblY=new JLabel("Y:");

   /////////////

   nfX=new JNumberField();
   nfX.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
    public void propertyChange(PropertyChangeEvent e) {
     nfX_propertyChange(e);
    }
   });

   /////////////

   nfY=new JNumberField();
   nfY.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
    public void propertyChange(PropertyChangeEvent e) {
     nfY_propertyChange(e);
    }
   });

   /////////////

   nfX.setDecimalDigits(3); //15Apr2000
   nfY.setDecimalDigits(3); //15Apr2000

   LayoutConstraint c1=new LayoutConstraint();
   c1.anchorToContainerLeft(PADX);
   c1.setFixedWidth(WIDTH);
   c1.anchorToContainerTop(PADY);
   c1.anchorToContainerBottom(PADY);
   add(lblX,c1);

   LayoutConstraint c2=new LayoutConstraint();
   c2.anchorToRightOf(lblX,0);
   c2.anchorRightToHProportionOf(this,0.47d);
   c2.anchorToContainerTop(PADY);
   c2.anchorToContainerBottom(PADY);
   add(nfX,c2);

   LayoutConstraint c3=new LayoutConstraint();
   c3.anchorLeftToHProportionOf(this,0.53d);
   c3.setFixedWidth(WIDTH);
   c3.anchorToContainerTop(PADY);
   c3.anchorToContainerBottom(PADY);
   add(lblY,c3);

   LayoutConstraint c4=new LayoutConstraint();
   c4.anchorToRightOf(lblY,0);
   c4.anchorToContainerRight(PADX);
   c4.anchorToContainerTop(PADY);
   c4.anchorToContainerBottom(PADY);
   add(nfY,c4);
  }

  public void setPointXY(double x,double y){
   nfX.setNumber(x);
   nfY.setNumber(y);
  }

  public void setPointX(double x){ nfX.setNumber(x); }
  public void setPointY(double y){ nfY.setNumber(y); }

  //

  public double getPointX(){ return nfX.getdouble(); }
  public double getPointY(){ return nfY.getdouble(); }

  //

  public void setPoint(Point2D p){
   setPointXY(p.getX(),p.getY());
  }

  public gr.cti.shapes.DoublePoint2D getPoint(){ //8May2000: temp: still-needing to return DoublePoint2D object's for velocity etc. storable properties of objects
   return new gr.cti.shapes.DoublePoint2D/*Point2D.Double*/(getPointX(),getPointY());
  }

  //

  public void firePropertyChange(){ //16May2000
   firePropertyChange("value",null,null); //don't pass any old or new value with the event
  }

  void nfX_propertyChange(PropertyChangeEvent e) {
   firePropertyChange();
  }

  void nfY_propertyChange(PropertyChangeEvent e) {
   firePropertyChange();
  }

}
