package gr.cti.eslate.utils;

import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;

/**
 * This class implements a simple copy/paste mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class ESlateClipboard
{
  /**
   * The clipboard where clips are placed.
   */
  private final static Clipboard clip = new Clipboard("ESlateClipboard");
  private static ArrayList<ESlateClipboardChangedListener> listeners =
    new ArrayList<ESlateClipboardChangedListener>();

  /**
   * This method implements the "copy" part of the copy/paste mechanism.
   * The supplied argument is serialized and copied to the clipboard.
   * @param     obj     The object to be copied. Specifying a null will free
   *                    any memory used by previous copies.
   * @exception NotSerializableException        This exception is thrown if
   *                    the above object does not implement the Serializable
   *                    interface.
   */
  public static void setContents(Object obj) throws NotSerializableException
  {
    ESlateClipboardWrapper aw = new ESlateClipboardWrapper(obj);
    clip.setContents(aw, aw);
    fireESlateClipboardChangedListeners();
  }

  /**
   * This method implements the "paste" part of the copy/paste mechanism.
   * It returns a copy of the object copied during the "copy" operation.
   * The object returned is a copy of the original copied object; any changes
   * made to that object, after the copy operation, are not reflected in the
   * returned object.
   * @return    A copy of the copied object. If there is no copied object,
   *            this method returns null.
   */
  public static Object getContents()
  {
    Object result = null;
    try {
      Transferable contents = clip.getContents(clip);
      if (contents != null) {
        result = contents.getTransferData(
          ESlateClipboardWrapper.getTransferDataFlavor()
        );
      }
    } catch (Exception e) {
    }
    return result;
  }

  /**
   * Frees any memory used by previous copies.
   */
  public static void clearContents()
  {
    try {
      ESlateClipboardWrapper aw = new ESlateClipboardWrapper(null);
      clip.setContents(aw, aw);
    } catch (NotSerializableException e) {
    }
  }

  /**
   * Add an E-Slate clipboard changed event listener. This listener's
   * handleESlateClipboardChangedEvent method will be
   * invoked whenever the E-Slate clipboard is modified.
   * @param     listener        The listener to add.
   */
  public static void addESlateClipboardChangedListener
    (ESlateClipboardChangedListener listener)
  {
    synchronized(listeners) {
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Remove an E-Slate clipboard changed event listener.
   * @param     listener        The listener to remove.
   */
  public static void removeESlateClipboardChangedListener
    (ESlateClipboardChangedListener listener)
  {
    synchronized(listeners) {
      int i = listeners.indexOf(listener);
      if (i >= 0) {
        listeners.remove(i);
      }
    }
  }

  /**
   * Fire all E-Slate clipboard changed event listeners.
   */
  @SuppressWarnings(value={"unchecked"})
  private static void fireESlateClipboardChangedListeners()
  {
    ESlateClipboardChangedEvent e = new ESlateClipboardChangedEvent(clip);
    ArrayList<ESlateClipboardChangedListener> l;
    synchronized (listeners) {
      l = (ArrayList<ESlateClipboardChangedListener>)(listeners.clone());
    }
    int nListeners = l.size();
    for (int i=0; i<nListeners; i++) {
      l.get(i).handleESlateClipboardChangedEvent(e);
    }
  }
}
