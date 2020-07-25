package gr.cti.eslate.webWindow;


import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleInputMultipleOutputPlug;
import gr.cti.eslate.base.ParentChangedEvent;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.container.ESlateContainer;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.eslateButton.ESlateButton;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.eslateToolBar.VisualGroup;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.sharedObject.UrlSO;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.StorageStructure;
import horst.webwindow.HTMLRenderer;
import horst.webwindow.WebWindow;
import horst.webwindow.event.LinkEvent;
import horst.webwindow.event.LinkListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;


public class ESlateWebBrowser extends JPanel implements WindowListener, 
                                                        ActionListener, 
                                                        ChangeListener, 
                                                        ItemListener,
                                                        KeyListener, 
                                                        ESlatePart, 
                                                        Externalizable, 
                                                        DefaultStateSetter {

    JMenuItem fontItem, encodingItem, clearHistoryItem, findItem, findNextItem, unLoadItem, openItem, closeItem, readerItem;
    ESlateContainer container;//= new ESlateContainer(this);
    JCheckBoxMenuItem renderWhileParsingItem, progressiveDisplayItem, sizableItem, appletsItem, tooltipItem, imagesItem, debugMessagesItem;
    MenuItem reloadPopup, findPopup, openPopup, searchPopup;
    ESlateButton open, back, forward, home, reload, search, print, stop, about;
    StatusPanel m_statusPanel;
    LocationPanel m_location;
    JMenuBar menuBar;
    ESlateToolBar m_toolbar;
    JPanel topPanel;
    public HTMLRenderer m_htmlPane;
    WebWindow m_webwindow;
    String imgDir;
    String homeURL, searchURL;
    String mwdFileLocation;
    History history = new History();
    SharedObjectPlug plug, plug2;
    private boolean containerIsShowing = false, plugsCreated = false;
    private String openedFileType = "";
    String currentPage = null;

    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    private ESlateHandle handle;
    private StringSO stringSO;
    private UrlSO urlSO;
    private static final int  FORMAT_VERSION = 1;

    private boolean menuBarVisible = false, toolBarVisible = false, locationPanelVisible = false, statusPanelVisible = false;
    private boolean menuBarUsed = false, locationPanelUsed = false;

    static final long serialVersionUID = -1414141414141414L;
    private final static String version = "2.0.4";

    private static ResourceBundle bundleMessages;
    LinkListener navigationListener;
    ArrayList<LinkListener> linkListeners = new ArrayList<LinkListener>();

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
     * Timer which measures the time required for the construction of the
     * WebWindow component, used by ESlateWebBrowser.
     */
    PerformanceTimer webWindowTimer;
    /**
     * Timer which measures the time required for initializing the E-Slate
     * aspect of the component.
     */
    PerformanceTimer initESlateAspectTimer;

    /**
     * The listener that notifies about changes to the state of the
     * Performance Manager.
     */
    PerformanceListener perfListener = null;

    private ESlateInfo getInfo() {
        String[] info = {
                bundleMessages.getString("part"),
                bundleMessages.getString("development"),
                //bundleMessages.getString("funding"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }

    public ESlateWebBrowser(String path, boolean showMenus, boolean showButtons, boolean showURLField) {
        this(path,showMenus,showButtons,showURLField,true);
    }

    public ESlateWebBrowser(ObjectInput in) throws Exception {

        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.webWindow.BrowserBundle", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        initializeCommon();
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
        getESlateHandle();
        initialize(in);

    }

    public ESlateWebBrowser() {
        this(null,false,false,false,true);
    }

    // note : for the below constructor the parameter notUsedParameter is useless, but its used to seperate the
    // public constructor ESlateWebBrowser(String path, boolean showMenus, boolean showButtons, boolean showURLField)
    // from the below one which is private
    private ESlateWebBrowser(String path, boolean showMenus, boolean showButtons, boolean showURLField, boolean notUsedParameter){
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.webWindow.BrowserBundle", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.constructionStarted(this);
        pm.init(constructorTimer);

        initializeCommon();
        initialize();
        if (path != null){
            setMenuBarVisible(showMenus);
            setLocationPanelVisible(showURLField);
            setToolBarVisible(showButtons);
            loadPage(path, true);
        }
        m_toolbar.getESlateHandle().addESlateListener(new ESlateAdapter(){
            public void parentChanged(ParentChangedEvent e){
                ESlateHandle oldParent = e.getOldParent();
                ESlateHandle newParent = e.getNewParent();
                if (handle.equals(oldParent) && !handle.equals(newParent)) {
                    m_toolbar = null;
                    setToolBarVisible(false);
                }
            }
        });
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
    }


    public WebWindow getWebWindow(){
        return this.m_webwindow;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == encodingItem) {
            JFrame frame = new JFrame();
            Image image = logo.getImage();

            frame.setIconImage(image);
            EncodingDialog dlg = new EncodingDialog(frame, bundleMessages.getString("Character Encoding"), m_htmlPane);

            dlg.showDialog(dlg, this, true);
        } else if (src == findNextItem)
            findNext();
        else if (src == print)
            m_htmlPane.printPage();
        else if (src == findItem || src == findPopup) {
            JFrame frame = new JFrame();
            Image image = logo.getImage();

            frame.setIconImage(image);
            FindTextDialog dlg = new FindTextDialog(frame, bundleMessages.getString("Find Text"), m_webwindow);

            FindTextDialog.showDialog(dlg, this, true);
        } else if (src == stop)
            m_htmlPane.stop();
        else if (src == reload || src == reloadPopup)
            reload();
        else if (src == openItem || src == open || src == openPopup)
            openFile();
        else if (src == back)
            goBack();
        else if (src == forward)
            goForward();
        else if (src == home) {
            try{
                URL home = new URL(homeURL);
                m_htmlPane.loadPage(home);
            }catch (MalformedURLException exc) {
              System.out.println("Malformed home URL");
            }
        }else if (src == search || src == searchPopup) {
            try{
                URL search = new URL(searchURL);
                m_htmlPane.loadPage(search);
            }catch (MalformedURLException exc) {
              System.out.println("Malformed search URL");
            }
        }else if (src == unLoadItem) {
            m_htmlPane.unLoadPage();
            m_htmlPane.repaint();
        } else if (src == unLoadItem)
            m_htmlPane.unLoadPage();
        else if (src == closeItem)
            System.out.println("dispose should happen..");
        else if (src == clearHistoryItem)
            clearHistory();
    }

    void createLocationPanel() {
        ImageButton img = new ImageButton((new ImageIcon(getClass().getResource("Images/World.gif")))/*.getImage()*/);

        img.bCanHighlight = false;
        m_location = new LocationPanel(img);
        m_location.textField.addKeyListener(this);
        m_location.textField.setText(currentPage);
    }

    void createMenus() {
        menuBar = new JMenuBar();
        debugMessagesItem = new JCheckBoxMenuItem(bundleMessages.getString("Show Debug Messages"));
        //iFramesItem = new JCheckBoxMenuItem(bundleMessages.getString("IFRAMES Enabled"));
        //iFramesItem.setState(true);
        renderWhileParsingItem = new JCheckBoxMenuItem(bundleMessages.getString("Render While Parsing"));
        renderWhileParsingItem.setState(false);
        //enableFramesItem = new JCheckBoxMenuItem(bundleMessages.getString("Frames Enabled"));
        //enableFramesItem.setState(true);
        progressiveDisplayItem = new JCheckBoxMenuItem(bundleMessages.getString("Progressive Image Display"));
        progressiveDisplayItem.setState(false);
        sizableItem = new JCheckBoxMenuItem(bundleMessages.getString("Always Sizable Frames"));
        sizableItem.setState(false);
        tooltipItem = new JCheckBoxMenuItem(bundleMessages.getString("Enable Tool Tips"));
        tooltipItem.setState(false);
        appletsItem = new JCheckBoxMenuItem(bundleMessages.getString("Load Applets"));
        appletsItem.setState(true);
        imagesItem = new JCheckBoxMenuItem(bundleMessages.getString("Load Images"));
        imagesItem.setState(true);
        fontItem = new JMenuItem(bundleMessages.getString("Set Font..."));
        fontItem.setEnabled(false);
        encodingItem = new JMenuItem(bundleMessages.getString("Character Encoding..."));
        clearHistoryItem = new JMenuItem(bundleMessages.getString("ClearHistory"));
        findItem = new JMenuItem(bundleMessages.getString("Find..."));
        findItem.setEnabled(false);
        findNextItem = new JMenuItem(bundleMessages.getString("FindNext"));
        findNextItem.setEnabled(false);
        unLoadItem = new JMenuItem(bundleMessages.getString("Unload Page"));
        unLoadItem.setEnabled(true);
        openItem = new JMenuItem(bundleMessages.getString("Open..."));
        closeItem = new JMenuItem(bundleMessages.getString("Close"));
        //aboutItem = new JMenuItem(bundleMessages.getString("About..."));
        JMenu fileItem = new JMenu(bundleMessages.getString("File"));

        fileItem.add(openItem);
        fileItem.add(unLoadItem);
        fileItem.add(closeItem);
        JMenu editMenu = new JMenu(bundleMessages.getString("Edit"));

        editMenu.add(encodingItem);
        editMenu.add(clearHistoryItem);
        editMenu.add(findItem);
        editMenu.add(findNextItem);
        //editMenu.add(iFramesItem);
        editMenu.add(renderWhileParsingItem);
        //editMenu.add(enableFramesItem);
        editMenu.add(progressiveDisplayItem);
        editMenu.add(sizableItem);
        editMenu.add(tooltipItem);
        editMenu.add(appletsItem);
        editMenu.add(imagesItem);
        //JMenu aboutMenu = new JMenu(bundleMessages.getString("Help"));
        //aboutMenu.add(aboutItem);
        //iFramesItem.addItemListener(this);
        renderWhileParsingItem.addItemListener(this);
        //enableFramesItem.addItemListener(this);
        fontItem.addActionListener(this);
        encodingItem.addActionListener(this);
        //proxyItem.addActionListener(this);
        findItem.addActionListener(this);
        findNextItem.addActionListener(this);
        progressiveDisplayItem.addItemListener(this);
        debugMessagesItem.addItemListener(this);
        sizableItem.addItemListener(this);
        tooltipItem.addItemListener(this);
        appletsItem.addItemListener(this);
        imagesItem.addItemListener(this);
        unLoadItem.addActionListener(this);
        openItem.addActionListener(this);
        closeItem.addActionListener(this);
        //aboutItem.addActionListener(this);
        menuBar.add(fileItem);
        menuBar.add(editMenu);
        //menuBar.add(aboutMenu);
        //setJMenuBar(menuBar);
    }

    void createPopupMenu() {
        PopupMenu popup = new PopupMenu();

        reloadPopup = new MenuItem("Reload");
        reloadPopup.addActionListener(this);
        popup.add(reloadPopup);
        findPopup = new MenuItem("Find");
        findPopup.addActionListener(this);
        popup.add(findPopup);
        openPopup = new MenuItem("Open...");
        openPopup.addActionListener(this);
        popup.add(openPopup);
        searchPopup = new MenuItem("Search...");
        searchPopup.addActionListener(this);
        popup.add(searchPopup);
        m_webwindow.setPopupMenu(popup);
    }

    void createToolBar() {
        if (m_toolbar != null) return;
        m_toolbar = new ESlateToolBar();
        //m_toolbar.addButton(open = new ImageButton((new ImageIcon(getClass().getResource("Images/Open.gif")))/*.getImage()*/, bundleMessages.getString("Open")), 0);
        //m_toolbar.addButton(back = new ImageButton((new ImageIcon(getClass().getResource("Images/Left.gif")))/*.getImage()*/, bundleMessages.getString("Back")), open.getWidth());
        //m_toolbar.addButton(forward = new ImageButton((new ImageIcon(getClass().getResource("Images/Right.gif")))/*.getImage()*/, bundleMessages.getString("Forward")), back.getWidth());
        //m_toolbar.addButton(home = new ImageButton((new ImageIcon(getClass().getResource("Images/Home.gif")))/*.getImage()*/, bundleMessages.getString("Home")), forward.getWidth());
        //m_toolbar.addButton(reload = new ImageButton((new ImageIcon(getClass().getResource("Images/RotCCDown.gif")))/*.getImage()*/, bundleMessages.getString("Reload")), home.getWidth());
        //m_toolbar.addButton(search = new ImageButton((new ImageIcon(getClass().getResource("Images/Search.gif")))/*.getImage()*/, bundleMessages.getString("Search")), reload.getWidth());
        //m_toolbar.addButton(stop = new ImageButton((new ImageIcon(getClass().getResource("Images/Stop.gif")))/*.getImage()*/, bundleMessages.getString("Stop")), search.getWidth());
        //m_toolbar.addButton(print = new ImageButton((new ImageIcon(getClass().getResource("Images/print.gif")))/*.getImage()*/, bundleMessages.getString("Print")), stop.getWidth());
        //m_toolbar.addButton(about = new ImageButton((new ImageIcon(getClass().getResource("Images/Help.gif"))).getImage(), bundleMessages.getString("About")));*/
        Dimension d = new Dimension(30,26);
        VisualGroup defaultGroup = m_toolbar.addVisualGroup();
        m_toolbar.add(defaultGroup, open = new ESlateButton(), "Open");
        open.setMaximumSize(d);
        open.setMinimumSize(d);
        open.setPreferredSize(d);
        open.setIcon(new ImageIcon(getClass().getResource("Images/Open.gif")));
        m_toolbar.add(defaultGroup, back = new ESlateButton(), "Back");
        back.setMaximumSize(d);
        back.setMinimumSize(d);
        back.setPreferredSize(d);
        back.setIcon(new ImageIcon(getClass().getResource("Images/Left.gif")));
        m_toolbar.add(defaultGroup, forward = new ESlateButton(), "Forward");
        forward.setMaximumSize(d);
        forward.setMinimumSize(d);
        forward.setPreferredSize(d);
        forward.setIcon(new ImageIcon(getClass().getResource("Images/Right.gif")));
        m_toolbar.add(defaultGroup, home = new ESlateButton(), "Home");
        home.setMaximumSize(d);
        home.setMinimumSize(d);
        home.setPreferredSize(d);
        home.setIcon(new ImageIcon(getClass().getResource("Images/Home.gif")));
        m_toolbar.add(defaultGroup, reload = new ESlateButton(), "Reload");
        reload.setMaximumSize(d);
        reload.setMinimumSize(d);
        reload.setPreferredSize(d);
        reload.setIcon(new ImageIcon(getClass().getResource("Images/RotCCDown.gif")));
        m_toolbar.add(defaultGroup, search = new ESlateButton(),"Search");
        search.setMaximumSize(d);
        search.setMinimumSize(d);
        search.setPreferredSize(d);
        search.setIcon(new ImageIcon(getClass().getResource("Images/Search.gif")));
        m_toolbar.add(defaultGroup, stop = new ESlateButton(), "Stop");
        stop.setMaximumSize(d);
        stop.setMinimumSize(d);
        stop.setPreferredSize(d);
        stop.setIcon(new ImageIcon(getClass().getResource("Images/Stop.gif")));
        m_toolbar.add(defaultGroup, print = new ESlateButton(), "Print");
        print.setMaximumSize(d);
        print.setMinimumSize(d);
        print.setPreferredSize(d);
        print.setIcon(new ImageIcon(getClass().getResource("Images/print.gif")));
        //m_toolbar.addButton(about = new ImageButton((new ImageIcon(getClass().getResource("Images/Help.gif"))).getImage(), bundleMessages.getString("About")));*/


        open.addActionListener(this);
        back.addActionListener(this);
        forward.addActionListener(this);
        home.addActionListener(this);
        reload.addActionListener(this);
        search.addActionListener(this);
        stop.addActionListener(this);
        print.addActionListener(this);
        //about.addActionListener(this);
        getESlateHandle().add(m_toolbar.getESlateHandle());
    }

    void findNext() {
        int pos = m_webwindow.findNext();

        if (pos == -1) {
            // MessageBox dlg = new MessageBox(frame, "Find Text", "Text Not Found!");
            JFrame frame = new JFrame();

            frame.setIconImage(logo.getImage());
            JOptionPane.showInternalMessageDialog(frame, "information", "information", JOptionPane.INFORMATION_MESSAGE);

            //dlg.setVisible(true);
        }
    }

    public static final String getInternetFormat(String str) {
        String s = "";

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c == '\\')
                s = s + "/";
            else
                s = s + c;
        }

        return s;
    }

    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();

        if (src == renderWhileParsingItem)
            m_htmlPane.setRenderWhileParsing(renderWhileParsingItem.getState());
        else
        if (src == progressiveDisplayItem)
            m_htmlPane.setProgressiveImageDisplay(progressiveDisplayItem.getState());
        else
        if (src == tooltipItem)
            m_htmlPane.setToolTips(tooltipItem.getState());
        else
        if (src == appletsItem)
            m_htmlPane.setLoadApplets(appletsItem.getState());
        else
        if (src == imagesItem)
            m_htmlPane.setLoadImages(imagesItem.getState());
    }

    public void keyPressed(KeyEvent ev) {
        //System.out.println("got keyPressed");
        if (ev.getKeyCode() == 10) {
            System.out.println("ENTER PRESSED, page should load...");
            //URL u = Helper.getURL(m_location.textField.getText());
            String u = m_location.textField.getText();

            if (u != null) {
                //System.out.println("page loading...");
                loadPage(u, true);
                //m_htmlPane.loadPage(u);
                //setLocation(u.toString());
            }
        } else if (ev.getKeyCode() == 114)
            findNext();
    }

    public void keyReleased(KeyEvent keyevent) {}

    public void keyTyped(KeyEvent keyevent) {}

    void layoutBrowser() {
        //createToolBar();
        if (toolBarVisible)
            topPanel.add(m_toolbar, BorderLayout.CENTER);

        if (locationPanelUsed) {
            //createLocationPanel();
            if (locationPanelVisible)
                topPanel.add(m_location, BorderLayout.SOUTH);
        }
//        setMenuBarVisible(true);
        if (menuBarUsed) {
            //createMenus();
            if (menuBarVisible)
                topPanel.add(menuBar, BorderLayout.NORTH);
        }
//        setSize(500, 500);
    }

    public void mouseEnteredLink(LinkEvent evt) {
        m_statusPanel.setLabel(evt.getLink());
    }

    public void mouseExitedLink(LinkEvent evt) {
        m_statusPanel.setLabel("");
    }

    void openFile() {
        JFrame frame = new JFrame();
        Image image = logo.getImage();

        frame.setIconImage(image);
        FileDialog fileDialog = new FileDialog(frame);

        fileDialog.setMode(0);
        fileDialog.setVisible(true);
        String file = fileDialog.getFile();

        if (file == null)
            return;
        String dir = fileDialog.getDirectory();

        dir = getInternetFormat(dir);
        file = getInternetFormat(file);
        URL u = Helper.getURL("file:/" + dir + file);

        if (u != null)
            loadPage(u.toString(), true);
    }

    public void stateChanged(ChangeEvent e) {
        m_htmlPane.setZoom((double) m_statusPanel.m_zoomSlider.getValue() / 100D);
        m_htmlPane.repaint();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void windowActivated(WindowEvent windowevent) {}

    public void windowClosed(WindowEvent event) {
        System.exit(0);
    }

    public void windowClosing(WindowEvent event) {// dispose();
    }

    public void windowDeactivated(WindowEvent windowevent) {}

    public void windowDeiconified(WindowEvent windowevent) {}

    public void windowIconified(WindowEvent windowevent) {}

    public void windowOpened(WindowEvent windowevent) {}

    public void addLinkListener(LinkListener l) {
        //System.out.println("Listener added!");
        myAddLinkListener(l);
    }

    private void myAddLinkListener(LinkListener  l) {
        m_htmlPane.removeLinkListener(navigationListener);
        for (int i = 0; i < linkListeners.size(); i++)
            m_htmlPane.removeLinkListener((LinkListener) linkListeners.get(i));
        linkListeners.add(0, l);
        for (int i = 0; i < linkListeners.size(); i++)
            m_htmlPane.addLinkListener((LinkListener) linkListeners.get(i));
        m_htmlPane.addLinkListener(navigationListener);
    }

    public void removeLinkListener(LinkListener l) {

        //System.out.println("removeListener called for listener : "+l);
        linkListeners.remove(l);
        m_htmlPane.removeLinkListener(l);
    }

    public ESlateHandle getESlateHandle() {

        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);

            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateWebBrowser"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.BrowserPrimitives");
            handle.setInfo(getInfo());
            setPlugsUsed(true);

            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        linkListeners.clear();
                        m_webwindow.dereference();
                        m_webwindow.removeAll();
                        m_webwindow = null;
                        removeAll();
                        if (menuBarUsed) {
                            renderWhileParsingItem.removeItemListener(ESlateWebBrowser.this);
                            fontItem.removeActionListener(ESlateWebBrowser.this);
                            encodingItem.removeActionListener(ESlateWebBrowser.this);
                            findItem.removeActionListener(ESlateWebBrowser.this);
                            findNextItem.removeActionListener(ESlateWebBrowser.this);
                            progressiveDisplayItem.removeItemListener(ESlateWebBrowser.this);
                            debugMessagesItem.removeItemListener(ESlateWebBrowser.this);
                            sizableItem.removeItemListener(ESlateWebBrowser.this);
                            tooltipItem.removeItemListener(ESlateWebBrowser.this);
                            appletsItem.removeItemListener(ESlateWebBrowser.this);
                            imagesItem.removeItemListener(ESlateWebBrowser.this);
                            unLoadItem.removeActionListener(ESlateWebBrowser.this);
                            openItem.removeActionListener(ESlateWebBrowser.this);
                            closeItem.removeActionListener(ESlateWebBrowser.this);
                        }
                        reloadPopup.removeActionListener(ESlateWebBrowser.this);
                        findPopup.removeActionListener(ESlateWebBrowser.this);
                        openPopup.removeActionListener(ESlateWebBrowser.this);
                        searchPopup.removeActionListener(ESlateWebBrowser.this);
                        if (toolBarVisible) {
                            if (open != null)
                                open.removeActionListener(ESlateWebBrowser.this);
                            if (back != null)
                                back.removeActionListener(ESlateWebBrowser.this);
                            if (forward != null)
                                forward.removeActionListener(ESlateWebBrowser.this);
                            if (home != null)
                                home.removeActionListener(ESlateWebBrowser.this);
                            if (reload != null)
                                reload.removeActionListener(ESlateWebBrowser.this);
                            if (search != null)
                                search.removeActionListener(ESlateWebBrowser.this);
                            if (stop != null)
                                stop.removeActionListener(ESlateWebBrowser.this);
                            if (print != null)
                                print.removeActionListener(ESlateWebBrowser.this);
                        }
                        if (m_statusPanel != null) {
                            m_statusPanel.m_zoomSlider.removeChangeListener(ESlateWebBrowser.this);
                            m_statusPanel.dispose();
                            m_statusPanel = null;
                        }
                        if (m_location != null)
                            m_location.textField.removeKeyListener(ESlateWebBrowser.this);
                        m_htmlPane.removeLinkListener(navigationListener);
                        m_htmlPane = null;

                        PerformanceManager pm = PerformanceManager.getPerformanceManager();
                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    /////////////////////////////// ADDED PROPERTIES\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void setMenuBarVisible(boolean visible) {
        if (menuBar == null)
            return;
        if (visible == menuBarVisible) return;
        menuBarVisible = visible;
        //createMenus();
        if (!visible)
            topPanel.remove(menuBar);
        else if (visible)
            topPanel.add(menuBar, BorderLayout.NORTH);

        repaint();
        revalidate();
    }

    public boolean isMenuBarVisible() {
        return menuBarVisible;

    }

    public void setToolBarVisible(boolean visible) {
        if (toolBarVisible == visible) return;
        if (visible && m_toolbar == null){
            createToolBar();
        }
        toolBarVisible = visible;
        //createToolBar();
        if (!toolBarVisible)
            topPanel.remove(m_toolbar);
        else
            topPanel.add(m_toolbar, BorderLayout.CENTER);

        repaint();
        revalidate();
    }

    public boolean isToolBarVisible() {
        return toolBarVisible;
    }

    public void setLocationPanelVisible(boolean visible) {
        if (m_location == null)
            return;
        if (locationPanelVisible == visible) return;
        locationPanelVisible = visible;
        //createLocationPanel();
        if (!locationPanelVisible)
            topPanel.remove(m_location);
        else
            topPanel.add(m_location, BorderLayout.SOUTH);

        repaint();
        revalidate();
    }

    public boolean isLocationPanelVisible() {
        return locationPanelVisible;

    }

    public void setStatusPanelVisible(boolean visible) {
        if (statusPanelVisible == visible) return;
        statusPanelVisible = visible;

        if (!statusPanelVisible)
            /*completePanel.*/remove(m_statusPanel);
        else
            /*completePanel.*/add(m_statusPanel, BorderLayout.SOUTH);

        repaint();
        revalidate();
    }

    public boolean isStatusPanelVisible() {
        return statusPanelVisible;

    }

    public void setLocation(String text) {
        loadPage(text, true);
        if (stringSO != null)
            stringSO.setString(text);
        try {
            if (urlSO != null)
                urlSO.setURL(new URL(text));
        } catch (MalformedURLException e) {
            System.out.println("Improper URL");
        }
    }

    public void setLocation(URL url) {
        loadPage(url.toString(), true);
        if (urlSO != null)
            urlSO.setURL(url);
        if (stringSO != null)
            stringSO.setString(url.toString());

    }

    public void setCurrentPage(URL url){
        loadPage(url.toString(), true);
        if (urlSO != null)
            urlSO.setURL(url);
        if (stringSO != null)
            stringSO.setString(url.toString());
    }

    public void setCurrentPage(String text) {
        loadPage(text, true);
        if (stringSO != null)
            stringSO.setString(text);
        try {
            if (urlSO != null)
                urlSO.setURL(new URL(text));
        } catch (MalformedURLException e) {
            System.out.println("Improper URL");
        }
    }


    public void loadPage(String s, boolean addToHistory) {

        //System.out.println("LOADPAGE called, going to -> "+s);
        if (s == null || s.length() == 0){
          clearContent();
          return;
        }
        try {
            if (s.endsWith(".mwd")) {
                //System.out.println("done that...");
                File file = new File(s);

                //System.out.println("check 1");
                if (file.exists()) {
                    //System.out.println("loading a microworld!");
                    if (addToHistory)
                        history.add(s);
                    if (!containerIsShowing) {
                        switchEnvironmentToContainer(s);
                        //System.out.println("After call to switchEnvironmentToContainer()");
                    } else
                        container.loadLocalMicroworld(s, false, true);
                    //System.out.println("check 2");
                    //System.out.println("reached here");
                    if (m_location != null) {

                        m_location.setLocation(s);
                        m_location.adjustFileTypeSettings("mwd");
                    }
                }
            } else {
                if (addToHistory)
                    history.add(s);
                //System.out.println("loading an html page");
                if (containerIsShowing)
                    switchEnvironmentToHtmlPane();
                if (m_htmlPane == null)
                  System.out.println("m_htmlPane is null!");
                 URL u = null;
                try{
                  u = new URL(s);
                }catch (NullPointerException exc){
                  System.out.println("new URL(s) fails! Exception caught is : ");
                  exc.printStackTrace();
                }
                if (u == null || s == null)
                  System.out.println("URL or string is null");
                m_htmlPane.loadPage(u);
                currentPage = s;
                if (m_location != null) {
                    m_location.setLocation(s);
                    m_location.adjustFileTypeSettings("html");
                }
            }

        } catch (MalformedURLException exc) {
            System.out.println("Malformed URL - Page or Microworld won't be loaded");
            //m_location.setLocation("Malformed URL - Try to give a valid URL");
        }
        //System.out.println("loadPage successfully terminated");
    }

    public String getURLLocation() {
        return currentPage;
    }

    public String getCurrentPage(){
        return currentPage;
    }

    public void setHomeURL(String s) {
        homeURL = s;
    }

    public void setSearchURL(String s) {
        searchURL = s;
    }

    public String getHomeURL() {
        return homeURL;
    }

    public String getSearchURL() {
        return searchURL;
    }

    public void reload() {
        if (!containerIsShowing)
            m_htmlPane.reload();
        else
            container.loadLocalMicroworld(mwdFileLocation, true, true);
        revalidate();
        repaint();
    }

    public void printPage() {
        if (!containerIsShowing)
            m_htmlPane.printPage();
    }

    public void goBack() {
        //System.out.println("goBack() called");
        history.back();
        //System.out.println("page to be loaded : "+ (String) history.getURLS().elementAt(history.getURLPointer()));
        loadPage((String) history.getURLS().elementAt(history.getURLPointer()), false);
        revalidate();
        repaint();
    }

    public void goForward() {
        //System.out.println("goForward() called");
        history.forward();
        //System.out.println("page to be loaded : "+ (String) history.getURLS().elementAt(history.getURLPointer()));
        loadPage((String) history.getURLS().elementAt(history.getURLPointer()), false);
        revalidate();
        repaint();

    }

    public void stop() {
        m_htmlPane.stop();
    }

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        initialize(in);
    }

    /**
     * Writes to the ESlateFieldMap values and properties to be stored
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());
        if (history.getURLPointer() >= 0) {
            fieldMap.put("HistoryIndexPointer", history.getURLPointer());
            fieldMap.put("History", history.getURLS());
            fieldMap.put("Location", history.getCurrentURL());
        }
        fieldMap.put("HomeURL", getHomeURL());
        fieldMap.put("SearchURL", getSearchURL());

        fieldMap.put("LoadApplets", m_htmlPane.getLoadApplets());

        fieldMap.put("LoadImages", m_htmlPane.getLoadImages());
        fieldMap.put("CharacterEncoding", m_htmlPane.getCharacterEncoding());
        fieldMap.put("AlwaysSizableFrames", m_htmlPane.getAlwaysSizableFrames());
        fieldMap.put("ProgressiveImageDisplay", m_htmlPane.getProgressiveDisplay());
        fieldMap.put("RenderWhileParsing", m_htmlPane.getRenderWhileParsing());
        fieldMap.put("ToolTips", m_htmlPane.getToolTips(true));
        fieldMap.put("PlugsUsed", isPlugsUsed());

        fieldMap.put("Opaque", isOpaque());

        JPanel test = new JPanel();

        if (getBorder() != null && !(getBorder() instanceof UIResource) && !getBorder().equals(test.getBorder())) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        } else if (getBorder() == null) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        }

        if (m_webwindow.getBorder() != null && !m_webwindow.getBorder().equals(test.getBorder())) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(m_webwindow.getBorder(), this);

                fieldMap.put("HtmlPaneBorder", bd);
            } catch (Throwable thr) {}
        } else if (m_webwindow.getBorder() == null) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(m_webwindow.getBorder(), this);

                fieldMap.put("HtmlPaneBorder", bd);
            } catch (Throwable thr) {}
        }
        fieldMap.put("UseMenuBar", isMenuBarUsed());
        //fieldMap.put("UseToolBar", isToolBarUsed());
        fieldMap.put("UseLocationPanel", isLocationPanelUsed());
        fieldMap.put("StatusPanelVisible", isStatusPanelVisible());
        fieldMap.put("LocationPanelVisible", isLocationPanelVisible());
        fieldMap.put("ToolBarVisible", isToolBarVisible());
        fieldMap.put("BrowserMenuBarVisible", isMenuBarVisible());

//        if (toolBarUsed) {
        String toolbarHandleName = null;
        ArrayList<ESlateHandle> children = new ArrayList<ESlateHandle>();
        if (m_toolbar != null) {
            children.add(m_toolbar.getESlateHandle());
            toolbarHandleName = m_toolbar.getESlateHandle().getComponentName();
        }
//            ESlateHandle th = m_toolbar.getESlateHandle();
//            ESlateHandle[] children = new ESlateHandle[] {th};
//System.out.println("Saving toolbar: " + th);
        getESlateHandle().saveChildren(fieldMap, "children", children);
        fieldMap.put("ToolbarName", toolbarHandleName);

//        }

        out.writeObject(fieldMap);
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    public void setCharacterEncoding(String s) {
        m_htmlPane.setCharacterEncoding(s);
    }

    public String getCharacterEncoding() {
        return m_htmlPane.getCharacterEncoding();
    }

    private void switchEnvironmentToContainer(String MworldToOpen) {
        //System.out.println("switched to container");
        containerIsShowing = true;
        print.setEnabled(false);
        //System.out.println("check3");
        /*completePanel.*/remove(m_webwindow); //browserPanel);
        //System.out.println("check4");
        try {
            //System.out.println("container: " + container);
            /*completePanel.*/add(container, BorderLayout.CENTER);
            //System.out.println("check 4.5");
        } catch (Exception e) {
            //System.out.println("Exception");
            e.printStackTrace();
        }
        //System.out.println("check5");
        container.loadLocalMicroworld(MworldToOpen, true, true);
        //System.out.println("check6");
        if (m_location != null) {
            m_location.textField.setText(MworldToOpen);
            m_location.adjustFileTypeSettings("mwd");
        }

    }

    private synchronized void switchEnvironmentToHtmlPane() {
        containerIsShowing = false;
        print.setEnabled(true);
        container.closeMicroworld(true);
        /*completePanel.*/remove(container);
        /*completePanel.*/add(m_webwindow/*browserPanel*/, BorderLayout.CENTER);
        m_location.adjustFileTypeSettings("html");
    }

    private String parseLink(String link) {
        if (link.startsWith("ESlateMwd:")) {
            mwdFileLocation = link.substring(10, link.length());
            return mwdFileLocation;
        } else return link;
    }

    public String getOpenedFileType() {
        return openedFileType;
    }

    public void setOpenedFileType(String s) {
        m_location.adjustFileTypeSettings(s);
    }

    public void clearHistory() {
        history.clear();
    }

    public HTMLRenderer getHTMLRenderer() {
        return m_htmlPane;
    }

    public static void main(String args[]) {
        JFrame browser = new JFrame();

        browser.getContentPane().add(new ESlateWebBrowser());
        browser.setSize(500, 500);
        browser.setVisible(true);
    }

    public void printListeners() {
        //EventListener[] l = getListeners(LinkListener.class);
        //for (int i=0;i<l.length;i++)
        //   System.out.println(l[i]);
        //System.out.println(l.length);
        //System.out.println("printing of listeners completed!");
    }

    public void updateUI() {
        //boolean opaque=isOpaque();
        Border border = getBorder();

        //Color bg = getBackground();
        //Color fg = getForeground();
        //Font f = getFont();
        super.updateUI();
        //setFont(f);
        //setBackground(bg);
        //setForeground(fg);
        //setOpaque(opaque);
        setBorder(border);
    }

    public void clearContent() {
        //System.out.println("clearContentCalled");
        try {
            loadPage(getClass().getResource("Files/empty.html").toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        m_location.textField.setText(" ");
    }

    private void createPlugs() {
        if (handle == null)
            return;
//System.out.println("Creating browser plugs");
        stringSO = new StringSO(handle);
        urlSO = new UrlSO(handle);
        try {
            SharedObjectListener sol = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        setLocation(((UrlSO) e.getSharedObject()).getURL());
                    }
                };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "URL", new Color(188, 143, 143),
                        gr.cti.eslate.sharedObject.UrlSO.class,
                        urlSO, sol);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        // make sure connected component gets value of vector when connected
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            UrlSO so = (UrlSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setLocation(so.getURL());
                        }
                    }
                }
            );
            handle.addPlug(plug);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

        try {
            SharedObjectListener sol2 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        setLocation(((StringSO) e.getSharedObject()).getString());
                    }
                };

            plug2 = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "URL2", new Color(139, 117, 0),
                        gr.cti.eslate.sharedObject.StringSO.class,
                        stringSO, sol2);
            plug2.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        // make sure connected component gets value of vector when connected
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            StringSO so = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setLocation(so.getString());
                        }
                    }
                }
            );
            handle.addPlug(plug2);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

    }

    private void destroyPlugs() {
        try {
            handle.removePlug(plug2);
            handle.removePlug(plug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug2 = null;
        plug = null;
        stringSO = null;
        urlSO = null;
    }

    public void setPlugsUsed(boolean create) {
        if (plugsCreated == create) 
            return;
        if (handle != null) {
            if (create)
                createPlugs();
            else
                destroyPlugs();
            plugsCreated = create;
        } else
            plugsCreated = false;
    }

    public boolean isPlugsUsed() {
        return plugsCreated;
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener == null) {
            perfListener = new PerformanceAdapter() {
                public void performanceManagerStateChanged(PropertyChangeEvent e) {
                    boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
                    // When the Performance Manager is enabled, try to attach the
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
     * been already attached, they are not attached again.
     * This method does not create any timers while the PerformanceManager is
     * disabled.
     */
    private void attachTimers() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        boolean pmEnabled = pm.isEnabled();

        // If the performance manager is disabled, install a listener which will
        // re-invoke this method when the performance manager is enabled.
        if (!pmEnabled && (perfListener == null)) {
            createPerformanceManagerListener(pm);
        }

        // Do nothing if the PerformanceManager is disabled.
        if (!pmEnabled) {
            return;
        }

        boolean timersCreated = (loadTimer != null);

        // If the component's timers have not been constructed yet, then
        // construct them. During construction, the timers are also attached.
        if (!timersCreated) {
            // Get the performance timer for this component.
            PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);

            // Construct and attach the component's timers.
            constructorTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                compoTimerGroup, bundleMessages.getString("ConstructorTimer"), true
            );
            loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                compoTimerGroup, bundleMessages.getString("LoadTimer"), true
            );
            saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                compoTimerGroup, bundleMessages.getString("SaveTimer"), true
            );
            initESlateAspectTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                compoTimerGroup, bundleMessages.getString("InitESlateAspectTimer"), true
            );

            pm.registerPerformanceTimerGroup(
                PerformanceManager.CONSTRUCTOR, constructorTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.LOAD_STATE, loadTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.SAVE_STATE, saveTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
            );
            webWindowTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                constructorTimer, bundleMessages.getString("WebWindowTimer"), true
            );
        }
    }

    private void initialize(ObjectInput in) throws IOException, ClassNotFoundException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);
