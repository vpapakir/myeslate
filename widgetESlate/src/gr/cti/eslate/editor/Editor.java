package gr.cti.eslate.editor;

import java.util.*;

import java.net.URL;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.print.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.text.Caret;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import org.netbeans.editor.*;
import org.netbeans.editor.ext.*;
import org.openide.text.*;

import gr.cti.eslate.utils.*;

/**
 * Editor panel built upon the NetBeans editor.
 *
 * @author      Petr Nejedly
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class Editor extends JPanel
  implements CaretListener, ChangeListener, Externalizable
{
  static final long serialVersionUID = 1L;

  private final static String COLORING_MAP = "coloringMap";
  private final static String FONT_NAME = "fontName";
  private final static String FONT_SIZE = "fontSize";
  private final static String MULTIPLE_FILE_EDITOR = "multipleFileEditor";
  private final static int saveFormatVersion = 1;

  // This is a netbeans resource bundle. Do not change.
  private static final String SETTINGS = "settings";

  /**
   * Document property holding String name of associated file
   */
  private static final String FILE = "file";
  /**
   * Document property holding Boolean if document was created or opened
   */
  private static final String CREATED = "created";
  /**
   * Document property holding Boolean modified information
   */
  private static final String MODIFIED = "modified";

  /**
   * Netbeans editor resources.
   */
  private ResourceBundle settings = ResourceBundle.getBundle(
    SETTINGS, Locale.getDefault()
  );
  /**
   * Editor pane resources.
   */
  private ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.editor.EditorResource", Locale.getDefault()
  );

  /**
   * Plain text file.
   */
  public final static String PLAIN_TEXT = "text/plain2";
  /**
   * Java file.
   */
  public final static String JAVA = "text/x-java";
  /**
   * Logo file.
   */
  public final static String LOGO = "text/x-logo";
  /**
   * JavaScript file.
   */
  public final static String JAVASCRIPT = "text/x-javascript";

  /**
   * A list of the classes of the supported editor kits.
   */
  static Class[] supportedKits =
  {
    gr.cti.eslate.editor.JavaKit.class,
    gr.cti.eslate.editor.JavaScriptKit.class,
    gr.cti.eslate.editor.LogoKit.class,
    gr.cti.eslate.editor.PlainKit.class
  };

  /**
   * A list of the focus listeners that have been added to the component.
   */
  private ArrayList<FocusListener> focusListeners =
    new ArrayList<FocusListener>();
  private boolean initted = false;

  private JFileChooser fileChooser;
  private JDialog customizerDialog = null;

  private int fileCounter = -1;
  Map<JComponent, JEditorPane> com2text =
    new HashMap<JComponent, JEditorPane>();
  Map<JComponent, UndoManager> com2um = new HashMap<JComponent, UndoManager>();
  Map <JComponent, Integer> com2blink = new HashMap<JComponent, Integer>();
  private boolean standalone;
  private String defaultFont = "monospaced";
  private final static int defaultFontSize = 12;
  private int fontSize = defaultFontSize;
  private boolean multiple = true;
  private boolean ignoreEvents = false;
  private ArrayList<Object> newInfo = new ArrayList<Object>();
  private boolean editable = true;
  private gr.cti.eslate.editor.FindDialogSupport findDialogSupport;
  private gr.cti.eslate.editor.GotoDialogSupport gotoDialogSupport;

  /**
   * The list of registered undo/redo status listeners.
   */
  private ArrayList<UndoRedoStatusListener> undoRedoStatusListeners =
    new ArrayList<UndoRedoStatusListener>();
  /**
   * The event to send to registered undo/redo status listeners.
   */
  private UndoRedoStatusEvent undoRedoStatusEvent;

  private static ImageIcon newIcon = null;
  private static ImageIcon openIcon = null;
  private static ImageIcon closeIcon = null;
  private static ImageIcon saveIcon = null;
  private static ImageIcon saveAllIcon = null;
  private static ImageIcon undoIcon = null;
  private static ImageIcon redoIcon = null;
  private static ImageIcon cutIcon = null;
  private static ImageIcon copyIcon = null;
  private static ImageIcon pasteIcon = null;
  private static ImageIcon deleteIcon = null;
  private static ImageIcon findIcon = null;
  private static ImageIcon replaceIcon = null;
  private static ImageIcon findNextIcon = null;
  private static ImageIcon printIcon = null;

  //private boolean newMenuEnabled = true;
  //private boolean openItemEnabled = true;
  //private boolean closeItemEnabled = true;
  //private boolean saveItemEnabled = true;
  //private boolean saveAsItemEnabled = true;
  //private boolean saveAllItemEnabled = true;
  //private boolean undoItemEnabled = true;
  //private boolean redoItemEnabled = true;
  //private boolean cutItemEnabled = true;
  //private boolean copyItemEnabled = true;
  //private boolean pasteItemEnabled = true;
  private boolean deleteItemEnabled = true;
  //private boolean selectAllItemEnabled = true;
  //private boolean findItemEnabled = true;
  //private boolean replaceItemEnabled = true;
  //private boolean findNextItemEnabled = true;
  //private boolean gotoLineItemEnabled = true;
  //private boolean configItemEnabled = true;
  //private boolean printItemEnabled = true;

  //private boolean newButtonEnabled = true;
  //private boolean openButtonEnabled = true;
  //private boolean closeButtonEnabled = true;
  //private boolean saveButtonEnabled = true;
  //private boolean saveAllButtonEnabled = true;
  //private boolean copyButtonEnabled = true;
  private boolean pasteButtonEnabled = true;
  private boolean cutButtonEnabled = true;
  //private boolean undoButtonEnabled = true;
  //private boolean redoButtonEnabled = true;
  //private boolean findButtonEnabled = true;
  private boolean findNextButtonEnabled = true;
  //private boolean replaceButtonEnabled = true;
  //private boolean printButtonEnabled = true;

  private static class Localizer implements LocaleSupport.Localizer
  {
    ResourceBundle bundle;
     
    public Localizer( String bundleName )
    {
       bundle = ResourceBundle.getBundle( bundleName );
    }
     
    public String getString( String key )
    {
      return bundle.getString( key );
    }
  }

  /**
   * Create an editor instance.
   */
  public Editor()
  {
    this(false);
  }

  /**
   * Create an editor instance.
   * @param     isApp   Specifies whether the editor is running as a
   *                    standalone application. If true, then the "File" menu
   *                    will contain and "Exit" item, and the editor will
   *                    update the title bar of the hosting Frame.
   */
  public Editor(boolean standalone)
  {
    super();
    LocaleSupport.addLocalizer(new Localizer("org.netbeans.editor.Bundle"));
    this.standalone = standalone;

    LocaleSupport.addLocalizer((LocaleSupport.Localizer)resources);

    // Feed our kits with their default Settings
    Settings.addInitializer(new BaseSettingsInitializer(), Settings.CORE_LEVEL);
    Settings.addInitializer(new ExtSettingsInitializer(), Settings.CORE_LEVEL);
    Settings.reset();

    // Create visual hierarchy
    initComponents();

    // Prepare the editor kits and such things
    readSettings();

    FontOptions.setFontSize(fontSize);
    undoRedoStatusEvent = new UndoRedoStatusEvent(this, false, false);

    addAncestorListener(new AncestorListener(){
      public void ancestorAdded(AncestorEvent e)
      {
        // This hack will ensure the correct visibility status of the status
        // bar, as setting the visibility before the component is displayed
        // does not seem to work.
        boolean visible = statusBarVisible;
        setStatusBarVisible(!visible);
        setStatusBarVisible(visible);

        // Unless we do this, all editor instances have a blinking caret!
        // Clinking on all editors cures this, so we simulate this by
        // temporarily grabbing the focus when the editor is made visible.
        //Component focusOwner =
          //SwingUtilities.findFocusOwner(SwingUtilities.getRoot(Editor.this));
        Component fo =
          KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        Component focusOwner = null;
        // verify focusOwner is a descendant of this component
        for (Component temp=focusOwner; temp!=null;
             temp = (temp instanceof Window) ? null : temp.getParent()) {
          if (temp == Editor.this) {
            focusOwner = fo;
            break;
          }
        }
        requestFocus();
        if (focusOwner != null) {
          focusOwner.requestFocus();
        }
      }
      public void ancestorMoved(AncestorEvent e)
      {
      }
      public void ancestorRemoved(AncestorEvent e)
      {
      }
    });

    initted = true;
  }

  public Dimension getPreferredSize()
  {
    Dimension size = new Dimension(640, 480);
    return size;
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   * (Tough luck -- K.K.)
   */
  private void initComponents()
  {
    tabPane = new JTabbedPane();
    tabPane.addChangeListener(this);

    setLayout(new BorderLayout());

    northPanel = new JPanel(new BorderLayout());
    add(northPanel, BorderLayout.NORTH);
    add(tabPane, BorderLayout.CENTER);
  }

  /**
   * Initializes the icons used in the menu and tool bars.
   */
  private void initIcons()
  {
    if (newIcon == null) {
      Class c = Editor.class;
      newIcon = new ImageIcon(c.getResource("res/new.gif"));
      openIcon = new ImageIcon(c.getResource("res/open.gif"));
      closeIcon = new ImageIcon(c.getResource("res/close.gif"));
      saveIcon = new ImageIcon(c.getResource("res/save.gif"));
      saveAllIcon = new ImageIcon(c.getResource("res/saveall.gif"));
      undoIcon = new ImageIcon(c.getResource("res/undo.gif"));
      redoIcon = new ImageIcon(c.getResource("res/redo.gif"));
      cutIcon = new ImageIcon(c.getResource("res/cut.gif"));
      copyIcon = new ImageIcon(c.getResource("res/copy.gif"));
      pasteIcon = new ImageIcon(c.getResource("res/paste.gif"));
      deleteIcon = new ImageIcon(c.getResource("res/delete.gif"));
      findIcon = new ImageIcon(c.getResource("res/find.gif"));
      replaceIcon = new ImageIcon(c.getResource("res/replace.gif"));
      findNextIcon = new ImageIcon(c.getResource("res/findnext.gif"));
      printIcon = new ImageIcon(c.getResource("res/print.gif"));
    }
  }

  /**
   * Initializes the menu bar.
   */
  private void initMenuBar()
  {
    initIcons();

    menuBar = new JMenuBar();
    fileMenu = new JMenu();
    newMenu = new JMenu();
    openItem = new JMenuItem();
    openItem = new JMenuItem();
    closeItem = new JMenuItem();
    saveItem = new JMenuItem();
    saveAsItem = new JMenuItem();
    saveAllItem = new JMenuItem();
    printItem = new JMenuItem();
    if (standalone) {
      exitItem = new JMenuItem();
    }
    editMenu = new JMenu();
    undoItem = new JMenuItem();
    redoItem = new JMenuItem();
    cutItem = new JMenuItem();
    copyItem = new JMenuItem();
    pasteItem = new JMenuItem();
    deleteItem = new JMenuItem();
    selectAllItem = new JMenuItem();
    searchMenu = new JMenu();
    findItem = new JMenuItem();
    replaceItem = new JMenuItem();
    findNextItem = new JMenuItem();
    gotoLineItem = new JMenuItem();
    optionMenu = new JMenu();
    configItem = new JMenuItem();

    //fileMenu.setMnemonic(KeyEvent.VK_F);
    fileMenu.setText(resources.getString("file"));

    //newMenu.setMnemonic(KeyEvent.VK_N);
    newMenu.setText(resources.getString("new"));
    newMenu.setIcon(newIcon);
    fileMenu.add(newMenu);

    // Add the subitems for each editor kit to the "new" menu item.
    int n = newInfo.size();
    for (int i=0; i<n; i+=2) {
      KitInfo ki = (KitInfo)(newInfo.get(i));
      String menuTitle = (String)(newInfo.get(i+1));
      JMenuItem item = new JMenuItem(menuTitle, ki.getIcon());
      //item.setMnemonic(menuMnemonic);
      NewFileActionListener nfal = new NewFileActionListener(ki);
      item.addActionListener(nfal);
      newMenu.add(item);
    }
    if (toolBar != null) {
      newInfo.clear();
      newInfo = null;
    }

    //openItem.setMnemonic(KeyEvent.VK_O);
    openItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)
    );
    openItem.setText(resources.getString("open"));
    openItem.setIcon(openIcon);
    openItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        openItemActionPerformed(evt);
      }
    });
    fileMenu.add(openItem);

    //closeItem.setMnemonic(KeyEvent.VK_C);
    closeItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK)
    );
    closeItem.setText(resources.getString("close"));
    closeItem.setIcon(closeIcon);
    closeItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        closeItemActionPerformed(evt);
      }
    });
    enableCloseItem(false);
    fileMenu.add(closeItem);

    fileMenu.add(new JSeparator());

    //saveItem.setMnemonic(KeyEvent.VK_S);
    saveItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)
    );
    saveItem.setText(resources.getString("save"));
    saveItem.setIcon(saveIcon);
    saveItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        saveItemActionPerformed(evt);
      }
    });
    enableSaveItem(false);
    fileMenu.add(saveItem);

    //saveAsItem.setMnemonic(KeyEvent.VK_A);
    saveAsItem.setText(resources.getString("saveAs"));
    saveAsItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        saveAsItemActionPerformed(evt);
      }
    });
    enableSaveAsItem(false);
    fileMenu.add(saveAsItem);

    saveAllItem.setMnemonic(KeyEvent.VK_L);
    saveAllItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK)
    );
    saveAllItem.setText(resources.getString("saveAll"));
    saveAllItem.setIcon(saveAllIcon);
    saveAllItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        saveAllItemActionPerformed(evt);
      }
    });
    enableSaveAllItem(false);
    fileMenu.add(saveAllItem);

    fileMenu.add(new JSeparator());
    printItem.setMnemonic(KeyEvent.VK_P);
    printItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK)
    );
    printItem.setText(resources.getString("print"));
    printItem.setIcon(printIcon);
    printItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        printItemActionPerformed(evt);
      }
    });
    enablePrintItem(false);
    fileMenu.add(printItem);

    if (standalone) {
      fileMenu.add(new JSeparator());

      //exitItem.setMnemonic(KeyEvent.VK_E);
      exitItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)
      );
      exitItem.setText(resources.getString("exit"));
      exitItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt)
        {
          exitItemActionPerformed(evt);
        }
      });
      fileMenu.add(exitItem);
    }
    menuBar.add(fileMenu);

    editMenu.setText(resources.getString("edit"));

    undoItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)
    );
    undoItem.setText(resources.getString("undo"));
    undoItem.setIcon(undoIcon);
    undoItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        undoItemActionPerformed(evt);
      }
    });
    enableUndoItem(false);
    editMenu.add(undoItem);

    redoItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK)
    );
    redoItem.setText(resources.getString("redo"));
    redoItem.setIcon(redoIcon);
    redoItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        redoItemActionPerformed(evt);
      }
    });
    enableRedoItem(false);
    editMenu.add(redoItem);

    editMenu.add(new JSeparator());

    cutItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)
    );
    cutItem.setText(resources.getString("cut"));
    cutItem.setIcon(cutIcon);
    cutItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        cutItemActionPerformed(evt);
      }
    });
    enableCutItem(false);
    editMenu.add(cutItem);

    copyItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)
    );
    copyItem.setText(resources.getString("copy"));
    copyItem.setIcon(copyIcon);
    copyItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        copyItemActionPerformed(evt);
      }
    });
    enableCopyItem(false);
    editMenu.add(copyItem);

    pasteItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK)
    );
    pasteItem.setText(resources.getString("paste"));
    pasteItem.setIcon(pasteIcon);
    pasteItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        pasteItemActionPerformed(evt);
      }
    });
    enablePasteItem(false);
    editMenu.add(pasteItem);

    deleteItem.setText(resources.getString("delete"));
    deleteItem.setIcon(deleteIcon);
    deleteItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        deleteItemActionPerformed(evt);
      }
    });
    enableDeleteItem(false);
    editMenu.add(deleteItem);

    editMenu.add(new JSeparator());

    selectAllItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK)
    );
    selectAllItem.setText(resources.getString("selectAll"));
    selectAllItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        selectAllItemActionPerformed(evt);
      }
    });
    enableSelectAllItem(false);
    editMenu.add(selectAllItem);

    menuBar.add(editMenu);

    searchMenu.setText(resources.getString("search"));
    findItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)
    );
    findItem.setText(resources.getString("find"));
    findItem.setIcon(findIcon);
    findItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        findItemActionPerformed(evt);
      }
    });
    enableFindItem(false);
    searchMenu.add(findItem);

    replaceItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK)
    );
    replaceItem.setText(resources.getString("replace"));
    replaceItem.setIcon(replaceIcon);
    replaceItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        replaceItemActionPerformed(evt);
      }
    });
    enableReplaceItem(false);
    searchMenu.add(replaceItem);

    findNextItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)
    );
    findNextItem.setText(resources.getString("searchAgain"));
    findNextItem.setIcon(findNextIcon);
    findNextItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        findNextItemActionPerformed(evt);
      }
    });
    enableFindNextItem(false);
    searchMenu.add(findNextItem);

    searchMenu.add(new JSeparator());

    gotoLineItem.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)
    );
    gotoLineItem.setText(resources.getString("gotoLine"));
    gotoLineItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        gotoLineItemActionPerformed(evt);
      }
    });
    enableGotoLineItem(false);
    searchMenu.add(gotoLineItem);

    menuBar.add(searchMenu);

    optionMenu.setText(resources.getString("options"));
    configItem.setText(resources.getString("configuration"));
    configItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        configItemActionPerformed(evt);
      }
    });
    enableConfigItem(true);
    optionMenu.add(configItem);

    menuBar.add(optionMenu);
  }

  /**
   * Initilaizes the tool bar.
   */
  private void initToolBar()
  {
    initIcons();

    toolBar = new JToolBar();
    toolBar.setFloatable(false);

    newButton = new ToolButton(newIcon);
    newButton.setToolTipText(resources.getString("newTip"));
    newButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        newButtonActionPerformed(evt);
      }
    });
    toolBar.add(newButton);
    newPopupMenu = new JPopupMenu();
    // Add the subitems for each editor kit to the pop-up menu
    int n = newInfo.size();
    for (int i=0; i<n; i+=2) {
      KitInfo ki = (KitInfo)(newInfo.get(i));
      String menuTitle = (String)(newInfo.get(i+1));
      JMenuItem item = new JMenuItem(menuTitle, ki.getIcon());
      //item.setMnemonic(menuMnemonic);
      NewFileActionListener nfal = new NewFileActionListener(ki);
      item.addActionListener(nfal);
      newPopupMenu.add(item);
    }
    if (menuBar != null) {
      newInfo.clear();
      newInfo = null;
    }
    openButton = new ToolButton(openIcon);
    openButton.setToolTipText(resources.getString("openTip"));
    openButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        openItemActionPerformed(evt);
      }
    });
    toolBar.add(openButton);
    closeButton = new ToolButton(closeIcon);
    closeButton.setToolTipText(resources.getString("closeTip"));
    closeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        closeItemActionPerformed(evt);
      }
    });
    enableCloseButton(false);
    toolBar.add(closeButton);
    saveButton = new ToolButton(saveIcon);
    saveButton.setToolTipText(resources.getString("saveTip"));
    enableSaveButton(false);
    toolBar.add(saveButton);
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        saveItemActionPerformed(evt);
      }
    });
    saveAllButton = new ToolButton(saveAllIcon);
    saveAllButton.setToolTipText(resources.getString("saveAllTip"));
    enableSaveAllButton(false);
    toolBar.add(saveAllButton);
    saveAllButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        saveAllItemActionPerformed(evt);
      }
    });
    printButton = new ToolButton(printIcon);
    printButton.setToolTipText(resources.getString("printTip"));
    enablePrintButton(false);
    toolBar.add(printButton);
    printButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        printItemActionPerformed(evt);
      }
    });

    toolBar.addSeparator();

    copyButton = new ToolButton(copyIcon);
    copyButton.setToolTipText(resources.getString("copyTip"));
    copyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        copyItemActionPerformed(evt);
      }
    });
    enableCopyButton(false);
    toolBar.add(copyButton);
    pasteButton = new ToolButton(pasteIcon);
    pasteButton.setToolTipText(resources.getString("pasteTip"));
    pasteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        pasteItemActionPerformed(evt);
      }
    });
    enablePasteButton(false);
    toolBar.add(pasteButton);
    cutButton = new ToolButton(cutIcon);
    cutButton.setToolTipText(resources.getString("cutTip"));
    cutButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        cutItemActionPerformed(evt);
      }
    });
    enableCutButton(false);
    toolBar.add(cutButton);

    toolBar.addSeparator();

    undoButton = new ToolButton(undoIcon);
    undoButton.setToolTipText(resources.getString("undoTip"));
    undoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        undoItemActionPerformed(evt);
      }
    });
    enableUndoButton(false);
    toolBar.add(undoButton);
    redoButton = new ToolButton(redoIcon);
    redoButton.setToolTipText(resources.getString("redoTip"));
    redoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        redoItemActionPerformed(evt);
      }
    });
    enableRedoButton(false);
    toolBar.add(redoButton);

    toolBar.addSeparator();

    findButton = new ToolButton(findIcon);
    findButton.setToolTipText(resources.getString("findTip"));
    findButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        findItemActionPerformed(evt);
      }
    });
    enableFindButton(false);
    toolBar.add(findButton);
    findNextButton = new ToolButton(findNextIcon);
    findNextButton.setToolTipText(resources.getString("findNextTip"));
    findNextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        findNextItemActionPerformed(evt);
      }
    });
    enableFindNextButton(false);
    toolBar.add(findNextButton);
    replaceButton = new ToolButton(replaceIcon);
    replaceButton.setToolTipText(resources.getString("replaceTip"));
    replaceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        replaceItemActionPerformed(evt);
      }
    });
    enableReplaceButton(false);
    toolBar.add(replaceButton);

    toolBar.addSeparator();

    Vector<String> fontNames = KitCustomizer.fontNames;
    fonts = new JComboBox(fontNames);
    int nFonts = fontNames.size();
    int sel = -1;
    for (int i=0; i<nFonts; i++) {
      String s = fontNames.get(i);
      if (s.equalsIgnoreCase(defaultFont)) {
        sel = i;
        break;
      }
    }
    if (sel >= 0) {
      fonts.setSelectedIndex(sel);
    }
    fonts.setMaximumSize(fonts.getPreferredSize());
    fonts.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt)
      {
        fontsItemStateChanged(evt);
      }
    });
    toolBar.add(fonts);

    fontSizes = new JComboBox(new Object[] {
      new Integer(8), new Integer(9), new Integer(10), new Integer(11),
      new Integer(12), new Integer(14), new Integer(16), new Integer(18),
      new Integer(20), new Integer(22), new Integer(24), new Integer(26),
      new Integer(28), new Integer(36), new Integer(48), new Integer(72)
    });
    fontSizes.setSelectedItem(new Integer(fontSize));
    fontSizes.setEditable(true);
    fontSizes.setToolTipText(resources.getString("fontSize"));
    Dimension s = new Dimension(48, fontSizes.getPreferredSize().height);
    fontSizes.setMinimumSize(s);
    fontSizes.setMaximumSize(s);
    fontSizes.setPreferredSize(s);
    fontSizes.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt)
      {
        fontSizesItemStateChanged(evt);
      }
    });
    toolBar.add(fontSizes);
  }

  /**
   * Action performed when the "new" button is pressed.
   * @param     evt     The event sent when the button is pressed.
   */
  private void newButtonActionPerformed(ActionEvent evt)
  {
    newPopupMenu.show(newButton, 0, 0);
  }

  /**
   * Action performed when the "copy" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void copyItemActionPerformed(ActionEvent evt)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.copy();
    }
  }

  /**
   * Action performed when the "paste" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void pasteItemActionPerformed(ActionEvent evt)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.paste();
    }
  }

  /**
   * Action performed when the "cut" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void cutItemActionPerformed(ActionEvent evt)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.cut();
    }
  }

  /**
   * Action performed when the "delete" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void deleteItemActionPerformed(ActionEvent evt)
  {
    delete();
  }

  /**
   * Deletes the selected text from the file being edited in the currently
   * selected tab.
   */
  public void delete()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.replaceSelection("");
    }
  }

  /**
   * Action performed when the "select all" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void selectAllItemActionPerformed(ActionEvent evt)
  {
    selectAll();
  }

  /**
   * Selects the entire text of the file being edited in the currently
   * selected tab.
   */
  public void selectAll()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.selectAll();
    }
  }

  /**
   * Action performed when the "undo" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void undoItemActionPerformed(ActionEvent evt)
  {
    undo();
  }

  /**
   * Performs an undo operation on the file being edited in the currently
   * selected tab.
   */
  public void undo()
  {
    UndoManager um = com2um.get(tabPane.getSelectedComponent());
    if ((um != null) && um.canUndo()) {
      um.undo();
    }
  }

  /**
   * Action performed when the "redo" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void redoItemActionPerformed(ActionEvent evt)
  {
    redo();
  }

  /**
   * Performs a redo operation on the file being edited in the currently
   * selected tab.
   */
  public void redo()
  {
    UndoManager um = com2um.get(tabPane.getSelectedComponent());
    if ((um != null) && um.canRedo()) {
      um.redo();
    }
  }

  /**
   * Action performed when the "find" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void findItemActionPerformed(ActionEvent evt)
  {
    find(evt);
  }

  /**
   * Pops up a find dialog for the file being edited in the currently
   * selected tab.
   * @param     evt     The event that caused the dialog to pop up.
   */
  private void find(ActionEvent evt)
  {
    find();
  }

  /**
   * Pops up a find dialog for the file being edited in the currently
   * selected tab.
   */
  public void find()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      getFindDialogSupport().showFindDialog();
    }
  }

  /**
   * Action performed when the "find next" menu item is selected.
   * @param     evt     The event sent when the menu item is selected.
   */
  private void findNextItemActionPerformed(ActionEvent evt)
  {
    findNext(evt);
  }

  /**
   * Finds the next matching string in the file being edited in the currently
   * selected tab.
   */
  public void findNext()
  {
    findNext(new ActionEvent(this, ActionEvent.ACTION_FIRST, ""));
  }

  /**
   * Finds the next matching string in the file being edited in the currently
   * selected tab.
   * @param     evt     The event that generated the find next operation.
   */
  private void findNext(ActionEvent evt)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      ExtKit kit = (ExtKit)(edit.getEditorKit());
      Action findNext = kit.getActionByName(ExtKit.findNextAction);
      findNext.actionPerformed(evt);
    }
  }

  /**
   * Action performed when the "print" menu item is selected.
   * @param     evt     The event sent when the menu item is selected.
   */
  private void printItemActionPerformed(ActionEvent evt)
  {
    print(evt);
  }

  /**
   * Prints the text in the currently selected tab.
   */
  public void print()
  {
    print(new ActionEvent(this, ActionEvent.ACTION_FIRST, ""));
  }

  /**
   * Prints the text in the currently selected tab.
   * @param     evt     The event that generated the print operation.
   */
  private void print(ActionEvent evt)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    StyledDocument sd = new FilterDocument(edit.getDocument());
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable((Printable)org.openide.text.NbDocument.findPageable(sd));
    if (job.printDialog()) {
      try {
        job.print();
      } catch (java.awt.print.PrinterException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Action performed when the "replace" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void replaceItemActionPerformed(ActionEvent evt)
  {
    replace(evt);
  }

  /**
   * Pops up a replace dialog for the file being edited in the currently
   * selected tab.
   * @param     evt     The event that cuased the dialog to pup up.
   */
  private void replace(ActionEvent evt)
  {
    replace();
  }

  /**
   * Pops up a replace dialog for the file being edited in the currently
   * selected tab.
   */
  public void replace()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      getFindDialogSupport().showReplaceDialog();
      Map props = FindSupport.getFindSupport().getFindProperties();
      String last = (String)props.get(SettingsNames.FIND_WHAT);
      if (last != null) {
        enableFindNextButton(true);
        enableFindNextItem(true);
      }
    }
  }

  /**
   * Replaces all occurrences of a string in the file in the currently
   * selected tab with another, without highlighting
   * the changes.
   * @param     oldText The text to replace.
   * @param     newText The text with which to replace the old text.
   */
  public void replace(String oldText, String newText)
  {
    Map<String, Object> props = new HashMap<String, Object>();
    props.put(SettingsNames.FIND_WHAT, oldText);
    props.put(SettingsNames.FIND_REPLACE_WITH, newText);
    props.put(SettingsNames.FIND_MATCH_CASE, new Boolean(false));
    props.put(SettingsNames.FIND_HIGHLIGHT_SEARCH, new Boolean(false));
    FindSupport fs = FindSupport.getFindSupport();
    Map oldProps = fs.getFindProperties();
    fs.putFindProperties(props);
    fs.replaceAll(null);
    fs.putFindProperties(oldProps);
  }

  /**
   * Action performed when a font size is selected in the font size selection
   * combo box.
   * @param     evt     The event sent when a font size is selected in the
   *                    font selection combo box.
   */
  private void fontSizesItemStateChanged(ItemEvent evt)
  {
    if (!ignoreEvents) {
      int size = Integer.parseInt(evt.getItem().toString());
      if (size > 0) {
        FontOptions.setFontSize(size);
        fontSize = size;
      }else{
        fontSizes.setSelectedItem(new Integer(fontSize));
      }
    }
  }

  /**
   * Action performed when a font is selected in the font selection combo box.
   * @param     evt     The event sent when a font is selected in the font
   *                    selection combo box.
   */
  private void fontsItemStateChanged(ItemEvent evt)
  {
    if (!ignoreEvents) {
      String family = (String)(evt.getItem());
      FontOptions.setFont(family);
    }
  }

  /**
   * Action performed when the "go to line" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void gotoLineItemActionPerformed(ActionEvent evt)
  {
    gotoLine(evt);
  }

  /**
   * Pops up a dialog specifying the line to which to move in the file being
   * edited in the currently selected tab.
   * @param     evt     The event that caused the dialog to pop up.
   */
  private void gotoLine(ActionEvent evt)
  {
    gotoLine();
  }

  /**
   * Pops up a dialog specifying the line to which to move in the file being
   * edited in the currently selected tab.
   */
  public void gotoLine()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      //Frame f = (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, this));
      getGotoDialogSupport().showGotoDialog();
      edit.requestFocus();
    }
  }

  /**
   * Action performed when the "save as" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void saveAsItemActionPerformed(ActionEvent evt)
  {
    saveAs();
  }

  /**
   * Pops up a dialog to speccify the name under which the file being
   * edited in the currently selected tab will be saved, and then saves that
   * file under the specified name.
   */
  public void saveAs()
  {
    saveAs(tabPane.getSelectedComponent());
  }

  /**
   * Action performed when the "save all" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void saveAllItemActionPerformed(ActionEvent evt)
  {
    saveAll();
  }

  /**
   * Saves all the files being edited by the editor.
   */
  public void saveAll()
  {
    int index = tabPane.getSelectedIndex();
    for (int i=0; i<tabPane.getComponentCount(); i++) {
      saveFile(tabPane.getComponentAt(i));
    }
    tabPane.setSelectedIndex(index);
  }

  /**
   * Action performed when the "save" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void saveItemActionPerformed(ActionEvent evt)
  {
    save();
  }

  /**
   * Saves the file being edited in the currently selected tab.
   */
  public void save()
  {
    saveFile(tabPane.getSelectedComponent());
  }

  /**
   * Action performed when the "exit" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void exitItemActionPerformed(ActionEvent evt)
  {
    doExit();
  }

  /**
   * Action performed when the "close" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void closeItemActionPerformed(ActionEvent evt)
  {
    close();
  }

  /**
   * Closes the file being edited in the currently selected tab.
   * If the file has been modified, a confirmation dialog will be displayed
   * before closing the file.
   */
  public void close()
  {
    close(true);
  }

  /**
   * Closes the file being edited in the currently selected tab.
   * @param     confirm If this variable is true and the file has been
   *                    modified, then a confirmation dialog will be displayed
   *                    before closing the file. If false, then the file will
   *                    be closed immediately.
   */
  public void close(boolean confirm)
  {
    Component editor = tabPane.getSelectedComponent();
    if ((editor != null) && (!confirm || checkClose(editor))) {
      int n = focusListeners.size();
      for (int i=0; i<n; i++) {
        editor.removeFocusListener(focusListeners.get(i));
      }
      tabPane.remove(editor);
      com2text.remove(editor);
      if (tabPane.getTabCount() == 0 ) {
        enableCloseButton(false);
        enableCloseItem(false);
        enableSaveButton(false);
        enableSaveItem(false);
        enableSaveAllItem(false);
        enableSaveAllButton(false);
        enableSaveAsItem(false);
        enableCutButton(false);
        enableCutItem(false);
        enablePasteButton(false);
        enablePasteItem(false);
        enableCopyButton(false);
        enableCopyButton(false);
        enableFindButton(false);
        enableFindItem(false);
        enableFindNextButton(false);
        enableFindNextItem(false);
        enableReplaceButton(false);
        enableReplaceItem(false);
        enableGotoLineItem(false);
        enableDeleteItem(false);
        enableSelectAllItem(false);
        enablePrintButton(false);
        enablePrintItem(false);
      }
      revalidate();
      repaint();
      UndoManager um = com2um.get(editor);
      com2um.remove(um);
      um.discardAllEdits();
      com2blink.remove(editor);
      editor = tabPane.getSelectedComponent();
      um = com2um.get(editor);
      updateUndoRedoUI(um);
      updateUIForMultiplicity();
    }
  }

  /**
   * Action performed when the "open" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void openItemActionPerformed(ActionEvent evt)
  {
    fileChooser.setMultiSelectionEnabled(true);
    int returnVal = fileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File[] files = fileChooser.getSelectedFiles();
      for (int i=0; i<files.length; i++) {
        openFile(files[i]);
      }
    }
    fileChooser.setMultiSelectionEnabled(false);
  }

  /**
   * Action performed when the "options" menu item is selected.
   * @param     evt     The event sent when the menu item is pressed.
   */
  private void configItemActionPerformed(ActionEvent evt)
  {
    config();
  }

  /**
   * Pops up a dialog that allows the user to specify what fonts and colors to
   * use for various types of text.
   */
  public void config()
  {
    Frame f = (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, this));
    customizerDialog = new JDialog(f, true);
    EditorCustomizer customizer = new EditorCustomizer();
    customizer.setObject(this);
    customizerDialog.getContentPane().setLayout(new BorderLayout());
    customizerDialog.getContentPane().add(customizer);
    customizerDialog.pack();
    int w = getWidth();
    int h = getHeight();
    Point loc = getLocationOnScreen();
    //Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension cs = customizerDialog.getSize();
    int x = loc.x + (w - cs.width) / 2;
    int y = loc.y + (h - cs.height) / 2;
    customizerDialog.setLocation(x, y);
    customizerDialog.setTitle(resources.getString("configTitle"));
    customizerDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e)
      {
        customizerDialog.dispose();
        customizerDialog = null;
      }
    });
    customizerDialog.setVisible(true);
  }

  /**
   * Exit the Application
   */
  private void exitForm(WindowEvent evt)
  {
    doExit();
  }

  /**
   * Save a file that is being edited.
   * @param     comp            The JTextComponent that is editing the file.
   * @param     checkOverwrite  If true, a confirmation dialog will be
   *                            displayed before overwriting an existing file.
   */
  private boolean saveFile(Component comp, File file, boolean checkOverwrite )
  {
    if (comp == null ) {
      return false;
    }
    tabPane.setSelectedComponent(comp);
    JTextComponent edit = com2text.get(comp);
    Document doc = edit.getDocument();

    if (checkOverwrite && file.exists()) {
      tabPane.setSelectedComponent(comp);
      int choice = JOptionPane.showOptionDialog(
        this, resources.getString("fileExists1") + file.getName() +
        resources.getString("fileExists2"),
        resources.getString("fileExists3"), JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,     // don't use a custom Icon
        new String[] {
          resources.getString("yes"),
          resources.getString("no"),
          resources.getString("cancel")
        },
        null      // no default selection
      );
      if (choice != 0 ) {
        return false;
      }
    }

    try {
      edit.write(new FileWriter(file ));
    } catch (IOException exc) {
      JOptionPane.showOptionDialog(
        this,
        resources.getString("cantWrite1") + file.getName() +
          resources.getString("cantWrite2"),
        resources.getString("error"),
        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
        null, new String[] {resources.getString("ok")},
        resources.getString("ok")
      );
      return false;
    }

    doc.putProperty(MODIFIED, new Boolean(false));
    doc.putProperty(CREATED, new Boolean(false));
    doc.putProperty(FILE, file);
    doc.addDocumentListener(new MarkingDocumentListener(comp));

    int index = tabPane.indexOfComponent(comp);
    String title = tabPane.getTitleAt(index);
    if (title.endsWith("*")) {
      title = title.substring(0, title.length() - 1);
    }
    tabPane.setTitleAt(index, title);

    return true;
  }

  /**
   * Save a file that is being edited.
   * @param     comp    The JTextComponent that is editing the file.
   */
  private boolean saveFile(Component comp)
  {
    if (comp == null ) {
      return false;
    }
    JTextComponent edit = com2text.get(comp);
    Document doc = edit.getDocument();
    File file = (File)doc.getProperty(FILE);
    boolean created = ((Boolean)doc.getProperty(CREATED)).booleanValue();

    return saveFile(comp, file, created);
  }

  /**
   * Save a file that is being edited. Before saving the file, a dialog will
   * be displayed, in which to specify the name under which the file will be
   * saved.
   * @param     comp    The JTextComponent that is editing the file.
   */
  private boolean saveAs(Component comp)
  {
    if(comp == null) {
      return false;
    }
    JTextComponent edit = com2text.get(comp);
    File file = (File)edit.getDocument().getProperty(FILE);

    fileChooser.setCurrentDirectory(file.getParentFile());
    fileChooser.setSelectedFile(file);
    KitInfo fileInfo = KitInfo.getKitInfoOrDefault(file);

    if (fileInfo != null) {
      fileChooser.setFileFilter( fileInfo );
    }

    // show the dialog, test the result
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      return saveFile(comp, fileChooser.getSelectedFile(), true);
    }else{
      return false; // Cancel was pressed - not saved
    }
  }

  /**
   * Open a file for editing.
   * @param     file    The file to edit.
   */
  public void openFile(File file)
  {
    KitInfo info = KitInfo.getKitInfoOrDefault(file);

    final JEditorPane pane = new JEditorPane(info.getType(), "");
    try {
      pane.read(new FileInputStream(file), file.getCanonicalPath());
    } catch (IOException exc) {
      JOptionPane.showOptionDialog(
        this,
        resources.getString("cantRead1") + file.getName() +
          resources.getString("cantRead2"),
        resources.getString("error"),
        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
        null, new String[] {resources.getString("ok")},
        resources.getString("ok")
      );
      return;
    }
    addEditorPane(pane, info.getIcon(), file, false);
  }

  /**
   * Performs the necessary cleanup before terminating the editor.
   */
  private void doExit()
  {
    boolean exit = true;
    while (tabPane.getComponentCount() > 0) {
      Component editor = tabPane.getComponentAt(0);
      if (checkClose(editor)) {
        int n = focusListeners.size();
        for (int i=0; i<n; i++) {
          editor.removeFocusListener(focusListeners.get(i));
        }
        tabPane.remove(editor);
        com2text.remove(editor);
        UndoManager um = com2um.get(editor);
        com2um.remove(editor);
        um.discardAllEdits();
      }else{
        //System.err.println("keeping");
        exit = false;
        break;
      }
    }
    if (exit) {
      System.exit(0);
    }
  }

  /**
   * This method is invoked before closing a file. If the file has been
   * modified, a dialog is presenting, given the opportunity to save the file
   * before closing it or to cancel the closing of the file.
   * @param     comp    The JTextComponent that is editing the file.
   * @return    True if the file should be closed, false if the closing of the
   *            file should be canceled.
   */
  private boolean checkClose(Component comp)
  {
    if (comp == null ) {
      return false;
    }
    JTextComponent edit = com2text.get(comp);
    Document doc = edit.getDocument();

    Object mod = doc.getProperty(MODIFIED);
    if ((mod == null) || !((Boolean)mod).booleanValue()) {
      return true;
    }

    tabPane.setSelectedComponent(comp);
    File file = (File)doc.getProperty(FILE);

    for (;;) {
      int choice = JOptionPane.showOptionDialog(
        this,
        resources.getString("fileModified1") + file.getName() +
          resources.getString("fileModified2"),
        resources.getString("fileModified3"), JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,     // don't use a custom Icon
        // use standard button titles
        new String[] {
          resources.getString("save"), resources.getString("saveAs"),
          resources.getString("discard"), resources.getString("cancel")
        },
        resources.getString("cancel")      //default selection
      );

      switch( choice ) {
        case JOptionPane.CLOSED_OPTION:
        case 4:
          return false; // Cancel or Esc pressed
        case 1:
          if(!saveAs(comp)) {
            continue;  // Ask for fileName, then save
          }
          return true;
        case 0:
          if (!saveFile(comp)) {
            continue; // else fall through
          }
        case 2:
          return true;  // Discard changes, close window
      }
      return false;
    }
  }

  /**
   * Adds a new tab to the editor.
   * @param     pane    An editor pane editing a new file.
   * @param     icon    The icon to appear on the tab.
   * @param     file    The file being edited by <code>pane</code>.
   * @param     created If true, then the file has just been created.
   *                    If false, the file was read from disk.
   */
  private void addEditorPane(JEditorPane pane, Icon icon, File file,
                             boolean created )
  {
    final JComponent c =
      (pane.getUI() instanceof BaseTextUI) ?
        Utilities.getEditorUI(pane).getExtComponent() :
        new JScrollPane(pane);
    Document doc = pane.getDocument();

    doc.addDocumentListener(new MarkingDocumentListener(c));
    doc.putProperty(FILE, file);
    doc.putProperty(CREATED, new Boolean(created));

    UndoManager um = new EditorUndoManager();
    com2um.put(c, um);

    doc.addUndoableEditListener(um);
    doc.putProperty(BaseDocument.UNDO_MANAGER_PROP, um);

    com2text.put(c, pane);
    com2blink.put(c, pane.getCaret().getBlinkRate());
    int n = focusListeners.size();
    for (int i=0; i<n; i++) {
      c.addFocusListener(focusListeners.get(i));
    }
    tabPane.addTab(file.getName(), icon, c, file.getAbsolutePath());
    tabPane.setSelectedComponent(c);
    revalidate();
    repaint();
    pane.requestFocus();
    updateEditUI(pane);
    updateUndoRedoUI(um);
    enableCloseButton(true);
    enableCloseItem(true);
    enableSaveButton(true);
    enableSaveItem(true);
    enableSaveAllItem(true);
    enableSaveAllButton(true);
    enableSaveAsItem(true);
    enableFindButton(true);
    enableFindItem(true);
    FindSupport fs = FindSupport.getFindSupport();
    String exp =
      (String)(fs.getFindProperties().get(SettingsNames.FIND_WHAT));
    if (exp != null) {
      enableFindNextButton(true);
      enableFindNextItem(true);
    }else{
      enableFindNextButton(false);
      enableFindNextItem(false);
    }
    enableReplaceButton(true);
    enableReplaceItem(true);
    enableGotoLineItem(true);
    enableSelectAllItem(true);
    enablePrintItem(true);
    enablePrintButton(true);
    updateUIForMultiplicity();
    pane.addCaretListener(this);
    setStatusBarVisible(pane, statusBarVisible);
  }

  /**
   * Creates a new file for editing.
   * @param     info    Describes which editor kit to use to edit the file.
   */
  private void createNewFile( KitInfo info )
  {
    final String fileName =
      ((++fileCounter == 0 ) ?
        "unnamed" :
        ("unnamed" + fileCounter)) + info.getDefaultExtension();
    final File file = new File(fileName).getAbsoluteFile();

    final JEditorPane pane = new JEditorPane(info.getType(), "");
    URL template = info.getTemplate();
    if (template != null) {
      try {
        pane.read(template.openStream(), file.getCanonicalPath());
      } catch (IOException e ) {
        JOptionPane.showOptionDialog(
          this, resources.getString("noTemplate"),
          resources.getString("error"),
          JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
          null, new String[] {resources.getString("ok")},
          resources.getString("ok")
        );
      }
    }
    addEditorPane(pane, info.getIcon(), file, true);
  }

  /**
   * Creates a new file for editing.
   * @param     type    Specifies what type of file to create. This should be
   *                    one of Editor.PLAIN_TEXT, Editor.JAVA,
   *                    Editor.JAVASCRIPT, or Editor.LOGO. Anything else is
   *                    equivalent to Editor.PLAIN_TEXT.
   */
  public void createNewFile(String type)
  {
    KitInfo kit = getKitInfo(type);
    createNewFile(kit);
  }

  /**
   * Creates a new plain text file for editing.
   */
  public void createNewFile()
  {
    createNewFile(PLAIN_TEXT);
  }

  /**
   * Returns the editor kit description associated with a given file type.
   * @param     type    The file type. This should be
   *                    one of Editor.PLAIN_TEXT, Editor.JAVA,
   *                    Editor.JAVASCRIPT, or Editor.LOGO. Anything else is
   *                    equivalent to Editor.PLAIN_TEXT.
   */
  private KitInfo getKitInfo(String type)
  {
    if ((type == null) ||
        !(type.equals(PLAIN_TEXT) || type.equals(JAVA) ||
          type.equals(LOGO) || type.equals(JAVASCRIPT))) {
      System.err.println(
        "*** Unknown type " + type + ". Using " + PLAIN_TEXT + " instead."
      );
      type = PLAIN_TEXT;
    }
    List l = KitInfo.getKitList();
    int n = l.size();
    KitInfo kit = null;
    for (int i=0; i<n; i++) {
      kit = (KitInfo)(l.get(i));
      if (type.equals(kit.getType())) {
        break;
      }
    }
    if (!type.equals(kit.getType())) {
      System.err.println(
        "*** Unknown type " + type + ". Using " + kit.getType() + " instead."
      );
    }
    return kit;
  }


  /**
   * Specifies whether the menu bar is visible.
   * @param     visible True if yes, false if no.
   */
  public void setMenuBarVisible(boolean visible)
  {
    if (visible != menuBarVisible) {
      menuBarVisible = visible;
      if (visible) {
        if (menuBar == null) {
          initMenuBar();
        }
        northPanel.add(menuBar, BorderLayout.NORTH);
      }else{
        northPanel.remove(menuBar);
      }
      revalidate();
    }
  }

  /**
   * Checks whether the menu bar is visible.
   * @return    True if yes, false if no.
   */
  public boolean isMenuBarVisible()
  {
    return menuBarVisible;
  }

  /**
   * Specifies whether the tool bar is visible.
   * @param     visible True if yes, false if no.
   */
  public void setToolBarVisible(boolean visible)
  {
    if (visible != toolBarVisible) {
      toolBarVisible = visible;
      if (visible) {
        if (toolBar == null) {
          initToolBar();
        }
        northPanel.add(toolBar, BorderLayout.SOUTH);
      }else{
        northPanel.remove(toolBar);
      }
      revalidate();
    }
  }

  /**
   * Checks whether the tool bar is visible.
   * @return    True if yes, false if no.
   */
  public boolean isToolBarVisible()
  {
    return toolBarVisible;
  }

  /**
   * Specifies whether the status bar is visible.
   * @param     visible True if yes, false if no.
   */
  public void setStatusBarVisible(boolean visible)
  {
    if (visible != statusBarVisible) {
      int nTabs = tabPane.getTabCount();
      for (int i=0; i<nTabs; i++) {
        JEditorPane edit = com2text.get(tabPane.getComponentAt(i));
        setStatusBarVisible(edit, visible);
      }
      statusBarVisible = visible;
    }
  }

  /**
   * Specifies whether the status bar of a particular editor pane is visible.
   * @param     edit    The editor pane for which to set the visibility of the
   *                    status bar.
   * @param     visible True if yes, false if no.
   */
  private void setStatusBarVisible(JEditorPane edit, boolean visible)
  {
    StatusBar sb = Utilities.getEditorUI(edit).getStatusBar();
    sb.setVisible(visible);
  }

  /**
   * Checks whether the status bar is visible.
   * @return    True if yes, false if no.
   */
  public boolean isStatusBarVisible()
  {
    return statusBarVisible;
  }

  /**
   * Returns a reference to the editor's tool bar.
   * @return    A reference to the editor's tool bar.
   */
  public JToolBar getToolBar()
  {
    return toolBar;
  }

  /**
   * Returns a reference to the status bar of the editor in the currently
   * selected tab.
   * @return    The requested status bar. Returns null if no file is being
   *            edited, 
   */
  public JPanel getStatusBar()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      StatusBar sb = Utilities.getEditorUI(edit).getStatusBar();
      return sb.getPanel();
    }else{
      return null;
    }
  }

  /**
   * Specifies whether the editor can edit multiple files. This method affects
   * the activation of those UI elements that can open a second file, thus
   * preventing the user from editing more than one file, which might lead to
   * confusion if the editor has been invoked programmatically to edit a
   * specific file.
   * @param     flag    If true, the editor will be able to edit multiple
   *                    files. If false, and the editor is currently editing
   *                    at most one file, then it will only edit single files.
   *                    I false and the editor is currently editing two or
   *                    more files, the setting will be ignored.
   */
  public void setMultipleFileEditor(boolean flag)
  {
    if (multiple != flag) {
      if (flag || (tabPane.getTabCount() < 2)) {
        multiple = flag;
        updateUIForMultiplicity();
      }
    }
  }

  /**
   * Activates and deactivates those UI elements that allow the editing of
   * multiple files.
   */
  private void updateUIForMultiplicity()
  {
    boolean enable;
    if (!multiple) {
      if (tabPane.getTabCount() > 0) {
        enable = false;
      }else{
        enable = true;
      }
    }else{
      enable = true;
    }
    enableOpenButton(enable);
    enableOpenItem(enable);
    enableNewButton(enable);
    enableNewMenu(enable);
  }

  /**
   * Checks whether the editor can edit multiple files.
   * @return    True if yes, false if no.
   */
  public boolean isMultipleFileEditor()
  {
    return multiple;
  }

  /**
   * Caret event handler; activates/deactivates cut. copy, paste, and
   * find next buttons and menu items.
   * @param     evt     The caret event to handle.
   */
  public void caretUpdate(CaretEvent evt)
  {
    JEditorPane edit = (JEditorPane)(evt.getSource());
    updateEditUI(edit);
    if (!findNextButtonEnabled) {
      FindSupport fs = FindSupport.getFindSupport();
      String exp =
        (String)(fs.getFindProperties().get(SettingsNames.FIND_WHAT));
      if (exp != null) {
        enableFindNextButton(true);
        enableFindNextItem(true);
      }
    }
  }

  /**
   * Tab pane change event handler; activates/deactivates cut. copy, and paste
   * buttons and menu items.
   * @param     evt     The change event to handle.
   */
  public void stateChanged(ChangeEvent evt)
  {
    Component c = tabPane.getSelectedComponent();
    JEditorPane edit = com2text.get(c);
    updateEditUI(edit);
    UndoManager um = com2um.get(c);
    updateUndoRedoUI(um);
  }

  /**
   * Updates the state of the edit and find UI elements.
   * @param     edit    The currently active editor pane.
   */
  private void updateEditUI(JEditorPane edit)
  {
    if (edit != null) {
      if (edit.getSelectedText() == null) {
        if (cutButtonEnabled) {
          enableCutButton(false);
          enableCutItem(false);
          enableCopyButton(false);
          enableCopyItem(false);
        }
        if (deleteItemEnabled) {
          enableDeleteItem(false);
        }
      }else{
        if (!cutButtonEnabled) {
          if (editable) {
            enableCutButton(true);
            enableCutItem(true);
          }
          enableCopyButton(true);
          enableCopyItem(true);
        }
        if (!deleteItemEnabled) {
          if (editable) {
            enableDeleteItem(true);
          }
        }
      }
      if (!pasteButtonEnabled) {
        enablePasteButton(true);
        enablePasteItem(true);
      }
    }
  }

  /**
   * Updates the state of the undo/redo UI elements.
   * @param     um      The undo manager on which to base the update.
   */
  private void updateUndoRedoUI(UndoManager um)
  {
    boolean undoState;
    boolean redoState;
    if (um != null) {
      undoState = um.canUndo();
      redoState = um.canRedo();
    }else{
      undoState = false;
      redoState = false;
    }
    enableUndoButton(undoState);
    enableUndoItem(undoState);
    enableRedoButton(redoState);
    enableRedoItem(redoState);
    undoRedoStatusEvent.undoState = undoState;
    undoRedoStatusEvent.redoState = redoState;
    synchronized (undoRedoStatusListeners) {
      int n = undoRedoStatusListeners.size();
      for (int i=0; i<n; i++) {
        UndoRedoStatusListener l = undoRedoStatusListeners.get(i);
        l.undoRedoStatusChanged(undoRedoStatusEvent);
      }
    }
  }

  /**
   * Add an undo/redo status event listener to the editor. Use this, e.g., if
   * you use your own tool bar, to keep the undo/redo buttons updated
   * correctly.
   * @param     l       The listener to add.
   */
  public void addUndoredoStatusListener(UndoRedoStatusListener l)
  {
    synchronized (undoRedoStatusListeners) {
      if (!undoRedoStatusListeners.contains(l)) {
        undoRedoStatusListeners.add(l);
      }
    }
  }

  /**
   * Remove an undo/redo status event listener from the editor.
   * Use this, e.g., if  you use your own tool bar, to keep the undo/redo
   * buttons updated correctly.
   * @param     l       The listener to remove.
   */
  public void removeUndoredoStatusListener(UndoRedoStatusListener l)
  {
    synchronized (undoRedoStatusListeners) {
      int ind = undoRedoStatusListeners.indexOf(l);
      if (ind >= 0) {
        undoRedoStatusListeners.remove(ind);
      }
    }
  }

  /**
   * Main entry point when the editor is run as an application.
   * @param     args    The names of the files to edit.
   */
  public static void main (String args[])
  {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }catch (Exception e) {
    }
    Editor editor = new Editor(true);
    editor.setMenuBarVisible(true);
    editor.setToolBarVisible(true);
    editor.setStatusBarVisible(true);
    JFrame f = new JFrame(editor.resources.getString("eSlateEd"));
    f.setIconImage(
      new
      ImageIcon(editor.getClass().getResource("res/eslateLogo.gif")).getImage()
    );
    f.setContentPane(editor);
    f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt)
      {
        JFrame fr = (JFrame)(evt.getWindow());
        Editor ed = (Editor)(fr.getContentPane());
        ed.exitForm(evt);
      }
    });
    f.pack();
    f.setLocation(150, 150);
    f.setVisible(true);

    for (int i=0; i<args.length; i++) {
      String fileName = args[i];
      editor.openFile(new File(fileName));
    }
  }

  // Variables declaration - do not modify
  private JMenuBar menuBar = null;
  private JToolBar toolBar = null;
  private JMenu fileMenu = null;
  private JMenu newMenu = null;
  private JMenuItem openItem = null;
  private JMenuItem closeItem = null;
  private JMenuItem saveItem = null;
  private JMenuItem saveAsItem = null;
  private JMenuItem saveAllItem = null;
  private JMenuItem exitItem = null;
  private JMenu editMenu = null;
  private JMenuItem undoItem = null;
  private JMenuItem redoItem = null;
  private JMenuItem cutItem = null;
  private JMenuItem copyItem = null;
  private JMenuItem pasteItem = null;
  private JMenuItem deleteItem = null;
  private JMenuItem selectAllItem = null;
  private JMenu searchMenu = null;
  private JMenuItem findItem = null;
  private JMenuItem replaceItem = null;
  private JMenuItem findNextItem = null;
  private JMenuItem gotoLineItem = null;
  private JMenu optionMenu = null;
  private JMenuItem configItem = null;
  private JMenuItem printItem = null;
  private JTabbedPane tabPane = null;

  private JPopupMenu newPopupMenu;
  private ToolButton newButton = null;
  private ToolButton openButton = null;
  private ToolButton closeButton = null;
  private ToolButton saveButton = null;
  private ToolButton saveAllButton = null;
  private ToolButton copyButton = null;
  private ToolButton pasteButton = null;
  private ToolButton cutButton = null;
  private ToolButton undoButton = null;
  private ToolButton redoButton = null;
  private ToolButton findButton = null;
  private ToolButton findNextButton = null;
  private ToolButton replaceButton = null;
  private ToolButton printButton = null;
  private JComboBox fonts = null;
  private JComboBox fontSizes = null;

  private boolean menuBarVisible = false;
  private boolean toolBarVisible = false;
  private boolean statusBarVisible = false;
  private JPanel northPanel;

  // End of variables declaration

  /**
   * Read the netbeans editor "settings" resource.
   * @exception MissingResourceException        Thrown if one of the required
   *                    resources is missing from the "settings" resource.
   */
  private void readSettings() throws MissingResourceException
  {
    File currentPath =
      new File(System.getProperty("user.dir")).getAbsoluteFile();
    fileChooser = new JFileChooser(currentPath);

    fileChooser.setFileView(new FileView() {
      // JDK1.2 compatibility fix
      public String getName(File f)
      {
        return null;
      }
      public String getDescription(File f)
      {
        return null;
      }
      public String getTypeDescription(File f)
      {
        return null;
      }
      public Boolean isTraversable(File f)
      {
        return null;
      }
      public Icon getIcon(File f)
      {
        if (f.isDirectory()) {
          return null;
        }
        KitInfo ki = KitInfo.getKitInfoForFile(f);
        return ki == null ? null : ki.getIcon();
      }
    });

    String kits = settings.getString("InstalledEditors");
    String defaultKit = settings.getString("DefaultEditor");

    StringTokenizer st = new StringTokenizer(kits, ",");
    while (st.hasMoreTokens()) {
      String kitName = st.nextToken();
      // At the first, we have to read ALL info about kit
      String contentType = settings.getString(kitName + "_ContentType");
      String extList = settings.getString(kitName + "_ExtensionList");
      String menuTitle = settings.getString(kitName + "_NewMenuTitle");
      //char menuMnemonic =
      //  settings.getString(kitName + "_NewMenuMnemonic" ).charAt(0);
      String templateURL = settings.getString(kitName + "_Template");
      String iconName = settings.getString(kitName + "_Icon");
      String filterTitle = settings.getString(kitName + "_FileFilterTitle");
      String kit = settings.getString(kitName + "_KitClass");

      // At the second, we surely need an instance of kitClass
      Class kitClass;
      try {
        kitClass = Class.forName(kit);
      } catch (ClassNotFoundException exc) { // we really need it
        throw new MissingResourceException(
          "Missing class", kit, "KitClass"
        );
      }

      // At the third, it is nice to have icon although we could live
      // without one
      Icon icon = null;
      ClassLoader loader = kitClass.getClassLoader();
      if (loader == null ) {
        loader = ClassLoader.getSystemClassLoader();
      }
      URL resource = loader.getResource(iconName);
      if (resource == null ) {
        resource = ClassLoader.getSystemResource(iconName);
      }
      if (resource != null) {
        icon = new ImageIcon(resource);
      }

      // At the fourth, try to get URL for template
      URL template = loader.getResource(templateURL);
      if (resource == null) {
        template = ClassLoader.getSystemResource(templateURL);
      }

      // Finally, convert the list of extensions to, ehm, List :-)
      List<String> l = new ArrayList<String>(5);
      StringTokenizer extST = new StringTokenizer(extList, ",");
      while (extST.hasMoreTokens()) {
        l.add( extST.nextToken());
      }

      // Actually create the KitInfo from provided informations
      KitInfo ki = new KitInfo(
        contentType, l, template, icon, filterTitle, kitClass, loader,
        defaultKit.equals(kitName)
      );

      // Make line numbers visible for this editor kit.
      Settings.setValue(
        kitClass, SettingsNames.LINE_NUMBER_VISIBLE, Boolean.TRUE
      );

/*
      // Make the MenuItem for it
      JMenuItem item = new JMenuItem(menuTitle, icon);
      //item.setMnemonic(menuMnemonic);
      NewFileActionListener nfal = new NewFileActionListener(ki);
      item.addActionListener(nfal);
      newMenu.add(item);

      // Make a similar item for the "new" button's popup menu.
      item = new JMenuItem(menuTitle, icon);
      item.addActionListener(nfal);
      newPopupMenu.add(item);
*/
      // Cache enough info to enable the creation of the tool and menu bars
      // when requested.
      newInfo.add(ki);
      newInfo.add(menuTitle);

      // Register a FileFilter for given type of file
      fileChooser.addChoosableFileFilter(ki);
    }

    // Finally, add fileFilter that would recognize files of all kits
    fileChooser.addChoosableFileFilter(new FileFilter() {
      public String getDescription()
      {
        return resources.getString("allRecognized");
      }

      public boolean accept( File f )
      {
        return f.isDirectory() || KitInfo.getKitInfoForFile(f) != null;
      }
    });

    if (KitInfo.getDefault() == null) {
      throw new MissingResourceException(
        "Missing default kit definition", defaultKit, "DefaultEditor"
      );
    }
  }

  /**
   * Returns the text of the file being edited in the currently selected tab.
   * @return    The requested text.
   */
  public String getText()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getText();
    }else{
      return null;
    }
  }

  /**
   * Sets the text of the file being edited in the currently selected tab.
   * @param     text    The text to use.
   */
  public void setText(String text)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.setText(text);
      edit.setCaretPosition(0);
    }
  }

  /**
   * Clears the undo/redo history of the file being edited in the currently
   * selected tab.
   */
  public void clearHistory()
  {
    Component c = tabPane.getSelectedComponent();
    UndoManager um = com2um.get(c);
    um.discardAllEdits();
  }

  /**
   * Returns the selected text of the file being edited in the currently
   * selected tab.
   * @return    The requested text.
   */
  public String getSelectedText()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getSelectedText();
    }else{
      return null;
    }
  }

  /**
   * Replaces the selected text of the file being edited in the currently
   * selected tab.
   * @param     text    The text with which to replace the selection.
   */
  public void replaceSelection(String text)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.replaceSelection(text);
    }
  }

  /**
   * Replaces a text range of the file being edited in the currently
   * selected tab.
   * @param     text    The text with which to replace the selection.
   * @param     start   The start position of the text to replace.
   * @param     end     The end position of the text to replace.
   */
  public void replaceRange(String text, int start, int end)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.setSelectionStart(start);
      edit.setSelectionEnd(end);
      edit.replaceSelection(text);
    }
  }

  /**
   * Inserts a piece of text in the file being edited in the currently
   * selected tab.
   * @param     text    The text to insert.
   * @param     pos     The position at which to insert the text
   * @exception BadLocationException    Thrown when the given insert position
   *                    is not a valid position within the document.
   */
  public void insert(String text, int pos) throws BadLocationException
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.getDocument().insertString(pos, text, null);
    }
  }

  /**
   * Returns the start position of the selected text in the file being
   * edited in the currently selected tab.
   * @return    The selected text's start position. Returns 0 for an
   *            empty document, or the value of dot if no selection.
   */
  public int getSelectionStart()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getSelectionStart();
    }else{
      return 0;
    }
  }

  /**
   * Returns the end position of the selected text in the file being
   * edited in the currently selected tab.
   * @return    The selected text's end position. Returns 0 if the
   *            document is empty, or the value of dot if there is no
   *            selection.
   */
  public int getSelectionEnd()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getSelectionEnd();
    }else{
      return 0;
    }
  }

  /**
   * Sets the selection start of the file being edited in the currently
   * selected tab to the specified position. The new starting point is
   * constrained to be before or at the current selection end.
   * @param     pos     The new selection start.
   */
  public void setSelectionStart(int pos)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.setSelectionStart(pos);
    }
  }

  /**
   * Sets the selection end of the file being edited in the currently
   * selected tab to the specified position. The new end point is constrained
   * to be at or after the current selection start.
   * @param     pos     The new end start.
   */
  public void setSelectionEnd(int pos)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.setSelectionEnd(pos);
    }
  }

  /**
   * Selects the text found between the specified start and end locations
   * in the currently selected tab.
   * This call is provided for backward compatibility. It is routed to a call
   * to setCaretPosition followed by a call to moveCaretPostion. The preferred
   * way to manage selection is by calling those methods directly.
   */
  public void select(int selectionStart, int selectionEnd)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.select(selectionStart, selectionEnd);
    }
  }

  /**
   * Returns the position of the text insertion caret for the file being
   * edited in the currently selected tab.
   * @return    The requested position.
   */
  public int getCaretPosition()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getCaretPosition();
    }else{
      return 0;
    }
  }

  /**
   * Sets the position of the text insertion caret for the file being
   * edited in the currently selected tab. Note
   * that the caret tracks change, so this may move if the underlying text of
   * the component is changed. If the document is null, does nothing.
   * @param     pos     The new caret position.
   */
  public void setCaretPosition(int pos)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.setCaretPosition(pos);
    }
  }

  /**
   * Moves the caret of the file being edited in the currently selected tab
   * to a new position, leaving behind a mark defined by the
   * last time setCaretPosition was called. This forms a selection.
   * @param     pos     The new caret position.
   */
  public void moveCaretPosition(int pos)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.moveCaretPosition(pos);
    }
  }

  /**
   * Sets the current color used to render the caret in the currently selected
   * tab. Setting to nulleffectively restores the default color. Setting the
   * color results in a PropertyChange event ("caretColor") being fired.
   * @param     color   The new caret color.
   */
  public void setCaretColor(Color color)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.setCaretColor(color);
    }
  }

  /**
   * Sets the position of the text insertion caret for the file being
   * edited in the currently selected tab to the beginning of the specified
   * line offset. If the document is null, does nothing.
   * @param     line    Line offset starting from 0.
   */
  public void gotoLine(int line)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
