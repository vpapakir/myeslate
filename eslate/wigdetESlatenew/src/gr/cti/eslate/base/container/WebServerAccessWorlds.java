package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.typeArray.BytBaseArray;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WebServerAccessWorlds {
    //The maximum file size that can be read in and transferred
    protected static final int MAX_FILE_LENGTH = 111000000;
    Locale locale;
    ResourceBundle containerBundle,waitBundle;
    ESlateContainer container;
//    WaitDialog pleaseWait;
    protected String webSite = null;
    /* The name of the dir, where mirror files and directories for remote sites
     * are created in order to be accessed through the WebFileDialog.
     */
    String webSitesLocalDirName;
    WebFileDialog webFileDialog = null;
//    WaitDialogTimerTask wtt=null;

    public WebServerAccessWorlds(ESlateContainer container) {
        this.container = container;
        locale = Locale.getDefault();
        containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);
        waitBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.WaitDialogBundle", Locale.getDefault());
        //System.out.println(" Constractor of server access worlds");
    }

    public Hashtable getWebDirectoryListing(String webSite, String webDir) {
//        System.out.println("getWebDirectoryListing --> webSite: " + webSite + ", webDir: " + webDir);
        try{
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
//            System.out.println("con.getIfModifiedSince(): " + con.getIfModifiedSince());
            con.setIfModifiedSince(0);
//            con.connect();

//            ObjectOutputStream req = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
//            req.writeObject("dir|"+System.getProperty("file.separator")+webDir);
//            req.flush();
//            req.close();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
            String action = "dir|" + System.getProperty("file.separator") + webDir;
            byte[] actionBytes = action.getBytes();
            dos.writeInt(actionBytes.length);
            dos.write(actionBytes);
            dos.close();

            ObjectInputStream res = new ObjectInputStream (new BufferedInputStream(con.getInputStream()));
            Object result = res.readObject ();
            res.close();

//            System.out.println("getWebDirectoryListing: " + result);
            if (result == null)
                return null;
            if (Hashtable.class.isInstance(result)) {
                Hashtable contents = (Hashtable) result;
/*                Enumeration enum = contents.keys();
                while (enum.hasMoreElements()) {
                    String key = (String) enum.nextElement();
                    String type = "file";
                    if (!((Boolean) contents.get(key)).booleanValue())
                        type = "directory";
                    System.out.println(type + ": " + key);
                }
*/
                return contents;
            }
            return null;
//            System.out.println("result: " + result.getClass().getName());
        }catch (MalformedURLException exc) {
/*nikos*/
//ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg9") + webSite, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
/*nikos telos*/
            return null;
        }catch (IOException exc) {
            System.out.println("Client i/o exception caught: " + exc.getClass() + ", " + exc.getMessage());
            return null;
        }catch (ClassNotFoundException exc) {
            System.out.println("Client ClassNotFound exception caught");
            return null;
        }
    }

    public int checkServerExistense(String webSite) {
//        System.out.println("checkServerExistense --> webSite: " + webSite);
        try{
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
            con.setIfModifiedSince(0);

//            ObjectOutputStream req = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
//            req.writeObject("dir");
//            req.flush();
//            req.close();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
            String action = "dir";
            byte[] actionBytes = action.getBytes();
            dos.writeInt(actionBytes.length);
            dos.write(actionBytes);
            dos.close();

            ObjectInputStream res = new ObjectInputStream (new BufferedInputStream
                           (con.getInputStream ()));
            Object result = res.readObject ();
            res.close();
        }catch (java.net.MalformedURLException exc) {
            System.out.println("Malformed URL " + webSite);
            return 1;
        }catch (java.net.NoRouteToHostException exc) {
            System.out.println("Host " + webSite + " unavailable");
            return 2;
        }catch (java.net.UnknownHostException exc) {
            System.out.println("Host " + webSite + " unavailable");
            return 2;
        }catch (IOException exc) {
            System.out.println(exc.getClass() + " Unable to connect to " + webSite);
            return 3;
        }catch (ClassNotFoundException exc) {
            System.out.println("ClassNotFoundException: should not occur");
        }
        return 0;
    }

    public boolean createWebDirectory(String webSite, String newWebDir) {
//        System.out.println("createWebDirectory --> webSite: " + webSite + ", newWebDir: " + newWebDir);
        try{
//System.out.println("webSite "+webSite);
//System.out.println("newWebDir "+newWebDir);
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
//            System.out.println("con.getIfModifiedSince(): " + con.getIfModifiedSince());
            con.setIfModifiedSince(0);
//            con.connect();

//            ObjectOutputStream req = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
//            req.writeObject("mkdir|"+System.getProperty("file.separator")+newWebDir);
//            req.flush();
//            req.close();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
            String action = "mkdir|" + System.getProperty("file.separator") + newWebDir;
            byte[] actionBytes = action.getBytes();
            dos.writeInt(actionBytes.length);
            dos.write(actionBytes);
            dos.close();

            ObjectInputStream res = new ObjectInputStream (new BufferedInputStream(con.getInputStream()));
            Object result = res.readObject ();
            res.close();
//            System.out.println("result: " + result);
            if (Boolean.class.isInstance(result))
                return ((Boolean) result).booleanValue();
            else
                return false;

        }catch (MalformedURLException exc) {
/*nikos*/
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg9") + webSite, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
/*nikos allagi*/
            return false;
        }catch (IOException exc) {
            System.out.println("Client i/o exception caught");
            return false;
        }catch (ClassNotFoundException exc) {
            System.out.println("Client ClassNotFound exception caught");
            return false;
        }
    }
