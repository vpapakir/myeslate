package gr.cti.eslate.base.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


class ESlateBasicInternalFrameUI extends javax.swing.plaf.basic.BasicInternalFrameUI {
    public MouseInputListener glassPaneDispatcher = null;
    public gr.cti.eslate.base.container.BorderListener newBorderListener = null;

    public static javax.swing.plaf.ComponentUI createUI(JComponent b)    {
        return new ESlateBasicInternalFrameUI((JInternalFrame)b);
    }
    public ESlateBasicInternalFrameUI(JInternalFrame b)   {
        super(b);
        newBorderListener = myCreateBorderListener(b);
    }

    protected MouseInputListener createGlassPaneDispatcher() {
	      return (glassPaneDispatcher = new GlassPaneDispatcher2());
    }

    protected JComponent createNorthPane(JInternalFrame w) {
        JComponent c = super.createNorthPane(w);
        return c;
    }

    protected MouseInputAdapter createBorderListener(JInternalFrame w) {
        return null;
    }
    protected JInternalFrame getFrame() {
        return frame;
    }
    protected gr.cti.eslate.base.container.BorderListener myCreateBorderListener(JInternalFrame w) {
        newBorderListener = new gr.cti.eslate.base.container.BorderListener(w, this);
//        System.out.println("myCreateBorderListener() returning newBorderListener");
        return newBorderListener;
    }
    public MouseInputAdapter getBorderlListener() {
//        System.out.println("getBorderlListener() returning c");
        return newBorderListener;
    }

    protected void installMouseHandlers(JComponent c) {
        c.addMouseListener(newBorderListener);
        c.addMouseMotionListener(newBorderListener);
    }
//    protected javax.swing.DesktopManager getDesktopManager() {
//        return super.getDesktopManager();
//    }

    protected class GlassPaneDispatcher2 implements MouseInputListener {
        boolean dontForwardMouseExit = false;
        /**
         * When inactive, mouse events are forwarded as appropriate either to
         * the UI to activate the frame or to the underlying child component.
         *
         * In keeping with the MDI messaging model (which JInternalFrame
         * emulates), only the mousePressed event is forwarded to the UI
         * to activate the frame.  The mouseEntered, mouseMoved, and
         * MouseExited events are forwarded to the underlying child
         * component, using methods derived from those in Container.
         * The other mouse events are purposely ignored, since they have
         * no meaning to either the frame or its children when the frame
         * is inactive.
         */
        public void mousePressed(MouseEvent e) {
            // what is going on here is the GlassPane is up on the inactive
            // internalframe and want's to "catch" the first mousePressed on
            // the frame in order to give it to the BorderLister (and not the
            // underlying component) and let it activate the frame
//            System.out.println("In GlassPaneDispatcher2 --> mousePressed");
/*            if (getBorderlListener() != null){
              getBorderlListener().mousePressed(e);
            }
*/
//            System.out.println("Forwarding pressed");
            /* When the frame is inactive and mousePressed activates it, the GlassPane
             * of the frame becomes invisible in its 'setSelected(true)' method. This results in:
             * a. the generation of a mouseExit event on the GlassPane, which is forwared to
             *    the actual component.
             * b. the generation of a MouseEnter event, because now that the GlassPane becomes
             *    invisible the cursor really enters the area of the component.
             * We use variable 'dontForwardMouseExit' to avoid (a). We can't avoid (b).
             */
            dontForwardMouseExit = true;
            OldESlateInternalFrame fr = (OldESlateInternalFrame) getFrame();
//            System.out.println("Mouse pressed on frame: " + fr.getTitle() + ", isComponentActivatedOnMousePress: " + fr.isComponentActivatedOnMousePress());
            if (fr.isComponentActivatedOnMousePress()) {
                try{
    //                System.out.println("Selecting frame");
                    fr.setActive(true);
                }catch (PropertyVetoException exc) {}
            }

            forwardMouseEvent(e);
        }
        /**
         * Forward the mouseEntered event to the underlying child container.
         * @see #mousePressed
         */
        public void mouseEntered(MouseEvent e) {
//            System.out.println("Forwarding entered");
            forwardMouseEvent(e);
        }
        /**
         * Forward the mouseMoved event to the underlying child container.
         * @see #mousePressed
         */
        public void mouseMoved(MouseEvent e) {
//            System.out.println("Forwarding moved");
            forwardMouseEvent(e);
        }
        /**
         * Forward the mouseExited event to the underlying child container.
         * @see #mousePressed
         */
        public void mouseExited(MouseEvent e) {
//            System.out.println("GLASSPANE mouseExited  dontForwardMouseExit: " +dontForwardMouseExit);
            if (dontForwardMouseExit) {
                dontForwardMouseExit = false;
            }else{
//                System.out.println("Forwarding exited");
                forwardMouseEvent(e);
            }
        }
        /**
         * Ignore mouseClicked events.
         * @see #mousePressed
         */
        public void mouseClicked(MouseEvent e) {
//            System.out.println("Forwarding mouseClicked");
            forwardMouseEvent(e);
        }
        /**
         * Ignore mouseReleased events.
         * @see #mousePressed
         */
        public void mouseReleased(MouseEvent e) {
//            System.out.println("Forwarding mouseReleased");
            forwardMouseEvent(e);
        }
        /**
         * Ignore mouseDragged events.
         * @see #mousePressed
         */
        public void mouseDragged(MouseEvent e) {
//            System.out.println("Forwarding mouseDragged");
            forwardMouseEvent(e);
        }