/*
      int pos = Utilities.getRowStartFromLineOffset(
        ((BaseDocument)edit.getDocument()), line
      );
      edit.setCaretPosition(pos);
*/
      // Follow the code from the "go to line" dialog as close as possible;
      // taking shortcuts does not seem to work, and even this code requires
      // Java 1.4 to work.
      BaseDocument doc = Utilities.getDocument(edit);
      if (doc != null) {
        int pos = Utilities.getRowStartFromLineOffset(doc, line);
        BaseKit kit = Utilities.getKit(edit);
        if (kit != null) {
          Action a = kit.getActionByName(ExtKit.gotoAction);
          if (a instanceof ExtKit.GotoAction) {
            pos = Utilities.getRowStartFromLineOffset(doc, line);
          }
        }
        if (pos != -1) {
          edit.getCaret().setDot(pos);
        }else{
          edit.getToolkit().beep();
        }
      }
    }
  }

//  /**
//   * Returns the view coordinates that appear in the upper left hand corner of
//   * the viewport of the file being edited in the currently selected tab.
//   * @return    A <code>Point</code> object giving the upper left coordinates.
//   *            If no file is being edited, or if, for some reason, the
//   *            viewport cannot be located, <code>null</code> is returned.
//   */
//  private Point getViewPosition()
//  {
//    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
//    if (edit != null) {
//      JViewport v =
//        (JViewport)(SwingUtilities.getAncestorOfClass(JViewport.class, edit));
//      if (v != null) {
//        return v.getViewPosition();
//      }
//    }
//    return null;
//  }
//
//  /**
//   * Sets the view coordinates that appear in the upper left hand corner of the
//   * viewport of the file being edited in the currently selected tab.
//   * @param     p       A <code>Point</code> object giving the upper left
//   *                    coordinates.
//   * @return    The requested coordinates. If no file is being edited, or if,
//   *            for some reason, the viewport cannot be located,
//   *            <code>null</code> is returned.
//   */
//  private void setViewPosition(Point p)
//  {
//    if (p != null) {
//      JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
//      if (edit != null) {
//        JViewport v =
//          (JViewport)(SwingUtilities.getAncestorOfClass(JViewport.class, edit));
//        if (v != null) {
//          v.setViewPosition(p);
//        }
//      }
//    }
//  }

  /**
   * Adds a caret listener for notification of any changes to the caret
   * for the file being edited in the currently selected tab.
   * @param     l       The listener to add.
   */
  public void addCaretListener(CaretListener l)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.addCaretListener(l);
    }
  }

  /**
   * Removes a caret listener from for the file being edited in the
   * currently selected tab.
   * @param     l       The listener to remove.
   */
  public void removeCaretListener(CaretListener l)
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.removeCaretListener(l);
    }
  }

  /**
   * Transfers the currently selected range in the file being
   * edited in the currently selected tab to
   * the system clipboard, removing the contents from the model. The current
   * selection is reset. Does nothing for null selections.
   */
  public void cut()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.cut();
    }
  }

  /**
   * Transfers the currently selected range in the file being
   * edited in the currently selected tab to the system clipboard, leaving the
   * contents in the text model. The current selection is remains intact. Does
   * nothing for null selections.
   */
  public void copy()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.copy();
    }
  }

  /**
   * Transfers the contents of the system clipboard into the file being
   * edited in the currently selected tab.
   * If there is a selection in the associated view, it is replaced with the
   * contents of the clipboard. If there is no selection, the clipboard
   * contents are inserted in front of the current insert position in the
   * associated view. If the clipboard is empty, does nothing.
   */
  public void paste()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.paste();
    }
  }

  /**
   * Returns the line offset (line number - 1) for some position in the file
   * being edited in the currently selected tab.
   * @param     pos     The position in the file.
   * @return    The requested line offset (line number - 1).
   * @exception BadLocationException    Thrown if an incorrect offset is
   *                    specified or if no file is being edited.
   * @deprecated        As of version 1.0.1, replaced by
   *                    {@link getLineOfOffset(int)}
   */
  public int getLineOffset(int pos) throws BadLocationException
  {
    return getLineOfOffset(pos);
  }

  /**
   * Returns the line offset (line number - 1) for some position in the file
   * being edited in the currently selected tab.
   * @param     pos     The position in the file.
   * @return    The requested line offset (line number - 1).
   * @exception BadLocationException    Thrown if an incorrect offset is
   *                    specified or if no file is being edited.
   */
  public int getLineOfOffset(int pos) throws BadLocationException
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return Utilities.getLineOffset((BaseDocument)(edit.getDocument()), pos);
    }else{
      throw new BadLocationException(resources.getString("noFileEdited"), pos);
    }
  }

  /**
   * Returns the start offset of a line in the file being edited in the
   * currently selected tab.
   * @param     line    Line offset starting from 0.
   * @return    Start position of the line
   * @exception BadLocationException    Thrown if <code>line</code> was
   *                    invalid or if no file is being edited.
   */
  public int getLineStartOffset(int line) throws BadLocationException
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      int offset = Utilities.getRowStartFromLineOffset(
        ((BaseDocument)edit.getDocument()), line
      );
      if (offset >= 0) {
        return offset;
      }else{
        throw new BadLocationException(resources.getString("noSuchLine"), line);
      }
    }else{
      throw new BadLocationException(resources.getString("noFileEdited"), line);
    }
  }

  /**
   * Returns the end offset of a line in the file being edited in the
   * currently selected tab.
   * @param     line    Line offset starting from 0.
   * @return    End position of the line
   * @exception BadLocationException    Thrown if <code>line</code> was
   *                    invalid or if no file is being edited.
   */
  public int getLineEndOffset(int line) throws BadLocationException
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      int offset = Utilities.getRowStartFromLineOffset(
        ((BaseDocument)edit.getDocument()), line
      );
      offset = Utilities.getRowEnd(
        ((BaseDocument)edit.getDocument()), offset
      );
      if (offset >= 0) {
        return offset;
      }else{
        throw new BadLocationException(resources.getString("noSuchLine"), line);
      }
    }else{
      throw new BadLocationException(resources.getString("noFileEdited"), line);
    }
  }

  /**
   * Returns the number of lines in the file being edited in the currently
   * selected tab.
   */
  public int getLineCount()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return Utilities.getRowCount((BaseDocument)(edit.getDocument()));
    }else{
      return 0;
    }
  }

  /**
   * Returns the model associated with the editor that is editing the file in
   * the currently selected tab.
   * @return    The requested document. Returns null if no file is being
   *            edited.
   */
  public Document getDocument()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getDocument();
    }else{
      return null;
    }
  }

  /**
   * Specifies whether the editor that is editing the file in the currently
   * selected tab should be editable.
   * @param     editable        True if the editor should be editable, false
   *                            otherwise.
   */
  public void setEditable(boolean editable)
  {
    Component c = tabPane.getSelectedComponent();
    JEditorPane edit = com2text.get(c);
    if (edit != null) {
      edit.setEditable(editable);
      if (editable) {
        updateEditUI(edit);
      }else{
        enableCutButton(false);
        enableCutItem(false);
        enableDeleteItem(false);
      }
      this.editable = editable;
      BaseCaret caret = (BaseCaret)(edit.getCaret());
      // Hide the caret for empty, non-editable files. Caret.setVisible(false)
      // does not work for some reason, but Caret.setBlinkRate(0) does.
      if (!editable && edit.getText().equals("")) {
        caret.setBlinkRate(0);
      }else{
        int rate = com2blink.get(c);
        caret.setBlinkRate(rate);
      }
    }
  }

  /**
   * Returns the caret of the editor that is editing the file in
   * the currently selected tab.
   * @return    The requested caret. Returns null if no file is being
   *            edited.
   */
  public Caret getCaret()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      return edit.getCaret();
    }else{
      return null;
    }
  }

  /**
   * Changes the title of the currently selected tab to something other than
   * the name of the file.
   * @param     title   The new title of the tab.
   */
  public void setTitle(String title)
  {
    int index = tabPane.getSelectedIndex();
    if (index >= 0) {
      String oldTitle = tabPane.getTitleAt(index);
      // If old title was marked with an asterisk, meaning that the file had
      // been modified, do the same with the new title.
      if (oldTitle.endsWith("*")) {
        title = title + "*";
      }
      tabPane.setTitleAt(index, title);
    }
  }

  /**
   * Changes the con of the currently selected tab to a specified icon.
   * @param     icon    The new icon of the tab.
   */
  public void setTitleIcon(Icon icon)
  {
    int index = tabPane.getSelectedIndex();
    if (index >= 0) {
      tabPane.setIconAt(index, icon);
    }
  }

  /**
   * Returns the title of the currently selected tab.
   * @return    The requested title. Returns null if no file is being edited.
   */
  public String getTitle()
  {
    int index = tabPane.getSelectedIndex();
    if (index >= 0) {
      return tabPane.getTitleAt(index);
    }else{
      return null;
    }
  }

  /**
   * Transfers the focus to the editor in the currently selected tab.
   */
  public void requestFocus()
  {
    JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
    if (edit != null) {
      edit.requestFocus();
    }else{
      super.requestFocus();
    }
  }

  /**
   * Adds a focus listener to the component. The listener will be actually
   * added to each of the editor panes displayed in the component.
   * @param     l       The listener to add.
   */
  public void addFocusListener(FocusListener l)
  {
    if (!initted) {
      return;
    }
    synchronized(focusListeners) {
      if (!focusListeners.contains(l)) {
        focusListeners.add(l);
      }
      int nTabs = tabPane.getTabCount();
      for (int i=0; i<nTabs; i++) {
        JEditorPane edit = com2text.get(tabPane.getComponentAt(i));
        edit.addFocusListener(l);
      }
    }
  }

  /**
   * Removes a focus listener from the component. The listener will be actually
   * removed from each of the editor panes displayed in the component.
   * @param     l       The listener to add.
   */
  public void removeFocusListener(FocusListener l)
  {
    synchronized(focusListeners) {
      if (focusListeners.contains(l)) {
        focusListeners.remove(l);
      }
      int nTabs = tabPane.getTabCount();
      for (int i=0; i<nTabs; i++) {
        JEditorPane edit = com2text.get(tabPane.getComponentAt(i));
        edit.removeFocusListener(l);
      }
    }
  }

  /**
   * Invoked when the component's look and feel is changed.
   */
  public void updateUI()
  {
    super.updateUI();
    if (customizerDialog != null) {
      SwingUtilities.updateComponentTreeUI(customizerDialog);
      customizerDialog.pack();
    }
    findDialogSupport = null;
    gotoDialogSupport = null;
    try {
      FontOptions.updateStatusBarFromUI();
    } catch (NullPointerException npe) {
    }
  }

  /**
   * Save the state of the component.
   * @param     out     The stream in which to save the state.
   * @exception IOException     Thrown when something goes wrong.
   */
  public void writeExternal(ObjectOutput out) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(saveFormatVersion, 5);

    StorageStructure coloringMap = getColorSettings();
    map.put(COLORING_MAP, coloringMap);
    map.put(FONT_NAME, fonts.getSelectedItem());
    map.put(FONT_SIZE, fontSizes.getSelectedItem());
    map.put(MULTIPLE_FILE_EDITOR, multiple);
    out.writeObject(map);
  }

  /**
   * Returns all the color settings for all supported editor kits.
   * @return    A StorageStructure containing the requested settings.
   */
  public StorageStructure getColorSettings()
  {
    ESlateFieldMap2 coloringMap = new ESlateFieldMap2(saveFormatVersion);
    String kits = settings.getString("InstalledEditors");
    StringTokenizer st = new StringTokenizer(kits, ",");
    while (st.hasMoreTokens()) {
      String kit = st.nextToken() + "_KitClass";
      Class kitClass;
      try {
        kitClass = Class.forName(settings.getString(kit));
      } catch (ClassNotFoundException e) {
        System.err.println("*** Class not found: " + kit);
        continue;
      }
      ESlateFieldMap2 kitMap = new ESlateFieldMap2(saveFormatVersion);
      String[] cNames = FontOptions.getColoringNames(kitClass);
      for (int i=0; i<cNames.length; i++) {
        FontAndColor fac = FontOptions.getFontAndColor(kitClass, cNames[i]);
        kitMap.put(cNames[i], fac);
      }
      coloringMap.put(kit, kitMap);
    }
    return coloringMap;
  }

  /**
   * Load the state of the component.
   * @param     in      The stream from which to load the state.
   * @exception IOException     Thrown when something goes wrong.
   */
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(in.readObject());
    StorageStructure coloringMap = (StorageStructure)(map.get(COLORING_MAP));
    setColorSettings(coloringMap);
    Object o = map.get(FONT_NAME);
    if (o != null) {
      ignoreEvents = true;
      fonts.setSelectedItem(o);
      ignoreEvents = false;
    }
    o = map.get(FONT_SIZE);
    ignoreEvents = true;
    fontSizes.setSelectedItem(o);
    ignoreEvents = false;
    multiple = map.get(MULTIPLE_FILE_EDITOR, true);
    updateUIForMultiplicity();
  }

  /**
   * Sets all the color settings for all supported editor kits.
   * @param     map     A StorageStructure containing the requested settings.
   */
  public void setColorSettings(StorageStructure map)
  {
    String kits = settings.getString("InstalledEditors");
    StringTokenizer st = new StringTokenizer(kits, ",");
    while (st.hasMoreTokens()) {
      String kit = st.nextToken() + "_KitClass";
      Class kitClass;
      try {
        kitClass = Class.forName(settings.getString(kit));
      } catch (ClassNotFoundException e) {
        System.err.println("*** Class not found: " + kit);
        continue;
      }
      StorageStructure kitMap = (StorageStructure)(map.get(kit));
      String[] cNames = FontOptions.getColoringNames(kitClass);
      for (int i=0; i<cNames.length; i++) {
        FontAndColor fac = (FontAndColor)(kitMap.get(cNames[i]));
        FontOptions.setFontAndColor(kitClass, cNames[i], fac);
      }
    }
  }

  /**
   * Returns the global font size used by the editor.
   * @return    The requested font size. This number may be meaningless if
   *            individual fonts have been specified by the user for various
   *            kinds of text.
   */
  public int getFontSize()
  {
    return fontSize;
  }

  /**
   * Sets the global font size used by the editor.
   * @param     size    The new font size.
   */
  public void setFontSize(int size)
  {
    if (size > 0) {
      FontOptions.setFontSize(size);
      fontSize = size;
      if (fontSizes != null) {
        ignoreEvents = true;
        fontSizes.setSelectedItem(new Integer(fontSize));
        ignoreEvents = false;
      }
    }
  }

  /**
   * Sets the global font size used by a particular editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     size            The new font size.
   */
  public void setFontSize(Class kitClass, int size)
  {
    if (size > 0) {
      FontOptions.setFontSize(kitClass, size);
    }
  }

  /**
   * Returns the name of the global font used by the editor.
   * @return    The requested font name. This font may be meaningless if
   *            individual fonts have been specified by the user for various
   *            kinds of text.
   */
  public String getFontName()
  {
    if (fonts != null) {
      return (String)(fonts.getSelectedItem());
    }else{
      return defaultFont;
    }
  }

  /**
   * Sets the name of the global font used by the editor.
   * @param     font    The new font name.
   */
  public void setFontName(String font)
  {
    FontOptions.setFont(font);
    if (fonts != null) {
      ignoreEvents = true;
      fonts.setSelectedItem(font);
      ignoreEvents = false;
    }
  }

  /**
   * Sets the name of the global font used by a particular editor kit.
   * @param     kitClass        The class of the editor kit.
   * @param     font            The new font name.
   */
  public void setFontName(Class kitClass, String font)
  {
    FontOptions.setFont(kitClass, font);
  }

  /**
   * Returns a list of the names of all available fonts.
   * @return    A Vector containing the requested names.
   */
  public static Vector getAvailableFontNames()
  {
    return KitCustomizer.fontNames;
  }

  /**
   * Enable the "new" menu.
   * @param     enable  True to enable, false to disable.
   */
  private void enableNewMenu(boolean enable)
  {
    //newMenuEnabled = enable;
    if (menuBar != null) {
      newMenu.setEnabled(enable);
    }
  }

  /**
   * Enable the "open" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableOpenItem(boolean enable)
  {
    //openItemEnabled = enable;
    if (menuBar != null) {
      openItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "close" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableCloseItem(boolean enable)
  {
    //closeItemEnabled = enable;
    if (menuBar != null) {
      closeItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "save" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableSaveItem(boolean enable)
  {
    //saveItemEnabled = enable;
    if (menuBar != null) {
      saveItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "save as" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableSaveAsItem(boolean enable)
  {
    //saveAsItemEnabled = enable;
    if (menuBar != null) {
      saveAsItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "save all" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableSaveAllItem(boolean enable)
  {
    //saveAllItemEnabled = enable;
    if (menuBar != null) {
      saveAllItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "print" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enablePrintItem(boolean enable)
  {
    //printItemEnabled = enable;
    if (menuBar != null) {
      printItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "undo" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableUndoItem(boolean enable)
  {
    //undoItemEnabled = enable;
    if (menuBar != null) {
      undoItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "redo" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableRedoItem(boolean enable)
  {
    //redoItemEnabled = enable;
    if (menuBar != null) {
      redoItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "cut" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableCutItem(boolean enable)
  {
    //cutItemEnabled = enable;
    if (menuBar != null) {
      cutItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "copy" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableCopyItem(boolean enable)
  {
    //copyItemEnabled = enable;
    if (menuBar != null) {
      copyItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "paste" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enablePasteItem(boolean enable)
  {
    //pasteItemEnabled = enable;
    if (menuBar != null) {
      pasteItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "delete" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableDeleteItem(boolean enable)
  {
    deleteItemEnabled = enable;
    if (menuBar != null) {
      deleteItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "selecet all" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableSelectAllItem(boolean enable)
  {
    //selectAllItemEnabled = enable;
    if (menuBar != null) {
      selectAllItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "find" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableFindItem(boolean enable)
  {
    //findItemEnabled = enable;
    if (menuBar != null) {
      findItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "replace" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableReplaceItem(boolean enable)
  {
    //replaceItemEnabled = enable;
    if (menuBar != null) {
      replaceItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "find next" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableFindNextItem(boolean enable)
  {
    //findNextItemEnabled = enable;
    if (menuBar != null) {
      findNextItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "go to line" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableGotoLineItem(boolean enable)
  {
    //gotoLineItemEnabled = enable;
    if (menuBar != null) {
      gotoLineItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "configure" menu item.
   * @param     enable  True to enable, false to disable.
   */
  private void enableConfigItem(boolean enable)
  {
    //configItemEnabled = enable;
    if (menuBar != null) {
      configItem.setEnabled(enable);
    }
  }

  /**
   * Enable the "new" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableNewButton(boolean enable)
  {
    //newButtonEnabled = enable;
    if (toolBar != null) {
      newButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "open" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableOpenButton(boolean enable)
  {
    //openButtonEnabled = enable;
    if (toolBar != null) {
      openButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "close" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableCloseButton(boolean enable)
  {
    //closeButtonEnabled = enable;
    if (toolBar != null) {
      closeButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "save" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableSaveButton(boolean enable)
  {
    //saveButtonEnabled = enable;
    if (toolBar != null) {
      saveButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "save as" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableSaveAllButton(boolean enable)
  {
    //saveAllButtonEnabled = enable;
    if (toolBar != null) {
      saveAllButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "print" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enablePrintButton(boolean enable)
  {
    //printButtonEnabled = enable;
    if (toolBar != null) {
      printButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "copy" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableCopyButton(boolean enable)
  {
    //copyButtonEnabled = enable;
    if (toolBar != null) {
      copyButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "paste" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enablePasteButton(boolean enable)
  {
    pasteButtonEnabled = enable;
    if (toolBar != null) {
      pasteButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "cut" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableCutButton(boolean enable)
  {
    cutButtonEnabled = enable;
    if (toolBar != null) {
      cutButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "undo" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableUndoButton(boolean enable)
  {
    //undoButtonEnabled = enable;
    if (toolBar != null) {
      undoButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "redo" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableRedoButton(boolean enable)
  {
    //redoButtonEnabled = enable;
    if (toolBar != null) {
      redoButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "find" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableFindButton(boolean enable)
  {
    //findButtonEnabled = enable;
    if (toolBar != null) {
      findButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "find next" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableFindNextButton(boolean enable)
  {
    findNextButtonEnabled = enable;
    if (toolBar != null) {
      findNextButton.setEnabled(enable);
    }
  }

  /**
   * Enable the "replace" button.
   * @param     enable  True to enable, false to disable.
   */
  private void enableReplaceButton(boolean enable)
  {
    //replaceButtonEnabled = enable;
    if (toolBar != null) {
      replaceButton.setEnabled(enable);
    }
  }

