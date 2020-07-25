package gr.cti.eslate.graph2;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.FemaleSingleIFSingleConnectionProtocolPlug;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.CalculatedFieldExistsException;
import gr.cti.eslate.database.engine.DoubleTableField;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.FieldNameInUseException;
import gr.cti.eslate.database.engine.FieldNotRemovableException;
import gr.cti.eslate.database.engine.FloatTableField;
import gr.cti.eslate.database.engine.IntegerTableField;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidFieldTypeException;
import gr.cti.eslate.database.engine.InvalidKeyFieldException;
import gr.cti.eslate.database.engine.NoFieldsInTableException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.StringTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.TableFieldBaseArray;
import gr.cti.eslate.database.engine.TableNotExpandableException;
import gr.cti.eslate.scripting.AsBarChart;
import gr.cti.eslate.scripting.LogoScriptable;
import gr.cti.eslate.tableModel.event.CellValueChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnAddedEvent;
import gr.cti.eslate.tableModel.event.ColumnRemovedEvent;
import gr.cti.eslate.tableModel.event.ColumnRenamedEvent;
import gr.cti.eslate.tableModel.event.ColumnReplacedEvent;
import gr.cti.eslate.tableModel.event.ColumnTypeChangedEvent;
import gr.cti.eslate.tableModel.event.DatabaseTableModelAdapter;
import gr.cti.eslate.tableModel.event.DatabaseTableModelListener;
import gr.cti.eslate.tableModel.event.RecordAddedEvent;
import gr.cti.eslate.tableModel.event.RecordRemovedEvent;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.DblBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.MathEF;

/**
 * Bar chart component.
 * @version     1.0.6, 21-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public class BarChart
    extends JPanel
    implements Externalizable, ESlatePart, AsBarChart, LogoScriptable
{
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	/** Used for externalization. */
	private static final int STORAGE_VERSION = 1;
	/** Keys used for externalization. */
//	private static final String KEY_FUNCTIONS = "functions";
	private static final String KEY_FIELD_X = "fieldX";
	private static final String KEY_FIELD_Y = "fieldY";
	/** E-Slate component version. */
	private final static String VERSION = "1.0.8";
	/** Continuous timer delays */
//	static final int INITIAL_DELAY=150;
//	static final int REPEAT_DELAY=30;
	/** Spinner step. */
//	private static final double SPINNER_STEP = 0.1;
	/** E-Slate handle. */
	private ESlateHandle handle = null;
	/** Chart panel. */
	private ChartPanel chartPanel;
//	/** Label indicating the function expression. */
//	private JLabel labelExpression;
//	/** Text field for entering the function to draw. */
//	private JTextField textFieldExpression;
//	/** List holding the history of showing expressions	 */
//	private JList listExpressions;
//	/** Scroll pane holding the expressions list. */
//	private JScrollPane scrollPaneExpressions;
//	/** Java math expression parser. */
//	private JEP jep;
	/** Category dataset. */
	private DefaultCategoryDataset dataset;
//	/** Popup menu of expressions list. */
//	private JPopupMenu popupMenu;
//	/** Menu item for function edit. */
//	private JMenuItem menuItemEdit;
//	/** Menu items for function domain. */
//	private JMenu menuDomain; 
//	private JMenuItem menuItemDomainFrom, menuItemDomainTo;
//	/** Menu item for changing drawing step. */
////	private JMenuItem menuItemStep;
//	/** Menu item for changing expression color. */
//	private JMenuItem menuItemColor;
//	/** Menu item for changing expression stroke. */
//	private JMenuItem menuItemStroke;
//	/** Menu items for transformation translation. */
//	private JMenu menuTranslation; 
//	private JCheckBoxMenuItem checkBoxMenuItemTranslationX, checkBoxMenuItemTranslationY;
//	/** Menu item for transformation rotation. */
//	private JCheckBoxMenuItem checkBoxMenuItemRotation;
//	/** Menu item for transformation scale. */
//	private JCheckBoxMenuItem checkBoxMenuItemScale;
//	/** Menu item for removing an expression from expressions list. */
//	private JMenuItem menuItemRemove;
//	/** Plugs of graph. */
//    private MultipleInputPlug plugExpression;
//    /** Min and max values of domain range. */
//    private double minX, maxX;
//    /** Check list manager. */
//    private CheckListManager checkListManager;
//    /** Previous selected expression index at list and chart. */
//    private int previousSelectedIndex = -1;
//    /** Array of showing functions. */
//    private ArrayList<Function> functions;
	/** Toolbar */
	private JToolBar toolBar;