        /*
         * Forward a mouse event to the current mouse target, setting it
         * if necessary.
         */
        private void forwardMouseEvent(MouseEvent e) {
            Component target = findComponentAt(getFrame().getContentPane(),
                                           e.getX(), e.getY());
            if (target != mouseEventTarget) {
            	  setMouseTarget(target, e);
            }
            retargetMouseEvent(e.getID(), e);
        }

        private Component mouseEventTarget = null;

        /*
         * Find the lightweight child component which corresponds to the
         * specified location.  This is similar to the new 1.2 API in
         * Container, but we need to run on 1.1.  The other changes are
         * due to Container.findComponentAt's use of package-private data.
         */
        private Component findComponentAt(Container c, int x, int y) {
            if (!c.contains(x, y)) {
            	  return c;
            }
            int ncomponents = c.getComponentCount();
            Component component[] = c.getComponents();
            for (int i = 0 ; i < ncomponents ; i++) {
	              Component comp = component[i];
        	      Point loc = comp.getLocation();
            	  if ((comp != null) && (comp.contains(x - loc.x, y - loc.y)) &&
          	      (comp.getPeer() instanceof java.awt.peer.LightweightPeer) &&
	                (comp.isVisible() == true)) {
              	    // found a component that intersects the point, see if there
              	    // is a deeper possibility.
              	    if (comp instanceof Container) {
                	      Container child = (Container) comp;
                	      Point childLoc = child.getLocation();
                	      Component deeper = findComponentAt(child,
                  						 x - childLoc.x, y - childLoc.y);
                	      if (deeper != null) {
                        		return deeper;
                	      }
              	    } else {
                	      return comp;
            	      }
	              }
            }
            return c;
        }

        /*
         * Set the child component to which events are forwarded, and
         * synthesize the appropriate mouseEntered and mouseExited events.
         */
        private void setMouseTarget(Component target, MouseEvent e) {
            if (mouseEventTarget != null) {
            	  retargetMouseEvent(MouseEvent.MOUSE_EXITED, e);
            }
            mouseEventTarget = target;
            if (mouseEventTarget != null) {
                if (e.getID() != MouseEvent.MOUSE_ENTERED)
                	  retargetMouseEvent(MouseEvent.MOUSE_ENTERED, e);
            }
        }

