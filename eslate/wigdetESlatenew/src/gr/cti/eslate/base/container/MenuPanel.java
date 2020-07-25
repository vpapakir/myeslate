package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.ObjectBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class MenuPanel extends JPanel {
    protected JMenuBar menuBar;
    protected JMenu microworldMenu, componentMenu, toolsMenu;
    protected HelpMenu helpMenu;
    protected ViewMenu microworldViewMenu;
    protected ComponentPaletteMenu componentNew;
    protected JMenu microworldLoad, microworldSaveAs, componentsMenu; //componentBarProperties
    protected JMenu printMenu, componentPrintMenu;
//    protected JMenuItem newDatabase, newChronometer, newClock, newDistance, newMasterClock;
//    protected JMenuItem newTime, newSteering, newVector;
    protected JMenuItem microworldLoadLocal, microworldLoadRemote, microworldSave;
    protected JMenuItem microworldSaveAsLocal, microworldSaveAsRemote;
    protected JMenuItem microworldPackUp, microworldPackDown, microworldPackLeft, microworldPackRight, microworldPackAll;
    protected JMenuItem  microworldProperties, microworldLayers, microworldCompLayers; //microworldProperties2, microworldInterface
    protected JMenuItem microworldNew, microworldRemoveAll, microworldClose, pinView, componentBar, microworldFTP, microworldExit; //, microworldRename;
    protected JMenuItem microworldNewView;
    protected JMenuItem componentRename, componentRemove, componentEditor, viewEditor, performanceMonitorEdit, performanceMonitorStatus;
    JMenuItem mwdHelpCreate, mwdHelpEdit, mwdHelpClear;
    JMenu mwdHelpMgmtMenu, performanceMonitorMenu;
    protected JMenuItem componentCut, componentCopy, componentPaste;
    protected JMenuItem componentSave, componentLoad;
    protected JMenuItem componentPrinterMI, componentPrintFileMI, componentPDFMI, componentRTFMI;
    protected JMenuItem componentSettings, componentCustomizer;
    protected JCheckBoxMenuItem compoResizable, frameTitleDisplayed;// composDraggable;
//    protected JCheckBoxMenuItem pinDisplayEnabled, helpEnabled, infoEnabled; //, componentFrozen; compoMinimizable, compoMaximizable, compoClosable, compoNameChangeableFromCompoBar
    protected JCheckBoxMenuItem compoActivatableByMousePress;
    protected JCheckBoxMenuItem /*toolsFreeze,*/ toolsJavaConsoleEnabled;
    protected JMenuItem toolsContainer, toolsConsoles, toolsGrid; //toolsLogoConsole
    protected JMenu toolsLAF;
    protected JMenu microworldNavigationMenu, microworldPackMenu;
    protected MyMenu microworldReopenMenu;
    protected JMenuItem microworldBack, microworldForward, microworldHistory;
    protected JMenuItem eSlateHelp, aboutESlate, microworldHelp;
    protected JMenuItem pageSetupMI, printerMI, printFileMI, pdfMI, rtfMI;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    /* The label at the end of the menuPanel which shows/hides the component bar.
     */
    JLabel compoMenuLabel;
    JLabel printStatusLabel;
    /* Indicates whether a print is in progress */
    private boolean printStatusOn = false, printingCancelled = false;
    JPanel indicationsPanel;
    boolean compoMenuLabelIsPressed = false, printStatusLabelIsPressed = false;
    ESlateComposer composer;
    ActionListener switchLAFlistener = null;

    public MenuPanel(/*ESlateContainer*/ESlateComposer cont) {
        super();
        this.composer = cont;
        menuBar = createMenuBar();
        menuBar.setBorder(null);
        menuBar.setBorderPainted(false);

        menuBar.setAlignmentY(CENTER_ALIGNMENT);

        // The Button (actually label) that shows/hides the component bar.
        compoMenuLabel = new JLabel(new ImageIcon(getClass().getResource("images/down.gif"))); //"B");
        compoMenuLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Dimension d = new Dimension(20, 19);
        compoMenuLabel.setMaximumSize(d);
        compoMenuLabel.setMinimumSize(d);
        compoMenuLabel.setPreferredSize(d);
        compoMenuLabel.setAlignmentY(CENTER_ALIGNMENT);
        compoMenuLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!compoMenuLabelIsPressed)
                    compoMenuLabel.setBorder(new javax.swing.border.EtchedBorder());
                else
                    compoMenuLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
            }
            public void mouseExited(MouseEvent e) {
                compoMenuLabel.setBorder(null);
            }
            public void mousePressed(MouseEvent e) {
                compoMenuLabelIsPressed = true;
                compoMenuLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
                composer.toggleComponentPanel();
            }
            public void mouseReleased(MouseEvent e) {
                Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getX(), e.getY(), MenuPanel.this);
                if (compoMenuLabel.getBounds().contains(p))
                    compoMenuLabel.setBorder(new javax.swing.border.EtchedBorder());
                else
                    compoMenuLabel.setBorder(null);
                compoMenuLabelIsPressed = false;
            }
        });

        indicationsPanel = new JPanel() {
            //Dimension size = new Dimension(30, 20);
            int width = 30;
            public Dimension getPreferredSize() {
                return new Dimension(width, getParent().getSize().height);
            }
            public Dimension getMinimumSize() {
                return new Dimension(width, getParent().getSize().height);
            }
            public Dimension getMaximumSize() {
                return new Dimension(width, getParent().getSize().height);
            }
        };
        indicationsPanel.setLayout(new java.awt.BorderLayout());
        indicationsPanel.setOpaque(false);
        setLayout(new BorderLayout());
        add(menuBar,BorderLayout.NORTH);
//        add(Box.createHorizontalStrut(5));
//        add(Box.createGlue());
//        add(indicationsPanel);
//        add(compoMenuLabel);
//        add(Box.createHorizontalStrut(5));

        switchLAFlistener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenuItem item = (JMenuItem) e.getSource();
                String lfName = item.getText();
                composer.setESlateLAF(lfName);
            }
        };
