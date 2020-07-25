package gr.cti.eslate.base;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

class ESlateBasicInternalFrameUI extends javax.swing.plaf.basic.BasicInternalFrameUI {
    public MouseInputListener glassPaneDispatcher = null;

    public static javax.swing.plaf.ComponentUI createUI(JComponent b)    {
        return new ESlateBasicInternalFrameUI((JInternalFrame)b);
    }
    public ESlateBasicInternalFrameUI(JInternalFrame b)   {
        super(b);
    }
    protected MouseInputListener createGlassPaneDispatcher() {
              return (glassPaneDispatcher = new GlassPaneDispatcher2());
    }
//    protected MouseInputAdapter createBorderListener(JInternalFrame w) {
//        return new BorderListener2();
//    }
    protected JInternalFrame getFrame() {
        return frame;
    }
    public MouseInputAdapter getBorderlListener() {
        return borderListener;
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
         * In keepingg with the MDI messaging model (which JInternalFrame
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
            try{
//                System.out.println("Selecting frame");
                getFrame().setSelected(true);
            }catch (PropertyVetoException exc) {}

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
        private void forwardMouseEvent(MouseEvent e)
        {
          int id = e.getID();
          if (id != MouseEvent.MOUSE_DRAGGED) {
            Component target =
              findComponentAt(getFrame().getContentPane(), e.getX(), e.getY());
            if (id==MouseEvent.MOUSE_RELEASED && target instanceof JScrollPane){
              target = ((JScrollPane)target).getViewport().getView();
            }
            if (target != mouseEventTarget) {
              setMouseTarget(target, e);
            }
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
        @SuppressWarnings(value={"deprecation"})
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
