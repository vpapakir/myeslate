package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.DateTableField;
import gr.cti.eslate.database.engine.DoubleTableField;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.StringTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.TableFieldBaseArray;
import gr.cti.eslate.database.engine.event.DatabaseAdapter;
import gr.cti.eslate.database.engine.event.DatabaseListener;
import gr.cti.eslate.database.engine.event.TableAddedEvent;
import gr.cti.eslate.database.engine.event.TableRemovedEvent;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IPolyLineLayerView;
import gr.cti.eslate.protocol.IPolygonLayerView;
import gr.cti.eslate.tableModel.event.ColumnAddedEvent;
import gr.cti.eslate.tableModel.event.ColumnRemovedEvent;
import gr.cti.eslate.tableModel.event.ColumnRenamedEvent;
import gr.cti.eslate.tableModel.event.ColumnTypeChangedEvent;
import gr.cti.eslate.tableModel.event.DatabaseTableModelAdapter;
import gr.cti.eslate.tableModel.event.TableModelAdapter;
import gr.cti.eslate.utils.ESlateFileDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

class LayerInfo extends JFrame {
	//Constants
	private final int NORMAL_OUTLINE=0;
	private final int NORMAL_FILL=1;
	private final int SELECTED_OUTLINE=2;
	private final int SELECTED_FILL=3;
	private final int HIGHLIGHTED_OUTLINE=4;
	private final int HIGHLIGHTED_FILL=5;
	//Members
	private Layer layer;
	private MapCreator creator;
	private DefaultListModel tableModel;
	private boolean modified;
	//Object selectability is a per view property. True propagates to all views.
	private boolean propagateObjSel=false;
	private ShapeFileInfo shapefileInfo;
	private Table cTable;
	private AbstractTableField tooltipField,labelField,fromField,toField;
	private JList table;
	private boolean firstTimeShown;
	private ActionListener propertyListener;
	private Component propertyFrame;
	private DatabaseListener dblist;
	private gr.cti.eslate.tableModel.event.TableModelListener tableRename;
	/**
	 * Looks for changes that should appear in the
	 * comboboxes of the LayerInfo frame.
	 */
	private gr.cti.eslate.tableModel.event.DatabaseTableModelListener tmListener;
	/**
	 * If true fires the PaintPropertiesChanged event for the layer.
	 */
	private boolean propertiesModified;

	/*
	 * Backup values used when "cancel" is pressed.
	 */
	private String bName;
	private boolean bModified;
	private ShapeFileInfo bShapefileInfo;
	private Table bTable;
	private AbstractTableField bTooltip;
	private AbstractTableField bLabel;
	private AbstractTableField bFrom;
	private AbstractTableField bTo;
	private boolean bEditable;
	private boolean bVisible;
	private boolean bShowNotSelected;
	private boolean bSelectObjects;
	private Color bNormalOutline;
	private Color bNormalFill;
	private Color bSelectedOutline;
	private Color bSelectedFill;
	private Color bHighlightedOutline;
	private Color bHighlightedFill;
	private int bAlphaNormalOutline;
	private int bAlphaNormalFill;
	private int bAlphaSelectedOutline;
	private int bAlphaSelectedFill;
	private int bAlphaHighlightedOutline;
	private int bAlphaHighlightedFill;
	private boolean bEnableVisible;
	private boolean bEnableShowNotSelected;
	private boolean bShowBlankRecord;
	private String bLongBL;
	private String bLatBL;
	private String bLongTR;
	private String bLatTR;


	private SmartLayout smartLayout1 = new SmartLayout();
	private JTextField name = new JTextField();
	private JLabel modifiedLabel = new JLabel();
	private JLabel shapeLabel = new JLabel();
	private JButton browse = new JButton();
	private JLabel shapefile = new JLabel();
	private JLabel tableLabel = new JLabel();
	private JLabel tooltipLabel = new JLabel();
	private JComboBox tooltip = new JComboBox();
	private JLabel labelLabel = new JLabel();
	private JComboBox label = new JComboBox();
	private JButton labelFont = new JButton();
	private JCheckBox editable = new JCheckBox();
	private JCheckBox visible = new JCheckBox();
	private JButton newTable = new JButton();
	private JCheckBox showNotSelected = new JCheckBox();
	private JCheckBox selectObjects = new JCheckBox();
	private JLabel colorsLabel = new JLabel();
	private JLabel normalLabel = new JLabel();
	private JButton normalOutline = new JButton();
	private JButton normalFill = new JButton();
	private JLabel selectedLabel = new JLabel();
	private JButton selectedOutline = new JButton();
	private JButton selectedFill = new JButton();
	private JLabel highlightedLabel = new JLabel();
	private JButton highlightedOutline = new JButton();
	private JButton highlightedFill = new JButton();
	private JCheckBox enableVisible = new JCheckBox();
	private JCheckBox enableShowNotSelected = new JCheckBox();
	private JCheckBox showBlankRecord = new JCheckBox();
	private JButton ok = new JButton();
	private JButton cancel = new JButton();
	private JLabel shapetype = new JLabel();
	private JButton property = new JButton();
	private JScrollPane scrollpane = new JScrollPane();
	private JButton apply = new JButton();
	private MyJSlider sldrNormalOutline = new MyJSlider();
	private MyJSlider sldrSelectedOutline = new MyJSlider();
	private MyJSlider sldrHighlightedOutline = new MyJSlider();
	private MyJSlider sldrNormalFill = new MyJSlider();
	private MyJSlider sldrSelectedFill = new MyJSlider();
	private MyJSlider sldrHighlightedFill = new MyJSlider();
	private JButton deftype = new JButton();
	private JLabel lblTime = new JLabel();
	private JComboBox cbxFrom = new JComboBox();
	private JComboBox cbxTo = new JComboBox();
	//Only in raster mode
	private JLabel coordsLabel = new JLabel();
	private JLabel longBLLabel = new JLabel();
	private JNumberField longBL = new JNumberField("");
	private JLabel latBLLabel = new JLabel();
	private JNumberField latBL = new JNumberField("");
	private JLabel longTRLabel = new JLabel();
	private JNumberField longTR = new JNumberField("");
	private JLabel latTRLabel = new JLabel();
	private JNumberField latTR = new JNumberField("");

	private LayerListener layerList=new gr.cti.eslate.mapModel.LayerAdapter() {
		public void layerGeographicObjectAdded(LayerEvent e) {
			if (shapefileInfo!=null) {
				shapefileInfo.size++;
				shapetype.setText(shapefileInfo.getSize()+" "+MapCreator.bundleCreator.getString("objects")+" "+MapCreator.bundleCreator.getString(""+shapefileInfo.getShapeType()));
			}
			table.repaint();
		}
		public void layerGeographicObjectRemoved(LayerEvent e) {
			if (shapefileInfo!=null) {
				shapefileInfo.size--;
				shapetype.setText(shapefileInfo.getSize()+" "+MapCreator.bundleCreator.getString("objects")+" "+MapCreator.bundleCreator.getString(""+shapefileInfo.getShapeType()));
			}
			table.repaint();
		}
	};


