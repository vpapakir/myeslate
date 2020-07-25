package gr.cti.eslate.eslateList;


import java.awt.event.*;
import javax.swing.JDialog;
import javax.swing.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import gr.cti.eslate.jeditlist.*;


public class ListDialog extends JDialog {

	private static final long serialVersionUID = -5284423519479002864L;
	ResourceBundle listBundle;
    JEditList list;
    ListVector vector = new ListVector();
    ImageIcon downIcon = new ImageIcon(getClass().getResource("Images/downArrow.gif"));
    ImageIcon upIcon = new ImageIcon(getClass().getResource("Images/upArrow.gif"));
    ImageIcon deleteIcon = new ImageIcon(getClass().getResource("Images/delete.gif"));
    ImageIcon addIcon = new ImageIcon(getClass().getResource("Images/add.gif"));
    JButton downButton, upButton, deleteButton, addButton;
    JScrollPane scrollPane;
    ImageIcon logo = new ImageIcon(getClass().getResource("Images/eslateLogo.gif"));
    boolean changeValues;

    public ListDialog(final ListVector initvector, JFrame frame, boolean bool) {

        super(frame, true);
        super.setSize(290, 230);
        super.setResizable(false);
        listBundle = ResourceBundle.getBundle("gr.cti.eslate.eslateList.ListBundle", Locale.getDefault());
        String title = listBundle.getString("DialogTitle");
        final String s = listBundle.getString("New element");

        setTitle(title);
        if (initvector != null)
            vector = (ListVector) initvector.clone();
        list = new JEditList();

        list.setListData(vector);
        list.setNewItemText(listBundle.getString("New element"));

        scrollPane = new JScrollPane(list);
        Dimension scrollPaneSize = new Dimension(235, 4 * 25 + 50);

        scrollPane.setMaximumSize(scrollPaneSize);
        scrollPane.setPreferredSize(scrollPaneSize);
        scrollPane.setMinimumSize(scrollPaneSize);

        upButton = new JButton(upIcon);
        downButton = new JButton(downIcon);
        addButton = new JButton(addIcon);
        deleteButton = new JButton(deleteIcon);
        if (list.getModel().getSize() == 0)
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
        upButton.setToolTipText(listBundle.getString("UpButtonTip"));
        downButton.setToolTipText(listBundle.getString("DownButtonTip"));
        addButton.setToolTipText(listBundle.getString("AddButtonTip"));
        deleteButton.setToolTipText(listBundle.getString("DeleteButtonTip"));

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
        JButton okButton = new JButton(listBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);

        buttonSize = new Dimension(90, 25);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);

        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(listBundle.getString("Cancel"));

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
                    int index = list.getSelectedIndex();

                    list.setSelectedIndex(index);
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
                    int index = list.getSelectedIndex();

                    if (index == -1) return;
                    if (index != 0) {
                        Object i = vector.get(index);

                        vector.removeElementAt(index);
                        vector.add(index - 1, i);
                        list.setListData(vector);
                        list.setSelectedIndex(index - 1);
                        // list.moveItem(index, index-1);
                    } else
                        upButton.setEnabled(false);
                }
            }
        );
        downButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = list.getSelectedIndex();

                    if (index == -1) return;
                    if (index != list.getModel().getSize() - 1) {
                        Object i = vector.get(index);

                        vector.removeElementAt(index);
                        vector.add(index + 1, i);
                        list.setListData(vector);
                        list.setSelectedIndex(index + 1);

                        //list.moveItem(index, index+1);
                        vector = (ListVector) list.getModel();
                    } else
                        downButton.setEnabled(false);
                }
            }
        );
        addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = list.getSelectedIndex();

                    if (index == -1)
                        vector.addElement(s);
                    else
                        vector.add(index, s);
                    list.setListData(vector);
                    list.setSelectedIndex(index);
                    deleteButton.setEnabled(true);

                }
            }
        );
        deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = list.getSelectedIndex();

                    list.deleteSelectedItems(list);
                    vector.remove(index);
                    if (list.getModel().getSize() == 0)
                        deleteButton.setEnabled(false);

                }
            }
        );

        list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    JEditList list = (JEditList) e.getSource();
                    int selectedIndex = list.getSelectedIndex();

                    if (selectedIndex == list.getModel().getSize() - 1)
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
        list.addJEditListListener(new gr.cti.eslate.jeditlist.event.JEditListAdapter() {
                public void itemEdited(gr.cti.eslate.jeditlist.event.ItemEditedEvent e) {
                    int index = list.getSelectedIndex();

                    vector.set(index, list.getModel().getElementAt(index));
                }

                public void editStarted(java.util.EventObject e) {
                    deleteButton.setEnabled(false);
                }
            }
        );

        list.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int index = list.getSelectedIndex();
                        JFrame frame = new JFrame();

                        frame.setIconImage(logo.getImage());
                        JDialog dialog = new JDialog(frame);
                        //pane.setInitialValue((String) vector.get(index));
                        String input = (String) JOptionPane.showInputDialog(dialog, listBundle.getString("New element"),
                                listBundle.getString("Input"), JOptionPane.QUESTION_MESSAGE, null, null, (String) vector.get(index));

                        if (input instanceof String && input != null && input.length() != 0) {
                            vector.set(index, input);
                            list.setListData(vector);
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

    public ListVector getVector() {
        return vector;
    }

    public boolean getchangedValues() {
        return changeValues;
    }
}

