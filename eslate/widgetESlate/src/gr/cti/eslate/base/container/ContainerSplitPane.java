package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.MathEF;


public class ContainerSplitPane extends JSplitPane {
    public static int DEFAULT_DIVIDER_LOCATION = 150;
    int previousDividerLocation = DEFAULT_DIVIDER_LOCATION;
    ESlateContainer container;
    LeftPanel leftPanel;
    boolean acceptNewDividerSize = false;

    public ContainerSplitPane(ESlateContainer container) {
        super();
        ((BasicSplitPaneUI) getUI()).getDivider().setBorder(null);
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setDividerLocation(0);
        acceptNewDividerSize = true;
        setDividerSize(0);
        acceptNewDividerSize = false;
        setContinuousLayout(true);
        setRightComponent(container.scrollPane);
        setLeftComponent(null);
        setBorder(null);

        this.container = container;
        leftPanel = new LeftPanel(this);
    }

    protected void processKeyEvent(java.awt.event.KeyEvent e) {
//        System.out.println("ContainerSplitPane Discarding key event");
    }

    public void setDividerSize(int newSize) {
        if (!acceptNewDividerSize) return;
        super.setDividerSize(newSize);
//        ((BasicSplitPaneUI) getUI()).getDivider().validate();
//        ((BasicSplitPaneUI) getUI()).getDivider().doLayout();
//        ((BasicSplitPaneUI) getUI()).getDivider().repaint();
        synchronized(getTreeLock()) {
            validateTree();
        }
        revalidate();
        repaint();
    }

    public void setDividerLocation(int location) {
        if (acceptNewDividerSize) return;
        super.setDividerLocation(location);
    }

    public void setDividerLocation(double location) {
        if (acceptNewDividerSize) return;
        super.setDividerLocation(location);
    }

    public void setContent(String title, java.awt.Component comp) {
        if (comp == null) {
            leftPanel.close();
            return;
        }

        leftPanel.setContent(title, comp);
        /* Ensure that the size of the E-Slate's desktop won't change because of the
         * appearance of the 1eft panel of the splitpane. The size of the desktop changes
         * when the maximum,minimum and preferred sizes of the desktop have not been set.
         * This usually happens for the new microworlds.
         */
        Dimension desktopPrefSize = container.lc.getPreferredSize();
        Dimension desktopSize = container.lc.getSize();
//        System.out.println("desktopPrefSize: " + desktopPrefSize + ", desktopSize: " + desktopSize);
        if (!desktopPrefSize.equals(desktopSize)) {
//            System.out.println("Adjusting desktop's preferred size to be: " + desktopPrefSize);
            container.lc.setPreferredSize(desktopSize);
        }

        setLeftComponent(leftPanel);
        if (getDividerLocation() <= 1) {
            setDividerLocation(previousDividerLocation);
        }
//        System.out.println(javax.swing.UIManager.getDefaults().get("SplitPane.dividerSize").getClass());
        int dsize = ((Integer)javax.swing.UIManager.getDefaults().get("SplitPane.dividerSize")).intValue();
        if (dsize < 5) dsize = 5;
        acceptNewDividerSize = true;
        setDividerSize(dsize);
        acceptNewDividerSize = false;

//System.out.println("3. setDividerSize():" + dsize);
//        ui.setDividerBorderSize(0); //1);
//        container.ensureDesktopContainsAllFrames();
    }

    public Component getContent() {
        return leftPanel.getContent();
    }

    public boolean isLeftPanelClosed() {
//        System.out.println("isLeftPanelClosed: " + (getLeftComponent() == null));
        return (getLeftComponent() == null);
//        return (ui.getDividerBorderSize() == 0);
    }

    public void setBorder(Border b) {
        super.setBorder(null);
    }

    public Border getBorder() {
        return null;
    }

    public void updateUI() {
        super.updateUI();

        ((BasicSplitPaneUI) getUI()).getDivider().setBorder(null);
//        setUI(ui);
        acceptNewDividerSize = true;
        if (leftPanel == null || leftPanel.getContent() == null) {
            setDividerSize(0);
            setDividerLocation(0);
        }else{
            int dsize = ((Integer)javax.swing.UIManager.getDefaults().get("SplitPane.dividerSize")).intValue();
            if (dsize < 5) dsize = 5;
            setDividerSize(dsize);
        }
        acceptNewDividerSize = false;
    }

    public JPanel getTopPanel() {
        if (leftPanel == null) return null;
        return leftPanel.topPanel;
    }
}

class LeftPanel extends JPanel {
    JLabel titleLabel = new JLabel();
    JPanel contentPanel = new JPanel(true);
    JPanel topPanel = null;
    javax.swing.JButton closeButton;
    ContainerSplitPane splitPane;
    private int titleLabelWidth = -1, titleLabelHeight = -1;
    Dimension labelDim = new Dimension();

