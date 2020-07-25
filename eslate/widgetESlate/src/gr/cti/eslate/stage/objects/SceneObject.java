//## ALLOW ALL OBJECTS to be placed in a container in Design mode and their properties to be set
//(displayed as icons)
//## then all the children of that container would be DATA MODELS shown by the Physics Scene
//VIEW as 2D objects or to some other view as 3D objects etc.
//## also could be dragged out of a Physics view onto a map as icons and back into some other view

//Title:        SceneObject
//Version:      4Apr2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  The base scene object

package gr.cti.eslate.stage.objects;

import gr.cti.eslate.stage.BaseObject;

import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.Container;
import java.awt.Shape;
import java.awt.Image;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.net.*;
import java.beans.PropertyChangeEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import javax.swing.ImageIcon;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.stage.models.AsShape;
import gr.cti.eslate.stage.models.IControlPointContainer;
import gr.cti.eslate.stage.ControlPoint;
import gr.cti.eslate.stage.models.*;

public abstract class SceneObject extends BaseObject implements AsSceneObject, //8Dec1999
                                                      java.beans.PropertyChangeListener,
                                                      IControlPointContainer, //STORABLE
                                                      //don't forget this one! now has
                                                      //"SelectionRequired" property to require
                                                      //that a selection always exists (this can be
                                                      //changed at runtime)
                                                      AsShape,
                                                      //24Feb2000: SceneObjects are now Cloneable!
                                                      Cloneable{

  //1Jun1999: serial-version, so that new vers load OK
  static final long serialVersionUID = 68128468572810510L;
  //public final static String STORAGE_VERSION="1"; //29Aug2000
  public final static int STORAGE_VERSION=1; //6/6/2002
  private transient Container container;
  //30Jun1999: made private, now descendents use calculateShape to calculate their shape, and
  //the ancestor handles caching via the getShape method that is called by the container when
  //it repaints
  private transient Shape cachedShape;
  private Image image;
  //FillPattern// //???
  //private String fillPattern;
  public TexturePaint texturePaint;
  private Color color=Color.red; //must set a default value, else object will be invisible
  //made private, now descendents should call setControlPoints, so that object is made a
  //listener of the new control point (to reshape when they change) and stops listening to
  //(& thus holding alive) the old control points (if any)
  private ControlPoint[] controlPoints;
  private boolean updatingDisabled;
  public static final String CONTROL_POINTS_PROPERTY="controlPoints";
  public static final String COLOR_PROPERTY="color";
  public static final String IMAGE_PROPERTY="image"; //12Jan2000
  public static final String SHAPE_PROPERTY="shape";

  private boolean preparedForCopy = false;

  //CONSTRUCTOR
  //30Jun1999: register with scripting using the descendent's overriden base name routine
  //28Aug1999: bug-fix: had "public void PhysicsObject" in declaration, so that wasn't getting
  //called, since this wasn't the constructor, but just a method called "PhysicsObject" that
  //returned no value ("void")
  public SceneObject(){
  }

  //DESTRUCTOR
  public void dispose(){
    setImage((Image)null); //12May2000
    removeAllControlPoints(); //15May2000
    //11May2000: call this, since the nameService unregistration code was moved there
    super.dispose();
  }

  public void setContainer(Container c){
    container=c;
  }

  public void update(){
    //23Jul1999: some operations need to be atomic and call update() only when they are done:
    //for example when translating an object, its control points get translated one by one and
    //throw back property change events: don't want to refresh at each of these events, only when
    //we're finished and the translation of the object (that is for all of its control points has
    //been done)

    //1Jun1999: if update has been called, then means the shape has been changed and needs
    //recalculation when the container refreshes
    if(!updatingDisabled){
      cachedShape=null;

      //Must fire shape changes after the invalidation of the cached shape (cachedShape=null)
      //firePropertyChange(SHAPE_PROPERTY,null,null); //don't pass the shape: mustn't calculate
      //the shape, unless some listener asks for it: else BATCH updates won't be that effective,
      //since all shapes [even not slave or master ones] will get recalculated at each change,
      //whereas non-bound shapes should only get to be calculated at next repaint (end of
      //BATCH update)
      if(container!=null)
        ((AsScene)container).refresh();
      //1Jun1999: not calling repaint, calling refresh so that it can be disabled when batch-drawing
    }
  }

  /**
   * Added by Drossos Nicolas in order to repaint only the specified area and not everything
   * @param r   The rectangle to repaint.
   */
  public void update(Rectangle r){
    //23Jul1999: some operations need to be atomic and call update() only when they are done:
    //for example when translating an object, its control points get translated one by one and
    //throw back property change events: don't want to refresh at each of these events, only when
    //we're finished and the translation of the object (that is for all of its control points has
    //been done)

    //1Jun1999: if update has been called, then means the shape has been changed and needs
    //recalculation when the container refreshes
    if(!updatingDisabled){
      cachedShape=null;

      //Must fire shape changes after the invalidation of the cached shape (cachedShape=null)
      //firePropertyChange(SHAPE_PROPERTY,null,null); //don't pass the shape: mustn't calculate
      //the shape, unless some listener asks for it: else BATCH updates won't be that effective,
      //since all shapes [even not slave or master ones] will get recalculated at each change,
      //whereas non-bound shapes should only get to be calculated at next repaint (end of
      //BATCH update)
      if(container!=null)
        ((AsScene)container).refresh(r);
      //1Jun1999: not calling repaint, calling refresh so that it can be disabled when batch-drawing
    }
  }

  //PropertyChangeListener
  public void propertyChange(PropertyChangeEvent e){
    //if(e.getName.equals("location"))
    //System.out.println("PhysicsObject:PropertyChange");
    update(); //recalculates the shape & tells our container to refresh
  }

  //AsShape
  public abstract Shape calculateShape();

  public Shape getShape(){
    //System.out.println("Shape.getShape()");
    if (cachedShape==null){
      //System.out.println("calulating shape for object "+getName());
      cachedShape=calculateShape();
    }
    return cachedShape;
  }

  //HasImage
  public Image getImage(){ //12Jan2000
    return image;
  }

  public void setImage(Image i){ //12Jan2000
    if(image!=null)
      image.flush(); //12May2000
    image=i;
    update();
  }

  public void setImage(String filename){ //12Jan2000
    //09May2000: faster: if passed an empty or null string then remove the current image
    //[must have a cast to an Image object for the null param of setImage(null)]
    //Birb-2Apr2001: don't use .equals("") or =="" but .length()==0 instead!
    if(filename.length()==0 || filename==null)
      setImage((Image)null);
    else
      setImage(new ImageIcon(filename,null));
    //4Apr2001-Birb: fixed by using "new ImageIcon" instead of Res.loadImageIcon
  }

  public void setImage(ImageIcon i){ //24Feb2000
    //09May2000: if passed a null imageIcon, set image to null too
    image=(i!=null)?i.getImage():null;
    update();
  }

  public void setImage(URL image){ //4Apr2001-Birb: added
    setImage(new ImageIcon(image,""));
  }

  //private boolean sleep(){try{Thread.sleep(100);}catch(InterruptedException e){};return true;}
  private void setFillPattern(Image image){
    try{
      int width=image.getWidth(null);
      int height=image.getHeight(null);
      if(width<=0)
        width=10;
      if(height<=0)
        height=10;
      //Create a buffered image texture patch of size width*height
      BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D big = bi.createGraphics();
      //Render into the BufferedImage graphics to create the texture
      big.drawImage(image,0,0,null);
      //Create a texture paint from the buffered image
      Rectangle r = new Rectangle(0,0,width,height);
      texturePaint = new TexturePaint(bi,r);
      //System.out.println(width+"x"+height);
    }catch(Exception e){e.printStackTrace();}
  }

  public void setFillPattern(String image){
    //fillPattern=image;
    setFillPattern(Toolkit.getDefaultToolkit().createImage(image));
    update();
  }

  //HasColor
  public Color getColor(){
    return color;
  }

  public void setColor(Color value){
    if(value!=getColor()){ //1Jun1999: if set to current value, ignore
      color=value;
      update();
    }
  }

  //IControlPointContainer
  public void removeAllControlPoints(){ //30Jun1999
    if(controlPoints!=null){
      for(int i=0;i<controlPoints.length;i++){
        ControlPoint p=controlPoints[i];
        p.removePropertyChangeListener(this);
        //15May2000: must do this so that these control points do unregister from the name service!
        p.dispose();
      }
      controlPoints=null;
    }
  }

  public ControlPoint[] getControlPoints(){
    return controlPoints;
  }

  //15May2000: now uses the passed-in control points and doesn't just copy the locations of
  //them (needed cause want the new control points with their names etc. and not just their
  //locations!)
  public void setControlPoints(ControlPoint[] cp){
    removeAllControlPoints();
    controlPoints=cp;
    //18May2000 it's usually not OK to pass null to this method: descendents like Ball etc. -
    //which need control points - will throw an exception in that case!
    if(cp!=null)
      for(int i=0;i<cp.length;i++)
        cp[i].addPropertyChangeListener(/*ControlPoint.LOCATION_PROPERTY,*/this);
  }

  //21May2000: set the locations of the object's control points to the locations of the
  //passed-in control points
  public void setControlPointsLocations(ControlPoint[] cp){
    if(cp!=null)
      for(int i=cp.length;i-->0;)
        controlPoints[i].setLocation(cp[i]);
      //the passed-in control points' array muct be of size equal to the count of our control points
  }

  //HasLocation2D
  //override to provide other center then control point 0
  public Point2D getLocation2D(){
    ControlPoint[] p=getControlPoints(); //this won't return a copy
    if (p!=null && p.length>0)
      return p[0].getLocation2D(); //.getLocation2D returns copy, maybe it shouldn't
    else
      return null;
  }

  //override if don't want to translate using control point 0 as center
  public void setLocation2D(Point2D newPos){
    //15-4-1999: not using getControlPoints()[0] any more, in case a descendent overrides the
    //getLocation() method
    Point2D oldPos=getLocation2D();
    //just in case where no control points exist - there usually is no "location" specified
    //then (it defaults to the 1st control point, but descendents could implement an object
    //that doesn't use control points at and uses some other "getLocation2D" implementation)

    //14Dec1999-speedup: do nothing if set again to the current location
    if (oldPos!=null && !oldPos.equals(newPos)){
      //15-4-1999: now using the translate(dragPos,dropPos) utility method instad of directly
      //using the translate(dx,dy) method
      translate(oldPos,newPos);
    }
    //update() not needed, will be done by the "translate" call
  }

  public void translate(double dx,double dy){
    //System.out.println("translate: "+dx+" "+dy);
    if (dx==0 && dy==0)
      return; //14Dec1999-speedup: do nothing in case of a translation by a (0,0) offset
    AffineTransform a=AffineTransform.getTranslateInstance(dx,dy);
    ControlPoint[] p=getControlPoints();
    updatingDisabled=true; //stop updates...
    //23Jul1999: wrapping this inside a block which disables and then reenables updating (don't
    //want the object to redraw till all of its control points have been translated)
    //8May2000: optimized for-loop
    for(int i=p.length;i-->0;)
      p[i].transform(a);
    updatingDisabled=false; //...reenable updates
    update(); //and then do the update!
  }

  public void translate(Point2D dragPos, Point2D dropPos){ //15-4-1999
    translate(dropPos.getX()-dragPos.getX(),dropPos.getY()-dragPos.getY());
  }

  public ESlateFieldMap2 getProperties(){
    ESlateFieldMap2 properties=new ESlateFieldMap2(STORAGE_VERSION);
    //ESlateFieldMap properties=super.getProperties(); //19Apr2000: get any ancestor properties too
    //if(properties==null)
    //  properties=new ESlateFieldMap(STORAGE_VERSION);
    properties.put("NAME_PROPERTY",getName());
    properties.put("CONTROL_POINTS_PROPERTY",getControlPoints());
    properties.put("COLOR_PROPERTY",getColor());
    Image img=getImage();
    //12Jan2000 //25Feb2000: must check for img!=null, since Hashtables don't allow placing
    //neither a null key nor a null value!
    if(img!=null){
      properties.put("IMAGE_PROPERTY",new NewRestorableImageIcon(img));
    }
    return properties;
    //25Nov1999: descendents should override that to return a hashtable with all the properties'
    //name-value pairs (they should call super.getProperties first and add entries to the hashtable
    //it returns and return that one - if super.getProperties() returns null, then create and
    //return a new Hashtable instance)
  }

  /*public void setProperties(ESlateFieldMap properties){
    System.out.println("SceneObject setProperties");
    if(properties==null)
      return;
    //allow ancestor's to set their properties too from the same hash
    //super.setProperties(properties);
    //must use try-catch for eacb property, since it might be missing (when loading some older
    //saved state) set default value in case this property is missing (due to some or old or
    //corrupted/wrong state loading)
    try{
      setName((String)properties.get("NAME_PROPERTY"));
    }catch(Exception e){
      System.out.println("Exception in setProperties...");
      setName(getBaseName());//will make a unique name from the BaseName
    }

    setControlPoints((ControlPoint[])properties.get("CONTROL_POINTS_PROPERTY"));
    try{
      setColor((Color)properties.get("COLOR_PROPERTY"));
    }catch(Exception e){
      setColor(Color.red);
    }

    Object im = properties.get("IMAGE_PROPERTY");
    if (im!=null){
      if (im instanceof ImageIcon) {
        setImage((ImageIcon)im);
      }else{
        setImage(((NewRestorableImageIcon)im).getImage());
      }
    }
    //25Nov1999: descendents should override that to set all the properties for the hashtable
    //with the name-value pairs (they should call super.setProperties too)
    System.out.println("SET properties in SCENE OBJ END");
  }*/

  //LogoScriptable
  public String[] getSupportedPrimitiveGroups(){
    //16May2000: now the BaseObject ancestor is LogoScriptable itself
    String[] p1=super.getSupportedPrimitiveGroups();
    int len=p1.length;
    String[] p2=new String[len+1]; //+2 if FillPatternPrimitives support gets implemented
    System.arraycopy(p1,0,p2,0,len);
    p2[len]="gr.cti.eslate.scripting.logo.ColorPrimitives";
    //p2[len+1]="gr.cti.eslate.scripting.logo.FillPatternPrimitives"; //commented out, not implemented yet
    return p2;
  }

  public void setPreparedForCopy(boolean b){
      preparedForCopy = b;
  }

  public boolean isPreparedForCopy(){
      return preparedForCopy;
  }

  //Cloning
  public Object clone() throws CloneNotSupportedException {
    //this calls BaseObject.clone() which also handles the name of the clone
    SceneObject o=(SceneObject)super.clone();
    o.setColor(getColor());
    o.setImage(getImage());
    //21May2000: don't just do setControlPoints(getControlPoints()) cause the clone and the
    //original object would then share the same control points and thus be always overlapping
    //and non separable
    o.setControlPointsLocations(getControlPoints());
    return o;
  }
}
