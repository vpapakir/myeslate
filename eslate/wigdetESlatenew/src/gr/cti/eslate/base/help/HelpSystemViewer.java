package gr.cti.eslate.base.help;

import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.browser.*;

//import inetsoft.report.*;
//import inetsoft.report.j2d.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.help.*;
import javax.help.plaf.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The class HelpSystemViewer is the one showing the help info.
 *
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class HelpSystemViewer extends JFrame
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private ResourceBundle errorInfo =
    ResourceBundle.getBundle(
      "gr.cti.eslate.base.help.ErrorMessageBundle",
      ESlateMicroworld.getCurrentLocale()
    );
  private ResourceBundle info =
    ResourceBundle.getBundle(
      "gr.cti.eslate.base.help.MessageBundle",
      ESlateMicroworld.getCurrentLocale()
    );
  private HelpSet emptyHelpSet = HelpSetLoader.getMasterHelpSet();
  private HelpSet hs;
  private DefaultHelpModel model;
  private boolean moreThanOneHelpSets = false;
  private DefaultHelpModel helpModel;
  private JHelpTOCNavigator toc;
  private JHelpIndexNavigator index;
  private JHelpSearchNavigator search;
  //private String newdb;
  //private String extension;
  private String helpSetName;
  private Container frame;
  private JPanel panel;
  private JToolBar buttonPanel;
  private JSplitPane splitPane;
  private JButton rightButton;
  private JButton printButton;
  private FindButton findButton;
  private JTabbedPane tabbedPane;
  private boolean initted = false;

  //protected Font plainFont = new Font("Helvetica", Font.PLAIN, 12);
  protected String page;
  protected NavigatorHandler navigator;
  protected UpdateHistory history;
  protected HelpSet badHelpSet = new HelpSet();
  protected ESlateBrowser browser;

  protected static int num;
  protected static Stack<String> forwardHistory;
  protected static Stack<String> backHistory;
  protected static boolean not;
  protected static Window infoWindow = null;
  protected static URL startingPage;
  protected static JButton leftButton;

  Font tempFont;

  /**
   * The constructor of the HelpSystemViewer class
   * @param     loadedHelpSet   The helpset to show.
   * @param     width   The width of the HelpViewer's window.
   * @param     height  The height of the HelpViewer's window.
   * @exception RunTimeException        Thrown if the content of the help
   *                    files is not composed properly.
   */
  public HelpSystemViewer(HelpSet loadedHelpSet, int width, int height)
  {
    super();
    /*
    if (ESlateMicroworld.getCurrentLocale().getLanguage().equals("el") ||
        ESlateMicroworld.getCurrentLocale().getLanguage().equals("el_GR")) {
      UIManager.put("Button.font", plainFont);
      UIManager.put("Label.font", plainFont);
      UIManager.put("TextField.font", plainFont);
      UIManager.put("ToolTip.font", plainFont);
    }
    */
    setIconImage(ESlate.getIconImage());

    hs = returnMasterHelpSet(loadedHelpSet);
    helpSetName = getHelpSetName(hs);
    /*extension = */getExtension(helpSetName);
    browser = ESlateBrowser.createBrowser();

    tempFont = browser.getProportionalFont();
    forwardHistory = new Stack<String>();
    backHistory = new Stack<String>();
    load(hs);
    setAppearance();
/*
    loadedHelpSet.addHelpSetListener(new HelpSetListener() {
      public void helpSetAdded(HelpSetEvent e)
      {
        reload(loadedHelpSet,e.getAction());
      }
      public void helpSetRemoved(HelpSetEvent e)
      {
        reload(loadedHelpSet,e.getAction());
      }
    });
*/
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      {
        setVisible(false);
      }
    });

    if (moreThanOneHelpSets) {
      setTitle(info.getString("helpTitle"));
    } else {
      setTitle(hs.toString());
    }
    backHistory.push(startingPage.toString());
    startShowing(width, height);
  }

  /**
   * The constructor of the HelpSystemViewer class
   * @param     loadedHelpSet   The helpset to show.
   * @param     locationX       The x_axis location of the window.
   * @param     locationY       The y_axis location of the window.
   * @param     width           The width of the HelpViewer's window.
   * @param     height          The height of the HelpViewer's window.
   * @exception RunTimeException        Thrown if the content of the help
   *                            files is not composed properly.
   */
  public HelpSystemViewer(HelpSet loadedHelpSet, int locationX,
                          int locationY, int width, int height)
  {
    this(loadedHelpSet, width, height);
    this.setLocation(locationX, locationY);
  }

  /*
   * When a hyperilnk event has occured to the browser we have update the
   * navigator's view in order to show the proper item.
   */
  private void updateNavigators(String nextTarget)
  {
    URL urlnext;
    toc.removeHelpModelListener(history);
    try {
      urlnext = new URL(nextTarget);
      model.setCurrentURL(urlnext);
    }
    catch (Exception el) {
      el.printStackTrace();
    }
    toc.addHelpModelListener(history);
  }

  private void loadNavigators(HelpSet hs)
  {
    try {
      helpModel = new DefaultHelpModel(hs);
      toc = new JHelpTOCNavigator(hs.getNavigatorView("TOC"), helpModel)
      {
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;
        
        public void updateUI()
        {
          super.updateUI();
          HelpNavigatorUI ui = getUI();
          if (ui instanceof ESlateTOCNavigatorUI) {
            ((ESlateTOCNavigatorUI)ui).updateColors();
          }
        }
        public void setUI(HelpNavigatorUI ui)
        {
          if (!(getUI() instanceof ESlateTOCNavigatorUI)) {
            super.setUI(ui);
          }
        }
      };
      toc.setUI(new ESlateTOCNavigatorUI(toc));
      index = new JHelpIndexNavigator(hs.getNavigatorView("Index"), helpModel)
      {
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;
        
        public void updateUI()
        {
          super.updateUI();
          HelpNavigatorUI ui = getUI();
          if (ui instanceof ESlateIndexNavigatorUI) {
            ((ESlateIndexNavigatorUI)ui).updateColors();
          }
        }
        public void setUI(HelpNavigatorUI ui)
        {
          if (!(getUI() instanceof ESlateIndexNavigatorUI)) {
            super.setUI(ui);
          }
        }
      };
      index.setUI(new ESlateIndexNavigatorUI(index));
      search =
        new JHelpSearchNavigator(hs.getNavigatorView("Search"), helpModel)
        {
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;
        
          public void updateUI()
          {
            super.updateUI();
            HelpNavigatorUI ui = getUI();
            if (ui instanceof ESlateSearchNavigatorUI) {
              ((ESlateSearchNavigatorUI)ui).updateColors();
            }
          }
          public void setUI(HelpNavigatorUI ui)
          {
            if (!(getUI() instanceof ESlateSearchNavigatorUI)) {
              super.setUI(ui);
            }
          }
        };
      //if (System.getProperty("java.version").startsWith("1.1.") &&
          //System.getProperty("java.vendor").startsWith("Sun")) {
      search.setUI(new ESlateSearchNavigatorUI(search));
      //}
      if (ESlateMicroworld.getCurrentLocale().getLanguage().equals("el") ||
          ESlateMicroworld.getCurrentLocale().getLanguage().equals("el_GR")) {
        fixFindLabel(index);
        fixFindLabel(search);
      }
      navigator = new NavigatorHandler(browser);
      history = new UpdateHistory(browser);

      index.addHelpModelListener(navigator);
      toc.addHelpModelListener(navigator);
      toc.addHelpModelListener(history);
      search.addHelpModelListener(navigator);
    }catch (Throwable e) {
      hideInfoWindow();
      badHelpSet = hs;
      throw new RuntimeException(
        e.getMessage()+"\n"+errorInfo.getString("attentionFile") +
        "\n" + errorInfo.getString("comp") + "\n"+getHelpSetName(hs) +
        errorInfo.getString("rightInfo")
      );
    }

    model = (DefaultHelpModel)toc.getModel();
    /*TOCView tocView = (TOCView)*/toc.getNavigatorView();

    // Throw something in order to show impossible.
    getStartingPage(hs);        // tocView,hs);
/*
    TOCView tocView = (TOCView)toc.getNavigatorView();
    Enumeration enum = tocView.getDataAsTree().postorderEnumeration();
    try {
      String firstPageID=enum.nextElement().toString();
      String mapid=firstPageID.substring(0,firstPageID.lastIndexOf(","));
      Map.ID map = Map.ID.create(mapid.substring(mapid.lastIndexOf(":")+2), hs);
      Map tryMap = hs.getCombinedMap();
      startingPage = tryMap.getURLFromID(map);
    } catch (Exception e) {
      HelpSetLoader.thread.stopExec();
      badHelpSet = hs;
      throw new RuntimeException(
        errorInfo.getString("toc") + "\n" + errorInfo.getString("comp") +
        "\n"+ getComponentsName(getHelpSetName(badHelpSet))
      );
    }
*/

    browser.addLinkFollowedListener(new LinkFollowedListener () {
      public void linkFollowed(LinkFollowedEvent ee)
      {
        String next = ee.getLink();
        try {
          backHistory.push(browser.getBackHistory().lastElement().toString());
        } catch (NoSuchElementException nsee) {
        }
        if (!backHistory.empty()) {
          leftButton.setEnabled(true);
        }
        updateNavigators(next);
        num=0;
      }
    });
  }

  /*
   * Set up the appeareance of the help system
   * and assign the proper listeners for it's
   * component buttons,etc...
   */
  private void setAppearance()
  {
    Insets insets = new Insets(2,2,2,2);

    URL leftIconURL = this.getClass().getResource("images/prev.gif");
    ImageIcon leftIcon = new ImageIcon(leftIconURL);
    leftButton = new JButton(leftIcon);
    leftButton.setMargin(insets);
    leftButton.setEnabled(false);
    leftButton.setToolTipText(info.getString("Back"));
    leftButton.setMinimumSize(new Dimension(26, 26));
    leftButton.setMaximumSize(new Dimension(26, 26));
    leftButton.setPreferredSize(new Dimension(26, 26));
    leftButton.setBorderPainted(false);

    URL rightIconURL = this.getClass().getResource("images/next.gif");
    ImageIcon rightIcon = new ImageIcon(rightIconURL);
    rightButton = new JButton(rightIcon);
    rightButton.setMargin(insets);
    rightButton.setToolTipText(info.getString("Next"));
    rightButton.setMinimumSize(new Dimension(26, 26));
    rightButton.setMaximumSize(new Dimension(26, 26));
    rightButton.setPreferredSize(new Dimension(26, 26));
    rightButton.setEnabled(false);
    rightButton.setBorderPainted(false);

    leftButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        try {
          if (backHistory.peek().toString().equals(browser.getCurrentLocation())) {
            backHistory.pop();
          }
          String now = backHistory.pop().toString();
          forwardHistory.push(browser.getCurrentLocation());
          try {
            browser.setCurrentLocation(now);
          } catch (Exception ex) {
          }
          updateNavigators(browser.getCurrentLocation());
          if (!forwardHistory.empty()) {
            rightButton.setEnabled(true);
          }
        } catch (Exception we) {
          leftButton.setEnabled(false);
          return;
        }
        if (backHistory.empty()) {
          leftButton.setEnabled(false);
          return;
        }
      }
    });

    rightButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        try {
          backHistory.push(browser.getCurrentLocation());
          if (!backHistory.empty()) {
            leftButton.setEnabled(true);
          }
          String now = forwardHistory.pop();
          try {
            browser.setCurrentLocation(now);
          } catch (Exception ex) {
          }
          updateNavigators(browser.getCurrentLocation());
          if (forwardHistory.peek().equals(browser.getCurrentLocation())) {
            forwardHistory.pop();
          }
        } catch (Exception we) {
          rightButton.setEnabled(false);
          return;
        }
        if (forwardHistory.empty()) {
          rightButton.setEnabled(false);
          return;
        }
      }
    });

    findButton = new FindButton(browser);
    findButton.setMargin(insets);
    findButton.setToolTipText(info.getString("dialogTitle"));
    findButton.setMinimumSize(new Dimension(26, 26));
    findButton.setMaximumSize(new Dimension(26, 26));
    findButton.setPreferredSize(new Dimension(26, 26));
    findButton.setBorderPainted(false);
    findButton.setEnabled(browser.supportsSearch());

    ImageIcon printIcon =
      new ImageIcon(this.getClass().getResource("images/print.gif"));
    printButton = new JButton(printIcon);
    printButton.setMargin(insets);
    printButton.setToolTipText(info.getString("print"));
    printButton.setMinimumSize(new Dimension(26, 26));
    printButton.setMaximumSize(new Dimension(26, 26));
    printButton.setPreferredSize(new Dimension(26, 26));
    printButton.setBorderPainted(false);
