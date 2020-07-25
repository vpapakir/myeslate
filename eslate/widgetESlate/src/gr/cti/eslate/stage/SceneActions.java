//Title:        Stage
//Version:      29Mar2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage;

import javax.swing.AbstractAction;
import gr.cti.utils.ToggleAction;
import java.awt.event.ActionEvent;
import javax.swing.*;
import gr.cti.eslate.stage.objects.SceneObject;
import gr.cti.eslate.utils.*;

import java.awt.Image;
import java.awt.print.PrinterJob; //14Apr2000: don't use java.awt.PrintJob!!!

import gr.cti.utils.ImageFile;

import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.utils.ObjectHash;

import java.io.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class SceneActions {

 //private static Properties printprefs = new Properties(); //static so that all physics share printprefs
 private Scene scene;

 public SceneActions(){
 }

 public SceneActions(Scene scene){
  setScene(scene);
 }

 public void setScene(Scene scene){
  this.scene=scene;
 }

//ACTIONS/////////////////////////////////////////////////////////////////////////////////

 abstract class MyAction extends AbstractAction{
  public MyAction(String name){
   Res.setupAction(this,name); //this Res.localizes the name too
  }
 }

 abstract class MyToggleAction extends ToggleAction{
  public MyToggleAction(String name){
   Res.setupAction(this,name); //this Res.localizes the name too
  }
 }

 ///////

 MyToggleAction //also have MyToggleAction for Selection and Zoom (and place them in a Tools radio menu)
  toggleAction_useSelectionTool=new MyToggleAction("Select")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   
   public void act(boolean selected){

   }
  },
  toggleAction_useZoomTool=new MyToggleAction("Zoom")
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public void act(boolean selected){

   }
  },
  toggleAction_viewCoordinates=new MyToggleAction("Coordinates")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    scene.setCoordinatesVisible(selected);
   }
  },
  toggleAction_viewControlPoints=new MyToggleAction("Control Points")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    scene.setControlPointsVisible(selected);
   }
  },
  toggleAction_viewAxis=new MyToggleAction("Axis")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    scene.setAxisVisible(selected);
   }
  },
  toggleAction_viewGrid=new MyToggleAction("Grid") //???
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    scene.setGridVisible(selected);
    //removed-for-now// gridMenuSelector.setSelected(selected);
   }
  },
  toggleAction_selectCartesianMapping=new MyToggleAction("Cartesian")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
//    if (selected) plot.setMapping(PlotPanel.MAPPING_CARTESIAN);
   }
  },
  toggleAction_selectPolarMapping=new MyToggleAction("Polar")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
//    if (selected) plot.setMapping(PlotPanel.MAPPING_POLAR);
   }
  },
  toggleAction_selectDotGrid=new MyToggleAction("Dots")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    if (selected) scene.setGridType(Scene.GRID_DOTS);
    //removed// toggleAction_viewGrid.setSelected(gridMenu.getSelection()!=null); //if a grid type gets selected, then show the grid if it isn't showing
   }
  },
  toggleAction_selectLineGrid=new MyToggleAction("Lines")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    if (selected) scene.setGridType(Scene.GRID_LINES);
    //removed// toggleAction_viewGrid.setSelected(gridMenu.getSelection()!=null); //if a grid type gets selected, then show the grid if it isn't showing
   }
  },
  toggleAction_selectSelectionTool=new MyToggleAction("Area Selection")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void act(boolean selected){
    if(selected) scene.setSelectionMode(true,this);
    else scene.setSelectionMode(false,null);
   }
  };

