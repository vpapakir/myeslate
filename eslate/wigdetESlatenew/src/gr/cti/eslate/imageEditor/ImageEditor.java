// Image Editor in JDK 1.3 by Drossos Nicolas @ 4/2000
// - ���������� �������� ��� �������� �� �� ��� �������� ��� �������
// ��� ��� ������������ ��� ImageEditor ���� ��� ���������� ���. ������������ ���� ���
// ������������ ��� �������� ����� �� AncestorResized, �� ���������� �� ��� ������� ����
// �� extentSize ��� �� ��������� exception.
// - ������ ���� ��� �������� ���� �� ������������ owner, ���� ��� ��������� global ����������
// ��� �� topFrame, ���� ���� �� ��� ��������� �� modal �������� ���� ��� �� ������ ��������.
// - ���� ��� ��� ����� � disposingHandle ������ �� ������ ���� ��� ��� ����� �� cancelationRespected
// ���� true ���� false, ������ �������� ���� ��� ������� ���������� ��� component.

package gr.cti.eslate.imageEditor;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputMultipleOutputPlug;
import gr.cti.eslate.base.sharedObject.SharedObject;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.colorPalette.ActiveColorEvent;
import gr.cti.eslate.colorPalette.ActiveColorListener;
import gr.cti.eslate.colorPalette.ColorPalette;
import gr.cti.eslate.iconPalette.IconPalette;
import gr.cti.eslate.iconPalette.IconPlacedEvent;
import gr.cti.eslate.iconPalette.IconPlacedListener;
import gr.cti.eslate.sharedObject.IconSO;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.NoBorderButton;
import gr.cti.eslate.utils.NoBorderToggleButton;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEditSupport;

import pv.jfcx.JPVSpin;
import pv.jfcx.JPVSpinPlus;

public class ImageEditor extends JPanel implements MouseListener, ClipboardOwner, Externalizable, ESlatePart {
	// Background, Foreground Colors
	Color backColor, foreColor;

	Color selectionColor=new Color(10,10,100,120);

	// The basic image
	BufferedImage basicImage, undoImage;

	// The dimensions of the image
	int xSize, ySize;

	// Dimensions
	private final static int BUTTON_WIDTH=25;

	private final static int BUTTON_HEIGHT=22;

	private final int RIGHT_PANEL_WIDTH=90;

	public final static Color TRANSPARENT_COLOR=new Color(255,255,255,0);

	Dimension buttonDimension=new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT);

	Dimension bigButtonDimension=new Dimension(RIGHT_PANEL_WIDTH - 10,22);

	public final static boolean IMAGE_EDITOR_OK=true;

	public final static boolean IMAGE_EDITOR_CANCEL=false;

	// Bundles
	protected ResourceBundle iEBundle=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.IEBundle",Locale.getDefault());

	protected ResourceBundle infor=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.VersionBundle",Locale.getDefault());

	// Fonts
	Font dialogFont;