/*        menuPanel = new JPanel(true);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.add(menuBar);
        menuPanel.add(Box.createHorizontalStrut(5));
        menuPanel.add(Box.createGlue());
        menuPanel.add(compoMenuLabel);
        menuPanel.add(Box.createHorizontalStrut(5));
*/
    }

    JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar() {
            public void updateUI() {
                super.updateUI();
                if (MenuPanel.this != null) {
                    MenuPanel.this.setBackground(this.getBackground());
//                    MenuPanel.this.indicationsPanel.setBackground(this.getBackground());
                }
            }
        };

        // MICROWORLD MENU
        String menuString = composer.containerBundle.getString("MicroworldMenu");
        microworldMenu = (JMenu) menuBar.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldMenu.setFont(greekUIFont);

        // MICROWORLD-->NEW
        menuString = composer.containerBundle.getString("MicroworldNew");
        microworldNew = (JMenuItem) microworldMenu.add(new JMenuItem(composer.microworldNewAction));
        microworldNew.setAccelerator((KeyStroke) composer.microworldNewAction.getValue(AbstractAction.ACCELERATOR_KEY));

        microworldMenu.addSeparator();

        // MICROWORLD-->LOAD
        menuString = composer.containerBundle.getString("MicroworldLoad");
        microworldLoad = (JMenu) microworldMenu.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldLoad.setFont(greekUIFont);

        // MICROWORLD-->LOCAL MICROWORLD
        microworldLoadLocal = (JMenuItem) microworldLoad.add(new JMenuItem(composer.microworldLoadAction));
        microworldLoadLocal.setAccelerator((KeyStroke) composer.microworldLoadAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->LOAD REMOTE MICROWORLD
        menuString = composer.containerBundle.getString("MicroworldLoadRemote");
        microworldLoadRemote = (JMenuItem) microworldLoad.add(new JMenuItem(menuString));

        microworldLoadRemote.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
            	composer.openLoadRemoteMicroworldDialog();
            }
        });

        // MICROWORLD-->REOPEN
        menuString = composer.containerBundle.getString("MicroworldReopen");
	microworldReopenMenu = (MyMenu) microworldMenu.add(new MyMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldReopenMenu.setFont(greekUIFont);

        // MICROWORLD-->NAVIGATE
        menuString = composer.containerBundle.getString("MicroworldNavigate");
        microworldNavigationMenu = (JMenu) microworldMenu.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldNavigationMenu.setFont(greekUIFont);

        // MICROWORLD-->NAVIGATE-->BACK
	microworldBack = (JMenuItem) microworldNavigationMenu.add(new JMenuItem(composer.microworldBackAction));

        // MICROWORLD-->NAVIGATE-->FORWARD
        microworldForward = (JMenuItem) microworldNavigationMenu.add(new JMenuItem(composer.microworldForwardAction));

        // MICROWORLD-->NAVIGATE-->HISTORY
        menuString = composer.containerBundle.getString("MicroworldHistory");
        microworldHistory = (JMenuItem) microworldNavigationMenu.add(new JMenuItem(menuString));

        microworldHistory.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (composer.mwdHistory.getHistoryIndex() == -1)
                    return;
                composer.containerUtils.forceMenuClose(new JMenu[] {microworldMenu, microworldNavigationMenu});
//                composer.containerUtils.forceMenuClose();
                if (!composer.splitPane.isLeftPanelClosed()) {
                    Component comp = composer.splitPane.getContent();
                    if (comp.getClass().getName().equals("gr.cti.eslate.base.container.HistoryDialog"))
                        return;
                }
                HistoryDialog d = new HistoryDialog(composer);//ESlateContainer.this);
                composer.splitPane.setContent(d.getPanelTitle(), d);
//                mwdHistoryDialogIsDisplayed = true;
            }
        });

        // MICROWORLD-->SAVE
	microworldSave = (JMenuItem) microworldMenu.add(new JMenuItem(composer.microworldSaveAction));
        microworldSave.setAccelerator((KeyStroke) composer.microworldSaveAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->SAVE AS
        menuString = composer.containerBundle.getString("MicroworldSaveAs");
        microworldSaveAs = (JMenu) microworldMenu.add(new JMenu(menuString));

        // MICROWORLD-->SAVE AS LOCAL MICROWORLD
        microworldSaveAsLocal = (JMenuItem) microworldSaveAs.add(new JMenuItem(composer.microworldSaveAsAction));
        microworldSaveAsLocal.setAccelerator((KeyStroke) composer.microworldSaveAsAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->SAVE AS REMOTE MICROWORLD
        menuString = composer.containerBundle.getString("MicroworldSaveAsRemote");
	microworldSaveAsRemote = (JMenuItem) microworldSaveAs.add(new JMenuItem(menuString));

        microworldSaveAsRemote.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.containerUtils.forceMenuClose(new JMenu[] {microworldMenu, microworldSaveAs});
//                composer.containerUtils.forceMenuClose();
                WebFileDialog webFileDialog = new WebFileDialog(composer, false); //ESlateContainer.this, false); //createWebFileDialog();
                if (webFileDialog != null) {
//                    webFileDialog.showSaveDialog(composer.parentComponent);
//                    if (webFileDialog.getSelectedFile() != null) {
/////nikosM new
                    int whichButtonPressed=webFileDialog.showSaveDialog(composer.parentComponent);
                    if ((webFileDialog.getSelectedFile() != null)&&(whichButtonPressed!=webFileDialog.CANCEL_OPTION)) {
/////nikosM
                        if (webFileDialog.isRemoteFile()) {
                            String serverFileName;
                            try{
                                serverFileName = webFileDialog.getSelectedFile().getCanonicalPath();
                            }catch (IOException exc) {
                                serverFileName = webFileDialog.getSelectedFile().toString();
                            }
//// nikosM
                            String webFile = composer.webServerMicrosHandle.getWebDirNameFromRoot(serverFileName, webFileDialog.getWebSiteMirror());
                            if (!composer.webServerMicrosHandle.saveFileToServer(webFileDialog.getWebSite(), webFile))
//// nikosM end
                                ESlateOptionPane.showMessageDialog(composer.parentComponent, composer.containerBundle.getString("ContainerMsg11"), composer.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            else
                                composer.setMicroworldChanged(false);
                        }else{
                            if (composer.saveAs(webFileDialog.getSelectedFile().toString(), false)) {
                                composer.currentlyOpenMwdFileName = webFileDialog.getSelectedFile().toString();
                                if (composer.microworld != null && !composer.microworld.microworldNameUserDefined)
                                    composer.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(composer.currentlyOpenMwdFileName, true));
                                composer.openFileRemote = false;
//// nikosM
                                composer.webServerMicrosHandle.webSite = null;
//// nikosM end
                                composer.setMicroworldChanged(false);
                            }
                        }
                    }
                }
//                System.out.println("currentlyOpenMwdFileName: " + currentlyOpenMwdFileName +
//                                   ", openFileRemote: " + openFileRemote + ", webSite: " + webSite);
            }
        });

        // MICROWORLD-->CLOSE
        microworldClose = (JMenuItem) microworldMenu.add(new JMenuItem(composer.microworldCloseAction));
        microworldClose.setAccelerator((KeyStroke) composer.microworldCloseAction.getValue(AbstractAction.ACCELERATOR_KEY));

        microworldMenu.addSeparator();

        // MICROWORLD-->RENAME
/*        menuString = composer.containerBundle.getString("MicroworldRename");
	      microworldRename = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
//        if (composer.containerBundle.getClass().getName().equals("gr.cti.eslate.base.composer.ContainerBundle_el_GR"))
//            microworldRename.setFont(greekUIFont);

        microworldRename.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
//                composer.renameMicroworld();
            }
        });
*/
        // MICROWORLD-->PROPERTIES
        microworldProperties = (JMenuItem) microworldMenu.add(new JMenuItem(composer.microworldPropertiesAction));
        microworldProperties.setAccelerator((KeyStroke) composer.microworldPropertiesAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->REMOVE ALL
        menuString = composer.containerBundle.getString("MicroworldRemoveAll");
		microworldRemoveAll = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));

        microworldRemoveAll.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.promptToClearMicroworld();
            }
        });

        // MICROWORLD-->PACK
        menuString = composer.containerBundle.getString("MicroworldPack");
        microworldPackMenu = (JMenu) microworldMenu.add(new JMenu(menuString));

        // MICROWORLD-->PACK-->UP
        menuString = composer.containerBundle.getString("MicroworldPackUp");
	microworldPackUp = (JMenuItem) microworldPackMenu.add(new JMenuItem(menuString));
        microworldPackUp.addActionListener(composer.microworldPackAction);

        // MICROWORLD-->PACK-->DOWN
        menuString = composer.containerBundle.getString("MicroworldPackDown");
        microworldPackDown = (JMenuItem) microworldPackMenu.add(new JMenuItem(menuString));
        microworldPackDown.addActionListener(composer.microworldPackAction);

        // MICROWORLD-->PACK-->LEFT
        menuString = composer.containerBundle.getString("MicroworldPackLeft");
        microworldPackLeft = (JMenuItem) microworldPackMenu.add(new JMenuItem(menuString));
        microworldPackLeft.addActionListener(composer.microworldPackAction);

        // MICROWORLD-->PACK-->RIGHT
        menuString = composer.containerBundle.getString("MicroworldPackRight");
        microworldPackRight = (JMenuItem) microworldPackMenu.add(new JMenuItem(menuString));
        microworldPackRight.addActionListener(composer.microworldPackAction);

        microworldPackMenu.addSeparator();

        // MICROWORLD-->PACK-->ALL SIDES
        menuString = composer.containerBundle.getString("MicroworldPackAll");
        microworldPackAll = (JMenuItem) microworldPackMenu.add(new JMenuItem(menuString));
        microworldPackAll.addActionListener(composer.microworldPackAction);

        // MICROWORLD-->SETTINGS
