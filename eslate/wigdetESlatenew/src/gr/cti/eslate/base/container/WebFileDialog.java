package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class WebFileDialog extends JFileChooser {
    String webSiteMirror;
    String webSite, webFile;
    ESlateContainer container;
    private boolean remoteFile = false;
  //  private byte[] serverFileStream = null;
    private URLConnection webSiteConnection = null;
//    int type = OPEN_DIALOG;
    boolean remoteOnly = false, disableCustonApproveAction = false;
    /* Replaces the private JDialog variable in JFileChooser */
    private JDialog dialog = null;
    /* Replaces the private 'dialogTitle' variable in JFileChooser */
    private String dialogTitle = null;
    private int returnValue = ERROR;

    private java.util.Timer renameTimer = new java.util.Timer();
    private UpdateRenameTask updateRenameTask=new UpdateRenameTask();
////nikosM new
    private boolean isScheduled=false;
//    private boolean newRenameTask=false;
//    private boolean makeNewSchedule=true;
//    private boolean sceduleIsActive=false;
////nikosM new end
    ResourceBundle bundle;
    /* Overrides the method of JFileChooser, because this method uses variable 'dialog'.
     */
    public void setDialogTitle(String dialogTitle) {
      	String oldValue = this.dialogTitle;
      	this.dialogTitle = dialogTitle;
      	if(dialog != null) {
      	    dialog.setTitle(dialogTitle);
      	}
      	firePropertyChange(DIALOG_TITLE_CHANGED_PROPERTY, oldValue, dialogTitle);
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public WebFileDialog(ESlateContainer container, boolean onlyRemoteContent) {
////nikosM new
        super(container.webServerMicrosHandle.webSitesLocalDirName);
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.WebFileDialogBundle", Locale.getDefault());
        this.container = container;
        this.remoteOnly = onlyRemoteContent;
        setRenameWebDialogFiles();

////nikosM end new
        addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(WebFileDialog.DIRECTORY_CHANGED_PROPERTY)) {
                    String prevDirFullName;
                    try{
                        prevDirFullName = ((File) e.getOldValue()).getCanonicalPath();
                    }catch (IOException exc) {
                        prevDirFullName = e.getOldValue().toString();
                    }

                    String newDirFullName;
                    try{
                        newDirFullName = ((File)e.getNewValue()).getCanonicalPath();
                    }catch (IOException exc) {
                        newDirFullName = e.getNewValue().toString();
                    }
                    if (prevDirFullName.length() > newDirFullName.length() && !remoteOnly)
                        return;  // We are moving up the directory hierarchy

                    /* When the HOME button is pressed, for some strange reason
                     * it occurs that prevDirFullName.equals(newDirFullName)
                     */
                    if (newDirFullName.equals(prevDirFullName))
                        return;

                    String newDirName = newDirFullName.substring(newDirFullName.lastIndexOf(
                            System.getProperty("file.separator"))+1);
                    /* If we are entering the local roor mirror directory for a web site
                     * then the WebFileDialog must be updated with the sites address.
                     */
                    if (WebFileDialog.this.container.webSites.count(newDirName) == 1) {
                        String webSiteAddress = (String) WebFileDialog.this.container.webSites.get(newDirName);
                        ((WebFileDialog) e.getSource()).setWebSite(webSiteAddress, new File(newDirFullName));
                    }

  //                  System.out.println("Root: " + ((WebFileDialog) e.getSource()).getWebSiteMirror());
                    String newDirNameFromRoot = null;
                    if (((WebFileDialog) e.getSource()).getWebSiteMirror() != null) { //The WebFileDialog is inside a web directory
////nikosM
                        newDirNameFromRoot = WebFileDialog.this.container.webServerMicrosHandle.getWebDirNameFromRoot(
////nikosM end
                            newDirFullName,
                            ((WebFileDialog) e.getSource()).getWebSiteMirror());
                    }

                    if (newDirNameFromRoot != null && !newDirNameFromRoot.equals(newDirFullName)) { //This means that the new directory is not local
////nikosM
                        Hashtable dirContents = WebFileDialog.this.container.webServerMicrosHandle.getWebDirectoryListing(
                            ((WebFileDialog) e.getSource()).getWebSite(),
                            newDirNameFromRoot);
                        File webMirrorDir = WebFileDialog.this.container.webServerMicrosHandle.createWebMirrorDirectory(
////nikosM end
                            newDirNameFromRoot,
                            ((WebFileDialog) e.getSource()).getWebSiteMirror(),
                            dirContents);

                        ((WebFileDialog) e.getSource()).rescanCurrentDirectory();
                    }else{
  //                      System.out.println("LOCAL FILE");
                        webSite = null;
                        webSiteMirror = null;
                        if (remoteOnly)
////nikosM
                            WebFileDialog.this.setCurrentDirectory(new File(WebFileDialog.this.container.webServerMicrosHandle.webSitesLocalDirName));
////nikosM end
                    }
                }
            }
        });
    }
