package gr.cti.structfile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * This class implements a panel that manipulates the contents of a structured
 * file.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 22-May-2006
 * @see         gr.cti.structfile.StructFile
 * @see         gr.cti.structfile.FileTool
 */
public class FileToolPanel extends JPanel implements ActionListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Class version.
   */
  private String version = "2.0.0";
  /**
   * Localized resources.
   */
  private ResourceBundle resources;
  /**
   * Preferred size;
   */
  private Dimension prefSize = new Dimension(640, 480);
  /**
   * File chooser.
   */
  private JFileChooser chooser = null;
  /**
   * Directory where the file chooser starts.
   */
  private String baseDir;
  /**
   * The structured file to manipulate.
   */
  private StructFile sf = null;
  /**
   * The scroll pane containing the view on the structured file.
   */
  private JScrollPane sp = null;
  /**
   * The tree with the view on the structured file.
   */
  private JTree tree = null;
  /**
   * The name of the structured file being edited.
   */
  private String structuredFileName = null;

  /**
   * Create a structured file maintenance panel.
   */
  public FileToolPanel()
  {
    super();

    resources = ResourceBundle.getBundle(
      "gr.cti.structfile.UtilResources", Locale.getDefault()
    );

    baseDir = System.getProperty("user.dir");

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }
/*
    // The default font, at least under Windows/Netscape does not show Greek
    // characters properly.  Substitute it with Helvetica, which does.
    Locale locale = Locale.getDefault();
    if (locale.getLanguage().equals("el") ||
        locale.getLanguage().equals("el_GR")) {
      Font plainFont = new Font("Helvetica", Font.PLAIN, 12);
      Font boldFont = new Font("Helvetica", Font.BOLD, 12);
      UIManager.put("Panel.font", plainFont);
      UIManager.put("Button.font", plainFont);
      UIManager.put("ToggleButton.font", plainFont);
      UIManager.put("RadioButton.font", plainFont);
      UIManager.put("MenuBar.font", plainFont);
      UIManager.put("Menu.font", plainFont);
      UIManager.put("MenuItem.font", plainFont);
      UIManager.put("PopupMenu.font", plainFont);
      UIManager.put("Label.font", plainFont);
      UIManager.put("List.font", plainFont);
      UIManager.put("ComboBox.font", plainFont);
      UIManager.put("TextField.font", plainFont);
      UIManager.put("PasswordField.font", plainFont);
      UIManager.put("TextArea.font", plainFont);
      UIManager.put("TextPane.font", plainFont);
      UIManager.put("EditorPane.font", plainFont);
      UIManager.put("ScrollPane.font", plainFont);
      UIManager.put("TabbedPane.font", plainFont);
      UIManager.put("Table.font", plainFont);
      UIManager.put("TableHeader.font", plainFont);
      UIManager.put("TitledBorder.font", plainFont);
      UIManager.put("ToolBar.font", plainFont);
      UIManager.put("ToolTip.font", plainFont);
      UIManager.put("ProgressBar.font", plainFont);
      UIManager.put("OptionPane.font", plainFont);
      UIManager.put("ColorChooser.font", plainFont);
      UIManager.put("Tree.font", plainFont);
      UIManager.put("CheckBox.font", plainFont);
      UIManager.put("CheckBoxMenuItem.font", plainFont);
      UIManager.put("RadioButtonMenuItem.font", plainFont);
      UIManager.put("Viewport.font", plainFont);

      UIManager.put("InternalFrame.titleFont", boldFont);
    }
*/

    setPreferredSize(prefSize);
    setLayout(new BorderLayout());

    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu(resources.getString("file"));
    JMenuItem mi;
    mi = new JMenuItem(resources.getString("new"));
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.ALT_MASK));
    mi.setActionCommand("new");
    mi.addActionListener(this);
    fileMenu.add(mi);
    mi = new JMenuItem(resources.getString("open"));
    mi.setActionCommand("open");
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.ALT_MASK));
    mi.addActionListener(this);
    fileMenu.add(mi);
    mi = new JMenuItem(resources.getString("close"));
    mi.setActionCommand("close");
    mi.addActionListener(this);
    fileMenu.add(mi);
    fileMenu.add(new JSeparator());
    mi = new JMenuItem(resources.getString("newFolder") + "...");
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.ALT_MASK));
    mi.setActionCommand("newFolder");
    mi.addActionListener(this);
    fileMenu.add(mi);
    mi = new JMenuItem(resources.getString("addFiles") + "...");
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.ALT_MASK));
    mi.setActionCommand("addFiles");
    mi.addActionListener(this);
    fileMenu.add(mi);
    mi = new JMenuItem(resources.getString("addFolder") + "...");
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.ALT_MASK));
    mi.setActionCommand("addFolder");
    mi.addActionListener(this);
    fileMenu.add(mi);
    mi = new JMenuItem(resources.getString("delete"));
    mi.setActionCommand("delete");
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    mi.addActionListener(this);
    fileMenu.add(mi);
    fileMenu.add(new JSeparator());
    mi = new JMenuItem(resources.getString("optimize"));
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.ALT_MASK));
    mi.setActionCommand("optimize");
    mi.addActionListener(this);
    fileMenu.add(mi);
    fileMenu.add(new JSeparator());
    mi = new JMenuItem(resources.getString("exit"));
    mi.setActionCommand("exit");
    mi.addActionListener(this);
    fileMenu.add(mi);

    JMenu helpMenu = new JMenu(resources.getString("help"));
    mi = new JMenuItem(resources.getString("about"));
    mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    mi.setActionCommand("about");
    mi.addActionListener(this);
    helpMenu.add(mi);

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);

    add(menuBar, BorderLayout.NORTH);
  }

  /**
   * Handle selections of menu items.
   * @param     e       Event received.
   */
  public void actionPerformed(ActionEvent e)
  {
    String cmd = e.getActionCommand();
    if (cmd.equals("new")) {
      newFile();
    }
    if (cmd.equals("open")) {
      openFile();
    }
    if (cmd.equals("close")) {
      closeFile();
    }
    if (cmd.equals("newFolder")) {
      newFolder();
    }
    if (cmd.equals("addFiles")) {
      addFiles();
    }
    if (cmd.equals("addFolder")) {
      addFolder();
    }
    if (cmd.equals("delete")) {
      deleteEntry();
    }
    if (cmd.equals("optimize")) {
      optimize();
    }
    if (cmd.equals("exit")) {
      exit();
    }
    if (cmd.equals("about")) {
      about();
    }
  }

  /**
   * Create a structured file.
   */
  private void newFile()
  {
    if (chooser == null) {
      chooser = new JFileChooser(baseDir);
    } else {
      chooser.setCurrentDirectory(new File(baseDir));
    }
    chooser.setDialogTitle(resources.getString("createFile"));
    chooser.setApproveButtonToolTipText(resources.getString("createFile"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setSelectedFile(null);
    chooser.rescanCurrentDirectory();
    String newFile = "";
    if (chooser.showDialog(this, resources.getString("create")) ==
          JFileChooser.APPROVE_OPTION &&
        chooser.getSelectedFile() != null) {
      try {
        newFile = chooser.getSelectedFile().getCanonicalPath();
        String newDir = chooser.getSelectedFile().getParent();
        if (newDir != null) {
          baseDir = newDir;
        }
      } catch (IOException ioe) {
      }
      if (sf != null) {
        try {
          sf.close();
          sf = null;
        } catch (IOException ioe) {
        }
      }
      try {
        sf = new StructFile(newFile, StructFile.NEW);
        displayFile(newFile);
      } catch (Exception e) {
        e.printStackTrace(System.out);
        error(e.getMessage());
      }
    }
  }

  /**
   * Open an existing structured file.
   */
  private void openFile()
  {
    if (chooser == null) {
      chooser = new JFileChooser(baseDir);
    } else {
      chooser.setCurrentDirectory(new File(baseDir));
    }
    chooser.setDialogTitle(resources.getString("openFile"));
    chooser.setApproveButtonToolTipText(resources.getString("openFile"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setSelectedFile(null);
    chooser.rescanCurrentDirectory();
    String newFile = "";
    if (chooser.showDialog(this, resources.getString("open")) ==
          JFileChooser.APPROVE_OPTION &&
        chooser.getSelectedFile() != null) {
      try {
        newFile = chooser.getSelectedFile().getCanonicalPath();
        String newDir = chooser.getSelectedFile().getParent();
        if (newDir != null) {
          baseDir = newDir;
        }
      } catch (IOException ioe) {
      }
      if (sf != null) {
        try {
          sf.close();
          sf = null;
        } catch (IOException ioe) {
        }
      }
      loadFile(newFile);
    }
  }

  /**
   * Loads a file.
   * @param     newFile The name of the file to load.
   */
  void loadFile(String newFile)
  {
    try {
      sf = new StructFile(newFile, StructFile.OLD);
      displayFile(newFile);
    } catch (Exception e) {
      error(e.getMessage());
    }
  }

  /**
   * Close the structured file.
   */
  private void closeFile()
  {
    if (sp != null) {
      remove(sp);
      sp = null;
      tree = null;
      repaint();
    }
    if (sf != null) {
      try {
        sf.close();
        sf = null;
      } catch (IOException ioe) {
      }
    }
    structuredFileName = null;
  }

  /**
   * Create a new folder in the structured file.
   */
  private void newFolder()
  {
    EntryNode node = ((EntryNode)(tree.getLastSelectedPathComponent()));
    if (node == null) {
      error(resources.getString("noEntry"));
      return;
    }
    if (!node.isRoot()) {
      Entry entry = node.getEntry();
      if (entry.getType() != Entry.DIRECTORY) {
        error(resources.getString("notDir1") + " " + node.getName() + " " +
              resources.getString("notDir2"));
        return;
      }
    }
    try {
      changeTo(node);
      String newName = resources.getString("newFolderName");
      String actualNewName = resources.getString("newFolder");
      int i = 1;
      Entry e;
      do {
        try {
          e = sf.findEntry(actualNewName);
        } catch (Exception ex) {
          e = null;
          break;
        }
        actualNewName = newName + " (" + ++i + ")";
      } while (e != null);
      Entry entry = sf.createDir(actualNewName);
      EntryNode child = new EntryNode(entry);
      node.add(child);
      sort(node);
      reloadModel();
      makeVisible(child);
      select(node);
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
      error(ex.getMessage());
    }
  }

  /**
   * Add files to the structured file.
   */
  private void addFiles()
  {
    EntryNode node = ((EntryNode)(tree.getLastSelectedPathComponent()));
    if (node == null) {
      error(resources.getString("noEntry"));
      return;
    }
    Entry e = node.getEntry();
    if (e.getType() != Entry.DIRECTORY) {
      error(resources.getString("notDir1") + " " + node.getName() + " " +
            resources.getString("notDir2"));
      return;
    }
    if (chooser == null) {
      chooser = new JFileChooser(baseDir);
    } else {
      chooser.setCurrentDirectory(new File(baseDir));
    }
    chooser.setDialogTitle(resources.getString("addFiles"));
    chooser.setApproveButtonToolTipText(resources.getString("addFiles"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(true);
    chooser.setSelectedFile(null);
    chooser.rescanCurrentDirectory();
    EntryNode newNode = null;
    // Multiple file selection in JFileChooser does not work yet, so we
    // use single selection for the time being.
    if (chooser.showDialog(this, resources.getString("add")) ==
          JFileChooser.APPROVE_OPTION &&
        chooser.getSelectedFile() != null) {
      File f = chooser.getSelectedFile();
      String newDir = f.getParent();
      if (newDir != null) {
        baseDir = newDir;
      }
      newNode = addFile(node, f);
      sort(node);
      reloadModel();
      makeVisible(newNode);
      select(node);
    }
/*
    if (chooser.showDialog(this, resources.getString("add")) ==
          JFileChooser.APPROVE_OPTION &&
        chooser.getSelectedFiles() != null) {
      File[] f = chooser.getSelectedFiles();
      String newDir = f[0].getParent();
      if (newDir != null) {
        baseDir = newDir;
      }
      for (int i=0; i<f.length; i++) {
        newNode = addFile(node, f[i]);
      }
      sort(node);
      reloadModel();
      makeVisible(newNode);
      select(node);
    }
*/
  }

  /**
   * Add a file to the structured file.
   * @param     node    The node corresponding to the folder where the
   *                    file will be copied.
   * @param     f       The file to add.
   * @return    The tree node corresponding to the newly created entry in the
   *            structured file.
   */
  private EntryNode addFile(EntryNode node, File f)
  {
    EntryNode newNode = null;
    try {
      if (!f.exists()) {
        error(resources.getString("notExist1") + " " + f.getCanonicalPath() +
              " " + resources.getString("notExist2"));
        return null;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
      error(ioe.getMessage());
      return null;
    }
    int BUFSIZE = 4096;
    try {
      changeTo(node);
      Entry oldEntry;
      String name = f.getName();
      try {
        oldEntry = sf.findEntry(name);
      } catch (Exception ex1) {
        oldEntry = null;
      }
      if (oldEntry != null) {
        String message;
        String fullName;
        try {
          fullName = f.getCanonicalPath();
        } catch (IOException e) {
          fullName = name;
        }
        if (oldEntry.type == Entry.DIRECTORY) {
          message = resources.getString("confirmOverWrite3") + " " +
                    name + " " +
                    resources.getString("confirmOverWrite4") + " " +
                    fullName +
                    resources.getString("confirmOverWrite5");
        }else{
          message = resources.getString("confirmOverWrite1") + " " +
                    name +
                    resources.getString("confirmOverWrite2");
        }
        if (!confirm(message)) {
          // Return the existing child node, to ensure that it is made visible
          // after adding files has been completed.
          return node.getChild(name);
        }else{
          node.remove(name);
        }
      }
      Entry outEntry = sf.createFile(name);
      StructOutputStream so = new StructOutputStream(sf, outEntry);
      FileInputStream fi = new FileInputStream(f);
      BufferedInputStream bi = new BufferedInputStream(fi, BUFSIZE);
      byte[] buf = new byte[BUFSIZE];
      int n;
      do {
        n = bi.read(buf, 0, BUFSIZE);
        if (n > 0) {
          so.write(buf, 0, n);
        }
      } while (n >= 0);
      bi.close();
      so.close();
      newNode = new EntryNode(outEntry);
      node.add(newNode);
    } catch(Exception ex2) {
      ex2.printStackTrace();
      error(ex2.getMessage());
    }
    return newNode;
  }

  /**
   * Recursively add the contents of a folder to the structured file.
   */
  private void addFolder()
  {
    EntryNode node = ((EntryNode)(tree.getLastSelectedPathComponent()));
    if (node == null) {
      error(resources.getString("noEntry"));
      return;
    }
    Entry e = node.getEntry();
    if (e.getType() != Entry.DIRECTORY) {
      error(resources.getString("notDir1") + " " + node.getName() + " " +
            resources.getString("notDir2"));
      return;
    }
    if (chooser == null) {
      chooser = new JFileChooser(baseDir);
    } else {
      chooser.setCurrentDirectory(new File(baseDir));
    }
    chooser.setDialogTitle(resources.getString("addFolder"));
    chooser.setApproveButtonToolTipText(resources.getString("addFolder"));
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser.setSelectedFile(null);
    chooser.rescanCurrentDirectory();
    EntryNode newNode = null;
    if (chooser.showDialog(this, resources.getString("add")) ==
          JFileChooser.APPROVE_OPTION &&
        chooser.getSelectedFile() != null) {
      File f = chooser.getSelectedFile();
      try {
        String newDir = chooser.getSelectedFile().getParent();
        if (newDir != null) {
          baseDir = newDir;
        }
        changeTo(node);
        newNode = addFolder(node, f);
      } catch(Exception ex) {
        ex.printStackTrace();
        error(ex.getMessage());
      }
      sort(node);
      reloadModel();
      makeVisible(newNode);
      select(node);
    }
  }

  /**
   * Recursively add a file or folder to the structured file.
   * @param     node    The node corresponding to the folder where the
   *                    file or folder will be copied.
   * @param     f       The file or folder that will be added.
   * @return    The tree node corresponding to the last of the newly created
   *            entries in the structured file.
   */
  private EntryNode addFolder(EntryNode node, File f)
  {
    EntryNode newNode = null;
    try {
      if (!f.exists()) {
        error(resources.getString("notExist3") + " " + f.getCanonicalPath() +
              " " + resources.getString("notExist4"));
        return null;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
      error(ioe.getMessage());
      return null;
    }
    String name = f.getName();
    String fullName;
    try {
      fullName = f.getCanonicalPath();
    } catch (IOException e) {
      fullName = name;
    }
    Entry oldEntry;
    try {
      oldEntry = sf.findEntry(name);
    } catch (Exception ex) {
      oldEntry = null;
    }
    try {
      if (f.isFile()) {
        if (oldEntry != null && oldEntry.getType() == Entry.DIRECTORY) {
          if (confirm(resources.getString("confirmOverWrite3") + " " +
                      name + " " +
                      resources.getString("confirmOverWrite4") + " " +
                      fullName +
                      resources.getString("confirmOverWrite5"))) {
            sf.deleteEntry(name);
            oldEntry = null;
            node.remove(name);
          }else{
            return node.getChild(name);
          }
        }
        newNode = addFile(node, f);
      }else{
        if (oldEntry != null && oldEntry.getType() == Entry.FILE) {
          if (confirm(resources.getString("confirmOverWrite6") + " " +
                      name + " " +
                      resources.getString("confirmOverWrite7") + " " +
                      fullName +
                      resources.getString("confirmOverWrite8"))) {
            sf.deleteEntry(name);
            oldEntry = null;
            node.remove(name);
          }else{
            return node.getChild(name);
          }
        }
        if (oldEntry == null) {
          Entry newEntry = sf.createDir(name);
          sf.changeDir(newEntry);
          newNode = new EntryNode(newEntry);
          node.add(newNode);
        }else{
          sf.changeDir(oldEntry);
          newNode = node.getChild(name);
        }
        String[] files = f.list();
        EntryNode dirNode = newNode;
        for (int i=0; i<files.length; i++) {
          newNode = addFolder(dirNode, new File(f, files[i]));
        }
        sf.changeToParentDir();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      error(ex.getMessage());
    }
    return newNode;
  }


  /**
   * Delete an entry in the structured file.
   */
  private void deleteEntry()
  {
    EntryNode node = ((EntryNode)(tree.getLastSelectedPathComponent()));
    EntryNode nodeToMakeVisible = null;;
    if (node == null) {
      error(resources.getString("noEntry"));
      return;
    }
    String message;
    if (node.isRoot()) {
      message = resources.getString("confirmDeleteContents");
    }else{
      Entry e = node.getEntry();
      if (e.getType() == Entry.FILE) {
        message = resources.getString("confirmDelete1") + " " + node.getName() +
                  resources.getString("confirmDelete2");
      }else{
        message = resources.getString("confirmDeleteAll1") + " " +
                  node.getName() + " " +
                  resources.getString("confirmDeleteAll2");
      }
    }
    if (confirm(message)) {
      if (node.isRoot()) {
        int n = node.getChildCount();
        for (int i=0; i<n; i++) {
          Entry e = ((EntryNode)(node.getChildAt(i))).getEntry();
          try {
            sf.changeToRootDir();
            sf.deleteEntry(e);
            node.removeAllChildren();
            nodeToMakeVisible = null;
          } catch (IOException ex){
            ex.printStackTrace(System.out);
            error(ex.getMessage());
          }
        }
      }else{
        try {
          EntryNode parent = (EntryNode)(node.getParent());
          changeTo(parent);
          Entry e = node.getEntry();
          sf.deleteEntry(e);
          int i = parent.getIndex(node);
          if (i > 0) {
            i--;
          }else{
            i = 1;
          } 
          if (parent.getChildCount() > 1) {
            nodeToMakeVisible = (EntryNode)(parent.getChildAt(i));
          }else{
            nodeToMakeVisible = parent;
          }
          parent.remove(node);
        } catch (IOException ex){
          ex.printStackTrace(System.out);
          error(ex.getMessage());
        }
      }
      reloadModel();
      makeVisible(nodeToMakeVisible);
    }
  }

  /**
   * Optimizes the structured file.
   */
  private void optimize()
  {
    String name = structuredFileName;
    closeFile();
    try {
      Defragment.defragment(name, "tmp.tmp");
    } catch (Exception e) {
      e.printStackTrace();
      error(e.getMessage());
    }
    loadFile(name);
  }

  /**
   * Exit the application.
   */
  void exit()
  {
    if (sf != null) {
      try {
        sf.close();
      } catch (IOException ioe) {
      }
    }
    System.exit(0);
  }

  /**
   * Displays an informational message about the application.
   */
  private void about()
  {
    String[] message;
    if (sf != null) {
      message = new String[5];
    }else{
      message = new String[3];
    }
    message[0] = resources.getString("fileToolAbout1") + " " + version;
    message[1] = resources.getString("fileToolAbout2");
    message[2] = resources.getString("fileToolAbout3");
    if (sf != null) {
      message[3] = " ";
      message[4] = resources.getString("formatVersion1") +
                   sf.getFormatVersion() +
                   resources.getString("formatVersion2");
    }
    info(resources.getString("fileToolAbout0"), message);
  }

  /**
   * Changes the current directory of the structured file to that
   * corresponding to an entry node.
   * @param     node    The entry node.
   * @exception IOException     Thrown if accessing the structured file fails.
   */
  private void changeTo(EntryNode node)
    throws IOException
  {
    sf.changeToRootDir();
    TreeNode[] path = node.getPath();
    for (int i=1; i<path.length; i++) {
      sf.changeDir(((EntryNode)(path[i])).getName());
    }
  }

  /**
   * Displays the structure of the structured file.
   * @param     name    The name of the file being displayed.
   */
  private void displayFile(String name)
  {
    if (sp != null) {
      remove(sp);
      sp = null;
      tree = null;
      repaint();
    }
    try {
      sf.changeToRootDir();
    } catch (IOException ioe) {
    }
    EntryNode root = new EntryNode(sf.getCurrentDirEntry());
    root.setUserObject(name);
    root.setName(name);
    structuredFileName = name;
    addChildren(root);
    tree = new JTree(root);
    tree.setRowHeight(
      2 +
      Math.max(
        tree.getRowHeight(),
        new ImageIcon(
          this.getClass().getResource("File.gif")).getIconHeight())
    );
    tree.setRootVisible(true);
    tree.setCellEditor(new EntryCellEditor(tree, sf));
    tree.setEditable(true);
    tree.setBackground(new Color(0xFFFFE4));
    tree.setCellRenderer(new EntryCellRenderer());
    sp = new JScrollPane(tree,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    add(sp, BorderLayout.CENTER);
    revalidate();
  }

  /**
   * Paint the component.
   * @param     g       The graphics context in which to paint the component.
   */
  public void paintComponent(Graphics g)
  {
    
     // Make sure the tree has its preferred size, rather than that of the
     // containing scroll pane, so that the scroll pane's scroll bar;s will
     // appear if required.
    if (tree != null) {
      tree.setMinimumSize(tree.getPreferredSize());
      tree.setMaximumSize(tree.getPreferredSize());
      tree.revalidate();
    }
    // Now we can paint the component.
    super.paintComponent(g);
  }

  /**
   * Recursively adds the children of a tree node associated with a directory
   * entry in a structured file.
   * @param     node    The node to which its children will be added.
   */
  private void addChildren(EntryNode node)
  {
    //Entry nodeEntry = node.getEntry();
    Vector children = sf.list();
    int length = children.size();
    for (int i=0; i<length; i++) {
      Entry e = (Entry)(children.elementAt(i));
      EntryNode child = new EntryNode(e);
      node.add(child);
      if (e.getType() == Entry.DIRECTORY) {
        try {
          //sf.changeDir(e.getName());
          sf.changeDir(e);
          addChildren(child);
          sf.changeToParentDir();
        } catch (IOException ioe) {
        }
      }
    }
    sort(node);
  }
  
  /**
   * Sort the children of a node according to their name.
   * @param     node    The node whose children will be sorted.
   */
  void sort(EntryNode node)
  {
    TreeTools.sort(node);
  }

  /**
   * Make a node visible.
   * @param     node    The node to make visible.
   */
  private void makeVisible(EntryNode node)
  {
    TreeTools.makeVisible(tree, node);
  }

  /**
   * Reloads the model of the JTree component displaying the structure of the
   * file.
   */
  void reloadModel()
  {
    TreeTools.reloadModel(tree);
  }

  /**
   * Selects a node in the JTree component displaying the structure of the
   * file.
   * @param     node    The node to select.
   */
  void select(EntryNode node)
  {
    TreeTools.select(tree, node);
  }

  /**
   * Displays an error message.
   * @param     message The message to display.
   */
  private void error(String message)
  {
    Object[] okLabel = {resources.getString("ok")};
    JOptionPane pane =
      new JOptionPane(
        message, JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION, null,
        okLabel, okLabel[0]
      );
    JDialog dialog = pane.createDialog(this, resources.getString("error"));
    dialog.setVisible(true);
  }

  /**
   * Displays an informational message.
   * @param     title   The title of the dialog box in which the message
   *                    will be displayed.
   * @param     message The message to display.
   */
  private void info(String title, Object message)
  {
    Object[] okLabel = {resources.getString("ok")};
    JOptionPane pane =
      new JOptionPane(
        message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION, null,
        okLabel, okLabel[0]
      );
    JDialog dialog = pane.createDialog(this, title);
    dialog.setVisible(true);
  }

  /**
   * Displays a confirmation dialog.
   * @param     message The message to display.
   * @return    True if the user confirms the question expressed by the
   *            message, false otherwise.
   */
  private boolean confirm(String message)
  {
    Object[] labels = {
      resources.getString("yes"),
      resources.getString("no")
    };
    JOptionPane pane =
      new JOptionPane(message, JOptionPane.QUESTION_MESSAGE,
                      JOptionPane.YES_NO_OPTION, null, labels, labels[0]);
    JDialog dialog = pane.createDialog(this, resources.getString("confirm"));
    dialog.setVisible(true);
    Object selectedValue = pane.getValue();
    if (selectedValue == null) {
      return false;
    }
    if (labels[0].equals(selectedValue)) {
      return true;
    }else{
      return false;
    }
  }
}