/*        menuString = container.containerBundle.getString("MicroworldProperties");
        microworldProperties2 = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
        microworldProperties2.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M,
                                                             java.awt.Event.CTRL_MASK | java.awt.Event.SHIFT_MASK,
                                                             false));

        microworldProperties2.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                container.showMicroworldPropertiesDialog2();
            }
        });

        // MICROWORLD-->INTERFACE
        menuString = container.containerBundle.getString("MicroworldInterface");
        microworldInterface = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldInterface.setFont(greekUIFont);

        microworldInterface.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                container.adjustContainerInterface();
            }
        });
*/
        // MICROWORLD-->LAYERS
        menuString = composer.containerBundle.getString("MicroworldLayers");
        microworldLayers = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldLayers.setFont(greekUIFont);

        microworldLayers.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (composer.microworld != null && !composer.microworld.mwdLayerMgmtAllowed)
                    return;
                if (composer.layerDialog != null && composer.layerDialog.isVisible()) {
                    composer.layerDialog.refreshLayerComponents();
                    composer.layerDialog.toFront();
                }else
                    composer.layerDialog = new LayerDialog(composer); //ESlateContainer.this);
            }
        });

        // MICROWORLD-->COMPONENT LAYERS
        menuString = composer.containerBundle.getString("MicroworldComponentLayers");
        microworldCompLayers = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldCompLayers.setFont(greekUIFont);

        microworldCompLayers.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (composer.microworld != null && !composer.microworld.mwdLayerMgmtAllowed)
                    return;
                Frame topLevelFrame = null;
                if (composer.parentComponent != null && Frame.class.isAssignableFrom(composer.parentComponent.getClass()))
                    topLevelFrame = (Frame) composer.parentComponent;
                new ComponentLayerDialog(topLevelFrame, composer); //ESlateContainer.this);
            }
        });

        microworldMenu.addSeparator();

        // MICROWORLD-->NEW VIEW
        microworldNewView = (JMenuItem) microworldMenu.add(new JMenuItem(composer.microworldNewViewAction));
        microworldNewView.setAccelerator((KeyStroke) composer.microworldNewViewAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->VIEWS
        menuString = composer.containerBundle.getString("MicroworldViewMenu");
        microworldViewMenu = (ViewMenu) microworldMenu.add(new ViewMenu(composer, menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldViewMenu.setFont(greekUIFont);

        microworldMenu.addSeparator();

        // MICROWORLD-->PAGE SETUP
        menuString = composer.containerBundle.getString("MicroworldPageSetup");
        pageSetupMI = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            pageSetupMI.setFont(greekUIFont);

        pageSetupMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.containerUtils.forceMenuClose(new JMenu[] {microworldMenu});
//                container.containerUtils.forceMenuClose();
                composer.pageSetupAction();
            }
        });

        // MICROWORLD-->PRINT
        menuString = composer.containerBundle.getString("Print");
        printMenu = (JMenu) microworldMenu.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            printMenu.setFont(greekUIFont);

        // MICROWORLD-->PRINT-->PRINTER
//        menuString = container.containerBundle.getString("PrintPrinter");
	printerMI = (JMenuItem) printMenu.add(new JMenuItem(composer.microworldPrintAction));
        printerMI.setAccelerator((KeyStroke) composer.microworldPrintAction.getValue(AbstractAction.ACCELERATOR_KEY));


        // MICROWORLD-->PRINT-->FILE
        menuString = composer.containerBundle.getString("PrintFile");
        printFileMI = (JMenuItem) printMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            componentPDFMI.setFont(greekUIFont);

        printFileMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {componentMenu, componentPrintMenu};
                composer.containerUtils.forceMenuClose(menus);
                composer.printMicroworld(ESlateContainer.FILE);
            }
        });

        // MICROWORLD-->PRINT-->PDF
/*        menuString = container.containerBundle.getString("PrintPDF");
	      pdfMI = (JMenuItem) printMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            pdfMI.setFont(greekUIFont);

        pdfMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {microworldMenu, printMenu};
                container.containerUtils.forceMenuClose(menus);
                container.printMicroworld(ESlateContainer.PDF_FILE);
            }
        });

        // MICROWORLD-->PRINT-->RTF
        menuString = container.containerBundle.getString("PrintRTF");
	      rtfMI = (JMenuItem) printMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            rtfMI.setFont(greekUIFont);

        rtfMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {microworldMenu, printMenu};
                container.containerUtils.forceMenuClose(menus);
                container.printMicroworld(ESlateContainer.RTF_FILE);
            }
        });
*/
        microworldMenu.addSeparator();

        // MICROWORLD-->FTP
/*
        menuString = composer.containerBundle.getString("MicroworldFTP");
	    microworldFTP = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));

        microworldFTP.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.containerUtils.forceMenuClose(new JMenu[] {microworldMenu});
                Frame topLevelFrame = null;
                if (composer.parentComponent != null && Frame.class.isAssignableFrom(composer.parentComponent.getClass()))
                    topLevelFrame = (Frame) composer.parentComponent;
                FTPDialog ftpDialog = new FTPDialog(topLevelFrame, composer); //ESlateContainer.this);
                ftpDialog.showDialog(composer); //ESlateContainer.this);
            }
        });
*/

        // MICROWORLD-->EXIT
        menuString = composer.containerBundle.getString("MicroworldExit");
	      microworldExit = (JMenuItem) microworldMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            microworldExit.setFont(greekUIFont);

        microworldExit.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                int k = composer.promptToSaveCurrentMicroworld();
                if (k == composer.CANCEL)
                    return;
                if (k == composer.SAVE)
                    composer.saveMicroworld(true);

                composer.closeMicroworld(false);
                composer.exit(0);
//                System.exit(0);
            }
        });


        // COMPONENT MENU
        menuString = composer.containerBundle.getString("ComponentMenu");
        componentMenu = (JMenu) menuBar.add(new JMenu(menuString));

        // COMPONENT-->NEW
        menuString = composer.containerBundle.getString("ComponentNew");
        componentNew = (ComponentPaletteMenu) componentMenu.add(new ComponentPaletteMenu(composer, menuString));

        componentMenu.addSeparator();

        // COMPONENT-->CUT
		componentCut = (JMenuItem) componentMenu.add(new JMenuItem(composer.componentCutAction));
        componentCut.setAccelerator((KeyStroke) composer.componentCutAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->COPY
		componentCopy = (JMenuItem) componentMenu.add(new JMenuItem(composer.componentCopyAction));
        componentCopy.setAccelerator((KeyStroke) composer.componentCopyAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->PASTE
		componentPaste = (JMenuItem) componentMenu.add(new JMenuItem(composer.componentPasteAction));
        componentPaste.setAccelerator((KeyStroke) composer.componentPasteAction.getValue(AbstractAction.ACCELERATOR_KEY));

        componentMenu.addSeparator();

        // COMPONENT-->RENAME
		componentRename = (JMenuItem) componentMenu.add(new JMenuItem(composer.componentRenameAction));

        // COMPONENT-->SAVE
        menuString = composer.containerBundle.getString("ComponentSave");
        componentSave = (JMenuItem) componentMenu.add(new JMenuItem(menuString));
        componentSave.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent component = composer.getActiveComponent();
                String fileName = composer.getSystemFile(true, composer.containerBundle.getString("SaveComponent") + " \"" + component.handle.getComponentName() + '\"', null, new String[] {"comp"});
                composer.saveComponent(component, fileName);
            }
        });

        // COMPONENT-->LOAD
        menuString = composer.containerBundle.getString("ComponentLoad");
        componentLoad = (JMenuItem) componentMenu.add(new JMenuItem(menuString));
        componentLoad.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                String fileName = composer.getSystemFile(false, composer.containerBundle.getString("LoadComponent"), null, new String[] {"comp"});
                composer.loadComponent(fileName);
            }
        });

        // COMPONENT-->REMOVE
        menuString = composer.containerBundle.getString("ComponentRemove");
        componentRemove = (JMenuItem) componentMenu.add(new JMenuItem(composer.componentRemoveAction));

        // COMPONENT-->PRINT
        menuString = composer.containerBundle.getString("Print");
        componentPrintMenu = (JMenu) componentMenu.add(new JMenu(menuString));

        // COMPONENT-->PRINT-->PRINTER
        menuString = composer.containerBundle.getString("PrintPrinter");
        componentPrinterMI = (JMenuItem) componentPrintMenu.add(new JMenuItem(menuString));

        componentPrinterMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {componentMenu, componentPrintMenu};
                composer.containerUtils.forceMenuClose(menus);
                if (composer.getActiveComponentHandle() == null)
                    return;
                composer.printComponent(composer.getActiveComponentHandle().getComponentName(), ESlateContainer.PRINTER);