///// nikosM
//// get from container the specified class
    public Component getComponentFromClass(java.awt.Container cont,java.lang.Class classToFind){
        Component []comps = cont.getComponents();
        for (int i=0;i<cont.getComponentCount();i++){
          if ((comps[i]!=null)&&(classToFind.isAssignableFrom(comps[i].getClass()))){
              return comps[i];
          }
          else
            if ((comps[i]!=null)&&(java.awt.Container.class.isAssignableFrom(comps[i].getClass()))) {
                Component tmpComponent = this.getComponentFromClass((java.awt.Container)comps[i],classToFind);
                if (tmpComponent!=null)
                    return tmpComponent;
            }
        }
        return null;
    }

///catch mouse clicks and open rename dialog if a single click happend
///or open a file if a double click happend
    public void setRenameWebDialogFiles(){
            NewDirFileSystemView newDirFileSystem= new NewDirFileSystemView();
            setFileSystemView(newDirFileSystem);
            java.awt.Component tmpComponent=getComponentFromClass((java.awt.Container)this,javax.swing.JTextField.class);
            final JTextField textField = (JTextField) tmpComponent;
            tmpComponent=getComponentFromClass((java.awt.Container)this,javax.swing.JList.class);
            final java.awt.event.MouseListener[] mls = (java.awt.event.MouseListener[])(tmpComponent.getListeners(java.awt.event.MouseListener.class));
            for (int i=0;i<mls.length;i++){
                tmpComponent.removeMouseListener(mls[i]);
            }
            javax.swing.event.ListSelectionListener[] lsl = (javax.swing.event.ListSelectionListener[])(tmpComponent.getListeners(javax.swing.event.ListSelectionListener.class));
            for (int i=0;i<lsl.length;i++){
                ((JList) tmpComponent).removeListSelectionListener(lsl[i]);
            }
            final JList jList=(JList)tmpComponent;
            final String webSiteFinal=webSite;
////nikosM news
            jList.addMouseListener(new MouseAdapter(){
                public void originalMousePressed(MouseEvent e)
                {
                    MouseEvent me = new MouseEvent(
                      jList, MouseEvent.MOUSE_PRESSED, 0,
                      java.awt.event.InputEvent.BUTTON1_MASK,
                      e.getX(), e.getY(), 1, false
                    );
                    for (int i=0;i<mls.length;i++){
                        mls[i].mousePressed(me);
                    }
                }
                public void mouseClicked(MouseEvent e){
                  long delayOfRename=550; /*millisecs*/
                  int index = jList.locationToIndex(e.getPoint());
                  if (jList.getSelectedIndex()!=index) {
                      jList.setSelectedIndex(index);
                      File f = (File) jList.getSelectedValue();
                      if (!f.isDirectory()) {
                          textField.setText(f.getName());
                          setSelectedFile(f);
                          // Invoking the original mousePressed methods will
                          // highlight the selected item.
                          originalMousePressed(e);
                      }else
                          textField.setText("");
                  }
                  else {
                    if ((index!=-1)&&(jList.getSelectedIndex()==index)) {
                          if ((e.getClickCount() != 2)) {
                               jList.setSelectedIndex(index);
                               updateRenameTask.setJList(jList);
                               updateRenameTask.setNewRenamedFileDir(getCurrentDirectory().toString());
                               renameTimer.schedule(updateRenameTask,delayOfRename);
                               isScheduled=true;
                          }
                          else {
                               if ((updateRenameTask==null)||(!isScheduled)||((isScheduled)&&(updateRenameTask.cancel()))) {
                                  isScheduled=false;
                                  updateRenameTask=null;
                                  File currentFileSelection=new File(jList.getSelectedValue().toString());
                                  if (currentFileSelection.isDirectory()) {
                                      setCurrentDirectory(currentFileSelection);
                                      updateRenameTask=new UpdateRenameTask();
                                      isScheduled=false;
                                  }
                                  else
                                      approveSelection();
                               }
                          }
                    }
                  }
                }
            });
////nikosM end
            jList.addMouseMotionListener(new MouseMotionAdapter(){
                public void mouseDragged(MouseEvent e){
                    int index = jList.locationToIndex(e.getPoint());
                    jList.setSelectedIndex(index);
                    File f = (File) jList.getSelectedValue();
                    if (!f.isDirectory())
                          textField.setText(f.getName());
                    else
                          textField.setText("");
                }
            });
///////nikosM new end
    }