/////////////

    public boolean renameWebFile(String webSite, String oldWebFile, String newWebFile) {
//        System.out.println("renameWebFile --> webSite: " + webSite + ", oldWebFile: " + oldWebFile+ ", newWebFile: " + newWebFile);
        try{
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
//            System.out.println("con.getIfModifiedSince(): " + con.getIfModifiedSince());
            con.setIfModifiedSince(0);
//            con.connect();

//            ObjectOutputStream req = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
//            req.writeObject("rn|"+System.getProperty("file.separator")+oldWebFile+"|"+newWebFile);
//            req.flush();
//            req.close();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
            String action = "rn|" + System.getProperty("file.separator") + oldWebFile + "|" + newWebFile;
            byte[] actionBytes = action.getBytes();
            dos.writeInt(actionBytes.length);
            dos.write(actionBytes);
            dos.close();

            ObjectInputStream res = new ObjectInputStream (new BufferedInputStream(con.getInputStream()));
            Object result = res.readObject ();
            res.close();
//            System.out.println("result: " + result);
            if (Boolean.class.isInstance(result))
                return ((Boolean) result).booleanValue();
            else
                return false;

        }catch (MalformedURLException exc) {
/*nikos*/
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg9") + webSite, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
/*nikos allagi*/
            return false;
        }catch (IOException exc) {
            System.out.println("Client i/o exception caught");
            return false;
        }catch (ClassNotFoundException exc) {
            System.out.println("Client ClassNotFound exception caught");
            return false;
        }
    }

/////////////

//nikosM news
    public boolean openMicroWorldFromWebFileDialog() {
                Locale locale = Locale.getDefault();
                ResourceBundle containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);

                WebFileDialog webFileDialog = new WebFileDialog(container, true);//container.webServerMicrosHandle.openWebFileDialog();
                webFileDialog.setDialogTitle(containerBundle.getString("ContainerMsg8"));

                if (webFileDialog != null) {
                    int whichButtonPressed=webFileDialog.showOpenDialog(container.parentComponent);
                    if ((webFileDialog.getSelectedFile() != null)&&(whichButtonPressed!=webFileDialog.CANCEL_OPTION)) {
                        if (!container.createNewMicroworld()) {
                            container.setLoadingMwd(false);
                            return false;
                        }

                        if ((webFileDialog.isRemoteFile())&&(container.webServerMicrosHandle.openRemoteMicroWorld(webFileDialog.getWebSite(),webFileDialog.getWebFile(),true))) {
                                    container.openFileRemote = true;
                                    container.webServerMicrosHandle.webSite = webFileDialog.getWebSite();
                                    container.currentlyOpenMwdFileName = webFileDialog.getWebFile();
                                    if (container.microworld != null && !container.microworld.microworldNameUserDefined)
                                        container.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(container.currentlyOpenMwdFileName, true));
                        }else{
                            if (container.loadLocalMicroworld(webFileDialog.getSelectedFile().toString(), true, true)) {
                                container.openFileRemote = false;
                                container.webServerMicrosHandle.webSite = null;
                                container.currentlyOpenMwdFileName = webFileDialog.getSelectedFile().toString();
                                if (container.microworld != null && !container.microworld.microworldNameUserDefined)
                                    container.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(container.currentlyOpenMwdFileName, true));
                            }else;
                        }
                    }
                }
                container.setLoadingMwd(false);
                return true;
    }

    public void saveMicroWorldAsFromWebFileDialog() {
                Locale locale = Locale.getDefault();
                ResourceBundle containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);
                WebFileDialog webFileDialog = new WebFileDialog(container, false);//container.webServerMicrosHandle.openWebFileDialog();
                webFileDialog.setDialogTitle(containerBundle.getString("ContainerMsg7"));
                int whichButtonPressed=webFileDialog.showSaveDialog(container);
                if ((webFileDialog.getSelectedFile() != null)&&(whichButtonPressed!=webFileDialog.CANCEL_OPTION)) {
                  if (webFileDialog.isRemoteFile()) {
                        String serverFileName;
                        try{
                           serverFileName = webFileDialog.getSelectedFile().getCanonicalPath();
                        }catch (java.io.IOException exc) {
                           serverFileName = webFileDialog.getSelectedFile().toString();
                        }
                        String webFile = container.webServerMicrosHandle.getWebDirNameFromRoot(serverFileName, webFileDialog.getWebSiteMirror());
                        if (!container.webServerMicrosHandle.saveFileToServer(webFileDialog.getWebSite(), webFile))
                            ESlateOptionPane.showMessageDialog(null, containerBundle.getString("ContainerMsg11"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                  }
                  else{
                        if (container.saveAs(webFileDialog.getSelectedFile().toString(), true)) {
                            container.currentlyOpenMwdFileName = webFileDialog.getSelectedFile().toString();
                            if (!container.microworld.microworldNameUserDefined)
                                container.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(container.currentlyOpenMwdFileName, true));
                            container.openFileRemote = false;
                            container.webServerMicrosHandle.webSite = null;
                        }
                  }
               }
    }

