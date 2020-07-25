//Title:        Logo
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  Logo component for Avakeeo

package gr.cti.eslate.logo; //DOESN'T WORK OK!!!

import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;

public class LogoTextAreas extends JPanel
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;

 public LogoTextAreas(){
  super();
  setPreferredSize(new Dimension(300,300));
 }

 public LogoTextAreas(Component child1, Component child2){
  this();
  setLayout(new GridLayout(2,1));
  add(child1);
  add(child2);
 }

 public void setBounds(int x, int y, int width, int height) {
  super.setBounds(x,y,width,height);
  setChildSize(getComponent(0),1,6d/10d);
  setChildSize(getComponent(1),1,4d/10d);
 }

 public void setChildSize(Component child,double widthRatio, double heightRatio){
  if(child!=null)
   child.setSize(
    new Dimension(
     (int)((double)getWidth()*widthRatio),
     (int)((double)getHeight()*heightRatio)
    )
   );
 }

}