//	/** Move buttons. */
//    private JButton buttonUp, buttonDown, buttonLeft, buttonRight;
//    /** Zoom buttons. */
//    private JButton buttonZoomIn, buttonZoomOut;
//    /** Reset at axes start button. */
//    private JButton buttonReset;
//    /** Normalize at y axis button. */
//    private JButton buttonNormalize;
//    /** Domain range spin buttons. */
//    private JSpinner spinnerDomainStart, spinnerDomainEnd;
//    /** A new expression to draw. */
//    private String expression;
//    /** Change listener for domain start spinner. */
//    private ChangeListener changeListenerDomainStart;
//    /** Change listener for domain end spinner. */
//    private ChangeListener changeListenerDomainEnd;
//    /** Visibility of expression insertion. */
//    private boolean expressionVisible = true;
//    /** Map of plug connected functions. */
//    private HashMap<ESlateHandle, Function> plugFunctions;
    /** Labels for field selection of plug connected database tables. */
    private JLabel labelX, labelY;
    /** Combo boxes for field selection of plug connected database tables. */
    private JComboBox comboBoxX, comboBoxY;
    /** Plug for connecting database tables */
    private FemaleSingleIFSingleConnectionProtocolPlug plug;
    /** Database table connected to graph through a plug. */
    private Table table;
    /**
     * Table model listener to attach to the database table.
     */
    private DatabaseTableModelListener tableModelListener;
    /** Selected database table fields for plot. */
    private String fieldX, fieldY;
    /** Arrays holding categories & values. */
    private StringBaseArray categories = new StringBaseArray();
    private DblBaseArray values = new DblBaseArray();
    /**
     * Indicates whether the graph data come from a local table or from
     * a table obtained through a plug connection.
     */
    private boolean usingLocalTable = false;
    /**
     * Local table from which to obtain data when accessing the component using Logo.
     */
    private Table localTable;
    /**
     * A <code>Double</code> equal to 0.0.
     */
    private final static Double ZERO = new Double(0.0);
    /**
     * Indicates that a data set contains numbers.
     */
    public final static int NUMBER = 0;
    /**
     * Indicates that a data set contains strings.
     */
    public final static int STRING = 1;

	/**
	 * Create a new graph.
	 */
	public BarChart() {
		super();
		
		initializeCommon();
		getESlateHandle();
	}
	
	/**
	 * ESR2 contructor.
	 * 
	 * @param oi
	 *            The input stream from which the component is constructed.
	 * @throws Exception
	 *             If something goes wrong during deserialization.
	 */
	public BarChart(ObjectInput oi) throws Exception {
		this();
		initialize(oi);
	}
	
	/**
	 * Common initialization.
	 */
	private void initializeCommon() {
		initialize();
	}

	/**
	 * Initialize from saved condition.
	 * @param oi Saved condition.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void initialize(ObjectInput oi) throws IOException, ClassNotFoundException {
		 StorageStructure fieldMap = (StorageStructure) oi.readObject();
		 
		 fieldX =  (String) fieldMap.get(KEY_FIELD_X); 
		 fieldY =  (String) fieldMap.get(KEY_FIELD_Y); 
		
//		 functions = (ArrayList<Function>) fieldMap.get(KEY_FUNCTIONS);
////		 SwingUtilities.invokeLater(new Runnable() {
////			 public void run() {
//				 DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
////				 defaultListModel.addElement(BundleMessages.getMessage("points"));
////				 int addedIndex = defaultListModel.getSize()-1;
////				 checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//				 for (int i = 0; i < functions.size(); i++) {
//					 Function function = functions.get(i);
//					 defaultListModel.addElement(function.getExpression());
//					 if (function.getExpression().equals(BundleMessages.getMessage("points"))) {
//						 drawPoints(function);
//					 }
//					 else
//						 drawFunction(function);
//					 int addedIndex = defaultListModel.getSize()-1;
//					 checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//				 }
////			 }
////		 });
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		initializeComponents();
		layoutComponents();
		attachListeners();
	}
	
	/**
	 * Initialize components.
	 */
	private void initializeComponents() {
		setPreferredSize(new Dimension(640, 480));
		
		localTable = new Table("Local data");
        tableModelListener = createTableModelListener();
//	    functions = new ArrayList<Function>();
//	    plugFunctions = new HashMap<ESlateHandle, Function>();
	    
		dataset = new DefaultCategoryDataset();
		JFreeChart chart = ChartFactory.createBarChart3D(null, // Title
				BundleMessages.getMessage("category"), // x-axis Label
				BundleMessages.getMessage("value"), // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		chartPanel = new ChartPanel(chart);
//		XYPlot plot = chart.getXYPlot();
//		NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//		axisX.setRange(-5, 5);
//		minX = axisX.getLowerBound();
//		maxX = axisX.getUpperBound();
//		NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//		axisY.setRange(-5, 5);
		
//		labelExpression = new JLabel("f(x) = ");
//		
//		textFieldExpression = new JTextField();
//		
//		listExpressions = new JList(new DefaultListModel()) {
//			private static final long serialVersionUID = 1L;
//            private int checkBoxWidth = new JCheckBox().getPreferredSize().width;
//            private int labelRemoveWidth = new JLabel(new ImageIcon(Graph2.class.getResource("images/clear.gif"))).getPreferredSize().width;
//            private int labelMenuWidth = new JLabel(new ImageIcon(Graph2.class.getResource("images/menu.gif"))).getPreferredSize().width;
//
//			public String getToolTipText(MouseEvent e) {
//                int row = locationToIndex(e.getPoint());
//                Rectangle cell = listExpressions.getCellBounds(row, row);
//                if (cell == null)
//                	return null;
//                if(e.getX()<=cell.x+checkBoxWidth) 
//                	return BundleMessages.getMessage("showHide");
//                else if(e.getX()>=cell.width-labelRemoveWidth) 
//                	return BundleMessages.getMessage("remove");
//                else if(e.getX()>=cell.width-labelRemoveWidth-labelMenuWidth && e.getX()<cell.width-labelRemoveWidth) 
//                	return BundleMessages.getMessage("menu");
//                return null;
//            }
//		};
//		listExpressions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		listExpressions.setFocusable(false);
//        checkListManager = new CheckListManager(listExpressions, this);
//		
//		scrollPaneExpressions = new JScrollPane(listExpressions);
//		
//		// Create a new parser object.
//		jep = new JEP();
//		// Add the standard functions and constants if you want to be able to
//		// evaluate expressions with trigonometric functions and the constants
//		// pi and e.
//		jep.addStandardFunctions();
//		jep.addStandardConstants();
//
//		popupMenu = new JPopupMenu();
//        popupMenu.setLightWeightPopupEnabled(false);
//        menuItemEdit = new JMenuItem();
//        menuItemEdit.setText(BundleMessages.getMessage("edit"));
////        popupMenu.add(menuItemEdit);
//        menuDomain = new JMenu();
//        menuDomain.setText(BundleMessages.getMessage("domain"));
//        menuItemDomainFrom = new JMenuItem();
//        menuItemDomainFrom.setText(BundleMessages.getMessage("from"));
//        menuDomain.add(menuItemDomainFrom);
//        menuItemDomainTo = new JMenuItem();
//        menuItemDomainTo.setText(BundleMessages.getMessage("to"));
//        menuDomain.add(menuItemDomainTo);
////        popupMenu.add(menuDomain);
////        menuItemStep = new JMenuItem();
////        menuItemStep.setText(BundleMessages.getMessage("step"));
////        popupMenu.add(menuItemStep);
//        menuItemColor = new JMenuItem();
//        menuItemColor.setText(BundleMessages.getMessage("color"));
////        popupMenu.add(menuItemColor);
//        menuItemStroke = new JMenuItem();
//        menuItemStroke.setText(BundleMessages.getMessage("stroke"));
////        popupMenu.add(menuItemStroke);
//        menuTranslation = new JMenu();
//        menuTranslation.setText(BundleMessages.getMessage("translation"));
//        checkBoxMenuItemTranslationX = new JCheckBoxMenuItem();
//        checkBoxMenuItemTranslationX.setText("x");
//        menuTranslation.add(checkBoxMenuItemTranslationX);
//        checkBoxMenuItemTranslationY = new JCheckBoxMenuItem();
//        checkBoxMenuItemTranslationY.setText("y");
//        menuTranslation.add(checkBoxMenuItemTranslationY);
////        popupMenu.add(menuTranslation);
//        checkBoxMenuItemRotation = new JCheckBoxMenuItem();
//        checkBoxMenuItemRotation.setText(BundleMessages.getMessage("rotation"));
////        popupMenu.add(checkBoxMenuItemRotation);
//        checkBoxMenuItemScale = new JCheckBoxMenuItem();
//        checkBoxMenuItemScale.setText(BundleMessages.getMessage("scale"));
////        popupMenu.add(checkBoxMenuItemScale);
//        menuItemRemove = new JMenuItem();
//        menuItemRemove.setText(BundleMessages.getMessage("remove"));
////        popupMenu.add(menuItemRemove);
////	    popupMenu.pack();
	    
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setBorderPainted(false);
		toolBar.setOpaque(false);
		
//		Dimension dimensionButtons = new Dimension(20, 20);
//		
//		buttonUp = new JButton();
//		buttonUp.setIcon(new ImageIcon(Graph2.class.getResource("images/up.gif")));
//		buttonUp.setFocusPainted(false);
//		buttonUp.setRequestFocusEnabled(false);
//		buttonUp.setFocusable(false);
//		buttonUp.setBorderPainted(false);
//		buttonUp.setOpaque(false);
//        buttonUp.setToolTipText(BundleMessages.getMessage("buttonUp"));
////        buttonUp.setMinimumSize(dimensionButtons);
////        buttonUp.setMaximumSize(dimensionButtons);
//        buttonUp.setPreferredSize(dimensionButtons);
//		
//		buttonDown = new JButton();
//		buttonDown.setIcon(new ImageIcon(Graph2.class.getResource("images/down.gif")));
//		buttonDown.setFocusPainted(false);
//		buttonDown.setRequestFocusEnabled(false);
//		buttonDown.setFocusable(false);
//		buttonDown.setOpaque(false);
//		buttonDown.setBorderPainted(false);
//        buttonDown.setToolTipText(BundleMessages.getMessage("buttonDown"));
////        buttonDown.setMinimumSize(dimensionButtons);
////        buttonDown.setMaximumSize(dimensionButtons);
//        buttonDown.setPreferredSize(dimensionButtons);
//		
//		buttonLeft = new JButton();
//		buttonLeft.setIcon(new ImageIcon(Graph2.class.getResource("images/left.gif")));
//		buttonLeft.setFocusPainted(false);
//		buttonLeft.setRequestFocusEnabled(false);
//		buttonLeft.setFocusable(false);
//		buttonLeft.setOpaque(false);
//		buttonLeft.setBorderPainted(false);
//        buttonLeft.setToolTipText(BundleMessages.getMessage("buttonLeft"));
////        buttonLeft.setMinimumSize(dimensionButtons);
////        buttonLeft.setMaximumSize(dimensionButtons);
//        buttonLeft.setPreferredSize(dimensionButtons);
//		
//		buttonRight = new JButton();
//		buttonRight.setIcon(new ImageIcon(Graph2.class.getResource("images/right.gif")));
//		buttonRight.setFocusPainted(false);
//		buttonRight.setRequestFocusEnabled(false);
//		buttonRight.setFocusable(false);
//		buttonRight.setOpaque(false);
//		buttonRight.setBorderPainted(false);
//        buttonRight.setToolTipText(BundleMessages.getMessage("buttonRight"));
////        buttonRight.setMinimumSize(dimensionButtons);
////        buttonRight.setMaximumSize(dimensionButtons);
//        buttonRight.setPreferredSize(dimensionButtons);
//		
//		buttonReset = new JButton();
//		buttonReset.setIcon(new ImageIcon(Graph2.class.getResource("images/reset.gif")));
//		buttonReset.setFocusPainted(false);
//		buttonReset.setRequestFocusEnabled(false);
//		buttonReset.setFocusable(false);
//		buttonReset.setBorderPainted(false);
//		buttonReset.setOpaque(false);
//		buttonReset.setToolTipText(BundleMessages.getMessage("buttonReset"));
////		buttonReset.setMinimumSize(dimensionButtons);
////		buttonReset.setMaximumSize(dimensionButtons);
//		buttonReset.setPreferredSize(dimensionButtons);
//		
//		buttonNormalize = new JButton();
//		buttonNormalize.setIcon(new ImageIcon(Graph2.class.getResource("images/normalize.gif")));
//		buttonNormalize.setFocusPainted(false);
//		buttonNormalize.setRequestFocusEnabled(false);
//		buttonNormalize.setFocusable(false);
//		buttonNormalize.setBorderPainted(false);
//		buttonNormalize.setOpaque(false);
//		buttonNormalize.setToolTipText(BundleMessages.getMessage("buttonNormalize"));
////		buttonNormalize.setMinimumSize(dimensionButtons);
////		buttonNormalize.setMaximumSize(dimensionButtons);
//		buttonNormalize.setPreferredSize(dimensionButtons);
//		
//		buttonZoomIn = new JButton();
//		buttonZoomIn.setIcon(new ImageIcon(Graph2.class.getResource("images/zoomIn.gif")));
//		buttonZoomIn.setFocusPainted(false);
//		buttonZoomIn.setRequestFocusEnabled(false);
//		buttonZoomIn.setFocusable(false);
//		buttonZoomIn.setOpaque(false);
//		buttonZoomIn.setBorderPainted(false);
//        buttonZoomIn.setToolTipText(BundleMessages.getMessage("buttonZoomIn"));
////        buttonZoomIn.setMinimumSize(dimensionButtons);
////        buttonZoomIn.setMaximumSize(dimensionButtons);
//        buttonZoomIn.setPreferredSize(dimensionButtons);
//		
//		buttonZoomOut = new JButton();
//		buttonZoomOut.setIcon(new ImageIcon(Graph2.class.getResource("images/zoomOut.gif")));
//		buttonZoomOut.setFocusPainted(false);
//		buttonZoomOut.setRequestFocusEnabled(false);
//		buttonZoomOut.setFocusable(false);
//		buttonZoomOut.setOpaque(false);
//		buttonZoomOut.setBorderPainted(false);
//        buttonZoomOut.setToolTipText(BundleMessages.getMessage("buttonZoomOut"));
////        buttonZoomOut.setMinimumSize(dimensionButtons);
////        buttonZoomOut.setMaximumSize(dimensionButtons);
//        buttonZoomOut.setPreferredSize(dimensionButtons);
//		
//		spinnerDomainStart = new JSpinner();
//		spinnerDomainStart.setRequestFocusEnabled(false);
//		spinnerDomainStart.setFocusable(false);
//		spinnerDomainStart.setOpaque(false);
//		JTextField spinnerTextField = ((JSpinner.DefaultEditor) spinnerDomainStart.getEditor()).getTextField();
//		Font spinnerFont = spinnerTextField.getFont();
//		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(
//				axisX.getLowerBound(), 
//				-Double.MAX_VALUE, 
//				Double.MAX_VALUE, 
//				0.1);
//	    spinnerDomainStart.setModel(spinnerNumberModel);
//	    spinnerDomainStart.setFont(spinnerFont);
//	    final int spinnersWidth = 64;
//	    spinnerDomainStart.setPreferredSize(new Dimension(spinnersWidth, spinnerDomainStart.getPreferredSize().height));
//        spinnerDomainStart.setToolTipText(BundleMessages.getMessage("spinnerDomainStart"));
//		
//		spinnerDomainEnd = new JSpinner();
//		spinnerDomainEnd.setRequestFocusEnabled(false);
//		spinnerDomainEnd.setFocusable(false);
//		spinnerDomainEnd.setOpaque(false);
//		spinnerNumberModel = new SpinnerNumberModel(
//				axisX.getUpperBound(), 
//				-Double.MAX_VALUE, 
//				Double.MAX_VALUE, 
//				0.1);
//	    spinnerDomainEnd.setModel(spinnerNumberModel);
//	    spinnerDomainEnd.setFont(spinnerFont);
//	    spinnerDomainEnd.setPreferredSize(new Dimension(spinnersWidth, spinnerDomainEnd.getPreferredSize().height));
//        spinnerDomainEnd.setToolTipText(BundleMessages.getMessage("spinnerDomainEnd"));
        
        labelX = new JLabel();
        labelX.setText(BundleMessages.getMessage("category"));
		
        labelY = new JLabel();
        labelY.setText(BundleMessages.getMessage("value"));
        
        comboBoxX = new JComboBox();
        comboBoxX.setRequestFocusEnabled(false);
        comboBoxX.setFocusable(false);
        comboBoxX.setOpaque(false);
        comboBoxX.setToolTipText(BundleMessages.getMessage("comboBoxCategory"));
        Dimension dimensionComboBox = new Dimension(80, 20);
        comboBoxX.setPreferredSize(dimensionComboBox);
        comboBoxX.setMaximumSize(dimensionComboBox);
        comboBoxX.setMinimumSize(dimensionComboBox);
        comboBoxX.setEditable(false);
        comboBoxX.setEnabled(false);
        
        comboBoxY = new JComboBox();
        comboBoxY.setRequestFocusEnabled(false);
        comboBoxY.setFocusable(false);
        comboBoxY.setOpaque(false);
        comboBoxY.setToolTipText(BundleMessages.getMessage("comboBoxValue"));
        comboBoxY.setPreferredSize(dimensionComboBox);
        comboBoxY.setMaximumSize(dimensionComboBox);
        comboBoxY.setMinimumSize(dimensionComboBox);
        comboBoxY.setEditable(false);
        comboBoxY.setEnabled(false);
	}

    /**
     * Creates the database table model listener that is attached to the
     * table from which data are read.
     * @return  The created listener.
     */
    private DatabaseTableModelListener createTableModelListener()
    {
        return new DatabaseTableModelAdapter() {
//			public void tableRenamed(TableRenamedEvent tablerenamedevent) {
//			}
//			public void tableHiddenStateChanged(TableHiddenStateChangedEvent tablehiddenstatechangedevent) {
//			}
//			public void selectedRecordSetChanged(SelectedRecordSetChangedEvent selectedrecordsetchangedevent) {
//			}
//			public void rowOrderChanged(RowOrderChangedEvent roworderchangedevent) {
//			}
			public void recordRemoved(RecordRemovedEvent recordremovedevent) {
				plotTable();
			}
			public void recordAdded(RecordAddedEvent recordaddedevent) {
				plotTable();
			}
//			public void currencyFieldChanged(ColumnEvent columnevent) {
//			}
			public void columnTypeChanged(ColumnTypeChangedEvent columntypechangedevent) {
				reloadFields();
			}
			public void columnReplaced(ColumnReplacedEvent columnreplacedevent) {
				reloadFields();
			}
			public void columnRenamed(ColumnRenamedEvent columnrenamedevent) {
				reloadFields();
			}
			public void columnRemoved(ColumnRemovedEvent columnremovedevent) {
				reloadFields();
			}
//			public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent columnremovablestatechangedevent) {
//			}
//			public void columnKeyChanged(ColumnKeyChangedEvent columnkeychangedevent) {
//			}
//			public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent columnhiddenstatechangedevent) {
//			}
//			public void columnEditableStateChanged(ColumnEditableStateChangedEvent columneditablestatechangedevent) {
//			}
			public void columnAdded(ColumnAddedEvent columnaddedevent) {
				reloadFields();
			}
			public void cellValueChanged(CellValueChangedEvent cellvaluechangedevent) {
				plotTable();
			}
//			public void calcColumnReset(CalcColumnResetEvent calccolumnresetevent) {
//			}
//			public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent calccolumnformulachangedevent) {
//			}
//			public void activeRecordChanged(ActiveRecordChangedEvent activerecordchangedevent) {
//			}
        };
    }
	
	/**
	 * Layout components.
	 */
	private void layoutComponents() {
		setLayout(new ExplicitLayout());
		
		toolBar.setLayout(new ExplicitLayout());
//		final int vGap = 8;
//        toolBar.add(spinnerDomainStart, new ExplicitConstraints(spinnerDomainStart,
//        		ContainerEF.left(toolBar).add(32),
////        		ContainerEF.centerY(toolBar).subtract(4),
//        		ComponentEF.top(spinnerDomainEnd).subtract(vGap),
//        		null, null,
//        		0.0, 1.0, true, true
//        ));
//        toolBar.add(spinnerDomainEnd, new ExplicitConstraints(spinnerDomainEnd,
//        		ComponentEF.centerX(spinnerDomainStart),
////        		ComponentEF.bottom(spinnerDomainStart).add(8),
//        		ContainerEF.centerY(toolBar),
//        		null, null,
//        		0.5, 0.5, true, true
//        ));
//        Separator separator = new Separator();
//        int separatorsWidth = 32;
//        toolBar.add(separator, new ExplicitConstraints(separator,
//        		ComponentEF.right(spinnerDomainStart),
////        		ContainerEF.centerY(toolBar),
//        		ComponentEF.top(spinnerDomainStart),
//        		MathEF.constant(separatorsWidth), 
////        		ContainerEF.height(toolBar).divide(2),
//        		ComponentEF.height(spinnerDomainStart).multiply(2).add(vGap),
//        		0.0, 0.0, true, true
//        ));
//        toolBar.add(buttonLeft, new ExplicitConstraints(buttonLeft,
//        		ComponentEF.right(separator),
//        		ComponentEF.centerY(buttonUp),
//        		null, null,
//        		0.0, 0.0, true, true
//        ));
//        toolBar.add(buttonUp, new ExplicitConstraints(buttonUp,
//        		ComponentEF.right(buttonLeft),
////        		ContainerEF.centerY(toolBar),
//        		ComponentEF.bottom(spinnerDomainStart).add(vGap/2),
//        		null, null,
//        		0.0, 1.0, true, true
//        ));
//        toolBar.add(buttonDown, new ExplicitConstraints(buttonDown,
//        		ComponentEF.centerX(buttonUp),
//        		ComponentEF.bottom(buttonUp),
//        		null, null,
//        		0.5, 0.0, true, true
//        ));
//        toolBar.add(buttonRight, new ExplicitConstraints(buttonRight,
//        		ComponentEF.right(buttonUp),
//        		ComponentEF.centerY(buttonLeft),
//        		null, null,
//        		0.0, 0.5, true, true
//        ));
//        separator = new Separator();
//        toolBar.add(separator, new ExplicitConstraints(separator,
//        		ComponentEF.right(buttonRight),
////        		ContainerEF.centerY(toolBar),
//        		ComponentEF.top(spinnerDomainStart),
//        		MathEF.constant(separatorsWidth), 
////        		ContainerEF.height(toolBar).divide(2),
//        		ComponentEF.height(spinnerDomainStart).multiply(2).add(vGap),
//        		0.0, 0.0, true, true
//        ));
//        toolBar.add(buttonZoomIn, new ExplicitConstraints(buttonZoomIn,
//        		ComponentEF.right(separator),
////        		ContainerEF.centerY(toolBar),//.subtract(4),
//        		ComponentEF.bottom(spinnerDomainStart).add(vGap/2),
//        		null, null,
//        		0.0, 1.0, true, true
//        ));
//        toolBar.add(buttonZoomOut, new ExplicitConstraints(buttonZoomOut,
//        		ComponentEF.centerX(buttonZoomIn),
//        		ComponentEF.bottom(buttonZoomIn),//.add(8),
//        		null, null,
//        		0.5, 0.0, true, true
//        ));
//        separator = new Separator();
//        toolBar.add(separator, new ExplicitConstraints(separator,
//        		ComponentEF.right(buttonZoomIn),
////        		ContainerEF.centerY(toolBar),
//        		ComponentEF.top(spinnerDomainStart),
//        		MathEF.constant(separatorsWidth), 
////        		ContainerEF.height(toolBar).divide(2),
//        		ComponentEF.height(spinnerDomainStart).multiply(2).add(vGap),
//        		0.0, 0.0, true, true
//        ));
//        toolBar.add(buttonReset, new ExplicitConstraints(buttonReset,
//        		ComponentEF.right(separator),
////        		ContainerEF.centerY(toolBar),//.subtract(4),
//        		ComponentEF.bottom(spinnerDomainStart).add(vGap/2),
//        		null, null,
//        		0.0, 1.0, true, true
//        ));
//        toolBar.add(buttonNormalize, new ExplicitConstraints(buttonNormalize,
//        		ComponentEF.centerX(buttonReset),
//        		ComponentEF.bottom(buttonReset),//.add(8),
//        		null, null,
//        		0.5, 0.0, true, true
//        ));
        toolBar.add(labelX, new ExplicitConstraints(labelX,
        		ComponentEF.left(comboBoxX).subtract(8),
        		ComponentEF.centerY(comboBoxX),
        		null, null,
        		1.0, 0.5, true, true
        ));
        toolBar.add(comboBoxX, new ExplicitConstraints(comboBoxX,
        		ContainerEF.centerX(toolBar).subtract(32),
        		ContainerEF.centerY(toolBar),
        		null, null,
        		1.0, 0.5, true, true
        ));
        toolBar.add(labelY, new ExplicitConstraints(labelY,
        		ContainerEF.centerX(toolBar).add(32),
        		ComponentEF.centerY(comboBoxY),
        		null, null,
        		0.0, 0.5, true, true
        ));
        toolBar.add(comboBoxY, new ExplicitConstraints(comboBoxY,
        		ComponentEF.right(labelY).add(8),
        		ComponentEF.centerY(comboBoxX),
        		null, null,
        		0.0, 0.5, true, true
        ));

//		Dimension preferredSize = buttonUp.getPreferredSize();
//		int minSize = Math.min(preferredSize.width, preferredSize.height);
//		toolBar.setPreferredSize(new Dimension(toolBar.getPreferredSize().width, minSize*2));
//		Dimension buttonsSize = new Dimension(minSize, minSize);
//		buttonUp.setPreferredSize(buttonsSize);
//		buttonDown.setPreferredSize(buttonsSize);
//		buttonLeft.setPreferredSize(buttonsSize);
//		buttonRight.setPreferredSize(buttonsSize);
//		buttonZoomIn.setPreferredSize(buttonsSize);
//		buttonZoomOut.setPreferredSize(buttonsSize);
        
//        if (expressionVisible) {
//	        add(labelExpression, new ExplicitConstraints(labelExpression,
//					ContainerEF.left(this).add(8),
//					ComponentEF.centerY(textFieldExpression),
//					null, null,
//					0.0, 0.5, true, true
//			));
//			add(textFieldExpression, new ExplicitConstraints(textFieldExpression,
//					ComponentEF.right(labelExpression),
//					ContainerEF.top(this).add(4),
//					ContainerEF.width(this).subtract(8).subtract(ComponentEF.right(labelExpression)), 
//					null,
//					0.0, 0.0, true, true
//			));
//        }
		add(toolBar, new ExplicitConstraints(toolBar,
				ContainerEF.left(this),
				ContainerEF.bottom(this),
				ContainerEF.width(this),	
				MathEF.constant(32),
				0.0, 1.0, true, true
		));
//		add(scrollPaneExpressions, new ExplicitConstraints(scrollPaneExpressions,
//				ContainerEF.right(this).subtract(8),
//				ContainerEF.bottom(this).subtract(4),
//				ContainerEF.width(this).subtract(8).subtract(ComponentEF.right(toolBar)),	
//				MathEF.constant(96),
//				1.0, 1.0, true, true
//		));
		add(chartPanel, new ExplicitConstraints(chartPanel,
				ContainerEF.left(this),
				ContainerEF.top(this),
				ContainerEF.width(this),
				ComponentEF.top(toolBar).subtract(ContainerEF.top(this)).subtract(8),
				0.0, 0.0, true, true
		));
	}
	
	/**
	 * Attach listeners.
	 */
	private void attachListeners() {
//		textFieldExpression.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String expression = textFieldExpression.getText();
//				addFunction(expression);
////				if (!isExpressionValid(expression))
////					return;
//////				DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//////				if (defaultListModel.indexOf(expression) != -1)
//////					return;
////				for (int i = 0; i < functions.size(); i++) {
////					if (functions.get(i).getExpression().equals(expression))
////						return;
////				}
//				
////				Function function = new Function(expression);
////				JFreeChart chart = chartPanel.getChart();
////				XYPlot plot = chart.getXYPlot();
////				NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
////				minX = axisX.getLowerBound();
////				maxX = axisX.getUpperBound();
////				function.setDomainFrom(minX);
////				function.setDomainTo(maxX);
////				addFunction(function);
////				DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
////				int addedIndex = defaultListModel.getSize()-1;
////				checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
////				textFieldExpression.setText(null);
//			}
//		});
//
////		listExpressions.addMouseListener(new MouseAdapter() {
////			private void maybePopupTrigger(final MouseEvent e) {
////				final int selectedIndex = listExpressions.getSelectedIndex();
////				if (e.isPopupTrigger() && selectedIndex!=-1 && listExpressions.getCellBounds(selectedIndex, selectedIndex).contains(e.getPoint())) {
////					SwingUtilities.invokeLater(new Runnable() {
////						public void run() {
////							popupMenu.show(e.getComponent(), e.getX(), e.getY());
////							Function function = functions.get(selectedIndex);
////							checkBoxMenuItemTranslationX.setSelected(function.getTranslationX()!=0);
////							checkBoxMenuItemTranslationX.setText("x"+(function.getTranslationX()==0?"":" ("+function.getTranslationX()+")"));
////							checkBoxMenuItemTranslationY.setSelected(function.getTranslationY()!=0);
////							checkBoxMenuItemTranslationY.setText("y"+(function.getTranslationY()==0?"":" ("+function.getTranslationY()+")"));
////							checkBoxMenuItemRotation.setSelected(function.getRotation()!=0);
////							checkBoxMenuItemRotation.setText(BundleMessages.getMessage("rotation")+(function.getRotation()==0?"":" ("+function.getRotation()+")"));
////							checkBoxMenuItemScale.setSelected(function.getScale()!=1);
////							checkBoxMenuItemScale.setText(BundleMessages.getMessage("scale")+(function.getScale()==1?"":" ("+function.getScale()+")"));
////						}
////					});
////				}
////			}
////			public void mousePressed(MouseEvent e) {
////				if (SwingUtilities.isRightMouseButton(e)) {
////					try {
////						Robot robot = new java.awt.Robot();
////						robot.mousePress(InputEvent.BUTTON1_MASK);
////						robot.mouseRelease(InputEvent.BUTTON1_MASK);
////					} catch (AWTException ae) {
////						System.out.println(ae);
////					}
////					maybePopupTrigger(e);
////				}
////			}
////			public void mouseReleased(MouseEvent e) {
////				maybePopupTrigger(e);
////			}
////		});
//		
//		listExpressions.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				if (e.getValueIsAdjusting())
//					 return;
//				int selectedIndex = listExpressions.getSelectedIndex();
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				XYItemRenderer itemRenderer = plot.getRenderer();
//				if (previousSelectedIndex != -1)
//					itemRenderer.setSeriesPaint(previousSelectedIndex, functions.get(previousSelectedIndex).getColor());
//				if (selectedIndex != -1) {
//					functions.get(selectedIndex).setColor((Color) itemRenderer.getSeriesPaint(selectedIndex));
//					itemRenderer.setSeriesPaint(selectedIndex, Color.BLACK);
//				}
//				previousSelectedIndex = selectedIndex;
//			}
//		});
//		
//		menuItemDomainFrom.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				Object domainFromObject = JOptionPane.showInputDialog(
//						Graph2.this, 
//						BundleMessages.getMessage("from"), 
//						BundleMessages.getMessage("domain"), 
//						JOptionPane.PLAIN_MESSAGE,
//						null, 
//						null,
//						function.getDomainFrom()); 
//				if (domainFromObject == null)
//					return;
//				try {
//					double domainFrom = Double.parseDouble((String) domainFromObject);
//					if (function.getDomainFrom() == domainFrom)
//						return;
//					if (domainFrom >= function.getDomainTo())
//						return;
//					function.setDomainFrom(domainFrom);
//					refreshFunctions();
//				} catch (Exception ex) {
//					return;
//				}
//			}
//		});
//		
//		menuItemDomainTo.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				Object domainToObject = JOptionPane.showInputDialog(
//						Graph2.this, 
//						BundleMessages.getMessage("to"), 
//						BundleMessages.getMessage("domain"), 
//						JOptionPane.PLAIN_MESSAGE,
//						null, 
//						null,
//						function.getDomainTo()); 
//				if (domainToObject == null)
//					return;
//				try {
//					double domainTo = Double.parseDouble((String) domainToObject);
//					if (function.getDomainTo() == domainTo)
//						return;
//					if (domainTo <= function.getDomainFrom())
//						return;
//					function.setDomainTo(domainTo);
//					refreshFunctions();
//				} catch (Exception ex) {
//					return;
//				}
//			}
//		});
//		
//		menuItemEdit.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//		    	int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				Object expressionObject = JOptionPane.showInputDialog(
//						Graph2.this, 
//						null, 
//						BundleMessages.getMessage("function"), 
//						JOptionPane.PLAIN_MESSAGE,
//						null, 
//						null,
//						function.getExpression()); 
//				if (expressionObject == null)
//					return;
//				String expression = (String) expressionObject;
//				if (!isExpressionValid(expression))
//					return;
////				DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
////				if (defaultListModel.indexOf(expression) != -1)
////					return;
//				for (int i = 0; i < functions.size(); i++) {
//					if (functions.get(i).getExpression().equals(expression))
//						return;
//				}
//				DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//				defaultListModel.set(defaultListModel.indexOf(function.getExpression()), expression);
//				function.setExpression(expression);
//				refreshFunctions();
//			}
//		});
//		
////		menuItemStep.addActionListener(new ActionListener() {
////			public void actionPerformed(ActionEvent e) {
////		    	int selectedIndex = listExpressions.getSelectedIndex();
////				Function function = functions.get(selectedIndex);
////		        Object[] options = {"0.01", "0.05", "0.1", "0.5", "1.0"};
////				Object stepObject = JOptionPane.showInputDialog(
////						Graph2.this, 
////						null, 
////						BundleMessages.getMessage("step"), 
////						JOptionPane.PLAIN_MESSAGE, 
////						null, 
////						options, 
////						String.valueOf(function.getStep()));
////				if (stepObject == null)
////					return;
////				double step = Double.parseDouble((String) stepObject);
////				function.setStep(step);
//////				removeFunction(selectedIndex);
//////				addFunction(function);
////				refreshFunctions();
////			}
////		});
//		
//		menuItemColor.addActionListener(new ActionListener() {
//		    public void actionPerformed(ActionEvent e) {
//		    	int selectedIndex = listExpressions.getSelectedIndex();
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				XYItemRenderer itemRenderer = plot.getRenderer();
//		        Color color;
//		        color = JColorChooser.showDialog(
////		            Graph2.this, BundleMessages.getMessage("plotColor"), (Color) itemRenderer.getSeriesPaint(selectedIndex)
//		            Graph2.this, BundleMessages.getMessage("plotColor"), functions.get(selectedIndex).getColor()
//		        );
//		        if (color == null)
//		        	return;
//	        	listExpressions.clearSelection();
//				itemRenderer.setSeriesPaint(selectedIndex, color);
//		    }
//		});
//		
//		menuItemStroke.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				XYItemRenderer itemRenderer = plot.getRenderer();
//		        Object[] options = {"0.25", "0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0"};
//				Object stroke = JOptionPane.showInputDialog(
//						Graph2.this, 
//						null, 
//						BundleMessages.getMessage("stroke"), 
//						JOptionPane.PLAIN_MESSAGE, 
//						null, 
//						options, 
//						String.valueOf(((BasicStroke) itemRenderer.getSeriesStroke(selectedIndex)).getLineWidth()));
//				if (stroke == null)
//					return;
//				itemRenderer.setSeriesStroke(selectedIndex, new BasicStroke(Float.parseFloat((String) stroke)));
//			}
//		});
//		
//		checkBoxMenuItemTranslationX.addActionListener(new ActionListener() {
//		    public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				double translationX = 0;
//		    	if (checkBoxMenuItemTranslationX.isSelected()) {
//					Object translationXObject = JOptionPane.showInputDialog(
//							Graph2.this, 
//							null, 
//							BundleMessages.getMessage("translation")+" x", 
//							JOptionPane.PLAIN_MESSAGE); 
//					if (translationXObject == null)
//						return;
//					try {
//						translationX = Double.parseDouble((String) translationXObject);
//					} catch (Exception e1) {
//						return;
//					}
//		    	}
//				function.setTranslationX(translationX);
////				removeFunction(selectedIndex);
////				addFunction(function);
//				refreshFunctions();
//		    }
//		});
//		
//		checkBoxMenuItemTranslationY.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				double translationY = 0;
//		    	if (checkBoxMenuItemTranslationY.isSelected()) {
//					Object translationYObject = JOptionPane.showInputDialog(
//							Graph2.this, 
//							null, 
//							BundleMessages.getMessage("translation")+" y", 
//							JOptionPane.PLAIN_MESSAGE); 
//					if (translationYObject == null)
//						return;
//					try {
//						translationY = Double.parseDouble((String) translationYObject);
//					} catch (Exception e1) {
//						return;
//					}
//		    	}
//				function.setTranslationY(translationY);
////				removeFunction(selectedIndex);
////				addFunction(function);
//				refreshFunctions();
//			}
//		});
//		
//		checkBoxMenuItemRotation.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				double rotation = 0;
//		    	if (checkBoxMenuItemRotation.isSelected()) {
//					Object rotationObject = JOptionPane.showInputDialog(
//							Graph2.this, 
//							BundleMessages.getMessage("degrees"), 
//							BundleMessages.getMessage("rotationAngle"), 
//							JOptionPane.PLAIN_MESSAGE); 
//					if (rotationObject == null)
//						return;
//					try {
//						rotation = Double.parseDouble((String) rotationObject);
//					} catch (Exception e1) {
//						return;
//					}
//		    	}
//				function.setRotation(rotation);
////				removeFunction(selectedIndex);
////				addFunction(function);
//				refreshFunctions();
//			}
//		});
//		
//		checkBoxMenuItemScale.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int selectedIndex = listExpressions.getSelectedIndex();
//				Function function = functions.get(selectedIndex);
//				double scale = 1;
//		    	if (checkBoxMenuItemScale.isSelected()) {
//					Object scaleObject = JOptionPane.showInputDialog(
//							Graph2.this, 
//							null, 
//							BundleMessages.getMessage("scaleFactor"), 
//							JOptionPane.PLAIN_MESSAGE); 
//					if (scaleObject == null)
//						return;
//					try {
//						scale = Double.parseDouble((String) scaleObject);
//					} catch (Exception e1) {
//						return;
//					}
//		    	}
//		    	if (scale == 0)
//		    		return;
//				function.setScale(scale);
////				removeFunction(selectedIndex);
////				addFunction(function);
//				refreshFunctions();
//			}
//		});
//		
//		menuItemRemove.addActionListener(new ActionListener() {
//		    public void actionPerformed(ActionEvent e) {
//		    	int selectedIndex = listExpressions.getSelectedIndex();
//		    	removeFunction(selectedIndex);
//		    }
//		});
//		
//		buttonUp.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//				double tickUnit = axisY.getTickUnit().getSize();
//				axisY.setRange(axisY.getLowerBound() + tickUnit, axisY.getUpperBound() + tickUnit);
//			}
//		});
//		buttonDown.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//				double tickUnit = axisY.getTickUnit().getSize();
//				axisY.setRange(axisY.getLowerBound() - tickUnit, axisY.getUpperBound() - tickUnit);
//			}
//		});
//		buttonLeft.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//				double tickUnit = axisX.getTickUnit().getSize();
//				axisX.setRange(axisX.getLowerBound() - tickUnit, axisX.getUpperBound() - tickUnit);
//			}
//		});
//		buttonRight.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//				double tickUnit = axisX.getTickUnit().getSize();
//				axisX.setRange(axisX.getLowerBound() + tickUnit, axisX.getUpperBound() + tickUnit);
//			}
//		});
//		
//		buttonReset.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//		    	JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//		    	double lowerBoundX = axisX.getLowerBound();
//		    	double upperBoundX = axisX.getUpperBound();
//				NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//		    	double lowerBoundY = axisY.getLowerBound();
//		    	double upperBoundY = axisY.getUpperBound();
//		    	axisX.setLowerBound(-(upperBoundX - lowerBoundX) / 2);
//		    	axisX.setUpperBound((upperBoundX - lowerBoundX) / 2);
//		    	axisY.setLowerBound(-(upperBoundY - lowerBoundY) / 2);
//		    	axisY.setUpperBound((upperBoundY - lowerBoundY) / 2);
//			}
//		});
//		buttonNormalize.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//		    	JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//				NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//				double axisVariation = (axisX.getUpperBound()-axisX.getLowerBound()) - (axisY.getUpperBound()-axisY.getLowerBound());
//				axisY.setLowerBound(axisY.getLowerBound() - axisVariation / 2);
//				axisY.setUpperBound(axisY.getUpperBound() + axisVariation / 2);
//			}
//		});
//		
//		buttonZoomIn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Dimension chartPanelSize = chartPanel.getSize();
//				chartPanel.zoomInBoth(chartPanelSize.width/2, chartPanelSize.height/2);
//			}
//		});
//		buttonZoomOut.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Dimension chartPanelSize = chartPanel.getSize();
//				chartPanel.zoomOutBoth(chartPanelSize.width/2, chartPanelSize.height/2);
//			}
//		});
//		
//		
//		spinnerDomainStart.getModel().addChangeListener(getChangeListenerDomainStart());
//		spinnerDomainEnd.getModel().addChangeListener(getChangeListenerDomainEnd());
//		
//		JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = chart.getXYPlot();
//		final NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//		axisX.addChangeListener(new AxisChangeListener() {
//			public void axisChanged(final AxisChangeEvent event) {
//				double lowerBound = axisX.getLowerBound();
//				double upperBound = axisX.getUpperBound();
//				if (lowerBound == minX && upperBound == maxX)
//					return;
//				minX = lowerBound;
//				maxX = upperBound;
//				refreshFunctions();
//				SwingUtilities.invokeLater(new Runnable() {
//				    public void run() {
//						double step = axisX.getTickUnit().getSize();
//						
//						SpinnerNumberModel spinnerNumberModelStart = (SpinnerNumberModel) spinnerDomainStart.getModel();
//						spinnerNumberModelStart.removeChangeListener(getChangeListenerDomainStart());
//						spinnerNumberModelStart.setStepSize(new Double(step));
//						spinnerNumberModelStart.setMaximum(new Double(maxX));
//						spinnerNumberModelStart.setValue(new Double(minX));
//						spinnerNumberModelStart.addChangeListener(getChangeListenerDomainStart());
//
//						SpinnerNumberModel spinnerNumberModelEnd = (SpinnerNumberModel) spinnerDomainEnd.getModel();
//				    	spinnerNumberModelEnd.removeChangeListener(getChangeListenerDomainEnd());
//						spinnerNumberModelEnd.setStepSize(new Double(step));
//						spinnerNumberModelEnd.setMinimum(new Double(minX));
//						spinnerNumberModelEnd.setValue(new Double(maxX));
//						spinnerNumberModelEnd.addChangeListener(getChangeListenerDomainEnd());
//				    }
//				});
//			}
//		});
//		
//		chartPanel.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//		    	if (!SwingUtilities.isLeftMouseButton(e))
//		    		return;
//		        Insets insets = chartPanel.getInsets();
//		        int x = (int) ((e.getX() - insets.left) / chartPanel.getScaleX());
//		        int y = (int) ((e.getY() - insets.top) / chartPanel.getScaleY());
//		    	// Find selected expression!
//		        ChartEntity entity = null;
//		        if (chartPanel.getChartRenderingInfo() != null) {
//		            EntityCollection entities = chartPanel.getChartRenderingInfo().getEntityCollection();
//		            if (entities != null) {
//		                entity = entities.getEntity(x, y);
//		                if (entity == null) {
//	                		listExpressions.clearSelection();
//		                	return;
//		                }
//		                
//		                int seriesIndex = ((XYItemEntity) entity).getSeriesIndex();
//		                listExpressions.setSelectedIndex(seriesIndex);		                
//		            }
//		        }
//			}
//		});
		
		comboBoxX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotTable();
			}
		});
		comboBoxY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotTable();
			}
		});
	}
	