//	Font menuFont=new Font("Helvetica",Font.PLAIN,12);

	// JPanels
	JPanel mainPanel, rightPanel, toolbarPanel, toolbarContainer, menuContainer, toolbar_MenuPanel, previewPanel, statusBar, drawingPanel, drawing_SettingsContainer, hostForScrollPane, stylePanel;

	CanvasPanel canvas;

	// ScrollPane
	JScrollPane mainScrollingArea;

	// RootPane
	JRootPane rootPane;

	// Glass Pane
	GridGlassPane gridPane;

	// Eslate
	private ESlateHandle handle;

	// Pin plug;
	// Plug plug;
	SingleInputMultipleOutputPlug plug;

	private SharedObject iconSO;

	boolean pinDisconnected=true; // true when the output pin is disconnected

	boolean realTimeUpdate=false; // true when the icon changes, and we must update in real time

	// the icon that the icon editor sends to its output pin
	// ESlateFieldMap fieldMap;
	// final String STR_FORMAT_VERSION = "205"; //ESlateImageEditor2.0.4
	// final String STR_FORMAT_VERSION = "210"; //ESlateImageEditor2.1.0
	final int STR_FORMAT_VERSION=210;

	static final long serialVersionUID=8130792760795892595L;

	// JTextfields for status bar
	JTextField currentPosField;

	JTextField currentImageSizeField;

	JTextField currentModeField;

	JTextField strokeField;

	// TabbedPane for palettes
	JTabbedPane tabbedPane;

	// Spaces
	// Component space1;
	// Component space2;
	// Component space3;

	// palettePanels
	ScrollPanel colorPalettePanel;

	ScrollPanel iconPalettePanel;

	// Palettes
	ColorPalette colorPalette;

	// Preview
	Preview preview;

	IconPalette iconPalette;

	// Preview
	Preview previewWindow;

	// Preview Colors Panel
	PreviewColorPanels previewColorPanels;

	// JMenuBar
	JMenuBar menuBar;

	// integer variables
	int previewSize=64; // determines the size of the preview

	int shapesChoice=1; // takes values from 1..4 and warns the shapes button which operation is selected

	// by the menuButton

	// The clipboard
	Clipboard clipBoard;

	// Colors
	Color buttonFrgrd=new Color(0,0,128);

	// JButtons
	NoBorderToggleButton panButton, rubberButton, selectButton, cropButton, magicWandButton, shapesButton, gridButton, pickerButton, flatFillButton;

	NoBorderButton unzoomButton, zoomButton, sendButton, newFileButton, openButton, saveButton, clearButton, imageSize, invertSelection, fillSelection, clearSelection, undoButton, pixelDimensionButton, cutButton, copyButton, pasteButton;

	JButton cancelButton, okButton;

	ButtonGroup bGroup=new ButtonGroup();

	// global menu items. Global because they need to be enabled/ disabled
	JMenuItem invertSelMenu, fillMenu, clearSelMenu;

	JMenu fileMenu;

	// Icons
	Icon newFile=loadImageIcon("Images/newFile.gif"," ");

	Icon pen=loadImageIcon("Images/pen.gif"," ");

	Icon line=loadImageIcon("Images/line.gif"," ");

	Icon rect=loadImageIcon("Images/rect.gif"," ");

	Icon ellip=loadImageIcon("Images/ellipse.gif"," ");

	Icon poly=loadImageIcon("Images/polyLine.gif"," ");

	Icon rectSelection=loadImageIcon("Images/select.gif"," ");

	Icon rectSelectWithArrow=loadImageIcon("Images/rectSelectWithArrow.gif"," ");

	Icon wand=loadImageIcon("Images/wand.gif"," ");

	Icon menuPen=loadImageIcon("Images/menuPen.gif"," ");

	Icon menuLine=loadImageIcon("Images/menuLine.gif"," ");

	Icon menuRect=loadImageIcon("Images/menuRect.gif"," ");

	Icon menuEllip=loadImageIcon("Images/menuEllipse.gif"," ");

	Icon menuPoly=loadImageIcon("Images/menuPolyline.gif"," ");

	Icon open=loadImageIcon("Images/open.gif"," ");

	Icon save=loadImageIcon("Images/save.gif"," ");

	Icon imageDim=loadImageIcon("Images/iconDim.gif"," ");

	Icon clear=loadImageIcon("Images/delete.gif"," ");

	Icon invert=loadImageIcon("Images/invertSelection.gif"," ");

	Icon fill=loadImageIcon("Images/backColor.gif"," ");

	Icon clsel=loadImageIcon("Images/delsel.gif"," ");

	Icon rubber=loadImageIcon("Images/rubber.gif"," ");

	Icon picker=loadImageIcon("Images/picker.gif"," ");

	Icon crop=loadImageIcon("Images/crop.gif"," ");

	Icon undo=loadImageIcon("Images/undo.gif"," ");

	Icon zoom=loadImageIcon("Images/zoom.gif"," ");

	Icon unzoom=loadImageIcon("Images/unzoom.gif"," ");

	Icon pan=loadImageIcon("Images/pan.gif"," ");

	Icon gridOnOff=loadImageIcon("Images/grid.gif"," ");

	Icon cut=loadImageIcon("Images/cut.gif"," ");

	Icon copy=loadImageIcon("Images/copy.gif"," ");

	Icon paste=loadImageIcon("Images/paste.gif"," ");

	Icon cutMenu=loadImageIcon("Images/cutMenu.gif"," ");

	Icon copyMenu=loadImageIcon("Images/copyMenu.gif"," ");

	Icon pasteMenu=loadImageIcon("Images/pasteMenu.gif"," ");

	Icon flatFill=loadImageIcon("Images/flatFill.gif"," ");

	Icon ellipseSelection=loadImageIcon("Images/ellipseSelection.gif"," ");

	Icon ellipseSelectWithArrow=loadImageIcon("Images/ellipseSelectWithArrow.gif"," ");

	// cursors
	Image penCursor=loadImageIcon("Images/penCursor.gif"," ").getImage();

	Image wandCursor=loadImageIcon("Images/wandCursor.gif"," ").getImage();

	Image rubberCursor=loadImageIcon("Images/rubberCursor.gif"," ").getImage();

	Image lineCursor=loadImageIcon("Images/lineCursor.gif"," ").getImage();

	Image rectCursor=loadImageIcon("Images/rectCursor.gif"," ").getImage();

	Image ellipseCursor=loadImageIcon("Images/ellipseCursor.gif"," ").getImage();

	Image polyLineCursor=loadImageIcon("Images/polyLineCursor.gif"," ").getImage();

	Image pickCursor=loadImageIcon("Images/pickCursor.gif"," ").getImage();

	Image wandCursorRem=loadImageIcon("Images/wandCursorRem.gif"," ").getImage();

	Image flatFillCursor=loadImageIcon("Images/flatFillCursor.gif"," ").getImage();

	Image noFlatFillCursor=loadImageIcon("Images/noFlatFillCursor.gif"," ").getImage();

	Image openHand=loadImageIcon("Images/panCursor.gif"," ").getImage();

	Image closeHand=loadImageIcon("Images/closeHand.gif"," ").getImage();

	Object[] cursors=new Object[12];

	// Available Modes
	private final static int PEN=0;

	private final static int RUBBER=1;

	private final static int LINE=2;

	private final static int RECT=3;

	private final static int ELLIPSE=4;

	private final static int POLY=5;

	private final static int SELECT_MODE=6;

	private final static int MAGIC_WAND=7;

	private final static int DRAG_SELECTION_MODE=8;

	private final static int PAN_MODE=9;

	private final static int PICK_MODE=10;

	private final static int FLAT_FILL_MODE=11;

	/*
	 * private final static int COPY = 1; private final static int CUT = 2; private final static int PASTE = 3; private
	 * final static int cellMode = 23;
	 */

	// initial mode
	private int mode=PEN;

	// Listeners
	ActionListener newFileListener;

	ActionListener openListener;

	ActionListener saveListener;

	ActionListener copyListener;

	ActionListener cutListener;

	ActionListener pasteListener;

	ActionListener deleteSelListener;

	// Menu Components
	JCheckBoxMenuItem gridBox;

	JMenuItem oneTo1;

	JMenu iconPaletteMenu, colorPaletteMenu, imageMenu;

	JRadioButtonMenuItem penMenu;

	JRadioButtonMenuItem lineMenu;

	JRadioButtonMenuItem rectMenu;

	JRadioButtonMenuItem ellipseMenu;

	JRadioButtonMenuItem polyMenu;

	JRadioButtonMenuItem rubberMenu;

	JRadioButtonMenuItem flatFillMenu;

	JRadioButtonMenuItem selectMenu;

	JRadioButtonMenuItem wandMenu;

	// StylePanel's elements
	StyleArea sArea1, sArea2, sArea3, sArea4, sArea5, sArea6, sArea7, sArea8, rStyle1, rStyle2, rStyle3;

	StyleArea selectedArea;

	StyleArea ellStyle1, ellStyle2, ellStyle3, polyStyle1, polyStyle2, polyStyle3;

	JPanel rectStylesPanel, lineWidthsPanel, ellipseStylesPanel, polyStylesPanel;

	// control variables
	boolean gridState=false;

	boolean isImageModified=false;

	boolean imageModifiedForDialog=false;

	boolean componentModified=false;

	boolean imageSaved;

	double scalingFactor=5;

	Point pointPressed;

	boolean extensionChanged;

	int fillStyle=1;

	Dimension extentSize;

	// Property Editor's vars
	boolean toolbarMap[]=new boolean[8];

	boolean rightPanelMap[]=new boolean[4];

	// dialogs
	// static JFrame dialogFrame = new JFrame();
	// static transient FileDialog fileDialog = new FileDialog(dialogFrame);
	JFileChooser fileChooser;// = new JFileChooser();

	MyFileFilter allFilter, gifFilter, jpgFilter, pngFilter, bmpFilter;// , lastFilter;

	javax.swing.filechooser.FileFilter lastFilter;

	SpinDialog dialogImageDimension;

	// image props
	String fileName;

	String imageName="Untitled";

	String extension=".gif";

	boolean firstPolyPoint=true;

	boolean gridFirstSet=true;

	// int activeThreads = 0;
	// Thread myThread;
	int threadsCount;

	Cursor previousCursor;

	String previousText;

	boolean firstZoom=true;

	// undo
	// UndoManager uManager;
	MyUndoManager uManager;

	public UndoableEditSupport uSupport;

	ArrayList<Point> oldUndoPixels;
	ArrayList<Color> oldUndoColor;

	Shape selection;

	Frame topFrame;

	javax.swing.Timer popupTimer;

	ActionListener selectionPopupTimerAction, shapesPopupTimerAction;

	boolean rectangularSelection=true;

	AncestorListener ancestorListener=new AncestorListener() {
		public void ancestorAdded(AncestorEvent event) {
			topFrame=(Frame) SwingUtilities.getAncestorOfClass(Frame.class,ImageEditor.this);
			addHierarchyBoundsListener(hierarchyResize);
			autoResize();
		}

		public void ancestorMoved(AncestorEvent event) {
		}

		public void ancestorRemoved(AncestorEvent event) {
		}
	};

	public ImageEditor() {
		super();

		setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));
		// initializing background color to white and foreground to black
		backColor=Color.white;
		foreColor=Color.black;
		setPreferredSize(new Dimension(550,490));

		dialogFont=new Font("dialog",Font.PLAIN,10);
		// UIManager.put("ToolTip.font", dialogFont);
		// UIManager.put("Panel.font", dialogFont);
		/*
		 * try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception
		 * e){e.printStackTrace();};
		 */

		// *** initiate basic panels
		mainPanel=new JPanel(true); // the lower-level panel
		hostForScrollPane=new JPanel(); // for the border
		canvas=new CanvasPanel(scalingFactor); // the drawing area (canvas)
		mainScrollingArea=new JScrollPane(); // the scroll pane owned by canvas
		rightPanel=new JPanel(); // the right panel
		toolbarPanel=new JPanel(true); // the toolbar Panel
		toolbarContainer=new JPanel(); // the container of toolbar
		menuBar=new JMenuBar(); // The bar for the menu
		menuContainer=new JPanel(); // The container of MenuBar
		toolbar_MenuPanel=new JPanel(); // The container of toolbar & menu
		previewPanel=new JPanel(true); // The preview panel
		statusBar=new JPanel(); // The status bar

		// setting layout for basic panels
		mainPanel.setLayout(new BorderLayout());
		rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel,BoxLayout.X_AXIS));
		toolbar_MenuPanel.setLayout(new BoxLayout(toolbar_MenuPanel,BoxLayout.Y_AXIS));
		menuContainer.setLayout(new BoxLayout(menuContainer,BoxLayout.X_AXIS));
		toolbarContainer.setLayout(new BoxLayout(toolbarContainer,BoxLayout.X_AXIS));
		previewPanel.setLayout(new BorderLayout());
		statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));
		setLayout(new BorderLayout());
		hostForScrollPane.setLayout(new BorderLayout());

		// additional features for the panels
		canvas.setBackground(new Color(220,220,220));
		Dimension previewDimension=new Dimension(previewSize,previewSize);
		previewPanel.setMaximumSize(previewDimension);
		previewPanel.setMinimumSize(previewDimension);
		previewPanel.setPreferredSize(previewDimension);
		previewPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		EmptyBorder outerBorder=new EmptyBorder(new Insets(5,5,5,5));
		hostForScrollPane.setBorder(outerBorder);

		// preview
		preview=new Preview(previewDimension);
		previewPanel.add(preview);

		// nesting - adding to panels
		menuContainer.add(menuBar);
		menuContainer.add(Box.createHorizontalGlue());

		toolbarContainer.add(toolbarPanel);
		toolbarContainer.add(Box.createHorizontalGlue());

		toolbar_MenuPanel.add(menuContainer);
		toolbar_MenuPanel.add(Box.createVerticalStrut(1));
		toolbar_MenuPanel.add(toolbarContainer);

		// hostForScrollPane.add(mainScrollingArea, BorderLayout.CENTER);

		mainPanel.add(toolbar_MenuPanel,BorderLayout.NORTH);
		mainPanel.add(hostForScrollPane,BorderLayout.CENTER);
		add(BorderLayout.CENTER,mainPanel);

		createToolbarButtons();
		createRightPanelElements();
		createMenu();
		addButtons();

		// initialize image and canvas
		xSize=500;
		ySize=500;

		basicImage=new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);
		drawInitialImage(basicImage);
		canvas.setCanvasImage(basicImage);
		preview.setPreviewImage(basicImage);

		rootPane=new JRootPane();
		rootPane.getContentPane().add(mainScrollingArea);
		EmptyBorder outerBorder2=new EmptyBorder(new Insets(1,1,1,1));
		mainScrollingArea.setBorder(outerBorder2);

		hostForScrollPane.add(rootPane,BorderLayout.CENTER);

		mainScrollingArea.setViewportView(canvas);
		gridPane=new GridGlassPane((int) canvas.getScalingFactor());
		// addComponentListener(componentResize);
		// addHierarchyBoundsListener(hierarchyResize);

		canvas.addMouseListener(this);

		mainScrollingArea.getHorizontalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
		mainScrollingArea.getVerticalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
		mainScrollingArea.getHorizontalScrollBar().addAdjustmentListener(adjustX);
		mainScrollingArea.getVerticalScrollBar().addAdjustmentListener(adjustY);

		mainScrollingArea.getHorizontalScrollBar().addMouseListener(moveListenerForCursor);
		mainScrollingArea.getVerticalScrollBar().addMouseListener(moveListenerForCursor);

		// defining cursors
		cursors[0]=Toolkit.getDefaultToolkit().createCustomCursor(penCursor,new Point(13,23),"penCursor");
		cursors[1]=Toolkit.getDefaultToolkit().createCustomCursor(rubberCursor,new Point(7,23),"rubberCursor");
		cursors[2]=Toolkit.getDefaultToolkit().createCustomCursor(lineCursor,new Point(12,12),"lineCursor");
		cursors[3]=Toolkit.getDefaultToolkit().createCustomCursor(rectCursor,new Point(12,12),"rectCursor");
		cursors[4]=Toolkit.getDefaultToolkit().createCustomCursor(ellipseCursor,new Point(12,12),"ellipseCursor");
		cursors[5]=Toolkit.getDefaultToolkit().createCustomCursor(polyLineCursor,new Point(12,12),"polyLineCursor");
		cursors[6]=Toolkit.getDefaultToolkit().createCustomCursor(wandCursor,new Point(10,23),"wandCursor");
		cursors[7]=Toolkit.getDefaultToolkit().createCustomCursor(pickCursor,new Point(10,23),"pickCursor");
		cursors[8]=Toolkit.getDefaultToolkit().createCustomCursor(flatFillCursor,new Point(8,19),"flatFillCursor");
		cursors[9]=Toolkit.getDefaultToolkit().createCustomCursor(noFlatFillCursor,new Point(8,19),"noFlatFillCursor");
		cursors[10]=Toolkit.getDefaultToolkit().createCustomCursor(openHand,new Point(13,23),"openHandCursor");
		cursors[11]=Toolkit.getDefaultToolkit().createCustomCursor(closeHand,new Point(13,23),"closeHandCursor");

		setMode(PEN);

		/*
		 * jpgFilter = new MyFileFilter(); jpgFilter.addExtension("jpg"); jpgFilter.setDescription("JPG Images");
		 * gifFilter = new MyFileFilter(); gifFilter.addExtension("gif"); gifFilter.setDescription("GIF Images");
		 * bmpFilter = new MyFileFilter(); bmpFilter.addExtension("bmp"); bmpFilter.setDescription("BMP Images");
		 * pngFilter = new MyFileFilter(); pngFilter.addExtension("png"); pngFilter.setDescription("PNG Images");
		 * lastFilter = gifFilter;
		 * 
		 * fileChooser.addChoosableFileFilter(jpgFilter); fileChooser.addChoosableFileFilter(gifFilter);
		 * fileChooser.addChoosableFileFilter(bmpFilter); fileChooser.addChoosableFileFilter(pngFilter);
		 */

		// uManager = new UndoManager();
		uManager=new MyUndoManager();
		uSupport=new UndoableEditSupport(this);
		uSupport.addUndoableEditListener(uManager);
		oldUndoPixels=new ArrayList<Point>();
		oldUndoColor=new ArrayList<Color>();

		// initializing the Map Arrays
		for (int i=0;i < toolbarMap.length;i++)
			toolbarMap[i]=true;
		for (int i=0;i < rightPanelMap.length;i++)
			rightPanelMap[i]=true;

		ActionListener UndoListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					uManager.setCanvasArgument(canvas);
					uManager.undo();
				} catch (CannotUndoException ex) {
					undoButton.setEnabled(false);
					System.out.println("Cannot undo");
				}
				;
			}
		};

		ActionListener selectAllListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select(new Point(0,0),new Point(xSize * (int) scalingFactor,ySize * (int) scalingFactor),false,true,false);
			}
		};

		canvas.registerKeyboardAction(newFileListener,KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(UndoListener,KeyStroke.getKeyStroke(KeyEvent.VK_Z,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(openListener,KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(saveListener,KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(selectAllListener,KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(copyListener,KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(cutListener,KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(pasteListener,KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		registerKeyboardAction(deleteSelListener,KeyStroke.getKeyStroke("DELETE"),JComponent.WHEN_IN_FOCUSED_WINDOW);

		addAncestorListener(ancestorListener);
		// setBorder(new EmptyBorder(0,0,0,0));

	}// end of Constructor

	public ImageEditor(int xDim,int yDim,Color backgr) {
		super();
		// initializing background color to white and foreground to black
		// backColor = Color.white;
		backColor=backgr;
		foreColor=Color.black;
		this.setPreferredSize(new Dimension(550,490));

		// Setting the proper font for Greek.
		dialogFont=new Font("dialog",Font.PLAIN,10);
		// UIManager.put("ToolTip.font", dialogFont);
		// UIManager.put("Panel.font", dialogFont);
		/*
		 * try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception
		 * e){e.printStackTrace();};
		 */

		// *** initiate basic panels
		mainPanel=new JPanel(true); // the lower-level panel
		hostForScrollPane=new JPanel(); // for the border
		canvas=new CanvasPanel(scalingFactor); // the drawing area (canvas)
		mainScrollingArea=new JScrollPane(); // the scroll pane owned by canvas
		rightPanel=new JPanel(); // the right panel
		toolbarPanel=new JPanel(true); // the toolbar Panel
		toolbarContainer=new JPanel(); // the container of toolbar
		menuBar=new JMenuBar(); // The bar for the menu
		menuContainer=new JPanel(); // The container of MenuBar
		toolbar_MenuPanel=new JPanel(); // The container of toolbar & menu
		previewPanel=new JPanel(true); // The preview panel
		statusBar=new JPanel(); // The status bar

		// setting layout for basic panels
		mainPanel.setLayout(new BorderLayout());
		rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel,BoxLayout.X_AXIS));
		toolbar_MenuPanel.setLayout(new BoxLayout(toolbar_MenuPanel,BoxLayout.Y_AXIS));
		menuContainer.setLayout(new BoxLayout(menuContainer,BoxLayout.X_AXIS));
		toolbarContainer.setLayout(new BoxLayout(toolbarContainer,BoxLayout.X_AXIS));
		previewPanel.setLayout(new BorderLayout());
		statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));
		setLayout(new BorderLayout());
		hostForScrollPane.setLayout(new BorderLayout());

		// additional features for the panels
		canvas.setBackground(new Color(220,220,220));
		Dimension previewDimension=new Dimension(previewSize,previewSize);
		previewPanel.setMaximumSize(previewDimension);
		previewPanel.setMinimumSize(previewDimension);
		previewPanel.setPreferredSize(previewDimension);
		previewPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		EmptyBorder outerBorder=new EmptyBorder(new Insets(5,5,5,5));
		hostForScrollPane.setBorder(outerBorder);

		// preview
		preview=new Preview(previewDimension);
		previewPanel.add(preview);

		// nesting - adding to panels
		menuContainer.add(menuBar);
		menuContainer.add(Box.createHorizontalGlue());

		toolbarContainer.add(toolbarPanel);
		toolbarContainer.add(Box.createHorizontalGlue());

		toolbar_MenuPanel.add(menuContainer);
		toolbar_MenuPanel.add(Box.createVerticalStrut(1));
		toolbar_MenuPanel.add(toolbarContainer);

		// hostForScrollPane.add(mainScrollingArea, BorderLayout.CENTER);

		mainPanel.add(toolbar_MenuPanel,BorderLayout.NORTH);
		mainPanel.add(hostForScrollPane,BorderLayout.CENTER);
		add(BorderLayout.CENTER,mainPanel);

		createToolbarButtons();
		createRightPanelElements();
		createMenu();
		addButtons();

		// initialize image and canvas
		xSize=xDim;
		ySize=yDim;
		basicImage=new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);
		drawInitialImage(basicImage);
		canvas.setCanvasImage(basicImage);
		preview.setPreviewImage(basicImage);

		rootPane=new JRootPane();
		rootPane.getContentPane().add(mainScrollingArea);
		EmptyBorder outerBorder2=new EmptyBorder(new Insets(1,1,1,1));
		mainScrollingArea.setBorder(outerBorder2);

		hostForScrollPane.add(rootPane,BorderLayout.CENTER);

		mainScrollingArea.setViewportView(canvas);
		gridPane=new GridGlassPane((int) canvas.getScalingFactor());
		// addComponentListener(componentResize);
		// addHierarchyBoundsListener(hierarchyResize);

		canvas.addMouseListener(this);

		mainScrollingArea.getHorizontalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
		mainScrollingArea.getVerticalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
		mainScrollingArea.getHorizontalScrollBar().addAdjustmentListener(adjustX);
		mainScrollingArea.getVerticalScrollBar().addAdjustmentListener(adjustY);

		mainScrollingArea.getHorizontalScrollBar().addMouseListener(moveListenerForCursor);
		mainScrollingArea.getVerticalScrollBar().addMouseListener(moveListenerForCursor);

		// defining cursors
		cursors[0]=Toolkit.getDefaultToolkit().createCustomCursor(penCursor,new Point(13,23),"penCursor");
		cursors[1]=Toolkit.getDefaultToolkit().createCustomCursor(rubberCursor,new Point(7,23),"rubberCursor");
		cursors[2]=Toolkit.getDefaultToolkit().createCustomCursor(lineCursor,new Point(12,12),"lineCursor");
		cursors[3]=Toolkit.getDefaultToolkit().createCustomCursor(rectCursor,new Point(12,12),"rectCursor");
		cursors[4]=Toolkit.getDefaultToolkit().createCustomCursor(ellipseCursor,new Point(12,12),"ellipseCursor");
		cursors[5]=Toolkit.getDefaultToolkit().createCustomCursor(polyLineCursor,new Point(12,12),"polyLineCursor");
		cursors[6]=Toolkit.getDefaultToolkit().createCustomCursor(wandCursor,new Point(10,23),"wandCursor");
		cursors[7]=Toolkit.getDefaultToolkit().createCustomCursor(pickCursor,new Point(10,23),"pickCursor");
		cursors[8]=Toolkit.getDefaultToolkit().createCustomCursor(flatFillCursor,new Point(8,19),"flatFillCursor");
		cursors[9]=Toolkit.getDefaultToolkit().createCustomCursor(noFlatFillCursor,new Point(8,19),"noFlatFillCursor");
		cursors[10]=Toolkit.getDefaultToolkit().createCustomCursor(openHand,new Point(13,23),"openHandCursor");
		cursors[11]=Toolkit.getDefaultToolkit().createCustomCursor(closeHand,new Point(13,23),"closeHandCursor");

		setMode(PEN);

		/*
		 * jpgFilter = new MyFileFilter(); jpgFilter.addExtension("jpg"); jpgFilter.setDescription("JPG Images");
		 * gifFilter = new MyFileFilter(); gifFilter.addExtension("gif"); gifFilter.setDescription("GIF Images");
		 * bmpFilter = new MyFileFilter(); bmpFilter.addExtension("bmp"); bmpFilter.setDescription("BMP Images");
		 * pngFilter = new MyFileFilter(); pngFilter.addExtension("png"); pngFilter.setDescription("PNG Images");
		 * lastFilter = gifFilter;
		 * 
		 * fileChooser.addChoosableFileFilter(jpgFilter); fileChooser.addChoosableFileFilter(gifFilter);
		 * fileChooser.addChoosableFileFilter(bmpFilter); fileChooser.addChoosableFileFilter(pngFilter);
		 */

		uManager=new MyUndoManager();
		uSupport=new UndoableEditSupport(this);
		uSupport.addUndoableEditListener(uManager);
		oldUndoPixels=new ArrayList<Point>();
		oldUndoColor=new ArrayList<Color>();

		// initializing the Map Arrays
		for (int i=0;i < toolbarMap.length;i++)
			toolbarMap[i]=true;
		for (int i=0;i < rightPanelMap.length;i++)
			rightPanelMap[i]=true;

		ActionListener UndoListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					uManager.setCanvasArgument(canvas);
					uManager.undo();
				} catch (CannotUndoException ex) {
					undoButton.setEnabled(false);
					System.out.println("Cannot undo");
				}
				;
			}
		};

		ActionListener selectAllListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select(new Point(0,0),new Point(xSize * (int) scalingFactor,ySize * (int) scalingFactor),false,true,false);
			}
		};

		canvas.registerKeyboardAction(newFileListener,KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(UndoListener,KeyStroke.getKeyStroke(KeyEvent.VK_Z,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(openListener,KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(saveListener,KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(selectAllListener,KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(copyListener,KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(cutListener,KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(pasteListener,KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		registerKeyboardAction(deleteSelListener,KeyStroke.getKeyStroke("DELETE"),JComponent.WHEN_IN_FOCUSED_WINDOW);

		addAncestorListener(ancestorListener);

	}// end of Constructor 2

	// constructor 3
	public ImageEditor(int xDim,int yDim) {
		super();
		// initializing background color to white and foreground to black
		backColor=Color.white;
		foreColor=Color.black;
		this.setPreferredSize(new Dimension(550,490));

		dialogFont=new Font("dialog",Font.PLAIN,10);
		// UIManager.put("ToolTip.font", dialogFont);
		// UIManager.put("Panel.font", dialogFont);
		/*
		 * try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception
		 * e){e.printStackTrace();};
		 */

		// *** initiate basic panels
		mainPanel=new JPanel(true); // the lower-level panel
		hostForScrollPane=new JPanel(); // for the border
		canvas=new CanvasPanel(scalingFactor); // the drawing area (canvas)
		mainScrollingArea=new JScrollPane(); // the scroll pane owned by canvas
		rightPanel=new JPanel(); // the right panel
		toolbarPanel=new JPanel(true); // the toolbar Panel
		toolbarContainer=new JPanel(); // the container of toolbar
		menuBar=new JMenuBar(); // The bar for the menu
		menuContainer=new JPanel(); // The container of MenuBar
		toolbar_MenuPanel=new JPanel(); // The container of toolbar & menu
		previewPanel=new JPanel(true); // The preview panel
		statusBar=new JPanel(); // The status bar

		// setting layout for basic panels
		mainPanel.setLayout(new BorderLayout());
		rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel,BoxLayout.X_AXIS));
		toolbar_MenuPanel.setLayout(new BoxLayout(toolbar_MenuPanel,BoxLayout.Y_AXIS));
		menuContainer.setLayout(new BoxLayout(menuContainer,BoxLayout.X_AXIS));
		toolbarContainer.setLayout(new BoxLayout(toolbarContainer,BoxLayout.X_AXIS));
		previewPanel.setLayout(new BorderLayout());
		statusBar.setLayout(new BoxLayout(statusBar,BoxLayout.X_AXIS));
		setLayout(new BorderLayout());
		hostForScrollPane.setLayout(new BorderLayout());

		// additional features for the panels
		canvas.setBackground(new Color(220,220,220));
		Dimension previewDimension=new Dimension(previewSize,previewSize);
		previewPanel.setMaximumSize(previewDimension);
		previewPanel.setMinimumSize(previewDimension);
		previewPanel.setPreferredSize(previewDimension);
		previewPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		EmptyBorder outerBorder=new EmptyBorder(new Insets(5,5,5,5));
		hostForScrollPane.setBorder(outerBorder);

		// preview
		preview=new Preview(previewDimension);
		previewPanel.add(preview);

		// nesting - adding to panels
		menuContainer.add(menuBar);
		menuContainer.add(Box.createHorizontalGlue());

		toolbarContainer.add(toolbarPanel);
		toolbarContainer.add(Box.createHorizontalGlue());

		toolbar_MenuPanel.add(menuContainer);
		toolbar_MenuPanel.add(Box.createVerticalStrut(1));
		toolbar_MenuPanel.add(toolbarContainer);

		// hostForScrollPane.add(mainScrollingArea, BorderLayout.CENTER);

		mainPanel.add(toolbar_MenuPanel,BorderLayout.NORTH);
		mainPanel.add(hostForScrollPane,BorderLayout.CENTER);
		add(BorderLayout.CENTER,mainPanel);

		createToolbarButtons();
		createRightPanelElements();
		createMenu();
		addButtons();

		// initialize image and canvas
		xSize=xDim;
		ySize=yDim;
		basicImage=new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);
		drawInitialImage(basicImage);
		canvas.setCanvasImage(basicImage);
		preview.setPreviewImage(basicImage);

		rootPane=new JRootPane();
		rootPane.getContentPane().add(mainScrollingArea);
		EmptyBorder outerBorder2=new EmptyBorder(new Insets(1,1,1,1));
		mainScrollingArea.setBorder(outerBorder2);

		hostForScrollPane.add(rootPane,BorderLayout.CENTER);

		mainScrollingArea.setViewportView(canvas);
		gridPane=new GridGlassPane((int) canvas.getScalingFactor());
		// addComponentListener(componentResize);
		// addHierarchyBoundsListener(hierarchyResize);

		canvas.addMouseListener(this);

		mainScrollingArea.getHorizontalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
		mainScrollingArea.getVerticalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
		mainScrollingArea.getHorizontalScrollBar().addAdjustmentListener(adjustX);
		mainScrollingArea.getVerticalScrollBar().addAdjustmentListener(adjustY);

		mainScrollingArea.getHorizontalScrollBar().addMouseListener(moveListenerForCursor);
		mainScrollingArea.getVerticalScrollBar().addMouseListener(moveListenerForCursor);

		// defining cursors
		cursors[0]=Toolkit.getDefaultToolkit().createCustomCursor(penCursor,new Point(13,23),"penCursor");
		cursors[1]=Toolkit.getDefaultToolkit().createCustomCursor(rubberCursor,new Point(7,23),"rubberCursor");
		cursors[2]=Toolkit.getDefaultToolkit().createCustomCursor(lineCursor,new Point(12,12),"lineCursor");
		cursors[3]=Toolkit.getDefaultToolkit().createCustomCursor(rectCursor,new Point(12,12),"rectCursor");
		cursors[4]=Toolkit.getDefaultToolkit().createCustomCursor(ellipseCursor,new Point(12,12),"ellipseCursor");
		cursors[5]=Toolkit.getDefaultToolkit().createCustomCursor(polyLineCursor,new Point(12,12),"polyLineCursor");
		cursors[6]=Toolkit.getDefaultToolkit().createCustomCursor(wandCursor,new Point(10,23),"wandCursor");
		cursors[7]=Toolkit.getDefaultToolkit().createCustomCursor(pickCursor,new Point(10,23),"pickCursor");
		cursors[8]=Toolkit.getDefaultToolkit().createCustomCursor(flatFillCursor,new Point(8,19),"flatFillCursor");
		cursors[9]=Toolkit.getDefaultToolkit().createCustomCursor(noFlatFillCursor,new Point(8,19),"noFlatFillCursor");
		cursors[10]=Toolkit.getDefaultToolkit().createCustomCursor(openHand,new Point(13,23),"openHandCursor");
		cursors[11]=Toolkit.getDefaultToolkit().createCustomCursor(closeHand,new Point(13,23),"closeHandCursor");

		setMode(PEN);

		/*
		 * jpgFilter = new MyFileFilter(); jpgFilter.addExtension("jpg"); jpgFilter.setDescription("JPG Images");
		 * gifFilter = new MyFileFilter(); gifFilter.addExtension("gif"); gifFilter.setDescription("GIF Images");
		 * bmpFilter = new MyFileFilter(); bmpFilter.addExtension("bmp"); bmpFilter.setDescription("BMP Images");
		 * pngFilter = new MyFileFilter(); pngFilter.addExtension("png"); pngFilter.setDescription("PNG Images");
		 * lastFilter = gifFilter;
		 * 
		 * fileChooser.addChoosableFileFilter(jpgFilter); fileChooser.addChoosableFileFilter(gifFilter);
		 * fileChooser.addChoosableFileFilter(bmpFilter); fileChooser.addChoosableFileFilter(pngFilter);
		 */

		uManager=new MyUndoManager();
		uSupport=new UndoableEditSupport(this);
		uSupport.addUndoableEditListener(uManager);
		oldUndoPixels=new ArrayList<Point>();
		oldUndoColor=new ArrayList<Color>();

		// initializing the Map Arrays
		for (int i=0;i < toolbarMap.length;i++)
			toolbarMap[i]=true;
		for (int i=0;i < rightPanelMap.length;i++)
			rightPanelMap[i]=true;

		ActionListener UndoListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					uManager.setCanvasArgument(canvas);
					uManager.undo();
				} catch (CannotUndoException ex) {
					undoButton.setEnabled(false);
					System.out.println("Cannot undo");
				}
				;
			}
		};

		ActionListener selectAllListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select(new Point(0,0),new Point(xSize * (int) scalingFactor,ySize * (int) scalingFactor),false,true,false);
			}
		};

		canvas.registerKeyboardAction(newFileListener,KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(UndoListener,KeyStroke.getKeyStroke(KeyEvent.VK_Z,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(openListener,KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(saveListener,KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(selectAllListener,KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(copyListener,KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(cutListener,KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		canvas.registerKeyboardAction(pasteListener,KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK),JComponent.WHEN_IN_FOCUSED_WINDOW);
		registerKeyboardAction(deleteSelListener,KeyStroke.getKeyStroke("DELETE"),JComponent.WHEN_IN_FOCUSED_WINDOW);

		addAncestorListener(ancestorListener);
	}// end of Constructor 3

	public void prepareFileChooser() {
		fileChooser=new JFileChooser();
		jpgFilter=new MyFileFilter();
		jpgFilter.addExtension("jpg");
		jpgFilter.setDescription("JPG Images");
		gifFilter=new MyFileFilter();
		gifFilter.addExtension("gif");
		gifFilter.setDescription("GIF Images");
		bmpFilter=new MyFileFilter();
		bmpFilter.addExtension("bmp");
		bmpFilter.setDescription("BMP Images");
		pngFilter=new MyFileFilter();
		pngFilter.addExtension("png");
		pngFilter.setDescription("PNG Images");
		allFilter=new MyFileFilter();
		allFilter.addExtension("jpg");
		allFilter.addExtension("gif");
		allFilter.addExtension("bmp");
		allFilter.addExtension("png");
		allFilter.setDescription("All Images");
		lastFilter=allFilter;

		fileChooser.addChoosableFileFilter(allFilter);
		fileChooser.addChoosableFileFilter(jpgFilter);
		fileChooser.addChoosableFileFilter(gifFilter);
		fileChooser.addChoosableFileFilter(bmpFilter);
		fileChooser.addChoosableFileFilter(pngFilter);
	}

	public void destroyImages() {
		if (basicImage != null)
			basicImage.flush();
		basicImage=null;
		if (undoImage != null)
			undoImage.flush();
		undoImage=null;
	}

	public void destroyImageEditor() {
		removeAllListeners();
		// myThread = null;
		uSupport=null;
		removeAll();
		mainPanel.removeAll();
		mainPanel=null;
		canvas.removeMouseListener(ImageEditor.this);
		canvas.resetKeyboardActions();
		canvas.destroyImages();
		canvas=null;
		ImageEditor.this.resetKeyboardActions();
		handle=null;
		iconSO=null;
		destroyImages();
		gridPane.destroyImages();
		preview.destroyImages();
		rightPanel=null;
		toolbarPanel=null;
		toolbarContainer=null;
		menuContainer=null;
		toolbar_MenuPanel=null;
		previewPanel=null;
		statusBar=null;
		drawingPanel=null;
		drawing_SettingsContainer=null;
		hostForScrollPane=null;
		stylePanel=null;
		mainScrollingArea=null;
		rootPane=null;
		gridPane=null;
		tabbedPane=null;
		colorPalettePanel=null;
		iconPalettePanel=null;
		colorPalette=null;
		preview=null;
		iconPalette=null;
		previewWindow=null;
		previewColorPanels=null;
		menuBar.removeAll();
		menuBar=null;
		panButton=null;
		rubberButton=null;
		selectButton=null;
		cropButton=null;
		magicWandButton=null;
		shapesButton=null;
		gridButton=null;
		pickerButton=null;
		flatFillButton=null;
		sendButton=null;
		unzoomButton=null;
		zoomButton=null;
		newFileButton=null;
		openButton=null;
		saveButton=null;
		clearButton=null;
		imageSize=null;
		invertSelection=null;
		fillSelection=null;
		clearSelection=null;
		undoButton=null;
		pixelDimensionButton=null;
		cutButton=null;
		copyButton=null;
		pasteButton=null;

		gridBox=null;
		oneTo1=null;
		iconPaletteMenu=null;
		colorPaletteMenu=null;
		imageMenu=null;
		penMenu=null;
		lineMenu=null;
		rectMenu=null;
		ellipseMenu=null;
		polyMenu=null;
		rubberMenu=null;
		flatFillMenu=null;
		selectMenu=null;
		wandMenu=null;

		penListener=null;
		lineListener=null;
		rectListener=null;
		ellipseListener=null;
		polyListener=null;
		selectListener=null;
		flatFillListener=null;
		wandListener=null;
		panListener=null;
		pickListener=null;

		sArea1=null;
		sArea2=null;
		sArea3=null;
		sArea4=null;
		sArea5=null;
		sArea6=null;
		sArea7=null;
		sArea8=null;
		rStyle1=null;
		rStyle2=null;
		rStyle3=null;
		selectedArea=null;
		ellStyle1=null;
		ellStyle2=null;
		ellStyle3=null;
		polyStyle1=null;
		polyStyle2=null;
		polyStyle3=null;

		newFileListener=null;
		openListener=null;
		saveListener=null;
		copyListener=null;
		cutListener=null;
		pasteListener=null;
		deleteSelListener=null;

		newFile=null;
		pen=null;
		line=null;
		rect=null;
		ellip=null;
		poly=null;
		rectSelection=null;
		rectSelectWithArrow=null;
		ellipseSelection=null;
		ellipseSelectWithArrow=null;
		wand=null;
		menuPen=null;
		menuLine=null;
		menuRect=null;
		menuEllip=null;
		menuPoly=null;
		open=null;
		save=null;
		imageDim=null;
		clear=null;
		invert=null;
		fill=null;
		clsel=null;
		rubber=null;
		picker=null;
		crop=null;
		undo=null;
		zoom=null;
		unzoom=null;
		pan=null;
		gridOnOff=null;
		cut=null;
		copy=null;
		paste=null;
		cutMenu=null;
		copyMenu=null;
		pasteMenu=null;
		flatFill=null;
	}

	// ESlate
	// ESlate Handling
	public ESlateHandle getESlateHandle() {
		String info[]= {infor.getString("part"),infor.getString("development"),infor.getString("contribution"),infor.getString("copyright")};
		ESlateInfo eslateInfo=new ESlateInfo(infor.getString("compo"),info);
		if (handle == null) {
			handle=ESlate.registerPart(this);
			handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ImageEditorPrimitives");
			handle.addESlateListener(new ESlateAdapter() {
				public void handleDisposed(HandleDisposalEvent e) {
					if (iconPalette.isPaletteModified) {
						int questionPalette=JOptionPane.showConfirmDialog(null,iEBundle.getString("wannaSavePalette"),iEBundle.getString("alertPalette"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if (questionPalette == JOptionPane.YES_OPTION)
							iconPalette.save();
					}
					if (colorPalette.isPaletteModified) {
						int questionColorPalette=JOptionPane.showConfirmDialog(null,iEBundle.getString("wannaSaveColorPalette"),iEBundle.getString("alertColorPalette"),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if (questionColorPalette == JOptionPane.YES_OPTION) {
							colorPalette.save();
						}
					}
					destroyImageEditor();
				}

				public void disposingHandle(HandleDisposalEvent e) {
					// Component is closing atomically
					if (e.cancellationRespected) {
						int question=JOptionPane.showConfirmDialog(null,iEBundle.getString("SaveIm"),iEBundle.getString("warnClose"),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
						// yes--> Save the image and quit
						// if pressed cancel in fileDialog then veto
						if (question == JOptionPane.YES_OPTION) {
							if (!saveSelected())
								e.vetoDisposal=true;
						}
						// no --> don't save the image and quit
						else if (question == JOptionPane.NO_OPTION) {
							// destroyImageEditor();
						}
						// cancel--> veto
						else
							e.vetoDisposal=true;
					}
					// close microworld
					else {
						componentModified=componentModified || iconPalette.isPaletteModified || colorPalette.isPaletteModified;
						// inform the microworld that smth has changed
						if (isImageModified || componentModified) {
							e.stateChanged=true;
						}
					}
				}
			});

			// iconSO = new ImageIconSO(handle);
			iconSO=new IconSO(handle);
			if (realTimeUpdate && !pinDisconnected)
				sendIcon();
			try {
				handle.setUniqueComponentName(iEBundle.getString("componame"));
			} catch (RenamingForbiddenException e) {
			}

			SharedObjectListener pinListener=new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent e) {
					Runtime rt=java.lang.Runtime.getRuntime();
					rt.gc();
					// ImageIcon img=((ImageIconSO)(e.getSharedObject())).getImageIcon();
					Icon ico=((IconSO) (e.getSharedObject())).getIconSO();

					// if (img != null){
					if (ico != null) {
						BufferedImage img=new BufferedImage(ico.getIconWidth(),ico.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
						ico.paintIcon(ImageEditor.this,img.getGraphics(),0,0);
						// if (!openImage(img.getImage(), false)){
						if (!openImage(img,false)) {
							// e.getSharedObject().getPin().disconnect();
							e.getSharedObject().getPlug().disconnect();
							return;
						}
					}
					autoResize();
				}
			};
			handle.setInfo(eslateInfo);
			try {
				// Pin plug = new SingleInputMultipleOutputPin(handle, iEBundle.getString("pin"), new
				// Color(216,191,216),
				// gr.cti.eslate.sharedObject.ImageIconSO.class,
				// iconSO,pinListener);
				/*
				 * plug = new SingleInputMultipleOutputPin(handle, iEBundle.getString("pin"), new Color(50,151,220),
				 * gr.cti.eslate.sharedObject.IconSO.class, iconSO,pinListener);
				 */
				plug=new SingleInputMultipleOutputPlug(handle,iEBundle,"pin",new Color(50,151,220),gr.cti.eslate.sharedObject.IconSO.class,iconSO,pinListener);
				plug.setNameLocaleIndependent("IconPin");
				// handle.addPin(plug);
				handle.addPlug(plug);

				plug.addConnectionListener(new ConnectionListener() {
					public void handleConnectionEvent(ConnectionEvent e) {
						if (e.getType() == Plug.OUTPUT_CONNECTION) {
							pinDisconnected=false;
							if (realTimeUpdate == false)
								sendButton.setVisible(true);
							else {
								sendButton.setVisible(false);
								sendIcon();
							}
						}
						if (e.getType() == Plug.INPUT_CONNECTION) {
							IconSO so=(IconSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
							Icon ico=so.getIconSO();
							if (ico != null) {
								BufferedImage img=new BufferedImage(ico.getIconWidth(),ico.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
								ico.paintIcon(ImageEditor.this,img.getGraphics(),0,0);
								if (!openImage(img,false))
									return;
							}
							autoResize();
						}
					}
				});
				plug.addDisconnectionListener(new DisconnectionListener() {
					public void handleDisconnectionEvent(DisconnectionEvent e) {
						if (sendButton != null && !plug.hasDependentsConnected()) {
							sendButton.setVisible(false);
							pinDisconnected=true;
						}
					}
				});
			}
			// catch (InvalidPinParametersException e) {}
			catch (InvalidPlugParametersException e) {
			}
			// catch (PinExistsException e) {}
			catch (PlugExistsException e) {
			}
			add(BorderLayout.NORTH,handle.getMenuPanel());
		}
		return handle;
	} // getESlateHandle

	public void sendIcon() {
		ImageIcon pic=new ImageIcon(canvas.getImage());
		// ((ImageIconSO)iconSO).setImageIcon(pic);
		((IconSO) iconSO).setIconSO(pic);
		autoResize();
	}// sendIcon

	// ////////////////////////////////////
	// ***** Property Editor's set - is methods
	private void toolbarHandle() {
		boolean placeTheSpace1=toolbarMap[1];
		boolean placeTheSpace3=toolbarMap[3];
		toolbarPanel.removeAll();
		for (int i=0;i < toolbarMap.length;i++) {
			if (toolbarMap[i]) {
				if (i == 0) {
					toolbarPanel.add(newFileButton);
					toolbarPanel.add(openButton);
					toolbarPanel.add(saveButton);
					if (!placeTheSpace1)
						toolbarPanel.add(Box.createHorizontalStrut(20));
				}
				if (i == 1) {
					toolbarPanel.add(clearButton);
					toolbarPanel.add(imageSize);
					toolbarPanel.add(cropButton);
					toolbarPanel.add(Box.createHorizontalStrut(20));
				}
				if (i == 2) {
					toolbarPanel.add(undoButton);
					if (!placeTheSpace3)
						toolbarPanel.add(Box.createHorizontalStrut(20));
				}
				if (i == 3) {
					toolbarPanel.add(cutButton);
					toolbarPanel.add(copyButton);
					toolbarPanel.add(pasteButton);
					toolbarPanel.add(Box.createHorizontalStrut(20));
				}
				if (i == 4) {
					toolbarPanel.add(zoomButton);
					toolbarPanel.add(unzoomButton);
					toolbarPanel.add(panButton);
					toolbarPanel.add(Box.createHorizontalStrut(20));
				}
				if (i == 5) {
					toolbarPanel.add(Box.createHorizontalStrut(2));
					toolbarPanel.add(gridButton);
				}
				if (i == 6) {
					toolbarPanel.add(selectButton);
				}
				if (i == 7) {
					toolbarPanel.add(magicWandButton);
					toolbarPanel.add(invertSelection);
					toolbarPanel.add(clearSelection);
					toolbarPanel.add(fillSelection);
				}
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						autoResize();
					}
				});
			}
			toolbarPanel.add(sendButton);
			if (pinDisconnected)
				sendButton.setVisible(false);
			mainPanel.repaint();
			mainPanel.revalidate();
		}
	}// toolbarHandle

	// set - is methods for configuring iconeditor's toolbar layout
	// 1st team [buttons new-load-save]
	public void setNewOpenSaveVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[0]=true;
			newFileButton.setVisible(true);
		} else {
			toolbarMap[0]=false;
			newFileButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isNewOpenSaveVisible() {
		return newFileButton.isVisible();
	}

	// 2nd team [buttons clear-imageSize]
	public void setClearDimensionCropVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[1]=true;
			clearButton.setVisible(true);
		} else {
			toolbarMap[1]=false;
			clearButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isClearDimensionCropVisible() {
		return clearButton.isVisible();
	}

	// 3rd team [button undo]
	public void setUndoButtonVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[2]=true;
			undoButton.setVisible(true);
		} else {
			toolbarMap[2]=false;
			undoButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isUndoButtonVisible() {
		return undoButton.isVisible();
	}

	// 4th team [buttons cut-copy-paste]
	public void setCutCopyPasteVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[3]=true;
			cutButton.setVisible(true);
		} else {
			toolbarMap[3]=false;
			cutButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isCutCopyPasteVisible() {
		return cutButton.isVisible();
	}

	// 5th team [buttons zoom-unzoom-pan]
	public void setZoomUnzoomPanVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[4]=true;
			zoomButton.setVisible(true);
		} else {
			toolbarMap[4]=false;
			zoomButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isZoomUnzoomPanVisible() {
		return zoomButton.isVisible();
	}

	// 6th team [button grid]
	public void setGridButtonVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[5]=true;
			gridButton.setVisible(true);
		} else {
			toolbarMap[5]=false;
			gridButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isGridButtonVisible() {
		return gridButton.isVisible();
	}

	// 7th team [button select]
	public void setSelectButtonVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[6]=true;
			selectButton.setVisible(true);
		} else {
			toolbarMap[6]=false;
			selectButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isSelectButtonVisible() {
		return selectButton.isVisible();
	}

	// 8th team [buttons magicWand - invertSelection]
	public void setWandInvertClearSVisible(boolean visibility) {
		if (visibility == true) {
			toolbarMap[7]=true;
			magicWandButton.setVisible(true);
		} else {
			toolbarMap[7]=false;
			magicWandButton.setVisible(false);
		}
		toolbarHandle();
		componentModified=true;
	}

	public boolean isWandInvertClearSVisible() {
		return magicWandButton.isVisible();
	}

	// -----------------------------------------
	private void rightPanelHandle() {
		rightPanel.removeAll();
		for (int i=0;i < rightPanelMap.length;i++) {
			if (rightPanelMap[i]) {
				if (i == 0) {
					rightPanel.add(previewPanel);
					rightPanel.add(Box.createVerticalStrut(4));
				}
				if (i == 1) {
					rightPanel.add(drawing_SettingsContainer);
				}
				if (i == 2) {
					rightPanel.add(tabbedPane);
					tabbedPane.repaint();
				}
			}
		}
		mainPanel.repaint();
		mainPanel.revalidate();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoResize();
			}
		});
	}// rightPanelHandle

	// inner abstract class MyRunnable
	/*
	 * private abstract class MyRunnable implements Runnable{ public MyRunnable(){ } }//MyRunnable
	 */

	// set - is methods for configuring iconeditor's buttonPanel layout
	// 1st team previewPanel
	public void setPreviewVisible(boolean visibility) {
		if (visibility == true) {
			rightPanelMap[0]=true;
			previewPanel.setVisible(true);
		} else {
			rightPanelMap[0]=false;
			previewPanel.setVisible(false);
		}
		rightPanelHandle();
		componentModified=true;
	}

	public boolean isPreviewVisible() {
		return previewPanel.isVisible();
	}

	// 2nd team penAndPickPanel
	public void setDrawingSettingsVisible(boolean visibility) {
		if (visibility == true) {
			rightPanelMap[1]=true;
			drawing_SettingsContainer.setVisible(true);
		} else {
			rightPanelMap[1]=false;
			drawing_SettingsContainer.setVisible(false);
		}
		rightPanelHandle();
		componentModified=true;
	}

	public boolean isDrawingSettingsVisible() {
		return drawing_SettingsContainer.isVisible();
	}

	// 3rd team tabbedPane [icon-color Palette]
	public void setPalettesVisible(boolean visibility) {
		if (visibility == true) {
			rightPanelMap[2]=true;
			tabbedPane.setVisible(true);
		} else {
			rightPanelMap[2]=false;
			tabbedPane.setVisible(false);
		}
		rightPanelHandle();
		componentModified=true;
	}

	public boolean isPalettesVisible() {
		return tabbedPane.isVisible();
	}

	public void setMenuVisible(boolean visibility) {
		if (visibility == true) {
			toolbar_MenuPanel.removeAll();
			menuContainer.setVisible(false);
			toolbarContainer.setVisible(false);
			toolbar_MenuPanel.add(menuContainer);
			menuContainer.setVisible(true);
			toolbar_MenuPanel.add(Box.createVerticalStrut(1));
			toolbar_MenuPanel.add(toolbarContainer);
			toolbarContainer.setVisible(true);
		} else {
			toolbar_MenuPanel.removeAll();
			menuContainer.setVisible(false);
			toolbarContainer.setVisible(false);
			toolbar_MenuPanel.add(toolbarContainer);
			toolbarContainer.setVisible(true);
		}

		if (pinDisconnected)
			sendButton.setVisible(false);

		mainPanel.repaint();
		mainPanel.revalidate();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoResize();
			}
		});
		componentModified=true;
	}

	public boolean isMenuVisible() {
		return menuContainer.isVisible();
	}

	public void setRealTimeUpdate(boolean realTime) {
		realTimeUpdate=realTime;
		if (realTimeUpdate == true)
			sendButton.setVisible(false);
		else
			sendButton.setVisible(true);
		componentModified=true;
	}

	public boolean isRealTimeUpdate() {
		return realTimeUpdate;
	}

	public void setStatusBarVisible(boolean visibility) {
		if (visibility == false) {
			statusBar.setVisible(false);
			mainPanel.remove(statusBar);
		} else {
			mainPanel.add(statusBar,BorderLayout.SOUTH);
			statusBar.setVisible(true);
		}
		mainPanel.repaint();
		mainPanel.revalidate();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoResize();
			}
		});
		componentModified=true;
	}

	public boolean isStatusBarVisible() {
		return statusBar.isVisible();
	}

	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		Object firstObj=in.readObject();
		if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
			// oldreadExternal
			ImageIcon img=(ImageIcon) firstObj;// (ImageIcon)in.readObject();
			openImage(img.getImage(),false);
			setNewOpenSaveVisible(in.readBoolean());
			setClearDimensionCropVisible(in.readBoolean());
			setUndoButtonVisible(in.readBoolean());
			setCutCopyPasteVisible(in.readBoolean());
			setZoomUnzoomPanVisible(in.readBoolean());
			setGridButtonVisible(in.readBoolean());
			setSelectButtonVisible(in.readBoolean());
			setWandInvertClearSVisible(in.readBoolean());
			setPreviewVisible(in.readBoolean());
			setDrawingSettingsVisible(in.readBoolean());
			setPalettesVisible(in.readBoolean());
			setRealTimeUpdate(in.readBoolean());
			pinDisconnected=in.readBoolean();
			ArrayList icons=(ArrayList) in.readObject();
			int numberOfIcons=in.readInt();
			iconPalette.restorePalette(numberOfIcons,icons);
			int tabSelected=in.readInt();
			tabbedPane.setSelectedIndex(tabSelected);
			setMenuVisible(in.readBoolean());
			setVisible(in.readBoolean());
			setStatusBarVisible(in.readBoolean());
			sendButton.setVisible(in.readBoolean());
			preview.setPreviewImage(canvas.getImage());
		} else {
			// ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
			StorageStructure fieldMap=(StorageStructure) firstObj;

			NewRestorableImageIcon img;
			if (fieldMap.getDataVersion().equals("ESlateImageEditor2.0.2") || fieldMap.getDataVersion().equals("ESlateImageEditor2")) {
				NewRestorableImageIcon imgTemp=(NewRestorableImageIcon) fieldMap.get("MainImage");
				img=new NewRestorableImageIcon(imgTemp.getImage());
			} else
				img=(NewRestorableImageIcon) fieldMap.get("MainImage");

			openImage(img.getImage(),false);
			setNewOpenSaveVisible(fieldMap.get("NewOpenSaveVisible",true));
			setClearDimensionCropVisible(fieldMap.get("ClearDimensionCropVisible",true));
			setUndoButtonVisible(fieldMap.get("UndoButtonVisible",true));
			setCutCopyPasteVisible(fieldMap.get("CutCopyPasteVisible",true));
			setZoomUnzoomPanVisible(fieldMap.get("ZoomUnzoomPanVisible",true));
			setGridButtonVisible(fieldMap.get("GridButtonVisible",true));
			setSelectButtonVisible(fieldMap.get("SelectButtonVisible",true));
			setWandInvertClearSVisible(fieldMap.get("WandInvertClearSVisible",true));
			setPreviewVisible(fieldMap.get("PreviewVisible",true));
			setDrawingSettingsVisible(fieldMap.get("DrawingSettingsVisible",true));
			setPalettesVisible(fieldMap.get("PalettesVisible",true));
			setRealTimeUpdate(fieldMap.get("RealTimeUpdate",true));
			pinDisconnected=fieldMap.get("PinDisconnected",true);

			ArrayList icons=(ArrayList) fieldMap.get("PaletteIcons");
			int numberOfIcons=fieldMap.get("NumberOfIcons",10);
			iconPalette.restorePalette(numberOfIcons,icons);
			int tabSelected=fieldMap.get("TabSelected",0);
			tabbedPane.setSelectedIndex(tabSelected);
			setMenuVisible(fieldMap.get("MenuVisible",true));
			setVisible(fieldMap.get("ImageEditorVisible",true));
			setStatusBarVisible(fieldMap.get("StatusBarVisible",true));
			sendButton.setVisible(fieldMap.get("SendButton",true));

			colorPalette.restorePalette((ArrayList) fieldMap.get("ColorPalette"));

			preview.setPreviewImage(canvas.getImage());
			componentModified=false;
			colorPalette.isPaletteModified=false;
			iconPalette.isPaletteModified=false;

			/*
			 * colorPalettePanel.remove(colorPalette); colorPalette = (ColorPalette)fieldMap.get("ColorPalette");
			 * colorPalettePanel.addPalette(colorPalette);
			 */

			BorderDescriptor bd=(BorderDescriptor) fieldMap.get("BorderDescriptor");
			// if (bd != null){
			try {
				setBorder(bd.getBorder());
			} catch (Throwable thr) {
			}
			// }

			try {
				if (Integer.valueOf(fieldMap.getDataVersion()).intValue() >= 209)
					zoomOperation("fix",fieldMap.get("ScalingFactor",10));
				if (Integer.valueOf(fieldMap.getDataVersion()).intValue() >= 210)
					setBackgroundColor((Color) fieldMap.get("BackgroundColor"));
				// setForegroundColor((Color)fieldMap.get("ForegroundColor"));
			} catch (NumberFormatException e) { // in older versions data format was a string of type ESlateImageEditor
				// ...
				System.out.println("old version of Image Editor");
			}

		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 fieldMap=new ESlateFieldMap2(STR_FORMAT_VERSION);
		BufferedImage iEditorsImage=canvas.getImage();
		// fieldMap.put("MainImage", new RestorableImageIcon(iEditorsImage));
		fieldMap.put("MainImage",new NewRestorableImageIcon(iEditorsImage));
		fieldMap.put("NewOpenSaveVisible",isNewOpenSaveVisible());
		fieldMap.put("ClearDimensionCropVisible",isClearDimensionCropVisible());
		fieldMap.put("UndoButtonVisible",isUndoButtonVisible());
		fieldMap.put("CutCopyPasteVisible",isCutCopyPasteVisible());
		fieldMap.put("ZoomUnzoomPanVisible",isZoomUnzoomPanVisible());
		fieldMap.put("GridButtonVisible",isGridButtonVisible());
		fieldMap.put("SelectButtonVisible",isSelectButtonVisible());
		fieldMap.put("WandInvertClearSVisible",isWandInvertClearSVisible());
		fieldMap.put("PreviewVisible",isPreviewVisible());
		fieldMap.put("DrawingSettingsVisible",isDrawingSettingsVisible());
		fieldMap.put("PalettesVisible",isPalettesVisible());
		fieldMap.put("RealTimeUpdate",isRealTimeUpdate());
		fieldMap.put("PinDisconnected",pinDisconnected);
		fieldMap.put("PaletteIcons",iconPalette.saveIconsToStream());
		fieldMap.put("NumberOfIcons",iconPalette.getIconsCount());
		fieldMap.put("TabSelected",tabbedPane.getSelectedIndex());
		fieldMap.put("MenuVisible",isMenuVisible());
		fieldMap.put("ImageEditorVisible",isVisible());
		fieldMap.put("StatusBarVisible",isStatusBarVisible());
		fieldMap.put("SendButton",sendButton.isVisible());

		fieldMap.put("ColorPalette",colorPalette.paletteToList());

		// fieldMap.put("ColorPalette", colorPalette);

		// if (getBorder() != null) {
		try {
			BorderDescriptor bd=ESlateUtils.getBorderDescriptor(getBorder(),this);
			fieldMap.put("BorderDescriptor",bd);
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		// }

		fieldMap.put("ScalingFactor",(int) (canvas.getScalingFactor() + 1));

		fieldMap.put("BackgroundColor",backColor);
		// fieldMap.put("ForegroundColor", foreColor);

		out.writeObject(fieldMap);
		out.flush();
		iEditorsImage.flush();
		componentModified=false;
		colorPalette.isPaletteModified=false;
		iconPalette.isPaletteModified=false;

		/*
		 * BufferedImage iEditorsImage = canvas.getImage(); out.writeObject(new ImageIcon(iEditorsImage));
		 * out.writeBoolean(isNewOpenSaveVisible()); out.writeBoolean(isClearDimensionCropVisible());
		 * out.writeBoolean(isUndoButtonVisible()); out.writeBoolean(isCutCopyPasteVisible());
		 * out.writeBoolean(isZoomUnzoomPanVisible()); out.writeBoolean(isGridButtonVisible());
		 * out.writeBoolean(isSelectButtonVisible()); out.writeBoolean(isWandInvertClearSVisible());
		 * out.writeBoolean(isPreviewVisible()); out.writeBoolean(isDrawingSettingsVisible());
		 * out.writeBoolean(isPalettesVisible()); out.writeBoolean(isRealTimeUpdate());
		 * out.writeBoolean(pinDisconnected); out.writeObject(iconPalette.saveIconsToStream());
		 * out.writeInt(iconPalette.getIconsCount()); out.writeInt(tabbedPane.getSelectedIndex());
		 * out.writeBoolean(isMenuVisible()); out.writeBoolean(isVisible()); out.writeBoolean(isStatusBarVisible());
		 * out.writeBoolean(sendButton.isVisible()); out.flush();
		 */
	}

	private void drawInitialImage(BufferedImage img) {
		Graphics2D g=(Graphics2D) img.getGraphics();
		g.setBackground(backColor);
		g.clearRect(0,0,xSize,ySize);
		currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);
		g.dispose();
	}

	private void addButtons() {
		// Adding ToggleButtons to the Button Group
		bGroup.add(selectButton);
		bGroup.add(cropButton);
		bGroup.add(shapesButton);
		bGroup.add(magicWandButton);
		bGroup.add(rubberButton);
		bGroup.add(flatFillButton);
		bGroup.add(pickerButton);
		bGroup.add(panButton);

		// Adding buttons to the panels
		toolbarPanel.add(newFileButton);
		toolbarPanel.add(openButton);
		toolbarPanel.add(saveButton);

		toolbarPanel.add(clearButton);
		toolbarPanel.add(imageSize);
		toolbarPanel.add(cropButton);

		toolbarPanel.add(Box.createHorizontalStrut(20));

		toolbarPanel.add(undoButton);
		toolbarPanel.add(cutButton);
		toolbarPanel.add(copyButton);
		toolbarPanel.add(pasteButton);

		// space1 = Box.createHorizontalStrut(20);
		toolbarPanel.add(Box.createHorizontalStrut(20));

		toolbarPanel.add(zoomButton);
		toolbarPanel.add(unzoomButton);
		toolbarPanel.add(panButton);

		// space2 = Box.createHorizontalStrut(20);
		toolbarPanel.add(Box.createHorizontalStrut(20));

		toolbarPanel.add(gridButton);
		toolbarPanel.add(selectButton);
		toolbarPanel.add(magicWandButton);
		toolbarPanel.add(invertSelection);
		toolbarPanel.add(clearSelection);
		toolbarPanel.add(fillSelection);

		clearSelection.setEnabled(false);
		fillSelection.setEnabled(false);
		invertSelection.setEnabled(false);

		// space3 = Box.createHorizontalStrut(20);
		toolbarPanel.add(Box.createHorizontalStrut(20));

		toolbarPanel.add(sendButton);
		sendButton.setVisible(false);
		// panButton.setEnabled(false);

		rightPanel.add(previewPanel);
		rightPanel.add(Box.createVerticalStrut(4));

		// The panel that contains the drawing tools and the colors previewer
		drawingPanel=new JPanel();
		drawingPanel.setLayout(new BoxLayout(drawingPanel,BoxLayout.Y_AXIS));
		drawingPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH / 2,130));
		drawingPanel.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH / 2,130));
		drawingPanel.setMinimumSize(new Dimension(RIGHT_PANEL_WIDTH / 2,130));
		// drawingPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

		drawingPanel.add(Box.createHorizontalStrut(2));

		JPanel shapesContainer=new JPanel();
		shapesContainer.setLayout(new BoxLayout(shapesContainer,BoxLayout.X_AXIS));
		shapesContainer.add(Box.createHorizontalGlue());
		shapesContainer.add(shapesButton);
		shapesContainer.add(Box.createHorizontalGlue());
		drawingPanel.add(shapesContainer);
		drawingPanel.add(Box.createHorizontalStrut(3));

		JPanel flatFillContainer=new JPanel();
		flatFillContainer.setLayout(new BoxLayout(flatFillContainer,BoxLayout.X_AXIS));
		flatFillContainer.add(Box.createHorizontalGlue());
		flatFillContainer.add(flatFillButton);
		flatFillContainer.add(Box.createHorizontalGlue());
		drawingPanel.add(flatFillContainer);

		JPanel rubberContainer=new JPanel();
		rubberContainer.setLayout(new BoxLayout(rubberContainer,BoxLayout.X_AXIS));
		rubberContainer.add(Box.createHorizontalGlue());
		rubberContainer.add(rubberButton);
		rubberContainer.add(Box.createHorizontalGlue());
		drawingPanel.add(rubberContainer);
		drawingPanel.add(Box.createHorizontalStrut(3));

		JPanel pickerContainer=new JPanel();
		pickerContainer.setLayout(new BoxLayout(pickerContainer,BoxLayout.X_AXIS));
		pickerContainer.add(Box.createHorizontalGlue());
		pickerContainer.add(pickerButton);
		pickerContainer.add(Box.createHorizontalGlue());
		drawingPanel.add(rubberContainer);
		// drawingPanel.add(pickerButton);
		drawingPanel.add(Box.createHorizontalStrut(3));

		// help panel for justifying previewColorPanels
		JPanel colorPanelsContainer=new JPanel();
		colorPanelsContainer.setLayout(new BoxLayout(colorPanelsContainer,BoxLayout.X_AXIS));
		colorPanelsContainer.add(Box.createHorizontalGlue());
		colorPanelsContainer.add(previewColorPanels);
		colorPanelsContainer.add(Box.createHorizontalGlue());
		drawingPanel.add(colorPanelsContainer);

		drawing_SettingsContainer=new JPanel();
		drawing_SettingsContainer.setLayout(new BoxLayout(drawing_SettingsContainer,BoxLayout.X_AXIS));
		drawing_SettingsContainer.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH,130));
		drawing_SettingsContainer.setMinimumSize(new Dimension(RIGHT_PANEL_WIDTH,130));
		drawing_SettingsContainer.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH,130));

		drawing_SettingsContainer.add(drawingPanel);
		drawing_SettingsContainer.add(stylePanel);

		// * Create a JTabbedPane and add the color Palette
		tabbedPane=new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH - 5,220));
		tabbedPane.setMaximumSize(new Dimension(RIGHT_PANEL_WIDTH - 5,220));
		tabbedPane.setMinimumSize(new Dimension(RIGHT_PANEL_WIDTH - 5,220));
		tabbedPane.setFont(dialogFont);

		colorPalettePanel=new ScrollPanel(RIGHT_PANEL_WIDTH - 5,170);
		colorPalettePanel.addPalette(colorPalette);

		iconPalettePanel=new ScrollPanel(RIGHT_PANEL_WIDTH - 5,170);

		iconPalettePanel.addPalette(iconPalette);

		tabbedPane.add(iEBundle.getString("colorPalette"),colorPalettePanel);
		tabbedPane.addTab(iEBundle.getString("iconPalette"),iconPalettePanel);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == 1) {
					iconPaletteMenu.setEnabled(true);
					colorPaletteMenu.setEnabled(false);
				} else {
					iconPaletteMenu.setEnabled(false);
					colorPaletteMenu.setEnabled(true);
				}
			}
		});

		preview.enableIconPaletteSupport(iconPalette);
		rightPanel.add(drawing_SettingsContainer);
		rightPanel.add(tabbedPane);
		rightPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		mainPanel.add(BorderLayout.EAST,rightPanel);

		// status bar
		currentPosField=new JTextField(iEBundle.getString("cell") + " 1,1");
		currentPosField.setEditable(false);
		currentPosField.setBackground(statusBar.getBackground());
		currentImageSizeField=new JTextField(iEBundle.getString("imSize") + " 32 x 32");
		currentImageSizeField.setEditable(false);
		currentImageSizeField.setBackground(statusBar.getBackground());
		currentModeField=new JTextField(iEBundle.getString("pencilMode"));
		currentModeField.setEditable(false);
		currentModeField.setBackground(statusBar.getBackground());

		statusBar.add(currentPosField);
		statusBar.add(currentImageSizeField);
		statusBar.add(currentModeField);
		mainPanel.add(statusBar,BorderLayout.SOUTH);
	}

	public void autoEnablePanButton() {
		extentSize=mainScrollingArea.getViewport().getExtentSize();
		if ((xSize - 1) * canvas.getScalingFactor() <= extentSize.width && (ySize - 1) * canvas.getScalingFactor() <= extentSize.height) {
			if (mode == PAN_MODE) {
				setMode(PEN);
				shapesButton.setSelected(true);
			}
			panButton.setEnabled(false);
		} else
			panButton.setEnabled(true);
	}

	private void enableRectStyles() {
		stylePanel.remove(0);
		stylePanel.add(rectStylesPanel,0);
		unselectAllRect();
		rStyle1.setSelected(true);
		fillStyle=rStyle1.getSkin();

		rStyle1.setDashed(selectedArea.isDashed());
		rStyle2.setDashed(selectedArea.isDashed());
		rStyle3.setDashed(selectedArea.isDashed());

		stylePanel.revalidate();
		stylePanel.repaint();
	}

	private void enableEllipseStyles() {
		stylePanel.remove(0);
		stylePanel.add(ellipseStylesPanel,0);
		unselectAllEllipse();
		ellStyle1.setSelected(true);
		fillStyle=ellStyle1.getSkin();

		ellStyle1.setDashed(selectedArea.isDashed());
		ellStyle2.setDashed(selectedArea.isDashed());
		ellStyle3.setDashed(selectedArea.isDashed());

		stylePanel.revalidate();
		stylePanel.repaint();
	}

	private void enablePolyStyles() {
		stylePanel.remove(0);
		stylePanel.add(polyStylesPanel,0);
		unselectAllPoly();
		polyStyle1.setSelected(true);
		fillStyle=polyStyle1.getSkin();

		polyStyle1.setDashed(selectedArea.isDashed());
		polyStyle2.setDashed(selectedArea.isDashed());
		polyStyle3.setDashed(selectedArea.isDashed());

		stylePanel.revalidate();
		stylePanel.repaint();
	}

	private void enableLineWidths() {
		stylePanel.remove(0);
		stylePanel.add(lineWidthsPanel,0);
		selectArea(selectedArea);
		stylePanel.revalidate();
		stylePanel.repaint();
	}

	public void invertSelectedArea() {
		canvas.invertSelection();
	}

	private void createRightPanelElements() {
		// The shapes Button
		shapesButton=new NoBorderToggleButton(menuPen);
		shapesButton.setMaximumSize(buttonDimension);
		shapesButton.setMinimumSize(buttonDimension);
		shapesButton.setPreferredSize(buttonDimension);
		shapesButton.setAlignmentY(CENTER_ALIGNMENT);
		shapesButton.setMargin(new Insets(0,0,0,0));
		shapesButton.setSelected(true);
		shapesButton.setToolTipText(iEBundle.getString("IconEditorMsg27"));
		shapesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (shapesChoice == 1) {
					enableLineWidths();
					penSelected();
					penMenu.setSelected(true);
				}
				if (shapesChoice == 2) {
					enableLineWidths();
					lineSelected();
					lineMenu.setSelected(true);
				}
				if (shapesChoice == 3) {
					enableRectStyles();
					rectSelected();
					rectMenu.setSelected(true);
				}
				if (shapesChoice == 4) {
					enableEllipseStyles();
					ellipseSelected();
					ellipseMenu.setSelected(true);
				}
				if (shapesChoice == 5) {
					enablePolyStyles();
					polySelected();
					polyMenu.setSelected(true);
				}
			}
		});
		shapesButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				popupTimer=new javax.swing.Timer(400,shapesPopupTimerAction);
				popupTimer.setRepeats(false);
				popupTimer.start();
			}

			public void mouseReleased(MouseEvent e) {
				popupTimer.stop();
			}
		});
		shapesPopupTimerAction=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupTimer.stop();
				JPopupMenu popup=new JPopupMenu();

				final JMenuItem penItem=new JMenuItem("Pen",pen);
				final JMenuItem lineItem=new JMenuItem("Line",line);
				final JMenuItem rectItem=new JMenuItem("Rectangle",rect);
				final JMenuItem ellItem=new JMenuItem("Ellipse",ellip);
				final JMenuItem polyItem=new JMenuItem("Polygon",poly);

				if (shapesButton.getIcon().equals(menuPen)) {
					popup.add(lineItem);
					popup.add(rectItem);
					popup.add(ellItem);
					popup.add(polyItem);
				} else if (shapesButton.getIcon().equals(menuLine)) {
					popup.add(penItem);
					popup.add(rectItem);
					popup.add(ellItem);
					popup.add(polyItem);
				} else if (shapesButton.getIcon().equals(menuRect)) {
					popup.add(penItem);
					popup.add(lineItem);
					popup.add(ellItem);
					popup.add(polyItem);
				} else if (shapesButton.getIcon().equals(menuEllip)) {
					popup.add(penItem);
					popup.add(lineItem);
					popup.add(rectItem);
					popup.add(polyItem);
				} else {
					popup.add(penItem);
					popup.add(lineItem);
					popup.add(rectItem);
					popup.add(ellItem);
				}

				popup.show(shapesButton,0,buttonDimension.height - 1);

				penItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						shapesButton.setIcon(menuPen);
						shapesChoice=1;
						enableLineWidths();
						shapesButton.setToolTipText(iEBundle.getString("IconEditorMsg27"));
						shapesButton.setSelected(true);
						penSelected();
						penMenu.setSelected(true);
						if (shapesButton.isSelected()) {
							penSelected();
							penMenu.setSelected(true);
						}
					}
				});
				lineItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						shapesButton.setIcon(menuLine);
						shapesChoice=2;
						enableLineWidths();
						shapesButton.setSelected(true);
						lineSelected();
						lineMenu.setSelected(true);
						shapesButton.setToolTipText(iEBundle.getString("line"));
						if (shapesButton.isSelected()) {
							lineSelected();
							lineMenu.setSelected(true);
						}
					}
				});

				rectItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						shapesButton.setIcon(menuRect);
						shapesChoice=3;
						enableRectStyles();
						shapesButton.setSelected(true);
						rectSelected();
						rectMenu.setSelected(true);
						shapesButton.setToolTipText(iEBundle.getString("rectangle"));
						if (shapesButton.isSelected()) {
							rectSelected();
							rectMenu.setSelected(true);
						}
					}
				});
				ellItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						shapesButton.setIcon(menuEllip);
						shapesChoice=4;
						enableEllipseStyles();
						shapesButton.setSelected(true);
						ellipseSelected();
						ellipseMenu.setSelected(true);
						shapesButton.setToolTipText(iEBundle.getString("ellipse"));
						if (shapesButton.isSelected()) {
							ellipseSelected();
							ellipseMenu.setSelected(true);
						}
					}
				});
				polyItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						shapesButton.setIcon(menuPoly);
						shapesChoice=5;
						enablePolyStyles();
						shapesButton.setSelected(true);
						polySelected();
						polyMenu.setSelected(true);
						shapesButton.setToolTipText(iEBundle.getString("poly"));
						if (shapesButton.isSelected()) {
							polySelected();
							polyMenu.setSelected(true);
						}
					}
				});
			}
		};
		shapesButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!shapesButton.isSelected() && !rubberButton.isSelected())
					stylePanel.setVisible(false);
				else
					stylePanel.setVisible(true);
			}
		});

		/*
		 * Icon arrow = loadImageIcon("Images/menuArrow.gif"," "); JButton menuButton = new JButton(arrow);
		 * shapesButton.setLayout(null); shapesButton.add(menuButton); menuButton.setBounds(new
		 * Rectangle(buttonDimension.width-8,0,7,buttonDimension.height)); menuButton.setMargin(new Insets(0, 0, 0, 0));
		 * menuButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { JPopupMenu
		 * popup = new JPopupMenu(); popup.setPreferredSize(new Dimension(buttonDimension.width,
		 * 5*buttonDimension.height+5));
		 * 
		 * EnhancedMenuItem penItem = new EnhancedMenuItem(menuPen); EnhancedMenuItem lineItem = new
		 * EnhancedMenuItem(menuLine); EnhancedMenuItem rectItem = new EnhancedMenuItem(menuRect); EnhancedMenuItem
		 * ellItem = new EnhancedMenuItem(menuEllip); EnhancedMenuItem polyItem = new EnhancedMenuItem(menuPoly);
		 * 
		 * popup.add(penItem); popup.add(lineItem); popup.add(rectItem); popup.add(ellItem); popup.add(polyItem);
		 * 
		 * popup.show(shapesButton,0, buttonDimension.height-1);
		 * 
		 * penItem.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e){
		 * shapesButton.setIcon(menuPen); shapesChoice = 1; enableLineWidths();
		 * shapesButton.setToolTipText(iEBundle.getString("IconEditorMsg27")); shapesButton.setSelected(true);
		 * penSelected(); penMenu.setSelected(true); if (shapesButton.isSelected())
		 * {penSelected();penMenu.setSelected(true);} } }); lineItem.addMouseListener(new MouseAdapter() { public void
		 * mousePressed(MouseEvent e){ shapesButton.setIcon(menuLine); shapesChoice = 2; enableLineWidths();
		 * shapesButton.setSelected(true); lineSelected(); lineMenu.setSelected(true);
		 * shapesButton.setToolTipText(iEBundle.getString("line")); if (shapesButton.isSelected()) {lineSelected();
		 * lineMenu.setSelected(true);} } });
		 * 
		 * rectItem.addMouseListener(new MouseAdapter() { public void mousePressed(MouseEvent e){
		 * shapesButton.setIcon(menuRect); shapesChoice = 3; enableRectStyles(); shapesButton.setSelected(true);
		 * rectSelected();rectMenu.setSelected(true); shapesButton.setToolTipText(iEBundle.getString("rectangle")); if
		 * (shapesButton.isSelected()){ rectSelected();rectMenu.setSelected(true);} } }); ellItem.addMouseListener(new
		 * MouseAdapter() { public void mousePressed(MouseEvent e){ shapesButton.setIcon(menuEllip); shapesChoice = 4;
		 * enableEllipseStyles(); shapesButton.setSelected(true); ellipseSelected();ellipseMenu.setSelected(true);
		 * shapesButton.setToolTipText(iEBundle.getString("ellipse")); if (shapesButton.isSelected())
		 * {ellipseSelected();ellipseMenu.setSelected(true);} } }); polyItem.addMouseListener(new MouseAdapter() {
		 * public void mousePressed(MouseEvent e){ shapesButton.setIcon(menuPoly); shapesChoice = 5; enablePolyStyles();
		 * shapesButton.setSelected(true); polySelected();polyMenu.setSelected(true);
		 * shapesButton.setToolTipText(iEBundle.getString("poly")); if (shapesButton.isSelected())
		 * {polySelected();polyMenu.setSelected(true);} } }); } });
		 */

		// The rubber button
		rubberButton=new NoBorderToggleButton(rubber);
		rubberButton.setFont(dialogFont);
		rubberButton.setForeground(buttonFrgrd);
		rubberButton.setToolTipText(iEBundle.getString("IconEditorMsg26"));
		rubberButton.setMaximumSize(buttonDimension);
		rubberButton.setMinimumSize(buttonDimension);
		rubberButton.setPreferredSize(buttonDimension);
		rubberButton.setAlignmentY(CENTER_ALIGNMENT);
		rubberButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rubberSelected();
				rubberMenu.setSelected(true);
			}
		});
		rubberButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!rubberButton.isSelected() && !shapesButton.isSelected())
					stylePanel.setVisible(false);
				else
					stylePanel.setVisible(true);
			}
		});

		// The flatFill button
		flatFillButton=new NoBorderToggleButton(flatFill);
		flatFillButton.setFont(dialogFont);
		flatFillButton.setForeground(buttonFrgrd);
		flatFillButton.setToolTipText(iEBundle.getString("fFill"));
		flatFillButton.setMaximumSize(buttonDimension);
		flatFillButton.setMinimumSize(buttonDimension);
		flatFillButton.setPreferredSize(buttonDimension);
		flatFillButton.setAlignmentY(CENTER_ALIGNMENT);
		flatFillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flatFillSelected();
				flatFillMenu.setSelected(true);
			}
		});

		// The fill selection button
		fillSelection=new NoBorderButton(fill);
		fillSelection.setFont(dialogFont);
		fillSelection.setForeground(buttonFrgrd);
		fillSelection.setToolTipText(iEBundle.getString("IconEditorMsg15"));
		fillSelection.setMaximumSize(buttonDimension);
		fillSelection.setMinimumSize(buttonDimension);
		fillSelection.setPreferredSize(buttonDimension);
		fillSelection.setAlignmentY(CENTER_ALIGNMENT);
		fillSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillSelectedArea();
			}
		});

		// The picker button
		pickerButton=new NoBorderToggleButton(picker);
		pickerButton.setFont(dialogFont);
		pickerButton.setForeground(buttonFrgrd);
		pickerButton.setToolTipText(iEBundle.getString("IconEditorMsg28"));
		pickerButton.setMaximumSize(buttonDimension);
		pickerButton.setMinimumSize(buttonDimension);
		pickerButton.setPreferredSize(buttonDimension);
		pickerButton.setAlignmentY(CENTER_ALIGNMENT);
		pickerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pickSelected();
			}
		});

		// The cancel and close buttons are enabled only when iconeditor is showing as a dialog
		// The cancel button
		cancelButton=new JButton(iEBundle.getString("Cancel"));
		cancelButton.setFont(dialogFont);
		cancelButton.setMargin(new Insets(0,0,0,0));
		cancelButton.setForeground(buttonFrgrd);
		cancelButton.setMaximumSize(bigButtonDimension);
		cancelButton.setMinimumSize(bigButtonDimension);
		cancelButton.setPreferredSize(bigButtonDimension);
		cancelButton.setAlignmentX(CENTER_ALIGNMENT);

		// The close button
		okButton=new JButton(iEBundle.getString("okDialog"));
		okButton.setFont(dialogFont);
		okButton.setForeground(buttonFrgrd);
		okButton.setMaximumSize(bigButtonDimension);
		okButton.setMinimumSize(bigButtonDimension);
		okButton.setPreferredSize(bigButtonDimension);
		okButton.setAlignmentX(CENTER_ALIGNMENT);

		// The style panel the panel that contains setting options for the stroke of the drawing tools
		stylePanel=new JPanel();
		stylePanel.setLayout(new BoxLayout(stylePanel,BoxLayout.Y_AXIS));
		// stylePanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		stylePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		Dimension stylePanelDimension=new Dimension((RIGHT_PANEL_WIDTH - 5) / 2,130);
		stylePanel.setPreferredSize(stylePanelDimension);
		stylePanel.setMaximumSize(stylePanelDimension);
		stylePanel.setMinimumSize(stylePanelDimension);

		Dimension upperPanelDimension=new Dimension((RIGHT_PANEL_WIDTH - 5) / 2 - 2,90);
		// The line widths panel
		lineWidthsPanel=new JPanel();
		lineWidthsPanel.setLayout(new BoxLayout(lineWidthsPanel,BoxLayout.Y_AXIS));
		lineWidthsPanel.setPreferredSize(upperPanelDimension);
		lineWidthsPanel.setMaximumSize(upperPanelDimension);
		lineWidthsPanel.setMinimumSize(upperPanelDimension);

		// The rect styles panel
		rectStylesPanel=new JPanel();
		rectStylesPanel.setLayout(new BoxLayout(rectStylesPanel,BoxLayout.Y_AXIS));
		rectStylesPanel.setPreferredSize(upperPanelDimension);
		rectStylesPanel.setMaximumSize(upperPanelDimension);
		rectStylesPanel.setMinimumSize(upperPanelDimension);

		// The ellipse styles panel
		ellipseStylesPanel=new JPanel();
		ellipseStylesPanel.setLayout(new BoxLayout(ellipseStylesPanel,BoxLayout.Y_AXIS));
		ellipseStylesPanel.setPreferredSize(upperPanelDimension);
		ellipseStylesPanel.setMaximumSize(upperPanelDimension);
		ellipseStylesPanel.setMinimumSize(upperPanelDimension);

		// The poly styles panel
		polyStylesPanel=new JPanel();
		polyStylesPanel.setLayout(new BoxLayout(polyStylesPanel,BoxLayout.Y_AXIS));
		polyStylesPanel.setPreferredSize(upperPanelDimension);
		polyStylesPanel.setMaximumSize(upperPanelDimension);
		polyStylesPanel.setMinimumSize(upperPanelDimension);

		// Panel for stroke information (editable)
		JPanel infoPanel=new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.X_AXIS));
		Dimension infoPanelDimension=new Dimension((RIGHT_PANEL_WIDTH - 5) / 2 - 5,20);
		infoPanel.setPreferredSize(infoPanelDimension);
		infoPanel.setMaximumSize(infoPanelDimension);
		infoPanel.setMinimumSize(infoPanelDimension);
		strokeField=new JTextField(3);
		// strokeField.setEditable(false);
		strokeField.setHorizontalAlignment(SwingConstants.RIGHT);

		JPVSpinPlus spinControl=new JPVSpinPlus(JPVSpinPlus.VERTICAL_CENTER);
		spinControl.setPreferredSize(new Dimension(11,11));
		spinControl.setMinimumSize(new Dimension(11,11));
		spinControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyleArea selectedArea;
				int newWidth=1;
				if (sArea1.isSelected())
					selectedArea=sArea1;
				else if (sArea2.isSelected())
					selectedArea=sArea2;
				else if (sArea3.isSelected())
					selectedArea=sArea3;
				else if (sArea4.isSelected())
					selectedArea=sArea4;
				else if (sArea5.isSelected())
					selectedArea=sArea5;
				else
					selectedArea=sArea6;

				int previousWidth=selectedArea.getStrokeWidth();

				if (e.getModifiers() == JPVSpin.DECREMENT) {
					String textString=strokeField.getText();
					int currentText=Integer.valueOf(textString).intValue();
					currentText--;
					if (currentText <= 0)
						currentText=previousWidth;
					strokeField.setText(String.valueOf(currentText));
					newWidth=currentText;
				} else {
					String textString=strokeField.getText();
					int currentText=Integer.valueOf(textString).intValue();
					currentText++;
					if (currentText > 14)
						currentText=previousWidth;
					strokeField.setText(String.valueOf(currentText));
					newWidth=currentText;
				}
				selectedArea.setStrokeWidth(newWidth);
				canvas.setStroke(selectedArea.getStroke());
				strokeField.getCaret().setBlinkRate(0);
				strokeField.getCaret().setVisible(false);
			}
		});

		strokeField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				StyleArea selectedArea;
				int newWidth=1;
				try {
					newWidth=Integer.valueOf(strokeField.getText()).intValue();
				} catch (Exception ex) {
				}
				if (sArea1.isSelected())
					selectedArea=sArea1;
				else if (sArea2.isSelected())
					selectedArea=sArea2;
				else if (sArea3.isSelected())
					selectedArea=sArea3;
				else if (sArea4.isSelected())
					selectedArea=sArea4;
				else if (sArea5.isSelected())
					selectedArea=sArea5;
				else
					selectedArea=sArea6;

				if (newWidth >= 14 && newWidth <= 0) {
					Integer previousWidth=new Integer(selectedArea.getStrokeWidth());
					strokeField.setText(previousWidth.toString());
					return;
				}
				selectedArea.setStrokeWidth(newWidth);
				canvas.setStroke(selectedArea.getStroke());
			}
		});

		// label for px indicator after the textfield
		JLabel pixLabel=new JLabel(" p");
		pixLabel.setPreferredSize(new Dimension(8,8));
		pixLabel.setMinimumSize(new Dimension(8,8));
