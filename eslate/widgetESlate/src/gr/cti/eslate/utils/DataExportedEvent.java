package gr.cti.eslate.utils;

import javax.swing.*;
import java.util.EventObject;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;

/**
 * Data exported event.
 *
 * @version     2.0.0, 18-May-2006
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 */
public class DataExportedEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JComponent sourceComponent = null;
  private Transferable data = null;
  private int action = DnDConstants.ACTION_NONE;

  public DataExportedEvent
    (Object source, JComponent sourceComponent, Transferable data, int action)
  {
    super(source);
    this.sourceComponent = sourceComponent;
    this.data = data;
    this.action = action;
  }

  public JComponent getSourceComponent()
  {
    return sourceComponent;
  }

  public Transferable getData()
  {
    return data;
  }

  public int getAction()
  {
    return action;
  }
}