//nikosM news end
    public boolean openRemoteMicroWorld(String webSiteName,String webFileName,boolean updateHistory) {
/// first check if the current microworld has changed and ask to user to save it
//        locale = Locale.getDefault();
//        containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);
//Thread.currentThread().dumpStack();
      try {
        if (container.isMicroworldChanged()) {
          String title, msg;
////nikosM currentMicroworld change
          String mwdName=container.microworld.eslateMwd.getName();
////nikosM currentMicroworld change end
          if (mwdName == null || mwdName.equals("null")) {
              title = containerBundle.getString("ContainerMsg20");
              msg = containerBundle.getString("ContainerMsg19");
          }else{
              title = containerBundle.getString("ContainerMsg20");
              msg = containerBundle.getString("ContainerMsg19") + mwdName + "\"";
          }
          Object[] yes_no_cancel = {containerBundle.getString("Yes"), containerBundle.getString("No"), containerBundle.getString("Cancel")};

          JOptionPane pane = new JOptionPane(msg,
              JOptionPane.QUESTION_MESSAGE,
              JOptionPane.YES_NO_CANCEL_OPTION,
              javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
              yes_no_cancel,
              containerBundle.getString("Yes"));
          JFrame contentFrame = new JFrame();
          contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
          javax.swing.JDialog dialog = pane.createDialog(contentFrame, title);
          container.playSystemSound(SoundTheme.QUESTION_SOUND);
          dialog.setVisible(true);
          Object option = pane.getValue();
         // When the JOptionPane closes by pressing the ESC button, the option variable has an Integer value of -1.
          if (option.toString().equals("-1") || option == containerBundle.getString("Cancel") || option == null)
              return true; // CANCEL

//                if ((ESlateOptionPane.showConfirmDialog(new JFrame(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
          if (option == containerBundle.getString("Yes")) {
//              dialog.hide();
              if (!saveFileToServer(webSite, container.currentlyOpenMwdFileName)) {//SAVE
                 return false;
              }
          }
/*        else
            return DONT_SAVE; // NO //do nothing
*/
        }
        container.setMicroworldChanged(false);
        if ((webSiteName==null)&&(webFileName==null)) {
            container.webServerMicrosHandle.openMicroWorldFromWebFileDialog();
            return true;
        }
//        System.out.println("meta to minima gia save");
///////nikosM new
        byte[] fileBytes;
        WaitDialog pleaseWait= new WaitDialog(true, true, true, null, true);
        WaitDialogTimerTask wtt = new WaitDialogTimerTask(container,true,true,pleaseWait);
        if ((fileBytes = openServerFile(webSiteName, webFileName, pleaseWait)) != null) {
            if (container.tmpFile != null) container.containerUtils.deleteFile(container.tmpFile.getPath());
            File tmpServerFile = container.saveByteArray(fileBytes, container.tmpFile); //webFileDialog.getServerFileStream(), tmpFile);
//// nikosM end
            if (tmpServerFile != null) {
//              System.out.println("Wrote file: " + tmpFile + ", exists? " + tmpFile.exists());
//              if (load(tmpFile.getAbsolutePath(), true, true)) {
////nikosM
//// find the webSiteName from the given servlet address
              Enumeration enumeration = container.webSites.keys();
              String address,siteName="";
              while (enumeration.hasMoreElements()) {
                String temp=(String) enumeration.nextElement();
                address = (String) container.webSites.get(temp);
                if ((address!=null)&&(address.equals(webSiteName)))
                  siteName=temp;
              }
                if (container.loadRemoteMicroworld(webFileName+containerBundle.getString("OnServer")+siteName/*container.tmpFile.getAbsolutePath()*/, updateHistory, wtt/*true*/, 3000)) {////nikosM end
                    container.openFileRemote = true;
                    webSite = webSiteName;
////nikosM end
                    container.currentlyOpenMwdFileName = webFileName;
                    if (!container.microworld.microworldNameUserDefined)
                         container.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(container.currentlyOpenMwdFileName, true));
                }
                wtt.disposeDialog();
                return true; //diladi to arxeio irthe kala. An iparxei lathos sto load tha xtipisei i load
//            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg17") + tmpFile.toString() + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//            System.out.println("2. temp file deleted? " + tmpFile.delete());
            }
        }
        wtt.disposeDialog();
        return false;
      }
      catch (IOException e){ return false;}
    }

    public byte[] openServerFile(String webSite, String pathToFile, WaitDialog pleaseWait) throws IOException {
//        System.out.println("openServerFile --> webSite: " + webSite + ", pathToFile: " + pathToFile);
        pleaseWait.setWebProgress(0);
        long  t1 = System.currentTimeMillis();
        Socket theSocket=null;
        try{
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
//            System.out.println("con.getIfModifiedSince(): " + con.getIfModifiedSince());
            con.setIfModifiedSince(0);
//            con.connect();

//            ObjectOutputStream req = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
//            req.writeObject("openFile|"+System.getProperty("file.separator")+pathToFile);
//            req.flush();
//            req.close();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
            String action = "openFile|" + System.getProperty("file.separator") + pathToFile;
            byte[] actionBytes = action.getBytes();
            dos.writeInt(actionBytes.length);
            dos.write(actionBytes);
            dos.close();

            DataInputStream res = new DataInputStream (new BufferedInputStream(con.getInputStream()));
//            int port = ((Integer)res.readObject()).intValue();
            int fileSize = res.readInt();
            long bytesReceived = 0;
            pleaseWait.setMessage(bytesReceived + waitBundle.getString("DownLoadMsg1") + fileSize + waitBundle.getString("DownLoadMsg2"));
            byte[] data = new byte[fileSize];
//            byte b;
            for (int i = 0; i < data.length; i++) {
                data[i] = res.readByte();
                bytesReceived++;
                if (bytesReceived % 100 == 0) {
                    pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*bytesReceived/fileSize-1));
                    pleaseWait.setMessage(bytesReceived + waitBundle.getString("DownLoadMsg1") + fileSize + waitBundle.getString("DownLoadMsg2"));
                }
            }
