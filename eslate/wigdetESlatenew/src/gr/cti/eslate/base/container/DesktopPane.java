package gr.cti.eslate.base.container;

//import java.awt.event.*;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.imageChooser.ImageChooser;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;


public class DesktopPane extends JDesktopPane {
    NewRestorableImageIcon icon;
//    Image stretchedImage = null;
    int iconDisplayMode;
    JScrollPane scrollPane;
    int gridStep = 20;
    boolean gridVisible = false;
    Color gridColor = java.awt.SystemColor.textText; //Color.white;
    ESlateContainer container;
    boolean snapToGrid = false;
    private boolean modalFrameVisible = false;
//    private gr.cti.eslate.utils.Timer timer = new gr.cti.eslate.utils.Timer();
	public boolean paintBgrOnly = true;
    /** This holds all the frames of the microworld which are both modal and visible. Multiple
     *  frames can be simultaneously modal. Some of them may be concurently visible too in the
     *  same microworld. They are all displayed at the higher layer level.
     */
    ArrayList visibleModalFrames = new ArrayList();
//    StretchImageTask stretchImageTask = null;
    Rectangle graphicsClipRect = new Rectangle();
    /** Stores any offset which should be used for the location of the upper left corner of the
     *  ESlateInternalFrame, when it is in maximum state.
     */
    Point maximumTopLeftCornerOffset = new Point(0, 0);
    /** Stores any offset which should be used for the location of the lower right corner of the
     *  ESlateInternalFrame, when it is in maximum state.
     */
    Point maximumBottomRightCornerOffset = new Point(0, 0);

    public DesktopPane(ESlateContainer container) {
        super();
        this.container = container;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (modalFrameVisible)
                    java.awt.Toolkit.getDefaultToolkit().beep();
            }
        });
/*        addHierarchyBoundsListener(new HierarchyBoundsAdapter()  {
            public void ancestorResized(HierarchyEvent e) {
                System.out.println("HierarchyBoundsAdapter getChanged(): " + e.getChanged().getClass());
            }
        });
*/
    }

    protected void processKeyEvent(java.awt.event.KeyEvent e) {
//        System.out.println("DesktopPane Discarding key event");
    }

    protected void myValidate() {
System.out.println("DesktopPane myValidate() called");
		synchronized(getTreeLock()) {
		    validateTree();
		}
    }

/*    public void setVisible(boolean visible) {
//System.out.println("DesktopPane setVisible(" + visible + ')');
        super.setVisible(visible);
    }
*/
    public Dimension gerPreferredSize() {
        Dimension dim = super.getPreferredSize();
//        System.out.println("DesktopPane gerPreferredSize(): " + dim);
        return dim;
    }

    public void setPreferredsize(Dimension dim) {
        super.setPreferredSize(dim);
    }

	protected void paintChildren(Graphics g) {
		if (!container.repaintManager.active) return;
		super.paintChildren(g);
	}

    protected void paintComponent(Graphics g) {
        if (!paintBgrOnly && !container.repaintManager.active)
            return;
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());

//        timer.init();
		if (!paintBgrOnly) {
			super.paintComponent(g);
		}