/*
    if (!isNullLocaleExtensionPage(getHelpSetName(hs))) {
      printButton.setEnabled(false);
    }
*/
    printButton.setEnabled(browser.supportsPrint());

    printButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
/*
        Component compo =
          browser.getParent().getParent().getParent().getParent().getParent();
        Frame fr = (Frame) compo;
        //PrinterJob printJob = PrinterJob.getPrinterJob();
        PrintJob printJob =
          Toolkit.getDefaultToolkit().getPrintJob(
            fr,info.getString("print"), null
          );
        browser.printDoc(printJob,true);
*/
        Component compo = browser;
        while (compo != null && ! (compo instanceof Frame)) {
          compo = compo.getParent();
        }
        browser.printDoc((Frame)compo, info.getString("print"));
      }
    });

    MouseListener BorderHighlightListener = new MouseAdapter() {
      public void mouseEntered(MouseEvent e)
      {
        ((AbstractButton)e.getSource()).setBorderPainted(true);
      }
      public void mouseExited(MouseEvent e)
      {
        ((AbstractButton)e.getSource()).setBorderPainted(false);
      }
    };

    leftButton.addMouseListener(BorderHighlightListener);
    rightButton.addMouseListener(BorderHighlightListener);
    printButton.addMouseListener(BorderHighlightListener);
    findButton.addMouseListener(BorderHighlightListener);

    buttonPanel = new JToolBar();
    buttonPanel.setFloatable(false);
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createHorizontalStrut(2));
    buttonPanel.add(leftButton);
    buttonPanel.add(Box.createHorizontalStrut(1));
    buttonPanel.add(rightButton);
    buttonPanel.add(Box.createHorizontalStrut(2));
    buttonPanel.add(new JToolBar.Separator(new Dimension(2, 25)));
    buttonPanel.add(Box.createHorizontalStrut(2));
    buttonPanel.add(findButton);
    buttonPanel.add(Box.createHorizontalStrut(1));
    buttonPanel.add(printButton);
    buttonPanel.add(Box.createHorizontalGlue());
