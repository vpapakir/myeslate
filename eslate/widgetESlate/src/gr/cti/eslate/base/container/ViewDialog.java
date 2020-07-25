package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ViewDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    ResourceBundle dialogBundle;
    JPanel mainPanel = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel topPanel = new JPanel();
    JPanel jPanel2 = new JPanel();
    JButton okButton, cancelButton;
    JList viewList;
    JScrollPane scrollPane;
    JPanel buttonPanel = new JPanel();
    JButton newViewButton = new JButton();
    JButton replaceViewButton = new JButton();
    JButton removeViewButton = new JButton();
    JButton activateViewButton = new JButton();
    JButton renameButton = new JButton();
    ESlateContainer container;
    MicroworldViewList views = null;
    DefaultListModel listModel;
    private int returnCode = DIALOG_CANCELLED;

    ViewDialog(java.awt.Frame parentFrame, ESlateContainer container) {
        super(parentFrame, true);

        /* This is an action controlled by a microworld setting. When the setting forbids
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.viewMgmtAllowed, "viewMgmtAllowed");

//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        this.container = container;
        views = (MicroworldViewList) container.mwdViews.clone();
        listModel = new DefaultListModel();
        for (int i=0; i<views.viewList.length; i++)
            listModel.addElement(views.viewList[i].viewName);

        dialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ViewDialogBundle", Locale.getDefault());
        setTitle(dialogBundle.getString("Title"));

        try  {
          jbInit();
//          pack();
        }
        catch(Exception ex) {
          ex.printStackTrace();
        }

        getRootPane().setDefaultButton(okButton);
        ESlateContainerUtils.showDialog(this, container, true);
    }

    void jbInit() throws Exception {
        viewList = new JList();
        viewList.setModel(listModel);
//        viewList.setRequestFocusEnabled(false);
        viewList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
//        viewList.setNewItemText(dialogBundle.getString("View"));

        scrollPane = new JScrollPane(viewList);
        Dimension scrollPaneSize = new Dimension(235, 200);
        scrollPane.setMinimumSize(scrollPaneSize);
        scrollPane.setPreferredSize(scrollPaneSize);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        newViewButton.setText(dialogBundle.getString("New"));
        newViewButton.setToolTipText(dialogBundle.getString("NewTip"));
        replaceViewButton.setText(dialogBundle.getString("Replace"));
        replaceViewButton.setToolTipText(dialogBundle.getString("ReplaceTip"));
        removeViewButton.setText(dialogBundle.getString("Remove"));
        removeViewButton.setToolTipText(dialogBundle.getString("RemoveTip"));
        activateViewButton.setText(dialogBundle.getString("Activate"));
        activateViewButton.setToolTipText(dialogBundle.getString("ActivateTip"));
        renameButton.setText(dialogBundle.getString("Rename"));
        renameButton.setToolTipText(dialogBundle.getString("RenameTip"));

        FontMetrics fm = newViewButton.getFontMetrics(newViewButton.getFont());
        int maxWidth = fm.stringWidth(newViewButton.getText());
        int w = fm.stringWidth(removeViewButton.getText());
        if (maxWidth < w) maxWidth = w;
        w = fm.stringWidth(replaceViewButton.getText());
        if (maxWidth < w) maxWidth = w;
        w = fm.stringWidth(activateViewButton.getText());
        if (maxWidth < w) maxWidth = w;
        w = fm.stringWidth(renameButton.getText());
        if (maxWidth < w) maxWidth = w;

        Dimension buttonDim = new Dimension(maxWidth+10, 25);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        newViewButton.setMargin(zeroInsets);
        replaceViewButton.setMargin(zeroInsets);
        removeViewButton.setMargin(zeroInsets);
        activateViewButton.setMargin(zeroInsets);
        renameButton.setMargin(zeroInsets);

        newViewButton.setMaximumSize(buttonDim);
        newViewButton.setMinimumSize(buttonDim);
        newViewButton.setPreferredSize(buttonDim);
        replaceViewButton.setMaximumSize(buttonDim);
        replaceViewButton.setMinimumSize(buttonDim);
        replaceViewButton.setPreferredSize(buttonDim);
        removeViewButton.setMaximumSize(buttonDim);
        removeViewButton.setMinimumSize(buttonDim);
        removeViewButton.setPreferredSize(buttonDim);
        activateViewButton.setMaximumSize(buttonDim);
        activateViewButton.setMinimumSize(buttonDim);
        activateViewButton.setPreferredSize(buttonDim);
        renameButton.setMaximumSize(buttonDim);
        renameButton.setMinimumSize(buttonDim);
        renameButton.setPreferredSize(buttonDim);

        Color color128 = new Color(0, 0, 128);
        newViewButton.setForeground(color128);
        replaceViewButton.setForeground(color128);
        removeViewButton.setForeground(color128);
        activateViewButton.setForeground(color128);
        renameButton.setForeground(color128);

//        buttonPanel.add(Box.createGlue());
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(newViewButton);
        buttonPanel.add(Box.createVerticalStrut(3));
        buttonPanel.add(replaceViewButton);
        buttonPanel.add(Box.createVerticalStrut(3));
        buttonPanel.add(renameButton);
        buttonPanel.add(Box.createVerticalStrut(3));
        buttonPanel.add(removeViewButton);
        buttonPanel.add(Box.createVerticalStrut(3));
        buttonPanel.add(activateViewButton);
        buttonPanel.add(Box.createGlue());

        topPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        topPanel.setLayout(new BorderLayout(5, 0));
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        okButton = new JButton(dialogBundle.getString("OK"));
        cancelButton = new JButton(dialogBundle.getString("Cancel"));

        okButton.setMargin(zeroInsets);
        okButton.setForeground(color128);
        cancelButton.setMargin(zeroInsets);
        cancelButton.setForeground(color128);

        Dimension buttonDim2 = new Dimension(90, 25);
        okButton.setMaximumSize(buttonDim2);
        okButton.setMinimumSize(buttonDim2);
        okButton.setPreferredSize(buttonDim2);
        cancelButton.setMaximumSize(buttonDim2);
        cancelButton.setMinimumSize(buttonDim2);
        cancelButton.setPreferredSize(buttonDim2);

        JPanel okCancelPanel = new JPanel(true);
        okCancelPanel.setLayout(new BoxLayout(okCancelPanel, BoxLayout.X_AXIS));

        okCancelPanel.add(Box.createGlue());
        okCancelPanel.add(okButton);
        okCancelPanel.add(Box.createHorizontalStrut(10));
        okCancelPanel.add(cancelButton);
        okCancelPanel.add(Box.createGlue());
        okCancelPanel.setBorder(new EmptyBorder(10, 0, 6, 0));

        mainPanel.setLayout(borderLayout1);
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(okCancelPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        viewList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2)
                    renameAction();
            }
        });
        newViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (container.microworld == null || !container.microworld.isViewCreationAllowed())
                    return;
                MicroworldView view = new MicroworldView(container, container.currentView);
                view.createDesktopItemInfos(container, container.microworld.isStoreSkinsPerView());
                views.addView(view);
                listModel.addElement(view.viewName);
                viewList.setModel(listModel);
                viewList.setSelectedIndex(listModel.size()-1);
                viewList.revalidate();
            }
        });
        replaceViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = viewList.getSelectedIndex();
                if (index == -1) return;
                if (container.microworld == null || !container.microworld.isViewCreationAllowed())
                    return;
                String viewName = (String) viewList.getSelectedValue();
                MicroworldView oldView = views.getView(viewName);
                MicroworldView view = new MicroworldView(container, container.currentView);
                view.createDesktopItemInfos(container, container.microworld.isStoreSkinsPerView());
                views.replaceView(oldView, view);
            }
        });
        removeViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = viewList.getSelectedIndex();
                if (index == -1) return;

                int indexToBeSelected = index;
                if ((listModel.size()-1) == index)
                    indexToBeSelected = index-1;

                String viewName = (String) viewList.getSelectedValue();
                listModel.remove(index);
                viewList.setModel(listModel);
                views.removeViewInternal(viewName);
                if (indexToBeSelected == -1) {
                    renameButton.setEnabled(false);
                    replaceViewButton.setEnabled(false);
                    activateViewButton.setEnabled(false);
                    removeViewButton.setEnabled(false);
                }else
                    viewList.setSelectedIndex(indexToBeSelected);

            }
        });
        renameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                renameAction();
            }
        });

        activateViewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = viewList.getSelectedIndex();
                if (index == -1) return;
                container.applyView(views.viewList[index]);
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        viewList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                JList list  = (JList) e.getSource();
                int index = list.getSelectedIndex();
                boolean buttonsEnabled = true;
                if (index == -1)
                    buttonsEnabled = false;

//                if (renameButton.isEnabled() != buttonsEnabled) {
                    renameButton.setEnabled(buttonsEnabled && container.microworld != null && container.microworld.isViewRenameAllowed());
                    replaceViewButton.setEnabled(buttonsEnabled && container.microworld != null && container.microworld.isViewCreationAllowed());
                    activateViewButton.setEnabled(buttonsEnabled && container.microworld != null && container.microworld.isViewActivationAllowed());
                    removeViewButton.setEnabled(buttonsEnabled && container.microworld != null && container.microworld.isViewRemovalAllowed());
//                }
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton.requestFocus();
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Initialization
        if (viewList.getModel().getSize() == 0) {
            renameButton.setEnabled(false);
            replaceViewButton.setEnabled(false);
            activateViewButton.setEnabled(false);
            removeViewButton.setEnabled(false);
        }else{
            viewList.setSelectedIndex(0);
        }
        if (container.microworld != null && container.microworld.isViewCreationAllowed())
            newViewButton.setEnabled(true);
        else{
            newViewButton.setEnabled(false);
            replaceViewButton.setEnabled(false);
        }
    }

    public void renameAction() {
        int index = viewList.getSelectedIndex();
        if (index == -1) return;
        String viewName = (String) viewList.getSelectedValue();
        String newName = (String) ESlateOptionPane.showInputDialog(ViewDialog.this,
                              dialogBundle.getString("Msg1"),
                              dialogBundle.getString("Msg2"),
                              JOptionPane.QUESTION_MESSAGE,
                              null,
                              null,
                              viewName);
        if (newName == null) return;
        MicroworldView v = views.getView(newName);
        if (v != null) {
            ESlateOptionPane.showMessageDialog(this, dialogBundle.getString("Msg3") + " \"" + newName + "\"", dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        listModel.setElementAt(newName, index);
        viewList.setModel(listModel);
        viewList.setSelectedIndex(index);
        views.renameView(viewName, newName);
    }

    public int getReturnCode() {
        return returnCode;
    }

    public MicroworldViewList getViews() {
        return views;
    }
}