//		pixLabel.setFont(new Font("Helvetica",Font.PLAIN,8));
		strokeField.setText("1");
		infoPanel.add(strokeField);
		infoPanel.add(spinControl);
		infoPanel.add(pixLabel);

		// separate from the up panel
		JSeparator separator=new JSeparator(JSeparator.VERTICAL);
		Dimension separatorDimension=new Dimension((RIGHT_PANEL_WIDTH - 5) / 2 - 5,2);
		separator.setPreferredSize(separatorDimension);
		separator.setMaximumSize(separatorDimension);
		separator.setMinimumSize(separatorDimension);
		separator.setBorder(new EtchedBorder(EtchedBorder.RAISED));

		// the line styles
		sArea1=new StyleArea(1,false,0);
		sArea2=new StyleArea(3,false,0);
		sArea3=new StyleArea(5,false,0);
		sArea4=new StyleArea(10,false,0);
		sArea5=new StyleArea(1,true,0);
		sArea6=new StyleArea(3,true,0);
		selectedArea=sArea1;
		selectArea(sArea1);

		// the rectStyles
		rStyle1=new StyleArea(1,false,1);
		rStyle2=new StyleArea(1,false,2);
		rStyle3=new StyleArea(1,false,3);

		// the ellipseStyles
		ellStyle1=new StyleArea(1,false,4);
		ellStyle2=new StyleArea(1,false,5);
		ellStyle3=new StyleArea(1,false,6);

		// the polyStyles
		polyStyle1=new StyleArea(1,false,7);
		polyStyle2=new StyleArea(1,false,8);
		polyStyle3=new StyleArea(1,false,9);

		rectStylesPanel.add(rStyle1);
		rectStylesPanel.add(rStyle2);
		rectStylesPanel.add(rStyle3);

		ellipseStylesPanel.add(ellStyle1);
		ellipseStylesPanel.add(ellStyle2);
		ellipseStylesPanel.add(ellStyle3);

		polyStylesPanel.add(polyStyle1);
		polyStylesPanel.add(polyStyle2);
		polyStylesPanel.add(polyStyle3);

		lineWidthsPanel.add(sArea1);
		lineWidthsPanel.add(sArea2);
		lineWidthsPanel.add(sArea3);
		lineWidthsPanel.add(sArea4);
		lineWidthsPanel.add(sArea5);
		lineWidthsPanel.add(sArea6);

		stylePanel.add(lineWidthsPanel);
		stylePanel.add(Box.createVerticalStrut(3));
		stylePanel.add(separator);
		stylePanel.add(Box.createVerticalStrut(3));
		stylePanel.add(infoPanel);

		rStyle1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllRect();
				rStyle1.setSelected(true);
				fillStyle=rStyle1.getSkin();
			}
		});

		rStyle2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllRect();
				rStyle2.setSelected(true);
				fillStyle=rStyle2.getSkin();
			}
		});

		rStyle3.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllRect();
				rStyle3.setSelected(true);
				fillStyle=rStyle3.getSkin();
			}
		});

		ellStyle1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllEllipse();
				ellStyle1.setSelected(true);
				fillStyle=ellStyle1.getSkin();
			}
		});

		ellStyle2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllEllipse();
				ellStyle2.setSelected(true);
				fillStyle=ellStyle2.getSkin();
			}
		});

		ellStyle3.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllEllipse();
				ellStyle3.setSelected(true);
				fillStyle=ellStyle3.getSkin();
			}
		});

		polyStyle1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllPoly();
				polyStyle1.setSelected(true);
				fillStyle=polyStyle1.getSkin();
			}
		});

		polyStyle2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllPoly();
				polyStyle2.setSelected(true);
				fillStyle=polyStyle2.getSkin();
			}
		});

		polyStyle3.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				unselectAllPoly();
				polyStyle3.setSelected(true);
				fillStyle=polyStyle3.getSkin();
			}
		});

		sArea1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Integer wid=new Integer(sArea1.getStrokeWidth());
				strokeField.setText(wid.toString());
				selectArea(sArea1);
				canvas.setStroke(sArea1.getStroke());
			}
		});

		sArea2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Integer wid=new Integer(sArea2.getStrokeWidth());
				strokeField.setText(wid.toString());
				selectArea(sArea2);
				canvas.setStroke(sArea2.getStroke());
			}
		});

		sArea3.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Integer wid=new Integer(sArea3.getStrokeWidth());
				strokeField.setText(wid.toString());
				selectArea(sArea3);
				canvas.setStroke(sArea3.getStroke());
			}
		});

		sArea4.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Integer wid=new Integer(sArea4.getStrokeWidth());
				strokeField.setText(wid.toString());
				selectArea(sArea4);
				canvas.setStroke(sArea4.getStroke());
			}
		});

		sArea5.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Integer wid=new Integer(sArea5.getStrokeWidth());
				strokeField.setText(wid.toString());
				selectArea(sArea5);
				canvas.setStroke(sArea5.getStroke());
			}
		});

		sArea6.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Integer wid=new Integer(sArea6.getStrokeWidth());
				strokeField.setText(wid.toString());
				selectArea(sArea6);
				canvas.setStroke(sArea6.getStroke());
			}
		});

		// The color Palette
		colorPalette=new ColorPalette(new Dimension(RIGHT_PANEL_WIDTH - 5,230));
		colorPalette.addActiveColorListener(new ActiveColorListener() {
			public void activeColorChanged(ActiveColorEvent e) {
				if (e.isForegroundChanged()) {
					if (colorPalette.getSelectedForegroundColor() == null)
						return;
					setForegroundColor(colorPalette.getSelectedForegroundColor());
					if (mode == RUBBER)
						setRubberColor(colorPalette.getSelectedBackgroundColor());
				} else {
					if (colorPalette.getSelectedBackgroundColor() == null)
						return;
					setBackgroundColor(colorPalette.getSelectedBackgroundColor());
					if (mode == RUBBER)
						setRubberColor(colorPalette.getSelectedBackgroundColor());
				}
			}
		});

		// The icon palette
		iconPalette=new IconPalette(new Dimension(RIGHT_PANEL_WIDTH - 5,230));
		iconPalette.addIconPlacedListener(new IconPlacedListener() {
			public void iconPlaced(IconPlacedEvent e) {
				if (e.getPoint() == null) {
					unregisterKeyboardAction(KeyStroke.getKeyStroke("DELETE"));
					return;
				}

				restoreCursor();
				Point p=e.getPoint();
				SwingUtilities.convertPointFromScreen(p,canvas);
				if (e.isDragging())
					canvas.setImageAsSelection(iconPalette.getSelectedIcon(),p,selectionColor,true);
				else {
					Point viewPos=mainScrollingArea.getViewport().getViewPosition();
					Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
					takeUndoImageBackup(viewPos,downRight);
					canvas.setImageAsSelection(iconPalette.getSelectedIcon(),p,selectionColor,false);
					clearSelection.setEnabled(true);
					fillSelection.setEnabled(true);
					invertSelection.setEnabled(true);

					invertSelMenu.setEnabled(true);
					fillMenu.setEnabled(true);
					clearSelMenu.setEnabled(true);
					iconPalette.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"palette"));
					preview.setPreviewImage(canvas.getImage());
				}
			}
		});

		// The preview colors panel
		previewColorPanels=new PreviewColorPanels(colorPalette);
	}

	private void restoreCursor() {
		if (mode == SELECT_MODE)
			return;
		setMode(SELECT_MODE);
		selectButton.setSelected(true);
	}

	private void selectArea(StyleArea area) {
		selectedArea.setSelected(false);
		selectedArea=area;
		area.setSelected(true);
	}

	public void clearSelectedArea() {
		canvas.fillSelectedShapes(backColor);
	}

	public void invertSelection() {
		canvas.invertSelection();
	}

	public Shape getSelectedArea() {
		return canvas.getSelectedShape();
	}

	public void setSelectedArea(Shape s) {
		canvas.setSelectedShape(s);
		selectButton.doClick();
		if (s != null) {
			clearSelection.setEnabled(true);
			fillSelection.setEnabled(true);
			invertSelection.setEnabled(true);
			invertSelMenu.setEnabled(true);
			fillMenu.setEnabled(true);
			clearSelMenu.setEnabled(true);
		}
	}

	private void unselectAllRect() {
		if (rStyle1.isSelected())
			rStyle1.setSelected(false);
		else if (rStyle2.isSelected())
			rStyle2.setSelected(false);
		else if (rStyle3.isSelected())
			rStyle3.setSelected(false);
	}

	private void unselectAllEllipse() {
		if (ellStyle1.isSelected())
			ellStyle1.setSelected(false);
		else if (ellStyle2.isSelected())
			ellStyle2.setSelected(false);
		else if (ellStyle3.isSelected())
			ellStyle3.setSelected(false);
	}

	private void unselectAllPoly() {
		if (polyStyle1.isSelected())
			polyStyle1.setSelected(false);
		else if (polyStyle2.isSelected())
			polyStyle2.setSelected(false);
		else if (polyStyle3.isSelected())
			polyStyle3.setSelected(false);
	}

	private void copyOperation() {
		clipBoard=getToolkit().getSystemClipboard();
		BufferedImage copyImage=canvas.getSelectedImage();

		int width=copyImage.getWidth();
		int height=copyImage.getHeight();
		try {
			int[] pixels=new int[width * height];
			PixelGrabber pg=new PixelGrabber(copyImage,0,0,width,height,pixels,0,width);
			pg.grabPixels();

			ByteArrayOutputStream byteOut=new ByteArrayOutputStream(width * height * 8 + 2 * 8);

			writeInt(byteOut,width);
			writeInt(byteOut,height);
			for (int i=0;i < width * height;i++)
				writeInt(byteOut,pixels[i]);
			byte[] buffOut=byteOut.toByteArray();
			byteOut.close();

			String sCopy=new String(buffOut);
			StringSelection sSelection=new StringSelection(sCopy);
			clipBoard.setContents(sSelection,this);
		} catch (Exception e) {
		}
		;
		copyImage.flush();
	}

	private void pasteOperation() {
		Transferable sTransf=clipBoard.getContents(this);
		if (sTransf == null)
			return;
		try {
			String sPaste=(String) sTransf.getTransferData(DataFlavor.stringFlavor);
			byte[] buffIn=sPaste.getBytes();
			ByteArrayInputStream byteIn=new ByteArrayInputStream(buffIn);
			int width=readInt(byteIn);
			int height=readInt(byteIn);
			int pixels[]=new int[width * height];
			for (int k=0;k < width * height;k++)
				pixels[k]=readInt(byteIn);
			byteIn.close();
			MemoryImageSource imgMem=new MemoryImageSource(width,height,pixels,0,width);
			Image img=createImage(imgMem);
			Point p=mainScrollingArea.getViewport().getViewPosition();
			if (width * scalingFactor > extentSize.width && height * scalingFactor > extentSize.height)
				canvas.setPastedImage(Utilities.makeBufferedImage(img),p.x + extentSize.width / 2,p.y + extentSize.height / 2);
			else
				canvas.setPastedImage(Utilities.makeBufferedImage(img),p.x + width * (int) scalingFactor / 2,p.y + height * (int) scalingFactor / 2);
			img.flush();
		} catch (Exception e) {
			System.out.println("exception pasting");
			e.printStackTrace();
		}
		;
	}

	// inherited from ClipboardOWner Interface
	public void lostOwnership(Clipboard clipboard,Transferable contents) {
	}

	static byte digits[]= {48,49,50,51,52,53,54,55,56,57,97,98,99,100,101,102};

	static byte buffer[];
	static {
		buffer=new byte[8];
	}

	static public void writeInt(OutputStream stream,int data) throws IOException {
		for (int k=0;k < 8;k++) {
			int d=data & 15;
			buffer[7 - k]=digits[d];
			data>>=4;
		}
		stream.write(buffer);
	}

	static public int readInt(InputStream stream) throws NumberFormatException, IOException {
		stream.read(buffer,0,8);
		int result=0;
		for (int k=0;k < 8;k++) {
			int d=0;
			switch (buffer[7 - k]) {
			case 48:
				d=0;
				break; // 0
			case 49:
				d=1;
				break; // 1
			case 50:
				d=2;
				break; // 2
			case 51:
				d=3;
				break; // 3
			case 52:
				d=4;
				break; // 4
			case 53:
				d=5;
				break; // 5
			case 54:
				d=6;
				break; // 6
			case 55:
				d=7;
				break; // 7
			case 56:
				d=8;
				break; // 8
			case 57:
				d=9;
				break; // 9
			case 97:
				d=10;
				break; // a
			case 98:
				d=11;
				break; // b
			case 99:
				d=12;
				break; // c
			case 100:
				d=13;
				break; // d
			case 101:
				d=14;
				break; // e
			case 102:
				d=15;
				break; // f
			default:
				throw new NumberFormatException("Wrong byte: " + buffer[7 - k]);
			}
			result|=d << (4 * k);
		}
		return result;
	}

	private void createToolbarButtons() {
		// The new File button
		newFileButton=new NoBorderButton(newFile);
		newFileButton.setFont(dialogFont);
		newFileButton.setForeground(buttonFrgrd);
		newFileButton.setToolTipText(iEBundle.getString("IconEditorMsg35"));
		newFileButton.setMaximumSize(buttonDimension);
		newFileButton.setMinimumSize(buttonDimension);
		newFileButton.setPreferredSize(buttonDimension);
		newFileButton.setAlignmentY(CENTER_ALIGNMENT);
		newFileListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewImage();
			}
		};
		newFileButton.addActionListener(newFileListener);

		// The open button
		openButton=new NoBorderButton(open);
		openButton.setFont(dialogFont);
		openButton.setForeground(buttonFrgrd);
		openButton.setToolTipText(iEBundle.getString("IconEditorMsg17"));
		openButton.setMaximumSize(buttonDimension);
		openButton.setMinimumSize(buttonDimension);
		openButton.setPreferredSize(buttonDimension);
		openButton.setAlignmentY(CENTER_ALIGNMENT);
		openListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSelected();
			}
		};
		openButton.addActionListener(openListener);

		// The save button
		saveButton=new NoBorderButton(save);
		saveButton.setFont(dialogFont);
		saveButton.setForeground(buttonFrgrd);
		saveButton.setToolTipText(iEBundle.getString("IconEditorMsg18"));
		saveButton.setMaximumSize(buttonDimension);
		saveButton.setMinimumSize(buttonDimension);
		saveButton.setPreferredSize(buttonDimension);
		saveButton.setAlignmentY(CENTER_ALIGNMENT);
		saveListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSelected();
			}
		};
		saveButton.addActionListener(saveListener);

		// The clear button
		clearButton=new NoBorderButton(clear);
		clearButton.setFont(dialogFont);
		clearButton.setForeground(buttonFrgrd);
		clearButton.setToolTipText(iEBundle.getString("IconEditorMsg7"));
		clearButton.setMaximumSize(buttonDimension);
		clearButton.setMinimumSize(buttonDimension);
		clearButton.setPreferredSize(buttonDimension);
		clearButton.setAlignmentY(CENTER_ALIGNMENT);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// iconPalette.deleteSelectedIcon();
				clearImage();
			}
		});
		// clearButton.addActionListener(this);

		// The button that determines the dimension of the icon
		imageSize=new NoBorderButton(imageDim);
		imageSize.setFont(dialogFont);
		imageSize.setForeground(buttonFrgrd);
		imageSize.setToolTipText(iEBundle.getString("IconEditorMsg31"));
		imageSize.setMaximumSize(buttonDimension);
		imageSize.setMinimumSize(buttonDimension);
		imageSize.setPreferredSize(buttonDimension);
		imageSize.setAlignmentY(CENTER_ALIGNMENT);
		imageSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogImageDimension=new SpinDialog(topFrame,xSize,ySize);
				dialogImageDimension.setLocationRelativeTo(ImageEditor.this);
				dialogImageDimension.setVisible(true);
				if (dialogImageDimension.isOk()) {
					BufferedImage undoIm=canvas.getImage();
					xSize=dialogImageDimension.getUpValue();
					ySize=dialogImageDimension.getDownValue();
					setImageSize(xSize,ySize);
					isImageModified=true;
					imageModifiedForDialog=true;
					currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);
					uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoIm,"imageSize"));
					undoIm.flush();
				}
				dialogImageDimension.dispose();
			}
		});

		// The crop button
		cropButton=new NoBorderToggleButton(crop);
		cropButton.setFont(dialogFont);
		cropButton.setForeground(buttonFrgrd);
		cropButton.setToolTipText(iEBundle.getString("cropText"));
		cropButton.setMaximumSize(buttonDimension);
		cropButton.setMinimumSize(buttonDimension);
		cropButton.setPreferredSize(buttonDimension);
		cropButton.setAlignmentY(CENTER_ALIGNMENT);
		cropButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMode(SELECT_MODE);
			}
		});

		// The undo button
		undoButton=new NoBorderButton(undo);
		undoButton.setFont(dialogFont);
		undoButton.setForeground(buttonFrgrd);
		undoButton.setToolTipText(iEBundle.getString("undoText"));
		undoButton.setMaximumSize(buttonDimension);
		undoButton.setMinimumSize(buttonDimension);
		undoButton.setPreferredSize(buttonDimension);
		undoButton.setAlignmentY(CENTER_ALIGNMENT);
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("scaling before: "+canvas.getScalingFactor());
				try {
					uManager.setCanvasArgument(canvas);
					uManager.undo();
				} catch (CannotUndoException ex) {
					undoButton.setEnabled(false);
					System.out.println("Cannot undo");
				}
				;
				if (realTimeUpdate && !pinDisconnected)
					sendIcon();
				// System.out.println("scaling after: "+canvas.getScalingFactor());
			}
		});

		cutButton=new NoBorderButton(cut);
		cutButton.setFont(dialogFont);
		cutButton.setForeground(buttonFrgrd);
		cutButton.setToolTipText(iEBundle.getString("cutText"));
		cutButton.setMaximumSize(buttonDimension);
		cutButton.setMinimumSize(buttonDimension);
		cutButton.setPreferredSize(buttonDimension);
		cutButton.setAlignmentY(CENTER_ALIGNMENT);
		cutListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.isAnythingSelected()) {
					copyOperation();
					clearSelectedArea();
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
				} else
					getToolkit().beep();
			}
		};
		cutButton.addActionListener(cutListener);

		copyButton=new NoBorderButton(copy);
		copyButton.setFont(dialogFont);
		copyButton.setForeground(buttonFrgrd);
		copyButton.setToolTipText(iEBundle.getString("copyText"));
		copyButton.setMaximumSize(buttonDimension);
		copyButton.setMinimumSize(buttonDimension);
		copyButton.setPreferredSize(buttonDimension);
		copyButton.setAlignmentY(CENTER_ALIGNMENT);
		copyListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.isAnythingSelected()) {
					copyOperation();
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
				} else
					getToolkit().beep();
			}
		};
		copyButton.addActionListener(copyListener);

		pasteButton=new NoBorderButton(paste);
		pasteButton.setFont(dialogFont);
		pasteButton.setForeground(buttonFrgrd);
		pasteButton.setToolTipText(iEBundle.getString("pasteText"));
		pasteButton.setMaximumSize(buttonDimension);
		pasteButton.setMinimumSize(buttonDimension);
		pasteButton.setPreferredSize(buttonDimension);
		pasteButton.setAlignmentY(CENTER_ALIGNMENT);
		pasteListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.isAnythingSelected()) {
					pasteOperation();
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
				} else
					getToolkit().beep();
			}
		};
		pasteButton.addActionListener(pasteListener);

		// *************************************************
		// The zoom Button
		zoomButton=new NoBorderButton(zoom);
		zoomButton.setFont(dialogFont);
		zoomButton.setForeground(buttonFrgrd);
		zoomButton.setToolTipText(iEBundle.getString("IconEditorMsg32"));
		zoomButton.setMaximumSize(buttonDimension);
		zoomButton.setMinimumSize(buttonDimension);
		zoomButton.setPreferredSize(buttonDimension);
		zoomButton.setAlignmentY(CENTER_ALIGNMENT);
		zoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomOperation("increase",1);
			}
		});

		// The unzoom Button
		unzoomButton=new NoBorderButton(unzoom);
		unzoomButton.setFont(dialogFont);
		unzoomButton.setForeground(buttonFrgrd);
		unzoomButton.setToolTipText(iEBundle.getString("IconEditorMsg33"));
		unzoomButton.setMaximumSize(buttonDimension);
		unzoomButton.setMinimumSize(buttonDimension);
		unzoomButton.setPreferredSize(buttonDimension);
		unzoomButton.setAlignmentY(CENTER_ALIGNMENT);
		unzoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unzoomOperation();
			}
		});

		// The pan button
		panButton=new NoBorderToggleButton(pan);
		panButton.setFont(dialogFont);
		panButton.setForeground(buttonFrgrd);
		panButton.setToolTipText(iEBundle.getString("IconEditorMsg30"));
		panButton.setMaximumSize(buttonDimension);
		panButton.setMinimumSize(buttonDimension);
		panButton.setPreferredSize(buttonDimension);
		panButton.setAlignmentY(CENTER_ALIGNMENT);
		panButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMode(PAN_MODE);
			}
		});

		// The grid on/off button
		gridButton=new NoBorderToggleButton(gridOnOff);
		gridButton.setFont(dialogFont);
		gridButton.setForeground(buttonFrgrd);
		gridButton.setToolTipText(iEBundle.getString("IconEditorMsg24"));
		gridButton.setMaximumSize(buttonDimension);
		gridButton.setMinimumSize(buttonDimension);
		gridButton.setPreferredSize(buttonDimension);
		gridButton.setAlignmentY(CENTER_ALIGNMENT);
		gridButton.setSelected(false);
		gridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gridButton.isSelected()) {
					gridState=true;
					autoResize();
					mainScrollingArea.getHorizontalScrollBar().addMouseListener(moveListenerForCursor);
					mainScrollingArea.getVerticalScrollBar().addMouseListener(moveListenerForCursor);
				} else {
					gridState=false;
					mainScrollingArea.getHorizontalScrollBar().removeMouseListener(moveListenerForCursor);
					mainScrollingArea.getVerticalScrollBar().removeMouseListener(moveListenerForCursor);
				}
				rootPane.getGlassPane().setVisible(gridState);
			}
		});

		// *****************************************************

		// The select Button
		selectButton=new NoBorderToggleButton(rectSelectWithArrow);
		selectButton.setFont(dialogFont);
		selectButton.setForeground(buttonFrgrd);
		selectButton.setToolTipText(iEBundle.getString("IconEditorMsg12"));
		selectButton.setMaximumSize(buttonDimension);
		selectButton.setMinimumSize(buttonDimension);
		selectButton.setPreferredSize(buttonDimension);
		selectButton.setAlignmentY(CENTER_ALIGNMENT);
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMode(SELECT_MODE);
				selectMenu.setSelected(true);
				// clearSelection.setEnabled(true);
				// fillSelection.setEnabled(true);
				// invertSelection.setEnabled(true);
			}
		});

		selectButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				popupTimer=new javax.swing.Timer(400,selectionPopupTimerAction);
				popupTimer.setRepeats(false);
				popupTimer.start();
			}

			public void mouseReleased(MouseEvent e) {
				popupTimer.stop();
			}
		});

		selectionPopupTimerAction=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupTimer.stop();
				JPopupMenu popup=new JPopupMenu();
				final JMenuItem item;
				if (selectButton.getIcon().equals(rectSelectWithArrow))
					item=new JMenuItem("Elliptical Selection",ellipseSelection);
				else
					item=new JMenuItem("Rectangular Selection",rectSelection);

				popup.add(item);
				popup.show(selectButton,0,buttonDimension.height - 1);

				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (item.getIcon().equals(rectSelection)) {
							selectButton.setIcon(rectSelectWithArrow);
							rectangularSelection=true;
						} else {
							selectButton.setIcon(ellipseSelectWithArrow);
							rectangularSelection=false;
						}
						setMode(SELECT_MODE);
						selectMenu.setSelected(true);
					}
				});
			}
		};

		// The magic Wand button
		magicWandButton=new NoBorderToggleButton(wand);
		magicWandButton.setFont(dialogFont);
		magicWandButton.setForeground(buttonFrgrd);
		magicWandButton.setToolTipText(iEBundle.getString("IconEditorMsg34"));
		magicWandButton.setMaximumSize(buttonDimension);
		magicWandButton.setMinimumSize(buttonDimension);
		magicWandButton.setPreferredSize(buttonDimension);
		magicWandButton.setAlignmentY(CENTER_ALIGNMENT);
		magicWandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMode(MAGIC_WAND);
				wandMenu.setSelected(true);
			}
		});

		// The invert Selection button
		invertSelection=new NoBorderButton(invert);
		invertSelection.setFont(dialogFont);
		invertSelection.setForeground(buttonFrgrd);
		invertSelection.setToolTipText(iEBundle.getString("IconEditorMsg16"));
		invertSelection.setMaximumSize(buttonDimension);
		invertSelection.setMinimumSize(buttonDimension);
		invertSelection.setPreferredSize(buttonDimension);
		invertSelection.setAlignmentY(CENTER_ALIGNMENT);
		invertSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				invertSelectedArea();
			}
		});

		// The clear selection button
		clearSelection=new NoBorderButton(clsel);
		clearSelection.setFont(dialogFont);
		clearSelection.setForeground(buttonFrgrd);
		clearSelection.setToolTipText(iEBundle.getString("IconEditorMsg14"));
		clearSelection.setMaximumSize(buttonDimension);
		clearSelection.setMinimumSize(buttonDimension);
		clearSelection.setPreferredSize(buttonDimension);
		clearSelection.setAlignmentY(CENTER_ALIGNMENT);
		deleteSelListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point viewPos=mainScrollingArea.getViewport().getViewPosition();
				Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
				takeUndoImageBackup(viewPos,downRight);
				clearSelectedArea();
				preview.setPreviewImage(canvas.getImage());
				uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"shape"));
				if (realTimeUpdate && !pinDisconnected)
					sendIcon();
			}
		};
		clearSelection.addActionListener(deleteSelListener);

		// *************************************************8

		// The send Button
		sendButton=new NoBorderButton(loadImageIcon("Images/send.gif"," "));
		sendButton.setFont(dialogFont);
		sendButton.setForeground(buttonFrgrd);
		sendButton.setToolTipText(iEBundle.getString("IconEditorMsg36"));
		sendButton.setMaximumSize(buttonDimension);
		sendButton.setMinimumSize(buttonDimension);
		sendButton.setPreferredSize(buttonDimension);
		sendButton.setAlignmentY(CENTER_ALIGNMENT);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendIcon();
			}
		});
	}

	private void createMenu() {
		// ************ The File Menu *************
		fileMenu=new JMenu(iEBundle.getString("file"));
//		fileMenu.setFont(menuFont);
		// The new File Item of the File Menu
		JMenuItem newFileItem=new JMenuItem(iEBundle.getString("new"),newFile);
		newFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK));