/*
    class MyPropertyChangeListener
      implements java.beans.PropertyChangeListener
    {
      public void propertyChange(java.beans.PropertyChangeEvent e)
      {
        //if (isNullLocaleExtensionPage(browser.getDocumentBaseString())) {
        //  printButton.setEnabled(false);
        //}else{
        //  printButton.setEnabled(true);
        //}
        printButton.setEnabled(browser.supportsPrint());
      }
    };

    buttonPanel.addPropertyChangeListener(new MyPropertyChangeListener());
*/
    panel = new JPanel(true);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(Box.createVerticalGlue());
    panel.add(buttonPanel);
    panel.add(Box.createVerticalGlue());

    tabbedPane = new JTabbedPane();
    URL tocIconURL = this.getClass().getResource("images/toc.gif");
    ImageIcon tocIcon = new ImageIcon(tocIconURL);
    URL indexIconURL = this.getClass().getResource("images/index.gif");
    ImageIcon indexIcon = new ImageIcon(indexIconURL);
    URL searchIconURL = this.getClass().getResource("images/search.gif");
    ImageIcon searchIcon = new ImageIcon(searchIconURL);
    tabbedPane.addTab(null, tocIcon, toc, info.getString("TOC"));
    tabbedPane.addTab(null, indexIcon, index, info.getString("Index"));
    if (!System.getProperty("java.vendor").startsWith("Microsoft")) {
      tabbedPane.addTab(null, searchIcon, search, info.getString("Search"));
    }
    // Insert into the browser a listener that looks at the locale of the
    // appearing page and enables or disables printButton depending on each
    // circumstance.
    browser.registerKeyboardAction(
      new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
          if (findButton.findDialog == null) {
            return;
          }
          String searchFor = findButton.findDialog.textField.getText();
          if (searchFor == null || searchFor.length() == 0) {
            return;
          }
          if (num!=0) {
            num=browser.search(num,searchFor,false);
          }else{
            num=browser.search(0,searchFor,false);
          }
        }
      },
      KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0),
      JComponent.WHEN_IN_FOCUSED_WINDOW
    );

