package gr.cti.eslate.timeMachine2;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputMultipleOutputPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.sharedObject.DateSO;
import gr.cti.eslate.sharedObject.TimeMachineSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * The Time Machine provides the means to interactivelly define time-periods and
 * time-lines of interest at various time scales (millenia, centuries, days,
 * etc).
 * 
 * @author augril
 */
public class TimeMachine2 extends JPanel implements Externalizable, ESlatePart {
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	/** Used for externalization. */
	private static final int STORAGE_VERSION = 1;
	/** E-Slate component version. */
	private final static String VERSION = "1.0.0";
	/** Save/load constants keys. */
	private static final String SCALE = "scale";
	private static final String LEFT_VISIBLE_YEAR = "leftVisibleYear";
	private static final String LEFT_SELECTED_YEAR = "leftSelectedYear";
	private static final String RIGHT_SELECTED_YEAR = "rightSelectedYear";
	private static final String COLOR_PANEL = "colorPanel";
	private static final String COLOR_RULER = "colorRuler";
	private static final String COLOR_LABELS = "colorLabels";
	private static final String COLOR_CURSOR = "colorCursor";
	/** Zoom step. */
	private static final int ZOOM_STEP = 10;
	/** E-Slate handle. */
	private ESlateHandle handle = null;
    /** The toolbar of the time machine. */
    private JToolBar toolBar;
    /** Pan toggle button. */
    private JToggleButton toggleButtonPan;
    /** Locate button. */
    private JButton buttonLocate;
    /** Scale combo box. */
    private JComboBox comboBoxScale;
    /** Zoom buttons. */
    private JButton buttonZoomIn, buttonZoomOut;
    /** Status panel. */
    private JPanel panelStatus;
    /** Cursor label. */
    private JLabel labelCursor;
    /** Color label. */
    private JLabel labelColor;
    /** Time panel. */
    private PanelTime panelTime;
	/** Media player listeners. */
	private ArrayList<TimeMachine2Listener> timeMachine2Listeners;
    /** Time machine shared objects. */
    private TimeMachineSO soSelectedPeriod;
    private DateSO soSelectedPeriodStart, soSelectedPeriodEnd;
	/** Plugs of time machine. */
    private SingleInputMultipleOutputPlug plugSelectedPeriod, plugSelectedPeriodStart, plugSelectedPeriodEnd;
    /** Mark color. */
    private Color colorMark;
	
