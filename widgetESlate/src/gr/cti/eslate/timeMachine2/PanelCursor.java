package gr.cti.eslate.timeMachine2;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

/**
 * Cursor panel.
 * @author augril
 */
class PanelCursor extends JPanel {
	private static final long serialVersionUID = 1L;

	/** Paint border. */
    private static final int BORDER_PAINT = 2;
    /** Minimum width. */
    private static final int MIN_WIDTH = BORDER_PAINT * 2 + 4;
	/** Continuous timer delays */
	private static final int INITIAL_DELAY=150;
	private static final int REPEAT_DELAY=30;
	/** Drag mode. */
	private static final int MODE_RESIZE_LEFT = 0;
	private static final int MODE_RESIZE_RIGHT = 1;
	private static final int MODE_MOVE = 2;
	/** Time panel. */
	private PanelTime panelTime;
	/** Custom cursors. */
	private Cursor cursorResizePan, cursorResizeGrab;
    /** Mouse input adapter. */
    private MouseInputAdapter mouseInputAdapter;
    /** Cursor start year. */
    private int yearStart = 0;
    /** Cursor end year. */
    private int yearEnd = 100;
    /** Drag state of the cursor. */
    private boolean dragged;
    /** Cursor color. */
    private Color colorCursor;
	
	/**
	 * Creates a new cursor panel. 
	 * @param panelTime Time panel.
	 */
	PanelCursor(PanelTime panelTime) {
		super();
		this.panelTime = panelTime;
		
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
		colorCursor = list.getSelectionBackground();
		
		setPreferredSize(new Dimension(50, 20));
		setBorder(null);
		setOpaque(false);
		
        cursorResizePan = Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(PanelCursor.class
						.getResource("images/movePan_32x32.gif")).getImage(),
				new Point(16, 16), "cursorMovePan");
        cursorResizeGrab = Toolkit.getDefaultToolkit().createCustomCursor(
        		new ImageIcon(PanelCursor.class
        				.getResource("images/moveGrab_32x32.gif")).getImage(),
        				new Point(16, 16), "cursorMoveGrab");
	}
	 
	/**
	 * Layout components.
	 */
	private void layoutComponents() {
	}
	
	/**
	 * Attach listeners.
	 */
	private void attachListeners() {
		addMouseListener(getMouseInputAdapter());
		addMouseMotionListener(getMouseInputAdapter());
	}

	private MouseInputAdapter getMouseInputAdapter() {
		if (mouseInputAdapter == null)
			mouseInputAdapter = new MouseInputAdapter() {
				private int dragMode;
				private Point pointOrigin;
		        private ContinuousTimer continuousTimer = new ContinuousTimer();
				
				public void mousePressed(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e))
						return;
					Dimension size = PanelCursor.this.getSize();
					if (e.getX()>BORDER_PAINT && e.getX()<size.width-BORDER_PAINT)
						panelTime.getTimeMachine2().setCursor(cursorResizeGrab);
					pointOrigin = e.getPoint();
					SwingUtilities.convertPointToScreen(pointOrigin, PanelCursor.this);
					if (e.getX()>=0 && e.getX()<=BORDER_PAINT)
						dragMode = MODE_RESIZE_LEFT;
					else if (e.getX()>=size.width-BORDER_PAINT && e.getX()<=size.width)
						dragMode = MODE_RESIZE_RIGHT;
					else
						dragMode = MODE_MOVE;
				}
				public void mouseReleased(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e))
						return;
					Dimension size = PanelCursor.this.getSize();
					if (!contains(e.getPoint()))
						panelTime.getTimeMachine2().setCursor(Cursor.getDefaultCursor());
					else if (e.getX()>BORDER_PAINT && e.getX()<size.width-BORDER_PAINT)
						panelTime.getTimeMachine2().setCursor(cursorResizePan);
					pointOrigin = null;
					dragged = false;
					continuousTimer.stop();
				}
			    public void mouseExited(MouseEvent e) {
			    	if (pointOrigin != null)
			    		return;
					panelTime.getTimeMachine2().setCursor(Cursor.getDefaultCursor());
			    }
				public void mouseMoved(MouseEvent e) {
					Dimension size = PanelCursor.this.getSize();
					if (e.getX()>=0 && e.getX()<=BORDER_PAINT)
						panelTime.getTimeMachine2().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					else if (e.getX()>=size.width-BORDER_PAINT && e.getX()<=size.width)
						panelTime.getTimeMachine2().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					else
						panelTime.getTimeMachine2().setCursor(cursorResizePan);
				}
				public void mouseDragged(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e))
						return;
					dragged = true;
					Point pointAtPanelTime = SwingUtilities.convertPoint(PanelCursor.this, e.getPoint(), panelTime);
					Point pointDrag = e.getPoint();
					SwingUtilities.convertPointToScreen(pointDrag, PanelCursor.this);
					Dimension size = PanelCursor.this.getSize();
					continuousTimer.setDragMode(dragMode);
					
					if (dragMode == MODE_MOVE) {
						int newX = getLocation().x + pointDrag.x - pointOrigin.x;
						if ((pointDrag.x <= pointOrigin.x && newX>=0) 
								|| (pointDrag.x >= pointOrigin.x && newX+size.width <= panelTime.getSize().width)) {
//							if (pointAtPanelTime.x>=0 && pointAtPanelTime.x<=panelTime.getSize().width) {
								continuousTimer.stop();
								setLocation(newX, getLocation().y);
								int yearsRange = yearEnd - yearStart;
								yearStart = panelTime.pixelToYear(getLocation().x);
								yearEnd = yearStart + yearsRange;
								panelTime.getTimeMachine2().cursorMoved();
								pointOrigin = pointDrag;
//							}
						}
						else {
							if (continuousTimer.isLeft() != pointDrag.x<pointOrigin.x)
								continuousTimer.stop();
							if (!continuousTimer.isRunning())
								continuousTimer.start(pointDrag.x<pointOrigin.x);
						}
					}
					else if (dragMode == MODE_RESIZE_RIGHT) {
						if (pointAtPanelTime.x>=0 && pointAtPanelTime.x<=panelTime.getSize().width) {
							continuousTimer.stop();
							int newWidth = size.width + pointDrag.x - pointOrigin.x;
							if (newWidth < MIN_WIDTH) {
								newWidth = MIN_WIDTH;
								Point pointRight = new Point(getLocation().x+newWidth, pointAtPanelTime.y);
								SwingUtilities.convertPointToScreen(pointRight, panelTime);
								pointOrigin.x = pointRight.x;
							}
							else
								pointOrigin = pointDrag;
							setSize(newWidth, size.height);
							yearEnd = panelTime.pixelToYear(getLocation().x+newWidth);
							panelTime.getTimeMachine2().cursorMoved();
						}
						else {
							if (!continuousTimer.isRunning())
								continuousTimer.start(pointDrag.x<pointOrigin.x);
						}
					}
					else if (dragMode == MODE_RESIZE_LEFT) {
						if (pointAtPanelTime.x>=0 && pointAtPanelTime.x<=panelTime.getSize().width) {
							continuousTimer.stop();
							int newWidth = size.width - (pointDrag.x - pointOrigin.x);
							int newX = getLocation().x + pointDrag.x - pointOrigin.x;
							if (newWidth < MIN_WIDTH) {
								newWidth = MIN_WIDTH;
								newX = getLocation().x + size.width - newWidth; 
								Point pointLeft = new Point(newX, pointAtPanelTime.y);
								SwingUtilities.convertPointToScreen(pointLeft, panelTime);
								pointOrigin.x = pointLeft.x;
							}
							else
								pointOrigin = pointDrag;
							setLocation(newX, getLocation().y);
							setSize(newWidth, size.height);
							yearStart = panelTime.pixelToYear(getLocation().x);
							panelTime.getTimeMachine2().cursorMoved();
						}
						else {
							if (!continuousTimer.isRunning())
								continuousTimer.start(pointDrag.x<pointOrigin.x);
						}
					}
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
		
		Dimension size = getSize();
		g.setColor(new Color(colorCursor.getRed(), colorCursor.getGreen(), colorCursor.getBlue(), 100));
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(colorCursor);
		g.fillRect(0, 0, size.width, BORDER_PAINT);
		g.fillRect(size.width-BORDER_PAINT, 0, BORDER_PAINT, size.height);
		g.fillRect(0, size.height-BORDER_PAINT, size.width, BORDER_PAINT);
		g.fillRect(0, 0, BORDER_PAINT, size.height);
	}

	/**
	 * Layout cursor panel.
	 */
	void place() {
		setBounds(
				panelTime.yearToPixel(yearStart), 
				getLocation().y, 
				panelTime.yearToPixel(yearEnd)-panelTime.yearToPixel(yearStart), 
				getSize().height);
	}
	
	/**
	 * Get cursor start year.
	 * @return Cursor start year.
	 */
	int getYearStart() {
		return yearStart;
	}
	
	/**
	 * Set cursor start year.
	 * @param yearEnd Cursor start year.
	 */
	void setYearStart(int yearStart) {
		if (yearStart < yearEnd)
			this.yearStart = yearStart;
	}

	/**
	 * Get cursor end year.
	 * @return Cursor start year.
	 */
	int getYearEnd() {
		return yearEnd;
	}
	
	/**
	 * Set cursor end year.
	 * @param yearEnd Cursor end year.
	 */
	void setYearEnd(int yearEnd) {
		if (yearEnd > yearStart)
			this.yearEnd = yearEnd;
	}

	/**
	 * Get drag state of cursor.
	 * @return Drag state of cursor.
	 */
	boolean isDragged() {
		return dragged;
	}

	/**
	 * Get cursor color.
	 * @return Cursor color.
	 */
	Color getColorCursor() {
		return colorCursor;
	}

	/**
	 * Set cursor color.
	 * @param colorCursor Cursor color.
	 */
	void setColorCursor(Color colorCursor) {
		this.colorCursor = colorCursor;
		repaint();
	}
	
	/**
	 * The timer sends consecutive events.
	 */
	private class ContinuousTimer extends Timer {
        private static final long serialVersionUID = 1L;
        private int dragMode; 
        private boolean left;
        
        private ContinuousTimer() {
			super(0, null);
			setInitialDelay(INITIAL_DELAY);
//			setDelay(REPEAT_DELAY);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int unit = ((Integer) panelTime.getTimeMachine2().getComboBoxScale().getSelectedItem()).intValue();
					int step = unit /10;
					setDelay(REPEAT_DELAY);
					if (unit < 10) {
						step = 2;
						setDelay(REPEAT_DELAY*10);
					}
					if (dragMode == MODE_MOVE) { // FIXME
						panelTime.setYearStart(panelTime.getYearStart() + step * (left?-1:1));
						panelTime.repaint();
						int yearsRange = yearEnd - yearStart;
						yearStart = panelTime.pixelToYear(getLocation().x);
						yearEnd = yearStart + yearsRange;
					}
					else if (dragMode == MODE_RESIZE_RIGHT) { //FIXME
						int newWidth = getSize().width + (panelTime.yearToPixel(step) - panelTime.yearToPixel(0)) * (left?-1:1);
						if (newWidth < MIN_WIDTH) // FIXME
							return;
						panelTime.setYearStart(panelTime.getYearStart() + step * (left?-1:1));
						panelTime.repaint();
						setLocation(panelTime.yearToPixel(yearStart), getLocation().y);
						setSize(newWidth, getSize().height);
						yearEnd = panelTime.pixelToYear(getLocation().x+newWidth);
					}
					else if (dragMode == MODE_RESIZE_LEFT) { //FIXME
						int newWidth = getSize().width - (panelTime.yearToPixel(step) - panelTime.yearToPixel(0)) * (left?-1:1);
						if (newWidth < MIN_WIDTH) // FIXME
							return;
						panelTime.setYearStart(panelTime.getYearStart() + step * (left?-1:1));
						panelTime.repaint();
						setSize(newWidth, getSize().height);
						yearStart = panelTime.pixelToYear(getLocation().x);
					}
					panelTime.getTimeMachine2().cursorMoved();
				}
			});
			setRepeats(true);
		}
		
		private void setDragMode(int dragMode) {
			this.dragMode = dragMode;
		}
		
		private boolean isLeft() {
			return left;
		}

		private void start(boolean left) {
			this.left = left;
			super.start();
		}
	}
}
