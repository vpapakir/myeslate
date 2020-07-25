package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.event.ESlateContainerListener;
import gr.cti.eslate.base.container.event.MwdChangedEvent;
import gr.cti.eslate.base.container.event.MwdHistoryChangedEvent;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
////nikosM end

public class HistoryDialog extends JPanel implements Disposable { //JFrame {
    Locale locale;
    ResourceBundle historyDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    private boolean ignoreEvent = false;

//JP    JButton closeButton;
    JList historyList;
    JScrollPane scrollPane;
    ESlateContainer cont;
    ESlateContainerListener containerListener;

    public HistoryDialog(ESlateContainer container) {
        super();
        setLayout(new BorderLayout());

        if (container == null)
            return;
        this.cont = container;

        locale = Locale.getDefault();
        historyDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.HistoryDialogBundle", locale);
        if (historyDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.HistoryDialogBundle_el_GR"))
            localeIsGreek = true;

//        setTitle(historyDialogBundle.getString("DialogTitle"));

        historyList = new JList(container.mwdHistory.getContents());
        scrollPane = new JScrollPane(historyList);

//        if (localeIsGreek)
//            historyList.setFont(greekUIFont);

        historyList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (cont.currentlyOpenMwdFileName != null)
            historyList.setSelectedIndex(cont.mwdHistory.getHistoryIndex());

        FontMetrics fm1 = getToolkit().getFontMetrics(historyList.getFont());
        int maxWidth = fm1.stringWidth(container.mwdHistory.getMicroworldAt(0));
        int itemCount = container.mwdHistory.getItemCount();
        for (int i=1; i<itemCount; i++) {
            int w = fm1.stringWidth(container.mwdHistory.getMicroworldAt(i));
            if (w > maxWidth)
                w = maxWidth;
        }

        maxWidth = maxWidth + 100;
        if (maxWidth > 600) maxWidth = 600;
        int height = itemCount * 28;
        if (height > 300) height = 300;

//        System.out.println("maxWidth: " + maxWidth + ", height: " + height);
        Dimension dim = new Dimension(maxWidth, height);
/*JP        scrollPane.setMinimumSize(dim);
        scrollPane.setMaximumSize(dim);
        scrollPane.setPreferredSize(dim);
*/
        // The button panel (OK, CANCEL)
/*JP        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        closeButton = new JButton(historyDialogBundle.getString("Close"));
        if (localeIsGreek)
            closeButton.setFont(greekUIFont);
        closeButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        closeButton.setMaximumSize(buttonSize);
        closeButton.setPreferredSize(buttonSize);
        closeButton.setMinimumSize(buttonSize);
        closeButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
*/
        // The main panel
/*JP        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 5));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
*/
//JP        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

//JP        getContentPane().add(mainPanel);
        add(scrollPane, BorderLayout.CENTER);  //JP mainPanel);

        historyList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
/////nikosM
////System.out.println("---> arxi tis valueChanged (o listener sti lista) e.getValueIsAdjusting(): " + e.getValueIsAdjusting());
              if (e.getValueIsAdjusting()) return;
              if (!cont.updatingHistoryFlag) {
/////nikosM end

                 String currentMwd = cont.currentlyOpenMwdFileName;
                 String mwd = (String) historyList.getSelectedValue();

                 if (mwd == null) return;

// check this later too if remote **
                 if (currentMwd != null && mwd.equals(currentMwd)) {
                     int selIndex = historyList.getSelectedIndex();
                     cont.mwdHistory.setCurrentMicroworld(selIndex);
                     return;
                 }

                 historyList.paintImmediately(historyList.getVisibleRect());
                 setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                 ignoreEvent = true;
////nikosM
                 historyList.setSelectedValue(mwd, true);
                 boolean mwdIsRemote=false;
                 for (int ii=0;ii<cont.mwdHistory.getHistorySize();ii++) {
                     if (cont.mwdHistory.getMicroworldAt(ii).equals(mwd)) {
                       if (cont.mwdHistory.isRemoteMicroworldAt(ii))
                           mwdIsRemote=true;
                       break;
                     }
                 }
                 if (mwdIsRemote) {
                      String webFile, webServer;
                      int c=mwd.indexOf(cont.containerBundle.getString("OnServer"));
                      System.out.println(c+" "+mwd);
                      webFile=mwd.substring(0,c);
                      String temp=mwd.substring(c+cont.containerBundle.getString("OnServer").length(),mwd.length());
                      System.out.println(temp);
                      webServer=(String) cont.webSites.get(temp);
/* checking again **
   if currentMwd is remote the mwd.equals(currentMwd) returns false even if
   it's the same file, due to the addition " on <ServerName>". So check again
*/
                      if ((webFile.equals(currentMwd))&&(webServer.equals(cont.webServerMicrosHandle.webSite))) {
                         int selIndex = historyList.getSelectedIndex();
                         cont.mwdHistory.setCurrentMicroworld(selIndex);
                         setCursor(Cursor.getDefaultCursor());
                         ignoreEvent = false;
                         return;
                      }
                      if (!cont.webServerMicrosHandle.openRemoteMicroWorld(webServer,webFile,false)) {
                          ESlateOptionPane.showMessageDialog(null, cont.containerBundle.getString("ContainerMsg12"), cont.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                      else {
                         historyList.setSelectedValue(currentMwd, true);
                         int selIndex = historyList.getSelectedIndex();
                         cont.mwdHistory.setCurrentMicroworld(selIndex);
                      }
                 }
                 else
/////nikosM end
                     if (!cont.loadLocalMicroworld(mwd, false, true)) {
//                      System.out.println("Selecting the original");
                        historyList.setSelectedValue(currentMwd, true);
                        int selIndex = historyList.getSelectedIndex();
                        cont.mwdHistory.setCurrentMicroworld(selIndex);
                     }
                setCursor(Cursor.getDefaultCursor());
                ignoreEvent = false;
              }
            }
        });

/*JP        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cont.removeContainerListener(containerListener);
                cont.mwdHistoryDialogIsDisplayed = false;
                cont = null;
                dispose();
            }
        });
*/

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
//                System.out.println("Revalidating the scrollpane");
                scrollPane.revalidate();
            }
        });

        containerListener = new gr.cti.eslate.base.container.event.ESlateContainerAdapter() {
            public void mwdChanged(MwdChangedEvent evt) {
                if (ignoreEvent) return;
////nikosM currentMicroworld change
                if (cont.microworld.eslateMwd == null)
////nikosM currentMicroworld change end
                    historyList.clearSelection();
                else
                    historyList.setSelectedIndex(cont.mwdHistory.getHistoryIndex());
                historyList.repaint();
            }
            public void mwdHistoryChanged(MwdHistoryChangedEvent evt) {
                historyList.setListData(cont.mwdHistory.getContents());
                historyList.setSelectedIndex(cont.mwdHistory.getHistoryIndex());
                historyList.repaint();
            }
        };

        container.addContainerListener(containerListener);