/*
    public int showOpenDialog(java.awt.Component parent) {
        type = OPEN_DIALOG;
        return super.showOpenDialog(parent);
    }
*/


    protected JDialog createDialog(Component parent) throws HeadlessException {
        dialog = super.createDialog(parent);

        // ESCAPE HANDLER
        registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelSelection();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        return dialog;
    }

    public int showDialogOld(Component parent, String approveButtonText) {
        if(approveButtonText != null) {
            setApproveButtonText(approveButtonText);
            setDialogType(CUSTOM_DIALOG);
        }

/*
        Frame frame = parent instanceof Frame ? (Frame) parent
              : (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);

        String title = null;

        if(getDialogTitle() != null) {
            title = dialogTitle;
        } else {
            title = getUI().getDialogTitle(this);
        }

        dialog = new JDialog(frame, title, true);
        java.awt.Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
*/

        // ESCAPE HANDLER
        registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelSelection();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialog.setVisible(true);
        return returnValue;
    }

/*
    public int showSaveDialog(java.awt.Component parent) {
        type = SAVE_DIALOG;
        return super.showSaveDialog(parent);
    }
*/

    public void approveSelection() {
//        super.approveSelection();
        	returnValue = APPROVE_OPTION;
        	if(dialog != null) {
        	    dialog.setVisible(false);
        	}
        	fireActionPerformed(APPROVE_SELECTION);

        if (disableCustonApproveAction)
            return;

  //      System.out.println("WebFileDialog --> selected file: " + getSelectedFile());
        File selectedFile = getSelectedFile();
        String selectedFileName;
        try{
            selectedFileName = selectedFile.getCanonicalPath();
        }catch (IOException exc) {
            selectedFileName = selectedFile.toString();
        }
//        System.out.println("selectedFileName: " + selectedFileName + ", webSiteMirror: " + webSiteMirror);
        if (webSiteMirror != null && selectedFileName.indexOf(webSiteMirror) != -1) { //not a local file
  //          System.out.println("Not a local file");
            remoteFile = true;
            if (getDialogType() == OPEN_DIALOG && container != null) {
////nikosM
                webFile = container.webServerMicrosHandle.getWebDirNameFromRoot(selectedFileName, webSiteMirror);
////nikosM end
  /*              container.paintImmediately(container.getVisibleRect());
                InfoWindow pleaseWait;
                String[] imageFileNames = new String[2];
                imageFileNames[0] = "images/read1.gif";
                imageFileNames[1] = "images/read2.gif";
                Thread thr = new Thread((pleaseWait = new InfoWindow(container, imageFileNames, container.containerBundle.getString("ContainerMsg10"), 800)));
                pleaseWait.getContentPane().setBackground(java.awt.Color.white);
                pleaseWait.setInvokingThread(thr);
                thr.start();
                thr.setPriority(Thread.MIN_PRIORITY);

                webFile = container.getWebDirNameFromRoot(selectedFileName, webSiteMirror);
                serverFileStream = container.openServerFile(
                      webSite,
                      webFile);

                pleaseWait.dispose();

                if (serverFileStream == null)
                    ESlateOptionPane.showMessageDialog(container.parentComponent, container.containerBundle.getString("ContainerMsg12"), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
  */
            }else{
  //              serverFileStream = null;
            }
            /* If this web site is not registered, then don't leave any local mirror dir on the
             * user's machine. This is important, beacuse this way we don't have to store the
             * addresses of the unregistered sites, so that the user to be able to use the same
             * instance of WebFileDialog to move between sites. This is allowed only for registered
             * sites.
             */
      //      System.out.println("Exists: " + container.webSites.countValues(webSite));
            if (container.webSites.countValues(webSite) == 0)
                container.containerUtils.removeDirectory(new File(webSiteMirror));
        }else{
            remoteFile = false;
  //          serverFileStream = null;
            webFile = null;
        }
  //      System.out.println("remoteFile: " + remoteFile + ",serverFileStream: " + serverFileStream);
    }


    public void cancelSelection() {
//        super.cancelSelection();
      	returnValue = CANCEL_OPTION;
      	if(dialog != null) {
      	    dialog.setVisible(false);
      	}
      	fireActionPerformed(CANCEL_SELECTION);
        remoteFile = false;
  //      serverFileStream = null;
    }

  /*  public byte[] getServerFileStream() {
        return serverFileStream;
    }
  */
    public boolean isRemoteFile() {
        return remoteFile;
    }

    public String getWebFile() {
        return webFile;
    }

    public void setRemoteOnly(boolean showRemoteOnly) {
        remoteOnly = showRemoteOnly;
    }

    public boolean isRemoteOnly() {
        return remoteOnly;
    }

    public void rescanCurrentDirectory() {
        super.rescanCurrentDirectory();
  //      System.out.println("WebFileDialog --> rescaning current directory");
  //      System.out.println("webSiteMirror: " + webSiteMirror + ", currDir: " + getCurrentDirectory());


        /* If the file dialog is positioned inside the web mirror site, then
         * perform a 'dir' on the FileServer, so that if any new directory was
         * created by the user, this directory will be replicated to the server.
         */
        String currentDir;
        try{
            currentDir = getCurrentDirectory().getCanonicalPath();
        }catch (IOException exc) {
            currentDir = getCurrentDirectory().toString();
        }
        if (webSiteMirror == null) {
            System.out.println("webSiteMirror = null");
            return;
        }
        if (currentDir.indexOf(webSiteMirror) != -1) { //not a local file
            if (container != null) {
////nikosM
                String currDirFromMirrorRoot = container.webServerMicrosHandle.getWebDirNameFromRoot(currentDir, webSiteMirror);
                Hashtable contents = container.webServerMicrosHandle.getWebDirectoryListing(webSite, currDirFromMirrorRoot);
//// nikosM end
  //              System.out.println("rescanCurrentDir contents: " + contents);

                /* Check if any new directory has been created. If it has, then create it on the server,
                 * too.
                 */
                String[] localContents = getCurrentDirectory().list();
                for (int i=0; i<localContents.length; i++) {
                    if (!contents.containsKey(localContents[i])) {
                        File newDir = new File(getCurrentDirectory(), localContents[i]);
                        String newDirName;
                        try{
                            newDirName = newDir.getCanonicalPath();
                        }catch (IOException exc) {
                            newDirName = newDir.toString();
                        }

                        if (newDir.isDirectory())
////nikosM
//                            container.createWebDirectory(webSite, container.getWebDirNameFromRoot(newDirName, webSiteMirror));
                            container.webServerMicrosHandle.createWebDirectory(webSite, container.webServerMicrosHandle.getWebDirNameFromRoot(newDirName, webSiteMirror));
////nikosM end
                    }
                }
            }
        }
    }

    public String getWebSiteMirror() {
        return webSiteMirror;
    }

    public void setWebSite(String webSite, File webMirrorDirectory) {
        setCurrentDirectory(webMirrorDirectory);
        try{
            this.webSiteMirror = webMirrorDirectory.getCanonicalPath();
        }catch (IOException exc) {
            this.webSiteMirror = webMirrorDirectory.toString();
        }
        this.webSite = webSite;
  //      System.out.println("setWebSite(): " + this.webSiteMirror);

        try{
            webSiteConnection = new URL(webSite).openConnection();
            webSiteConnection.setDoOutput(true);
            webSiteConnection.setDoInput(true);
            webSiteConnection.setDefaultUseCaches(false);
            webSiteConnection.setUseCaches(false);
            webSiteConnection.setIfModifiedSince(0);
            webSiteConnection.connect();
        }catch (java.net.MalformedURLException exc) {}
         catch (java.io.IOException exc) {}
    }

    public String getWebSite() {
        return webSite;
    }

    public URLConnection getWebSiteConnection() {
        return webSiteConnection;
    }

    /**
     * Returns the name of the selected file, adding the default microworld
     * file extension, if no extension is specified by the user.
     */
    @Override
    public File getSelectedFile()
    {
      File f = super.getSelectedFile();
      if (f != null) {
        String name = f.getName();
        if (name.indexOf('.') < 0
            && container.mwdFileExtensions != null
            && container.mwdFileExtensions.length > 0) {
          name = name + "." + container.mwdFileExtensions[0];
          String parent = f.getParent();
          f = new File(parent, name);
        }
      }
      return f;
    }