//            res.readFully(data);

            res.close();

            return data;
/*
            int lookupIndex = 0;
            if (webSite.startsWith("http://"))
                lookupIndex = 7;
            int slashIndex = webSite.indexOf('/', lookupIndex);
            int colonIndex = webSite.indexOf(':', lookupIndex);
            if (slashIndex == -1)
                slashIndex = webSite.length()-1;
            if (colonIndex == -1)
                colonIndex = webSite.length()-1;
            int index = (slashIndex >= colonIndex)? colonIndex:slashIndex;
            String hostname = webSite.substring(lookupIndex, index);
//            String hostname = "localhost";
            byte[] contents=null;
            int fileSize = 0;
            try {
                  theSocket = new Socket (hostname,port);
            }
            catch (java.net.ConnectException e) {
                  pleaseWait.dispose();
                  if (theSocket != null)
                      theSocket.close();
                  System.out.println(e.getMessage());
                  return null;
            }catch (IOException e){
                System.out.println(e.getMessage());
                pleaseWait.dispose();
                if (theSocket != null)
                    theSocket.close();
                return null;
            }

            ObjectInputStream socOis = new ObjectInputStream(theSocket.getInputStream());
//                DataInputStream socOis = new DataInputStream(theSocket.getInputStream());
            fileSize = ((Integer)socOis.readObject()).intValue();
//                ObjectOutputStream socOos = new ObjectOutputStream(theSocket.getOutputStream());
//                socOos.writeInt(1);
            contents = new byte [fileSize];
            int blockSize = 512;
            int bytesReceived=0;
            int numOfCells = pleaseWait.getWebBarCellCount();
            int cellBytes = (int) contents.length / numOfCells;
            int nextUpdateAt = 0 + cellBytes;
            pleaseWait.setMessage(bytesReceived+waitBundle.getString("DownLoadMsg1")+fileSize+waitBundle.getString("DownLoadMsg2"));
            while (true) {
                byte[] tmpContents = (byte[])socOis.readObject();
                for (int i=0;i<tmpContents.length;i++){
                    contents[i+bytesReceived]=tmpContents[i];
                }
                bytesReceived+=tmpContents.length;
                if (bytesReceived > nextUpdateAt) {
                    pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*bytesReceived/fileSize-1));
                    pleaseWait.setMessage(bytesReceived+waitBundle.getString("DownLoadMsg1")+fileSize+waitBundle.getString("DownLoadMsg2"));
                    nextUpdateAt = nextUpdateAt + cellBytes;
                }

                if (bytesReceived>=fileSize) break;
            }

            pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*contents.length/fileSize-1));
//                pleaseWait.setProgress2(pleaseWait.getMaximum2());
            theSocket.close();
            if (contents == null) {
                theSocket.close();
                pleaseWait.dispose();
                ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg12"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return null;
            }
            if (byte[].class.isInstance(contents)) {
                return (byte[]) contents;
            }
            else{
                pleaseWait.dispose();
                ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg12"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return null;
            }
*/
        }catch (UnknownHostException e) {
              pleaseWait.dispose();
              if (theSocket != null)
                  theSocket.close();
              System.out.println(e.getMessage());
              return null;
        }
        catch (IOException e) {
              pleaseWait.dispose();
              if (theSocket != null)
                  theSocket.close();
              System.out.println(e.getMessage());
              return null;
        }
/*
        catch (ClassNotFoundException e) {
              pleaseWait.dispose();
              if (theSocket != null)
                  theSocket.close();
              System.out.println(e.getMessage());
              return null;
        }
*/
        catch (Throwable thr) {
              pleaseWait.dispose();
              if (theSocket != null)
                  theSocket.close();
              System.out.println("message: " + thr.getMessage());
//              thr.printStackTrace();
              return null;
        }

    }


//    protected byte[] readByteStreamFromFile(File file) {
    public byte[] readByteStreamFromFile(File file) {
//        System.out.println("readByteStreamFromFile --> file: " + file + ", size: " + file.length());
        if (!file.exists() || !file.isFile())
            return null;
        if (file.length() > MAX_FILE_LENGTH) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg18"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }

        BytBaseArray contents2 = new BytBaseArray();
        try{
//            System.out.println("Array size: " + contents.length + ", file size: " + file.length());
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            java.io.DataInputStream dis = new java.io.DataInputStream(bis);
            byte[] contents = new byte[fis.available()];
            dis.readFully(contents);
/*            int i=0;
            byte b;
            while (true) {
                try{
                    b = dis.readByte();
                }
                catch (java.io.EOFException exc1) {
                    break;
                }
//                contents[i] = b;
                contents2.add(b);
                i++;
            }
//            System.out.println("size of file :"+i);
*/
//            byte[] contents = new byte[i/*new Long(file.length()).intValue()*/];
//            contents=contents2.toArray();

            try{
                dis.close();
            }catch (IOException exc) {
                System.out.println("IOException while closing file: " + exc.getClass() + ", " + exc.getMessage());
            }

            return contents;
        }catch (FileNotFoundException exc) {
            System.out.println("File " + container.tmpFile + " not found");
            return null;
        }catch (IOException exc) {
            System.out.println("I/O exception: " + exc.getClass() + ", " + exc.getMessage());
            return null;
        }
    }