//long start = System.currentTimeMillis();
        Object firstObj = in.readObject();
//        ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
        StorageStructure fieldMap = (StorageStructure) firstObj;

//System.out.println("Initialize 1: " + (System.currentTimeMillis()-start));

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));
//System.out.println("Initialize 1.1: " + (System.currentTimeMillis()-start));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
//System.out.println("Initialize 1.2: " + (System.currentTimeMillis()-start));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
//System.out.println("Initialize 1.3: " + (System.currentTimeMillis()-start));
        setMaximumSize(fieldMap.get("MaximumSize", getMaximumSize()));
//System.out.println("Initialize 1.4: " + (System.currentTimeMillis()-start));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize(fieldMap.get("PreferredSize", getPreferredSize()));
//System.out.println("Initialize 1.5: " + (System.currentTimeMillis()-start));
        if (fieldMap.containsKey("HistoryIndexPointer")) {
            history.setURLPointer(fieldMap.get("HistoryIndexPointer", history.getURLPointer()));
            history.setURLS((Vector) fieldMap.get("History", history.getURLS()));
            setLocation(fieldMap.get("Location", history.getCurrentURL()));
        }
//System.out.println("Initialize 2: " + (System.currentTimeMillis()-start));
        setHomeURL(fieldMap.get("HomeURL", getHomeURL()));
