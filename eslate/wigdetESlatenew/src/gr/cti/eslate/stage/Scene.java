//...add: some GUI to do one-way binds and to display those as pointed arrows of bound point to
//master point
//...add: when one way bound a point is constrained by its master and can't move unless the master
//moves
//...allow binding onto a path, so that a control point can't move out of it (the path will be a
//vetoable listener of the point and shall throw an exception when the point get out of the path)
//...allow a one-way bind to take a distance value, so that when a bound point tries to move to
//an illegal point (more distant), "move" on the axis of the illegal->master positions, till the
//point gets legal (distance is "just" OK)

//Version: 5/6/2002

package gr.cti.eslate.stage;

import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import gr.cti.eslate.stage.objects.*;
import gr.cti.eslate.stage.models.*;
import gr.cti.eslate.utils.*;
import gr.cti.shapes.*;
import gr.cti.utils.*;
import gr.cti.eslate.stage.constraints.models.IConstraint;
import gr.cti.eslate.stage.constraints.models.IConstraintContainer;
import gr.cti.eslate.stage.constraints.base.*;
import gr.cti.eslate.stage.constraints.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.stage.customizers.*;
import java.awt.print.*;
import gr.cti.eslate.base.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class Scene extends JPanel implements AsScene, LogoScriptable, HasName, Externalizable,
                                            Printable, IConstraintContainer, ESlatePart{

  //16Oct1999: serial-version, so that new vers load OK
  static final long serialVersionUID = 16101999L;
  //public final static String STORAGE_VERSION="2";
  public final static int STORAGE_VERSION=2;
  private JCheckedActionPopup ppConstraintsPopup;
  private JCheckedActionPopup psConstraintsPopup;
  private ArrayList<IConstraint> constraints=new ArrayList<IConstraint>();
  private boolean refreshEnabled=true;
  //3Nov1999: reuse control point shape
  private RectangularShape controlPointShape=new Circle2D(0,0,CONTROL_POINT_RADIUS);
  private Rectangle2D selector=new Rectangle2D.Double(); //26Apr2000: create once and reuse!
  private boolean doingSelection; //=false; //26Apr2000
  private ToggleAction selectorAction; //26Apr2000
  //using a static clipboard so that we can do cut/copy/paste from one Scene to another
  //(temp - use system or E-Slate clipboard in the future)
  //19May2000: now using a byte array as clipboard
  private static byte[] clipboard=null;
  //3Nov1999: buffer to copy component's transform data at paintComponent invokation,
  //before touching it and setting it as the new transform (at paint's ending we restore
  //the old transform)
  private AffineTransform newTransform=new AffineTransform();
  boolean bkgrImageVisible=true;
  Image bkgrImage;
  private Color COLOR_AXIS=Color.darkGray;
  private Color COLOR_GRID=Color.black;
  //15-4-1999: using blue for control points for more visibility
  private Color COLOR_CONTROLPOINT=Color.blue;
  private Color COLOR_CONTROLPOINT_SELECTED=Color.cyan; //30Jun1999

  private static final Stroke DOTTED_THIN_STROKE=new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                                      BasicStroke.JOIN_MITER, 10.0f, new float[]{0,10,5}, 0.0f);
  private static final Stroke NORMAL_STROKE=new BasicStroke();

  private Stroke axisStroke=NORMAL_STROKE; //14Apr2000
  private Stroke selectionStroke=DOTTED_THIN_STROKE; //14Apr2000
  private Stroke shapesStroke=NORMAL_STROKE; //14Apr2000

  public static final double CONTROL_POINT_RADIUS=3; //?
  public static final double CONTROL_POINT_RADIUS_SQR=CONTROL_POINT_RADIUS*CONTROL_POINT_RADIUS;

  private AffineTransform worldToViewTransform,viewToWorldTransform;
  //private AffineTransform identity=new AffineTransform();
  //private AffineTransform rotation=AffineTransform.getRotateInstance(Math.PI/20);

  private JLabel coordinatesLabel;
  private MyMouser myMouser=new MyMouser();

  private ArrayList<Object> objects=new ArrayList<Object>();
  private ArrayList<Object> controlPointsSelection=new ArrayList<Object>();
  private ArrayList<Object> objectsSelection=new ArrayList<Object>();
  private ESlateHandle sceneHandle;

  //constants
  public static final int GRID_DOTS=0; //the default grid type
  public static final int GRID_LINES=1;

  //Property fields//
  private boolean controlPointsVisible;
  private boolean gridVisible;
  private final static double DEFAULT_GRID_SIZE = 100.0;
  private double gridSize = DEFAULT_GRID_SIZE;
  private boolean axisVisible;
  //15Oct1999 //20Oct1999: made this true by default (so coords show at new physics' instances)
  private boolean coordinatesVisible=true;
  private boolean objectMovementEnabled=true; //22Oct1999
  private boolean controlPointMovementEnabled=true; //22Oct1999
  private boolean viewMovementEnabled=true;
  private boolean objectsAdjustable=true;
  private boolean marksOverShapes/*=false*/; //15Nov1999
  //...we don't need a "color" field, keeping it at the component's background
  private int gridType; //27Nov1999 //25Feb2000: make "int" from "byte" for convenience

  public SceneActions actions; //21May2000: reuse one SceneActions object

  public static final String OBJECTS_PROPERTY="objects";
  //18May2000-removed// public static final String CONSTRAINTS_PROPERTY="constraints";
  public static final String OBJECTS_AND_CONSTRAINTS_PROPERTY="objectsAndContstraints";

  public static final String WORLD_TO_VIEW_TRANSFORM_PROPERTY="worldToViewTransform";

  public static final String COLOR_PROPERTY="color";
  public static final String OBJECT_MOVEMENT_ENABLED_PROPERTY="objectMovementEnabled";
  public static final String CONTROL_POINT_MOVEMENT_ENABLED_PROPERTY="controlPointMovementEnabled";
  public static final String MARKS_OVER_SHAPES_PROPERTY="marksOverShapes";
  public static final String VIEW_MOVEMENT_ENABLED_PROPERTY="viewMovementEnabled";

  //23Feb2000: this was changed to "coordinatesVisible" from "coordinatesLabelVisible", so older
  //saved microworlds will use the default value (true) for coordinatesVisible property and ignore
  //the old saved one
  public static final String COORDINATES_VISIBLE_PROPERTY="coordinatesVisible";
  public static final String CONTROL_POINTS_VISIBLE_PROPERTY="controlPointsVisible"; //25Apr2000
  public static final String GRID_VISIBLE_PROPERTY="gridVisible"; //25Feb2000
  public static final String AXIS_VISIBLE_PROPERTY="axisVisible"; //20Apr2000

  public static final String GRID_TYPE_PROPERTY="gridType"; //24Feb2000
  public static final String IMAGE_PROPERTY="image"; //25Feb2000

  public static final String GRID_SIZE_PROPERTY = "gridSize";

  public static final String OBJECTS_ADJUSTABLE_PROPERTY = "objectsAdjustable";

  // CONSTRUCTOR
  public Scene(){
    super();
    //31-8-1998 (put at start cause affects listeners)
    enableEvents(ComponentEvent.COMPONENT_RESIZED);

    setDoubleBuffered(true); //24Sep1999
    setOpaque(true); //30Sep1999
    actions=new SceneActions(this);
    addMouseListener(myMouser);
    addMouseMotionListener(myMouser);
    setLayout(null); //15-4-1999
    addCoordinatesLabel();
    setKeyboardHandlers();
  }

  public void setKeyboardHandlers(){
    //Ctrl+A=select all
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_MASK,false),
                  actions.action_SelectAll);
    //DEL=clear
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0,false),actions.action_Clear);
    //Ctrl+X=cut
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK,false),actions.action_Cut);
    //Ctrl+C=copy
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK,false),actions.action_Copy);
    //Ctrl+V=paste
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK,false),actions.action_Paste);
    //Ctrl+N=new
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK,false),actions.action_New);
    //Ctrl+L=load
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_MASK,false),actions.action_Load);
    //Ctrl+S=save
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK,false),actions.action_Save);
    //Ctrl+F=photo ("f"oto)
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_MASK,false),actions.action_Photo);
    //Ctrl+P=print
    setKeyAction(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK,false),actions.action_Print);
  }

  private void setKeyAction(KeyStroke keystroke,Action action){ //21May2000
    //from G.Tsironis code snippet
    registerKeyboardAction(action, keystroke, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  public void addCoordinatesLabel(){ //30Aug1999
    //15-4-1999: adding a JLabel to show the current world coord [!!! this causes internally
    //thrown and consumed RasterLockingExceptions when moving the mouse and one or more shapes
    //have been added to the scene !!!]
    add(coordinatesLabel=new JLabel());
    coordinatesLabel.setVisible(coordinatesVisible); //15Oct1999
    coordinatesLabel.setBounds(10,10,200,20); //23Feb2000: made the location label wider
  }

  public void removeHostedComponents(){
    removeAll();
    //30Aug1999: must add the location label again after removing all children
    addCoordinatesLabel();
  }

  // DESTRUCTOR
  public void dispose(){ //29Aug1999
    clearScene();
    //not needed: we don't register the Scene object to the nameservice for now (cause Stage
    //registers and uses the Scene as one of its facets)
    //nameSupport.unregister();
  }

  // HasName
  public String getName(){ //30Nov1999
    if (sceneHandle != null)
      return sceneHandle.getComponentName();
    else
      return null;
    //return nameSupport.getName();
  }

  public void setName(String value){ //30Nov1999
    try{
      sceneHandle.setComponentName(value);
    }catch(Exception e){
        System.out.println("exception in naming scene");
    }
    //nameSupport.setName(value);
  }

  public Image getPhoto(){ //10May2000
    return Res.getSnapshotImage(this);
  }

  public void addJustBelow(SceneObject objToAdd, SceneObject objOnTop){ //31Mar2000
    int index=objects.indexOf(objOnTop);
    if(index<0) {
      error("addJustBelow tried to add an object below one not found in the Scene's objects' list");
      return;
    }
    objects.add(index, objToAdd);
    sceneHandle.add(objToAdd.getESlateHandle());
    objToAdd.setContainer(this);
    refresh();
  }

  public void add(SceneObject obj)
  {
    Rectangle2D r = getWorldViewRect();
    Point2D.Double p = new Point2D.Double(
      r.getX() + r.getWidth() / 2.0,
      r.getY() + r.getHeight() / 2.0
    );
    obj.setLocation2D(p);

    objects.add(obj); //26Nov1999: made Java1.1 compatible
    sceneHandle.add(obj.getESlateHandle());
    obj.setContainer(this);
    //24Feb2000: now when adding a new object, it won't do a repaint (and thus show up) if
    //refresh is disabled [refresh() is called instead of repaint()]
    refresh();
    //Shape s = getViewToWorldTransform().createTransformedShape(obj.getShape());
    //refresh(s.getBounds());
  }

  @SuppressWarnings("unchecked")
  public void addSceneObjects(ArrayList v){ //13Apr2000
    for(int i=0;i<v.size();i++){
      add((SceneObject)v.get(i));
      sceneHandle.add(((SceneObject)v.get(i)).getESlateHandle());
    }
  }

  @SuppressWarnings("unchecked")
  public ArrayList getObjects(){
    return objects;
  }

  //15-4-1999: now removes old objects from scripting engine's namespace
  @SuppressWarnings("unchecked")
  public void setObjects(ArrayList objects){
    //must remove all objects from the scripting engine, so that new ones can register with
    //any of their names
    removeAllObjects();
    if(objects!=null){ //30Nov1999: support for passing null to just remove all objects
      for(int i=0,count=objects.size();i<count;i++){ //must add in the same order
        //don't just do this.objects=objects, else the objects won't get a handle back to the
        //PhysicsPanel to refresh it when their display changes
        SceneObject sObj = (SceneObject)objects.get(i);
        add(sObj);
        if (sObj instanceof PhysicsObject) {
          PhysicsObject pObj = (PhysicsObject)sObj;
          pObj.restoreDeferredLocation();
        }
        //sceneHandle.add(((SceneObject)objects.get(i)).getESlateHandle());
      }
    }
    refresh();
    //repaint();
  }

  @SuppressWarnings("unchecked")
  public ArrayList getConstraints(){ //30Nov1999
    return constraints;
  }

  @SuppressWarnings("unchecked")
  public void setConstraints(ArrayList cons){ //30Nov1999
    removeAllConstraints(); //remove existing constraints
    //set new ones if any
    if(cons!=null) {
      //constraints = cons;
      int nConstraints = cons.size();
      constraints = new ArrayList<IConstraint>(nConstraints);
      for (int i=0; i<nConstraints; i++) {
        constraints.add((IConstraint)cons.get(i));
      }
      for (int i=0; i<nConstraints; i++) {
        AbstractConstraint c = (AbstractConstraint)cons.get(i);
        String[] names = c.getPendingNames();
        if (names != null) {
          try {
            c.setMembersByName(names, sceneHandle);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public ArrayList getObjectsSelection(){
    //12May2000: returning a clone of the objects selection vector (cause in some routines we
    //dump the current selection object and pass it to the clipboard)
    return (ArrayList)objectsSelection.clone();
  }

  //26Jul1999: returning the selection Vector itself, if client modifies it, should then repaint us
  @SuppressWarnings("unchecked")
  public ArrayList getControlPointsSelection(){
    return controlPointsSelection;
  }

  public void processComponentEvent(ComponentEvent e){
    super.processComponentEvent(e); //do this first!!!
    //18Oct1999: do this only when worldToViewTransform is null (so it'll work at component
    //start, bug not at microworld state loads... at resizes it will zoom)
    if (worldToViewTransform==null && e.getID()==ComponentEvent.COMPONENT_RESIZED)
      //30Nov1999: moved code to specialized method [setDefaultWorldToViewTranform]
      setDefaultWorldToViewTransform();
  }

  /*
    private Runtime runtime=Runtime.getRuntime();
    long lastFree=runtime.freeMemory();

    private void dumpMemoryStatistics(String comment){
     long free=runtime.freeMemory();
     System.out.println(comment+": total="+runtime.totalMemory()+" free="+free+" freeChange="+
                                                              (free-lastFree));
     lastFree=free;
    }
  */

  //don't call this directly: should only be called from Swing's paint-thread: otherwise
  //uncomment the "synchronized" block below
  public void paintComponent(Graphics g){
    try{
      super.paintComponent(g);
      Graphics2D g2d=(Graphics2D)g;
      //10May2000: rare-situation, so don't spend time to check: it can be caught by the
      //try/catch block if(g2d==null) return; //9May2000

      //g2d.setColor(Color.blue);
      //g2d.fillRect(0,0,getWidth(),getHeight());
      //do this before applying a new transform (to draw in default [display device]
      //transform of the component)
      if(bkgrImageVisible)
        paintBkgrImage(g2d);
      if(worldToViewTransform==null)
        return; //30Sep1999: return if worldToViewTransform hasn't yet been calculated
      //non-sync'ed: Swing assures us that all painting is serialized on a single thread
      //and we're not calling paintComponent directly elsewhere//
      //synchronized(this){ //3Nov1999
      AffineTransform oldTransform=g2d.getTransform(); //keep current transform of object
      //3Nov1999:reuse-one-oldTransform-object
      // AffineTransform newTransform=new AffineTransform(oldTransform);
      //copy the transform data, cause Graphics2D will give us a reference to its internal
      ///transform object (which we'll change in the following statements an want to restore
      //later to this kept copy)

      //3Nov1999: reuse the newTransform buffer (keep the old transform there)
      newTransform.setTransform(oldTransform);
      newTransform.concatenate(worldToViewTransform);
      //must call setTransform to inform the Graphics that we've changed the current transform
      g2d.setTransform(newTransform);
      //26-3-1999: bug-fix
      //try{viewToWorldTransform=worldToViewTransform.createInverse();}catch(Exception e){}
      //must not invert the combined Transform (that is newTransform), but only our component's
      //view transform (the worldToViewTransform), since mouse events are sent in our component's
      //view coordinate space (whose startPoint is translated regarding to the Graphics2D view
      //coordinate space startPoint)
      if(doingSelection)
        paintSelector(g2d);
      //14Nov1999: this property checks if shapes will be over axis and grid, or under it
      if(marksOverShapes){
        //15Apr1999: decided to show axis and grid over the objects, to simplify their positioning
        paintShapes(g2d);
        paintMarks(g2d);
      }else{
        paintMarks(g2d);
        paintShapes(g2d);
      }
      //do last, cause after paintComponent, the paintComponent methods of our children are
      //called (our button would get rotated too)
      g2d.setTransform(oldTransform);
      //non-sync'ed: Swing assures us that all painting is serialized on a single thread and
      //we're not calling paintComponent directly elsewhere
      // } //3Nov1999: end of sync-block
    }catch(Exception e){
      error("Error at Scene.paintComponent :\n"+e);
      //e.printStackTrace(); //don't print a stack trace in case of errors (this would slow
      //down the whole system in case of a problematic situation where exceptions may be thrown
      //at every repaint)
    } //30Sep1999: just in case something fails, don't disrupt the repaint of our parent container
  }

  public void paintSelector(Graphics2D g2d){
    g2d.setStroke(selectionStroke);
    g2d.draw(selector);
  }

  //14Nov1999: separated from "paintComponent" routine
  public void paintMarks(Graphics2D g2d){
    if(gridVisible)
      paintGrid(g2d);
    if(axisVisible)
      paintAxis(g2d); //axis must paint over the grid, so draw after drawing the grid
  }

  public void paintBkgrImage(Graphics2D g2d){ //8-4-1999
    if (bkgrImage!=null)
      g2d.drawImage(bkgrImage,0,0,getWidth(),getHeight(),null);
  }

  //8-4-1999
  //any routine called by "paintComponent" should reuse objects (don't use "new" operator!!!)
  public void paintGrid(Graphics2D g2d)
  {
    Rectangle2D r = getWorldViewRect();

    double x = r.getX();
    double x1 = Math.round(x / gridSize) * gridSize;
    double y = r.getY();
    double y1 = Math.round(y / gridSize) * gridSize;
    double x2 = x1 + r.getWidth();
    double y2 = y1 + r.getHeight();

    g2d.setColor(COLOR_GRID);
    for (y=y1; y<=y2; y+=gridSize) {
      for (x=x1; x<=x2; x+=gridSize) {
        g2d.draw(new Circle2D(x, y, 1));
      }
    }
  }

  public void paintAxis(Graphics2D g2d){ //8-4-1999
    //26Jul1999: setting the stroke to Normal for the axis, cause when selecting objects,
    //sometimes the axis showed up in a dotted line...
    g2d.setStroke(axisStroke);
    g2d.setColor(COLOR_AXIS);

    Rectangle2D r = getWorldViewRect();
    double x1 = r.getX();
    double y1 = r.getY();
    double x2 = x1 + r.getWidth();
    double y2 = y1 + r.getHeight();
    if (y1<=0 && 0<=y2) {
      g2d.drawLine((int)x1, 0,  (int)x2, 0);
    }
    if (x1<=0 && 0<=x2) {
      g2d.drawLine(0, (int)y1,  0, (int)y2);
    }
  }

  public void paintShapes(Graphics2D g2d){
    //more recent ones added at end of vector, so go on top
    for(int i=0,count=objects.size();i<count;i++){
      Object obj=objects.get(i);
      if(objectsSelection.contains(obj))
        g2d.setStroke(selectionStroke); //30Jun1999
      else{
        //should have a property per shape to select from various strokes or even make
        //custom ones! (and if that property is null for some object, use a default
        //"shapesStroke" property of the Scene)
        g2d.setStroke(shapesStroke);
      }

      Image img=(obj instanceof HasImage)?((HasImage)obj).getImage():null; //23Apr2000
      if(img!=null && obj instanceof HasLocation2D){
        Point2D p=((HasLocation2D)obj).getLocation2D();
        //maybe reuse one or two transform objects here instead of calling
        //getTranslateInstance/getRotateInstance???
        double imgWidth2=img.getWidth(this)/2;
        double imgHeight2=img.getHeight(this)/2;
        //translate image's center to object's center
        AffineTransform transform=AffineTransform.getTranslateInstance(p.getX()-imgWidth2,
                                                                      p.getY()-imgHeight2);
        //flip the image
        transform.concatenate(new AffineTransform(1,0,0,-1,0,img.getHeight(this)));
        if(obj instanceof HasAngle){
          //must use "-" sign
          transform.concatenate(AffineTransform.getRotateInstance(
              -/*NumberUtil*/Math.toRadians(((HasAngle)obj).getAngle()), imgWidth2, imgHeight2 ));
        }
        // Use high quality rendering, so that, if the image is rotated, it
        // does not look awful.
        Object rh = g2d.getRenderingHint(RenderingHints.KEY_RENDERING);
        g2d.setRenderingHint(
          RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        );
        //also do rotation transform if object is instance of "HasAngle" i/f ???
        g2d.drawImage(img,transform,this);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, rh);
      }
      else{
        Shape shape=(obj instanceof AsShape)?((AsShape)obj).getShape():null;
        if(shape!=null){
          // Don't fill springs, or weirdness will occur under Java 6.
          boolean fill = !(obj instanceof gr.cti.eslate.stage.objects.Spring);
          //?use HasTexturePaint i/f
          paintShape(g2d,shape,((HasColor)obj).getColor(),((SceneObject)obj).texturePaint, fill);
        }
      }
      //draw the control points
      if(controlPointsVisible)
        paintControlPoints(g2d,((IControlPointContainer)obj).getControlPoints());
    }
  }

  public void paintShape
    (Graphics2D g2d, Shape s, Color color, TexturePaint tp, boolean fill)
  {
    g2d.setColor(color);
    g2d.setPaint(tp); // Add the texture paint to the graphics context.
    if (fill) {
      g2d.fill(s); //fill
    }
    g2d.setColor(Color.black); //shape's contour color???
    g2d.draw(s); //draw contour
  }

  public void paintControlPoints(Graphics2D g2d,ControlPoint[] controlPoints){
    for(int i=controlPoints.length;i-->0;)
      paintControlPoint(g2d,controlPoints[i]);
  }

  public void paintControlPoint(Graphics2D g2d,ControlPoint p){
    if(controlPointsSelection.contains(p))
      g2d.setColor(COLOR_CONTROLPOINT_SELECTED); //30Jun1999
    else
      g2d.setColor(COLOR_CONTROLPOINT);
    //3Nov1999// g2d.draw(new Circle2D(p.getLocation2D(),CONTROL_POINT_RADIUS));
    //3Nov1999: try to reuse the control point shape instead of creating new ones//
    Point2D pos=p.getLocation2D();
    double r=CONTROL_POINT_RADIUS; //copy class field here for more speed at the frame calculation
    double r2=r*2; //same as above
    controlPointShape.setFrame(pos.getX()-r,pos.getY()-r,r2,r2);
    g2d.draw(controlPointShape);
  }

  // PROPERTIES
  //ControlPointsVisible property
  //15Apr2000: renamed to "isControlPointsVisible" from "getControlPointsVisible"
  public boolean isControlPointsVisible(){
    return controlPointsVisible;
  }

  public void setControlPointsVisible(boolean flag){
    if(controlPointsVisible!=flag){ //12May2000: firing property change events
      controlPointsVisible=flag;
      refresh();
      //repaint();
      //since a boolean can be TRUE or FALSE and we only fire events if value changed,
      //the old value is the boolean opposite of the new value
      firePropertyChange(CONTROL_POINTS_VISIBLE_PROPERTY,!flag,flag);
    }
  }

  //GridVisible property//
  public boolean isGridVisible(){
    return gridVisible;
  }

  public void setGridVisible(boolean flag){
    if(gridVisible!=flag){ //12May2000: firing property change events
      gridVisible=flag;
      repaint();
      //since a boolean can be TRUE or FALSE and we only fire events if value changed,
      //the old value is the boolean opposite of the new value
      firePropertyChange(GRID_VISIBLE_PROPERTY,!flag,flag);
    }
  }

  /**
   * Sets the size of the grid.
   * @param     s       The size of the grid. It must be positive.
   */
  public void setGridSize(double s)
  {
    if (s != gridSize) {
      if (s > 0.0) {
        double oldSize = gridSize;
        gridSize = s;
        repaint();
        firePropertyChange(GRID_SIZE_PROPERTY, oldSize, gridSize);
      }else{
        throw new IllegalArgumentException(Res.localize("badGridSize"));
      }
    }
  }

  /**
   * Returns the size of the grid.
   * @return    The size of the grid.
   */
  public double getGridSize()
  {
    return gridSize;
  }

  //AxisVisible property
  public boolean isAxisVisible(){
    return axisVisible;
  }

  public void setAxisVisible(boolean flag){
    //12May2000: firing property change events
    if(axisVisible!=flag){
      axisVisible=flag;
      refresh();
      //repaint();
      //since a boolean can be TRUE or FALSE and we only fire events if value changed,
      //the old value is the boolean opposite of the new value
      firePropertyChange(AXIS_VISIBLE_PROPERTY,!flag,flag);
    }
  }

  //coordinatesVisible property
  public boolean isCoordinatesVisible(){ //15Oct1999
    return coordinatesVisible;
  }

  public void setCoordinatesVisible(boolean flag){ //15Oct1999
    if(coordinatesVisible!=flag){ //15Nov1999: firing property change events
      coordinatesVisible=flag;
      coordinatesLabel.setVisible(flag);
      //since a boolean can be TRUE or FALSE and we only fire events if value changed, the
      //old value is the boolean opposite of the new value
      firePropertyChange(COORDINATES_VISIBLE_PROPERTY,!flag,flag);
    }
  }

  //ObjectMovementEnabled property
  public boolean isObjectMovementEnabled(){
    return objectMovementEnabled;
  }

  public void setObjectMovementEnabled(boolean flag){
    if(objectMovementEnabled!=flag){ //15Nov1999: firing property change events
      objectMovementEnabled=flag;
      //since a boolean can be TRUE or FALSE and we only fire events if value changed, the
      //old value is the boolean opposite of the new value
      firePropertyChange(OBJECT_MOVEMENT_ENABLED_PROPERTY,!flag,flag);
    }
  }

  //ViewMovementEnabled property
  public boolean isViewMovementEnabled(){
    return viewMovementEnabled;
  }

  public void setViewMovementEnabled(boolean flag){
    if(viewMovementEnabled!=flag){
      viewMovementEnabled=flag;
      //since a boolean can be TRUE or FALSE and we only fire events if value changed, the
      //old value is the boolean opposite of the new value
      firePropertyChange(VIEW_MOVEMENT_ENABLED_PROPERTY,!flag,flag);
    }
  }

  //ObjectsAdjustable property
  public boolean isObjectsAdjustable(){
    return objectsAdjustable;
  }

  public void setObjectsAdjustable(boolean flag){
    if(objectsAdjustable!=flag){
      objectsAdjustable=flag;
      //since a boolean can be TRUE or FALSE and we only fire events if value changed, the
      //old value is the boolean opposite of the new value
      firePropertyChange(OBJECTS_ADJUSTABLE_PROPERTY,!flag,flag);
    }
  }

  //ControlPointMovementEnabled property
  public boolean isControlPointMovementEnabled(){
    return controlPointMovementEnabled;
  }

  public void setControlPointMovementEnabled(boolean flag){
    if(controlPointMovementEnabled!=flag){ //15Nov1999: firing property change events
      controlPointMovementEnabled=flag;
      //since a boolean can be TRUE or FALSE and we only fire events if value changed, the
      //old value is the boolean opposite of the new value
      firePropertyChange(CONTROL_POINT_MOVEMENT_ENABLED_PROPERTY,!flag,flag);
    }
  }

  //MarksOverShapes property
  public boolean isMarksOverShapes(){ //15Nov1999
    return marksOverShapes;
  }

  public void setMarksOverShapes(boolean flag){ //15Nov1999
    if(marksOverShapes!=flag){
      marksOverShapes=flag;
      //repaint();
      refresh();
      //since a boolean can be TRUE or FALSE and we only fire events if value changed,
      //the old value is the boolean opposite of the new value
      firePropertyChange(MARKS_OVER_SHAPES_PROPERTY,!flag,flag);
    }
  }

  //GridType property//
  public int getGridType(){
    return gridType;
  }

  public void setGridType(int value){
    gridType=value;
    //repaint();
    refresh();
  }

  // TRANSFORMATIONS
  public AffineTransform getWorldToViewTransform(){ //17Oct1999
    return worldToViewTransform;
  }

  public void setWorldToViewTransform(AffineTransform t){
    worldToViewTransform=t;
    try{
      //26-3-1999:using this again, not using the inverse "newTransform" at paintComponent:
      //must not invert the combined Transform (that is newTransform), but only our component's
      //view transform (the worldToViewTransform), since mouse events are sent in our component's
      //view coordinate space (whose startPoint is translated regarding to the Graphics2D view
      //coordinate space startPoint)
      viewToWorldTransform=worldToViewTransform.createInverse();
    }catch(Exception e){
      error("Non invertable WorldToView tranform! (can't create the ViewToWorldTransform for this one)");
    }
    //repaint(); //this leads to a redundant repaint at resize events
    refresh();
  }

  public AffineTransform getViewToWorldTransform(){ //17Oct1999
    return viewToWorldTransform;
  }

  public void setViewToWorldTransform(AffineTransform t){ //17Oct1999
    viewToWorldTransform=t;
    try{
      worldToViewTransform=viewToWorldTransform.createInverse();
    }catch(Exception e){
      error("Non invertable ViewToView tranform! (can't create the WorldToViewTransform for this one)");
    }
    //repaint(); //this leads to a redundant repaint at resize events
    refresh();
  }

  //30Nov1999 //23Feb2000: This will create and assign a new AffineTransform object to
  //"worldToViewTransform" (it's called from the processComponentEvent at the first
  //COMPONENT_RESIZED event only)
  public void setDefaultWorldToViewTransform(){
    setWorldToViewTransform(new AffineTransform(1,0,0,-1,0,getHeight()));
  }

  @SuppressWarnings({ "unchecked", "unchecked" })
  public void showCustomizer(Object object){
    if(object==null)
      return;
    //18May2000: store the constraints, since if the user presses cancel on the customizer,
    //the object
    byte[] constraintData=Cloning.storeObject(getConstraints());

    //19May2000: now showing customizer pages with an order that goes from special to general
    //(the last one is the one of the BaseObject)
    //should pass an array of such customizers, one for each ancestor of the object
    //(maybe better ask the object to return the CustomizerClasses it wants with the order
    //[page order in the customizer dialog] it likes)
    new CustomizerDialog((BaseObject)object,new JComponent[]{
                          new BallCustomizer(),
                          new SlopeCustomizer(),
                          new SpringCustomizer(),
                          new PhysicsObjectCustomizer(),
                          new SceneObjectCustomizer(),
                          new BaseObjectCustomizer()}).setVisible(true);

    //18May2000: assuming setConstraints disposes the previous constraints
    setConstraints((ArrayList)Cloning.restoreObject(constraintData));
  }

  //26Nov1999: not Java1.1 compatible (unless the Collection i/f is packaged in the .jar too)
  @SuppressWarnings(value={"unchecked"})
  private void toggle(Object o,Collection v){
    if(v.contains(o))
      v.remove(o);
    else
      v.add(o);
  }

/*
  private void toggle(Object o,Vector v){ //Java1.1 compatible
   if(v.contains(o)) v.removeElement(o); else v.addElement(o);
  }
*/

  public void setCoordinatesLabel(double x,double y){ //14Apr2000
    //!!! should use a StringBuffer here !!!
    coordinatesLabel.setText("("+NumberUtil.round_n(x,3)+" , "+NumberUtil.round_n(y,3)+")");
  }

  // MOUSE

  public void setSelectionMode(boolean flag,ToggleAction selectorAction){ //26Apr2000
    //must empty the old selection rectangle, else a selection rect will show up immediately
    //after setting the selection mode to on if a repaint happens (e.g. if this tool was
    //selected from a menu)
    selector.setFrame(0,0,0,0);
    this.selectorAction=selectorAction;
    doingSelection=flag;
  }

  class MyMouser extends MouseAdapter implements MouseMotionListener{
    //23Feb2000: reusing the same point object for the world point in mouse event handlers
    private Point2D worldPoint=new Point2D.Double();
    //23Feb2000: reusing the same point object for the local point in mouse event handlers
    private Point2D localPoint=new Point2D.Double();
    //8-4-1999: for view panning
    //23Feb2000: creating only once reusing the same "startPoint" object
    private Point2D startPoint=new Point2D.Double();
    private ControlPoint controlPoint;
    //15-4-1999: it's OK to hold the object instance, even if the scene is cleared while
    //dragging is occuring
    private SceneObject object;

    //27Jul1999: mouseReleased must not release the controlPoint&object vars set by the
    //mousePressed method, now have them cleared at mouseMoved instead
    public void mouseClicked(MouseEvent e){
      if (!isObjectsAdjustable()) {
        return;
      }
      if(SwingUtilities.isRightMouseButton(e)) {
        JPopupMenu pop=makeObjectPopup(object);
        if (pop!=null){
          //Must add the absolute screen location of the invoker to the popup location ???
          //(else shows-up at wrong place on screen)
          Point screen=Scene.this.getLocationOnScreen();
          //must have this so that the popup closes when we click at someplace out of it and
          //that no multiple popups showup when right-clicking many times
          pop.setInvoker(Scene.this);
          //???doesn't show at the exact place we clicked???
          pop.setLocation(screen.x+e.getX(),screen.y+e.getY());
          //don't use "show()", it's deprecated, use "setVisible(true)" instead
          pop.setVisible(true);
        }else{ //13Apr2000
          //rebuild this at every right click since we want it to contain some options
          //depending on objectsSelection (Cut/Copy/Clone/Clear) and some depending on
          //the "clipboard" having contents (Paste)
          pop=makeScenePopup();
          //Must add the absolute screen location of the invoker to the popup location ???
          //(else shows-up at wrong place on screen)
          Point screen=Scene.this.getLocationOnScreen();
          //must have this so that the popup closes when we click at someplace out of it and
          //that no multiple popups showup when right-clicking many times
          pop.setInvoker(Scene.this);
          //???doesn't show at the exact place we clicked???
          pop.setLocation(screen.x+e.getX(),screen.y+e.getY());
          //don't use "show()", it's deprecated, use "setVisible(true)" instead
          pop.setVisible(true);
        }
      //21Dec1999: now not showing the customizer when CTRL+CLICKing an object
      }else if(!e.isControlDown()){
        if(controlPoint!=null){
          //18May2000: now left-clicking a control point shows a customizer for it and not
          //for its container object (right click and select properties to show a customizer
          //for the container object of the control point [useful for very thin objects the
          //shape of which is very hard to spot and click])
          showCustomizer(controlPoint);
        }
        else
          showCustomizer(object); //8Dec1999: left-click for now on objects to show customizer
      }
    }

    //23Feb2000: encapsulate the internal buffer "localPoint"
    private Point2D getLocalPoint(MouseEvent e){
      localPoint.setLocation(e.getX(),e.getY());
      return localPoint;
    }

    public void mousePressed(MouseEvent e){
      //System.out.println("MousePressed_View: "+e.getX()+":"+e.getY());
      if(viewToWorldTransform==null)
        return; //6Oct1999: return if viewToWorldTransform hasn't yet been calculated
      //23Feb2000: don't use the transform(xxx,null) method that creates a new Point2D object,
      //but reuse the same "startPoint" object don't use the worldPoint variable here,
      //cause its changed at MouseMoved/MouseDragged
      viewToWorldTransform.transform(getLocalPoint(e),startPoint);
      if(doingSelection) { //26Apr2000
        selector.setFrame(startPoint.getX(),startPoint.getY(),0,0);
        //repaint();
        refresh();
        return;
      }
      //8-4-1999: can't drag an invisible control point
      if (controlPointsVisible)
        controlPoint=findControlPoint(startPoint);
      //22Jul1999:fixed-bug:see_comment_at_end_of_next_line// if (controlPoint==null)
      //15-4-1999: if no (visible) control point was hit and we did hit an object, select it
      //so that we can drag it arround
      //15-4-1999: always keep the object [not only if (controlPoint==null)], in case the
      //SHIFT is pressed later during dragging (SHIFT results in dragging the object, even
      //if we're over a visible control point)
      object=findSceneObject(startPoint);
      //21Dec1999: try to locate an object from any control point of it we might have
      //pressed a mouse button on
      if (object==null && controlPoint!=null)
        object=findControlPointOwner(controlPoint);

      //29Jun1999: do control points' or objects' selections with CTRL + click
      if(e.isControlDown()){
        if(controlPoint!=null && !e.isShiftDown()){
          //21Dec1999: now allowing SHIFT to force object selection when CTRL+SHIFT+CLICKING
          //on a control point (instead of selecting the control point)
          toggle(controlPoint,controlPointsSelection);
        }
        else{
          //controlPointsSelection.clear();
          //1Jul1999: allow both control points and objects to be selected (for binding)...
          if(object!=null)
            toggle(object,objectsSelection);
          else{
            objectsSelection.clear();
            //2Jul1999: ...must CTRL+click in empty space to clear both object' and contol
            //points' selections
            controlPointsSelection.clear();
          }
        }
        //repaint(); //show selected control points
        refresh();
      }
    }

    public void translateSelectedObjects(double dx,double dy){ //22Jul1999
      for(int i=0;i<objectsSelection.size();i++)
        ((SceneObject)objectsSelection.get(i)).translate(dx,dy);
    }

    public void updateCoordinatesLabel(Point2D worldPoint){ //15-4-1999
      setCoordinatesLabel(worldPoint.getX(),worldPoint.getY());
    }

    public void mouseDragged(MouseEvent e){
      //6Oct1999: return if viewToWorldTransform hasn't yet been calculated
      if(viewToWorldTransform==null)
        return;
      //23Feb2000: reuse the same "localPoint" object [encapsulated by "getLocalPoint" method]
      //instead of creating a new Point2D object
      //23Feb2000: don't use the viewToWorldTransform(xxx,null), avoid having a new Point2D
      //object created, reuse the same "worldPoint" object
      viewToWorldTransform.transform(getLocalPoint(e),worldPoint);
      //System.out.println("MouseDragged_World: "+worldPoint.getX()+":"+worldPoint.getY());
      //26Apr2000: added objects-intersecting-rectangular-area selection tool
      if (doingSelection)
        selector.setFrameFromDiagonal(startPoint.getX(),startPoint.getY(),worldPoint.getX(),worldPoint.getY()); //use startPoint's getX()/getY() here, not "selector.getX()" & "selector.getY()" (cause the RectangularShape class reorders it's edges so that the first one has the min coords)
      else{
        //15-4-1999: holding SHIFT down (if not ALT is down) always results in dragging the object
        //we initially pressed the mouse button on, even if we pressed it on a visible control point
        //15-4-1999: holding ALT down always results in dragging the view even if we are over a
        //visible control point or an object
        //16-4-1999: moved !ALT check here, so that view is dragged even when no ALT is pressed
        //(and no control point is dragged)
        if (controlPointMovementEnabled && controlPoint!=null && !e.isShiftDown() && !e.isAltDown())
          controlPoint.setLocation(worldPoint); //move selected controlPoint
        //15-4-1999:allow object dragging
        //15-4-1999: holding ALT down always results in dragging the view even if we are over
        //a visible control point or an object
        //16-4-1999: moved !ALT check here, so that view is dragged even when no ALT is
        //pressed (and no object is dragged)
        else if (objectMovementEnabled && object!=null && !e.isAltDown()){
          //22Jul1999: if the dragged object belongs to the "selection"...
          if(objectsSelection.contains(object))
            translateSelectedObjects(worldPoint.getX()-startPoint.getX(),worldPoint.getY()-startPoint.getY()); //...drag all selected objects, instead of just the one the mouse was pressed upon
          else{
            //...else drag just the object we pressed the mouse button upon to drag
            object.translate(startPoint,worldPoint);
          }
          //23Feb2000: we're now reusing the buffer where "worldPoint" is pointing at, so if
          //we assign "startPoint" to that buffer to use it later, we must get "worldPoint" to
          //point to some no-more being used buffer (in this case the old one where "startPoint"
          //was pointing to)
          Point2D temp=startPoint;
          startPoint=worldPoint;
          //must set startPoint=worldPoint, so that next drag gets the new drag offset and
          //not an accumulated one for all the drags since the first mouse press (this is not
          //like when we translate the view, where we mustn't set startPoint=worldPoint after
          //each dragging)
          worldPoint=temp;
        }
        else{ //else (no control point selected) move view
          if (viewMovementEnabled) {
            worldToViewTransform.translate(worldPoint.getX()-startPoint.getX(),
                                            worldPoint.getY()-startPoint.getY());
            //do this to have viewToWorldTransform (the inverse of worldToViewTransform) created
            setWorldToViewTransform(worldToViewTransform);
          }
          //don't set startPoint=worldPoint here, we've practically already done that by
          //translating the transform (next worldPoint will be translated by the current
          //|worldPoint-startPoint|--> distance vect)
        }
        updateCoordinatesLabel(worldPoint); //15-4-1999
        //will repaint twice in the last else case (view pan/tilt), but it's OK [Swing
        //combines many repaints into one]
        //repaint();
        refresh();
      }
    }

    //can't check for shift or alt to change cursor, cause the alt or SHIFT keypress,
    //after the mouse has stabilized over an object/control point, wouldn't change the cursor
    //till the first mouse move, which would not look like a consistent behaviour (need to
    //get kbd events for this and check for modifier-key presses)
    public void mouseMoved(MouseEvent e){
      //System.out.println("MouseMoved_View: "+e.getX()+":"+e.getY());
      //27Jul1999: move here the "held-object releasing code" from mouseRelease, cause
      //mouseClicked needs the controlPoint&object vars and gets invoked after
      //MousePressed&MouseReleased
      controlPoint=null; //no need to still hold a control point if there's no drag going on
      object=null; //no need to still hold an object if there's no drag going on
      if(viewToWorldTransform==null)
        return; //6Oct1999: return if viewToWorldTransform hasn't yet been calculated
      //23Feb2000: reuse the same "localPoint" object [encapsulated by "getLocalPoint" method]
      //instead of creating a new Point2D object //23Feb2000: don't use the
      //viewToWorldTransform(xxx,null), avoid having a new Point2D object created, reuse the same
      //"worldPoint" object
      viewToWorldTransform.transform(getLocalPoint(e),worldPoint);
      //System.out.println("MouseMoved_World: "+worldPoint.getX()+":"+worldPoint.getY());
      updateCoordinatesLabel(worldPoint); //15-4-1999
      //15Apr2000: if control points are not visible, don't change the cursor to a hand
      //when passing over one (instead continue with checking whether we're over the object's
      //shape etc. and show appropriate cursors)
      if (controlPointsVisible && /*!e.isShiftDown() && */ findControlPoint(worldPoint)!=null){
        //15-4-1999: changed to HAND_CURSOR
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
      else if (/*!e.isCtrlDown() && */ findSceneObject(worldPoint)!=null){
        //15-4-1999: when over an object and not over a visible control point show a MOVE_CURSOR
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
      }
      else{
        //15-4-1999: using a CROSS_CURSOR when over a blank part of the scene area (also
        //show coords on some label in the toolbar???)
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      }
    }

    public void mouseReleased(MouseEvent e){
      //System.out.println("Mouse Released");
      //27Jul1999: moved the "held-object releasing code" code from "mouseReleased" to
      //"mouseMoved", cause mouseClicked needs the controlPoint&object vars and gets invoked
      //after MousePressed&MouseReleased
      /*
      //removed-action-old-comment: no need to still hold the control point after dragging ends
      controlPoint=null;
      //removed-action-old-comment: no need to still hold the object after dragging ends
      object=null;
      */
      if(doingSelection){ //26Apr2000
        //if CTRL is pressed, add to current/old selection, else empty the old selection
        if(!e.isControlDown())
          emptyObjectsSelection();
        addObjectsInAreaToSelection(selector);
        doingSelection=false;
        //this will do "doingSelection=false" too, but we won't assume that a "selectorAction"
        //ToggleAction instance that behaves this way always exists
        if(selectorAction!=null)
          selectorAction.setSelected(false);
      }
    }

    //13Apr2000: on mouse exit from scene, empty the coordinates label text (mouseEntered
    //method is not needed)
    public void mouseExited(MouseEvent e){
      coordinatesLabel.setText("");
    }
  }

  // EDITING
  public void selectAll(){ //29Jun1999
    //8Aug1999: fixed-bug, was assigning the objects to the objectsSelection variable instead
    //of copying the references from it to the selection collection
    objectsSelection.addAll(objects);
    //8Aug1999: repaint to show selection
    //repaint();
    refresh();
  }

  //------------- CopyPaste-START -------------// //12Apr2000
  private boolean isClipboardEmpty(){
    return clipboard==null;
  }

  public void cut(){
    if (objectsSelection.isEmpty())
      return; //change current clipboard only if something is selected to cut
    copy();
    clearSelection();
  }

  //13Apr2000: cut just one object independently of what the current selection is
  public void cut(Object obj){
    copy(obj);
    removeSceneObject(obj);
  }

  public void copy(){
    if (objectsSelection.isEmpty())
      return; //change current clipboard only if something is selected to copy
    //19May2000: now serializing the current selection into the clipboard instead of
    //putting live objects there

    for (int i=0; i < objectsSelection.size(); i++)
        ((SceneObject)objectsSelection.get(i)).setPreparedForCopy(true);

    clipboard=Cloning.storeObject(objectsSelection);
  }

  //13Apr2000: copy just one object independently of what the current selection is
  public void copy(Object obj){
    ArrayList<Object> clipdata=new ArrayList<Object>();
    SceneObject scObj = (SceneObject)obj;
    scObj.setPreparedForCopy(true);
    clipdata.add(obj);
    //19May2000: now placing the object in a Vector and serializing it into the clipboard
    //instead of putting live objects there
    clipboard=Cloning.storeObject(clipdata);

    //clipdata.add(obj);
    //19May2000: now placing the object in a Vector and serializing it into the clipboard
    //instead of putting live objects there
    //clipboard=Cloning.storeObject(clipdata);
  }

  @SuppressWarnings("unchecked")
  public void paste()
  {
    if (clipboard != null) {
      //19May2000: deserializing Vector containing objects from the clipboard
      ArrayList objList = (ArrayList)Cloning.restoreObject(clipboard);
      addSceneObjects(objList);
      for (int i=0; i < objList.size(); i++) {
        ((SceneObject)objList.get(i)).setPreparedForCopy(false);
      }
    }
  }

  //------------- CopyPaste-END -------------//

  //29Jun1999: this is an "Edit/Clear", an "Edit/Cut" should just place on the clipboard
  //without unregistering from the scripting engine... (just set the container to null for
  //the objects... when added again set it back)
  //13Apr2000: renamed to "clearSelection" from "clear"
  public void clearSelection(){
    //21May2000: fixed-bug: must  the current selection Vector contents before we
    //enumerate on its elements and call removeObject on each of them, cause removeObject
    //removes the elements from the Vector! //objectsSelection is never null
    Object[] objectsToRemove=objectsSelection.toArray();
    for(int i=objectsToRemove.length;i-->0;)
      removeObject(objectsToRemove[i]); //using array instead of a clone Vector for more speed
    objectsSelection.clear();
    //repaint();
    refresh();
  }

  public boolean isSelected(Object o){
    return objectsSelection.contains(o) || controlPointsSelection.contains(o);
  }

  public void split(Object o){ //19May2000
    for(int i=constraints.size();i-->0;){
      IConstraint constraint=constraints.get(i);
      if(constraint.hasMember(o))
        removeConstraint(constraint);
    }
  }

  public void split(){ //18May2000: implemented!
    for(int i=constraints.size();i-->0;){
      IConstraint constraint=constraints.get(i);
      Object members[]=constraint.getMembers();
      for(int j=members.length;j-->0;)
        if(isSelected(members[j])){
          removeConstraint(constraint);
          break; //exit-for
        }
      //deselect any selected control points & objects after the split action has been done
      emptySelection();
    }
//   for(int j=i+1;j<size;j++)
//    ControlPoint.twoWayUnbind(bindPoint,(ControlPoint)controlPointsSelection.elementAt(j)); //make all points unbind if bound (in any direction!!!)
//  }
  }

  public SceneObject findSceneObject(String name){ //24Feb2000: find a scene object by name
    if(name!=null)
      for(int i=objects.size();i-->0;){
        SceneObject o=(SceneObject)objects.get(i);
        //03Apr2000: now names' comparison is case insensitive (for Greek too) and
        //"Greek punctuation insensitive"
        if (CharacterCase.areEqualIgnoreCase(o.getName(),name))
          return o;
      }
    return null;
  }

  public SceneObject findSceneObject(Point2D p){
    if(p!=null){ //23Feb2000
      //more recent ones added at end of vector, so first check them
      for(int i=objects.size();i-->0;){
        SceneObject o=(SceneObject)objects.get(i);
        if (((AsShape)o).getShape().contains(p))
          return o;
      }
    }
    return null;
  }

  public SceneObject findControlPointOwner(ControlPoint p){ //21Dec1999
    if(p!=null) //23Feb2000
      for(int i=objects.size();i-->0;){
        Object o=objects.get(i);
        ControlPoint[] controlPoints=((IControlPointContainer)o).getControlPoints();
        for(int j=controlPoints.length;j-->0;)
          if (controlPoints[j]==p)
            return (SceneObject)o;
      }
    return null;
  }

  public ControlPoint findControlPoint(Point2D p){
    if(p!=null){ //23Feb2000
      //more recent ones added at end of vector, so first check them
      for(int i=objects.size();i-->0;){
        ControlPoint[] controlPoints=((IControlPointContainer)objects.get(i)).getControlPoints();
        for(int j=controlPoints.length;j-->0;){
          if (controlPoints[j].getLocation2D().distanceSq(p)<=CONTROL_POINT_RADIUS_SQR)
            return controlPoints[j];
        }
      }
    }
    return null;
  }

  // POPUPs
  public JPopupMenu makeObjectPopup(SceneObject o){
    if (o!=null){
      //some GUIs might show a title for the popup: use the object's name as title
      //1Dec1999: localized
      JPopupMenu pop=new JPopupMenu(Res.localize(o.getName()));
      //pop.addSeparator();
      pop.add(new ObjectActionBringToFront(o));
      pop.add(new ObjectActionSendToBack(o));
      pop.addSeparator();
      pop.add(new ObjectActionCut(o));
      pop.add(new ObjectActionCopy(o));
      //-- paste is not a logical action for a right-clicked object --//
      pop.add(new ObjectActionClear(o));
      pop.add(new ObjectActionClone(o));
      pop.addSeparator();
      //19May2000: show different action title depending on whether the object is already
      //selected or not
      pop.add(new ObjectActionToggleSelection(isSelected(o)?"Deselect":"Select",o));
      pop.add(new ObjectActionProperties(o));
      /* //not needed, will show popup whereever the user clicks
         Point2D viewPoint=worldToViewTransform.transform(o.getLocation2D(),(Point2D)null);
         //anyway can do must simpler, using pop.setInvoker(this);
         //and pop.setLocation((int)viewPoint.getX(),(int)viewPoint.getY());
         Point screenPos=this.getLocationOnScreen();
         //must have this for multiple popups no to show up and for popups to go away when
         //clicking out of them
         pop.setInvoker(Scene.this);
         pop.setLocation(screenPos.x+(int)viewPoint.getX(),screenPos.y+(int)viewPoint.getY());
      */
      //pop.addSeparator();
      return pop;
    }else
      return null;
  }

  public JPopupMenu makeScenePopup(){ //13Apr2000: popup for right clicks on empty scene areas
    JPopupMenu pop=new JPopupMenu(Res.localize("title"));
    boolean objectsSelectionNotEmpty=!objectsSelection.isEmpty();
    boolean clipboardNotEmpty=!isClipboardEmpty();
    //12May2000: show Cut&Copy options only if there's some selection
    if(objectsSelectionNotEmpty) {
      pop.add(actions.action_Cut);
      pop.add(actions.action_Copy);
    }
    //12May2000: show Paste option only if there's content in the clipboard
    if(clipboardNotEmpty)
      pop.add(actions.action_Paste);
    //12May2000: show Clear&Clone options only if there's some selection
    if(objectsSelectionNotEmpty) {
      pop.add(actions.action_Clear);
      pop.add(actions.action_Clone);
    }
    //19May2000: add the separator only if some items have been added above it at
    //the start of the popup
    if(objectsSelectionNotEmpty || clipboardNotEmpty)
      pop.addSeparator();
    //21May2000: show "Select All" option only if the scene is not empty
    if(!objects.isEmpty()){
      pop.add(actions.action_SelectAll);
      pop.addSeparator();
    }
    pop.add(actions.action_New);
    pop.add(actions.action_Load); //21May2000
    pop.add(actions.action_Save); //21May2000
    pop.addSeparator();
    pop.add(actions.action_Photo); //21May2000
    pop.add(actions.action_Print);
    pop.addSeparator();
    pop.add(actions.action_zoomIn);
    pop.add(actions.action_zoomOut);
    return pop;
  }

  public void bringToFront(Object o){ //26Ju11999
    if(o==null)
      return; //1Apr2000
    if (objects.get(objects.size()-1)!=o){
      objects.remove(o);
      objects.add(o); //26Nov1999: made Java1.1 compatible
    }
    //repaint();
    refresh();
  }

  public void bringToFront(String objName){ //1Apr2000
    bringToFront(findSceneObject(objName));
  }

  public void sendToBack(Object o){ //26Ju11999
    if(o==null)
      return; //1Apr2000
    if (objects.get(0)!=o){
      objects.remove(o);
      objects.add(0,o);
    }
    //repaint();
    refresh();
  }

  public void sendToBack(String objName){ //1Apr2000
    sendToBack(findSceneObject(objName));
  }

  //PANNING/TITLING
  public void translate(double offsX, double offsY){
    worldToViewTransform.translate(offsX,offsY);
    //do this to have viewToWorldTransform (the inverse of worldToViewTransform)
    //created (and also have a repaint done)
    setWorldToViewTransform(worldToViewTransform);
  }

  //ZOOMING
  public void zoom(double factor){ //repaint not occuring at unzoom???
    worldToViewTransform.concatenate(AffineTransform.getScaleInstance(factor,factor));
    //26-3-1999: do this to have viewToWorldTransform (the inverse of worldToViewTransform) created
    setWorldToViewTransform(worldToViewTransform);
    //must repaint to update the scene
    //20Oct1999: don't need to repaint, worldToViewTransform now does this
    //repaint();
    //setViewExtents(10,20, 50,60); //test!!!
  }

  //26Jul1999: zoom/unzoom to fit this X-range (Y scales accordingly preserving x:y ratio)
  public void zoomX(double fromX, double toX){
    //not OK:SHOULD KEEP THE TOP-LEFT, BOTTOM-RIGHT VIEW COORDS AS THEY WERE BEFORE THE RESIZE !!
    setWorldToViewTransform(new AffineTransform(1,fromX,0,-1,toX,getHeight()));
  }

  //26Jul1999: zoom/unzoom to fit this Y-range (X scales accordingly preserving x:y ratio)
  public void zoomY(double fromY, double toY){
    //not OK:SHOULD KEEP THE TOP-LEFT, BOTTOM-RIGHT VIEW COORDS AS THEY WERE BEFORE THE RESIZE !!
    setWorldToViewTransform(new AffineTransform(1,0,fromY,-1,0,toY+getHeight()));
  }

  public void zoom(Point2D center,double factor){ //26Jul1999: to do
  }

  //20Oct1999 (bottom-left, top-right)
  public void setViewExtents(double x1,double y1, double x2,double y2){
    double fx=getWidth()/(x2-x1);
    double fy=getHeight()/(y1-y2);
    setWorldToViewTransform( new AffineTransform(fx,0, 0,fy, x2, y2 ) );
  }

  public void zoomToFitAllObjects(){ //26Jul1999
    zoomToFitObjects(objects);
  }

  public void zoomToFitSelectedObjects(){ //26Jul1999
    zoomToFitObjects(objectsSelection);
  }

  @SuppressWarnings("unchecked")
  public void zoomToFitObjects(ArrayList obj){ //26Jul1999: TO-DO

  }

  //PhysicsScene
  //8Aug1999: private, doesn't repaint //WATCH-OUT: this also disposes the object!
  private void removeObject(Object o){
    //26Apr2000: must check for null, removeSceneObject assumes we do that
    if(o!=null){
      //19May2000: remove from current selection, in case it contains it!
      objectsSelection.remove(o);
      //19May2000: removing an object also removes all of the constraints it takes part in
      split(o);
      if(o instanceof IControlPointContainer){
        ControlPoint cp[]=((IControlPointContainer)o).getControlPoints();
        //8Jun2000: in case the object returns no control-points (or if it's an object
        //corrupted during loading)
        if(cp!=null)
          for(int i=cp.length;i-->0;){
            //19May2000: when removing an object, also remove all its control points
            //from the current control points selection (in case they're selected)
            controlPointsSelection.remove(cp[i]);
            //19May2000: when removing an object, also remove all constraints its control
            //points take part in
            split(cp[i]);
          }
      }
      //11May2000: first remove from Scene's object vector, then do disposal!
      objects.remove(o);
      if (sceneHandle != null) {
        sceneHandle.remove(((SceneObject)o).getESlateHandle());
      }
      ((SceneObject)o).dispose();
    }
  }

  //8Aug1999: made private, doesn't repaint
  private void removeAllObjects(){
    //8Aug1999: to unregister from scripting engine and do any other cleanup needed
    //(an object should call its control points dispose methods when it is disposed itself)
    for(int i=objects.size();i-->0;)
      removeObject(objects.get(i));
    objects.clear();
  }

  private void removeAllConstraints(){ //31Nov1999
    for(int i=constraints.size();i-->0;)
      removeConstraint(constraints.get(i)); //31Nov1999
    constraints.clear();
  }

  public void clearScene(){ //2-4-1999
    removeAllConstraints(); //16May2000
    //28Aug1999: remove GUI Components added to this panel (for example text areas)
    //30Aug1999: not just calling "removeAll", cause need to not have the coordinatesLabel removed
    removeHostedComponents();
    removeAllObjects();
    setImage((Image)null); //12May2000
    //13Apr2000: since this is called from scripting, don't do repaint if it's disabled
    refresh();
  }

  //Selections
  public void addObjectsInAreaToSelection(Rectangle2D area){ //26Apr2000
    for(int i=objects.size();i-->0;){
      Object o=objects.get(i);
      if(o instanceof AsShape && ((AsShape)o).getShape().intersects(area))
        objectsSelection.add(o);
    }
    //repaint();
    refresh();
  }

  public void emptySelection(){ //18May2000
    emptyControlPointsSelection();
    emptyObjectsSelection();
  }

  public void emptyControlPointsSelection(){ //8Aug1999
    controlPointsSelection.clear();
    //repaint(); //!!!
    refresh();
  }

  public void emptyObjectsSelection(){ //8Aug1999
    objectsSelection.clear();
    //repaint(); //!!!
    refresh();
  }

  public SceneObject newSceneObject(String type) throws Exception{
    SceneObject obj;
    try{
      //22Jul1999: had changed package for physics' objects, that's why new objects couldn't
      //be instantiated via scripting
      Class<?> class1 = Class.forName("gr.cti.eslate.stage.objects."+type);
      obj = (SceneObject)class1.newInstance();
      add(obj);
      return obj;
    }catch(Exception e){
      e.printStackTrace(); //22Jul1999: printing a stack trace
      throw new Exception("Couldn't make a new "+type);
    }
  }

  public void removeSceneObject(Object obj){ //15May2000
    removeObject(obj);
    refresh();
  }

  //remove object by name //25Feb2000: implemented!
  public void removeSceneObject(String name){
    //this one is internal utility method, it won't repaint
    //removeObject will check for null, so no need to check whether "findSceneObject" returned
    //null cause an object with the given name wasn't found
    removeObject(findSceneObject(name));
    //do refresh(), not repaint(), since this method is called from scripting (might want to
    //do a "batch" removal of some objects with no repaints in between)
    refresh();
  }

  //Cloning//
  public SceneObject cloneSceneObject(String name){ //24Feb2000
    //1Apr2000: moved rest of code to separate "SceneObject cloneSceneObject(SceneObject)" method
    return cloneSceneObject(findSceneObject(name));
  }

  public SceneObject cloneSceneObject(SceneObject obj){ //1Apr2000
    //checking for null (i.e. if cloneSceneObject(String) was passed in an unknown
    //object name and that resulting in it calling this method with a null param)
    if(obj==null)
      return null;

    SceneObject cloneObj;

    //clone the object
    try{
      //24Feb2000: all current objects of Stage now support deep cloning and registering
      //with a unique name based on their source/generator object's name
      cloneObj=(SceneObject)obj.clone();
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
      return null;
    }

    //add the object to the scene //31Mar2000: adding the clone below its "genetor"
    addJustBelow(cloneObj,obj);
    //1Apr2000: fixed-bug introduced in 31Mar2000: return the clone, not the genetor object!!!
    return cloneObj;
  }

  @SuppressWarnings("unchecked")
  private ArrayList getClones(ArrayList originals){
    ArrayList<SceneObject> clones=new ArrayList<SceneObject>();
    for(int i=0;i<originals.size();i++)
      try{
        clones.add((SceneObject)((SceneObject)originals.get(i)).clone());
      }catch(CloneNotSupportedException e){
        error("Error at Scene.getClones method, tried to clone a non-cloneable object");
      }
    return clones;
  }

  public void cloneSelection(){ //13Apr2000
    if(objectsSelection!=null) //21May2000
      addSceneObjects(getClones(objectsSelection));
      //don't use serialization-deserialization for cloning the selection: more memory
      //consumption, less speedy (+at this version the objects' icons can be saved with up
      //to a max of 256 colors [saving a GIF stream])
      //addSceneObjects((Vector)Cloning.cloneObject(objectsSelection));
  }

  //REFRESH DISABLING FOR SCRIPTING [BATCH/OFF-SCREEN DRAWING/UPDATING]
  public void setRefreshEnabled(boolean enabled){ //1Jun1999: batch-drawing
    //shouldn't check if is dirty to avoid extra repaints? [maybe not, cause if a script
    //used a refresh_disabled / refresh_enaled block, then it must have done some drawing
    //in the block which it needed to be batch/offscreen drawn and updated using only one
    //repaint at the end (when drawing was completed)]
    if(enabled && !refreshEnabled)
      repaint();
    refreshEnabled=enabled;
  }

  public void refresh(){ //1Jun1999: batch-drawing
    if(refreshEnabled){
      repaint();
    }
  }

  public void refresh(Rectangle r){
    if(refreshEnabled)
      repaint(r);
  }

  //INTERFACES
  //HasImage
  public Image getImage(){
    return bkgrImage;
  }

  public void setImage(Image i){
    if(bkgrImage!=null)
      bkgrImage.flush(); //12May2000
    bkgrImage=i;
    //repaint();
    refresh();
  }

  public void setImage(ImageIcon icon){ //25Feb2000
    setImage((icon!=null)?icon.getImage():null); //4Apr2001-Birb: fixed to accept null
  }

  //3Apr2001-Birb: fixed by using "new ImageIcon" instead of "Class.getResource"
  public void setImage(String filename){
    if(filename.length()==0 || filename==null){
      //faster: if passed an empty or null string then remove the current image
      //[must have a cast to an Image object for the null param of setImage(null)]
      //Birb-2Apr2001: don't use .equals("") or =="" but .length()==0 instead!
      setImage((Image)null);
    }
    else
      setImage(new ImageIcon(filename,null));
  }

  public void setImage(URL image){
    setImage(new ImageIcon(image,"")); //4Apr2001-Birb: changed to call setImage(ImageIcon)
  }

  public void setFillPattern(String image){
  }
  //HasColor ///////////////////
  public Color getColor(){
    return getBackground();
  }
  public void setColor(Color value){
    setBackground(value);
  }

  //LogoScriptable
  //24Feb2000: need static to call from Stage
  public static String[] _getSupportedPrimitiveGroups(){
    return new String[]{
      "gr.cti.eslate.scripting.logo.ScenePrimitives",
      "gr.cti.eslate.scripting.logo.ColorPrimitives",
      "gr.cti.eslate.scripting.logo.ImagePrimitives"
    };
  }

  public String[] getSupportedPrimitiveGroups(){ //24Feb2000
    return _getSupportedPrimitiveGroups();
  }

  //ACTIONS
  public abstract class ObjectAction extends AbstractAction{ //27Jul1999
    protected Object actionObject;

    public ObjectAction(String title,Object obj){
      //26Jul1999: was passing "name" by accident instead of "description" and
      //Java2.1 (and 2.1.2) was throwing "internal compiler error:
      //java.lang.NullPointerException: ."
      Res.setupAction(this,title);
      actionObject=obj;
    }
  }

  public class ObjectActionBringToFront extends ObjectAction  //27Jul1999
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    public ObjectActionBringToFront(Object obj){
      super("Bring To Front",obj);
    }
    public void actionPerformed(ActionEvent e){
      bringToFront(actionObject);
    }
  }

 public class ObjectActionSendToBack extends ObjectAction   //27Jul1999
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  public ObjectActionSendToBack(Object obj){
   super("Send To Back",obj);
  }
  public void actionPerformed(ActionEvent e){
   sendToBack(actionObject);
  }
 }

 public class ObjectActionToggleSelection extends ObjectAction  //13Apr2000
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public ObjectActionToggleSelection(String title,Object obj){ //allow different titles at creation (e.g. either "select" or "deselect")
   super(title,obj);
  }
  public void actionPerformed(ActionEvent e){
   toggle(actionObject,objectsSelection);
   //repaint(); //must do repaint here!
   refresh();
  }
 }

 public class ObjectActionCut extends ObjectAction  //13Apr2000
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;

  public ObjectActionCut(Object obj){
   super("Cut",obj);
  }
  public void actionPerformed(ActionEvent e){
   if(objectsSelection.contains(actionObject)) cut(); //if object is member of a selection, cut the whole selection
   else cut(actionObject);
  }
 }

 public class ObjectActionCopy extends ObjectAction  //13Apr2000
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;

  public ObjectActionCopy(Object obj){
   super("Copy",obj);
  }
  public void actionPerformed(ActionEvent e){
   if(objectsSelection.contains(actionObject)) copy(); //if object is member of a selection, cut the whole selection
   else copy(actionObject);
  }
 }

 public class ObjectActionClear extends ObjectAction  //13Apr2000
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public ObjectActionClear(Object obj){
   super("Clear",obj);
  }
  public void actionPerformed(ActionEvent e){
   if(objectsSelection.contains(actionObject)) clearSelection(); //if object is member of a selection, clear the whole selection
   else {
    removeObject((SceneObject)actionObject);
    //repaint(); //must do repaint here!
    refresh();
   }
  }
 }

 public class ObjectActionClone extends ObjectAction  //13Apr2000: this doesn't change the clipboard contents, just inserts new clone(s) in the scene
 {
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 
 public ObjectActionClone(Object obj){
   super("Clone",obj);
  }
  public void actionPerformed(ActionEvent e){
   if(objectsSelection.contains(actionObject)) cloneSelection(); //if object is member of a selection, clone the whole selection
   else cloneSceneObject((SceneObject)actionObject);
  }
 }

 public class ObjectActionProperties extends ObjectAction
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
   
  public ObjectActionProperties(Object obj){
   super("Properties...",obj);
  }
  public void actionPerformed(ActionEvent e){
   showCustomizer(actionObject);
  }
 }


 public JCheckedActionPopup getPointPointConstraintsPopup(){ //29Jul1999 //2Dec1999: now returns a JCheckedActionPopup
  if(ppConstraintsPopup==null){ //maybe we should empty and refill the popup (when we'll have dynamic constraint classes loading)
   JCheckedActionPopup m=new JCheckedActionPopup();
   for(int i=0;i<constraintsPointPoint.length;i++){
    Class<?> constraint=constraintsPointPoint[i];
    if(constraint!=null) m.add(new PointPointConstraintAction(constraint));
    else m.addSeparator(); //2Dec1999: allowing null entries in the constraints menu to create separators
   }
   ppConstraintsPopup=m;
  }
  return ppConstraintsPopup;
 }

 public JCheckedActionPopup getPointShapeConstraintsPopup(){ //2Dec1999
  if(psConstraintsPopup==null){ //maybe we should empty and refill the popup (when we'll have dynamic constraint classes loading)
   JCheckedActionPopup m=new JCheckedActionPopup();
   for(int i=0;i<constraintsPointShape.length;i++){
    Class<?> constraint=constraintsPointShape[i];
    if(constraint!=null) m.add(new PointShapeConstraintAction(constraint));
    else m.addSeparator(); //2Dec1999: allowing null entries in the constraints menu to create separators
   }
   psConstraintsPopup=m;
  }
  return psConstraintsPopup;
 }

 ////

 private Object[] getSelectedObjectsAndControlPoints(){
  //Vector vec=new Vector(objectsSelection.size(),controlPointsSelection.size());
  ArrayList<Object> vec=new ArrayList<Object>(objectsSelection.size());
  vec.addAll(objectsSelection);
  vec.addAll(controlPointsSelection);
  return vec.toArray(); //Java2 only: for Java1.1 use "VectorUtil.getElements(vec)"
 }

 ////////////

 @SuppressWarnings("unchecked")
