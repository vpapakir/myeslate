package gr.cti.eslate.webWindow;


import java.awt.*;
import javax.swing.*;


public class StatusPanel extends JPanel
    implements Runnable {
    class ConnectionThread
        implements Runnable {
        boolean keepRunning = true;

        public void run() {
            int idx = 0;

            do
                try {
                    if (m_bShowReadingData) {
                        m_img.setImageIcon(m_images[idx]);
                        m_img.repaint();
                        if (idx == m_images.length - 1)
                            idx = 0;
                        else
                            idx++;
                    } else
                    if (idx != 0) {
                        idx = 0;
                        m_img.setImageIcon(m_images[idx]);
                        m_img.repaint();
                    }
                    Thread.currentThread();
                    Thread.sleep(200L);
                } catch (Exception _ex) {}
            while (keepRunning);
        }

        ConnectionThread() {}
    }

    public StatusPanel(String imgDir) {
        //setBackground(SystemColor.control);
        m_images = new ImageIcon[3];
        m_images[0] = (new ImageIcon(getClass().getResource("Images/1.gif")))/*.getImage()*/;
        m_images[1] = (new ImageIcon(getClass().getResource("Images/2.gif")))/*.getImage()*/;
        m_images[2] = (new ImageIcon(getClass().getResource("Images/3.gif")))/*.getImage()*/;
        m_img = new ImageButton(m_images[0], "");
        m_img.bCanHighlight = false;
        connectionRunnable = new ConnectionThread();
        connectionThread = new Thread(connectionRunnable);
        connectionThread.start();
        memoryLabel = new MyLabel("Memory: ");
        linkLabel = new MyLabel();
        linkPanel = new MainPanel();
        linkPanel.setLayout(new GridBagLayout());
        Helper.addComponent(linkPanel, linkLabel, 17, 2, 1, 1, 0, 0, new Insets(0, 0, 0, 0), 0, 0, 1.0D, 0.0D);
        Helper.addComponent(linkPanel, m_img, 13, 0, 1, 1, 1, 0, new Insets(0, 0, 0, 0), 0, 0, 0.0D, 0.0D);
        m_zoomSlider = new JSlider(1, 200, 100);
        m_zoomSlider.setPreferredSize(new Dimension(140, 14));
        memoryPanel = new MainPanel();
        usedLabel = new MyLabel();
        usedLabel.m_textColor = Color.red;
        totalLabel = new MyLabel();
        totalLabel.m_textColor = Color.blue;
        memoryPanel.setLayout(new GridLayout(1, 5));
        memoryPanel.add(memoryLabel);
        memoryPanel.add(usedLabel);
        memoryPanel.add(totalLabel);
        memoryPanel.add(new MyLabel("Zoom : "));
        memoryPanel.add(m_zoomSlider);
        //        memoryPanel.setLayout(new GridBagLayout());
        //        Helper.addComponent(memoryPanel, memoryLabel, 17, 0, 1, 1, 0, 0, new Insets(0, 0, 0, 0), 0, 0, 0.0D, 0.0D);
        //        Helper.addComponent(memoryPanel, usedLabel, 17, 0, 1, 1, 1, 0, new Insets(0, 0, 0, 0), 0, 0, 0.0D, 0.0D);
        //        Helper.addComponent(memoryPanel, totalLabel, 17, 0, 1, 1, 2, 0, new Insets(0, 0, 0, 0), 0, 0, 0.0D, 0.0D);
        //        Helper.addComponent(memoryPanel, new MyLabel("Zoom:"), 17, 0, 1, 1, 3, 0, new Insets(0, 10, 0, 0), 0, 0, 0.0D, 0.0D);
        //        Helper.addComponent(memoryPanel, m_zoomSlider, 17, 0, 1, 1, 4, 0, new Insets(0, 0, 0, 0), 0, 0, 1.0D, 0.0D);
        setLayout(new GridLayout(1, 1));
        add(linkPanel);
        //add(memoryPanel);

        //        setLayout(new GridBagLayout());
        //        Helper.addComponent(this, linkPanel, 17, 2, 1, 1, 0, 0, new Insets(5, 5, 5, 5), 0, 0, 1.0D, 0.0D);
        //        Helper.addComponent(this, memoryPanel, 17, 2, 1, 1, 0, 1, new Insets(0, 0, 0, 0), 0, 0, 1.0D, 0.0D);
        Thread t = new Thread(this);

        t.start();
    }

    public void paint(Graphics g) {
        Dimension d = getSize();

        g.setColor(SystemColor.control);
        g.fillRect(0, 0, d.width, 5/*d.height*/);
        super.paint(g);
    }

    public void run() {
        do
            try {
                long free = Runtime.getRuntime().freeMemory();
                long total = Runtime.getRuntime().totalMemory();
                long used = total - free;
                String s = " Used: " + used / 1024L + "Kbytes";

                usedLabel.setText(s);
                s = " Total: " + total / 1024L + "KBytes";
                totalLabel.setText(s);
                Thread.currentThread();
                Thread.sleep(500L);
            } catch (Exception _ex) {}
        while (!disposed);
    }

    void setLabel(String s) {
        linkLabel.setText(s);
    }

    void dispose() {
        connectionRunnable.keepRunning = false;
        disposed = true;
        //      m_images = null;
        //      m_img = null;
        //      linkLabel.m_offscreen.flush();
        //      linkLabel = null;
        //      memoryLabel.m_offscreen.flush();
        //      memoryLabel = null;
        //      usedLabel.m_offscreen.flush();
        //      usedLabel = null;
        //      totalLabel.m_offscreen.flush();
        //      totalLabel = null;
        //      totalLabel.m_offscreen.flush();
        //      totalLabel = null;
        linkPanel.removeAll();
        linkPanel = null;
        memoryPanel.removeAll();
        memoryPanel = null;
        m_zoomSlider = null;
        removeAll();
        connectionRunnable = null;
        connectionThread = null;
    }

    ImageIcon m_images[];
    ImageButton m_img;
    MyLabel linkLabel;
    MyLabel memoryLabel;
    MyLabel usedLabel;
    MyLabel totalLabel;
    MainPanel linkPanel;
    MainPanel memoryPanel;
    JSlider m_zoomSlider;
    boolean m_bShowReadingData;
    ConnectionThread connectionRunnable = null;
    Thread connectionThread = null;
    boolean disposed = false;
}