//                composer.styleReportPrint(composer.mwdComponents.activeComponent.frame, ESlateContainer.PRINTER);
            }
        });

        // COMPONENT-->PRINT-->FILE
        menuString = composer.containerBundle.getString("PrintFile");
        componentPrintFileMI = (JMenuItem) componentPrintMenu.add(new JMenuItem(menuString));

        componentPrintFileMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {componentMenu, componentPrintMenu};
                composer.containerUtils.forceMenuClose(menus);
                composer.printComponent(composer.getActiveComponentHandle().getComponentName(), ESlateContainer.FILE);
            }
        });

        // COMPONENT-->PRINT-->PDF
/*        menuString = container.containerBundle.getString("PrintPDF");
        componentPDFMI = (JMenuItem) componentPrintMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            componentPDFMI.setFont(greekUIFont);

        componentPDFMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {componentMenu, componentPrintMenu};
                container.containerUtils.forceMenuClose(menus);
                container.styleReportPrint(container.mwdComponents.activeComponent.frame, ESlateContainer.PDF_FILE);
            }
        });

        // COMPONENT-->PRINT-->RTF
        menuString = container.containerBundle.getString("PrintRTF");
        componentRTFMI = (JMenuItem) componentPrintMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            componentRTFMI.setFont(greekUIFont);

        componentRTFMI.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JMenu[] menus = new JMenu[] {componentMenu, componentPrintMenu};
                container.containerUtils.forceMenuClose(menus);
                container.styleReportPrint(container.scrollPane.getViewport(), ESlateContainer.RTF_FILE);
            }
        });
*/
        componentMenu.addSeparator();

        // COMPONENT-->RESIZABLE
//        menuString = container.containerBundle.getString("ComponentResizable");
        compoResizable = (JCheckBoxMenuItem) componentMenu.add(new JCheckBoxMenuItem(composer.componentResizableAction));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            compoResizable.setFont(greekUIFont);
/*        compoResizable.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setResizable(compoResizable.isSelected());
            }
        });
*/
        // COMPONENT-->FREEZE
/*        menuString = containerBundle.getString("ComponentFrozen");
	      componentFrozen = (JCheckBoxMenuItem) componentMenu.add(new JCheckBoxMenuItem(menuString));
        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            componentFrozen.setFont(greekUIFont);
        componentFrozen.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (activeFrame == null)
                    return;
                activeFrame.setFrozen(componentFrozen.isSelected());
            }
        });
*/
        // COMPONENT-->ACTIVATED BY MOUSE PRESS
//        menuString = container.containerBundle.getString("ComponentActivatableByMousePress");
        compoActivatableByMousePress = (JCheckBoxMenuItem) componentMenu.add(new JCheckBoxMenuItem(composer.componentActivateOnClickAction));
/*        compoActivatableByMousePress.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setComponentActivatedOnMouseClick(compoActivatableByMousePress.isSelected());
            }
        });
*/
        // COMPONENT-->DISPLAY FRAME TITLE
        menuString = composer.containerBundle.getString("FrameTitleDisplayed");
        frameTitleDisplayed = (JCheckBoxMenuItem) componentMenu.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            frameTitleDisplayed.setFont(greekUIFont);
        frameTitleDisplayed.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = composer.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                /* This checks for old-type frozen components and unfreezes them */
//if                if (activeComponent.frame.isFrozen())
//if                    activeComponent.frame.setFrozen(false);
                activeComponent.frame.setTitlePanelVisible(frameTitleDisplayed.isSelected());
            }
        });

        // COMPONENT-->BAR PROPERTIES