////nikosM new
    public boolean saveFileToServer(String webSite, String webFile) {
        //Save the current microworld to the tmpFile
//System.out.println("Time0 :"+System.currentTimeMillis());
//          pleaseWait= new WaitDialog(false,true);
//          wtt = new WaitDialogTimerTask(container,false,true,pleaseWait);
        WaitDialog pleaseWait= new WaitDialog(true, false, true, null, true);
        WaitDialogTimerTask wtt = new WaitDialogTimerTask(container,false,true,pleaseWait);
//System.out.println("Time00 :"+System.currentTimeMillis());
        if (!container.saveAs(container.tmpFile.getAbsolutePath(),wtt)) {
            wtt.disposeDialog();
//            thr.stop();
            return false;
        }
//        pleaseWait.set(waitBundle.getString("SaveCompTitle")
        // Read the contents of the tmpFile into a byte array
        byte[] contents = readByteStreamFromFile(container.tmpFile);
        if (contents == null)
            return false;
        container.paintImmediately(container.getVisibleRect());
//System.out.print(webSite+webFile);
//if there is no extension add the "*.mwd"
        if (webFile.indexOf(".")==-1) webFile+=".mwd";
//System.out.println("Time000 :"+System.currentTimeMillis());
        boolean saved = saveByteStreamToServer(
            webSite,
            webFile,
            contents,
            pleaseWait);
//        pleaseWait.dispose();

        if (saved) {
            container.currentlyOpenMwdFileName = webFile;
            if (!container.microworld.microworldNameUserDefined)
                container.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(container.currentlyOpenMwdFileName, true));
            container.openFileRemote = true;
            this.webSite = webSite;
        }
        wtt.disposeDialog();
//        thr.stop();
//        System.out.println("1. temp file deleted? " + container.tmpFile.delete());
        return saved;
    }

    boolean saveByteStreamToServer(String webSite, String pathToFile, byte[] fileData, WaitDialog pleaseWait) {
//        System.out.println("saveByteStreamToServer --> webSite: " + webSite + ", pathToFile: " + pathToFile);
//        WebServerWaitDialog pleaseWait;
//        WaitDialogThread thr = new WaitDialogThread (/*(pleaseWait= new WebServerWaitDialog(false))*/);
        try{
//            thr.start();
//            thr.setPriority(Thread.MIN_PRIORITY);
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
            con.setIfModifiedSince(0);
//System.out.println("Time2 :"+System.currentTimeMillis());
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            String action = "saveFile|" + System.getProperty("file.separator") + pathToFile;
            byte[] actionBytes = action.getBytes();
            dos.writeInt(actionBytes.length);
            dos.write(actionBytes);
            dos.writeInt(fileData.length);
            int bytesSent = 0;
            pleaseWait.setMessage(bytesSent + waitBundle.getString("DownLoadMsg1") + fileData.length + waitBundle.getString("DownLoadMsg2"));
            for (int i = 0; i < fileData.length; i++) {
                dos.writeByte(fileData[i]);
                bytesSent++;
                if (i % 100 == 0) {
                    pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum() * bytesSent / fileData.length-1));
                    pleaseWait.setMessage(bytesSent + waitBundle.getString("DownLoadMsg1") + fileData.length + waitBundle.getString("DownLoadMsg2"));
                    con.getOutputStream().flush();
                }
            }
//            dos.write(fileData);
            dos.close();
//            req.writeObject("saveFile|"+System.getProperty("file.separator")+pathToFile);
//            req.writeObject(contents);
//            req.close();
            ObjectInputStream res = new ObjectInputStream (new BufferedInputStream(con.getInputStream()));
/*
            int[] ports = (int[])res.readObject();
            int port = ports[0];
            int port2 = ports[1];
*/
            res.close();
            pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum() * fileData.length / fileData.length - 1));
            return true;
