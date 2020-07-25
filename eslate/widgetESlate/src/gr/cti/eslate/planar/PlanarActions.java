//Title:        Your Product Name
//Version:      29Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  Your description

package gr.cti.eslate.planar;

import javax.swing.*;
import java.awt.event.*;
import gr.cti.utils.*;

public class PlanarActions { //29Jun2000: separated actions from Planar class

 Planar planar;

 public PlanarActions(Planar planar) {
  this.planar=planar;
 }

 ////////////

 private void setupAction(AbstractAction a,String name){
  String text=Res.localize(name);
  //System.out.println(name+"->"+text);
  a.putValue(AbstractAction.NAME,text);
  //putValue(SHORT_DESCRIPTION,text); //?
  //putValue(LONG_DESCRIPTION,text); //?
  ImageIcon icon=Res.loadImageIcon("images/"+name+".gif",text);
  if (icon!=null) a.putValue(AbstractAction.SMALL_ICON,icon); //check for null else will throw exception
 }

 ////////////

 abstract class MyAction extends AbstractAction{
  public MyAction(String name){
   setupAction(this,name); //this localizes the name too
  }
 }

 abstract class MyToggleAction extends ToggleAction{
  public MyToggleAction(String name){
   setupAction(this,name); //this localizes the name too
  }
 }

 ///////

 MyToggleAction //also have MyToggleAction for Selection and Zoom (and place them in a Tools radio menu)
  toggleAction_useSelectionTool=new MyToggleAction("Select"){
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   public void act(boolean selected){

   }
  },
  toggleAction_useZoomTool=new MyToggleAction("Zoom"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){

   }
  },
  toggleAction_viewControlPoints=new MyToggleAction("Control Points"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    planar.plot.setControlPointsVisible(selected);
   }
  },
  toggleAction_viewAxis=new MyToggleAction("Axis"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    planar.plot.setAxisVisible(selected);
   }
  },
  toggleAction_selectCartesianMapping=new MyToggleAction("Cartesian"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    if (selected) planar.plot.setMapping(PlotPanel.MAPPING_CARTESIAN);
   }
  },
  toggleAction_selectPolarMapping=new MyToggleAction("Polar"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    if (selected) planar.plot.setMapping(PlotPanel.MAPPING_POLAR);
   }
  },
  toggleAction_viewGrid=new MyToggleAction("Grid"){ //???
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    planar.plot.setGridVisible(selected);
   }
  },
  toggleAction_selectDotGrid=new MyToggleAction("Dots"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    if (selected) planar.plot.setGridType(PlotPanel.GRID_DOTS);
   }
  },
  toggleAction_selectLineGrid=new MyToggleAction("Lines"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void act(boolean selected){
    if (selected) planar.plot.setGridType(PlotPanel.GRID_LINES);
   }
  };

 ///////////

 MyAction
  action_New=new MyAction("New"){
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.plot.clearAll(); //???
   }
  },
  action_Open=new MyAction("Open"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    //...
   }
  },
  action_Print=new MyAction("Print"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.printPlot();
   }
  },
  action_Cut=new MyAction("Cut"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.plot.cut();
   }
  },
  action_Copy=new MyAction("Copy"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.plot.copy();
   }
  },
  action_Paste=new MyAction("Paste"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.plot.paste();
   }
  },
  action_Clear=new MyAction("Clear"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.plot.clear();
   }
  },
  action_SelectAll=new MyAction("Select All"){
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
   public void actionPerformed(ActionEvent e){
    planar.plot.selectAll();
   }
  };

}