package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.event.ESlateComponentEvent;
import gr.cti.eslate.base.container.event.ESlateInternalFrameEvent;
import gr.cti.eslate.base.container.event.ESlateInternalFrameListener;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameTitlePanel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * Title:
 * Description: This class provides methods which offer support in the use of the
 * ESlateInternalFrames. These methods are here because the ESlateContainer class
 * is oveloaded. Some of these methods which may be usefull will also appear in the
 * public API of the ESlateContainer class. However the real job will be delegated to
 * methods of this class.
 * to support
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class ESlateInternalFrameUtils {
    ESlateContainer container = null;
    ArrayList frameAdapters = new ArrayList();
    ArrayList frames = new ArrayList();
    /* The listener which is added to the ESlateInternalFrames' title panel and
     * shows the componentPopupMenu.
     */
    MouseListener titlePanelMouseListener = null;
    PropertyChangeListener framePropertyListener = null;
    ESlateInternalFrameListener internalFrameListener = null;

    public ESlateInternalFrameUtils(ESlateContainer container) {
        this.container = container;
    }

    void adjustTitlePanelToMicroworldSettings(ESlateInternalFrame frame) {
        frame.adjustTitlePanelButtons(container.currentView.minimizeButtonVisible,
                container.currentView.maximizeButtonVisible,
                container.currentView.closeButtonVisible,
                container.currentView.helpButtonVisible,
                container.currentView.pinButtonVisible,
                container.currentView.infoButtonVisible);

//if        frame.setComponentNameChangeableFromMenuBar(container.currentView.controlBarTitleActive);
        frame.setResizable(container.currentView.resizeAllowed);
        frame.setTitlePanelVisible(container.currentView.controlBarsVisible);
        frame.setIconifiable(container.currentView.componentMinimizeAllowed);
        frame.setMaximizable(container.currentView.componentMaximizeAllowed);
        if (container.microworld != null)
            frame.setClosable(container.microworld.isComponentRemovalAllowed());
        else
            frame.setClosable(true);
    }

    public ESlateInternalFrameComponentAdapter getFrameAdapter(ESlateInternalFrame f) {
        int index = frames.indexOf(f);
        if (index == -1) return null;
        return (ESlateInternalFrameComponentAdapter) frameAdapters.get(index);
    }

    public void addComponentListener(ESlateInternalFrame frame) {
        ESlateInternalFrameComponentAdapter adapter = new ESlateInternalFrameComponentAdapter(container);
        frame.addComponentListener(adapter);
        frameAdapters.add(adapter);
        frames.add(frame);
    }

    public void removeComponentListener(ESlateInternalFrame f) {
        int index = frames.indexOf(f);
        if (index == -1) return;
        ESlateInternalFrameComponentAdapter adapter = (ESlateInternalFrameComponentAdapter) frameAdapters.get(index);
        f.removeComponentListener(adapter);
        frameAdapters.remove(index);
        frames.remove(index);
    }

    public MouseListener getTitlePanelMouseListener() {
        if (titlePanelMouseListener == null) {
            titlePanelMouseListener = new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    if (!(container instanceof ESlateComposer)) return;
                    if (SwingUtilities.isRightMouseButton(e)) {
                        ESlateInternalFrameTitlePanel title = (ESlateInternalFrameTitlePanel) e.getSource();
                        ESlateInternalFrame frame = title.getFrame();
                        Point p = SwingUtilities.convertPoint(title, e.getX(), e.getY(), frame);
                        ((ESlateComposer) container).showComponentPopup(frame, p.x, p.y);
                    }
                }
            };
        }
        return titlePanelMouseListener;
    }

    public void addTitlePanelMouseListener(ESlateInternalFrame f) {
        if (f.getTitlePanel() instanceof ESlateInternalFrameTitlePanel) {
            ((ESlateInternalFrameTitlePanel) f.getTitlePanel()).addMouseListener(getTitlePanelMouseListener());
        }
    }

    public void removeTitlePanelMouseListener(ESlateInternalFrame f) {
        if (f.getTitlePanel() instanceof ESlateInternalFrameTitlePanel)
            ((ESlateInternalFrameTitlePanel) f.getTitlePanel()).removeMouseListener(getTitlePanelMouseListener());
    }

    public PropertyChangeListener getFramePropertyListener() {
        if (framePropertyListener == null) {
            framePropertyListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    String propertyName = e.getPropertyName();

                    if (propertyName == ESlateInternalFrame.IS_ICON_PROPERTY) {
                    }else if (propertyName == ESlateInternalFrame.IS_MAXIMUM_PROPERTY) {
                        ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                        ESlateComponent eslateComponent =
                            container.mwdComponents.getComponent(frame);
                        boolean maximum = ((Boolean) e.getNewValue()).booleanValue();
                        if (maximum) {
                            /* Set the window equal to the view port of the scroll pane of the desktop pane
                             * of the container. Take into account the offset's supported by DesktopPane.
                             */
                            Point location = container.scrollPane.getViewport().getViewPosition();
                            Dimension size = container.scrollPane.getViewport().getSize();
                            Point lowerRightCorner = new Point(location.x + size.width, location.y + size.height);

                            Point upperLeftCornerOffset = frame.getDesktopPane().getMaximumTopLeftCornerOffset();
                            location.x = location.x + upperLeftCornerOffset.x;
                            location.y = location.y + upperLeftCornerOffset.y;

                            Point lowerRightCornerOffset = frame.getDesktopPane().getMaximumBottomRightCornerOffset();
                            lowerRightCorner.x = lowerRightCorner.x - lowerRightCornerOffset.x;
                            lowerRightCorner.y = lowerRightCorner.y - lowerRightCornerOffset.y;

                            frame.setLocation(location);
                            frame.setSize(lowerRightCorner.x - location.x, lowerRightCorner.y - location.y);
                            if (frame.isIcon())
                                frame.setVisible(true);
                        }else{
                            frame.setLocation(frame.getDesktopItemRestoreLocation());
                            frame.setSize(frame.getDesktopItemRestoreSize());
                        }

                        if (frame.isIcon() && maximum) {
                            try{
                                frame.setIcon(false);
                            }catch (Exception exc) {}
                        }
                    }else if (propertyName == ESlateInternalFrame.IS_CLOSED_PROPERTY) {
                    }else if (propertyName == ESlateInternalFrame.RESIZABLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.NAME_EDITABLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.ICONIFIABLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.IS_COMPONENT_ACTIVATED_ON_MOUSE_CLICK_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.MAXIMIZABLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.CLOSABLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.PLUG_BUTTON_VISIBLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.HELP_BUTTON_VISIBLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.INFO_BUTTON_VISIBLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.IS_TITLE_PANEL_VISIBLE_PROPERTY) {
                        container.setMicroworldChanged(true);
                    }else if (propertyName == ESlateInternalFrame.IS_SELECTED_PROPERTY) {
                    }
                }
            };
        }
        return framePropertyListener;
    }

    public void addPropertyChangeListener(ESlateInternalFrame frame) {
        frame.addPropertyChangeListener(getFramePropertyListener());
    }

    public void removePropertyChangeListener(ESlateInternalFrame frame) {
        if (framePropertyListener != null)
            frame.removePropertyChangeListener(framePropertyListener);
    }

    public ESlateInternalFrameListener getESlateInternalFrameListener() {
        if (internalFrameListener == null) {
            internalFrameListener = new ESlateInternalFrameListener() {
                public void internalFrameOpened(ESlateInternalFrameEvent e) {
                }
                public void internalFrameClosing(ESlateInternalFrameEvent e) {
                    ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                    ESlateComponent eslateComponent =
                        container.mwdComponents.getComponent(frame);
//                    boolean closed = ((Boolean) e.getNewValue()).booleanValue();
                    if (eslateComponent != null) {
                        container.removeComponent(eslateComponent, true, true);
//                        if (closed)
                    }
                }
                public void internalFrameClosed(ESlateInternalFrameEvent e) {
                }
                public void internalFrameIconified(ESlateInternalFrameEvent e) {
                    ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                    ESlateComponent eslateComponent =
                        container.mwdComponents.getComponent(frame);
//                    boolean icon = ((Boolean) e.getNewValue()).booleanValue();
                    frame.setVisible(false);
                    if (eslateComponent != null) {
//                        if (icon)
                            eslateComponent.fireComponentIconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ICONIFIED, eslateComponent.handle.getComponentName()));
//                        else
//                            eslateComponent.fireComponentDeiconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEICONIFIED, eslateComponent.handle.getComponentName()));
                    }
                }
//                public void internalFrameDeiconified(ESlateInternalFrameEvent e) {
                public void internalFrameRestored(ESlateInternalFrameEvent e) {
                    ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                    ESlateComponent eslateComponent =
                        container.mwdComponents.getComponent(frame);
//                    boolean icon = ((Boolean) e.getNewValue()).booleanValue();
                    frame.setVisible(true);
                    if (eslateComponent != null) {
//                        if (icon)
//                            eslateComponent.fireComponentIconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ICONIFIED, eslateComponent.handle.getComponentName()));
//                        else
//                            eslateComponent.fireComponentDeiconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEICONIFIED, eslateComponent.handle.getComponentName()));
                            eslateComponent.fireComponentRestored(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_RESTORED, eslateComponent.handle.getComponentName()));
                    }
                }
                public void internalFrameMaximized(ESlateInternalFrameEvent e) {
                    ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                    ESlateComponent eslateComponent =
                        container.mwdComponents.getComponent(frame);
                    if (eslateComponent != null) {
                        eslateComponent.fireComponentMaximized(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_MAXIMIZED, eslateComponent.handle.getComponentName()));
                    }
                }
                public void internalFrameActivated(ESlateInternalFrameEvent e) {
                    ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                    ESlateComponent eslateComponent =
                        container.mwdComponents.getComponent(frame);
//                    boolean active = ((Boolean) e.getNewValue()).booleanValue();
//System.out.println("internalFrameActivated frame: " + frame.getTitle() + ", eslateComponent: " + eslateComponent);
                    if (eslateComponent != null) {
//                        if (active)
                            eslateComponent.fireComponentActivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ACTIVATED, eslateComponent.handle.getComponentName()));