/*
            int lookupIndex = 0;
            if (webSite.startsWith("http://"))
                lookupIndex = 7;
            int slashIndex = webSite.indexOf('/', lookupIndex);
            int colonIndex = webSite.indexOf(':', lookupIndex);
            if (slashIndex == -1)
                slashIndex = webSite.length()-1;
            if (colonIndex == -1)
                colonIndex = webSite.length()-1;
            int index = (slashIndex >= colonIndex)? colonIndex:slashIndex;
            String hostname = webSite.substring(lookupIndex, index);
            Socket theSocket = null;
            Socket secSocket = null;
//            String hostname = "localhost";
            try {
                  theSocket = new Socket (hostname,port);
                  secSocket = new Socket (hostname,port2);
          }
            catch (java.net.ConnectException e) {
                  pleaseWait.dispose();
                  if (theSocket != null)
                      theSocket.close();
                  System.out.println(e.getMessage());
                  return false;
            }catch (IOException e){
                System.out.println(e.getMessage());
                pleaseWait.dispose();
                if (theSocket != null)
                    theSocket.close();
                return false;
            }

            ObjectOutputStream socOos = new ObjectOutputStream(theSocket.getOutputStream());
//                DataInputStream socOis = new DataInputStream(theSocket.getInputStream());
            socOos.writeObject(new Integer(contents.length));
            int blockSize = 512;
            int bytesSent=0;
            int numOfCells = pleaseWait.getWebBarCellCount();
            int cellBytes = (int) contents.length / numOfCells;
            int nextUpdateAt = 0 + cellBytes;
            if (contents.length<blockSize)
                blockSize=contents.length;
            pleaseWait.setMessage(bytesSent+waitBundle.getString("DownLoadMsg1")+contents.length+waitBundle.getString("DownLoadMsg2"));
            while (true) {
                byte[] tmpContents = new byte[blockSize];
                for (int i=0;i<blockSize;i++){
                    tmpContents[i]=contents[i+bytesSent];
                }
                socOos.writeObject(tmpContents);

                bytesSent+=blockSize;
                if (blockSize<512) break;
                if (bytesSent+blockSize>contents.length)
                    blockSize=contents.length-bytesSent;

                if (bytesSent > nextUpdateAt) {
                    pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*bytesSent/contents.length-1));
                    pleaseWait.setMessage(bytesSent+waitBundle.getString("DownLoadMsg1")+contents.length+waitBundle.getString("DownLoadMsg2"));
                    nextUpdateAt = nextUpdateAt + cellBytes;
                }

                if (bytesSent >= contents.length) break;
            }

            pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*contents.length/contents.length-1));
            socOos.flush();
            socOos.close();
            theSocket.close();

//            String hostname = "localhost";
            boolean verifyData = false;
            try {
                  pleaseWait.setMessage(waitBundle.getString("ClosingConnection"));
                  ObjectInputStream socOis = new ObjectInputStream(secSocket.getInputStream());
                  verifyData = socOis.readBoolean();
                  System.out.println("read verifyData: " + verifyData);
                  secSocket.close();
            }
            catch (java.net.ConnectException e) {
                  pleaseWait.dispose();
                  if (secSocket != null)
                      secSocket.close();
                  System.out.println(e.getMessage());
                  return false;
            }catch (IOException e){
                System.out.println(e.getMessage());
                pleaseWait.dispose();
                if (secSocket != null)
                    secSocket.close();
                return false;
            }

            if (!verifyData) {
                ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg12"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return false;
            }else
                return true;
*/
/*            if (contents == null) {
                theSocket.close();
                pleaseWait.dispose();
                ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg12"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (byte[].class.isInstance(contents)) {
                return (byte[]) contents;
            }
            else{
                pleaseWait.dispose();
                ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg12"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return null;
            }
*/

            // WRITING BLOCKS OF DATA
/*            int blockSize = 512;
            int blockSent = 0;
            req.writeObject(new Integer(contents.length));
            int lastByte = contents.length-1;
            byte[] bytes = new byte[blockSize];
            String downLoad1Msg = waitBundle.getString("DownLoadMsg1");
            String downLoad2Msg = waitBundle.getString("DownLoadMsg2");
            String msg = downLoad1Msg + contents.length + downLoad2Msg;
            int numOfCells = pleaseWait.getWebBarCellCount();
            int cellBytes = (int) contents.length / numOfCells;
            int nextUpdateAt = 0 + cellBytes;
            while (true) {
                int blockEnd = (blockSize * (blockSent+1))-1;
//System.out.println("blockEnd :"+blockEnd);
                if (blockEnd > (lastByte)) {
                    blockSize =  blockEnd - lastByte;
                    blockEnd = lastByte;
                    bytes = new byte[blockSize];
                }
                int blockStart = blockSize*blockSent;
//System.out.println("blockEnd :"+blockEnd);
//System.out.println("lastByte :"+lastByte);
                for (int i= blockStart; i<= blockEnd; i++)
                        bytes[i-blockStart]=contents[i];

                req.write(bytes);
                req.flush();
//                bytes = null;
//System.out.print("bytes :"+bytes+" ");

                if (blockEnd == lastByte) {
                    break;
                }
//System.out.println("blockend != lastbyte :");
                blockSent++;
                if (blockEnd > nextUpdateAt) {
//System.out.println("blockSent :"+blockSent);
                    pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*blockEnd/contents.length-pleaseWait.getWebMaximum()/50));//pleaseWait.getProgress2()+progressBarAddition-contents.length/100);
                    pleaseWait.setMessage(blockEnd+msg); //waitBundle.getString("DownLoadMsg1")+contents.length+waitBundle.getString("DownLoadMsg2"));
                    nextUpdateAt = nextUpdateAt + cellBytes;
                    req.flush();
                }
            }
            pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*contents.length/contents.length-pleaseWait.getWebMaximum()/50));//pleaseWait.getProgress2()+progressBarAddition-contents.length/100);
            pleaseWait.setMessage(waitBundle.getString("ClosingConnection"));
            req.flush ();
            req.close();
            java.io.DataInputStream res = new java.io.DataInputStream (new BufferedInputStream
                           (con.getInputStream ()));
            pleaseWait.setWebProgress(pleaseWait.getWebMaximum());
            boolean result = res.readBoolean ();
//            System.out.println("Save result: " + result);
            res.close();
//            pleaseWait.dispose();
//            thr.stop();
            return result;
*/
        }catch (MalformedURLException exc) {
            pleaseWait.dispose();
//            thr.stop();
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg9") + webSite, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }catch (IOException exc) {
            pleaseWait.dispose();
//            thr.stop();
            System.out.println("Client i/o exception caught " + exc.getClass() + ", " + exc.getMessage());
            exc.printStackTrace();
            return false;
        }catch (Throwable thr) {
            pleaseWait.dispose();
//            thr.stop();
            System.out.println("Client i/o exception caught " + thr.getClass() + ", " + thr.getMessage());
            return false;
        }
    }

