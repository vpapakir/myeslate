package gr.cti.eslate.tableInspector;

import gr.cti.eslate.navigator.Navigator;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.JFrame;

public class DefaultBrowserFrame extends JFrame implements BrowserFrame {
   private Navigator browser;

   public DefaultBrowserFrame(String title) {
        super(title);
        browser=new Navigator();
        browser.setMenuBarVisible(false);
        browser.setToolBarVisible(false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(browser,BorderLayout.CENTER);
    }

    public void setSize(int w,int h) {
        super.setSize(w,h);
    }

    @SuppressWarnings("deprecation")
	public void show() {
        super.show();
    }


    public void setURL(URL url) {
        try {
			browser.setCurrentLocation(url.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
        getContentPane().doLayout();
    }
}