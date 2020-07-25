package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class FTPDialog extends JDialog {
    Locale locale;
    ResourceBundle ftpDialogBundle;
    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;

    JButton addComponent, removeComponent, editComponent, openMicroworldFile;
    JButton upload, download;
    JFileChooser localFileChooser;
    WebFileDialog remoteFileChooser;
    JLabel preloadlb;
    JTextField preloadField;
    ESlateContainer container;

  public FTPDialog(java.awt.Frame parentFrame, final ESlateContainer container) {
/////nikosM new
        super(parentFrame, true);
////nikosM new end
        this.container = container;

        locale = Locale.getDefault();
        ftpDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.FTPDialogBundle", locale);
        if (ftpDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.FTPDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(ftpDialogBundle.getString("DialogTitle"));

        upload = new JButton(new ImageIcon(getClass().getResource("images/upload.gif")));
        download = new JButton(new ImageIcon(getClass().getResource("images/download.gif")));
        upload.setToolTipText(ftpDialogBundle.getString("UploadFile"));
        download.setToolTipText(ftpDialogBundle.getString("DownloadFile"));
        Dimension buttonSize = new Dimension(40, 25);
        upload.setMaximumSize(buttonSize);
        upload.setPreferredSize(buttonSize);
        upload.setMinimumSize(buttonSize);
        download.setMaximumSize(buttonSize);
        download.setPreferredSize(buttonSize);
        download.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        upload.setMargin(zeroInsets);
        download.setMargin(zeroInsets);

        JPanel ftpButtonPanel = new JPanel(true);
        ftpButtonPanel.setLayout(new BoxLayout(ftpButtonPanel, BoxLayout.Y_AXIS));

        ftpButtonPanel.add(Box.createGlue());
        ftpButtonPanel.add(Box.createVerticalStrut(30));
        ftpButtonPanel.add(upload);
        ftpButtonPanel.add(Box.createVerticalStrut(14));
        ftpButtonPanel.add(download);
        ftpButtonPanel.add(Box.createGlue());
        ftpButtonPanel.setBorder(new EmptyBorder(5,5,5,5));

        localFileChooser = new JFileChooser();
        remoteFileChooser = new WebFileDialog(container, true);
        ((WebFileDialog) remoteFileChooser).disableCustonApproveAction = true;

        Dimension dim = new Dimension(311, 300); //155);
        localFileChooser.setMinimumSize(dim);
        localFileChooser.setPreferredSize(dim);
        localFileChooser.setMaximumSize(dim);
//        dim = new Dimension(300, 155);
        remoteFileChooser.setMinimumSize(dim);
        remoteFileChooser.setPreferredSize(dim);
        remoteFileChooser.setMaximumSize(dim);

        JPanel localFileChooserPanel = new JPanel(true);
        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(ftpDialogBundle.getString("LocalSystem"));
        tb1.setTitleColor(titleBorderColor);
        if (localeIsGreek)
            tb1.setTitleFont(greekUIFont);
        localFileChooserPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,0,2,0)));
        localFileChooserPanel.add(localFileChooser);

        JPanel remoteFileChooserPanel = new JPanel(true);
        TitledBorder tb2 = new TitledBorder(ftpDialogBundle.getString("RemoteSystem"));
        tb2.setTitleColor(titleBorderColor);
        if (localeIsGreek)
            tb2.setTitleFont(greekUIFont);
        remoteFileChooserPanel.setBorder(new CompoundBorder(tb2, new EmptyBorder(0,0,2,0)));
        remoteFileChooserPanel.add(remoteFileChooser);

        JPanel ftpPanel = new JPanel(true);
        ftpPanel.setLayout(new BoxLayout(ftpPanel, BoxLayout.X_AXIS));
        ftpPanel.setBorder(new EmptyBorder(5,0,0,0));

        ftpPanel.add(localFileChooserPanel);
        ftpPanel.add(ftpButtonPanel);
        ftpPanel.add(remoteFileChooserPanel);

        // The button panel (CLOSE)
        final JButton closeButton = new JButton(ftpDialogBundle.getString("Close"));
        if (localeIsGreek)
            closeButton.setFont(greekUIFont);
        Color color128 = new Color(0, 0, 128);
        closeButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
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

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(ftpPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        localFileChooser.remove(0);
        Component comp = ((JPanel) localFileChooser.getComponent(0)).getComponent(3);
        Component comp2 = ((JPanel) localFileChooser.getComponent(0)).getComponent(5);
        Component comp3 = ((JPanel) localFileChooser.getComponent(0)).getComponent(9);
        ((JPanel) localFileChooser.getComponent(0)).removeAll();
        ((JPanel) localFileChooser.getComponent(0)).add(new JLabel(ftpDialogBundle.getString("Dir")));
        ((JPanel) localFileChooser.getComponent(0)).add(Box.createHorizontalStrut(5));
        ((JPanel) localFileChooser.getComponent(0)).add(comp);
        ((JPanel) localFileChooser.getComponent(0)).add(Box.createHorizontalStrut(10));
        ((JPanel) localFileChooser.getComponent(0)).add(comp2);
        ((JPanel) localFileChooser.getComponent(0)).add(Box.createHorizontalStrut(10));
        ((JPanel) localFileChooser.getComponent(0)).add(comp3);
        localFileChooser.remove(4);
        localFileChooser.remove(4);
        localFileChooser.remove(3);

        remoteFileChooser.remove(0);
        comp = ((JPanel) remoteFileChooser.getComponent(0)).getComponent(3);
        comp2 = ((JPanel) remoteFileChooser.getComponent(0)).getComponent(5);
        comp3 = ((JPanel) remoteFileChooser.getComponent(0)).getComponent(9);
        ((JPanel) remoteFileChooser.getComponent(0)).removeAll();
        ((JPanel) remoteFileChooser.getComponent(0)).add(new JLabel(ftpDialogBundle.getString("Dir")));
        ((JPanel) remoteFileChooser.getComponent(0)).add(Box.createHorizontalStrut(5));
        ((JPanel) remoteFileChooser.getComponent(0)).add(comp);
        ((JPanel) remoteFileChooser.getComponent(0)).add(Box.createHorizontalStrut(10));
        ((JPanel) remoteFileChooser.getComponent(0)).add(comp2);
        ((JPanel) remoteFileChooser.getComponent(0)).add(Box.createHorizontalStrut(10));
        ((JPanel) remoteFileChooser.getComponent(0)).add(comp3);
        remoteFileChooser.remove(4);
        remoteFileChooser.remove(4);
        remoteFileChooser.remove(3);

        localFileChooser.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) ||
                    evt.getPropertyName().equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                    File file = localFileChooser.getSelectedFile();
                    if (file == null || remoteFileChooser.getWebSite() == null)
                        upload.setEnabled(false);
                    else
                        upload.setEnabled(true);
                }else if (evt.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    upload.setEnabled(false);
                }
            }
        });
        remoteFileChooser.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) ||
                    evt.getPropertyName().equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                    File file = remoteFileChooser.getSelectedFile();
                    if (file == null)
                        download.setEnabled(false);
                    else
                        download.setEnabled(true);
                }else if (evt.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    File file = localFileChooser.getSelectedFile();
                    if (file == null || remoteFileChooser.getWebSite() == null)
                        upload.setEnabled(false);
                    else
                        upload.setEnabled(true);
                    download.setEnabled(false);
                }
            }
        });

        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                File file = localFileChooser.getSelectedFile();
                if (file == null)
                    return;

                if (remoteFileChooser.getWebSite() == null)
                    return;