//  browser.setProportionalFont(new Font("Helvetica", Font.PLAIN, 12));
//  browser.setFixedFont(new Font("Courier", Font.PLAIN, 12));
    browser.setVisible(true);
    browser.setLocale(ESlateMicroworld.getCurrentLocale());
/*
    // Don't do this here, because if the browser is Internet Explorer,
    // we get an ActiveXMissingProxyException.
    try {
      String start = startingPage.toString();
      try {
        browser.setCurrentLocation(start);
      } catch (Exception e) {
      }
    } catch (NullPointerException e){
      e.printStackTrace();
    }
*/
    splitPane =
      new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, browser);
    splitPane.setContinuousLayout(true);
    splitPane.setDividerLocation(210);

    frame = getContentPane();
    frame.setLayout(new BorderLayout());
    frame.add("Center", splitPane);
    frame.add("North",panel);
    frame.setVisible(true);
  }

  /**
   * Enable or disable the "table" of contents tab.
   * @param     enable  If true, enable the tab. If false, disable it.
   */
  public void enableTOC(boolean enable)
  {
    tabbedPane.setEnabledAt(0, enable);
  }

  /**
   * Enable or disable the "index" tab.
   * @param     enable  If true, enable the tab. If false, disable it.
   */
  public void enableIndex(boolean enable)
  {
    tabbedPane.setEnabledAt(1, enable);
  }

  /**
   * Enable or disable the "search" tab.
   * @param     enable  If true, enable the tab. If false, disable it.
   */
  public void enableSearch(boolean enable)
  {
    if (tabbedPane.getTabCount() >= 3) {
      tabbedPane.setEnabledAt(2, enable);
    }
  }

