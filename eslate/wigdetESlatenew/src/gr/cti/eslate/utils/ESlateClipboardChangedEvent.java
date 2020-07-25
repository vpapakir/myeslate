package gr.cti.eslate.utils;

/**
 * Event triggered when a plug is connected to another plug.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateClipboardChangedEvent extends java.util.EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Constructs an E-Slate clipboard changed event.
   * @param     source  The object that generated the event.
   */
  public ESlateClipboardChangedEvent(Object source)
  {
    super(source);
  }
}
