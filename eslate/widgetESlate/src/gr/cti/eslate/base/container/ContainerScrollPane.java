package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ContainerScrollPane extends JScrollPane {
    Rectangle highlightRect = new Rectangle(0,0,0,0);
    boolean freezeScreen = false;

    public ContainerScrollPane(Component view) {
        super(view);
        getViewport().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                invalidateHighlightRect();
            }
        });
        getVerticalScrollBar().setUnitIncrement(20);
        getHorizontalScrollBar().setUnitIncrement(20);
    }

    protected void paintComponent(Graphics g) {
        if (freezeScreen == true) return;
        super.paintComponent(g);
//System.out.println("ContainerScrollPane paint()");

        if (!highlightRect.isEmpty()) {
//            System.out.println("Drawing highlight rect " + highlightRect);
            g.setColor(Color.yellow);
            g.drawRect(highlightRect.x, highlightRect.y, highlightRect.width, highlightRect.height);
            if (getVerticalScrollBar().isVisible())
                getVerticalScrollBar().repaint();
            if (getHorizontalScrollBar().isVisible())
                getHorizontalScrollBar().repaint();
        }
    }
    
    public void invalidateHighlightRect() {
        if (!highlightRect.isEmpty())
            highlightRect = new Rectangle();
    }

    protected void validateTree() {
//        if (freezeScreen == true) return;
//System.out.println("ContainerScrollPane VALIDATETREE()");
//Thread.currentThread().dumpStack();
    	synchronized(getTreeLock()) {
            super.validateTree();
        }
    }

    protected void processKeyEvent(java.awt.event.KeyEvent e) {
//        System.out.println("ContainerScrollPane Discarding key event");
    }

    public void updateUI() {
//        if (freezeScreen == true) return;
        super.updateUI();
        /* The standard key-bindings of the scrollpane should be reset, because there are components
         * for which these keys do some action. These key events will also reach this scrollpane, in
         * which case the scrollpane will do some action too, which is undesirable. For example if
         * you press PAGE-UP/DOWN in the WebWindow browser and the size of the microworld is bigger
         * than its visible part, then PAGE-UP/DOWN would also be executed by the Container's scrollpane.
         */
        setActionMap(new ActionMap());
        setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
    }

}