//  /**
//   * Called when greek locale is detected in order to make a label be in greek
//   * and not in english which is it's default.
//   */
//  private boolean isNullLocaleExtensionPage(String temporal)
//  {
//    //System.out.println("Name : "+temporal);
//    int index = temporal.lastIndexOf("_");
//    //System.out.println("Index : "+index);
//    if (index>0) {
//      return false;
//    }else{
//      return true;
//    }
//  }

  private void fixFindLabel(Container c)
  {
    if (c.toString().lastIndexOf("JLabel")>0) {
      ((JLabel)c).setText("Έυρεση:");
    }else{
      fixFindLabel((Container)c.getComponents()[0]);
    }
  }

  private String getComponentsName(String hsName)
  {
    int temp=hsName.lastIndexOf("Help");
    return  hsName.substring(0, temp);
  }

  private void startShowing(int frameWidth, int frameHeight)
  {
    hideInfoWindow();
    hideInfoWindow();
    setSize(frameWidth,frameHeight);
    //setVisible(true);
  }

  private String getHelpSetName(HelpSet helpSet)
  {
    String tempString = helpSet.getHelpSetURL().toString();
    int lastIndex = tempString.lastIndexOf("+");
    return tempString.substring(lastIndex+1, tempString.length());
  }

  private void mergeHelpSetsViews(HelpSet helpSet)
  {
    HelpSet tempHelpSet;
    int thesi = 0;
    Enumeration totalHelpSets = helpSet.getHelpSets();

    if (!moreThanOneHelpSets) {
      return;
    }
    tempHelpSet = (HelpSet)totalHelpSets.nextElement();

    do {
      try {
        toc.merge(tempHelpSet.getNavigatorView("TOC"));
        index.merge(tempHelpSet.getNavigatorView("Index"));
        search.merge(tempHelpSet.getNavigatorView("Search"));
      } catch(Error el) {
        if (el.getMessage().toString().indexOf("Mal")>0) {
          thesi = el.getMessage().toString().indexOf("Mal");
          String message=el.getMessage().toString().substring(thesi);
          thesi = message.indexOf(":");
          message = message.substring(thesi);
          hideInfoWindow();
          badHelpSet=tempHelpSet;
          throw new RuntimeException(
            errorInfo.getString("fileNotFound") + "\n" + el.getMessage() +
            "\n"+ errorInfo.getString("attentionFile")
          );
        }else{
          String comp = getComponentsName(getHelpSetName(tempHelpSet));
          hideInfoWindow();
          badHelpSet = tempHelpSet;
          throw new RuntimeException(
            errorInfo.getString("folderNotFound") + "\n" +
            errorInfo.getString("search") + "\n" +
            comp.substring(comp.lastIndexOf("/")+1) + "\n" +
            errorInfo.getString("attentionFolder")
          );
        }
      }
      try {
        tempHelpSet = (HelpSet)totalHelpSets.nextElement();
      } catch(Exception e) {
        tempHelpSet = null;
      }
    }
    while (tempHelpSet != null);
  }

  private void checkForMoreThanOneHelpSets(HelpSet hs)
  {
    Enumeration e = hs.getHelpSets();
    moreThanOneHelpSets = e.hasMoreElements();
  }

  private HelpSet returnMasterHelpSet(HelpSet master)
  {
    HelpSet tempHelpSet = emptyHelpSet;
    String title = emptyHelpSet.getTitle();
    Enumeration addedOnes = master.getHelpSets();
    try {
      while (tempHelpSet.getTitle().equals(title)) {
        tempHelpSet = (HelpSet)addedOnes.nextElement();
      }
      while(addedOnes.hasMoreElements()) {
        HelpSet temp = (HelpSet)addedOnes.nextElement();
        if (!temp.getTitle().equals(title)) {
          tempHelpSet.add(temp);
        }
      }
    } catch (Exception e) {
      if (master.getTitle().equals(title)) {
        hideInfoWindow();
        throw new RuntimeException(errorInfo.getString("impossible"));
      }else{
        return master;
      }
    }
    return tempHelpSet;
  }

  private String getExtension(String name)
  {
    int arxi = name.indexOf("_");
    if (arxi > 0) {
      int telos = name.lastIndexOf(".");
      return name.substring(arxi, telos);
    }else{
      return null;
    }
  }

  private void load(HelpSet newHelpSet)
  {
    checkForMoreThanOneHelpSets(newHelpSet);
    try {
      loadNavigators(newHelpSet);
      mergeHelpSetsViews(newHelpSet);
    } catch (Throwable t) {
      System.out.println(t.getMessage());
      if (badHelpSet.equals(newHelpSet)) {
        if (moreThanOneHelpSets) {
          HelpSet temp = returnMasterHelpSet(newHelpSet);
          badHelpSet = null;
          load(temp);
        }else{
          hideInfoWindow();
          throw new RuntimeException(t.getMessage());
        }
      }else{
        newHelpSet.remove(badHelpSet);
        badHelpSet = null;
        load(newHelpSet);
      }
    }
  }