////////////////

 class MyNewShapeAction extends MyAction //30Jun1999
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
   
  private Class<?> shapeClass;

  public MyNewShapeAction(String name,String shapeClassName){
   super(name);
   try{
    shapeClass=Class.forName(shapeClassName);
   }catch(Exception e){
    System.out.println(e+"\nCouldn't make action that creates shape "+shapeClassName); //28Aug1999
   }
  }

  @SuppressWarnings("unchecked")
  public MyNewShapeAction(String name,Class shapeClass){
   super(name);
   this.shapeClass=shapeClass;
  }

  public void actionPerformed(ActionEvent e) {
   try{
    scene.add((SceneObject)shapeClass.newInstance());
   }catch(Exception ex){
    ex.printStackTrace(); //28Aug1999: print a stack trace showing where the object creation failed
    System.err.println(ex+"\nCouldn't create instance for shape "+shapeClass.getName()); //28Aug1999
   }
  }

 }

 ///////////

 public MyNewShapeAction[] newShapeActions=new MyNewShapeAction[]{ //30Jun1999
   new MyNewShapeAction("Rope","gr.cti.eslate.stage.objects.Rope"),
   new MyNewShapeAction("Box","gr.cti.eslate.stage.objects.Box"),
   new MyNewShapeAction("Square Box","gr.cti.eslate.stage.objects.SquareBox"),
   new MyNewShapeAction("Round Box","gr.cti.eslate.stage.objects.RoundBox"),
   new MyNewShapeAction("Ball","gr.cti.eslate.stage.objects.Ball"),
   new MyNewShapeAction("Slope","gr.cti.eslate.stage.objects.Slope"),
   new MyNewShapeAction("Spring","gr.cti.eslate.stage.objects.Spring"),
   new MyNewShapeAction("PolyLine","gr.cti.eslate.stage.objects.PolyLine"),
   new MyNewShapeAction("Quadratic Curve","gr.cti.eslate.stage.objects.QuadraticCurve"),
   new MyNewShapeAction("Cubic Curve","gr.cti.eslate.stage.objects.CubicCurve")
 };

 ///////////

 MyAction
  action_New=new MyAction("New")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   
   public void actionPerformed(ActionEvent e){
    scene.clearScene(); //8Aug1999: it isn't sufficient to just do a selectAll and a clear, cause the clearscene also removes any background image of the scene etc.
   }
  },
  action_Load=new MyAction("Load...") //16May2000
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    ESlateFileDialog f=new ESlateFileDialog(new JFrame(),Res.localize("loadSceneMsg"),ESlateFileDialog.LOAD,".scene");
    f.setDefaultExtension(new String[]{".scene"});
    f.setFile("*.scene");
          f.show();

    if(f.getFile()==null) {
     f.dispose();
     return; //1Jun2000: if user pressed CANCEL just return
    }

    String filepath=f.getDirectory()+f.getFile();
    //System.out.println(filepath);
    f.dispose(); //18May2000

    FileInputStream fis=null;
    ObjectInputStream ois=null;
    try{
     fis=new FileInputStream(filepath);
     ois=new ObjectInputStream(fis);
     scene.dispose(); //17May2000: must dispose first, before calling readObject (else inner objects will be renamed to have unique name)
     Object o=ois.readObject();
     if(o instanceof StorageStructure) //29Mar2001: compatibility
      scene.setProperties((StorageStructure)o); //scene should fire some state changed event to Stage for it to update its UI (toggle buttons etc.)
     else
      scene.setProperties((ObjectHash)o); //scene should fire some state changed event to Stage for it to update its UI (toggle buttons etc.)
    }catch(Exception ex){
     ex.printStackTrace();
     try{ois.close();}catch(Exception ex1){}
     try{fis.close();}catch(Exception ex2){}
    }
   }
  },
  action_Save=new MyAction("Save...") //16May2000
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    ESlateFileDialog f=new ESlateFileDialog(new JFrame(),Res.localize("saveSceneMsg"),ESlateFileDialog.SAVE,".scene");
    f.setDefaultExtension(new String[]{".scene"});
    f.setFile("*.scene");
          f.show();

    if(f.getFile()==null) {
     f.dispose();
     return; //1Jun2000: if user pressed CANCEL just return
    }

    String filepath=f.getDirectory()+f.getFile();
    //System.out.println(filepath);
    f.dispose(); //18May2000

    FileOutputStream fos=null;
    ObjectOutputStream oos=null;
    try{
     fos=new FileOutputStream(filepath);
     oos=new ObjectOutputStream(fos);
     oos.writeObject(scene.getProperties());
     oos.flush();
    }catch(Exception ex){
     ex.printStackTrace();
     try{oos.close();}catch(Exception ex1){}
     try{fos.close();}catch(Exception ex2){}
    }
   }
  },
  action_Print=new MyAction("Print...") //14Apr2000: now using Java2-style printing
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    //System.out.println("PlotPanel:print");
    PrinterJob job = PrinterJob.getPrinterJob();

    /*PageFormat pf=*/job.pageDialog(job.defaultPage()); //show page format dialog
    job.setPrintable(scene); //must be called after the "job.pageDialog" call, or else a "job.validatePage(pf)" is needed here
                             //bad for users: asks whether to "print pages 1-9999": can't tell Java how many pages you have: use Book API instead

    if(job.printDialog())
     try{job.print();}
     catch(Exception printerException){
      showError(Res.localize("Failed to print")); //10May2000
      System.err.println("Error while printing scene");
      printerException.printStackTrace(); //21May2000
     }
   }
  },
  action_Photo=new MyAction("Photo...") //9May2000
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  
   public void actionPerformed(ActionEvent e){
    Image img=scene.getPhoto();

    if(img!=null){
     /*
     IconEditorDialog ic=new IconEditorDialog(); //that one shows limited sizes of images... save directly instead using code from Canvas
     ImageIcon icon=new ImageIcon(img);
     try{ ic.setIcon(icon); } catch(Exception ex) { System.err.println(e); }
     ic.showDialog(scene);
     */
     ImageFile.saveImage(img,Res.localize("Save image")); //10May2000

     img.flush(); //dispose of the image data!!! (user may have saved it from the image editor dialog)
    }else System.err.println("SceneActions.action_Photo: scene.getPhoto() returned null!"); //10May2000
   }
  },

  ///////////////////////////////////////////////
  action_Cut=new MyAction("Cut")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.cut(); //12Apr2000
   }
  },
  action_Copy=new MyAction("Copy")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.copy(); //12Apr2000
   }
  },
  action_Paste=new MyAction("Paste")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e)
   {
    scene.paste(); //12Apr2000
   }
  },
  action_Clear=new MyAction("Clear")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.clearSelection();
   }
  },
  action_SelectAll=new MyAction("Select All")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.selectAll();
   }
  },
  action_Clone=new MyAction("Clone") //13Apr2000
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.cloneSelection();
   }
  },