////nikosM new
///this class do the renaming after some millisecs
    class UpdateRenameTask extends TimerTask{
        private JList jList;
        private String newRenamedFileDir;

        protected void setJList(JList jlist){
          jList=jlist;
        }
        protected void setNewRenamedFileDir(String newFile){
           newRenamedFileDir=newFile;
        }

        public void run(){
            String renameFileName = (String) ESlateOptionPane.showInputDialog(WebFileDialog.this,
                            bundle.getString("RenameFileName"),
                            bundle.getString("RenameFile"),
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            ((File)jList.getSelectedValue()).getName());

            if ((renameFileName!=null)) { //&&(container.webServerMicrosHandle.webFileDialog.isShowing())) {
    //           System.out.print("jList.getSelectedValue().toString() ="+jList.getSelectedValue().toString());
                File fileToBeRenamed=new File(jList.getSelectedValue().toString());
                File newFileNewName=new File(newRenamedFileDir+System.getProperty("file.separator")+renameFileName);
                if (!fileToBeRenamed.renameTo(newFileNewName))
                    ESlateOptionPane.showMessageDialog(WebFileDialog.this, bundle.getString("RenameError"), bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                else {
                    // rename file at server too
                    String oldFileFromRoot=WebFileDialog.this.container.webServerMicrosHandle.getWebDirNameFromRoot(fileToBeRenamed.toString(),getWebSiteMirror());
                    String newFileFromRoot=WebFileDialog.this.container.webServerMicrosHandle.getWebDirNameFromRoot(newFileNewName.toString(),getWebSiteMirror());
                    WebFileDialog.this.container.webServerMicrosHandle.renameWebFile(getWebSite(),oldFileFromRoot,newFileFromRoot);
                    rescanCurrentDirectory();
                    File f = (File) jList.getSelectedValue();
                    java.awt.Component tmpComponent=getComponentFromClass((java.awt.Container)WebFileDialog.this,javax.swing.JTextField.class);
                    JTextField textField = (JTextField) tmpComponent;
                    if (!f.isDirectory() && textField != null) {
                        textField.setText(f.getName());
                        setSelectedFile(f);
                    }
                }
            }
            updateRenameTask=new UpdateRenameTask();
            isScheduled=false;
    //       makeNewSchedule=true;
    //       sceduleIsActive=false;
        }
    }
}
////nikosM new end
