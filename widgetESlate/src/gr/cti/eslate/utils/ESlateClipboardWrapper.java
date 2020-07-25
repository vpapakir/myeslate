package gr.cti.eslate.utils;

import java.io.*;
import java.awt.datatransfer.*;

/**
 * This class implements a wrapper for arbitrary objects, that implements
 * the Transferable interface, so that they can be copied and pasted to a
 * clipboard. The only restriction placed on the objects is that they must
 * implement the Serializable interface. The wrapped objects are serialized
 * upon creation of a wrapper object, and deserialized upon retrieval.
 * Thus, any changes made to the original object between the copy and the
 * paste operation do not affect the copied object. In addition, any changes
 * to the pasted object will not affect other pasted objects.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class ESlateClipboardWrapper implements Transferable, ClipboardOwner
{
  /**
   * A list of supported data flavors.
   */
  private final static DataFlavor df[] = {
    new DataFlavor(ESlateClipboardWrapper.class, "ESlateClipboardWrapper")
  };

  /**
   * The output stream where objects are serialized.
   */
  private ByteArrayOutputStream bos;

  /**
   * Create a wrapper for a given object.
   * @param     obj     The object to wrap.
   * @exception NotSerializableException        This exception is thrown if
   *                    the above object does not implement the Serializable
   *                    interface.
   */
  public ESlateClipboardWrapper(Object obj) throws NotSerializableException
  {
    super();
    bos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();
    } catch (IOException e) {
    }
  }

  /**
   * Returns the one and only data flavor supported by this Tranferable
   * object.
   * @return    The supported data flavor.
   */
  public static DataFlavor getTransferDataFlavor()
  {
    return df[0];
  }

  /**
   * Returns a list of the data flavors supported by this Transferable object.
   * @return    The list of supported data flavors.
   */
  public DataFlavor[] getTransferDataFlavors()
  {
    return df;
  }

  /**
   * Checks whether a given data flavor is supported by this Transferable
   * object.
   * @param     flavor  The data flavor to be checked.
   * @return    True/false.
   */
  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    if (flavor != null && flavor.equals(df[0])) {
      return true;
    }else{
      return false;
    }
  }

  /**
   * Returns a copy of the wrapped object.
   * @param     flavor  The data flavor of the object. This should be equal to
   *                    getTransferDataFlavor().
   * @exception UnsupportedFlavorException      This exception is thrown
   *                    if any other data flavor is specified above.
   * @return    A copy of the wrapped object.
   */
  public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException
  {
    Object obj = null;
    if (isDataFlavorSupported(flavor)) {
      if (bos.size() > 0) {
        try {
          ByteArrayInputStream bis =
            new ByteArrayInputStream(bos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bis);
          obj = ois.readObject();
        } catch (Throwable e) {
          e.printStackTrace();
        }
      } else {
        obj = null;
      }
    } else {
      throw new UnsupportedFlavorException(flavor);
    }
    return obj;
  }

  /**
   * Required by the Clipboardowner interface.
   */
  public void lostOwnership(Clipboard clipboard, Transferable contents)
  {
  }
}