//System.out.println("Initialize 2.1: " + (System.currentTimeMillis()-start));
        setSearchURL(fieldMap.get("SearchURL", getSearchURL()));
//System.out.println("Initialize 2.2: " + (System.currentTimeMillis()-start));
        if (fieldMap.containsKey("PlugsUsed")) {
//System.out.println("Initialize 2.2.1: " + (System.currentTimeMillis()-start));
            setPlugsUsed(fieldMap.get("PlugsUsed", isPlugsUsed()));
//System.out.println("Initialize 2.2.2: " + (System.currentTimeMillis()-start));
        }
//System.out.println("Initialize 2.3: " + (System.currentTimeMillis()-start));

//System.out.println("Initialize 2.4: " + (System.currentTimeMillis()-start));
        setMenuBarUsed(fieldMap.get("UseMenuBar", isMenuBarUsed()));
        setLocationPanelUsed(fieldMap.get("UseLocationPanel", isLocationPanelUsed()));
//System.out.println("Initialize 2.5: " + (System.currentTimeMillis()-start));
        setStatusPanelVisible(fieldMap.get("StatusPanelVisible", isStatusPanelVisible()));
        setLocationPanelVisible(fieldMap.get("LocationPanelVisible", isLocationPanelVisible()));
        setMenuBarVisible(fieldMap.get("BrowserMenuBarVisible", isMenuBarVisible()));