/*JP        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cont.mwdHistoryDialogIsDisplayed = false;
                dispose();
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = closeButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = closeButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(closeButton);

        //Show the dialog
        pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (getSize().height >= screenSize.height-30) {

            System.out.println("getsize(): " + getSize());
            System.out.println("mainPanel.getSize(): " + mainPanel.getSize() + ", screenSize.height: " + screenSize.height);
            setSize(new Dimension(getSize().width+10, screenSize.height-30));
            pack();
//            mainPanel.setPreferredSize(new Dimension(mainPanel.getSize().width, screenSize.height-100)); //30-27));
//            mainPanel.revalidate();
            System.out.println("mainPanel.getSize(): " + mainPanel.getSize() + ", screenSize.height: " + screenSize.height);
//            scrollPane.setPreferredSize(new Dimension(scrollPane.getSize().width, screenSize.height-30-buttonPanel.getSize().height));
//            scrollPane.revalidate();
        }

        int x, y;
        if (container == null || !container.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = container.getBounds();
            java.awt.Point compLocation = container.getLocationOnScreen();
    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
            x = compLocation.x + compBounds.width/2 - getSize().width/2;
            y = compLocation.y + compBounds.height/2-getSize().height/2;
            if (x+getSize().width > screenSize.width)
                x = screenSize.width - getSize().width;
            if (y+getSize().height > screenSize.height)
                y = screenSize.height - getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        setLocation(x, y);
        historyList.requestFocus();
        show();
*/
    }

    public String getPanelTitle() {
        return historyDialogBundle.getString("DialogTitle");
    }

    public void disposed() {
        cont.removeContainerListener(containerListener);
    }
}