//System.out.println("paintBgrOnly:  " + paintBgrOnly + ", icon: " + icon);
        if (icon != null) {
            // TILE ICON
            if (iconDisplayMode == ImageChooser.TILE_IMAGE) {
                int tileW = icon.getIconWidth();
                int tileH = icon.getIconHeight();
                int xpos=0, ypos=0;
                int width = scrollPane.getViewport().getExtentSize().width;
                int height = scrollPane.getViewport().getExtentSize().height;

                int startx = scrollPane.getViewport().getViewPosition().x;
                int starty = scrollPane.getViewport().getViewPosition().y;
                int endx = startx + width;
                int endy = starty + height;
                Graphics cg;

                cg = g.create();
                graphicsClipRect = g.getClipBounds(graphicsClipRect);
                Rectangle r = new Rectangle(startx, starty, width, height);
                if (graphicsClipRect != null)
                    r = r.intersection(graphicsClipRect);
                cg.setClip(r); //startx, starty, width, height);
                for (ypos = starty; endy - ypos > 0; ypos += tileH) {
                    for (xpos = startx; endx - xpos > 0; xpos += tileW) {
                        icon.paintIcon(this, cg, xpos, ypos);
                    }
                }
                cg.dispose();
            }else if (iconDisplayMode == ImageChooser.CENTER_IMAGE || iconDisplayMode == ImageChooser.FIT_IMAGE) { //CENTERED ICON
                int iconWidth = icon.getIconWidth();
                int iconHeight = icon.getIconHeight();
                int width = scrollPane.getViewport().getExtentSize().width;
                int height = scrollPane.getViewport().getExtentSize().height;

                int startx = scrollPane.getViewport().getViewPosition().x;
                int starty = scrollPane.getViewport().getViewPosition().y;
                int endx = startx + width;
                int endy = starty + height;
                int xpos = startx + ((width-iconWidth)/2);
                int ypos = starty + ((height-iconHeight)/2);
                Graphics cg;

                cg = g.create();
                graphicsClipRect = g.getClipBounds(graphicsClipRect);
                Rectangle r = new Rectangle(startx, starty, width, height);
                if (graphicsClipRect != null)
                    r = r.intersection(graphicsClipRect);
                cg.setClip(r); //startx, starty, width, height);
                icon.paintIcon(this, cg, xpos, ypos);
                cg.dispose();
            }
//            }else if (iconDisplayMode == ImageChooser.FIT_IMAGE) { //STRETCH ICON TO FIT
/*                int iconWidth = icon.getIconWidth();
                int iconHeight = icon.getIconHeight();
                int width = scrollPane.getViewport().getExtentSize().width;
                int height = scrollPane.getViewport().getExtentSize().height;
*/
//                int startx = scrollPane.getViewport().getViewPosition().x;
//                int starty = scrollPane.getViewport().getViewPosition().y;
/*                int endx = startx + width;
                int endy = starty + height;
                int xpos = (endx-startx) + (iconWidth/2);
                int ypos = (endy-starty) + (iconHeight/2);
*/
/*                Graphics cg;

                cg = g.create();
//                cg.drawImage(icon.getImage(), startx, starty, width, height, null);
        System.out.println("1 DesktopPane paintComponent(): " + timer.lapse());
                cg.drawImage(stretchedImage, startx, starty, this);
        System.out.println("2 DesktopPane paintComponent(): " + timer.lapse());
                cg.dispose();
            }
*/
        }
        if (container.microworld != null && gridVisible) {
            g.setColor(gridColor);
            Point desktopPos = scrollPane.getViewport().getViewPosition();
            int startX = desktopPos.x - (desktopPos.x % gridStep);
            int startY = desktopPos.y - (desktopPos.y % gridStep);
            int stopX = startX + scrollPane.getViewport().getExtentSize().width;
            int stopY = startY + scrollPane.getViewport().getExtentSize().height;
            for (int i=startX; i<stopX; i += gridStep) {
                for (int j=startY; j<stopY; j += gridStep) {
                    g.drawRect(i, j, 1, 1);
                }
            }
        }