//System.out.println("Initialize 3: " + (System.currentTimeMillis()-start));
        m_htmlPane.setLoadApplets(fieldMap.get("LoadApplets", m_htmlPane.getLoadApplets()));
        m_htmlPane.setLoadImages(fieldMap.get("LoadImages", m_htmlPane.getLoadImages()));
        m_htmlPane.setCharacterEncoding(fieldMap.get("CharacterEncoding", m_htmlPane.getCharacterEncoding()));
        m_htmlPane.setAlwaysSizableFrames(fieldMap.get("AlwaysSizableFrames", m_htmlPane.getAlwaysSizableFrames()));
        m_htmlPane.setProgressiveImageDisplay(fieldMap.get("ProgressiveImageDisplay", m_htmlPane.getProgressiveDisplay()));
        m_htmlPane.setRenderWhileParsing(fieldMap.get("RenderWhileParsing", m_htmlPane.getRenderWhileParsing()));
        m_htmlPane.setToolTips(fieldMap.get("ToolTips", m_htmlPane.getToolTips(true)));

//System.out.println("Initialize 4: " + (System.currentTimeMillis()-start));
        if (isMenuBarUsed()) {
            imagesItem.setState(fieldMap.get("LoadImages", m_htmlPane.getLoadImages()));
            appletsItem.setState(fieldMap.get("LoadApplets", m_htmlPane.getLoadApplets()));
            sizableItem.setState(fieldMap.get("AlwaysSizableFrames", m_htmlPane.getAlwaysSizableFrames()));
            progressiveDisplayItem.setState(fieldMap.get("ProgressiveImageDisplay", m_htmlPane.getProgressiveDisplay()));
            renderWhileParsingItem.setState(fieldMap.get("RenderWhileParsing", m_htmlPane.getRenderWhileParsing()));
            tooltipItem.setState(fieldMap.get("ToolTips", m_htmlPane.getToolTips(true)));
        }

