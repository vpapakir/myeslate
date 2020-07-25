package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/////nikosM
//// with this class we override the createNewFolder method so as to ask
//// for the name of the folder to be created
class NewDirFileSystemView extends javax.swing.filechooser.FileSystemView{
  ResourceBundle bundle;

  public NewDirFileSystemView(){
    bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.WebFileDialogBundle", Locale.getDefault());
  }

  public File createNewFolder(File containingDir){
    System.out.println("super.createNewFolder() : ");
//    JOptionPane newFolderNamePane = new JOptionPane();
    String newFoldersName=ESlateOptionPane.showInputDialog(null,bundle.getString("NewDirName"),bundle.getString("NewDir"),JOptionPane.PLAIN_MESSAGE);
    if (newFoldersName!=null){
      File file=null;
      try {
        file=getFileSystemView().createNewFolder(containingDir);
        System.out.println("containingDir"+containingDir);
//        System.out.println("file.getCanonicalPath() :"+file.getCanonicalPath());
//        System.out.println("file.getParent() :"+file.getParent());
//        System.out.println("file.getAbsolutePath() :"+file.getAbsolutePath());
//System.out.println("______");
//System.out.println("newFoldersName :"+newFoldersName);
        int myI=file.getPath().lastIndexOf(System.getProperty("file.separator"));
//System.out.println(myI);
//System.out.println("substring :"+file.getPath().substring(0,myI+1));
        String newFoldersCompleteName=file.getPath().substring(0,myI+1)+newFoldersName;
//        String newFoldersCompleteName=+newFoldersName;
        System.out.println("newFoldersCompleteName :"+newFoldersCompleteName);
        if (!file.renameTo(new File(newFoldersCompleteName))){
           System.out.println("rename failed");
           System.out.println("File deleted ?"+file.delete());//<- delete to New Folder pou dimiourgithike
           ESlateOptionPane.showMessageDialog(null,bundle.getString("ErrorNewDirMsg"),bundle.getString("ErrorNewDir"),JOptionPane.ERROR_MESSAGE);
        }
        else {
// if rename returns true create the new dir at the server too
//           container.webServerMicrosHandle.createWebDirectory(webSite, container.webServerMicrosHandle.getWebDirNameFromRoot(newDirName, webSiteMirror));
        }
        return file;
      }
      catch(IOException e){
        System.out.print("Cannot create file :"+e.toString());
        return null;
      }
    }
//    else
    return null;
  }

  public File[] getRoots(){
    return getFileSystemView().getRoots();
  }
  public boolean isHiddenFile(File file){
     return getFileSystemView().isHiddenFile(file);
  }

  public boolean isRoot(File file){
     return getFileSystemView().isRoot(file);
  }

}