//		newFileItem.setFont(menuFont);
		newFileItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				fileMenu.setPopupMenuVisible(false);
				createNewImage();
			}
		});

		// The open Item of the File Menu
		JMenuItem openItem=new JMenuItem(iEBundle.getString("openM"),open);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK));
//		openItem.setFont(menuFont);
		openItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				fileMenu.setPopupMenuVisible(false);
				openSelected();
			}
		});

		// The save Item of the File Menu
		JMenuItem saveItem=new JMenuItem(iEBundle.getString("saveM"),save);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK));
//		saveItem.setFont(menuFont);
		saveItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				fileMenu.setPopupMenuVisible(false);
				saveSelected();
			}
		});
		// adding the items of the file manu
		fileMenu.add(newFileItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		// ******************** end of file menu *************************

		// ************ The Edit Menu *************
		JMenu editMenu=new JMenu(iEBundle.getString("edit"));
//		fileMenu.setFont(menuFont);
		// The cut Item of the Edit Menu
		JMenuItem cutItem=new JMenuItem(iEBundle.getString("cutText"),cutMenu);
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
//		cutItem.setFont(menuFont);
		cutItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (canvas.isAnythingSelected()) {
					copyOperation();
					clearSelectedArea();
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
				} else
					getToolkit().beep();
			}
		});

		// The copy Item of the Edit Menu
		JMenuItem copyItem=new JMenuItem(iEBundle.getString("copyText"),copyMenu);
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK));
//		copyItem.setFont(menuFont);
		copyItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (canvas.isAnythingSelected()) {
					copyOperation();
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
				} else
					getToolkit().beep();
			}
		});

		// The paste Item of the Edit Menu
		JMenuItem pasteItem=new JMenuItem(iEBundle.getString("pasteText"),pasteMenu);
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK));
//		pasteItem.setFont(menuFont);
		pasteItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (canvas.isAnythingSelected()) {
					pasteOperation();
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
				} else
					getToolkit().beep();
			}
		});
		// adding the items of the edit manu
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		// ******************** end of edit menu *************************

		// ********************* The image menu ***************************
		imageMenu=new JMenu(iEBundle.getString("image"));