//	/**
//	 * Get <code>ChangeListener</code> for domain start spinner.
//	 * @return Domain start spinner change listener.
//	 */
//	private ChangeListener getChangeListenerDomainStart() {
//		if (changeListenerDomainStart == null) {
//			changeListenerDomainStart = new ChangeListener() {
//				public void stateChanged(ChangeEvent e) {
//					SpinnerNumberModel spinnerNumberModelStart = (SpinnerNumberModel) spinnerDomainStart.getModel();
//			    	double domainStart = ((Double) spinnerNumberModelStart.getValue()).doubleValue();
//			    	JFreeChart chart = chartPanel.getChart();
//					XYPlot plot = chart.getXYPlot();
//					NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//			    	axisX.setLowerBound(domainStart);
//					NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//			    	axisY.setLowerBound(axisY.getUpperBound()-(axisX.getUpperBound()-axisX.getLowerBound()));
//				}
//			};
//		}
//		return changeListenerDomainStart;
//	}
	
//	/**
//	 * Get <code>ChangeListener</code> for domain end spinner.
//	 * @return Domain end spinner change listener.
//	 */
//	private ChangeListener getChangeListenerDomainEnd() {
//		if (changeListenerDomainEnd == null) {
//			changeListenerDomainEnd = new ChangeListener() {
//				public void stateChanged(ChangeEvent e) {
//					SpinnerNumberModel spinnerNumberModelEnd = (SpinnerNumberModel) spinnerDomainEnd.getModel();
//			    	double domainEnd = ((Double) spinnerNumberModelEnd.getValue()).doubleValue();
//					JFreeChart chart = chartPanel.getChart();
//					XYPlot plot = chart.getXYPlot();
//					NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//			    	axisX.setUpperBound(domainEnd);
//					NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//			    	axisY.setUpperBound(axisY.getLowerBound()+(axisX.getUpperBound()-axisX.getLowerBound()));
//				}
//			};
//		}
//		return changeListenerDomainEnd;
//	}
	
	/**
	 * Returns the component's E-Slate handle.
	 * @return The requested handle. If the component's constructor has not been
	 *         called, this method returns null.
	 */
	public ESlateHandle getESlateHandle() {
		if (handle == null)
			initESlate();
		return handle;
	}

	/**
	 * Initializes the E-Slate functionality of the component.
	 */
	private void initESlate() {
		handle = ESlate.registerPart(this);
		handle.setInfo(getInfo());
		try {
			handle.setUniqueComponentName(BundleMessages.getMessage("nameBarChart"));
		} catch (RenamingForbiddenException e) {
			e.printStackTrace();
		}

		try {
//			// Plug for expressions
//			SharedObjectListener expressionListener = new SharedObjectListener() {
//				public void handleSharedObjectEvent(SharedObjectEvent e) {
//					StringSO sharedObject = (StringSO) e.getSharedObject();
//					String expression = sharedObject.getString().trim();
////					if (!isExpressionValid(expression))
////						return;
//////					DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//////					if (defaultListModel.indexOf(expression) != -1)
//////						return;
////					for (int i = 0; i < functions.size(); i++) {
////						if (functions.get(i).getExpression().equals(expression))
////							return;
////					}
//////					removeAllFunctions();
////					JFreeChart chart = chartPanel.getChart();
////					XYPlot plot = chart.getXYPlot();
////					NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
////					minX = axisX.getLowerBound();
////					maxX = axisX.getUpperBound();
////					Function function = new Function(expression, minX, maxX);
////					addFunction(function);
////					DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
////					int addedIndex = defaultListModel.getSize()-1;
////					checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//					addPlugFunction(sharedObject.getHandle(), expression);
//				}
//			};
//			plugExpression = new MultipleInputPlug(handle, BundleMessages.getResourceBundle(),
//					"expressionPlug", new Color(139, 117, 0),
//					StringSO.class, expressionListener);
//			plugExpression.setNameLocaleIndependent("expressionPlug");
//			handle.addPlug(plugExpression);
//			plugExpression.addConnectionListener(new ConnectionListener() {
//				public void handleConnectionEvent(ConnectionEvent e) {
//					if (e.getType() == Plug.INPUT_CONNECTION) {
//						StringSO sharedObject = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
//						String expression = sharedObject.getString().trim();
////						if (!isExpressionValid(expression))
////							return;
//////						DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//////						if (defaultListModel.indexOf(expression) != -1)
//////							return;
////						for (int i = 0; i < functions.size(); i++) {
////							if (functions.get(i).getExpression().equals(expression))
////								return;
////						}
//////						removeAllFunctions();
////						JFreeChart chart = chartPanel.getChart();
////						XYPlot plot = chart.getXYPlot();
////						NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
////						minX = axisX.getLowerBound();
////						maxX = axisX.getUpperBound();
////						Function function = new Function(expression, minX, maxX);
////						addFunction(function);
////						DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
////						int addedIndex = defaultListModel.getSize()-1;
////						checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//						addPlugFunction(sharedObject.getHandle(), expression);
//					}
//				}
//			});
//			plugExpression.addDisconnectionListener(new DisconnectionListener() {
//				public void handleDisconnectionEvent(DisconnectionEvent e) {
//					StringSO sharedObject = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
//					plugFunctions.remove(sharedObject.getHandle());
//				}
//			});
			
			// Plug for connecting to a database table
			plug = new FemaleSingleIFSingleConnectionProtocolPlug(
					handle, BundleMessages.getResourceBundle(), "tablePlug", new Color(102, 88, 187),
					Table.class);
			plug.setNameLocaleIndependent("tablePlug");
			handle.addPlug(plug);
			plug.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
                    switchToTable(
                      (Table) e.getPlug().getHandle().getComponent()
                    );
				}
			});
			plug.addDisconnectionListener(new DisconnectionListener() {
				public void handleDisconnectionEvent(DisconnectionEvent e) {
                    switchToTable(null);
				}
			});
		} catch (InvalidPlugParametersException e) {
			e.printStackTrace();
		} catch (PlugExistsException e) {
			e.printStackTrace();
		}
		