        /*
         * Dispatch an event clone, retargeted for the current mouse target.
         */
        void retargetMouseEvent(int id, MouseEvent e) {
	          // fix for bug #4202966 -- hania
          	// When retargetting a mouse event, we need to translate
          	// the event's coordinates relative to the target.
          	Point p = javax.swing.SwingUtilities.convertPoint(getFrame().getContentPane(),
      					      e.getX(), e.getY(),
			      		      mouseEventTarget);
            MouseEvent retargeted = new MouseEvent(mouseEventTarget,
                                                   id,
                                                   e.getWhen(),
                                                   e.getModifiers(),
                                                   p.x,
                                                   p.y,
                                                   e.getClickCount(),
                                                   e.isPopupTrigger());
//            System.out.println("Dispatching mouse event id: " + id + " to component: " + mouseEventTarget);
            mouseEventTarget.dispatchEvent(retargeted);

            /* HACK....
             * Buttons need to get a mouseReleased event too, in order to have their actions executed.
             * So generate a mouseReleased event too, and send it to the appropriate button.
             */
/*            if (javax.swing.AbstractButton.class.isInstance(mouseEventTarget) && id == MouseEvent.MOUSE_PRESSED) {
//                System.out.println("Send a mouseReleased too");
                MouseEvent retargeted2 = new MouseEvent(mouseEventTarget,
                                                   MouseEvent.MOUSE_RELEASED,
                                                   e.getWhen(),
                                                   e.getModifiers(),
                                                   p.x,
                                                   p.y,
                                                   e.getClickCount(),
                                                   e.isPopupTrigger());
                mouseEventTarget.dispatchEvent(retargeted2);

            }
*/        }

    }

}

/////////////////////////////////////////////////////////////////////////
/// Border Listener Class for the ESlateInternalFrame.
/////////////////////////////////////////////////////////////////////////
/**
 * This class is taken from the BasicInternalFrameUI source. Listens for border adjustments.
 */
class BorderListener extends MouseInputAdapter implements javax.swing.SwingConstants {
    // _x & _y are the mousePressed location in absolute coordinate system
    int _x, _y;
  	// __x & __y are the mousePressed location in source view's coordinate system
	  int __x, __y;
    Rectangle startingBounds;
    int resizeDir;
//    JInternalFrame frame;
    ESlateBasicInternalFrameUI frameUI;
    /* GT refreshRect is used to perform repaints on the desktop pane. A problem in the
     * new scroll mechanism in Java 1.3 cause a lot of garbage on the desktop pane, when
     * the desktop pane has been auto-expanded and the dragged frame is moved towards the
     * center of the visible part of the desktop pane. We try to take care of the proplem in
     * the BorderListener of th frame.
     */
    Rectangle refreshRect = new Rectangle();

    protected final int RESIZE_NONE  = 0;
    int resizeCornerSize = 16;

    public BorderListener(JInternalFrame fr, ESlateBasicInternalFrameUI ui) {
//        frame = fr;
        frameUI = ui;
    }