//  /**
//   * Enables or diables menu items according to the state of the
//   * corresponding booleans.
//   */
//  private void enableDisableMenuItems()
//  {
//    if (menuBar != null) {
//      newMenu.setEnabled(newMenuEnabled);
//      closeItem.setEnabled(closeItemEnabled);
//      saveItem.setEnabled(saveItemEnabled);
//      saveAsItem.setEnabled(saveAsItemEnabled);
//      saveAllItem.setEnabled(saveAllItemEnabled);
//      undoItem.setEnabled(undoItemEnabled);
//      redoItem.setEnabled(redoItemEnabled);
//      cutItem.setEnabled(cutItemEnabled);
//      copyItem.setEnabled(copyItemEnabled);
//      pasteItem.setEnabled(pasteItemEnabled);
//      deleteItem.setEnabled(deleteItemEnabled);
//      selectAllItem.setEnabled(selectAllItemEnabled);
//      findItem.setEnabled(findItemEnabled);
//      replaceItem.setEnabled(replaceItemEnabled);
//      findNextItem.setEnabled(findNextItemEnabled);
//      gotoLineItem.setEnabled(gotoLineItemEnabled);
//      configItem.setEnabled(configItemEnabled);
//      printItem.setEnabled(printItemEnabled);
//    }
//  }
//
//  /**
//   * Enables or diables toolbar button according to the state of the
//   * corresponding booleans.
//   */
//  private void enableDisableButtons()
//  {
//    if (toolBar != null) {
//      newButton.setEnabled(newButtonEnabled);
//      closeButton.setEnabled(closeButtonEnabled);
//      saveButton.setEnabled(saveButtonEnabled);
//      saveAllButton.setEnabled(saveAllButtonEnabled);
//      copyButton.setEnabled(copyButtonEnabled);
//      pasteButton.setEnabled(pasteButtonEnabled);
//      cutButton.setEnabled(cutButtonEnabled);
//      undoButton.setEnabled(undoButtonEnabled);
//      redoButton.setEnabled(redoButtonEnabled);
//      findButton.setEnabled(findButtonEnabled);
//      findNextButton.setEnabled(findNextButtonEnabled);
//      replaceButton.setEnabled(replaceButtonEnabled);
//      printButton.setEnabled(printButtonEnabled);
//    }
//  }

  /**
   * Returns an instance of gr.cti.eslate.editor.FindDialogSupport, creating
   * it when invoked for the first time.
   * @return    The requested instance.
   */
  private gr.cti.eslate.editor.FindDialogSupport getFindDialogSupport()
  {
    if (findDialogSupport == null) {
      Frame f = (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, this));
      findDialogSupport =
        new gr.cti.eslate.editor.FindDialogSupport(f, tabPane);
    }
    return findDialogSupport;
  }

  /**
   * Returns an instance of gr.cti.eslate.editor.GotoDialogSupport, creating
   * it when invoked for the first time.
   * @return    The requested instance.
   */
  private gr.cti.eslate.editor.GotoDialogSupport getGotoDialogSupport()
  {
    if (gotoDialogSupport == null) {
      Frame f = (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, this));
      gotoDialogSupport =
        new gr.cti.eslate.editor.GotoDialogSupport(f, tabPane);
    }
    return gotoDialogSupport;
  }

  /**
   * Returns the editor pane in the currently selected tab.
   * @return    The requested editor pane. If no file is being edited, this
   *            method returns null.
   */
  public JEditorPane getSelectedComponent()
  {
    if (tabPane.getTabCount() > 0) {
      JEditorPane edit = com2text.get(tabPane.getSelectedComponent());
      return edit;
    }else{
      return null;
    }
  }

  /**
   * This class describes the editor kits that are used by the editor.
   */
  private static final class KitInfo extends FileFilter
  {
    private static List<KitInfo> kits = new ArrayList<KitInfo>();
    private static KitInfo defaultKitInfo;

    /**
     * Returns a list of the descriptions of all available editor kits.
     * @return  The requested list.
     */
    public static List getKitList()
    {
      return new ArrayList<KitInfo>(kits);
    }

    /**
     * Returns the description of the default editor kit.
     * @return  The requested description.
     */
    public static KitInfo getDefault()
    {
      return defaultKitInfo;
    }

    /**
     * Returns the description of the editor kit corresponding to a given
     * file.
     * @param   f       The file.
     * @return  The requested description. If no editor kit can be found for
     * the given file, the description of the default editor kit is returned,
     * instead.
     */
    public static KitInfo getKitInfoOrDefault(File f)
    {
      KitInfo ki = getKitInfoForFile(f);
      return ki == null ? defaultKitInfo : ki;
    }

    /**
     * Returns the description of the editor kit corresponding to a given
     * file.
     * @param   f       The file.
     * @return  The requested description. If no editor kit can be found for
     * the given file, null is returned, instead.
     */
    public static KitInfo getKitInfoForFile(File f)
    {
      for (int i=0; i<kits.size(); i++) {
        if (kits.get(i).accept(f)) {
          return kits.get(i);
        }
      }
      return null;
    }

    private String type;
    private String[] extensions;
    private URL template;
    private Icon icon;
    private Class kitClass;
    private String description;

    /**
     * Create an editor kit description.
     * @param   type            The MIME type corresponding to the editor kit.
     * @param   exts            A list of the file name extensions
     *                          corresponding to this type.
     * @param   template        The URL of a file to be used as a template
     *                          when creating new files of this type.
     * @param   icon            The icon to associate with this MIME type.
     * @param   description     A short description of this file type.
     * @param   kitClass        The class of the editor kit to use to edit
     *                          files of this type.
     * @param   loader          The class loader to use to load the editor
     *                          kit class.
     * @param   isDefault       Specifies whether the editor kit is the
     *                          default editor kit.
     */
    @SuppressWarnings(value={"unchecked"})
    public KitInfo(String type, List exts, URL template, Icon icon,
                   String description, Class kitClass, ClassLoader loader,
                   boolean isDefault )
    {
      this.type = type;
      this.extensions = (String[])exts.toArray(new String[0]);
      this.template = template;
      this.icon = icon;
      this.description = description;
      this.kitClass = kitClass;

      // Register us
      JEditorPane.registerEditorKitForContentType(
        type, kitClass.getName(), loader
      );
      kits.add(this);
      if (isDefault) {
        defaultKitInfo = this;
      }
    }

    /**
     * Returns the MIME type associated with the editor kit.
     * @return  The requested MIME type.
     */
    public String getType()
    {
      return type;
    }

    /**
     * Returns the default file extension associated with this MIME type.
     */
    public String getDefaultExtension()
    {
      return extensions[0];
    }

    /**
     * Returns the URL of a file that can be used as a template when creating
     * new files of this MIME type.
     * @return  The requested URL.
     */
    public URL getTemplate()
    {
      return template;
    }

    /**
     * Return the icon associated with this MIME type.
     * @return  The requested icon.
     */
    public Icon getIcon()
    {
      return icon;
    }

    /**
     * Returns the class of the editor kit used to edit files of this MIME
     * type.
     * @return the requested class.
     */
    public Class getKitClass()
    {
      return kitClass;
    }

    /**
     * Returns the description of this MIME type.
     * @return  The requested description.
     */
    public String getDescription()
    {
      return description;
    }

    /**
     * Checks whether a file can be edited with this editor kit.
     * @param   f       The file to check.
     * @return  True if yes, false if no. If the file is a directory, this
     *          method returns <code>true</code>.
     */
    public boolean accept(File f)
    {
      if (f.isDirectory()) {
        return true;
      }
      String fileName = f.getName();
      for (int i=0; i<extensions.length; i++) {
        if (fileName.endsWith( extensions[i])) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Action listener invoked when one of the subitems of the "new" menu item
   * is selected, to create a new file of a particular type.
   */
  private class NewFileActionListener implements ActionListener
  {
    /**
     * The type of the file to create.
     */
    KitInfo type;

    /**
     * Create a new listener.
     * @param   type    The type of the file to create.
     */
    public NewFileActionListener(KitInfo type)
    {
      this.type = type;
    }

    /**
     * Invoked when the corresponding subitem of the "new" menu item is
     * selected.
     * @param   evt     The event sent when the subitem is selected.
     */
    public void actionPerformed( ActionEvent evt )
    {
      createNewFile(type);
    }
  }


  /**
   * Listener listening for document changes on opened documents. There is
   * initially one instance per opened document, but this listener is
   * one-fire only - as soon as it gets fired, marks changes and removes
   * itself from document. On save, new Listener is hooked again.
   */
  private class MarkingDocumentListener implements DocumentListener
  {
    private Component comp;

    public MarkingDocumentListener(Component comp)
    {
      this.comp = comp;
    }

    private void markChanged(DocumentEvent evt)
    {
      Document doc = evt.getDocument();
      doc.putProperty(MODIFIED, new Boolean(true));

      //File file = (File)doc.getProperty(FILE);
      int index = tabPane.indexOfComponent(comp);

      String title = tabPane.getTitleAt(index);
      if (!title.endsWith("*")) {
        title = title + "*";
      }
      tabPane.setTitleAt(index, title);

      doc.removeDocumentListener(this);
    }

    public void changedUpdate(DocumentEvent e)
    {
    }

    public void insertUpdate(DocumentEvent evt)
    {
      markChanged(evt);
    }

    public void removeUpdate(DocumentEvent evt)
    {
      markChanged(evt);
    }
  }

  /**
   * Undo manager for the editor. It ensures that the activation state of
   * the editor's undo/redo buttons and menu items reflects the state of the
   * undo manager.
   */
  private class EditorUndoManager extends UndoManager
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public void undoableEditHappened(UndoableEditEvent e)
    {
      super.undoableEditHappened(e);
      updateUndoRedoUI(this);
    }

    public void undo() throws CannotUndoException
    {
      super.undo();
      updateUndoRedoUI(this);
    }

    public void redo() throws CannotRedoException
    {
      super.redo();
      updateUndoRedoUI(this);
    }

    public void undoOrRedo()  throws CannotUndoException, CannotRedoException
    {
      super.undoOrRedo();
      updateUndoRedoUI(this);
    }

    public void discardAllEdits()
    {
      super.discardAllEdits();
      updateUndoRedoUI(this);
    }
  }

}