/*
  private void reload(HelpSet newHelpSet,int action)
  {
    System.out.println(" ");
    System.out.println("Something Added or removed");
    System.out.println("Correct HelpSet");
    debug(newHelpSet);
    System.out.println("Old HelpSet");
    debug(hs);

    try {
      if (action > 0) {
        clearGoneHelpSet(hs,newHelpSet);
      }else{
        mergeNewHelpSet(newHelpSet);
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void clearGoneHelpSet(HelpSet oldHS, HelpSet newHS)
  {
    remove(oldHS);
    Enumeration e= newHS.getHelpSets();
    if (!e.hasMoreElements()) {
      //setEnabled(false);
      browser.htmlClear();
      browser.htmlClear();
      return;
      //throw new RuntimeException(errorInfo.getString("impossible"));
    }else{
      mergeHelpSetsViews(newHS);
    }
    newHS=returnMasterHelpSet(newHS);
    hs = newHS;
    getStartingPage((TOCView)hs.getNavigatorView("TOC"),hs);
    try {
      String start = startingPage.toString();
      try {
        browser.setCurrentLocation(start);
      } catch (Exception e) {
      }
    }catch (NullPointerException ev) {
      ev.printStackTrace();
    }
  }

  private void remove(HelpSet temp)
  {
    toc.remove(temp.getNavigatorView("TOC"));
    index.remove(temp.getNavigatorView("Index"));
    search.remove(temp.getNavigatorView("Search"));
    search.setSearchEngine(null);
  }

  private void mergeNewHelpSet(HelpSet newHS)
  {
    Enumeration en = newHS.getHelpSets();
    HelpSet myHelpSet = null;
    int thesi = 0;
    try {
      while (en.hasMoreElements()) {
        myHelpSet = (HelpSet)en.nextElement();
      }
      toc.merge(myHelpSet.getNavigatorView("TOC"));
      index.merge(myHelpSet.getNavigatorView("Index"));
      search.merge(myHelpSet.getNavigatorView("Search"));
    }catch (Throwable el) {
      //What to do with the wrong helpSets that maybe want to be merged
      if (el.getMessage().toString().indexOf("Mal")>0) {
        thesi = el.getMessage().toString().indexOf("Mal");
        String message = el.getMessage().toString().substring(thesi);
        thesi = message.indexOf(":");
        message = message.substring(thesi);
        throw new RuntimeException(
          errorInfo.getString("fileNotFound") + "\n" + el.getMessage() +
          "\n" + errorInfo.getString("attentionFile")
        );
      }else{
        String comp = getComponentsName(getHelpSetName(myHelpSet));
        throw new RuntimeException(
          errorInfo.getString("folderNotFound") + "\n" +
          errorInfo.getString("search") + "\n" +
          comp.substring(comp.lastIndexOf("/")+1) + "\n" +
          errorInfo.getString("attentionFolder")
        );
      }
    }
    newHS = returnMasterHelpSet(newHS);
    hs = newHS;
    DefaultHelpModel newHelpModel = new DefaultHelpModel(hs);
    model = newHelpModel;

    getStartingPage((TOCView)hs.getNavigatorView("TOC"),hs);
    try {
      String start = startingPage.toString();
      try {
        browser.setCurrentLocation(start);
      } catch (Exception e) {
      }
      browser.refresh();
    }catch (NullPointerException ev) {
      ev.printStackTrace();
    }
  }
*/

