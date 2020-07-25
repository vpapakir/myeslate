package gr.cti.eslate.mapViewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * <p>Title: Map Viewer</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author Giorgos Vasiliou
 * @version 1.0
 */

class TimedPopupWindow extends JWindow {
	JLabel label;
	Timer closeTimer;
	private int maxw=-Integer.MAX_VALUE;
	TimedPopupWindow(final MapPane mp) {
		label=new JLabel() {
			public void paintComponent(Graphics g) {
				((Graphics2D)g).setRenderingHints(mp.getRenderingHints());
				super.paintComponent(g);
			};
		};
		label.setFont(new Font("Arial",Font.PLAIN,10));
		label.setOpaque(false);
		getContentPane().add(label);
		((JComponent)getContentPane()).setOpaque(true);
		getContentPane().setBackground(Color.white);
	}
	void setText(String text) {
		int pr=label.getPreferredSize().width;
		if (maxw<pr)
			maxw=pr;
		label.setText(text);
		if (label.getPreferredSize().width>maxw)
			pack();
		if (closeTimer==null) {
			//Delay to avoid sending too many resize events
			closeTimer=new Timer(3000,new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					maxw=-Integer.MAX_VALUE;
				}
			});
			closeTimer.setRepeats(false);
			closeTimer.setCoalesce(true);
		}
		if (!closeTimer.isRunning())
			closeTimer.start();
		else
			closeTimer.restart();
	}
}