//		imageMenu.setFont(menuFont);
		// The imageSize Item of the image Menu
		JMenuItem imageSizeMenu=new JMenuItem(iEBundle.getString("imsize"),imageDim);
//		imageSizeMenu.setFont(menuFont);
		imageSizeMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				imageMenu.setPopupMenuVisible(false);
				dialogImageDimension=new SpinDialog(topFrame,xSize,ySize);
				dialogImageDimension.setLocationRelativeTo(ImageEditor.this);
				dialogImageDimension.setVisible(true);
				if (dialogImageDimension.isOk()) {
					xSize=dialogImageDimension.getUpValue();
					ySize=dialogImageDimension.getDownValue();
					setImageSize(xSize,ySize);
					currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);
				}
				dialogImageDimension.dispose();
			}
		});

		// The clearImage Item of the image Menu
		JMenuItem clearImageMenu=new JMenuItem(iEBundle.getString("clearim"),clear);
//		clearImageMenu.setFont(menuFont);
		clearImageMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				clearImage();
			}
		});

		// The grid Item of the image Menu
		gridBox=new JCheckBoxMenuItem(iEBundle.getString("grid"),true);
//		gridBox.setFont(menuFont);
		gridBox.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!gridBox.isSelected()) {
					gridState=true;
					gridButton.setSelected(true);
					mainScrollingArea.getHorizontalScrollBar().addMouseListener(moveListenerForCursor);
					mainScrollingArea.getVerticalScrollBar().addMouseListener(moveListenerForCursor);
				} else {
					gridState=false;
					gridButton.setSelected(false);
					mainScrollingArea.getHorizontalScrollBar().removeMouseListener(moveListenerForCursor);
					mainScrollingArea.getVerticalScrollBar().removeMouseListener(moveListenerForCursor);
				}
				rootPane.getGlassPane().setVisible(gridState);
			}
		});

		// The zoom Item of the image Menu
		JMenu zoomMenu=new JMenu(iEBundle.getString("zoom"));