//  private void debug(HelpSet newHelpSet)
//  {
//    System.out.println(newHelpSet);
//    Enumeration e = newHelpSet.getHelpSets();
//    while (e.hasMoreElements()) {
//      System.out.println(e.nextElement());
//    }
//  }

  private void getStartingPage(HelpSet hs)
  { 
    try {
      javax.help.Map.ID mapID = hs.getHomeID();
      javax.help.Map tryMap = hs.getCombinedMap();
      startingPage = tryMap.getURLFromID(mapID);
    }catch (Exception e) {
      hideInfoWindow();
      badHelpSet = hs;
      //e.printStackTrace();
      // Fix the message properly
      String theComponent = getHelpSetName(badHelpSet);
      System.out.println(
        errorInfo.getString("noStartingPageDetails") + " " +
        theComponent.substring(theComponent.lastIndexOf("/")+1) +
        errorInfo.getString("noStartingPageDetails2")
      );
      throw new RuntimeException(errorInfo.getString("impossible"));
    }
/*
    Enumeration enum = tocView.getDataAsTree().postorderEnumeration();
    try {
      String firstPageID = enum.nextElement().toString();
      String mapid = firstPageID.substring(0, firstPageID.lastIndexOf(","));
      javax.help.Map.ID map = javax.help.Map.ID.create(
        mapid.substring(mapid.lastIndexOf(":")+2), hs
      );
      javax.help.Map tryMap = hs.getCombinedMap();
      startingPage = tryMap.getURLFromID(map);
    }catch (Exception e) {
      hideInfoWindow();
      badHelpSet = hs;
      throw new RuntimeException(
        errorInfo.getString("toc") + "\n" + errorInfo.getString("comp") +
        "\n" + getComponentsName(getHelpSetName(badHelpSet))
      );
    }
*/
  }

  /**
   * Display an informational message in a new window. The contents of the
   * window are specified in three parts, which are displayed in three rows.
   * @param     info1   The first part of the window's contents.
   * @param     info2   The second part of the window's contents.
   *                    It can be a string, an icon, or any object that has a
   *                    to_String() method.
   * @param     info3   The third part of the window's contents.
   */
  protected static void showInfoWindow(String info1, Object info2, String info3)
  {
    infoWindow = new Window(new Frame());
    JPanel p = new JPanel(true);
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    p.setBorder(
      new CompoundBorder(
        new BevelBorder(SoftBevelBorder.LOWERED),new EmptyBorder(4,3,4,3)
      )
    );
    //p.setBorder(new CompoundBorder(
    //  new EtchedBorder(
    //    EtchedBorder.LOWERED,Color.gray.darker(),Color.gray),new EmptyBorder(2,1,2,1)
    //  )
    //);
    //p.setBorder(new EtchedBorder(
    //  EtchedBorder.LOWERED, Color.gray, Color.gray.brighter())
    //); //,Color.gray,Color.gray.darker()));
    //p.setBorder(BorderFactory.createRaisedBevelBorder());

    if (info1 != null) {
      JLabel lab1 = new JLabel(info1);
      //lab1.setFont(new Font("Helvetica", Font.BOLD, 14));
      Font font = lab1.getFont();
      lab1.setFont(font.deriveFont(Math.max(font.getSize(), 14), Font.BOLD));
      lab1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      lab1.setForeground(new Color(0, 110, 52));
      p.add(lab1);
    }

    p.add(Box.createVerticalStrut(3));

    if (info2 != null) {
      JLabel lab2;
      if (info2 instanceof Icon) {
        lab2 = new JLabel((Icon)info2);
      }else{
        if (info2 instanceof String) {
          lab2 = new JLabel((String)info2);
        }else{
          lab2 = new JLabel(info2.toString());
        }
      }
      //Dimension d = lab2.getPreferredSize();
      //lab2.setPreferredSize(new Dimension(d.width+20, d.height+20));
      lab2.setHorizontalAlignment(JLabel.CENTER);
      lab2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      p.add(lab2);
    }

    if (info3 != null) {
      JLabel lab3 = new JLabel(info3);
      //lab3.setFont(new Font("Helvetica", Font.ITALIC, 14));
      Font font = lab3.getFont();
      lab3.setFont(font.deriveFont(Math.max(font.getSize(), 14), Font.ITALIC));
      lab3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      lab3.setForeground(new Color(0, 110, 52)); //0, 48, 159
      p.add(lab3);
    }
    JPanel pp = new JPanel(true);
    pp.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    pp.setBounds(1, 1, 1, 1);
    pp.add(p);
    pp.setOpaque(true);
    infoWindow.add(pp);
    infoWindow.pack();
    // Center window on screen--This may not work under X11, where the window
    // manager may do as it pleases!
    Dimension screen = infoWindow.getToolkit().getScreenSize();
    Dimension win = infoWindow.getPreferredSize();
    infoWindow.setLocation((screen.width - win.width) / 2,
                           (screen.height - win.height) / 2);
    infoWindow.setVisible(true);
    infoWindow.toFront();

    pp.revalidate();
    pp.paintImmediately(pp.getVisibleRect());
  }

  /**
   * Hide the informational window displayed by showInfoWindow().
   */
  protected static void hideInfoWindow()
  {
    if (infoWindow != null) {
      infoWindow.setVisible(false);
      ESlateHandle.removeAllRecursively(infoWindow);
      infoWindow.dispose();
      infoWindow = null;
    }
  }

  /**
   * Show the frame.
   */
  @SuppressWarnings(value={"deprecation"})
  public void show()
  {
    super.show();
    // The first time this method is called, tell the browser to display the
    // starting page. We can't do this at initialization, because if the
    // browser is Internet Explorer, we get an ActiveXMissingProxyException.
    if (!initted) {
      initted = true;
      try {
        String start = startingPage.toString();
        try {
          browser.setCurrentLocation(start);
        } catch (Exception e) {
        }
      }
      catch (NullPointerException e){
        e.printStackTrace();
      }
    }
  }
}