//                        else
//                            eslateComponent.fireComponentDeactivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEACTIVATED, eslateComponent.handle.getComponentName()));
                    }
                }
                public void internalFrameDeactivated(ESlateInternalFrameEvent e) {
                    ESlateInternalFrame frame = (ESlateInternalFrame) e.getSource();
                    ESlateComponent eslateComponent =
                        container.mwdComponents.getComponent(frame);
//                    boolean active = ((Boolean) e.getNewValue()).booleanValue();
                    if (eslateComponent != null) {
//                        if (active)
//                            eslateComponent.fireComponentActivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ACTIVATED, eslateComponent.handle.getComponentName()));
//                        else
                            eslateComponent.fireComponentDeactivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEACTIVATED, eslateComponent.handle.getComponentName()));
                    }
                }
            };
        }
        return internalFrameListener;
    }

    public void addFrameListener(ESlateInternalFrame frame) {
        frame.addESlateInternalFrameListener(getESlateInternalFrameListener());
    }

    public void removeFrameListener(ESlateInternalFrame frame) {
        if (internalFrameListener != null)
            frame.removeESlateInternalFrameListener(internalFrameListener);
    }

    class ESlateInternalFrameComponentAdapter extends ComponentAdapter {
        protected boolean disableMoveListener = false;
        ESlateContainer container = null;

        public ESlateInternalFrameComponentAdapter(ESlateContainer container) {
            this.container = container;
        }

        public void componentMoved(ComponentEvent e) {
//                System.out.println("Component moved");
            if (container.disableComponentMoveListener) {
//                    System.out.println("setting disableComponentMoveListener to false");
                container.disableComponentMoveListener = false;
                return;
            }

            if (disableMoveListener) {
                disableMoveListener = false;
                return;
            }

            if (container.servingComponentEvent)
                return;
            container.servingComponentEvent = true;
            ESlateInternalFrame fr = (ESlateInternalFrame) e.getSource();

//                System.out.println("Component moved--> " +  fr.getTitle());
            Rectangle lcBounds = container.lc.getBounds();
            Rectangle compoBounds = fr.getBounds();

            /* If the scrollPane's highlight rectangle is not empty, which means that
             * some component is highlighted and if the part por all of the highlightRect
             * is inside this component, then invalidate the highlight rectangle.
             */
            if (!container.scrollPane.highlightRect.isEmpty()) {
                if (!container.scrollPane.highlightRect.intersection(compoBounds).isEmpty()) {
                    container.scrollPane.invalidateHighlightRect();
                }
            }

            /* Auto-expand behaviour. When a component is moved around and it reaches the end
             * of the desktop pane (caution: not the end of the visible area of the pane, but the
             * end of the desktop pane itself), then the desktop pane expands automatically, to the
             * direction the component is moved.
             */
            MicroworldView currentView = container.currentView;
            if (currentView.mwdResizable && currentView.mwdAutoExpandable && (compoBounds.x < 0 || compoBounds.y < 0)) {
                int xDiff = compoBounds.x;
                int yDiff = compoBounds.y;
                /* When component is moved beyond the left/top borders of the Container, the latter
                 * increases size in steps equal to the grid's step. To enable this, dragFrame() and
                 * 'endDraggingFrame()' methods of the Desktop manager have been overriden.
                 */
//                    if (lc.gridVisible) {
                    if (xDiff < 0) {
                        int times = Math.abs(xDiff / container.lc.gridStep) + 1;
                        xDiff = -container.lc.gridStep * times;
//                            System.out.println("times: " + times + ", xDiff: " + xDiff);
                        container.jumpedPixelsX += container.lc.gridStep*times;
                    }
                    if (yDiff < 0) {
                        int times = Math.abs(yDiff / container.lc.gridStep) + 1;
                        yDiff = -container.lc.gridStep * times;
                        container.jumpedPixelsY += container.lc.gridStep*times;
                    }
//                    }

                if (xDiff > 0) xDiff = 0; else xDiff = -xDiff;
                if (yDiff > 0) yDiff = 0; else yDiff = -yDiff;
                container.autoExpandOccured = true;
                ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
/*0                    for (int i=0; i<components.size(); i++) {
                    int frameIndex = ((Integer) componentFrameIndex.at(i)).intValue();
                    ESlateInternalFrame fr1 = null;
                    if (frameIndex != -1)
                        fr1 = (ESlateInternalFrame) componentFrames.at(frameIndex);
                    if (fr != null && !fr1.isFrozen()) {
*/
                for (int i=0; i<frames.length; i++) {
                    ESlateInternalFrame fr1 = frames[i];
//if                    if (!fr1.isFrozen()) {
                        Rectangle rect = fr1.getBounds();
//                                System.out.println("rect.x: " + rect.x + ", xDiff: " + xDiff);
                        container.frameUtils.getFrameAdapter(fr1).disableMoveListener = true;
                        fr1.setBounds(rect.x + xDiff, rect.y + yDiff, rect.width, rect.height);
//                            System.out.println("setBounds 1");
//if                    }else{
//if                        Component comp = (Component) mwdComponents.getComponent(fr1).object;
//0                            Component comp = (Component) components.at(i);
//if                        Rectangle rect = comp.getBounds();
//if                        container.disableComponentMoveListener = true;
//if                        comp.setBounds(rect.x + xDiff, rect.y + yDiff, rect.width, rect.height);
//                            System.out.println("setBounds 2");

                        /* If the frame is frozen, we must move it, so that when it becomes dun-frozen
                         * it will be located at the same place as the already moved component.
                         */
//if                        if (fr1 != null && fr1.isFrozen()) {
//if                            Rectangle rect1 = fr1.getBounds();
//if                            fr1.getFrameAdapter().disableMoveListener = true;
//if                            fr1.setBounds(rect1.x + xDiff, rect1.y + yDiff, rect1.width, rect1.height);
//                                System.out.println("setBounds 3");
//if                        }
//if                    }
                }
                Dimension newSize = new Dimension(lcBounds.width + xDiff, lcBounds.height + yDiff);
//                    System.out.println("xDiff: " + xDiff + ", newSize: " + newSize);
                container.lc.setPreferredSize(newSize);
                container.lc.setMinimumSize(newSize);
                container.lc.setMaximumSize(newSize);
                fr.revalidate();
                container.scrollPane.revalidate();
                container.lc.myValidate();
            }
            if (currentView.mwdResizable && currentView.mwdAutoExpandable && ((compoBounds.x+compoBounds.width) > lcBounds.width || (compoBounds.y + compoBounds.height) > lcBounds.height)) {
                int xDiff = -(lcBounds.width-(compoBounds.x+compoBounds.width));
                int yDiff = -(lcBounds.height-(compoBounds.y+compoBounds.height));
                if (yDiff < 0) yDiff = 0;
                if (xDiff < 0) xDiff = 0;
                Dimension newSize = new Dimension(lcBounds.width + xDiff, lcBounds.height + yDiff);
//                    System.out.println("xDiff: " + xDiff + ", newSize: " + newSize);
                container.lc.setPreferredSize(newSize);
                container.lc.setMinimumSize(newSize);
                container.lc.setMaximumSize(newSize);

                int extentWidth = container.scrollPane.getViewport().getExtentSize().width;
                int viewWidth = container.scrollPane.getViewport().getViewSize().width;
                int extentHeight = container.scrollPane.getViewport().getExtentSize().height;
                int viewHeight = container.scrollPane.getViewport().getViewSize().height;
                int xPos = 0, yPos = 0;
                if ((compoBounds.x+compoBounds.width) > lcBounds.width)
                    xPos = viewWidth-extentWidth;
                else
                    xPos = container.scrollPane.getViewport().getViewPosition().x + xDiff;
                if ((compoBounds.y + compoBounds.height) > lcBounds.height)
                    yPos = viewHeight-extentHeight;
                else
                    yPos = container.scrollPane.getViewport().getViewPosition().y + yDiff;

                container.scrollPane.getViewport().setViewPosition(new Point(xPos, yPos));
//                    System.out.println("Setting view position 7");
                container.scrollPane.getVerticalScrollBar().repaint();
                container.autoExpandOccured = true;
                container.autoExpandOccured2 = true;
                container.scrollPane.revalidate();
            }

            /* Auto-scroll behaviour: when a component is moved around and any of its sides gets out
             * of the visible rectangle of the viewport of the desktop pane, the desktop pane scrolls,
             * so that all the component is visible.
             */
//                System.out.println("autoExpandOccured: " + autoExpandOccured);
            if (currentView.mwdAutoScrollable && !container.autoExpandOccured) {
                container.ensureFrameIsVisible(null, compoBounds, false);
            }

            container.autoExpandOccured = false;
            container.servingComponentEvent = false;
        }

        public void componentResized(ComponentEvent e) {
            /* Revalidate the menuPanel of the component
             */
            ESlateInternalFrame fr = (ESlateInternalFrame) e.getSource();

            container.disableComponentMoveListener = true;
            Rectangle lcBounds = container.lc.getBounds();
            Rectangle compoBounds = fr.getBounds();
            boolean b1 = false, b2 = false;
//                System.out.println("Component resized " + compoBounds);

            /* Auto-expand behaviour. When a component is resized and it reaches the end
             * of the desktop pane (caution: not the end of the visible area of the pane, but the
             * end of the desktop pane itself), then the desktop pane expands automatically, to the
             * direction the component is moved.
             */
            MicroworldView currentView = container.currentView;
            if (currentView.mwdResizable && currentView.mwdAutoExpandable && (compoBounds.x < 0 || compoBounds.y < 0)) {
                int xDiff = compoBounds.x;
                int yDiff = compoBounds.y;
                /* When component is resized beyond the left/top borders of the Container, the latter
                 * increases size in steps equal to the grid's step. To enable this, 'resizeFrame()'
                 * and 'endResizingFrame()' methods of the Desktop manager have been overriden.
                 */
//                    if (lc.gridVisible) {
                    if (xDiff < 0) {
                        xDiff = -container.lc.gridStep;
                        container.jumpedPixelsX += container.lc.gridStep;
                        b1 = true;
                    }
                    if (yDiff < 0) {
                        yDiff = -container.lc.gridStep;
                        container.jumpedPixelsY += container.lc.gridStep;
                        b2 = true;
                    }
//                    }
                if (xDiff > 0) xDiff = 0; else xDiff = -xDiff;
                if (yDiff > 0) yDiff = 0; else yDiff = -yDiff;
                ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
/*0                    for (int i=0; i<components.size(); i++) {
                    int frameIndex = ((Integer) componentFrameIndex.at(i)).intValue();
                    ESlateInternalFrame fr1 = null;
                    if (frameIndex != -1)
                        fr1 = (ESlateInternalFrame) componentFrames.at(frameIndex);
                    if (fr1 != null && !fr1.isFrozen()) {
*/
                for (int i=0; i<frames.length; i++) {
                    ESlateInternalFrame fr1 = frames[i];
//if                    if (!fr1.isFrozen()) {
                        Rectangle rect = fr1.getBounds();
//                            System.out.println("rect.x: " + rect.x + ", xDiff: " + xDiff + ", rect.width: " + rect.width);
                        container.disableComponentMoveListener = true;
//                            System.out.println("BEFORE xDiff: " + xDiff+ ".  Bounds of frame " + fr1.getTitle() + " :  " + fr1.getBounds());
                        fr1.setBounds(rect.x + xDiff, rect.y + yDiff, rect.width, rect.height);
//                            fr1.setBounds(rect.x + xDiff, rect.y + yDiff, rect.width+xDiff, rect.height+yDiff);
//                            System.out.println("Bounds of frame " + fr1.getTitle() + " :  " + fr1.getBounds());
//                            System.out.println("setBounds 5");
//if                    }else{
//if                        Component comp = (Component) mwdComponents.getComponent(fr1).object;
//0                            Component comp = (Component) components.at(i);
//if                        Rectangle rect = comp.getBounds();
//if                        disableComponentMoveListener = true;
//if                        comp.setBounds(rect.x + xDiff, rect.y + yDiff, rect.width+xDiff, rect.height+yDiff);

                        /* If the frame is frozen, we must move it, so that when it becomes dun-frozen
                         * it will be located at the same place as the already moved component.
                         */
//if                        if (fr1 != null && fr1.isFrozen()) {
//if                            Rectangle rect1 = fr1.getBounds();
//                            System.out.println("rect.x: " + rect.x + ", xDiff: " + xDiff + ", rect.width: " + rect.width);
//if                            disableComponentMoveListener = true;
//if                            fr1.setBounds(rect1.x + xDiff, rect1.y + yDiff, rect1.width+xDiff, rect1.height+yDiff);
//if                        }
//if                    }
                }
                Dimension newSize = new Dimension(lcBounds.width + xDiff, lcBounds.height + yDiff);
//                    System.out.println("xDiff: " + xDiff + ", newSize: " + newSize);
                container.lc.setPreferredSize(newSize);
                container.lc.setMinimumSize(newSize);
                container.lc.setMaximumSize(newSize);

                container.scrollPane.revalidate();
//                    ESlateContainer.this.setMicroworldChanged(true);
//                    System.out.println("microworldChanged 3");
                return;
            }
            if (currentView.mwdResizable && currentView.mwdAutoExpandable && ((compoBounds.x+compoBounds.width) > lcBounds.width || (compoBounds.y + compoBounds.height) > lcBounds.height)) {
                int xDiff = -(lcBounds.width-(compoBounds.x+compoBounds.width));
                int yDiff = -(lcBounds.height-(compoBounds.y+compoBounds.height));
                if (yDiff < 0) yDiff = 0;
                if (xDiff < 0) xDiff = 0;
                Dimension newSize = new Dimension(lcBounds.width + xDiff, lcBounds.height + yDiff);
//                    System.out.println("xDiff: " + xDiff + ", newSize: " + newSize);
                container.lc.setPreferredSize(newSize);
                container.lc.setMinimumSize(newSize);
                container.lc.setMaximumSize(newSize);

//                    scrollPane.getViewport().setViewPosition(new Point(
//                          scrollPane.getViewport().getViewPosition().x + xDiff,
//                          scrollPane.getViewport().getViewPosition().y + yDiff
//                    ));

                container.scrollPane.revalidate();
//                    ESlateContainer.this.setMicroworldChanged(true);
//                    System.out.println("microworldChanged 4");
                return;
            }

            /* Auto-scroll behaviour: when a component is moved around and any of its sides gets out
             * of the visible rectangle of the viewport of the desktop pane, the desktop pane scrolls,
             * so that all the component is visible.
             */
        }
    }

}
