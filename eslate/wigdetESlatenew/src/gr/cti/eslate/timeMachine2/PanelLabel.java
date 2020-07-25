package gr.cti.eslate.timeMachine2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Panel showing the cursor selected time period labels.
 * @author augril
 */
class PanelLabel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Time panel.
	 */
	private PanelTime panelTime;
	
	/**
	 * Construct a label panel.
	 */
	PanelLabel(PanelTime panelTime) {
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
		setPreferredSize(new Dimension(128, 20));
		setBorder(null);
		setOpaque(false);
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
	}
	
	/**
	 * Overwrites <code>paintComponent</code> method.
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		int timeFrom = panelTime.pixelToYear(panelTime.getPanelCursor().getLocation().x);
		String yearFrom = PanelTime.yearLabel(timeFrom);
		int timeTo = panelTime.pixelToYear(panelTime.getPanelCursor().getLocation().x+panelTime.getPanelCursor().getSize().width);
		String yearTo = PanelTime.yearLabel(timeTo);
		String label = yearFrom+" - "+yearTo;
		FontMetrics fontMetrics = g.getFontMetrics();
		Dimension size = getSize();
		int labelWidth = fontMetrics.stringWidth(label);
		if (labelWidth > size.width)
			setSize(labelWidth, size.height);
		g.drawString(label, (size.width-labelWidth)/2, getSize().height-fontMetrics.getMaxDescent());
	}
}