	LayerInfo(Layer layr,MapCreator crtor,boolean onCloseAddToLayers) {
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		tmListener=new DamnJBuilderTM();
		tableRename=new DamnJBuilderRename();

		try {
			setIconImage((new ImageIcon(Map.class.getResource("images/mapModelBeanIcon.gif")).getImage()));
		} catch(Exception e) {/*No icon! Sniff!*/}

		propertiesModified=false;
		this.layer=layr;
		if (layer!=null && layer.getViews().size()>0) {
			((LayerView) layer.getViews().get(0)).addLayerListener(layerList);
		}
		firstTimeShown=onCloseAddToLayers;
		this.creator=crtor;
		creator.addMapCreatorListener(new MapCreatorListener() {
			public void databaseDefinition() {
				Table table=LayerInfo.this.creator.getDatabase().getTable((cTable==null)?"":cTable.getTitle());
				//Changing a table "asynchronously" can be done by changing the database from the map info pane.
				//If the table has similar data they will be kept.
				if (table!=null && cTable!=null && table.getTitle().equals(cTable.getTitle()) && table.getRecordCount()>=cTable.getRecordCount()) {
					for (int i=0;i<table.getFieldCount();i++)
						try {
							//This damn TableField throws NullPointerException on equals!
							if (tooltipField!=null && table.getTableField(i).getName().equals(tooltipField.getName())) {
								tooltipField=table.getTableField(i);
								bTooltip=tooltipField;
							}
							if (labelField!=null && table.getTableField(i).getName().equals(labelField.getName())) {
								labelField=table.getTableField(i);
								bLabel=labelField;
							}
							if (fromField!=null && table.getTableField(i).getName().equals(fromField.getName()) && table.getTableField(i).getDataType()==DateTableField.DATA_TYPE) {
								fromField=table.getTableField(i);
								bFrom=fromField;
							}
							if (toField!=null && table.getTableField(i).getName().equals(toField.getName()) && table.getTableField(i).getDataType()==DateTableField.DATA_TYPE) {
								toField=table.getTableField(i);
								bTo=toField;
							}
						} catch(InvalidFieldIndexException e) {}

				} else {
					tooltipField=labelField=fromField=toField=null;
				}

				setLocalcTable(table);
				try {
					if (layer!=null) {
						layer.setTable(cTable);
						layer.setTipBase(tooltipField);
						layer.setLabelBase(labelField);
						layer.setDateFromBase(fromField);
						layer.setDateToBase(toField);
					}
				} catch(InvalidLayerDataException e) {
					System.err.println("MAP#200005181303: Invalid table set to the layer.");
				}
				setModified(true);

				tableModel.removeAllElements();
				tooltip.removeAllItems();
				label.removeAllItems();
				cbxFrom.removeAllItems();
				cbxTo.removeAllItems();
				if (shapefileInfo!=null)
					buildTableList();
				dblist=new DamnJBuilderDB();
				LayerInfo.this.creator.getDatabase().addDatabaseListener(dblist);
				//For some strange reason the fields are nullified by the building. Reset them
				if (bLabel!=null) {
					labelField=bLabel;
					label.setSelectedItem(layer.getLabelBase());
				} else
					if (label.getItemCount()>0)
						label.setSelectedIndex(0);
					else
						label.setSelectedIndex(-1);
				if (bTooltip!=null) {
					tooltipField=bTooltip;
					tooltip.setSelectedItem(tooltipField);
				} else
					if (tooltip.getItemCount()>0)
						tooltip.setSelectedIndex(0);
					else
						tooltip.setSelectedIndex(-1);
				if (bFrom!=null) {
					fromField=bFrom;
					cbxFrom.setSelectedItem(fromField);
				} else
					if (cbxFrom.getItemCount()>0)
						cbxFrom.setSelectedIndex(0);
					else
						cbxFrom.setSelectedIndex(-1);
				if (bTo!=null) {
					toField=bTo;
					cbxTo.setSelectedItem(toField);
				} else
					if (cbxTo.getItemCount()>0)
						cbxTo.setSelectedIndex(0);
					else
						cbxTo.setSelectedIndex(-1);
			}
			public void tableDefinition(Table newtable) {
				if (shapefileInfo!=null) {
					if (newtable.getRecordCount()==shapefileInfo.getSize()) {
						int si=table.getSelectedIndex();
						int l=-1,t=-1,df=-1,dt=-1;
						//Keep the selected items because the selection changes after the addition of the table
						if (si!=-1) {
							l=label.getSelectedIndex();
							t=tooltip.getSelectedIndex();
							df=cbxFrom.getSelectedIndex();
							dt=cbxTo.getSelectedIndex();
						}
						if (!tableModel.contains(newtable))
							tableModel.add(0,newtable);
						else {
							//Has been added by tableAdded. Remove it to add it again on top.
							tableModel.removeElement(newtable);
							tableModel.add(0,newtable);
						}
						table.setSelectedIndex(table.getSelectedIndex()+1);
						//Restore the selected items
						if (si!=-1) {
							label.setSelectedIndex(l);
							tooltip.setSelectedIndex(t);
							cbxFrom.setSelectedIndex(df);
							cbxTo.setSelectedIndex(dt);
						}
					} else if (newtable.getRecordCount()>shapefileInfo.getSize())
						if (!tableModel.contains(newtable))
							tableModel.addElement(newtable);
				}
			}
			public void mapCreatorClosing() {
				dispose();
			}
			public void mapCreatorClosed() {
				dispose();
			}
		});

		//Customize dialog with multilingual support
		setSize(500,610);
		setResizable(false);
		Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((s.width-getWidth())/2,(s.height-getHeight())/2);

		browse.setText(MapCreator.bundleCreator.getString("browse"));
		property.setText(MapCreator.bundleCreator.getString("property"));
		deftype.setText(MapCreator.bundleCreator.getString("newlayertype"));
		shapeLabel.setText(MapCreator.bundleCreator.getString("shapefile"));
		shapefile.setText(MapCreator.bundleCreator.getString("mustdefshapefile"));
		tableLabel.setText(MapCreator.bundleCreator.getString("tabledata"));
		tooltipLabel.setText(MapCreator.bundleCreator.getString("tooltip"));
		labelLabel.setText(MapCreator.bundleCreator.getString("label"));
		lblTime.setText(MapCreator.bundleCreator.getString("timefromto"));
		newTable.setText(MapCreator.bundleCreator.getString("newtable"));
		colorsLabel.setText(MapCreator.bundleCreator.getString("colors"));
		normalLabel.setText(MapCreator.bundleCreator.getString("normal"));
		normalOutline.setText(MapCreator.bundleCreator.getString("outline"));
		normalFill.setText(MapCreator.bundleCreator.getString("fill"));
		selectedLabel.setText(MapCreator.bundleCreator.getString("selected"));
		selectedOutline.setText(MapCreator.bundleCreator.getString("outline"));
		selectedFill.setText(MapCreator.bundleCreator.getString("fill"));
		highlightedLabel.setText(MapCreator.bundleCreator.getString("highlighted"));
		highlightedOutline.setText(MapCreator.bundleCreator.getString("outline"));
		highlightedFill.setText(MapCreator.bundleCreator.getString("fill"));
		editable.setText(MapCreator.bundleCreator.getString("editable"));
		visible.setText(MapCreator.bundleCreator.getString("visible"));
		showNotSelected.setText(MapCreator.bundleCreator.getString("shownotselected"));
		selectObjects.setText(MapCreator.bundleCreator.getString("selectobjects"));
		enableVisible.setText(MapCreator.bundleCreator.getString("enablevisible"));
		enableShowNotSelected.setText(MapCreator.bundleCreator.getString("enableshownotselected"));
		showBlankRecord.setText(MapCreator.bundleCreator.getString("showblankrecord"));
		ok.setText(MapCreator.bundleCreator.getString("ok"));
		cancel.setText(MapCreator.bundleCreator.getString("cancel"));
		apply.setText(MapCreator.bundleCreator.getString("apply"));
		coordsLabel.setText(MapCreator.bundleCreator.getString("rastercoordinates"));
		longBLLabel.setText(MapCreator.bundleCreator.getString("minlong"));
		latBLLabel.setText(MapCreator.bundleCreator.getString("minlat"));
		longTRLabel.setText(MapCreator.bundleCreator.getString("maxlong"));
		latTRLabel.setText(MapCreator.bundleCreator.getString("maxlat"));

		//Visual customizations
		normalOutline.setRequestFocusEnabled(false);
		normalFill.setRequestFocusEnabled(false);
		selectedOutline.setRequestFocusEnabled(false);
		selectedFill.setRequestFocusEnabled(false);
		highlightedOutline.setRequestFocusEnabled(false);
		highlightedFill.setRequestFocusEnabled(false);
		sldrNormalOutline.setRequestFocusEnabled(false);
		sldrSelectedOutline.setRequestFocusEnabled(false);
		sldrHighlightedOutline.setRequestFocusEnabled(false);
		sldrNormalFill.setRequestFocusEnabled(false);
		sldrSelectedFill.setRequestFocusEnabled(false);
		sldrHighlightedFill.setRequestFocusEnabled(false);
		apply.setEnabled(false);
		//Min Long Field
		longBL.setValidChars("0123456789.-");
		longBL.setHorizontalAlignment(JNumberField.RIGHT);
		//Min Lat Field
		latBL.setValidChars("0123456789.-");
		latBL.setHorizontalAlignment(JNumberField.RIGHT);
		//Max Long Field
		longTR.setValidChars("0123456789.-");
		longTR.setHorizontalAlignment(JNumberField.RIGHT);
		//Max Lat Field
		latTR.setValidChars("0123456789.-");
		latTR.setHorizontalAlignment(JNumberField.RIGHT);

		VectorLayer vlayer=null;
		if (layer instanceof VectorLayer)
			vlayer=(VectorLayer) layer;
		if ((vlayer!=null) && (vlayer.getNormalOutlineColor()!=null)) {
			normalOutline.setBackground(new Color(vlayer.getNormalOutlineColor().getRed(),vlayer.getNormalOutlineColor().getGreen(),vlayer.getNormalOutlineColor().getBlue()));
			sldrNormalOutline.setValue(100-(int) (vlayer.getNormalOutlineColor().getAlpha()/255f*100));
		} else {
			normalOutline.setBackground(new Color(64,64,255));
			sldrNormalOutline.setValue(0);
		}
		if ((vlayer!=null) && (vlayer.getNormalFillColor()!=null)) {
			normalFill.setBackground(new Color(vlayer.getNormalFillColor().getRed(),vlayer.getNormalFillColor().getGreen(),vlayer.getNormalFillColor().getBlue()));
			sldrNormalFill.setValue(100-(int) (vlayer.getNormalFillColor().getAlpha()/255f*100));
		} else {
			normalFill.setBackground(new Color(128,128,255));
			sldrNormalFill.setValue(0);
		}
		if ((vlayer!=null) && (vlayer.getSelectedOutlineColor()!=null)) {
			selectedOutline.setBackground(new Color(vlayer.getSelectedOutlineColor().getRed(),vlayer.getSelectedOutlineColor().getGreen(),vlayer.getSelectedOutlineColor().getBlue()));
			sldrSelectedOutline.setValue(100-(int) (vlayer.getSelectedOutlineColor().getAlpha()/255f*100));
		} else {
			selectedOutline.setBackground(new Color(255,64,255));
			sldrSelectedOutline.setValue(0);
		}
		if ((vlayer!=null) && (vlayer.getSelectedFillColor()!=null)) {
			selectedFill.setBackground(new Color(vlayer.getSelectedFillColor().getRed(),vlayer.getSelectedFillColor().getGreen(),vlayer.getSelectedFillColor().getBlue()));
			sldrSelectedFill.setValue(100-(int) (vlayer.getSelectedFillColor().getAlpha()/255f*100));
		} else {
			selectedFill.setBackground(new Color(255,128,255));
			sldrSelectedFill.setValue(0);
		}
		if ((vlayer!=null) && (vlayer.getHighlightedOutlineColor()!=null)) {
			highlightedOutline.setBackground(new Color(vlayer.getHighlightedOutlineColor().getRed(),vlayer.getHighlightedOutlineColor().getGreen(),vlayer.getHighlightedOutlineColor().getBlue()));
			sldrHighlightedOutline.setValue(100-(int) (vlayer.getHighlightedOutlineColor().getAlpha()/255f*100));
		} else {
			highlightedOutline.setBackground(new Color(255,255,128));
			sldrHighlightedOutline.setValue(0);
		}
		if ((vlayer!=null) && (vlayer.getHighlightedFillColor()!=null)) {
			highlightedFill.setBackground(new Color(vlayer.getHighlightedFillColor().getRed(),vlayer.getHighlightedFillColor().getGreen(),vlayer.getHighlightedFillColor().getBlue()));
			sldrHighlightedFill.setValue(100-(int) (vlayer.getHighlightedFillColor().getAlpha()/255f*100));
		} else {
			highlightedFill.setBackground(new Color(128,255,128));
			sldrHighlightedFill.setValue(0);
		}

		normalOutline.setForeground(getProperColor(normalOutline.getBackground()));
		normalFill.setForeground(getProperColor(normalFill.getBackground()));
		selectedOutline.setForeground(getProperColor(selectedOutline.getBackground()));
		selectedFill.setForeground(getProperColor(selectedFill.getBackground()));
		highlightedOutline.setForeground(getProperColor(highlightedOutline.getBackground()));
		highlightedFill.setForeground(getProperColor(highlightedFill.getBackground()));

		normalOutline.addActionListener(new ColorActionListener(NORMAL_OUTLINE,normalOutline));
		normalFill.addActionListener(new ColorActionListener(NORMAL_FILL,normalFill));
		selectedOutline.addActionListener(new ColorActionListener(SELECTED_OUTLINE,selectedOutline));
		selectedFill.addActionListener(new ColorActionListener(SELECTED_FILL,selectedFill));
		highlightedOutline.addActionListener(new ColorActionListener(HIGHLIGHTED_OUTLINE,highlightedOutline));
		highlightedFill.addActionListener(new ColorActionListener(HIGHLIGHTED_FILL,highlightedFill));

		sldrNormalOutline.addChangeListener(new AlphaChangeListener(sldrNormalOutline));
		sldrNormalFill.addChangeListener(new AlphaChangeListener(sldrNormalFill));
		sldrSelectedOutline.addChangeListener(new AlphaChangeListener(sldrSelectedOutline));
		sldrSelectedFill.addChangeListener(new AlphaChangeListener(sldrSelectedFill));
		sldrHighlightedOutline.addChangeListener(new AlphaChangeListener(sldrHighlightedOutline));
		sldrHighlightedFill.addChangeListener(new AlphaChangeListener(sldrHighlightedFill));

		sldrNormalOutline.addMouseListener(new AlphaMouseListener(sldrNormalOutline));
		sldrNormalFill.addMouseListener(new AlphaMouseListener(sldrNormalFill));
		sldrSelectedOutline.addMouseListener(new AlphaMouseListener(sldrSelectedOutline));
		sldrSelectedFill.addMouseListener(new AlphaMouseListener(sldrSelectedFill));
		sldrHighlightedOutline.addMouseListener(new AlphaMouseListener(sldrHighlightedOutline));
		sldrHighlightedFill.addMouseListener(new AlphaMouseListener(sldrHighlightedFill));

		sldrNormalOutline.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+sldrNormalOutline.getValue()+"%");
		sldrNormalFill.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+sldrNormalFill.getValue()+"%");
		sldrSelectedOutline.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+sldrSelectedOutline.getValue()+"%");
		sldrSelectedFill.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+sldrSelectedFill.getValue()+"%");
		sldrHighlightedOutline.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+sldrHighlightedOutline.getValue()+"%");
		sldrHighlightedFill.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+sldrHighlightedFill.getValue()+"%");

		//Initializations
		table=new JList();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableModel=new DefaultListModel();
		table.setCellRenderer(new TableCellRenderer());
		table.setModel(tableModel);
		table.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				setLocalcTable((Table) table.getSelectedValue());
				buildFieldCombos();
			}
		});
		scrollpane.getViewport().setView(table);
		scrollpane.setBorder(name.getBorder());

		label.setRenderer(new ComboRenderer());
		tooltip.setRenderer(new ComboRenderer());
		cbxFrom.setRenderer(new ComboRenderer());
		cbxTo.setRenderer(new ComboRenderer());

		if (creator.getDatabase()!=null) {
			dblist=new DamnJBuilderDB();
			creator.getDatabase().addDatabaseListener(dblist);
		}

		property.setEnabled(false);
		newTable.setEnabled(false);
		setModified(false);
		if (layer!=null) {
			if (layer instanceof RasterLayer)
				convertToRasterLayerInfo();
			browse.setEnabled(false);
			deftype.setEnabled(false);
			name.setText(layer.getName());
			setTitle(name.getText());
			if (layer instanceof PointLayer)
				shapefileInfo=new ShapeFileInfo(MapCreator.bundleCreator.getString("shapefiledefined"),ShapeFileInfo.POINT_LAYER,layer.getObjectCount());
			else if (layer instanceof PolyLineLayer)
				shapefileInfo=new ShapeFileInfo(MapCreator.bundleCreator.getString("shapefiledefined"),ShapeFileInfo.POLYLINE_LAYER,layer.getObjectCount());
			else if (layer instanceof PolygonLayer)
				shapefileInfo=new ShapeFileInfo(MapCreator.bundleCreator.getString("shapefiledefined"),ShapeFileInfo.POLYGON_LAYER,layer.getObjectCount());
			else if (layer instanceof RasterLayer)
				shapefileInfo=new ShapeFileInfo(MapCreator.bundleCreator.getString("shapefiledefined"),ShapeFileInfo.RASTER_LAYER,layer.getObjectCount());
			showShapeFileInfo();
			buildTableList();
			setLocalcTable(layer.getTable());
			table.setSelectedValue(cTable,true);
			tooltipField=layer.getTipBase();
			labelField=layer.getLabelBase();
			fromField=layer.getDateFromBase();
			toField=layer.getDateToBase();
			buildFieldCombos();
			if (labelField!=null)
				label.setSelectedItem(layer.getLabelBase());
			else
				if (label.getItemCount()>0)
					label.setSelectedIndex(0);
				else
					label.setSelectedIndex(-1);
			if (tooltipField!=null)
				tooltip.setSelectedItem(tooltipField);
			else
				if (tooltip.getItemCount()>0)
					tooltip.setSelectedIndex(0);
				else
					tooltip.setSelectedIndex(-1);
			if (fromField!=null)
				cbxFrom.setSelectedItem(fromField);
			else
				if (cbxFrom.getItemCount()>0)
					cbxFrom.setSelectedIndex(0);
				else
					cbxFrom.setSelectedIndex(-1);
			if (toField!=null)
				cbxTo.setSelectedItem(toField);
			else
				if (cbxTo.getItemCount()>0)
					cbxTo.setSelectedIndex(0);
				else
					cbxTo.setSelectedIndex(-1);
			editable.setSelected(layer.isEditable());
			visible.setSelected(layer.getDefaultVisibility());
			showNotSelected.setSelected(!layer.getHideUnselected());
			selectObjects.setSelected(layer.getCanSelectObjects());
			enableVisible.setSelected(layer.isHideable());
			enableShowNotSelected.setSelected(layer.getCanHideUnselected());
			showBlankRecord.setSelected(layer.getShowBlankRecordObjects());
			//Only for rasters
			if (layer instanceof RasterLayer) {
				Rectangle2D.Double r=((RasterLayer) layer).getBoundingRect();
				if (r!=null) {
					longBL.setNumber(r.getX());
					latBL.setNumber(r.getY());
					longTR.setNumber(r.getX()+r.getWidth());
					latTR.setNumber(r.getY()+r.getHeight());
				}
				sldrNormalOutline.setValue((int) (100-((RasterLayer) layer).getNormalViewTransparencyLevel()*100/255d));
				sldrSelectedOutline.setValue((int) (100-((RasterLayer) layer).getSelectedViewTransparencyLevel()*100/255d));
				sldrHighlightedOutline.setValue((int) (100-((RasterLayer) layer).getHighlightedViewTransparencyLevel()*100/255d));
			}
		} else {
			name.setText(MapCreator.bundleCreator.getString("layer"));
			setTitle(name.getText());
			shapetype.setText(" ");
			editable.setSelected(true);
			visible.setSelected(true);
			showNotSelected.setSelected(true);
			selectObjects.setSelected(true);
			enableVisible.setSelected(true);
			enableShowNotSelected.setSelected(true);
			showBlankRecord.setSelected(true);
		}

		//Removed as of task #537
		enableShowNotSelected.setVisible(false);

		//Name field listener
		name.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
				setTitle(name.getText());
			}
		});

		//Browse button listener
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				ESlateFileDialog fd=new ESlateFileDialog(LayerInfo.this,"",ESlateFileDialog.LOAD,new String[]{".shp"});
				fd.setTitle(MapCreator.bundleCreator.getString("shapefile"));
				if (MapCreator.globalDir!=null)
					fd.setDirectory(MapCreator.globalDir);
				fd.setFile("*.shp;*.gif");
				fd.show();
				if (fd.getFile()!=null) {
					MapCreator.globalDir=fd.getDirectory();
					try {
						//Shapefile, vector graphics
						if (fd.getFile().toLowerCase().endsWith("shp")) {
							String shp=fd.getDirectory()+fd.getFile();
							shapefileInfo=RTree.getShapeFileInfo(new URL("file:///"+shp),new URL("file:///"+shp.substring(0,shp.length()-1)+"x"));
						} else if (fd.getFile().toLowerCase().endsWith("gif")) {
						//Gif, raster graphics
							shapefileInfo=new ShapeFileInfo("file:///"+fd.getDirectory()+fd.getFile(),ShapeFileInfo.RASTER_LAYER,255);
						} else
							throw new Exception("No file type!");
						showShapeFileInfo();
						setModified(true);
						buildTableList();
					} catch(Exception ex) {
						shapefileInfo=null;
						JOptionPane.showMessageDialog(LayerInfo.this,MapCreator.bundleCreator.getString("errorshapefile"),"",JOptionPane.ERROR_MESSAGE);
						System.err.println("MAPCREATOR#LI0001201230: Invalid state! Information message follows. You may continue working.");
						ex.printStackTrace();
					}
				}
				setCursor(Helpers.normalCursor);
			}
		});

		//New layer type button listener
		deftype.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Helpers.waitCursor);
				shapefileInfo=new ShapeFileInfo(MapCreator.bundleCreator.getString("newlayer"),ShapeFileInfo.POINT_LAYER,0);
				showShapeFileInfo();
				setModified(true);
				buildTableList();
				setCursor(Helpers.normalCursor);
			}
		});

		//Tooltip combo listener
		tooltip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
				if (tooltip.getSelectedIndex()==0) //The "null" object.
					tooltipField=null;
				else
					tooltipField=(AbstractTableField) tooltip.getSelectedItem();
			}
		});

		//Label combo listener
		label.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
				if (label.getSelectedIndex()==0) //The "null" object.
					labelField=null;
				else
					labelField=(AbstractTableField) label.getSelectedItem();
			}
		});

		//Label font listener
		labelFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		//Date from combo listener
		cbxFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
				if (cbxFrom.getSelectedIndex()==0) //The "null" object.
					fromField=null;
				else
					fromField=(AbstractTableField) cbxFrom.getSelectedItem();
			}
		});

		//Date to combo listener
		cbxTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
				if (cbxTo.getSelectedIndex()==0) //The "null" object.
					toField=null;
				else
					toField=(AbstractTableField) cbxTo.getSelectedItem();
			}
		});

		//New Table button listener
		newTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=JOptionPane.showInputDialog(LayerInfo.this,MapCreator.bundleCreator.getString("givetablename"),"",JOptionPane.QUESTION_MESSAGE);
				if ((s!=null) && (!s.equals(""))) {
					if (layer instanceof VectorLayer) {
						try {
							Table newtable=new Table();
							newtable.addField(MapCreator.bundleCreator.getString("newfield"),StringTableField.DATA_TYPE,true,true,false);
							int size=0;
							ArrayList go=layer.getGeographicObjects(false);
							if (go!=null)
								size=go.size();
							else
								size=shapefileInfo.getSize();
							for (int i=size-1;i>-1;i--) {
							//The last one will be added with "false" on moreToBeAdded parameter of commit
								newtable.getRecordEntryStructure().startRecordEntry();
								newtable.getRecordEntryStructure().commitNewRecord((i==0)?false:true);
							}
							LayerInfo.this.creator.getDatabase().addTable(newtable,s,true);
							LayerInfo.this.creator.addTable(newtable);
							table.setSelectedValue(newtable,true);
							setModified(true);
						} catch(Exception ex) {
							JOptionPane.showMessageDialog(LayerInfo.this,MapCreator.bundleCreator.getString("tablenotcreated"),"",JOptionPane.ERROR_MESSAGE);
							System.err.println("MAPCREATOR#LI0002091330: Invalid state! Information message follows. You may continue working.");
							ex.printStackTrace();
						}
					} else { //Instance of RasterLayer
						try {
							Table newtable=((RasterLayer) layer).produceNewTable();
							LayerInfo.this.creator.getDatabase().addTable(newtable,s,true);
							LayerInfo.this.creator.addTable(newtable);
							table.setSelectedValue(newtable,true);
							setModified(true);
						} catch(Exception ex) {
							JOptionPane.showMessageDialog(LayerInfo.this,MapCreator.bundleCreator.getString("tablenotcreated"),"",JOptionPane.ERROR_MESSAGE);
							System.err.println("MAPCREATOR#LI0104091506: Invalid state! Information message follows. You may continue working.");
							ex.printStackTrace();
						}
					}
				}
			}
		});

		//OK button listener
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (applyChanges())
					setVisible(false);
			}
		});

		//Cancel button listener
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				if (firstTimeShown)
					return;
				setTitle(bName);
				name.setText(bName);
				modified=bModified;
				if (bShapefileInfo!=null)
					shapefile.setText(bShapefileInfo.getPath());
				else
					shapefile.setText(MapCreator.bundleCreator.getString("mustdefshapefile"));
				shapefileInfo=bShapefileInfo;
				if (shapefileInfo==null)
					shapetype.setText(" ");
				else
					shapetype.setText(shapefileInfo.getSize()+" "+MapCreator.bundleCreator.getString("objects")+" "+MapCreator.bundleCreator.getString(""+shapefileInfo.getShapeType()));
				buildTableList();
				setLocalcTable(bTable);
				if (cTable!=null)
					table.setSelectedValue(cTable,true);
				buildFieldCombos();
				tooltipField=bTooltip;
				if (tooltipField!=null)
					tooltip.setSelectedItem(tooltipField);
				labelField=bLabel;
				if (labelField!=null)
					label.setSelectedItem(labelField);
				fromField=bFrom;
				if (fromField!=null)
					cbxFrom.setSelectedItem(fromField);
				toField=bTo;
				if (toField!=null)
					cbxTo.setSelectedItem(toField);
				editable.setSelected(bEditable);
				visible.setSelected(bVisible);
				showNotSelected.setSelected(bShowNotSelected);
				selectObjects.setSelected(bSelectObjects);
				normalOutline.setBackground(bNormalOutline);
				normalFill.setBackground(bNormalFill);
				selectedOutline.setBackground(bSelectedOutline);
				selectedFill.setBackground(bSelectedFill);
				highlightedOutline.setBackground(bHighlightedOutline);
				highlightedFill.setBackground(bHighlightedFill);
				sldrNormalOutline.setValue(bAlphaNormalOutline);
				sldrNormalFill.setValue(bAlphaNormalFill);
				sldrSelectedOutline.setValue(bAlphaSelectedOutline);
				sldrSelectedFill.setValue(bAlphaSelectedFill);
				sldrHighlightedOutline.setValue(bAlphaHighlightedOutline);
				sldrHighlightedFill.setValue(bAlphaHighlightedFill);
				enableVisible.setSelected(bEnableVisible);
				enableShowNotSelected.setSelected(bEnableShowNotSelected);
				showBlankRecord.setSelected(bShowBlankRecord);
				longBL.setText(bLongBL);
				latBL.setText(bLatBL);
				longTR.setText(bLongTR);
				latTR.setText(bLatTR);
				applyChanges();
			}
		});

		//Apply button listener
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyChanges();
				apply.setEnabled(false);
			}
		});

		//Editable checkbox.
		editable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
			}
		});

		//Visible checkbox.
		visible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
			}
		});

		//ShowNotSelected checkbox.
		showNotSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
			}
		});

		//SelectObjects checkbox.
		selectObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a=JOptionPane.showConfirmDialog(LayerInfo.this,MapCreator.bundleCreator.getString("propagateobjsel"),null,JOptionPane.YES_NO_OPTION);
				if (a==JOptionPane.YES_OPTION)
					propagateObjSel=true;
				else
					propagateObjSel=false;
				setModified(true);
			}
		});

		//enableVisible checkbox.
		enableVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
			}
		});

		//enableShowNotSelected checkbox.
		enableShowNotSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
			}
		});

		//showBlankRecord checkbox.
		showBlankRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setModified(true);
			}
		});

		//Modified listener
		longBL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
			}
		});

		//Modified listener
		latBL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
			}
		});

		//Modified listener
		longTR.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
			}
		});

		//Modified listener
		latTR.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				setModified(true);
			}
		});
		backUp();
	}

	private class DamnJBuilderDB extends DatabaseAdapter {
		public void tableAdded(TableAddedEvent e) {
			buildTableList();
		}
		public void tableRemoved(TableRemovedEvent e) {
			buildTableList();
		}
	};

	private class DamnJBuilderRename extends TableModelAdapter {
		public void tableRenamed(TableRemovedEvent e) {
			table.repaint();
		}
	}

	private class DamnJBuilderTM extends DatabaseTableModelAdapter {
		public void columnAdded(ColumnAddedEvent e) {
			//To rebuild property dialogs
			showShapeFileInfo();
			buildFieldCombos();
		}
		public void columnRemoved(ColumnRemovedEvent e) {
			//To rebuild property dialogs
			showShapeFileInfo();
			if (labelField!=null && e.getColumnName().equals(labelField.getName()))
				labelField=null;
			if (tooltipField!=null && e.getColumnName().equals(tooltipField.getName()))
				tooltipField=null;
			if (fromField!=null && e.getColumnName().equals(fromField.getName()))
				fromField=null;
			if (toField!=null && e.getColumnName().equals(toField.getName()))
				toField=null;
			buildFieldCombos();
		}
		public void columnRenamed(ColumnRenamedEvent e) {
			//To rebuild property dialogs
			showShapeFileInfo();
			buildFieldCombos();
		}
		public void columnTypeChanged(ColumnTypeChangedEvent e) {
			buildFieldCombos();
		}
	}
	/**
	 * Action when the apply button is pressed.
	 * @return True, when changes were applied successfuly.
	 */
	private boolean applyChanges() {
		setCursor(Helpers.waitCursor);
		if (name.getText().equals("")) {
			JOptionPane.showMessageDialog(LayerInfo.this,MapCreator.bundleCreator.getString("definename"),"",JOptionPane.ERROR_MESSAGE);
			setCursor(Helpers.normalCursor);
			return false;
		}
		if (shapefileInfo==null) {
			JOptionPane.showMessageDialog(LayerInfo.this,MapCreator.bundleCreator.getString("mustdefshapefile"),"",JOptionPane.ERROR_MESSAGE);
			setCursor(Helpers.normalCursor);
			return false;
		}
		if (firstTimeShown) {
			LayerInfo.this.creator.addLayer(LayerInfo.this);
			firstTimeShown=false;
		}

		//Apply changes
		layer.setName(name.getText());
		try {
			layer.setTable(cTable);
		} catch(InvalidLayerDataException e) {
			System.err.println("MAP#200005181303: Invalid table set to the layer.");
		}
		layer.setTipBase(tooltipField);
		layer.setLabelBase(labelField);
		layer.setDateFromBase(fromField);
		layer.setDateToBase(toField);
		layer.setEditable(editable.isSelected());
		layer.setDefaultVisibility(visible.isSelected());
		layer.setHideUnselected(!showNotSelected.isSelected());
		layer.setCanSelectObjects(selectObjects.isSelected(),propagateObjSel);
		layer.setHideable(enableVisible.isSelected());
		layer.setCanHideUnselected(enableShowNotSelected.isSelected());
		layer.setShowBlankRecordObjects(showBlankRecord.isSelected());
		if (layer instanceof VectorLayer) {
			VectorLayer vlayer=(VectorLayer) layer;
			vlayer.setNormalOutlineColor(new Color(normalOutline.getBackground().getRed(),normalOutline.getBackground().getGreen(),normalOutline.getBackground().getBlue(),(int) ((100-sldrNormalOutline.getValue())*255f/100)));
			vlayer.setNormalFillColor(new Color(normalFill.getBackground().getRed(),normalFill.getBackground().getGreen(),normalFill.getBackground().getBlue(),(int) ((100-sldrNormalFill.getValue())*255f/100)));
			vlayer.setSelectedOutlineColor(new Color(selectedOutline.getBackground().getRed(),selectedOutline.getBackground().getGreen(),selectedOutline.getBackground().getBlue(),(int) ((100-sldrSelectedOutline.getValue())*255f/100)));
			vlayer.setSelectedFillColor(new Color(selectedFill.getBackground().getRed(),selectedFill.getBackground().getGreen(),selectedFill.getBackground().getBlue(),(int) ((100-sldrSelectedFill.getValue())*255f/100)));
			vlayer.setHighlightedOutlineColor(new Color(highlightedOutline.getBackground().getRed(),highlightedOutline.getBackground().getGreen(),highlightedOutline.getBackground().getBlue(),(int) ((100-sldrHighlightedOutline.getValue())*255f/100)));
			vlayer.setHighlightedFillColor(new Color(highlightedFill.getBackground().getRed(),highlightedFill.getBackground().getGreen(),highlightedFill.getBackground().getBlue(),(int) ((100-sldrHighlightedFill.getValue())*255f/100)));
		} else { //Instance of RasterLayer
			RasterLayer rLayer=(RasterLayer) layer;
			try {
				double minLon=(new Double(longBL.getText())).doubleValue();
				double minLat=(new Double(latBL.getText())).doubleValue();
				double maxLon=(new Double(longTR.getText())).doubleValue();
				double maxLat=(new Double(latTR.getText())).doubleValue();
				Rectangle2D.Double r=new Rectangle.Double(minLon,minLat,maxLon-minLon,maxLat-minLat);
				rLayer.setBoundingRect(r);
			} catch(NumberFormatException e) {
				//Do nothing. The rectangle is not valid and will disappear.
			}
			rLayer.setNormalViewTransparencyLevel((int) (255-sldrNormalOutline.getValue()*255/100d));
			rLayer.setSelectedViewTransparencyLevel((int) (255-sldrSelectedOutline.getValue()*255/100d));
			rLayer.setHighlightedViewTransparencyLevel((int) (255-sldrHighlightedOutline.getValue()*255/100d));
		}
		if (propertiesModified) {
			propertiesModified=false;
			if (propertyFrame instanceof PointLayerProperties) {
				PointLayerProperties lp=(PointLayerProperties) propertyFrame;
				PointLayer l=(PointLayer) layer;
				l.setCircleRadius(lp.getCircleRadius());
				l.setCircleFilled(lp.isCircleFilled());
				l.clearIcons();
				l.setSingleNormalIcon(lp.getNormalIcon());
				l.setSingleSelectedIcon(lp.getSelectedIcon());
				l.setSingleHighlightedIcon(lp.getHighlightedIcon());
				MyTableModel tm=lp.getTableModel();
				for (int i=0;i<tm.getRowCount();i++) {
					if (tm.getValueAt(i,0)!=null) {
						if (lp.getIconBase().getDataType()==DoubleTableField.DATA_TYPE && tm.getValueAt(i,0).getClass().equals(String.class)) {
							try {
								l.addNormalIcon(new Double((String) tm.getValueAt(i,0)),(Icon) tm.getValueAt(i,1));
								l.addSelectedIcon(new Double((String) tm.getValueAt(i,0)),(Icon) tm.getValueAt(i,2));
								l.addHighlightedIcon(new Double((String) tm.getValueAt(i,0)),(Icon) tm.getValueAt(i,3));
							} catch(Throwable t) {/*NumberFormatException*/}
						} else {
							l.addNormalIcon(tm.getValueAt(i,0),(Icon) tm.getValueAt(i,1));
							l.addSelectedIcon(tm.getValueAt(i,0),(Icon) tm.getValueAt(i,2));
							l.addHighlightedIcon(tm.getValueAt(i,0),(Icon) tm.getValueAt(i,3));
						}
					}
				}
				//Finally set the paint mode. If we set it earlier the event produced
				//will have incorrect attributes, e.g. circle radius.
				if (lp.isCircleSelected())
					l.setPaintMode(IPointLayerView.PAINT_AS_CIRCLE);
				else if (lp.isSameIconsSelected())
					l.setPaintMode(IPointLayerView.PAINT_AS_SAME_ICONS);
				else if (lp.isMultipleIconsUnboundSelected()) {
					try {
						if (l.getIconBase()==null || !l.getIconBase().isHidden()) {
							AbstractTableField ctf=l.getTable().getTableField(PointLayer.ICON_ID);
							l.setIconBase(ctf);
						}
						l.setPaintMode(IPointLayerView.PAINT_AS_MULTIPLE_ICONS);
					} catch(Throwable e) {
						try {
							l.setIconBase(l.getTable().addField(PointLayer.ICON_ID,StringTableField.DATA_TYPE,true,true,true));
							l.setPaintMode(IPointLayerView.PAINT_AS_MULTIPLE_ICONS);
						} catch(Throwable t1){
							System.err.println("MAP#200009051551: Cannot set multiple icon view.");
							l.setPaintMode(IPointLayerView.PAINT_AS_CIRCLE);
						}
					}
				} else if (lp.isMultipleIconsSelected()) {
					l.setPaintMode(IPointLayerView.PAINT_AS_MULTIPLE_ICONS);
					l.setIconBase(lp.getIconBase());
				}
			} else if (propertyFrame instanceof PolyLineLayerProperties) {
				PolyLineLayerProperties lp=(PolyLineLayerProperties) propertyFrame;
				PolyLineLayer l=(PolyLineLayer) layer;
				l.setLineWidth(lp.getLineWidth());
				switch (lp.cmbAntialias.getSelectedIndex()) {
					case 1:
						l.setAntialiasState(1);
						break;
					case 2:
						l.setAntialiasState(0);
						break;
					default:
						l.setAntialiasState(-1);
				}
				try {
					l.setErrorTolerance((new Float(lp.txtErrorTol.getText())).floatValue());
				} catch(Throwable ex) {
					l.setErrorTolerance(-1);
				}
				//Finally set the paint mode. If we set it earlier the event produced
				//will have incorrect attributes, e.g. circle radius.
				if (lp.isStraightLineSelected())
					l.setPaintMode(IPolyLineLayerView.PAINT_AS_STRAIGHT_LINE);
				else if (lp.isDashedLineSelected())
					l.setPaintMode(IPolyLineLayerView.PAINT_AS_DASHED_LINE);
				else if (lp.isDottedLineSelected())
					l.setPaintMode(IPolyLineLayerView.PAINT_AS_DOTTED_LINE);
			} else if (propertyFrame instanceof PolygonLayerProperties) {
				PolygonLayerProperties lp=(PolygonLayerProperties) propertyFrame;
				PolygonLayer l=(PolygonLayer) layer;
				l.setLineWidth(lp.getLineWidth());
				l.setPolygonFilled(lp.isPolygonFilled());
				switch (lp.cmbAntialias.getSelectedIndex()) {
					case 1:
						l.setAntialiasState(1);
						break;
					case 2:
						l.setAntialiasState(0);
						break;
					default:
						l.setAntialiasState(-1);
				}
				try {
					l.setErrorTolerance((new Float(lp.txtErrorTol.getText())).floatValue());
				} catch(Throwable ex) {
					l.setErrorTolerance(-1);
				}
				//Finally set the paint mode. If we set it earlier the event produced
				//will have incorrect attributes, e.g. circle radius.
				if (lp.isStraightLineSelected())
					l.setPaintMode(IPolygonLayerView.PAINT_AS_STRAIGHT_LINE);
				else if (lp.isDashedLineSelected())
					l.setPaintMode(IPolygonLayerView.PAINT_AS_DASHED_LINE);
				else if (lp.isDottedLineSelected())
					l.setPaintMode(IPolygonLayerView.PAINT_AS_DOTTED_LINE);
			}
		}

		LayerInfo.this.creator.repaint();
		setCursor(Helpers.normalCursor);
		backUp();
		return true;
	}
	/**
	 * Implements the ActionListener for the color buttons.
	 */
	private class ColorActionListener implements ActionListener {
		private int type;
		private JButton button;
		ColorActionListener(int type,JButton button) {
			super();
			this.type=type;
			this.button=button;
		}
		public void actionPerformed(ActionEvent e) {
			Color cl=JColorChooser.showDialog(LayerInfo.this,MapCreator.bundleCreator.getString("colors"),button.getBackground());
			if (cl!=null) {
				button.setBackground(cl);
				button.setForeground(getProperColor(cl));
				setModified(true);
			}
		}
	};
	/**
	 * Version of a JSlider that paints its value as a percentage in the middle.
	 */
	private class MyJSlider extends JSlider {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			String mes=getValue()+" %";
			int mw=getFontMetrics(getFont()).stringWidth(mes);
			int mh=getFontMetrics(getFont()).getAscent();
			g.setColor(new Color(255,255,255,175));
			g.fillRect((getWidth()-mw)/2,(getHeight()-mh)/2,mw,mh);
			g.setColor(Color.black);
			g.drawString(mes,(getWidth()-mw)/2,(getHeight()+mh)/2-2);
		}
	};
	/**
	 * Implements the ChangeListener for the alpha sliders.
	 */
	private class AlphaChangeListener implements ChangeListener {
		private JSlider slider;
		AlphaChangeListener(JSlider slider) {
			super();
			this.slider=slider;
		}
		public void stateChanged(ChangeEvent e) {
			setModified(true);
			slider.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+slider.getValue()+"%");
			slider.repaint();
		}
	};
	private class AlphaMouseListener extends MouseAdapter {
		private JSlider slider;
		private int initial;
		private int reshow;
		AlphaMouseListener(JSlider slider) {
			super();
			this.slider=slider;
		}
		public void mouseEntered(MouseEvent e) {
			initial=ToolTipManager.sharedInstance().getInitialDelay();
			reshow=ToolTipManager.sharedInstance().getReshowDelay();
			slider.setToolTipText(MapCreator.bundleCreator.getString("transparency")+": "+slider.getValue()+"%");
			ToolTipManager.sharedInstance().setInitialDelay(1);
			ToolTipManager.sharedInstance().setReshowDelay(1);
		}
		public void mouseExited(MouseEvent e) {
			ToolTipManager.sharedInstance().setInitialDelay(initial);
			ToolTipManager.sharedInstance().setReshowDelay(reshow);
		}
	};
	/**
	 * Calculates a proper foreground color for the given background color.
	 */
	private Color getProperColor(Color c) {
		if ((c.getRed()+c.getGreen()+c.getBlue())/3>128)
			return Color.black;
		else
			return Color.white;
	}
	/**
	 * ActionListener for property button on point layers.
	 */
	private class PointLayerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setCursor(Helpers.waitCursor);
			((PointLayerProperties) propertyFrame).setTable(cTable);
			((PointLayerProperties) propertyFrame).setLayer((PointLayer) layer);
			propertyFrame.setVisible(true);
			setModified(true);
			propertiesModified=true;
			setCursor(Helpers.normalCursor);
		}
	};
	/**
	 * ActionListener for property button on polyLine layers.
	 */
	private class PolyLineLayerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setCursor(Helpers.waitCursor);
			((PolyLineLayerProperties) propertyFrame).setLayer((PolyLineLayer) layer);
			propertyFrame.setVisible(true);
			setModified(true);
			propertiesModified=true;
			setCursor(Helpers.normalCursor);
		}
	};
	/**
	 * ActionListener for property button on polygon layers.
	 */
	private class PolygonLayerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setCursor(Helpers.waitCursor);
			((PolygonLayerProperties) propertyFrame).setLayer((PolygonLayer) layer);
			propertyFrame.setVisible(true);
			setModified(true);
			propertiesModified=true;
			setCursor(Helpers.normalCursor);
		}
	};
	/**
	 * Shows shapefile information.
	 */
	private void showShapeFileInfo() {
		if (shapefileInfo==null)
			return;
		if (layer==null) {
			//Check whether the shapefile info is a valid URL or a user defined layer.
			try {
				new URL(shapefileInfo.getPath());
				try {
					if (shapefileInfo.getShapeType()==ShapeFileInfo.RASTER_LAYER) {
						URL gif=new URL(shapefileInfo.getPath());
						layer=new RasterLayer(gif,cTable);
						sldrNormalOutline.setValue((int) (100-((RasterLayer) layer).getNormalViewTransparencyLevel()*100/255d));
						sldrSelectedOutline.setValue((int) (100-((RasterLayer) layer).getSelectedViewTransparencyLevel()*100/255d));
						sldrHighlightedOutline.setValue((int) (100-((RasterLayer) layer).getHighlightedViewTransparencyLevel()*100/255d));
					} else { //it is a vector layer
						URL shp=new URL(shapefileInfo.getPath());
						URL shx=new URL(shapefileInfo.getPath().substring(0,shapefileInfo.getPath().length()-1)+"x");
						if (shapefileInfo.getShapeType()==ShapeFileInfo.POINT_LAYER)
							layer=new PointLayer(shp,shx,cTable,creator.map.getDataPrecision());
						else if (shapefileInfo.getShapeType()==ShapeFileInfo.POLYLINE_LAYER)
							layer=new PolyLineLayer(shp,shx,cTable,creator.map.getDataPrecision());
						else if (shapefileInfo.getShapeType()==ShapeFileInfo.POLYGON_LAYER)
							layer=new PolygonLayer(shp,shx,cTable,creator.map.getDataPrecision());
					}
				} catch(Exception ex) {
					System.err.println("MAP#2000030710000: Error constructing layer! Information message follows:");
					ex.printStackTrace();
					setCursor(Helpers.normalCursor);
					throw new RuntimeException("Cannot create layer!");
				}
			} catch(MalformedURLException mue) {
				if (shapefileInfo.getShapeType()==ShapeFileInfo.POINT_LAYER)
					layer=new PointLayer();
				else if (shapefileInfo.getShapeType()==ShapeFileInfo.POLYLINE_LAYER)
					layer=new PolyLineLayer();
				else if (shapefileInfo.getShapeType()==ShapeFileInfo.POLYGON_LAYER)
					layer=new PolygonLayer();
				else if (shapefileInfo.getShapeType()==ShapeFileInfo.RASTER_LAYER)
					layer=new RasterLayer();
				if (cTable!=null)
					try {
						layer.setTable(cTable);
					} catch(InvalidLayerDataException ex) {
						System.err.println("MAP#200004201428: Cannot set the table to the new layer.");
					}

			}
			if (layer!=null && layer.getViews().size()>0)
				((LayerView) layer.getViews().get(0)).addLayerListener(layerList);
		}

		shapefileInfo.size=layer.getObjectCount();

		//Customize control depending on the layer type
		if (shapefileInfo.getShapeType()==ShapeFileInfo.RASTER_LAYER) {
			convertToRasterLayerInfo();
		} else {
			colorsLabel.setText(MapCreator.bundleCreator.getString("colors"));
			if (shapefileInfo.getShapeType()==ShapeFileInfo.POINT_LAYER) {
				editable.setEnabled(true);
			} else {
				editable.setSelected(false);
				editable.setEnabled(false);
			}
		}
		shapefile.setText(shapefileInfo.getPath());
		shapetype.setText(shapefileInfo.getSize()+" "+MapCreator.bundleCreator.getString("objects")+" "+MapCreator.bundleCreator.getString(""+shapefileInfo.getShapeType()));
		browse.setEnabled(false);
		deftype.setEnabled(false);
		property.setEnabled(true);
		Dimension s=Toolkit.getDefaultToolkit().getScreenSize();
		if (shapefileInfo.getShapeType()==ShapeFileInfo.POINT_LAYER) {
			property.removeActionListener(propertyListener);
			propertyFrame=new PointLayerProperties(creator.map);
			propertyFrame.setLocation((s.width-propertyFrame.getWidth())/2,(s.height-propertyFrame.getHeight())/2);
			property.addActionListener(propertyListener=new PointLayerActionListener());
		} else if (shapefileInfo.getShapeType()==ShapeFileInfo.POLYLINE_LAYER) {
			property.removeActionListener(propertyListener);
			propertyFrame=new PolyLineLayerProperties();
			propertyFrame.setLocation((s.width-propertyFrame.getWidth())/2,(s.height-propertyFrame.getHeight())/2);
			property.addActionListener(propertyListener=new PolyLineLayerActionListener());
		} else if (shapefileInfo.getShapeType()==ShapeFileInfo.POLYGON_LAYER) {
			property.removeActionListener(propertyListener);
			propertyFrame=new PolygonLayerProperties();
			propertyFrame.setLocation((s.width-propertyFrame.getWidth())/2,(s.height-propertyFrame.getHeight())/2);
			property.addActionListener(propertyListener=new PolygonLayerActionListener());
		} else if (shapefileInfo.getShapeType()==ShapeFileInfo.RASTER_LAYER) {
			property.removeActionListener(propertyListener);
			property.setEnabled(false);
		}
	}
	/**
	 * Changes the frame into a raster layer info frame.
	 */
	private void convertToRasterLayerInfo() {
		editable.setSelected(false);
		editable.setEnabled(false);
		selectObjects.setSelected(false);
		selectObjects.setEnabled(false);
		colorsLabel.setText(MapCreator.bundleCreator.getString("rastertransp"));
		normalOutline.setVisible(false);
		normalFill.setVisible(false);
		selectedOutline.setVisible(false);
		selectedFill.setVisible(false);
		highlightedOutline.setVisible(false);
		highlightedFill.setVisible(false);
		sldrNormalFill.setVisible(false);
		sldrSelectedFill.setVisible(false);
		sldrHighlightedFill.setVisible(false);
		label.setVisible(false);
		labelLabel.setVisible(false);
		lblTime.setVisible(false);
		cbxFrom.setVisible(false);
		cbxTo.setVisible(false);
		if (layer==null)
			showNotSelected.setSelected(true);

		this.getContentPane().add(sldrNormalOutline, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.EdgeAnchor(normalLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.EdgeAnchor(normalLabel, Anchor.Left, Anchor.Same, Anchor.Left, 0),
									new com.thwt.layout.EdgeAnchor(normalLabel, Anchor.Width, Anchor.Same, Anchor.Width, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrSelectedOutline, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.EdgeAnchor(selectedLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.EdgeAnchor(selectedLabel, Anchor.Left, Anchor.Same, Anchor.Left, 0),
									new com.thwt.layout.EdgeAnchor(selectedLabel, Anchor.Width, Anchor.Same, Anchor.Width, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrHighlightedOutline, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.EdgeAnchor(highlightedLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.EdgeAnchor(highlightedLabel, Anchor.Left, Anchor.Same, Anchor.Left, 0),
									new com.thwt.layout.EdgeAnchor(highlightedLabel, Anchor.Width, Anchor.Same, Anchor.Width, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(editable, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(longTR, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
									new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.6, Anchor.Right, Anchor.Width, 0.0, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(coordsLabel, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(sldrNormalOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(longBLLabel, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(coordsLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
									new com.thwt.layout.EdgeAnchor(longBL, Anchor.Height, Anchor.Same, Anchor.Height, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(longBL, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.FixedDimensionAnchor(Anchor.Height, 20),
									new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
									new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -3)));
		this.getContentPane().add(latBLLabel, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
									new com.thwt.layout.EdgeAnchor(latBL, Anchor.Height, Anchor.Same, Anchor.Height, 0),
									new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(latBL, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
									new com.thwt.layout.EdgeAnchor(latBLLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(longTRLabel, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(longBLLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.EdgeAnchor(longTR, Anchor.Height, Anchor.Same, Anchor.Height, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(longTR, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.EdgeAnchor(longTRLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(longBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -3),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(latTRLabel, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.EdgeAnchor(latBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.EdgeAnchor(latTR, Anchor.Height, Anchor.Same, Anchor.Height, 0),
									new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 3),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0)));
		this.getContentPane().add(latTR, new com.thwt.layout.LayoutConstraint(
									new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
									new com.thwt.layout.EdgeAnchor(latTRLabel, Anchor.Right, Anchor.Right, Anchor.Left, 5),
									new com.thwt.layout.EdgeAnchor(latBL, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
									new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}
	/**
	 * Removes the rename listener from the tables in the table model.
	 */
	private void removeRenameListener() {
		for (int i=0;i<tableModel.getSize();i++)
			((Table) tableModel.elementAt(i)).removeTableModelListener(tableRename);
	}
	/**
	 * Builds the table list.
	 */
	private void buildTableList() {
		if (creator.getDatabase()==null) {
			tableModel.removeAllElements();
			tooltip.removeAllItems();
			label.removeAllItems();
			cbxFrom.removeAllItems();
			cbxTo.removeAllItems();
			newTable.setEnabled(false);
			return;
		}
		newTable.setEnabled(true);
		ArrayList tables=creator.getDatabase().getTables();
		tableModel.removeAllElements();
		//Build the list only if there is a shapefile
		if (shapefileInfo==null)
			return;

		int ts=tables.size();
		//First put the equal number of records tables
		for (int i=0;i<ts;i++)
			if (((Table) tables.get(i)).getRecordCount()==shapefileInfo.getSize())
				tableModel.addElement(tables.get(i));
		//Then put the greater number of records tables
		for (int i=0;i<ts;i++)
			if (((Table) tables.get(i)).getRecordCount()>shapefileInfo.getSize())
				tableModel.addElement(tables.get(i));
		if (layer.getTable()!=null)
			table.setSelectedValue(layer.getTable(),true);
	}
	/**
	 * Builds the field combos putting the field names.
	 */
	private void buildFieldCombos() {
		Object tt=tooltip.getSelectedItem();
		Object lb=label.getSelectedItem();
		Object df=cbxFrom.getSelectedItem();
		Object dt=cbxTo.getSelectedItem();
		tooltip.removeAllItems();
		label.removeAllItems();
		cbxFrom.removeAllItems();
		cbxTo.removeAllItems();
		if (table.getSelectedIndex()!=-1) {
			tooltip.addItem("none"); //The "null" object.
			label.addItem("none"); //The "null" object.
			cbxFrom.addItem("none"); //The "null" object.
			cbxTo.addItem("none"); //The "null" object.
			TableFieldBaseArray f=((Table) tableModel.elementAt(table.getSelectedIndex())).getFields();
			for (int i=0;i<f.size();i++) {
				tooltip.addItem(f.get(i));
				label.addItem(f.get(i));
				AbstractTableField tf = (AbstractTableField) f.get(i);
				if (tf.getDataType()==DateTableField.DATA_TYPE) {
					cbxFrom.addItem(f.get(i));
					cbxTo.addItem(f.get(i));
				}
			}
			if (tt!=null && !(tt instanceof String))
				tooltip.setSelectedItem(tt);
			else
				tooltip.setSelectedIndex(0);
			if (lb!=null && !(lb instanceof String))
				label.setSelectedItem(lb);
			else
				label.setSelectedIndex(0);
			if (df!=null && !(df instanceof String))
				cbxFrom.setSelectedItem(df);
			else
				cbxFrom.setSelectedIndex(0);
			if (dt!=null && !(tt instanceof String))
				cbxTo.setSelectedItem(tt);
			else
				cbxTo.setSelectedIndex(0);
		}
	}
	/**
	 * The cell renderer for the table list.
	 */
	private class TableCellRenderer extends JLabel implements ListCellRenderer {
		Color cyan=new Color(198,255,198);
		TableCellRenderer() {
			setHorizontalAlignment(SwingConstants.LEFT);
			setBorder(new CompoundBorder(new EmptyBorder(1,1,1,1),new LineBorder(new Color(225,225,225))));
			setHorizontalTextPosition(SwingConstants.LEFT);
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
			Table t=(Table) value;
			if (t==null || shapefileInfo==null) {
				System.err.println("MAPCREATOR#200008291526: Debug the LayerInfo UI.");
				setBackground(Color.white);
				setText("");
				return this;
			}
			setBackground(isSelected ? cyan : LayerInfo.this.getBackground());
			if (t.getRecordCount()==shapefileInfo.getSize())
				setForeground(new Color(255,120,0));
			else
				setForeground(Color.gray);
			setText(t.getTitle()+" ("+t.getRecordCount()+" "+MapCreator.bundleCreator.getString("records")+")");
			return this;
		}
	}
	/**
	 * The cell renderer for the table list.
	 */
	static class ComboRenderer extends JLabel implements ListCellRenderer {
		Color cyan=new Color(198,255,198);
		ComboRenderer() {
			setHorizontalAlignment(SwingConstants.LEFT);
			setHorizontalTextPosition(SwingConstants.LEFT);
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus) {
			if (value!=null && value instanceof AbstractTableField)
				setText(((AbstractTableField) value).getName());
			else
				setText(MapCreator.bundleCreator.getString("none"));
			return this;
		}
	}
	/**
	 * Sets the modification flag.
	 */
	protected void setModified(boolean value) {
		modified=value;
		if (value)
			modifiedLabel.setForeground(Color.red);
		else
			modifiedLabel.setForeground(Color.gray);
		apply.setEnabled(value);
	}
	/**
	 * Gets the modified flag.
	 */
	protected boolean isModified() {
		return modified;
	}
	/**
	 * Keeps a backup of values as well as showing or hiding the dialog.
	 */
	public void setVisible(boolean value) {
		if (value) {
			backUp();
			showShapeFileInfo();
		}
		if (!value && layer!=null && layer.getViews().size()>0)
			((LayerView) layer.getViews().get(0)).removeLayerListener(layerList);
		super.setVisible(value);
	}
	/**
	 * @return The Table associated with this layer.
	 */
	protected Table getTable() {
		return cTable;
	}
	/**
	 * @return The Shapefile associated with this layer information.
	 */
	protected ShapeFileInfo getShapefileInfo() {
		return shapefileInfo;
	}
	/**
	 * Keeps a backup of values as well as showing the dialog.
	 */
	public void show() {
		backUp();
		super.show();
	}

	public void dispose() {
		setVisible(false);
		super.dispose();
	}

	private void backUp() {
		bName=name.getText();
		bModified=modified;
		bShapefileInfo=shapefileInfo;
		bTable=cTable;
		bTooltip=tooltipField;
		bLabel=labelField;
		bFrom=fromField;
		bTo=toField;
		bEditable=editable.isSelected();
		bVisible=visible.isSelected();
		bShowNotSelected=showNotSelected.isSelected();
		bSelectObjects=selectObjects.isSelected();
		bNormalOutline=normalOutline.getBackground();
		bNormalFill=normalFill.getBackground();
		bSelectedOutline=selectedOutline.getBackground();
		bSelectedFill=selectedFill.getBackground();
		bHighlightedOutline=highlightedOutline.getBackground();
		bHighlightedFill=highlightedFill.getBackground();
		bAlphaNormalOutline=sldrNormalOutline.getValue();
		bAlphaNormalFill=sldrNormalFill.getValue();
		bAlphaSelectedOutline=sldrSelectedOutline.getValue();
		bAlphaSelectedFill=sldrSelectedFill.getValue();
		bAlphaHighlightedOutline=sldrHighlightedOutline.getValue();
		bAlphaHighlightedFill=sldrHighlightedFill.getValue();
		bEnableVisible=enableVisible.isSelected();
		bEnableShowNotSelected=enableShowNotSelected.isSelected();
		bShowBlankRecord=showBlankRecord.isSelected();
		bLongBL=longBL.getText();
		bLatBL=latBL.getText();
		bLongTR=longTR.getText();
		bLatTR=latTR.getText();
	}

	public String getName() {
		return name.getText();
	}
	/**
	 * Adds listener for the selected Table. Looks for changes that should appear in the
	 * comboboxes of the LayerInfo frame.
	 */
	private void setLocalcTable(Table t) {
		if (cTable==t)
			return;
		if (cTable!=null)
			cTable.removeTableModelListener(tmListener);
		cTable=t;
		if (cTable!=null)
			cTable.addTableModelListener(tmListener);
	}
	/**
	 * Checks if the given table is compatible with the existing one, i.e. if the fields associated with
	 * actions are of the same type and name.
	 */
	boolean checkTable(Table table) {
		boolean tt,lb,df,dt,tb;
		if ((table!=null && cTable==null) || (table==null && cTable!=null))
			return false;
		tt=(tooltipField==null);
		lb=(labelField==null);
		df=(fromField==null);
		dt=(toField==null);
		//Changing a table "asynchronously" can be done by changing the database from the map info pane.
		//If the table has similar data they will be kept.
		if (table!=null && cTable!=null && table.getTitle().equals(cTable.getTitle()) && table.getRecordCount()>=cTable.getRecordCount()) {
			for (int i=0;i<table.getFieldCount();i++)
				try {
					//This damn TableField throws NullPointerException on equals!
					if (tooltipField!=null && table.getTableField(i).getName().equals(tooltipField.getName()))
						tt=true;
					if (labelField!=null && table.getTableField(i).getName().equals(labelField.getName()))
						lb=true;
					if (fromField!=null && table.getTableField(i).getName().equals(fromField.getName()) && table.getTableField(i).getDataType()==DateTableField.DATA_TYPE)
						df=true;
					if (toField!=null && table.getTableField(i).getName().equals(toField.getName()) && table.getTableField(i).getDataType()==DateTableField.DATA_TYPE)
						dt=true;
				} catch(InvalidFieldIndexException e) {}
		} else
			return false;
		//Returns true if all the data have been associated correctly.
		return tt && lb && df && dt;
	}

	protected Layer getLayer() {
		return layer;
	}

	void die() {
		removeAll();
		if (layer!=null && dblist!=null)
			creator.getDatabase().removeDatabaseListener(dblist);
	}

	private void jbInit() throws Exception {
		name.setFont(new java.awt.Font(name.getFont().getName(), 1, (int) (name.getFont().getSize()*1.5)));
		name.setForeground(new java.awt.Color(0, 0, 160));
		name.setText("Layer");
		this.getContentPane().setLayout(smartLayout1);
		modifiedLabel.setFont(new java.awt.Font(modifiedLabel.getFont().getName(), 1, (int) (modifiedLabel.getFont().getSize()*1.6)));
		modifiedLabel.setForeground(Color.gray);
		modifiedLabel.setText("*");
		Font lblFont=new java.awt.Font(shapeLabel.getFont().getName(), 1, shapeLabel.getFont().getSize());
		shapeLabel.setFont(lblFont);
		shapeLabel.setText("jLabel1");
		browse.setText("Browse");
		shapefile.setText("Shapefile");
		tableLabel.setFont(lblFont);
		tableLabel.setText("jLabel2");
		tooltipLabel.setFont(lblFont);
		tooltipLabel.setText("jLabel1");
		labelLabel.setFont(lblFont);
		labelLabel.setText("jLabel1");
		labelFont.setText("Aa...");
		labelFont.setForeground(Color.blue);
		labelFont.setFont(new java.awt.Font(shapeLabel.getFont().getName(), Font.ITALIC, shapeLabel.getFont().getSize()));
		editable.setOpaque(false);
		editable.setSelected(true);
		editable.setText("Editable");
		editable.setFont(lblFont);
		visible.setOpaque(false);
		visible.setSelected(true);
		visible.setText("Initialy visible");
		visible.setFont(lblFont);
		newTable.setText("New Table");
		showNotSelected.setOpaque(false);
		showNotSelected.setSelected(true);
		showNotSelected.setText("Show not selected");
		showNotSelected.setFont(lblFont);
		selectObjects.setOpaque(false);
		selectObjects.setSelected(true);
		selectObjects.setText("May select objects");
		selectObjects.setFont(lblFont);
		colorsLabel.setBackground(new java.awt.Color(0, 0, 160));
		colorsLabel.setFont(lblFont);
		colorsLabel.setForeground(Color.yellow);
		colorsLabel.setOpaque(true);
		colorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		colorsLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		colorsLabel.setText("Colors");
		normalLabel.setText("Normal");
		normalOutline.setText("Outline");
		normalFill.setText("Fill");
		selectedLabel.setText("Selected");
		selectedOutline.setText("Outline");
		selectedFill.setText("Fill");
		highlightedLabel.setText("Highlighted");
		highlightedOutline.setText("Outline");
		highlightedFill.setText("Fill");
		enableVisible.setOpaque(false);
		enableVisible.setSelected(true);
		enableVisible.setText("Enable");
		enableVisible.setFont(lblFont);
		enableShowNotSelected.setOpaque(false);
		enableShowNotSelected.setSelected(true);
		enableShowNotSelected.setText("Enable");
		enableShowNotSelected.setFont(lblFont);
		showBlankRecord.setOpaque(false);
		showBlankRecord.setSelected(true);
		showBlankRecord.setText("Show blank record objects");
		showBlankRecord.setFont(lblFont);
		coordsLabel.setFont(lblFont);
		ok.setText("OK");
		cancel.setText("Cancel");
		shapetype.setForeground(new java.awt.Color(255, 120, 0));
		shapetype.setText("jLabel1");
		property.setText("Define icons");
		scrollpane.setBorder(null);
		apply.setText("Apply");
		sldrNormalOutline.setValue(0);
		sldrNormalOutline.setSnapToTicks(true);
		sldrNormalOutline.setMajorTickSpacing(100);
		sldrNormalOutline.setMinorTickSpacing(1);
		sldrNormalOutline.setDoubleBuffered(true);
		sldrNormalOutline.setOpaque(false);
		sldrSelectedOutline.setValue(0);
		sldrSelectedOutline.setMajorTickSpacing(100);
		sldrSelectedOutline.setMinorTickSpacing(1);
		sldrSelectedOutline.setDoubleBuffered(true);
		sldrSelectedOutline.setOpaque(false);
		sldrHighlightedOutline.setValue(0);
		sldrHighlightedOutline.setMajorTickSpacing(100);
		sldrHighlightedOutline.setMinorTickSpacing(1);
		sldrHighlightedOutline.setDoubleBuffered(true);
		sldrHighlightedOutline.setOpaque(false);
		sldrNormalFill.setValue(0);
		sldrNormalFill.setMajorTickSpacing(100);
		sldrNormalFill.setMinorTickSpacing(1);
		sldrNormalFill.setDoubleBuffered(true);
		sldrNormalFill.setOpaque(false);
		sldrSelectedFill.setValue(0);
		sldrSelectedFill.setMajorTickSpacing(100);
		sldrSelectedFill.setMinorTickSpacing(1);
		sldrSelectedFill.setDoubleBuffered(true);
		sldrSelectedFill.setOpaque(false);
		sldrHighlightedFill.setValue(0);
		sldrHighlightedFill.setMajorTickSpacing(100);
		sldrHighlightedFill.setMinorTickSpacing(1);
		sldrHighlightedFill.setDoubleBuffered(true);
		sldrHighlightedFill.setOpaque(false);
		deftype.setText("Type");
		lblTime.setFont(lblFont);
		lblTime.setText("jLabel1");
		this.getContentPane().add(name, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.EdgeAnchor(modifiedLabel, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(modifiedLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(shapeLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(name, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(shapefile, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(browse, Anchor.Left, Anchor.Left, Anchor.Right, 3),
					new com.thwt.layout.EdgeAnchor(shapeLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(tableLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(property, Anchor.Bottom, Anchor.Below, Anchor.Top, -5),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(tooltipLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(tableLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(tooltip, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(tooltipLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(labelLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(tooltip, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(label, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(labelLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
//		this.getContentPane().add(labelFont, new com.thwt.layout.LayoutConstraint(
//					new com.thwt.layout.EdgeAnchor(label, Anchor.Right, Anchor.Right, Anchor.Left, 5),
//					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
//					new com.thwt.layout.EdgeAnchor(labelLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
//					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(colorsLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Bottom, Anchor.Below, Anchor.Top, 10),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(normalLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 6),
					new com.thwt.layout.EdgeAnchor(colorsLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.33, Anchor.Right, Anchor.Width, 0.0, -3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(normalOutline, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 6),
					new com.thwt.layout.EdgeAnchor(normalLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.33, Anchor.Right, Anchor.Width, 0.0, -3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(normalFill, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 6),
					new com.thwt.layout.EdgeAnchor(sldrNormalOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.33, Anchor.Right, Anchor.Width, 0.0, -3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(selectedLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(normalLabel, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(colorsLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(normalLabel, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(selectedOutline, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(selectedLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(selectedFill, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(normalFill, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(sldrSelectedOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(highlightedLabel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(colorsLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.33, Anchor.Right, Anchor.Width, 0.0, -3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(highlightedOutline, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(selectedOutline, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(highlightedLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 3),
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(highlightedFill, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(selectedFill, Anchor.Right, Anchor.Right, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(sldrHighlightedOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(editable, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(sldrNormalFill, Anchor.Bottom, Anchor.Below, Anchor.Top, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalProportion, 0.6, Anchor.Right, Anchor.Width, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(visible, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(editable, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(enableVisible, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Left, Anchor.Left, 0.0, -45),
					new com.thwt.layout.EdgeAnchor(visible, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(showNotSelected, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(selectObjects, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(selectObjects, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(visible, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(enableShowNotSelected, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Left, Anchor.Left, 0.0, -45),
					new com.thwt.layout.EdgeAnchor(showNotSelected, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(showBlankRecord, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(showNotSelected, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(ok, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.EdgeAnchor(cancel, Anchor.Left, Anchor.Left, Anchor.Right, 20),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(cancel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(shapetype, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(shapefile, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(scrollpane, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(tableLabel, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Top, Anchor.Above, Anchor.Bottom, 0),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, 0)));
		this.getContentPane().add(apply, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 10),
					new com.thwt.layout.EdgeAnchor(cancel, Anchor.Right, Anchor.Right, Anchor.Left, 20),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(deftype, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(browse, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(property, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -5, 1.0)));
		this.getContentPane().add(browse, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(shapeLabel, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(property, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -5, 1.0)));
		this.getContentPane().add(property, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(deftype, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -5, 1.0)));
		this.getContentPane().add(newTable, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(cbxFrom, Anchor.Bottom, Anchor.Same, Anchor.Bottom, 0),
					new com.thwt.layout.EdgeAnchor(scrollpane, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, -7, 1.0)));
		this.getContentPane().add(lblTime, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.EdgeAnchor(newTable, Anchor.Right, Anchor.Right, Anchor.Left, 5),
					new com.thwt.layout.EdgeAnchor(label, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(cbxFrom, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(lblTime, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(lblTime, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.FractionAnchor(lblTime, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Right, 0.0, -2),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(cbxTo, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(cbxFrom, Anchor.Top, Anchor.Same, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(lblTime, Anchor.Right, Anchor.Same, Anchor.Right, 0),
					new com.thwt.layout.FractionAnchor(lblTime, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.Left, 0.0, 2),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrNormalOutline, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(normalOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrSelectedOutline, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(selectedOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(selectedOutline, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(selectedOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrHighlightedOutline, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(highlightedOutline, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(highlightedOutline, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(highlightedOutline, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrNormalFill, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(normalFill, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(normalFill, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(normalFill, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrSelectedFill, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(selectedFill, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(selectedFill, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(selectedFill, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.getContentPane().add(sldrHighlightedFill, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.EdgeAnchor(highlightedFill, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
					new com.thwt.layout.EdgeAnchor(highlightedFill, Anchor.Left, Anchor.Same, Anchor.Left, 0),
					new com.thwt.layout.EdgeAnchor(highlightedFill, Anchor.Width, Anchor.Same, Anchor.Width, 0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
	}
}