//		zoomMenu.setFont(menuFont);

		oneTo1=new JMenuItem("1:1");
//		oneTo1.setFont(menuFont);
		JMenuItem twoTo1=new JMenuItem("2:1");
//		twoTo1.setFont(menuFont);
		JMenuItem fiveTo1=new JMenuItem("5:1");
//		fiveTo1.setFont(menuFont);
		JMenuItem tenTo1=new JMenuItem("10:1");
//		tenTo1.setFont(menuFont);
		JMenuItem fifteenTo1=new JMenuItem("15:1");
//		fifteenTo1.setFont(menuFont);

		oneTo1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				zoomOperation("fix",1);
			}
		});

		twoTo1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				zoomOperation("fix",2);
			}
		});

		fiveTo1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				zoomOperation("fix",5);
			}
		});

		tenTo1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				zoomOperation("fix",10);
			}
		});
		fifteenTo1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				zoomOperation("fix",15);
			}
		});
		zoomMenu.add(oneTo1);
		zoomMenu.add(twoTo1);
		zoomMenu.add(fiveTo1);
		zoomMenu.add(tenTo1);
		zoomMenu.add(fifteenTo1);

		// adding the items of the image menu
		imageMenu.add(imageSizeMenu);
		imageMenu.add(clearImageMenu);
		imageMenu.add(new JSeparator());
		imageMenu.add(zoomMenu);
		imageMenu.add(gridBox);

		// ***************** end of the image Menu **************************

		// ********************* The tools menu ***************************
		JMenu toolsMenu=new JMenu(iEBundle.getString("tools"));
//		toolsMenu.setFont(menuFont);

		// The invert selection Item of the tools Menu
		invertSelMenu=new JMenuItem(iEBundle.getString("IconEditorMsg16") + "...",invert);
		invertSelMenu.setEnabled(false);
//		invertSelMenu.setFont(menuFont);
		invertSelMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				invertSelectedArea();
			}
		});
		// The fill selection Item of the tools Menu
		fillMenu=new JMenuItem(iEBundle.getString("IconEditorMsg15") + "...",fill);
		fillMenu.setEnabled(false);
//		fillMenu.setFont(menuFont);
		fillMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				fillSelection.setSelected(true);
				canvas.fillSelectedShapes(foreColor);
				preview.setPreviewImage(canvas.getImage());
			}
		});

		// The clear selection Item of the tools Menu
		clearSelMenu=new JMenuItem(iEBundle.getString("IconEditorMsg14") + "...",clsel);
		clearSelMenu.setEnabled(false);
//		clearSelMenu.setFont(menuFont);
		clearSelMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				clearSelectedArea();
			}
		});

		// The select Item of the tools Menu
		selectMenu=new JRadioButtonMenuItem(iEBundle.getString("IconEditorMsg12") + "...",rectSelection);
//		selectMenu.setFont(menuFont);
		selectMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setMode(SELECT_MODE);
				selectButton.setSelected(true);
				selectMenu.setSelected(true);
			}
		});
		// The magic wand Item of the tools Menu
		wandMenu=new JRadioButtonMenuItem(iEBundle.getString("IconEditorMsg34") + "...",wand);
//		wandMenu.setFont(menuFont);
		wandMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setMode(MAGIC_WAND);
				magicWandButton.setSelected(true);
				wandMenu.setSelected(true);
			}
		});
		// The pen Item of the tools Menu
		penMenu=new JRadioButtonMenuItem(iEBundle.getString("IconEditorMsg27") + "...",pen,true);
//		penMenu.setFont(menuFont);
		penMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				shapesButton.setIcon(menuPen);
				shapesChoice=1;
				shapesButton.setToolTipText(iEBundle.getString("IconEditorMsg27"));
				shapesButton.setSelected(true);
				enableLineWidths();
				penSelected();
			}
		});
		// The rubber Item of the tools Menu
		rubberMenu=new JRadioButtonMenuItem(iEBundle.getString("IconEditorMsg26") + "...",rubber);
//		rubberMenu.setFont(menuFont);
		rubberMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				rubberButton.setSelected(true);
				rubberSelected();
				rubberMenu.setSelected(true);
			}
		});

		// The flatFillMenu Item of the tools Menu
		flatFillMenu=new JRadioButtonMenuItem(iEBundle.getString("fFill") + "...",flatFill);
//		flatFillMenu.setFont(menuFont);
		flatFillMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				flatFillButton.setSelected(true);
				flatFillSelected();
				flatFillMenu.setSelected(true);
			}
		});

		// The line Item of the tools Menu
		lineMenu=new JRadioButtonMenuItem(iEBundle.getString("line") + "...",line);
//		lineMenu.setFont(menuFont);
		lineMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				shapesButton.setIcon(menuLine);
				shapesChoice=2;
				shapesButton.setToolTipText(iEBundle.getString("line"));
				shapesButton.setSelected(true);
				enableLineWidths();
				lineSelected();
			}
		});
		// The Rect Item of the tools Menu
		rectMenu=new JRadioButtonMenuItem(iEBundle.getString("rectangle") + "...",rect);
//		rectMenu.setFont(menuFont);
		rectMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				shapesButton.setIcon(menuRect);
				shapesChoice=3;
				shapesButton.setToolTipText(iEBundle.getString("rectangle"));
				shapesButton.setSelected(true);
				enableRectStyles();
				rectSelected();
			}
		});
		// The Ellipse Item of the tools Menu
		ellipseMenu=new JRadioButtonMenuItem(iEBundle.getString("ellipse") + "...",ellip);
//		ellipseMenu.setFont(menuFont);
		ellipseMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				shapesButton.setIcon(menuEllip);
				shapesChoice=4;
				shapesButton.setToolTipText(iEBundle.getString("ellipse"));
				shapesButton.setSelected(true);
				enableEllipseStyles();
				ellipseSelected();
			}
		});
		// The Poly Item of the tools Menu
		polyMenu=new JRadioButtonMenuItem(iEBundle.getString("poly") + "...",poly);
//		polyMenu.setFont(menuFont);
		polyMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				shapesButton.setIcon(menuPoly);
				shapesChoice=5;
				shapesButton.setToolTipText(iEBundle.getString("poly"));
				shapesButton.setSelected(true);
				enablePolyStyles();
				polySelected();
			}
		});
		// The button group for the radio buttons of the menu
		ButtonGroup toolItemsGroup=new ButtonGroup();
		toolItemsGroup.add(selectMenu);
		toolItemsGroup.add(wandMenu);
		toolItemsGroup.add(penMenu);
		toolItemsGroup.add(rubberMenu);
		toolItemsGroup.add(flatFillMenu);
		toolItemsGroup.add(lineMenu);
		toolItemsGroup.add(rectMenu);
		toolItemsGroup.add(ellipseMenu);
		toolItemsGroup.add(polyMenu);

		// The pick foreground Item of the tools Menu
		JMenuItem pickForeMenu=new JMenuItem(iEBundle.getString("IconEditorMsg28") + "...",picker);
//		pickForeMenu.setFont(menuFont);
		pickForeMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pickerButton.setSelected(true);
				pickSelected();
			}
		});
		toolItemsGroup.add(pickForeMenu);

		// adding the items of the tools menu
		toolsMenu.add(penMenu);
		toolsMenu.add(rubberMenu);
		toolsMenu.add(lineMenu);
		toolsMenu.add(rectMenu);
		toolsMenu.add(ellipseMenu);
		toolsMenu.add(polyMenu);
		toolsMenu.add(flatFillMenu);
		toolsMenu.add(selectMenu);
		toolsMenu.add(wandMenu);
		toolsMenu.add(new JSeparator());
		toolsMenu.add(invertSelMenu);
		toolsMenu.add(fillMenu);
		toolsMenu.add(clearSelMenu);
		toolsMenu.add(new JSeparator());
		toolsMenu.add(pickForeMenu);

		// ***************** end of the tools Menu **************************

		// ********************* The palettes menu ***************************
		iconPaletteMenu=new JMenu(iEBundle.getString("iPalette"));
//		iconPaletteMenu.setFont(menuFont);
		iconPaletteMenu.setEnabled(false);

		// The new palette Item of the icon palette Menu
		JMenuItem newIconPaletteMenu=new JMenuItem(iEBundle.getString("newIconPalette"),loadImageIcon("Images/newIconPalette.gif"," "));
//		newIconPaletteMenu.setFont(menuFont);
		newIconPaletteMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				iconPaletteMenu.setPopupMenuVisible(false);
				NewPaletteDialog newPaletteDialog=new NewPaletteDialog(topFrame,iEBundle.getString("newIconPalette"),1);
				newPaletteDialog.showDialog(mainPanel);
				int numberOfIcons=newPaletteDialog.getNumberOfElements();
				if (numberOfIcons == -1)
					return;
				iconPalette.newPalette(numberOfIcons);
			}
		});

		// The open palette Item of the icon palette Menu
		JMenuItem openIconPaletteMenu=new JMenuItem(iEBundle.getString("openIconPalette"),loadImageIcon("Images/openIconPalette.gif"," "));
//		openIconPaletteMenu.setFont(menuFont);
		openIconPaletteMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				iconPalette.open();
			}
		});

		// The save palette Item of the icon palette Menu
		JMenuItem saveIconPaletteMenu=new JMenuItem(iEBundle.getString("saveIconPalette"),loadImageIcon("Images/saveIconPalette.gif"," "));
//		saveIconPaletteMenu.setFont(menuFont);
		saveIconPaletteMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				iconPalette.save();
			}
		});
		colorPaletteMenu=new JMenu(iEBundle.getString("cPalette"));
//		colorPaletteMenu.setFont(menuFont);
		colorPaletteMenu.setEnabled(true);

		// The new palette Item of the color palette Menu
		JMenuItem newColorPaletteMenu=new JMenuItem(iEBundle.getString("newColorPalette"),loadImageIcon("Images/newColorPalette.gif"," "));
//		newColorPaletteMenu.setFont(menuFont);
		newColorPaletteMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				colorPaletteMenu.setPopupMenuVisible(false);
				NewPaletteDialog newPaletteDialog=new NewPaletteDialog(topFrame,iEBundle.getString("newColorPalette"),2);
				newPaletteDialog.showDialog(mainPanel);
				int numberOfColors=newPaletteDialog.getNumberOfElements();
				int numberOfCustomColors=newPaletteDialog.getNumberOfCustomElements();
				if (numberOfColors == -1)
					return;
				if (numberOfCustomColors == -1)
					return;
				colorPalette.newPalette(numberOfColors,numberOfCustomColors);
			}
		});

		// The open palette Item of the Color palette Menu
		JMenuItem openColorPaletteMenu=new JMenuItem(iEBundle.getString("openColorPalette"),loadImageIcon("Images/openColorPalette.gif"," "));
//		openColorPaletteMenu.setFont(menuFont);
		openColorPaletteMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				colorPalette.open();
			}
		});

		// The save palette Item of the icon palette Menu
		JMenuItem saveColorPaletteMenu=new JMenuItem(iEBundle.getString("saveColorPalette"),loadImageIcon("Images/saveColorPalette.gif"," "));
