package gr.cti.eslate.timeMachine2;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * Panel showing the time. 
 * @author augril
 */
class PanelTime extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Base value of years per pixel.
	 */
	static final float YEARS_PER_PIXEL_BASE = 2.0f;
	/**
	 * Time machine 2 instance.
	 */
	private TimeMachine2 timeMachine2;
	/**
	 * Cursor panel.
	 */
	private PanelCursor panelCursor;
	/**
	 * Labels panel.
	 */
//	private PanelLabel panelLabel;
	/**
	 * Custom cursors.
	 */
	private Cursor cursorPan, cursorGrab;
    /**
     * Mouse input adapter.
     */
    private MouseInputAdapter mouseInputAdapter;
    /**
     * Time start year.
     */
    private int yearStart = -400;
	/**
	 * Years per pixel.
	 */
	private float yearsPerPixel = YEARS_PER_PIXEL_BASE;
	/** Ruler color. */
	private Color colorRuler;
	/** Labels color. */
	private Color colorLabels;
	
	/**
	 * Construct a new time panel.
	 * @param timeMachine2
	 */
	PanelTime(TimeMachine2 timeMachine2) {
		super();
		this.timeMachine2 = timeMachine2;
		
		initialize();
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
		JList list = new JList();
		colorRuler = list.getForeground();
		colorLabels = list.getForeground();
		
		setBorder(BorderFactory.createLoweredBevelBorder());
		setOpaque(true);
		setBackground(list.getBackground());
		
        cursorPan = Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(PanelTime.class
						.getResource("images/pan_32x32.gif")).getImage(),
				new Point(16, 16), "cursorPan");
        cursorGrab = Toolkit.getDefaultToolkit().createCustomCursor(
        		new ImageIcon(PanelTime.class
        				.getResource("images/grab_32x32.gif")).getImage(),
        				new Point(16, 16), "cursorGrab");
        
		panelCursor = new PanelCursor(this);
	}
	
	/**
	 * Layout components.
	 */
	private void layoutComponents() {
		setLayout(null);
		
		add(panelCursor);
//		add(panelLabel);
	}
	
	/**
	 * Attach listeners.
	 */
	private void attachListeners() {
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				panelCursor.setBounds(
						yearToPixel(panelCursor.getYearStart()), 
						(getSize().height-panelCursor.getPreferredSize().height)/2, 
						yearToPixel(panelCursor.getYearEnd())-yearToPixel(panelCursor.getYearStart()), 
						panelCursor.getPreferredSize().height);
//				panelLabel.setBounds(
//						panelCursor.getLocation().x-(panelLabel.getPreferredSize().width-panelCursor.getPreferredSize().width)/2, 
//						panelCursor.getLocation().y+panelCursor.getPreferredSize().height,
//						panelLabel.getPreferredSize().width, 
//						panelLabel.getPreferredSize().height);
			}
		});
		
		addMouseListener(getMouseInputAdapter());
		addMouseMotionListener(getMouseInputAdapter());
	}
	
	/**
	 * Get mouse input adapter.
	 * @return Mouse input adapter.
	 */
	private MouseInputAdapter getMouseInputAdapter() {
		if (mouseInputAdapter == null)
			mouseInputAdapter = new MouseInputAdapter() {
				private int originX;

				public void mousePressed(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e))
						return;
					if (!timeMachine2.getToggleButtonPan().isSelected())
						return;
					originX = e.getX();
					timeMachine2.setCursor(cursorGrab);
				}
				public void mouseReleased(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e))
						return;
					if (!timeMachine2.getToggleButtonPan().isSelected())
						return;
					originX = 0;
					if (!contains(e.getPoint()))
						timeMachine2.setCursor(Cursor.getDefaultCursor());
					else
						timeMachine2.setCursor(cursorPan);
				}
			    public void mouseExited(MouseEvent e) {
					if (contains(e.getPoint()))
						return;
			    	if (originX != 0)
			    		return;
			    	if (panelCursor.isDragged())
			    		return;
			    	timeMachine2.setCursor(Cursor.getDefaultCursor());
			    }
				public void mouseMoved(MouseEvent e) {
					if (timeMachine2.getToggleButtonPan().isSelected())
						timeMachine2.setCursor(cursorPan);
					else
						timeMachine2.setCursor(Cursor.getDefaultCursor());
				}
				public void mouseDragged(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e))
						return;
					if (!timeMachine2.getToggleButtonPan().isSelected())
						return;
					timeMachine2.setCursor(cursorGrab);
					yearStart -= pixelToYear(e.getX()) - pixelToYear(originX);
					repaint();
					
					panelCursor.setLocation(yearToPixel(panelCursor.getYearStart()), panelCursor.getLocation().y);
					originX = e.getX();
				}
			};
		return mouseInputAdapter;
	}
	
	/**
	 * Overwrites <code>paintComponent</code> method.
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int axisHeight = 4;
		int markHeight = 12;
		Dimension size = getSize();
		g.setColor(colorRuler);
		int axisY = (size.height-axisHeight)/2; 
		g.fillRect(0, axisY, size.width, axisHeight);
		int unit = ((Integer) timeMachine2.getComboBoxScale().getSelectedItem()).intValue();
		int timeEnd = pixelToYear(size.width);
		for (int year = yearStart; year < timeEnd+1; year++) {
			if ((float) year/unit - year/unit == 0) {
				int pixel = yearToPixel(year);
				g.setColor(colorRuler);
				if ((float) year/unit/2 - year/unit/2 == 0)
					g.drawLine(pixel, axisY-markHeight, pixel, axisY);
				else
					g.drawLine(pixel, axisY+axisHeight, pixel, axisY+axisHeight+markHeight-1);
				String label = yearLabel(year);
				g.setColor(colorLabels);
				FontMetrics fontMetrics = g.getFontMetrics();
				g.drawString(
						label, 
						pixel-fontMetrics.stringWidth(label)/2, 
						((float) year/unit/2 - year/unit/2 == 0)?axisY-markHeight-fontMetrics.getMaxDescent():axisY+axisHeight+markHeight+fontMetrics.getMaxAscent());
			}
		}
	}
	
	/**
	 * Pixel to year.
	 * @param pixel Pixel to process.
	 * @return Corresponding year.
	 */
	int pixelToYear(int pixel) {
		return (int) (yearStart + pixel * yearsPerPixel);
	}
	
	/**
	 * Year to pixel.
	 * @param year Year to process.
	 * @return Corresponding pixel.
	 */
	int yearToPixel(int year) {
		return (int) ((year - yearStart) / yearsPerPixel);
	}
	
	/**
	 * Construct year label.
	 * @param year Year to parse.
	 * @return Year label.
	 */
	static String yearLabel(int year) {
		return String.valueOf(Math.abs(year))+(year==0?"":(year<0?" "+BundleMessages.getMessage("b.c."):" "+BundleMessages.getMessage("a.d.")));
	}

	/**
	 * Center time panel in order to show the cursor.
	 */
	void center() {
		if (panelCursor.getSize().width < getSize().width)
			yearStart = pixelToYear(panelCursor.getLocation().x - (getSize().width-panelCursor.getSize().width)/2);
		else
			yearStart = pixelToYear(panelCursor.getLocation().x);
		repaint();
		
		panelCursor.setLocation(yearToPixel(panelCursor.getYearStart()), panelCursor.getLocation().y);
	}
	
	/**
	 * Get cursor panel.
	 * @return Cursor panel.
	 */
	PanelCursor getPanelCursor() {
		return panelCursor;
	}

	/**
	 * Set years per pixel.
	 * @param yearsPerPixel Years per pixel.
	 */
	void setYearsPerPixel(float yearsPerPixel) {
		this.yearsPerPixel = yearsPerPixel;
	}

	/**
	 * Get time start year.
	 * @return Time start year.
	 */
	int getYearStart() {
		return yearStart;
	}

	/**
	 * Set time start year.
	 * @param timeStartYear Time start year.
	 */
	void setYearStart(int timeStartYear) {
		this.yearStart = timeStartYear;
	}

	/**
	 * Get time machine.
	 * @return Time machine.
	 */
	TimeMachine2 getTimeMachine2() {
		return timeMachine2;
	}

	/**
	 * Get ruler color.
	 * @return Ruler color.
	 */
	Color getColorRuler() {
		return colorRuler;
	}

	/**
	 * Set ruler color.
	 * @param colorRuler Ruler color.
	 */
	void setColorRuler(Color colorRuler) {
		this.colorRuler = colorRuler;
		repaint();
	}
	
	/**
	 * Get labels color.
	 * @return Labels color.
	 */
	Color getColorLabels() {
		return colorLabels;
	}
	
	/**
	 * Set labels color.
	 * @param colorLabels Labels color.
	 */
	void setColorLabels(Color colorLabels) {
		this.colorLabels = colorLabels;
		repaint();
	}
}