	/**
	 * Constructs a new Time machine.
	 */
	public TimeMachine2() {
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
	public TimeMachine2(ObjectInput oi) throws Exception {
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
	 * 
	 * @param oi
	 *            Saved condition.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void initialize(ObjectInput oi) throws IOException,
			ClassNotFoundException {
		 StorageStructure fieldMap = (StorageStructure) oi.readObject();
		 
		 Integer scale = (Integer) fieldMap.get(SCALE);
		 int leftVisibleYear = ((Integer) fieldMap.get(LEFT_VISIBLE_YEAR)).intValue();
		 panelTime.setYearStart(leftVisibleYear);
		 int leftSelectedYear = ((Integer) fieldMap.get(LEFT_SELECTED_YEAR)).intValue();
		 panelTime.getPanelCursor().setYearStart(leftSelectedYear);
		 int rightSelectedYear = ((Integer) fieldMap.get(RIGHT_SELECTED_YEAR)).intValue();
		 panelTime.getPanelCursor().setYearEnd(rightSelectedYear);
		 
		 comboBoxScale.setSelectedItem(scale);
		 setLeftVisibleYear(leftVisibleYear);
		 setLeftSelectedYear(leftSelectedYear);
		 setRightSelectedYear(rightSelectedYear);
		 
		 panelTime.setBackground((Color) fieldMap.get(COLOR_PANEL));
		 panelTime.setColorRuler((Color) fieldMap.get(COLOR_RULER));
		 panelTime.setColorLabels((Color) fieldMap.get(COLOR_LABELS));
		 panelTime.getPanelCursor().setColorCursor((Color) fieldMap.get(COLOR_CURSOR));
		 
		 scaleChanged();
		 
		 Calendar calendar = Calendar.getInstance();
		 calendar.set(leftSelectedYear, 0, 1);
         Date fromDate = calendar.getTime();
		 soSelectedPeriodStart.setDate(fromDate);
		 calendar.set(rightSelectedYear, 11, 31);
		 Date toDate = calendar.getTime();
         soSelectedPeriodEnd.setDate(toDate);
         soSelectedPeriod.setTimePeriod(fromDate, toDate);
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
	 * Initialize the components.
	 */
	private void initializeComponents() {
		setPreferredSize(new Dimension(400, 120));
//		setOpaque(false);
//		setBorder(UIManager.getBorder("InternalFrame.border"));
		
		timeMachine2Listeners = new ArrayList<TimeMachine2Listener>();
		
//        Dimension dimensionButtons = new Dimension(25, 25);
        
        toggleButtonPan = new JToggleButton();
        toggleButtonPan.setIcon(new ImageIcon(getClass().getResource("images/pan.gif")));
        toggleButtonPan.setFocusPainted(false);
        toggleButtonPan.setRequestFocusEnabled(false);
        toggleButtonPan.setFocusable(false);
        toggleButtonPan.setBorderPainted(false);
        toggleButtonPan.setOpaque(false);
        toggleButtonPan.setToolTipText(BundleMessages.getMessage("buttonPan"));
//        toggleButtonPan.setPreferredSize(dimensionButtons);
//        toggleButtonPan.setMaximumSize(dimensionButtons);
//        toggleButtonPan.setMinimumSize(dimensionButtons);
        
        buttonLocate = new JButton();
        buttonLocate.setIcon(new ImageIcon(getClass().getResource("images/locate.gif")));
        buttonLocate.setFocusPainted(false);
        buttonLocate.setRequestFocusEnabled(false);
        buttonLocate.setFocusable(false);
        buttonLocate.setBorderPainted(false);
        buttonLocate.setOpaque(false);
        buttonLocate.setToolTipText(BundleMessages.getMessage("buttonLocate"));
//        buttonLocate.setPreferredSize(dimensionButtons);
//        buttonLocate.setMaximumSize(dimensionButtons);
//        buttonLocate.setMinimumSize(dimensionButtons);
        
        comboBoxScale = new JComboBox();
        comboBoxScale.setRequestFocusEnabled(false);
        comboBoxScale.setFocusable(false);
        comboBoxScale.setOpaque(false);
        comboBoxScale.setToolTipText(BundleMessages.getMessage("comboBoxScale"));
        Dimension dimensionComboBox = new Dimension(64, 24);
        comboBoxScale.setPreferredSize(dimensionComboBox);
        comboBoxScale.setMaximumSize(dimensionComboBox);
        comboBoxScale.setMinimumSize(dimensionComboBox);
        comboBoxScale.setEditable(true);
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) comboBoxScale.getModel();
        defaultComboBoxModel.addElement(10);
        defaultComboBoxModel.addElement(50);
        defaultComboBoxModel.addElement(100);
        defaultComboBoxModel.addElement(200);
        defaultComboBoxModel.addElement(500);
        defaultComboBoxModel.addElement(1000);
        comboBoxScale.setSelectedIndex(2);
        
        buttonZoomIn = new JButton();
        buttonZoomIn.setIcon(new ImageIcon(getClass().getResource("images/zoomIn.gif")));
        buttonZoomIn.setFocusPainted(false);
        buttonZoomIn.setRequestFocusEnabled(false);
        buttonZoomIn.setFocusable(false);
        buttonZoomIn.setBorderPainted(false);
        buttonZoomIn.setOpaque(false);
        buttonZoomIn.setToolTipText(BundleMessages.getMessage("buttonZoomIn"));
//        buttonZoomIn.setPreferredSize(dimensionButtons);
//        buttonZoomIn.setMaximumSize(dimensionButtons);
//        buttonZoomIn.setMinimumSize(dimensionButtons);
        
        buttonZoomOut = new JButton();
        buttonZoomOut.setIcon(new ImageIcon(getClass().getResource("images/zoomOut.gif")));
        buttonZoomOut.setFocusPainted(false);
        buttonZoomOut.setRequestFocusEnabled(false);
        buttonZoomOut.setFocusable(false);
        buttonZoomOut.setBorderPainted(false);
        buttonZoomOut.setOpaque(false);
        buttonZoomOut.setToolTipText(BundleMessages.getMessage("buttonZoomOut"));
//        buttonZoomOut.setPreferredSize(dimensionButtons);
//        buttonZoomOut.setMaximumSize(dimensionButtons);
//        buttonZoomOut.setMinimumSize(dimensionButtons);
        
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setBorderPainted(false);
//		toolBar.setOpaque(false);
//        toolBar.setPreferredSize(new Dimension(64, 32));
        
        panelTime = new PanelTime(this);
        
        panelStatus = new JPanel();
//        panelStatus.setOpaque(false);
        
        final Dimension dimensionLabelColor = new Dimension(16, 16);
        labelColor = new JLabel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g) {
        		super.paintComponent(g);
    			Graphics2D g2 = (Graphics2D) g;
        		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
    			g2.setColor(colorMark);
        		g2.fillOval(2, 2, dimensionLabelColor.width-4, dimensionLabelColor.height-4);
        	}
        };
        labelColor.setPreferredSize(dimensionLabelColor);
        labelColor.setMaximumSize(dimensionLabelColor);
        labelColor.setMinimumSize(dimensionLabelColor);
//        labelColor.setOpaque(true);
//        labelColor.setBackground(Color.RED);
        
        labelCursor = new JLabel();
        labelCursor.setOpaque(true);
        labelCursor.setHorizontalAlignment(SwingConstants.CENTER);
//        Dimension dimensionLabelCursor = new Dimension(64, 16);
//        labelCursor.setPreferredSize(dimensionLabelCursor);
//        labelCursor.setMaximumSize(dimensionLabelCursor);
//        labelCursor.setMinimumSize(dimensionLabelCursor);
		labelCursor.setText(PanelTime.yearLabel(panelTime.getPanelCursor().getYearStart())+" - "+PanelTime.yearLabel(panelTime.getPanelCursor().getYearEnd()));
        
		scaleChanged();
	}