//		saveColorPaletteMenu.setFont(menuFont);
		saveColorPaletteMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				colorPalette.save();
			}
		});

		// adding items to the palette Menu
		iconPaletteMenu.add(newIconPaletteMenu);
		iconPaletteMenu.add(openIconPaletteMenu);
		iconPaletteMenu.add(saveIconPaletteMenu);
		colorPaletteMenu.add(newColorPaletteMenu);
		colorPaletteMenu.add(openColorPaletteMenu);
		colorPaletteMenu.add(saveColorPaletteMenu);
		// ***************** end of the icon palette Menu **************************

		// adding the menus to the menu Bar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(imageMenu);
		menuBar.add(toolsMenu);
		menuBar.add(colorPaletteMenu);
		menuBar.add(iconPaletteMenu);
		// ///////// end of menu bar
	}

	public void removeAllListeners() {
		canvas.removeMouseMotionListener(penListener);
		canvas.removeMouseMotionListener(lineListener);
		canvas.removeMouseMotionListener(rectListener);
		canvas.removeMouseMotionListener(ellipseListener);
		canvas.removeMouseMotionListener(polyListener);
		canvas.removeMouseMotionListener(selectListener);
		canvas.removeMouseMotionListener(flatFillListener);
		canvas.removeMouseMotionListener(wandListener);
		canvas.removeMouseMotionListener(panListener);
		canvas.removeMouseMotionListener(pickListener);

	}// removeAllListeners

	// Modes
	private void setMode(int mode) {
		this.mode=mode;
		if (mode == PEN) {
			currentModeField.setText(iEBundle.getString("pencilMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[0]);
			gridPane.setCursor((Cursor) cursors[0]);
			removeAllListeners();
			canvas.addMouseMotionListener(penListener);
		} else if (mode == RUBBER) {
			currentModeField.setText(iEBundle.getString("rubberMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[1]);
			gridPane.setCursor((Cursor) cursors[1]);
			removeAllListeners();
			canvas.addMouseMotionListener(penListener);
		} else if (mode == LINE) {
			currentModeField.setText(iEBundle.getString("lineMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[2]);
			gridPane.setCursor((Cursor) cursors[2]);
			removeAllListeners();
			canvas.addMouseMotionListener(lineListener);
		} else if (mode == RECT) {
			currentModeField.setText(iEBundle.getString("rectMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[3]);
			gridPane.setCursor((Cursor) cursors[3]);
			removeAllListeners();
			canvas.addMouseMotionListener(rectListener);
		} else if (mode == ELLIPSE) {
			currentModeField.setText(iEBundle.getString("ellMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[4]);
			gridPane.setCursor((Cursor) cursors[4]);
			removeAllListeners();
			canvas.addMouseMotionListener(ellipseListener);
		} else if (mode == POLY) {
			currentModeField.setText(iEBundle.getString("polyMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[5]);
			gridPane.setCursor((Cursor) cursors[5]);
			removeAllListeners();
			canvas.addMouseMotionListener(polyListener);
		} else if (mode == SELECT_MODE) {
			currentModeField.setText(iEBundle.getString("selectMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			removeAllListeners();
			canvas.addMouseMotionListener(selectListener);
		} else if (mode == MAGIC_WAND) {
			currentModeField.setText(iEBundle.getString("wandMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[6]);
			gridPane.setCursor((Cursor) cursors[6]);
			removeAllListeners();
			canvas.addMouseMotionListener(wandListener);
		} else if (mode == DRAG_SELECTION_MODE) {
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		} else if (mode == PAN_MODE) {
			currentModeField.setText(iEBundle.getString("panMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[10]);
			gridPane.setCursor((Cursor) cursors[10]);
			removeAllListeners();
			canvas.addMouseMotionListener(panListener);
		} else if (mode == PICK_MODE) {
			currentModeField.setText(iEBundle.getString("pickMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[7]);
			gridPane.setCursor((Cursor) cursors[7]);
			removeAllListeners();
			canvas.addMouseMotionListener(pickListener);
		} else if (mode == FLAT_FILL_MODE) {
			currentModeField.setText(iEBundle.getString("flatMode"));
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			canvas.setCursor((Cursor) cursors[8]);
			gridPane.setCursor((Cursor) cursors[8]);
			removeAllListeners();
			canvas.addMouseMotionListener(flatFillListener);
		}
	}

	private void setRubberColor(Color color) {
		foreColor=color;
	}

	public void setForegroundColor(Color color) {
		if (color.getAlpha() != 255)
			foreColor=new Color(color.getRed(),color.getGreen(),color.getBlue(),0);
		else
			foreColor=color;
		previewColorPanels.setForegroundColor(foreColor);
		componentModified=true;
	}

	public Color getForegroundColor() {
		return foreColor;
	}

	public void setBackgroundColor(Color color) {
		// canvas.setCanvasBackground(backColor, color);
		if (color.getAlpha() != 255)
			backColor=new Color(color.getRed(),color.getGreen(),color.getBlue(),0);
		else
			backColor=color;
		previewColorPanels.setBackgroundColor(backColor);
	}

	public Color getBackgroundColor() {
		return backColor;
	}

	// for back compatibility
	// /////////////////////////////
	public boolean isImageSaved() {
		return imageSaved;
	}// isImageSaved

	// Sets the file of the icon.
	public void setFileName(String fileName) {
		this.fileName=fileName;
	}// setFileName

	/**
	 * Returns the file of the icon, which was diplayed in the icon editor before it closed, if the icon was loaded from
	 * a file. The contents of the file may ne inconsistent with the icon itself. This can happen if changes to the icon
	 * were not saved to the file, before the editor was closed.
	 */
	public String getFileName() {
		return fileName;
	}// getFileName
	
	public JFileChooser getFileChooser() {
		if (fileChooser==null)
			prepareFileChooser();
		return fileChooser;
	}

	// Sets the foreground color of the icon editor.
	public void setForegroundColor(Color color,int transparency) {
		if (transparency != 255)
			foreColor=new Color(color.getRed(),color.getGreen(),color.getBlue(),0);
		else
			foreColor=new Color(color.getRed(),color.getGreen(),color.getBlue(),255);
		previewColorPanels.setForegroundColor(foreColor);
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
	}// setForegroundColor

	public int getForegroundTransparency() {
		return foreColor.getAlpha();
	}// getForegroundTransparency

	// Sets the background color of the icon editor.
	public void setBackgroundColor(Color color,int transparency) {
		if (color.getAlpha() != 255)
			backColor=new Color(color.getRed(),color.getGreen(),color.getBlue(),0);
		else
			backColor=new Color(color.getRed(),color.getGreen(),color.getBlue(),255);
		previewColorPanels.setBackgroundColor(backColor);
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
		componentModified=true;
	}// setBackgroundColor

	public int getBackgroundTransparency() {
		return backColor.getAlpha();
	}// getBackgroundTransparency

	// //////////////////////////////

	private int toOriginalCoords(int coord) {
		return coord / (int) scalingFactor;
	}

	private int toRealCoords(int coord) {
		return coord * (int) scalingFactor;
	}

	// operation methods
	// when pen Button is selected
	private void penSelected() {
		setMode(PEN);
		setForegroundColor(colorPalette.getSelectedForegroundColor());
	}// end of penSelected

	// when rubber Button is selected
	private void rubberSelected() {
		setMode(RUBBER);
		setRubberColor(colorPalette.getSelectedBackgroundColor());
	}// end of rubberSelected

	private void flatFillSelected() {
		setMode(FLAT_FILL_MODE);
		setForegroundColor(colorPalette.getSelectedForegroundColor());
	}

	// when line Button is selected
	private void lineSelected() {
		setMode(LINE);
		setForegroundColor(colorPalette.getSelectedForegroundColor());
	}// end of lineSelected

	private void rectSelected() {
		setMode(RECT);
		setForegroundColor(colorPalette.getSelectedForegroundColor());
	}// end of rectSelected

	private void ellipseSelected() {
		setMode(ELLIPSE);
		setForegroundColor(colorPalette.getSelectedForegroundColor());
	}// end of ellipseSelected

	private void polySelected() {
		setMode(POLY);
		setForegroundColor(colorPalette.getSelectedForegroundColor());
	}// end of polySelected

	private void pickSelected() {
		setMode(PICK_MODE);
	}// end of penSelected

	private void drawPoint(int x,int y,boolean cont) {
		if (cont) {
			canvas.drawContPoints(x,y,foreColor);
		} else {
			// takeUndoPixelBackup(x, y);
			canvas.drawPoint(x,y,foreColor);
		}
	}

	private void drawLine(Point p1,Point p2) {
		canvas.drawLine(p1.x,p1.y,p2.x,p2.y,foreColor,false);
	}

	private void drawDraggedLine(Point p1,Point p2) {
		canvas.drawLine(p1.x,p1.y,p2.x,p2.y,foreColor,true);
	}

	private void drawRectangle(Point p1,Point p2,boolean square) {
		if (square)
			canvas.drawRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,false,true);
		else
			canvas.drawRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,false,false);
	}

	private void fillRectangle(Point p1,Point p2,boolean square,boolean outline) {
		if (square) {
			if (outline)
				canvas.fillRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,true,true);
			else
				canvas.fillRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,true,false);
		} else {
			if (outline)
				canvas.fillRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,false,true);
			else
				canvas.fillRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,false,false);
		}
	}

	private void fillEllipse(Point p1,Point p2,boolean circle,boolean outline) {
		if (circle) {
			if (outline)
				canvas.fillEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,true,true);
			else
				canvas.fillEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,true,false);
		} else {
			if (outline)
				canvas.fillEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,false,true);
			else
				canvas.fillEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,backColor,false,false,false);
		}
	}

	private void drawDraggedRect(Point p1,Point p2,boolean square) {
		if (square)
			canvas.drawRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,true,true);
		else
			canvas.drawRectangle(p1.x,p1.y,p2.x,p2.y,foreColor,true,false);
	}

	private void drawEllipse(Point p1,Point p2,boolean circle) {
		if (circle)
			canvas.drawEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,false,true);
		else
			canvas.drawEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,false,false);
	}

	private void drawDraggedEllipse(Point p1,Point p2,boolean circle) {
		if (circle)
			canvas.drawEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,true,true);
		else
			canvas.drawEllipse(p1.x,p1.y,p2.x,p2.y,foreColor,true,false);
	}

	private void drawPoly(Point p1,boolean firstPoint) {
		canvas.drawPoly(p1.x,p1.y,foreColor,backColor,firstPoint);
	}

	private void select(Point p1,Point p2,boolean dragging,boolean rectSelection,boolean symetricSelection) {
		canvas.select(p1.x,p1.y,p2.x,p2.y,selectionColor,"normal",dragging,rectSelection,symetricSelection);
	}

	private void addToSelection(Point p1,Point p2) {
		canvas.select(p1.x,p1.y,p2.x,p2.y,selectionColor,"add",false,rectangularSelection,false);
	}

	private void removeFromSelection(Point p1,Point p2) {
		canvas.select(p1.x,p1.y,p2.x,p2.y,selectionColor,"remove",false,rectangularSelection,false);
	}

	private void pan(int differenceX,int differenceY) {
		Point currentPosition=mainScrollingArea.getViewport().getViewPosition();

		if ((xSize - 1) * canvas.getScalingFactor() <= extentSize.width)
			differenceX=0;
		else {
			if (currentPosition.x - differenceX <= 0)
				currentPosition.x=differenceX;
			else if (currentPosition.x - differenceX + extentSize.width >= xSize * canvas.getScalingFactor())
				currentPosition.x=differenceX - extentSize.width + xSize * (int) canvas.getScalingFactor();
		}

		if ((ySize - 1) * canvas.getScalingFactor() <= extentSize.height)
			differenceY=0;
		else {
			if (currentPosition.y - differenceY <= 0)
				currentPosition.y=differenceY;
			else if (currentPosition.y - differenceY + extentSize.height >= ySize * canvas.getScalingFactor())
				currentPosition.y=differenceY - extentSize.height + ySize * (int) canvas.getScalingFactor();
		}

		Point newPosition=new Point(currentPosition.x - differenceX,currentPosition.y - differenceY);
		mainScrollingArea.getViewport().setViewPosition(newPosition);

	}

	public void zoomOperation(String mode,int factor) {
		// store initial state
		if (firstZoom) {
			firstZoom=false;
			previousCursor=canvas.getCursor();
			previousText=currentModeField.getText();
		}
		// set the wait properties
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		currentModeField.setForeground(Color.red);
		currentModeField.setText(iEBundle.getString("pleasewait"));

		WaitNow waitThread=new WaitNow(mode,factor,previousText,previousCursor) {
			public void run() {
				if (!unzoomButton.isEnabled())
					unzoomButton.setEnabled(true);

				Point pos=mainScrollingArea.getViewport().getViewPosition();
				Point middlePoint=new Point(pos.x + extentSize.width / 2,pos.y + extentSize.height / 2);
				Point realMiddlePoint=canvas.getRealImagePoint(middlePoint);

				if (this.mode.equals("increase"))
					scalingFactor=canvas.getScalingFactor() + 1;
				else
					scalingFactor=this.factor;

				canvas.setScalingFactor(scalingFactor);

				gridPane.setScalingFactor((int) scalingFactor);

				mainScrollingArea.setViewportView(canvas);

				Point newMiddlePoint=canvas.getViewImagePoint(realMiddlePoint);
				Point newViewPos=new Point(newMiddlePoint.x - extentSize.width / 2,newMiddlePoint.y - extentSize.height / 2);
				mainScrollingArea.getViewport().setViewPosition(newViewPos);
				mainScrollingArea.getHorizontalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
				mainScrollingArea.getVerticalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
				autoEnablePanButton();
				// SwingUtilities.invokeLater(new MyRunnable(){
				// public void run(){
				autoResize();
				// }
				// });
				threadsCount--;
				if (threadsCount == 0) {
					currentModeField.setForeground(Color.black);
					currentModeField.setText(text);
					canvas.setCursor(cursor);
					gridPane.setCursor(cursor);
					ImageEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					firstZoom=true;
				}
				// System.out.println("thread finished");
			}
		};
		// myThread = new Thread(waitThread);
		SwingUtilities.invokeLater(waitThread);
		// activeThreads = myThread.activeCount();
		threadsCount++;
	}

	public void unzoomOperation() {
		if (firstZoom) {
			firstZoom=false;
			previousCursor=canvas.getCursor();
			previousText=currentModeField.getText();
		}
		// set the wait properties
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		currentModeField.setForeground(Color.red);
		currentModeField.setText(iEBundle.getString("pleasewait"));

		WaitNow waitThread=new WaitNow(previousText,previousCursor) {
			public void run() {
				if (scalingFactor >= 2) {
					Point pos=mainScrollingArea.getViewport().getViewPosition();
					Point middlePoint=new Point(pos.x + extentSize.width / 2,pos.y + extentSize.height / 2);
					Point realMiddlePoint=canvas.getRealImagePoint(middlePoint);

					scalingFactor=canvas.getScalingFactor() - 1;
					if (scalingFactor == 1)
						unzoomButton.setEnabled(false);

					canvas.setScalingFactor(scalingFactor);
					gridPane.setScalingFactor((int) scalingFactor);
					mainScrollingArea.setViewportView(canvas);

					Point newMiddlePoint=canvas.getViewImagePoint(realMiddlePoint);
					Point newViewPos=new Point(newMiddlePoint.x - extentSize.width / 2,newMiddlePoint.y - extentSize.height / 2);
					mainScrollingArea.getViewport().setViewPosition(newViewPos);
					mainScrollingArea.getHorizontalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
					mainScrollingArea.getVerticalScrollBar().setUnitIncrement((int) canvas.getScalingFactor());
				}
				autoEnablePanButton();
				// SwingUtilities.invokeLater(new MyRunnable(){
				// public void run(){
				autoResize();
				// }
				// });
				threadsCount--;
				if (threadsCount == 0) {
					currentModeField.setForeground(Color.black);
					currentModeField.setText(text);
					canvas.setCursor(cursor);
					gridPane.setCursor(cursor);
					ImageEditor.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					firstZoom=true;
				}
			}
		};
		// myThread = new Thread(waitThread);
		SwingUtilities.invokeLater(waitThread);
		// activeThreads = myThread.activeCount();
		threadsCount++;
	}

	private void takeUndoImageBackup(Point xPressed,Point yPressed) {
		undoImage=canvas.createSubImage(xPressed,yPressed);
	}

	private void takeUndoImageBackup() {
		undoImage=new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=undoImage.createGraphics();
		g2.drawImage(canvas.getImage(),0,0,null);
		undoImage.flush();
		g2.dispose();
	}

	private void takeUndoImageBackup(Rectangle undoRect) {
		undoImage=canvas.createSubImage(new Point(undoRect.x,undoRect.y),new Point(undoRect.width,undoRect.height));
	}

	private void takeUndoSelectBackup() {
		selection=canvas.getSelectedShape();
	}

	// ********* methods for use in logo primitives
	// sets the icon whose path is indicated by the string iconPath
	public void setIcon(String iconPath) {
		openImage(iconPath);
		componentModified=true;
	}// setIcon

	// sets the grid on / off
	public void setGrid(boolean flag) {
		if (flag) {
			gridState=true;
			mainScrollingArea.getHorizontalScrollBar().addMouseListener(moveListenerForCursor);
			mainScrollingArea.getVerticalScrollBar().addMouseListener(moveListenerForCursor);
		} else {
			gridState=false;
			mainScrollingArea.getHorizontalScrollBar().removeMouseListener(moveListenerForCursor);
			mainScrollingArea.getVerticalScrollBar().removeMouseListener(moveListenerForCursor);
		}
		rootPane.getGlassPane().setVisible(gridState);
		repaint();
		componentModified=true;
	}// setGrid

	public void drawIconPoint(int x,int y) {
		Point viewPos=mainScrollingArea.getViewport().getViewPosition();
		Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
		takeUndoImageBackup(viewPos,downRight);
		drawPoint(toRealCoords(x),toRealCoords(y),false);
		uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"shape"));
		if (realTimeUpdate && !pinDisconnected) {
			sendIcon();
		}
	}// drawIconPoint

	public void drawLogoEllipse(int startX,int startY,int endX,int endY) {
		drawEllipse(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)),false);
	}// drawLogoEllipse

	public void fillLogoEllipse(int startX,int startY,int endX,int endY) {
		fillEllipse(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)),false,false);
	}// drawLogoEllipse

	public void drawLogoCircle(int centerX,int centerY,int rad) {
		int startX=centerX - rad;
		int startY=centerY - rad;
		int endX=centerX + rad;
		int endY=centerY + rad;
		drawEllipse(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)),false);
		if (startX < 0 || startY < 0)
			repaint();
	}// drawLogoCircle

	public void fillLogoCircle(int centerX,int centerY,int rad) {
		int startX=centerX - rad;
		int startY=centerY - rad;
		int endX=centerX + rad;
		int endY=centerY + rad;
		fillEllipse(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)),false,false);
		if (startX < 0 || startY < 0)
			repaint();
	}// drawLogoCircle

	public void drawLogoLine(int startX,int startY,int endX,int endY) {
		drawLine(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)));
	}// drawLogoLine

	public void drawLogoRectangle(int startX,int startY,int endX,int endY) {
		drawRectangle(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)),false);
	}// drawLogoLine

	public void fillLogoRectangle(int startX,int startY,int endX,int endY) {
		fillRectangle(new Point(toRealCoords(startX),toRealCoords(startY)),new Point(toRealCoords(endX),toRealCoords(endY)),false,false);
	}// drawLogoLine

	public void pickLogoForeground(int x,int y) {
		Point p=new Point(toRealCoords(x),toRealCoords(y));
		int color=canvas.getPixel(p);
		Color col=new Color(color);
		setForegroundColor(new Color(color));
		colorPalette.setSelectedForegroundColor(col);
		colorPalette.repaint();
	}

	public void pickLogoBackground(int x,int y) {
		Point p=new Point(toRealCoords(x),toRealCoords(y));
		int color=canvas.getPixel(p);
		Color col=new Color(color);
		setBackgroundColor(new Color(color));
		colorPalette.setSelectedBackgroundColor(col);
		colorPalette.repaint();
	}

	public void selectLogoArea(int x,int y,int width,int height) {
		int x1=toRealCoords(x);
		int y1=toRealCoords(y);
		int x2=toRealCoords(x + width);
		int y2=toRealCoords(y + height);
		select(new Point(x1,y1),new Point(x2,y2),false,true,false);
	}

	public void addToLogoSelectionArea(int x,int y,int width,int height) {
		int x1=toRealCoords(x);
		int y1=toRealCoords(y);
		int x2=toRealCoords(x + width);
		int y2=toRealCoords(y + height);
		addToSelection(new Point(x1,y1),new Point(x2,y2));
	}

	public void removeFromLogoSelectionArea(int x,int y,int width,int height) {
		int x1=toRealCoords(x);
		int y1=toRealCoords(y);
		int x2=toRealCoords(x + width);
		int y2=toRealCoords(y + height);
		removeFromSelection(new Point(x1,y1),new Point(x2,y2));
	}

	public void fillLogoArea(int x,int y,int width,int height) {
		int x1=toRealCoords(x);
		int y1=toRealCoords(y);
		int x2=toRealCoords(x + width);
		int y2=toRealCoords(y + height);
		fillRectangle(new Point(x1,y1),new Point(x2,y2),false,false);
	}

	public void fillSelectedArea() {
		Point viewPos=mainScrollingArea.getViewport().getViewPosition();
		Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
		takeUndoImageBackup(viewPos,downRight);
		canvas.fillSelectedShapes(foreColor);
		preview.setPreviewImage(canvas.getImage());
		uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"shape"));
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
	}

	public void setLogoCellSize(int cell) {
		zoomOperation("fix",cell);
		componentModified=true;
	}

	public void setLogoImageSize(int xSize,int ySize) {
		BufferedImage undoIm=canvas.getImage();
		setImageSize(xSize,ySize);
		currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);
		uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoIm,"imageSize"));
		undoImage.flush();
		componentModified=true;
	}

	public boolean showCell(int x,int y) {
		Point position=new Point(toRealCoords(x),toRealCoords(y));
		if ((toRealCoords(xSize) - position.x) > mainScrollingArea.getViewport().getExtentSize().width && (toRealCoords(ySize) - position.y) > mainScrollingArea.getViewport().getExtentSize().height) {
			mainScrollingArea.getViewport().setViewPosition(position);
			repaint();
			return false;
		} else
			return true;
	}// showCell

	public void selectCells(Color color) {
		canvas.deselectAll();
		canvas.selectPixels(color);
		setMode(SELECT_MODE);
		selectButton.setSelected(true);
	}// selectCells

	// //////////////////////////////

	// creates a new blank image xSize * ySize
	public void createNewImage() {
		if (isImageModified) {
			int question=JOptionPane.showConfirmDialog(null,iEBundle.getString("saving"),iEBundle.getString("alert"),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (question == JOptionPane.YES_OPTION)
				saveSelected();
			else if (question == JOptionPane.NO_OPTION) {
				isImageModified=false;
				fileName="";
				imageSaved=true;
			} else
				return;
		}
		if (fileChooser == null)
			prepareFileChooser();
		if (fileChooser.getSelectedFile() != null) {
			isImageModified=false;
			fileName="";
			imageSaved=true;
		}
		canvas.setScalingFactor(scalingFactor);
		mainScrollingArea.getHorizontalScrollBar().setUnitIncrement((int) scalingFactor);
		mainScrollingArea.getVerticalScrollBar().setUnitIncrement((int) scalingFactor);
		basicImage=new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);
		drawInitialImage(basicImage);
		canvas.setCanvasImage(basicImage);
		mainScrollingArea.setViewportView(canvas);
		autoEnablePanButton();
		imageModifiedForDialog=true;
		preview.setPreviewImage(canvas.getImage());
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
	}

	// Creates the Open File dialog and calls the OpenImage method with the returned file name.
	protected void openSelected() {
		if (fileChooser == null)
			prepareFileChooser();
		fileChooser.setDialogTitle(iEBundle.getString("IconEditorMsg3"));
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileFilter(lastFilter);
		int returnVal=fileChooser.showOpenDialog(mainPanel);

		File selectedFile=fileChooser.getSelectedFile();
		if (returnVal == JFileChooser.APPROVE_OPTION && selectedFile != null) {
			fileName=selectedFile.getAbsolutePath();
			int length=fileName.length();
			String extension=fileName.substring(length - 4);
			if (!extension.equalsIgnoreCase(".gif") && !extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".png") && !extension.equalsIgnoreCase(".bmp")) {
				System.out.println("Load File Failed");
				JOptionPane.showMessageDialog(this,iEBundle.getString("cannotLoad1") + selectedFile + iEBundle.getString("cannotLoad2"),iEBundle.getString("alert"),JOptionPane.ERROR_MESSAGE);
				isImageModified=true;
				imageModifiedForDialog=true;
				return;
			}
			// lastFilter = (MyFileFilter)fileChooser.getFileFilter();
			lastFilter=fileChooser.getFileFilter();
			openImage(fileName);
			imageSaved=false;
			imageModifiedForDialog=true;
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			isImageModified=true;
			imageModifiedForDialog=true;
			return;
		} else {
			System.out.println("Load File Failed");
			JOptionPane.showMessageDialog(this,iEBundle.getString("cannotLoad1") + selectedFile + iEBundle.getString("cannotLoad2"),iEBundle.getString("alert"),JOptionPane.ERROR_MESSAGE);
			isImageModified=true;
			imageModifiedForDialog=true;
		}
		preview.setPreviewImage(canvas.getImage());
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
	}// openSelected

	// Opens the indicated image file.
	private void openImage(String file) {
		fileName=file;
		// System.out.println("file: " +file);

		// Image img1 = utilities.blockingLoad(file);

		NewRestorableImageIcon imIcon=new NewRestorableImageIcon(file);

		Image img=imIcon.getImage();
		// Image img = Jimi.getImage(file);

		int img_width=img.getWidth(null);
		int img_height=img.getHeight(null);

		if (img_width > 0) {
			xSize=img_width;
			ySize=img_height;
			autoEnablePanButton();
			basicImage=Utilities.makeBufferedImage(img);
			canvas.setCanvasImage(basicImage);
			mainScrollingArea.setViewportView(canvas);
			currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);

		} else {
			JOptionPane.showMessageDialog(this,iEBundle.getString("noimage"),iEBundle.getString("alert"),JOptionPane.ERROR_MESSAGE);
			img.flush();
		}
		img.flush();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoResize();
			}
		});
	}// openImage

	// Opens the indicated image.
	public boolean openImage(Image img,boolean displayMessage) {
		if (img == null)
			return false;
		int img_width=img.getWidth(null);
		int img_height=img.getHeight(null);

		xSize=img_width;
		ySize=img_height;
		autoEnablePanButton();
		currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);

		basicImage=Utilities.makeBufferedImage(img);

		canvas.setCanvasImage(basicImage);
		mainScrollingArea.setViewportView(canvas);
		img.flush();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				autoResize();
			}
		});
		preview.setPreviewImage(canvas.getImage());
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
		return true;
	} // openImage(Image img, boolean displayMessage)

	// Creates the Save Image Dialog box and calls the saveImage method.
	public boolean saveSelected() {
		if (fileChooser == null)
			prepareFileChooser();
		fileChooser.setDialogTitle(iEBundle.getString("IconEditorMsg1"));
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

		fileChooser.setFileFilter(gifFilter);
		int returnVal=fileChooser.showSaveDialog(mainPanel);

		if (returnVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
			String desc=fileChooser.getFileFilter().getDescription();
			String selectedFilePath=fileChooser.getSelectedFile().getAbsolutePath();
			int length=selectedFilePath.length();
			String extension=selectedFilePath.substring(length - 4);
			imageSaved=true;

			boolean nonAcceptableExtension=false;
			if (!extension.equals(".gif") && !extension.equals(".jpg") && !extension.equals(".png") && !extension.equals(".bmp"))
				nonAcceptableExtension=true;

			// if no extension or unknown extension then the extension depends on the type selected
			if (nonAcceptableExtension) {
				if (desc.indexOf("gif") != -1) {
					extension=".gif";
					selectedFilePath=selectedFilePath.concat(extension);
					saveImage(selectedFilePath,canvas.getImage(),0);
				} else if (desc.indexOf("jpg") != -1) {
					extension=".jpg";
					selectedFilePath=selectedFilePath.concat(extension);
					saveImage(selectedFilePath,canvas.getImage(),1);
				} else if (desc.indexOf("png") != -1) {
					extension=".png";
					selectedFilePath=selectedFilePath.concat(extension);
					saveImage(selectedFilePath,canvas.getImage(),2);
				} else {
					extension=".bmp";
					selectedFilePath=selectedFilePath.concat(extension);
					saveImage(selectedFilePath,canvas.getImage(),3);
				}
			} else { // the extension is as given by the user
				if (extension.equals(".gif"))
					saveImage(selectedFilePath,canvas.getImage(),0);
				else if (extension.equals(".jpg"))
					saveImage(selectedFilePath,canvas.getImage(),1);
				else if (extension.equals(".png"))
					saveImage(selectedFilePath,canvas.getImage(),2);
				else
					saveImage(selectedFilePath,canvas.getImage(),3);
			}
			isImageModified=false;
			return true;
		} else {
			imageName="Untitled";
			// System.out.println("Save File Failed");
			return false;
		}
	}// saveSelected

	// saves the image as a GIF or JPEG or BMP or PNG
	private void saveImage(String file,BufferedImage img,int mode) {
		try {
			// RestorableImageIcon imageToSave = new RestorableImageIcon(img);
			NewRestorableImageIcon imageToSave=new NewRestorableImageIcon(img);
			FileOutputStream fout=new FileOutputStream(file);

			if (mode == 0) {
				// imageToSave.saveImage(RestorableImageIcon.GIF, fout);
				imageToSave.saveImage(NewRestorableImageIcon.GIF,fout);
				// GifEncoder ge = new GifEncoder(img,fout);
				// ge.encode();
			} else if (mode == 1) {
				// imageToSave.saveImage(RestorableImageIcon.JPG, fout);
				imageToSave.saveImage(NewRestorableImageIcon.JPG,fout);
				/*
				 * try{ JPEGImageEncoder jpgEncoder = JPEGCodec.createJPEGEncoder(fout);
				 * jpgEncoder.encode(convertToRGB(img)); } catch(ImageFormatException ex){System.out.println("wrong
				 * format");}
				 */
			} else if (mode == 2)
				// imageToSave.saveImage(RestorableImageIcon.PNG, fout);
				imageToSave.saveImage(NewRestorableImageIcon.PNG,fout);
			else
				// imageToSave.saveImage(RestorableImageIcon.BMP, fout);
				imageToSave.saveImage(NewRestorableImageIcon.BMP,fout);

			fout.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}// saveImage

	// Clears the Image
	public void clearImage() {
		takeUndoImageBackup();
		BufferedImage img=canvas.getImage();
		drawInitialImage(img);
		canvas.setCanvasImage(img);
		preview.setPreviewImage(img);
		uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,"image"));
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
		img.flush();
	}

	// sets the image size
	public void setImageSize(int width,int height) {
		basicImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		BufferedImage img=canvas.getImage();
		Graphics2D g=(Graphics2D) basicImage.getGraphics();

		g.setBackground(backColor);
		g.clearRect(0,0,width,height);

		g.setComposite(AlphaComposite.Src);

		g.drawImage(img,0,0,null);
		canvas.setCanvasImage(basicImage);
		preview.setPreviewImage(basicImage);
		mainScrollingArea.setViewportView(canvas);
		Dimension gridDim=new Dimension(0,0);
		int imageWidth=width * (int) scalingFactor;
		int imageHeight=height * (int) scalingFactor;
		gridDim.width=(extentSize.width > imageWidth) ? imageWidth : extentSize.width;
		gridDim.height=(extentSize.height > imageHeight) ? imageHeight : extentSize.height;

		gridPane.setPaintArea(gridDim);
		rootPane.setGlassPane(gridPane);

		currentImageSizeField.setText(iEBundle.getString("imSize") + width + " x " + height);
		autoEnablePanButton();
		if (realTimeUpdate && !pinDisconnected)
			sendIcon();
		img.flush();
		g.dispose();
		componentModified=true;
	}

	// loads the indicated by the given string icon
	public ImageIcon loadImageIcon(String filename,String description) {
		try {
			URL u=this.getClass().getResource(filename);
			if (u != null)
				return new ImageIcon(u,description);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}// end of loadImageIcon

	/*
	 * ComponentAdapter componentResize = new ComponentAdapter(){ public void componentResized(ComponentEvent e){
	 * System.out.println("component"); autoResize(); } };
	 */

	HierarchyBoundsAdapter hierarchyResize=new HierarchyBoundsAdapter() {
		public void ancestorResized(HierarchyEvent e) {
			// System.out.println("auto resize called in listener");
			autoResize();
		}
	};

	private void autoResize() {
		extentSize=mainScrollingArea.getViewport().getExtentSize();
		if (gridState) {
			Dimension gridDim=new Dimension(0,0);
			int imageWidth=canvas.getImage().getWidth() * (int) scalingFactor;
			int imageHeight=canvas.getImage().getHeight() * (int) scalingFactor;
			gridDim.width=(extentSize.width > imageWidth) ? imageWidth : extentSize.width;
			gridDim.height=(extentSize.height > imageHeight) ? imageHeight : extentSize.height;
			gridPane.setPaintArea(gridDim);
			rootPane.setGlassPane(gridPane);
			rootPane.getGlassPane().setVisible(gridState);
		}
		autoEnablePanButton();
	}

	// Listener implementations
	public void mouseClicked(MouseEvent e) {
		if (mode == SELECT_MODE) {
			canvas.deselectAll();
			clearSelection.setEnabled(false);
			fillSelection.setEnabled(false);
			invertSelection.setEnabled(false);
			invertSelMenu.setEnabled(false);
			fillMenu.setEnabled(false);
			clearSelMenu.setEnabled(false);
		}
	}

	// inner abstract class MyRunnable
	private abstract class WaitNow implements Runnable {
		MouseEvent e;

		String text;

		Cursor cursor;

		String mode;

		int factor;

		public WaitNow(MouseEvent e,String text,Cursor cursor) {
			this.e=e;
			this.text=text;
			this.cursor=cursor;
		}

		public WaitNow(String mode,int factor,String text,Cursor cursor) {
			this.mode=mode;
			this.factor=factor;
			this.text=text;
			this.cursor=cursor;
		}

		public WaitNow(String text,Cursor cursor) {
			this.text=text;
			this.cursor=cursor;
		}
	}// MyRunnable

	private Rectangle findProperRect(Point p1,Point p2) {
		int fromX=Math.min(p1.x,p2.x);
		int toX=Math.max(p1.x,p2.x);
		int fromY=Math.min(p1.y,p2.y);
		int toY=Math.max(p1.y,p2.y);
		return new Rectangle(fromX - canvas.getStrokeWidth() * (int) scalingFactor,fromY - canvas.getStrokeWidth() * (int) scalingFactor,toX + 2 * canvas.getStrokeWidth() * (int) scalingFactor,toY + 2 * canvas.getStrokeWidth() * (int) scalingFactor);
	}

	public void mouseEntered(MouseEvent e) {
		;
	}

	public void mouseExited(MouseEvent e) {
		;
	}

	public void mousePressed(MouseEvent e) {
		iconPalette.setFocused(false);
		if (!undoButton.isEnabled())
			undoButton.setEnabled(true);
		registerKeyboardAction(deleteSelListener,KeyStroke.getKeyStroke("DELETE"),JComponent.WHEN_IN_FOCUSED_WINDOW);
		if (canvas.intoImage(e.getPoint())) {
			isImageModified=true;
			imageModifiedForDialog=true;
			pointPressed=e.getPoint();
			if (mode == PEN) {
				Point viewPos=mainScrollingArea.getViewport().getViewPosition();
				Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
				takeUndoImageBackup(viewPos,downRight);
				drawPoint(e.getX(),e.getY(),false);
			}
			if (mode == RUBBER) {
				drawPoint(e.getX(),e.getY(),false);
			}
			if (mode == POLY) {
				if (SwingUtilities.isRightMouseButton(e)) {
					canvas.stopPoly(e.getX(),e.getY());
					firstPolyPoint=true;
					Point viewPos=mainScrollingArea.getViewport().getViewPosition();
					Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
					uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"shape"));
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
					return;
				} else if (e.getClickCount() == 2) {
					if (fillStyle == 7)
						canvas.closePoly(e.getX(),e.getY(),1);
					else if (fillStyle == 8)
						canvas.closePoly(e.getX(),e.getY(),2);
					else
						canvas.closePoly(e.getX(),e.getY(),3);
					firstPolyPoint=true;
					Point viewPos=mainScrollingArea.getViewport().getViewPosition();
					Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
					uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"shape"));
					if (realTimeUpdate && !pinDisconnected)
						sendIcon();
					return;
				} else {
					if (firstPolyPoint) {
						Point viewPos=mainScrollingArea.getViewport().getViewPosition();
						Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
						takeUndoImageBackup(viewPos,downRight);
						drawPoly(pointPressed,true);
						firstPolyPoint=false;
					} else
						drawPoly(pointPressed,false);
				}
			}
			if (mode == SELECT_MODE) {
				takeUndoSelectBackup();
				if (!e.isControlDown() && !e.isShiftDown()) {
					canvas.deselectAll();
					clearSelection.setEnabled(false);
					fillSelection.setEnabled(false);
					invertSelection.setEnabled(false);
					invertSelMenu.setEnabled(false);
					fillMenu.setEnabled(false);
					clearSelMenu.setEnabled(false);
				}
			}
			if (mode == PAN_MODE) {
				canvas.setCursor((Cursor) cursors[11]);
				gridPane.setCursor((Cursor) cursors[11]);
			}
			if (mode == MAGIC_WAND) {
				clearSelection.setEnabled(true);
				fillSelection.setEnabled(true);
				invertSelection.setEnabled(true);
				invertSelMenu.setEnabled(true);
				fillMenu.setEnabled(true);
				clearSelMenu.setEnabled(true);

				Cursor previousCursor=canvas.getCursor();
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				String previousText=currentModeField.getText();
				currentModeField.setForeground(Color.red);
				currentModeField.setText(iEBundle.getString("pleasewait"));

				SwingUtilities.invokeLater(new WaitNow(e,previousText,previousCursor) {
					public void run() {
						takeUndoSelectBackup();
						if (!this.e.isControlDown() && !this.e.isShiftDown()) {
							canvas.deselectAll();
						}
						if (this.e.isShiftDown())
							canvas.magicWand(this.e.getX(),this.e.getY(),selectionColor,"add");
						else if (this.e.isControlDown())
							canvas.magicWand(this.e.getX(),this.e.getY(),selectionColor,"remove");
						else
							canvas.magicWand(this.e.getX(),this.e.getY(),selectionColor,"normal");
						uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,selection,"select"));
						currentModeField.setForeground(Color.black);
						currentModeField.setText(text);
						canvas.setCursor(cursor);
						gridPane.setCursor(cursor);
					}
				});

				// Cursor previousCursor = canvas.getCursor();
				// canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				// String previousText = currentModeField.getText();
				// currentModeField.setText("Wait");
				// System.out.println("here0");
				/*
				 * takeUndoSelectBackup(); if (!e.isControlDown() && !e.isShiftDown()){ canvas.deselectAll(); } if
				 * (e.isShiftDown()) canvas.magicWand(e.getX(), e.getY(),selectionColor, "add"); else if
				 * (e.isControlDown()) canvas.magicWand(e.getX(), e.getY(),selectionColor, "remove"); else
				 * canvas.magicWand(e.getX(), e.getY(),selectionColor, "normal"); System.out.println("here1");
				 */
				// currentModeField.setText(previousText);
				// canvas.setCursor(previousCursor);
			}
			if (mode == DRAG_SELECTION_MODE) {
				if (SwingUtilities.isRightMouseButton(e)) {
					canvas.deselectAll();
					clearSelection.setEnabled(false);
					fillSelection.setEnabled(false);
					invertSelection.setEnabled(false);
					invertSelMenu.setEnabled(false);
					fillMenu.setEnabled(false);
					clearSelMenu.setEnabled(false);
					canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					return;
				}
				if (!(e.isShiftDown() || e.isControlDown())) {
					canvas.createAreaToDrag(pointPressed,backColor);
				} else {
					setMode(SELECT_MODE);
					mousePressed(e);
				}
			}
			if (mode == PICK_MODE) {
				int color=canvas.getPixel(e.getPoint());
				Color col=new Color(color);

				if (SwingUtilities.isRightMouseButton(e)) {
					setBackgroundColor(new Color(color));
					colorPalette.setSelectedBackgroundColor(col);
				} else {
					setForegroundColor(new Color(color));
					colorPalette.setSelectedForegroundColor(col);
				}
				colorPalette.repaint();
			}
			if (mode == FLAT_FILL_MODE) {
				if (canvas.existsSelection && !canvas.containsPoint(e.getPoint())) {
					getToolkit().beep();
					return;
				}

				Cursor previousCursor=canvas.getCursor();
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				String previousText=currentModeField.getText();
				currentModeField.setForeground(Color.red);
				currentModeField.setText(iEBundle.getString("pleasewait"));

				SwingUtilities.invokeLater(new WaitNow(e,previousText,previousCursor) {
					public void run() {
						takeUndoImageBackup();
						canvas.flatFill(this.e.getX(),this.e.getY(),foreColor);
						uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,"image"));
						currentModeField.setForeground(Color.black);
						currentModeField.setText(text);
						canvas.setCursor(cursor);
						gridPane.setCursor(cursor);
					}
				});
			}
		} else
			pointPressed=null;
	}// mousePressed

	public void mouseReleased(MouseEvent e) {
		if (pointPressed == null)
			return;
		if (mode == PEN) {
			Point viewPos=mainScrollingArea.getViewport().getViewPosition();
			Point downRight=new Point(viewPos.x + extentSize.width,viewPos.y + extentSize.height);
			uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,viewPos,downRight,scalingFactor,"shape"));
			if (realTimeUpdate && !pinDisconnected) {
				sendIcon();
			}
		}
		if (mode == LINE) {
			Rectangle undoRect=findProperRect(pointPressed,e.getPoint());
			takeUndoImageBackup(undoRect);
			drawLine(pointPressed,e.getPoint());
			uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,new Point(undoRect.x,undoRect.y),new Point(undoRect.width,undoRect.height),scalingFactor,"shape"));
			if (realTimeUpdate && !pinDisconnected)
				sendIcon();
		} else if (mode == RECT) {
			Rectangle undoRect=findProperRect(pointPressed,e.getPoint());
			takeUndoImageBackup(undoRect);
			if (e.isControlDown()) {
				if (fillStyle == 1)
					drawRectangle(pointPressed,e.getPoint(),true);
				else if (fillStyle == 2)
					fillRectangle(pointPressed,e.getPoint(),true,true);
				else
					fillRectangle(pointPressed,e.getPoint(),true,false);
			} else {
				if (fillStyle == 1)
					drawRectangle(pointPressed,e.getPoint(),false);
				else if (fillStyle == 2)
					fillRectangle(pointPressed,e.getPoint(),false,true);
				else
					fillRectangle(pointPressed,e.getPoint(),false,false);
			}
			uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,new Point(undoRect.x,undoRect.y),new Point(undoRect.width,undoRect.height),scalingFactor,"shape"));
			if (realTimeUpdate && !pinDisconnected)
				sendIcon();
		} else if (mode == ELLIPSE) {
			Rectangle undoRect=findProperRect(pointPressed,e.getPoint());
			takeUndoImageBackup(undoRect);
			if (e.isControlDown()) {
				if (fillStyle == 1)
					drawEllipse(pointPressed,e.getPoint(),true);
				else if (fillStyle == 2)
					fillEllipse(pointPressed,e.getPoint(),true,true);
				else
					fillEllipse(pointPressed,e.getPoint(),true,false);
			} else {
				if (fillStyle == 4)
					drawEllipse(pointPressed,e.getPoint(),false);
				else if (fillStyle == 5)
					fillEllipse(pointPressed,e.getPoint(),false,true);
				else
					fillEllipse(pointPressed,e.getPoint(),false,false);
			}
			uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoImage,new Point(undoRect.x,undoRect.y),new Point(undoRect.width,undoRect.height),scalingFactor,"shape"));
			if (realTimeUpdate && !pinDisconnected)
				sendIcon();
		} else if (mode == SELECT_MODE) {
			clearSelection.setEnabled(true);
			fillSelection.setEnabled(true);
			invertSelection.setEnabled(true);
			invertSelMenu.setEnabled(true);
			fillMenu.setEnabled(true);
			clearSelMenu.setEnabled(true);
			boolean isCrop=false;
			Point releasePoint=e.getPoint();
			if (!canvas.intoImage(e.getPoint())) {
				releasePoint=canvas.getProperPoint(e.getPoint());
			}
			if (e.isShiftDown())
				addToSelection(pointPressed,releasePoint);
			else if (e.isControlDown())
				removeFromSelection(pointPressed,releasePoint);
			else {
				select(pointPressed,releasePoint,false,rectangularSelection,e.isAltDown());
				if (cropButton.isSelected()) {
					isCrop=true;
					Rectangle r=canvas.getSelectedRect();
					if (r.width <= 0 || r.height <= 0)
						return;
					BufferedImage undoIm=canvas.getImage();
					xSize=r.width;
					ySize=r.height;
					BufferedImage selImage=canvas.getSelectedImage();
					canvas.setCanvasImage(selImage);
					setImageSize(xSize,ySize);
					canvas.deselectAll();
					clearSelection.setEnabled(false);
					fillSelection.setEnabled(false);
					invertSelection.setEnabled(false);
					invertSelMenu.setEnabled(false);
					fillMenu.setEnabled(false);
					clearSelMenu.setEnabled(false);
					currentImageSizeField.setText(iEBundle.getString("imSize") + xSize + " x " + ySize);
					uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,undoIm,"imageSize"));
					undoIm.flush();
				}
			}
			if (!isCrop)
				uSupport.postEdit(new MyUndoableEdit(ImageEditor.this,selection,"select"));
			return;
		} else if (mode == DRAG_SELECTION_MODE) {
			canvas.stopDragging(e.getPoint());
			// clearSelection.setEnabled(false);
			// fillSelection.setEnabled(false);
			// invertSelection.setEnabled(false);
			// invertSelMenu.setEnabled(false);
			// fillMenu.setEnabled(false);
			// clearSelMenu.setEnabled(false);
			if (realTimeUpdate && !pinDisconnected)
				sendIcon();
		} else if (mode == PAN_MODE) {
			canvas.setCursor((Cursor) cursors[10]);
			gridPane.setCursor((Cursor) cursors[10]);
			pan(e.getX() - pointPressed.x,e.getY() - pointPressed.y);
		}
		preview.setPreviewImage(canvas.getImage());
	}// mouseReleased

	AdjustmentListener adjustX=new AdjustmentListener() {
		public void adjustmentValueChanged(AdjustmentEvent e) {
			int xPos=mainScrollingArea.getViewport().getViewPosition().x;
			float floatDiv=(float) xPos / (float) scalingFactor;
			int intDiv=xPos / (int) scalingFactor;
			float adjustFactorX=floatDiv - intDiv;
			gridPane.setAdjustX(adjustFactorX);
		}
	};

	AdjustmentListener adjustY=new AdjustmentListener() {
		public void adjustmentValueChanged(AdjustmentEvent e) {
			int yPos=mainScrollingArea.getViewport().getViewPosition().y;
			float floatDiv=(float) yPos / (float) scalingFactor;
			int intDiv=yPos / (int) scalingFactor;
			float adjustFactorY=floatDiv - intDiv;
			gridPane.setAdjustY(adjustFactorY);
		}
	};

	// Motion Listeners
	// Pen Listener
	MouseMotionListener penListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				if (mode == RUBBER) {
					canvas.setCursor((Cursor) cursors[1]);
					gridPane.setCursor((Cursor) cursors[1]);
				} else {
					canvas.setCursor((Cursor) cursors[0]);
					gridPane.setCursor((Cursor) cursors[0]);
				}
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (canvas.intoImage(e.getPoint()))
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			drawPoint(e.getX(),e.getY(),true);
		}
	};// drawListener

	// Line Listener
	MouseMotionListener lineListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[2]);
				gridPane.setCursor((Cursor) cursors[2]);
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (canvas.intoImage(e.getPoint()))
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			drawDraggedLine(pointPressed,e.getPoint());
		}
	};// lineListener

	MouseMotionListener rectListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[3]);
				gridPane.setCursor((Cursor) cursors[3]);
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (canvas.intoImage(e.getPoint()))
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			int x=(e.getX() < 0) ? 0 : e.getX();
			int y=(e.getY() < 0) ? 0 : e.getY();

			if (e.isControlDown())
				drawDraggedRect(pointPressed,new Point(x,y),true);
			else
				drawDraggedRect(pointPressed,new Point(x,y),false);
		}
	};// rectListener

	MouseMotionListener ellipseListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[4]);
				gridPane.setCursor((Cursor) cursors[4]);
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (canvas.intoImage(e.getPoint()))
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			int x=(e.getX() < 0) ? 0 : e.getX();
			int y=(e.getY() < 0) ? 0 : e.getY();

			if (e.isControlDown())
				drawDraggedEllipse(pointPressed,new Point(x,y),true);
			else
				drawDraggedEllipse(pointPressed,new Point(x,y),false);
		}
	};// ellipseListener

	MouseMotionListener polyListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[5]);
				gridPane.setCursor((Cursor) cursors[5]);
				if (!firstPolyPoint)
					drawDraggedLine(pointPressed,e.getPoint());
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	};// polyListener

	MouseMotionListener flatFillListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				if (!canvas.existsSelection) {
					canvas.setCursor((Cursor) cursors[8]);
					gridPane.setCursor((Cursor) cursors[8]);
				}
				if (canvas.existsSelection && !canvas.containsPoint(e.getPoint())) {
					canvas.setCursor((Cursor) cursors[9]);
					gridPane.setCursor((Cursor) cursors[9]);
					currentModeField.setText(iEBundle.getString("noflatMode"));
				} else if (canvas.containsPoint(e.getPoint())) {
					canvas.setCursor((Cursor) cursors[8]);
					gridPane.setCursor((Cursor) cursors[8]);
					currentModeField.setText(iEBundle.getString("flatMode"));
				}
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	};// flatFillListener

	MouseMotionListener pickListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[7]);
				gridPane.setCursor((Cursor) cursors[7]);
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	};// pickListener

	MouseMotionListener selectListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
				if (mode == DRAG_SELECTION_MODE && (e.isControlDown() || e.isShiftDown())) {
					setMode(SELECT_MODE);
					return;
				}
				if (mode == SELECT_MODE && (e.isControlDown() || e.isShiftDown())) {
					// canvas.setCursor((Cursor)cursors[5]);
					// gridPane.setCursor((Cursor)cursors[5]);
					return;
				}
				if (mode == SELECT_MODE || mode == DRAG_SELECTION_MODE) {
					if (canvas.containsPoint(e.getPoint())) {
						setMode(DRAG_SELECTION_MODE);
					} else
						setMode(SELECT_MODE);
				}
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (pointPressed == null)
				return;
			if (mode == SELECT_MODE)
				select(pointPressed,e.getPoint(),true,rectangularSelection,e.isAltDown());
			else if (mode == DRAG_SELECTION_MODE) {
				canvas.dragSelection(e.getX(),e.getY());
			}
		}
	};// selectListener

	MouseMotionListener wandListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[6]);
				gridPane.setCursor((Cursor) cursors[6]);
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
				if (mode == DRAG_SELECTION_MODE && (e.isControlDown() || e.isShiftDown())) {
					setMode(MAGIC_WAND);
					return;
				}
				if (mode == MAGIC_WAND && (e.isControlDown() || e.isShiftDown()))
					return;
				if (mode == MAGIC_WAND || mode == DRAG_SELECTION_MODE) {
					if (canvas.containsPoint(e.getPoint()))
						setMode(DRAG_SELECTION_MODE);
					else
						setMode(MAGIC_WAND);
				}
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (mode == DRAG_SELECTION_MODE)
				canvas.dragSelection(e.getX(),e.getY());
		}
	};// wandListener

	MouseMotionListener panListener=new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent e) {
			if (canvas.intoImage(e.getPoint())) {
				canvas.setCursor((Cursor) cursors[10]);
				gridPane.setCursor((Cursor) cursors[10]);
				currentPosField.setText(iEBundle.getString("cell") + toOriginalCoords(e.getX()) + " , " + toOriginalCoords(e.getY()));
			} else {
				canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}

		public void mouseDragged(MouseEvent e) {
			pan(e.getX() - pointPressed.x,e.getY() - pointPressed.y);
		}
	};// ellipseListener

	MouseListener moveListenerForCursor=new MouseAdapter() {
		Cursor previousCursor;

		public void mouseEntered(MouseEvent e) {
			previousCursor=gridPane.getCursor();
			gridPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		public void mouseExited(MouseEvent e) {
			gridPane.setCursor(previousCursor);
		}
	};

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // new ESlateLookAndFeel());
		} catch (Exception e) {
		}

		JFrame jfr=new JFrame("ImageEditor");
		jfr.setResizable(true);

		ImageEditor iE=new ImageEditor();
		Dimension iEditorSize=iE.getPreferredSize();
		jfr.setSize(iEditorSize.width,iEditorSize.height + 20);

		jfr.getContentPane().add(iE);

		jfr.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		jfr.setVisible(true);
	}

}// end of ImageEditor class

