package gr.cti.eslate.tableInspector;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

class JButtonWithPopup extends gr.cti.eslate.utils.NoBorderButton {
    JPopupMenu menu;
    private int count=0;
    protected ImageIcon all,selected;
    private ButtonGroup bg;

    JButtonWithPopup(ImageIcon all,ImageIcon selected) {
        super(all);
        this.all=all;
        this.selected=selected;
	    menu=new JPopupMenu();
        menu.setLightWeightPopupEnabled(false);
        menu.setBorderPainted(true);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menu.show(getParent(),getX()+getWidth()-1,getY());
            }
        });
        bg=new ButtonGroup();

    }

    protected void addElement(String name) {
        if (count<2) {
            menuItem m;
            menu.add(m=new menuItem(name));
            bg.add(m);
            count++;
        }
    }

    protected void setValueAsynchronously(int i) {
        if ((i==0) || (i==1)) {
            ((JCheckBoxMenuItem) menu.getComponent(i)).setSelected(true);
            //((JCheckBoxMenuItem) menu.getComponent((i+1) % 2)).setSelected(false);

            if (isShowAllSelected()) {
                setDisabledIcon(null);
                setIcon(all);
            } else {
                setDisabledIcon(null);
                setIcon(selected);
            }

        }
    }

    protected void removeAllElements() {
        menu.removeAll();
        count=0;
    }

    protected int getItemCount() {
        return count;
    }

    protected boolean isShowAllSelected() {
        return ((JCheckBoxMenuItem) menu.getComponent(0)).isSelected();
    }

    protected void forwardChange() {
        Object temp=getParent();
        if (temp!=null)
            do {
                temp=((java.awt.Component) temp).getParent();
            } while (!(temp instanceof TableInspector) && (temp!=null));

        if (isShowAllSelected())
            setIcon(all);
        else
            setIcon(selected);
        repaint();

        if (temp!=null)
            ((TableInspector) temp).showStateChanged();
    }

    class menuItem extends JCheckBoxMenuItem {
        menuItem(final String name) {
            super(name);
            setHorizontalTextPosition(4);
            setFont(new Font("Helvetica",Font.PLAIN,11));
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButtonWithPopup.this.forwardChange();
                }
            });
        }
    }
}
