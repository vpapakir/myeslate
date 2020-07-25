package gr.cti.eslate.vector;

import java.text.*;
import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class is a JApplet wrapper for the master clock component.
 *
 * The minimum and maximum values of the time scale slider can be specified
 * via the "minimum" and "maximum" parameters in the applet tag; the default
 * is 1 and 100, respectively.
 * The initial value of the time scale can be specified via the "initial"
 * parameter in the applet tag; the default is 1.
 * <P>
 * The vector's initial size can be specified via the "horizontal" and
 * "vertical" parameters in the applet tag; default is (0,0).
 * The names of the vector's horizontal and vertical components can be
 * specified via the "horizontal_name" and "vertical_name" parameters in the
 * applet tag; default names are "X" and "Y".
 * The vector can be either editable (the user can modify its value via the
 * GUI) or non-editable. This can be specified via the "editable" parameter in
 * the applet tag; the default is "true".
 * The number of digits displayed after the decimal point can be specified via
 * the "precision" parameter in the applet tag; default is 2.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 * @see         gr.cti.eslate.vector.VectorComponent
 */
public class VectorApplet extends JApplet
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private VectorComponent vector = null;

  /**
   * Applet parameter information.
   * @return    Array of parameter information strings.
   */
  public String[][] getParameterInfo()
  {
    String[][] info =
    {
      // Parameter Name   Kind of Value  Description
      {"menubar",  "boolean",  "Specify if the menu bar will be displayed"},
      {"debug",    "boolean",  "Specify if debugging messages will be printed"},
      {"horizontal",      "double",   "Horizontal component of vector"},
      {"vertical",        "double",   "Vertical component of vector"},
      {"horizontal_name", "string",   "Name of horizontal component of vector"},
      {"vertical_name",   "string",   "Name of vertical component of vector"},
      {"editable",        "boolean",  "Specifies whether the vector is editable by the user"},
      {"precision",       "integer",  "Specifies the maximum number of digits after the decimal point"}
    };
    return info;
  }

  /**
   * Initialize the applet.
   */
  public void init()
  {
    super.init();

    // Parse applet parameters.
    String str;
    Double horizontal = null, vertical = null;
    Integer precision = null;
    String horizontalName = null, verticalName = null;
    Boolean editable = null;
    NumberFormat nf =
      NumberFormat.getInstance(ESlateMicroworld.getCurrentLocale());

    // Horizontal component.
    str = getParameter("horizontal");
    if (str != null) {
      try {
        horizontal = new Double(nf.parse(str).doubleValue());
      } catch (ParseException e) {
      }
    }

    // Vertical component.
    str = getParameter("vertical");
    if (str != null) {
      try {
        vertical = new Double(nf.parse(str).doubleValue());
      } catch (ParseException e) {
      }
    }

    // Numeric precision
    str = getParameter("precision");
    if (str != null) {
      try {
        precision = new Integer(nf.parse(str).intValue());
      } catch (ParseException e) {
      }
    }

    // Editable or not?
    str = getParameter("editable");
    if (str != null) {
      editable = new Boolean(str);
    }

    // Name of horizontal component.
    horizontalName = getParameter("horizontal_name");

    // Name of vertical component.
    verticalName = getParameter("vertical_name");

    // Check if menu bar is wanted
    boolean needMenuPanel = true;
    str = getParameter("menubar");
    if (str != null) {
      needMenuPanel = Boolean.valueOf(str).booleanValue();
    }

    // Check whether debugging should be enabled
    boolean debug = false;
    str = getParameter("debug");
    if (str != null) {
       debug = Boolean.valueOf(str).booleanValue();
    }

    // Create and add the vector panel.
    vector = new VectorComponent(needMenuPanel);

    vector.getESlateHandle().setDebugStatus(debug);
    if (horizontal != null) {
      vector.setEast(horizontal.doubleValue());
    }
    if (vertical != null) {
      vector.setNorth(vertical.doubleValue());
    }
    if (editable != null) {
      vector.setEditable(editable.booleanValue());
    }
    if (horizontalName != null) {
      try {
        vector.setEastName(horizontalName);
      } catch (NameUsedException pee) {
        System.out.println(pee.getMessage());
      }
    }
    if (verticalName != null) {
      try {
        vector.setNorthName(verticalName);
      } catch (NameUsedException pee) {
        System.out.println(pee.getMessage());
      }
    }
    if (precision != null) {
      vector.setPrecision(precision.intValue());
    }
    getContentPane().add(vector);

    // Notify state changed listeners
    vector.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      vector, ESlateMicroworld.COMPONENT_INITIALIZED
    );
  }

  /**
   * Overrides JApplet.start(), to invoke any component state changed
   * listeners.
   */
  public void start()
  {
    super.start();
    // Notify state changed listeners
    vector.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      vector, ESlateMicroworld.COMPONENT_STARTED
    );
  }

  /**
   * Overrides JApplet.stop(), to invoke any component state changed
   * listeners.
   */
  public void stop()
  {
    super.stop();
    // Notify state changed listeners
    vector.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      vector, ESlateMicroworld.COMPONENT_STOPPED
    );
  }

  /**
   * Cleans up when the component is destroyed.
   */
  public void destroy()
  {
    // Notify state changed listeners
    vector.getESlateHandle().getESlateMicroworld().fireComponentStateChangedListeners(
      vector, ESlateMicroworld.COMPONENT_DESTROYED
    );

    vector.dispose();
    vector = null;
    ESlateHandle.removeAllRecursively(this);
    super.destroy();
  }

}