//    public boolean uploadByteStreamToServer(String webSite, String pathToFile, byte[] contents, WaitDialog pleaseWait) {
//        System.out.println("saveByteStreamToServer --> webSite: " + webSite + ", pathToFile: " + pathToFile);
//        WebServerWaitDialog pleaseWait;
//        WaitDialogThread thr = new WaitDialogThread (/*(pleaseWait= new WebServerWaitDialog(false))*/);
/*        try{
            URL server = new URL(webSite);
            URLConnection con = server.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
            con.setIfModifiedSince(0);
            pleaseWait= new WaitDialog(false, true, true);
            WaitDialogTimerTask wtt = new WaitDialogTimerTask(container,false,true,pleaseWait);

            java.io.ObjectOutputStream req = new java.io.ObjectOutputStream(new java.io.DataOutputStream(
                          new BufferedOutputStream(con.getOutputStream(), 1024)));
            req.writeObject("uploadFile|"+System.getProperty("file.separator")+pathToFile);
            req.writeObject(new Integer(contents.length));
            int bytesToSent=512;
            byte[] tmpContents = new byte[bytesToSent];
            for (int ii=0;ii<contents.length;) {
                 if (bytesToSent+ii>contents.length) {
                     bytesToSent=contents.length-ii;
                     tmpContents = new byte[bytesToSent];
                 }
                 for (int j=0;j<bytesToSent;j++)
                     tmpContents[j]=contents[j+ii];
                 req.writeObject(tmpContents);
                 pleaseWait.setWebProgress((int)((double)pleaseWait.getWebMaximum()*ii/contents.length-pleaseWait.getWebMaximum()/50));//pleaseWait.getProgress2()+progressBarAddition-contents.length/100);
                 pleaseWait.setMessage(ii+waitBundle.getString("DownLoadMsg1")+contents.length+waitBundle.getString("DownLoadMsg2"));
                 ii+=bytesToSent;
            }
            tmpContents = null;
            contents=null;


*/
/*            for (int i=0; i<contents.length; i++) {
                req.writeObject(new Byte(contents[i]));
                if ((i%512)==0) {
                  pleaseWait.setProgress((int)((double)pleaseWait.getMaximum()*i/contents.length-pleaseWait.getMaximum()/50));//pleaseWait.getProgress2()+progressBarAddition-contents.length/100);
                  pleaseWait.setMessage(i+waitBundle.getString("DownLoadMsg1")+contents.length+waitBundle.getString("DownLoadMsg2"));
                }
            }
*//*            pleaseWait.setMessage(waitBundle.getString("ClosingConnection"));
            req.flush ();
            req.close();

            java.io.ObjectInputStream res = new java.io.ObjectInputStream (new BufferedInputStream
                           (con.getInputStream ()));
            boolean result = res.readBoolean();
            System.out.println("Save result: " + result);
            res.close();
            pleaseWait.dispose();
            return result;

        }catch (MalformedURLException exc) {
            pleaseWait.dispose();
//            thr.stop();
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg9") + webSite, containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }catch (IOException exc) {
            pleaseWait.dispose();
//            thr.stop();
            System.out.println("Client i/o exception caught " + exc.getClass() + ", " + exc.getMessage());
            exc.printStackTrace();
            return false;
        }
    }*/
////nikosM new end

    /* Removes the specified root directory -the directory under "webSitesLocalDirName" and
     * all its contents.
     */
    public void removeWebMirrorRootDirectory(String directoryName) {
        if (directoryName == null || directoryName.trim().length() == 0)
            return;
        File webSiteMirrorRootDir = new File(webSitesLocalDirName, directoryName);
        if (webSiteMirrorRootDir.exists())
            container.containerUtils.removeDirectory(webSiteMirrorRootDir);
    }


    /* Creates the specified web mirror directory. If "webSiteMirrorDirName" is null, then
     * a root web mirror directory is created. If "contents" is not null, then an empty file
     * for each entry in the "contents" Hashtable is created in the new directory.
     */
    public File createWebMirrorDirectory(String directoryName, String webSiteMirrorDirName, Hashtable contents) {
//        System.out.println("createWebMirrorDirectory --> directoryName: " + directoryName + ", webSiteMirrorDirName: " + webSiteMirrorDirName);
        if (webSiteMirrorDirName == null || webSiteMirrorDirName.trim().length() == 0)
            webSiteMirrorDirName = webSitesLocalDirName; //tmpDirName; //System.getProperty("user.home");
        if (directoryName == null)
            return null;

        File webSiteMirrorDir = new File(webSiteMirrorDirName);
        if (!webSiteMirrorDir.exists()) {
            /* Probably we visited the local file system and the went back to the
             * web server file system. This results in the webSiteMirrorDirName
             * being removed. Try to create it again. If this attempt is again
             * unsuccesful, then whole mechanism cannot work, because th i/o media
             * cannot be writen. In this case, abort.
             */
            if (!webSiteMirrorDir.mkdir())
                return null;
        }

        File newDirectory = new File(webSiteMirrorDir, directoryName);
        if (!newDirectory.exists()) {
            if (!newDirectory.mkdir()) {
                System.out.println("Failed to create directory: " + newDirectory);
                return null;
            }
        }else{
          /* When the directory already exists, its contents must be deleted
           * so that the directory will be in sync with the directory on the
           * Web server. The proper contents of the direcory are stored in the
           * following 'while' statement.
           */

          container.containerUtils.removeDirectoryContents(newDirectory);
          System.out.println("Web directory already created");
        }

        if (contents != null) {
            Enumeration enumeration = contents.keys();
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                File newFile = new File(newDirectory, name);
                if (((Boolean) contents.get(name)).booleanValue()) {
                    try{
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), Charset.forName("UTF-8")), 16);
//\                        BufferedWriter bw = new BufferedWriter(new FileWriter(newFile), 16);
                        bw.close();
                    }catch (IOException exc) {
                        System.out.println("Unable to create web file " + newFile);
                    }
                }else{
                    if (!newFile.mkdir()) {
                        System.out.println("Unable to create directory: " + newFile);
                    }
                }
            }
        }
        return newDirectory;
    }

    public String stripWebSiteName(String webSite) {
        int startIndex = webSite.indexOf("//") + 2;
        int endIndex = webSite.indexOf(startIndex, '/');
        if (endIndex == -1)
            endIndex = webSite.length()-1;
        webSite = webSite.substring(startIndex, endIndex);
        if ((endIndex = webSite.indexOf(':')) != -1)
            webSite = webSite.substring(0, endIndex);

        return webSite;
    }

