package gr.cti.eslate.utils.help;

import java.io.*;

import gr.cti.eslate.utils.*;

/**
 * This class contains information on the ordering of help files.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class OrderHint implements Externalizable
{
  /**
   * The help file corresponding to this instance.
   */
  String fileName;
  /**
   * The order in which the help file should appear in the help.
   */
  int order;
  /**
   * If the help file is a directory, this contains a list of the OrderHints
   * corresponding to the contents of that directory.
   */
  OrderHint[] children;

  private final static String FILENAME = "fileName";
  private final static String ORDER = "order";
  private final static String CHILDREN = "children";

  static final long serialVersionUID = 1L;
  static final int storageVersion = 1;

  /**
   * Construct an OrderHint instance.
   * @param     ft      The FileTree instance corresponding to a help file.
   * @param     order   The order in which the help file should appear in the
   *                    help.
   */
  OrderHint(FileTree ft, int order)
  {
    fileName = ft.fileName;
    this.order = order;
    int n = ft.getChildCount();
    children = new OrderHint[n];
    for (int i=0; i<n; i++) {
      FileTree ft2 = (FileTree)(ft.getChildAt(i));
      children[i] = new OrderHint(ft2, i);
    }
  }

  /**
   * No-argument constructor required for externalization.
   */
  public OrderHint()
  {
  }

  /**
   * Save the instance.
   * @param     oo      The stream where the instance should be saved.
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion, 3);

    map.put(FILENAME, fileName);
    map.put(ORDER, order);
    map.put(CHILDREN, children);

    oo.writeObject(map);
  }

  /**
   * Load the instance.
   * @param     oi      The stream from where the instance should be loaded.
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    fileName = map.get(FILENAME, "");
    order = map.get(ORDER, 0);
    children = (OrderHint[])(map.get(CHILDREN));
  }
}
