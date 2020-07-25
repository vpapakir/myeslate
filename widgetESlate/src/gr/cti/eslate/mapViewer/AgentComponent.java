package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.IAgent;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

public class AgentComponent extends JComponent {

	public AgentComponent(IAgent agnt) {
		this.agent=agnt;
		lockPhasePainting=false;
		locAngle=0;
		embarked=0;
		lastPaintedAnimationFrame=0;
		setLayout(null);
		setCursor(null);
		//Embark label "button"
		lblEmbark=new JLabel(imgEmbark);
		lblEmbark.setToolTipText(MapViewer.messagesBundle.getString("embark"));
		final Cursor embarkCursor=MapViewer.createCustomCursor("images/embarkcursor.gif",new Point(9,7),new Point(17,15),"embarkcursor");
		lblEmbark.setCursor(embarkCursor);
		lblEmbark.addMouseListener(new MouseAdapter() {
			JPopupMenu pop;
			public void mouseEntered(MouseEvent e) {
				setCursor(embarkCursor);
				lblEmbark.setToolTipText("<html><body bgcolor=#008000><font face=Arial color=#FF8080 size=3><strong>"+AgentComponent.this.agent.getName()+":</strong><p><font color=#00FF00>"+MapViewer.messagesBundle.getString("embark")+"</font></font></body></html>");
			}
			public void mouseExited(MouseEvent e) {
				setCursor(null);
			}
			public void mouseReleased(MouseEvent e) {
				pop=new JPopupMenu();
				Iterator it=embark.iterator();
				while (it.hasNext()) {
					IAgent onAgent=(IAgent) it.next();
					JMenuItem item=new JMenuItem(onAgent.getName());
					pop.add(item);
					item.addActionListener(new EmbarkListener(onAgent));
				}
				JMenuItem cancel=new JMenuItem(MapViewer.messagesBundle.getString("close"));
				cancel.setForeground(Color.red);
				pop.add(cancel);
				pop.show(getParent(),getX()-3,getY()-3);
			}
		});

		lblDisembark=new JLabel(imgDisembark);
		final Cursor disembarkCursor=MapViewer.createCustomCursor("images/disembarkcursor.gif",new Point(9,7),new Point(17,15),"disembarkcursor");
		lblDisembark.setCursor(disembarkCursor);
		lblDisembark.addMouseListener(new MouseAdapter() {
			JPopupMenu pop;
			public void mouseEntered(MouseEvent e) {
				setCursor(disembarkCursor);
				lblDisembark.setToolTipText("<html><body bgcolor=#D08700><font face=Arial color=#FF4040 size=3><strong>"+AgentComponent.this.agent.getName()+":</strong><p><font color=#FFD872>"+MapViewer.messagesBundle.getString("disembark")+"</font></font></body></html>");
			}
			public void mouseExited(MouseEvent e) {
				setCursor(null);
			}
			public void mouseReleased(MouseEvent e) {
				pop=new JPopupMenu();
				Iterator it=agent.getEmbarkedAgents();
				while (it.hasNext()) {
					IAgent onAgent=(IAgent) it.next();
					JMenuItem item=new JMenuItem(onAgent.getName());
					pop.add(item);
					if (!onAgent.isValidLocation(agent.getLongitude(),agent.getLatitude()))
						item.setEnabled(false);
					item.addActionListener(new DisembarkListener(onAgent));
				}
				JMenuItem cancel=new JMenuItem(MapViewer.messagesBundle.getString("close"));
				cancel.setForeground(Color.red);
				pop.add(cancel);
				pop.show(getParent(),getX()-3,getY()-3);
			}
		});
		lblActive=new JLabel(imgActive) {
			public String getToolTipText() {
				return AgentComponent.this.getToolTipText();
			}
		};
		add(lblActive);
		/*When there are multiple agents embarked one in the other, this guarantees that a disembarking
		  agent with agents embarked will have the disembark symbol.*/
		if (agent.hasAgentsEmbarked()) {
		   add(lblDisembark);
		   embarked=agent.getEmbarkedAgentsCount();
		}

		embark=new HashSet();
		enableEvents(AWTEvent.MOUSE_EVENT_MASK+AWTEvent.MOUSE_MOTION_EVENT_MASK);
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	private class EmbarkListener implements ActionListener {
		IAgent lag;
		EmbarkListener(IAgent a) {
			lag=a;
		}
		public void actionPerformed(ActionEvent e) {
			lag.embark(agent);
		}
	};
	private class DisembarkListener implements ActionListener {
		IAgent lag;
		DisembarkListener(IAgent a) {
			lag=a;
		}
		public void actionPerformed(ActionEvent e) {
			agent.disembark(lag);
		}
	};
	/**
	 * Gets its size from the associated agent.
	 */
	public Dimension getPreferredSize() {
		return agent.getFaceSize(agent.getTiltFromUpright()+(((MotionPane) getParent()).mapPane.map.getActiveRegionView().getOrientation()));
	}
	/**
	 * Gets its size from the associated agent.
	 */
	public Dimension getMaximumSize() {
		return agent.getFaceSize(agent.getTiltFromUpright()+(((MotionPane) getParent()).mapPane.map.getActiveRegionView().getOrientation()));
	}
	/**
	 * Gets its size from the associated agent.
	 */
	public Dimension getMinimumSize() {
		return agent.getFaceSize(agent.getTiltFromUpright()+(((MotionPane) getParent()).mapPane.map.getActiveRegionView().getOrientation()));
	}
	/**
	 * Gets its size from the associated agent.
	 */
	public Dimension getSize() {
		return agent.getFaceSize(agent.getTiltFromUpright()+(((MotionPane) getParent()).mapPane.map.getActiveRegionView().getOrientation()));
	}

	void setLockPhasePainting(boolean b) {
		lockPhasePainting=b;
	}

	void rotate(double d) {
		locAngle+=d;
		repaint();
	}

	IAgent mayEmbark(String agentName) {
		IAgent r=null;
		Iterator it=embark.iterator();
		while (it.hasNext() && r==null) {
			IAgent a=(IAgent) it.next();
			if (a.getName().equalsIgnoreCase(agentName))
				r=a;
		}
		return r;
	}

	void setMayEmbark(IAgent a,boolean b) {
		if (b) {
			embark.add(a);
			add(lblEmbark);
			lblEmbark.setBounds(0,0,9,9);
			repaint();
		} else {
			embark.remove(a);
			if (embark.size()==0) {
				remove(lblEmbark);
				repaint(0,0,9,9);
			}
		}
	}

	void clearMayEmbarkList() {
		embark.clear();
	}

	void embark(IAgent a) {
		embarked++;
		add(lblDisembark);
		lblDisembark.setBounds(getWidth()-9,0,9,9);
		setMayEmbark(a,false);
	}

	void disembark(IAgent a) {
		embarked--;
		if (embarked==0)
			remove(lblDisembark);
	}

	public void paintComponent(Graphics g) {
		lblEmbark.setBounds(0,0,9,9);
		lblDisembark.setBounds(getWidth()-9,0,9,9);
		//Disabled for ALTG
//		if (((MotionPane) getParent()).getActiveAgent()==agent) {
//			lblActive.setBounds(getWidth()-6,getHeight()-6,6,6);
//			lblActive.setVisible(true);
//		} else
			lblActive.setVisible(false);
		super.paintComponent(g);
		if (!lockPhasePainting)
			agent.paintFace(g,agent.getTiltFromUpright()+(((MotionPane) getParent()).mapPane.map.getActiveRegionView().getOrientation()));
		else
			agent.paintFace(g,agent.getTiltFromUpright()+(((MotionPane) getParent()).mapPane.map.getActiveRegionView().getOrientation())+locAngle);
		if (agent.isEmbarked()) {
			g.setColor(new Color(255,255,255,100));
			g.fillOval(0,0,getWidth(),getHeight());
			g.setColor(new Color(0,255,0,100));
			g.drawOval(0,0,getWidth(),getHeight());
		}
	}

//	public String getToolTipText() {
//		MapPane mapPane=(MapPane) SwingUtilities.getAncestorOfClass(MapPane.class,this);
//		if (mapPane!=null)
//		{//System.out.println("TOOLTIP AGENT "+mapPane.getMultilineTooltip());
//			return mapPane.getMultilineTooltip(getTool);//"<html><body bgcolor=#FFFF90><font face=Arial color=#800000 size=3><strong>"+AgentComponent.this.agent.getName()+"</strong></font></p></body></html>";
//		}else
//			return null;
//	}

	private void dispatch(MouseEvent e) {
		//First, convert the coordinates
		Point p=SwingUtilities.convertPoint(this,e.getX(),e.getY(),getParent());
		getParent().dispatchEvent(new MouseEvent(this,e.getID(),System.currentTimeMillis(),e.getModifiers(),p.x,p.y,e.getClickCount(),e.isPopupTrigger()));
	}

	//This listener watches the events and keeps the ones that interest it. The others are dispatched
	//in the lower layers.
//	protected void processMouseEvent(MouseEvent e) {
//		if (e.getID()==MouseEvent.MOUSE_PRESSED && SwingUtilities.isRightMouseButton(e))
			//Bring the component to the front
//			((MotionPane) getParent()).setActiveAgent(agent);
//		else
//			dispatch(e);
//	}

	protected void processMouseMotionEvent(MouseEvent e) {
		dispatch(e);
	}

	protected void animate() {
		if (agent.isAnimated() && agent.getVelocity()>0)
			lastPaintedAnimationFrame=(lastPaintedAnimationFrame+1) % 4;
	}

	IAgent agent;
	private boolean lockPhasePainting;
	double locAngle;
	private JLabel lblEmbark,lblDisembark,lblActive;
	private int embarked;
	int lastPaintedAnimationFrame;
	/**
	 * Knows where the agent may embark.
	 */
	private HashSet embark;
	/**
	 * Image resources.
	 */
	private static final ImageIcon imgEmbark=new ImageIcon(AgentComponent.class.getResource("images/embark.gif"));
	private static final ImageIcon imgDisembark=new ImageIcon(AgentComponent.class.getResource("images/disembark.gif"));
	private static final ImageIcon imgActive=new ImageIcon(AgentComponent.class.getResource("images/activeagent.gif"));
}
