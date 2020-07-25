package gr.cti.structfile;

import javax.swing.tree.*;

/**
 * This class implements nodes corresponding to structured file entries.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
class EntryNode extends DefaultMutableTreeNode
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The associated structured file Entry.
   */
  private Entry entry;
  /**
   * The name of the node.
   */
  private String name;

  /**
   * Constructs an EntryNode.
   * @param     obj     The associated structured file entry.
   */
  EntryNode(Object obj)
  {
    super(((Entry)(obj)).getName());
    entry = (Entry)obj;
    name = entry.getName();
  }

  /**
   * Returns the structured file entry associated with this node.
   */
  Entry getEntry()
  {
    return entry;
  }

  /**
   * Sets the name of the node.
   * @param     name    The name of the node.
   */
  void setName(String name)
  {
    this.name = name;
  }

  /**
   * Returns the name of the node.
   * @return    The requested name.
   */
  String getName()
  {
    return name;
  }

  /**
   * Compares the node to another node.
   * @param     node    The node to which to compare this node.
   * @return    If this node corresponds to a file entry and the argument
   *            corresponds to a directory entry, this method returns a
   *            negative number. If this node corresponds to a directory entry
   *            and the argument corresponds to a file entry, this method
   *            returns a positive number. Otherwise, it returns the result of
   *            lexicographically comparing the names associated with the two
   *            entries.
   */
  int compareTo(EntryNode node)
  {
    int type1 = getEntry().getType();
    int type2 = node.getEntry().getType();
    // We want to group directories before files, so consider directories to
    // be always lexicographically smaller than files.
    if (type1 == Entry.DIRECTORY && type2 == Entry.FILE) {
      return -1;
    }
    if (type1 == Entry.FILE && type2 == Entry.DIRECTORY) {
      return 1;
    }
    String s1 = (String)getUserObject();
    String s2 = (String)(node.getUserObject());
    return s1.compareTo(s2);
  }

  /**
   * Returns the child node of the node, that has a given name.
   * @param     name    The name of the child node.
   * @return    The requested child node, or null if no such child node
   *            exists.
   */
  EntryNode getChild(String name)
  {
    int nChildren = getChildCount();
    for (int i=0; i<nChildren; i++) {
      EntryNode en = (EntryNode)getChildAt(i);
      if (name.equals(en.getName())) {
        return en;
      }
    }
    return null;
  }

  /**
   * Removes a child node from the node, that has a given name.
   * @param     child   The name of the child node.
   */
  void remove(String name)
  {
    EntryNode child = getChild(name);
    if (child != null) {
      remove(child);
    }
  }
}