    public LeftPanel(ContainerSplitPane splitPane) {
        super(true);
        setLayout(new BorderLayout());
        setBorder(new EtchedBorder()); //new CompoundBorder(new EmptyBorder(1,0,0,0), new EtchedBorder()));

        topPanel = new JPanel();
        ExplicitLayout tpl=new ExplicitLayout();
        topPanel.setLayout(tpl);
        topPanel.setBackground(UIManager.getColor("controlShadow"));
        titleLabel.setForeground(UIManager.getColor("textHighlightText"));
        topPanel.add(titleLabel,new ExplicitConstraints(
        		titleLabel,
        		ContainerEF.left(topPanel).add(5),
        		ContainerEF.centerY(topPanel),
        		null,
        		null,
        		0,0.5,true,true
        ));
        closeButton = new javax.swing.JButton(UIManager.getIcon("InternalFrame.closeIcon"));
        closeButton.setUI(new BasicButtonUI());
        closeButton.setRequestFocusEnabled(false);
        closeButton.setMargin(new Insets(0,0,0,0));
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(closeButton.getIcon().getIconWidth(),closeButton.getIcon().getIconHeight()));
        closeButton.setMaximumSize(closeButton.getPreferredSize());
        closeButton.setOpaque(false);
        topPanel.add(closeButton,new ExplicitConstraints(
        		closeButton,
        		ContainerEF.right(topPanel).subtract(5),
        		ContainerEF.centerY(topPanel),
        		null,
        		null,
        		1,0.5,true,true
        ));
        topPanel.setBorder(BorderFactory.createEtchedBorder());
        tpl.setPreferredLayoutSize(ComponentEF.preferredWidth(titleLabel).add(ComponentEF.preferredWidth(closeButton)).add(15),
        		MathEF.max(ComponentEF.height(titleLabel),ComponentEF.height(closeButton)).add(2)
        );

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        contentPanel.setLayout(new BorderLayout());

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        this.splitPane = splitPane;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adjustLabelSize();
            }
        });
    }

    public void updateUI() {
        super.updateUI();
        if (topPanel!=null) {
        	topPanel.setBackground(UIManager.getColor("controlShadow"));
            titleLabel.setForeground(UIManager.getColor("textHighlightText"));
        }
        if (closeButton!=null) {
        	closeButton.setUI(new BasicButtonUI());
        	closeButton.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
        	closeButton.setRequestFocusEnabled(false);
        	closeButton.setMargin(new Insets(0,0,0,0));
        	closeButton.setBorderPainted(false);
        	closeButton.setOpaque(false);
        	closeButton.setPreferredSize(new Dimension(closeButton.getIcon().getIconWidth(),closeButton.getIcon().getIconHeight()));
        	closeButton.setMaximumSize(closeButton.getPreferredSize());
        }
        if (titleLabel != null) {
            FontMetrics fm = titleLabel.getFontMetrics(titleLabel.getFont());
            titleLabelWidth = fm.stringWidth(titleLabel.getText());
            titleLabelHeight = fm.getHeight();
            adjustLabelSize();
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
        titleLabel.revalidate();
        FontMetrics fm = titleLabel.getFontMetrics(titleLabel.getFont());
        titleLabelWidth = fm.stringWidth(titleLabel.getText());
        titleLabelHeight = fm.getHeight();
    }

    protected void adjustLabelSize() {
        int totalWidth = 5 + titleLabelWidth + 16 +2;
        int availableWidth = getSize().width;
        if (availableWidth < totalWidth)
            labelDim.width = titleLabelWidth - (totalWidth-availableWidth);
        else
            labelDim.width = titleLabelWidth;
        labelDim.height = titleLabelHeight; //titleLabel.getPreferredSize().height;
        titleLabel.setPreferredSize(labelDim);
        titleLabel.setMaximumSize(labelDim);
        titleLabel.setMinimumSize(labelDim);
        topPanel.revalidate();
    }

    public void setContent(String title, java.awt.Component comp) {
        if (getContent() != null && Disposable.class.isAssignableFrom(getContent().getClass()))
            ((Disposable) getContent()).disposed();
        contentPanel.removeAll();
        comp.setSize(contentPanel.getSize());
        contentPanel.add(comp, BorderLayout.CENTER);
        setTitle(title);
        contentPanel.revalidate();
        revalidate();
    }

    public Component getContent() {
        if (contentPanel == null || contentPanel.getComponentCount() == 0)
            return null;
        return contentPanel.getComponent(0);
    }

    public void close() {
        if (getContent() != null && Disposable.class.isAssignableFrom(getContent().getClass()))
            ((Disposable) getContent()).disposed();

        contentPanel.removeAll();
        splitPane.previousDividerLocation = splitPane.getDividerLocation();
        splitPane.acceptNewDividerSize = true;
        splitPane.setDividerSize(0);
        splitPane.acceptNewDividerSize = true;
        splitPane.setDividerLocation(0);
//        splitPane.ui.setDividerBorderSize(0);
        splitPane.setLeftComponent(null);
    }
}


class EtchedLineBorder extends EtchedBorder {
    public Insets getBorderInsets(Component c)       {
        return new Insets(2, 0, 0, 0);
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = insets.bottom = 0;
        insets.top = 2;
        return insets;
    }

    public boolean isBorderOpaque() { return true; }

    public void paintBorder(Component c, java.awt.Graphics g, int x, int y, int width, int height) {
        int w = width;
        int h = height;

        g.translate(x, y);
        g.setColor(etchType == LOWERED? getShadowColor(c) : getHighlightColor(c));
        g.drawLine(0, h-2, w-1, h-2);
        g.setColor(etchType == LOWERED? getHighlightColor(c) : getShadowColor(c));
        g.drawLine(0, h-1, w-1, h-1);
        g.translate(-x, -y);
    }

}

