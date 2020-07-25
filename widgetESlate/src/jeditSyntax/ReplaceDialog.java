/*
 * ReplaceDialog.java
 *
 */

package jeditSyntax;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * @author  Kriton Kyrimis
 */
public class ReplaceDialog extends javax.swing.JDialog
{
    /**    
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * Specifies if case sensitive search was selected.
     */
    public boolean isCaseSensitive;
    /**
     * Specifies if backwards search was selected.
     */
    public boolean isBackwards;
    /**
     * Contains the search for which to search. If <code>null</code>, the
     * dialog was canceled.
     */
    public String findText = null;
    /**
     * Contains the text with which to replace the text.
     */
    public String replaceText = "";
    /**
     * Indicates whether to replace all occurrences.
     */
    public boolean replaceAll = false;
    /**
     * Indicates whether plain search was requested.
     */
    public boolean onlyFind = false;
     
    /** Creates new form ReplaceDialog */
    public ReplaceDialog(Component owner)
    {
      super(
        owner instanceof Frame ?
          (Frame)owner :
          (Frame)(SwingUtilities.getAncestorOfClass(Frame.class, owner)),
        ResourceBundle.getBundle("jeditSyntax/FindDialogResources").getString(
          "replace"
        ),
        true
      );
      initComponents();
      tweakComponents();
      center(owner);
    }

    /**
     * Centers the dialog relative to its owner.
     * @param     owner   The owner of the dialog. If it is <null>code</code>,
     *                    the dialog will be placed in the center of the screen.
     */
    private void center(Component owner)
    {
      Dimension s = getSize();
      int width = s.width;
      int height = s.height;
      int x, y;
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int screenWidth = screenSize.width;
      int screenHeight = screenSize.height;
      if (owner == null || !owner.isVisible()) {
        x = (screenWidth - width) / 2;
        y = (screenHeight - height) / 2;
      }else{
        Dimension ownerSize = owner.getSize();
        int ownerWidth = ownerSize.width;
        int ownerHeight = ownerSize.height;
        Point ownerLocation = owner.getLocationOnScreen();
        x = ownerLocation.x + (ownerWidth - width) / 2;
        y = ownerLocation.y + (ownerHeight - height) / 2;
        if ((x + width) > screenWidth) {
          x = screenWidth - width;
        }
        if ((y + height) > screenHeight) {
          y = screenHeight - height;
        }
        if (x < 0) {
          x = 0;
        }
        if (y < 0) {
          y = 0;
        }
      }
      setLocation(x, y);
    }
    
    public void setCaseSensitive(boolean b)
    {
      isCaseSensitive = b;
      caseSensitive.setSelected(b);
    }

    public void setBackwardsSearch(boolean b)
    {
      isBackwards = b;
      backwards.setSelected(b);
    }

    public void setFindText(String t)
    {
      findText = t;
      findField.setText(t);
    }

    public void setReplaceText(String t)
    {
      replaceText = t;
      replaceField.setText(t);
    }

    public void tweakComponents()
    {
      setResizable(false);

      isCaseSensitive = caseSensitive.isSelected();
      isBackwards = backwards.isSelected();

      caseSensitive.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          isCaseSensitive = caseSensitive.isSelected();
        }
      });

      backwards.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          isBackwards = backwards.isSelected();
        }
      });

      ActionListener findListener = new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          onlyFind = true;
          findText = findField.getText();
          if ("".equals(findText)) {
            findText = null;  // Cancel the dialog.
          }
          dispose();
        }
      };

      findButton.addActionListener(findListener);
      findField.addActionListener(findListener);

      ActionListener replaceListener = new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          onlyFind = false;
          replaceAll = false;
          findText = findField.getText();
          if ("".equals(findText)) {
            findText = null;  // Cancel the dialog.
          }else{
            replaceText = replaceField.getText();
          }
          dispose();
        }
      };

      replaceButton.addActionListener(replaceListener);
      replaceField.addActionListener(replaceListener);

      replaceAllButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          onlyFind = false;
          replaceAll = true;
          findText = findField.getText();
          if ("".equals(findText)) {
            findText = null;  // Cancel the dialog.
          }else{
            replaceText = replaceField.getText();
          }
          dispose();
        }
      });

      cancelButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          findText = null;  // Cancel the dialog.
          dispose();
        }
      });

    }


    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        findLabel = new javax.swing.JLabel();
        caseSensitive = new javax.swing.JCheckBox();
        backwards = new javax.swing.JCheckBox();
        findLabel2 = new javax.swing.JLabel();
        findButton = new javax.swing.JButton();
        replaceButton = new javax.swing.JButton();
        replaceAllButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        replaceField = new javax.swing.JTextField();
        findField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        findLabel.setText(java.util.ResourceBundle.getBundle("jeditSyntax/FindDialogResources").getString("find:"));

        caseSensitive.setText(java.util.ResourceBundle.getBundle("jeditSyntax/FindDialogResources").getString("caseSensitive"));
        caseSensitive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        caseSensitive.setMargin(new java.awt.Insets(0, 0, 0, 0));

        backwards.setText(java.util.ResourceBundle.getBundle("jeditSyntax/FindDialogResources").getString("searchBackwards"));
        backwards.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        backwards.setMargin(new java.awt.Insets(0, 0, 0, 0));

        findLabel2.setText(java.util.ResourceBundle.getBundle("jeditSyntax/FindDialogResources").getString("replace:"));

        findButton.setText(java.util.ResourceBundle.getBundle("jeditSyntax.FindDialogResources").getString("find"));

        replaceButton.setText(java.util.ResourceBundle.getBundle("jeditSyntax.FindDialogResources").getString("replace"));

        replaceAllButton.setText(java.util.ResourceBundle.getBundle("jeditSyntax.FindDialogResources").getString("replaceAll"));

        cancelButton.setText(java.util.ResourceBundle.getBundle("jeditSyntax.FindDialogResources").getString("cancel"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(caseSensitive)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(findLabel2)
                            .add(findLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(findField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .add(replaceField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)))
                    .add(backwards))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(cancelButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(findButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(replaceButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, replaceAllButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(findLabel)
                    .add(findButton)
                    .add(findField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(findLabel2)
                    .add(replaceButton)
                    .add(replaceField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(caseSensitive)
                    .add(replaceAllButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cancelButton)
                    .add(backwards))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReplaceDialog(new javax.swing.JFrame()).setVisible(true);
                System.exit(0);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox backwards;
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox caseSensitive;
    private javax.swing.JButton findButton;
    private javax.swing.JTextField findField;
    private javax.swing.JLabel findLabel;
    private javax.swing.JLabel findLabel2;
    private javax.swing.JButton replaceAllButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JTextField replaceField;
    // End of variables declaration//GEN-END:variables
    
}
