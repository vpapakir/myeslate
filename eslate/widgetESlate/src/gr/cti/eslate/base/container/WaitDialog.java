package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class WaitDialog extends JWindow {
    JPanel mainPanel;
    private JPanel messagePanel;
    private JLabel progressLb;
    private JProgressBar localBar,webBar;
    private JLabel waitLb, localLabel, webLabel;
    ResourceBundle bundle;
    private boolean twoBars, loading, webOperation;
    boolean displayProgressInfo = true;
    Font titleFont = null;
    Color titleColor = null;

////nikosM new
    public WaitDialog(boolean twoBars, boolean loading, boolean webOperation, String dialogMsg, boolean displayProgressInfo) {
        super(new JFrame());
        this.twoBars = twoBars;
        this.loading = loading;
        this.webOperation = webOperation;
        this.displayProgressInfo = displayProgressInfo;
        //System.out.println("WaitDialog constructor twoBars: " + twoBars + ", loading: " + loading + ", webOperation: " + webOperation);

        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.WaitDialogBundle", Locale.getDefault());
//mainPanel

        if (twoBars) webOperation = true;

        if (webOperation) {
            webBar = createBar(0, 1000000);
        }else{
            localBar = createBar(0, 100);
        }
        if (twoBars) {
            if (webBar == null) {
                webBar = createBar(0, 1000000);
            }else if (localBar == null) {
                localBar = createBar(0, 100);
            }
        }

        waitLb = new JLabel();
        Font f = waitLb.getFont();
        if (titleFont == null)
            waitLb.setFont(f.deriveFont(Font.BOLD));
        else
            waitLb.setFont(titleFont);
        waitLb.setAlignmentX(0.5f);
        if (titleColor == null)
            waitLb.setForeground(new Color(0, 0, 128));
        else
            waitLb.setForeground(titleColor);
        setTitle(dialogMsg);

        if (twoBars) {
            if (loading) {
                webLabel = new JLabel(bundle.getString("DownLoadTitle"));
                localLabel = new JLabel(bundle.getString("Loading"));
            }
            else {
                localLabel = new JLabel(bundle.getString("Saving"));
                webLabel = new JLabel(bundle.getString("DownLoadTitle"));
            }

            Font f1 = localLabel.getFont();
            int w2=getFontMetrics(f1).stringWidth(localLabel.getText());

            Font f2 = webLabel.getFont();
            int w1=getFontMetrics(f2).stringWidth(webLabel.getText());
            int wantedHeight;
            if (w1>w2) wantedHeight=w1; else wantedHeight=w2;
            Dimension dim = new Dimension(wantedHeight,getFontMetrics(f2).getHeight());
            localLabel.setPreferredSize(dim);
            localLabel.setMaximumSize(dim);
            localLabel.setMinimumSize(dim);
            webLabel.setPreferredSize(dim);
            webLabel.setMaximumSize(dim);
            webLabel.setMinimumSize(dim);
        }

        progressLb = new JLabel(" ");

        JPanel borderPanel = new JPanel(true);
        borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));
        borderPanel.setBorder(new CompoundBorder(
                  new EtchedBorder(EtchedBorder.LOWERED),
                  new EmptyBorder(5, 5, 4, 5)));
        if (twoBars) {
            JPanel progressPanel = new JPanel(true);
            JPanel progressPanel2 = new JPanel(true);
            progressPanel2.setLayout(new BoxLayout(progressPanel2,BoxLayout.X_AXIS));
            progressPanel.setLayout(new BoxLayout(progressPanel,BoxLayout.X_AXIS));
            if (loading) {
                progressPanel.add(webLabel);
                progressPanel.add(Box.createHorizontalStrut(2));
                progressPanel.add(webBar);

                progressPanel2.add(localLabel);
                progressPanel2.add(Box.createHorizontalStrut(2));
                progressPanel2.add(localBar);
            }else{
                progressPanel.add(localLabel);
                progressPanel.add(Box.createHorizontalStrut(2));
                progressPanel.add(localBar);

                progressPanel2.add(webLabel);
                progressPanel2.add(Box.createHorizontalStrut(2));
                progressPanel2.add(webBar);
            }
            borderPanel.add(progressPanel); //, BorderLayout.SOUTH);
            borderPanel.add(Box.createVerticalStrut(7));
            borderPanel.add(progressPanel2); //, BorderLayout.SOUTH);
        }else{
            if (webOperation)
                borderPanel.add(webBar);
            else
                borderPanel.add(localBar);
        }

        messagePanel = new JPanel(true);
        messagePanel.setLayout(new BorderLayout(0,3));
        if (displayProgressInfo)
            messagePanel.add(progressLb, BorderLayout.CENTER);
        progressLb.setAlignmentX(0);


        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));// BorderLayout(0, 10));
        mainPanel.setBorder(new CompoundBorder(
                  new BevelBorder(BevelBorder.RAISED),
                  new EmptyBorder(7, 2, 2, 2)));

        mainPanel.add(waitLb);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(borderPanel);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(messagePanel/*, BorderLayout.EAST*/);
        setContentPane(mainPanel);

