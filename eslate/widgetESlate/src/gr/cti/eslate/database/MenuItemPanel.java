package gr.cti.eslate.database;

import javax.swing.MenuSelectionManager;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.plaf.LabelUI;

import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;


class MenuItemPanel extends JPanel {
    ImageIcon enabledIcon;
    ImageIcon disabledIcon;
    String menuText;
    String sortCut;
    JPanel buttonPanel;
    JButton toolButton;
    JLabel menuLabel;
    JLabel sortCutLabel;
    JMenu menu;
    transient private ActionListener al;
    private static Insets nullInsets = new Insets(0, 0, 0, 0);

    public MenuItemPanel(JMenu menu, String menuText, String sortCut, ImageIcon enabledIcon, ImageIcon disabledIcon) {
        super();

        if (menuText == null)
            throw new NullPointerException("The text for the menu item cannot be null");

        if (menu == null)
            throw new NullPointerException("The menu to which the menu item will be added cannot be null");

        this.menu = menu;
        this.menuText = menuText;
        this.sortCut = sortCut;
        this.enabledIcon = enabledIcon;
        this.disabledIcon = disabledIcon;

        LabelUI labelUI = (LabelUI) MenuLabelUI.createUI(menuLabel);

        menuLabel = new JLabel(menuText);
        menuLabel.setAlignmentY(CENTER_ALIGNMENT);
//        menuLabel.setIcon(enabledIcon);
        menuLabel.setUI(labelUI);

        buttonPanel = new JPanel(true);
        Dimension d = new Dimension(24, 19);
        buttonPanel.setMaximumSize(d);
        buttonPanel.setMinimumSize(d);
        buttonPanel.setPreferredSize(d);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentY(CENTER_ALIGNMENT);

        if (enabledIcon != null) {
            toolButton = new JButton(enabledIcon);
            if (disabledIcon != null)
                toolButton.setDisabledIcon(disabledIcon);

            d = new Dimension(20, 19);
            toolButton.setMaximumSize(d);
            toolButton.setMinimumSize(d);
            toolButton.setPreferredSize(d);

            toolButton.setAlignmentX(CENTER_ALIGNMENT);
            toolButton.setAlignmentY(CENTER_ALIGNMENT);
            toolButton.setMargin(nullInsets);
            toolButton.setBorderPainted(false);
            toolButton.setRequestFocusEnabled(false);

            toolButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    JPanel p = (JPanel) ((JButton) e.getSource()).getParent().getParent();
                    mouseEnteredAction(p);
                }
                public void mouseExited(MouseEvent e) {
                    JPanel p = (JPanel) ((JButton) e.getSource()).getParent().getParent();
                    if (p.contains(e.getPoint()))
                        return;
                    mouseExitedAction(p);
                }
                public void mouseReleased(MouseEvent e) {
                    JPanel p = (JPanel) ((JButton) e.getSource()).getParent().getParent();
                    if (!p.contains(e.getPoint()) && !buttonPanel.contains(e.getPoint()))
                        return;
                    mouseReleasedAction(p);
                }
            });

            buttonPanel.add(Box.createGlue());
            buttonPanel.add(toolButton);
            buttonPanel.add(Box.createGlue());
        }else
            buttonPanel.setOpaque(false);
/*            menuLabel.setIconTextGap(4);
	    if (disabledIcon != null)
            menuLabel.setDisabledIcon(disabledIcon);
*/

        if (sortCut != null) {
            sortCutLabel = new JLabel(sortCut);
            sortCutLabel.setUI(labelUI);
        }

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//        add(Box.createHorizontalStrut(3));
/*        if (enabledIcon == null)
            add(Box.createHorizontalStrut(24));
*/
        add(buttonPanel);
        add(menuLabel);
        add(Box.createGlue());
        if (sortCutLabel != null)
            add(sortCutLabel);
        add(Box.createHorizontalStrut(5));

        enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK);

        menu.add(this);
    }

    protected void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            JPanel p = (JPanel) e.getSource();
            mouseEnteredAction(p);
        }else if (e.getID() == MouseEvent.MOUSE_EXITED) {
            if (buttonPanel.contains(e.getPoint()))
                return;
            JPanel p = (JPanel) e.getSource();
            mouseExitedAction(p);
        }else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            JPanel p = (JPanel) e.getSource();
            if (!p.contains(e.getPoint()) && !buttonPanel.contains(e.getPoint()))
                return;
            mouseReleasedAction(p);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        menuLabel.setEnabled(enabled);
        if (toolButton != null)
            toolButton.setEnabled(enabled);
        if (sortCutLabel != null)
            sortCutLabel.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return super.isEnabled();
    }

    public void addActionListener(ActionListener actionListener) {
        al = actionListener;
    }


    public void setMaximumSize(Dimension dim) {
        super.setMaximumSize(dim);
        buttonPanel.setMaximumSize(new Dimension(24, dim.height));
    }

    public void setMinimumSize(Dimension dim) {
        super.setMinimumSize(dim);
        buttonPanel.setMinimumSize(new Dimension(24, dim.height));
    }

    public void setPreferredSize(Dimension dim) {
        super.setPreferredSize(dim);
        buttonPanel.setPreferredSize(new Dimension(24, dim.height));
    }


    protected void mouseEnteredAction(JPanel p) {
        if (!isEnabled())
            return;
        p.setBackground(new Color(0,0,128));
        menuLabel.setForeground(Color.white);
        if (toolButton != null)
            toolButton.setBorderPainted(true);
        if (sortCutLabel != null)
            sortCutLabel.setForeground(Color.white);

        p.repaint();
//        p.paintImmediately(p.getVisibleRect());
    }


    protected void mouseExitedAction(JPanel p) {
          if (!isEnabled())
              return;
          p.setBackground(Color.lightGray);
          menuLabel.setForeground(Color.black);
          if (toolButton != null)
              toolButton.setBorderPainted(false);
          if (sortCutLabel != null)
              sortCutLabel.setForeground(Color.black);

          p.repaint();
//          p.paintImmediately(p.getVisibleRect());
    }

    protected void mouseReleasedAction(JPanel p) {
          p.setBackground(Color.lightGray);
          menuLabel.setForeground(Color.black);
          if (sortCutLabel != null)
              sortCutLabel.setForeground(Color.black);
          if (toolButton != null)
              toolButton.setBorderPainted(false);
          p.repaint();
//          p.paintImmediately(p.getVisibleRect());

          menu.setPopupMenuVisible(false);

          MenuSelectionManager.defaultManager().clearSelectedPath();

          if (isEnabled() && al != null) {
              al.actionPerformed(new ActionEvent(this, 0, ""));
          }
    }
}

