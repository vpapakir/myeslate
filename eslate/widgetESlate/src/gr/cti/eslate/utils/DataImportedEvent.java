package gr.cti.eslate.utils;

import javax.swing.*;
import java.util.EventObject;
import java.awt.datatransfer.Transferable;
import java.awt.*;

/**
 * Data imported event.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class DataImportedEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JComponent destination = null;
  private Transferable transferable = null;
  Point dropLocation = null;

  public DataImportedEvent
    (Object source, JComponent destination, Transferable transferable,
     Point dropLocation)
  {
    super(source);
    this.destination = destination;
    this.transferable = transferable;
    this.dropLocation = dropLocation;
  }

  public JComponent getDestination()
  {
    return destination;
  }

  public Transferable getTransferable()
  {
    return transferable;
  }

  public Point getDropLocation()
  {
    return dropLocation;
  }
}