public IConstraint createSelectedPointsConstraint(Class constraintClass){ //for ControlPoints only
  Object[] members=controlPointsSelection.toArray(); //Java2 only: for Java1.1 use "VectorUtil.getElements(controlPointsSelection)"
  try{return AbstractConstraint.createAndEnforceConstraint(members,constraintClass);}
  catch(Exception e){
   e.printStackTrace(); //19Apr2000: printing a stack trace when an exception can't be set
   String message=e.getLocalizedMessage();
   JOptionPane.showMessageDialog(this,Res.localize("badConstraint")+((message!=null)?"\n"+message:""),Res.localize("Error"),JOptionPane.ERROR_MESSAGE); //8Aug1999: showing error message if constraint couldn't be forced //19Apr2000: checking whether there's no exception message and not trying to print e.getLocalizedMessage() in this case (instead of printing out "null" on the error dialog)
   return null;
  }
 }

 @SuppressWarnings("unchecked")
public IConstraint createSelectedPointShapeConstraint(Class constraintClass){ //for ControlPoints & Objects
  Object[] members=getSelectedObjectsAndControlPoints();
  try{return AbstractConstraint.createAndEnforceConstraint(members,constraintClass);}
  catch(Exception e){
   JOptionPane.showMessageDialog(this,Res.localize("badConstraint")+"\n"+e.getLocalizedMessage(),Res.localize("Error"),JOptionPane.ERROR_MESSAGE); //showing error message if constraint couldn't be forced
   return null;
  }
 }

 ////////////

 public class PointPointConstraintAction extends AbstractAction //29Jul1999
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
   
  private Class<?> constraintClass;
  @SuppressWarnings("unchecked")
  public PointPointConstraintAction(Class constraintClass){
   Res.setupAction(this,Res.localize(getConstraintTitle(constraintClass))); //how can we cast the Class to an AbstractAction class and call its static method
   this.constraintClass=constraintClass;
  }
  public void actionPerformed(ActionEvent e){
   addConstraint(createSelectedPointsConstraint(constraintClass));
   emptyControlPointsSelection(); //8Aug1999
  }
 }

 //

 public class PointShapeConstraintAction extends AbstractAction //2Dec1999
 {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
   
  private Class<?> constraintClass;
  @SuppressWarnings("unchecked")
  public PointShapeConstraintAction(Class constraintClass){
   Res.setupAction(this,Res.localize(getConstraintTitle(constraintClass))); //how can we cast the Class to an AbstractAction class and call its static method
   this.constraintClass=constraintClass;
  }
  public void actionPerformed(ActionEvent e){
   addConstraint(createSelectedPointShapeConstraint(constraintClass));
   emptyControlPointsSelection();
   emptyObjectsSelection();
  }
 }

 ////////////

 private Class<?>[] constraintsPointPoint={ //29Jul1999, should load this dynamically with getClass and try/catch (or even better parsing some subdir of our resources for appropriate constraint classes)
  PointOntoPointConstraint.class,
  null, //2Dec1999: menu separator
  FixedPointDistanceFromPointConstraint.class,
  MinPointDistanceFromPointConstraint.class,
  MaxPointDistanceFromPointConstraint.class,
  null, //2Dec1999: menu separator
  FixedPointOffsetFromPointConstraint.class,
  null, //2Dec1999: menu separator
  PointsArithmeticMedianConstraint.class
//  PointsGeometricMedianConstraint.class
 };

 private Class<?>[] constraintsPointShape={ //29Jul1999, should load this dynamically with getClass and try/catch (or even better parsing some subdir of our resources for appropriate constraint classes)
  PointIntoShapeConstraint.class
 };

 @SuppressWarnings("unchecked")