//	    handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.Graph2Primitives");
	}

	/**
	 * Returns Copyright information.
	 * 
	 * @return The Copyright information.
	 */
	private ESlateInfo getInfo() {
		String[] info = { BundleMessages.getMessage("credits1"),
				BundleMessages.getMessage("credits2"),
				BundleMessages.getMessage("credits3") };
		return new ESlateInfo(BundleMessages.getMessage("componentNameBarChart") + ", "
				+ BundleMessages.getMessage("version") + " " + VERSION, info);
	}

	/**
	 * Reload plugged database table fields.
	 */
	private void reloadFields() {
		String selectedFieldX = (String) comboBoxX.getSelectedItem();
		comboBoxX.removeAllItems();
		comboBoxX.addItem("");
		String selectedFieldY = (String) comboBoxY.getSelectedItem();
		comboBoxY.removeAllItems();
		comboBoxY.addItem("");
		TableFieldBaseArray tableFieldBaseArray = table.getFields();
		for (int i = 0; i < tableFieldBaseArray.size(); i++) {
			AbstractTableField tableField = tableFieldBaseArray.get(i);
			if (tableField instanceof StringTableField)
				comboBoxX.addItem(tableField.getName());
			else if (tableField instanceof DoubleTableField || tableField instanceof FloatTableField || tableField instanceof IntegerTableField)
				comboBoxY.addItem(tableField.getName());
		}
		comboBoxX.setSelectedItem(selectedFieldX);
		comboBoxY.setSelectedItem(selectedFieldY);
	}
	
