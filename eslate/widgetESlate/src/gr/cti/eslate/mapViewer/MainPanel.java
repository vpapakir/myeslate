package gr.cti.eslate.mapViewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.JDesktopPane;
import javax.swing.JToolTip;
import javax.swing.UIManager;

/**
 * The main panel class.
 * @author Giorgos Vasiliou
 * @version 1.0, 29-May-2002
 */

class MainPanel extends JDesktopPane {
	MainPanel(MapPane mapPane) {
		this.mapPane=mapPane;
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}
	/**
	 * Overriden to show the tooltip in the panel font.
	 */
	public JToolTip createToolTip() {
		JToolTip t = super.createToolTip();
		t.setFont(getFont());
		return t;
	}
	/**
	 * Stops the events when the component is "busy".
	 */
	protected void processMouseEvent(MouseEvent e) {
		if (!mapPane.isBusy()) {
			super.processMouseEvent(e);
			callMIL(e);
		}
	}
	/**
	 * Stops the events when the component is "busy".
	 */
	protected void processMouseMotionEvent(MouseEvent e) {
		if (!mapPane.isBusy()) {
			super.processMouseMotionEvent(e);
			callMIL(e);
		}
	}

	private void callMIL(MouseEvent e) {
		switch (e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				mil.mousePressed(e);
				break;
			case MouseEvent.MOUSE_RELEASED:
				mil.mouseReleased(e);
				break;
			case MouseEvent.MOUSE_CLICKED:
				mil.mouseClicked(e);
				break;
			case MouseEvent.MOUSE_ENTERED:
				mil.mouseEntered(e);
				break;
			case MouseEvent.MOUSE_EXITED:
				mil.mouseExited(e);
				break;
			case MouseEvent.MOUSE_DRAGGED:
				mil.mouseDragged(e);
				break;
			case MouseEvent.MOUSE_MOVED:
				mil.mouseMoved(e);
				break;
		}
	}

	//This listener informs for mouse action inside the map area.
	//It is placed in here to ensure that it will be called after the
	//tool event listeners have finished.
	javax.swing.event.MouseInputListener mil=new javax.swing.event.MouseInputListener() {
		private double[] sp=new double[2];
		public void mousePressed(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				sp[0]=e.getX(); sp[1]=e.getY();
				mapPane.getTransform().transform(sp,0,sp,0,1);
				mapPane.viewer.listeners.mousePressedOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_PRESSED,e.getWhen(),e.getModifiers(),sp[0],sp[1],e.getClickCount(),e.isPopupTrigger()));
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				double x=e.getX();
				double y=e.getY();
				mapPane.viewer.listeners.mouseReleasedOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_RELEASED,e.getWhen(),e.getModifiers(),x,y,e.getClickCount(),e.isPopupTrigger()));
			}
		}
		public void mouseEntered(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				double x=e.getX();
				double y=e.getY();
				mapPane.viewer.listeners.mouseEnteredOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_ENTERED,e.getWhen(),e.getModifiers(),x,y,e.getClickCount(),e.isPopupTrigger()));
			}
		}
		public void mouseExited(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				double x=e.getX();
				double y=e.getY();
				mapPane.viewer.listeners.mouseExitedOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_EXITED,e.getWhen(),e.getModifiers(),x,y,e.getClickCount(),e.isPopupTrigger()));
			}
		}
		public void mouseClicked(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				double x=e.getX();
				double y=e.getY();
				mapPane.viewer.listeners.mouseClickedOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_CLICKED,e.getWhen(),e.getModifiers(),x,y,e.getClickCount(),e.isPopupTrigger()));
			}
		}
		public void mouseDragged(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				double x=e.getX();
				double y=e.getY();
				mapPane.viewer.listeners.mouseDraggedOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_DRAGGED,e.getWhen(),e.getModifiers(),x,y,e.getClickCount(),e.isPopupTrigger()));
			}
		}
		public void mouseMoved(MouseEvent e) {
			if (mapPane.viewer.listeners!=null) {
				double x=e.getX();
				double y=e.getY();
				mapPane.viewer.listeners.mouseMovedOnMapArea(new MapViewerMouseEvent(e.getSource(),MapViewerMouseEvent.MAP_VIEWER_MOUSE_MOVED,e.getWhen(),e.getModifiers(),x,y,e.getClickCount(),e.isPopupTrigger()));
			}
		}
	};

	public void updateUI() {
		super.updateUI();
		this.setBackground((Color) UIManager.get("controlShadow"));
	}
	private MapPane mapPane;
}