////////////////////////////////////////////////////////////////////////////////
  action_newJTextField=new MyAction("TextField")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    JTextField tf=new JTextField("TESTING...");
    scene.add(tf);
    tf.setBounds(50,50,100,100);
    gr.cti.eslate.scripting.logo.ComponentPrimitives.scriptableObjects.putNumbered("TextField",tf);
    gr.cti.eslate.scripting.logo.ComponentPrimitives.loadPrimitives(new String[]{"gr.cti.eslate.scripting.logo.JTextComponentPrimitives"}); //!!! this won't work if Logo is loaded after, should track all needed primitives (load/unload) and when new machine comes up load them into it (not just ask all LogoScriptables, since some objects may not be LogoScriptables and someother may have loaded the primitives for them - of course that other could be LogoScriptable and dynamically return apart from its own primitive groups, the prim groups needed by its non-LogoScriptable children...
   }
  },
////////////////////////////////////////////////////////////////////////////////
  action_zoomIn=new MyAction("Zoom In")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.zoom(2);
   }
  },
  action_zoomOut=new MyAction("Zoom Out")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
    
   public void actionPerformed(ActionEvent e){
    scene.zoom(1/2d); //26-3-1999: bug-fix: now unzoom is OK (had 1/2 which is an int and gets compiled to 0)
   }
  },
////////////////////////////////////////////////////////////////////////////////
  action_Split=new MyAction("Split") //29Jun1999: break any bindings between any of the selected control points
  {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
    
  public void actionPerformed(ActionEvent e){
    scene.split();
   }
  };

 private void showError(String error){
  JOptionPane.showMessageDialog(scene,error,Res.localize("Error!"),JOptionPane.ERROR_MESSAGE);
 }


}