//	/**
//	 * Draw a new function.
//	 * @param function Function to draw.
//	 */
//	private void drawFunction(Function function) {
//		JFreeChart chart = chartPanel.getChart();
//		final XYPlot plot = chart.getXYPlot();
//		ValueAxis axisX = plot.getDomainAxis();
//		double lowerBound = axisX.getLowerBound();
//		double upperBound = axisX.getUpperBound();
//
//		// Find possible transformations
//		double translationX = function.getTranslationX();
//		double translationY = function.getTranslationY();
//		double rotation = function.getRotation();
//		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
//		for (int i = 0; i < functions.size(); i++) {
//			if (function.getExpression().equals(functions.get(i).getExpression())) {
//				renderer.setSeriesLinesVisible(i, rotation==0);
//				renderer.setSeriesShapesVisible(i, rotation!=0);
//				renderer.setSeriesShapesFilled(i, rotation==0);
//				renderer.setSeriesShape(i, new Rectangle(1, 1));
//				break;
//			}
//		}
//		double scale = function.getScale();
//
//		// Calculate step
//		double step = (upperBound - lowerBound) / chartPanel.getSize().width;// * 2;
////System.out.println(step);		
//		if (step < 0.1)
//			step = 0.1;
//		
//		// Create a XY chart
//		XYSeries series = new XYSeries(function.getExpression());
//		double from = function.getDomainFrom()>=lowerBound ? function.getDomainFrom() : lowerBound;
//		double to = function.getDomainTo()<=upperBound ? function.getDomainTo() : upperBound;
//		for (double x = from; x <= to; x+=step) {
//			jep.addVariable("x", x);
//			jep.parseExpression(function.getExpression());
//			double y = jep.getValue();
//			// Perform transformations
//			double rotationRadians = -rotation * Math.PI / 180;
//			// Rotation
//			double newX = x * Math.cos(rotationRadians) - y * Math.sin(rotationRadians);
//			double newY = x * Math.sin(rotationRadians) + y * Math.cos(rotationRadians);
//			// Scale
//			newX = newX * scale;
//			newY = newY * scale;
//			// Translation
//			newX = newX + translationX;
//			newY = newY + translationY;
//			series.add(newX, newY);
//		}
////System.out.println(series.getItemCount());		
//		
//		// Add the series to the data set
//		dataset.addSeries(series);
//	}
	
	/**
	 * Draw points collection.
	 */
//	private void drawPoints(Function function) {
	private void drawPoints() {
//		JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = chart.getXYPlot();
//		
//		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
//		for (int i = 0; i < functions.size(); i++) {
//			if (function.getExpression().equals(functions.get(i).getExpression())) {
//				renderer.setSeriesLinesVisible(i, false);
//				renderer.setSeriesShapesVisible(i, true);
//				renderer.setSeriesShapesFilled(i, false);
//				renderer.setSeriesShape(i, new Rectangle(1, 1));
//				break;
//			}
//		}
		
//		XYSeries series = new XYSeries(BundleMessages.getMessage("points"));
//		for (Iterator iter = function.getPoints().iterator(); iter.hasNext();) {
//			Point2D.Double point = (Point2D.Double) iter.next();
//			series.add(point.x, point.y);
//		}
		for (int i = 0; i < categories.size(); i++)
			dataset.setValue(values.get(i), (String) comboBoxX.getSelectedItem(), categories.get(i));
		
//		dataset.addSeries(series);
	}
	
