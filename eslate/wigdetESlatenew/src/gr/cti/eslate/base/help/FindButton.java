package gr.cti.eslate.base.help;

import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.util.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.browser.*;

class FindButton extends JButton
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    protected ResourceBundle info = ResourceBundle.getBundle("gr.cti.eslate.base.help.MessageBundle",ESlateMicroworld.getCurrentLocale());

    protected FindDialog findDialog=null;
    private ESlateBrowser browser;
    private ImageIcon offBulb;
    private ImageIcon onBulb;

    public FindButton(ESlateBrowser passedBrowser) {
        super();
        browser = passedBrowser;

        URL offBulbURL = this.getClass().getResource("images/bulb1.gif");
        offBulb = new ImageIcon(offBulbURL);
        URL onBulbURL = this.getClass().getResource("images/bulb2.gif");
        onBulb = new ImageIcon(onBulbURL);

            setIcon(offBulb);
        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent ev) {
                setIcon(onBulb);
            }
            public void mouseExited(MouseEvent ev) {
                setIcon(offBulb);
            }
            public void mouseClicked(MouseEvent ev) {
                if (findDialog == null)
                    findDialog = new FindDialog(browser);
                else
                    findDialog.setVisible(true);
                    HelpSystemViewer.num=findDialog.i;
                    findDialog.i=0;
            }
        });
    }
}