/*        menuString = container.containerBundle.getString("ComponentBarProperties");
        componentBarProperties = (JMenu) componentMenu.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            componentBarProperties.setFont(greekUIFont);

        // COMPONENT-->MINIMIZABLE
        menuString = container.containerBundle.getString("ComponentMinimizable");
        compoMinimizable = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            compoMinimizable.setFont(greekUIFont);
        compoMinimizable.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setIconifiable(compoMinimizable.isSelected());
            }
        });

        // COMPONENT-->MAXIMIZABLE
        menuString = container.containerBundle.getString("ComponentMaximizable");
	      compoMaximizable = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            compoMaximizable.setFont(greekUIFont);
        compoMaximizable.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setMaximizable(compoMaximizable.isSelected());
            }
        });

        // COMPONENT-->CLOSABLE
        menuString = container.containerBundle.getString("ComponentClosable");
	      compoClosable = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            compoClosable.setFont(greekUIFont);
        compoClosable.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setClosable(compoClosable.isSelected());
            }
        });

        // COMPONENT-->PIN DISPLAY ENABLED
        menuString = container.containerBundle.getString("PinDisplayEnabled");
	      pinDisplayEnabled = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            pinDisplayEnabled.setFont(greekUIFont);
        pinDisplayEnabled.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setPlugButtonVisible(pinDisplayEnabled.isSelected());
            }
        });

        // COMPONENT-->HELP DISPLAY ENABLED
        menuString = container.containerBundle.getString("HelpEnabled");
	      helpEnabled = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            helpEnabled.setFont(greekUIFont);
        helpEnabled.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setHelpButtonVisible(helpEnabled.isSelected());
            }
        });
        // COMPONENT-->INFO DISPLAY ENABLED
        menuString = container.containerBundle.getString("InfoVisible");
	      infoEnabled = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            infoEnabled.setFont(greekUIFont);
        infoEnabled.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setInfoButtonVisible(infoEnabled.isSelected());
            }
        });

        // COMPONENT-->CHANGE NAME FROM MENU BAR
        menuString = container.containerBundle.getString("ComponentNameChangeableFromMenuBar");
	      compoNameChangeableFromCompoBar = (JCheckBoxMenuItem) componentBarProperties.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            compoNameChangeableFromCompoBar.setFont(greekUIFont);
        compoNameChangeableFromCompoBar.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                ESlateComponent activeComponent = container.mwdComponents.activeComponent;
                if (activeComponent == null)
                    return;
                activeComponent.frame.setComponentNameChangeableFromMenuBar(compoNameChangeableFromCompoBar.isSelected());
            }
        });
*/
        // COMPONENT-->SETTINGS
		componentSettings = (JMenuItem) componentMenu.add(new JMenuItem(composer.componentSettingsAction));
		componentSettings.setAccelerator((KeyStroke) composer.componentSettingsAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->CUSTOMIZER
        menuString = composer.containerBundle.getString("ComponentCustomizer");
		componentCustomizer = (JMenuItem) componentMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            componentCustomizer.setFont(greekUIFont);
        componentCustomizer.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.initCustomizer();
            }
        });

        componentMenu.addSeparator();

        // COMPONENT-->COMPONENTS
        menuString = composer.containerBundle.getString("Components");
        componentsMenu = (JMenu) componentMenu.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            componentsMenu.setFont(greekUIFont);
        componentsMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                ESlateComponent activeComponent = composer.mwdComponents.activeComponent;
                if (activeComponent == null) {
//0                if (container.activeFrame == null) {
                    javax.swing.MenuElement[] elements = componentsMenu.getPopupMenu().getSubElements();
                    for (int i=0; i<elements.length; i++)
                        ((JCheckBoxMenuItem) elements[i]).setSelected(false);
                }else{
                    javax.swing.MenuElement[] elements = componentsMenu.getPopupMenu().getSubElements();
                    String activeComponentName = activeComponent.handle.getComponentName();
                    for (int i=0; i<elements.length; i++) {
                        if (!((JCheckBoxMenuItem) elements[i]).getText().equals(activeComponentName))
                            ((JCheckBoxMenuItem) elements[i]).setSelected(false);
                        else
                            ((JCheckBoxMenuItem) elements[i]).setSelected(true);
                    }
                }
            }
        });

	      // TOOLS MENU
        menuString = composer.containerBundle.getString("PreferencesMenu");
	toolsMenu = (JMenu) menuBar.add(new JMenu(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            toolsMenu.setFont(greekUIFont);

	      // TOOLS-->FREEZE
/*	      menuString = containerBundle.getString("PreferencesFreeze");
	      toolsFreeze = (JCheckBoxMenuItem) toolsMenu.add(new JCheckBoxMenuItem(menuString));
        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            toolsFreeze.setFont(greekUIFont);

        toolsFreeze.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (toolsFreeze.isSelected())
                    freezeComponents();
                else
                    frameComponents();
            }
        });
*/
		// TOOLS-->COMPONENT BAR	
		componentBar = (JMenuItem) toolsMenu.add(new JCheckBoxMenuItem(composer.showComponentBarAction));
		componentBar.setAccelerator((KeyStroke) composer.showComponentBarAction.getValue(AbstractAction.ACCELERATOR_KEY));
		toolsMenu.addSeparator();
	
        // TOOLS-->PIN VIEW
//        menuString = container.containerBundle.getString("PinView");
	pinView = (JMenuItem) toolsMenu.add(new JMenuItem(composer.plugEditorAction));
        pinView.setAccelerator((KeyStroke) composer.plugEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // TOOLS-->COMPONENT EDITOR
//        menuString = container.containerBundle.getString("ComponentEditor");
	componentEditor = (JMenuItem) toolsMenu.add(new JMenuItem(composer.componentEditorAction));
        componentEditor.setAccelerator((KeyStroke) composer.componentEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // TOOLS-->VIEW EDITOR
//        menuString = container.containerBundle.getString("ViewEditor");
	viewEditor = (JMenuItem) toolsMenu.add(new JMenuItem(composer.viewEditorAction));
        viewEditor.setAccelerator((KeyStroke) composer.viewEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        toolsMenu.addSeparator();

        // TOOLS-->PERFORMANCE MONITOR
        menuString = composer.containerBundle.getString("PerformanceMonitor");
	performanceMonitorMenu = (JMenu) toolsMenu.add(new JMenu(menuString));

        menuString = composer.containerBundle.getString("PerformanceMonitorStart");
	performanceMonitorStatus = (JMenuItem) performanceMonitorMenu.add(new JMenuItem(menuString));
        performanceMonitorStatus.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                PerformanceManager pm = PerformanceManager.getPerformanceManager();
                PerformanceManager.getPerformanceManager().setEnabled(!pm.isEnabled());
            }
        });

//        menuString = container.containerBundle.getString("PerformanceMonitorEdit");
	performanceMonitorEdit = (JMenuItem) performanceMonitorMenu.add(new JMenuItem(composer.pmEditorAction));
        performanceMonitorEdit.setAccelerator((KeyStroke) composer.pmEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // TOOLS-->MICROWORLD HELP
        menuString = composer.containerBundle.getString("MicroworldHelpMgmt");
	mwdHelpMgmtMenu = (JMenu) toolsMenu.add(new JMenu(menuString));

        // TOOLS-->MICROWORLD HELP-->CREATE
//        menuString = container.containerBundle.getString("MicroworldHelpCreate");
	mwdHelpCreate = (JMenuItem) mwdHelpMgmtMenu.add(new JMenuItem(composer.microworldHelpEditorAction));
        mwdHelpCreate.setAccelerator((KeyStroke) composer.microworldHelpEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // TOOLS-->MICROWORLD HELP-->EDIT
        menuString = composer.containerBundle.getString("MicroworldHelpEdit");
	mwdHelpEdit = (JMenuItem) mwdHelpMgmtMenu.add(new JMenuItem(menuString));
        mwdHelpEdit.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.editMicroworldHelp();
            }
        });

        // TOOLS-->MICROWORLD HELP-->CLEAR
        menuString = composer.containerBundle.getString("MicroworldHelpClear");
	mwdHelpClear = (JMenuItem) mwdHelpMgmtMenu.add(new JMenuItem(menuString));
        mwdHelpClear.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.clearMicroworldHelp();
            }
        });

        toolsMenu.addSeparator();

        // TOOLS-->OPTIONS
	toolsContainer = (JMenuItem) toolsMenu.add(new JMenuItem(composer.containerSettingsAction));
        toolsContainer.setAccelerator((KeyStroke) composer.containerSettingsAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // TOOLS-->GRID
        toolsGrid = (JMenuItem) toolsMenu.add(new JMenuItem(composer.gridEditorAction));
        toolsGrid.setAccelerator((KeyStroke) composer.gridEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // TOOLS-->LOOK&FEEL
        menuString = composer.containerBundle.getString("PreferencesL&F");
        toolsLAF = (JMenu) toolsMenu.add(new JMenu(menuString));
//        toolsLF.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L,
//                                                             java.awt.Event.ALT_MASK,
//                                                             false));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            toolsLAF.setFont(greekUIFont);
        toolsLAF.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                String currentLAFClassName = UIManager.getLookAndFeel().getClass().getName();
                String currentLAFName = composer.installedLAFs.getName(currentLAFClassName);
                Component[] items = toolsLAF.getMenuComponents();
                for (int i=0; i<items.length; i++)
                    ((JCheckBoxMenuItem) items[i]).setSelected(false);
                if (currentLAFName != null) {
                    for (int i=0; i<items.length; i++) {
                        JCheckBoxMenuItem item = (JCheckBoxMenuItem) toolsLAF.getItem(i);
                        if (item.getText().equals(currentLAFName)) {
                            item.setSelected(true);
                            break;
                        }
                    }
                }
            }
        });

        // TOOLS-->ARRANGE WINDOWS
/*        menuString = containerBundle.getString("ArrangeWindows");
	      arrangeWindows = (JMenuItem) toolsMenu.add(new JMenuItem(menuString));
        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            arrangeWindows.setFont(greekUIFont);
        arrangeWindows.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                int startX = 0, endX = 0;
                Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                endX = screenSize.width;
                if (propertiesDialog != null) {
                    propertiesDialog.setLocation(0, 0);
                    startX = propertiesDialog.getSize().width;
                }
                if (eventDialog != null) {
                    endX = endX - eventDialog.getSize().width;
                    eventDialog.setLocation(endX, 0);
                }
                Container topLevelAncestor = ESlateContainer.this.getTopLevelAncestor();
                int height = topLevelAncestor.getBounds().height;
                topLevelAncestor.setBounds(startX, 0, endX-startX, height-10);
                topLevelAncestor.doLayout();                                   //Java 1.2
                ESlateContainer.this.invalidate();
                ESlateContainer.this.validateTree();
                ESlateContainer.this.doLayout();
                ESlateContainer.this.revalidate();                             //Java 1.2
                topLevelAncestor.setBounds(startX, 0, endX-startX, height);
            }
        });
*/
        toolsMenu.addSeparator();

        // TOOLS-->JAVA CONSOLE ENABLED
        menuString = composer.containerBundle.getString("PreferencesConsolesEnabled");
        toolsJavaConsoleEnabled = (JCheckBoxMenuItem) toolsMenu.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            toolsJavaConsoleEnabled.setFont(greekUIFont);

        toolsJavaConsoleEnabled.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (toolsJavaConsoleEnabled.isSelected()) {
                    composer.redirectOutputToJavaConsole();
                    composer.javaConsoleEnabled = true;
                }else{
                    composer.useStandardJavaOutput();
                    composer.javaConsoleEnabled = false;
                    if (composer.consoles != null
                        && composer.consoles.consoleDialog != null) {
                      composer.consoles.consoleDialog.setVisible(false);
                    }
                }
                composer.writeContainerProperties();
            }
        });

        // TOOLS-->JAVA CONSOLE
//        menuString = container.containerBundle.getString("PreferencesConsoles");
	toolsConsoles = (JMenuItem) toolsMenu.add(new JMenuItem(composer.openConsolesAction));
        toolsConsoles.setAccelerator((KeyStroke) composer.openConsolesAction.getValue(AbstractAction.ACCELERATOR_KEY));
