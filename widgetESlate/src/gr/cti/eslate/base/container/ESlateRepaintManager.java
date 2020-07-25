package gr.cti.eslate.base.container;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;


/** This is the custom RepaintManager used by E-Slate. The extra feature of
 *  this manager is that it becomes inactive while a microworld is being
 *  loaded. While inactive none of its method functions. This speeds up
 *  microworld loading and also ensures that the microworld will appears in
 *  one step and not in pieces. This is not always true though. If a
 *  component's paintImmediately() is called while the microworld is loading
 *  the component is repainted without the use of the RepaintManager and
 *  therefore the RepaintManager cannot do anything about it. To avoid this
 *  the components must check the state of the ESlateRepaintManager in their
 *  paint() method, before performing any painting.
 */
public class ESlateRepaintManager extends RepaintManager {
        boolean active = true;
        ESlateContainer container = null;
		ArrayList componentsToRepaintAfterMicroworldLoad = new ArrayList();

        ESlateRepaintManager(ESlateContainer container) {
            super();
            this.container = container;
        }

        public boolean isActive() {
            return active;
        }

        public void paintDirtyRegions() {
            if (!active || container.isMicroworldLoading()) {
//System.out.println("paintDirtyRegions() do nothing");
                return;
            }
long st = System.currentTimeMillis();
            super.paintDirtyRegions();
//System.out.println("this: " + this);
//System.out.println("paintDirtyRegions(): " + (System.currentTimeMillis()-st));
        }

        public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
            if (!active) {
//                    System.out.println("Skipping addDirtyRegion() : " + c.getClass());
                return;
            }
            super.addDirtyRegion(c, x, y, w, h);
        }

        public Rectangle getDirtyRegion(JComponent aComponent) {
            if (!active) {
//                    System.out.println("Skipping addDirtyRegion() : " + c.getClass());
                return null;
            }
            return super.getDirtyRegion(aComponent);
        }

        public void markCompletelyDirty(JComponent aComponent) {
            if (!active) {
//                    System.out.println("Skipping addDirtyRegion() : " + c.getClass());
                return;
            }
            super.markCompletelyDirty(aComponent);
        }

        public void validateInvalidComponents() {
            if (!active) {
//                    System.out.println("Skipping addDirtyRegion() : " + c.getClass());
                return;
            }
            super.validateInvalidComponents();
        }

        public void addInvalidComponent(JComponent invalidComponent) {
            if (!active) {
//                    System.out.println("Skipping addDirtyRegion() : " + c.getClass());
                return;
            }
            super.addInvalidComponent(invalidComponent);
        }

/*            public java.awt.Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
            if (skipRepainting) {
                System.out.println("Skipping getOffscreenBuffer() : " + c.getClass());
                return null;
            }
            return super.getOffscreenBuffer(c, proposedWidth, proposedHeight);
        }

		public void forceRepaintOnStart(Component comp) {
			forceRepaintOnMicroworldLoad(comp);
		}
*/
		public void forceRepaintOnMicroworldLoad(Component comp) {
			if (componentsToRepaintAfterMicroworldLoad.contains(comp)) {
				return;
			}
			componentsToRepaintAfterMicroworldLoad.add(comp);
		}



		Rectangle tmp = new Rectangle();

		void collectDirtyComponents(Hashtable dirtyComponents,
					JComponent dirtyComponent,
					Vector roots) {
			int dx, dy, rootDx, rootDy;
			Component component, rootDirtyComponent,parent;
		//Rectangle tmp;
			Rectangle cBounds;
			boolean opaqueAncestorFound = false;

			// Find the highest parent which is dirty.  When we get out of this
			// rootDx and rootDy will contain the translation from the
			// rootDirtyComponent's coordinate system to the coordinates of the
			// original dirty component.  The tmp Rect is also used to compute the
			// visible portion of the dirtyRect.

			component = rootDirtyComponent = dirtyComponent;

			cBounds = dirtyComponent.getBounds();

			dx = rootDx = 0;
			dy = rootDy = 0;
			tmp.setBounds((Rectangle) dirtyComponents.get(dirtyComponent));

			// System.out.println("Collect dirty component for bound " + tmp +
			//                                   "component bounds is " + cBounds);;
			SwingUtilities.computeIntersection(0,0,cBounds.width,cBounds.height,tmp);

			if (tmp.isEmpty()) {
				// System.out.println("Empty 1");
				return;
			}

			if(dirtyComponent.isOpaque())
				opaqueAncestorFound = true;

			for(;;) {
				parent = component.getParent();
				if(parent == null)
					break;

				if(!(parent instanceof JComponent))
					break;

				component = parent;

				if(((JComponent)component).isOpaque())
					opaqueAncestorFound = true;

				dx += cBounds.x;
				dy += cBounds.y;
				tmp.setLocation(tmp.x + cBounds.x,
								tmp.y + cBounds.y);

				cBounds = ((JComponent)component).getBounds();
				tmp = SwingUtilities.computeIntersection(0,0,cBounds.width,cBounds.height,tmp);

				if (tmp.isEmpty()) {
					// System.out.println("Empty 2");
					return;
				}

				if (dirtyComponents.get(component) != null) {
					rootDirtyComponent = component;
					rootDx = dx;
					rootDy = dy;
				}
			}

			if (dirtyComponent != rootDirtyComponent) {
			Rectangle r;
				tmp.setLocation(tmp.x + rootDx - dx,
					tmp.y + rootDy - dy);
			r = (Rectangle)dirtyComponents.get(rootDirtyComponent);
			SwingUtilities.computeUnion(tmp.x,tmp.y,tmp.width,tmp.height,r);
			}

			// If we haven't seen this root before, then we need to add it to the
			// list of root dirty Views.

			if (!roots.contains(rootDirtyComponent))
				roots.addElement(rootDirtyComponent);
		}

}