	/**
	 * Layout the components.
	 */
	private void layoutComponents() {
		setLayout(new BorderLayout());

		toolBar.add(toggleButtonPan);
		toolBar.addSeparator();
		toolBar.add(comboBoxScale);
		toolBar.addSeparator();
		toolBar.add(buttonLocate);
		toolBar.addSeparator();
		toolBar.add(buttonZoomIn);
		toolBar.add(buttonZoomOut);

		add(toolBar, BorderLayout.PAGE_START);
		add(panelTime, BorderLayout.CENTER);
		add(panelStatus, BorderLayout.SOUTH);
		
		panelStatus.setLayout(new BorderLayout());
		panelStatus.add(labelCursor, BorderLayout.CENTER);
		panelStatus.add(labelColor, BorderLayout.WEST);
	}

	/**
	 * Attach various listeners.
	 */
	private void attachListeners() {
		addTimeMachine2Listener(new TimeMachine2Listener() {
			public void scaleChanged(ScaleEvent e) {
			}
			public void selectedPeriodChanged(SelectedPeriodEvent e) {
				 Calendar calendar = Calendar.getInstance();
				 calendar.set(panelTime.getPanelCursor().getYearStart(), 0, 1);
				 Date fromDate = calendar.getTime();
				 calendar.set(panelTime.getPanelCursor().getYearEnd(), 11, 31);
				 Date toDate = calendar.getTime();
				if (soSelectedPeriodStart != null)
					soSelectedPeriodStart.setDate(fromDate);
				if (soSelectedPeriodEnd != null)
					soSelectedPeriodEnd.setDate(toDate);
				if (soSelectedPeriod != null)
					soSelectedPeriod.setTimePeriod(fromDate, toDate);
			}
		});
        
		comboBoxScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int scale;
				try {
					scale = ((Integer) comboBoxScale.getSelectedItem()).intValue();
				} catch (Exception e1) {
					return;
				}
				if (scale <= 0)
					return;
				scaleChanged();
			}
		});
		
		buttonLocate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				int scale = panelTime.getPanelCursor().getYearEnd() - panelTime.getPanelCursor().getYearStart();				
