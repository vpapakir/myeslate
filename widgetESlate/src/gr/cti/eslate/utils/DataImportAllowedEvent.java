package gr.cti.eslate.utils;

import javax.swing.*;
import java.util.EventObject;
import java.awt.datatransfer.DataFlavor;
import java.awt.*;

/**
 * Data import allowed event.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class DataImportAllowedEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JComponent destination = null;
  private DataFlavor[] flavors = null;
  Point dragLocation = null;

  public DataImportAllowedEvent
  (Object source, JComponent destination, DataFlavor[] flavors,
   Point dragLocation)
  {
    super(source);
    this.destination = destination;
    this.flavors = flavors;
    this.dragLocation = dragLocation;
  }

  public JComponent getDestination()
  {
    return destination;
  }

  public DataFlavor[] getFlavors()
  {
    return flavors;
  }

  public Point getDragLocation()
  {
    return dragLocation;
  }
}