class StyleArea extends JComponent {
	int width;

	boolean dashed;

	Stroke stroke, normalStroke;

	boolean state;

	int skinType; // 0: line, 1: outlined rect, 2: filledOutlinedRect 3: filled-nooutline rect

	// 4: outlined ellipse, 5: filledOutlinedEllipse, 6: filled-nonoutlined ellipse
	// 7: outlined poly, 8: filledOutlinedPoly, 9: filled-nonoutlined poly

	public StyleArea(int width,boolean dashed,int skin) {
		super();
		this.width=width;
		this.dashed=dashed;
		Dimension areaDimension;
		if (skin == 0)
			areaDimension=new Dimension(35,15);
		else
			areaDimension=new Dimension(35,30);
		setPreferredSize(areaDimension);
		setMinimumSize(areaDimension);
		setMaximumSize(areaDimension);
		skinType=skin;
		if (dashed)
			stroke=new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2,2},0);
		else
			stroke=new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0);

		normalStroke=new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0);
	}

	public void setSelected(boolean s) {
		if (s) {
			state=true;
			setBorder(new LineBorder(Color.red));
		} else {
			state=false;
			this.setBorder(null);
		}
	}

	public void setDashed(boolean d) {
		dashed=d;
		if (d)
			stroke=new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2,2},0);
		else
			stroke=new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0);
	}

	public boolean isDashed() {
		return dashed;
	}

	public boolean isSelected() {
		return state;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public int getStrokeWidth() {
		return width;
	}

	public void setSkin(int skin) {
		skinType=skin;
	}

	public int getSkin() {
		return skinType;
	}

	public void setStrokeWidth(int px) {
		width=px;
		if (dashed)
			stroke=new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0,new float[] {2,2},0);
		else
			stroke=new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,0);
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.black);
		g2.setStroke(stroke);
		int[] xPoints= {10,25,16,27,27,6,10};
		int[] yPoints= {7,7,17,17,23,23,7};

		if (skinType == 0)
			g2.drawLine(5,7,30,7);
		else if (skinType == 1)
			g2.drawRect(7,7,20,12);
		else if (skinType == 2) {
			g2.setColor(Color.gray);
			g2.fillRect(7,7,20,12);
			g2.setColor(Color.black);
			g2.drawRect(7,7,20,12);
		} else if (skinType == 3)
			g2.fillRect(7,7,20,12);
		else if (skinType == 4)
			g2.drawOval(7,7,20,12);
		else if (skinType == 5) {
			g2.setColor(Color.gray);
			g2.fillOval(7,7,20,12);
			g2.setColor(Color.black);
			g2.drawOval(7,7,20,12);
		} else if (skinType == 6)
			g2.fillOval(7,7,20,12);
		else if (skinType == 7)
			g2.drawPolygon(xPoints,yPoints,6);
		else if (skinType == 8) {
			g2.setColor(Color.gray);
			g2.fillPolygon(xPoints,yPoints,6);
			g2.setColor(Color.black);
			g2.drawPolygon(xPoints,yPoints,6);
		} else
			g2.fillPolygon(xPoints,yPoints,6);

		g2.setStroke(normalStroke);
	}
}