/*        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));// BorderLayout(0, 10));
        mainPanel.setBorder(new CompoundBorder(
                  new BevelBorder(BevelBorder.RAISED),
                  new EmptyBorder(7, 2, 2, 2)));


        borderPanel = new JPanel(true);
        borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));// BorderLayout(0, 10));
        borderPanel.setBorder(new CompoundBorder(
                  new EtchedBorder(EtchedBorder.LOWERED),
                  new EmptyBorder(5, 5, 4, 5)));

//progressPanel 1

        progressPanel = new JPanel(true);
        progressPanel.setLayout(new BoxLayout(progressPanel,BoxLayout.X_AXIS));
        progressPanel.add(label1);
        progressPanel.add(Box.createHorizontalStrut(2));
        progressPanel.add(progressBar);
//        borderPanel.add(progressPanel); //, BorderLayout.SOUTH);

        progressPanel2 = new JPanel(true);
        progressPanel2.setLayout(new BoxLayout(progressPanel2,BoxLayout.X_AXIS));
        progressPanel2.add(label2);
        progressPanel2.add(Box.createHorizontalStrut(2));
        progressPanel2.add(progressBar2);
//
        if (!webLoading) {
          borderPanel.add(progressPanel); //, BorderLayout.SOUTH);
          borderPanel.add(Box.createVerticalStrut(7));
          borderPanel.add(progressPanel2); //, BorderLayout.SOUTH);
        }
        else {
          borderPanel.add(progressPanel2); //, BorderLayout.SOUTH);
          borderPanel.add(Box.createVerticalStrut(7));
          borderPanel.add(progressPanel); //, BorderLayout.SOUTH);
        }
//messagePanel
        messagePanel = new JPanel(true);
        messagePanel.setLayout(new BorderLayout(0,3));
        messagePanel.add(progressLb, BorderLayout.CENTER);
        progressLb.setAlignmentX(0);
// adding
        Dimension d = new Dimension(368,106);
        mainPanel.setPreferredSize(d);
        mainPanel.setMinimumSize(d);
        mainPanel.setMaximumSize(d);
        mainPanel.add(waitLb);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(borderPanel);
        mainPanel.add(Box.createVerticalStrut(4));
*/
//        mainPanel.add(messagePanel/*, BorderLayout.EAST*/);
//        mainPanel.
//        setContentPane(mainPanel);

    }