//	/**
//	 * Check if a function expression is valid.
//	 * @param expression Expression to check.
//	 * @return Validity of expression.
//	 */
//	private boolean isExpressionValid(String expression) {
//		if (expression==null || expression.length()==0)
//			return false;
//		jep.addVariable("x", 1);
//		jep.parseExpression(expression);
//		if (jep.hasError())
//			return false;
//		return true;
//	}
	
	/**
	 * Add and draw a new function.
	 * @param function Function to add/draw.
	 * @param step Draw step.
	 */
//	private void addFunction(Function function) {
//		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//		defaultListModel.addElement(function.getExpression());
//		functions.add(function);
//		draw(function);
//	}
	
//	/**
//	 * Remove a drawed function.
//	 * @param expressionIndex Expression index.
//	 */
//	void removeFunction(int expressionIndex) {
//		// Remove also the function from the plug funtions, if exists
//		Set keySet = plugFunctions.keySet();
//		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
//			ESlateHandle handle = (ESlateHandle) iterator.next();
//			if (plugFunctions.get(handle).getExpression().equals(functions.get(expressionIndex).getExpression())) {
//				plugFunctions.remove(handle);
//				break;
//			}
//		}
//		
//		JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = chart.getXYPlot();
//		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
//		
//		ArrayList<Color> colors = new ArrayList<Color>();
//		for (int i = expressionIndex+1; i < functions.size(); i++)
//			colors.add((Color) renderer.getSeriesPaint(i));
//		
//		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//    	defaultListModel.removeElementAt(expressionIndex);
//		dataset.removeSeries(expressionIndex);
//		PaintList paintList = renderer.getPaintList();
//		for (int i = expressionIndex+1; i < functions.size(); i++)
//			paintList.setPaint(i, null);
//		functions.remove(expressionIndex);
//		
//		int colorIndex = 0;
//		for (int i = expressionIndex; i < functions.size(); i++)
//			paintList.setPaint(i, colors.get(colorIndex++));
////			renderer.setSeriesPaint(i, colors.get(colorIndex++));
//		
//		refreshFunctions();
//		
//		if (comboBoxX.isEnabled())
//			comboBoxX.setSelectedIndex(0);
//		if (comboBoxY.isEnabled())
//			comboBoxY.setSelectedIndex(0);
//	}
	
	/**
	 * Remove all drawed functions.
	 */
//	private void removeAllFunctions() {
//		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//		defaultListModel.removeAllElements();
//		dataset.removeAllSeries();
//		functions.clear();
//	}
	
	/**
	 * Refresh drawings (called after a change has occured).
	 */
	private void refreshFunctions() {
//		dataset.removeAllSeries();
		dataset.clear();
		
//		int nFunctions = functions.size();
//		for (int i = 0; i < nFunctions; i++) {
//			Function function = functions.get(i);
//			 if (function.getExpression().equals(BundleMessages.getMessage("points")))
				drawPoints();
//			 else
//				 drawFunction(function);
//		}
	}
	
//	/**
//	 * Set an expression's visibility at chart.
//	 * @param index Index of expression at list and dataset.
//	 * @param visible Visible or not.
//	 */
//	void setFunctionVisible(int index, boolean visible) {
//		JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = (XYPlot) chart.getPlot();
//		XYItemRenderer itemRenderer = plot.getRenderer();
//		itemRenderer.setSeriesVisible(index, visible);
//	}
	
//	/**
//	 * Show popup menu for selected function.
//	 * @param e Mouse event
//	 */
//	void showPopupMenu(MouseEvent e) {
//		int selectedIndex = listExpressions.getSelectedIndex();
//		if (selectedIndex ==-1)
//			return;
////		SwingUtilities.invokeLater(new Runnable() {
////			public void run() {
//				popupMenu.removeAll();
//				Function function = functions.get(selectedIndex);
//				if (!function.getExpression().equals(BundleMessages.getMessage("points"))) {
//					popupMenu.add(menuItemEdit);
//					popupMenu.add(menuDomain);
//				}
//		        popupMenu.add(menuItemColor);
//				if (!function.getExpression().equals(BundleMessages.getMessage("points"))) {
//			        popupMenu.add(menuItemStroke);
//			        popupMenu.add(menuTranslation);
//			        popupMenu.add(checkBoxMenuItemRotation);
//			        popupMenu.add(checkBoxMenuItemScale);
//				}
//		        popupMenu.add(menuItemRemove);
//			    popupMenu.pack();
//
//				if (!function.getExpression().equals(BundleMessages.getMessage("points"))) {
//					checkBoxMenuItemTranslationX.setSelected(function.getTranslationX()!=0);
//					checkBoxMenuItemTranslationX.setText("x"+(function.getTranslationX()==0?"":" ("+function.getTranslationX()+")"));
//					checkBoxMenuItemTranslationY.setSelected(function.getTranslationY()!=0);
//					checkBoxMenuItemTranslationY.setText("y"+(function.getTranslationY()==0?"":" ("+function.getTranslationY()+")"));
//					checkBoxMenuItemRotation.setSelected(function.getRotation()!=0);
//					checkBoxMenuItemRotation.setText(BundleMessages.getMessage("rotation")+(function.getRotation()==0?"":" ("+function.getRotation()+")"));
//					checkBoxMenuItemScale.setSelected(function.getScale()!=1);
//					checkBoxMenuItemScale.setText(BundleMessages.getMessage("scale")+(function.getScale()==1?"":" ("+function.getScale()+")"));
//				}
//				
//	    		popupMenu.show(e.getComponent(), e.getX(), e.getY());
////			}
////		});
//	}
	
//	/**
//	 * Get drawed expression.
//	 * @return Drawed expression.
//	 */
//	public String getExpression() {
//		return expression;
//	}

//	/**
//	 * Set expression to draw.
//	 * @param expression Function's expression.
//	 */
//	public void setExpression(String expression) {
//		this.expression = expression;
////		if (!isExpressionValid(expression))
////			return;
//////		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//////		if (defaultListModel.indexOf(expression) != -1)
//////			return;
////		for (int i = 0; i < functions.size(); i++) {
////			if (functions.get(i).getExpression().equals(expression))
////				return;
////		}
//////		removeAllFunctions();
////		JFreeChart chart = chartPanel.getChart();
////		XYPlot plot = chart.getXYPlot();
////		NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
////		minX = axisX.getLowerBound();
////		maxX = axisX.getUpperBound();
////		Function function = new Function(expression, minX, maxX);
////		addFunction(function);
////		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
////		int addedIndex = defaultListModel.getSize()-1;
////		checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//		addFunction(expression);
//	}

//	/**
//	 * Get expression insertion visibility.
//	 * @return Expression insertion visibility.
//	 */
//	public boolean isExpressionVisible() {
//		return expressionVisible;
//	}

//	/**
//	 * Set expression insertion visibility.
//	 * @param expressionVisible Expression insertion visibility.
//	 */
//	public void setExpressionVisible(boolean expressionVisible) {
//		if (expressionVisible == this.expressionVisible)
//			return;
//		this.expressionVisible = expressionVisible;
//		removeAll();
//		layoutComponents();
//		revalidate();
//	}

//	/**
//	 * Add a new function (through connected plug).
//	 * @param handle Component that add/change a function.
//	 * @param expression Function's expression.
//	 */
//	private void addPlugFunction(ESlateHandle handle, String expression) {
//		// Search if function is among the plug ones..
//		// Function wasn't found, so we add it
////System.out.println(plugFunctions.size()+" "+expression);
//		if (!plugFunctions.containsKey(handle)) {
//			if (!addFunction(expression))
//				return;
//			for (int i = 0; i < functions.size(); i++) {
//				if (functions.get(i).getExpression().equals(expression))
//					plugFunctions.put(handle, functions.get(i));
//			}
//			return;
//		}
//		
//		// Ok we found it, so we have to simply update its expression
//		if (!isExpressionValid(expression))
//			return;
//		for (int i = 0; i < functions.size(); i++) {
//			if (functions.get(i).getExpression().equals(expression))
//				return;
//		}
//		Function function = plugFunctions.get(handle);
//		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//		defaultListModel.set(defaultListModel.indexOf(function.getExpression()), expression);
//		function.setExpression(expression);
//		refreshFunctions();
//	}
	
//	/**
//	 * Add a new function.
//	 * @param expression Function's expression.
//	 * @return True for adding the function.
//	 */
//	public boolean addFunction(String expression) {
//		// Check expression's validity
//		if (!isExpressionValid(expression))
//			return false;
//		for (int i = 0; i < functions.size(); i++) {
//			if (functions.get(i).getExpression().equals(expression))
//				return false;
//		}
//		textFieldExpression.setText(null);
//		
//		// Construct new function
//		JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = chart.getXYPlot();
//		NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//		minX = axisX.getLowerBound();
//		maxX = axisX.getUpperBound();
//		Function function = new Function(expression, minX, maxX);
//		
//		// Add & draw the function
//		DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//		defaultListModel.addElement(function.getExpression());
//		functions.add(function);
//		drawFunction(function);
//
//		// Select new drawn function
//		int addedIndex = defaultListModel.getSize()-1;
//		checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//		return true;
//	}
	
//	/**
//	 * Remove a function.
//	 * @param expression Function's expression.
//	 */
//	public void removeFunction(String expression) {
//		for (int i = 0; i < functions.size(); i++) {
//			if (functions.get(i).getExpression().equals(expression)) {
//				removeFunction(i);
//				break;
//			}
//		}
//	}
	
//	/**
//	 * Set function's domain.
//	 * @param expression Function's expression.
//	 * @param domainFrom Domain's start.
//	 * @param domainTo Domain's end.
//	 */
//	public void setFunctionDomain(String expression, double domainFrom, double domainTo) {
//		for (int i = 0; i < functions.size(); i++) {
//			Function function = functions.get(i);
//			if (function.getExpression().equals(expression)) {
//				if (function.getDomainFrom() == domainFrom && function.getDomainTo() == domainTo)
//					return;
//				if (domainFrom >= domainTo)
//					return;
//				function.setDomainFrom(domainFrom);
//				function.setDomainTo(domainTo);
//				refreshFunctions();
//				break;
//			}
//		}
//	}
	
//	/**
//	 * Set function color.
//	 * @param expression Function's expression.
//	 * @param color Function's color.
//	 */
//	public void setFunctionColor(String expression, Color color) {
//		JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = chart.getXYPlot();
//		XYItemRenderer itemRenderer = plot.getRenderer();
//		for (int i = 0; i < functions.size(); i++) {
//			Function function = functions.get(i);
//			if (function.getExpression().equals(expression)) {
//				itemRenderer.setSeriesPaint(i, color);
//				break;
//			}
//		}
//	}
	
	/**
	 * Zoom.
	 * @param zoomIn Zoom in or out.
	 */
	public void zoom(boolean zoomIn)
    {
		Dimension chartPanelSize = chartPanel.getSize();
		if (zoomIn) {
			chartPanel.zoomInRange(chartPanelSize.width/2, chartPanelSize.height/2);
		}else{
			chartPanel.zoomOutRange(chartPanelSize.width/2, chartPanelSize.height/2);
        }
	}
	
