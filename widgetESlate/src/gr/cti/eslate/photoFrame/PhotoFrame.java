package gr.cti.eslate.photoFrame;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.protocol.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.Print;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

public class PhotoFrame extends JPanel implements Externalizable, ESlatePart,
        ActorInterface {
    private static final String VERSION = "3.0.11";

    JScrollPane s;

    Plug pin;

    ImagePanel imagePanel;

    protected boolean hasBeenDestroyed = false;

    private ESlateHandle handle;
    private static BufferedImage nullDefaultImage;
    private BufferedImage nullImage;
    
    private boolean autofit, stretch;
    
    private Icon nullImageIcon;

    private File filename;

    /**
     * Timer which measures the time required for loading the state of the
     * component.
     */
    PerformanceTimer loadTimer;

    /**
     * Timer which measures the time required for saving the state of the
     * component.
     */
    PerformanceTimer saveTimer;

    /**
     * Timer which measures the time required for the construction of the
     * component.
     */
    PerformanceTimer constructorTimer;

    /**
     * Timer which measures the time required for initializing the E-Slate
     * aspect of the component.
     */
    PerformanceTimer initESlateAspectTimer;

    /**
     * The listener that notifies about changes to the state of the Performance
     * Manager.
     */
    PerformanceListener perfListener = null;

    // AVAKEEO-START//
    transient protected ResourceBundle m; // GREEK//

    private boolean borderChanged;

    private int plugCount = 1;

    // Number of animated variables.
    private int varCount = 4;

    // Array of component variables values to export through actor plug.
    private IntBaseArray varValues;

    // Structure to hold the property IDs and names of the variables.
    private AnimatedPropertyStructure animatedPropertyStructure;

    // Actor's name listeners.
    private ArrayList actorNameListeners = new ArrayList();

    private MaleSingleIFMultipleConnectionProtocolPlug animationPlug;

    /**
     * Applet information.
     * 
     * @return Applet information string.
     */
    public String getAppletInfo() {
        return "PhotoFrame component v"+VERSION+", by George Vasiliou";
    }

    /**
     * Applet parameter information.
     * 
     * @return Array of parameter information strings.
     */
    public String[][] getParameterInfo() {
        String[][] info = {
        // Parameter Name Kind of Value Description
        { "loadImage", "URL", "The image intially displayed" }, };

        return info;
    }

    // public PhotoFrameColorChange colors;
    // don't make this private: inner classes shall throw Illegal Access Error
    // if using JARs

    
    public PhotoFrame() {
        super();
        ImageIcon nullDefaultIcon= new ImageIcon(PhotoFrame.class.getResource("images/noimage.png"));
        if (nullDefaultImage==null){
            nullDefaultImage = new BufferedImage(nullDefaultIcon.getIconWidth(), 
                                                 nullDefaultIcon.getIconHeight(), 
                                                 BufferedImage.TYPE_INT_ARGB);
            Graphics g = nullDefaultImage.getGraphics();
            g.drawImage(nullDefaultIcon.getImage(), 0, 0, null);
            g.dispose();
        }
        
        try {
            m = ResourceBundle.getBundle(
                    "gr.cti.eslate.photoFrame.MessagesBundle", Locale
                            .getDefault());
        } catch (MissingResourceException e) {
            System.err.println("Couldn't find messages resource");
        }

        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                imagePanel.resize();
            }
        });

        autofit = false;
        setBackground((Color) UIManager.getColor("controlShadow"));
        setBorder(new NoTopOneLineBevelBorder(BevelBorder.RAISED));
        borderChanged = false;
        setPreferredSize(new Dimension(350, 270));
        setLayout(new BorderLayout());
        // GUI//
        add(s = new JScrollPane(), BorderLayout.CENTER);
        s.setBorder(null);
        imagePanel = new ImagePanel(s);
        s.setOpaque(false);
        JViewport viewport = s.getViewport();
        viewport.setOpaque(false);
        viewport.setView(imagePanel);

        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
    }
    

    public ESlateHandle getESlateHandle() {
        if (handle==null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);
            handle = ESlate.registerPart(this);
            try {
                handle.setUniqueComponentName(m.getString("componame"));
            } catch (RenamingForbiddenException e) {
            }

            // Information dialog
            String[] info = { m.getString("part"), m.getString("development"),
                    m.getString("copyright") };

            handle.setInfo(new ESlateInfo(m.getString("compo")+" "+VERSION,
                    info));

            // Create pin
            try {
                Class soClass = Class
                        .forName("gr.cti.eslate.sharedObject.StringSO");
                SharedObjectListener sol = new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent soe) {
                        
                        String str = ((StringSO) (soe.getSharedObject()))
                                .getString();
                        URL url;
                        try {
                            url = new URL(str);
                        } catch (MalformedURLException e) {
                            try {
                                url = new URL(handle.getESlateMicroworld()
                                        .getDocumentBase(), str);
                            } catch (MalformedURLException ee) {
                                url = null;
                            }
                        }
                        imagePanel.setImage(url);
                    }
                };

                pin = new MultipleInputPlug(handle, null, m.getString("pin"),
                        new Color(139, 117, 0), soClass, sol);
                pin.setNameLocaleIndependent("pin");
                pin.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                        imagePanel.setImage((URL) null);
                    }
                });
                pin.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        String str = ((StringSO) (((SharedObjectPlug) e.getPlug()).getSharedObject()))
                        .getString();
                        URL url;
                        try {
                            url = new URL(str);
                        } catch (MalformedURLException exc) {
                            try {
                                url = new URL(handle.getESlateMicroworld()
                                        .getDocumentBase(), str);
                            } catch (MalformedURLException ee) {
                                url = null;
                            }
                        }
                        imagePanel.setImage(url);
                    }
                });

                handle.addPlug(pin);

                // Icon pin
                soClass = Class.forName("gr.cti.eslate.sharedObject.IconSO");
                sol = new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent soe) {
                        try {
                            imagePanel.setImage(((IconSO) (soe
                                    .getSharedObject())).getIconSO());
                        } catch (Exception e) {
                            imagePanel.setImage((Icon) null);
                        }
                    }
                };
                Plug iiPlug = new MultipleInputPlug(handle, null, m
                        .getString("imagepin"), new Color(50, 151, 220),
                        soClass, sol);

                iiPlug.setNameLocaleIndependent("imagepin");
                iiPlug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType()==Plug.INPUT_CONNECTION) {
                            IconSO so = (IconSO) ((SharedObjectPlug) e
                                    .getPlug()).getSharedObject();

                            imagePanel.setImage(so.getIconSO());
                        }
                    }
                });

                iiPlug.addDisconnectionListener(new DisconnectionListener() {
                    public void handleDisconnectionEvent(DisconnectionEvent e) {
                        imagePanel.setImage((Icon) null);
                    }
                });
                handle.addPlug(iiPlug);

            } catch (InvalidPlugParametersException e) {
                System.out.println("Invalid Plug Parameters.");
            } catch (PlugExistsException e) {
                System.out.println("Plug Exists.");
            } catch (ClassNotFoundException e) {// Possibly couldn't find the
                // interface/SO. No Plug.
            } catch (NoClassDefFoundError e) {// Possibly couldn't find the
                // interface/SO. No Plug.
            }

            /*
             * try { Class
             * soClass=Class.forName("gr.cti.eslate.sharedObject.View");
             * SharedObjectListener sol=new SharedObjectListener() { public void
             * handleSharedObjectEvent(SharedObjectEvent soe) { try {
             * imagePanel.setImage(((IconSO)(soe.getSharedObject())).getIconSO()); }
             * catch(Exception e) { imagePanel.setImage((Icon) null); } } };
             * Plug view=new MultipleInputPlug(handle, null, "tv", new
             * Color(255,170,0), soClass, sol);
             * view.setNameLocaleIndependent("tv");
             * view.addConnectionListener(new ConnectionListener() { public void
             * handleConnectionEvent(ConnectionEvent e){ if (e.getType() ==
             * Plug.INPUT_CONNECTION) { View so = (View)
             * ((SharedObjectPlug)e.getPlug()).getSharedObject();
             * imagePanel.setImage(so.getIcon()); } } });
             * 
             * view.addDisconnectionListener(new DisconnectionListener() {
             * public void handleDisconnectionEvent(DisconnectionEvent e) {
             * imagePanel.setImage((Icon) null); } }); handle.addPlug(view); }
             * catch (InvalidPlugParametersException e) {
             * System.out.println(e.getMessage()); } catch (PlugExistsException
             * e) { System.out.println(e.getMessage()); } catch
             * (ClassNotFoundException e) { //Possibly couldn't find the
             * interface/SO. No Plug. } catch (NoClassDefFoundError e) {
             * //Possibly couldn't find the interface/SO. No Plug. }
             */

            createProtocolPlug();

            handle.addESlateListener(new ESlateAdapter() {
                public void handleDisposed(HandleDisposalEvent e) {
                    destroy();
                    PerformanceManager pm = PerformanceManager
                            .getPerformanceManager();
                    pm.removePerformanceListener(perfListener);
                    perfListener = null;
                }
            });
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    
    public void print(String title) {
        String file = System.getProperty("java.io.tmpdir")+"tempPhotoFile.jpg";

        try {
            FileOutputStream fout = new FileOutputStream(file);
            NewRestorableImageIcon restorableIcon = new NewRestorableImageIcon(
                    imagePanel.fullImage.getImage());

            restorableIcon.saveImage(NewRestorableImageIcon.JPG, fout);
            Print.printImage(file, 0, 0, 0, 0, true, false, 1, title, true,
                    true);
            // Print.printImage(file, new Insets(0,0,0,0),1, 0,100);
        } catch (Exception e) {
            System.out.println(e);
        }
        File f = new File(file);

        f.deleteOnExit();
    }
    

    public void updateUI() {
        super.updateUI();
        setBackground((Color) UIManager.getColor("controlShadow"));
    }

    
    public void setBorder(Border b) {
        borderChanged = true;
        super.setBorder(b);
    }

    
    public void setOpaque(boolean b) {
        super.setOpaque(b);
        repaint();
    }

    
    public void setAutomaticallyFitImage(boolean b) {
        autofit = b;
    }

    
    public boolean getAutomaticallyFitImage() {
        return autofit;
    }

    
    public void setStretchImage(boolean b) {
        stretch = b;
    }

    
    public boolean getStretchImage() {
        return stretch;
    }
    
    public Icon getNullImageIcon(){
        return nullImageIcon;
    }
    
    public void setNullImageIcon(Icon icon){
        this.nullImageIcon = icon;
        if (icon!=null && icon instanceof ImageIcon){
            ImageIcon imgIcon = (ImageIcon) icon;
            nullImage = new BufferedImage(imgIcon.getIconWidth(), 
                    imgIcon.getIconHeight(), 
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = nullImage.getGraphics();
            g.drawImage(imgIcon.getImage(), 0, 0, null);
            g.dispose();
            if (!imagePanel.validImage)
                setImage((Icon) null);

                
        }else{
            nullImage=null;
        }
    }
    
    public void readExternal(ObjectInput in) throws ClassNotFoundException,
            IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(loadTimer);
        Object compatCheck = in.readObject();

        if (compatCheck instanceof StorageStructure) {
            // ESlateFieldMap fm = (ESlateFieldMap) compatCheck;
            StorageStructure fm = (StorageStructure) compatCheck;

            setBackground(fm.get("background", getBackground()));
            setOpaque(fm.get("opaque", isOpaque()));
            setVisible(fm.get("visible", isVisible()));
            setMaximumSize(fm.get("MaximumSize", getMaximumSize()));
            setMinimumSize(fm.get("MinimumSize", getMinimumSize()));
            setPreferredSize(fm.get("PreferredSize",
                    getPreferredSize()));
            
            if (fm.containsKey("NullImage"))
                setNullImageIcon((Icon) fm.get("NullImage", getNullImageIcon()));
            if (fm.containsKey("Image"))
                    setImage((Icon) fm.get("Image", getImage()));
            else
                setImage((Icon) null);    
            if (fm.containsKey("Filename"))
                setFilename(new File((String) fm.get("Filename", (String) null)));

            if (fm.containsKey("border")) {
                try {
                    BorderDescriptor bd = (BorderDescriptor) fm.get("border");

                    setBorder(bd.getBorder());
                } catch (Throwable thr) {/* No Border */
                }
            }
            autofit = fm.get("autofit", false);
            stretch = fm.get("stretch", false);
        } else { // Old version

            setBackground(((Color) compatCheck));
            try {
                Vector r = (Vector) in.readObject();

                setOpaque(((Boolean) r.elementAt(0)).booleanValue());
                setVisible(((Boolean) r.elementAt(1)).booleanValue());
            } catch (Exception e) {
            }
        }
        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(saveTimer);
        // ESlateFieldMap fm = new ESlateFieldMap("2.0");
        ESlateFieldMap2 fm = new ESlateFieldMap2(2);

        fm.put("background", getBackground());
        fm.put("opaque", isOpaque());
        fm.put("visible", isVisible());
        fm.put("autofit", autofit);
        fm.put("stretch", stretch);
        fm.put("MaximumSize", getMaximumSize());
        fm.put("MinimumSize", getMinimumSize());
        fm.put("PreferredSize", getPreferredSize());
        if (getFilename()!=null&&getFilename().getAbsolutePath()!=null)
            fm.put("Filename", getFilename().getAbsolutePath());
        
        if (getNullImageIcon() != null)
            fm.put("NullImage", new NewRestorableImageIcon(((ImageIcon) getNullImageIcon()).getImage()));
        
        if (getImage()!=null && imagePanel.validImage)
            fm.put("Image", new NewRestorableImageIcon(getImage().getImage()));

        if (borderChanged&&! (getBorder() instanceof UIResource)) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(
                        getBorder(), this);

                fm.put("border", bd);
            } catch (Throwable thr) {
            }
        }
        out.writeObject(fm);
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    // //////////////////////////////////////////////////////////////////////

    public class ImagePanel extends JPanel {
        protected ImageIcon fullImage, scaledImage;
        
        protected boolean validImage = false;

        protected int iconWidth, iconHeight, paneWidth, paneHeight;

        protected boolean scaled = false;

        protected boolean noscaleneeded = true;

        protected JScrollPane sp;

        protected JLabel label;

        protected URL urlShowing;

        public ImagePanel(JScrollPane s) {
            super();

            this.setOpaque(false);

            setLayout(new BorderLayout());
            this.sp = s;
            this.label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(false);
            add(label, BorderLayout.CENTER);
            // label.setAlignmentX(CENTER_ALIGNMENT);
            // label.setAlignmentY(CENTER_ALIGNMENT);

            label.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (autofit&&! stretch) {
                        scaleImage();
                    }
                }
            });
        }

        void stretchImage() {
            int width = sp.getViewport().getWidth();
            int height = sp.getViewport().getHeight();
            ImageIcon stretchedImage = new ImageIcon(fullImage.getImage()
                    .getScaledInstance(width, height, Image.SCALE_DEFAULT));
            label.setIcon(stretchedImage);

            sp.setViewportView(ImagePanel.this);
            sp.getViewport().setViewPosition(new Point(0, 0));
            sp.validate();

        }

        void scaleImage() {
            if (fullImage.getImage()==null)
                return;
            // if (noscaleneeded) return;

            if (fullImage.getIconWidth()-paneWidth==0
                    || fullImage.getIconHeight()-paneHeight==0 || paneWidth*paneHeight==0) {
                scaled = false;
                return;
            }
            // if (scaledImage == null) {
            label.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            // Must scale down (or up (N)) the image
            float aspect = ((float) fullImage.getIconWidth())
                    /fullImage.getIconHeight();
            if (((float) paneWidth)/paneHeight<aspect) {
                iconWidth = paneWidth;
                iconHeight = (int) (1d*paneWidth/fullImage.getIconWidth()*fullImage
                        .getIconHeight());
            } else {
                iconWidth = (int) (1d*paneHeight/fullImage.getIconHeight()*fullImage
                        .getIconWidth());
                iconHeight = paneHeight;
            }
            scaledImage = new ImageIcon(fullImage.getImage().getScaledInstance(
                    iconWidth, iconHeight, Image.SCALE_DEFAULT));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            scaled = true;
            // }

            if (scaled) {
                label.setIcon(scaledImage);
            } else {
                label.setIcon(fullImage);
            }
            sp.setViewportView(ImagePanel.this);
            sp.getViewport().setViewPosition(new Point(0, 0));
            sp.validate();
        }

        public int getIconWidth() {
            return iconWidth;
        }

        public int getIconHeight() {
            return iconHeight;
        }

        public void setImage(URL u) {
            try {
                if (u==null){
                    setImage((ImageIcon) null);
                    urlShowing = null;
                }else if (!u.equals(urlShowing)) {
                    urlShowing = u;
                    Icon icon = new ImageIcon(u);
                    setImage(icon);

                }
            } catch (Exception e) {
                setImage((Icon) null);
            }
        }

        public void setImage(Icon ic) {
            BufferedImage img = null;
            if (ic!=null) {
                img = new BufferedImage(ic.getIconWidth(), ic.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                ic.paintIcon(this, img.getGraphics(), 0, 0);
                validImage = true;
            }else{
                // No null images allowed anymore
//                img = null;
                if (nullImageIcon!=null)
                    img = nullImage;
                else
                    img = nullDefaultImage;
                validImage = false;
                
            }
            paneWidth = sp.getWidth();// - 18;
            paneHeight = sp.getHeight();// - 18;
            if (img==null) {
                if (label.getIcon()!=null)
                    ((ImageIcon) label.getIcon()).getImage().flush();
                label.setIcon(null);
                iconWidth = iconHeight = 0;
                if (scaledImage!=null)
                    scaledImage.getImage().flush();
                if (fullImage!=null)
                    fullImage.getImage().flush();
                scaledImage = null;
                fullImage = null;
                urlShowing = null;
                noscaleneeded = true;
                repaint();
                // 19990811:paintImmediately(0,0,getWidth(),getHeight());
                label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            try {
                if (fullImage!=null)
                    fullImage.getImage().flush();
                if (scaledImage!=null)
                    scaledImage.getImage().flush();
                fullImage = null;
                scaledImage = null;
                fullImage = new ImageIcon(img);

                if ((fullImage.getIconWidth()==paneWidth)
                        ||(fullImage.getIconHeight()==paneHeight)) {
                    label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    noscaleneeded = false;
                } else {
                    label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    noscaleneeded = true;
                    scaledImage = fullImage;
                    iconWidth = fullImage.getIconWidth();
                    iconHeight = fullImage.getIconHeight();
                }
                repaint();
                // 19990811:paintImmediately(0,0,iconWidth,iconHeight);
                if (! autofit)
                    label.setIcon(fullImage);
                else if (stretch)
                    stretchImage();
                else
                    scaleImage();
                sp.setViewportView(this);
                sp.getViewport().setViewPosition(new Point(0, 0));
                sp.validate();
                scaled = false;
            } catch (Exception e) {
                System.err.println("Couldn't show image!");
                e.printStackTrace();
            }
        }

        protected void resetMemory() { // MemManagement convenience
            if (fullImage!=null) {
                fullImage.getImage().flush();
                fullImage = null;
            }
            if (scaledImage!=null) {
                scaledImage.getImage().flush();
                scaledImage = null;
            }
        }

        protected void resize() {
            if ((! isVisible())||(fullImage==null))
                return;
            paneWidth = sp.getWidth();// - 18;
            paneHeight = sp.getHeight();// - 18;
            if ((fullImage.getIconWidth()==paneWidth)
                    ||(fullImage.getIconHeight()==paneHeight)) {
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                noscaleneeded = false;
                scaledImage = null;
            } else {
                label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                noscaleneeded = true;
                scaledImage = fullImage;
                iconWidth = fullImage.getIconWidth();
                iconHeight = fullImage.getIconHeight();
            }
            repaint();
            // 19990811:paintImmediately(0,0,iconWidth,iconHeight);
            // label.setIcon(fullImage);
            if (stretch)
                stretchImage();
            else if (autofit)
                scaleImage();
            sp.setViewportView(this);
            sp.getViewport().setViewPosition(new Point(0, 0));
            sp.validate();
            scaled = false;
        }

        // Why was this needed?
        /*
         * private class ShrinkImageThread extends Thread { private int
         * paneWidth, paneHeight;
         * 
         * ShrinkImageThread(int x, int y) { paneWidth = x; paneHeight = y; }
         * 
         * public void run() {} }
         */
    }

    public void destroy() {
        hasBeenDestroyed = true;
        System.out.println("-------------------------------------");
        System.out.println("Destroying Photo Frame...");
        try {
            s.removeAll();
            s = null;
            pin = null;
            imagePanel.resetMemory();
            imagePanel = null;
            m = null;

            destroyProtocolPlug();
        } catch (Exception e) {
            System.out.println("Problems encountered during destroying.");
        }
        // super.destroy();
        System.out.println("-------------------------------------");
    }

    /*
     * public JPanel getImagePanel(){ return imagePanel; }
     */
    public void setImage(String s) {
        try {
            URL url = new URL("file:/"+s);
            imagePanel.setImage(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setImage(URL u) {
        imagePanel.setImage(u);
    }

    public void setFilename(File filename) {
        if (filename!=null&&! filename.equals(this.filename)) {
            this.filename = filename;
            setImage(filename.getAbsolutePath());
        }
    }

    public File getFilename() {
        return filename;
    }

    public void setImage(Icon icon) {
        imagePanel.setImage(icon);
    }
    

    public ImageIcon getImage() {
        return imagePanel.fullImage;
    }

    public void clearMediaContent() {
        imagePanel.setImage((Icon) null);
        repaint();
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener==null) {
            perfListener = new PerformanceAdapter() {
                public void performanceManagerStateChanged(PropertyChangeEvent e) {
                    boolean enabled = ((Boolean) e.getNewValue())
                            .booleanValue();

                    // When the Performance Manager is enabled, try to attach
                    // the
                    // timers.
                    if (enabled) {
                        attachTimers();
                    }
                }
            };
            pm.addPerformanceListener(perfListener);
        }
    }

    /**
     * This method creates and attaches the component's timers. The timers are
     * created only once and are assigned to global variables. If the timers
     * have been already created, they are not re-created. If the timers have
     * been already attached, they are not attached again. This method does not
     * create any timers while the PerformanceManager is disabled.
     */
    private void attachTimers() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        boolean pmEnabled = pm.isEnabled();

        // If the performance manager is disabled, install a listener which will
        // re-invoke this method when the performance manager is enabled.
        if (! pmEnabled&&(perfListener==null)) {
            createPerformanceManagerListener(pm);
        }

        // Do nothing if the PerformanceManager is disabled.
        if (! pmEnabled) {
            return;
        }

        boolean timersCreated = (loadTimer!=null);

        // If the component's timers have not been constructed yet, then
        // construct them. During construction, the timers are also attached.
        if (! timersCreated) {
            // Get the performance timer group for this component.
            PerformanceTimerGroup compoTimerGroup = pm
                    .getPerformanceTimerGroup(this);

            // Construct and attach the component's timers.
            constructorTimer = (PerformanceTimer) pm
                    .createPerformanceTimerGroup(compoTimerGroup, m
                            .getString("ConstructorTimer"), true);
            loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                    compoTimerGroup, m.getString("LoadTimer"), true);
            saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                    compoTimerGroup, m.getString("SaveTimer"), true);
            initESlateAspectTimer = (PerformanceTimer) pm
                    .createPerformanceTimerGroup(compoTimerGroup, m
                            .getString("InitESlateAspectTimer"), true);
            pm.registerPerformanceTimerGroup(PerformanceManager.CONSTRUCTOR,
                    constructorTimer, this);
            pm.registerPerformanceTimerGroup(PerformanceManager.LOAD_STATE,
                    loadTimer, this);
            pm.registerPerformanceTimerGroup(PerformanceManager.SAVE_STATE,
                    saveTimer, this);
            pm.registerPerformanceTimerGroup(
                    PerformanceManager.INIT_ESLATE_ASPECT,
                    initESlateAspectTimer, this);
        }
    }

    /**
     * Create multiple protocol plug.
     */
    private void createProtocolPlug() {
        try {
            animationPlug = new MaleSingleIFMultipleConnectionProtocolPlug(
                    handle, null, m.getString("animationPlug"), new Color(0,
                            100, 255));
            animationPlug.setNameLocaleIndependent(animationPlug
                    .getInternalName()
                    +plugCount);
            animationPlug.setName(animationPlug.getName()+plugCount);
            animationPlug.addConnectionListener(new ConnectionListener() {
                public void handleConnectionEvent(ConnectionEvent e) {
                    plugCount++;
                    e.getOwnPlug().removeConnectionListener(this);
                    createProtocolPlug();
                }
            });
            animationPlug.addDisconnectionListener(new DisconnectionListener() {
                public void handleDisconnectionEvent(DisconnectionEvent e) {
                    try {
                        handle.removePlug(e.getOwnPlug());
                    } catch (NoSuchPlugException nspe) {
                    }
                }
            });
            try {
                handle.addPlug(animationPlug);
            } catch (PlugExistsException e) {
                System.err.println("Plug exists");
                e.printStackTrace();
            }
        } catch (InvalidPlugParametersException e) {
        } catch (PlugExistsException e) {
        }
    }

    private void destroyProtocolPlug() {
        // try
        // {
        // if(animationPlug != null)
        // handle.removePlug(animationPlug);
        // }
        // catch(Exception exc)
        // {
        // System.out.println("Plug to be removed not found");
        // exc.printStackTrace();
        // }
        animationPlug = null;
    }

    /**
     * @return The values of the variables of the actor.
     */
    public IntBaseArray getVarValues() {
        if (varValues==null)
            varValues = new IntBaseArray(varCount);
        else
            varValues.clear();
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, PhotoFrame.this);
        Rectangle bounds = internalFrame.getBounds();
        varValues.add(bounds.x);
        varValues.add(bounds.y);
        varValues.add(bounds.width);
        varValues.add(bounds.height);
        return varValues;
    }

    /**
     * Sets the values of the actor's variables.
     * 
     * @param varValues
     *            The values of the actor's variables.
     * @param animationSession
     *            The animation session to set the values.
     */
    public void setVarValues(IntBaseArray varValues,
            AnimationSession animationSession) {
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, PhotoFrame.this);
        Rectangle bounds = internalFrame.getBounds();
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        int i = 0;
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(0))
                        .getPropertyID())) {
            x = varValues.get(i);
            i++;
        }
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(1))
                        .getPropertyID())) {
            y = varValues.get(i);
            i++;
        }
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(2))
                        .getPropertyID())) {
            width = varValues.get(i);
            i++;
        }
        if (animationSession
                .isAnimated(((AnimatedPropertyDescriptor) animationSession
                        .getAnimatedPropertyStructure()
                        .getAnimatedPropertyDescriptors().get(3))
                        .getPropertyID())) {
            height = varValues.get(i);
        }
        internalFrame.setBounds(x, y, width, height);
    }

    /**
     * Get the animated property structure.
     * 
     * @return The animated property structure.
     */
    public AnimatedPropertyStructure getAnimatedPropertyStructure() {
        if (animatedPropertyStructure==null) {
            animatedPropertyStructure = new AnimatedPropertyStructure();
            AnimatedPropertyDescriptor aniProDesc1 = new AnimatedPropertyDescriptor(
                    0, m.getString("x"));
            AnimatedPropertyDescriptor aniProDesc2 = new AnimatedPropertyDescriptor(
                    1, m.getString("y"));
            AnimatedPropertyDescriptor aniProDesc3 = new AnimatedPropertyDescriptor(
                    2, m.getString("width"));
            AnimatedPropertyDescriptor aniProDesc4 = new AnimatedPropertyDescriptor(
                    3, m.getString("height"));
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc1);
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc2);
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc3);
            animatedPropertyStructure
                    .addAnimatedPropertyDescriptor(aniProDesc4);
        }
        return animatedPropertyStructure;
    }

    /**
     * Get plugs number.
     * 
     * @return The plug's number.
     */
    public int getPlugCount() {
        return plugCount;
    }

    /**
     * Actor is active (on stage).
     * 
     * @param animationSession
     *            The actor's animation session.
     */
    public void onStage(AnimationSession animationSession) {
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, PhotoFrame.this);
        internalFrame.setVisible(true);
    }

    /**
     * Actor is inactive (off stage).
     * 
     * @param animationSession
     *            The actor's animation session.
     */
    public void offStage(AnimationSession animationSession) {
        ESlateInternalFrame internalFrame = (ESlateInternalFrame) SwingUtilities
                .getAncestorOfClass(ESlateInternalFrame.class, PhotoFrame.this);
        internalFrame.setVisible(false);
    }

    /**
     * Get actor name.
     * 
     * @return The actor's name.
     */
    public String getActorName() {
        return m.getString("actorName");
    }

    /**
     * Add a listener for actor name events.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addActorNameListener(ActorNameListener listener) {
        synchronized (actorNameListeners) {
            if (! actorNameListeners.contains(listener)) {
                actorNameListeners.add(listener);
            }
        }
    }

    /**
     * Remove a listener from actor's name events.
     * @param	listener	The listener to remove.
     */
    public void removeActorNameListener(ActorNameListener listener) {
        synchronized (actorNameListeners) {
            int ind = actorNameListeners.indexOf(listener);
            if (ind>=0) {
                actorNameListeners.remove(ind);
            }
        }
    }

    /**
     * Fires all listeners registered for milestone events.
     * @param	actorName  The actor's name that changed.
     */
    public void fireActorNameListeners(String actorName) {
        ArrayList listeners;
        synchronized (actorNameListeners) {
            listeners = (ArrayList) (actorNameListeners.clone());
        }
        int size = listeners.size();
        for (int i = 0; i<size; i++) {
            ActorNameListener l = (ActorNameListener) (listeners.get(i));
            ActorNameEvent e = new ActorNameEvent(this, actorName);
            l.actorNameChanged(e);
        }
    }

    /**
     * Turns an Icon into a NewRestorableImageIcon (in case its not)
     * @param icon The icon to be transformed to NewRestorableImageIcon
     */
    public NewRestorableImageIcon toRestorableImageIcon(Icon icon) {
        
        if (icon == null){
            return null;
        }
        if (icon instanceof NewRestorableImageIcon)
            return new NewRestorableImageIcon(((NewRestorableImageIcon) icon) .getImage());
        if (icon instanceof ImageIcon){
            return new NewRestorableImageIcon(((ImageIcon) icon).getImage());
        }
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.getGraphics();
        icon.paintIcon(this, g, 0, 0);
        g.dispose();
        return new NewRestorableImageIcon(b);
    }
}
