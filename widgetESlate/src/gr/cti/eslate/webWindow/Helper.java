package gr.cti.eslate.webWindow;


import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;


public class Helper {

    public Helper() {}

    public static void addComponent(Container con, Component comp, int anchor, int fill, int gridheight, int gridwidth, int gridx, int gridy, 
        Insets insets, int ipadx, int ipady, double weightx, double weighty) {
        GridBagLayout gridbag = (GridBagLayout) con.getLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = anchor;
        c.fill = fill;
        c.gridheight = gridheight;
        c.gridwidth = gridwidth;
        c.gridx = gridx;
        c.gridy = gridy;
        c.insets = insets;
        c.ipadx = ipadx;
        c.ipady = ipady;
        c.weightx = weightx;
        c.weighty = weighty;
        gridbag.setConstraints(comp, c);
        con.add(comp);
    }

    public static Image getImage(Component c, String s) {
        Image i = Toolkit.getDefaultToolkit().getImage(s);

        if (i != null)
            return waitForImage(c, i);
        else
            return null;
    }

    public static URL getURL(String src) {
        URL u = null;

        try {
            u = new URL(src);
        } catch (MalformedURLException _ex) {
            System.out.println("Malformed URL ");
        }

        return u;
    }

    public static URL formURL(URL u) {
        ///to be done: this method should chck whether urls have http:// token or not and should add it
        //if its not there
        return null;
    }

    static Image waitForImage(Component c, Image i) {
        MediaTracker tracker = new MediaTracker(c);

        tracker.addImage(i, 0);
        try {
            tracker.waitForID(0);
            if (tracker.isErrorAny())
                return null;
        } catch (InterruptedException _ex) {}
        return i;
    }
}