public static String getConstraintTitle(Class constraintClass){
  try{
   java.lang.reflect.Method getTitleMethod=constraintClass.getMethod("getTitle"/*, new Class[0]*/);
   if(getTitleMethod!=null) return (String)(getTitleMethod.invoke(null/*, new Class[0]*/));
   else return null;
  }catch(Exception e){return null;}
 }

 //Printable//

 public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
  if(pi>0) return /*Printable.*/NO_SUCH_PAGE;
  Graphics2D g2d=(Graphics2D)g;
  g2d.transform(new AffineTransform(pf.getMatrix())); //21May2000: translate(pf.getImageableX(),pf.getImageableY()) seems that wasn't sufficient to print //???
  //g2d.setFont(new Font("Monospaced", Font.PLAIN,12)); //???

  /* -- stretch ???
  Rectangle2D view=getWorldViewRect();
  setWorldToViewTransform(new AffineTransform(
   pf.getImageableWidth()/view.getWidth(),0, 0, //or view.getMinX() ???
   -pf.getImageableHeight()/view.getHeight(),0, pf.getImageableHeight() //or view.getMinY() ???
  ));
  */

  /* //???
  AffineTransform a=getWorldToViewTransform();
  AffineTransform aa=(AffineTransform)a.clone();
  aa.concatenate(new AffineTransform(pf.getMatrix()));
  setWorldToViewTransform(a); //?
  */

  paintComponent(g2d); //paint(g2d); //21May2000: calling "paint(g2d)" seems to fail to print correctly, but "paintComponents" prints much better
  //g2d.drawRect(0,0,100,100);

  /*setWorldToViewTransform(a); //? */

  return /*Printable.*/PAGE_EXISTS;
 }

 public void setWorldViewBounds(Rectangle2D r){ //21May2000: ???
  AffineTransform a=new AffineTransform(1,0,0,-1,0,getHeight());
  a.concatenate(new AffineTransform(getWidth()/r.getWidth(),0,r.getMinX(), 0,getHeight()/r.getHeight(),r.getMinY()));
  setWorldToViewTransform(a);
 }

 public Rectangle2D getWorldViewRect(){ //21May2000: get the current view window in world coordinates
  double bounds[]=new double[4];
  bounds[0]=bounds[1]=0;
  bounds[2]=getWidth(); bounds[3]=getHeight();
  getViewToWorldTransform().transform(bounds,0,bounds,0,2); //pass 2 as last param, not 4 (means 2 [X,Y] points)
  Rectangle2D rect=new Rectangle2D.Double();
  rect.setFrameFromDiagonal(bounds[0],bounds[1],bounds[2],bounds[3]);
  return rect;
 }

