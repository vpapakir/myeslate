package gr.cti.eslate.graph2;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author augril
 */
class CheckListManager extends MouseAdapter implements ListSelectionListener { 
	private ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
	private JList list;
//	private int checkBoxWidth = new JCheckBox().getPreferredSize().width;
	private Graph2 graph2;
//	private int labelWidth = new JLabel("X ").getPreferredSize().width;
	private CheckListCellRenderer checkListCellRenderer;
     
    CheckListManager(JList list, Graph2 graph2){
        this.list = list;
        this.graph2 = graph2;
        
        checkListCellRenderer = new CheckListCellRenderer(list, listSelectionModel); 
        list.setCellRenderer(checkListCellRenderer); 
        
        KeyStroke space = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        Action spaceAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				toggleSelection(CheckListManager.this.list.getSelectedIndex()); 
            }
        };
        list.getInputMap().put(space, "SPACE");
        list.getActionMap().put("SPACE", spaceAction);
        
        list.addMouseListener(this); 
        listSelectionModel.addListSelectionListener(this);
    } 
 
    ListSelectionModel getSelectionModel(){ 
        return listSelectionModel; 
    } 
    
    void toggleSelection(int index){ 
        if(index<0) 
            return; 
 
//		String expression = (String) list.getModel().getElementAt(index);
			
       if(listSelectionModel.isSelectedIndex(index)) { 
            listSelectionModel.removeSelectionInterval(index, index);
        	graph2.setFunctionVisible(index, false);
        }
        else { 
            listSelectionModel.addSelectionInterval(index, index);
        	graph2.setFunctionVisible(index, true);
        }
    } 
 
    public void mouseClicked(MouseEvent me){ 
    	if (!SwingUtilities.isLeftMouseButton(me))
    		return;
        int index = list.locationToIndex(me.getPoint()); 
        if(index<0) 
            return; 
        if (!list.getCellBounds(index, index).contains(me.getPoint()))
        	return;
//        if(me.getX()>list.getCellBounds(index, index).x+checkBoxWidth) 
//            return; 
//        toggleSelection(index); 
        if(me.getX()<=list.getCellBounds(index, index).x+checkListCellRenderer.getCheckBox().getPreferredSize().width)
        	toggleSelection(index);
        else if(me.getX()>=list.getCellBounds(index, index).width-checkListCellRenderer.getLabelRemove().getPreferredSize().width)
        	graph2.removeFunction(index);
        else if(me.getX()>=list.getCellBounds(index, index).width-checkListCellRenderer.getLabelRemove().getPreferredSize().width-checkListCellRenderer.getLabelMenu().getPreferredSize().width && me.getX()<list.getCellBounds(index, index).width-checkListCellRenderer.getLabelMenu().getPreferredSize().width)
        	graph2.showPopupMenu(me);
    } 
 
    public void valueChanged(ListSelectionEvent e){ 
        list.repaint(list.getCellBounds(e.getFirstIndex(), e.getLastIndex())); 
    } 
}