//System.out.println("Initialize 5: " + (System.currentTimeMillis()-start));
        getESlateHandle().restoreChildObjects(fieldMap, "children");
        String toolbarHandleName = fieldMap.get("ToolbarName", (String) null);
        if (toolbarHandleName != null) {
            ESlateHandle toolbarHandle = getESlateHandle().getChildHandle(toolbarHandleName);
            if (toolbarHandle != null){
                m_toolbar = (ESlateToolBar) toolbarHandle.getComponent();
                setToolBarVisible(fieldMap.get("ToolBarVisible", isToolBarVisible()));
                if (m_toolbar.getTool("Open") != null){
                    open = (ESlateButton) m_toolbar.getTool("Open");
                    open.addActionListener(this);
                }
                if (m_toolbar.getTool("Back") != null){
                    back = (ESlateButton) m_toolbar.getTool("Back");
                    back.addActionListener(this);
                }
                if (m_toolbar.getTool("Forward") != null){
                    forward = (ESlateButton) m_toolbar.getTool("Forward");
                    forward.addActionListener(this);
                }
                if (m_toolbar.getTool("Home") != null){
                    home = (ESlateButton) m_toolbar.getTool("Home");
                    home.addActionListener(this);
                }
                if (m_toolbar.getTool("Reload") != null){
                    reload = (ESlateButton) m_toolbar.getTool("Reload");
                    reload.addActionListener(this);
                }
                if (m_toolbar.getTool("Search") != null){
                    search = (ESlateButton) m_toolbar.getTool("Search");
                    search.addActionListener(this);
                }
                if (m_toolbar.getTool("Print") != null){
                    print = (ESlateButton) m_toolbar.getTool("Print");
                    print.addActionListener(this);
                }
                if (m_toolbar.getTool("Stop") != null){
                    stop = (ESlateButton) m_toolbar.getTool("Stop");
                    stop.addActionListener(this);
                }
            }
        }

        //setToolBarUsed(fieldMap.get("UseToolBar", isToolBarUsed()));


        //System.out.println("Initialize 6: " + (System.currentTimeMillis()-start));
        setOpaque(fieldMap.get("Opaque", isOpaque()));


        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }

        if (fieldMap.containsKey("HtmlPaneBorder")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("HtmlPaneBorder");

                setHtmlPaneBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }

        layoutBrowser();

