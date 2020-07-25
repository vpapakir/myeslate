package gr.cti.eslate.agent;

import java.awt.Component;
import java.beans.PropertyEditorSupport;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditorEmbarksOn extends PropertyEditorSupport {
    private JScrollPane scroll;
    private JList list;

    public EditorEmbarksOn() {
        super();
        list=new JList();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        DefaultListModel model=new DefaultListModel();
        Iterator it=Agent.existingTypes.iterator();
        while (it.hasNext())
            model.addElement(it.next());
        list.setModel(model);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String[] s=new String[list.getSelectedValues().length];
                System.arraycopy(list.getSelectedValues(),0,s,0,list.getSelectedValues().length);
                getAValue().setEmbarkOnAgents(s);
            }
        });

        scroll=new JScrollPane();
        scroll.setBorder((new JTextField()).getBorder());
        scroll.setViewportView(list);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String[] s=getAValue().getEmbarkOnAgents();
                int[] idx=new int[s.length];
                for (int i=0;i<s.length;i++)
                    idx[i]=((DefaultListModel) list.getModel()).indexOf(s[i]);
                list.setSelectedIndices(idx);
            }
        });
    }
    /**
     * @return The editor is a listbox in a scroll pane.
     */
    public Component getCustomEditor() {
        return scroll;
    }
    /**
     * The original value cast to Agent.
     */
    private Agent getAValue() {
        return (Agent) super.getValue();
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
