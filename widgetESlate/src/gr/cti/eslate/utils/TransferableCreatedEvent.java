package gr.cti.eslate.utils;

import javax.swing.*;
import java.util.EventObject;


/**
 * TransferableCreatedEvent
 *
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 */
public class TransferableCreatedEvent extends EventObject
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    private JComponent dragSource = null;

    public TransferableCreatedEvent(Object source, JComponent dragSource) {
        super(source);
        this.dragSource = dragSource;
    }

    public JComponent getDragSource() {
        return dragSource;
    }
}
