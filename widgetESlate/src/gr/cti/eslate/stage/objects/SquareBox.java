//Title:        Physics
//Version:      15May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

package gr.cti.eslate.stage.objects;

import java.awt.Shape;

import gr.cti.shapes.*;
import gr.cti.eslate.stage.*;
import gr.cti.eslate.stage.models.*;

public class SquareBox extends Box implements HasLength{

  static final long serialVersionUID = 5740019320803175686L; //1Jun1999

  // LogoScriptable ///////////
  public String[] getSupportedPrimitiveGroups(){
   String[] p1=super.getSupportedPrimitiveGroups();
   int len=p1.length;
   String[] p2=new String[len+1];
   System.arraycopy(p1,0,p2,0,len);
   p2[len]="gr.cti.eslate.scripting.logo.LengthPrimitives";
   return p2;
  }

  public SquareBox() {
   this(50,50,40);
  }

  public SquareBox(double side){
   this(50,50,side);
  }

  public SquareBox(double cx,double cy,double side){
   super(); //28Aug1999: call super in order to register as scriptable
   this.center.setLocation(cx,cy); //already allocated by super()
   this.edge.setLocation(cx+side,cy); //already allocated by super()
  }

  // OVERRIDEs //////////////////////////////

  public String getBaseName(){ //30Jun1999
   return Res.localize("SquareBox"); //30Nov1999: localized
  }

  public Shape calculateShape(){ //30Jun1999: now caching is done at ancestor
   return new Square(center.getLocationX(),center.getLocationY(),edge.getLocationX(),edge.getLocationY());
  }

  // HasWidth override //////////////
  public void setObjectWidth(double value){
    super.setObjectWidth(value);
    //don't forget super here too, else calls setObjectHeight, which will call
    //super.setObjectHeight, and, if have forgotten too, call setObjectWidth, that is do a
    //recursing LOOP!!! (will cause a stack overflow error)
    super.setObjectHeight(value);
  }

  // HasHeight override /////////////
  public void setObjectHeight(double value){
    super.setObjectHeight(value);
    super.setObjectWidth(value); //don't forget super here too
  }

  // HasLength //1Jun1999

  public double getLength(){
   return getObjectWidth();
  }

  public void setLength(double value){
   setObjectWidth(value);
   //setObjectHeight(value); //setObjectWidth will also call setObjectHeight, so don't need to call it twice
  }
}