/*
        // TOOLS-->LOGO CONSOLE
        menuString = container.containerBundle.getString("PreferencesLogoConsole");
        toolsLogoConsole = (JMenuItem) toolsMenu.add(new JMenuItem(menuString));
        toolsLogoConsole.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L,
                                                             java.awt.Event.ALT_MASK,
                                                             false));
        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            toolsLogoConsole.setFont(greekUIFont);

        toolsLogoConsole.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (container.loc != null)
                    container.consoles.showConsole(container); //ESlateContainer.this);
            }
        });
*/
        // HELP MENU
        menuString = composer.containerBundle.getString("HelpMenu");
        helpMenu = (HelpMenu) menuBar.add(new HelpMenu(menuString));

        // HELP-->E-SLATE HELP
        eSlateHelp = (JMenuItem) helpMenu.add(new JMenuItem(composer.containerHelpAction));
        eSlateHelp.setAccelerator((KeyStroke) composer.containerHelpAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // HELP-->ABOUT
        menuString = composer.containerBundle.getString("AboutESlate");
        aboutESlate = (JMenuItem) helpMenu.add(new JMenuItem(menuString));
        aboutESlate.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                JDialog aboutDialog = new AboutDialog(composer.parentFrame, composer); //ESlateContainer.this);
            }
        });

        helpMenu.addSeparator();

        // HELP-->MICROWORLD
        menuString = composer.containerBundle.getString("MicroworldHelp");
        microworldHelp = (JMenuItem) helpMenu.add(new JMenuItem(menuString));
        microworldHelp.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                composer.showMicroworldHelp();
            }
        });

        microworldMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                if (microworldReopenMenu.getItemCount() == 0)
                    microworldReopenMenu.setEnabled(false);
                else
                    microworldReopenMenu.setEnabled(true);

                if (composer.microworld != null && composer.microworld.viewActivationAllowed
                && composer.mwdViews.viewList != null && composer.mwdViews.viewList.length > 0)
                    microworldViewMenu.setEnabled(true);
                else
                    microworldViewMenu.setEnabled(false);

                microworldBack.setEnabled(composer.microworldBackAction.isEnabled());
/*                if (container.mwdHistory.canGoBack((container.microworld != null)))
                    microworldBack.setEnabled(true);
                else
                    microworldBack.setEnabled(false);
*/
                microworldForward.setEnabled(composer.microworldForwardAction.isEnabled());