//System.out.println("Initialize 7: " + (System.currentTimeMillis()-start));
        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    }

    private void initializeCommon() {
//long start = System.currentTimeMillis();
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
//System.out.println("initializeCommon() 1: "  + (System.currentTimeMillis()-start));
//        browserPanel = new MainPanel(true);
//System.out.println("initializeCommon() 2: "  + (System.currentTimeMillis()-start));
//        browserPanel.setLayout(new GridBagLayout());
//        completePanel = new JPanel();
//        completePanel.setLayout(new BorderLayout());
//        completePanel.add(topPanel, BorderLayout.NORTH);
//        completePanel.add(m_webWindow/*browserPanel*/, BorderLayout.CENTER);
        setLayout(new BorderLayout(0, 2));
//        add(completePanel, BorderLayout.CENTER);
//System.out.println("initializeCommon() 3: "  + (System.currentTimeMillis()-start));

        homeURL = /*new URL(*/"http://e-slate.cti.gr"/*)*/;
        searchURL = /*new URL(*/"http://www.google.com"/*)*/;
//System.out.println("initializeCommon() 4: "  + (System.currentTimeMillis()-start));

        setPreferredSize(new Dimension(300, 300));
        imgDir = "..\\images";
        JScrollBar hScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        JScrollBar vScrollBar = new JScrollBar(JScrollBar.VERTICAL);
//System.out.println("initializeCommon() 5: "  + (System.currentTimeMillis()-start));

        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(webWindowTimer);
        m_webwindow = new WebWindow(false, hScrollBar, vScrollBar);
        pm.stop(webWindowTimer);
        pm.displayTime(webWindowTimer, "", "ms");
//System.out.println("initializeCommon() 6: "  + (System.currentTimeMillis()-start));
        m_htmlPane = (HTMLRenderer) m_webwindow.getHTMLPane();
//System.out.println("initializeCommon() 6.1: "  + (System.currentTimeMillis()-start));
//        Helper.addComponent(browserPanel, m_webwindow, 10, 1, 1, 1, 0, 0, new Insets(5, 5, 5, 5), 0, 0, 1.0D, 1.0D);
//System.out.println("initializeCommon() 7: "  + (System.currentTimeMillis()-start));

        navigationListener = new LinkListener() {
                    public boolean linkClicked(LinkEvent e) {
                        String result = parseLink(e.getLink());

                        loadPage(result, true);
                        return true;
                    }

                    public void mouseExitedLink(LinkEvent e) {}

                    public void mouseEnteredLink(LinkEvent e) {}

                    public void mousePressedOnLink(LinkEvent e) {}

                    public void mouseReleasedOnLink(LinkEvent e) {}
                };
//System.out.println("initializeCommon() 7.1: "  + (System.currentTimeMillis()-start));
        m_htmlPane.addLinkListener(navigationListener);
//System.out.println("initializeCommon() 8: "  + (System.currentTimeMillis()-start));
        createPopupMenu();
//System.out.println("initializeCommon() 9: "  + (System.currentTimeMillis()-start));
        add(topPanel, BorderLayout.NORTH);
        add(m_webwindow, BorderLayout.CENTER);
//System.out.println("initializeCommon() 10: "  + (System.currentTimeMillis()-start));
    }

    private void initialize() {
        setMenuBarUsed(true);
        //setToolBarUsed(true);
        createToolBar();
        setLocationPanelUsed(true);

        toolBarVisible = true;
        menuBarVisible = true;
        locationPanelVisible = true;
        m_statusPanel = new StatusPanel(imgDir);
        m_statusPanel.m_zoomSlider.addChangeListener(this);
        layoutBrowser();
    }

    public void setOpaque(boolean opaque){
        super.setOpaque(opaque);
        if(m_webwindow != null)
            m_webwindow.setTransparent(!opaque);
    }

    public void setMenuBarUsed(boolean b) {
        if (menuBarUsed == b) return;
        menuBarUsed = b;

        if (b) {
            if (menuBar != null)
                return;
            createMenus();
        } else {
            setMenuBarVisible(false);
            menuBar = null;
        }
    }

    public void unloadPage(){
        m_htmlPane.unLoadPage();
        m_htmlPane.repaint();
    }

    public boolean isMenuBarUsed() {
        return menuBarUsed;
    }

    public void setLocationPanelUsed(boolean b) {
        if (locationPanelUsed == b) return;
        locationPanelUsed = b;

        if (b) {
            if (m_location != null)
                return;
            createLocationPanel();
        } else {
            if (m_location == null)
                return;
            setLocationPanelVisible(false);
            m_location = null;
        }
    }

    public boolean isLocationPanelUsed() {
        return locationPanelUsed;
    }

    public void setHtmlPaneBorder(Border border){
        if (border == m_webwindow.getBorder()) return;
        m_webwindow.setBorder(border);
    }

    public Border getHtmlPaneBorder(){
        return m_webwindow.getBorder();
    }

    public void setDefaultState(ESlateToolBar toolBar){
        if (toolBar != m_toolbar)
            return;
        boolean b = isToolBarVisible();
        m_toolbar = null;
        createToolBar();
        setToolBarVisible(b);

    }
}