    protected javax.swing.DesktopManager getDesktopManager() {
      	if(frameUI.getFrame().getDesktopPane() != null && frameUI.getFrame().getDesktopPane().getDesktopManager() != null)
      	    return frameUI.getFrame().getDesktopPane().getDesktopManager();
        return null;
//      	if(sharedDesktopManager == null)
//        	  sharedDesktopManager = createDesktopManager();
//      	return sharedDesktopManager;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1 && e.getSource() == frameUI.getNorthPane()) {
            if (frameUI.getFrame().isIconifiable() && frameUI.getFrame().isIcon()) {
                try {
                    frameUI.getFrame().setIcon(false);
                } catch (PropertyVetoException e2) { }
            }else if(frameUI.getFrame().isMaximizable()) {
                if(!frameUI.getFrame().isMaximum())
                    try { frameUI.getFrame().setMaximum(true); } catch (PropertyVetoException e2) { }
                else
                    try { frameUI.getFrame().setMaximum(false); } catch (PropertyVetoException e3) { }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(resizeDir == RESIZE_NONE)
            getDesktopManager().endDraggingFrame(frameUI.getFrame());
        else
            getDesktopManager().endResizingFrame(frameUI.getFrame());
        _x = 0;
        _y = 0;
        __x = 0;
        __y = 0;
        startingBounds = null;
        resizeDir = RESIZE_NONE;
    }

    public void mousePressed(MouseEvent e) {
        JInternalFrame frame = frameUI.getFrame();
        Point p = SwingUtilities.convertPoint((Component)e.getSource(),
                        e.getX(), e.getY(), null);
        __x = e.getX();
        __y = e.getY();
        _x = p.x;
        _y = p.y;
        startingBounds = frame.getBounds();

        OldESlateInternalFrame fr = (OldESlateInternalFrame) frameUI.getFrame();
//        System.out.println("BorderListener mouse pressed on frame: " + fr.getTitle() + ", isComponentActivatedOnMousePress: " + fr.isComponentActivatedOnMousePress());
        if (fr.isComponentActivatedOnMousePress()) {
            if(!frame.isSelected()) {
                try {
                    fr.setActive(true);
                } catch (PropertyVetoException e1) {
                }
            }
        }

        if(!frame.isResizable() || e.getSource() == frameUI.getNorthPane()) {
            resizeDir = RESIZE_NONE;
        		getDesktopManager().beginDraggingFrame(frame);
            return;
        }

        if(e.getSource() == frame) {
            Insets i = frame.getInsets();
            if(e.getX() <= i.left) {
                if(e.getY() < resizeCornerSize + i.top)
                    resizeDir = NORTH_WEST;
                else if(e.getY() > frame.getHeight() - resizeCornerSize - i.bottom)
                    resizeDir = SOUTH_WEST;
                else
                    resizeDir = WEST;
            }else if(e.getX() >= frame.getWidth() - i.right) {
                if(e.getY() < resizeCornerSize + i.top)
                    resizeDir = NORTH_EAST;
                else if(e.getY() > frame.getHeight() - resizeCornerSize - i.bottom)
                    resizeDir = SOUTH_EAST;
                else
                    resizeDir = EAST;
            }else if(e.getY() <= i.top) {
                if(e.getX() < resizeCornerSize + i.left)
                    resizeDir = NORTH_WEST;
                else if(e.getX() > frame.getWidth() - resizeCornerSize - i.right)
                    resizeDir = NORTH_EAST;
                else
                    resizeDir = NORTH;
            }else if(e.getY() >= frame.getHeight() - i.bottom) {
                if(e.getX() < resizeCornerSize + i.left)
                    resizeDir = SOUTH_WEST;
                else if(e.getX() > frame.getWidth() - resizeCornerSize - i.right)
                    resizeDir = SOUTH_EAST;
                else
                    resizeDir = SOUTH;
            }
	          getDesktopManager().beginResizingFrame(frame, resizeDir);
            return;
        }
    }

    public void mouseDragged(MouseEvent e) {
        JInternalFrame frame = frameUI.getFrame();
        if ( startingBounds == null ) {
	          // (STEVE) Yucky work around for bug ID 4106552
            return;
        }

        Point p;
	      int newX, newY, newW, newH;
        int deltaX;
        int deltaY;
	      Dimension min;
	      Dimension max;
        p = SwingUtilities.convertPoint((Component)e.getSource(),
                                        e.getX(), e.getY(), null);

        // Handle a MOVE
        if(e.getSource() == frameUI.getNorthPane()) {
            if (frame.isMaximum()) {
                return;  // don't allow moving of maximized frames.
            }
		        Insets i = frame.getInsets();
        		int pWidth, pHeight;
  		      Dimension s = frame.getParent().getSize();
        		pWidth = s.width;
        		pHeight = s.height;

	          newX = startingBounds.x - (_x - p.x);
	          newY = startingBounds.y - (_y - p.y);
        		// Make sure we stay in-bounds
        		if(newX + i.left <= -__x)
		            newX = -__x - i.left;
            // GT was removed so that a Frame can be dragged beyond the top edge of the
            // container for as much as is needed. This would normally be done for the left edge
            // of the Container too, but it is neglected on purpose since the width of the
            // component bar is much bigger that its height.
//		        if (newY + i.top <= -__y)
//                newY = -__y - i.top;
            if (newX + __x + i.right > pWidth)
                newX = pWidth - __x - i.right;
            if (newY + __y + i.bottom > pHeight)
                newY =  pHeight - __y - i.bottom;

            getDesktopManager().dragFrame(frame, newX, newY);

            /* GT
             * Workaround which eliminates the UI garbage created inside the desktop pane,
             * when auto-expansion has occured and the dragged frame is moved towards the
             * center of the visible area of the desktop pane.
             */
            ESlateContainerWindowsDesktopManager mgr = (ESlateContainerWindowsDesktopManager) getDesktopManager();
            if (mgr.container.autoExpandOccured2) {
//                System.out.println("Repainting");
                Rectangle frameRect = frame.getVisibleRect();
                frame.paintImmediately(frameRect);
                frame.getDesktopPane().paintImmediately(frame.getDesktopPane().getVisibleRect());
            }

            return;
        }

        if(!frame.isResizable()) {
            return;
        }

	      min = frame.getMinimumSize();
	      max = frame.getMaximumSize();

        deltaX = _x - p.x;
        deltaY = _y - p.y;

	      newX = frame.getX();
	      newY = frame.getY();
	      newW = frame.getWidth();
	      newH = frame.getHeight();

        switch(resizeDir) {
            case RESIZE_NONE:
                return;
            case NORTH:
            		if (startingBounds.height + deltaY < min.height)
            		    deltaY = -(startingBounds.height - min.height);
            		else if(startingBounds.height + deltaY > max.height)
            		    deltaY = (startingBounds.height - min.height);
            		newX = startingBounds.x;
            		newY = startingBounds.y - deltaY;
            		newW = startingBounds.width;
            		newH = startingBounds.height + deltaY;
                break;
            case NORTH_EAST:
            		if(startingBounds.height + deltaY < min.height)
            		    deltaY = -(startingBounds.height - min.height);
            		else if(startingBounds.height + deltaY > max.height)
            		    deltaY = (startingBounds.height - min.height);

	            	if (startingBounds.width - deltaX < min.width)
            		    deltaX = (startingBounds.width - min.width);
            		else if(startingBounds.width - deltaX > max.width)
            		    deltaX = -(startingBounds.width - min.width);

            		newX = startingBounds.x;
            		newY = startingBounds.y - deltaY;
            		newW = startingBounds.width - deltaX;
            		newH = startingBounds.height + deltaY;
                break;
            case EAST:
            		if(startingBounds.width - deltaX < min.width)
            		    deltaX = (startingBounds.width - min.width);
            		else if(startingBounds.width - deltaX > max.width)
            		    deltaX = -(startingBounds.width - min.width);

            		newW = startingBounds.width - deltaX;
            		newH = startingBounds.height;
                break;
            case SOUTH_EAST:
            		if (startingBounds.width - deltaX < min.width)
            		    deltaX = (startingBounds.width - min.width);
            		else if(startingBounds.width - deltaX > max.width)
            		    deltaX = -(startingBounds.width - min.width);

            		if (startingBounds.height - deltaY < min.height)
            		    deltaY = (startingBounds.height - min.height);
            		else if (startingBounds.height - deltaY > max.height)
            		    deltaY = -(startingBounds.height - min.height);

            		newW = startingBounds.width - deltaX;
            		newH = startingBounds.height - deltaY;
                break;
            case SOUTH:
    		        if (startingBounds.height - deltaY < min.height)
            		    deltaY = (startingBounds.height - min.height);
            		else if(startingBounds.height - deltaY > max.height)
            		    deltaY = -(startingBounds.height - min.height);

             		newW = startingBounds.width;
            		newH = startingBounds.height - deltaY;
                break;
            case SOUTH_WEST:
            		if(startingBounds.height - deltaY < min.height)
            		    deltaY = (startingBounds.height - min.height);
            		else if(startingBounds.height - deltaY > max.height)
            		    deltaY = -(startingBounds.height - min.height);

              	if (startingBounds.width + deltaX < min.width)
            		    deltaX = -(startingBounds.width - min.width);
            		else if(startingBounds.width + deltaX > max.width)
                    deltaX = (startingBounds.width - min.width);

              	newX = startingBounds.x - deltaX;
            		newY = startingBounds.y;
            		newW = startingBounds.width + deltaX;
              	newH = startingBounds.height - deltaY;
                break;
            case WEST:
            		if(startingBounds.width + deltaX < min.width)
            		    deltaX = -(startingBounds.width - min.width);
                else if(startingBounds.width + deltaX > max.width)
            		    deltaX = (startingBounds.width - min.width);

            		newX = startingBounds.x - deltaX;
            		newY = startingBounds.y;
            		newW = startingBounds.width + deltaX;
            		newH = startingBounds.height;
                break;
            case NORTH_WEST:
            		if (startingBounds.width + deltaX < min.width)
		                deltaX = -(startingBounds.width - min.width);
            		else if (startingBounds.width + deltaX > max.width)
            		    deltaX = (startingBounds.width - min.width);

            		if (startingBounds.height + deltaY < min.height)
            		    deltaY = -(startingBounds.height - min.height);
            		else if(startingBounds.height + deltaY > max.height)
            		    deltaY = (startingBounds.height - min.height);

            		newX = startingBounds.x - deltaX;
            		newY = startingBounds.y - deltaY;
            		newW = startingBounds.width + deltaX;
            		newH = startingBounds.height + deltaY;
                break;
            default:
                return;
        }
      	getDesktopManager().resizeFrame(frame, newX, newY, newW, newH);
    }

/*    public void mouseEntered(MouseEvent e)    {
        if (e.getSource() == frameUI.getNorthPane())
            titlePanelMouseMoved(e);
        else
            componentMouseMoved(e);
    }
*/
    public void mouseMoved(MouseEvent e)    {
        OldESlateInternalFrame frame = (OldESlateInternalFrame) frameUI.getFrame();
//        Component comp = ((Component) e.getSource());
//        System.out.println("mouseMoved frame");
        if (!frame.isResizable() || frame.isMaximum())
        		return;
        if(e.getSource() == frame) {
            Insets i = frame.getInsets();
            if (e.getX() <= i.left) {
//                System.out.println("Here 1");
                if (e.getY() < resizeCornerSize + i.top) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                    frame.setBorder(frame.NORTH_WEST_BORDER);
                }else if(e.getY() > frame.getHeight() - resizeCornerSize - i.bottom) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                    frame.setBorder(frame.SOUTH_WEST_BORDER);
                }else{
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    frame.setBorder(frame.WEST_BORDER);
                }
      		      return;
            }else if (e.getX() >= frame.getWidth() - i.right) {
//                System.out.println("Here 2");
                if(e.getY() < resizeCornerSize + i.top) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    frame.setBorder(frame.NORTH_EAST_BORDER);
                }else if(e.getY() > frame.getHeight() - resizeCornerSize - i.bottom) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    frame.setBorder(frame.SOUTH_EAST_BORDER);
                }else{
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    frame.setBorder(frame.EAST_BORDER);
                }
      		      return;
            }else if (e.getY() <= i.top) {
//                System.out.println("Here 3");
                if (e.getX() < resizeCornerSize + i.left) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                    frame.setBorder(frame.NORTH_WEST_BORDER);
                }else if (e.getX() > frame.getWidth() - resizeCornerSize - i.right) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    frame.setBorder(frame.NORTH_EAST_BORDER);
                }else{
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    frame.setBorder(frame.NORTH_BORDER);
                }
      		      return;
            }else if (e.getY() >= frame.getHeight() - i.bottom) {
//                System.out.println("Here 4");
                if(e.getX() < resizeCornerSize + i.left) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                    frame.setBorder(frame.SOUTH_WEST_BORDER);
                }else if(e.getX() > frame.getWidth() - resizeCornerSize - i.right) {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    frame.setBorder(frame.SOUTH_EAST_BORDER);
                }else {
              			frame.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                    frame.setBorder(frame.SOUTH_BORDER);
                }
      		      return;
            }
        }
	      frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        frame.restoreBorder(); //frame.FRAME_NORMAL_BORDER);
    }

    public void mouseExited(MouseEvent e)    {
        /* Discard mouseExited events that occur when a frame is being dragged. This was
         * the reason why the NORTH_SOUTH_WEST_EAST_BORDER border (which is given to a frame
         * when dragging starts) dissapeared suddenly while it was dragged around.
         * This was also the reason the border used while resizing a frame dissappeared.
         */
        ESlateContainerWindowsDesktopManager mgr = (ESlateContainerWindowsDesktopManager) getDesktopManager();
        if (mgr.resizingFrame || mgr.draggingFrame)
            return;
        /* If the frame is maximized, don't mess around with its border, cause it needs none!*/
        if (frameUI.getFrame().isMaximum())
            return;

        frameUI.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        ((OldESlateInternalFrame) frameUI.getFrame()).restoreBorder(); // ((ESlateInternalFrame) frameUI.getFrame()).FRAME_NORMAL_BORDER);
  	}