/*                if (container.mwdHistory.canGoForward())
                    microworldForward.setEnabled(true);
                else
                    microworldForward.setEnabled(false);
*/
                if (composer.mwdHistory.getHistoryIndex() == -1) // || mwdHistoryDialogIsDisplayed)
                    microworldHistory.setEnabled(false);
                else
                    microworldHistory.setEnabled(true);

                microworldClose.setEnabled(composer.microworldCloseAction.isEnabled());
                microworldPackMenu.setEnabled(composer.microworldPackAction.isEnabled());
                microworldSave.setEnabled(composer.microworldSaveAction.isEnabled());
                microworldSaveAs.setEnabled(composer.microworldSaveAsAction.isEnabled());
                microworldProperties.setEnabled(composer.microworldPropertiesAction.isEnabled());
                microworldNewView.setEnabled(composer.microworldNewViewAction.isEnabled());

                if (composer.microworld == null) {
//                    microworldSave.setEnabled(false);
//                    microworldSaveAs.setEnabled(false);
                    microworldRemoveAll.setEnabled(false);
                    pageSetupMI.setEnabled(false);
                    printMenu.setEnabled(false);
//                    microworldRename.setEnabled(false);
//                    microworldPackMenu.setEnabled(false);
//                    microworldProperties2.setEnabled(false);
//                    microworldInterface.setEnabled(false);
                    microworldLayers.setEnabled(false);
                    microworldCompLayers.setEnabled(false);
//                    microworldNewView.setEnabled(false);
                }else{
/*                    if (container.microworld.mwdStorageAllowed) {
                        microworldSave.setEnabled(true);
                        microworldSaveAs.setEnabled(true);
                    }else{
                        microworldSave.setEnabled(false);
                        microworldSaveAs.setEnabled(false);
                    }
*/
                    if (composer.microworld != null && composer.microworld.componentRemovalAllowed)
                        microworldRemoveAll.setEnabled(true);
                    else
                        microworldRemoveAll.setEnabled(false);

//                    if (container.currentView != null && container.currentView.mwdTitleEnabled)
//                        microworldRename.setEnabled(true);
//                    else
//                        microworldRename.setEnabled(false);

//                    container.microworldCloseAction.setEnabled(true);
//                    microworldPackMenu.setEnabled(true);
//                    microworldNewView.setEnabled(container.microworld.viewCreationAllowed);
                    if (composer.currentView != null && composer.currentView.mwdPageSetupAllowed)
                        pageSetupMI.setEnabled(true);
                    else
                        pageSetupMI.setEnabled(false);
//                    microworldProperties2.setEnabled(true);

/*                    if (container.currentView != null && container.currentView.mwdBgrdChangeAllowed)
                        microworldInterface.setEnabled(true);
                    else
                        microworldInterface.setEnabled(false);
*/
                    microworldLayers.setEnabled(composer.microworld.mwdLayerMgmtAllowed);
                    microworldCompLayers.setEnabled(composer.microworld.mwdLayerMgmtAllowed);
                    if (composer.currentView != null && composer.currentView.mwdPrintAllowed)
                        printMenu.setEnabled(true);
                    else
                        printMenu.setEnabled(false);

                }
            }
        });
        componentMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                componentCut.setEnabled(composer.componentCutAction.isEnabled());
                componentCopy.setEnabled(composer.componentCopyAction.isEnabled());
                componentPaste.setEnabled(composer.componentPasteAction.isEnabled());
                componentRename.setEnabled(composer.componentRenameAction.isEnabled());
                componentRemove.setEnabled(composer.componentRemoveAction.isEnabled());
                compoResizable.setEnabled(composer.componentResizableAction.isEnabled());
                compoActivatableByMousePress.setEnabled(composer.componentActivateOnClickAction.isEnabled());
				componentSettings.setEnabled(composer.componentSettingsAction.isEnabled());
                if (composer.microworld == null) {
                    componentNew.setEnabled(false);
//                    componentCut.setEnabled(false);
                    componentCopy.setEnabled(false);
//                    componentPaste.setEnabled(false);
//                    compoMaximizable.setEnabled(false);
//                    compoMinimizable.setEnabled(false);
//                    compoClosable.setEnabled(false);
//                    pinDisplayEnabled.setEnabled(false);
//                    helpEnabled.setEnabled(false);
//                    infoEnabled.setEnabled(false);
                    frameTitleDisplayed.setEnabled(false);
                    frameTitleDisplayed.setSelected(false);
//                    compoResizable.setEnabled(false);
                    compoResizable.setSelected(false);
//                    componentBarProperties.setEnabled(false);
//                    compoNameChangeableFromCompoBar.setEnabled(false);
//                    compoActivatableByMousePress.setEnabled(false);
                    compoActivatableByMousePress.setSelected(false);
//                    componentRename.setEnabled(false);
                    componentSave.setEnabled(false);
                    componentLoad.setEnabled(false);
//                    componentRemove.setEnabled(false);
                    componentPrintMenu.setEnabled(false);
//                    componentSettings.setEnabled(false);
                    componentsMenu.setEnabled(false);
//                    componentFrozen.setEnabled(false);
//                    componentFrozen.setSelected(false);
                    componentCustomizer.setEnabled(false);
                }else{
                    if (composer.microworld != null &&
                    composer.microworld.componentInstantiationAllowed &&
                    !composer.lc.isModalFrameVisible()) {
                        componentNew.setEnabled(true);
                        componentLoad.setEnabled(true);
//                        componentPaste.setEnabled(true);
                    }else{
                        componentNew.setEnabled(false);
                        componentLoad.setEnabled(false);
//                        componentPaste.setEnabled(false);
                    }
                    ESlateComponent activeComponent = composer.mwdComponents.activeComponent;
                    if (activeComponent == null) { // || activeComponent.frame == null) {
//0                    if (container.activeFrame == null) {
//                        componentRename.setEnabled(false);
                        componentSave.setEnabled(false);
                        frameTitleDisplayed.setEnabled(false);
                        frameTitleDisplayed.setSelected(false);
//                        componentCut.setEnabled(false);
//                        componentCopy.setEnabled(false);
//                        compoMaximizable.setEnabled(false);
//                        compoMinimizable.setEnabled(false);
//                        compoClosable.setEnabled(false);
//                        pinDisplayEnabled.setEnabled(false);
//                        helpEnabled.setEnabled(false);
//                        infoEnabled.setEnabled(false);
//                        compoResizable.setEnabled(false);
                        compoResizable.setSelected(false);
//                        componentBarProperties.setEnabled(false);
//                        compoNameChangeableFromCompoBar.setEnabled(false);
//                        compoActivatableByMousePress.setEnabled(false);
                        compoActivatableByMousePress.setSelected(false);
                        componentRemove.setEnabled(false);
                        componentPrintMenu.setEnabled(false);
//                        componentSettings.setEnabled(false);
                        componentsMenu.setEnabled(false);
//                        componentFrozen.setEnabled(false);
//                        componentFrozen.setSelected(false);
                        componentCustomizer.setEnabled(false);
                    }else{
                        componentSave.setEnabled(true);
//                        componentCopy.setEnabled(true);
                        if (activeComponent.frame == null) {
//                            componentSettings.setEnabled(false);
                            componentPrintMenu.setEnabled(false);
                            frameTitleDisplayed.setEnabled(false);
                            frameTitleDisplayed.setSelected(false);
//                            componentBarProperties.setEnabled(false);
//                            compoActivatableByMousePress.setEnabled(false);
                            compoActivatableByMousePress.setSelected(false);
//                            compoResizable.setEnabled(false);
                            compoResizable.setSelected(false);
                        }else{
//                            componentSettings.setEnabled(true);
                            if (composer.currentView != null && composer.currentView.componentPrintAllowed)
                                componentPrintMenu.setEnabled(true);
                            else
                                componentPrintMenu.setEnabled(false);
//if                            if (activeComponent.frame.isFrozen()) {
//if                                frameTitleDisplayed.setEnabled(false);
//if                                frameTitleDisplayed.setSelected(false);
//if                            }else{
                                frameTitleDisplayed.setEnabled(true);
                                frameTitleDisplayed.setSelected(activeComponent.frame.isTitlePanelVisible());
//if                            }

//if                            if (activeComponent.frame.isFrozen())
//if                                componentBarProperties.setEnabled(false);
//if                            else
//if                                componentBarProperties.setEnabled(true);

//if                            if (activeComponent.frame.isFrozen()) {
//if                                compoActivatableByMousePress.setEnabled(false);
//if                                compoActivatableByMousePress.setSelected(false);
//if                            }else{
/*                                if (container.currentView != null && container.currentView.componentActivationMethodChangeAllowed)
                                    compoActivatableByMousePress.setEnabled(true);
                                else
                                    compoActivatableByMousePress.setEnabled(false);
*/
                                compoActivatableByMousePress.setSelected(activeComponent.frame.isComponentActivatedOnMouseClick());
//if                            }

                            if (activeComponent.frame.isMaximum()) { // || activeComponent.frame.isFrozen())
//                                compoResizable.setEnabled(false);
                                compoResizable.setSelected(false);
                            }else{
//                                compoResizable.setEnabled(true);
                                compoResizable.setSelected(activeComponent.frame.isResizable());
                            }

                            if (activeComponent.frame.isTitlePanelVisible()) {
//                                compoMaximizable.setEnabled(true);
//                                compoMinimizable.setEnabled(true);
//                                compoClosable.setEnabled(true);
//                                pinDisplayEnabled.setEnabled(true);
//                                helpEnabled.setEnabled(true);
//                                infoEnabled.setEnabled(true);
//                                compoNameChangeableFromCompoBar.setEnabled(true);
//                                compoClosable.setSelected(activeComponent.frame.isClosable());
//                                compoMaximizable.setSelected(activeComponent.frame.isMaximizable());
//                                compoMinimizable.setSelected(activeComponent.frame.isIconifiable());
//                                pinDisplayEnabled.setSelected(activeComponent.frame.isPlugButtonVisible());
//                                helpEnabled.setSelected(activeComponent.frame.isHelpButtonVisible());
//                                infoEnabled.setSelected(activeComponent.frame.isInfoButtonVisible());
//                                compoNameChangeableFromCompoBar.setSelected(activeComponent.frame.isComponentNameChangeableFromMenuBar());
                            }else{
/*                                compoMaximizable.setEnabled(false);
                                compoMinimizable.setEnabled(false);
                                compoClosable.setEnabled(false);
                                pinDisplayEnabled.setEnabled(false);
                                helpEnabled.setEnabled(false);
                                infoEnabled.setEnabled(false);
                                compoClosable.setSelected(false);
                                compoMaximizable.setSelected(false);
                                compoMinimizable.setSelected(false);
                                pinDisplayEnabled.setSelected(false);
                                helpEnabled.setSelected(false);
                                infoEnabled.setSelected(false);
*/
//                                compoNameChangeableFromCompoBar.setEnabled(false);
                            }
                        }

/*                        if (componentFrozenStateChangeAllowed)
                            componentFrozen.setEnabled(true);
                        else
                            componentFrozen.setEnabled(false);
*/
/*                        if (container.microworld.componentNameChangeAllowed)
                            componentRename.setEnabled(true);
                        else
                            componentRename.setEnabled(false);
                        if (container.microworld.componentRemovalAllowed) {
                            componentRemove.setEnabled(true);
//                            componentCut.setEnabled(true);
                        }else{
                            componentRemove.setEnabled(false);
//                            componentCut.setEnabled(false);
                        }
*/
//0                        if (container.activeFrame.isFrozen()) {

                        if (composer.mwdComponents != null && composer.mwdComponents.size() != 0)
                            componentsMenu.setEnabled(true);
                        else
                            componentsMenu.setEnabled(false);

                        if (!activeComponent.checkedForCustomizer) {
//                            try{
                                java.beans.BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(activeComponent.object.getClass());
                                if (compoInfo != null) {
//                                activeFrame.checkedForCustomizer = true;
                                    activeComponent.customizerClass = compoInfo.getBeanDescriptor().getCustomizerClass();
//                                }catch (Exception exc) {
//                                    container.activeFrame.checkedForCustomizer = true;
//                                }
                                }else
                                    activeComponent.customizerClass = null;
                        }
                        componentCustomizer.setEnabled((activeComponent.customizerClass != null)? true:false);

                    }
                }
            }
        });

        toolsMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                pinView.setEnabled(composer.plugEditorAction.isEnabled());
                componentEditor.setEnabled(composer.componentEditorAction.isEnabled());
                viewEditor.setEnabled(composer.viewEditorAction.isEnabled());
                performanceMonitorEdit.setEnabled(composer.pmEditorAction.isEnabled());
                mwdHelpCreate.setEnabled(composer.microworldHelpEditorAction.isEnabled());
                toolsContainer.setEnabled(composer.containerSettingsAction.isEnabled());
                toolsGrid.setEnabled(composer.gridEditorAction.isEnabled());
                toolsConsoles.setEnabled(composer.openConsolesAction.isEnabled());
/*                if (container.currentView != null && container.currentView.plugConnectionChangeAllowed)
                    pinView.setEnabled(true);
                else
                    pinView.setEnabled(false);
*/
                if (composer.installedLAFs.getAvailableCount() == 0) {
                    toolsLAF.setEnabled(false);
                }else{
                    if (composer.microworld == null) {
                        toolsLAF.setEnabled(true);
                    }else{
                        toolsLAF.setEnabled(composer.microworld.mwdLAFChangeAllowed);
                    }
                }

//                toolsLogoConsole.setEnabled((container.loc != null));