//				comboBoxScale.setSelectedItem(scale);
//				scaleChanged();
				panelTime.center();
			}
		});
		
		buttonZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int scale = ((Integer) comboBoxScale.getSelectedItem()).intValue();
				scale -= ZOOM_STEP;
				if (scale <= 0)
					return;
				comboBoxScale.setSelectedItem(scale);
				scaleChanged();
			}
		});
		buttonZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int scale = ((Integer) comboBoxScale.getSelectedItem()).intValue();
				scale += ZOOM_STEP;
				comboBoxScale.setSelectedItem(scale);
				scaleChanged();
			}
		});
	}

	/**
	 * Returns the component's E-Slate handle.
	 * 
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
			handle.setUniqueComponentName(BundleMessages.getMessage("name"));
		} catch (RenamingForbiddenException e) {
			e.printStackTrace();
		}

//		if (toolBar != null)
//			handle.add(toolBar.getESlateHandle());

//		add(handle.getMenuPanel(), BorderLayout.NORTH);

		soSelectedPeriodStart = new DateSO(handle);
		soSelectedPeriodEnd = new DateSO(handle);
		soSelectedPeriod = new TimeMachineSO(handle);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(panelTime.getPanelCursor().getYearStart(), 0, 1);
		Date fromDate = calendar.getTime();
		soSelectedPeriodStart.setDate(fromDate);
		calendar.set(panelTime.getPanelCursor().getYearEnd(), 11, 31);
		Date toDate = calendar.getTime();
		soSelectedPeriodEnd.setDate(toDate);
		soSelectedPeriod.setTimePeriod(fromDate, toDate);
		
		try {
			// Plug for selected period
			SharedObjectListener selectedPeriodListener = new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent e) {
                    TimeMachineSO timeMachineSO = (TimeMachineSO) e.getSharedObject();
					Date fromDate = timeMachineSO.getFrom();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(fromDate);
					int selectedPeriodStart = calendar.get(Calendar.YEAR);
					Date toDate = timeMachineSO.getTo();
					calendar.setTime(toDate);
					int selectedPeriodEnd = calendar.get(Calendar.YEAR);
					
					if (selectedPeriodEnd <= selectedPeriodStart)
						return;
					PanelCursor panelCursor = panelTime.getPanelCursor();
					panelCursor.setYearStart(selectedPeriodStart);
					panelCursor.setYearEnd(selectedPeriodEnd);
					panelCursor.place();
					cursorMoved();
					
//					soSelectedPeriodStart.setValue(panelTime.getPanelCursor().getYearStart());
				}
			};
			plugSelectedPeriod = new SingleInputMultipleOutputPlug(handle, BundleMessages.getResourceBundle(),
					"selectedPeriodPlug", Color.WHITE,
					TimeMachineSO.class, soSelectedPeriod, selectedPeriodListener);
			plugSelectedPeriod.setNameLocaleIndependent("selectedPeriodPlug");
			handle.addPlug(plugSelectedPeriod);
			plugSelectedPeriod.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					if (e.getType() == Plug.INPUT_CONNECTION) {
                        try {
							TimeMachineSO timeMachineSO = (TimeMachineSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
							Date fromDate = timeMachineSO.getFrom();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(fromDate);
							int selectedPeriodStart = calendar.get(Calendar.YEAR);
							Date toDate = timeMachineSO.getTo();
							calendar.setTime(toDate);
							int selectedPeriodEnd = calendar.get(Calendar.YEAR);

							if (selectedPeriodEnd <= selectedPeriodStart)
								return;
							PanelCursor panelCursor = panelTime.getPanelCursor();
							panelCursor.setYearStart(selectedPeriodStart);
							panelCursor.setYearEnd(selectedPeriodEnd);
							panelCursor.place();
							cursorMoved();
						} catch (Exception e1) {
							e1.printStackTrace();
							plugSelectedPeriod.disconnect();
						}
					}
				}
			});
			
			// Plug for selected period start
			SharedObjectListener selectedPeriodStartListener = new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent e) {
                    DateSO dateSO = (DateSO) e.getSharedObject();
                    Date fromDate = dateSO.getDate();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(fromDate);
					int selectedPeriodStart = calendar.get(Calendar.YEAR);

					PanelCursor panelCursor = panelTime.getPanelCursor();
					panelCursor.setYearStart(selectedPeriodStart);
					panelCursor.place();
					cursorMoved();

//					soSelectedPeriodStart.setDate(fromDate);
				}
			};
			plugSelectedPeriodStart = new SingleInputMultipleOutputPlug(handle, BundleMessages.getResourceBundle(),
					"selectedPeriodStartPlug", new Color(34,139,134),
					DateSO.class, soSelectedPeriodStart, selectedPeriodStartListener);
			plugSelectedPeriodStart.setNameLocaleIndependent("selectedPeriodStartPlug");
            plugSelectedPeriod.addPlug(plugSelectedPeriodStart);
			plugSelectedPeriodStart.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					if (e.getType() == Plug.INPUT_CONNECTION) {
						try {
							DateSO dateSO = (DateSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
							Date fromDate = dateSO.getDate();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(fromDate);
							int selectedPeriodStart = calendar.get(Calendar.YEAR);

							PanelCursor panelCursor = panelTime.getPanelCursor();
							panelCursor.setYearStart(selectedPeriodStart);
							panelCursor.place();
							cursorMoved();
						} catch (RuntimeException e1) {
							e1.printStackTrace();
							plugSelectedPeriodStart.disconnect();
						}
					}
				}
			});

			// Plug for selected period end
			SharedObjectListener selectedPeriodEndListener = new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent e) {
                    DateSO dateSO = (DateSO) e.getSharedObject();
                    Date fromDate = dateSO.getDate();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(fromDate);
					int selectedPeriodEnd = calendar.get(Calendar.YEAR);

					PanelCursor panelCursor = panelTime.getPanelCursor();
					panelCursor.setYearEnd(selectedPeriodEnd);
					panelCursor.place();
					cursorMoved();

//					soSelectedPeriodEnd.setValue(panelTime.getPanelCursor().getYearEnd());
				}
			};
			plugSelectedPeriodEnd = new SingleInputMultipleOutputPlug(handle, BundleMessages.getResourceBundle(),
					"selectedPeriodEndPlug", new Color(34,139,134),
					DateSO.class, soSelectedPeriodEnd, selectedPeriodEndListener);
			plugSelectedPeriodEnd.setNameLocaleIndependent("selectedPeriodEndPlug");
			plugSelectedPeriod.addPlug(plugSelectedPeriodEnd);
			plugSelectedPeriodEnd.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					if (e.getType() == Plug.INPUT_CONNECTION) {
						try {
							DateSO dateSO = (DateSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
							Date fromDate = dateSO.getDate();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(fromDate);
							int selectedPeriodEnd = calendar.get(Calendar.YEAR);

							PanelCursor panelCursor = panelTime.getPanelCursor();
							panelCursor.setYearEnd(selectedPeriodEnd);
							panelCursor.place();
							cursorMoved();
						} catch (RuntimeException e1) {
							e1.printStackTrace();
							plugSelectedPeriodEnd.disconnect();
						}
					}
				}
			});
		} catch (InvalidPlugParametersException e) {
			e.printStackTrace();
		} catch (PlugExistsException e) {
			e.printStackTrace();
		}
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
		return new ESlateInfo(BundleMessages.getMessage("componentName") + ", "
				+ BundleMessages.getMessage("version") + " " + VERSION, info);
	}

	/**
	 * Add a listener for time machine events.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addTimeMachine2Listener(TimeMachine2Listener listener) {
		synchronized (timeMachine2Listeners) {
			if (!timeMachine2Listeners.contains(listener)) {
				timeMachine2Listeners.add(listener);
			}
		}
	}

	/**
	 * Remove a listener for time machine events.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeTimeMachine2Listener(TimeMachine2Listener listener) {
		synchronized (timeMachine2Listeners) {
			int ind = timeMachine2Listeners.indexOf(listener);
			if (ind >= 0) {
				timeMachine2Listeners.remove(ind);
			}
		}
	}

	/**
	 * Fires all listeners registered for time machine scale change events.
	 */
	private void fireScaleChanged() {
		ArrayList listeners;
		synchronized (timeMachine2Listeners) {
			listeners = (ArrayList) (timeMachine2Listeners.clone());
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			TimeMachine2Listener l = (TimeMachine2Listener) (listeners.get(i));
			ScaleEvent e = new ScaleEvent(this);
			l.scaleChanged(e);
		}
	}
	
	/**
	 * Fires all listeners registered for time machine selected period change events.
	 * @param cursorYearStart Cursor start year.
	 * @param cursorYearEnd Cursor end year.
	 */
	void fireSelectedPeriodChanged(int cursorYearStart, int cursorYearEnd) {
		ArrayList listeners;
		synchronized (timeMachine2Listeners) {
			listeners = (ArrayList) (timeMachine2Listeners.clone());
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			TimeMachine2Listener l = (TimeMachine2Listener) (listeners.get(i));
			SelectedPeriodEvent e = new SelectedPeriodEvent(this, cursorYearStart, cursorYearEnd);
			l.selectedPeriodChanged(e);
		}
	}
	
	/**
	 * Events occured because of cursor move.
	 */
	void cursorMoved() {
		PanelCursor panelCursor = panelTime.getPanelCursor();
		int yearStart = panelCursor.getYearStart();
		int yearEnd = panelCursor.getYearEnd();
		labelCursor.setText(PanelTime.yearLabel(yearStart)+" - "+PanelTime.yearLabel(yearEnd));
		fireSelectedPeriodChanged(yearStart, yearEnd);
	}
	
	/**
	 * Scale changed events.
	 */
	private void scaleChanged() {
		int scale = ((Integer) comboBoxScale.getSelectedItem()).intValue();
		panelTime.setYearsPerPixel(PanelTime.YEARS_PER_PIXEL_BASE * scale / 100f);
		panelTime.repaint();
		
		panelTime.getPanelCursor().place();

		fireScaleChanged();
		
		if (scale <= 50)
			colorMark = Color.WHITE;
		else if (scale > 50 && scale <= 100)
			colorMark = Color.CYAN;
		else if (scale > 100 && scale <= 200)
			colorMark = Color.GREEN;
		else if (scale > 200 && scale <= 500)
			colorMark = Color.YELLOW;
		else if (scale > 500 && scale <= 1000)
			colorMark = Color.ORANGE;
		else
			colorMark = Color.RED;
		labelColor.repaint();
	}
	
	/**
	 * Get pan toggle button.
	 * @return Pan toggle button.
	 */
	JToggleButton getToggleButtonPan() {
		return toggleButtonPan;
	}

	/**
	 * Get zoom combo box.
	 * @return Zoom combo box.
	 */
	JComboBox getComboBoxScale() {
		return comboBoxScale;
	}

	/**
	 * Get cursor label.
	 * @return Cursor label.
	 */
	JLabel getLabelCursor() {
		return labelCursor;
	}
	
	/**
	 * Get left visible year.
	 * @return Left visible year.
	 */
	public int getLeftVisibleYear() {
		return panelTime.getYearStart();
	}
	
	/**
	 * Set left visible year.
	 * @param leftVisibleYear Left visible year.
	 */
	public void setLeftVisibleYear(int leftVisibleYear) {
		panelTime.setYearStart(leftVisibleYear);
		panelTime.repaint();
		panelTime.getPanelCursor().setLocation(panelTime.yearToPixel(panelTime.getPanelCursor().getYearStart()), panelTime.getPanelCursor().getLocation().y);
	}
	
	/**
	 * Get left selected year.
	 * @return Left selected year.
	 */
	public int getLeftSelectedYear() {
		return panelTime.getPanelCursor().getYearStart();
	}
	
	/**
	 * Set left selected eyar.
	 * @param leftSelectedYear Left selected year.
	 */
	public void setLeftSelectedYear(int leftSelectedYear) {
		PanelCursor panelCursor = panelTime.getPanelCursor();
		if (leftSelectedYear >= panelCursor.getYearEnd())
			return;
		panelCursor.setYearStart(leftSelectedYear);
		panelCursor.place();
		cursorMoved();
	}
	
	/**
	 * Get right selected year.
	 * @return Right selected year.
	 */
	public int getRightSelectedYear() {
		return panelTime.getPanelCursor().getYearEnd();
	}
	
	/**
	 * Set right selected year.
	 * @param rightSelectedYear Right selected year.
	 */
	public void setRightSelectedYear(int rightSelectedYear) {
		PanelCursor panelCursor = panelTime.getPanelCursor();
		if (rightSelectedYear <= panelCursor.getYearStart())
			return;
		panelCursor.setYearEnd(rightSelectedYear);
		panelCursor.place();
		cursorMoved();
	}

	/**
	 * Get scale.
	 * @return Scale.
	 */
	public int getScale() {
		return ((Integer) comboBoxScale.getSelectedItem()).intValue();
	}
	
	/**
	 * Set scale.
	 * @param scale Scale.
	 */
	public void setScale(int scale) {
		comboBoxScale.setSelectedItem(scale);
	}
	
	/**
	 * Get cursor color.
	 * @return Cursor color.
	 */
	public Color getColorCursor() {
		return panelTime.getPanelCursor().getColorCursor();
	}

	/**
	 * Set cursor color.
	 * @param colorCursor Cursor color.
	 */
	public void setColorCursor(Color colorCursor) {
		panelTime.getPanelCursor().setColorCursor(colorCursor);
	}

	/**
	 * Get labels color.
	 * @return Labels color.
	 */
	public Color getColorLabels() {
		return panelTime.getColorLabels();
	}

	/**
	 * Set labels color.
	 * @param colorLabels Labels color.
	 */
	public void setColorLabels(Color colorLabels) {
		panelTime.setColorLabels(colorLabels);
	}

	/**
	 * Get panel color.
	 * @return Panel color.
	 */
	public Color getColorPanel() {
		return panelTime.getBackground();
	}

	/**
	 * Set panel color.
	 * @param colorPanel Panel color.
	 */
	public void setColorPanel(Color colorPanel) {
		panelTime.setBackground(colorPanel);
	}

	/**
	 * Get ruler color.
	 * @return Ruler color.
	 */
	public Color getColorRuler() {
		return panelTime.getColorRuler();
	}

	/**
	 * Set ruler color.
	 * @param colorRuler Ruler color.
	 */
	public void setColorRuler(Color colorRuler) {
		panelTime.setColorRuler(colorRuler);
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

		fieldMap.put(SCALE, (Integer) comboBoxScale.getSelectedItem());
		fieldMap.put(LEFT_VISIBLE_YEAR, panelTime.getYearStart());
		fieldMap.put(LEFT_SELECTED_YEAR, panelTime.getPanelCursor().getYearStart());
		fieldMap.put(RIGHT_SELECTED_YEAR, panelTime.getPanelCursor().getYearEnd());
		fieldMap.put(COLOR_PANEL, panelTime.getBackground());
		fieldMap.put(COLOR_RULER, panelTime.getColorRuler());
		fieldMap.put(COLOR_LABELS, panelTime.getColorLabels());
		fieldMap.put(COLOR_CURSOR, panelTime.getPanelCursor().getColorCursor());

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

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			TimeMachine2 timeMachine2 = new TimeMachine2();

			JFrame frame = new JFrame("Time machine 2");
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					System.exit(0);
				}
			});
			frame.setContentPane(timeMachine2);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.toFront();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