/*    private void componentMouseMoved(MouseEvent e) {
        ESlateInternalFrame frame = (ESlateInternalFrame) frameUI.getFrame();
        Component comp = ((Component) e.getSource());
//        System.out.println("mouseMoved frame");
        if (!frame.isResizable())
        		return;
//        System.out.println("e.getSource() == frame : " + (e.getSource() == frame));
//        if(e.getSource() == frame) {
            Insets i = new Insets(4, 4, 4, 4); //frame.getInsets();
//            System.out.println("e.getPoint(): " + e.getPoint());
            if (e.getX() <= i.left) {
//                System.out.println("Here 1");
                if (!frame.isTitlePanelVisible() && e.getY() < resizeCornerSize + i.top)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                else if(e.getY() > comp.getSize().height - resizeCornerSize - i.bottom)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                else{
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
//                    System.out.println("Setting W_RESIZE_CURSOR cursor");
                }
      		      return;
            }else if (e.getX() >= comp.getSize().width - i.right) {
//                System.out.println("Here 2");
                if(!frame.isTitlePanelVisible() && e.getY() < resizeCornerSize + i.top)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                else if(e.getY() > comp.getSize().height - resizeCornerSize - i.bottom)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                else
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
      		      return;
            }else if (e.getY() <= i.top) {
//                System.out.println("Here 3 frame.isTitlePanelVisible(): " + frame.isTitlePanelVisible());
                if (frame.isTitlePanelVisible()) {
            	      comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                if (!frame.isTitlePanelVisible() && e.getX() < resizeCornerSize + i.left)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                else if (!frame.isTitlePanelVisible() && e.getX() > comp.getSize().width - resizeCornerSize - i.right)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                else
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
      		      return;
            }else if (e.getY() >= comp.getSize().height - i.bottom) {
//                System.out.println("Here 4");
                if(e.getX() < resizeCornerSize + i.left)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                else if(e.getX() > comp.getSize().width - resizeCornerSize - i.right)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                else
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
      		      return;
            }
//        }
	      comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void titlePanelMouseMoved(MouseEvent e) {
        ESlateInternalFrame frame = (ESlateInternalFrame) frameUI.getFrame();
        Component comp = ((Component) e.getSource());
//        System.out.println("mouseMoved frame");
        if (!frame.isResizable())
        		return;
//        System.out.println("e.getSource() == frame : " + (e.getSource() == frame));
//        if(e.getSource() == frame) {
            Insets i = new Insets(4, 4, 4, 4); //frame.getInsets();
//            System.out.println("e.getPoint(): " + e.getPoint());
            if (e.getX() <= i.left) {
//                System.out.println("Here 1");
                if (e.getY() < resizeCornerSize + i.top)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                else if(e.getY() > comp.getSize().height - resizeCornerSize - i.bottom)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                else{
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
//                    System.out.println("Setting W_RESIZE_CURSOR cursor");
                }
      		      return;
            }else if (e.getX() >= comp.getSize().width - i.right) {
//                System.out.println("Here 2");
                if(e.getY() < resizeCornerSize + i.top)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                else if(e.getY() > comp.getSize().height - resizeCornerSize - i.bottom)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                else
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
      		      return;
            }else if (e.getY() <= i.top) {
//                System.out.println("Here 3 frame.isTitlePanelVisible(): " + frame.isTitlePanelVisible());
                if (e.getX() < resizeCornerSize + i.left)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                else if (e.getX() > comp.getSize().width - resizeCornerSize - i.right)
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                else
              			comp.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
      		      return;
            }else if(e.getY() >= comp.getSize().height - i.bottom) {
                comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
//        }
	      comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
*/
}    /// End BorderListener Class

