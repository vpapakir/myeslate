package gr.cti.eslate.eslateComboBox;


import java.awt.event.*;
import javax.swing.JDialog;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import gr.cti.eslate.jeditlist.*;


public class ComboBoxDialog extends JDialog {

	private static final long serialVersionUID = 7454535189036873191L;
	ResourceBundle comboBoxBundle;
    JEditList comboBoxList;
    ComboBoxVector vector = new ComboBoxVector();
    ImageIcon downIcon = new ImageIcon(getClass().getResource("Images/downArrow.gif"));
    ImageIcon upIcon = new ImageIcon(getClass().getResource("Images/upArrow.gif"));
    ImageIcon deleteIcon = new ImageIcon(getClass().getResource("Images/delete.gif"));
    ImageIcon addIcon = new ImageIcon(getClass().getResource("Images/add.gif"));
    JButton downButton, upButton, deleteButton, addButton;
    JScrollPane scrollPane;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    boolean changeValues;

    /**
     * Constructs the dialog to be used for specifying the combobox model's elements
     * @param initvector The combobox vector to use as initial vector
     * @param frame The JFrame that contains the dialog
     * @param bool A boolean specifying whether the dialog is modal or not
     */

    public ComboBoxDialog(final ComboBoxVector initvector, JFrame frame, boolean bool) {

        super(frame, true);
        super.setSize(290, 230);
        super.setResizable(false);
        comboBoxBundle = ResourceBundle.getBundle("gr.cti.eslate.eslateComboBox.ComboBoxBundle", Locale.getDefault());
        String title = comboBoxBundle.getString("DialogTitle");
        final String s = comboBoxBundle.getString("New element");

        setTitle(title);
        if (initvector != null)
            vector = (ComboBoxVector) initvector.clone();
        comboBoxList = new JEditList();

        comboBoxList.setListData(vector);
        comboBoxList.setNewItemText(comboBoxBundle.getString("New element"));

        scrollPane = new JScrollPane(comboBoxList);
        Dimension scrollPaneSize = new Dimension(235, 4 * 25 + 50);

        scrollPane.setMaximumSize(scrollPaneSize);
        scrollPane.setPreferredSize(scrollPaneSize);
        scrollPane.setMinimumSize(scrollPaneSize);

        upButton = new JButton(upIcon);
        downButton = new JButton(downIcon);
        addButton = new JButton(addIcon);
        deleteButton = new JButton(deleteIcon);
        if (comboBoxList.getModel().getSize() == 0)
            deleteButton.setEnabled(false);

        Dimension buttonSize = new Dimension(27, 22);

        upButton.setMaximumSize(buttonSize);
        upButton.setPreferredSize(buttonSize);
        upButton.setMinimumSize(buttonSize);
        downButton.setMaximumSize(buttonSize);
        downButton.setPreferredSize(buttonSize);
        downButton.setMinimumSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        addButton.setMinimumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMinimumSize(buttonSize);
        upButton.setToolTipText(comboBoxBundle.getString("UpButtonTip"));
        downButton.setToolTipText(comboBoxBundle.getString("DownButtonTip"));
        addButton.setToolTipText(comboBoxBundle.getString("AddButtonTip"));
        deleteButton.setToolTipText(comboBoxBundle.getString("DeleteButtonTip"));

        JPanel listButtonPanel = new JPanel();

        listButtonPanel.setLayout(new BoxLayout(listButtonPanel, BoxLayout.Y_AXIS));

        listButtonPanel.add(Box.createGlue());
        listButtonPanel.add(upButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(downButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(addButton);
        listButtonPanel.add(Box.createVerticalStrut(3));
        listButtonPanel.add(deleteButton);
        listButtonPanel.add(Box.createGlue());

        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(scrollPane);
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(listButtonPanel);
        topPanel.add(Box.createGlue());
        topPanel.setBorder(new EmptyBorder(3, 4, 3, 4));

        // The button panel (APPLY, OK, CANCEL)
        JButton okButton = new JButton(comboBoxBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);

        buttonSize = new Dimension(90, 25);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);

        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(comboBoxBundle.getString("Cancel"));

        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // The main panel
        JPanel mainPanel = new JPanel(true);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);
        getContentPane().add(mainPanel);

        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeValues = true;
                    int index = comboBoxList.getSelectedIndex();

                    comboBoxList.setSelectedIndex(index);
                    dispose();
                }
            }
        );
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeValues = false;
                    dispose();
                }
            }
        );
        upButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = comboBoxList.getSelectedIndex();

                    if (index == -1) return;
                    if (index != 0) {
                        Object i = vector.get(index);

                        vector.removeElementAt(index);
                        vector.add(index - 1, i);
                        comboBoxList.setListData(vector);
                        comboBoxList.setSelectedIndex(index - 1);
                        // comboBoxList.moveItem(index, index-1);
                    } else
                        upButton.setEnabled(false);
                }
            }
        );
        downButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = comboBoxList.getSelectedIndex();

                    if (index == -1) return;
                    if (index != comboBoxList.getModel().getSize() - 1) {
                        Object i = vector.get(index);

                        vector.removeElementAt(index);
                        vector.add(index + 1, i);
                        comboBoxList.setListData(vector);
                        comboBoxList.setSelectedIndex(index + 1);

                        //comboBoxList.moveItem(index, index+1);
                        vector = (ComboBoxVector) comboBoxList.getModel();
                        //System.out.println(vector);
                    } else
                        downButton.setEnabled(false);
                }
            }
        );
        addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = comboBoxList.getSelectedIndex();

                    if (index == -1)
                        vector.addElement(s);
                    else
                        vector.add(index, s);
                    comboBoxList.setListData(vector);
                    comboBoxList.setSelectedIndex(index);
                    deleteButton.setEnabled(true);

                }
            }
        );
        deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = comboBoxList.getSelectedIndex();

                    comboBoxList.deleteSelectedItems(comboBoxList);
                    vector.remove(index);
                    if (comboBoxList.getModel().getSize() == 0)
                        deleteButton.setEnabled(false);

                }
            }
        );

        comboBoxList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int selectedIndex = comboBoxList.getSelectedIndex();

                    if (selectedIndex == comboBoxList.getModel().getSize() - 1)
                        downButton.setEnabled(false);
                    else
                        downButton.setEnabled(true);
                    if (selectedIndex == 0)
                        upButton.setEnabled(false);
                    else
                        upButton.setEnabled(true);
                }
            }
        );
        comboBoxList.addJEditListListener(new gr.cti.eslate.jeditlist.event.JEditListAdapter() {
                public void itemEdited(gr.cti.eslate.jeditlist.event.ItemEditedEvent e) {
                    int index = comboBoxList.getSelectedIndex();

                    vector.set(index, comboBoxList.getModel().getElementAt(index));
                }

                public void editStarted(java.util.EventObject e) {
                    deleteButton.setEnabled(false);
                }
            }
        );

        comboBoxList.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int index = comboBoxList.getSelectedIndex();
                        JFrame frame = new JFrame();

                        frame.setIconImage(logo.getImage());
                        JDialog dialog = new JDialog(frame);
                        //pane.setInitialValue((String) vector.get(index));
                        String input = (String) JOptionPane.showInputDialog(dialog, comboBoxBundle.getString("New element"),
                                comboBoxBundle.getString("Input"), JOptionPane.QUESTION_MESSAGE, null, null, (String) vector.get(index));

                        if (input instanceof String && input != null && input.length() != 0) {
                            vector.set(index, input);
                            comboBoxList.setListData(vector);
                        }
                    }
                }
            }
        );

        getRootPane().registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    javax.swing.ButtonModel bm = cancelButton.getModel();

                    bm.setArmed(true);
                    bm.setPressed(true);
                    if (initvector != null)
                        vector = initvector;
                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    javax.swing.ButtonModel bm = cancelButton.getModel();

                    bm.setPressed(false);
                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

    }

    /**
     * Method responsible for showing the dialog window at the right place
     * @param dialog The dialog window to be shown
     * @param centerAroundComp The Component to which the dialog window will be centered around
     * @param pack The boolean to determine if the dialog window is going to be packed or not
     */

    public static void showDialog(Window dialog, Component centerAroundComp, boolean pack) {

        if (pack)

            dialog.pack();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;

        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width / 2) - (dialog.getSize().width / 2);
            y = (screenSize.height / 2) - (dialog.getSize().height / 2);
        } else {
            Rectangle compBounds = centerAroundComp.getBounds();
            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();

            //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
            x = compLocation.x + compBounds.width / 2 - dialog.getSize().width / 2;
            y = compLocation.y + compBounds.height / 2 - dialog.getSize().height / 2;
            if (x + dialog.getSize().width > screenSize.width)
                x = screenSize.width - dialog.getSize().width;
            if (y + dialog.getSize().height > screenSize.height)
                y = screenSize.height - dialog.getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        dialog.setLocation(x, y);
        dialog.setVisible(true);
    }

    /**
     * Returns the comboboxVector
     * @return vector The comboboxVector
     */

    public ComboBoxVector getVector() {
        return vector;
    }

    /**
     * Returns the boolean showing whether the values of the combobox vector have changed
     */

    public boolean getChangedValues() {
        return changeValues;
    }
}