/*
class MySearch extends BasicSearchNavigatorUI
{
  public MySearch (JHelpSearchNavigator nav)
  {
    super(nav);
  }

  public void itemsFound(SearchEvent e)
  {
    //super.itemsFound(e);
    SearchTOCItem tocitem;
    Vector nodes = new Vector();

    // Add all the children of the topnode to the Vector of nodes.
    Enumeration children = topNode.children();
    while (children.hasMoreElements()) {
      DefaultMutableTreeNode node =
        (DefaultMutableTreeNode) children.nextElement();
      nodes.addElement(node);
    }
    HelpModel helpmodel = searchnav.getModel();
    HelpSet hs = helpmodel.getHelpSet();
    javax.help.Map map = hs.getCombinedMap();
    Enumeration itemEnum = e.getSearchItems();
    while (itemEnum.hasMoreElements()) {
      SearchItem item = (SearchItem) itemEnum.nextElement();
      URL url;
      try {
        if (System.getProperty("java.version").startsWith("1.1.")) {
          url = new URL(item.getBase(), fixFilenameString(item.getFilename()));
          item = new SearchItem(
            item.getBase(), item.getTitle(), item.getLang(),
            fixFilenameString(item.getFilename()), item.getConfidence(),
            item.getBegin(), item.getEnd(), new Vector()
          );
        }else{
          url = new URL(item.getBase(), item.getFilename());
        }
      }catch (MalformedURLException me) {
        continue;
      }
      boolean foundNode = false;
      DefaultMutableTreeNode node = null;
      Enumeration nodesEnum = nodes.elements();
      while (nodesEnum.hasMoreElements()) {
        node = (DefaultMutableTreeNode)nodesEnum.nextElement();
        tocitem = (SearchTOCItem) node.getUserObject();
        URL testURL = tocitem.getURL();
        //System.out.println("testURL: " + testURL + " url: " + url);
        //if (testURL != null) {
        //  System.out.println("sameFile(): " + url.sameFile(testURL));
        //}
        if (testURL != null && url != null && url.sameFile(testURL)) {
          tocitem = (SearchTOCItem) node.getUserObject();
          tocitem.addSearchHit(new SearchHit(
            item.getConfidence(), item.getBegin(), item.getEnd())
          );
          foundNode = true;
          break;
        }
      }
      if (!foundNode) {
        tocitem = new SearchTOCItem(item);
        node = new DefaultMutableTreeNode(tocitem);
        nodes.addElement(node);
      }
    }
    topNode.removeAllChildren();
    // Create an array of the elements for sorting & copy the elements
    // into the array.
    DefaultMutableTreeNode[] array = new DefaultMutableTreeNode[nodes.size()];
    nodes.copyInto(array);
    // Sort the array (Quick Sort)
    quickSort(array, 0, array.length - 1);
    // Reload the topNode. Everthing is in order now.
    for (int i=0; i < array.length ; i++) {
      topNode.add((DefaultMutableTreeNode)array[i]);
    }
    // Tell the tree to repaint itself
    ((DefaultTreeModel)tree.getModel()).reload();
    tree.invalidate();
    tree.repaint();
    ((DefaultTreeModel)tree.getModel()).reload();
  }

  private String fixFilenameString(String oldString)
  {
    String newString;
    newString = oldString.replace('\\','/');
    return newString;
  }

  void quickSort(DefaultMutableTreeNode a[], int lo0, int hi0)
  {
    int lo = lo0;
    int hi = hi0;
    int mid;

    if ( hi0 > lo0) {
      // Arbitrarily establishing partition element as the midpoint of
      // the array.
      mid = ( lo0 + hi0 ) / 2;

      // Loop through the array until indices cross.
      while ( lo <= hi ) {
        // Find the first element that is greater than or equal to
        // the partition element starting from the left Index.
        while( ( lo < hi0 ) && ( compare(a[lo],a[mid]) > 0 )) {
          ++lo;
        }
        // Find an element that is smaller than or equal to
        // the partition element starting from the right Index.
        while( ( hi > lo0 ) && ( compare(a[hi],a[mid]) < 0 )) {
          --hi;
        }
        // If the indexes have not crossed, swap.
        if( lo <= hi ) {
          swap(a, lo, hi);
          ++lo;
          --hi;
        }
      }
      // If the right index has not reached the left side of array
      // must now sort the left partition.
      if ( lo0 < hi ) {
        quickSort( a, lo0, hi );
      }
      // If the left index has not reached the right side of array
      // must now sort the right partition.
      if ( lo < hi0 ) {
        quickSort( a, lo, hi0 );
      }
    }
  }

  private void swap(DefaultMutableTreeNode a[], int i, int j)
  {
    DefaultMutableTreeNode T;
    T = a[i];
    a[i] = a[j];
    a[j] = T;
  }

  private int compare (DefaultMutableTreeNode node1,
                       DefaultMutableTreeNode node2)
  {
    SearchTOCItem item1, item2;
    double confidence1, confidence2;
    int hits1, hits2;

    item1 = (SearchTOCItem) node1.getUserObject();
    confidence1 = item1.getConfidence();
    hits1 = item1.hitCount();

    item2 = (SearchTOCItem) node2.getUserObject();
    confidence2 = item2.getConfidence();
    hits2 = item2.hitCount();

    // Confidence is a penality. The lower the better>
    if (confidence1 > confidence2) {
      // node1 is less than node2
      return -1;
    }else{
      if (confidence1 < confidence2) {
        // node1 is greater than node2
        return 1;
      }else{
        // Confidences are the same check the hits
        if (hits1 < hits2) {
          // node1 is less than node2
          return -1;
        }else{
          if (hits1 > hits2) {
            // node2 is greater than node2
            return 1;
          }
        }
      }
    }
    // nodes1 and nodes2 are equivalent
    return 0;
  }
}
*/