/*                if (container.javaConsoleEnabled) {
                    if (container.microworld != null)
                        toolsConsoles.setEnabled(container.microworld.consolesAllowed);
                    else
                        toolsConsoles.setEnabled(true);
                }else
                    toolsConsoles.setEnabled(false);
*/
                toolsJavaConsoleEnabled.setSelected(composer.javaConsoleEnabled);

                if (composer.microworld == null) {
//                    toolsContainer.setEnabled(true);
//                    viewEditor.setEnabled(false);
//                    componentEditor.setEnabled(true);
                    mwdHelpMgmtMenu.setEnabled(false);
//                    toolsGrid.setEnabled(true);
                }else{
//                    toolsContainer.setEnabled(container.microworld.isEslateOptionMgmtAllowed());
//                    viewEditor.setEnabled(container.microworld.viewMgmtAllowed);
//                    componentEditor.setEnabled(container.microworld.componentPropertyMgmtAllowed ||
//                                               container.microworld.componentEventMgmtAllowed ||
//                                               container.microworld.componentSoundMgmtAllowed);
//                    toolsGrid.setEnabled(container.microworld.gridMgmtAllowed);
                    mwdHelpMgmtMenu.setEnabled(true);
//                    mwdHelpCreate.setEnabled(true);
                    mwdHelpEdit.setEnabled(composer.microworld.eslateMwd.hasHelp());
                    mwdHelpClear.setEnabled(composer.microworld.eslateMwd.hasHelp());
                }
                boolean pmEnabled = PerformanceManager.getPerformanceManager().isEnabled();
                performanceMonitorStatus.setText((pmEnabled)? composer.containerBundle.getString("PerformanceMonitorStop") : composer.containerBundle.getString("PerformanceMonitorStart"));
//                performanceMonitorEdit.setEnabled(pmEnabled);
            }
        });

        helpMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                if (composer.microworld == null)
                    microworldHelp.setEnabled(false);
                else
                    microworldHelp.setEnabled(composer.microworld.eslateMwd.hasHelp());
            }
        });

        return menuBar;
    }

    public void setPrintStatusOn(boolean on) {
        if (printStatusOn == on) return;
        if (on) {
            if (printStatusLabel == null) {
                printStatusLabel = new JLabel(new ImageIcon(this.getClass().getResource("images/print.gif")));
//                printStatusLabel.setText(" ");
                printStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                printStatusLabel.setPreferredSize(new Dimension(22, 16));
                printStatusLabel.setIconTextGap(2);
                printStatusLabel.setBorder(new javax.swing.border.EmptyBorder(2,2,2,2));
                printStatusLabel.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        composer.setCursor(java.awt.Cursor.getDefaultCursor(), false);
                        if (!printStatusLabelIsPressed)
                            printStatusLabel.setBorder(new javax.swing.border.EtchedBorder());
                        else
                            printStatusLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
                    }
                    public void mouseExited(MouseEvent e) {
                        printStatusLabel.setBorder(new javax.swing.border.EmptyBorder(2,2,2,2));
                        composer.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR), false);
                    }
                    public void mousePressed(MouseEvent e) {
                        printStatusLabelIsPressed = true;
                        printStatusLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
                        printStatusLabel.setIcon(new ImageIcon(this.getClass().getResource("images/stopprint.gif")));
                        indicationsPanel.paintImmediately(indicationsPanel.getVisibleRect());
                        printingCancelled = true;
//                        container.toggleComponentPanel();
                    }
                    public void mouseReleased(MouseEvent e) {
                        Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getX(), e.getY(), MenuPanel.this);
                        if (printStatusLabel.getBounds().contains(p))
                            printStatusLabel.setBorder(new javax.swing.border.EtchedBorder());
                        else
                            printStatusLabel.setBorder(null);
                        printStatusLabelIsPressed = false;
                    }
                });
            }
            indicationsPanel.add(printStatusLabel, java.awt.BorderLayout.CENTER);
            printingCancelled = false;
        }else{
            if (printStatusLabel != null) {
                indicationsPanel.remove(printStatusLabel);
            }
            composer.setCursor(java.awt.Cursor.getDefaultCursor(), false);
        }
        indicationsPanel.invalidate();
        indicationsPanel.doLayout();
        indicationsPanel.revalidate();
        indicationsPanel.paintImmediately(indicationsPanel.getVisibleRect());
        printStatusOn = on;
    }

    public boolean isPrintStatusOn() {
        return printStatusOn;
    }

    public void setPagePrinted(Integer pageNum) {
        if (!printStatusOn) return;
        if (pageNum == null)
            printStatusLabel.setText(null);
        else
            printStatusLabel.setText(pageNum.toString());
        indicationsPanel.paintImmediately(indicationsPanel.getVisibleRect());
    }

    public boolean wasPrintingCancelled() {
        return printingCancelled;
    }

    /* Updates the JMenuItems which are in the toolsLF menu to reflect the status
     * of the supplied InstalledLookAndFeelStructure.
     */
    public void updateLookFeelMenu(InstalledLookAndFeelStructure struct) {
        StringBaseArray lfNames = struct.getAvailableNames();
        BoolBaseArray nameExists = new BoolBaseArray(lfNames.size());
        for (int i=0; i<lfNames.size(); i++)
            nameExists.add(false);

        int itemCount = toolsLAF.getItemCount();
        for (int i=0; i<itemCount; i++) {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) toolsLAF.getItem(i);
            int index = lfNames.indexOf(item.getText());
            if (index == -1) {
                toolsLAF.remove(item);
                itemCount--;
                i--;
            }else
                nameExists.set(index, true);
        }

        for (int i=0; i<nameExists.size(); i++) {
            if (!nameExists.get(i)) {
                JCheckBoxMenuItem item = new JCheckBoxMenuItem(lfNames.get(i));
                item.addActionListener(switchLAFlistener);
                toolsLAF.add(item, i);
            }
        }
    }

}

class MyMenu extends javax.swing.JMenu {
    public MyMenu(String txt) {
        super(txt);
    }

    protected Point getPopupMenuOrigin() {
        return super.getPopupMenuOrigin();
    }
}


/* This JMenu subclass implements the E-Slate's Help menu. A subclass is needed because this
 * menu has a variable number of menu items, depending on the number of components in the
 * microworld. An entry for each component is included in the menu. This item provides
 * access to the component's help. Aw components come and go in E-Slate, the contents of the
 * HelpMenu keep changing.
 */
class HelpMenu extends JMenu {
    StringBaseArray componentHelpItems = new StringBaseArray();
    ObjectBaseArray handles = new ObjectBaseArray();

    public HelpMenu(String txt) {
        super(txt);
    }

    public void addComponentHelpItem(String itemName, ESlateHandle handle) {
        if (itemName == null) return;

        // Don't allow dublicate item addition
        if (componentHelpItems.contains(itemName))
            return;

        JMenuItem item = new JMenuItem(itemName);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itName = ((JMenuItem) e.getSource()).getText();
                int index = componentHelpItems.indexOf(itName);
                if (index != -1)
                    ((ESlateHandle) handles.get(index)).showHelpWindow();
                else
                    System.out.println("Inconsistency error in HelpMenu addComponentHelpItem()");
            }
        });

        // If this is the first item added, then insert a Separator first.
        if (componentHelpItems.size() == 0)
            addSeparator();
        add(item);
        componentHelpItems.add(itemName);
        handles.add(handle);
//        System.out.println("Added to ObjectBaseArray handle: " + handle + ", size: " + handles.size());
//        System.out.println("Internal length: " + ((Object[]) handles.toArray()).length);
    }

    public void removeComponentHelpItem(String name) {
        JMenuItem item = getComponentHelpItem(name);
        if (item != null) {
            remove(item);
            int index = componentHelpItems.indexOf(name);
//            componentHelpItems.set(index, null);
            ESlateHandle handle = (ESlateHandle) handles.get(index);
            componentHelpItems.remove(index);
            handles.remove(index);
//            System.out.println("Removed from ObjectBaseArray handle: " + handle + ", size: " + handles.size());
//            System.out.println("Internal length: " + ((Object[]) handles.toArray()).length);
            if (handles.size() == 0)
                handles.clear();
            // If no component exists in the microworld then remove the separator
            if (componentHelpItems.size() == 0)
                remove(4); //2
        }
    }

    private JMenuItem getComponentHelpItem(String itemName) {
        // Check if there exists an item with this name
        if (!componentHelpItems.contains(itemName))
            return null;
        JMenuItem item = null;
        for (int i=5/*3*/; i<getItemCount(); i++) {
            if (((JMenuItem) getItem(i)).getText().equals(itemName)) {
                item = getItem(i);
                break;
            }
        }
        if (item == null)
            System.out.println("Inconsistency in getComponentHelpItem() in HelpMenu");
        return item;
    }

    public void renameComponentHelpItem(String oldName, String newName) {
        JMenuItem item = getComponentHelpItem(oldName);
        if (item != null) {
            item.setText(newName);
            componentHelpItems.replace(oldName, newName);
        }
    }
}