//	/**
//	 * Set domain view window.
//	 * @param from Domain view start.
//	 * @param to Domain view end.
//	 */
//	public void setView(double from, double to) {
//		if (from >=  to)
//			return;
//    	JFreeChart chart = chartPanel.getChart();
//		XYPlot plot = chart.getXYPlot();
//		NumberAxis axisX = (NumberAxis) plot.getDomainAxis();
//    	axisX.setLowerBound(from);
//    	axisX.setUpperBound(to);
//		NumberAxis axisY = (NumberAxis) plot.getRangeAxis();
//    	axisY.setLowerBound(axisY.getUpperBound()-(axisX.getUpperBound()-axisX.getLowerBound()));
//    	axisY.setUpperBound(axisY.getLowerBound()+(axisX.getUpperBound()-axisX.getLowerBound()));
//	}
	
	/**
	 * Plot a series of points based on two fields of a plugged database table. 
	 */
	private void plotTable() {
//		Function function = null;
//		for (int i = 0; i < functions.size(); i++) {
//			if (functions.get(i).getExpression().equals(BundleMessages.getMessage("points"))) {
//				function = functions.get(i);
//				function.removePoints();
//				refreshFunctions();
		dataset.clear();
//				break;
//			}
//		}
		
		String fieldX = (String) comboBoxX.getSelectedItem();
		String fieldY = (String) comboBoxY.getSelectedItem();
		if (fieldX == null || fieldY == null)
			return;
		if (fieldX.equals("") || fieldY.equals(""))
			return;
		
//		if (function == null) {
//			function = new Function(BundleMessages.getMessage("points"));
//			DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//			defaultListModel.addElement(function.getExpression());
//			functions.add(function);
//			int addedIndex = defaultListModel.getSize()-1;
//			checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//		}
		
		categories.clear();
		values.clear();
		int nRows = table.getRowCount();
		for (int j = 0; j < nRows; j++) {
			try {
				String category = (String) table.getCell(fieldX, j);
				Double valueNumber = (Double) table.getCell(fieldY, j);
				if (category == null || valueNumber == null)
					continue;
				double value = valueNumber.doubleValue();
				categories.add(category);
				values.add(value);
		
//				boolean pointFound = false;
//				ArrayList<Point2D.Double> points = function.getPoints();
//				for (Iterator iter = points.iterator(); iter.hasNext();) {
//					Point2D.Double point = (Point2D.Double) iter.next();
//					if (point.x == x && point.y == y) {
//						pointFound = true;
//						break;
//					}
//				}
//				if (!pointFound) {
//					function.getPoints().add(new DoublePoint(x, y));
//					refreshFunctions();
//				}
			} catch (InvalidCellAddressException e) {
				e.printStackTrace();
			}
		}
		refreshFunctions();
	}
	
//	/**
//	 * Plot a point.
//	 * @param x
//	 * @param y
//	 */
//	public void plot(double x, double y) {
//		Function function = null;
//		for (int i = 0; i < functions.size(); i++) {
//			if (functions.get(i).getExpression().equals(BundleMessages.getMessage("points"))) {
//				function = functions.get(i);
//				break;
//			}
//		}
//		boolean pointFound = false;
//		if (function == null) {
//			function = new Function(BundleMessages.getMessage("points"));
//			DefaultListModel defaultListModel = (DefaultListModel) listExpressions.getModel();
//			defaultListModel.addElement(function.getExpression());
//			functions.add(function);
//			int addedIndex = defaultListModel.getSize()-1;
//			checkListManager.getSelectionModel().addSelectionInterval(addedIndex, addedIndex);
//		}
//		else {
//			ArrayList<Point2D.Double> points = function.getPoints();
//			for (Iterator iter = points.iterator(); iter.hasNext();) {
//				Point2D.Double point = (Point2D.Double) iter.next();
//				if (point.x == x && point.y == y) {
//					pointFound = true;
//					break;
//				}
//			}
//		}
//		if (!pointFound) {
//			function.getPoints().add(new DoublePoint(x, y));
//			refreshFunctions();
//		}
//	}
	
//	/**
//	 * Set plot color.
//	 * @param color
//	 */
//	public void plotColor(Color color) {
//		for (int i = 0; i < functions.size(); i++) {
//			if (functions.get(i).getExpression().equals(BundleMessages.getMessage("points"))) {
//				JFreeChart chart = chartPanel.getChart();
//				XYPlot plot = chart.getXYPlot();
//				XYItemRenderer itemRenderer = plot.getRenderer();
//				itemRenderer.setSeriesPaint(0, color);
//				break;
//			}
//		}
//	}
	
