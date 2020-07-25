package gr.cti.eslate.mapViewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JPanel;

class CategoryPane extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
    /**
     * To avoid seeing the parent cursor when dragging.
     */
    boolean isDragging=false;

    CategoryPane(String title) {
        super();
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        setBorder(new CategoryBorder(title));
    }

    /**
     * Known method.
     */
    public void setFont(Font f) {
        super.setFont(f);
        for (int i=0;i<getComponentCount();i++)
            ((Component) getComponents()[i]).setFont(f);
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
    }
} 