//    public WaitDialog(/*java.awt.Frame parentFrame, */boolean loading, boolean webOperation) {
/*        super(new JFrame());
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.WaitDialogBundle", Locale.getDefault());

        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));// BorderLayout(0, 10));
        mainPanel.setBorder(new CompoundBorder(
                  new BevelBorder(BevelBorder.RAISED),
                  new EmptyBorder(7, 2, 2, 2)));

        JLabel waitLb = new JLabel();
        if (!webOperation) {
            if (loading)
                waitLb.setText(bundle.getString("LoadTitle"));
            else
                waitLb.setText(bundle.getString("SaveTitle"));
        }
        else {
            if (loading)
                waitLb.setText(bundle.getString("DownLoadTitle"));
            else
                waitLb.setText(bundle.getString("DownLoadTitle"));
        }
        waitLb.setForeground(new Color(0, 0, 128));
        Font f = waitLb.getFont();
        waitLb.setFont(f.deriveFont(Font.BOLD));
        waitLb.setAlignmentX(0.5f);
        mainPanel.add(waitLb);

        localBar = createBar(0, 100);

        if (!webOperation) {
            if (loading)
                progressLb = new JLabel(bundle.getString("Loading"));
            else
                progressLb = new JLabel(bundle.getString("Saving"));
        }
        else {
            if (loading)
                progressLb = new JLabel(bundle.getString("DownLoadTitle"));
            else
                progressLb = new JLabel(bundle.getString("DownLoadTitle"));
        }
//        progressLb.setBorder(new javax.swing.border.LineBorder(Color.black));
        progressPanel = new JPanel(true);
        progressPanel.setLayout(new BorderLayout(0,3)); //BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.add(progressBar, BorderLayout.NORTH);
        progressPanel.add(progressLb, BorderLayout.CENTER);
        progressLb.setAlignmentX(0);
        progressBar.setAlignmentX(0);
        progressPanel.setBorder(new CompoundBorder(
                  new EtchedBorder(EtchedBorder.LOWERED),
                  new EmptyBorder(5, 5, 4, 5)));
        progressPanel.setAlignmentX(0.5f);

        mainPanel.add(Box.createVerticalStrut(7));
        mainPanel.add(progressPanel); //, BorderLayout.SOUTH);
//        getContentPane().setLayout(new BorderLayout());
//        setSize(200, 80);
//        mainPanel.revalidate();
        setContentPane(mainPanel);
//        add(mainPanel);
//        pack();
    }
*/
/* constructor to show the web progressbar  */
//    public WaitDialog(/*java.awt.Frame parentFrame,*/ boolean webLoading/*, boolean showTwoProgressBars*/) {
/*        super(new JFrame());
//        isLoading = loading;
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.WaitDialogBundle", Locale.getDefault());
//mainPanel
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));// BorderLayout(0, 10));
        mainPanel.setBorder(new CompoundBorder(
                  new BevelBorder(BevelBorder.RAISED),
                  new EmptyBorder(7, 2, 2, 2)));

//texts and messages
        waitLb = new JLabel();
        Font f = waitLb.getFont();
        waitLb.setFont(f.deriveFont(Font.BOLD));
        waitLb.setAlignmentX(0.5f);
        waitLb.setForeground(new Color(0, 0, 128));

        if (webLoading) {
            waitLb.setText(bundle.getString("LoadTitle"));
            label2 = new JLabel(bundle.getString("DownLoadTitle"));
            label1 = new JLabel(bundle.getString("Loading"));
        }
        else {
            waitLb.setText(bundle.getString("SaveTitle"));
            label1 = new JLabel(bundle.getString("Saving"));
            label2 = new JLabel(bundle.getString("DownLoadTitle"));
        }

        progressLb = new JLabel("");
        progressLb.setAlignmentX(0);

// progressBar 1
        localBar = createBar(0, 1000000);
// progressBar 2
        webBar = createBar(0, 1000000);
//borderPanel
        borderPanel = new JPanel(true);
        borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.Y_AXIS));// BorderLayout(0, 10));
        borderPanel.setBorder(new CompoundBorder(
                  new EtchedBorder(EtchedBorder.LOWERED),
                  new EmptyBorder(5, 5, 4, 5)));

//progressPanel 1

        label1.setAlignmentX(0);
        progressBar.setAlignmentX(0);
        progressPanel = new JPanel(true);
        progressPanel.setLayout(new BoxLayout(progressPanel,BoxLayout.X_AXIS));
        progressPanel.add(label1);
        progressPanel.add(Box.createHorizontalStrut(2));
        progressPanel.add(progressBar);
//        borderPanel.add(progressPanel); //, BorderLayout.SOUTH);

        Font f1 = label1.getFont();
        int w2=getFontMetrics(f1).stringWidth(label1.getText());

        Font f2 = label2.getFont();
        int w1=getFontMetrics(f2).stringWidth(label2.getText());
        int wantedHeight;
        if (w1>w2) wantedHeight=w1; else wantedHeight=w2;
        Dimension dim = new Dimension(wantedHeight,getFontMetrics(f2).getHeight());
        label1.setPreferredSize(dim);
        label1.setMaximumSize(dim);
        label1.setMinimumSize(dim);
        label2.setPreferredSize(dim);
        label2.setMaximumSize(dim);
        label2.setMinimumSize(dim);

//progressPanel 2
        label2.setAlignmentX(0);
        progressBar2.setAlignmentX(0);
        progressPanel2 = new JPanel(true);
        progressPanel2.setLayout(new BoxLayout(progressPanel2,BoxLayout.X_AXIS));
        progressPanel2.add(label2);
        progressPanel2.add(Box.createHorizontalStrut(2));
        progressPanel2.add(progressBar2);
//
        if (!webLoading) {
          borderPanel.add(progressPanel); //, BorderLayout.SOUTH);
          borderPanel.add(Box.createVerticalStrut(7));
          borderPanel.add(progressPanel2); //, BorderLayout.SOUTH);
        }
        else {
          borderPanel.add(progressPanel2); //, BorderLayout.SOUTH);
          borderPanel.add(Box.createVerticalStrut(7));
          borderPanel.add(progressPanel); //, BorderLayout.SOUTH);
        }
//messagePanel
        messagePanel = new JPanel(true);
        messagePanel.setLayout(new BorderLayout(0,3));
        messagePanel.add(progressLb, BorderLayout.CENTER);
        progressLb.setAlignmentX(0);
// adding
        Dimension d = new Dimension(368,106);
        mainPanel.setPreferredSize(d);
        mainPanel.setMinimumSize(d);
        mainPanel.setMaximumSize(d);
        mainPanel.add(waitLb);
        mainPanel.add(Box.createVerticalStrut(4));
        mainPanel.add(borderPanel);
        mainPanel.add(Box.createVerticalStrut(4));
*///        mainPanel.add(messagePanel/*, BorderLayout.EAST*/);
//        mainPanel.
//        setContentPane(mainPanel);
//    }
//nikosM end


    void paintImmediately() {
//        System.out.println("mainPanel.getVisibleRect(): " + mainPanel.getVisibleRect());
        mainPanel.paintImmediately(mainPanel.getVisibleRect());
    }

    public void setMessage(String message) {
        if (!displayProgressInfo) return;

        if (message == null) message = "";
        progressLb.setText(message);
        if (isShowing())
            progressLb.paintImmediately(progressLb.getVisibleRect());
    }

    public void setProgressInfoDisplayed(boolean dpi) {
        displayProgressInfo = dpi;
    }

    public boolean isProgressInfoDisplayed() {
        return displayProgressInfo;
    }

    public void setTitle(String title) {
        if (!twoBars) {
            if (!webOperation) {
                if (title == null) {
                    if (loading)
                        waitLb.setText(bundle.getString("LoadTitle"));
                    else
                        waitLb.setText(bundle.getString("SaveTitle"));
                }else{
                    waitLb.setText(title);
                }
            }else{
                waitLb.setText(bundle.getString("DownLoadTitle"));
            }
        }else{
            if (title == null) {
                if (loading)
                    waitLb.setText(bundle.getString("LoadTitle"));
                else
                    waitLb.setText(bundle.getString("SaveTitle"));
            }else{
                waitLb.setText(title);
            }
        }
    }

    public void setTitleFont(Font f) {
        titleFont = f;
        waitLb.setFont(f);
    }

    public void setTitleColor(Color c) {
        titleColor = c;
        waitLb.setForeground(c);
    }

    public void setLocalProgress(int value) {
//        System.out.println("setProgress: " + value);
        localBar.setValue(value);
        if (isShowing())
            localBar.paintImmediately(localBar.getVisibleRect());
    }

    public int getLocalProgress() {
        return localBar.getValue();
    }

    public int getLocalMinimum() {
        return localBar.getMinimum();
    }

    public int getLocalMaximum() {
        return localBar.getMaximum();
    }

