package gr.cti.eslate.utils.help;

import java.util.*;
import java.io.*;
import javax.swing.tree.*;

/**
 * This class implements a node in the structure of the help files.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
@SuppressWarnings("unchecked")
class FileTree implements Comparable, MutableTreeNode
{
  /**
   * Indicates that this node represents a directory.
   */
  final static int DIRECTORY = 0;
  /**
   * Indicates that this node represents a plain file.
   */
  final static int FILE = 1;

  /**
   * The child nodes of this node.
   */
  ArrayList<FileTree> contents;
  /**
   * The parent node of this node.
   */
  private FileTree parent = null;
  /**
   * The type of the node. One of DIRECTORY, FILE.
   */
  int type;
  /**
   * The name of the file represented by this node, with "."s replaced
   * by "_"s.
   */
  private String name;
  /**
   * The name of the file represented by this node.
   */
  private String fName;
  /**
   * The full path name of the file represented by this node, relative to the
   * top-level help file directory.
   */
  String fileName;
  /**
   * A target name to be used in the map file for the file represented by this
   * node, created by replacing with "_" all "."s and file separators in the
   * file's path name relative to the top-level help file directory.
   */
  String mapName;
  /**
   * The description of the contents of the file represented by this node.
   */
  String description;

  /**
   * Construct the top-level node, recursively adding its children.
   * @param     file    The top-level help file directory.
   */
  FileTree(File file)
  {
    super();
    parent = null;
    buildTree(file);
  }

  /**
   * Construct a node, recursively adding its children.
   * @param     file    The file or folder corresponding to the node.
   * @param     parent  The parent node.
   */
  private FileTree(File file, FileTree parent)
  {
    super();
    this.parent = parent;
    buildTree(file);
  }

  /**
   * Fill in the information associated with the node and add the node's
   * children.
   */
  private void buildTree(File file)
  {
    mapName = getMapName();
    fName = file.getName();
    name = file.getName().replace('.', '_');
    fileName = getFileName();
    if (file.isFile()) {
      type = FILE;
      contents = null;
      description = getDescription(file);
      mapName = getMapName();
    }else{
      description = name;
      String[] list = file.list();
      int n = list.length;
      contents = new ArrayList<FileTree>();
      for (int i=0; i<n; i++) {
        String s = list[i].toLowerCase();
        File f = new File(file, list[i]);
        if (s.endsWith(".htm") || s.endsWith(".html") || f.isDirectory()) {
          contents.add(new FileTree(f, this));
        }
      }
    }
  }

  /**
   * Constructs the target name to be used in the map file for the file
   * represented by this node, created by replacing with "_" all "."s and
   * file separators in the file's path name relative to the top-level help
   * file directory.
   * @return    The requested name.
   */
  private String getMapName()
  {
    ArrayList<String> a = new ArrayList<String>();
    for (FileTree ft=this; ft!=null; ft=ft.parent) {
      a.add(ft.name);
    }
    int n = a.size() - 2;
    StringBuffer s = new StringBuffer();
    for (int i=n; i>=0; i--) {
      s.append(a.get(i));
      if (i > 0) {
        s.append('_');
      }
    }
    return s.toString();
  }

  /**
   * Constructs the full path name of the file represented by this node,
   * relative to the top-level help file directory.
   * @return    The requested name.
   */
  private String getFileName()
  {
    ArrayList<String> a = new ArrayList<String>();
    for (FileTree ft=this; ft!=null; ft=ft.parent) {
      a.add(ft.fName);
    }
    int n = a.size() - 2;
    StringBuffer s = new StringBuffer();
    for (int i=n; i>=0; i--) {
      s.append(a.get(i));
      if (i > 0) {
        s.append('/');
      }
    }
    return s.toString();
  }

  /**
   * Constructs the description of the contents of the file represented
   * by this node.
   * @return    The text between the file's &lt;TITLE&gt; and &lt;/TITLE&gt;
   *            tags. If the file does not contain such tags, the name of the
   *            file is returned, instead.
   */
  private String getDescription(File file)
  {
    DataInputStream in = null;
    try {
      FileInputStream fis = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(fis);
      in = new DataInputStream(bis);
      final int START = 0;
      final int BR = 1;
      final int BR_T = 2;
      final int BR_TI = 3;
      final int BR_TIT = 4;
      final int BR_TITL = 5;
      final int BR_TITLE = 6;
      final int BR_TITLE_BR = 7;
      ByteArrayOutputStream b = new ByteArrayOutputStream();
      byte ch;
      int state = START;
      boolean haveSpace = false;
      boolean start = true;
      do {
        try {
          ch = in.readByte();
        } catch (EOFException eof) {
          break;
        }
        char c = Character.toLowerCase((char)ch);
        switch (state) {
          case START:
            if (c == '<') {
              state = BR;
            }
            break;
          case BR:
            if (c == 't') {
              state = BR_T;
            }else{
              state = START;
            }
            break;
          case BR_T:
            if (c == 'i') {
              state = BR_TI;
            }else{
              state = START;
            }
            break;
          case BR_TI:
            if (c == 't') {
              state = BR_TIT;
            }else{
              state = START;
            }
            break;
          case BR_TIT:
            if (c == 'l') {
              state = BR_TITL;
            }else{
              state = START;
            }
            break;
          case BR_TITL:
            if (c == 'e') {
              state = BR_TITLE;
            }else{
              state = START;
            }
            break;
          case BR_TITLE:
            if (c == '>') {
              state = BR_TITLE_BR;
            }else{
              state = START;
            }
            break;
          case BR_TITLE_BR:
            if (c == '<') {
              in.close();
              return b.toString();
            }else{
              if ((ch==' ') || (ch=='\n') || (ch=='\r') || (ch == '\t')) {
                if (!start && !haveSpace) {
                  haveSpace = true;
                  b.write(' ');
                }
              }else{
                haveSpace = false;
                start = false;
                b.write(ch);
              }
            }
        }
      } while (true) ;
      in.close();
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
      try {
        in.close();
      } catch (Exception e) {
      }
    }
    return file.getName();
  }

  /**
   * Returns the node corresponding to the first help file in the hierarchy.
   * @return    The node corresponding to the first file in the top-level
   *            folder. If the top-level folder contains only folders,
   *            getFirst() is recursively invoked on all nodes, until a file
   *            is found.
   */
  FileTree getFirst()
  {
    if (type == FILE) {
      return this;
    }else{
      int n = contents.size();
      for (int i=0; i<n; i++) {
        FileTree ft = contents.get(i);
        if (ft.type == FILE) {
          return ft;
        }
      }
      for (int i=0; i<n; i++) {
        FileTree ft = contents.get(i);
        FileTree tmp = ft.getFirst();
        if (tmp != null) {
          return tmp;
        }
      }
      return null;
    }
  }

  /**
   * Returns a string representation of the node.
   * @return    The description of the contents of the file represented
   *            by this node.
   */
  public String toString()
  {
    return description;
  }

  /**
   * Compares two FileTrees, according to their descriptions.
   * @param     o       The FileTree to compare to this node.
   * @return    If the description of this node is lexicograpically before the
   *            descripttion of <code>o</code>, -1 is returned. If the two
   *            descriptions are the same, 0 is returned. Otherwise, 1 is
   *            returned.
   */
  public int compareTo(Object o)
  {
    FileTree ft2 = (FileTree)o;
    return description.compareTo(ft2.description);
  }

  /**
   * Returns the children of the reciever as an Enumeration.
   * @return    The requested enumeration.
   */
  public Enumeration children()
  {
    return new FileTreeEnumeration(contents);
  }

  /**
   * Returns true if the node allows children.
   * @return    Always returns true.
   */
  public boolean getAllowsChildren()
  {
    return true;
  }

  /**
   * Returns the child <code>TreeNode</code> at index <code>childIndex</code>.
   * @return    The requested node.
   */
  public TreeNode getChildAt(int childIndex)
  {
    return contents.get(childIndex);
  }

  /**
   * Returns the number of children <code>TreeNodes</code> the node contains.
   * @return    The requested number.
   */
  public int getChildCount()
  {
    if (contents != null) {
      return contents.size();
    }else{
      return 0;
    }
  }

  /**
   * Returns the index of <code>node</coe> in the node's children.
   * @return    The requested index. If the node does not contain
   *            <code>node</code>, -1 will be returned.
   */
  public int getIndex(TreeNode node)
  {
    return contents.indexOf(node);
  }

  /**
   * Returns the parent <code>TreeNode</code> of the node.
   * @return    The requested node.
   */
  public TreeNode getParent()
  {
    return parent;
  }

  /**
   * Returns true if the node is a leaf.
   * @return    True if the node is a leaf, false otherwise.
   */
  public boolean isLeaf()
  {
    return (contents == null) || (contents.size() == 0);
  }

  /**
   * Adds <code>child</code> to the node at <code>index</code>.
   * @param     child   The node to add.
   * @param     index   The position at which the node will be added.
   */
  public void insert(MutableTreeNode child, int index)
  {
    FileTree ft = (FileTree)child;
    FileTree oldParent = ft.parent;
    if (oldParent != null) {
      oldParent.remove(ft);
    }
    contents.add(index, ft);
    ft.parent = this;
  }

  /**
   * Removes the child at <code>index</code> from the node.
   * @param     index   The index of the child to remove.
   */
  public void remove(int index)
  {
    FileTree ft = contents.get(index);
    if (ft != null) {
      contents.remove(index);
      ft.parent = null;
    }
  }

  /**
   * Removes <code>node</code> from the node.
   * @param     node    The node to remove.
   */
  public void remove(MutableTreeNode node)
  {
    FileTree ft = (FileTree)node;
    contents.remove(node);
    ft.parent = null;
  }

  /**
   * Removes the node from its parent.
   */
  public void removeFromParent()
  {
    if (parent != null) {
      parent.remove(this);
    }
  }

  /**
   * Sets the parent of the node to <code>newParent</code>.
   * @param     newParent       The new parent of the node.
   */
  public void setParent(MutableTreeNode newParent)
  {
    if (parent != null) {
      parent.remove(this);
    }
    newParent.insert(this, newParent.getChildCount());
  }

  /**
   * Resets the user object of the receiver to <code>object</code>.
   * This method does not do anything.
   * @param     object  Ignored.
   */
  public void setUserObject(Object object)
  {
  }

  /**
   * Returns the path from the root, to get to this node. The last element in
   * the path is this node.
   * @return    An array of TreeNode objects giving the path, where the
   *            first element in the path is the root and the last element
   *            is this node.
   */
  public TreeNode[] getPath()
  {
    ArrayList<TreeNode> a = new ArrayList<TreeNode>();
    TreeNode node = this;
    do {
      a.add(node);
      node = node.getParent();
    } while (node != null);
    int n = a.size();
    TreeNode[] path = new TreeNode[n];
    for (int i=0; i<n; i++) {
      path[i] = a.get(n-i-1);
    }
    return path;
  }

  /**
   * Recursively removes empty directories from the FileTree structure.
   */
  void trimEmptyDirs()
  {
    if (!isLeaf()) {
      int n = contents.size();
      for (int i=n-1; i>=0; i--) {
        FileTree ft = contents.get(i);
        ft.trimEmptyDirs();
        if ((ft.type == DIRECTORY) && (ft.contents.size() == 0)) {
          remove(ft);
        }
      }
    }
  }

  /**
   * Order the nodes of the tree according to a set of hints.
   * @param     oh      The order hints.
   */
  void order(OrderHint oh)
  {
    OrderHint[] coh = oh.children;
    int n = coh.length;
    for (int i=0; i<n; i++) {
      OrderHint oh2 = coh[i];
      int nNodes = getChildCount();
      for (int j=0; j<nNodes; j++) {
        FileTree ft = contents.get(j);
        if (ft.fileName.equals(oh2.fileName)) {
          remove(j);
          insert(ft, oh2.order);
          if (ft.type == DIRECTORY) {
            ft.order(oh2);
          }
          break;
        }
      }
    }
  }

  /**
   * An implementation of <code>Enumeration</code> for the
   * <code>children()</code> method.
   */
  @SuppressWarnings("unchecked")
  private class FileTreeEnumeration implements Enumeration
  {
    /**
     * A copy of the node's children list.
     */
    ArrayList a;
    /**
     * The number of this node's children.
     */
    private int n;
    /**
     * The index of the next child to return.
     */
    private int current = 0;

    /**
     * Construct the enumeration.
     * @param   a       The node's children.
     */
    private FileTreeEnumeration(ArrayList a)
    {
      this.a = (ArrayList)(a.clone());
      n = this.a.size();
    }

    /**
     * Tests if this enumeration contains more elements.
     * @return  <code>true</code> if and only if this enumeration object
     *          contains at least one more element to provide;
     *          <code>false</code> otherwise.
     */
    public boolean hasMoreElements()
    {
      return (current < n);
    }

    /**
     * Returns the next element of this enumeration if this enumeration object
     * has at least one more element to provide.
     * @return  The next element of this enumeration.
     * @exception           NoSuchElementException      Thrown if no more
     *                                                  elements exist.
     */
    public Object nextElement() throws NoSuchElementException
    {
      return a.get(current++);
    }
  }

}
