package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FilePropertyEditor extends PropertyEditorSupport{
    ResourceBundle filePropertyEditorBundle;
    JButton button;
    ImageIcon logo = new ImageIcon(FilePropertyEditor.class.getResource("images/eslateLogo.gif"));
    File file = null;
    JPanel panel;
    JFileChooser fileDialog = null;
    JTextField fileNameField;
    PropertyChangeSupport pcs;
    /* The file extensions and their description to be used by the fileDialog */
    String[] extensions = null, descriptions = null;
    /* The selection mode of the fileDialog */
    int selectionMode = JFileChooser.FILES_ONLY;

    public FilePropertyEditor() {
        super();
        filePropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.FilePropertyEditorBundle", Locale.getDefault());
        pcs = new PropertyChangeSupport(this);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
      pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
      pcs.removePropertyChangeListener(listener);
    }

    public void setValue(Object value){
        if( value != null && File.class.isInstance(value)) {
            file = (File) value;
        }else{
        }
    }

    public Object getValue(){
      return file;
    }

    public java.awt.Component getCustomEditor() {

      panel = new JPanel(new BorderLayout(2,0));
      fileNameField = new JTextField();
      if (file != null)
          fileNameField.setText(file.getAbsolutePath());
      ImageIcon ii = new ImageIcon(FilePropertyEditor.class.getResource("images/openFile.gif"));
      button = new JButton(ii);
      Dimension buttonSize = new Dimension(23, 17);
      button.setMaximumSize(buttonSize);
      button.setPreferredSize(buttonSize);
      button.setMinimumSize(buttonSize);
      button.setMargin(new Insets(0, 0, 0, 1));

      panel.add(fileNameField, BorderLayout.CENTER);
      panel.add(button, BorderLayout.EAST);

      fileNameField.addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
              if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                  String fileName = fileNameField.getText();
                  if (fileName == null || fileName.trim().length() == 0) {
                      File oldFile = file;
                      file = null;
                      pcs.firePropertyChange("File", oldFile, file);
                  }else{
                      File oldFile = file;
                      File newFile = new File(fileName);
                      if (newFile.exists()) {
                          file = newFile;
                          pcs.firePropertyChange("File", oldFile, file);
                      }else{
                          ESlateOptionPane.showMessageDialog(panel, filePropertyEditorBundle.getString("Msg1") + newFile + "\" " + filePropertyEditorBundle.getString("Msg2"), filePropertyEditorBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                      }
                  }
              }
          }
      });
      button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              if (fileDialog == null) {
                  fileDialog = new JFileChooser();
                  fileDialog.setMultiSelectionEnabled(false);
                  fileDialog.setFileSelectionMode(selectionMode); //JFileChooser.FILES_AND_DIRECTORIES);
                  fileDialog.setDialogTitle(filePropertyEditorBundle.getString("Title"));
                  fileDialog.setApproveButtonText(filePropertyEditorBundle.getString("Open"));
                  fileDialog.setCurrentDirectory(new File(System.getProperty("user.dir")));
                  // Add the file filters, if any exists
                  if (extensions != null && descriptions != null && selectionMode != JFileChooser.DIRECTORIES_ONLY) {
                      int max = (extensions.length > descriptions.length)? extensions.length:descriptions.length;
                      for (int i=0; i<max; i++) {
                          if (extensions[i] != null && extensions[i].trim().length() != 0 &&
                              descriptions[i] != null && descriptions[i].trim().length() != 0)
                              fileDialog.addChoosableFileFilter(new FFilter(extensions[i], descriptions[i]));
                      }
                  }
              }
              int retVal = fileDialog.showOpenDialog(panel);
              if (retVal == JFileChooser.APPROVE_OPTION) {
                  File selFile = fileDialog.getSelectedFile();
                  if (selFile != null) {
                      if (selFile.exists()) {
                          File oldFile = file;
                          file = selFile;
                          fileNameField.setText(file.getAbsolutePath());
                          pcs.firePropertyChange("File", oldFile, file);
                      }else{
                          ESlateOptionPane.showMessageDialog(panel, filePropertyEditorBundle.getString("Msg1") + selFile + "\" " + filePropertyEditorBundle.getString("Msg2"), filePropertyEditorBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                      }
                  }
              }



/*          JFrame frame = new JFrame();
          Image image=logo.getImage();
          frame.setIconImage(image);
          FileChooserDialog dialog = new FileChooserDialog();
          dialog.show();
*/
        }
      });

      return panel;
    }

    public String getAsText() {
      return null;
    }

    public boolean supportsCustomEditor() {
      return true;
    }

    public void setSelectionMode(int mode) {
        if (mode == JFileChooser.FILES_ONLY ||
            mode == JFileChooser.DIRECTORIES_ONLY ||
            mode == JFileChooser.FILES_AND_DIRECTORIES)
            selectionMode = mode;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public void setDescriptions(String[] descriptions) {
        this.descriptions = descriptions;
    }

}

class FFilter extends javax.swing.filechooser.FileFilter {
    String extension = null, description = null;

    public FFilter(String extension, String description) {
        super();
        this.extension = extension.toLowerCase();
        this.description = description;
    }

    public boolean accept(File f) {
        if (f.isDirectory())
            return true;
        if (f.getPath().toLowerCase().endsWith(extension))
            return true;
        return false;
    }

    public String getDescription() {
        return description;
    }
}