//        System.out.println("3 DesktopPane paintComponent(): " + timer.lapse());
    }

    public void setIcon(NewRestorableImageIcon icon) {
        if (this.icon == icon) return;
//        if (this.icon != null && this.icon.getImage() != null) {
//            this.icon.getImage().flush();
//            this.icon.setImage(null);
//        }
        this.icon = icon;
        if (icon == null) {
            iconDisplayMode = ImageChooser.NO_IMAGE;
//            setStretchedImage(null);
        }else{
//            if (iconDisplayMode == ImageChooser.FIT_IMAGE)
//                setStretchedImage(icon);
        }
    }

    public NewRestorableImageIcon getIcon() {
        return icon;
    }

    public void setIconDisplayMode(int mode) {
        if (mode != ImageChooser.NO_IMAGE && mode != ImageChooser.FIT_IMAGE &&
          mode != ImageChooser.CENTER_IMAGE && mode != ImageChooser.TILE_IMAGE)
            return;
        if (iconDisplayMode == mode) return;

        iconDisplayMode = mode;
        if (mode == ImageChooser.FIT_IMAGE)
            updateStretchedImage();
    }

    public int getDisplayMode() {
        return iconDisplayMode;
    }

    /* When there is a backgroundIcon in STRETCHED mode, the streched image is produced by
     * this method, when the icon is first set. The produced image becomes the actual image
     * of the icon. So every time the image is stretched it is additionally corrupted.
     * Therefore it is best for a microworld that needs a streched image, to apply the image
     * at the end of the authoring.
     */
    void updateStretchedImage() {
        if (icon != null && (icon.getIconWidth() != scrollPane.getViewport().getExtentSize().width || icon.getIconHeight() != scrollPane.getViewport().getExtentSize().height) && !container.loadingMwd) { // the last condition is a hack, cause when a microworld is loaded the scrollpane changes size(1 or 2 pixels)
System.out.println("STRETCHING BGR IMAGE TO FIT");
//long start = System.currentTimeMillis();
//System.out.println("updateStretchedImage() Creating strecthedImage scrollPane.getViewport().getExtentSize(): " + scrollPane.getViewport().getExtentSize() + ", icon dimension: " + icon.getIconWidth() + ", " + icon.getIconHeight());
            Image stretchedImage = icon.getImage().getScaledInstance(scrollPane.getViewport().getExtentSize().width, scrollPane.getViewport().getExtentSize().height, Image.SCALE_SMOOTH); //SCALE_DEFAULT);
            icon.getImage().flush();
            icon.setImage(stretchedImage);
//System.out.println("ET: " + (System.currentTimeMillis()-start) + ", image dimensions: " + stretchedImage.getWidth(this) + ", " + stretchedImage.getHeight(this));
        }
    }

    public void setScrollPane(JScrollPane scrPane) {
        if (this.scrollPane == scrPane) return;
        this.scrollPane = scrPane;
/*      Automatic image stretching when the scrollpane dimensions change is forbidden, cause
        it's to slow and loads memory. At design-time this could be an option, but at runtime
        there is no reason for this.
        this.scrollPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Image img = null;
                if (icon != null)
                    img = icon.getImage();
//System.out.println("ComponentListener scrollPane.getViewport().getExtentSize(): " + scrollPane.getViewport().getExtentSize() + ", img: " + img.getWidth(DesktopPane.this) + ", " + img.getHeight(DesktopPane.this));
                if (img != null && (scrollPane.getViewport().getExtentSize().width != img.getWidth(DesktopPane.this) || scrollPane.getViewport().getExtentSize().height != img.getHeight(DesktopPane.this))) {
                    updateStretchedImage();
//                    if (stretchImageTask != null)
//                        stretchImageTask.taskEnabled = false;
//                    stretchImageTask = new StretchImageTask(DesktopPane.this);
//                    javax.swing.SwingUtilities.invokeLater(stretchImageTask);
                    System.out.println("RESIZING IMAGE TO FIT");
//                    updateStretchedImage();
                }
            }
        });
*/
    }

    public void disableFrameDragging() {
        if (gr.cti.eslate.base.container.FrameDragAdjust.class.isInstance(getDesktopManager()))
            ((gr.cti.eslate.base.container.FrameDragAdjust) getDesktopManager()).disableFrameDragging();
        else
            System.out.println("Cannot disable frame dragging");
    }

    public void enableFrameDragging() {
        if (gr.cti.eslate.base.container.FrameDragAdjust.class.isInstance(getDesktopManager()))
            ((gr.cti.eslate.base.container.FrameDragAdjust) getDesktopManager()).enableFrameDragging();
        else
            System.out.println("Cannot enable frame dragging");
    }

    /* This method is always called by ESlateInternalFrames, when theie modali status changes.
     */
    public void setModalFrameVisible(boolean visible, ESlateInternalFrame frame) {
//        if (modalFrameVisible == visible) return;
        modalFrameVisible = visible;
        if (visible) {
            if (!visibleModalFrames.contains(frame))
                visibleModalFrames.add(frame);
        }else{
            visibleModalFrames.remove(frame);
        }

        /* The GlassPane of the microworld becomes invisible only when there are no more visible
           modal frames.
        */
        if (container.isGlassPaneVisible() != visible)
            container.setGlassPaneVisible(!visibleModalFrames.isEmpty());
    }

    public boolean isModalFrameVisible() {
        return container.isGlassPaneVisible();
    }

    /** Returns the number of modal frames which are currently visible.
     */
    public int getVisibleModalFrameCount() {
        return visibleModalFrames.size();
    }

    /** Sets the (x,y) offset which is used to calculate the location (upper left corner)
     *  of an ESlateInternalFrame, when in maximum state.
     */
    public void setMaximumTopLeftCornerOffset(Point p) {
        maximumTopLeftCornerOffset = p;
    }

    /** Returns the (x,y) offset which is used to calculate the location (upper left corner)
     *  of an ESlateInternalFrame, when in maximum state.
     */
    public Point getMaximumTopLeftCornerOffset() {
        return maximumTopLeftCornerOffset;
    }

    /** Sets the (x,y) offset which is used to calculate the location of the lower right corner
     *  of an ESlateInternalFrame, when in maximum state.
     */
    public void setMaximumBottomRightCornerOffset(Point p) {
        maximumBottomRightCornerOffset = p;
    }

    /** Returns the (x,y) offset which is used to calculate the location of the lower right corner
     *  of an ESlateInternalFrame, when in maximum state.
     */
    public Point getMaximumBottomRightCornerOffset() {
        return maximumBottomRightCornerOffset;
    }

}

/*class StretchImageTask implements Runnable {
    boolean taskEnabled = true;
    DesktopPane desktopPane = null;

    public StretchImageTask(DesktopPane pane) {
        this.desktopPane = pane;
    }

    public void run() {
        if (!taskEnabled) return;
        desktopPane.updateStretchedImage();
        desktopPane.repaint();
System.out.println("run icon dim: " + desktopPane.icon.getIconWidth() + ", " + desktopPane.icon.getIconHeight());
    }
}
*/