//////////////////

 private void error(String message){ //14Apr2000
  System.err.println("Scene: "+message);
 }



 //Externalizable//

 public Object[] getObjectsAndConstraints(){ //18May2000: must load first the objects and then the constraints: the hashtable doesn't ensure the order in which these too will be loaded, so we must save them in an array
  Object[] o=new Object[2];
  o[0]=getObjects();
  o[1]=getConstraints(); //first load the objects, then the constraints
  return o;
 }

  @SuppressWarnings("unchecked")
  public void setObjectsAndConstraints(Object[] o){ //18May2000
    setObjects((ArrayList)o[0]);
    setConstraints((ArrayList)o[1]);
  }

  //ESlatePart
  public ESlateHandle getESlateHandle() {
    if (sceneHandle == null){
      sceneHandle = ESlate.registerPart(this);
      sceneHandle.addPrimitiveGroup(getSupportedPrimitiveGroups());
      for (int i=0; i < objects.size(); i++){
        sceneHandle.add(((BaseObject)objects.get(i)).getESlateHandle());
      }
    }
    return sceneHandle;
  }

  //29Mar2001-FINAL-VERSION: compatibility with old persistence code
  @SuppressWarnings("unchecked")
  public void setProperties(ObjectHash properties){
    if(properties==null)
      return;
    //scene appearance
    try{
      setWorldToViewTransform((AffineTransform)properties.get(WORLD_TO_VIEW_TRANSFORM_PROPERTY));
    }catch(Exception e){
      setDefaultWorldToViewTransform();
    }
    try{
      setColor((Color)properties.get(COLOR_PROPERTY));
    }catch(Exception e){
      setColor(Color.gray);
    }
    try{
      Object im = properties.get(IMAGE_PROPERTY);
      if (im instanceof ImageIcon)
        setImage((ImageIcon)im);
      else
        setImage(((NewRestorableImageIcon)im).getImage());
    }catch(Exception e){
      setImage((Image)null);
    }
    try{
      setMarksOverShapes(properties.getBoolean(MARKS_OVER_SHAPES_PROPERTY));
    }catch(Exception e){
      setMarksOverShapes(false);
    }
    try{
      setAxisVisible(properties.getBoolean(AXIS_VISIBLE_PROPERTY));
    }catch(Exception e){
      setAxisVisible(false);
    }
    try{
      setGridType(properties.getInt(GRID_TYPE_PROPERTY));
    }catch(Exception e){
      setGridType(GRID_LINES);
    }
    try{
      setGridSize(properties.getDouble(GRID_SIZE_PROPERTY));
    }catch(Exception e){
      setGridSize(DEFAULT_GRID_SIZE);
    }
    try{
      setGridVisible(properties.getBoolean(GRID_VISIBLE_PROPERTY));
    }catch(Exception e){
      setGridVisible(false);
    }
    try{
      setCoordinatesVisible(properties.getBoolean(COORDINATES_VISIBLE_PROPERTY));
    }catch(Exception e){
      setCoordinatesVisible(true);
    }
    try{
      setControlPointsVisible(properties.getBoolean(CONTROL_POINTS_VISIBLE_PROPERTY));
    }catch(Exception e){
      setControlPointsVisible(false);
    }

    //scene objects and constraints
    try{
      setObjectsAndConstraints((Object[])properties.get(OBJECTS_AND_CONSTRAINTS_PROPERTY));
    }
    //use-for-compatibility: scene versions before 18May2000 saved the objects separately
    catch(Exception e){
      //constraints weren't saved/loaded OK in older versions//
      setConstraints(null);
      //scene objects
      try{
        setObjects((ArrayList)properties.get(OBJECTS_PROPERTY));
      }catch(Exception ex){
        setObjects(null);
      }
    }
    //scene functionality
    try{
      setObjectMovementEnabled(properties.getBoolean(OBJECT_MOVEMENT_ENABLED_PROPERTY));
    }catch(Exception e){
      setObjectMovementEnabled(true);
    }
    try{
      setControlPointMovementEnabled(properties.getBoolean(CONTROL_POINT_MOVEMENT_ENABLED_PROPERTY));
    }catch(Exception e){
      setControlPointMovementEnabled(true);
    }
  }

  @SuppressWarnings("unchecked")
  public void setProperties(StorageStructure properties){ //30Nov1999
    if(properties==null)
      return;
    //scene appearance
    try{
      setWorldToViewTransform((AffineTransform)properties.get("WORLD_TO_VIEW_TRANSFORM_PROPERTY"));
    }catch(Exception e){
      setDefaultWorldToViewTransform();
    }
    try{
      setColor((Color)properties.get("COLOR_PROPERTY"));
    }catch(Exception e){
      setColor(Color.gray);
    }
    try{
      Object im = properties.get("IMAGE_PROPERTY");
      if (im instanceof ImageIcon)
        setImage((ImageIcon)im);
      else
        setImage(((NewRestorableImageIcon)im).getImage());
    }catch(Exception e){
      setImage((Image)null);
    }
    setMarksOverShapes(properties.get("MARKS_OVER_SHAPES_PROPERTY",false));
    setAxisVisible(properties.get("AXIS_VISIBLE_PROPERTY",false));
    setGridType(properties.get("GRID_TYPE_PROPERTY",GRID_LINES));
    setGridSize(properties.get("GRID_SIZE_PROPERTY", DEFAULT_GRID_SIZE));
    setGridVisible(properties.get("GRID_VISIBLE_PROPERTY",false));
    setCoordinatesVisible(properties.get("COORDINATES_VISIBLE_PROPERTY",true));
    setControlPointsVisible(properties.get("CONTROL_POINTS_VISIBLE_PROPERTY",false));
    setObjectMovementEnabled(properties.get("OBJECT_MOVEMENT_ENABLED_PROPERTY",true));
    setControlPointMovementEnabled(properties.get("CONTROL_POINT_MOVEMENT_ENABLED_PROPERTY",true));
    setViewMovementEnabled(properties.get("VIEW_MOVEMENT_ENABLED_PROPERTY",true));
    setObjectsAdjustable(properties.get("OBJECTS_ADJUSTABLE_PROPERTY",true));

    if (Integer.valueOf(properties.getDataVersion()).intValue() < 2){
        Object[] obj = (Object[])properties.get("OBJECTS_AND_CONSTRAINTS_PROPERTY");
        try{
          //setObjectsAndConstraints(obj);
          Vector tmpVector = (Vector)obj[0];
          ArrayList<BaseObject> objects = new ArrayList<BaseObject>();
          for (int i=0; i < tmpVector.size(); i++) {
            objects.add((BaseObject)tmpVector.elementAt(i));
          }
          for (int i=0; i < objects.size(); i++) {
            getESlateHandle().add(objects.get(i).getESlateHandle());
          }

          setObjects(objects);

          Vector tmpVector2 = (Vector)obj[1];
          ArrayList<IConstraint> constraints = new ArrayList<IConstraint>();
          for (int i=0; i < tmpVector2.size(); i++) {
            constraints.add((IConstraint)tmpVector2.elementAt(i));
          }
          setConstraints(constraints);
          //use-for-compatibility: scene versions before 18May2000 saved the objects separately
        }catch(Exception e){
        //constraints weren't saved/loaded OK in older versions//
          setConstraints(null);
          //scene objects
          setObjects((ArrayList)properties.get("OBJECTS_PROPERTY"));
        }
    }
    else{
        ArrayList<Object> objs = new ArrayList<Object>();
        try{
          Object[] objects = getESlateHandle().restoreChildObjects(properties, "SceneObjects");
          if (objects != null) {
            for (int i=0; i < objects.length; i++) {
              objs.add(((ESlateHandle)objects[i]).getComponent());
            }
          }
          setObjects(objs);
          String[] objectNames = (String[])properties.get("OBJECT_NAMES_PROPERTY");
          // Restore original order of scene objects.
          if (objectNames != null) {
            for (int i=0; i<objectNames.length; i++) {
              bringToFront(findSceneObject(objectNames[i]));
            }
          }
          setConstraints((ArrayList)properties.get("CONSTRAINTS_PROPERTY"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
  }



  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    //first dispose [clear any current contents] and then load state (this is needed
    //if readExternal is called on an already existing instance which might have inner objects
    //with names that will cause a problem to loaded inner objects when they try to register their
    //names)
    dispose();
    Object o=in.readObject();

    if(o instanceof StorageStructure){
      StorageStructure properties = (StorageStructure)o;
      setProperties(properties);
    }
    //29Mar2001: compatibility with old microworlds, that are possibly stored without using
    // ESlateFieldMap
    else{
      ObjectHash properties = (ObjectHash)o;
      setProperties(properties);
    }
  }

  public ESlateFieldMap2 getProperties(){
    ESlateFieldMap2 properties =new ESlateFieldMap2(STORAGE_VERSION);
    if(properties!=null){
      // setting the preparedForCopy flag of all objects to false so that the storing
      // of handles will be performed normally, no matter the copy operation being on the move
      // (that is that the copy operation has been done but no paste)
      int nObjects = objects.size();
      for (int i=0; i < nObjects; i++) {
          ((SceneObject)objects.get(i)).setPreparedForCopy(false);
      }
      clipboard = null;

      //scene appearance
      properties.put("WORLD_TO_VIEW_TRANSFORM_PROPERTY",getWorldToViewTransform());
      properties.put("COLOR_PROPERTY",getColor());
      Image img=getImage();
      //25Feb2000 //must check for img!=null, since Hashtables don't allow placing neither a
      //null key nor a null value!
      if(img!=null)
        properties.put("IMAGE_PROPERTY",new NewRestorableImageIcon(img));
      properties.put("MARKS_OVER_SHAPES_PROPERTY",isMarksOverShapes());
      properties.put("AXIS_VISIBLE_PROPERTY",isAxisVisible()); //20Apr2000
      properties.put("GRID_TYPE_PROPERTY",getGridType()); //25Feb2000
      properties.put("GRID_VISIBLE_PROPERTY",isGridVisible()); //25Feb2000
      properties.put("GRID_SIZE_PROPERTY", getGridSize());
      properties.put("COORDINATES_VISIBLE_PROPERTY",isCoordinatesVisible());
      properties.put("CONTROL_POINTS_VISIBLE_PROPERTY",isControlPointsVisible()); //15Apr2000
      //scene and constraints objects//
      //properties.put("OBJECTS_AND_CONSTRAINTS_PROPERTY",getObjectsAndConstraints()); //18May2000: saving 1st objects, then constaints, placed both in an array
      properties.put("CONSTRAINTS_PROPERTY",getConstraints());
      //scene functionality
      properties.put("OBJECT_MOVEMENT_ENABLED_PROPERTY",isObjectMovementEnabled());
      properties.put("CONTROL_POINT_MOVEMENT_ENABLED_PROPERTY",isControlPointMovementEnabled());
      properties.put("VIEW_MOVEMENT_ENABLED_PROPERTY",isViewMovementEnabled());
      properties.put("OBJECTS_ADJUSTABLE_PROPERTY",isObjectsAdjustable());
      String[] objectNames = new String[nObjects];
      for (int i=0; i<nObjects; i++) {
        SceneObject so = (SceneObject)objects.get(i);
        objectNames[i] = so.getESlateHandle().getComponentName();
      }
      properties.put("OBJECT_NAMES_PROPERTY", objectNames);
      /* Check if there exists children of type ControlPoint. If it exists then save it using
        * saveChildren().
      */
      ESlateHandle[] handles = getESlateHandle().toArray();
      ArrayList<ESlateHandle> handlesToSave = new ArrayList<ESlateHandle>();
      for (int i=0; i < handles.length; i++) {
          if (SceneObject.class.isAssignableFrom(handles[i].getComponent().getClass())) {
              handlesToSave.add(handles[i]);
          }
      }
      if (handlesToSave.size() != 0){
          ESlateHandle[] handlesToSaveArray = new ESlateHandle[handlesToSave.size()];
          for (int i=0; i < handlesToSaveArray.length; i++)
              handlesToSaveArray[i] = handlesToSave.get(i);
          try{
            getESlateHandle().saveChildren(properties, "SceneObjects", handlesToSaveArray);
          }catch(IOException e){
              e.printStackTrace();
          }
      }
    }
    return properties;
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    ESlateFieldMap2 properties =getProperties();
    out.writeObject(properties); //30Nov1999
  }

  public int getConstraintsCount() {
    return constraints.size();
  }

  public IConstraint getConstraint(int index) {
    return constraints.get(index);
  }

  //29Ju11999: now keeping the constraints here and not at each point
  public void addConstraint(IConstraint c){
    //8Aug1999: now checking for "c!=null" //26Nov1999: made Java1.1 compatible
    if(c!=null)
      constraints.add(c);
  } //...should have a means to remove constraints once some of their members gets killed

  public void removeConstraint(IConstraint constraint){
    constraints.remove(constraint);
    constraint.dispose();
  }
}