//nikosM
                byte[] contents = container.webServerMicrosHandle.readByteStreamFromFile(file);
//                byte[] contents = container.readByteStreamFromFile(file);
//nikosM end
                if (contents == null)
                    return;

                String currServerDir = remoteFileChooser.getCurrentDirectory().toString();
                String fileName = file.toString().substring(file.toString().lastIndexOf(System.getProperty(
                                  "file.separator"))+1);
                String webFileDir = currServerDir.substring(currServerDir.indexOf(remoteFileChooser.getWebSiteMirror())+
                                 remoteFileChooser.getWebSiteMirror().length());
                String webFile;
                if (webFileDir.length() != 0)
                    webFile = webFileDir + System.getProperty("file.separator") + fileName;
                else
                    webFile = fileName;

                System.out.println("webFile: " + webFile);

//nikosM new
//                boolean saved = container.uploadByteStreamToServer(
                WaitDialog pleaseWait = new WaitDialog(false, false, true, null, true);
                pleaseWait.showDialog(container);
                boolean saved = container.webServerMicrosHandle.saveByteStreamToServer(
//nikosM new
                    remoteFileChooser.getWebSite(),
                    webFile,
                    contents,
                    pleaseWait);
System.out.println("OK ? ="+saved);
                //Update the contents of the current directoryr of local web site mirror
//nikosM change
//                container.createWebMirrorDirectory(webFileDir, remoteFileChooser.getWebSiteMirror(),
//                    container.getWebDirectoryListing(remoteFileChooser.getWebSite(), webFileDir));
                container.webServerMicrosHandle.createWebMirrorDirectory(webFileDir, remoteFileChooser.getWebSiteMirror(),
                    container.webServerMicrosHandle.getWebDirectoryListing(remoteFileChooser.getWebSite(), webFileDir));
//nikosM end of change

                remoteFileChooser.rescanCurrentDirectory();
                pleaseWait.dispose();
            }
        });

        download.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (remoteFileChooser.getSelectedFile() == null)
                    return;

                File selectedFile = remoteFileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                String selectedFilePath;
                try{
                    selectedFilePath = selectedFile.getCanonicalPath();
                }catch (java.io.IOException exc) {
                    selectedFilePath = selectedFile.getAbsolutePath();
                }

//                System.out.println("selectedFilePath: " + selectedFilePath);
//                System.out.println("fileName: " + fileName);
//                System.out.println("websiteMirror: " + remoteFileChooser.getWebSiteMirror());
//nikosM new
//                String webFile = container.getWebDirNameFromRoot(selectedFilePath, remoteFileChooser.getWebSiteMirror());
//                byte[] serverByteStream = container.openServerFile(
                String webFile = container.webServerMicrosHandle.getWebDirNameFromRoot(selectedFilePath, remoteFileChooser.getWebSiteMirror());
                try {
                  WaitDialog pleaseWait = new WaitDialog(false, false, true, null, true);
                  pleaseWait.showDialog(container);
                  byte[] serverByteStream = container.webServerMicrosHandle.openServerFile(
//nikosM new end
                                          remoteFileChooser.getWebSite(),
                                          webFile,
                                          pleaseWait);
//                System.out.println("webFile: " + webFile);
                  if (serverByteStream != null) {
                      container.saveByteArray(serverByteStream,
                            new File(localFileChooser.getCurrentDirectory(), fileName));
                      localFileChooser.rescanCurrentDirectory();
                  }
                  pleaseWait.dispose();
                }catch (java.io.IOException e){}
            }
        });

        //Initialize the Dialog
        upload.setEnabled(false);
        download.setEnabled(false);


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
    }

    public void showDialog(Component comp) {
        pack();
        setResizable(false);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (comp == null || !comp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = comp.getBounds();
            java.awt.Point compLocation = comp.getLocationOnScreen();
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
        System.out.println("Size: " + remoteFileChooser.getPreferredSize());
        show();
    }

}
