package gr.cti.eslate.vector;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.utils.*;

/**
 * This class implements a vector component. The component presents a vector
 * to the user in graphical and numerical (both north/east and lenth/angle)
 * forms. The user can specify the vector's value either by clicking/dragging
 * the mouse on the graph, or by specifying the numeric values of the vector's
 * components. Finally, the user can specify a scale factor, to make large
 * vectors fit entirely in the graph, and to make small vectors visible.
 * <P>
 * <B>Component plugs:</B>
 * <UL>
 * <LI><B>Vector</B> This is a multimple input&nbsp;/&nbsp;single output plug
 * associated with a
 * <A HREF="gr.cti.eslate.sharedObject.VectorData.html">VectorData</A>
 * shared object.
 * The plug's color is Color.green.
 * </LI>
 * </UL>
 * <P>
 * <B>Logo primitives:</B>
 * <UL>
 * <LI><B>SETVECTOR [x y]</B> Sets the value of the vector in cartesian
 * coordinates.</LI>
 * <LI><B>VECTOR</B> Returns the value of the vector in cartesian
 * coordinates.</LI>
 * <LI><B>SETVECTORNORTH value</B> Sets the value of the vertical component of
 * the vector.</LI>
 * <LI><B>VECTORNORTH</B> Returns the value of the vertical component of
 * the vector.</LI>
 * <LI><B>SETVECTOREAST value</B> Sets the value of the horizontal component of
 * the vector.</LI>
 * <LI><B>VECTOREAST</B> Returns the value of the horizontal component of
 * the vector.</LI>
 * <LI><B>SETVECTORPOLAR [length angle]</B> Sets the value of the vector in
 * polar coordinates.</LI>
 * <LI><B>VECTORPOLAR</B> Returns the value of the vector in polar
 * coordinates.</LI>
 * <LI><B>SETVECTORLENGTH value</B> Sets the length of the the vector.</LI>
 * <LI><B>VECTORLENGTH</B> Returns the length of the the vector.</LI>
 * <LI><B>SETVECTORANGLE value</B> Sets the angle of the the vector.</LI>
 * <LI><B>VECTORANGLE</B> Returns the angle of the the vector.</LI>
 * <LI><B>SETVECTORSCALE value</B> Sets the scale of the the vector.</LI>
 * <LI><B>VECTORSCALE</B> Returns the scale of the the vector.</LI>
 * <LI><B>SETPRECISION n</B> Sets the number of digits displayed after the
 * decimal point.</LI>
 * <LI><B>PRECISION</B> Returns the number of digits displayed after the
 * decimal point.</LI>
 * </UL>
 * @author      Kriton Kyrimis
 * @version     2.0.2, 23-Jan-2008
 * @see         gr.cti.eslate.sharedObject.VectorData
 */