//	/**
//	 * Clear all plotted points.
//	 */
//	public void plotClear() {
//		for (int i = 0; i < functions.size(); i++) {
//			Function function = functions.get(i);
//			if (function.getExpression().equals(BundleMessages.getMessage("points"))) {
////				function.getPoints().clear();
////				refreshFunctions();
//				removeFunction(i);
//				break;
//			}
//		}
//	}
	
	/**
	 * @see gr.cti.eslate.scripting.LogoScriptable#getSupportedPrimitiveGroups()
	 */
	public String[] getSupportedPrimitiveGroups() {
		  return new String[] {"gr.cti.eslate.scripting.logo.BarChartPrimitives"};
	}
	
	/**
	 * The object implements the writeExternal method to save its contents by
	 * calling the methods of DataOutput for its primitive values or calling the
	 * writeObject method of ObjectOutput for objects, strings, and arrays.
	 * 
	 * @serialData Overriding methods should use this tag to describe the data
	 *             layout of this Externalizable object. List the sequence of
	 *             element types and, if possible, relate the element to a
	 *             public/protected field and/or method of this Externalizable
	 *             class.
	 * 
	 * @param out
	 *            the stream to write the object to
	 * @exception IOException
	 *                Includes any I/O exceptions that may occur
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);

//		fieldMap.put(KEY_FUNCTIONS, functions);
		fieldMap.put(KEY_FIELD_X, (String) comboBoxX.getSelectedItem());
		fieldMap.put(KEY_FIELD_Y, (String) comboBoxY.getSelectedItem());
		
		out.writeObject(fieldMap);
		out.flush();
	}

	/**
	 * The object implements the readExternal method to restore its contents by
	 * calling the methods of DataInput for primitive types and readObject for
	 * objects, strings and arrays. The readExternal method must read the values
	 * in the same sequence and with the same types as were written by
	 * writeExternal.
	 * 
	 * @param in
	 *            the stream to read data from in order to restore the object
	 * @exception IOException
	 *                if I/O errors occur
	 * @exception ClassNotFoundException
	 *                If the class for an object being restored cannot be found.
	 */
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		initialize(in);
	}



    /**
     * Selects the table field to plot in the x axis
     * @param       field   The name of the x axis field.
     * @return      True, if the field was successfully selected, false
     *              otherwise.
     */
    public boolean setXAxisField(String field)
    {
      int i = Utils.getItemIndex(comboBoxX, field);
      if (i >= 0) {
        comboBoxX.setSelectedIndex(i);
        return true;
      }else{
        return false;
      }
    }

    /**
     * Selects the table field to plot in the y axis
     * @param       field   The name of the y axis field.
     * @return      True, if the field was successfully selected, false
     *              otherwise.
     */
    public boolean setYAxisField(String field)
    {
      int i = Utils.getItemIndex(comboBoxY, field);
      if (i >= 0) {
        comboBoxY.setSelectedIndex(i);
        return true;
      }else{
        return false;
      }
    }

    /**
     * Returns the name of the table field that is plotted in the x axis.
     * @return    The name of the table field that is plotted in the x axis.
     */
    public String getXAxisField()
    {
      return (String)comboBoxX.getSelectedItem();
    }

    /**
     * Returns the name of the table field that is plotted in the y axis.
     * @return    The name of the table field that is plotted in the y axis.
     */
    public String getYAxisField()
    {
      return (String)comboBoxY.getSelectedItem();
    }

    /**
     * Returns the minimum value for the y axis.
     * @return    The minimum value for the y axis.
     */
    public double getYAxisMin()
    {
      JFreeChart chart = chartPanel.getChart();
      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      NumberAxis3D axisY = (NumberAxis3D) plot.getRangeAxis();
      return axisY.getLowerBound();
    }

    /**
     * Returns the maximum value for the y axis.
     * @return    The maximum value for the y axis.
     */
    public double getYAxisMax()
    {
      JFreeChart chart = chartPanel.getChart();
      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      NumberAxis3D axisY = (NumberAxis3D) plot.getRangeAxis();
      return axisY.getUpperBound();
    }

    /**
     * Sets the local table as the table from which data are read.
     * If the component had been connected to another table via a plug,
     * the plug is disconnected.
     */
    private void useLocalTable()
    {
      // switchToTable will set usingLocalTable to true.
      boolean haveLocalTable = usingLocalTable;

      if (!haveLocalTable && (plug != null)) {
        plug.disconnect();
      }

      switchToTable(localTable);
    }

    /**
     * Sets the table from which data are read.
     * @param   t   The table to use.
     */
    private void switchToTable(Table t)
    {
      if (table == null && t == null) {
        return;
      }
      if (table != null && table.equals(t)) {
        return;
      }

      if (table != null) {
        table.removeTableModelListener(tableModelListener);
      }
      table = t;
      if (table != null) {
        table.addTableModelListener(tableModelListener);
      }
      if (localTable.equals(t)) {
        usingLocalTable = true;
      }else{
        usingLocalTable = false;
        try {
          localTable.removeAllFields();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (table != null) {
        comboBoxX.setEnabled(true);
        comboBoxY.setEnabled(true);
        reloadFields();
					
        comboBoxX.setSelectedItem(fieldX); 
        comboBoxY.setSelectedItem(fieldY);
        fieldX = null;
        fieldY = null;
      }else{
        comboBoxX.removeAllItems();
        comboBoxX.setEnabled(false);
        comboBoxY.removeAllItems();
        comboBoxY.setEnabled(false);
      }
    }

    /**
     * Check if the local table is the table from which data are read.
     * @exception   RuntimeException    Thrown if data are read from
     *                                  a table obtained through a plug,
     *                                  or if data are not obtained from
     *                                  any table.
     */
    private void checkLocalTable()
    {
      if (!usingLocalTable) {
        throw new RuntimeException(BundleMessages.getMessage("notLocalTable"));
      }
    }

    /**
     * Adds a data set (i.e., a field) containing numbers to the local table.
     * @param   title   The name of the data set.
     * @param   data    The data to add.
     * @throws AttributeLockedException 
     * @throws InvalidFieldTypeException 
     * @throws InvalidKeyFieldException 
     * @throws InvalidFieldNameException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws TableNotExpandableException 
     * @throws NoFieldsInTableException 
     * @throws InvalidFieldIndexException 
     * @exception   IllegalArgumentException    Thrown if a dataset with
     *                  the given title already exists.
     */
    public void addDataSet(String title, double[] data)
        throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException
    {
      useLocalTable();
      try {
        table.getFieldIndex(title);
        String msg = BundleMessages.getMessage("dataSetExists");
        throw new IllegalArgumentException(MessageFormat.format(msg, title));
      } catch (InvalidFieldNameException e) {
      }
      table.addField(title, Double.class);
      setTableCells(title, data);
      reloadFields();
    }

    /**
     * Adds a data set (i.e., a field) containing strings to the local table.
     * @param   title   The name of the data set.
     * @param   data    The data to add.
     * @throws AttributeLockedException 
     * @throws InvalidFieldTypeException 
     * @throws InvalidKeyFieldException 
     * @throws InvalidFieldNameException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws TableNotExpandableException 
     * @throws NoFieldsInTableException 
     * @throws InvalidFieldIndexException 
     * @exception   IllegalArgumentException    Thrown if a dataset with
     *                  the given title already exists.
     */
    public void addDataSet(String title, String[] data)
        throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException
    {
      useLocalTable();
      try {
        table.getFieldIndex(title);
        String msg = BundleMessages.getMessage("dataSetExists");
        throw new IllegalArgumentException(MessageFormat.format(msg, title));
      } catch (InvalidFieldNameException e) {
      }
      table.addField(title, String.class);
      setTableCells(title, data);
      reloadFields();
    }

    /**
     * Updates the data belonging to an existing number data set (i.e., field)
     * in the local table.
     * @param   title   The name of the data set.
     * @param   data    The new data for the data set.
     * @throws InvalidFieldNameException 
     * @throws InvalidFieldNameException 
     * @throws TableNotExpandableException 
     * @throws NoFieldsInTableException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws InvalidFieldIndexException 
     * @throws IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     */
    public void setDataSet(String title, double[] data)
        throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException
    {
      checkLocalTable();
      table.getFieldIndex(title); // Check that the field exists
      setTableCells(title, data);
      reloadFields();
    }

    /**
     * Updates the data belonging to an existing strinig data set (i.e., field)
     * in the local table.
     * @param   title   The name of the data set.
     * @param   data    The new data for the data set.
     * @throws InvalidFieldNameException 
     * @throws InvalidFieldNameException 
     * @throws TableNotExpandableException 
     * @throws NoFieldsInTableException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws InvalidFieldIndexException 
     * @throws IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     */
    public void setDataSet(String title, String[] data)
        throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException
    {
      checkLocalTable();
      table.getFieldIndex(title); // Check that the field exists
      setTableCells(title, data);
      reloadFields();
    }
    
    /**
     * Sets the values of a numeric field in the local table.
     * If the table contains
     * fewer rows than the data provided, additional rows are added, having
     * 0 in all other fields. If the table contains more rows
     * than the data provided, 0s are placed in the additional rows.
     * @param   title   The name of the field.
     * @param   data    The values of the field.
     * @throws InvalidFieldNameException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws TableNotExpandableException 
     * @throws NoFieldsInTableException 
     * @throws InvalidFieldIndexException 
     */
    private void setTableCells(String title, double[] data)
      throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException
    {
      int nRows = table.getRecordCount();
      int n = Math.min(data.length, nRows);
      int fieldIndex = table.getFieldIndex(title);
      for (int i=0; i<n; i++) {
        table.setCell(fieldIndex, i, data[i]);
      }
      if (n < nRows) {
        for (int i=n; i<nRows; i++) {
          table.setCell(fieldIndex, i, ZERO);
        }
      }else{
        if (n < data.length) {
          for (int i=n; i<data.length; i++) {
            addRow(fieldIndex, data[i]);
          }
        }
      }
    }

    /**
     * Sets the values of a string field in the local table.
     * If the table contains fewer rows than the data provided, additional
     * rows are added, having empty strings in all other fields. If the table
     * contains more rows than the data provided, empty strings are placed in
     * the additional rows.
     * @param   title   The name of the field.
     * @param   data    The values of the field.
     * @throws InvalidFieldNameException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws TableNotExpandableException 
     * @throws NoFieldsInTableException 
     * @throws InvalidFieldIndexException 
     */
    private void setTableCells(String title, String[] data)
      throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException
    {
      int nRows = table.getRecordCount();
      int n = Math.min(data.length, nRows);
      int fieldIndex = table.getFieldIndex(title);
      for (int i=0; i<n; i++) {
        table.setCell(fieldIndex, i, data[i]);
      }
      if (n < nRows) {
        for (int i=n; i<nRows; i++) {
          table.setCell(fieldIndex, i, "");
        }
      }else{
        if (n < data.length) {
          for (int i=n; i<data.length; i++) {
            addRow(fieldIndex, data[i]);
          }
        }
      }
    }

    /**
     * Adds a row of data in the local table. All elements but one in the
     * new row will be 0 or empty strings, depending on their type.
     * @param   fieldIndex  The index of the non-<code>null</code> field.
     * @param   value       The value of the non-<code>null</code> field.
     * @throws TableNotExpandableException 
     * @throws DuplicateKeyException 
     * @throws NullTableKeyException 
     * @throws NoFieldsInTableException 
     * @throws InvalidDataFormatException 
     * @throws InvalidFieldIndexException 
     */
    private void addRow(int fieldIndex, double value)
      throws InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException, InvalidFieldIndexException
    {
      ArrayList<Object> newRow = createNewRow();
      newRow.set(fieldIndex, value);
      table.addRecord(newRow, false);
    }

    /**
     * Adds a row of data in the local table. All elements but one in the
     * new row will be 0 or empty strings, depending on their type.
     * @param   fieldIndex  The index of the non-<code>null</code> field.
     * @param   value       The value of the non-<code>null</code> field.
     * @throws TableNotExpandableException 
     * @throws DuplicateKeyException 
     * @throws NullTableKeyException 
     * @throws NoFieldsInTableException 
     * @throws InvalidDataFormatException 
     * @throws InvalidFieldIndexException 
     */
    private void addRow(int fieldIndex, String value)
      throws InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException, InvalidFieldIndexException
    {
      ArrayList<Object> newRow = createNewRow();
      newRow.set(fieldIndex, value);
      table.addRecord(newRow, false);
    }

    /**
     * Creates the data for a new row in the table. All elements are 0 or empty
     * strings, depending on their type.
     * @throws InvalidFieldIndexException 
     * @return  The data for a new row in the table.
     * @throws TableNotExpandableException 
     * @throws DuplicateKeyException 
     * @throws NullTableKeyException 
     * @throws NoFieldsInTableException 
     * @throws InvalidDataFormatException 
     */
    private ArrayList<Object> createNewRow() throws InvalidFieldIndexException
    {
      int nFields = table.getFieldCount();
      ArrayList<Object> newRow = new ArrayList<Object>(nFields);
      for (int i=0; i<nFields; i++) {
        Class<?> c = table.getTableField(i).getClass();
        if (c.equals(DoubleTableField.class)) {
          newRow.add(ZERO);
        }else{
          newRow.add("");
        }
      }
      return newRow;
    }
    
    /**
     * Sets the value of a numeric element in a given data set (i.e., field)
     * in the
     * local table.
     * @param   title   The title of the data set.
     * @param   pos     The position of the element in the data set.
     * @param   value   The value of the element.
     * @throws  IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     * @throws InvalidFieldNameException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     */
    public void setElement(String title, int pos, double value)
      throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      table.setCell(fieldIndex, pos, value);
    }

    /**
     * Sets the value of a string element in a given data set (i.e., field)
     * in the
     * local table.
     * @param   title   The title of the data set.
     * @param   pos     The position of the element in the data set.
     * @param   value   The value of the element.
     * @throws  IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     * @throws InvalidFieldNameException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     */
    public void setElement(String title, int pos, String value)
      throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      table.setCell(fieldIndex, pos, value);
    }

    /**
     * Returns the type of a data set (i.e., field).
     * @param   title   The title of the data set.
     * @return  One of <code>NUMBER</code>, <code>STRING</code>.
     * @throws InvalidFieldNameException 
     * @throws InvalidFieldIndexException 
     */
    public int getDataSetType(String title) throws InvalidFieldNameException, InvalidFieldIndexException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      Class<?> c = table.getTableField(fieldIndex).getClass();
      if (c.equals(DoubleTableField.class)) {
        return NUMBER;
      }else{
        return STRING;
      }
    }

    /**
     * Adds a new numeric element at the bottom of a given data set
     * (i.e., field) in the local table.
     * @param   title   The title of the data set.
     * @param   value   The value of the element.
     * @throws InvalidFieldNameException 
     * @throws TableNotExpandableException 
     * @throws DuplicateKeyException 
     * @throws NullTableKeyException 
     * @throws NoFieldsInTableException 
     * @throws InvalidDataFormatException 
     * @throws InvalidFieldIndexException 
     * @exception   IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     */
    public void addElement(String title, double value)
      throws InvalidFieldNameException, InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException, InvalidFieldIndexException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      addRow(fieldIndex, value);
    }

    /**
     * Adds a new string element at the bottom of a given data set
     * (i.e., field) in the local table.
     * @param   title   The title of the data set.
     * @param   value   The value of the element.
     * @throws InvalidFieldNameException 
     * @throws TableNotExpandableException 
     * @throws DuplicateKeyException 
     * @throws NullTableKeyException 
     * @throws NoFieldsInTableException 
     * @throws InvalidDataFormatException 
     * @throws InvalidFieldIndexException 
     * @exception   IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     */
    public void addElement(String title, String value)
      throws InvalidFieldNameException, InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException, InvalidFieldIndexException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      addRow(fieldIndex, value);
    }

    /**
     * Removes a data set (i.e., field) from the local table.
     * @param   title   The name of the data set.
     * @throws AttributeLockedException 
     * @throws FieldNotRemovableException 
     * @throws CalculatedFieldExistsException 
     * @throws TableNotExpandableException 
     * @throws InvalidFieldNameException 
     * @throws InvalidFieldNameException 
     */
    public void removeDataSet(String title)
      throws InvalidFieldNameException, TableNotExpandableException, CalculatedFieldExistsException, FieldNotRemovableException, AttributeLockedException
    {
      checkLocalTable();
      table.removeField(title);
    }

    /**
     * Removes an element from the given data set (i.e., field) in the local
     * table.
     * Elements below the given element are moved upwards by one position,
     * and the last element in the data set is set to 0 or an empty string,
     * depending on the data set type.
     * @param   title   The title of the data set.
     * @param   pos     The position of the element in the data set.
     * @throws InvalidFieldNameException 
     * @throws AttributeLockedException 
     * @throws DuplicateKeyException 
     * @throws InvalidDataFormatException 
     * @throws NullTableKeyException 
     * @throws InvalidCellAddressException 
     * @throws InvalidFieldIndexException 
     */
    public void removeElement(String title, int pos)
      throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, InvalidFieldIndexException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      int nRowsMinus1 = table.getRecordCount() - 1;
      
      for (int i=pos; i<nRowsMinus1; i++) {
        table.setCell(fieldIndex, pos, table.getCell(fieldIndex, i+1));
      }
      if (getDataSetType(title) == NUMBER) {
        table.setCell(fieldIndex, nRowsMinus1, 0.0);
      }else{
        table.setCell(fieldIndex, nRowsMinus1, "");
      }
    }

    /**
     * Returns an element in a given data set (i.e., field) in the local
     * table.
     * @param   title   The title of the data set.
     * @param   pos     The position of the element in the data set.
     * @return  The requested element.
     * @throws InvalidFieldNameException 
     * @throws InvalidCellAddressException 
     */
    public Object getElement(String title, int pos)
      throws InvalidFieldNameException, InvalidCellAddressException
    {
      checkLocalTable();
      int fieldIndex = table.getFieldIndex(title);
      return table.getCell(fieldIndex, pos);
    }

    /**
     * Returns the data in a given data set (i.e., field) in the local table.
     * @param   title   The title of the data set.
     * @return  An list containing the data in the specified data set.
     * @throws InvalidFieldNameException 
     */
    public ArrayBase getDataSet(String title)
      throws InvalidFieldNameException
    {
      checkLocalTable();
      return table.getField(title);
    }

    /**
     * Returns the titles of the data sets (i.e., fields) in the local table.
     * @return  A list containing the titles of the data sets in the local
     *          table.
     */
    public StringBaseArray getDataSetTitles()
    {
      checkLocalTable();
      return table.getFieldNames();
    }

    /**
     * Changes the title of a data set (i.e., field) in the local table.
     * @param   oldTitle    The old title of the data set.
     * @param   newTitle    The new title of the data set.
     * @throws FieldNameInUseException 
     * @throws InvalidFieldNameException 
     */
    public void setDataSetTitle(String oldTitle, String newTitle)
      throws InvalidFieldNameException, FieldNameInUseException
    {
      checkLocalTable();
      table.renameField(oldTitle, newTitle);
    }

//	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			Graph2 graph2 = new Graph2();
//
//			JFrame frame = new JFrame("Graph 2");
//			frame.addWindowListener(new WindowAdapter() {
//				public void windowClosed(WindowEvent e) {
//					System.exit(0);
//				}
//			});
//			frame.setContentPane(graph2);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.pack();
//			frame.setLocationRelativeTo(null);
//			frame.setVisible(true);
//			frame.toFront();
//			
////			graph2.addFunction("x^2");
////			graph2.plot(1.5, 1.5);
////			graph2.plot(2.5, 2.5);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
