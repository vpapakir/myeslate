package gr.cti.eslate.base.container;

import java.applet.AppletContext;
import java.io.File;

import javax.swing.JApplet;


public class ESlateContainerApplet extends JApplet {
  static ESlateContainerApplet applet = null;
  ESlateContainer container;

  public ESlateContainerApplet() {
      container = new ESlateContainer();
      setContentPane(container);
      applet = this;
  }

  public void start() {
      MenuPanel menuPanel = (container instanceof ESlateComposer)? ((ESlateComposer) container).menuPanel:null;
      if (menuPanel != null)
          menuPanel.microworldExit.setVisible(false);

      String preloadLocalFile = getParameter("preloadLocalFile");
      if (preloadLocalFile != null && preloadLocalFile.trim().length() != 0) {
//          container.paintImmediately(container.getVisibleRect());
          this.setVisible(true);
//            if (container.parentComponent != null && ESlateContainerApplet.class.isAssignableFrom(container.parentComponent.getClass())) {
//                System.out.println("show() 2");
//                ((ESlateContainerApplet) container.parentComponent).show();

//            }


//          System.out.println("1. preloadLocalFile: " + preloadLocalFile);
          // First check if the given file name is relative or absolute
          File preloadFile = new File(preloadLocalFile);
          /* If the file is relative, then form its absolute path name, based on the
           * document base of the html file.
           */
          if (!preloadFile.isAbsolute()) {
//              System.out.println("Relative path file");
              /* If the given file path is not absolute the try to find the file
               * relatively to the document base of the html file. This is valid only
               * if the html file is loaded from the local machine.
               */
              String docBase = getDocumentBase().toString();
              if (docBase.startsWith("file:/")) {
                  //Remove the last part -the name of the html- from docBase
                  int index = docBase.lastIndexOf('/');
                  docBase = docBase.substring(6, index);

                  //Replace all the ocuurences of '/' character with File.separator
                  char firstFileSeparatorChar = File.separator.charAt(0);
                  String restOfFileSeparator = "";
                  if (File.separator.length() > 1)
                      restOfFileSeparator = File.separator.substring(1);
                  StringBuffer docBaseBuf = new StringBuffer(docBase);
                  for (int i=0; i<docBaseBuf.length(); i++) {
                      if (docBaseBuf.charAt(i) == '/')
                          docBaseBuf.setCharAt(i, firstFileSeparatorChar);
                          docBaseBuf.insert(i, restOfFileSeparator);
                  }
                  docBaseBuf.append(File.separator);
                  docBase = docBaseBuf.toString();
                  if (new File(docBase + preloadLocalFile).exists())
                      preloadLocalFile = docBase + preloadLocalFile;
              }
          }
//          System.out.println("preloadLocalFile: " + preloadLocalFile);

          container.loadLocalMicroworld(preloadLocalFile, true, true);
/*          container.createNewMicroworld();
          if (container.load(preloadLocalFile)) {
              container.currentlyOpenMwdFileName = preloadLocalFile;
              container.openFileRemote = false;
              container.webSite = null;
          }else
              ESlateOptionPane.showMessageDialog(this, container.containerBundle.getString("ContainerMsg17") + preloadLocalFile + "\"", container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
*/
          return;
      }

      String preloadRemoteFile = getParameter("preloadRemoteFile");
      String preloadRemoteSite = getParameter("preloadRemoteSite");
      //System.out.println("preloadRemoteFile: " + preloadRemoteFile);
      //System.out.println("preloadRemoteSite: " + preloadRemoteSite);
      if (preloadRemoteFile != null && preloadRemoteFile.trim().length() != 0
          && preloadRemoteSite != null && preloadRemoteSite.trim().length() != 0) {
          try{
//// nikosM
              show();
              container.webServerMicrosHandle.openRemoteMicroWorld(preloadRemoteSite,preloadRemoteFile,true);
/*              WaitDialog pleaseWait = new WaitDialog(true, true, true);
              ESlateContainerUtils.showDialog(pleaseWait, this, true);
//              pleaseWait.showDialog(this);
              byte[] fileBytes = container.webServerMicrosHandle.openServerFile(preloadRemoteSite, preloadRemoteFile, pleaseWait);
//// nikosM end
              if (fileBytes == null)
                  return;
              File tmpServerFile = container.saveByteArray(fileBytes, container.getTmpFile());
              if (tmpServerFile != null) {
                  if (container.createNewMicroworld()) {
                      if (container.load(container.getTmpFile().getAbsolutePath(), false, true)) {
                          container.openFileRemote = true;
//// nikosM
                          container.webServerMicrosHandle.webSite = preloadRemoteSite;
//                          container.webSite = preloadRemoteSite;
//// nikosM end
                          container.currentlyOpenMwdFileName = preloadRemoteFile;
                      }else{
                          container.getTmpFile().delete();
                          throw new Exception();
                      }
                  }
                  container.getTmpFile().delete();
              }
*/
          }catch (Throwable thr) {
              thr.printStackTrace();
              if (container != null)
                  container.closeMicroworld(false);
          }
      }
  }

  public void stop() {
      ((ESlateContainer) getContentPane()).closeMicroworld(true);
  }

  // Trick to provide an AppletContext to other components. Map needs it in order to
  // start an external browser window.
  public static AppletContext getContext() {
      if (applet != null)
          return applet.getAppletContext();
      else
          return null;
  }

}