//nikosM
    public void setWebProgress(int value) {
        webBar.setValue(value);
        if (isShowing())
            webBar.paintImmediately(webBar.getVisibleRect());
    }

    public int getWebProgress() {
        return webBar.getValue();
    }

    public int getLocalBarCellCount() {
        int cellLength = ((ProgressBarUI) localBar.getUI()).getCellLength();
        int cellSpacing = ((ProgressBarUI) localBar.getUI()).getCellSpacing();
        int size = localBar.getSize().width;
        return (int) size/(cellLength+cellSpacing);
    }

    public int getWebBarCellCount() {
        int cellLength = ((ProgressBarUI) webBar.getUI()).getCellLength();
        int cellSpacing = ((ProgressBarUI) webBar.getUI()).getCellSpacing();
        int size = webBar.getSize().width;
        return (int) size/(cellLength+cellSpacing);
    }

    public int getWebMinimum() {
        return webBar.getMinimum();
    }

    public int getWebMaximum() {
        return webBar.getMaximum();
    }

    public void setWebMaximum(int max) {
        webBar.setMinimum(max);
    }

    public boolean isWebOperation() {
        return webOperation;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isTwoBars() {
        return twoBars;
    }

    JProgressBar createBar(int min, int max) {
        JProgressBar progressBar = new JProgressBar(min, max) {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = 300;
                return d;
            }
        };
        progressBar.setUI(new ProgressBarUI());
        return progressBar;
    }

    public void showDialog(javax.swing.JComponent comp) {
        ESlateContainerUtils.showDialog(this, comp, true);
        comp.paintImmediately(comp.getVisibleRect());
        paintImmediately();
    }

}
//nikosM end

class ProgressBarUI extends BasicProgressBarUI {
    protected int getCellLength() {
        return super.getCellLength();
    }
    protected int getCellSpacing() {
        return super.getCellSpacing();
    }
}

