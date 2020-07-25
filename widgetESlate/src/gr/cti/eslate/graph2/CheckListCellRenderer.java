package gr.cti.eslate.graph2;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;

/**
 * @author augril
 */
class CheckListCellRenderer extends JPanel implements ListCellRenderer{ 
	private static final long serialVersionUID = 1L;
	private ListCellRenderer listCellRenderer; 
    private ListSelectionModel listSelectionModel; 
    private JCheckBox checkBox;
    private boolean layout = false;
    private JLabel labelRemove, labelMenu;
 
    CheckListCellRenderer(JList list, ListSelectionModel listSelectionModel){ 
        this.listCellRenderer = list.getCellRenderer(); 
        this.listSelectionModel = listSelectionModel;

        setLayout(new ExplicitLayout()); 
        setBorder(BorderFactory.createLineBorder(list.getSelectionBackground()));
        setOpaque(true);
        setPreferredSize(new Dimension(0, 24));
        
        checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        
        labelRemove = new JLabel(new ImageIcon(CheckListCellRenderer.class.getResource("images/clear.gif")));
        labelRemove.setOpaque(false);
        
        labelMenu = new JLabel(new ImageIcon(CheckListCellRenderer.class.getResource("images/menu.gif")));
        labelMenu.setOpaque(false);
    } 
 
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){ 
        Component renderer = listCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
        
//        String expression = (String) list.getModel().getElementAt(index);

        checkBox.setSelected(listSelectionModel.isSelectedIndex(index));
        
        if (!layout) {
        	layout = true;
        	removeAll(); 
            add(checkBox, new ExplicitConstraints(checkBox,
            		ContainerEF.left(this),
            		ContainerEF.centerY(this),
            		null, null,
            		0.0, 0.5, true, true
            ));
            add(labelRemove, new ExplicitConstraints(labelRemove,
            		ContainerEF.right(this),
            		ContainerEF.centerY(this),
            		null, null,
            		1.0, 0.5, true, true
            ));
            add(labelMenu, new ExplicitConstraints(labelMenu,
            		ComponentEF.left(labelRemove),
            		ContainerEF.centerY(this),
            		null, null,
            		1.0, 0.5, true, true
            ));
            add(renderer, new ExplicitConstraints(renderer,
            		ComponentEF.right(checkBox),
            		ContainerEF.centerY(this),
            		ComponentEF.left(labelMenu).subtract(ComponentEF.right(checkBox)), 
            		null,
            		0.0, 0.5, true, true
            ));
//        	add(checkBox, BorderLayout.WEST); 
//        	add(renderer, BorderLayout.CENTER);
//        	add(labelRemove, BorderLayout.EAST);
        }

    	if (isSelected) {
    		setBackground(list.getSelectionBackground());
    		setForeground(list.getSelectionForeground());
    	} else {
    		setBackground(list.getBackground());
    		setForeground(list.getForeground());
    	}
    	
        return this; 
    }

	/**
	 * @return Returns the checkBox.
	 */
	JCheckBox getCheckBox() {
		return checkBox;
	}

	/**
	 * @return Returns the labelRemove.
	 */
	JLabel getLabelRemove() {
		return labelRemove;
	}

	/**
	 * @return the labelMenu
	 */
	JLabel getLabelMenu() {
		return labelMenu;
	}
}