public class VectorComponent extends JPanel
  implements ESlatePart, ActionListener, FocusListener, AsVectorComponent,
             Externalizable, Serializable
{
  private ESlateHandle handle;
  private JLabel northLabel, eastLabel, lengthLabel, angleLabel, scaleLabel;
  private JTextField northField, eastField, lengthField, angleField, scaleField;
  private VectorPanel vPanel = null;
  private final static int hspace = 4;
  private final static int vspace = 4;
  private final static int bigVspace = 2 * vspace;
  private final static int prefCols = 5;
  final static Dimension winSize = new Dimension(290, 220);
  final static Dimension minSize = new Dimension(180, 140);
  private VectorData vect;
  private NumberSO xSO;
  private NumberSO ySO;
  private NumberSO lSO;
  private NumberSO aSO;
  private double hor;
  private double ver;
  private double scale;
  private String horizontalName;
  private String verticalName;
  private String angleName;
  private String lengthName;
  private boolean changingScale;
  private ResourceBundle resources;
  private final static String version = "2.0.2";
  private final static int saveVersion = 1;
  private NumberFormat nf;
  private boolean editable = true;
  private boolean graphVisible = true;
  private boolean componentsVisible = true;
  private Box numCol = null;;
  private JPanel contents;
  private JPanel pane;
  private final static int DEFAULT_PRECISION = 2;
  private int precision = DEFAULT_PRECISION;
  private double inverseNegativePrecision;
  private boolean needMenuBar;
  private Plug xPlug;
  private Plug yPlug;
  private Plug lPlug;
  private Plug aPlug;
  private double lastAngle = 0.0;
  private ArrayList<VectorListener> vectorListeners =
    new ArrayList<VectorListener>();
  private double maxLength = 0.0;
  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  PerformanceTimer saveTimer;
  /**
   * Timer which measures the time required for the construction of the
   * component.
   */
  PerformanceTimer constructorTimer;
  /**
   * Timer which measures the time required for initializing the E-Slate
   * aspect of the component.
   */
  PerformanceTimer initESlateAspectTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  private final static String HORIZONTAL = "horizontal";
  private final static String VERTICAL = "vertical";
  private final static String SCALE = "scale";
  private final static String PRECISION = "precision";
  private final static String EDITABLE = "editable";
  private final static String GRAPH_VISIBLE = "graphVisible";
  private final static String COMPONENTS_VISIBLE = "componentsVisible";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";
  private final static String HORIZONTAL_NAME = "horizontalName";
  private final static String VERTICAL_NAME = "verticalName";
  private final static String ANGLE_NAME = "angleName";
  private final static String LENGTH_NAME = "lengthName";
  private final static String LAST_ANGLE = "lastAngle";
  private final static String MAX_LENGTH = "maxLength";
  static final long serialVersionUID = 8897658012262911547L;

  /**
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  private ESlateInfo getInfo()
  {
    String[] info = {
      resources.getString("credits1"),
      resources.getString("credits2"),
      resources.getString("credits3"),
    };
    return new ESlateInfo(
      resources.getString("componentName") + ", " +
        resources.getString("version") + " " + version,
      info);
  }

  /**
   * Creates a vector component.
   * @param     x               The vector's horizontal component.
   * @param     y               The vector's vertical component.
   * @param     scale           The scale used to draw the vector.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public VectorComponent(double x, double y, double scale, boolean needMenuBar)
  {
    super();
    initVector(x, y, scale, needMenuBar);
  }

  /**
   * Creates a vector component. Drawing scale is 1.0.
   * @param     x               The vector's horizontal component.
   * @param     y               The vector's vertical component.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public VectorComponent(double x, double y, boolean needMenuBar)
  {
    super();
    initVector(x, y, 1.0, needMenuBar);
  }

  /**
   * Creates a vector component. Vector size is (0,0) and the drawing scale is
   * 1.0.
   * @param     needMenuBar     Specifies whether the E-Slate menu bar will be
   *                            visible.
   */
  public VectorComponent(boolean needMenuBar)
  {
    super();
    initVector(0.0, 0.0, 1.0, needMenuBar);
  }

  /**
   * Creates a vector component. Vector size is (0,0) the drawing scale is
   * 1.0, and the menu bar will be visible.
   */
  public VectorComponent()
  {
    super();
    initVector(0.0, 0.0, 1.0, false);
  }

  /**
   * Initialize component and create component GUI.
   * @param     x               The vector's horizontal component.
   * @param     y               The vector's vertical component.
   * @param     vscale          The scale used to draw the vector.
   * @param     needMenuBar     Specifies whether to put the E-Slate menu bar
   *                            at the top of the component.
   */
  private void initVector(double x, double y, double vscale,
                          boolean needMenuBar)
  {
    resources = ResourceBundle.getBundle(
      "gr.cti.eslate.vector.VectorResource", Locale.getDefault()
    );

    attachTimers();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionStarted(this);
    pm.init(constructorTimer);

    this.needMenuBar = needMenuBar;

    setPreferredSize(VectorComponent.winSize);
    setMinimumSize(VectorComponent.minSize);
    
    calculateInverseNegativePrecision();
    nf = NumberFormat.getInstance(Locale.getDefault());
    nf.setMaximumFractionDigits(precision);

    hor = x;
    ver = y;
    scale = vscale;
    changingScale = false;

    Box row, column1, column2, scaleRow;
    /*
    Font normalFont;
    Locale locale = Locale.getDefault();
    if (locale.getLanguage().equals("el") ||
        locale.getLanguage().equals("el_GR")) {
      // Generic fonts do not support Greek characters properly.
      normalFont = new Font("Helvetica", Font.PLAIN, 12);
    }else{
      normalFont = new Font("SansSerif", Font.PLAIN, 12);
    }
    */

    horizontalName = resources.getString("x");
    verticalName = resources.getString("y");
    angleName = resources.getString("angle");
    lengthName = resources.getString("length");

    setLayout(new BorderLayout());

    contents = new JPanel(new BorderLayout());
    pane = contents;
    pane.setForeground(Color.black);
    pane.setBorder(BorderFactory.createRaisedBevelBorder());

    row = Box.createHorizontalBox();
    column1 = Box.createVerticalBox();
    column2 = Box.createVerticalBox();
    numCol = Box.createVerticalBox();
    scaleRow = Box.createHorizontalBox();

    vPanel = new VectorPanel();
    //vPanel.setSize(vecSize);
    if (editable) {
      vPanel.addMouseMotionListener(vPanel);
      vPanel.addMouseListener(vPanel);
    }

    eastLabel = new JLabel(horizontalName);
    //eastLabel.setFont(normalFont);
    eastLabel.setAlignmentX(CENTER_ALIGNMENT);
    column1.add(eastLabel);
    eastField = new JTextField();
    //eastField.setFont(normalFont);
    eastField.addActionListener(this);
    if (editable) {
      eastField.addFocusListener(this);
    }
    eastField.setColumns(prefCols);
    eastField.setMaximumSize(eastField.getPreferredSize());
    eastField.setEditable(editable);
    column1.add(eastField);
    column1.add(Box.createVerticalStrut(bigVspace));
    lengthLabel = new JLabel(lengthName);
    //lengthLabel.setFont(normalFont);
    lengthLabel.setAlignmentX(CENTER_ALIGNMENT);
    column1.add(lengthLabel);
    lengthField = new JTextField();
    //lengthField.setFont(normalFont);
    lengthField.addActionListener(this);
    if (editable) {
      lengthField.addFocusListener(this);
    }
    lengthField.setColumns(prefCols);
    lengthField.setMaximumSize(eastField.getPreferredSize());
    lengthField.setEditable(editable);
    column1.add(lengthField);
    column1.add(Box.createVerticalStrut(bigVspace));
    scaleLabel = new JLabel(resources.getString("scale") + " 1:");
    //scaleLabel.setFont(normalFont);
    scaleLabel.setAlignmentX(RIGHT_ALIGNMENT);
    Dimension scaleSize = new Dimension(
      scaleLabel.getMinimumSize().width, eastField.getPreferredSize().height);
    scaleLabel.setPreferredSize(scaleSize);
    scaleLabel.setMaximumSize(scaleSize);
    scaleLabel.setMinimumSize(scaleSize);
    scaleRow.add(Box.createHorizontalGlue());
    scaleRow.add(scaleLabel);
    column1.add(scaleRow);

    northLabel = new JLabel(verticalName);
    //northLabel.setFont(normalFont);
    northLabel.setAlignmentX(CENTER_ALIGNMENT);
    column2.add(northLabel);
    northField = new JTextField();
    //northField.setFont(normalFont);
    northField.addActionListener(this);
    if (editable) {
      northField.addFocusListener(this);
    }
    northField.setColumns(prefCols);
    northField.setMaximumSize(eastField.getPreferredSize());
    northField.setEditable(editable);
    column2.add(northField);
    column2.add(Box.createVerticalStrut(bigVspace));
    angleLabel = new JLabel(angleName);
    //angleLabel.setFont(normalFont);
    angleLabel.setAlignmentX(CENTER_ALIGNMENT);
    column2.add(angleLabel);
    angleField = new JTextField();
    //angleField.setFont(normalFont);
    angleField.addActionListener(this);
    if (editable) {
      angleField.addFocusListener(this);
    }
    angleField.setColumns(prefCols);
    angleField.setMaximumSize(eastField.getPreferredSize());
    angleField.setEditable(editable);
    column2.add(angleField);
    column2.add(Box.createVerticalStrut(bigVspace));
    scaleField = new JTextField();
    //scaleField.setFont(normalFont);
    scaleField.addActionListener(this);
    scaleField.addFocusListener(this);
    scaleField.setColumns(prefCols);
    scaleField.setMaximumSize(eastField.getPreferredSize());
    column2.add(scaleField);

    row.add(column1);
    row.add(Box.createHorizontalStrut(hspace));
    row.add(column2);
    numCol.add(row);

    pane.add(vPanel, BorderLayout.CENTER);
    pane.add(numCol, BorderLayout.EAST);

    add(pane, BorderLayout.CENTER);

    setNE(hor, ver);
    setScale(scale);

    addHierarchyListener(new HierarchyListener()
    {
      public void hierarchyChanged(HierarchyEvent e)
      {
        Container c = scaleField.getParent();
        while (c != null) {
          if (c.isFocusCycleRoot()) {
            c.setFocusTraversalPolicy(new MyFocusTraversalPolicy());
            removeHierarchyListener(this);
            break;
          }
          c = c.getParent();
        }
      }
    });
    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }

  /**
   * Add a listener for vector events.
   * @param     listener        The listener to add.
   */
  public void addVectorListener(VectorListener listener)
  {
    synchronized (vectorListeners) {
      if (!vectorListeners.contains(listener)) {
        vectorListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener for vector events.
   * @param     listener        The listener to remove.
   */
  public void removeVectorListener(VectorListener listener)
  {
    synchronized (vectorListeners) {
      int ind = vectorListeners.indexOf(listener);
      if (ind >= 0) {
        vectorListeners.remove(ind);
      }
    }
  }

  /**
   * Specifies whether the vector is editable by the user.
   * @param     status  True if yes, false if no.
   */
  public void setEditable(boolean status)
  {
    if (editable != status) {
      if (status) {
        vPanel.addMouseMotionListener(vPanel);
        vPanel.addMouseListener(vPanel);
        eastField.addFocusListener(this);
        lengthField.addFocusListener(this);
        northField.addFocusListener(this);
        angleField.addFocusListener(this);
      }else{
        vPanel.removeMouseMotionListener(vPanel);
        vPanel.removeMouseListener(vPanel);
        eastField.removeFocusListener(this);
        lengthField.removeFocusListener(this);
        northField.removeFocusListener(this);
        angleField.removeFocusListener(this);
      }
      eastField.setEditable(status);
      lengthField.setEditable(status);
      northField.setEditable(status);
      angleField.setEditable(status);
      editable = status;
    }
  }

  /**
   * Tests whether the vector is editable by the user.
   * @return    True if yes, false if no.
   */
  public boolean isEditable()
  {
    return editable;
  }

  /**
   * Specifies whether the vector's graphical representation should be
   * displayed.
   * @param     status  True if yes, false if no.
   */
  public void setGraphVisible(boolean status)
  {
    if (status != graphVisible) {
      if (status) {
        pane.add(vPanel, BorderLayout.CENTER);
      }else{
        pane.remove(vPanel);
      }
      graphVisible = status;
      reLayOut();
    }
  }

  /**
   * Tests whether the vector's graphical representation is being
   * displayed.
   * @return    status  True if yes, false if no.
   */
  public boolean isGraphVisible()
  {
    return graphVisible;
  }

  /**
   * Specifies whether the vector's numeric representation should be
   * displayed.
   * @param     status  True if yes, false if no.
   */
  public void setComponentsVisible(boolean status)
  {
    if (status != componentsVisible) {
      if (status) {
        pane.add(numCol, BorderLayout.EAST);
      }else{
        pane.remove(numCol);
      }
      componentsVisible = status;
      reLayOut();
    }
  }

  /**
   * Adjusts the top level layout of the component depending on the
   * visibility of the graph and numeric areas.
   */
  private void reLayOut()
  {
    pane.removeAll();
    if (graphVisible && componentsVisible) {
      pane.add(vPanel, BorderLayout.CENTER);
      pane.add(numCol, BorderLayout.EAST);
    }else{
      if (graphVisible) {
        pane.add(vPanel, BorderLayout.CENTER);
      }else{
        if (componentsVisible) {
          pane.add(numCol, BorderLayout.CENTER);
        }
      }
    }
    if (!graphVisible) {
      scaleLabel.setVisible(false);
      scaleField.setVisible(false);
    }else{
      scaleLabel.setVisible(true);
      scaleField.setVisible(true);
    }
    pane.revalidate();
    pane.paintImmediately(pane.getVisibleRect());
    refreshDisplay();
  }

  /**
   * Tests whether the vector's numeric representation is being
   * displayed.
   * @return    status  True if yes, false if no.
   */
  public boolean isComponentsVisible()
  {
    return componentsVisible;
  }

  /**
   * Returns the vector's vertical component.
   * @return    The vertical component.
   */
  public double getNorth()
  {
    return ver;
  }

  /**
   * Returns the vector's horizontal component.
   * @return    The horizontal component.
   */
  public double getEast()
  {
    return hor;
  }

  /**
   * Returns the vector's angle in degrees.
   * @return    The vector's angle in degrees.
   */
  public double getAngle()
  {
    if ((hor == 0.0) && (ver == 0.0)) {
      return lastAngle;
    }else{
      double degrees = Math.atan2(ver, hor) * 180.0 / Math.PI;
      if (degrees < 0.0) {
        degrees += 360.0;
      }
      lastAngle = degrees;
      return degrees;
    }
  }

  /**
   * Returns the vector's length.
   * @return    The length.
   */
  public double getLength()
  {
    return Math.sqrt(hor*hor + ver*ver);
  }

  /**
   * Returns the vector's scale.
   * @return    The scale.
   */
  public double getScale()
  {
    return scale;
  }

  /**
   * Sets the vector's value by specifying its horizontal and vertical
   * components.
   * @param     x       Vector's horizontal component.
   * @param     y       Vector's vertical component.
   */
  public void setNE(double x, double y)
  {
    setNEValue(x, y);
    if (vect != null) {
      vect.setVectorData(hor, ver);
      xSO.setValue(hor);
      ySO.setValue(ver);
      lSO.setValue(getLength());
      aSO.setValue(getAngle());
    }
  }

  /**
   * Called by the two setNE() functions to update the various text fields.
   * @param     x       Vector's horizontal component.
   * @param     y       Vector's vertical component.
   */
  private void setNEValue(double x, double y)
  {
    double oldHor = hor;
    double oldVer = ver;
    double len = Math.sqrt(x*x + y*y);
    if (maxLength > 0.0 && len > maxLength) {
      double a = Math.atan2(y, x);
      x = maxLength * Math.cos(a);
      y = maxLength * Math.sin(a);
    }
    hor = x;
    ver = y;
    refreshDisplay();
    fireVectorListeners(oldHor, oldVer, hor, ver);
  }

  /**
   * Ensures that the value of the vector is actually displayed in the UI.
   */
  private void refreshDisplay()
  {
    if (componentsVisible) {
      northField.setText(myFormat(ver));
      eastField.setText(myFormat(hor));
      lengthField.setText(myFormat(getLength()));
      angleField.setText(myFormat(getAngle()));
    }
    if (graphVisible) {
      vPanel.setValue(hor, ver, scale);
      vPanel.repaint();
    }
  }

  /**
   * Fires all listeners registered for vector events.
   * @param     oldX    The old x value of the vector.
   * @param     oldY    The old y value of the vector.
   * @param     newX    The new x value of the vector.
   * @param     newY    The new y value of the vector.
   */
  @SuppressWarnings(value={"unchecked"})
  private void fireVectorListeners
    (double oldX, double oldY, double newX, double newY)
  {
    ArrayList<VectorListener> listeners;
    synchronized(vectorListeners) {
      listeners = (ArrayList<VectorListener>)(vectorListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      VectorListener l = (VectorListener)(listeners.get(i));
      VectorEvent e = new VectorEvent(
        handle, oldX, oldY, newX, newY
      );
      l.valueChanged(e);
    }
  }

  /**
   * Sets the number of digits displayed after the decimal point.
   * @param     n       The number of digits.
   */
  public void setPrecision(int n)
  {
    precision = n;
    calculateInverseNegativePrecision();
    nf.setMaximumFractionDigits(precision);
    setNEValue(hor, ver);
  }

  /**
   * Returns the number of digits displayed after the decimal point.
   * @return    The requested number.
   */
  public int getPrecision()
  {
    return precision;
  }

  /**
   * Sets the vector's scale.
   * @param     sc      Vector's scale.
   */
  public void setScale(double sc)
  {
    changingScale = true;
    if (sc <= 0.0) {
      ESlateOptionPane.showMessageDialog(
        this, resources.getString("needPositive"),
        resources.getString("error"),
        JOptionPane.ERROR_MESSAGE);
/*
      sc = 1.0;
      for (int i=0; i<Math.abs(precision); i++) {
        sc /= 10.0;
      }
*/
      sc = scale;
    }
    scale = sc;
    vPanel.setValue(hor, ver, scale);
    vPanel.repaint();
    scaleField.setText(myFormat(scale));
    changingScale = false;
  }

  /**
   * Sets the vector's vertical component.
   * @param     y       Vector's vertical component.
   */
  public void setNorth(double y)
  {
    setNE(hor, y);
  }

  /**
   * Sets the vector's horizontal component.
   * @param     x       Vector's horizontal component.
   */
  public void setEast(double x)
  {
    setNE(x, ver);
  }

  /**
   * Sets the vector's value by specifying its length and angle.
   * @param     length  Vector's length.
   * @param     angle   Vector's angle.
   */
  public void setLA(double length, double angle)
  {
    double radians = angle * Math.PI / 180.0;
    double x = length * Math.cos(radians);
    double y = length * Math.sin(radians);
    setNE(x, y);
  }

  /**
   * Sets the vector's length.
   * @param     length  Vector's length.
   */
  public void setLength(double length)
  {
    setLA(length, getAngle());
  }

  /**
   * Sets the vector's angle.
   * @param     angle   Vector's angle.
   */
  public void setAngle(double angle)
  {
    setLA(getLength(), angle);
  }

  /**
   * Sets the name of the horizontal component.
   * @param     name    The name of the horizontal component.
   * @exception NameUsedException       Thrown if the specified name is the
   *                                    same as that of the vertical
   *                                    component, the length, or the angle.
   */
  public void setEastName(String name) throws NameUsedException
  {
    if (name.equals(verticalName) || name.equals(angleName) ||
        name.equals(lengthName)) {
      throw new NameUsedException("nameUsed");
    }else{
      try {
        xPlug.setName(name);
      } catch (PlugExistsException pee) {
      }
      horizontalName = name;
      eastLabel.setText(horizontalName);
    }
  }

  /**
   * Returns the name of the horizontal component.
   * @return    The name of the horizontal component.
   */
  public String getEastName()
  {
    return horizontalName;
  }

  /**
   * Sets the name of the vertical component.
   * @param     name    The name of the vertical component.
   * @exception NameUsedException       Thrown if the specified name is the
   *                                    same as that of the horizontal
   *                                    component, the length, or the angle.
   */
  public void setNorthName(String name) throws NameUsedException
  {
    if (name.equals(horizontalName) || name.equals(angleName) ||
        name.equals(lengthName)) {
      throw new NameUsedException("nameUsed");
    }else{
      try {
        yPlug.setName(name);
      } catch (PlugExistsException pee) {
      }
      verticalName = name;
      northLabel.setText(verticalName);
    }
  }

  /**
   * Returns the name of the vertical component.
   * @return    The name of the vertical component.
   */
  public String getNorthName()
  {
    return verticalName;
  }

  /**
   * Sets the name of the angle of the vector.
   * @param     name    The name of the angle of the vector.
   * @exception NameUsedException       Thrown if the specified name is the
   *                                    same as that of the horizontal
   *                                    component, the vertical component, or
   *                                    the length.
   */
  public void setAngleName(String name) throws NameUsedException
  {
    if (name.equals(verticalName) || name.equals(horizontalName) ||
        name.equals(lengthName)) {
      throw new NameUsedException("nameUsed");
    }else{
      try {
        aPlug.setName(name);
      } catch (PlugExistsException pee) {
      }
      angleName = name;
      angleLabel.setText(angleName);
    }
  }

  /**
   * Returns the name of the angle of the vector.
   * @return    The name of the angle of the vector.
   */
  public String getAngleName()
  {
    return angleName;
  }

  /**
   * Sets the name of the length of the vector.
   * @param     name    The name of the length of the vector.
   * @exception NameUsedException       Thrown if the specified name is the
   *                                    same as that of the horizontal
   *                                    component, the vertical component, or
   *                                    the angle.
   */
  public void setLengthName(String name) throws NameUsedException
  {
    if (name.equals(verticalName) || name.equals(horizontalName) ||
        name.equals(angleName)) {
      throw new NameUsedException("nameUsed");
    }else{
      try {
        lPlug.setName(name);
      } catch (PlugExistsException pee) {
      }
      lengthName = name;
      lengthLabel.setText(lengthName);
    }
  }

  /**
   * Returns the name of the length of the vector.
   * @return    The name of the length of the vector.
   */
  public String getLengthName()
  {
    return lengthName;
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The requested handle. If the component's constructor has not
   *            been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      handle = ESlate.registerPart(this);

      initESlate();

      pm.stop(initESlateAspectTimer);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, handle, "", "ms");
    }
    return handle;
  }

  /**
   * Initializes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    //handle = ESlate.registerPart(this);

    handle.setInfo(getInfo());

    vect = new VectorData(handle);
    SharedObjectListener vListener = new SharedObjectListener()
    {
      public synchronized void handleSharedObjectEvent(SharedObjectEvent soe)
      {
        VectorData newVect = (VectorData)(soe.getSharedObject());
        hor = newVect.getX();
        ver = newVect.getY();
        setNE(hor, ver);
      }
    };

    xSO = new NumberSO(handle, getX());
    SharedObjectListener xListener = new SharedObjectListener()
    {
      public synchronized void handleSharedObjectEvent(SharedObjectEvent soe)
      {
        NumberSO newX = (NumberSO)(soe.getSharedObject());
        hor = newX.doubleValue();
        setNE(hor, ver);
      }
    };

    ySO = new NumberSO(handle, getY());
    SharedObjectListener yListener = new SharedObjectListener()
    {
      public synchronized void handleSharedObjectEvent(SharedObjectEvent soe)
      {
        NumberSO newY = (NumberSO)(soe.getSharedObject());
        ver = newY.doubleValue();
        setNE(hor, ver);
      }
    };

    lSO = new NumberSO(handle, getLength());
    SharedObjectListener lListener = new SharedObjectListener()
    {
      public synchronized void handleSharedObjectEvent(SharedObjectEvent soe)
      {
        NumberSO newLength = (NumberSO)(soe.getSharedObject());
        setLength(newLength.doubleValue());
      }
    };

    aSO = new NumberSO(handle, getAngle());
    SharedObjectListener aListener = new SharedObjectListener()
    {
      public synchronized void handleSharedObjectEvent(SharedObjectEvent soe)
      {
        NumberSO newAngle = (NumberSO)(soe.getSharedObject());
        setAngle(newAngle.doubleValue());
      }
    };

    try {
      Plug plug = new SingleInputMultipleOutputPlug(
        handle, resources, "vector", Color.green,
        gr.cti.eslate.sharedObject.VectorData.class, vect, vListener
      );
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get value of vector from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            VectorData so =
              (VectorData)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            setNE(so.getX(), so.getY());
          }
        }
      });

      xPlug = new SingleInputMultipleOutputPlug(
        handle, resources, "x", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, xSO, xListener
      );
      plug.addPlug(xPlug);
      xPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get X value of vector from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            NumberSO so =
              (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            setEast(so.doubleValue());
          }
        }
      });
      try {
        xPlug.setName(horizontalName);
      } catch (PlugExistsException pee) {
      }

      yPlug = new SingleInputMultipleOutputPlug(
        handle, resources, "y", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, ySO, yListener
      );
      plug.addPlug(yPlug);
      yPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get Y value of vector from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            NumberSO so =
              (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            setNorth(so.doubleValue());
          }
        }
      });
      try {
        yPlug.setName(verticalName);
      } catch (PlugExistsException pee) {
      }

      lPlug = new SingleInputMultipleOutputPlug(
        handle, resources, "length", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, lSO, lListener
      );
      plug.addPlug(lPlug);
      lPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get length of vector from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            NumberSO so =
              (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            setLength(so.doubleValue());
          }
        }
      });
      try {
        lPlug.setName(lengthName);
      } catch (PlugExistsException pee) {
      }

      aPlug = new SingleInputMultipleOutputPlug(
        handle, resources, "angle", new Color(135, 206, 250),
        gr.cti.eslate.sharedObject.NumberSO.class, aSO, aListener
      );
      plug.addPlug(aPlug);
      aPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
        {
          // Get angle of vector from the component to which we connected.
          if (e.getType() == Plug.INPUT_CONNECTION) {
            NumberSO so =
              (NumberSO)(((SharedObjectPlug)(e.getPlug())).getSharedObject());
            setAngle(so.doubleValue());
          }
        }
      });
      try {
        aPlug.setName(angleName);
      } catch (PlugExistsException pee) {
      }


    } catch (InvalidPlugParametersException e) {
      System.out.println(e.getMessage());
    } catch (PlugExistsException e) {
      System.out.println(e.getMessage());
    }

    // Register supported Logo primitive group.
    handle.addPrimitiveGroup(
      "gr.cti.eslate.scripting.logo.VectorComponentPrimitives"
    );

    try {
      handle.setUniqueComponentName(resources.getString("name"));
    } catch (RenamingForbiddenException e) {
    }

    if (needMenuBar) {
      add(handle.getMenuPanel(), BorderLayout.NORTH);
    }
    handle.addESlateListener( new ESlateAdapter() {
      public void handleDisposed(HandleDisposalEvent e)
      {
        dispose();
      }
    });
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    ESlateFieldMap2 map = new ESlateFieldMap2(saveVersion, 14);

    map.put(HORIZONTAL, hor);
    map.put(VERTICAL, ver);
    map.put(SCALE, scale);
    map.put(PRECISION, precision);
    map.put(EDITABLE, editable);
    map.put(GRAPH_VISIBLE, graphVisible);
    map.put(COMPONENTS_VISIBLE, componentsVisible);
    map.put(MINIMUM_SIZE, getMinimumSize());
    map.put(MAXIMUM_SIZE, getMaximumSize());
    map.put(PREFERRED_SIZE, getPreferredSize());
    map.put(HORIZONTAL_NAME, horizontalName);
    map.put(VERTICAL_NAME, verticalName);
    map.put(ANGLE_NAME, angleName);
    map.put(LENGTH_NAME, lengthName);
    map.put(LAST_ANGLE, lastAngle);
    map.put(MAX_LENGTH, maxLength);

    oo.writeObject(map);

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    Object obj = (oi.readObject());
    double h, v, sc;
    int p;
    boolean edit;
    if (obj instanceof StorageStructure) {
      StorageStructure map = (StorageStructure)obj;
      h = map.get(HORIZONTAL, 0.0);
      v = map.get(VERTICAL, 0.0);
      lastAngle = map.get(LAST_ANGLE, 0.0);
      maxLength = map.get(MAX_LENGTH, 0.0);
      sc = map.get(SCALE, 0.0);
      p = map.get(PRECISION, DEFAULT_PRECISION);
      edit = map.get(EDITABLE, true);
      graphVisible = map.get(GRAPH_VISIBLE, true);
      componentsVisible = map.get(COMPONENTS_VISIBLE, true);
      setMinimumSize(map.get(MINIMUM_SIZE, VectorComponent.winSize));
      setMaximumSize(map.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(map.get(PREFERRED_SIZE, VectorComponent.minSize));
      try {
        setEastName(map.get(HORIZONTAL_NAME, horizontalName));
      } catch (NameUsedException nue) {
        System.out.println(nue.getMessage());
      }
      try {
        setNorthName(map.get(VERTICAL_NAME, verticalName));
      } catch (NameUsedException nue) {
        System.out.println(nue.getMessage());
      }
      try {
        setAngleName(map.get(ANGLE_NAME, angleName));
      } catch (NameUsedException nue) {
        System.out.println(nue.getMessage());
      }
      try {
        setLengthName(map.get(LENGTH_NAME, lengthName));
      } catch (NameUsedException nue) {
        System.out.println(nue.getMessage());
      }
    }else{
      h = ((Double)obj).doubleValue();
      v = ((Double)(oi.readObject())).doubleValue();
      lastAngle = 0.0;
      sc = ((Double)(oi.readObject())).doubleValue();
      try {
        p = ((Integer)(oi.readObject())).intValue();
      } catch (IOException e) {
        // We are probably reading the state from an older version that did not
        // save the precision. Use default value.
        p = DEFAULT_PRECISION;
      }
      edit = true;
      graphVisible = true;
      componentsVisible = true;
    }
    setScale(sc);
    setNE(h, v);
    setPrecision(p);
    setEditable(edit);
    reLayOut();

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Save the component's state.
   * @param     oo      The stream where the state should be saved.
   */
  public void writeObject(ObjectOutputStream oo) throws IOException
  {
    writeExternal(oo);
  }

  /**
   * Load the component's state.
   * @param     oi      The stream from where the state should be loaded.
   */
  public void readObject(ObjectInputStream oi)
    throws IOException, ClassNotFoundException
  {
    readExternal(oi);
  }

  /**
   * Run the component as an application--useful for debugging.
   * @param     args    Application's arguments (not used).
   */
  public static void main(String[] args)
  {
    MainFrame f = new MainFrame("", winSize);

    VectorComponent vector = new VectorComponent();

    f.add("Center", vector);
    f.pack();

    f.setTitle(vector.resources.getString("name"));

    f.setVisible(true);
  }

  /**
   * Action event handler.
   * @param     e       The event to handle.
   */
  public void actionPerformed(java.awt.event.ActionEvent e)
  {
    Object source = e.getSource();
    String s;
    double temp;

    if (source.equals(northField)) {
      s = northField.getText();
      if (s.equals("")) {
        temp = 0.0;
        northField.setText("0");
      }else{
        try {
          temp = nf.parse(s).doubleValue();
        } catch (ParseException pe) {
          temp = ver;
        }
      }
      setNorth(temp);
    }else{
      if (source.equals(eastField)) {
        s = eastField.getText();
        if (s.equals("")) {
          temp = 0.0;
          eastField.setText("0");
        }else{
          try {
            temp = nf.parse(s).doubleValue();
          } catch (ParseException pe) {
            temp = hor;
          }
        }
        setEast(temp);
      }else{
        if (source.equals(lengthField)) {
          s = lengthField.getText();
          if (s.equals("")) {
            temp = 0.0;
            lengthField.setText("0");
          }else{
            try {
              temp = nf.parse(s).doubleValue();
            } catch (ParseException pe) {
              temp = getLength();
            }
          }
          setLength(temp);
        }else{
          if (source.equals(angleField)) {
            s = angleField.getText();
            if (s.equals("")) {
              temp = 0.0;
              angleField.setText("0");
            }else{
              try {
                temp = nf.parse(s).doubleValue();
              } catch (ParseException pe) {
                temp = getAngle();
              }
            }
            setAngle(temp);
          }else{
            if (source.equals(scaleField)) {
              s = scaleField.getText();
              if (s.equals("")) {
                temp = 0.0;
                scaleField.setText("0");
              }else{
                try {
                  temp = nf.parse(s).doubleValue();
                } catch (ParseException pe) {
                  temp = scale;
                }
              }
              setScale(temp);
            }
          }
        }
      }
    }
  }

  /**
   * Handle "focus gained" events.
   * @param     e       The event to handle.
   */
  public void focusGained(FocusEvent e)
  {
  }

  /**
   * Handle "focus lost" events.
   * @param     e       he event to handle.
   */
  public void focusLost(FocusEvent e)
  {
    JTextField tf = (JTextField)e.getComponent();
    double temp;
    String s;

    if (tf.equals(northField)) {
      s = northField.getText();
      if (s.equals("")){
        temp = 0.0;
        northField.setText("0");
      }else{
        try {
          temp = nf.parse(s).doubleValue();
        } catch (ParseException pe) {
          temp = ver;
        }
      }
      setNorth(temp);
    }else{
      if (tf.equals(eastField)) {
        s = eastField.getText();
        if (s.equals("")) {
          temp = 0.0;
          eastField.setText("0");
        }else{
          try {
            temp = nf.parse(s).doubleValue();
          } catch (ParseException pe) {
            temp = hor;
          }
        }
        setEast(temp);
      }else{
        if (tf.equals(lengthField)) {
          s = lengthField.getText();
          if (s.equals("")){
            temp = 0.0;
            lengthField.setText("0");
          }else{
            try {
              temp = nf.parse(s).doubleValue();
            } catch (ParseException pe) {
              temp = getLength();
            }
          }
          setLength(temp);
        }else{
          if (tf.equals(angleField)) {
            s = angleField.getText();
            if (s.equals("")) {
              temp = 0.0;
              angleField.setText("0");
            }else{
              try {
                temp = nf.parse(s).doubleValue();
              } catch (ParseException pe) {
                temp = getAngle();
              }
            }
            setAngle(temp);
          }else{
            // If focus lost event was generated by popping up an error
            // message from the setScale method, we do not want to process
            // this event. If we do, this will generate a second error
            // message, confusing the user.
            if (!changingScale && tf.equals(scaleField)) {
              s = scaleField.getText();
              if (s.equals("")) {
                temp = 0.0;
                scaleField.setText("0");
              }else{
                try {
                  temp = nf.parse(s).doubleValue();
                } catch (ParseException pe) {
                  temp = scale;
                }
              }
              setScale(temp);
            }
          }
        }
      }
    }
  }

  /**
   * Formats a number for display.
   * @param     num     The number to format.
   * @return    The formatted number. Effort is taken that, unlike
   *            <code>NumberFormat.format()</code>, "-0" is not returned for
   *            negative numbers smaller than the current precision.
   */
  private String myFormat(double num)
  {
    //
    // For negative numbers whose magnitude is less than the displayed
    // precision, simply return "0", to avoid getting a "-0", which formatting
    // would return. Do this for 0, as well, to cover the case of IEEE
    // negative 0.
    if ((num <= 0.0) && (num > inverseNegativePrecision)) {
      return "0";
    }else{
      return nf.format(num);
    }
  }

  /**
   * Calculates the limit above which <code>myFormat</code> will simply return
   * "0", instead of formating its argument.
   */
  private void calculateInverseNegativePrecision()
  {
    long n = 1L;
    for (int i=0; i<precision; i++) {
      n *= 10L;
    }
    inverseNegativePrecision = -1.0 / (double)n;
  }

  /**
   * Free resources. After invoking this method, the component is unusable.
   */
  public void dispose()
  {
    if (handle != null) {
      handle.dispose();
      handle = null;
    }
    northLabel = null;
    eastLabel = null;
    lengthLabel = null;
    angleLabel = null;
    scaleLabel = null;
    if (editable) {
      northField.removeFocusListener(this);
      eastField.removeFocusListener(this);
      lengthField.removeFocusListener(this);
      angleField.removeFocusListener(this);
      scaleField.removeFocusListener(this);
    }
    northField = null;
    eastField = null;
    lengthField = null;
    angleField = null;
    scaleField = null;
    vPanel = null;
    vect = null;
    horizontalName = null;
    verticalName = null;
    angleName = null;
    lengthName = null;
    resources = null;
    nf = null;
    numCol = null;
    contents = null;
    xPlug = null;
    yPlug = null;
    lPlug = null;
    aPlug = null;

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

  /**
   * Sets the maximum allowed length of the vector.
   * @param     maxLength       The maximum allowed length of the vector.
   *                            Specify 0 or a negative number to remove any
   *                            restriction on the maximum allowed length of
   *                            the vector.
   */
  public void setMaxLength(double maxLength)
  {
    if (maxLength > 0.0) {
      this.maxLength = maxLength;
      // Enforce the new restriction.
      setNE(hor, ver);
    }else{
      this.maxLength = 0.0;
    }
  }

  /**
   * Returns the maximum allowed length of the vector.
   * @return    The maximum allowed length of the vector. If no restriction
   *            has been placed on the maximum allowed the vector, 0 is
   *            returned.
   */
  public double getMaxLength()
  {
    return maxLength;
  }
   

  /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm)
  {
    if (perfListener == null) {
      perfListener = new PerformanceAdapter() {
        public void performanceManagerStateChanged(PropertyChangeEvent e)
        {
          boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
          // When the Performance Manager is enabled, try to attach the
          // timers.
          if (enabled) {
            attachTimers();
          }
        }
      };
      pm.addPerformanceListener(perfListener);
    }
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  private void attachTimers()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    boolean pmEnabled = pm.isEnabled();

    // If the performance manager is disabled, install a listener which will
    // re-invoke this method when the performance manager is enabled.
    if (!pmEnabled && (perfListener == null)) {
      createPerformanceManagerListener(pm);
    }

    // Do nothing if the PerformanceManager is disabled.
    if (!pmEnabled) {
      return;
    }

    boolean timersCreated = (loadTimer != null);
    // If the component's timers have not been constructed yet, then
    // construct them. During construction, the timers are also attached.
    if (!timersCreated) {
      // Get the performance timer group for this component.
      PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
      // Construct and attach the component's timers.
      constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, resources.getString("InitESlateAspectTimer"), true
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.CONSTRUCTOR, constructorTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.LOAD_STATE, loadTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.SAVE_STATE, saveTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
      );
    }
  }

  /* This class implements the panel with the vector's graphical
   * representation.
   *
   * @author    Kriton Kyrimis
   * @version   1.5.17, 4-Aug-1999
   */
  class VectorPanel extends JPanel
                    implements MouseMotionListener, MouseListener
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;

    private double hor;
    private double ver;
    private double scale;
    private Polygon p;

    /**
     * Create a VectorPanel.
     */
    public VectorPanel()
    {
      super(true);
      this.setOpaque(false);
      //setBorder(BorderFactory.createLoweredBevelBorder());
      this.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
      // Intialize p to a polygon with 3 points.
      // Since we are going to draw the polygon (the vector's arrowhead)
      // multiple times, it is better if we only allocate it once,
      // then manipulate the points directly.
      p = new Polygon();
      p.addPoint(0, 0);
      p.addPoint(0, 0);
      p.addPoint(0, 0);
    }

    /**
     * Paint the VectorPanel.
     */
    public void paint(Graphics g)
    {
      int w = this.getSize().width;
      int h = this.getSize().height;
      int w2 = w / 2;
      int h2 = h / 2;
      double x, y;

      x = hor / scale;
      y = ver / scale;

      // Preserve sign of x, y coordinates.
      int xSign, ySign;
      if (x < 0) {
        x = -x;
        xSign = -1;
      }else{
        xSign = 1;
      }
      if (y < 0) {
        y = -y;
        ySign = -1;
      }else{
        ySign = 1;
      }

      // Clip vector to visible area, as Java 2 seems to get stuck when
      // drawing very long lines.
      double x0 = x;
      double y0 = y;
      if (x0 > w2) {
        x0 = (double)w2;
        y0 = ((double)w2 * y) / x;
      }
      if (y0 > h2) {
        x0 = ((double)h2 * x0) / y0;
        y0 = (double)h2;
      }

      // Restore sign of x, y coordinates.
      x *= xSign;
      x0 *= xSign;
      y *= ySign;
      y0 *= ySign;

      g.setColor(Color.blue);
      g.drawLine(w2, 0, w2, h-1);
      g.drawLine(0, h2, w-1, h2);

      g.setColor(Color.black);

      g.drawLine(w2, h2, w2+(int)x0, h2-(int)y0);
      g.drawLine(w2-1, h2, w2+(int)x0-1, h2-(int)y0);
      g.drawLine(w2+1, h2, w2+(int)x0+1, h2-(int)y0);
      g.drawLine(w2, h2-1, w2+(int)x0, h2-(int)y0-1);
      g.drawLine(w2, h2+1, w2+(int)x0, h2-(int)y0+1);

      // Draw the arrowhead (code provided by G. Vasiliou)
      double length = Math.sqrt(x*x+y*y);
      double arrowLength;
      if (length > (1072d / 100d)) {
        arrowLength = 1072d / 100d;
      }else{
        arrowLength = 2 * length / 3;
      }
      double f1 = Math.atan2(y, x) - (4336d / 10000d);  //0.4336=25deg in pi
      double f2 = Math.PI - Math.atan2(y, x) - (4336d / 10000d);
      double consta = arrowLength * 11033 / 10000;
        // 1.1033 == Math.tan(25deg) / Math.sin(25deg);
      int lx, ly, rx, ry, ix, iy;
      ix = (int)x;
      iy = (int)y;
      p.xpoints[0] = w2+ix;
      p.ypoints[0] = h2-iy;
      //p.addPoint(w2+ix, h2-iy);
      lx = ix - (int)Math.floor((Math.cos(f1) * consta));
      ly = iy - (int)Math.floor((Math.sin(f1) * consta));
      rx = ix + (int)Math.ceil((Math.cos(f2) * consta));
      ry = iy - (int)Math.floor((Math.sin(f2) * consta));
      p.xpoints[1] = w2+lx;
      p.ypoints[1] = h2-ly;
      //p.addPoint(w2+lx, h2-ly);
      p.xpoints[2] = w2+rx;
      p.ypoints[2] = h2-ry;
      //p.addPoint(w2+rx, h2-ry);
      g.fillPolygon(p);

      // Draw the vector's components
      g.drawLine(w2, h2, w2+(int)x0, h2);
      g.drawLine(w2, h2, w2, h2-(int)y0);

      // Draw the arrowhead for the horizontal component
      length = Math.abs(x);
      if (length > (1072d / 100d)) {
        arrowLength = 1072d / 100d;
      }else{
        arrowLength = 2 * length / 3;
      }
      f1 = Math.atan2(0, x) - (4336d / 10000d); //0.4336=25deg in pi
      f2 = Math.PI - Math.atan2(0, x) - (4336d / 10000d);
      consta = arrowLength * 11033 / 10000;
        // 1.1033 == Math.tan(25deg) / Math.sin(25deg);
      ix = (int)x;
      iy = 0;
      lx = ix - (int)Math.floor((Math.cos(f1)*consta));
      ly = iy - (int)Math.floor((Math.sin(f1)*consta));
      rx = ix + (int)Math.ceil((Math.cos(f2)*consta));
      ry = iy - (int)Math.floor((Math.sin(f2)*consta));
      g.drawLine(w2+ix, h2-iy, w2+lx, h2-ly);
      g.drawLine(w2+ix, h2-iy, w2+rx, h2-ry);

      // Draw the arrowhead for the vertical component
      length = Math.abs(y);
      if (length > (1072d / 100d)) {
        arrowLength = 1072d / 100d;
      }else{
        arrowLength = 2 * length / 3;
      }
      f1 = Math.atan2(y, 0) - (4336d / 10000d); //0.4336=25deg in pi
      f2 = Math.PI - Math.atan2(y, 0) - (4336d / 10000d);
      consta = arrowLength * 11033 / 10000;
        // 1.1033 == Math.tan(25deg) / Math.sin(25deg);
      ix = 0;
      iy = (int)y;
      lx = ix - (int)Math.floor((Math.cos(f1)*consta));
      ly = iy - (int)Math.floor((Math.sin(f1)*consta));
      rx = ix + (int)Math.ceil((Math.cos(f2)*consta));
      ry = iy - (int)Math.floor((Math.sin(f2)*consta));
      g.drawLine(w2+ix, h2-iy, w2+lx, h2-ly);
      g.drawLine(w2+ix, h2-iy, w2+rx, h2-ry);

      this.paintBorder(g);
    }

    /**
     * Set the value of the copy of the vector's parameters
     * maintained by the VectorPanel.
     * @param   x       Horizontal component.
     * @param   y       Vertical component.
     * @param   sc      Scale.
     */
    public void setValue(double x, double y, double sc)
    {
      hor = x;
      ver = y;
      scale = sc;
    }

    /**
     * Called by the various mouse event handlers to process the events.
     * @param   e       The event to handle.
     */
    private void mouseUpdate(MouseEvent e)
    {
      int w = vPanel.getSize().width;
      int h = vPanel.getSize().height;
      int w2 = w / 2;
      int h2 = h / 2;
      Insets ins = this.getBorder().getBorderInsets(this);
      int top = ins.top;
      int left = ins.left;
      int bottom = ins.bottom;
      int right = ins.right;
      int xClick = e.getX();
      int yClick = e.getY();
      if (xClick <= left) {
        xClick = left;
      }
      if (xClick > w-right) {
        xClick = w - right - 1;
      }
      if (yClick <= top) {
        yClick = top;
      }
      if (yClick > h-bottom) {
        yClick = h - bottom - 1;
      }
      setNE(scale*(double)(xClick-w2), scale*(double)(h-1-yClick-h2));
    }

    /**
     * Handle "mouse moved" events.
     * @param   e       The event to handle.
     */
    public void mouseMoved(MouseEvent e)
    {
    }

    /**
     * Used to prevent updating the vector when the plug menu is shown.
     */
    private boolean doDrag = false;

    /**
     * Handle "mouse dragged" events.
     * @param   e       The event to handle.
     */
    public void mouseDragged(MouseEvent e)
    {
      if (!(e.isPopupTrigger()) && doDrag) {
        mouseUpdate(e);
      }
    }

    /**
     * Handle "mouse pressed" events.
     * @param   e       The event to handle.
     */
    public void mousePressed(MouseEvent e)
    {
      if (e.isPopupTrigger()) {
        doDrag = false; // Don't update vector when plug menu is shown
      }else{
        doDrag = true;
        mouseUpdate(e);
      }
    }

    /**
     * Handle "mouse released" events.
     * @param   e       The event to handle.
     */
    public void mouseReleased(MouseEvent e)
    {
      if (e.isPopupTrigger()) {
//      }else{
//      mouseUpdate(e);
      }
    }

    /**
     * Handle "mouse clicked" events.
     * @param   e       The event to handle.
     */
    public void mouseClicked(MouseEvent e)
    {
    }

    /**
     * Handle "mouse entered" events.
     * @param   e       The event to handle.
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Handle "mouse exited" events.
     * @param   e       The event to handle.
     */
    public void mouseExited(MouseEvent e)
    {
    }

//    /**
//     * VectorPanel's maximum size.
//     * @return        The maximum size.
//     */
//    public Dimension getMaximumSize()
//    {
//      return VectorComponent.vecSize;
//    }

//    /**
//     * VectorPanel's preferred size.
//     * @return        The preferred size.
//     */
//    public Dimension getPreferredSize()
//    {
//      return VectorComponent.vecSize;
//    }
  }

  /**
   * Focus traversal policy for the vector component.
   */
  private class MyFocusTraversalPolicy extends FocusTraversalPolicy
  {
    @Override
    public Component getComponentAfter
      (Container focusCycleRoot, Component aComponent)
    {
      if (aComponent.equals(eastField)) {
        return northField;
      }
      if (aComponent.equals(northField)) {
        return lengthField;
      }
      if (aComponent.equals(lengthField)) {
        return angleField;
      }
      if (aComponent.equals(angleField)) {
        return scaleField;
      }
      if (aComponent.equals(scaleField)) {
        return eastField;
      }
      return eastField;
    }

    @Override
    public Component getComponentBefore
      (Container focusCycleRoot, Component aComponent)
    {
      if (aComponent.equals(eastField)) {
        return scaleField;
      }
      if (aComponent.equals(scaleField)) {
        return angleField;
      }
      if (aComponent.equals(angleField)) {
        return lengthField;
      }
      if (aComponent.equals(lengthField)) {
        return northField;
      }
      if (aComponent.equals(northField)) {
        return eastField;
      }
      return eastField;
    }

    @Override
    public Component getDefaultComponent(Container focusCycleRoot)
    {
      return eastField;
    }

    @Override
    public Component getLastComponent(Container focusCycleRoot)
    {
      return scaleField;
    }

    @Override
    public Component getFirstComponent(Container focusCycleRoot)
    {
      return eastField;
    }
  }
}