/*    public String dressWebSiteName(String webSite) {
        return "http://" + webSite + ":8080/servlet/FileServer";
    }
*/
    public String getWebDirNameFromRoot(String currentDirName, String webSiteRootDirName) {
//        System.out.println("getWebDirNameFromRoot --> currentDirName: " +currentDirName + ", webSiteRootDirName: " + webSiteRootDirName);
        int index;
        if ((index = currentDirName.indexOf(webSiteRootDirName)) == -1) {
//System.out.println("Returning: " + currentDirName);
            return currentDirName;
        }
        if (currentDirName.equals(webSiteRootDirName)) {//webSiteMirrorRoot
//System.out.println("Returning: " + "");
            return "";
        }else{
//System.out.println("Returning: " + currentDirName.substring(index+webSiteRootDirName.length()+1));
            return  currentDirName.substring(index+webSiteRootDirName.length()+1);
        }
    }

    public WebFileDialog openWebFileDialog(String webSite/*, String webDir*/) {
        if (webSite == null)
            return null;

//        System.out.println("1. Checking site status");
        int siteStatus = checkServerExistense(webSite);
//        System.out.println();
//        System.out.println("2. siteStatus: " + siteStatus);
        if (siteStatus == 1) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg13") + webSite + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (siteStatus == 2) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg14") + webSite + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (siteStatus == 3) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, containerBundle.getString("ContainerMsg15") + webSite + "\"", containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }

//Hashtable dirContents = getWebDirectoryListing(webSite, "");
        Hashtable dirContents = getWebDirectoryListing(webSite,/* webDir*/"");
//        System.out.println("stripWebSiteName(webSite): " + stripWebSiteName(webSite));

        File webMirrorDir;
//        System.out.println();
//        System.out.println("3. webSites: " + container.webSites);
        if (container.webSites != null && container.webSites.countValues(webSite) != 0) {
            // Find the name of the web site and create a local mirror dir with this name
            Enumeration enumeration = container.webSites.keys();
            String dirName = null;
            while (enumeration.hasMoreElements()) {
                Object key = enumeration.nextElement();
                if (container.webSites.get(key).equals(webSite))
                    dirName = (String) key;
            }
///nikosM code
//            dirName = dirName+"("+(String) container.webSiteCommonDirs.get(dirName)+")";
///nikosM end of code
//            System.out.println("dirName: " + dirName);
            webMirrorDir = createWebMirrorDirectory(dirName, null, dirContents);
        }else{
            /* If the web site is not registered, then create a local mirror dir named
             * after the site's address.
             */
            webMirrorDir = createWebMirrorDirectory(stripWebSiteName(webSite), null, dirContents);
        }

            //        System.out.println("webMirrorDir: " + webMirrorDir);
        if (webMirrorDir != null) {
            webFileDialog = new WebFileDialog(container, false);
            webFileDialog.setWebSite(webSite, webMirrorDir);

            webFileDialog.setDialogTitle("Open file");
            webFileDialog.setMultiSelectionEnabled(false);
            webFileDialog.setDialogType(webFileDialog.OPEN_DIALOG);
            webFileDialog.setRenameWebDialogFiles();


        }
        return webFileDialog;
    }


/*    public WebFileDialog createWebFileDialog() {
        String webSite = (String) ESlateOptionPane.showInputDialog(container.parentComponent,
                              containerBundle.getString("LoadRemoteMwdMsg2"),
                              containerBundle.getString("LoadRemoteMwdMsg1"),
                              JOptionPane.QUESTION_MESSAGE,
                              null,
                              null,
                              "http://150.140.100.166:8080/servlet/FileServer");
//        String webDir ="";
        return openWebFileDialog(webSite);
    }
*/
    protected void updateWebSiteMirrorRootDirs() {
        container.containerUtils.removeDirectoryContents(new File(webSitesLocalDirName));
        Enumeration enumeration = container.webSites.keys();
        String webSiteName, address;/*, commonDir;*/
        boolean available;
        while (enumeration.hasMoreElements()) {
            webSiteName = (String) enumeration.nextElement();
            address = (String) container.webSites.get(webSiteName);
            available = ((Boolean) container.webSiteAvailability.get(webSiteName)).booleanValue();
//            commonDir = (String) container.webSiteCommonDirs.get(webSiteName);
            if (available)
                createWebMirrorDirectory(webSiteName/*+'('+commonDir+')'*/, null, null);
        }
    }
}
